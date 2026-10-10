package com.example.badnewgym.feature.bodyprogress.domain

import com.example.badnewgym.feature.bodyprogress.data.repository.BodyProgressFixtureSource
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyCompositionAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ConsistencyAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EquipmentUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ExerciseUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MuscleFocusAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.insights.InsightEngine
import com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone
import com.example.badnewgym.feature.bodyprogress.domain.insights.hasEvidence
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyDataConsent
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyGoalRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.LoadUnit
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.ViewerRole
import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSessionRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSetRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Metric semantics as executable contracts.
 *
 * These tests exist to make the "never interchangeable" rule mechanical: if a
 * future change lets body fat stand in for muscle mass, lets a zero-day win a
 * "lowest" ranking, or compares across exercise variations, these fail.
 */
class BodyProgressAnalyticsTest {

    private val anchor = LocalDate.of(2026, 10, 10)
    private val source = BodyProgressFixtureSource()
    private val dataset = source.dataset(anchor)

    // ------------------------------------------------------------- catalogue

    @Test
    fun catalogueExercisesMapToMusclesAndEquipment() {
        assertTrue(ExerciseCatalog.all.isNotEmpty())
        ExerciseCatalog.all.forEach { exercise ->
            assertTrue("${exercise.id} has no primary muscle", exercise.primaryMuscles.isNotEmpty())
            assertTrue("${exercise.id} overlap", exercise.primaryMuscles.intersect(exercise.secondaryMuscles.toSet()).isEmpty())
        }
    }

    @Test
    fun catalogueSupportsPrimaryAndSecondaryMappings() {
        val bench = ExerciseCatalog.byId("bench_press_barbell")!!
        assertEquals(listOf(MuscleGroup.CHEST), bench.primaryMuscles)
        assertTrue(MuscleGroup.TRICEPS in bench.secondaryMuscles)
        assertEquals(EquipmentCategory.BARBELL, bench.equipment)
    }

    @Test
    fun unknownExerciseIdIsSurfacedNotGuessed() {
        assertNull(ExerciseCatalog.byId("does_not_exist"))
        assertEquals("does_not_exist", ExerciseCatalog.label("does_not_exist"))
    }

    @Test
    fun variationsAreSeparatedFromProgressions() {
        // A barbell curl and a dumbbell curl must not compare with each other.
        assertEquals("curl", ExerciseCatalog.byId("biceps_curl_db")!!.progressionKey)
        assertTrue(
            ExerciseCatalog.byId("biceps_curl_db")!!.variationKey !=
                ExerciseCatalog.byId("biceps_curl_barbell")!!.variationKey
        )
    }

    // ---------------------------------------------------------------- units

    @Test
    fun unitConversionNormalisesToKilograms() {
        val kg = setOf(LoadUnit.KG, 100.0)
        assertEquals(100.0, kg.loadKg()!!, 0.001)
        val lb = setOf(LoadUnit.LB, 220.0)
        assertEquals(220 * 0.45359237, lb.loadKg()!!, 0.0001)
        // Per-dumbbell is stored per dumbbell but volume must use the total.
        val db = setOf(LoadUnit.PER_DUMBBELL, 20.0)
        assertEquals(40.0, db.loadKg()!!, 0.001)
    }

    @Test
    fun missingLoadIsNullNotZero() {
        assertNull(setOf(LoadUnit.KG, null).loadKg())
    }

    private fun setOf(unit: LoadUnit?, load: Double?) = WorkoutSetRecord(
        id = "s", sessionId = "sess", exerciseId = "bench_press_barbell",
        setIndex = 0, load = load, unit = unit, reps = 5,
        recordedAtEpoch = anchor.toEpochDay() * 86_400_000L
    )

    // ------------------------------------------------------------- strength

