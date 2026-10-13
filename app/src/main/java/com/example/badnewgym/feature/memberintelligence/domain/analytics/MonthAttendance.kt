package com.example.badnewgym.feature.memberintelligence.domain.analytics

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import java.time.LocalDate
import java.time.YearMonth

/**
 * Full-calendar-month attendance.
 *
 * Unlike the rolling 30-day [AttendanceWindow], this always renders every
 * valid date of the selected month (28/29/30/31 days depending on month and
 * year). Days after [today] are FUTURE; elapsed days without a record are
 * UNKNOWN - never silently converted into absences.
 *
 * Percentage uses open days only: holidays, closures, future and unknown days
 * are excluded from the denominator, never counted as absences.
 */
data class AttendanceMonth(
    val year: Int,
    val month: Int,
    val days: List<AttendanceDay>
) {
    val present: Int get() = days.count { it.state == AttendanceDayState.PRESENT }
    val late: Int get() = days.count { it.state == AttendanceDayState.LATE }
    val absent: Int get() = days.count { it.state == AttendanceDayState.ABSENT }
    val holiday: Int get() = days.count { it.state == AttendanceDayState.HOLIDAY }
    val closed: Int get() = days.count { it.state == AttendanceDayState.CLOSED }
    val future: Int get() = days.count { it.state == AttendanceDayState.FUTURE }
    val unknown: Int get() = days.count { it.state == AttendanceDayState.UNKNOWN }
    val attended: Int get() = present + late

    /** Days that actually expected the member: no holidays, closures, future or unknowns. */
    val openDays: List<AttendanceDay> get() = days.filter {
        it.state != AttendanceDayState.HOLIDAY &&
            it.state != AttendanceDayState.CLOSED &&
            it.state != AttendanceDayState.FUTURE &&
            it.state != AttendanceDayState.UNKNOWN
    }

    /** Completion over open days, 0 when there is nothing to measure. */
    val percent: Int get() {
        val open = openDays.size
        if (open <= 0) return 0
        return attended * 100 / open
    }

    val monthLabel: String get() {
        val name = java.time.Month.of(month).name.lowercase()
            .replaceFirstChar { it.uppercase() }
        return "$name $year"
    }

    /** Compact header form ("Oct 2026") where the full label would crowd controls. */
    val shortLabel: String get() {
        val name = java.time.Month.of(month).name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }
        return "$name $year"
    }
}

/**
 * Builds a complete month. [marks] carries the recorded state per epoch day;
 * any elapsed day missing from it becomes UNKNOWN rather than an invented
 * absence. [paymentDays] flags dates with a recorded payment (marker only).
 */
fun buildAttendanceMonth(
    year: Int,
    month: Int,
    today: LocalDate,
    marks: Map<Long, AttendanceDayState>,
    paymentDays: Set<Long> = emptySet(),
    /** Recorded visit minutes per epoch day; absent on days with no record. */
    minutes: Map<Long, Int> = emptyMap()
): AttendanceMonth {
    val length = YearMonth.of(year, month).lengthOfMonth()
    val days = (1..length).map { dom ->
        val date = LocalDate.of(year, month, dom)
        val epoch = date.toEpochDay()
        val state = if (date.isAfter(today)) {
            AttendanceDayState.FUTURE
        } else {
            marks[epoch] ?: AttendanceDayState.UNKNOWN
        }
        AttendanceDay(
            epochDay = epoch,
            dayOfMonth = dom,
            state = state,
            visitMinutes = minutes[epoch],
            hasPayment = epoch in paymentDays
        )
    }
    return AttendanceMonth(year, month, days)
}

/**
 * Percentage-point delta of [current] versus [previous].
 *
 * Only valid when the previous month fully elapsed before [today] and both
 * months have open days; otherwise null (no comparison is shown instead of a
 * misleading one).
 */
fun compareMonthPoints(
    current: AttendanceMonth,
    previous: AttendanceMonth?,
    today: LocalDate
): Int? {
    if (previous == null) return null
    val prevEnd = LocalDate.of(previous.year, previous.month, YearMonth.of(previous.year, previous.month).lengthOfMonth())
    if (!prevEnd.isBefore(today)) return null
    if (previous.openDays.isEmpty() || current.openDays.isEmpty()) return null
    return current.percent - previous.percent
}

/**
 * Consecutive attended days ending at the most recent elapsed day.
 * Holidays and closures are skipped, never a break; unknown days break the
 * run (no record, no claim).
 */
fun AttendanceMonth.currentStreak(): Int {
    var streak = 0
    var started = false
    val elapsed = days.filter { it.state != AttendanceDayState.FUTURE }.sortedByDescending { it.epochDay }
    elapsed.forEach { day ->
        when (day.state) {
            AttendanceDayState.HOLIDAY, AttendanceDayState.CLOSED -> Unit
            AttendanceDayState.PRESENT, AttendanceDayState.LATE -> { streak++; started = true }
            else -> if (started) return streak
        }
    }
    return streak
}

/** Longest attended run in the month, ignoring holidays and closures. */
fun AttendanceMonth.longestStreak(): Int {
    var best = 0
    var run = 0
    days.filter { it.state != AttendanceDayState.FUTURE }.sortedBy { it.epochDay }.forEach { day ->
        when (day.state) {
            AttendanceDayState.HOLIDAY, AttendanceDayState.CLOSED -> Unit
            AttendanceDayState.PRESENT, AttendanceDayState.LATE -> { run++; best = maxOf(best, run) }
            else -> run = 0
        }
    }
    return best
}
