package com.example.badnewgym.feature.memberintelligence.domain.layout

import com.example.badnewgym.feature.memberintelligence.domain.hero.EventPriority
import com.example.badnewgym.feature.memberintelligence.domain.hero.HeroEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MiEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.MiFeature
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetSize

/**
 * Compact card widget slot (spec section 6/40/47).
 *
 * Each slot declares everything the layout engine needs: priority, visibility,
 * condition, size, order, data availability, entitlement, hero eligibility,
 * primary action and detail destination.
 */
enum class WidgetSlotKind {
    HERO_EVENT,
    MEMBER_IDENTITY,
    PAYMENT,
    TRAINER,
    MACHINE_ISSUE,
    ATTENDANCE,
    WORKOUT,
    BODY_GOALS,
    PROGRESS_PHOTOS,
    PRIMARY_ACTION
}

data class WidgetSlot(
    val kind: WidgetSlotKind,
    val priority: Int,
    val minHeightDp: Int,
    val preferredHeightDp: Int,
    val heroEligible: Boolean = false,
    val feature: MiFeature? = null,
    val order: Int = 0
) {
    fun visible(entitlement: MiEntitlement): Boolean = feature?.let { entitlement.permits(it) } ?: true
}

/**
 * Widget layout engine (spec 6/47).
 *
 * Responsibilities:
 *  1. choose the single hero
 *  2. order supporting widgets by priority
 *  3. decide what fits the height budget
 *  4. route what does not fit to a detail destination rather than shrinking it
 *
 * The engine never compresses a widget below its readable minimum. Overflow is
 * demoted to a detail sheet instead (spec 6 / detail-first strategy 65).
 */
object WidgetLayoutEngine {

    /** Height budget in dp for the compact runtime card. */
    const val PREFERRED_BUDGET_DP = 520
    const val MAX_BUDGET_DP = 600
    const val CHROME_DP = 44 // rail + header chrome

    data class Plan(
        val hero: HeroEvent,
        /** Ordered slots that fit inside the budget. */
        val visible: List<WidgetSlot>,
        /** Slots that did not fit; each must expose a detail destination. */
        val deferred: List<WidgetSlot>,
        /** True when the card must render as a scrollable region. */
        val scrollable: Boolean
    )

    fun plan(
        hero: HeroEvent,
        slots: List<WidgetSlot>,
        entitlement: MiEntitlement,
        adminOrder: List<WidgetSlotKind> = emptyList(),
        adminDisabled: Set<WidgetSlotKind> = emptySet(),
        budgetDp: Int = PREFERRED_BUDGET_DP
    ): Plan {
        val eligible = slots
            .filter { it.visible(entitlement) && it.kind !in adminDisabled }
            .filter { it.kind != WidgetSlotKind.HERO_EVENT }
            .sortedWith(
                compareBy(
                    { adminOrder.indexOf(it.kind).let { i -> if (i >= 0) i else Int.MAX_VALUE } },
                    { -it.priority },
                    { it.order }
                )
            )

        var used = CHROME_DP + (slots.firstOrNull { it.kind == WidgetSlotKind.HERO_EVENT }?.preferredHeightDp ?: 64)
        val visible = mutableListOf<WidgetSlot>()
        val deferred = mutableListOf<WidgetSlot>()
        var scrollable = false

        for (slot in eligible) {
            if (used + slot.preferredHeightDp <= budgetDp) {
                visible += slot
                used += slot.preferredHeightDp
            } else {
                // Do not squeeze. Demote to detail.
                deferred += slot
                scrollable = true
            }
        }

        // If even the top priority widget cannot fit, allow it and scroll rather
        // than hiding the most important operational intelligence.
        if (visible.isEmpty() && deferred.isNotEmpty()) {
            visible += deferred.removeAt(0)
            scrollable = true
        }

        return Plan(hero, visible, deferred, scrollable)
    }

    /** Maps the spec's 5 logical sizes onto compact-card semantics. */
    fun spanFor(size: VisitWidgetSize): WidgetSpan = when (size) {
        VisitWidgetSize.XS -> WidgetSpan(1, 1)
        VisitWidgetSize.S -> WidgetSpan(1, 1)
        VisitWidgetSize.M -> WidgetSpan(1, 1)
        VisitWidgetSize.L -> WidgetSpan(1, 1)
        VisitWidgetSize.XL -> WidgetSpan(1, 1)
    }

    data class WidgetSpan(val columns: Int, val rows: Int) {
        val isHalf: Boolean get() = columns == 1
    }

    /** Baseline slot catalogue with spec priorities. */
    fun defaultSlots(hasTrainer: Boolean, hasPhotos: Boolean): List<WidgetSlot> {
        val list = mutableListOf(
            WidgetSlot(WidgetSlotKind.HERO_EVENT, 100, 64, 64, heroEligible = true),
            WidgetSlot(WidgetSlotKind.MEMBER_IDENTITY, 95, 86, 86, feature = MiFeature.MEMBER_CARD),
            WidgetSlot(WidgetSlotKind.PAYMENT, 90, 96, 96, feature = MiFeature.PAYMENT_COLLECTION),
            WidgetSlot(WidgetSlotKind.ATTENDANCE, 60, 118, 118, feature = MiFeature.ATTENDANCE),
            WidgetSlot(WidgetSlotKind.WORKOUT, 50, 132, 132, feature = MiFeature.WORKOUT),
            WidgetSlot(WidgetSlotKind.BODY_GOALS, 45, 120, 120, feature = MiFeature.BODY_GOALS),
            WidgetSlot(WidgetSlotKind.PRIMARY_ACTION, 40, 44, 44)
        )
        if (hasTrainer) list.add(WidgetSlot(WidgetSlotKind.TRAINER, 80, 84, 84, feature = MiFeature.TRAINER))
        if (hasPhotos) list.add(WidgetSlot(WidgetSlotKind.PROGRESS_PHOTOS, 30, 76, 76, feature = MiFeature.PROGRESS_PHOTOS))
        return list
    }

    /** Machine issues displace everything: they become the hero. */
    fun slotsWithMachineIssue(): List<WidgetSlot> =
        listOf(WidgetSlot(WidgetSlotKind.MACHINE_ISSUE, 100, 84, 84, feature = MiFeature.MACHINE_ISSUES)) +
            defaultSlots(hasTrainer = false, hasPhotos = false)
                .filter { it.kind != WidgetSlotKind.HERO_EVENT }

    fun priorityOf(priority: EventPriority): Int = priority.score
}