package com.example.badnewgym.feature.memberintelligence.domain.analytics

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Guards the attendance trend chip: a real delta must surface (non-zero) and
 * a flat period must stay silent (zero) so Home never fabricates momentum.
 */
class AttendanceTrendTest {

    private fun window(firstHalf: List<AttendanceDayState>, secondHalf: List<AttendanceDayState>): AttendanceWindow {
        val anchor = LocalDate.of(2026, 10, 9)
        val states = firstHalf + secondHalf
        val days = states.mapIndexed { i, state ->
            val date = anchor.minusDays((states.size - 1 - i).toLong())
            AttendanceDay(date.toEpochDay(), date.dayOfMonth, state)
        }
        return AttendanceWindow(2026, 10, days)
    }

    @Test
    fun fallingSecondHalf_reportsNegativeDelta() {
        val good = List(15) { AttendanceDayState.PRESENT }
        val bad = List(8) { AttendanceDayState.PRESENT } + List(7) { AttendanceDayState.ABSENT }
        val delta = AttendanceAnalytics.trendPoints(window(good, bad))
        assertTrue("expected negative delta, got $delta", delta < 0)
    }

    @Test
    fun flatPeriod_reportsZeroDelta() {
        val half = List(15) { AttendanceDayState.PRESENT }
        assertEquals(0, AttendanceAnalytics.trendPoints(window(half, half.toList())))
    }

    @Test
    fun closedDays_doNotBreakStreak() {
        val days = listOf(
            AttendanceDayState.PRESENT, AttendanceDayState.HOLIDAY,
            AttendanceDayState.PRESENT, AttendanceDayState.HOLIDAY,
            AttendanceDayState.PRESENT
        )
        val w = window(days.take(2), days.drop(2))
        assertEquals(3, AttendanceAnalytics.currentStreak(w))
    }
}
