package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.GymWeeklySchedule
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import java.util.Calendar

/**
 * Whether a given calendar day is a gym operating day.
 *
 * `CLOSED_SCHEDULE`  – the configured weekly schedule marks the weekday as closed.
 * `CLOSED_RECORD`    – an explicit holiday/closure record exists for that date.
 * `OPEN`             – a normal operating day.
 * `UNKNOWN`          – the schedule is unknown and no record exists; the day is
 *                      never assumed closed (and never assumed open-with-data).
 */
enum class DayClosure { OPEN, CLOSED_SCHEDULE, CLOSED_RECORD, UNKNOWN }

/**
 * One day of the combined attendance + workout view.
 *
 * [workoutMinutes] stays `null` when the day has no workout record: missing data is
 * never coerced to a zero-duration workout. [closure] keeps attendance and workout
 * interpretation separate so a closed day can never read as member inactivity.
 */
data class CombinedDay(
    val dayOfMonth: Int,
    val date: Long,
    val attendance: AttendanceDayStatus,
    val closure: DayClosure,
    val workoutMinutes: Int?,
    val workoutStatus: WorkoutDayStatus
) {
    /** A confirmed closure never counts against attendance or workout discipline. */
    val isGymClosed: Boolean
        get() = closure == DayClosure.CLOSED_SCHEDULE || closure == DayClosure.CLOSED_RECORD

    /** Counted as an attended day. `LATE` is a visit; `UNKNOWN` is not. */
    val isVisit: Boolean
        get() = attendance == AttendanceDayStatus.PRESENT || attendance == AttendanceDayStatus.LATE

    /**
     * A day carries a real workout only when it is recorded **and** the duration is a
     * positive measurement.
     *
     * A `RECORDED` day with a null or non-positive duration is contradictory data: it
     * is not evidence of a session. Treating it as one would both draw a bar in the
     * chart and drag the monthly average down, so it is read as "no workout" instead.
     */
    val hasRecordedWorkout: Boolean
        get() = !isGymClosed &&
            workoutStatus == WorkoutDayStatus.RECORDED &&
            (workoutMinutes ?: 0) > 0
}

/**
 * Builds the per-day combined model for one calendar month.
 *
 * Attendance and workout stay independent: an attendance record never invents a
 * workout, and a workout record never invents attendance.
 */
fun buildCombinedDays(
    attendanceHistory: List<AttendanceDay>,
    workoutHistory: List<WorkoutDay>,
    monthKey: Long,
    schedule: GymWeeklySchedule?
): List<CombinedDay> {
    val attendanceByDay = attendanceHistory
        .filter { DashboardMath.monthKey(it.date) == monthKey }
        .associateBy { dayOfMonthOf(it.date) }
    val workoutByDay = workoutHistory
        .filter { DashboardMath.monthKey(it.date) == monthKey }
        .associateBy { dayOfMonthOf(it.date) }
    val dayCount = DashboardMath.daysInMonthForMonthKey(monthKey)

    return (1..dayCount).map { day ->
        val date = DashboardMath.dateForMonthDay(monthKey, day)
        val attendance = attendanceByDay[day]?.status ?: AttendanceDayStatus.UNKNOWN
        val workout = workoutByDay[day]
        val weekday = Calendar.getInstance().apply { timeInMillis = date }.get(Calendar.DAY_OF_WEEK)
        val closure = when {
            attendance == AttendanceDayStatus.HOLIDAY || attendance == AttendanceDayStatus.CLOSED ->
                DayClosure.CLOSED_RECORD
            // No schedule configured: the day is genuinely unknown. It is never assumed
            // closed, and it is never assumed to be an open day with data either.
            schedule == null -> DayClosure.UNKNOWN
            schedule.isClosedOnWeekday(weekday) -> DayClosure.CLOSED_SCHEDULE
            else -> DayClosure.OPEN
        }
        CombinedDay(
            dayOfMonth = day,
            date = date,
            attendance = attendance,
            closure = closure,
            workoutMinutes = workout?.durationMinutes,
            workoutStatus = workout?.status ?: WorkoutDayStatus.UNKNOWN
        )
    }
}

