package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Pure presentation-level derivations for the Member Intelligence dashboard.
 *
 * Every number the dashboard renders must come from this object or from the domain
 * engines, so that the behaviour is unit-testable and no figure is invented in a
 * composable. Missing data is represented by null — never as a zero.
 */
object DashboardMath {
    const val DAY_MS = 86_400_000L

    // ------------------------------------------------------------------ membership ring

    /** Server-reported remaining days, never below zero. */
    fun daysRemaining(membership: MembershipStatus?, now: Long): Int {
        if (membership == null) return 0
        val reported = membership.daysRemaining
        val derived = ceil((membership.expiryDate - now).toDouble() / DAY_MS).toInt()
        return maxOf(0, minOf(reported, derived))
    }

    /** Elapsed share of the plan term in whole percent, based on real dates. */
    fun elapsedPercent(membership: MembershipStatus?, now: Long): Int {
        if (membership == null) return 0
        val total = ((membership.expiryDate - membership.startDate) / DAY_MS).toInt().coerceAtLeast(1)
        val consumed = (((now - membership.startDate).coerceAtLeast(0)) / DAY_MS).toInt()
        return ((consumed.toDouble() / total) * 100).roundToInt().coerceIn(0, 100)
    }

    // ------------------------------------------------------------------ attendance

    /** Total length of the plan term in whole days, from the real validity dates. */
    fun planTotalDays(membership: MembershipStatus?): Int {
        if (membership == null) return 0
        return ((membership.expiryDate - membership.startDate) / DAY_MS).toInt().coerceAtLeast(1)
    }

    /** Whole days from [now] until [target]; negative once the target has passed. */
    fun daysBetween(now: Long, target: Long): Int =
        ((target - now).toDouble() / DAY_MS).let { kotlin.math.ceil(it).toInt() }

    /** visits/target share in whole percent; null target means not computable. */
    fun attendancePercent(visits: Int, target: Int?): Int? {
        if (target == null || target <= 0) return null
        return ((visits.toDouble() / target) * 100).roundToInt()
    }

    /** Month key as year*12+month index so arithmetic across years works. */
    fun monthKey(time: Long): Long {
        val c = Calendar.getInstance().apply { timeInMillis = time }
        return c.get(Calendar.YEAR).toLong() * 12L + c.get(Calendar.MONTH).toLong()
    }

    fun previousMonthKey(key: Long): Long = key - 1L

