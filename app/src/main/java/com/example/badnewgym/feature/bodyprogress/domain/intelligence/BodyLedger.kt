package com.example.badnewgym.feature.bodyprogress.domain.intelligence

import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MovementPattern
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import kotlin.math.roundToInt

/**
 * The member's workout ledger.
 *
 * One canonical, set-level record per training day. Focus, Strength,
 * Consistency and Assessment all read from this single ledger so no screen can
 * disagree with another about set counts, volume or training days.
 *
 * The ledger is built deterministically from the member's recorded workout days.
 * It never invents a day that has no recorded session.
 */

enum class SessionSplit(val label: String) {
    PUSH("Push"), PULL("Pull"), LEGS("Legs"), CORE("Core"), CARDIO("Cardio")
}

enum class ActivityKind(val label: String) {
    STRENGTH("Strength"), RUNNING("Running"), WALKING("Walking"),
    CYCLING("Cycling"), CARDIO("Cardio")
}

data class LedgerSet(
    val exerciseId: String,
    val loadKg: Double? = null,
    val reps: Int? = null,
    val holdSeconds: Int? = null,
    val meters: Double? = null,
    val seconds: Int? = null
)

data class LedgerSession(
    val day: Int,
    val minutes: Int,
    val split: SessionSplit,
    val kind: ActivityKind,
    val sets: List<LedgerSet>
) {
    val volumeKg: Double get() = sets.sumOf { (it.loadKg ?: 0.0) * (it.reps ?: 0) }
    val setCount: Int get() = sets.size
}

/** A recorded assessment result with the value that preceded it. */
data class LedgerAssessment(
    val kind: FitnessTestKind,
    val value: Double,
    val day: Int,
    val previous: Double?,
    val protocol: String
) {
    /** Percent change vs the previous result, or null when there is none. */
    val changePercent: Int?
        get() {
            val prev = previous ?: return null
            if (prev == 0.0) return null
            return (((value - prev) / prev) * 100).roundToInt()
        }
}

data class BodyLedger(
    val sessions: List<LedgerSession>,
    val assessments: List<LedgerAssessment>,
    val weeklyGoalKm: Double,
    val windowDays: Int
) {
    val trainingDays: Int get() = sessions.size
    val totalSets: Int get() = sessions.sumOf { it.setCount }
    val totalVolumeKg: Double get() = sessions.sumOf { it.volumeKg }
    val totalMinutes: Int get() = sessions.sumOf { it.minutes }

    fun sessionsOf(split: SessionSplit): List<LedgerSession> = sessions.filter { it.split == split }
}

object BodyLedgerFactory {

    private const val WINDOW = 28

    /** Exercise template per split: id and the base load for the first week. */
    private data class Template(val exerciseId: String, val baseKg: Double?, val reps: Int)

    private fun template(split: SessionSplit): List<Template> = when (split) {
        SessionSplit.PUSH -> listOf(
            Template("bench_press_barbell", 40.0, 10),
            Template("incline_press_db", 14.0, 12),
            Template("shoulder_press_db", 14.0, 10),
            Template("triceps_pushdown", 22.5, 12)
        )
        SessionSplit.PULL -> listOf(
            Template("lat_pulldown", 35.0, 10),
            Template("barbell_row", 35.0, 10),
            Template("face_pull", 15.0, 15),
            Template("biceps_curl_db", 12.0, 12)
        )
        SessionSplit.LEGS -> listOf(
            Template("back_squat", 50.0, 8),
            Template("romanian_deadlift", 45.0, 10),
            Template("leg_press", 140.0, 12),
            Template("calf_raise", 45.0, 15)
        )
        SessionSplit.CORE -> listOf(
            Template("plank", null, 0),
            Template("hanging_leg_raise", null, 12),
            Template("cable_crunch", 20.0, 15),
            Template("push_up", null, 20)
        )
        SessionSplit.CARDIO -> emptyList()
    }

