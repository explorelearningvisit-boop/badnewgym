package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.GymConfiguration
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceResult
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.IssueSeverity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import java.util.UUID

class MemberIntelligenceEngine {

    fun evaluate(
        snapshot: MemberSnapshot,
        currentEvent: MemberEvent,
        currentTime: Long,
        gymConfiguration: GymConfiguration? = null
    ): IntelligenceResult {
        val signals = generateSignals(snapshot, currentEvent, currentTime, gymConfiguration)
        val primary = signals.firstOrNull {
            it.priority == SignalPriority.P0_CRITICAL || it.priority == SignalPriority.P1_ACTION_REQUIRED
        } ?: signals.firstOrNull()
        val secondary = signals
            .filter { it.id != primary?.id && it.priority != SignalPriority.P3_BACKGROUND }
            .take(3)
        val cta = ContextualCtaEngine.resolve(primary, snapshot)
        return IntelligenceResult(signals, primary, secondary, cta)
    }

    fun generateSignals(
        snapshot: MemberSnapshot,
        currentEvent: MemberEvent,
        currentTime: Long,
        gymConfiguration: GymConfiguration? = null
    ): List<IntelligenceSignal> {
        val signals = mutableListOf<IntelligenceSignal>()
        val membershipLifecycle = MembershipStatusCalculator.resolve(snapshot.membership)
        val paymentLifecycle = PaymentCalculator.resolve(snapshot.payment)

        val criticalIssue = snapshot.issues.firstOrNull { it.severity == IssueSeverity.CRITICAL }
        if (criticalIssue != null) {
            signals.add(
                signal(
                    category = SignalCategory.COMPLAINT,
                    priority = SignalPriority.P0_CRITICAL,
                    title = "Issue requires attention",
                    subtitle = criticalIssue.description,
                    source = MenuType.INSIGHT,
                    action = SignalAction("Resolve Issue", "RESOLVE_ISSUE"),
                    time = currentTime,
                    evidence = listOf(criticalIssue.description)
                )
            )
        }

        val payment = snapshot.payment
        if (paymentLifecycle == PaymentLifecycle.FAILED || currentEvent.eventType == EventType.PAYMENT_FAILED) {
            signals.add(
                signal(
                    category = SignalCategory.PAYMENT,
                    priority = SignalPriority.P0_CRITICAL,
                    title = "Payment failed",
                    subtitle = "Retry the last collection",
                    source = MenuType.PAYMENT,
                    action = SignalAction("Retry Payment", "RETRY_PAYMENT"),
                    time = currentTime
                )
            )
        } else if (payment != null && payment.totalOutstanding > 0) {
            val overdue = payment.overdueDays > 0
            signals.add(
                signal(
                    category = SignalCategory.PAYMENT,
                    priority = if (payment.overdueDays > 7) SignalPriority.P0_CRITICAL else SignalPriority.P1_ACTION_REQUIRED,
                    title = "${MoneyFormat.inr(payment.totalOutstanding)} outstanding",
                    subtitle = if (overdue) "Overdue by ${payment.overdueDays} days" else "Payment due",
                    value = MoneyFormat.inr(payment.totalOutstanding),
                    source = MenuType.PAYMENT,
                    action = SignalAction("Collect ${MoneyFormat.inr(payment.totalOutstanding)}", "COLLECT_PAYMENT"),
                    time = currentTime,
                    evidence = payment.breakdown.map { "${it.label}: ${MoneyFormat.inr(it.amount)}" }
                )
            )
        }

        val membership = snapshot.membership
        if (membership != null) {
            when (membershipLifecycle) {
                MembershipLifecycle.EXPIRED, MembershipLifecycle.CANCELLED -> {
                    signals.add(
                        signal(
                            category = SignalCategory.MEMBERSHIP,
                            priority = SignalPriority.P0_CRITICAL,
                            title = "Membership expired",
                            subtitle = "Expired ${kotlin.math.abs(membership.daysRemaining)} days ago",
                            source = MenuType.PLAN,
                            action = SignalAction("Renew Membership", "RENEW_PLAN"),
                            time = currentTime
                        )
                    )
                }
                MembershipLifecycle.EXPIRING -> {
                    signals.add(
                        signal(
                            category = SignalCategory.MEMBERSHIP,
                            priority = SignalPriority.P1_ACTION_REQUIRED,
                            title = "Expiring soon",
                            subtitle = "${membership.daysRemaining} days left",
                            source = MenuType.PLAN,
                            action = SignalAction("Offer Renewal", "RENEW_PLAN"),
                            time = currentTime,
                            evidence = listOf("${membership.planName} · ${membership.planType}")
                        )
                    )
                }
                else -> Unit
            }
        }

        if (currentEvent.eventType == EventType.NEW_MEMBER) {
            signals.add(
                signal(
                    category = SignalCategory.GENERAL,
                    priority = SignalPriority.P1_ACTION_REQUIRED,
                    title = "New member",
                    subtitle = "Complete onboarding",
                    source = MenuType.HOME,
                    action = SignalAction("Complete Onboarding", "ONBOARDING"),
                    time = currentTime
                )
            )
        }

        val attendance = AttendancePeriodCalculator.labeledSummary(snapshot.attendance, snapshot.membership)
        if (attendance != null && AttendancePeriodCalculator.shouldShowCurrentPeriod(snapshot.membership)) {
            val target = attendance.target ?: gymConfiguration?.attendanceTargetDefault
            val percent = AttendancePeriodCalculator.consistencyPercent(attendance.visits, target)
            val low = target != null && target > 0 && percent < 70
            signals.add(
                signal(
                    category = SignalCategory.ATTENDANCE,
                    priority = if (low) SignalPriority.P1_ACTION_REQUIRED else SignalPriority.P2_IMPORTANT,
                    title = "${attendance.visits}/${target ?: "—"} visits",
                    subtitle = "${attendance.periodName} · $percent% consistency",
                    value = "$percent%",
                    source = MenuType.ATTENDANCE,
                    action = if (low) SignalAction("Recover Attendance", "RECOVER_ATTENDANCE") else null,
                    time = currentTime
                )
            )
        }

        val trainer = snapshot.trainer
        if (trainer != null) {
            val remaining = (trainer.sessionsTotal - trainer.sessionsUsed).coerceAtLeast(0)
            signals.add(
                signal(
                    category = SignalCategory.TRAINER,
                    priority = if (remaining == 0) SignalPriority.P1_ACTION_REQUIRED else SignalPriority.P2_IMPORTANT,
                    title = if (remaining == 0) "PT renewal due" else trainer.trainerName,
                    subtitle = "$remaining of ${trainer.sessionsTotal} sessions left",
                    source = MenuType.TRAINER,
                    action = SignalAction(if (remaining == 0) "Renew PT" else "Open Trainer Session", "VIEW_TRAINER"),
                    time = currentTime,
                    evidence = listOfNotNull(trainer.focus)
                )
            )
        }

        snapshot.workout?.currentRoutine?.let { routine ->
            signals.add(
                signal(
                    category = SignalCategory.ENGAGEMENT,
                    priority = SignalPriority.P2_IMPORTANT,
                    title = routine,
                    subtitle = "Last workout focus",
                    source = MenuType.WORKOUT,
                    action = null,
                    time = currentTime
                )
            )
        }

        return signals.sortedBy { it.priority }
    }

    private fun signal(
        category: SignalCategory,
        priority: SignalPriority,
        title: String,
        subtitle: String?,
        value: String? = null,
        source: MenuType?,
        action: SignalAction?,
        time: Long,
        evidence: List<String> = emptyList()
    ) = IntelligenceSignal(
        id = UUID.randomUUID().toString(),
        category = category,
        priority = priority,
        title = title,
        subtitle = subtitle,
        value = value,
        sourceMenu = source,
        action = action,
        timestamp = time,
        validUntil = null,
        evidence = evidence
    )
}
