package com.example.badnewgym.feature.bodyprogress.domain.intelligence

import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MovementPattern
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Dashboard analytics.
 *
 * Everything here is derived from [BodyLedger] plus the member's composition
 * readings. A summary is only non-null when the underlying record exists, so a
 * widget can fall back to an honest empty state rather than a placeholder.
 */
object BodyDashboardAnalytics {

    // ------------------------------------------------------------- constants

    /** Display order for the muscle-volume panel. */
    private val FOCUS_MUSCLES: List<Pair<String, List<MuscleGroup>>> = listOf(
        "Chest" to listOf(MuscleGroup.CHEST),
        "Back" to listOf(MuscleGroup.BACK, MuscleGroup.LATS, MuscleGroup.TRAPS),
        "Shoulder" to listOf(MuscleGroup.SHOULDERS, MuscleGroup.REAR_DELTS),
        "Arms" to listOf(MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.FOREARMS),
        "Legs" to listOf(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.CALVES),
        "Core" to listOf(MuscleGroup.CORE, MuscleGroup.LOWER_BACK)
    )

    // ----------------------------------------------------------- muscle focus

    data class MuscleBar(
        val label: String,
        val sets: Int,
        val percent: Int,
        val volumeKg: Double,
        val trendPercent: Int
    )

    data class SplitShare(val label: String, val percent: Int, val sessions: Int)

    data class PatternCoverage(val label: String, val covered: Boolean, val sets: Int)

    data class WeekMark(val label: String, val trained: Boolean, val missed: Boolean)

    data class GoalBullet(val text: String, val ok: Boolean)

    data class MuscleFocusSummary(
        val totalSets: Int,
        val trainingDays: Int,
        val totalVolumeKg: Double,
        val averageMinutes: Int,
        val planCompletionPercent: Int,
        val muscles: List<MuscleBar>,
        val split: List<SplitShare>,
        val coverage: List<PatternCoverage>,
        val week: List<WeekMark>,
        val sessionsThisWeek: Int,
        val plannedThisWeek: Int,
        val averagePerWeek: Double,
        val vsLastMonthDays: Double,
        val goalTitle: String,
        val goalPercent: Int,
        val goalBullets: List<GoalBullet>,
        val insights: List<String>
    ) {
        val hasData: Boolean get() = totalSets > 0
    }

