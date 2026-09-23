package com.example.badnewgym.feature.memberintelligence.design.semantics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

enum class MemberTierVisual {
    NORMAL,
    SILVER,
    GOLD,
    PREMIUM,
    VIP_ELITE
}

enum class MemberStateVisual {
    ACTIVE,
    PAYMENT_DUE,
    PAYMENT_OVERDUE,
    EXPIRED,
    FROZEN,
    TRAINER_ACTIVE,
    CRITICAL_ALERT
}

data class MemberSemanticStyle(
    val tierVisual: MemberTierVisual,
    val tierLabel: String,
    val tierIcon: ImageVector,
    val tierAccentColor: Color,
    val tierBgColor: Color,
    val tierBorderColor: Color,
    val stateVisual: MemberStateVisual,
    val stateLabel: String,
    val stateIcon: ImageVector,
    val stateAccentColor: Color,
    val stateBgColor: Color,
    val stateBorderColor: Color,
    val isUrgent: Boolean,
    val urgentMessage: String?,
    val prominentBorderColor: Color
)

object MemberSemanticResolver {

    fun resolve(
        snapshot: MemberSnapshot,
        currentEvent: MemberEvent? = null,
        theme: ThemeId = ThemeId.NATURAL_FRESH
    ): MemberSemanticStyle {
        val isDark = theme in listOf(
            ThemeId.FUTURISTIC_NEON,
            ThemeId.BEAST_MODE,
            ThemeId.PURPLE_ROYAL,
            ThemeId.MINIMAL_DARK,
            ThemeId.PREMIUM_3D
        )

        // 1. Resolve Tier Visual
        val planName = snapshot.membership?.planName.orEmpty().lowercase()
        val tierVisual = when {
            snapshot.identity.tier == MembershipTier.VIP || planName.contains("vip") || planName.contains("elite") ->
                MemberTierVisual.VIP_ELITE
            snapshot.identity.tier == MembershipTier.PREMIUM || planName.contains("premium") ->
                MemberTierVisual.PREMIUM
            planName.contains("gold") ->
                MemberTierVisual.GOLD
            planName.contains("silver") ->
                MemberTierVisual.SILVER
            else ->
                MemberTierVisual.NORMAL
        }

        val tierLabel = when (tierVisual) {
            MemberTierVisual.VIP_ELITE -> "VIP ELITE"
            MemberTierVisual.PREMIUM -> "PREMIUM"
            MemberTierVisual.GOLD -> "GOLD TIER"
            MemberTierVisual.SILVER -> "SILVER TIER"
            MemberTierVisual.NORMAL -> "MEMBER"
        }

        val tierIcon = when (tierVisual) {
            MemberTierVisual.VIP_ELITE -> Icons.Rounded.Star
            MemberTierVisual.PREMIUM -> Icons.Rounded.WorkspacePremium
            MemberTierVisual.GOLD -> Icons.Rounded.WorkspacePremium
            MemberTierVisual.SILVER -> Icons.Rounded.WorkspacePremium
            MemberTierVisual.NORMAL -> Icons.Rounded.Person
        }

        val (tierAccent, tierBg, tierBorder) = when (tierVisual) {
            MemberTierVisual.VIP_ELITE -> Triple(
                Color(0xFFFBBF24),
                if (isDark) Color(0xFF2E1A04) else Color(0xFFFEF3C7),
                Color(0xFFF59E0B)
            )
            MemberTierVisual.PREMIUM -> Triple(
                Color(0xFF059669),
                if (isDark) Color(0xFF063B2B) else Color(0xFFD1FAE5),
                Color(0xFF10B981)
            )
            MemberTierVisual.GOLD -> Triple(
                Color(0xFFD97706),
                if (isDark) Color(0xFF2E1C05) else Color(0xFFFEF9C3),
                Color(0xFFFBBF24)
            )
            MemberTierVisual.SILVER -> Triple(
                Color(0xFF64748B),
                if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                Color(0xFF94A3B8)
            )
            MemberTierVisual.NORMAL -> Triple(
                Color(0xFF475569),
                if (isDark) Color(0xFF182230) else Color(0xFFF8FAFC),
                Color(0xFFCBD5E1)
            )
        }

        // 2. Resolve State Visual with Strict Priority:
        // Priority: Critical Issue / Safety > Expired > Overdue > Payment Due > Frozen > Trainer Active > Active
        val payment = snapshot.payment
        val membership = snapshot.membership
        val isExpired = membership?.isActive == false ||
                (membership?.daysRemaining ?: 1) <= 0 ||
                membership?.lifecycle == MembershipLifecycle.EXPIRED

        val isOverdue = (payment?.overdueDays ?: 0) > 0 && (payment?.totalOutstanding ?: 0.0) > 0
        val isPaymentDue = (payment?.totalOutstanding ?: 0.0) > 0 && !isOverdue
        val isFrozen = membership?.lifecycle == MembershipLifecycle.FROZEN
        val isTrainerActive = currentEvent?.eventType == EventType.TRAINER_SESSION ||
                (snapshot.trainer != null && (snapshot.trainer.sessionsTotal - snapshot.trainer.sessionsUsed) > 0)
        val hasCriticalIssue = snapshot.issues.any { it.severity == IssueSeverity.CRITICAL }

        val (stateVisual, isUrgent, urgentMessage) = when {
            hasCriticalIssue -> Triple(
                MemberStateVisual.CRITICAL_ALERT,
                true,
                snapshot.issues.firstOrNull { it.severity == IssueSeverity.CRITICAL }?.description ?: "Critical Action Required"
            )
            isExpired -> Triple(
                MemberStateVisual.EXPIRED,
                true,
                "Membership Expired • Renewal Required"
            )
            isOverdue -> Triple(
                MemberStateVisual.PAYMENT_OVERDUE,
                true,
                "Overdue: ₹${payment?.totalOutstanding?.toInt() ?: 0} (${payment?.overdueDays}d overdue)"
            )
            isPaymentDue -> Triple(
                MemberStateVisual.PAYMENT_DUE,
                true,
                "Payment Due: ₹${payment?.totalOutstanding?.toInt() ?: 0}"
            )
            isFrozen -> Triple(
                MemberStateVisual.FROZEN,
                false,
                null
            )
            isTrainerActive -> Triple(
                MemberStateVisual.TRAINER_ACTIVE,
                false,
                null
            )
            else -> Triple(
                MemberStateVisual.ACTIVE,
                false,
                null
            )
        }

        val stateLabel = when (stateVisual) {
            MemberStateVisual.CRITICAL_ALERT -> "ALERT"
            MemberStateVisual.EXPIRED -> "EXPIRED"
            MemberStateVisual.PAYMENT_OVERDUE -> "OVERDUE"
            MemberStateVisual.PAYMENT_DUE -> "DUE"
            MemberStateVisual.FROZEN -> "FROZEN"
            MemberStateVisual.TRAINER_ACTIVE -> "PT ACTIVE"
            MemberStateVisual.ACTIVE -> "ACTIVE"
        }

        val stateIcon = when (stateVisual) {
            MemberStateVisual.CRITICAL_ALERT -> Icons.Rounded.Warning
            MemberStateVisual.EXPIRED -> Icons.Rounded.Cancel
            MemberStateVisual.PAYMENT_OVERDUE -> Icons.Rounded.WarningAmber
            MemberStateVisual.PAYMENT_DUE -> Icons.Rounded.WarningAmber
            MemberStateVisual.FROZEN -> Icons.Rounded.PauseCircle
            MemberStateVisual.TRAINER_ACTIVE -> Icons.Rounded.FitnessCenter
            MemberStateVisual.ACTIVE -> Icons.Rounded.CheckCircle
        }

        val (stateAccent, stateBg, stateBorder) = when (stateVisual) {
            MemberStateVisual.CRITICAL_ALERT -> Triple(
                Color(0xFFDC2626),
                if (isDark) Color(0xFF450A0A) else Color(0xFFFEF2F2),
                Color(0xFFEF4444)
            )
            MemberStateVisual.EXPIRED -> Triple(
                Color(0xFFDC2626),
                if (isDark) Color(0xFF3B0712) else Color(0xFFFEF2F2),
                Color(0xFFEF4444)
            )
            MemberStateVisual.PAYMENT_OVERDUE -> Triple(
                Color(0xFFDC2626),
                if (isDark) Color(0xFF450A0A) else Color(0xFFFEF2F2),
                Color(0xFFEF4444)
            )
            MemberStateVisual.PAYMENT_DUE -> Triple(
                Color(0xFFD97706),
                if (isDark) Color(0xFF451A03) else Color(0xFFFFFBEB),
                Color(0xFFF59E0B)
            )
            MemberStateVisual.FROZEN -> Triple(
                Color(0xFF0284C7),
                if (isDark) Color(0xFF082F49) else Color(0xFFE0F2FE),
                Color(0xFF38BDF8)
            )
            MemberStateVisual.TRAINER_ACTIVE -> Triple(
                Color(0xFF7C3AED),
                if (isDark) Color(0xFF2E1065) else Color(0xFFF5F3FF),
                Color(0xFF8B5CF6)
            )
            MemberStateVisual.ACTIVE -> Triple(
                Color(0xFF16A34A),
                if (isDark) Color(0xFF052E16) else Color(0xFFDCFCE7),
                Color(0xFF22C55E)
            )
        }

        // When urgent state is present (overdue, expired, critical),
        // urgent semantic state visibly overrides decorative tier styling on card border
        val prominentBorder = if (isUrgent) stateBorder else tierBorder

        return MemberSemanticStyle(
            tierVisual = tierVisual,
            tierLabel = tierLabel,
            tierIcon = tierIcon,
            tierAccentColor = tierAccent,
            tierBgColor = tierBg,
            tierBorderColor = tierBorder,
            stateVisual = stateVisual,
            stateLabel = stateLabel,
            stateIcon = stateIcon,
            stateAccentColor = stateAccent,
            stateBgColor = stateBg,
            stateBorderColor = stateBorder,
            isUrgent = isUrgent,
            urgentMessage = urgentMessage,
            prominentBorderColor = prominentBorder
        )
    }
}
