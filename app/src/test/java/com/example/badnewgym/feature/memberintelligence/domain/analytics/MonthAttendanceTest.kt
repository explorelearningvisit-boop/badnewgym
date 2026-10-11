package com.example.badnewgym.feature.memberintelligence.domain.analytics

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Full-month calendar contracts: every valid date rendered, correct
 * denominators, honest comparisons.
 */
class MonthAttendanceTest {

    private val today = LocalDate.of(2026, 10, 11)

    private fun fullMarks(year: Int, month: Int, state: AttendanceDayState): Map<Long, AttendanceDayState> {
        val length = java.time.YearMonth.of(year, month).lengthOfMonth()
        return (1..length).associate { dom ->
            LocalDate.of(year, month, dom).toEpochDay() to state
        }
    }

    @Test
    fun februaryHas28DaysIn2026And29In2024() {
        val feb26 = buildAttendanceMonth(2026, 2, LocalDate.of(2026, 3, 1), fullMarks(2026, 2, AttendanceDayState.PRESENT))
        assertEquals(28, feb26.days.size)
        val feb24 = buildAttendanceMonth(2024, 2, LocalDate.of(2024, 3, 1), fullMarks(2024, 2, AttendanceDayState.PRESENT))
        assertEquals(29, feb24.days.size)
    }

    @Test
    fun october2026Has31DaysWithFutureAfterToday() {
        val marks = fullMarks(2026, 10, AttendanceDayState.PRESENT)
        val month = buildAttendanceMonth(2026, 10, today, marks)
        assertEquals(31, month.days.size)
        assertEquals((1..31).toList(), month.days.map { it.dayOfMonth })
        assertEquals(11, month.days.count { it.state == AttendanceDayState.PRESENT })
        assertEquals(20, month.future)
        assertEquals("October 2026", month.monthLabel)
    }

    @Test
    fun unmarkedPastDaysAreUnknownNeverAbsent() {
        val month = buildAttendanceMonth(2026, 10, today, emptyMap())
        assertEquals(11, month.unknown)
        assertEquals(0, month.absent)
        assertEquals(0, month.openDays.size)
        assertEquals(0, month.percent)
    }

    @Test
    fun percentExcludesHolidaysClosuresFutureAndUnknown() {
        val marks = mutableMapOf<Long, AttendanceDayState>()
        // Oct 1-11: 6 present, 1 late, 1 absent, 1 holiday, 1 closed, 1 unknown.
        val states = listOf(
            AttendanceDayState.PRESENT, AttendanceDayState.PRESENT, AttendanceDayState.PRESENT,
            AttendanceDayState.PRESENT, AttendanceDayState.PRESENT, AttendanceDayState.PRESENT,
            AttendanceDayState.LATE, AttendanceDayState.ABSENT, AttendanceDayState.HOLIDAY,
            AttendanceDayState.CLOSED
        )
        (1..10).forEach { dom ->
            marks[LocalDate.of(2026, 10, dom).toEpochDay()] = states[dom - 1]
        }
        val month = buildAttendanceMonth(2026, 10, today, marks)
        assertEquals(6, month.present)
        assertEquals(1, month.late)
        assertEquals(1, month.absent)
        assertEquals(1, month.holiday)
        assertEquals(1, month.closed)
        assertEquals(1, month.unknown)
        // Open days: 6 present + 1 late + 1 absent = 8; attended 7 -> 87%.
        assertEquals(8, month.openDays.size)
        assertEquals(87, month.percent)
    }

    @Test
    fun paymentFlagsDoNotChangeState() {
        val marks = fullMarks(2026, 10, AttendanceDayState.PRESENT)
        val payDay = LocalDate.of(2026, 10, 3).toEpochDay()
        val month = buildAttendanceMonth(2026, 10, today, marks, setOf(payDay))
        assertTrue(month.days.first { it.dayOfMonth == 3 }.hasPayment)
        assertEquals(AttendanceDayState.PRESENT, month.days.first { it.dayOfMonth == 3 }.state)
        assertEquals(1, month.days.count { it.hasPayment })
    }

    @Test
    fun comparisonNeedsFullyElapsedPreviousMonth() {
        val oct = buildAttendanceMonth(2026, 10, today, fullMarks(2026, 10, AttendanceDayState.PRESENT))
        val sep = buildAttendanceMonth(2026, 9, today, fullMarks(2026, 9, AttendanceDayState.PRESENT))
        assertEquals(0, compareMonthPoints(oct, sep, today))
        // Current month as "previous" is still in progress -> no comparison.
        assertNull(compareMonthPoints(sep, oct, today))
        assertNull(compareMonthPoints(oct, null, today))
    }

    @Test
    fun comparisonDeltaIsPercentagePoints() {
        val marks = fullMarks(2026, 9, AttendanceDayState.PRESENT).toMutableMap()
        marks[LocalDate.of(2026, 9, 1).toEpochDay()] = AttendanceDayState.ABSENT
        marks[LocalDate.of(2026, 9, 2).toEpochDay()] = AttendanceDayState.ABSENT
        marks[LocalDate.of(2026, 9, 3).toEpochDay()] = AttendanceDayState.ABSENT
        // September: 30 open days, 27 attended -> 90%.
        val sep = buildAttendanceMonth(2026, 9, today, marks)
        assertEquals(90, sep.percent)
        // October: 11 open days, all attended -> 100%.
        val oct = buildAttendanceMonth(2026, 10, today, fullMarks(2026, 10, AttendanceDayState.PRESENT))
        assertEquals(100, oct.percent)
        assertEquals(10, compareMonthPoints(oct, sep, today))
    }

    @Test
    fun streaksSkipClosuresButBreakOnUnknown() {
        val marks = mutableMapOf<Long, AttendanceDayState>()
        // Oct 1-11: P P P P P CLOSED P P UNKNOWN P P (8-9 Oct).
        val states = mapOf(
            1 to AttendanceDayState.PRESENT, 2 to AttendanceDayState.PRESENT,
            3 to AttendanceDayState.PRESENT, 4 to AttendanceDayState.PRESENT,
            5 to AttendanceDayState.PRESENT, 6 to AttendanceDayState.CLOSED,
            7 to AttendanceDayState.PRESENT, 8 to AttendanceDayState.PRESENT,
            9 to AttendanceDayState.UNKNOWN, 10 to AttendanceDayState.PRESENT,
            11 to AttendanceDayState.PRESENT
        )
        states.forEach { (dom, s) -> marks[LocalDate.of(2026, 10, dom).toEpochDay()] = s }
        val month = buildAttendanceMonth(2026, 10, today, marks)
        // Current run: Oct 10-11 -> 2 (unknown on the 9th breaks it).
        assertEquals(2, month.currentStreak())
        // Longest: Oct 1-8 skipping the closed 6th -> 7.
        assertEquals(7, month.longestStreak())
    }
}