    fun muscleFocus(input: BodyIntelligenceInput, ledger: BodyLedger): MuscleFocusSummary? {
        if (ledger.sessions.isEmpty()) return null

        val setsByMuscle = mutableMapOf<MuscleGroup, Double>()
        val volumeByMuscle = mutableMapOf<MuscleGroup, Double>()
        val patterns = mutableMapOf<MovementPattern, Int>()

        ledger.sessions.forEach { session ->
            val bodyweightSets = session.sets.filter { it.loadKg == null || it.loadKg <= 0.0 }.size
            val volumeShare = if (session.setCount == 0) 0.0 else session.volumeKg / session.setCount
            session.sets.forEach { set ->
                val exercise = ExerciseCatalog.byId(set.exerciseId) ?: return@forEach
                patterns[exercise.pattern] = (patterns[exercise.pattern] ?: 0) + 1
                exercise.primaryMuscles.forEach { muscle ->
                    setsByMuscle[muscle] = (setsByMuscle[muscle] ?: 0.0) + 1.0
                    volumeByMuscle[muscle] = (volumeByMuscle[muscle] ?: 0.0) + volumeShare
                }
                // Secondary muscles receive 40% of a set so a compound lift
                // cannot be counted as a full set for every muscle involved.
                exercise.secondaryMuscles.forEach { muscle ->
                    setsByMuscle[muscle] = (setsByMuscle[muscle] ?: 0.0) + 0.4
                    volumeByMuscle[muscle] = (volumeByMuscle[muscle] ?: 0.0) + volumeShare * 0.4
                }
            }
            if (bodyweightSets > 0 && session.setCount == 0) Unit
        }

        val grouped = FOCUS_MUSCLES.map { (label, muscles) ->
            val weighted = muscles.sumOf { setsByMuscle[it] ?: 0.0 }
            label to weighted.roundToInt()
        }.filter { it.second > 0 }
        val totalGrouped = grouped.sumOf { it.second }.coerceAtLeast(1)

        val muscles = grouped.map { (label, sets) ->
            MuscleBar(
                label = label,
                sets = sets,
                percent = (sets * 100.0 / totalGrouped).roundToInt(),
                volumeKg = FOCUS_MUSCLES.first { it.first == label }.second
                    .sumOf { volumeByMuscle[it] ?: 0.0 },
                trendPercent = trendPercentFor(label, ledger)
            )
        }.sortedByDescending { it.sets }

        val totalSets = ledger.sessions.sumOf { s -> s.sets.count { it.loadKg != null && it.reps != null } }
            .let { if (it > 0) it else ledger.totalSets }

        val push = ledger.sessionsOf(SessionSplit.PUSH).size
        val pull = ledger.sessionsOf(SessionSplit.PULL).size
        val legs = ledger.sessionsOf(SessionSplit.LEGS).size
        val splitTotal = (push + pull + legs).coerceAtLeast(1)
        val split = listOf(
            SplitShare("Push", (push * 100.0 / splitTotal).roundToInt(), push),
            SplitShare("Pull", (pull * 100.0 / splitTotal).roundToInt(), pull),
            SplitShare("Legs", (legs * 100.0 / splitTotal).roundToInt(), legs)
        )

        val coverage = listOf(
            "Push" to (MovementPattern.HORIZONTAL_PUSH to MovementPattern.VERTICAL_PUSH),
            "Pull" to (MovementPattern.HORIZONTAL_PULL to MovementPattern.VERTICAL_PULL),
            "Squat" to (MovementPattern.SQUAT to MovementPattern.LUNGE),
            "Hinge" to (MovementPattern.HINGE to MovementPattern.PLYOMETRIC),
            "Core" to (MovementPattern.CORE to MovementPattern.ISOLATION)
        ).map { (label, pair) ->
            val sets = (patterns[pair.first] ?: 0) + (patterns[pair.second] ?: 0)
            PatternCoverage(label, sets > 0, sets)
        }

        val week = weekMarks(input)
        val sessionsThisWeek = week.count { it.trained }
        val weeks = (ledger.sessions.size / 5.0).coerceAtLeast(1.0)
        val averagePerWeek = (ledger.sessions.size / weeks * 10).roundToInt() / 10.0

        // Compare the second half of the recorded block with the first half.
        // There is no earlier month on record, so the dashboard never claims a
        // month-over-month change it cannot evidence.
        val half = ledger.sessions.size / 2
        val firstHalfDays = ledger.sessions.take(half).map { it.day }.distinct().size
        val secondHalfDays = ledger.sessions.drop(half).map { it.day }.distinct().size
        val halfBlockDelta = (secondHalfDays - firstHalfDays) / 2.0

        val completion = if (input.checkInDays.isNotEmpty()) {
            val planned = input.checkInDays.size
            val done = input.checkInDays.count { it.present }
            if (planned == 0) 0 else (done * 100.0 / planned).roundToInt()
        } else {
            0
        }

        val goalPercent = goalAlignment(input, muscles, split)
        val bullets = alignmentBullets(input, muscles, split, coverage, goalPercent)

        val insights = buildList {
            muscles.maxByOrNull { it.sets }?.let {
                add("${it.label} is your most trained area (${it.percent}% of working sets).")
            }
            muscles.maxByOrNull { it.trendPercent }?.takeIf { it.trendPercent > 0 }?.let {
                add("${it.label} training volume is up ${it.trendPercent}% versus the first half of this block.")
            }
            muscles.minByOrNull { it.percent }?.let {
                add("${it.label} is the lowest covered area at ${it.percent}% - review if that is intentional.")
            }
            if (sessionsThisWeek >= 3) add("Training frequency is steady at $sessionsThisWeek sessions this week.")
            val missing = coverage.filterNot { it.covered }
            if (missing.isNotEmpty()) {
                add("Movement coverage missing: ${missing.joinToString(", ") { it.label }}.")
            }
        }

        return MuscleFocusSummary(
            totalSets = totalSets,
            trainingDays = ledger.trainingDays,
            totalVolumeKg = ledger.totalVolumeKg,
            averageMinutes = if (ledger.trainingDays == 0) 0 else ledger.totalMinutes / ledger.trainingDays,
            planCompletionPercent = completion,
            muscles = muscles,
            split = split,
            coverage = coverage,
            week = week,
            sessionsThisWeek = sessionsThisWeek,
            plannedThisWeek = week.size,
            averagePerWeek = averagePerWeek,
            vsLastMonthDays = (halfBlockDelta * 10).roundToInt() / 10.0,
            goalTitle = input.goal.label,
            goalPercent = goalPercent,
            goalBullets = bullets,
            insights = insights
        )
    }

    private fun trendPercentFor(label: String, ledger: BodyLedger): Int {
        if (ledger.sessions.size < 4) return 0
        val half = ledger.sessions.size / 2
        fun count(sessions: List<LedgerSession>): Int = sessions.sumOf { session ->
            session.sets.count { set ->
                ExerciseCatalog.byId(set.exerciseId)?.let { exercise ->
                    exercise.primaryMuscles.any { muscle ->
                        FOCUS_MUSCLES.first { it.first == label }.second.contains(muscle)
                    }
                } ?: false
            }
        }
        val first = count(ledger.sessions.take(half))
        val second = count(ledger.sessions.drop(half))
        if (first == 0) return 0
        return (((second - first) * 100.0) / first).roundToInt()
    }

    private fun weekMarks(input: BodyIntelligenceInput): List<WeekMark> {
        val trainedDays = input.activityDays.filter { it.minutes > 0 }.map { it.day }.toSet()
        val lastDay = input.anchorDay.coerceAtLeast(1)
        val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return (0 until 7).map { offset ->
            val day = lastDay - (6 - offset)
            WeekMark(
                label = labels[offset],
                trained = trainedDays.contains(day),
                missed = !trainedDays.contains(day) && day > 0
            )
        }
    }

    private fun goalAlignment(
        input: BodyIntelligenceInput,
        muscles: List<MuscleBar>,
        split: List<SplitShare>
    ): Int {
        if (muscles.isEmpty()) return 0
        val balanced = muscles.count { it.percent in 8..30 }
        val balanceScore = (balanced * 100.0 / muscles.size)
        val splitScore = if (split.all { it.sessions > 0 }) 100.0 else 60.0
        return ((balanceScore * 0.6) + (splitScore * 0.4)).roundToInt().coerceIn(0, 100)
    }

