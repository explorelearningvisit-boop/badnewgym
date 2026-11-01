package com.example.badnewgym.feature.bodyprogress.domain.intelligence

import kotlin.math.abs

/**
 * Pure analytics for the Body Intelligence dashboard.
 *
 * Every function is deterministic and works on the records it is given. When a
 * denominator is missing the result is null / an empty list - never a stand-in
 * number - so a widget can render an honest empty state.
 */
object BodyIntelligenceAnalytics {

    // ------------------------------------------------------------- projection

    /**
     * Applies privacy *before* aggregation.
     *
     * A hidden category is removed from `readings` here, which means the
     * composition chart, the muscle summary and the insight engine cannot see
     * it even by accident.
     */
    fun viewOf(
        input: BodyIntelligenceInput,
        privacy: BodyPrivacySettings = BodyPrivacySettings.Visible
    ): BodyIntelligenceView {
        val visibleReadings = input.readings.filter { privacy.isVisible(it.privacy) }
        val hidden = input.readings.map { it.privacy }.distinct().filterNot { privacy.isVisible(it) }
        val activity = activitySummary(input)
        val consistency = consistencySummary(input)
        val insights = insightsFor(input, visibleReadings, activity, consistency, privacy)
        return BodyIntelligenceView(
            input = input,
            privacy = privacy,
            readings = visibleReadings,
            activity = activity,
            consistency = consistency,
            insights = insights,
            hiddenCategories = hidden
        )
    }

    // --------------------------------------------------------------- activity

    fun activitySummary(input: BodyIntelligenceInput): ActivitySummary {
        val sessions = input.activityDays.filter { it.minutes > 0 }
        if (sessions.isEmpty()) return ActivitySummary()
        val total = sessions.sumOf { it.minutes }
        val longest = sessions.maxByOrNull { it.minutes }
        return ActivitySummary(
            totalMinutes = total,
            sessionDays = sessions.size,
            averageMinutes = total / sessions.size,
            longestMinutes = longest?.minutes ?: 0,
            longestDay = longest?.day,
            dailyMinutes = sessions.sortedBy { it.day }.map { it.day to it.minutes }
        )
    }

    // ------------------------------------------------------------ consistency

    /**
     * Attendance and training stay separate: a gym check-in with no recorded
     * session is reported as [DayMarker.CHECKED_IN_ONLY], never as training.
     */
    fun consistencySummary(input: BodyIntelligenceInput): ConsistencySummary {
        val workoutByDay = input.activityDays.filter { it.minutes > 0 }.associate { it.day to it.minutes }
        val checkInByDay = input.checkInDays.filter { it.present && !it.holiday }
            .associateBy { it.day }
        val days = (workoutByDay.keys + checkInByDay.keys).sorted()
        if (days.isEmpty()) return ConsistencySummary()

        val first = days.first()
        val last = days.last()
        val markers = (first..last).map { day ->
            val marker = when {
                workoutByDay[day] != null -> DayMarker.TRAINED
                checkInByDay[day] != null -> DayMarker.CHECKED_IN_ONLY
                else -> DayMarker.REST
            }
            day to marker
        }
        val trained = workoutByDay.size
        val checkedIn = checkInByDay.size
        return ConsistencySummary(
            trainedDays = trained,
            checkInDays = checkedIn,
            checkInWithoutWorkout = checkInByDay.keys.count { workoutByDay[it] == null },
            updatedAtDay = last,
            markers = markers
        )
    }

    // --------------------------------------------------------------- insights