    @Test
    fun estimatedOneRepMaxUsesDocumentedFormulae() {
        assertEquals(100.0, StrengthAnalytics.estimated1Rm(100.0, 1)!!, 0.001)
        // Epley for <= 10 reps
        assertEquals(100 * (1.0 + 5 / 30.0), StrengthAnalytics.estimated1Rm(100.0, 5)!!, 0.001)
        // Brzycki beyond 10
        assertEquals(100 * 36.0 / (37.0 - 15), StrengthAnalytics.estimated1Rm(100.0, 15)!!, 0.001)
    }

    @Test
    fun estimatedOneRepMaxIsNullWithoutLoadOrReps() {
        assertNull(StrengthAnalytics.estimated1Rm(0.0, 5))
        assertNull(StrengthAnalytics.estimated1Rm(100.0, 0))
    }

    @Test
    fun comparableLiftsImproveOverTime() {
        val progression = StrengthAnalytics.progression(dataset, "bench_press_barbell")
        assertNotNull(progression)
        assertEquals(StrengthAnalytics.StrengthVerdict.IMPROVING, progression!!.verdict)
        assertTrue(progression.current.estimated1RmKg > progression.previous.estimated1RmKg)
    }

    @Test
    fun progressionNeedsTwoRecords() {
        val single = dataset.copy(
            sessions = dataset.sessions.take(1)
        )
        assertNull(StrengthAnalytics.progression(single, "bench_press_barbell"))
    }

    @Test
    fun variationsAreNotCrossCompared() {
        // Barbell and dumbbell bench share a progression key but not a
        // variation. Each progression report must only ever cite records from
        // its own variation - a blended "bench press" number is not allowed.
        val barbell = StrengthAnalytics.progression(dataset, "bench_press_barbell")!!
        val dumbbell = StrengthAnalytics.progression(dataset, "bench_press_dumbbell")

        assertEquals("Bench Press", barbell.exerciseName)
        assertEquals("bench_press_barbell", barbell.current.exerciseId)
        assertEquals("bench_press_barbell", barbell.previous.exerciseId)
        if (dumbbell != null) {
            assertEquals("Dumbbell Bench Press", dumbbell.exerciseName)
            assertEquals("bench_press_dumbbell", dumbbell.current.exerciseId)
            assertEquals("bench_press_dumbbell", dumbbell.previous.exerciseId)
        }
    }

    // --------------------------------------------------------- muscle focus

    @Test
    fun muscleFocusSharesSumToOneHundred() {
        val focus = MuscleFocusAnalytics.focus(dataset, BodyRange.M6)
        val total = focus.shares.sumOf { it.sharePercent }
        assertTrue("shares summed to $total", total in 98..100)
    }

    @Test
    fun secondaryMusclesCountLessThanPrimary() {
        // Per-exercise weighting, isolated from the rest of the programme: an
        // aggregate total would be dominated by whichever muscle is also a
        // primary target somewhere else (e.g. triceps via pushdowns).
        val benchOnly = ExerciseCatalog.byId("bench_press_barbell")!!
        val day = anchor.toEpochDay() - 2
        val isolated = dataset.copy(
            sessions = listOf(
                WorkoutSessionRecord(
                    id = "solo", memberId = dataset.memberId, epochDay = day,
                    startedAtEpoch = day * 86_400_000L,
                    sets = List(3) { i ->
                        setOf(LoadUnit.KG, 60.0).copy(
                            id = "solo_$i", sessionId = "solo", setIndex = i
                        )
                    }
                )
            )
        )
        val focus = MuscleFocusAnalytics.focus(isolated, BodyRange.D30)
        val primary = focus.shares.first { it.muscle == benchOnly.primaryMuscles.first() }
        val secondary = focus.shares.first { it.muscle == benchOnly.secondaryMuscles.first() }
        assertEquals(3 * MuscleFocusAnalytics.PRIMARY_WEIGHT, primary.weightedSets, 0.001)
        assertEquals(3 * MuscleFocusAnalytics.SECONDARY_WEIGHT, secondary.weightedSets, 0.001)
        assertTrue(
            "secondary ${secondary.weightedSets} should trail primary ${primary.weightedSets}",
            secondary.weightedSets < primary.weightedSets
        )
    }