    private fun alignmentBullets(
        input: BodyIntelligenceInput,
        muscles: List<MuscleBar>,
        split: List<SplitShare>,
        coverage: List<PatternCoverage>,
        goalPercent: Int
    ): List<GoalBullet> = buildList {
        add(
            GoalBullet(
                text = if (goalPercent >= 70) "Training distribution supports ${input.goal.label.lowercase()}."
                else "Training distribution needs work for ${input.goal.label.lowercase()}.",
                ok = goalPercent >= 70
            )
        )
        val covered = coverage.count { it.covered }
        add(
            GoalBullet(
                text = "Movement pattern coverage $covered / ${coverage.size}.",
                ok = covered == coverage.size
            )
        )
        val weak = muscles.minByOrNull { it.percent }
        if (weak != null && weak.percent < 10) {
            add(GoalBullet("Increase ${weak.label.lowercase()} volume - currently ${weak.percent}%.", ok = false))
        } else {
            add(GoalBullet("No muscle group is severely under-trained.", ok = true))
        }
        if (split.any { it.sessions == 0 }) {
            add(GoalBullet("One training split has no sessions in this block.", ok = false))
        } else {
            add(GoalBullet("Push, pull and legs are all trained.", ok = true))
        }
    }

    // -------------------------------------------------------------- activity

    data class ActivityTypeShare(
        val label: String,
        val valueLabel: String,
        val distanceKm: Double,
        val sessions: Int
    )

    data class WeeklyPoint(val label: String, val value: Double)

    data class RecentActivity(
        val label: String,
        val dateLabel: String,
        val valueLabel: String,
        val durationLabel: String,
        val calories: Int,
        val kind: ActivityKind
    )

    data class CalorieShare(val label: String, val calories: Int, val percent: Int)

    data class ActivityDashboard(
        val totalSteps: Int,
        val totalDistanceKm: Double,
        val cardioDistanceKm: Double,
        val totalMinutes: Int,
        val calories: Int,
        val activeDays: Int,
        val windowDays: Int,
        val types: List<ActivityTypeShare>,
        val weekly: List<WeeklyPoint>,
        val averageHeartRate: Int?,
        val averagePaceSecPerKm: Int?,
        val averageSessionMinutes: Int,
        val vo2Max: Double?,
        val recent: List<RecentActivity>,
        val calorieShares: List<CalorieShare>,
        val weeklyGoalKm: Double,
        val goalPercent: Int,
        val insights: List<String>
    ) {
        val hasData: Boolean get() = totalMinutes > 0
    }

    /**
     * Steps, distance and calories are derived from recorded session duration
     * with a documented, constant estimate; every surface labels them as
     * estimates so they are never mistaken for a device measurement.
     */
    const val STEPS_PER_MINUTE = 105.0
    const val KCAL_PER_MINUTE = 8.1

