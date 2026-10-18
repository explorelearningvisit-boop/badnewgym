package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class DashboardMathTest {

    private val day = DashboardMath.DAY_MS

    private fun october2026(dayOfMonth: Int, hour: Int = 12): Long =
        Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, dayOfMonth, hour, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    // ---------------------------------------------------------------- membership ring

    @Test
    fun `elapsed percent reflects consumed plan term`() {
        val membership = MembershipStatus(
            planName = "Gold Plan", planType = "12 Months",
            startDate = october2026(1) - day * 300,
            expiryDate = october2026(1) + day * 65,
            daysRemaining = 65, isActive = true
        )
        assertEquals(300.0 / 365.0 * 100, DashboardMath.elapsedPercent(membership, october2026(1)).toDouble(), 0.6)
    }

    @Test
    fun `elapsed percent never exceeds the plan bounds`() {
        val membership = MembershipStatus(
            planName = "Gold Plan", planType = "12 Months",
            startDate = october2026(1) - day * 10,
            expiryDate = october2026(1) + day * 20,
            daysRemaining = 20, isActive = true
        )
        assertEquals(100, DashboardMath.elapsedPercent(membership, october2026(1) + day * 40))
        assertEquals(0, DashboardMath.elapsedPercent(membership, october2026(1) - day * 40))
    }

    @Test
    fun `days remaining never goes negative and caps at the reported value`() {
        val membership = MembershipStatus(
            planName = "Gold Plan", planType = "12 Months",
            startDate = october2026(1) - day * 10,
            expiryDate = october2026(1) - day * 5,
            daysRemaining = 30, isActive = false
        )
        assertEquals(0, DashboardMath.daysRemaining(membership, october2026(1)))
    }

    @Test
    fun `days remaining rounds a partial future day up`() {
        val now = october2026(1)
        val membership = MembershipStatus(
            planName = "Prime Plan", planType = "100 Days",
            startDate = now - DashboardMath.DAY_MS * 75,
            expiryDate = now + DashboardMath.DAY_MS * 25 - 1,
            daysRemaining = 25, isActive = true
        )
        assertEquals(25, DashboardMath.daysRemaining(membership, now))
    }

    @Test
    fun `membership without plan is treated as zero elapsed`() {
        assertEquals(0, DashboardMath.elapsedPercent(null, october2026(1)))
    }

    // ---------------------------------------------------------------- attendance

    @Test
    fun `attendance percent divides visits by target`() {
        assertEquals(62, DashboardMath.attendancePercent(16, 26))
        assertEquals(77, DashboardMath.attendancePercent(24, 31))
    }

    @Test
    fun `attendance percent is not computable without target`() {
        assertNull(DashboardMath.attendancePercent(16, null))
        assertNull(DashboardMath.attendancePercent(16, 0))
    }

    @Test
    fun `status counts come from the current calendar month only`() {
        val days = listOf(
            AttendanceDay(october2026(1), AttendanceDayStatus.PRESENT),
            AttendanceDay(october2026(2), AttendanceDayStatus.LATE),
            AttendanceDay(october2026(3), AttendanceDayStatus.UNKNOWN),
            AttendanceDay(october2026(1) - day * 40, AttendanceDayStatus.PRESENT) // September, excluded
        )
        val counts = DashboardMath.statusCounts(days, october2026(5))
        assertEquals(AttendanceDayStatus.entries.size, counts.size) // full status map shape
        assertEquals(1, counts[AttendanceDayStatus.PRESENT])
        assertEquals(1, counts[AttendanceDayStatus.LATE])
        assertEquals(1, counts[AttendanceDayStatus.UNKNOWN])
        assertEquals(0, counts[AttendanceDayStatus.ABSENT])
    }

    @Test
    fun `october 2026 starts on a thursday column`() {
        // 1 Oct 2026 is a Thursday; Sunday-first layout uses offset 4.
        assertEquals(4, DashboardMath.firstWeekdayOffset(october2026(1)))
        assertEquals(4, DashboardMath.firstWeekdayOffset(october2026(18)))
    }

    @Test
    fun `october 2026 has thirty one days`() {
        assertEquals(31, DashboardMath.daysInMonth(october2026(15)))
    }

    @Test
    fun `month-key helpers agree with the timestamp helpers for the same month`() {
        val octoberKey = DashboardMath.monthKey(october2026(15))
        assertEquals(4, DashboardMath.firstWeekdayOffsetForMonth(octoberKey))
        assertEquals(31, DashboardMath.daysInMonthForMonth(octoberKey))
    }

    @Test
    fun `february in a leap year has twenty nine days`() {
        val feb2028Key = DashboardMath.monthKey(
            Calendar.getInstance().apply {
                set(2028, Calendar.FEBRUARY, 10, 12, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        )
        assertEquals(29, DashboardMath.daysInMonthForMonth(feb2028Key))
    }

    @Test
    fun `month-key status counts include only the requested month`() {
        val septemberKey = DashboardMath.monthKey(october2026(1) - day * 5)
        val octoberKey = DashboardMath.monthKey(october2026(15))
        val days = listOf(
            AttendanceDay(october2026(1), AttendanceDayStatus.PRESENT),
            AttendanceDay(october2026(2), AttendanceDayStatus.LATE),
            AttendanceDay(october2026(1) - day * 5, AttendanceDayStatus.PRESENT) // September
        )
        val octoberCounts = DashboardMath.statusCountsForMonth(days, octoberKey)
        assertEquals(1, octoberCounts[AttendanceDayStatus.PRESENT])
        assertEquals(1, octoberCounts[AttendanceDayStatus.LATE])
        assertEquals(0, octoberCounts[AttendanceDayStatus.ABSENT])

        val septemberCounts = DashboardMath.statusCountsForMonth(days, septemberKey)
        assertEquals(1, septemberCounts[AttendanceDayStatus.PRESENT])
        assertEquals(0, septemberCounts[AttendanceDayStatus.LATE])
    }

    // ---------------------------------------------------------------- workout

    private fun workout(date: Long, minutes: Int?, status: WorkoutDayStatus = WorkoutDayStatus.RECORDED) =
        WorkoutDay(date, minutes, status)

    @Test
    fun `recorded average ignores unknown and confirmed no-workout days`() {
        val days = listOf(
            workout(october2026(1), 40),
            workout(october2026(2), null, WorkoutDayStatus.UNKNOWN),
            workout(october2026(3), 0, WorkoutDayStatus.CONFIRMED_NO_WORKOUT),
            workout(october2026(4), 50)
        )
        assertEquals(45, DashboardMath.recordedAverageMinutes(days))
    }

    @Test
    fun `recorded average is null without recorded minutes`() {
        val days = listOf(
            workout(october2026(1), null, WorkoutDayStatus.UNKNOWN),
            workout(october2026(2), 0, WorkoutDayStatus.CONFIRMED_NO_WORKOUT)
        )
        assertNull(DashboardMath.recordedAverageMinutes(days))
    }

    @Test
    fun `buckets separate above and below average sessions`() {
        val recorded = workout(october2026(1), 70)
        val low = workout(october2026(2), 20)
        val noShow = workout(october2026(3), 0, WorkoutDayStatus.CONFIRMED_NO_WORKOUT)
        val unknown = workout(october2026(4), null, WorkoutDayStatus.UNKNOWN)
        assertEquals(DashboardMath.WorkoutBucket.ABOVE_AVERAGE, DashboardMath.workoutBucket(recorded, 45))
        assertEquals(DashboardMath.WorkoutBucket.BELOW_AVERAGE, DashboardMath.workoutBucket(low, 45))
        assertEquals(DashboardMath.WorkoutBucket.NO_WORKOUT, DashboardMath.workoutBucket(noShow, 45))
        assertEquals(DashboardMath.WorkoutBucket.NO_RECORD, DashboardMath.workoutBucket(unknown, 45))
    }

    @Test
    fun `month averages use only that month's recorded sessions`() {
        val days = listOf(
            workout(october2026(1), 40),
            workout(october2026(3), 60),
            workout(october2026(1) - day * 31, 20),
            workout(october2026(2), null, WorkoutDayStatus.UNKNOWN)
        )
        assertEquals(50.0, DashboardMath.averageForMonth(days, DashboardMath.monthKey(october2026(2)))!!, 0.01)
        assertEquals(20.0, DashboardMath.averageForMonth(days, DashboardMath.monthKey(october2026(1) - day * 31))!!, 0.01)
    }

    @Test
    fun `month over month percent needs a previous average`() {
        assertEquals(50.0, DashboardMath.monthOverMonthPercent(60.0, 40.0)!!, 0.01)
        assertNull(DashboardMath.monthOverMonthPercent(60.0, null))
        assertNull(DashboardMath.monthOverMonthPercent(60.0, 0.0))
        assertNull(DashboardMath.monthOverMonthPercent(null, 40.0))
    }

    // ---------------------------------------------------------------- payment timing

    @Test
    fun `payment before the due date counts as on time`() {
        assertEquals(PaymentTimeliness.ON_TIME, DashboardMath.timelinessFor(october2026(10), october2026(15)))
        assertEquals(PaymentTimeliness.ON_TIME, DashboardMath.timelinessFor(october2026(15), october2026(15)))
    }

    @Test
    fun `payment within two weeks late counts as late`() {
        assertEquals(PaymentTimeliness.LATE, DashboardMath.timelinessFor(october2026(29), october2026(15)))
    }

    @Test
    fun `payment more than two weeks late counts as very late`() {
        assertEquals(PaymentTimeliness.VERY_LATE, DashboardMath.timelinessFor(october2026(30), october2026(15)))
    }

    @Test
    fun `unknown due date keeps payment timing unknown`() {
        assertEquals(PaymentTimeliness.UNKNOWN, DashboardMath.timelinessFor(october2026(10), null))
    }

    // ---------------------------------------------------------------- body progress

    @Test
    fun `muscle gain grades weight gains as favourable`() {
        assertEquals(
            DashboardMath.DeltaDirection.FAVORABLE,
            DashboardMath.deltaDirection("weight", "Muscle Gain", 2.0)
        )
        assertEquals(
            DashboardMath.DeltaDirection.UNFAVORABLE,
            DashboardMath.deltaDirection("weight", "Muscle Gain", -2.0)
        )
    }

    @Test
    fun `fat loss grades weight drops as favourable`() {
        assertEquals(
            DashboardMath.DeltaDirection.FAVORABLE,
            DashboardMath.deltaDirection("weight", "Fat Loss", -2.0)
        )
    }

    @Test
    fun `muscle growth is favourable for known goals`() {
        assertEquals(
            DashboardMath.DeltaDirection.FAVORABLE,
            DashboardMath.deltaDirection("muscle", "Muscle Gain", 1.2)
        )
        assertEquals(
            DashboardMath.DeltaDirection.UNFAVORABLE,
            DashboardMath.deltaDirection("muscle", "Muscle Gain", -0.4)
        )
    }

    @Test
    fun `unknown goal keeps every change neutral`() {
        assertEquals(
            DashboardMath.DeltaDirection.NEUTRAL,
            DashboardMath.deltaDirection("weight", null, 3.0)
        )
        assertEquals(
            DashboardMath.DeltaDirection.NEUTRAL,
            DashboardMath.deltaDirection("body fat", "Unrelated Goal Text", -1.0)
        )
    }

    @Test
    fun `bmi bands follow the standard thresholds`() {
        assertEquals("Underweight", DashboardMath.bmiBand(17.0))
        assertEquals("Normal", DashboardMath.bmiBand(22.9))
        assertEquals("Overweight", DashboardMath.bmiBand(27.0))
        assertEquals("Obese", DashboardMath.bmiBand(31.5))
        assertNull(DashboardMath.bmiBand(null))
    }

    @Test
    fun `bmi movement toward the healthy band is favourable`() {
        assertEquals(
            DashboardMath.DeltaDirection.FAVORABLE,
            DashboardMath.bmiDirection(26.0, 29.0)
        )
        assertEquals(
            DashboardMath.DeltaDirection.UNFAVORABLE,
            DashboardMath.bmiDirection(28.0, 25.5)
        )
        assertEquals(
            DashboardMath.DeltaDirection.NEUTRAL,
            DashboardMath.bmiDirection(22.0, 24.0)
        )
    }

    // ---------------------------------------------------------------- month keys

    @Test
    fun `month keys cross the year boundary`() {
        val december = Calendar.getInstance().apply { set(2026, Calendar.DECEMBER, 15) }.timeInMillis
        val january = Calendar.getInstance().apply { set(2027, Calendar.JANUARY, 15) }.timeInMillis
        assertEquals(DashboardMath.previousMonthKey(DashboardMath.monthKey(january)), DashboardMath.monthKey(december))
        assertFalse(DashboardMath.monthKey(january) == DashboardMath.monthKey(december))
        assertTrue(DashboardMath.monthKey(january) - DashboardMath.monthKey(december) == 1L)
    }
}