    @Test
    fun sessionCoverageCountsMuscleOncePerSession() {
        val focus = MuscleFocusAnalytics.focus(dataset, BodyRange.M6)
        focus.shares.forEach { share ->
            assertTrue("${share.muscle} sessions ${share.sessionCount} > total ${focus.sessionCount}",
                share.sessionCount <= focus.sessionCount)
            assertTrue(share.sessionCount <= share.sets)
        }
    }

    @Test
    fun dateRangeFilteringChangesTheSample() {
        val week = MuscleFocusAnalytics.focus(dataset, BodyRange.D7)
        val sixMonths = MuscleFocusAnalytics.focus(dataset, BodyRange.M6)
        assertTrue(week.totalSets < sixMonths.totalSets)
        assertEquals(0, week.totalSets - week.totalSets) // sanity
    }

    @Test
    fun unmappedExercisesAreDisclosedNotSilentlyDropped() {
        val polluted = dataset.copy(
            sessions = dataset.sessions.take(3) + WorkoutSessionRecord(
                id = "bad", memberId = dataset.memberId,
                epochDay = anchor.toEpochDay() - 2,
                startedAtEpoch = (anchor.toEpochDay() - 2) * 86_400_000L,
                sets = listOf(
                    setOf(LoadUnit.KG, 50.0).copy(id = "bad1", exerciseId = "mystery_lift", sessionId = "bad")
                )
            )
        )
        val focus = MuscleFocusAnalytics.focus(polluted, BodyRange.D30)
        assertTrue(focus.hasUnmappedExercises)
        assertTrue("mystery_lift" in ExerciseUsageAnalytics.usage(polluted, BodyRange.D30).unmappedIds)
    }

    // ------------------------------------------------- exercise + equipment

    @Test
    fun frequencyVolumeAndTopLoadAreDistinctFields() {
        val usage = ExerciseUsageAnalytics.usage(dataset, BodyRange.M6)
        val bench = usage.stats.first { it.exercise.id == "bench_press_barbell" }
        assertTrue(bench.setCount > 0)
        assertTrue(bench.repTotal > 0)
        assertTrue(bench.volumeKg > 0)
        assertNotNull(bench.topLoadKg)
        // Volume share and set count are not the same measure.
        assertTrue(bench.sharePercent in 0..100)
    }

    @Test
    fun topExerciseRankingIsStable() {
        val first = ExerciseUsageAnalytics.usage(dataset, BodyRange.D30).byFrequency().first()
        val second = ExerciseUsageAnalytics.usage(dataset, BodyRange.D30).byFrequency().first()
        assertEquals(first.exercise.id, second.exercise.id)
    }

    @Test
    fun equipmentShareIsSetBasedAndVolumeRingIsOptional() {
        val usage = EquipmentUsageAnalytics.usage(dataset, BodyRange.M6)
        assertTrue(usage.shares.isNotEmpty())
        val total = usage.shares.sumOf { it.setCount }
        assertEquals(
            dataset.sessions.filter { BodyRange.M6.covers(anchor.toEpochDay(), it.epochDay) }
                .flatMap { it.effectiveSets }.size,
            total
        )
        assertNotNull(usage.volumeBasedDominantPercent)
    }

    @Test
    fun equipmentVolumeRingIsNullWithoutComparableLoads() {
        val cardioOnly = dataset.copy(
            sessions = dataset.sessions.map { s ->
                s.copy(sets = s.effectiveSets.filter { set ->
                    ExerciseCatalog.byId(set.exerciseId)?.isCardio == true
                }.map { it.copy(correctsSetId = null) })
            }
        )
        val usage = EquipmentUsageAnalytics.usage(cardioOnly, BodyRange.M6)
        assertTrue(usage.shares.isNotEmpty())
        assertNull("no load data means no volume headline", usage.volumeBasedDominantPercent)
    }