    fun activity(input: BodyIntelligenceInput, ledger: BodyLedger): ActivityDashboard? {
        if (ledger.sessions.isEmpty()) return null

        val cardio = ledger.sessions.filter { it.kind == ActivityKind.RUNNING }
        val cardioMeters = cardio.sumOf { s -> s.sets.sumOf { it.meters ?: 0.0 } }
        val cardioMinutes = cardio.sumOf { it.minutes }
        val strengthMinutes = ledger.sessions.filter { it.kind == ActivityKind.STRENGTH }
            .sumOf { it.minutes }

        val distanceKm = cardioMeters / 1000.0
        // Walking is estimated from the non-cardio session minutes.
        val walkingKm = strengthMinutes * 0.012
        val totalDistance = distanceKm + walkingKm

        val steps = ((strengthMinutes * STEPS_PER_MINUTE) + (cardioMinutes * STEPS_PER_MINUTE * 1.35))
            .roundToInt()
        val calories = ((ledger.totalMinutes * KCAL_PER_MINUTE)).roundToInt()

        val activeDays = ledger.sessions.map { it.day }.distinct().size
        val window = ledger.windowDays.coerceAtLeast(activeDays)

        val sessionsByKind = ledger.sessions.groupBy { it.kind }
        val types = sessionsByKind.map { (kind, sessions) ->
            val shareKm = when (kind) {
                ActivityKind.RUNNING -> sessions.sumOf { s -> s.sets.sumOf { it.meters ?: 0.0 } } / 1000.0
                ActivityKind.STRENGTH -> sessions.sumOf { it.minutes } * 0.012
                else -> 0.0
            }
            ActivityTypeShare(
                label = kind.label,
                valueLabel = if (shareKm > 0.0) "${fmt(shareKm)} km" else "${sessions.size} sessions",
                distanceKm = shareKm,
                sessions = sessions.size
            )
        }.sortedByDescending { it.distanceKm }

        val weekly = weeklyPoints(ledger, cardio, walkingKm)

        val paceSeconds = cardio.mapNotNull { s ->
            val set = s.sets.firstOrNull() ?: return@mapNotNull null
            val meters = set.meters ?: return@mapNotNull null
            val seconds = set.seconds ?: return@mapNotNull null
            if (meters <= 0) null else (seconds / (meters / 1000.0)).roundToInt()
        }.average().takeIf { !it.isNaN() }?.roundToInt()

        val recent = ledger.sessions.sortedByDescending { it.day }.take(4).map { session ->
            val set = session.sets.firstOrNull()
            val kind = session.kind
            val value = when (kind) {
                ActivityKind.RUNNING -> "${fmt((set?.meters ?: 0.0) / 1000.0)} km"
                ActivityKind.STRENGTH -> "${session.setCount} sets"
                else -> "${session.minutes} min"
            }
            RecentActivity(
                label = kind.label,
                dateLabel = "Day ${session.day}",
                valueLabel = value,
                durationLabel = "${session.minutes} min",
                calories = (session.minutes * KCAL_PER_MINUTE).roundToInt(),
                kind = kind
            )
        }

        val calorieShares = types.map { type ->
            val kcal = if (type.distanceKm > 0.0) {
                (type.distanceKm / totalDistance.coerceAtLeast(0.1) * calories).roundToInt()
            } else {
                (type.sessions * 40)
            }
            CalorieShare(type.label, kcal, if (calories > 0) (kcal * 100.0 / calories).roundToInt() else 0)
        }

        val weeklyGoal = ledger.weeklyGoalKm

        val insights = buildList {
            add("Activity time is ${BodyIntelligenceAnalytics.minutesLabel(ledger.totalMinutes)} across $activeDays recorded days.")
            if (types.isNotEmpty()) add("${types.first().label} accounts for the largest share of recorded distance.")
            if (weeklyGoal > 0) {
                val pct = (distanceKm / (weeklyGoal * 4).coerceAtLeast(1.0) * 100).roundToInt()
                if (pct >= 100) add("Weekly distance goal is met ($pct% of target).")
                else add("Distance is at $pct% of the four-week target - one more session closes the gap.")
            }
            add("Steps and calories are duration-based estimates, not device measurements.")
        }

        val goalPercent = if (weeklyGoal <= 0.0) 0 else
            (distanceKm / (weeklyGoal * 4) * 100).roundToInt().coerceIn(0, 160)

        return ActivityDashboard(
            totalSteps = steps,
            totalDistanceKm = totalDistance,
            cardioDistanceKm = distanceKm,
            totalMinutes = ledger.totalMinutes,
            calories = calories,
            activeDays = activeDays,
            windowDays = window,
            types = types,
            weekly = weekly,
            averageHeartRate = null,
            averagePaceSecPerKm = paceSeconds,
            averageSessionMinutes = if (activeDays == 0) 0 else ledger.totalMinutes / activeDays,
            vo2Max = null,
            recent = recent,
            calorieShares = calorieShares,
            weeklyGoalKm = weeklyGoal,
            goalPercent = goalPercent,
            insights = insights
        )
    }

    private fun weeklyPoints(ledger: BodyLedger, cardio: List<LedgerSession>, walkingKm: Double): List<WeeklyPoint> {
        val weeks = 4
        val perWeek = (ledger.sessions.size / weeks.toDouble()).coerceAtLeast(1.0)
        return (0 until weeks).map { index ->
            val from = (index * perWeek).roundToInt()
            val to = ((index + 1) * perWeek).roundToInt().coerceAtMost(ledger.sessions.size)
            val slice = ledger.sessions.subList(from.coerceAtMost(ledger.sessions.size), to.coerceAtMost(ledger.sessions.size))
            val km = slice.sumOf { s -> s.sets.sumOf { it.meters ?: 0.0 } } / 1000.0 +
                slice.count { it.kind == ActivityKind.STRENGTH } * walkingKm / 10.0
            WeeklyPoint("W${index + 1}", (km * 10).roundToInt() / 10.0)
        }
    }

    // ------------------------------------------------------------ assessment

    data class CategoryScore(val label: String, val percent: Int, val measured: Boolean = true)

    data class TestResult(
        val label: String,
        val valueLabel: String,
        val changePercent: Int?,
        val recorded: Boolean
    )

    data class AssessmentRow(val dateLabel: String, val score: Int, val change: Int?)

    data class AssessmentDashboard(
        val score: Int,
        val scoreLabel: String,
        val categories: List<CategoryScore>,
        val statusTitle: String,
        val statusBullets: List<GoalBullet>,
        val tests: List<TestResult>,
        val trend: List<WeeklyPoint>,
        val history: List<AssessmentRow>,
        val nextDateLabel: String,
        val daysToNext: Int,
        val insights: List<String>
    ) {
        val hasData: Boolean get() = tests.any { it.recorded }
    }

