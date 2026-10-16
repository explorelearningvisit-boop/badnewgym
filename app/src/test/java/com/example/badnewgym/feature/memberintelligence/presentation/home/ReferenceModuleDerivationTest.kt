package com.example.badnewgym.feature.memberintelligence.presentation.home

import com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceMonth
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTransaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

/**
 * The payment module's new derivations must never invent plan history.
 * A previous plan appears only when named by the records; a timeline month is
 * marked paid only when a transaction timestamp falls inside it.
 */
class PaymentDerivationTest {

    private val zone = ZoneId.systemDefault()
    private fun at(month: YearMonth, day: Int): Long =
        month.atDay(day.coerceAtMost(month.lengthOfMonth()))
            .atStartOfDay(zone).toInstant().toEpochMilli()

    @Test
    fun noPreviousPlanNameMeansNoPreviousPlanEvenWithHistory() {
        val history = listOf(
            MembershipHistory("Pulse", at(YearMonth.of(2026, 1), 1), at(YearMonth.of(2026, 3), 31))
        )
        assertNull(previousPlanOf("Gold Plan", null, history))
        assertNull(previousPlanOf("Gold Plan", "  ", history))
    }

    @Test
    fun aRecordedPreviousPlanIsShownButNeverUpgradesItself() {
        // No corroborating history entry: the name stands, the period stays blank.
        val p = previousPlanOf("Gold Plan", "Pulse", emptyList())
        assertEquals("Pulse", p?.name)
        assertNull(p?.period)
    }

    @Test
    fun historyCorroboratesThePeriodOfADifferentPlan() {
        val history = listOf(
            MembershipHistory("Pulse", at(YearMonth.of(2026, 4), 1), at(YearMonth.of(2026, 6), 30)),
            MembershipHistory("Gold Plan", at(YearMonth.of(2026, 7), 1), at(YearMonth.of(2026, 10), 31))
        )
        val p = previousPlanOf("Gold Plan", "Pulse", history)
        assertEquals("Pulse", p?.name)
        assertTrue("period must name the corroborated range", p?.period?.contains("2026") == true)
    }

    @Test
    fun theTimelineCoversTwelveRealMonthsEndingNow() {
        val months = paymentTimelineMonths(emptyList(), YearMonth.of(2026, 10))
        assertEquals(12, months.size)
        assertEquals(YearMonth.of(2025, 11), months.first().yearMonth)
        assertEquals(YearMonth.of(2026, 10), months.last().yearMonth)
        assertTrue("current month is the due month", months.last().isDueMonth)
        assertFalse("no transaction means no paid month", months.any { it.hasTransaction })
    }

    @Test
    fun onlyMonthsContainingATransactionAreMarkedPaid() {
        val now = YearMonth.of(2026, 10)
        val history = listOf(
            PaymentTransaction("p1", 2800.0, at(YearMonth.of(2026, 9), 16), "UPI", "Plan")
        )
        val months = paymentTimelineMonths(history, now)
        assertEquals(1, months.count { it.hasTransaction })
        assertEquals(YearMonth.of(2026, 9), months.first { it.hasTransaction }.yearMonth)
    }
}

/**
 * Profile tiles report recorded state only. A missing record removes its tile;
 * nothing here substitutes a plausible zero.
 */
class IdentityStatsTest {

    private fun snapshot(lifetime: Int?): MemberSnapshot = MemberSnapshot(
        gymId = "gym1",
        identity = MemberIdentity(
            name = "Test Member",
            photoUrl = null,
            tier = MembershipTier.NORMAL,
            memberSince = 0L,
            code = "BG000",
            isVerified = false,
            age = 23,
            gender = "Male"
        ),
        membership = null,
        attendance = attendance(lifetime),
        payment = null,
        trainer = null,
        workout = null,
        supplements = null,
        nutrition = null,
        services = null
    )

    private fun attendance(lifetime: Int?) = if (lifetime == null) null else AttendanceSummary(
        periodName = "Current Month",
        visits = 0,
        target = null,
        lifetimeVisits = lifetime
    )

    private fun month(vararg states: AttendanceDayState): AttendanceMonth {
        val days = states.mapIndexed { i, s ->
            AttendanceDay(
                epochDay = LocalDate.of(2026, 10, i + 1).toEpochDay(),
                dayOfMonth = i + 1,
                state = s,
                visitMinutes = if (s == AttendanceDayState.PRESENT || s == AttendanceDayState.LATE) 60 else null
            )
        }
        return AttendanceMonth(2026, 10, days)
    }

    @Test
    fun allTilesPopulateFromRecords() {
        val s = identityStats(
            snapshot(214),
            month(
                AttendanceDayState.PRESENT, AttendanceDayState.PRESENT,
                AttendanceDayState.LATE, AttendanceDayState.ABSENT
            )
        )
        assertEquals(214, s.totalVisits)
        assertEquals(60, s.avgVisitMin)
        assertTrue("streak must be positive", (s.streak ?: 0) > 0)
        assertTrue("best must cover the streak", (s.bestStreak ?: 0) >= (s.streak ?: 0))
    }

    @Test
    fun anAverageNeverCountsDaysWithoutARecordedDuration() {
        val s = identityStats(
            snapshot(null),
            month(AttendanceDayState.PRESENT, AttendanceDayState.FUTURE, AttendanceDayState.UNKNOWN)
        )
        assertEquals(60, s.avgVisitMin)
        assertNull("no lifetime count means no visits tile", s.totalVisits)
    }

    @Test
    fun nothingRecordedMeansNoTiles() {
        val s = identityStats(snapshot(null), AttendanceMonth(2026, 10, emptyList()))
        assertNull(s.streak)
        assertNull(s.bestStreak)
        assertNull(s.totalVisits)
        assertNull(s.avgVisitMin)
    }
}