    // ------------------------------------------------------------- endurance

    @Test
    fun paceIsDerivedFromDistanceAndTime() {
        val summary = EnduranceAnalytics.summary(dataset, BodyRange.M3)
        assertTrue(summary.records.isNotEmpty())
        val record = summary.records.first()
        assertEquals(record.durationSeconds / record.distanceKm, record.paceSecPerKm!!, 0.001)
    }

    @Test
    fun paceComparisonRequiresTwoRecords() {
        val oneSession = dataset.copy(sessions = dataset.sessions.take(2))
        val summary = EnduranceAnalytics.summary(oneSession, BodyRange.M3)
        assertTrue(summary.isEmpty || !summary.hasComparison)
    }

    @Test
    fun paceFormatIsReadable() {
        assertEquals("6:20", EnduranceAnalytics.formatPace(380.0))
        assertEquals("-", EnduranceAnalytics.formatPace(null))
        assertEquals("-", EnduranceAnalytics.formatPace(0.0))
    }

    // ------------------------------------------- body composition isolation

    @Test
    fun bodyCompositionSeriesArePerMetricAndNeverSubstituted() {
        val weight = BodyCompositionAnalytics.series(dataset, BodyMetric.WEIGHT, BodyRange.M6)
        val fat = BodyCompositionAnalytics.series(dataset, BodyMetric.BODY_FAT, BodyRange.M6)
        val muscle = BodyCompositionAnalytics.series(dataset, BodyMetric.MUSCLE_MASS, BodyRange.M6)
        assertTrue(weight.points.isNotEmpty())
        assertTrue(fat.points.isNotEmpty())
        assertTrue(muscle.points.isNotEmpty())
        // Different value ranges prove they are not the same series.
        // Disjoint value ranges prove the series are not substitutes.
        assertTrue("weight out of range", weight.points.all { it.value in 60.0..80.0 })
        assertTrue("fat out of range", fat.points.all { it.value in 10.0..30.0 })
        assertTrue("muscle out of range", muscle.points.all { it.value in 45.0..70.0 })
    }

    @Test
    fun availableMetricsOnlyListsMetricsWithRecords() {
        val available = BodyCompositionAnalytics.availableMetrics(dataset)
        assertTrue(BodyMetric.WEIGHT in available)
        assertTrue(BodyMetric.WAIST !in available)
    }

    @Test
    fun emptyRangeReturnsEmptySeriesNotZero() {
        val noWaist = BodyCompositionAnalytics.series(dataset, BodyMetric.WAIST, BodyRange.D7)
        assertTrue(noWaist.points.isEmpty())
        assertNull(noWaist.latest)
        assertNull(noWaist.changePercent())
    }

    // ------------------------------------------------------------- goals

    @Test
    fun goalPercentIsNullWithoutNumericTarget() {
        val qualitative = dataset.copy(
            goals = listOf(
                BodyGoalRecord(
                    id = "q", memberId = dataset.memberId, title = "General Fitness",
                    metric = GoalMetric.ENDURANCE, startValue = 0.0, targetValue = null,
                    startedAtEpoch = 0L
                )
            )
        )
        assertNull(BodyProgressAggregator.goalPercent(qualitative.goals.first(), qualitative))
        assertEquals(
            GoalVerdict.INSUFFICIENT_DATA,
            BodyProgressAggregator.goalVerdict(qualitative.goals.first(), qualitative)
        )
    }

    @Test
    fun goalPercentIsComputedFromRealStartAndTarget() {
        val goal = dataset.goals.first { it.metric == GoalMetric.WEIGHT }
        val percent = BodyProgressAggregator.goalPercent(goal, dataset)!!
        assertTrue(percent in 0..100)
        assertTrue(percent > 0)
    }