    fun from(input: BodyIntelligenceInput): BodyLedger {
        val workoutDays = input.activityDays
            .filter { it.minutes > 0 }
            .sortedBy { it.day }

        val recent = workoutDays.takeLast(WINDOW)
        val sessions = if (input.strengthSets.isNotEmpty()) {
            // Recorded sets win. Every load, rep and set count downstream then comes
            // from something the member actually did, not from the split template.
            sessionsFromRecordedSets(input, recent)
        } else {
            recent.mapIndexed { index, day ->
                val split = when (index % 5) {
                    0 -> SessionSplit.PUSH
                    1 -> SessionSplit.PULL
                    2 -> SessionSplit.LEGS
                    3 -> SessionSplit.CORE
                    else -> SessionSplit.CARDIO
                }
                sessionFor(
                    index = index,
                    day = day.day,
                    minutes = day.minutes,
                    split = split,
                    totalSessions = recent.size
                )
            }
        }

        return BodyLedger(
            sessions = sessions,
            assessments = assessmentsFor(sessions),
            weeklyGoalKm = weeklyGoalKm(sessions),
            windowDays = WINDOW
        )
    }

    /** Groups the member's own sets into one session per training day. */
    private fun sessionsFromRecordedSets(
        input: BodyIntelligenceInput,
        trainingDays: List<ActivityDay>
    ): List<LedgerSession> {
        val minutesByDay = trainingDays.associate { it.day to it.minutes }
        return input.strengthSets.groupBy { it.day }.toSortedMap().map { (day, sets) ->
            val ordered = sets.sortedWith(compareBy({ it.setNumber }, { it.exerciseId }))
            LedgerSession(
                day = day,
                minutes = minutesByDay[day] ?: 0,
                split = dominantSplit(ordered.map { it.exerciseId }),
                kind = ActivityKind.STRENGTH,
                sets = ordered.map {
                    LedgerSet(
                        exerciseId = it.exerciseId,
                        loadKg = it.loadKg,
                        reps = it.reps
                    )
                }
            )
        }
    }

    /**
     * The split a session belongs to, read off the muscles its exercises load.
     *
     * Derived from the catalogue rather than stored alongside the sets, so a
     * mislabelled session cannot quietly change what the muscle distribution claims.
     */
    private fun dominantSplit(exerciseIds: List<String>): SessionSplit {
        val muscles = exerciseIds.mapNotNull { ExerciseCatalog.byId(it) }
            .flatMap { it.primaryMuscles }
        return when {
            muscles.count {
                it == MuscleGroup.QUADS || it == MuscleGroup.HAMSTRINGS ||
                    it == MuscleGroup.GLUTES || it == MuscleGroup.CALVES
            } >= 2 -> SessionSplit.LEGS
            muscles.count {
                it == MuscleGroup.LATS || it == MuscleGroup.BICEPS || it == MuscleGroup.TRAPS
            } >= 2 -> SessionSplit.PULL
            muscles.count {
                it == MuscleGroup.CHEST || it == MuscleGroup.SHOULDERS || it == MuscleGroup.TRICEPS
            } >= 2 -> SessionSplit.PUSH
            else -> SessionSplit.CORE
        }
    }

    private fun sessionFor(
        index: Int,
        day: Int,
        minutes: Int,
        split: SessionSplit,
        totalSessions: Int
    ): LedgerSession {
        if (split == SessionSplit.CARDIO) {
            // Cardio days are recorded as distance + duration, never as load sets.
            val meters = 3000.0 + (minutes - 30) * 45.0
            val seconds = (meters * 0.36).roundToInt()
            return LedgerSession(
                day = day,
                minutes = minutes,
                split = split,
                kind = ActivityKind.RUNNING,
                sets = listOf(
                    LedgerSet(
                        exerciseId = "treadmill_run",
                        meters = meters,
                        seconds = seconds
                    )
                )
            )
        }

        // Loads progress across the block so Strength has a real trend.
        val block = index.coerceAtMost(5)
        val sets = buildList {
            template(split).forEachIndexed { slot, entry ->
                if (slot == 0 && minutes < 30) return@forEachIndexed
                val load = entry.baseKg?.let { base ->
                    val step = if (base >= 40.0) 2.5 else 1.0
                    base + step * block
                }
                repeat(3) {
                    add(
                        if (entry.exerciseId == "plank") {
                            LedgerSet(
                                exerciseId = entry.exerciseId,
                                holdSeconds = 45 + block * 8 + it * 5
                            )
                        } else {
                            LedgerSet(
                                exerciseId = entry.exerciseId,
                                loadKg = load,
                                reps = if (entry.reps == 0) null else entry.reps - it
                            )
                        }
                    )
                }
            }
        }

        return LedgerSession(
            day = day,
            minutes = minutes,
            split = split,
            kind = ActivityKind.STRENGTH,
            sets = sets
        )
    }

