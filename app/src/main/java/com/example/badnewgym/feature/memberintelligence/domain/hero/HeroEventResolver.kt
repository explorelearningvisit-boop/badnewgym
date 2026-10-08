package com.example.badnewgym.feature.memberintelligence.domain.hero

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MachineIssue
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MiEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.StaffAlert
import com.example.badnewgym.feature.memberintelligence.domain.model.TrainerSession
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth

/**
 * Event priority engine (spec section 5).
 *
 * Priorities are data, admin-overridable, and default to the spec baseline.
 * Exactly one hero is produced. Competing heroes never coexist.
 */
enum class EventPriority(val score: Int) {
    MACHINE_SAFETY_ISSUE(100),
    PAYMENT_OVERDUE(95),
    ACTIVE_CHECK_IN(90),
    PAYMENT_DUE_SOON(85),
    TRAINER_ISSUE(80),
    CHECK_OUT(75),
    PAYMENT_TRANSACTION(72),
    WALK_IN(70),
    STAFF_ALERT(68),
    ATTENDANCE_CONCERN(60),
    WORKOUT_INSIGHT(50),
    BODY_PROGRESS(45),
    NORMAL_MEMBER_STATE(30);

    /** Admin override can raise or lower a base priority without removing the capability. */
    fun adjustedBy(overrides: Map<EventPriority, Int>): EventPriority {
        val delta = overrides[this] ?: return this
        return resolve((score + delta).coerceIn(1, 120))
    }

    companion object {
        fun resolve(score: Int): EventPriority =
            entries.firstOrNull { it.score == score } ?: nearest(score)

        private fun nearest(score: Int): EventPriority =
            entries.minByOrNull { kotlin.math.abs(it.score - score) } ?: NORMAL_MEMBER_STATE
    }
}

/**
 * The single hero event. Every variant carries the four things staff need:
 * event type, primary value, time, current status, plus at most one primary action.
 */
sealed interface HeroEvent {
    val priority: EventPriority
    val title: String
    val primaryValue: String
    val timeLabel: String
    val statusLabel: String
    val primaryAction: HeroAction?

    data class MachineIssue(
        val issue: com.example.badnewgym.feature.memberintelligence.domain.model.MachineIssue,
        override val timeLabel: String
    ) : HeroEvent {
        override val priority = EventPriority.MACHINE_SAFETY_ISSUE
        override val title = "MACHINE ISSUE"
        override val primaryValue = issue.machineName
        override val statusLabel = when (issue.state) {
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.REPORTED -> "CRITICAL"
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.IN_PROGRESS -> "IN PROGRESS"
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.RESOLVED -> "RESOLVED"
        }
        override val primaryAction = when (issue.state) {
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.REPORTED ->
                HeroAction("MARK IN PROGRESS", HeroActionType.MARK_ISSUE_IN_PROGRESS)
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.IN_PROGRESS ->
                HeroAction("MARK RESOLVED", HeroActionType.MARK_ISSUE_RESOLVED)
            com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.RESOLVED -> null
        }
    }

    data class PaymentOverdue(val situation: PaymentSituation.Overdue, override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.PAYMENT_OVERDUE
        override val title = "PAYMENT OVERDUE"
        override val primaryValue = situation.amount.toMoney()
        override val statusLabel = "OVERDUE BY ${situation.overdueDays} DAYS"
        override val primaryAction = HeroAction("COLLECT PAYMENT", HeroActionType.COLLECT_PAYMENT)
    }

    data class ActiveCheckIn(
        val startedAtEpoch: Long,
        val liveMinutes: Int,
        override val timeLabel: String,
        val lateness: com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness =
            com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.UNKNOWN,
        val latenessLabel: String? = null
    ) : HeroEvent {
        override val priority = EventPriority.ACTIVE_CHECK_IN
        override val title = "CHECK-IN"
        override val primaryValue = liveMinutes.asDuration()
        override val statusLabel = "ACTIVE"
        override val primaryAction = HeroAction("END SESSION", HeroActionType.END_SESSION)
    }

    data class PaymentDue(val situation: PaymentSituation.Due, override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.PAYMENT_DUE_SOON
        override val title = "PAYMENT DUE"
        override val primaryValue = situation.amount.toMoney()
        override val statusLabel = timeLabel
        override val primaryAction = HeroAction("COLLECT PAYMENT", HeroActionType.COLLECT_PAYMENT)
    }

    data class TrainerIssue(val alert: StaffAlert, override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.TRAINER_ISSUE
        override val title = "STAFF ALERT"
        override val primaryValue = alert.title
        override val statusLabel = "${alert.minutesLate} min late"
        override val primaryAction = HeroAction("CALL", HeroActionType.CALL_TRAINER)
    }

