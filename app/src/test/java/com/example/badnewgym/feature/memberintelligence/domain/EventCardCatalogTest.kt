package com.example.badnewgym.feature.memberintelligence.domain

import com.example.badnewgym.feature.memberintelligence.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class EventCardCatalogTest {

    @Test
    fun testEventCardCatalogHasAllExpectedEvents() {
        // Verify key representative events exist in the catalog
        val walkInSpec = EventCardCatalog.forEvent(EventType.WALK_IN)
        assertEquals(EventCardKind.WALK_IN, walkInSpec.kind)
        assertEquals(MenuType.ATTENDANCE, walkInSpec.defaultMenu)
        assertEquals(SignalPriority.P2_IMPORTANT, walkInSpec.priority)
        assertEquals(CardDecision.START_TRIAL, walkInSpec.primaryDecision)
        assertTrue(walkInSpec.showTimeline)
        assertTrue(walkInSpec.evidenceKeys.contains("walkInAt"))
        assertTrue(walkInSpec.evidenceKeys.contains("trialDays"))

        val trialStartedSpec = EventCardCatalog.forEvent(EventType.TRIAL_STARTED)
        assertEquals(EventCardKind.TRIAL, trialStartedSpec.kind)
        assertEquals(MenuType.PLAN, trialStartedSpec.defaultMenu)

        val trialExpiredSpec = EventCardCatalog.forEvent(EventType.TRIAL_EXPIRED)
        assertEquals(EventCardKind.TRIAL, trialExpiredSpec.kind)
        assertEquals(SignalPriority.P1_ACTION_REQUIRED, trialExpiredSpec.priority)
        assertEquals(CardDecision.CONVERT_TRIAL, trialExpiredSpec.primaryDecision)

        val freezeSpec = EventCardCatalog.forEvent(EventType.FREEZE_STARTED)
        assertEquals(EventCardKind.FREEZE, freezeSpec.kind)
        assertEquals(CardDecision.UNFREEZE, freezeSpec.primaryDecision)

        val banSpec = EventCardCatalog.forEvent(EventType.BANNED)
        assertEquals(EventCardKind.BAN, banSpec.kind)
        assertEquals(SignalPriority.P0_CRITICAL, banSpec.priority)
        assertEquals(CardDecision.VIEW_BAN_REASON, banSpec.primaryDecision)

        val paymentOverdueSpec = EventCardCatalog.forEvent(EventType.PAYMENT_OVERDUE)
        assertEquals(EventCardKind.PAYMENT, paymentOverdueSpec.kind)
        assertEquals(MenuType.PAYMENT, paymentOverdueSpec.defaultMenu)
        assertEquals(SignalPriority.P1_ACTION_REQUIRED, paymentOverdueSpec.priority)
        assertEquals(CardDecision.COLLECT_PAYMENT, paymentOverdueSpec.primaryDecision)

        val machineFaultSpec = EventCardCatalog.forEvent(EventType.MACHINE_FAULT)
        assertEquals(EventCardKind.FACILITY_OPERATION, machineFaultSpec.kind)
        assertEquals(SignalPriority.P0_CRITICAL, machineFaultSpec.priority)
    }

    @Test
    fun testEventCardRouterCheckInOverrides() {
        val baseIdentity = MemberIdentity("Test User", null, MembershipTier.NORMAL, System.currentTimeMillis())
        val checkInEvent = MemberEvent("e1", "m1", "g1", EventType.CHECK_IN, System.currentTimeMillis())

        // 1. Normal Check-in with no issues
        val cleanSnapshot = MemberSnapshot(
            gymId = "gym1",
            identity = baseIdentity,
            membership = null,
            attendance = null,
            payment = null,
            trainer = null,
            workout = null,
            supplements = null,
            nutrition = null,
            services = null
        )
        val cleanRoute = EventCardRouter.route(checkInEvent, cleanSnapshot)
        assertEquals(EventCardKind.CHECK_IN, cleanRoute.kind)
        assertEquals(MenuType.ATTENDANCE, cleanRoute.defaultMenu)

        // 2. Check-in with Overdue Payment in issues
        val overdueSnapshot = cleanSnapshot.copy(
            issues = listOf(MemberIssue("i1", "Membership payment is overdue by 5 days", IssueSeverity.HIGH))
        )
        val overdueRoute = EventCardRouter.route(checkInEvent, overdueSnapshot)
        assertEquals(EventType.PAYMENT_OVERDUE, overdueRoute.eventType)
        assertEquals(MenuType.PAYMENT, overdueRoute.defaultMenu)
        assertEquals(CardDecision.COLLECT_PAYMENT, overdueRoute.primaryDecision)

        // 3. Check-in with Expired Membership in recentEvents
        val expiredSnapshot = cleanSnapshot.copy(
            recentEvents = listOf(MemberEvent("e2", "m1", "g1", EventType.MEMBERSHIP_EXPIRED, System.currentTimeMillis()))
        )
        val expiredRoute = EventCardRouter.route(checkInEvent, expiredSnapshot)
        assertEquals(EventType.MEMBERSHIP_EXPIRED, expiredRoute.eventType)
        assertEquals(MenuType.PLAN, expiredRoute.defaultMenu)
        assertEquals(CardDecision.RENEW_PLAN, expiredRoute.primaryDecision)

        // 4. Check-in with Scheduled Trainer Session
        val trainerSnapshot = cleanSnapshot.copy(
            recentEvents = listOf(MemberEvent("e3", "m1", "g1", EventType.TRAINER_SESSION_SCHEDULED, System.currentTimeMillis()))
        )
        val trainerRoute = EventCardRouter.route(checkInEvent, trainerSnapshot)
        assertEquals(EventType.TRAINER_SESSION_SCHEDULED, trainerRoute.eventType)
        assertEquals(MenuType.TRAINER, trainerRoute.defaultMenu)
        assertEquals(CardDecision.START_SESSION, trainerRoute.primaryDecision)
    }

    @Test
    fun testNonCheckInEventRouting() {
        val baseIdentity = MemberIdentity("Test User", null, MembershipTier.NORMAL, System.currentTimeMillis())
        val emptySnapshot = MemberSnapshot(
            gymId = "gym1",
            identity = baseIdentity,
            membership = null,
            attendance = null,
            payment = null,
            trainer = null,
            workout = null,
            supplements = null,
            nutrition = null,
            services = null
        )

        val workoutEvent = MemberEvent("e1", "m1", "g1", EventType.WORKOUT_COMPLETED, System.currentTimeMillis())
        val workoutRoute = EventCardRouter.route(workoutEvent, emptySnapshot)
        assertEquals(EventType.WORKOUT_COMPLETED, workoutRoute.eventType)
        assertEquals(MenuType.WORKOUT, workoutRoute.defaultMenu)

        val faultEvent = MemberEvent("e2", "m1", "g1", EventType.MACHINE_FAULT, System.currentTimeMillis())
        val faultRoute = EventCardRouter.route(faultEvent, emptySnapshot)
        assertEquals(EventType.MACHINE_FAULT, faultRoute.eventType)
        assertEquals(MenuType.SERVICES, faultRoute.defaultMenu)
    }

    @Test
    fun testEventCardVariantResolver() {
        // Late check-in
        val lateCheckIn = MemberEvent("e1", "m1", "g1", EventType.CHECK_IN, System.currentTimeMillis(), metadata = mapOf("latenessMinutes" to "15"))
        val latePres = EventCardVariantResolver.resolve(lateCheckIn)
        assertEquals(EventCardVariant.LATE_CHECK_IN, latePres.variant)
        assertEquals("Late check-in (15 min)", latePres.headlineOverride)

        // Early check-in
        val earlyCheckIn = MemberEvent("e2", "m1", "g1", EventType.CHECK_IN, System.currentTimeMillis(), metadata = mapOf("earlyMinutes" to "20"))
        val earlyPres = EventCardVariantResolver.resolve(earlyCheckIn)
        assertEquals(EventCardVariant.EARLY_CHECK_IN, earlyPres.variant)
        assertEquals("Early check-in (20 min)", earlyPres.headlineOverride)

        // Overdue payment
        val overduePayment = MemberEvent("e3", "m1", "g1", EventType.PAYMENT_OVERDUE, System.currentTimeMillis(), metadata = mapOf("overdueDays" to "7"))
        val overduePres = EventCardVariantResolver.resolve(overduePayment)
        assertEquals(EventCardVariant.OVERDUE, overduePres.variant)
        assertEquals("Payment overdue (7 days)", overduePres.headlineOverride)

        // Payment failed
        val failedPayment = MemberEvent("e4", "m1", "g1", EventType.PAYMENT_FAILED, System.currentTimeMillis())
        val failedPres = EventCardVariantResolver.resolve(failedPayment)
        assertEquals(EventCardVariant.FAILED, failedPres.variant)

        // Trial converted
        val trialConverted = MemberEvent("e5", "m1", "g1", EventType.TRIAL_CONVERTED, System.currentTimeMillis())
        val convertedPres = EventCardVariantResolver.resolve(trialConverted)
        assertEquals(EventCardVariant.CONVERTED, convertedPres.variant)

        // Trial expired
        val trialExpired = MemberEvent("e6", "m1", "g1", EventType.TRIAL_EXPIRED, System.currentTimeMillis())
        val expiredPres = EventCardVariantResolver.resolve(trialExpired)
        assertEquals(EventCardVariant.EXPIRED, expiredPres.variant)

        // Ban active
        val banned = MemberEvent("e7", "m1", "g1", EventType.BANNED, System.currentTimeMillis())
        val bannedPres = EventCardVariantResolver.resolve(banned)
        assertEquals(EventCardVariant.BLOCKED, bannedPres.variant)

        // Ban lifted
        val banLifted = MemberEvent("e8", "m1", "g1", EventType.BAN_LIFTED, System.currentTimeMillis())
        val banLiftedPres = EventCardVariantResolver.resolve(banLifted)
        assertEquals(EventCardVariant.RESOLVED, banLiftedPres.variant)
    }
}

