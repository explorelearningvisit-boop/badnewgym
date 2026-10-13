package com.example.badnewgym.feature.memberintelligence.domain.analytics

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyTrend
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalDirection
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricKey
import com.example.badnewgym.feature.memberintelligence.domain.model.ProgressVerdict
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutTrend
import kotlin.math.abs

/**
 * Pure analytics over real records. No predictions, no smoothing that invents
 * data, no "improvement" claims unless the observations support them (spec 26/31).
 */
object AttendanceAnalytics {

    /**
     * Completion ratio over expected open days only - closed days never dilute
     * it. Returns 0 when there is genuinely nothing to measure.
     */
    fun completion(window: AttendanceWindow): Float {
        val open = window.elapsed.filter { it.state != AttendanceDayState.HOLIDAY }
        if (open.isEmpty()) return 0f
        return open.count { it.isAttended }.toFloat() / open.size
    }

    /**
     * Consecutive attended days ending at the most recent elapsed day.
     *
     * Days the gym was closed are skipped, never treated as a break: a holiday
     * cannot end a streak the member actually maintained.
     */
    fun currentStreak(window: AttendanceWindow): Int {
        var streak = 0
        var started = false
        window.elapsed.sortedByDescending { it.epochDay }.forEach { day ->
            when {
                day.state == AttendanceDayState.HOLIDAY || day.state == AttendanceDayState.CLOSED -> Unit
                day.isAttended -> { streak++; started = true }
                started -> return streak
            }
        }
        return streak
    }

    /** Longest run of attended days, ignoring days the gym was closed. */
    fun longestStreak(window: AttendanceWindow): Int {
        val sorted = window.elapsed.sortedBy { it.epochDay }
        var best = 0; var run = 0
        sorted.forEach { day ->
            when {
                day.state == AttendanceDayState.HOLIDAY || day.state == AttendanceDayState.CLOSED -> Unit
                day.isAttended -> { run++; best = maxOf(best, run) }
                else -> run = 0
            }
        }
        return best
    }

    /** First-half vs second-half attendance delta in percentage points. */
    fun trendPoints(window: AttendanceWindow): Int {
        val sorted = window.elapsed.sortedBy { it.epochDay }
        if (sorted.size < 4) return 0
        val half = sorted.size / 2
        val first = sorted.take(half).count { it.isAttended }.toDouble() / half * 100
        val second = sorted.drop(half).count { it.isAttended }.toDouble() / (sorted.size - half) * 100
        return (second - first).toInt()
    }
}

object WorkoutAnalytics {

    const val PAGE_SIZE = 7

    data class Window(val startIndex: Int, val days: List<WorkoutDay>) {
        val total: Int get() = days.sumOf { it.minutes }
        val peak: WorkoutDay? get() = days.maxByOrNull { it.minutes }
        val lowest: WorkoutDay? get() = days.filter { it.minutes > 0 }.minByOrNull { it.minutes }
        val startDate: String get() = days.firstOrNull()?.dateLabel.orEmpty()
        val endDate: String get() = days.lastOrNull()?.dateLabel.orEmpty()
    }

    data class WorkoutDay(
        val dayOfMonth: Int,
        val minutes: Int,
        val weekdayShort: String,
        val dateLabel: String,
        val monthShort: String = "",
        /** True when the recorded session was cardio: drives the bar color. */
        val cardio: Boolean = false
    )

    data class Summary(
        val totalMinutes: Int,
        val averageMinutes: Int,
        val peak: WorkoutDay?,
        val lowest: WorkoutDay?,
        val bestWeek: String?,
        val trend: WorkoutTrend,
        val trendDeltaPercent: Int,
        val activeDays: Int,
        val totalDays: Int
    )

    /** Paged 7 day viewport over the monthly dataset (spec 23/24). */
    fun window(month: WorkoutMonth, windowIndex: Int): Window? {
        val weeks = calendarWeeks(month)
        if (weeks.isEmpty()) return null
        val index = windowIndex.coerceIn(0, weeks.lastIndex)
        return Window(index, weeks[index])
    }

    fun windowCount(month: WorkoutMonth): Int =
        maxOf(1, calendarWeeks(month).size)