    data class CheckOut(
        val endedAtEpoch: Long,
        val durationMinutes: Int,
        override val timeLabel: String
    ) : HeroEvent {
        override val priority = EventPriority.CHECK_OUT
        override val title = "CHECK-OUT"
        override val primaryValue = durationMinutes.asDuration()
        override val statusLabel = "COMPLETED"
        override val primaryAction = HeroAction("VIEW SESSION", HeroActionType.VIEW_SESSION)
    }

    data class PaymentTransaction(
        val amount: Double,
        val method: String,
        override val timeLabel: String
    ) : HeroEvent {
        override val priority = EventPriority.PAYMENT_TRANSACTION
        override val title = "PAYMENT"
        override val primaryValue = amount.toMoney()
        override val statusLabel = method
        override val primaryAction = null
    }

    data class WalkIn(override val timeLabel: String, val isGuest: Boolean) : HeroEvent {
        override val priority = EventPriority.WALK_IN
        override val title = "WALK-IN"
        override val primaryValue = if (isGuest) "Guest" else "New Member"
        override val statusLabel = "Arrived"
        override val primaryAction =
            HeroAction(if (isGuest) "START SESSION" else "START MEMBERSHIP", HeroActionType.START_MEMBERSHIP)
    }

    data class AttendanceConcern(val window: AttendanceWindow, override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.ATTENDANCE_CONCERN
        override val title = "ATTENDANCE"
        override val primaryValue = "${window.attended} / ${window.days.size}"
        override val statusLabel = "${window.absent} absent"
        override val primaryAction = HeroAction("VIEW DETAILS", HeroActionType.OPEN_ATTENDANCE)
    }

    data class TrainerSessionHero(val session: TrainerSession, override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.NORMAL_MEMBER_STATE
        override val title = "TRAINER"
        override val primaryValue = session.focus ?: "Session"
        override val statusLabel = timeLabel
        override val primaryAction = HeroAction("MESSAGE", HeroActionType.MESSAGE_TRAINER)
    }

    data class NormalState(override val timeLabel: String) : HeroEvent {
        override val priority = EventPriority.NORMAL_MEMBER_STATE
        override val title = "MEMBER"
        override val primaryValue = "Active"
        override val statusLabel = "No action needed"
        override val primaryAction = null
    }
}

enum class HeroActionType {
    COLLECT_PAYMENT, END_SESSION, VIEW_SESSION, CALL_TRAINER, MESSAGE_TRAINER,
    MARK_ISSUE_IN_PROGRESS, MARK_ISSUE_RESOLVED, START_MEMBERSHIP, OPEN_ATTENDANCE
}

data class HeroAction(val label: String, val type: HeroActionType)

/** Inputs the resolver needs that are not part of MemberSnapshot. */
data class HeroContext(
    val nowEpoch: Long,
    val checkInStartedAtEpoch: Long? = null,
    val checkInScheduledAtEpoch: Long? = null,
    val checkOutEndedAtEpoch: Long? = null,
    val completedDurationMinutes: Int? = null,
    val paymentSituation: PaymentSituation = PaymentSituation.Settled,
    val attendance: AttendanceWindow? = null,
    val workout: WorkoutMonth? = null,
    val trainer: TrainerSession? = null,
    val machineIssues: List<MachineIssue> = emptyList(),
    val staffAlerts: List<StaffAlert> = emptyList(),
    val walkIn: Boolean = false,
    val walkInIsGuest: Boolean = true,
    val entitlement: MiEntitlement = MiEntitlement()
)

/**
 * Resolves exactly one hero event.
 *
 * Order of evaluation follows the spec baseline priority; the highest scoring
 * eligible candidate wins. Entitlement can suppress capabilities, never grant them.
 */
object HeroEventResolver {

    fun resolve(
        snapshot: MemberSnapshot,
        eventType: EventType,
        context: HeroContext,
        priorityOverrides: Map<EventPriority, Int> = emptyMap()
    ): HeroEvent {
        val candidates = buildCandidates(snapshot, eventType, context)
        return candidates
            .filter { eligible(it, context.entitlement) }
            .maxByOrNull { it.priority.adjustedBy(priorityOverrides).score }
            ?: HeroEvent.NormalState("Active")
    }

    private fun eligible(event: HeroEvent, entitlement: MiEntitlement): Boolean = when (event) {
        is HeroEvent.MachineIssue -> entitlement.permits(MiFeatureCap.MACHINE_ISSUES)
        is HeroEvent.PaymentOverdue, is HeroEvent.PaymentDue ->
            entitlement.permits(MiFeatureCap.PAYMENT_COLLECTION)
        is HeroEvent.TrainerIssue -> entitlement.permits(MiFeatureCap.TRAINER)
        is HeroEvent.TrainerSessionHero -> entitlement.permits(MiFeatureCap.TRAINER)
        is HeroEvent.AttendanceConcern -> entitlement.permits(MiFeatureCap.ATTENDANCE)
        else -> true
    }

