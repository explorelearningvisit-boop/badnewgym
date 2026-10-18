package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.GymWeeklySchedule
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * Data-rule tests for the combined attendance + workout card.
 *
 * These lock the honesty rules: missing is never zero, a gym closure is never an
 * absence, Sunday is never automatically a holiday, and closures stay out of the
 * attendance denominator and the workout averages.
 */
class AttendanceWorkoutAnalyticsTest {

    private fun dateIn(year: Int, month: Int, day: Int): Long = Calendar.getInstance().apply {
        clear()
        set(year, month, day, 12, 0, 0)
    }.timeInMillis

    // October 2026: the 1st is a Thursday, 31 days.
    private val october2026 = DashboardMath.monthKey(dateIn(2026, Calendar.OCTOBER, 1))

    @Test
    fun `month model covers every calendar day of the month`() {
        val days = buildCombinedDays(emptyList(), emptyList(), october2026, null)
        assertEquals(31, days.size)
        assertEquals(1, days.first().dayOfMonth)
        assertEquals(31, days.last().dayOfMonth)
    }

    @Test
    fun `sunday is not treated as a holiday when no closure is configured`() {
        // 2026-10-04 and 2026-10-11 are Sundays.
        val sunday = dateIn(2026, Calendar.OCTOBER, 4)
        val days = buildCombinedDays(
            attendanceHistory = listOf(AttendanceDay(sunday, AttendanceDayStatus.PRESENT)),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = GymWeeklySchedule.OPEN_EVERY_DAY
        )
        val sundayDay = days.first { it.dayOfMonth == 4 }
        assertFalse("Sunday must not be an implicit closure", sundayDay.isGymClosed)
        assertEquals(AttendanceDayStatus.PRESENT, sundayDay.attendance)
    }

    @Test
    fun `configured closure day reports gym off and is excluded from the attendance rate`() {
        // Mondays closed. In October 2026 those are the 5th, 12th, 19th and 26th.
        val schedule = GymWeeklySchedule(closedWeekdays = setOf(Calendar.MONDAY))
        val attendance = listOf(
            6 to AttendanceDayStatus.PRESENT,
            7 to AttendanceDayStatus.PRESENT,
            8 to AttendanceDayStatus.PRESENT,
            9 to AttendanceDayStatus.ABSENT
        ).map { (day, status) ->
            AttendanceDay(dateIn(2026, Calendar.OCTOBER, day), status)
        }
        val days = buildCombinedDays(attendance, emptyList(), october2026, schedule)

        val closureDay = days.first { it.dayOfMonth == 5 }
        assertTrue(closureDay.isGymClosed)
        assertEquals(DayClosure.CLOSED_SCHEDULE, closureDay.closure)

        val breakdown = attendanceBreakdown(days)
        assertEquals(3, breakdown.present)
        assertEquals(1, breakdown.absent)
        assertEquals("all four Mondays are gym off", 4, breakdown.off)
        assertEquals("31 days minus 4 closures", 27, breakdown.operatingDays)
        assertEquals("the breakdown partitions the month", 31, breakdown.monthDays)
        // 3 visits out of the 4 days that carry a recorded outcome (3 present, 1 absent).
        // The other 23 days are unrecorded, so they are excluded rather than counted
        // as absences, and the 4 closures never enter the denominator at all.
        assertEquals(75, attendanceRateExcludingClosures(days))
    }

    @Test
    fun `a present record on a configured closure day does not resurrect the day`() {
        // A stale/incorrect ledger marking a closure as present must still read as
        // "Gym Off": the schedule outranks the attendance record.
        val schedule = GymWeeklySchedule(closedWeekdays = setOf(Calendar.MONDAY))
        val days = buildCombinedDays(
            attendanceHistory = listOf(
                AttendanceDay(dateIn(2026, Calendar.OCTOBER, 5), AttendanceDayStatus.PRESENT)
            ),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = schedule
        )
        val day = days.first { it.dayOfMonth == 5 }
        assertTrue(day.isGymClosed)
        assertEquals(0, attendanceBreakdown(days).present)
    }