    /**
     * Evidence-linked observations only. Each insight names the records it came
     * from; when the records are insufficient it says so instead of guessing.
     */
    fun insightsFor(
        input: BodyIntelligenceInput,
        visibleReadings: List<CompositionReading>,
        activity: ActivitySummary,
        consistency: ConsistencySummary,
        privacy: BodyPrivacySettings
    ): List<BodyInsight> {
        val out = mutableListOf<BodyInsight>()

        visibleReadings.forEach { reading ->
            val goal = reading.goalLabel
            val delta = reading.deltaLabel
            if (goal.isNullOrBlank() || delta.isNullOrBlank()) return@forEach
            out += BodyInsight(
                title = "${reading.label} is tracking ${goal.removePrefix("Goal:").trim()}",
                detail = "Recorded change $delta, latest ${reading.valueLabel}.",
                evidence = "${reading.label} · ${reading.valueLabel} · $delta · goal ${
                    goal.removePrefix("Goal:").trim()
                }",
                tone = if (reading.deltaIsFavourable) InsightTone.POSITIVE else InsightTone.ATTENTION,
                metricKey = reading.metric.name
            )
        }

        if (activity.hasData) {
            out += BodyInsight(
                title = "${activity.sessionDays} training days averaged ${activity.averageMinutes} min",
                detail = "Longest recorded session was ${activity.longestMinutes} min" +
                    (activity.longestDay?.let { " on day $it" } ?: "") + ".",
                evidence = "${activity.totalMinutes} min recorded across ${activity.sessionDays} days.",
                tone = InsightTone.NEUTRAL,
                metricKey = "activity"
            )
        }

        if (consistency.checkInWithoutWorkout > 0) {
            out += BodyInsight(
                title = "${consistency.checkInWithoutWorkout} gym check-ins have no completed workout",
                detail = "Attendance is recorded separately from training, so these days are not counted as sessions.",
                evidence = "${consistency.checkInDays} check-ins · ${consistency.trainedDays} training days.",
                tone = InsightTone.ATTENTION,
                metricKey = "consistency"
            )
        }

        if (!input.hasSetLevelRecords) {
            out += BodyInsight(
                title = "Strength records are not stored yet",
                detail = "Every exercise is listed from the catalogue as Not started until a set is logged.",
                evidence = "0 set-level records for ${input.memberId}.",
                tone = InsightTone.DATA,
                metricKey = "strength"
            )
        }

        if (privacy.hidden.isNotEmpty()) {
            out += BodyInsight(
                title = "${privacy.hidden.size} body categories are private",
                detail = "Hidden values are excluded from this dashboard and from these insights.",
                evidence = privacy.hidden.joinToString(", ") { it.label },
                tone = InsightTone.DATA,
                metricKey = "privacy"
            )
        }

        if (visibleReadings.isEmpty() && !input.hasSetLevelRecords && !activity.hasData) {
            out += BodyInsight(
                title = "Not enough records to interpret yet",
                detail = "Log a body measurement or a workout to start the trend analysis.",
                evidence = "0 measurements · 0 sessions.",
                tone = InsightTone.DATA
            )
        }

        // High-signal first, then a stable order for the rest.
        return out.sortedBy { toneWeight(it.tone) }
    }

    private fun toneWeight(tone: InsightTone): Int = when (tone) {
        InsightTone.ATTENTION -> 0
        InsightTone.POSITIVE -> 1
        InsightTone.NEUTRAL -> 2
        InsightTone.DATA -> 3
    }

    // ---------------------------------------------------------------- display

    /** Percent change between the first and last trend point, or null. */
    fun trendChangePercent(points: List<Float>): Double? {
        if (points.size < 2) return null
        val first = points.first()
        val last = points.last()
        if (first == 0f) return null
        return ((last - first) / abs(first)) * 100.0
    }

    /** Rounds a minutes value to a compact human label. */
    fun minutesLabel(minutes: Int): String {
        if (minutes < 60) return "$minutes min"
        val h = minutes / 60
        val m = minutes % 60
        return if (m == 0) "${h}h" else "${h}h ${m}m"
    }
}

/**
 * Adaptive, stable widget order.
 *
 * The goal decides the base order. Data availability then promotes widgets that
 * actually have records, without ever dropping one: the result always contains
 * all eight widgets exactly once, so nothing becomes unreachable.
 */
