package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.EventSource
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies that payments recorded from the dashboard change the returned snapshot
 * like a real backend would: outstanding drops, a real transaction appears in the
 * history, the lifecycle is re-derived, and stale payment-due issues disappear.
 */
class StubMemberRepositoryPaymentTest {

    private fun paymentEvent(repository: StubMemberRepositoryImpl, memberId: String, amount: Double, key: String? = null, at: Long = System.currentTimeMillis()): MemberEvent {
        return MemberEvent(
            id = "test_${at}_${amount}",
            memberId = memberId,
            gymId = "gym1",
            eventType = EventType.PAYMENT,
            occurredAt = at,
            source = EventSource.STAFF,
            metadata = mapOf("amount" to amount.toString()),
            idempotencyKey = key
        )
    }

    @Test
    fun `recorded payment reduces the outstanding amount`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val before = repository.getMemberSnapshot("BG204").getOrThrow()
        val outstandingBefore = before.payment?.totalOutstanding ?: 0.0
        assertTrue("Fixture must start with an open due to exercise the flow", outstandingBefore > 0.0)

        val dueDate = before.payment?.dueDate
        val now = System.currentTimeMillis()
        val recorded = paymentEvent(repository, "BG204", outstandingBefore, at = now)
        repository.recordEvent(recorded)

        val after = repository.refreshMember("BG204").getOrThrow()
        assertEquals(0.0, after.payment!!.totalOutstanding, 0.001)
        assertEquals(outstandingBefore, after.payment!!.history.first().amount, 0.001)
        val expectedTimeliness = DashboardMath.timelinessFor(now, dueDate)
        assertEquals(expectedTimeliness, after.payment!!.history.first().timeliness)
        // A settled payment can no longer be overdue.
        assertEquals(0, after.payment!!.overdueDays)
    }

    @Test
    fun `settled member no longer carries the payment-due issue`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val before = repository.getMemberSnapshot("BG204").getOrThrow()
        val dueIssuesBefore = before.issues.count { it.description.contains("payment due", ignoreCase = true) }
        val outstandingBefore = before.payment?.totalOutstanding ?: 0.0
        repository.recordEvent(paymentEvent(repository, "BG204", outstandingBefore))

        val after = repository.refreshMember("BG204").getOrThrow()
        val dueIssuesAfter = after.issues.count { it.description.contains("payment due", ignoreCase = true) }
        assertTrue("Fixture should have had a payment-due issue", dueIssuesBefore > 0)
        assertEquals(0, dueIssuesAfter)
    }

    @Test
    fun `recording again with the same idempotency key does not double count`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val before = repository.getMemberSnapshot("BG204").getOrThrow()
        val outstandingBefore = before.payment?.totalOutstanding ?: 0.0
        val half = outstandingBefore / 2.0
        val key = "desk_txn_42"
        repository.recordEvent(paymentEvent(repository, "BG204", half, key = key))
        repository.recordEvent(paymentEvent(repository, "BG204", half, key = key))

        val after = repository.refreshMember("BG204").getOrThrow()
        assertEquals(outstandingBefore / 2.0, after.payment!!.totalOutstanding, 0.001)
        // Only one session transaction for the idempotent replay.
        assertEquals(1, after.payment!!.history.count { it.method == "Recorded at desk" })
    }

    @Test
    fun `payment events without a numeric amount are rejected`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val event = MemberEvent(
            id = "no_amount",
            memberId = "BG204",
            gymId = "gym1",
            eventType = EventType.PAYMENT,
            occurredAt = System.currentTimeMillis(),
            source = EventSource.STAFF
        )
        val result = repository.recordEvent(event)
        assertTrue(result.isFailure)
        val snapshot = repository.refreshMember("BG204").getOrThrow()
        assertEquals(0, snapshot.payment!!.history.count { it.method == "Recorded at desk" })
    }

    @Test
    fun `partial session payments keep an unpaid remainder`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val before = repository.getMemberSnapshot("BG204").getOrThrow()
        val outstandingBefore = before.payment?.totalOutstanding ?: 0.0
        val partial = outstandingBefore * 0.25
        repository.recordEvent(paymentEvent(repository, "BG204", partial))

        val after = repository.refreshMember("BG204").getOrThrow()
        assertEquals(outstandingBefore - partial, after.payment!!.totalOutstanding, 0.001)
        // Breakdown lines stay for the unpaid remainder — money is still owed.
        assertEquals(before.payment!!.breakdown.size, after.payment!!.breakdown.size)
    }

    @Test
    fun `overlay applies per member and matches members by numeric id alias`() = runBlocking {
        val repository = StubMemberRepositoryImpl()
        val before = repository.getMemberSnapshot("BG204").getOrThrow()
        repository.recordEvent(paymentEvent(repository, "1", before.payment!!.totalOutstanding))
        val byCode = repository.refreshMember("BG204").getOrThrow()
        val byId = repository.refreshMember("1").getOrThrow()
        assertEquals(0.0, byCode.payment!!.totalOutstanding, 0.001)
        assertEquals(0.0, byId.payment!!.totalOutstanding, 0.001)
    }

    @Test
    fun `timeliness of a desk payment follows the due date lifecycle`() {
        // Pure mapping check to document fixture behaviour: paying 3+ days after
        // the due date is late, within the same day is on time.
        val now = System.currentTimeMillis()
        assertEquals(PaymentTimeliness.ON_TIME, DashboardMath.timelinessFor(now, now))
        assertEquals(PaymentTimeliness.LATE, DashboardMath.timelinessFor(now + DashboardMath.DAY_MS * 3, now))
    }
}
