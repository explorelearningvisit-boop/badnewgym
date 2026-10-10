package com.example.badnewgym.feature.bodyprogress.data.repository

import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyDataConsent
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyGoalRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMeasurement
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import com.example.badnewgym.feature.bodyprogress.domain.model.DataSourceKind
import com.example.badnewgym.feature.bodyprogress.domain.model.Exercise
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalDirection
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.LoadUnit
import com.example.badnewgym.feature.bodyprogress.domain.model.MeasurementMethod
import com.example.badnewgym.feature.bodyprogress.domain.model.MovementPattern
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.VerificationState
import com.example.badnewgym.feature.bodyprogress.domain.model.ViewerRole
import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSessionRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSetRecord
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Development fixture source.
 *
 * FIXTURE ONLY - never used as a production data source. Deterministic
 * (seeded) so screenshots and tests are reproducible. Every record carries a
 * real method, unit, timestamp and verification state so analytics behave the
 * way they will against production data.
 */
class BodyProgressFixtureSource(
    private val memberId: String = "BG305",
    private val seed: Int = 11
) {

    private fun round1(v: Double): Double = (v * 10).toInt() / 10.0

    private fun day(anchor: LocalDate, back: Int): Long = anchor.minusDays(back.toLong()).toEpochDay()
    private fun millis(anchor: LocalDate, back: Int, hour: Int = 18, minute: Int = 30): Long =
        LocalDateTime.of(anchor.minusDays(back.toLong()), java.time.LocalTime.of(hour, minute))
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    /** Progressive loads: heavier over time, so progression is genuinely rising. */
    private fun liftProgression(baseKg: Double, stepKg: Double, sessions: Int, repPattern: List<Int>): List<List<Pair<Double, Int>>> =
        (0 until sessions).map { i ->
            val load = baseKg + stepKg * i
            List(3) { s -> load to repPattern[(i + s) % repPattern.size] }
        }

    private fun setOf(
        anchor: LocalDate, back: Int, sessionId: String, exercise: Exercise,
        index: Int, load: Double?, reps: Int?, unit: LoadUnit?,
        distanceMeters: Double? = null, durationSeconds: Int? = null,
        verification: VerificationState = VerificationState.SELF_REPORTED,
        source: DataSourceKind = DataSourceKind.MEMBER_ENTRY,
        correctsSetId: String? = null
    ) = WorkoutSetRecord(
        id = "set_${sessionId}_${exercise.id}_$index",
        sessionId = sessionId,
        exerciseId = exercise.id,
        setIndex = index,
        load = load, unit = unit, reps = reps,
        distanceMeters = distanceMeters, durationSeconds = durationSeconds,
        recordedAtEpoch = millis(anchor, back),
        verification = verification, source = source,
        correctsSetId = correctsSetId
    )

    fun dataset(anchor: LocalDate = LocalDate.now()): BodyProgressDataset {
        val catalog = com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
        fun ex(id: String) = catalog.byId(id)!!

        val bench = ex("bench_press_barbell")
        val latPulldown = ex("lat_pulldown")
        val curl = ex("biceps_curl_db")
        val squat = ex("back_squat")
        val legPress = ex("leg_press")
        val shoulderPress = ex("shoulder_press_db")
        val inclineDb = ex("incline_press_db")
        val pushdown = ex("triceps_pushdown")
        val rdl = ex("romanian_deadlift")
        val hipThrust = ex("hip_thrust")
        val calf = ex("calf_raise")
        val plank = ex("plank")
        val run = ex("treadmill_run")
        val deadlift = ex("deadlift")
        val row = ex("seated_row_cable")

        var sessions: List<WorkoutSessionRecord> = emptyList()

        // ---- 12 weeks of a real push/pull/legs split.
        //
        // `slot` counts from the most RECENT week backwards (0 = this week),
        // so a heavier load always belongs to a later date. Generating it the
        // other way round would make every progression read as a regression.
        val benchSets = liftProgression(40.0, 2.5, 12, listOf(8, 8, 6))
        val squatSets = liftProgression(50.0, 5.0, 12, listOf(6, 6, 5))
        val curlSets = liftProgression(12.0, 1.0, 12, listOf(12, 10, 10))
        val pulldownSets = liftProgression(35.0, 2.5, 12, listOf(10, 10, 8))
        val pressSets = liftProgression(14.0, 1.0, 12, listOf(10, 10, 8))
        val rdlSets = liftProgression(45.0, 2.5, 12, listOf(10, 8, 8))
        val pushdownSets = liftProgression(22.5, 1.25, 12, listOf(12, 12, 10))
        val WEEKS = 12
        /** Load set for a slot counted back from the present. */
        fun slot(back: Int): Int = WEEKS - 1 - (back / 7)

        for (slotIndex in 0 until WEEKS) {
            val back = 6 + (slotIndex * 7)
            if (back > 180) break
            val week = WEEKS - 1 - slotIndex

            // PUSH
            val pushId = "s_push_$week"
            sessions += WorkoutSessionRecord(
                id = pushId, memberId = memberId, epochDay = day(anchor, back),
                startedAtEpoch = millis(anchor, back, 18, 0),
                sets = buildList {
                    benchSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back, pushId, bench, i, load, reps, LoadUnit.KG))
                    }
                    inclineDb.let { ex ->
                        listOf(12.0 to 10, 10.0 to 12).forEachIndexed { i, (load, reps) ->
                            add(setOf(anchor, back, pushId, ex, i, load, reps, LoadUnit.PER_DUMBBELL))
                        }
                    }
                    pressSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back, pushId, shoulderPress, i, load, reps, LoadUnit.PER_DUMBBELL))
                    }
                    pushdownSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back, pushId, pushdown, i, load, reps, LoadUnit.KG))
                    }
                }
            )

            // PULL
            val pullId = "s_pull_$week"
            sessions += WorkoutSessionRecord(
                id = pullId, memberId = memberId, epochDay = day(anchor, back - 1),
                startedAtEpoch = millis(anchor, back - 1, 18, 0),
                sets = buildList {
                    pulldownSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 1, pullId, latPulldown, i, load, reps, LoadUnit.KG))
                    }
                    listOf(40.0 + week * 2.0 to 10, 40.0 + week * 2.0 to 10).forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 1, pullId, row, i, load, reps, LoadUnit.KG))
                    }
                    curlSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 1, pullId, curl, i, load, reps, LoadUnit.PER_DUMBBELL))
                    }
                }
            )

            // LEGS
            val legId = "s_leg_$week"
            sessions += WorkoutSessionRecord(
                id = legId, memberId = memberId, epochDay = day(anchor, back - 2),
                startedAtEpoch = millis(anchor, back - 2, 9, 0),
                sets = buildList {
                    squatSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 2, legId, squat, i, load, reps, LoadUnit.KG))
                    }
                    listOf(180.0 to 12, 180.0 to 12, 160.0 to 15).forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 2, legId, legPress, i, load, reps, LoadUnit.KG))
                    }
                    rdlSets[week].forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 2, legId, rdl, i, load, reps, LoadUnit.KG))
                    }
                    listOf(60.0 + week * 2.0 to 10).forEachIndexed { i, (load, reps) ->
                        add(setOf(anchor, back - 2, legId, hipThrust, i, load, reps, LoadUnit.KG))
                    }
                    val calfLoad = 45.0 + week
                    listOf(calfLoad to 15, calfLoad to 15, (calfLoad + 5) to 12)
                        .forEachIndexed { i, pair -> add(setOf(anchor, back - 2, legId, calf, i, pair.first, pair.second, LoadUnit.KG)) }
                }
            )
        }

        // ---- core + one conditioning day per week.
        // Slot 0 is the most recent week, so distance grows and pace falls over
        // time (higher week index = further back = slower, longer).
        for (slotIndex in 0 until 12) {
            val back = 4 + (slotIndex * 7)
            if (back > 180) break
            val week = 12 - 1 - slotIndex
            val coreId = "s_core_$week"
            sessions += WorkoutSessionRecord(
                id = coreId, memberId = memberId, epochDay = day(anchor, back),
                startedAtEpoch = millis(anchor, back, 19, 30),
                sets = buildList {
                    listOf(60, 75, 90).forEachIndexed { i, secs ->
                        add(setOf(anchor, back, coreId, plank, i, null, null, null, durationSeconds = secs))
                    }
                }
            )
            val runId = "s_run_$week"
            val meters = (3000 + week * 180).toDouble()
            val seconds = (meters * (6.35 - week * 0.03)).toInt()
            sessions += WorkoutSessionRecord(
                id = runId, memberId = memberId, epochDay = day(anchor, back),
                startedAtEpoch = millis(anchor, back, 7, 15),
                sets = listOf(
                    setOf(
                        anchor, back, runId, run, 0, null, null, null,
                        distanceMeters = meters, durationSeconds = seconds,
                        verification = VerificationState.VERIFIED,
                        source = DataSourceKind.INCLINOMETER
                    )
                )
            )
        }

        // ---- a genuine correction: an early heavy deadlift set was logged wrong.
        val wrongDeadlift = setOf(
            anchor, 70, "s_pull_9", deadlift, 0, 120.0, 5, LoadUnit.KG,
            verification = VerificationState.SELF_REPORTED
        )
        val correctedDeadlift = wrongDeadlift.copy(
            id = "set_corrected_deadlift",
            load = 110.0,
            recordedAtEpoch = millis(anchor, 69),
            verification = VerificationState.VERIFIED,
            source = DataSourceKind.STAFF_ENTRY,
            correctsSetId = wrongDeadlift.id
        )
        sessions = sessions.map { session ->
            if (session.id == "s_pull_9") session.copy(sets = session.sets + wrongDeadlift + correctedDeadlift)
            else session
        }

        // ---- body measurements: 8 points, genuine non-monotonic shape
        // Realistic, deliberately non-monotonic body-composition history.
        // Weights trend up for a muscle-gain phase; body fat falls as a
        // fraction of the same body weight, which is physically consistent.
        data class Reading(val back: Int, val weightKg: Double, val bodyFatPct: Double)

        val measurementScript = listOf(
            Reading(175, 67.4, 18.6), Reading(154, 67.8, 18.4), Reading(133, 67.6, 18.5),
            Reading(112, 68.4, 18.1), Reading(91, 68.3, 18.2), Reading(70, 69.0, 17.8),
            Reading(49, 68.9, 17.9), Reading(21, 69.5, 17.6)
        )
        val measurements = buildList {
            measurementScript.forEach { reading ->
                val fatMass = reading.weightKg * reading.bodyFatPct / 100.0
                val leanMass = reading.weightKg - fatMass
                add(
                    BodyMeasurement(
                        id = "m_w_${reading.back}", memberId = memberId, metric = BodyMetric.WEIGHT,
                        value = reading.weightKg, unit = "kg",
                        measuredAtEpoch = millis(anchor, reading.back, 7, 30),
                        method = MeasurementMethod.DIRECT_SCALE, source = DataSourceKind.SCALE,
                        verification = VerificationState.VERIFIED
                    )
                )
                add(
                    BodyMeasurement(
                        id = "m_bf_${reading.back}", memberId = memberId, metric = BodyMetric.BODY_FAT,
                        value = reading.bodyFatPct, unit = "%",
                        measuredAtEpoch = millis(anchor, reading.back, 7, 32),
                        method = MeasurementMethod.BIA, source = DataSourceKind.SCALE,
                        verification = VerificationState.ESTIMATED
                    )
                )
                // Muscle mass is derived from the same scale reading, so it stays
                // physically consistent with weight and fat instead of drifting.
                add(
                    BodyMeasurement(
                        id = "m_mm_${reading.back}", memberId = memberId, metric = BodyMetric.MUSCLE_MASS,
                        value = round1(leanMass), unit = "kg",
                        measuredAtEpoch = millis(anchor, reading.back, 7, 33),
                        method = MeasurementMethod.BIA, source = DataSourceKind.SCALE,
                        verification = VerificationState.ESTIMATED
                    )
                )
                add(
                    BodyMeasurement(
                        id = "m_fm_${reading.back}", memberId = memberId, metric = BodyMetric.FAT_MASS,
                        value = round1(fatMass), unit = "kg",
                        measuredAtEpoch = millis(anchor, reading.back, 7, 34),
                        method = MeasurementMethod.BIA, source = DataSourceKind.SCALE,
                        verification = VerificationState.ESTIMATED
                    )
                )
            }
        }

        // 5K times: newer tests are faster, so a later date holds a lower time.
        val fitnessTests = buildList {
            for (slotIndex in 0 until 8) {
                val back = 6 + slotIndex * 7
                val week = 8 - 1 - slotIndex
                add(
                    FitnessTestRecord(
                        id = "t_5k_$week", memberId = memberId, kind = FitnessTestKind.FIVE_K_TIME,
                        value = 26.3 + week * 0.35, unit = "min",
                        performedAtEpoch = millis(anchor, back, 6, 40),
                        verification = VerificationState.VERIFIED,
                        source = DataSourceKind.STAFF_ENTRY, protocol = "treadmill-5k"
                    )
                )
            }
        }

        val goals = listOf(
            BodyGoalRecord(
                id = "g_muscle", memberId = memberId, title = "Muscle Gain",
                // Start/target match the first and last scale readings, and the
                // direction is explicit: weight goes UP for this goal.
                metric = GoalMetric.WEIGHT, startValue = 67.4, targetValue = 72.0,
                startedAtEpoch = millis(anchor, 175),
                direction = GoalDirection.INCREASE
            ),
            BodyGoalRecord(
                id = "g_fat", memberId = memberId, title = "Body Fat Reduction",
                metric = GoalMetric.BODY_FAT, startValue = 18.6, targetValue = 15.0,
                startedAtEpoch = millis(anchor, 175),
                direction = GoalDirection.DECREASE
            ),
            BodyGoalRecord(
                id = "g_endurance", memberId = memberId, title = "Endurance",
                metric = GoalMetric.FIVE_K_TIME, startValue = 28.5, targetValue = 25.0,
                startedAtEpoch = millis(anchor, 120)
            )
        )

        return BodyProgressDataset(
            memberId = memberId,
            sessions = sessions.sortedByDescending { it.epochDay },
            measurements = measurements,
            fitnessTests = fitnessTests,
            goals = goals,
            consent = BodyDataConsent(
                memberId = memberId,
                allowsBodyComposition = true,
                allowsProgressPhotos = true,
                visibleTo = setOf(ViewerRole.SELF, ViewerRole.OWNER, ViewerRole.STAFF)
            ),
            anchor = anchor
        )
    }

    /** A member with workout records but no body measurements - honest empty body state. */
    fun workoutOnlyDataset(anchor: LocalDate = LocalDate.now()): BodyProgressDataset {
        val full = dataset(anchor)
        return full.copy(
            measurements = emptyList(),
            goals = full.goals.filter { it.metric == GoalMetric.STRENGTH }
                .map { it.copy(metric = GoalMetric.STRENGTH) }
        )
    }

    /** A member with almost nothing recorded - exercises the empty states. */
    fun sparseDataset(anchor: LocalDate = LocalDate.now()): BodyProgressDataset =
        BodyProgressDataset(
            memberId = "BG999",
            sessions = emptyList(),
            measurements = emptyList(),
            fitnessTests = emptyList(),
            goals = listOf(
                BodyGoalRecord(
                    id = "g_fitness", memberId = "BG999", title = "General Fitness",
                    metric = GoalMetric.ENDURANCE, startValue = 0.0, targetValue = null,
                    startedAtEpoch = anchor.toEpochDay() * 86_400_000L,
                    note = "No measurable target configured yet"
                )
            ),
            consent = BodyDataConsent("BG999"),
            anchor = anchor
        )
}

/** Range labels shared by both surfaces. */
val BODY_RANGE_LABELS: List<Pair<BodyRange, String>> = BodyRange.entries.map { it to it.label }