    /** Inverse of [monthKey]: builds a calendar positioned at 00:00 on day 1 of that month. */
    private fun calendarForMonthKey(key: Long): Calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, (key / 12L).toInt())
        set(Calendar.MONTH, (key % 12L).toInt())
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    /** Number of days in the month identified by [key] (leap years handled by Calendar). */
    fun daysInMonthForMonthKey(key: Long): Int =
        calendarForMonthKey(key).getActualMaximum(Calendar.DAY_OF_MONTH)

    /** Epoch-millis at 00:00 on [dayOfMonth] of the month identified by [key]. */
    fun dateForMonthDay(key: Long, dayOfMonth: Int): Long =
        calendarForMonthKey(key).apply { set(Calendar.DAY_OF_MONTH, dayOfMonth) }.timeInMillis

    /** Column offset (0..6, Sunday = 0) that the 1st of [key]'s month occupies. */
    fun firstWeekdayOffsetForMonthKey(key: Long): Int =
        when (calendarForMonthKey(key).get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 6
        }

    /** "Oct 2026" style label for a month key. */
    fun monthLabelForKey(key: Long): String {
        val calendar = calendarForMonthKey(key)
        return "${MONTH_ABBREVIATIONS[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
    }

    private val MONTH_ABBREVIATIONS = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    /** Counts of each status inside the calendar month of [now]. */
    fun statusCounts(days: List<AttendanceDay>, now: Long): Map<AttendanceDayStatus, Int> {
        val key = monthKey(now)
        val counted = days
            .filter { monthKey(it.date) == key }
            .groupingBy { it.status }
            .eachCount()
        return AttendanceDayStatus.entries.associateWith { counted[it] ?: 0 }
    }

    /** Column offset (0..6, Sunday = 0) that the 1st of the current month occupies. */
    fun firstWeekdayOffset(now: Long): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 6
        }
    }

    fun daysInMonth(now: Long): Int = Calendar.getInstance().apply { timeInMillis = now }
        .getActualMaximum(Calendar.DAY_OF_MONTH)

    /** Column offset (0..6, Sunday = 0) that the 1st of an arbitrary month key occupies. */
    fun firstWeekdayOffsetForMonth(monthKey: Long): Int {
        val calendar = monthCalendar(monthKey)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 6
        }
    }

    fun daysInMonthForMonth(monthKey: Long): Int =
        monthCalendar(monthKey).getActualMaximum(Calendar.DAY_OF_MONTH)

    /** Counts of each status inside an arbitrary month key. */
    fun statusCountsForMonth(days: List<AttendanceDay>, key: Long): Map<AttendanceDayStatus, Int> {
        val counted = days
            .filter { monthKey(it.date) == key }
            .groupingBy { it.status }
            .eachCount()
        return AttendanceDayStatus.entries.associateWith { counted[it] ?: 0 }
    }

    private fun monthCalendar(monthKey: Long): Calendar {
        val year = (monthKey / 12).toInt()
        val month = (monthKey % 12).toInt()
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    // ------------------------------------------------------------------ workout

    enum class WorkoutBucket { ABOVE_AVERAGE, BELOW_AVERAGE, NO_WORKOUT, NO_RECORD }

    /** Average of recorded minutes; null when nothing is recorded. Unknown days never count as 0. */
    fun recordedAverageMinutes(days: List<WorkoutDay>): Int? {
        val recorded = days.asSequence()
            .filter { it.status == WorkoutDayStatus.RECORDED && it.durationMinutes != null }
            .map { it.durationMinutes!!.toDouble() }
        val list = recorded.toList()
        if (list.isEmpty()) return null
        return list.average().roundToInt()
    }

    fun peakRecordedDay(days: List<WorkoutDay>): WorkoutDay? = days
        .filter { it.status == WorkoutDayStatus.RECORDED && it.durationMinutes != null }
        .maxByOrNull { it.durationMinutes!! }

    fun lowestRecordedDay(days: List<WorkoutDay>): WorkoutDay? = days
        .filter { it.status == WorkoutDayStatus.RECORDED && it.durationMinutes != null }
        .minByOrNull { it.durationMinutes!! }

    fun workoutBucket(day: WorkoutDay, averageMinutes: Int?): WorkoutBucket = when {
        day.status == WorkoutDayStatus.CONFIRMED_NO_WORKOUT -> WorkoutBucket.NO_WORKOUT
        day.durationMinutes == null || day.status == WorkoutDayStatus.UNKNOWN -> WorkoutBucket.NO_RECORD
        averageMinutes == null || averageMinutes <= 0 -> WorkoutBucket.ABOVE_AVERAGE
        day.durationMinutes >= averageMinutes -> WorkoutBucket.ABOVE_AVERAGE
        else -> WorkoutBucket.BELOW_AVERAGE
    }

    /** Average of recorded minutes for the given calendar month; null when nothing recorded. */
    fun averageForMonth(days: List<WorkoutDay>, monthKey: Long): Double? {
        val recorded = days
            .filter { monthKey(it.date) == monthKey && it.status == WorkoutDayStatus.RECORDED && it.durationMinutes != null }
            .map { it.durationMinutes!!.toDouble() }
        if (recorded.isEmpty()) return null
        return recorded.average()
    }

    /** Percent difference current vs previous average; null when previous is absent or zero. */
    fun monthOverMonthPercent(current: Double?, previous: Double?): Double? {
        if (current == null || previous == null || previous <= 0.0) return null
        return ((current - previous) / previous) * 100.0
    }

    // ------------------------------------------------------------------ payment timing

    /** Classification used when a new payment is recorded. Unknown due date stays unknown. */
    fun timelinessFor(paidAt: Long, dueDate: Long?): PaymentTimeliness {
        if (dueDate == null) return PaymentTimeliness.UNKNOWN
        if (paidAt <= dueDate) return PaymentTimeliness.ON_TIME
        val daysLate = ((paidAt - dueDate) / DAY_MS).toInt()
        return if (daysLate <= 14) PaymentTimeliness.LATE else PaymentTimeliness.VERY_LATE
    }

    // ------------------------------------------------------------------ body progress

    enum class DeltaDirection { FAVORABLE, UNFAVORABLE, NEUTRAL }

    /**
     * Goal-aware colour direction for a metric delta. Without a known goal the
     * change is neutral: the dashboard must not grade a change without context.
     */
    fun deltaDirection(metric: String, goal: String?, delta: Double): DeltaDirection {
        if (goal == null || delta == 0.0) return DeltaDirection.NEUTRAL
        val knownGoal = goal.lowercase()
        val recognized = knownGoal.contains("muscle") || knownGoal.contains("gain") ||
            knownGoal.contains("fat") || knownGoal.contains("loss") || knownGoal.contains("slim")
        if (!recognized) return DeltaDirection.NEUTRAL
        val isMuscleGain = knownGoal.contains("muscle") || knownGoal.contains("gain")
        val isFatLoss = knownGoal.contains("fat") || knownGoal.contains("loss") || knownGoal.contains("slim")
        return when (metric.lowercase()) {
            "muscle" -> if (delta > 0) DeltaDirection.FAVORABLE else DeltaDirection.UNFAVORABLE
            "body fat" -> if (delta < 0) DeltaDirection.FAVORABLE else DeltaDirection.UNFAVORABLE
            "weight" -> when {
                isMuscleGain && delta > 0 -> DeltaDirection.FAVORABLE
                isMuscleGain && delta < 0 -> DeltaDirection.UNFAVORABLE
                isFatLoss && delta < 0 -> DeltaDirection.FAVORABLE
                isFatLoss && delta > 0 -> DeltaDirection.UNFAVORABLE
                else -> DeltaDirection.NEUTRAL
            }
            else -> DeltaDirection.NEUTRAL
        }
    }

    /** Standard WHO band label for BMI; null when BMI is not measured. */
    fun bmiBand(bmi: Double?): String? = when {
        bmi == null -> null
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }

    /** Distance of BMI from the middle of the healthy band, used for movement direction. */
    private fun bmiDistanceFromBandCenter(bmi: Double): Double = abs(bmi - 21.7)

    fun bmiDirection(latest: Double?, previous: Double?): DeltaDirection {
        if (latest == null || previous == null || latest == previous) return DeltaDirection.NEUTRAL
        val latestInside = latest >= 18.5 && latest < 25.0
        if (latestInside) return DeltaDirection.NEUTRAL
        val movedToward = bmiDistanceFromBandCenter(latest) < bmiDistanceFromBandCenter(previous)
        return if (movedToward) DeltaDirection.FAVORABLE else DeltaDirection.UNFAVORABLE
    }
}