    private fun buildCandidates(
        snapshot: MemberSnapshot,
        eventType: EventType,
        context: HeroContext
    ): List<HeroEvent> {
        val out = mutableListOf<HeroEvent>()

        context.machineIssues.firstOrNull { it.state != com.example.badnewgym.feature.memberintelligence.domain.model.IssueState.RESOLVED }
            ?.let { out += HeroEvent.MachineIssue(it, relative(it.reportedAtEpoch, context.nowEpoch)) }

        context.staffAlerts.firstOrNull()
            ?.let { out += HeroEvent.TrainerIssue(it, relative(it.affectedSessionAtEpoch ?: context.nowEpoch, context.nowEpoch)) }

        when (context.paymentSituation) {
            is PaymentSituation.Overdue -> out += HeroEvent.PaymentOverdue(context.paymentSituation, relativeDay(context.nowEpoch))
            is PaymentSituation.Due -> out += HeroEvent.PaymentDue(context.paymentSituation, dueLabel(context))
            is PaymentSituation.Partial -> out += HeroEvent.PaymentDue(
                PaymentSituation.Due(context.paymentSituation.remaining, context.paymentSituation.dueAtEpoch ?: context.nowEpoch),
                dueLabel(context)
            )
            PaymentSituation.Settled -> Unit
        }

        val checkInStart = context.checkInStartedAtEpoch
        if (checkInStart != null) {
            val variants = com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants
            val lateness = variants.resolve(checkInStart, context.checkInScheduledAtEpoch, context.nowEpoch)
            val label = when (lateness) {
                com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.JUST_NOW -> "Just now"
                com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.ON_TIME -> "On Time"
                com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.LATE,
                com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.VERY_LATE ->
                    context.checkInScheduledAtEpoch?.let { variants.lateLabel(checkInStart, it) }
                com.example.badnewgym.feature.memberintelligence.domain.variants.CheckInVariants.Lateness.UNKNOWN -> null
            }
            out += HeroEvent.ActiveCheckIn(
                checkInStart,
                ((context.nowEpoch - checkInStart) / 60_000L).toInt().coerceAtLeast(0),
                clockTime(checkInStart),
                lateness,
                label
            )
        }

        val checkOutEnd = context.checkOutEndedAtEpoch
        if (checkOutEnd != null && checkInStart != null) {
            val mins = context.completedDurationMinutes
                ?: ((checkOutEnd - checkInStart) / 60_000L).toInt()
            out += HeroEvent.CheckOut(checkOutEnd, mins, clockTime(checkOutEnd))
        }

        if (context.walkIn) {
            out += HeroEvent.WalkIn(clockTime(context.nowEpoch), context.walkInIsGuest)
        }

        context.trainer?.let { session ->
            if (session.scheduledAtEpoch >= context.nowEpoch - 3_600_000L) {
                out += HeroEvent.TrainerSessionHero(session, clockTime(session.scheduledAtEpoch))
            }
        }

        context.attendance?.let { window ->
            if (window.absent >= 4) {
                out += HeroEvent.AttendanceConcern(window, "${window.attended} of ${window.days.size} days")
            }
        }

        out += HeroEvent.NormalState("Active")
        return out
    }

    private fun dueLabel(context: HeroContext): String {
        val due = (context.paymentSituation as? PaymentSituation.Due)?.dueAtEpoch
            ?: (context.paymentSituation as? PaymentSituation.Partial)?.dueAtEpoch
            ?: return "Payment due"
        val days = kotlin.math.ceil((due - context.nowEpoch) / 86_400_000.0).toInt()
        return if (days <= 0) "Due today" else "Due in $days days"
    }
}

/** Alias so the resolver reads cleanly without importing the enum name twice. */
private typealias MiFeatureCap = com.example.badnewgym.feature.memberintelligence.domain.model.MiFeature

private const val MINUTE_MS = 60_000L

internal fun Int.asDuration(): String =
    if (this < 60) "$this min" else "${this / 60}h ${(this % 60).toString().padStart(2, '0')}m"

internal fun Double.toMoney(): String =
    "₹" + String.format(java.util.Locale.getDefault(), "%,.0f", this)

internal fun clockTime(epoch: Long): String =
    java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(epoch))

internal fun relativeDay(epoch: Long): String =
    java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Date(epoch))

internal fun relative(epoch: Long, now: Long): String {
    val mins = ((now - epoch) / MINUTE_MS).toInt()
    return when {
        mins < 1 -> "Just now"
        mins < 60 -> "$mins min ago"
        mins < 1440 -> "${mins / 60}h ago"
        else -> relativeDay(epoch)
    }
}