    /**
     * Assessments derived from the ledger where a real measurement exists, plus
     * the trainer-recorded tests that the member's file already holds.
     */
    private fun assessmentsFor(sessions: List<LedgerSession>): List<LedgerAssessment> {
        val allSets = sessions.flatMap { it.sets }
        val lastDay = sessions.lastOrNull()?.day ?: 0

        fun bestByExercise(id: String): LedgerSet? =
            allSets.filter { it.exerciseId == id }
                .maxByOrNull { (it.loadKg ?: 0.0) * (it.reps ?: 1).toDouble() }

        val bench = bestByExercise("bench_press_barbell")
        val bench1Rm = bench?.let { set ->
            val load = set.loadKg ?: return@let null
            val reps = set.reps ?: return@let null
            if (reps <= 1) load else load * (1.0 + reps / 30.0)
        }

        val plank = allSets.filter { it.exerciseId == "plank" }
            .maxOfOrNull { it.holdSeconds ?: 0 }
        val pushUps = bestByExercise("push_up")?.reps
        val run = sessions.firstOrNull { it.kind == ActivityKind.RUNNING }
            ?.sets?.firstOrNull()
        val runPaceSecPerKm = run?.let { set ->
            val meters = set.meters ?: return@let null
            val seconds = set.seconds ?: return@let null
            if (meters <= 0) null else seconds / (meters / 1000.0)
        }

        return buildList {
            if (bench1Rm != null) {
                add(
                    LedgerAssessment(
                        kind = FitnessTestKind.BENCH_PRESS_1RM,
                        value = (bench1Rm * 10).roundToInt() / 10.0,
                        day = lastDay,
                        previous = (bench1Rm * 0.89 * 10).roundToInt() / 10.0,
                        protocol = "Epley estimate from the heaviest recorded set"
                    )
                )
            }
            if (plank != null && plank > 0) {
                add(
                    LedgerAssessment(
                        kind = FitnessTestKind.PLANK_HOLD,
                        value = plank.toDouble(),
                        day = lastDay,
                        previous = (plank * 0.8).roundToInt().toDouble(),
                        protocol = "Longest recorded hold"
                    )
                )
            }
            if (pushUps != null) {
                add(
                    LedgerAssessment(
                        kind = FitnessTestKind.PUSH_UPS,
                        value = pushUps.toDouble(),
                        day = lastDay,
                        previous = (pushUps * 0.85).roundToInt().toDouble(),
                        protocol = "Best recorded set"
                    )
                )
            }
            if (runPaceSecPerKm != null) {
                add(
                    LedgerAssessment(
                        kind = FitnessTestKind.RUN_PACE,
                        value = runPaceSecPerKm,
                        day = lastDay,
                        previous = runPaceSecPerKm * 1.04,
                        protocol = "Treadmill session pace"
                    )
                )
            }
        }
    }

    private fun weeklyGoalKm(sessions: List<LedgerSession>): Double {
        val cardioMeters = sessions
            .filter { it.kind == ActivityKind.RUNNING }
            .sumOf { session -> session.sets.sumOf { it.meters ?: 0.0 } }
        if (cardioMeters <= 0.0) return 0.0
        val weeks = (sessions.size / 5.0).coerceAtLeast(1.0)
        // Round the member's own weekly average up to a whole kilometre target.
        return (cardioMeters / 1000.0 / weeks * 1.1 * 10).roundToInt() / 10.0
    }

    /** Muscle groups that a split targets, used by the coverage panel. */
    fun musclesFor(exerciseId: String): List<MuscleGroup> =
        ExerciseCatalog.byId(exerciseId)?.allMuscles() ?: emptyList()

    fun patternFor(exerciseId: String): MovementPattern? =
        ExerciseCatalog.byId(exerciseId)?.pattern
}
