package com.example.badnewgym.feature.memberintelligence.domain.analytics

import com.example.badnewgym.feature.memberintelligence.data.repository.MemberIntelligenceV4DataSource
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalDirection
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalKind
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricKey
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.ProgressVerdict
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession
import com.example.badnewgym.feature.memberintelligence.presentation.home.WorkoutRange
import com.example.badnewgym.feature.memberintelligence.presentation.home.validateCollection
import com.example.badnewgym.feature.memberintelligence.presentation.home.visitOrdinal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * 4.0 acceptance rules as executable contracts: payment states, workout
 * ranges, attendance denominator, body verdict semantics, visit numbering.
 */
class HomeAcceptanceTest {

    private val dataSource = MemberIntelligenceV4DataSource()

    // ---------------------------------------------------------- payment

    @Test
    fun settledWhenNothingOutstanding() {
        assertEquals(
            PaymentSituation.Settled,
            dataSource.paymentSituation(0.0, 0, null)
        )
    }

    @Test
    fun dueWhenOutstandingAndNotOverdue() {
        val situation = dataSource.paymentSituation(2800.0, 0, 1_700_000_000_000L)
        assertTrue(situation is PaymentSituation.Due)
        assertEquals(2800.0, (situation as PaymentSituation.Due).amount, 0.001)
    }

    @Test
    fun overdueBeatsDue() {
        val situation = dataSource.paymentSituation(2000.0, 4, 1_700_000_000_000L)
        assertTrue(situation is PaymentSituation.Overdue)
        assertEquals(4, (situation as PaymentSituation.Overdue).overdueDays)
    }

    @Test
    fun partialKeepsTrueRemainder() {
        val situation = dataSource.paymentSituation(2800.0, 0, 1_700_000_000_000L, partialReceived = 1000.0)
        assertTrue(situation is PaymentSituation.Partial)
        situation as PaymentSituation.Partial
        assertEquals(1000.0, situation.received, 0.001)
        assertEquals(1800.0, situation.remaining, 0.001)
    }

    @Test
    fun collectionGuardRejectsGarbage() {
        assertEquals("Nothing outstanding on this account.", validateCollection(100.0, 0.0))
        assertEquals("Enter an amount greater than zero.", validateCollection(0.0, 2800.0))
        assertEquals("Enter an amount greater than zero.", validateCollection(-50.0, 2800.0))
        assertTrue(validateCollection(99999.0, 2800.0)!!.startsWith("Amount exceeds"))
        assertNull(validateCollection(2800.0, 2800.0))
        assertNull(validateCollection(1000.0, 2800.0))
    }

    // ---------------------------------------------------------- workout ranges

    private fun month(): WorkoutMonth = dataSource.workoutMonth(LocalDate.of(2026, 10, 9))

    @Test
    fun everyRangeHasExactDayCountAndOrder() {
        val m = month()
        val newest = m.sorted().last().epochDay
        listOf(7, 14, 15, 30).forEach { n ->
            val days = WorkoutAnalytics.rangeDays(m, n)
            assertEquals("range $n", n, days.size)
            // Chronological day-of-month sequence ending at the dataset end.
            val expected = ((n - 1) downTo 0).map {
                LocalDate.ofEpochDay(newest - it).dayOfMonth
            }
            assertEquals("order for range $n", expected, days.map { it.dayOfMonth })
        }
    }

    @Test
    fun rangeLabelCoversFirstToLast() {
        val label = WorkoutAnalytics.rangeLabel(month(), 7)
        assertTrue("label was '$label'", label.contains("–"))
    }

    @Test
    fun rangeSummaryMatchesItsSessions() {
        val m = month()
        val newest = m.sorted().last().epochDay
        val last7 = m.sessions.filter { it.epochDay in (newest - 6)..newest }
        val summary = WorkoutAnalytics.summarize(last7)
        assertEquals(last7.sumOf { it.minutes }, summary.totalMinutes)
        assertEquals(last7.count { it.minutes > 0 }, summary.activeDays)
        val peak = last7.maxOfOrNull { it.minutes } ?: 0
        assertEquals(peak, summary.peak?.minutes ?: 0)
    }

