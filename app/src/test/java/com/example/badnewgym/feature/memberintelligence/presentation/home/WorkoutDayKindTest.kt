package com.example.badnewgym.feature.memberintelligence.presentation.home

import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

/**
 * Day classification and duration formatting for the workout chart.
 *
 * The contract requires a recorded session, a day with nothing recorded, and a
 * day that has not happened yet to stay distinguishable. Folding a future day
 * into "rest day" would assert something the records do not support.
 */
class WorkoutDayKindTest {

    private val oct = YearMonth.of(2026, 10)

    private fun day(dom: Int, minutes: Int) =
        WorkoutAnalytics.WorkoutDay(
            dayOfMonth = dom,
            minutes = minutes,
            weekdayShort = "",
            dateLabel = "$dom Oct"
        )

    @Test
    fun aRecordedSessionIsAWorkoutDay() {
        val anchor = LocalDate.of(2026, 10, 16)
        assertEquals(
            WorkoutDayKind.WORKOUT,
            workoutDayKind(3, 45, anchor, oct)
        )
    }

    @Test
    fun aPastDayInsideTheMonthWithNoSessionIsARestDay() {
        val anchor = LocalDate.of(2026, 10, 16)
        assertEquals(
            WorkoutDayKind.REST_DAY,
            workoutDayKind(5, 0, anchor, oct)
        )
    }

    @Test
    fun todayIsNotFutureEvenWithoutASession() {
        val anchor = LocalDate.of(2026, 10, 16)
        assertEquals(WorkoutDayKind.REST_DAY, workoutDayKind(16, 0, anchor, oct))
    }

    @Test
    fun daysAfterTodayAreNoRecordNotRestDays() {
        val anchor = LocalDate.of(2026, 10, 16)
        assertEquals(WorkoutDayKind.NO_RECORD, workoutDayKind(17, 0, anchor, oct))
        assertEquals(WorkoutDayKind.NO_RECORD, workoutDayKind(31, 0, anchor, oct))
    }

    @Test
    fun aWholeLaterMonthHasNoRecord() {
        val anchor = LocalDate.of(2026, 10, 16)
        assertEquals(WorkoutDayKind.NO_RECORD, workoutDayKind(1, 0, anchor, YearMonth.of(2026, 11)))
    }

    @Test
    fun chipsReconcileToTheWholeMonth() {
        val anchor = LocalDate.of(2026, 10, 16)
        val days = (1..31).map { day(it, if (it in setOf(1, 2, 5)) 40 else 0) }
        val chips = workoutDayChips(days, anchor, oct)
        assertEquals(31, chips.total)
        assertEquals(3, chips.workout)
        // 1-16 happened: 16 days, 3 with sessions, the rest rest days.
        assertEquals(13, chips.rest)
        assertEquals(15, chips.noRecord)
    }

    @Test
    fun longDurationsReadAsHoursAndShortOnesAsMinutes() {
        assertEquals("2.0 hr", fmtWorkoutDuration(120))
        assertEquals("1.5 hr", fmtWorkoutDuration(90))
        assertEquals("33.5 hr", fmtWorkoutDuration(2010))
        assertEquals("20 min", fmtWorkoutDuration(20))
        assertEquals("59 min", fmtWorkoutDuration(59))
        assertEquals("1.0 hr", fmtWorkoutDuration(60))
    }
}