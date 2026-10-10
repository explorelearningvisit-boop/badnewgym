package com.example.badnewgym.feature.bodyprogress.domain.analytics

import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyRegion
import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.Exercise
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.VerificationState
import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSetRecord
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.roundToInt

// ------------------------------------------------------------------ ranges

/** Selectable analysis windows. Ranges longer than the data simply report less. */
enum class BodyRange(val days: Int, val label: String) {
    D7(7, "7D"), D14(14, "14D"), D15(15, "15D"), D30(30, "30D"),
    M3(91, "3M"), M6(182, "6M"), Y1(365, "1Y"), ALL(-1, "All");

    fun covers(anchorDay: Long, day: Long): Boolean =
        this == ALL || (anchorDay - day) < days
}

data class MetricSeries(
    val metric: BodyMetric,
    val points: List<Point>,
    val method: String? = null
) {
    data class Point(val day: Long, val value: Double, val verification: VerificationState)

    val latest: Point? get() = points.maxByOrNull { it.day }
    val earliest: Point? get() = points.minByOrNull { it.day }
    val hasHistory: Boolean get() = points.size >= 2
    /** Percent change between first and last, or null when not computable. */
    fun changePercent(): Double? {
        val a = earliest ?: return null
        val b = latest ?: return null
        if (a.value == 0.0) return null
        return ((b.value - a.value) / abs(a.value)) * 100.0
    }
}

// ------------------------------------------------------------------ body composition

object BodyCompositionAnalytics {

    /**
     * Body composition history for one metric, oldest first.
     *
     * Body weight, fat and muscle never stand in for each other: this returns
     * exactly the records that exist, and an empty list when there are none.
     */
    fun series(
        dataset: BodyProgressDataset,
        metric: BodyMetric,
        range: BodyRange = BodyRange.D30
    ): MetricSeries {
        val anchor = dataset.anchor.toEpochDay()
        val points = dataset.measurements
            .filter { it.metric == metric }
            .filter { range.covers(anchor, it.epochDay) }
            .sortedBy { it.measuredAtEpoch }
            .map { MetricSeries.Point(it.epochDay, it.value, it.verification) }
        val method = dataset.measurements
            .filter { it.metric == metric }
            .maxByOrNull { it.measuredAtEpoch }
            ?.method?.label
        return MetricSeries(metric, points, method)
    }

    /** Metrics that actually have records - the UI must not list the others. */
    fun availableMetrics(dataset: BodyProgressDataset): List<BodyMetric> =
        dataset.measurements.map { it.metric }.distinct()

    fun latest(dataset: BodyProgressDataset, metric: BodyMetric): MetricSeries.Point? =
        series(dataset, metric, BodyRange.ALL).latest

    fun changeSince(dataset: BodyProgressDataset, metric: BodyMetric, range: BodyRange): Double? =
        series(dataset, metric, range).changePercent()
}

// ------------------------------------------------------------------ muscle focus

/**
 * Muscle-focus aggregation.
 *
 * Weighting contract (documented, not arbitrary):
 *  - a set on an exercise contributes PRIMARY_WEIGHT to each primary muscle
 *    and SECONDARY_WEIGHT to each secondary muscle;
 *  - session coverage counts a muscle once per session, never per set;
 *  - volume uses normalised kg x reps so different equipment still compares.
 */
object MuscleFocusAnalytics {

    const val PRIMARY_WEIGHT = 1.0
    const val SECONDARY_WEIGHT = 0.4

    data class MuscleShare(
        val muscle: MuscleGroup,
        val weightedSets: Double,
        val sessionCount: Int,
        val sets: Int,
        val sharePercent: Int,
        val volumeKg: Double
    )

    data class MuscleFocus(
        val shares: List<MuscleShare>,
        val sessionCount: Int,
        val exerciseCount: Int,
        val totalSets: Int,
        /** True when some sets referenced an exercise missing from the catalogue. */
        val hasUnmappedExercises: Boolean
    ) {
        val top: List<MuscleShare> get() = shares.take(3)
        val isEmpty: Boolean get() = totalSets == 0
        fun region(region: BodyRegion): Double =
            shares.filter { it.muscle.region == region }
                .sumOf { it.weightedSets }
    }