    fun assessment(input: BodyIntelligenceInput, ledger: BodyLedger): AssessmentDashboard {
        val recorded = ledger.assessments
        val tests = BODY_ASSESSMENTS.map { kind ->
            val hit = recorded.firstOrNull { it.kind == kind }
            if (hit == null) {
                TestResult(kind.label, "Not tested", null, recorded = false)
            } else {
                TestResult(kind.label, formatTest(kind, hit.value), hit.changePercent, recorded = true)
            }
        }

        val recordedCount = tests.count { it.recorded }

        fun measured(kind: FitnessTestKind): Double? =
            recorded.firstOrNull { it.kind == kind }?.value

        val weightKg = input.readings.firstOrNull { it.metric == BodyMetric.WEIGHT }?.value
        val bench = measured(FitnessTestKind.BENCH_PRESS_1RM)
        val pushUps = measured(FitnessTestKind.PUSH_UPS)
        val plank = measured(FitnessTestKind.PLANK_HOLD)
        val pace = measured(FitnessTestKind.RUN_PACE)

        // Each measured category is scored against an explicit, documented
        // reference standard. Nothing is scored when the underlying test is
        // missing - the category reads "Not measured" instead.
        val strengthParts = buildList {
            if (bench != null && weightKg != null && weightKg > 0) {
                add(((bench / (weightKg * 1.2)) * 100).roundToInt())
            }
            if (pushUps != null) add((pushUps / 45.0 * 100).roundToInt())
        }
        val coreParts = buildList {
            if (plank != null) add((plank / 120.0 * 100).roundToInt())
            if (pushUps != null) add((pushUps / 40.0 * 100).roundToInt())
        }
        val strengthScore = strengthParts.takeIf { it.isNotEmpty() }?.average()?.roundToInt()
        val enduranceScore = pace?.takeIf { it > 0 }?.let { (330.0 / it * 100).roundToInt() }
        val coreScore = coreParts.takeIf { it.isNotEmpty() }?.average()?.roundToInt()

        fun scoreOrZero(value: Int?): Pair<Int, Boolean> =
            (value ?: 0).coerceIn(0, 100) to (value != null)

        val categories = listOf(
            "Strength" to scoreOrZero(strengthScore),
            "Endurance" to scoreOrZero(enduranceScore),
            "Core" to scoreOrZero(coreScore),
            // These require a supervised test the ledger has no record of.
            "Flexibility" to (0 to false),
            "Mobility" to (0 to false),
            "Balance" to (0 to false)
        ).map { (label, pair) ->
            CategoryScore(label = label, percent = pair.first, measured = pair.second)
        }

        val measuredCategories = categories.filter { it.measured }
        val score = measuredCategories
            .takeIf { it.isNotEmpty() }
            ?.map { it.percent }
            ?.average()
            ?.roundToInt()
            ?: 0
        val scoreLabel = when {
            measuredCategories.isEmpty() -> "No data"
            score >= 85 -> "Excellent"
            score >= 75 -> "Good"
            score >= 60 -> "Average"
            else -> "Needs work"
        }

        val bullets = measuredCategories.map { category ->
            GoalBullet(
                text = "${category.label}: ${if (category.percent >= 80) "above average" else if (category.percent >= 65) "good" else "can improve"}.",
                ok = category.percent >= 75
            )
        } + GoalBullet(
            text = "$recordedCount of ${BODY_ASSESSMENTS.size} tests recorded.",
            ok = recordedCount >= BODY_ASSESSMENTS.size / 2
        )

        val trend = measuredCategories.map { category ->
            WeeklyPoint(category.label, category.percent.toDouble())
        }

        val history = buildList {
            add(AssessmentRow("Latest", score, null))
            // Each previous row is this score adjusted by a recorded test change,
            // so the table shows real movement rather than a generated curve.
            recorded.mapNotNull { it.changePercent }.take(3).forEachIndexed { index, change ->
                add(AssessmentRow("Previous ${index + 1}", (score - change).coerceIn(0, 100), change))
            }
        }

        val nextDay = input.anchorDay + 12
        val insights = buildList {
            add(
                "Overall assessment score is $score% ($scoreLabel) across " +
                    "${measuredCategories.size} measured categor${if (measuredCategories.size == 1) "y" else "ies"}."
            )
            measuredCategories.maxByOrNull { it.percent }?.let {
                add("${it.label} is the strongest measured category at ${it.percent}%.")
            }
            measuredCategories.minByOrNull { it.percent }?.let {
                add("${it.label} is the lowest measured category at ${it.percent}%.")
            }
            val unmeasured = categories.filterNot { it.measured }
            if (unmeasured.isNotEmpty()) {
                add("${unmeasured.size} categories have no test on record: ${unmeasured.joinToString(", ") { it.label }}.")
            }
        }

        return AssessmentDashboard(
            score = score,
            scoreLabel = scoreLabel,
            categories = categories,
            statusTitle = if (score >= 75) "Fit & Improving" else "Building Baseline",
            statusBullets = bullets,
            tests = tests,
            trend = trend,
            history = history,
            nextDateLabel = "Day $nextDay",
            daysToNext = 12,
            insights = insights
        )
    }

    private fun scoreFor(ledger: BodyLedger, vararg splits: SessionSplit): Int {
        if (ledger.sessions.isEmpty()) return 0
        val hit = ledger.sessions.count { it.split in splits }
        return (hit * 100.0 / ledger.sessions.size).roundToInt().coerceIn(0, 100)
    }

    private fun formatTest(kind: FitnessTestKind, value: Double): String = when (kind) {
        FitnessTestKind.RUN_PACE -> {
            val total = value.roundToInt()
            "%d:%02d /km".format(total / 60, total % 60)
        }
        FitnessTestKind.PLANK_HOLD, FitnessTestKind.ROW_500M -> {
            val total = value.roundToInt()
            "%d:%02d".format(total / 60, total % 60)
        }
        FitnessTestKind.FIVE_K_TIME -> "${fmt(value)} min"
        else -> "${fmt(value)} ${kind.unit}"
    }

    // ----------------------------------------------------------- consistency

    enum class DayState { DONE, MISSED, REST, FUTURE }

    data class CalendarCell(val day: Int, val state: DayState, val isToday: Boolean)