object BodyWidgetPriority {

    private val GOAL_ORDER: Map<TrainingGoal, List<BodyWidget>> = mapOf(
        TrainingGoal.FAT_LOSS to listOf(
            BodyWidget.COMPOSITION, BodyWidget.ACTIVITY, BodyWidget.STRENGTH, BodyWidget.CONSISTENCY,
            BodyWidget.HUMAN_BODY, BodyWidget.MUSCLE_FOCUS, BodyWidget.ASSESSMENT, BodyWidget.INSIGHTS
        ),
        TrainingGoal.MUSCLE_GAIN to listOf(
            BodyWidget.HUMAN_BODY, BodyWidget.STRENGTH, BodyWidget.MUSCLE_FOCUS, BodyWidget.COMPOSITION,
            BodyWidget.ACTIVITY, BodyWidget.ASSESSMENT, BodyWidget.CONSISTENCY, BodyWidget.INSIGHTS
        ),
        TrainingGoal.STRENGTH to listOf(
            BodyWidget.STRENGTH, BodyWidget.ASSESSMENT, BodyWidget.MUSCLE_FOCUS, BodyWidget.CONSISTENCY,
            BodyWidget.HUMAN_BODY, BodyWidget.COMPOSITION, BodyWidget.ACTIVITY, BodyWidget.INSIGHTS
        ),
        TrainingGoal.ENDURANCE to listOf(
            BodyWidget.ACTIVITY, BodyWidget.ASSESSMENT, BodyWidget.CONSISTENCY, BodyWidget.COMPOSITION,
            BodyWidget.STRENGTH, BodyWidget.MUSCLE_FOCUS, BodyWidget.HUMAN_BODY, BodyWidget.INSIGHTS
        ),
        TrainingGoal.MAINTENANCE to listOf(
            BodyWidget.STRENGTH, BodyWidget.CONSISTENCY, BodyWidget.COMPOSITION, BodyWidget.MUSCLE_FOCUS,
            BodyWidget.ACTIVITY, BodyWidget.ASSESSMENT, BodyWidget.HUMAN_BODY, BodyWidget.INSIGHTS
        ),
        TrainingGoal.GENERAL to listOf(
            BodyWidget.ASSESSMENT, BodyWidget.COMPOSITION, BodyWidget.ACTIVITY, BodyWidget.STRENGTH,
            BodyWidget.CONSISTENCY, BodyWidget.HUMAN_BODY, BodyWidget.MUSCLE_FOCUS, BodyWidget.INSIGHTS
        )
    )

    fun goalOrder(goal: TrainingGoal): List<BodyWidget> =
        GOAL_ORDER[goal] ?: GOAL_ORDER.getValue(TrainingGoal.GENERAL)

    /** True when this widget has at least one record to render. */
    fun hasData(widget: BodyWidget, view: BodyIntelligenceView): Boolean = when (widget) {
        BodyWidget.HUMAN_BODY -> view.readings.isNotEmpty()
        BodyWidget.COMPOSITION -> view.readings.isNotEmpty()
        BodyWidget.STRENGTH -> view.input.hasSetLevelRecords
        BodyWidget.MUSCLE_FOCUS -> view.input.hasSetLevelRecords
        BodyWidget.ACTIVITY -> view.activity.hasData
        BodyWidget.ASSESSMENT -> false
        BodyWidget.CONSISTENCY -> view.consistency.hasData
        BodyWidget.INSIGHTS -> view.insights.isNotEmpty()
    }

    /**
     * Stable promotion: widgets with records move ahead of empty ones, each
     * group keeping its goal-defined relative order. Repeated calls with the
     * same input return the same list, so the dashboard never reshuffles on
     * refresh.
     */
    fun order(view: BodyIntelligenceView): List<BodyWidget> {
        val base = goalOrder(view.goal)
        val (withData, empty) = base.partition { hasData(it, view) }
        return withData + empty
    }
}