    fun focus(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): MuscleFocus {
        val anchor = dataset.anchor.toEpochDay()
        val sessions = dataset.sessions.filter { range.covers(anchor, it.epochDay) }
        val sets = sessions.flatMap { it.effectiveSets }
        val exerciseIds = sets.map { it.exerciseId }.distinct()
        val mapped = exerciseIds.mapNotNull { ExerciseCatalog.byId(it) }
        val unmapped = exerciseIds.size - mapped.size

        val weighted = mutableMapOf<MuscleGroup, Double>()
        val volume = mutableMapOf<MuscleGroup, Double>()
        val setCount = mutableMapOf<MuscleGroup, Int>()
        val sessionCount = mutableMapOf<MuscleGroup, Int>()

        fun add(muscle: MuscleGroup, weight: Double, volumeKg: Double) {
            weighted[muscle] = (weighted[muscle] ?: 0.0) + weight
            volume[muscle] = (volume[muscle] ?: 0.0) + volumeKg
            setCount[muscle] = (setCount[muscle] ?: 0) + 1
        }

        for (session in sessions) {
            val sessionMuscles = mutableSetOf<MuscleGroup>()
            for (set in session.effectiveSets) {
                val exercise = ExerciseCatalog.byId(set.exerciseId) ?: continue
                val volumeKg = (set.loadKg() ?: 0.0) * (set.reps ?: 0)
                exercise.primaryMuscles.forEach {
                    add(it, PRIMARY_WEIGHT, volumeKg)
                    sessionMuscles += it
                }
                exercise.secondaryMuscles.forEach {
                    add(it, SECONDARY_WEIGHT, volumeKg)
                    sessionMuscles += it
                }
            }
            sessionMuscles.forEach { sessionCount[it] = (sessionCount[it] ?: 0) + 1 }
        }

        val total = weighted.values.sum()
        val shares = weighted.entries
            .map { (muscle, value) ->
                MuscleShare(
                    muscle = muscle,
                    weightedSets = value,
                    sessionCount = sessionCount[muscle] ?: 0,
                    sets = setCount[muscle] ?: 0,
                    sharePercent = if (total > 0) ((value / total) * 100).roundToInt() else 0,
                    volumeKg = volume[muscle] ?: 0.0
                )
            }
            .sortedWith(compareByDescending<MuscleShare> { it.weightedSets }.thenBy { it.muscle.label })

        return MuscleFocus(
            shares = shares,
            sessionCount = sessions.size,
            exerciseCount = mapped.size,
            totalSets = sets.size,
            hasUnmappedExercises = unmapped > 0
        )
    }

    /**
     * Muscle groups with no recorded work in the window.
     *
     * Reported as "not recorded", never as weak or underdeveloped: absence of
     * a record is not evidence about the body.
     */
    fun notRecorded(focus: MuscleFocus, candidates: List<MuscleGroup> = MuscleGroup.entries): List<MuscleGroup> {
        val seen = focus.shares.map { it.muscle }.toSet()
        return candidates.filter { it !in seen }
    }
}

// ------------------------------------------------------------------ exercise + equipment

object ExerciseUsageAnalytics {

    data class ExerciseStat(
        val exercise: Exercise,
        val setCount: Int,
        val repTotal: Int,
        val volumeKg: Double,
        val sessionCount: Int,
        val topLoadKg: Double?,
        val lastDay: Long,
        val sharePercent: Int,
        /** Share of recorded SETS; distinct from [sharePercent], which is volume. */
        val frequencySharePercent: Int = 0
    )

    data class Usage(
        val stats: List<ExerciseStat>,
        val unmappedIds: List<String>
    ) {
        val isEmpty: Boolean get() = stats.isEmpty()
        fun byFrequency(): List<ExerciseStat> = stats.sortedWith(
            compareByDescending<ExerciseStat> { it.setCount }.thenBy { it.exercise.name }
        )
        fun byVolume(): List<ExerciseStat> = stats.sortedWith(
            compareByDescending<ExerciseStat> { it.volumeKg }.thenBy { it.exercise.name }
        )
    }

