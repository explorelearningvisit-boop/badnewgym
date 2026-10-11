package com.example.badnewgym.feature.bodyprogress.domain

import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyCompositionAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ConsistencyAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EquipmentUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ExerciseUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MetricSeries
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MuscleFocusAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyGoalRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.insights.Insight
import com.example.badnewgym.feature.bodyprogress.domain.insights.InsightEngine
import com.example.badnewgym.feature.bodyprogress.domain.insights.hasEvidence
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Aggregate for the compact Home widget.
 *
 * Everything below is derived once per (dataset, range) in the repository or
 * view-model layer, so the widget never loads or scans full workout history
 * during composition.
 */
data class BodyProgressSummary(
    val range: BodyRange,
    val goal: BodyGoalRecord?,
    /** Null when the goal has no numeric denominator - no fake ring. */
    val goalPercent: Int?,
    val goalVerdict: GoalVerdict,
    val primaryMetric: BodyMetric?,
    val primarySeries: MetricSeries?,
    /** Every metric that has records in the selected range, for chart switching. */
    val compositionSeries: Map<BodyMetric, MetricSeries>,
    val supportingMetrics: List<MetricSeries>,
    val muscleFocus: MuscleFocusAnalytics.MuscleFocus,
    val usage: ExerciseUsageAnalytics.Usage,
    val equipment: EquipmentUsageAnalytics.EquipmentUsage,
    val topExercise: ExerciseUsageAnalytics.ExerciseStat?,
    val equipmentDominantPercent: Int?,
    val strength: StrengthAnalytics.Progression?,
    val endurance: EnduranceAnalytics.EnduranceSummary,
    val consistency: ConsistencyAnalytics.Consistency,
    val insights: List<Insight>,
    val availableMetrics: List<BodyMetric>,
    val dataNotes: List<String>
) {
    val hasAnyData: Boolean get() = !muscleFocus.isEmpty || primarySeries != null
    val isQualitativeGoal: Boolean get() = goal != null && goal.targetValue == null

    fun focusShare(muscle: MuscleGroup): Int? =
        muscleFocus.shares.firstOrNull { it.muscle == muscle }?.sharePercent

    fun compositionSeries(metric: BodyMetric): MetricSeries? = compositionSeries[metric]

    /** Exercises ranked by recorded set count; share is relative to all sets. */
    fun exercisesByFrequency(): List<ExerciseUsageAnalytics.ExerciseStat> {
        val total = muscleFocus.totalSets
        if (total <= 0) return emptyList()
        return usage.byFrequency().map { it.copy(frequencySharePercent = (it.setCount * 100) / total) }
    }
}

enum class GoalVerdict(val label: String) {
    ON_TRACK("On Track"),
    IMPROVING("Improving"),
    REGRESSING("Needs Attention"),
    STABLE("Stable"),
    INSUFFICIENT_DATA("Insufficient Data")
}

/**
 * Builds the widget summary from real records.
 *
 * Rules kept here rather than in the UI:
 *  - only metrics with actual readings are offered;
 *  - the goal percentage is computed from start/target and is null when the
 *    goal has no numeric target;
 *  - the primary metric is chosen by goal relevance, then by data availability.
 */
object BodyProgressAggregator {