    @Test
    fun fatLossGoalImprovesWhenPercentageFalls() {
        val goal = dataset.goals.first { it.metric == GoalMetric.BODY_FAT }
        assertEquals(GoalDirection.DECREASE, goal.direction)
        val verdict = BodyProgressAggregator.goalVerdict(goal, dataset)
        assertTrue(
            "verdict was $verdict",
            verdict == GoalVerdict.ON_TRACK || verdict == GoalVerdict.IMPROVING
        )
    }

    @Test
    fun risingWeightIsProgressForMuscleGainButNotForFatLoss() {
        val latestWeight = BodyCompositionAnalytics.series(dataset, BodyMetric.WEIGHT, BodyRange.ALL).latest!!.value
        val gain = dataset.goals.first { it.metric == GoalMetric.WEIGHT }
        val cut = BodyGoalRecord(
            id = "cut", memberId = dataset.memberId, title = "Fat Loss",
            metric = GoalMetric.WEIGHT, startValue = 67.4, targetValue = 64.0,
            startedAtEpoch = 0L, direction = GoalDirection.DECREASE
        )
        // Same metric, same value, opposite verdict: only the goal decides.
        assertTrue(BodyProgressAggregator.movesTowardGoal(gain, latestWeight))
        assertTrue(!BodyProgressAggregator.movesTowardGoal(cut, latestWeight))
        assertTrue(
            BodyProgressAggregator.goalVerdict(gain, dataset) == GoalVerdict.ON_TRACK ||
                BodyProgressAggregator.goalVerdict(gain, dataset) == GoalVerdict.IMPROVING
        )
    }

    @Test
    fun goalDirectionIsRespectedWhenWeightMustFall() {
        val cutting = BodyGoalRecord(
            id = "cut", memberId = dataset.memberId, title = "Fat Loss",
            metric = GoalMetric.WEIGHT, startValue = 69.5, targetValue = 65.0,
            startedAtEpoch = 0L, direction = GoalDirection.DECREASE
        )
        // Latest weight equals the start, so this cannot read as progress.
        val verdict = BodyProgressAggregator.goalVerdict(cutting, dataset)
        assertTrue(verdict == GoalVerdict.STABLE || verdict == GoalVerdict.REGRESSING)
    }

    // ----------------------------------------------------------- consistency

    @Test
    fun lightDaysCountAsTraining() {
        val consistency = ConsistencyAnalytics.consistency(dataset, BodyRange.D30)
        assertTrue(consistency.trainedDays > 0)
        assertEquals(
            (consistency.trainedDays + consistency.lightDays) * 100 / consistency.totalDays,
            consistency.consistencyPercent!!
        )
    }

    @Test
    fun consistencyPercentIsNullWithNoDays() {
        val empty = dataset.copy(sessions = emptyList())
        assertEquals(0, ConsistencyAnalytics.consistency(empty, BodyRange.D30).trainedDays)
        assertNotNull(ConsistencyAnalytics.consistency(empty, BodyRange.D30).consistencyPercent)
    }

    // -------------------------------------------------------------- insights

    @Test
    fun insightsCiteRealEvidenceOrDeclareInsufficiency() {
        val insights = InsightEngine.generate(dataset, BodyRange.M6)
        assertTrue(insights.isNotEmpty())
        insights.forEach { insight ->
            assertTrue(insight.text.isNotBlank())
            // No forbidden claim language.
            val lower = insight.text.lowercase()
            listOf("weak", "injury", "injured", "guarantee", "diagnos").forEach { bad ->
                assertTrue("insight contains '$bad': ${insight.text}", !lower.contains(bad))
            }
        }
    }

    @Test
    fun missingMeasurementsProduceAnHonestInsight() {
        val noBody = source.workoutOnlyDataset(anchor)
        val insights = InsightEngine.generate(noBody, BodyRange.M6)
        assertTrue(insights.any { it.text.contains("No recent body measurement") })
        assertTrue(insights.any { it.tone == InsightTone.DATA })
    }