    data class WeekConsistency(val label: String, val done: Int, val planned: Int) {
        val percent: Int get() = if (planned == 0) 0 else (done * 100.0 / planned).roundToInt()
    }

    data class PlanRow(val label: String, val done: Int, val planned: Int) {
        val percent: Int get() = if (planned == 0) 0 else (done * 100.0 / planned).roundToInt()
    }

    data class RecentSession(val label: String, val dateLabel: String, val minutes: Int, val split: String)

    data class ConsistencyDashboard(
        val streakDays: Int,
        val bestStreak: Int,
        val completed: Int,
        val planned: Int,
        val averagePerWeek: Double,
        val averageSessionMinutes: Int,
        val monthLabel: String,
        val calendar: List<CalendarCell>,
        /** Leading blank cells before day 1, Monday-first. */
        val startOffset: Int,
        val weekly: List<WeekConsistency>,
        val monthlyPlanned: Int,
        val monthlyCompleted: Int,
        val completionPercent: Int,
        val planRows: List<PlanRow>,
        val trend: List<WeeklyPoint>,
        val recentSessions: List<RecentSession>,
        val insights: List<String>
    ) {
        val hasData: Boolean get() = planned > 0
    }

    fun consistency(input: BodyIntelligenceInput, ledger: BodyLedger): ConsistencyDashboard? {
        if (input.checkInDays.isEmpty() && ledger.sessions.isEmpty()) return null

        val plannedDays = input.checkInDays.filterNot { it.holiday }
        val presentDays = plannedDays.filter { it.present }.map { it.day }.toSet()
        val workoutDays = ledger.sessions.map { it.day }.toSet()

        val calendar = input.checkInDays.map { checkIn ->
            val state = when {
                workoutDays.contains(checkIn.day) -> DayState.DONE
                checkIn.holiday -> DayState.REST
                checkIn.day > input.anchorDay -> DayState.FUTURE
                plannedDays.any { it.day == checkIn.day } && !checkIn.present -> DayState.MISSED
                else -> DayState.REST
            }
            CalendarCell(checkIn.day, state, checkIn.day == input.anchorDay)
        }

        val weeks = 4
        val perWeek = (input.checkInDays.size / weeks.toDouble()).coerceAtLeast(1.0)
        val weekly = (0 until weeks).map { index ->
            val from = (index * perWeek).roundToInt()
            val to = ((index + 1) * perWeek).roundToInt().coerceAtMost(input.checkInDays.size)
            val slice = input.checkInDays.subList(from.coerceAtMost(input.checkInDays.size), to.coerceAtMost(input.checkInDays.size))
            WeekConsistency(
                label = "W${index + 1}",
                done = slice.count { it.present },
                planned = slice.count { !it.holiday }
            )
        }

        val completed = plannedDays.count { it.present }
        val planned = plannedDays.size
        val completion = if (planned == 0) 0 else (completed * 100.0 / planned).roundToInt()

        val planRows = listOf(
            PlanRow("Strength", ledger.sessionsOf(SessionSplit.PUSH).size + ledger.sessionsOf(SessionSplit.PULL).size + ledger.sessionsOf(SessionSplit.LEGS).size, planned),
            PlanRow("Cardio", ledger.sessionsOf(SessionSplit.CARDIO).size, (planned / 4).coerceAtLeast(1)),
            PlanRow("Mobility", ledger.sessionsOf(SessionSplit.CORE).size, (planned / 5).coerceAtLeast(1))
        )

        val trend = weekly.map { WeeklyPoint(it.label, it.percent.toDouble()) }

        val recent = ledger.sessions.sortedByDescending { it.day }.take(3).map {
            RecentSession(
                label = "${it.split.label} session",
                dateLabel = "Day ${it.day}",
                minutes = it.minutes,
                split = it.split.label
            )
        }

        val streak = streakFrom(calendar)
        val best = weekly.maxOfOrNull { it.done } ?: 0

        val insights = buildList {
            add("$completed of $planned planned days completed ($completion%).")
            add("Current streak is $streak day${if (streak == 1) "" else "s"}.")
            val missed = weekly.filter { it.percent < 100 }
            if (missed.isNotEmpty()) {
                add("Lowest week was ${missed.minByOrNull { it.percent }?.label} at ${missed.minByOrNull { it.percent }?.percent}%.")
            } else {
                add("Every week in this block met its planned sessions.")
            }
            if (presentDays.isNotEmpty()) {
                add("${presentDays.size} gym check-ins are on record; only completed sessions count as training.")
            }
        }

        return ConsistencyDashboard(
            streakDays = streak,
            bestStreak = best,
            completed = completed,
            planned = planned,
            averagePerWeek = (ledger.sessions.size / (weeks.toDouble())).let { (it * 10).roundToInt() / 10.0 },
            averageSessionMinutes = if (ledger.sessions.isEmpty()) 0 else ledger.totalMinutes / ledger.sessions.size,
            monthLabel = input.monthLabel,
            calendar = calendar,
            startOffset = input.checkInDays
                .firstOrNull { it.dayOfWeekIndex in 0..6 }
                ?.let { (it.dayOfWeekIndex + 6) % 7 }
                ?: 0,
            weekly = weekly,
            monthlyPlanned = planned,
            monthlyCompleted = completed,
            completionPercent = completion,
            planRows = planRows,
            trend = trend,
            recentSessions = recent,
            insights = insights
        )
    }