    private val shortWeekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    /**
     * Splits the dataset into Monday based calendar weeks.
     *
     * Paging by session count produced a final window holding only the few
     * remaining sessions, which rendered as a chart of zeros. Weeks are the
     * unit a bar chart actually reads in, so every window is a real week.
     */
    private fun calendarWeeks(
        month: WorkoutMonth
    ): List<List<WorkoutDay>> {
        val sessions = month.sorted()
        if (sessions.isEmpty()) return emptyList()
        val byDay = sessions.associateBy { it.epochDay }
        val first = java.time.LocalDate.ofEpochDay(sessions.first().epochDay)
        val last = java.time.LocalDate.ofEpochDay(sessions.last().epochDay)
        var cursor = first.minusDays((first.dayOfWeek.value - 1).toLong())

        val weeks = mutableListOf<List<WorkoutDay>>()
        while (!cursor.isAfter(last)) {
            weeks += (0 until PAGE_SIZE).map { i ->
                val date = cursor.plusDays(i.toLong())
                val session = byDay[date.toEpochDay()]
                WorkoutDay(
                    dayOfMonth = date.dayOfMonth,
                    minutes = session?.minutes ?: 0,
                    weekdayShort = shortWeekdays[i],
                    dateLabel = "${shortWeekdays[i].take(3)} ${date.dayOfMonth}",
                    monthShort = shortMonth(date.monthValue),
                    cardio = session?.type == com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutType.CARDIO
                )
            }
            cursor = cursor.plusDays(PAGE_SIZE.toLong())
        }
        return weeks
    }

    fun summary(month: WorkoutMonth): Summary {
        val sorted = month.sorted()
        val base = summarize(sorted)
        return base.copy(
            bestWeek = bestWeek(sorted, month),
            totalDays = java.time.YearMonth.of(month.year, month.month).lengthOfMonth()
        )
    }

    /**
     * Range-scoped totals over an explicit session list (7/14/15/30-day
     * selector). Same honest math as [summary], minus the month framing.
     */
    fun summarize(sessions: List<com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession>): Summary {
        val sorted = sessions.sortedBy { it.epochDay }
        val active = sorted.filter { it.minutes > 0 }
        val total = sorted.sumOf { it.minutes }
        val avg = if (active.isEmpty()) 0 else total / active.size
        val peak = sorted.maxByOrNull { it.minutes }?.takeIf { it.minutes > 0 }?.let { dayOfSession(it) }
        val lowest = active.minByOrNull { it.minutes }?.let { dayOfSession(it) }
        val trend = trend(sorted)
        return Summary(
            totalMinutes = total,
            averageMinutes = avg,
            peak = peak,
            lowest = lowest,
            bestWeek = null,
            trend = trend.first,
            trendDeltaPercent = trend.second,
            activeDays = active.size,
            totalDays = sorted.size
        )
    }

    /**
     * Last [days] calendar days ending at the newest dataset date, oldest
     * first. Zero-day entries are real "no session" days, never invented
     * minutes - the chart renders them as empty stubs.
     */
    fun rangeDays(month: WorkoutMonth, days: Int): List<WorkoutDay> {
        val sessions = month.sorted()
        if (sessions.isEmpty() || days <= 0) return emptyList()
        val byDay = sessions.associateBy { it.epochDay }
        val end = java.time.LocalDate.ofEpochDay(sessions.last().epochDay)
        return (days - 1 downTo 0).map { back ->
            val date = end.minusDays(back.toLong())
            val session = byDay[date.toEpochDay()]
            val wd = shortWeekdays[date.dayOfWeek.value - 1]
            WorkoutDay(
                dayOfMonth = date.dayOfMonth,
                minutes = session?.minutes ?: 0,
                weekdayShort = wd,
                dateLabel = "$wd ${date.dayOfMonth}",
                monthShort = shortMonth(date.monthValue),
                cardio = session?.type == com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutType.CARDIO
            )
        }
    }

    fun rangeLabel(month: WorkoutMonth, days: Int): String {
        val list = rangeDays(month, days)
        if (list.isEmpty()) return ""
        val first = list.first()
        val last = list.last()
        return "${first.dateLabel} ${first.monthShort} – ${last.dateLabel} ${last.monthShort}"
    }

    private fun dayOfSession(s: com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession) = WorkoutDay(
        dayOfMonth = s.dayOfMonth,
        minutes = s.minutes,
        weekdayShort = shortWeekdays[(java.time.LocalDate.ofEpochDay(s.epochDay).dayOfWeek.value - 1)],
        dateLabel = "${shortWeekdays[(java.time.LocalDate.ofEpochDay(s.epochDay).dayOfWeek.value - 1)]} ${s.dayOfMonth}",
        monthShort = shortMonth(java.time.LocalDate.ofEpochDay(s.epochDay).monthValue),
        cardio = s.type == com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutType.CARDIO
    )