    fun usage(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): Usage {
        val anchor = dataset.anchor.toEpochDay()
        val sessions = dataset.sessions.filter { range.covers(anchor, it.epochDay) }
        val unmapped = linkedSetOf<String>()

        val byExercise = sessions
            .flatMap { it.effectiveSets }
            .groupBy { set ->
                ExerciseCatalog.byId(set.exerciseId)?.also { }.also { if (it == null) unmapped += set.exerciseId }
                set.exerciseId
            }

        val totalVolume = byExercise.values.sumOf { group ->
            group.sumOf { (it.loadKg() ?: 0.0) * (it.reps ?: 0) }
        }

        val stats = byExercise.mapNotNull { (id, group) ->
            val exercise = ExerciseCatalog.byId(id) ?: return@mapNotNull null
            val volume = group.sumOf { (it.loadKg() ?: 0.0) * (it.reps ?: 0) }
            ExerciseStat(
                exercise = exercise,
                setCount = group.size,
                repTotal = group.sumOf { it.reps ?: 0 },
                volumeKg = volume,
                sessionCount = sessions.count { s -> s.effectiveSets.any { it.exerciseId == id } },
                topLoadKg = group.mapNotNull { it.loadKg() }.maxOrNull(),
                lastDay = group.maxOf { it.recordedAtEpoch / 86_400_000L },
                sharePercent = if (totalVolume > 0) ((volume / totalVolume) * 100).roundToInt() else 0
            )
        }.sortedByDescending { it.volumeKg }

        return Usage(stats, unmapped.toList())
    }
}

object EquipmentUsageAnalytics {

    data class EquipmentShare(
        val category: EquipmentCategory,
        val setCount: Int,
        val sharePercent: Int
    )

    data class EquipmentUsage(
        val shares: List<EquipmentShare>,
        val unmappedIds: List<String>,
        /** Null when no comparable volume data exists - no fake 52% ring. */
        val volumeBasedDominantPercent: Int?
    )

    fun usage(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): EquipmentUsage {
        val anchor = dataset.anchor.toEpochDay()
        val sets = dataset.sessions
            .filter { range.covers(anchor, it.epochDay) }
            .flatMap { it.effectiveSets }
        val unmapped = linkedSetOf<String>()

        val counts = mutableMapOf<EquipmentCategory, Int>()
        val volume = mutableMapOf<EquipmentCategory, Double>()
        sets.forEach { set ->
            val exercise = ExerciseCatalog.byId(set.exerciseId) ?: run { unmapped += set.exerciseId; return@forEach }
            counts[exercise.equipment] = (counts[exercise.equipment] ?: 0) + 1
            volume[exercise.equipment] = (volume[exercise.equipment] ?: 0.0) +
                (set.loadKg() ?: 0.0) * (set.reps ?: 0)
        }
        val total = counts.values.sum()
        val totalVolume = volume.values.sum()
        val shares = counts.entries.map { (cat, count) ->
            EquipmentShare(cat, count, if (total > 0) ((count.toDouble() / total) * 100).roundToInt() else 0)
        }.sortedByDescending { it.setCount }

        // A "Free Weights 52%" headline is only meaningful when volume exists.
        val dominant = volume.entries.maxByOrNull { it.value }
        val dominantPercent = if (totalVolume > 0 && dominant != null) {
            ((dominant.value / totalVolume) * 100).roundToInt()
        } else null

        return EquipmentUsage(shares, unmapped.toList(), dominantPercent)
    }
}

// ------------------------------------------------------------------ strength

object StrengthAnalytics {

    data class ComparableSet(
        val exerciseId: String,
        val exerciseName: String,
        val day: Long,
        val loadKg: Double,
        val reps: Int,
        val estimated1RmKg: Double,
        val verification: VerificationState
    )

    data class Progression(
        val exerciseName: String,
        val previous: ComparableSet,
        val current: ComparableSet,
        /** Null when the two records are not comparable. */
        val loadDeltaKg: Double?,
        val repDelta: Int,
        val verdict: StrengthVerdict
    )

    enum class StrengthVerdict { IMPROVING, REGRESSING, STABLE, INSUFFICIENT_DATA }

    /**
     * Estimated one-rep max.
     *
     * Epley for reps <= 10, Brzycki beyond. Returns null when reps or load is
     * missing - an estimate is never invented from bodyweight work.
     */
    fun estimated1Rm(loadKg: Double, reps: Int): Double? {
        if (reps <= 0 || loadKg <= 0) return null
        if (reps == 1) return loadKg
        return if (reps <= 10) loadKg * (1.0 + reps / 30.0)
        else loadKg * 36.0 / (37.0 - reps)
    }

