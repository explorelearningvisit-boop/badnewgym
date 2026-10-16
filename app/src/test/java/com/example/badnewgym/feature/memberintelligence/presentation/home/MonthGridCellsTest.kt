package com.example.badnewgym.feature.memberintelligence.presentation.home

import com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceMonth
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Weekday alignment for the seven-column month grid.
 *
 * The reference renders a real Sunday-first calendar. The previous two-row
 * layout had no weekday columns at all, so a reader could not tell which day
 * fell on which weekday. These tests pin the alignment for the awkward cases:
 * a month starting on Sunday, a month starting on the last column, leap
 * February, and every month length.
 */
class MonthGridCellsTest {

    private fun month(year: Int, monthNo: Int): AttendanceMonth {
        val length = LocalDate.of(year, monthNo, 1).lengthOfMonth()
        val days = (1..length).map { d ->
            AttendanceDay(
                epochDay = LocalDate.of(year, monthNo, d).toEpochDay(),
                dayOfMonth = d,
                state = AttendanceDayState.PRESENT
            )
        }
        return AttendanceMonth(year, monthNo, days)
    }

    @Test
    fun everyDayOfEveryMonthLandsUnderItsRealWeekdayColumn() {
        // Covers 28/29/30/31-day months and both leap and common Februaries.
        for (year in listOf(2024, 2025, 2026, 2027)) {
            for (m in 1..12) {
                val cells = monthGridCells(month(year, m))
                val real = (1..LocalDate.of(year, m, 1).lengthOfMonth()).map { d ->
                    val date = LocalDate.of(year, m, d)
                    // java.time: MONDAY=1..SUNDAY=7 -> Sunday-first index.
                    date.dayOfWeek.value % 7
                }
                val actual = cells.mapIndexedNotNull { i, cell ->
                    if (cell == null) null else i % 7
                }
                assertEquals(
                    "$year-$m must align every day to its real weekday",
                    real,
                    actual
                )
            }
        }
    }

    @Test
    fun aMonthStartingOnSundayHasNoLeadingPadding() {
        // 1 Aug 2027 is a Sunday.
        val cells = monthGridCells(month(2027, 8))
        assertEquals(1, cells.first()?.dayOfMonth)
    }

    @Test
    fun aMonthStartingOnSaturdayPutsDayOneInTheLastColumn() {
        // 1 May 2027 is a Saturday.
        val cells = monthGridCells(month(2027, 5))
        assertNull(cells[0])
        assertEquals(1, cells[6]?.dayOfMonth)
    }

    @Test
    fun leapFebruaryHasTwentyNineDays() {
        val cells = monthGridCells(month(2024, 2))
        val feb = month(2024, 2)
        assertEquals(29, feb.days.size)
        assertEquals(29, cells.count { it != null })
    }

    @Test
    fun commonFebruaryHasTwentyEightDays() {
        assertEquals(28, monthGridCells(month(2025, 2)).count { it != null })
    }

    @Test
    fun theGridIsAlwaysWholeWeeksSoNoDayIsDropped() {
        for (m in 1..12) {
            val cells = monthGridCells(month(2026, m))
            assertEquals("month $m must be whole weeks", 0, cells.size % 7)
            val days = cells.count { it != null }
            assertEquals(
                "month $m lost or duplicated days",
                LocalDate.of(2026, m, 1).lengthOfMonth(),
                days
            )
        }
    }

    @Test
    fun anEmptyMonthRendersNothingRatherThanCollapsing() {
        assertTrue(monthGridCells(AttendanceMonth(2026, 5, emptyList())).isEmpty())
    }
}