    @Test
    fun zeroDayIsNeverTheLowest() {
        val sessions = listOf(
            WorkoutSession(epochDay = 10, dayOfMonth = 1, minutes = 60),
            WorkoutSession(epochDay = 11, dayOfMonth = 2, minutes = 25)
        )
        val summary = WorkoutAnalytics.summarize(sessions)
        assertEquals(25, summary.lowest?.minutes ?: 0)
    }

    @Test
    fun weekPagerCoversWholeDataset() {
        val m = month()
        assertTrue(WorkoutAnalytics.windowCount(m) >= 4)
        val covered = (0 until WorkoutAnalytics.windowCount(m))
            .flatMap { WorkoutAnalytics.window(m, it)!!.days }
            .count { it.minutes > 0 }
        assertEquals(m.sessions.size, covered)
    }

    // ---------------------------------------------------------- attendance

    @Test
    fun percentageUsesOpenDaysOnly() {
        val window = dataSource.attendanceWindow(LocalDate.of(2026, 10, 9))
        val open = window.elapsed.count { it.state != AttendanceDayState.HOLIDAY }
        val attended = window.present + window.late
        assertEquals(attended.toFloat() / open, AttendanceAnalytics.completion(window), 0.001f)
    }

    @Test
    fun totalMinutesNeverLeakIntoAttendance() {
        val window = dataSource.attendanceWindow(LocalDate.of(2026, 10, 9))
        // visitMinutes exist on records but no attendance ratio may use them.
        assertTrue(window.elapsed.all { it.visitMinutes == null || it.visitMinutes!! > 0 })
        assertEquals(window.present + window.late, window.attended)
    }

    // ---------------------------------------------------------- body

    private fun goal(kind: GoalKind, direction: GoalDirection, start: Double, current: Double, target: Double) =
        BodyGoal(
            id = "t", kind = kind, primaryMetric = MetricKey.WEIGHT,
            direction = direction, startValue = start, currentValue = current,
            targetValue = target, startedAtEpoch = 0L, updatedAtEpoch = 1L
        )

    @Test
    fun gainUpIsGoodLossDownIsGood() {
        assertEquals(
            ProgressVerdict.IMPROVING,
            BodyGoalAnalytics.verdict(goal(GoalKind.MUSCLE_GAIN, GoalDirection.INCREASE, 67.0, 69.5, 72.0))
        )
        assertEquals(
            ProgressVerdict.IMPROVING,
            BodyGoalAnalytics.verdict(goal(GoalKind.BODY_FAT_REDUCTION, GoalDirection.DECREASE, 24.0, 21.0, 18.0))
        )
    }

    @Test
    fun wrongDirectionIsRegressing() {
        assertEquals(
            ProgressVerdict.REGRESSING,
            BodyGoalAnalytics.verdict(goal(GoalKind.MUSCLE_GAIN, GoalDirection.INCREASE, 67.0, 66.0, 72.0))
        )
    }

    @Test
    fun nearTargetIsOnTrack() {
        assertEquals(
            ProgressVerdict.ON_TRACK,
            BodyGoalAnalytics.verdict(goal(GoalKind.MUSCLE_GAIN, GoalDirection.INCREASE, 67.0, 71.8, 72.0))
        )
    }

    // ---------------------------------------------------------- identity labels

    @Test
    fun visitNumberingFormat() {
        assertEquals("Today's Visit #1", visitOrdinal(1))
        assertEquals("Today's Visit #2", visitOrdinal(2))
        assertEquals("Today's Visit #11", visitOrdinal(11))
    }

    @Test
    fun rangeShortLabels() {
        assertEquals("7D", WorkoutRange.D7.short)
        assertEquals("30D", WorkoutRange.D30.short)
    }
}