    @Test
    fun `explicit closure record wins over an open schedule`() {
        val closureDate = dateIn(2026, Calendar.OCTOBER, 7)
        val days = buildCombinedDays(
            attendanceHistory = listOf(AttendanceDay(closureDate, AttendanceDayStatus.HOLIDAY)),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = GymWeeklySchedule.OPEN_EVERY_DAY
        )
        val day = days.first { it.dayOfMonth == 7 }
        assertEquals(DayClosure.CLOSED_RECORD, day.closure)
        assertTrue(day.isGymClosed)
    }

    @Test
    fun `closure is not counted as absent`() {
        val closureDate = dateIn(2026, Calendar.OCTOBER, 7)
        val absentDate = dateIn(2026, Calendar.OCTOBER, 9)
        val days = buildCombinedDays(
            attendanceHistory = listOf(
                AttendanceDay(closureDate, AttendanceDayStatus.HOLIDAY),
                AttendanceDay(absentDate, AttendanceDayStatus.ABSENT)
            ),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = null
        )
        val breakdown = attendanceBreakdown(days)
        assertEquals("closure must never inflate the absent count", 1, breakdown.absent)
        assertEquals(1, breakdown.off)
    }

    @Test
    fun `missing workout data is never a zero duration`() {
        val days = buildCombinedDays(
            attendanceHistory = listOf(
                AttendanceDay(dateIn(2026, Calendar.OCTOBER, 3), AttendanceDayStatus.PRESENT)
            ),
            // No workout record at all for this day.
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = null
        )
        val day = days.first { it.dayOfMonth == 3 }
        assertNull("missing workout must stay null, not 0", day.workoutMinutes)
        assertEquals(WorkoutDayStatus.UNKNOWN, day.workoutStatus)
        assertNull(averageRecordedMinutes(days))
    }

    @Test
    fun `average counts recorded days only and ignores unknown and closed days`() {
        val workout = listOf(
            WorkoutDay(dateIn(2026, Calendar.OCTOBER, 2), 40, WorkoutDayStatus.RECORDED),
            WorkoutDay(dateIn(2026, Calendar.OCTOBER, 3), 60, WorkoutDayStatus.RECORDED),
            // Day 4 has no record at all.
            WorkoutDay(dateIn(2026, Calendar.OCTOBER, 5), 0, WorkoutDayStatus.CONFIRMED_NO_WORKOUT)
        )
        val schedule = GymWeeklySchedule(closedWeekdays = setOf(Calendar.MONDAY))
        val days = buildCombinedDays(emptyList(), workout, october2026, schedule)
        // 40 and 60 recorded -> 50. Day 5 is a confirmed no-workout, not a zero average.
        assertEquals(50, averageRecordedMinutes(days))
        assertEquals(60, peakRecordedMinutes(days)?.first)
        assertEquals(40, lowestRecordedMinutes(days)?.first)
    }

    @Test
    fun `workout on a closed day is not interpreted as member inactivity`() {
        val closureDate = dateIn(2026, Calendar.OCTOBER, 5)
        val days = buildCombinedDays(
            attendanceHistory = emptyList(),
            workoutHistory = listOf(
                WorkoutDay(closureDate, null, WorkoutDayStatus.UNKNOWN)
            ),
            monthKey = october2026,
            schedule = GymWeeklySchedule(closedWeekdays = setOf(Calendar.MONDAY))
        )
        val closed = days.first { it.dayOfMonth == 5 }
        assertTrue(closed.isGymClosed)
        assertFalse("a closed day must not become a confirmed no-workout", closed.workoutStatus == WorkoutDayStatus.CONFIRMED_NO_WORKOUT)
        assertNull(closed.workoutMinutes)
    }

    @Test
    fun `late days count as visits`() {
        val lateDate = dateIn(2026, Calendar.OCTOBER, 6)
        val days = buildCombinedDays(
            attendanceHistory = listOf(AttendanceDay(lateDate, AttendanceDayStatus.LATE)),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = null
        )
        val late = days.first { it.dayOfMonth == 6 }
        assertTrue(late.isVisit)
        assertEquals(1, attendanceBreakdown(days).late)
    }

