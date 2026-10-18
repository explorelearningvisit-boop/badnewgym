package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.engine.PaymentCalculator
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTransaction
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

/**
 * Demo repository backed by scenario fixtures.
 *
 * [recordEvent] keeps a session-scoped overlay so that payments recorded from the
 * dashboard change the returned snapshot exactly like a real backend would accept
 * and reflect a recorded transaction. The overlay lives only in this stub —
 * production repositories map real persisted transactions instead.
 */
class StubMemberRepositoryImpl : MemberIntelligenceRepository {
    private val seenKeys = mutableSetOf<String>()
    private val snapshots = MutableStateFlow<MemberSnapshot?>(null)
    private val sessionPayments = mutableMapOf<String, MutableList<RecordedPayment>>()

    override suspend fun getMemberSnapshot(memberId: String): Result<MemberSnapshot> {
        delay(100)
        val (snapshot, _) = when (canonicalCodeForId(memberId)) {
            "BG204" -> MemberScenarios.naturalFreshYash()
            "BG105" -> MemberScenarios.futuristicNeonArjun()
            "BG310" -> MemberScenarios.minimalDarkRiya()
            "BG407" -> MemberScenarios.glassmorphismNeha()
            "BG001" -> MemberScenarios.premium3dKabir()
            "BG220" -> MemberScenarios.vibrantGradientAarav()
            "BG330" -> MemberScenarios.beastModeRohan()
            "BG502" -> MemberScenarios.purpleRoyalSimran()
            else -> MemberScenarios.naturalFreshYash()
        }
        val effective = snapshot.withSessionPayments(canonicalCodeForId(memberId))
        snapshots.value = effective
        return Result.success(effective)
    }

    override fun observeMemberEvents(gymId: String): Flow<MemberEvent> = flow {
        while (true) {
            delay(12_000)
            emit(
                MemberEvent(
                    id = "event_${System.currentTimeMillis()}",
                    memberId = snapshots.value?.id ?: "1",
                    gymId = gymId,
                    eventType = EventType.CHECK_IN,
                    occurredAt = System.currentTimeMillis()
                )
            )
        }
    }

    override fun observeMemberUpdates(memberId: String): Flow<MemberSnapshot> = snapshots.filterNotNull()

    override suspend fun recordEvent(event: MemberEvent): Result<Unit> {
        val key = event.idempotencyKey
        if (key != null && !seenKeys.add(key)) {
            return Result.success(Unit)
        }
        delay(50)
        when (event.eventType) {
            EventType.PAYMENT, EventType.PAYMENT_SUCCESS, EventType.RENEWAL -> {
                val amount = event.metadata["amount"]?.toDoubleOrNull()
                    ?: return Result.failure(IllegalArgumentException("Payment events require a numeric amount"))
                if (amount <= 0.0) return Result.failure(IllegalArgumentException("Payment amount must be positive"))
                sessionPayments
                    .getOrPut(canonicalCodeForId(event.memberId)) { mutableListOf() }
                    .add(RecordedPayment(amount = amount, recordedAt = event.occurredAt))
            }
            else -> Unit
        }
        return Result.success(Unit)
    }

    override suspend fun refreshMember(memberId: String): Result<MemberSnapshot> = getMemberSnapshot(memberId)

    /** Any identifier ("1".."8" or the code itself) is canonicalised to the member code. */
    private fun canonicalCodeForId(memberId: String): String = when (memberId) {
        "1", "BG204" -> "BG204"
        "2", "BG105" -> "BG105"
        "3", "BG310" -> "BG310"
        "4", "BG407" -> "BG407"
        "5", "BG001" -> "BG001"
        "6", "BG220" -> "BG220"
        "7", "BG330" -> "BG330"
        "8", "BG502" -> "BG502"
        else -> memberId
    }

    private data class RecordedPayment(val amount: Double, val recordedAt: Long)

    /** Applies every payment recorded this session on top of the scenario fixture. */
    private fun MemberSnapshot.withSessionPayments(memberKey: String): MemberSnapshot {
        val recordedPayments = sessionPayments[memberKey]?.takeIf { it.isNotEmpty() } ?: return this
        val basePayment = payment ?: return this
        val totalRecorded = recordedPayments.sumOf { it.amount }
        val remaining = (basePayment.totalOutstanding - totalRecorded).coerceAtLeast(0.0)
        val settlements = recordedPayments.mapIndexed { index, entry ->
            PaymentTransaction(
                id = "session_${memberKey}_$index",
                amount = entry.amount,
                occurredAt = entry.recordedAt,
                method = "Recorded at desk",
                purpose = "Membership",
                timeliness = DashboardMath.timelinessFor(entry.recordedAt, basePayment.dueDate)
            )
        }
        val updatedPayment = basePayment
            .copy(
                totalOutstanding = remaining,
                overdueDays = if (remaining <= 0.0) 0 else basePayment.overdueDays,
                lastPaymentAmount = recordedPayments.last().amount,
                lastPaymentDate = recordedPayments.last().recordedAt,
                lastPaymentMethod = "Recorded at desk",
                lifetimePaid = (basePayment.lifetimePaid ?: 0.0) + totalRecorded,
                breakdown = if (remaining <= 0.0) emptyList() else basePayment.breakdown,
                history = settlements + basePayment.history
            )
            .copy(lifecycle = null)
            .let { it.copy(lifecycle = PaymentCalculator.resolve(it)) }
        // Once nothing is outstanding, the payment-due issue no longer reflects reality.
        val updatedIssues = if (remaining <= 0.0) {
            issues.filterNot { it.description.contains("payment due", ignoreCase = true) }
        } else {
            issues
        }
        return copy(payment = updatedPayment, issues = updatedIssues)
    }
}