/** Day-of-month (1..31) for an epoch-millis timestamp. */
fun dayOfMonthOf(date: Long): Int =
    Calendar.getInstance().apply { timeInMillis = date }.get(Calendar.DAY_OF_MONTH)

/**
 * Attendance rate over **recorded operating days only**.
 *
 * Two exclusions, for two different reasons:
 *  * gym-closed days, so a closure can never look like an absence;
 *  * days with no recorded outcome (`UNKNOWN`), which are not evidence of either way.
 *
 * Days still in the future inside the current month are `UNKNOWN`, so they can never
 * depress the rate. Returns null when no day in the month carries a known outcome.
 */
fun attendanceRateExcludingClosures(days: List<CombinedDay>): Int? {
    val recorded = days.filterNot { it.isGymClosed }
        .filter { it.attendance != AttendanceDayStatus.UNKNOWN }
    if (recorded.isEmpty()) return null
    val visits = recorded.count { it.isVisit }
    return ((visits.toDouble() / recorded.size) * 100).toInt()
}

/** Number of operating days (closures removed) in the given month model. */
fun operatingDayCount(days: List<CombinedDay>): Int = days.count { !it.isGymClosed }

/**
 * Counts present / absent / late / off for the month.
 *
 * `off` merges schedule closures and explicit closure records, and is reported
 * separately from `absent` so the two are never conflated.
 *
 * The buckets are a **disjoint partition of [monthDays]**: any day is at most one of
 * present / absent / late / off / unknown. `off` is *not* a subset of
 * [operatingDays] — closures are exactly the days removed from it — so a share taken
 * against [operatingDays] could exceed 100%. Shares must be taken against [monthDays].
 */
data class AttendanceBreakdown(
    val present: Int,
    val absent: Int,
    val late: Int,
    val off: Int,
    val unknown: Int,
    val operatingDays: Int,
    /** Every calendar day in the month; the buckets above partition it. */
    val monthDays: Int
)

fun attendanceBreakdown(days: List<CombinedDay>): AttendanceBreakdown = AttendanceBreakdown(
    present = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.PRESENT },
    absent = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.ABSENT },
    late = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.LATE },
    off = days.count { it.isGymClosed },
    unknown = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.UNKNOWN },
    operatingDays = operatingDayCount(days),
    monthDays = days.size
)

/**
 * Average workout minutes per **recorded** day in the month.
 *
 * Only days with a real recorded session ([CombinedDay.hasRecordedWorkout]) count.
 * Unknown days, gym-closed days and contradictory zero/non-positive "recorded" days
 * are excluded rather than averaged in as zero. Null when nothing was recorded.
 */
fun averageRecordedMinutes(days: List<CombinedDay>): Int? {
    val recorded = days.filter { it.hasRecordedWorkout }.mapNotNull { it.workoutMinutes }
    if (recorded.isEmpty()) return null
    return (recorded.sum().toDouble() / recorded.size).toInt()
}

/** Peak recorded session in the month, or null when nothing was recorded. */
fun peakRecordedMinutes(days: List<CombinedDay>): Pair<Int, Int>? =
    days.filter { it.hasRecordedWorkout }
        .maxByOrNull { it.workoutMinutes!! }
        ?.let { it.workoutMinutes!! to it.dayOfMonth }

/** Lowest recorded session in the month, or null when nothing was recorded. */
fun lowestRecordedMinutes(days: List<CombinedDay>): Pair<Int, Int>? =
    days.filter { it.hasRecordedWorkout }
        .minByOrNull { it.workoutMinutes!! }
        ?.let { it.workoutMinutes!! to it.dayOfMonth }

/** Highest recorded minutes in the month, used to scale the chart axis. Null when empty. */
fun chartCeilingMinutes(days: List<CombinedDay>): Int =
    days.filter { it.hasRecordedWorkout }
        .mapNotNull { it.workoutMinutes }
        .maxOrNull()
        ?.let { maxOf(60, ((it + 29) / 30) * 30) }
        ?: 60