    @Test
    fun `attendance rate ignores days with no recorded outcome`() {
        val days = buildCombinedDays(
            attendanceHistory = listOf(
                AttendanceDay(dateIn(2026, Calendar.OCTOBER, 2), AttendanceDayStatus.PRESENT),
                AttendanceDay(dateIn(2026, Calendar.OCTOBER, 3), AttendanceDayStatus.ABSENT),
                AttendanceDay(dateIn(2026, Calendar.OCTOBER, 4), AttendanceDayStatus.LATE)
            ),
            workoutHistory = emptyList(),
            monthKey = october2026,
            schedule = null
        )
        val breakdown = attendanceBreakdown(days)
        assertEquals(1, breakdown.present)
        assertEquals(1, breakdown.absent)
        assertEquals(1, breakdown.late)
        assertEquals("no schedule means no closures", 31, breakdown.operatingDays)
        assertEquals("the other 28 days are unrecorded", 28, breakdown.unknown)
        assertEquals(31, breakdown.monthDays)
        // 2 visits (present + late) out of the 3 days with a known outcome. The 28
        // unrecorded days must not be treated as absences: that would read 6%.
        assertEquals(66, attendanceRateExcludingClosures(days))
    }

    @Test
    fun `a null schedule marks days unknown rather than assumed open`() {
        val days = buildCombinedDays(emptyList(), emptyList(), october2026, null)
        assertTrue(
            "with no configured schedule every day is UNKNOWN, never silently OPEN",
            days.all { it.closure == DayClosure.UNKNOWN }
        )
        assertFalse("UNKNOWN is not a closure", days.any { it.isGymClosed })
        assertNull(attendanceRateExcludingClosures(days))
    }

    @Test
    fun `off share and the other counters partition the month without exceeding 100 percent`() {
        val schedule = GymWeeklySchedule(closedWeekdays = setOf(Calendar.MONDAY))
        val attendance = listOf(
            6 to AttendanceDayStatus.PRESENT,
            7 to AttendanceDayStatus.PRESENT,
            8 to AttendanceDayStatus.PRESENT,
            9 to AttendanceDayStatus.ABSENT
        ).map { (day, status) -> AttendanceDay(dateIn(2026, Calendar.OCTOBER, day), status) }
        val breakdown = attendanceBreakdown(buildCombinedDays(attendance, emptyList(), october2026, schedule))

        val sum = breakdown.present + breakdown.absent + breakdown.late + breakdown.off + breakdown.unknown
        assertEquals("buckets must partition the month", breakdown.monthDays, sum)
        assertEquals(
            "off must be a share of the month, not of the operating days it is excluded from",
            4 * 100 / 31,
            breakdown.off * 100 / breakdown.monthDays
        )
        assertTrue("no bucket share may exceed the month", breakdown.off <= breakdown.monthDays)
    }

    @Test
    fun `chart ceiling is a rounded minute scale with a 60m floor`() {
        val workout = listOf(
            WorkoutDay(dateIn(2026, Calendar.OCTOBER, 2), 47, WorkoutDayStatus.RECORDED)
        )
        val days = buildCombinedDays(emptyList(), workout, october2026, null)
        assertEquals(60, chartCeilingMinutes(days))

        val longer = listOf(
            WorkoutDay(dateIn(2026, Calendar.OCTOBER, 2), 75, WorkoutDayStatus.RECORDED)
        )
        val days2 = buildCombinedDays(emptyList(), longer, october2026, null)
        assertEquals(90, chartCeilingMinutes(days2))
    }

    @Test
    fun `month key helpers agree with the timestamp helpers`() {
        val first = dateIn(2026, Calendar.OCTOBER, 1)
        assertEquals(october2026, DashboardMath.monthKey(first))
        assertEquals(31, DashboardMath.daysInMonthForMonthKey(october2026))
        // 2028 is a leap year: February must have 29 days.
        val feb2028 = DashboardMath.monthKey(dateIn(2028, Calendar.FEBRUARY, 1))
        assertEquals(29, DashboardMath.daysInMonthForMonthKey(feb2028))
        assertNotNull(DashboardMath.dateForMonthDay(october2026, 17))
    }
}