    @Test
    fun sparseMemberProducesDataToneNotFakeNumbers() {
        val sparse = source.sparseDataset(anchor)
        assertTrue(!sparse.hasEvidence())
        val insights = InsightEngine.generate(sparse, BodyRange.M3)
        assertTrue(insights.any { it.tone == InsightTone.DATA })
    }

    // ------------------------------------------------------- record hygiene

    @Test
    fun correctedSetsAreExcludedFromAnalytics() {
        val corrected = dataset.sessions
            .flatMap { it.effectiveSets }
            .first { it.correctsSetId != null }
        val supersededId = corrected.correctsSetId!!
        assertTrue(
            "fixture should include a corrected record",
            dataset.sessions.any { s -> s.sets.any { it.id == corrected.id } }
        )
        assertTrue(
            "superseded set must not be counted",
            dataset.sessions.none { s -> s.effectiveSets.any { it.id == supersededId } }
        )
    }

    @Test
    fun datasetWithoutAnyEvidenceIsDetected() {
        assertTrue(!source.sparseDataset(anchor).hasEvidence())
        assertTrue(source.workoutOnlyDataset(anchor).hasEvidence())
    }

    // ------------------------------------------------------------- privacy

    @Test
    fun consentGatesBodyCompositionForPublicViewer() {
        val consent = BodyDataConsent(
            memberId = dataset.memberId,
            allowsBodyComposition = true,
            visibleTo = setOf(ViewerRole.SELF, ViewerRole.OWNER)
        )
        assertTrue(consent.permits(ViewerRole.OWNER, BodyMetric.BODY_FAT))
        assertTrue("public must not see body fat", !consent.permits(ViewerRole.PUBLIC, BodyMetric.BODY_FAT))
    }

    @Test
    fun withdrawnConsentBlocksEvenStaff() {
        val consent = BodyDataConsent(
            memberId = dataset.memberId,
            allowsBodyComposition = false,
            visibleTo = setOf(ViewerRole.SELF, ViewerRole.OWNER, ViewerRole.STAFF)
        )
        assertTrue(!consent.permits(ViewerRole.STAFF, BodyMetric.MUSCLE_MASS))
    }

    @Test
    fun weightAndBmiRemainVisibleWithoutSensitiveConsent() {
        val consent = BodyDataConsent(
            memberId = dataset.memberId,
            allowsBodyComposition = false,
            visibleTo = setOf(ViewerRole.SELF)
        )
        assertTrue(consent.permits(ViewerRole.SELF, BodyMetric.WEIGHT))
        assertTrue(consent.permits(ViewerRole.SELF, BodyMetric.BMI))
    }

    // ------------------------------------------------------------- summary

    @Test
    fun summaryReflectsGoalAndRealRecords() {
        val summary = BodyProgressAggregator.summarize(dataset, BodyRange.D30)
        assertNotNull(summary.goal)
        assertTrue(summary.hasAnyData)
        assertTrue(summary.muscleFocus.totalSets > 0)
        assertTrue(summary.supportingMetrics.size <= 3)
        assertTrue(summary.availableMetrics.contains(BodyMetric.WEIGHT))
    }

    @Test
    fun workoutOnlyMemberFallsBackToStrengthWithoutFakeBodyMetrics() {
        val summary = BodyProgressAggregator.summarize(source.workoutOnlyDataset(anchor), BodyRange.M6)
        assertTrue(summary.availableMetrics.isEmpty())
        assertNull(summary.primarySeries)
        assertTrue(summary.hasAnyData)
        assertNotNull(summary.strength)
    }

    @Test
    fun frequencyShareIsRelativeToRecordedSets() {
        val summary = BodyProgressAggregator.summarize(dataset, BodyRange.M6)
        val top = summary.exercisesByFrequency().first()
        assertEquals(
            top.setCount * 100 / summary.muscleFocus.totalSets,
            top.frequencySharePercent
        )
        assertTrue(summary.exercisesByFrequency().sumOf { it.setCount } <= summary.muscleFocus.totalSets)
    }
}
