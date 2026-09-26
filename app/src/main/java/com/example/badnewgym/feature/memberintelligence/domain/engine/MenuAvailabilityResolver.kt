package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureKey
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuRailSide
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority

object MenuAvailabilityResolver {
    fun resolve(
        snapshot: MemberSnapshot,
        signals: List<IntelligenceSignal>,
        entitlements: FeatureEntitlement = EntitlementResolver.default()
    ): List<MemberMenu> {
        val hasTrainer = snapshot.trainer != null
        val hasSupplements = snapshot.supplements?.hasHistory == true
        val hasNutrition = snapshot.nutrition?.isSubscribed == true
        val activeServices = snapshot.services.orEmpty().filter { it.isActive }
        val premiumTier = snapshot.identity.tier == com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier.PREMIUM ||
            snapshot.identity.tier == com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier.VIP
        // Premium/VIP members get the Services rail so their plan-linked access can be inspected;
        // only recorded active services are labeled "active" inside the Services panel.
        val hasServices = activeServices.isNotEmpty() || premiumTier
        val hasAdvertisement = snapshot.promotion != null

        fun serviceSummary(): String? =
            activeServices
                .map { it.serviceName }
                .filter { it.isNotBlank() }
                .distinct()
                .take(3)
                .joinToString(" • ")
                .ifBlank { null }

        fun menu(
            type: MenuType,
            visible: Boolean,
            locked: Boolean = false,
            defaultPriority: Int,
            railSide: MenuRailSide = MenuRailSide.LEFT,
            summaryOverride: String? = null
        ): MemberMenu {
            val menuSignals = signals.filter { it.sourceMenu == type }
            val primarySignal = menuSignals.minByOrNull { it.priority.ordinal }
            val severity = primarySignal?.priority
            val summary = summaryOverride ?: primarySignal?.subtitle ?: primarySignal?.title

            return MemberMenu(
                id = type,
                label = type.defaultLabel,
                priority = defaultPriority,
                isVisible = visible,
                isEnabled = visible && !locked,
                isLocked = locked,
                badgeCount = menuSignals.size,
                hasAlert = severity == SignalPriority.P0_CRITICAL || severity == SignalPriority.P1_ACTION_REQUIRED,
                severity = severity,
                summary = summary,
                railSide = railSide
            )
        }

        return listOf(
            // CORE: Home, Attendance, Plan, Payment
            menu(MenuType.HOME, true, defaultPriority = 10, railSide = MenuRailSide.LEFT),
            menu(MenuType.ATTENDANCE, true, defaultPriority = 20, railSide = MenuRailSide.LEFT),
            menu(
                MenuType.PLAN,
                true,
                defaultPriority = 30,
                railSide = MenuRailSide.LEFT,
                summaryOverride = snapshot.membership?.planName
            ),
            menu(MenuType.PAYMENT, true, defaultPriority = 40, railSide = MenuRailSide.LEFT),

            // TRAINING: Trainer, Workout
            menu(
                MenuType.TRAINER,
                visible = hasTrainer,
                locked = hasTrainer && !entitlements.allows(FeatureKey.TRAINER_MANAGEMENT),
                defaultPriority = 50,
                railSide = MenuRailSide.LEFT,
                summaryOverride = snapshot.trainer?.trainerName
            ),
            menu(MenuType.WORKOUT, visible = snapshot.workout != null, defaultPriority = 60, railSide = MenuRailSide.LEFT),

            // WELLNESS: Supplements, Nutrition
            menu(
                MenuType.SUPPLEMENTS,
                visible = hasSupplements,
                locked = hasSupplements && !entitlements.allows(FeatureKey.SUPPLEMENTS),
                defaultPriority = 70,
                railSide = MenuRailSide.LEFT,
                summaryOverride = snapshot.supplements?.lastPurchaseName
            ),
            menu(
                MenuType.NUTRITION,
                visible = hasNutrition,
                locked = hasNutrition && !entitlements.allows(FeatureKey.NUTRITION),
                defaultPriority = 80,
                railSide = MenuRailSide.LEFT,
                summaryOverride = snapshot.nutrition?.planName
            ),

            // SERVICES: Services
            menu(
                MenuType.SERVICES,
                visible = hasServices,
                defaultPriority = 90,
                railSide = MenuRailSide.LEFT,
                summaryOverride = serviceSummary() ?: if (premiumTier) "Premium access" else null
            ),

            // INTELLIGENCE: History, Insight
            menu(MenuType.HISTORY, true, defaultPriority = 100, railSide = MenuRailSide.LEFT),
            menu(MenuType.INSIGHT, true, locked = !entitlements.allows(FeatureKey.ADVANCED_INSIGHTS), defaultPriority = 110, railSide = MenuRailSide.LEFT),

            // UTILITY: More
            menu(MenuType.MORE, true, defaultPriority = 120, railSide = MenuRailSide.LEFT),

            // CONTEXTUAL: Offers
            menu(
                MenuType.ADVERTISEMENT,
                visible = hasAdvertisement,
                defaultPriority = 130,
                railSide = MenuRailSide.LEFT,
                summaryOverride = snapshot.promotion?.badge ?: snapshot.promotion?.title
            )
        ).filter { it.isVisible }.sortedBy { it.priority }
    }
}