    /**
     * Only records from the SAME variation key are compared.
     *
     * A heavier dumbbell curl is not "progress" over a lighter barbell curl,
     * and reps dropping while load rises is reported, not smoothed over.
     */
    fun progression(
        dataset: BodyProgressDataset,
        exerciseId: String,
        range: BodyRange = BodyRange.ALL
    ): Progression? {
        val exercise = ExerciseCatalog.byId(exerciseId) ?: return null
        val anchor = dataset.anchor.toEpochDay()
        val candidates = dataset.sessions
            .flatMap { it.effectiveSets }
            .filter { it.exerciseId == exerciseId }
            .filter { range.covers(anchor, it.recordedAtEpoch / 86_400_000L) }
            .mapNotNull { set ->
                val load = set.loadKg() ?: return@mapNotNull null
                val reps = set.reps ?: return@mapNotNull null
                val e1rm = estimated1Rm(load, reps) ?: return@mapNotNull null
                ComparableSet(
                    exerciseId = exerciseId,
                    exerciseName = exercise.name,
                    day = set.recordedAtEpoch / 86_400_000L,
                    loadKg = load, reps = reps,
                    estimated1RmKg = e1rm, verification = set.verification
                )
            }
            .sortedBy { it.day }
        if (candidates.size < 2) return null

        // Compare the best set of the two most recent DISTINCT sessions.
        // Comparing the last two sets of one session would report a rep drop
        // inside a single workout as a training regression.
        val perDay = candidates.groupBy { it.day }
            .map { (_, rows) -> rows.maxBy { it.estimated1RmKg } }
            .sortedBy { it.day }
        if (perDay.size < 2) return null

        val previous = perDay[perDay.size - 2]
        val current = perDay.last()
        val delta = current.loadKg - previous.loadKg
        val repDelta = current.reps - previous.reps
        val e1rmDelta = current.estimated1RmKg - previous.estimated1RmKg

        // Estimated 1RM is the fairer comparison when reps differ; a load
        // increase paired with a large rep drop is not silently a win.
        val verdict = when {
            abs(e1rmDelta) < 0.5 -> StrengthVerdict.STABLE
            e1rmDelta > 0 -> StrengthVerdict.IMPROVING
            else -> StrengthVerdict.REGRESSING
        }
        return Progression(
            exerciseName = exercise.name,
            previous = previous,
            current = current,
            loadDeltaKg = delta,
            repDelta = repDelta,
            verdict = verdict
        )
    }

    /** Best comparable set across every catalogue entry that tracks load. */
    fun bestRecords(dataset: BodyProgressDataset, range: BodyRange = BodyRange.ALL): List<ComparableSet> {
        val anchor = dataset.anchor.toEpochDay()
        return dataset.sessions
            .flatMap { it.effectiveSets }
            .filter { range.covers(anchor, it.recordedAtEpoch / 86_400_000L) }
            .mapNotNull { set ->
                val exercise = ExerciseCatalog.byId(set.exerciseId) ?: return@mapNotNull null
                if (!exercise.tracksLoad) return@mapNotNull null
                val load = set.loadKg() ?: return@mapNotNull null
                val reps = set.reps ?: return@mapNotNull null
                val e1rm = estimated1Rm(load, reps) ?: return@mapNotNull null
                ComparableSet(
                    exerciseId = exercise.id, exerciseName = exercise.name,
                    day = set.recordedAtEpoch / 86_400_000L,
                    loadKg = load, reps = reps,
                    estimated1RmKg = e1rm, verification = set.verification
                )
            }
            .groupBy { it.exerciseId }
            .mapNotNull { (_, rows) -> rows.maxByOrNull { it.estimated1RmKg } }
            .sortedByDescending { it.estimated1RmKg }
    }
}

// ------------------------------------------------------------------ endurance

object EnduranceAnalytics {

    data class RunRecord(val day: Long, val distanceKm: Double, val durationSeconds: Int) {
        /** Pace in seconds per km; null for a zero-length session. */
        val paceSecPerKm: Double? get() = if (distanceKm > 0) durationSeconds / distanceKm else null
    }

