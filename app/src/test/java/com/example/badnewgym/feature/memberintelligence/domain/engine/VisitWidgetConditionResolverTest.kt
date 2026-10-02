package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VisitWidgetConditionResolverTest {
    private fun base(payment: PaymentSummary? = null): MemberSnapshot =
        MemberSnapshot(
            id = "m1",
            gymId = "g1",
            identity = MemberIdentity("Aman", null, MembershipTier.NORMAL, 0, "BG1", true),
            membership = MembershipStatus("Gold", "MONTHLY", 0, 0, 18, true),
            attendance = AttendanceSummary("Aug", 19, 28, 100, 4, 3.2),
            payment = payment,
            trainer = null,
            workout = WorkoutSummary(0, "Chest", 90, 500),
            supplements = null,
            nutrition = null,
            services = emptyList()
        )

    private fun event(type: EventType) = MemberEvent(
        id = "e1", memberId = "m1", gymId = "g1", eventType = type, occurredAt = 1L
    )

    @Test
    fun checkoutShowsGymTimeAndKeepsPaymentAlert() {
        val snapshot = base(PaymentSummary(2800.0, 0, null, null, null))
        val widgets = VisitWidgetConditionResolver.resolve(
            event(EventType.CHECK_OUT), snapshot, VisitWidgetEntitlements(), VisitWidgetLayout()
        ).map { it.id }

        assertTrue(VisitWidgetId.GYM_TIME in widgets)
        assertTrue(VisitWidgetId.PAYMENT_ALERT in widgets)
        assertFalse(VisitWidgetId.OFFERS in widgets)
    }

    @Test
    fun serverRevocationHidesPaidWidget() {
        val snapshot = base(PaymentSummary(2800.0, 0, null, null, null))
        val access = VisitWidgetEntitlements(
            serverEnabledFeatures = FeatureKey.entries.filter { it != FeatureKey.PAYMENT_AUTOMATION }.toSet(),
            subscriptionPlan = "Trial",
            subscriptionActive = true
        )
        val widgets = VisitWidgetConditionResolver.resolve(
            event(EventType.CHECK_OUT), snapshot, access, VisitWidgetLayout()
        ).map { it.id }

        assertFalse(VisitWidgetId.PAYMENT_ALERT in widgets)
        assertTrue(VisitWidgetId.GYM_TIME in widgets)
    }

    @Test
    fun localDisableWinsOnlyInsideEntitlement() {
        val layout = VisitWidgetLayout(
            locallyDisabled = setOf(VisitWidgetId.GYM_TIME)
        )
        val widgets = VisitWidgetConditionResolver.resolve(
            event(EventType.CHECK_OUT), base(), VisitWidgetEntitlements(), layout
        ).map { it.id }

        assertFalse(VisitWidgetId.GYM_TIME in widgets)
    }
}
