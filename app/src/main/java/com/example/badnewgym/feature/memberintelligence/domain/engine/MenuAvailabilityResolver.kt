package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureKey
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
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
        val hasServices = snapshot.services.orEmpty().any { it.isActive }

        fun menu(
            type: MenuType,
            visible: Boolean,
            locked: Boolean = false,
            defaultPriority: Int
        ): MemberMenu {
            val menuSignals = signals.filter { it.sourceMenu == type }
            val primarySignal = menuSignals.minByOrNull { it.priority.ordinal }
            
            val severity = primarySignal?.priority
            val summary = primarySignal?.subtitle ?: primarySignal?.title
            
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
                summary = summary
            )
        }

        return listOf(
            menu(MenuType.HOME, true, defaultPriority = 0),
            menu(MenuType.ATTENDANCE, true, defaultPriority = 10),
            menu(MenuType.PLAN, true, defaultPriority = 20),
            menu(MenuType.PAYMENT, true, defaultPriority = 30),
            menu(
                MenuType.TRAINER,
                visible = hasTrainer,
                locked = hasTrainer && !entitlements.allows(FeatureKey.TRAINER_MANAGEMENT),
                defaultPriority = 40
            ),
            menu(MenuType.WORKOUT, visible = snapshot.workout != null, defaultPriority = 50),
            menu(
                MenuType.SUPPLEMENTS,
                visible = hasSupplements,
                locked = hasSupplements && !entitlements.allows(FeatureKey.SUPPLEMENTS),
                defaultPriority = 60
            ),
            menu(
                MenuType.NUTRITION,
                visible = hasNutrition,
                locked = hasNutrition && !entitlements.allows(FeatureKey.NUTRITION),
                defaultPriority = 70
            ),
            menu(MenuType.SERVICES, visible = hasServices, defaultPriority = 80),
            menu(MenuType.HISTORY, true, defaultPriority = 90),
            menu(
                MenuType.INSIGHT,
                visible = true,
                locked = !entitlements.allows(FeatureKey.ADVANCED_INSIGHTS),
                defaultPriority = 100
            )
        ).filter { it.isVisible }.sortedBy { it.priority }
    }
}