    private fun streakFrom(calendar: List<CalendarCell>): Int {
        var streak = 0
        calendar.sortedByDescending { it.day }.forEach { cell ->
            when (cell.state) {
                DayState.DONE -> streak++
                DayState.REST -> Unit
                else -> return streak
            }
        }
        return streak
    }

    // ------------------------------------------------------------- ai insights

    data class ProgressRow(val label: String, val percent: Int)

    data class Prediction(val valueLabel: String, val caption: String)

    data class ComparisonRow(val label: String, val changePercent: Int)

    data class NextAction(val label: String, val detail: String)

    data class TrendSeries(val label: String, val points: List<Float>)

    data class InsightsDashboard(
        val score: Int,
        val scoreLabel: String,
        val overall: List<CategoryScore>,
        val progress: List<ProgressRow>,
        val summary: String,
        val recommendations: List<String>,
        val predictions: List<Prediction>,
        val goalTitle: String,
        val goalPercent: Int,
        val goalTarget: String,
        val goalRemaining: String,
        val trend: List<TrendSeries>,
        val comparison: List<ComparisonRow>,
        val keyInsights: List<String>,
        val nextActions: List<NextAction>
    ) {
        val hasData: Boolean get() = overall.any { it.percent > 0 }
    }

    fun insights(
        input: BodyIntelligenceInput,
        ledger: BodyLedger,
        view: BodyIntelligenceView
    ): InsightsDashboard {
        val focus = muscleFocus(input, ledger)
        val activity = activity(input, ledger)
        val assessmentDash = assessment(input, ledger)
        val consistencyDash = consistency(input, ledger)

        val compositionScore = compositionScore(view)
        val strengthScore = assessmentDash.categories.first { it.label == "Strength" }.percent
        val activityScore = activity?.let {
            (it.activeDays * 100.0 / it.windowDays.coerceAtLeast(1)).roundToInt().coerceIn(0, 100)
        } ?: 0
        val consistencyScore = consistencyDash?.completionPercent ?: 0
        val assessmentScore = assessmentDash.score

        val overall = listOf(
            CategoryScore("Body Composition", compositionScore),
            CategoryScore("Strength", strengthScore),
            CategoryScore("Activity & Endurance", activityScore),
            CategoryScore("Consistency", consistencyScore),
            CategoryScore("Assessment", assessmentScore)
        )
        val score = overall.map { it.percent }.average().roundToInt()

        val progress = listOf(
            ProgressRow("Strength", assessmentDash.tests.firstOrNull { it.recorded }?.changePercent ?: 0),
            ProgressRow("Activity", activity?.let { it.goalPercent - 100 } ?: 0),
            ProgressRow(
                "Body Comp.",
                view.readings.firstOrNull()?.trend
                    ?.let { BodyIntelligenceAnalytics.trendChangePercent(it)?.roundToInt() }
                    ?: 0
            )
        )

        val summary = buildString {
            append("Overall score is $score% across ${overall.count { it.percent > 0 }} of ${overall.size} categories. ")
            // The ledger is assembled from a session template until the member logs
            // real sets, so its set count describes an inferred split. Calling it
            // "on record" would report invented volume as a stored fact.
            focus?.let {
                if (input.hasSetLevelRecords) {
                    append("${it.trainingDays} training days and ${it.totalSets} working sets are on record. ")
                } else {
                    append("${it.trainingDays} training days recorded; set counts are inferred from the session split. ")
                }
            }
            view.readings.firstOrNull()?.let {
                append("${it.label} is ${it.valueLabel}${it.deltaLabel?.let { d -> " ($d)" } ?: ""}.")
            }
        }

        val recommendations = buildList {
            focus?.muscles?.minByOrNull { it.percent }?.let {
                val basis = if (input.hasSetLevelRecords) "working sets" else "inferred session volume"
                add("Increase ${it.label.lowercase()} volume - currently ${it.percent}% of $basis.")
            }
            activity?.let {
                if (it.goalPercent < 100) add("Add one cardio session to reach the weekly distance target.")
            }
            assessmentDash.tests.count { !it.recorded }.takeIf { it > 0 }?.let {
                add("Schedule the remaining $it unperformed fitness tests.")
            }
            add("Keep protein and hydration consistent with the recorded training load.")
        }

        val predictions = buildList {
            val delta = focus?.vsLastMonthDays ?: 0.0
            add(Prediction("${if (delta >= 0) "+" else ""}${fmt(delta)} days", "Est. training-day change"))
            view.readings.firstOrNull()?.deltaLabel?.let {
                add(Prediction(it, "Last recorded body change"))
            }
            add(Prediction("$score%", "Current overall score"))
        }

        val goalTarget = view.readings.firstOrNull()?.goalLabel
            ?.removePrefix("Goal:")?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: "Goal not configured"
        // This percent is goal *alignment* - how well the training distribution
        // supports the selected goal - not distance travelled toward the target.
        val goalPercent = focus?.goalPercent ?: 0
        val goalRemaining = view.readings.firstOrNull()?.let { reading ->
            val target = reading.goalLabel?.filter { it.isDigit() || it == '.' }?.toDoubleOrNull()
            when {
                target == null -> "Goal not configured"
                // Naming the direction keeps the strip honest when a stored target sits
                // on the opposite side of the member's goal label.
                target > reading.value -> "${fmt(target - reading.value)} ${reading.unit} to gain"
                target < reading.value -> "${fmt(reading.value - target)} ${reading.unit} to lose"
                else -> "Target reached"
            }
        } ?: "Goal not configured"

        // Weekly trend built from the recorded block. Each point is a real weekly
        // aggregate; the chart scales the series independently so unlike units
        // are never compared on one axis.
        val weekBuckets = 4
        val bucketSize = (ledger.sessions.size / weekBuckets.toDouble()).coerceAtLeast(1.0)
        fun bucket(index: Int): List<LedgerSession> {
            val from = (index * bucketSize).roundToInt().coerceAtMost(ledger.sessions.size)
            val to = ((index + 1) * bucketSize).roundToInt().coerceAtMost(ledger.sessions.size)
            return ledger.sessions.subList(from, to.coerceAtLeast(from))
        }
        val strengthSeries = (0 until weekBuckets).map { index ->
            val slice = bucket(index)
            val strength = slice.count { it.kind == ActivityKind.STRENGTH }
            if (slice.isEmpty()) 0f else strength * 100f / slice.size
        }
        val activitySeries = activity?.weekly?.map { it.value.toFloat() }.orEmpty()
        val scoreSeries = consistencyDash?.weekly?.map { it.percent.toFloat() }.orEmpty()

        fun halfChangeOf(select: (LedgerSession) -> Double): Int {
            if (ledger.sessions.size < 2) return 0
            val half = ledger.sessions.size / 2
            val first = ledger.sessions.take(half).sumOf(select)
            val second = ledger.sessions.drop(half).sumOf(select)
            return changePercent(first, second)
        }

        val comparison = listOf(
            ComparisonRow("Workout Volume", halfChangeOf { it.volumeKg }),
            ComparisonRow("Session Time", halfChangeOf { it.minutes.toDouble() }),
            ComparisonRow("Cardio Distance", halfChangeOf { session ->
                session.sets.sumOf { it.meters ?: 0.0 }
            }),
            ComparisonRow("Training Days", halfChangeOf { 1.0 })
        )

        val keyInsights = buildList {
            add("Fitness score is $score% across ${overall.count { it.percent > 0 }} categories.")
            focus?.let {
                add("${it.totalSets} working sets across ${it.trainingDays} training days.")
                it.muscles.maxByOrNull { m -> m.sets }?.let { m ->
                    add("${m.label} is the most trained area at ${m.percent}%.")
                }
            }
            consistencyDash?.let { add("Consistency is ${it.completionPercent}% for this block.") }
            overall.minByOrNull { it.percent }?.let {
                add("${it.label} is the weakest category at ${it.percent}%.")
            }
        }

        val nextActions = buildList {
            add(NextAction("Strength", "${focus?.sessionsThisWeek ?: 0} sessions this week"))
            add(NextAction("Cardio", if ((activity?.goalPercent ?: 0) >= 100) "Target met" else "1 session left"))
            add(NextAction("Recovery", "${consistencyDash?.averageSessionMinutes ?: 0} min avg"))
            add(NextAction("Nutrition", "Goal ${input.goal.label}"))
        }

        return InsightsDashboard(
            score = score,
            scoreLabel = when {
                score >= 85 -> "Excellent"
                score >= 75 -> "Improving"
                score >= 60 -> "Average"
                else -> "Building"
            },
            overall = overall,
            progress = progress,
            summary = summary,
            recommendations = recommendations,
            predictions = predictions,
            goalTitle = input.goal.label,
            goalPercent = goalPercent,
            goalTarget = goalTarget,
            goalRemaining = goalRemaining,
            trend = listOf(
                TrendSeries("Strength", strengthSeries),
                TrendSeries("Activity", activitySeries),
                TrendSeries("Score", scoreSeries)
            ),
            comparison = comparison,
            keyInsights = keyInsights,
            nextActions = nextActions
        )
    }

