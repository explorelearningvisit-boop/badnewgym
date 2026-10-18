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
            schedule != null && schedule.isClosedOnWeekday(weekday) -> DayClosure.CLOSED_SCHEDULE
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
 * Attendance rate over **operating days only**.
 *
 * Gym-closed days are removed from the denominator so a closure can never look like
 * an absence. Returns null when the month has no known operating day.
 */
fun attendanceRateExcludingClosures(days: List<CombinedDay>): Int? {
    val operating = days.filterNot { it.isGymClosed }
    if (operating.isEmpty()) return null
    val visits = operating.count { it.isVisit }
    return ((visits.toDouble() / operating.size) * 100).toInt()
}

/** Number of operating days (closures removed) in the given month model. */
fun operatingDayCount(days: List<CombinedDay>): Int = days.count { !it.isGymClosed }

/**
 * Counts present / absent / late / off for the month.
 *
 * `off` merges schedule closures and explicit closure records, and is reported
 * separately from `absent` so the two are never conflated.
 */
data class AttendanceBreakdown(
    val present: Int,
    val absent: Int,
    val late: Int,
    val off: Int,
    val unknown: Int,
    val operatingDays: Int
)

fun attendanceBreakdown(days: List<CombinedDay>): AttendanceBreakdown = AttendanceBreakdown(
    present = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.PRESENT },
    absent = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.ABSENT },
    late = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.LATE },
    off = days.count { it.isGymClosed },
    unknown = days.count { !it.isGymClosed && it.attendance == AttendanceDayStatus.UNKNOWN },
    operatingDays = operatingDayCount(days)
)

/**
 * Average workout minutes per **recorded** day in the month.
 *
 * Only `RECORDED` days with a duration count. Unknown days and gym-closed days are
 * excluded rather than averaged in as zero. Null when nothing was recorded.
 */
fun averageRecordedMinutes(days: List<CombinedDay>): Int? {
    val recorded = days.filterNot { it.isGymClosed }
        .filter { it.workoutStatus == WorkoutDayStatus.RECORDED && it.workoutMinutes != null }
        .mapNotNull { it.workoutMinutes }
    if (recorded.isEmpty()) return null
    return (recorded.sum().toDouble() / recorded.size).toInt()
}

/** Peak recorded session in the month, or null when nothing was recorded. */
fun peakRecordedMinutes(days: List<CombinedDay>): Pair<Int, Int>? =
    days.filterNot { it.isGymClosed }
        .filter { it.workoutStatus == WorkoutDayStatus.RECORDED && it.workoutMinutes != null }
        .maxByOrNull { it.workoutMinutes!! }
        ?.let { it.workoutMinutes!! to it.dayOfMonth }

/** Lowest recorded session in the month, or null when nothing was recorded. */
fun lowestRecordedMinutes(days: List<CombinedDay>): Pair<Int, Int>? =
    days.filterNot { it.isGymClosed }
        .filter { it.workoutStatus == WorkoutDayStatus.RECORDED && it.workoutMinutes != null }
        .minByOrNull { it.workoutMinutes!! }
        ?.let { it.workoutMinutes!! to it.dayOfMonth }

/** Highest recorded minutes in the month, used to scale the chart axis. Null when empty. */
fun chartCeilingMinutes(days: List<CombinedDay>): Int =
    days.filterNot { it.isGymClosed }
        .mapNotNull { it.workoutMinutes }
        .maxOrNull()
        ?.let { maxOf(60, ((it + 29) / 30) * 30) }
        ?: 60