    fun summarize(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): BodyProgressSummary {
        val goal = dataset.goals.firstOrNull { it.active }
        val available = BodyCompositionAnalytics.availableMetrics(dataset)
        val focus = MuscleFocusAnalytics.focus(dataset, range)
        val usage = ExerciseUsageAnalytics.usage(dataset, range)
        val equipment = EquipmentUsageAnalytics.usage(dataset, range)
        val consistency = ConsistencyAnalytics.consistency(dataset, range)
        val endurance = EnduranceAnalytics.summary(dataset, range)
        val insights = InsightEngine.generate(dataset, range)

        // Goal progress is a question about the whole programme, so the primary
        // chart always gets a window long enough to hold a trend - a 30-day
        // window can legitimately hold a single reading.
        val primaryMetric = pickPrimary(goal, available)
        val primaryRange = chartRange(range)
        val primarySeries = primaryMetric?.let { BodyCompositionAnalytics.series(dataset, it, primaryRange) }
        // Every metric that has records is carried so the analytics screen can
        // switch charts without re-running the whole aggregation.
        val allSeries: Map<BodyMetric, MetricSeries> =
            available.associateWith { BodyCompositionAnalytics.series(dataset, it, range) }
        val supporting = available
            .filter { it != primaryMetric }
            .take(3)
            .mapNotNull { allSeries[it] }
            .filter { it.points.isNotEmpty() }

        val strengthTarget = usage.stats
            .firstOrNull { it.exercise.pattern == com.example.badnewgym.feature.bodyprogress.domain.model.MovementPattern.HORIZONTAL_PUSH }
            ?: usage.stats.firstOrNull()
        val strength = strengthTarget?.let {
            StrengthAnalytics.progression(dataset, it.exercise.id, range)
        }

        val percent = goalPercent(goal, dataset, range)
        val verdict = goalVerdict(goal, dataset, range)

        val notes = buildList {
            if (focus.hasUnmappedExercises || usage.unmappedIds.isNotEmpty()) {
                add("Some logged sets reference exercises missing from the catalogue.")
            }
            if (dataset.consent.allowsBodyComposition.not()) {
                add("Body-composition sharing is switched off for this member.")
            }
            primarySeries?.let { series ->
                if (!series.hasHistory && series.points.isNotEmpty()) {
                    add("Only one ${series.metric.label} reading on record - trend not yet assessable.")
                }
            }
        }

        return BodyProgressSummary(
            range = range,
            goal = goal,
            goalPercent = percent,
            goalVerdict = verdict,
            primaryMetric = primaryMetric,
            primarySeries = primarySeries,
            compositionSeries = allSeries,
            supportingMetrics = supporting,
            muscleFocus = focus,
            usage = usage,
            equipment = equipment,
            topExercise = usage.byFrequency().firstOrNull(),
            equipmentDominantPercent = equipment.volumeBasedDominantPercent,
            strength = strength,
            endurance = endurance,
            consistency = consistency,
            insights = insights,
            availableMetrics = available,
            dataNotes = notes
        )
    }

    /**
     * Window used for the primary trend chart. Widens to at least 3 months (or
     * all history) so the chart is never reduced to a single dot, while the
     * card header still reports the range the user selected.
     */
    fun chartRange(selected: BodyRange): BodyRange = when {
        selected.days < 0 -> BodyRange.ALL
        selected.days < BodyRange.M3.days -> BodyRange.M3
        else -> selected
    }

    /** Goal completion percentage; null without a real denominator. */
    fun goalPercent(
        goal: BodyGoalRecord?,
        dataset: BodyProgressDataset,
        range: BodyRange = BodyRange.ALL
    ): Int? {
        if (goal == null) return null
        val target = goal.targetValue ?: return null
        val span = target - goal.startValue
        if (abs(span) < 0.0001) return null
        val current = currentForGoal(goal, dataset, range) ?: return null
        val progress = (current - goal.startValue) / span
        return (progress.coerceIn(0.0, 1.0) * 100).toInt()
    }

    fun goalVerdict(
        goal: BodyGoalRecord?,
        dataset: BodyProgressDataset,
        range: BodyRange = BodyRange.ALL
    ): GoalVerdict {
        if (goal == null) return GoalVerdict.INSUFFICIENT_DATA
        val current = currentForGoal(goal, dataset, range) ?: return GoalVerdict.INSUFFICIENT_DATA
        // Direction comes from the goal, never from the metric: weight rising is
        // progress for muscle gain and a miss for fat loss.
        val movingTowardTarget = when (goal.direction) {
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.INCREASE -> current > goal.startValue
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.DECREASE -> current < goal.startValue
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.HOLD ->
                return GoalVerdict.INSUFFICIENT_DATA
        }
        if (!movingTowardTarget) {
            val same = abs(current - goal.startValue) < 0.0001
            return if (same) GoalVerdict.STABLE else GoalVerdict.REGRESSING
        }
        val percent = goalPercent(goal, dataset, range) ?: return GoalVerdict.IMPROVING
        return if (percent >= 85) GoalVerdict.ON_TRACK else GoalVerdict.IMPROVING
    }

    /** True when [value] moves this goal in the intended direction. */
    fun movesTowardGoal(goal: BodyGoalRecord, value: Double): Boolean =
        when (goal.direction) {
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.INCREASE -> value > goal.startValue
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.DECREASE -> value < goal.startValue
            com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection.HOLD -> false
        }

    // ------------------------------------------------------------ focus groups

    /** Display group for the Home muscle-focus panel. */
    data class FocusGroup(val label: String, val sharePercent: Int, val sets: Int)