    data class EnduranceSummary(
        val records: List<RunRecord>,
        val bestPace: RunRecord?,
        val latestPace: RunRecord?,
        val totalDistanceKm: Double,
        /** Null when fewer than two comparable records exist. */
        val paceChangeSecPerKm: Double?,
        val testKind: FitnessTestKind? = null
    ) {
        val isEmpty: Boolean get() = records.isEmpty()
        val hasComparison: Boolean get() = paceChangeSecPerKm != null
    }

    /** Pace analysis from real cardio records - never a synthetic stamina score. */
    fun summary(dataset: BodyProgressDataset, range: BodyRange = BodyRange.M3): EnduranceSummary {
        val anchor = dataset.anchor.toEpochDay()
        val cardioIds = ExerciseCatalog.all.filter { it.isCardio && it.tracksDistance }.map { it.id }.toSet()
        val records = dataset.sessions
            .filter { range.covers(anchor, it.epochDay) }
            .flatMap { it.effectiveSets }
            .filter { it.exerciseId in cardioIds }
            .mapNotNull { set ->
                val meters = set.distanceMeters ?: return@mapNotNull null
                val seconds = set.durationSeconds ?: return@mapNotNull null
                if (meters <= 0 || seconds <= 0) return@mapNotNull null
                RunRecord(set.recordedAtEpoch / 86_400_000L, meters / 1000.0, seconds)
            }
            .sortedBy { it.day }

        val byDay = records.groupBy { it.day }.map { (day, rows) ->
            RunRecord(
                day = day,
                distanceKm = rows.sumOf { it.distanceKm },
                durationSeconds = rows.sumOf { it.durationSeconds }
            )
        }.sortedBy { it.day }

        val withPace = byDay.filter { it.paceSecPerKm != null }
        val best = withPace.minByOrNull { it.paceSecPerKm!! }
        val latest = withPace.lastOrNull()
        val change = if (withPace.size >= 2) {
            latest!!.paceSecPerKm!! - withPace.first().paceSecPerKm!!
        } else null

        return EnduranceSummary(
            records = byDay,
            bestPace = best,
            latestPace = latest,
            totalDistanceKm = byDay.sumOf { it.distanceKm },
            paceChangeSecPerKm = change
        )
    }

    /** Human pace such as 6:20 /km. */
    fun formatPace(secPerKm: Double?): String {
        if (secPerKm == null || secPerKm <= 0) return "-"
        val total = secPerKm.roundToInt()
        return "%d:%02d".format(total / 60, total % 60)
    }
}

// ------------------------------------------------------------------ consistency

object ConsistencyAnalytics {

    enum class DayState(val label: String) {
        WORKOUT("Workout"), LIGHT("Light"), ABSENT("Absent"), REST("Rest")
    }

    data class Consistency(
        val days: List<Pair<Long, DayState>>,
        val trainedDays: Int,
        val lightDays: Int,
        val absentDays: Int,
        val totalDays: Int
    ) {
        /** Null when there are no trained days to divide by. */
        val consistencyPercent: Int?
            get() = if (totalDays > 0) ((trainedDays + lightDays) * 100 / totalDays) else null
    }

    /**
     * Consistency from session volume: a light day still counts as training,
     * an empty day is reported as "Absent" (not a failure verdict).
     */
    fun consistency(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): Consistency {
        val anchor = dataset.anchor.toEpochDay()
        val days = if (range.days < 0) 30 else range.days
        val volumeByDay = dataset.sessions.associate { it.epochDay to it.effectiveSets.size }
        var trained = 0
        var light = 0
        var absent = 0
        val rows = (days - 1 downTo 0).map { back ->
            val day = anchor - back
            val count = volumeByDay[day] ?: 0
            val state = when {
                count >= 4 -> DayState.WORKOUT
                count >= 1 -> DayState.LIGHT
                else -> DayState.ABSENT
            }
            when (state) {
                DayState.WORKOUT -> trained++
                DayState.LIGHT -> light++
                DayState.ABSENT -> absent++
                DayState.REST -> Unit
            }
            day to state
        }
        return Consistency(rows, trained, light, absent, days)
    }
}

/** Helper for building a rolling window of epoch days ending at the anchor. */
fun rollingDays(anchor: LocalDate, count: Int): List<Long> {
    val end = anchor.toEpochDay()
    return (count - 1 downTo 0).map { end - it }
}

/** Convenience accessor used by the UI layer. */
fun WorkoutSetRecord.effectiveLoadKg(): Double? = loadKg()
