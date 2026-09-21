package com.example.badnewgym.feature.memberintelligence.design.colors

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority

object ThemeResolver {

    fun resolveEventBadgeColor(eventType: EventType, themeColors: BADGymColors): Color {
        return when (eventType) {
            EventType.CHECK_IN, EventType.RENEWAL, EventType.REACTIVATION -> themeColors.success
            EventType.PAYMENT, EventType.CHECK_OUT -> themeColors.info
            EventType.PAYMENT_FAILED, EventType.EXPIRED, EventType.COMPLAINT -> themeColors.danger
            EventType.FREEZE, EventType.MAINTENANCE -> themeColors.warning
            EventType.TRAINER_SESSION -> Color(0xFF8B5CF6) // Purple
            EventType.WORKOUT -> Color(0xFF14B8A6) // Teal
            EventType.SUPPLEMENT_PURCHASE -> Color(0xFFF97316) // Orange
            EventType.NUTRITION -> Color(0xFFF43F5E) // Coral
            EventType.SERVICE_PURCHASE, EventType.NEW_MEMBER, EventType.WALK_IN -> themeColors.accent
            EventType.UNKNOWN -> themeColors.textMuted
        }
    }

    fun resolveMemberTierColor(tier: MembershipTier, themeColors: BADGymColors): Color {
        return when (tier) {
            MembershipTier.VIP -> themeColors.vip
            MembershipTier.PREMIUM -> themeColors.info
            MembershipTier.NORMAL -> themeColors.accent
            MembershipTier.CORPORATE -> themeColors.accentStrong
            MembershipTier.TRIAL -> themeColors.warning
            MembershipTier.WALK_IN -> themeColors.textMuted
        }
    }

    fun resolveSignalColor(signal: IntelligenceSignal, themeColors: BADGymColors): Color {
        return when (signal.priority) {
            SignalPriority.P0_CRITICAL -> themeColors.danger
            SignalPriority.P1_ACTION_REQUIRED -> themeColors.warning
            SignalPriority.P2_IMPORTANT -> themeColors.info
            SignalPriority.P3_BACKGROUND -> themeColors.textSecondary
        }
    }

    fun resolveSignalSoftColor(signal: IntelligenceSignal, themeColors: BADGymColors): Color {
        return when (signal.priority) {
            SignalPriority.P0_CRITICAL -> themeColors.dangerSoft
            SignalPriority.P1_ACTION_REQUIRED -> themeColors.warningSoft
            SignalPriority.P2_IMPORTANT -> themeColors.infoSoft
            SignalPriority.P3_BACKGROUND -> themeColors.surfaceMuted
        }
    }
}