    private val FOCUS_GROUPS: List<Pair<String, Set<MuscleGroup>>> = listOf(
        "Chest" to setOf(MuscleGroup.CHEST),
        "Back" to setOf(MuscleGroup.BACK, MuscleGroup.LATS, MuscleGroup.TRAPS),
        "Arms" to setOf(MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.FOREARMS),
        "Legs" to setOf(
            MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES,
            MuscleGroup.CALVES, MuscleGroup.HIP_FLEXORS
        ),
        "Others" to setOf(
            MuscleGroup.SHOULDERS, MuscleGroup.REAR_DELTS,
            MuscleGroup.CORE, MuscleGroup.LOWER_BACK
        )
    )

    /**
     * Recorded-set distribution across five display groups.
     *
     * The denominator is the focus's total weighted sets, so the shares always
     * describe the same whole; rounding drift is folded into the largest
     * group so the panel sums to 100%.
     */
    fun focusGroups(focus: MuscleFocusAnalytics.MuscleFocus): List<FocusGroup> {
        if (focus.isEmpty) return emptyList()
        val byMuscle = focus.shares.associate { it.muscle to it }
        val total = focus.shares.sumOf { it.weightedSets }
        if (total <= 0) return emptyList()
        data class Raw(val label: String, val weight: Double, val sets: Int)
        val raws = FOCUS_GROUPS.map { (label, members) ->
            Raw(
                label,
                members.sumOf { byMuscle[it]?.weightedSets ?: 0.0 },
                members.sumOf { byMuscle[it]?.sets ?: 0 }
            )
        }
        val rounded = raws.map { (it.weight / total * 100).roundToInt() }.toMutableList()
        val drift = 100 - rounded.sum()
        if (drift != 0) {
            val biggest = raws.indices.maxByOrNull { raws[it].weight } ?: 0
            rounded[biggest] = (rounded[biggest] + drift).coerceAtLeast(0)
        }
        return raws.mapIndexed { i, raw -> FocusGroup(raw.label, rounded[i], raw.sets) }
    }

    private fun currentForGoal(
        goal: BodyGoalRecord,
        dataset: BodyProgressDataset,
        range: BodyRange
    ): Double? = when (goal.metric) {
        GoalMetric.WEIGHT -> latestMeasurement(dataset, BodyMetric.WEIGHT, range)
        GoalMetric.BODY_FAT -> latestMeasurement(dataset, BodyMetric.BODY_FAT, range)
        GoalMetric.MUSCLE_MASS -> latestMeasurement(dataset, BodyMetric.MUSCLE_MASS, range)
        GoalMetric.WAIST -> latestMeasurement(dataset, BodyMetric.WAIST, range)
        GoalMetric.FIVE_K_TIME -> dataset.fitnessTests
            .filter { range.covers(dataset.anchor.toEpochDay(), it.epochDay) }
            .minByOrNull { it.performedAtEpoch }?.value
        GoalMetric.RUN_DISTANCE -> EnduranceAnalytics.summary(dataset, range)
            .latestPace?.distanceKm
        GoalMetric.STRENGTH -> StrengthAnalytics.bestRecords(dataset, range).firstOrNull()?.loadKg
        // A qualitative goal has no single measurable value.
        GoalMetric.ENDURANCE -> null
    }

    private fun latestMeasurement(
        dataset: BodyProgressDataset,
        metric: BodyMetric,
        range: BodyRange
    ): Double? = BodyCompositionAnalytics.series(dataset, metric, range).latest?.value

    /** Goal-aware metric priority; falls back to whatever data exists. */
    private fun pickPrimary(goal: BodyGoalRecord?, available: List<BodyMetric>): BodyMetric? {
        if (available.isEmpty()) return null
        val preferred = when (goal?.metric) {
            GoalMetric.WEIGHT -> BodyMetric.WEIGHT
            GoalMetric.BODY_FAT -> BodyMetric.BODY_FAT
            GoalMetric.MUSCLE_MASS -> BodyMetric.MUSCLE_MASS
            GoalMetric.WAIST -> BodyMetric.WAIST
            else -> null
        }
        return preferred?.takeIf { it in available } ?: available.first()
    }
}

/** Dataset-level guard used by both surfaces before rendering any chart. */
fun BodyProgressDataset.renderableSummary(range: BodyRange): Boolean = hasEvidence()