    private fun shortMonth(month: Int): String =
        java.time.Month.of(month).name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }

    private fun dayOf(s: com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession, month: WorkoutMonth) = dayOfSession(s)

    private fun bestWeek(sorted: List<com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession>, month: WorkoutMonth): String? {
        if (sorted.size < PAGE_SIZE) return null
        var best: Pair<Int, Int>? = null
        sorted.chunked(PAGE_SIZE).forEachIndexed { i, chunk ->
            val sum = chunk.sumOf { it.minutes }
            if (best == null || sum > best!!.first) best = sum to i
        }
        val idx = best?.second ?: return null
        val chunk = sorted.chunked(PAGE_SIZE)[idx]
        val from = chunk.first().dayOfMonth
        val to = chunk.last().dayOfMonth
        return "$from–$to ${monthLabel(month)}"
    }

    /**
     * Honest trend detection. High coefficient of variation reads as MIXED
     * instead of being smoothed into a fake uptrend.
     */
    fun trend(sorted: List<com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession>): Pair<WorkoutTrend, Int> {
        val active = sorted.filter { it.minutes > 0 }.map { it.minutes }
        if (active.size < 4) return WorkoutTrend.NONE to 0
        val mean = active.average()
        val sd = kotlin.math.sqrt(active.sumOf { (it - mean) * (it - mean) } / active.size)
        if (mean <= 0) return WorkoutTrend.NONE to 0
        val cv = sd / mean
        val half = active.size / 2
        val first = active.take(half).average()
        val second = active.drop(half).average()
        val deltaPct = if (first > 0) (((second - first) / first) * 100).toInt() else 0
        if (cv > 0.75) return WorkoutTrend.MIXED to deltaPct
        return when {
            deltaPct >= 8 -> WorkoutTrend.UP to deltaPct
            deltaPct <= -8 -> WorkoutTrend.DOWN to deltaPct
            else -> WorkoutTrend.STABLE to deltaPct
        }
    }

    private fun monthLabel(month: WorkoutMonth): String =
        java.time.Month.of(month.month).name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }
}

object BodyGoalAnalytics {

    /**
     * Goal aware interpretation (spec 30).
     * Weight gain and weight loss both read green when moving toward the goal.
     */
    fun verdict(goal: BodyGoal): ProgressVerdict {
        if (goal.targetValue == goal.startValue) return ProgressVerdict.UNKNOWN
        val moved = goal.delta
        val toward = when (goal.direction) {
            GoalDirection.INCREASE -> moved
            GoalDirection.DECREASE -> -moved
            GoalDirection.HOLD -> return ProgressVerdict.UNKNOWN
        }
        val span = abs(goal.targetValue - goal.startValue)
        if (span == 0.0) return ProgressVerdict.UNKNOWN
        val progress = (toward / span).coerceIn(0.0, 1.0)
        return when {
            toward == 0.0 -> ProgressVerdict.STABLE
            toward > 0 && progress >= 0.85 -> ProgressVerdict.ON_TRACK
            toward > 0 -> ProgressVerdict.IMPROVING
            progress > 1.0 -> ProgressVerdict.OFF_TRACK
            else -> ProgressVerdict.REGRESSING
        }
    }

    /** Real observed shape from history - never a decorative straight line. */
    fun trend(history: MetricHistory): BodyTrend {
        val values = history.ordered().map { it.value }
        if (values.size < 3) return BodyTrend.NONE
        val deltas = values.zipWithNext { a, b -> b - a }
        val ups = deltas.count { it > 0.0001 }
        val downs = deltas.count { it < -0.0001 }
        val flats = deltas.size - ups - downs
        return when {
            ups == 0 && downs == 0 -> BodyTrend.STABLE
            flats >= deltas.size / 2 -> BodyTrend.STABLE
            ups >= deltas.size * 0.7 -> BodyTrend.UP
            downs >= deltas.size * 0.7 -> BodyTrend.DOWN
            else -> BodyTrend.MIXED
        }
    }

    /** Value range for chart scaling; null when there is genuinely no data. */
    fun range(history: MetricHistory): Pair<Double, Double>? {
        val values = history.ordered().map { it.value }
        if (values.isEmpty()) return null
        val lo = values.min()
        val hi = values.max()
        val pad = maxOf((hi - lo) * 0.18, 0.4)
        return (lo - pad) to (hi + pad)
    }

    fun metricsOf(goals: List<BodyGoal>): List<MetricKey> = goals.map { it.primaryMetric }.distinct()
}