package com.example.badnewgym.feature.memberintelligence.design.colors

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal

data class AdaptiveThemeContext(
    val baseColors: BADGymColors,
    val primaryText: Color,
    val secondaryText: Color,
    val badgeColor: Color,
    val eventColor: Color,
    val activeSignalColor: Color?,
    val isPremium: Boolean,
    val useGlassmorphism: Boolean
)

object AdaptiveThemeEngine {

    fun resolve(
        themeId: ThemeId,
        snapshot: MemberSnapshot,
        currentEventType: EventType,
        highestPrioritySignal: IntelligenceSignal?
    ): AdaptiveThemeContext {
        val baseColors = themeId.colors()
        
        // 1. Semantic overrides based on Event
        val eventColor = ThemeResolver.resolveEventBadgeColor(currentEventType, baseColors)
        
        // 2. State overrides based on Priority
        val activeSignalColor = highestPrioritySignal?.let {
            ThemeResolver.resolveSignalColor(it, baseColors)
        }
        
        // 3. Member Tier styling
        val badgeColor = ThemeResolver.resolveMemberTierColor(snapshot.identity.tier, baseColors)
        val isPremium = snapshot.identity.tier in listOf(
            MembershipTier.VIP, MembershipTier.PREMIUM
        ) // Note: GOLD doesn't exist in enum, only VIP, PREMIUM, NORMAL, CORPORATE, TRIAL, WALK_IN
        
        // 4. Contrast Resolver (Dynamic text color based on surface)
        val primaryText = ContrastResolver.resolvePrimaryText(baseColors.surface, baseColors)
        val secondaryText = ContrastResolver.resolveSecondaryText(baseColors.surface, baseColors)

        val useGlassmorphism = themeId == ThemeId.GLASSMORPHISM

        return AdaptiveThemeContext(
            baseColors = baseColors,
            primaryText = primaryText,
            secondaryText = secondaryText,
            badgeColor = badgeColor,
            eventColor = eventColor,
            activeSignalColor = activeSignalColor,
            isPremium = isPremium,
            useGlassmorphism = useGlassmorphism
        )
    }
}