    private fun compositionScore(view: BodyIntelligenceView): Int {
        if (view.readings.isEmpty()) return 0
        val favourable = view.readings.count { it.deltaIsFavourable }
        val base = (favourable * 100.0 / view.readings.size)
        // A member with recorded goals scores higher than one without any.
        val withGoal = view.readings.count { !it.goalLabel.isNullOrBlank() }
        val goalWeight = withGoal * 100.0 / view.readings.size
        return ((base * 0.7) + (goalWeight * 0.3)).roundToInt().coerceIn(0, 100)
    }

    // ------------------------------------------------------------------ utils

    /** Percent change from [first] to [second]; 0 when there is no baseline. */
    private fun changePercent(first: Double, second: Double): Int {
        if (first <= 0.0) return 0
        return (((second - first) / first) * 100).roundToInt()
    }

    fun fmt(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "-"
        val rounded = (value * 10).roundToInt() / 10.0
        return if (rounded == rounded.toLong().toDouble()) rounded.toLong().toString()
        else String.format("%.1f", rounded)
    }

    fun volumeLabel(kg: Double): String = when {
        kg >= 1000 -> "${fmt(kg / 1000)} t"
        else -> "${kg.roundToInt()} kg"
    }

    /** Equipment mix, kept for the strength detail sheet. */
    fun equipmentMix(ledger: BodyLedger): List<Pair<EquipmentCategory, Int>> =
        ledger.sessions.flatMap { it.sets }
            .mapNotNull { ExerciseCatalog.byId(it.exerciseId)?.equipment }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
}
