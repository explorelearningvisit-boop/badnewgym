package com.example.badnewgym.feature.memberintelligence.domain

import com.example.badnewgym.feature.memberintelligence.domain.fixture.MemberIntelligenceFixtureUniverse
import com.example.badnewgym.feature.memberintelligence.domain.fixture.TemporalBucket
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class MemberIntelligenceFixtureUniverseTest {

    @Test
    fun testEveryEventTypeHasAtLeastOneFixture() {
        val allFixtures = MemberIntelligenceFixtureUniverse.allEvents
        val coveredTypes = allFixtures.map { it.eventType }.toSet()

        // Verify every non-UNKNOWN EventType is covered
        for (eventType in EventType.values()) {
            assertTrue(
                "EventType ${eventType.name} must have a deterministic fixture in MemberIntelligenceFixtureUniverse",
                coveredTypes.contains(eventType)
            )
        }
        assertEquals("Should cover all 69 EventType values including UNKNOWN", 69, coveredTypes.size)
    }

    @Test
    fun testEveryEventCardKindIsReachable() {
        val allFixtures = MemberIntelligenceFixtureUniverse.allEvents
        val reachableKinds = allFixtures.map { it.cardKind }.toSet()

        for (kind in EventCardKind.values()) {
            if (kind == EventCardKind.INSIGHT) continue // Insight is a derived intelligence signal card
            assertTrue(
                "EventCardKind ${kind.name} must be reached by the fixture universe",
                reachableKinds.contains(kind)
            )
        }
    }

    @Test
    fun testEveryFixtureResolvesToExpectedCardKind() {
        val allFixtures = MemberIntelligenceFixtureUniverse.allEvents
        for (fixture in allFixtures) {
            val catalogSpec = EventCardCatalog.forEvent(fixture.eventType)
            assertEquals(
                "Fixture ${fixture.id} with EventType ${fixture.eventType} must resolve to catalog kind ${catalogSpec.kind}",
                catalogSpec.kind,
                fixture.cardKind
            )
        }
    }

    @Test
    fun testDeterministicRoutingMatchesCatalogSpec() {
        val allFixtures = MemberIntelligenceFixtureUniverse.allEvents
        for (fixture in allFixtures) {
            // For non-check-in events, route must exactly equal catalog.forEvent
            if (fixture.eventType != EventType.CHECK_IN) {
                val routedSpec = EventCardRouter.route(fixture.event, fixture.snapshot)
                assertEquals(
                    "Routing for ${fixture.eventType} should be deterministic",
                    EventCardCatalog.forEvent(fixture.eventType).kind,
                    routedSpec.kind
                )
            }
        }
    }

    @Test
    fun testWalkInToTrialStoryTransitions() {
        val walkInFixture = MemberIntelligenceFixtureUniverse.forEventType(EventType.WALK_IN)
        val trialStartedFixture = MemberIntelligenceFixtureUniverse.forEventType(EventType.TRIAL_STARTED)
        val trialConvertedFixture = MemberIntelligenceFixtureUniverse.forEventType(EventType.TRIAL_CONVERTED)
        val trialExpiredFixture = MemberIntelligenceFixtureUniverse.forEventType(EventType.TRIAL_EXPIRED)

        assertEquals(EventCardKind.WALK_IN, walkInFixture.cardKind)
        assertEquals(EventCardKind.TRIAL, trialStartedFixture.cardKind)
        assertEquals(EventCardKind.TRIAL, trialConvertedFixture.cardKind)
        assertEquals(EventCardKind.TRIAL, trialExpiredFixture.cardKind)

        // Verify evidence keys
        assertTrue(walkInFixture.event.metadata.containsKey("walkInAt"))
        assertTrue(trialStartedFixture.event.metadata.containsKey("startedAt"))
        assertTrue(trialConvertedFixture.event.metadata.containsKey("convertedAt"))
        assertTrue(trialExpiredFixture.event.metadata.containsKey("expiredAt"))

        // Verify primary decisions
        assertEquals(CardDecision.START_TRIAL, walkInFixture.spec.primaryDecision)
        assertEquals(CardDecision.OPEN_DETAIL, trialStartedFixture.spec.primaryDecision)
        assertEquals(CardDecision.RENEW_PLAN, trialConvertedFixture.spec.primaryDecision)
        assertEquals(CardDecision.CONVERT_TRIAL, trialExpiredFixture.spec.primaryDecision)
    }

    @Test
    fun testFreezeAndBanStatesAreDistinct() {
        val freezeStart = MemberIntelligenceFixtureUniverse.forEventType(EventType.FREEZE_STARTED)
        val freezeEnd = MemberIntelligenceFixtureUniverse.forEventType(EventType.FREEZE_ENDED)
        val banned = MemberIntelligenceFixtureUniverse.forEventType(EventType.BANNED)
        val banLifted = MemberIntelligenceFixtureUniverse.forEventType(EventType.BAN_LIFTED)

        assertEquals(EventCardKind.FREEZE, freezeStart.cardKind)
        assertEquals(EventCardKind.FREEZE, freezeEnd.cardKind)
        assertEquals(EventCardKind.BAN, banned.cardKind)
        assertEquals(EventCardKind.BAN, banLifted.cardKind)

        // Ban must have critical priority P0
        assertEquals(SignalPriority.P0_CRITICAL, banned.spec.priority)
        assertEquals(CardDecision.VIEW_BAN_REASON, banned.spec.primaryDecision)

        // Freeze must have P2 / unfreeze decision
        assertEquals(CardDecision.UNFREEZE, freezeStart.spec.primaryDecision)

        assertTrue(freezeStart.event.metadata.containsKey("freezeStart"))
        assertTrue(banned.event.metadata.containsKey("reason"))
        assertTrue(banLifted.event.metadata.containsKey("liftedAt"))
    }

    @Test
    fun testCheckInOverrideRoutingRemainsDeterministic() {
        val checkInFixture = MemberIntelligenceFixtureUniverse.forEventType(EventType.CHECK_IN)

        // 1. Normal clean check-in routes to CHECK_IN
        val cleanRoute = EventCardRouter.route(checkInFixture.event, checkInFixture.snapshot)
        assertEquals(EventCardKind.CHECK_IN, cleanRoute.kind)

        // 2. Check-in with overdue payment routes to PAYMENT_OVERDUE
        val overdueSnapshot = checkInFixture.snapshot.copy(
            issues = listOf(MemberIssue("iss_1", "Membership payment is overdue", IssueSeverity.HIGH))
        )
        val overdueRoute = EventCardRouter.route(checkInFixture.event, overdueSnapshot)
        assertEquals(EventType.PAYMENT_OVERDUE, overdueRoute.eventType)
        assertEquals(EventCardKind.PAYMENT, overdueRoute.kind)

        // 3. Check-in with expired membership routes to MEMBERSHIP_EXPIRED
        val expiredSnapshot = checkInFixture.snapshot.copy(
            recentEvents = listOf(MemberEvent("ev_exp", "m1", "g1", EventType.MEMBERSHIP_EXPIRED, System.currentTimeMillis()))
        )
        val expiredRoute = EventCardRouter.route(checkInFixture.event, expiredSnapshot)
        assertEquals(EventType.MEMBERSHIP_EXPIRED, expiredRoute.eventType)
        assertEquals(EventCardKind.MEMBERSHIP, expiredRoute.kind)

        // 4. Check-in with scheduled trainer session routes to TRAINER_SESSION_SCHEDULED
        val trainerSnapshot = checkInFixture.snapshot.copy(
            recentEvents = listOf(MemberEvent("ev_pt", "m1", "g1", EventType.TRAINER_SESSION_SCHEDULED, System.currentTimeMillis()))
        )
        val trainerRoute = EventCardRouter.route(checkInFixture.event, trainerSnapshot)
        assertEquals(EventType.TRAINER_SESSION_SCHEDULED, trainerRoute.eventType)
        assertEquals(EventCardKind.TRAINER, trainerRoute.kind)
    }

    @Test
    fun testEdgeCasesDeterministicProperties() {
        val edgeCases = MemberIntelligenceFixtureUniverse.edgeCases

        // 1. Long member name
        val longNameEdge = edgeCases.firstOrNull { it.id == "edge_long_name" }
        assertNotNull(longNameEdge)
        assertTrue(longNameEdge!!.snapshot.identity.name.length > 30)

        // 2. Missing photo
        val missingPhotoEdge = edgeCases.firstOrNull { it.id == "edge_missing_photo" }
        assertNotNull(missingPhotoEdge)
        assertNull(missingPhotoEdge!!.snapshot.identity.photoUrl)

        // 3. Missing payment
        val missingPaymentEdge = edgeCases.firstOrNull { it.id == "edge_missing_payment" }
        assertNotNull(missingPaymentEdge)
        assertNull(missingPaymentEdge!!.snapshot.payment)

        // 4. Zero outstanding
        val zeroOutstandingEdge = edgeCases.firstOrNull { it.id == "edge_zero_outstanding" }
        assertNotNull(zeroOutstandingEdge)
        assertEquals(0.0, zeroOutstandingEdge!!.snapshot.payment?.totalOutstanding ?: -1.0, 0.001)

        // 5. Incomplete attendance pattern
        val incompletePatternEdge = edgeCases.firstOrNull { it.id == "edge_incomplete_pattern" }
        assertNotNull(incompletePatternEdge)
        assertTrue((incompletePatternEdge!!.snapshot.attendance?.weeklyPattern?.size ?: 0) < 7)

        // 6. Exactly 7 days attendance pattern
        val exactly7DaysEdge = edgeCases.firstOrNull { it.id == "edge_7_days_pattern" }
        assertNotNull(exactly7DaysEdge)
        assertEquals(7, exactly7DaysEdge!!.snapshot.attendance?.weeklyPattern?.size ?: 0)

        // 7. No active services
        val noServiceEdge = edgeCases.firstOrNull { it.id == "edge_no_service" }
        assertNotNull(noServiceEdge)
        assertTrue(noServiceEdge!!.snapshot.services.orEmpty().isEmpty())

        // 8. No trainer
        val noTrainerEdge = edgeCases.firstOrNull { it.id == "edge_no_trainer" }
        assertNotNull(noTrainerEdge)
        assertNull(noTrainerEdge!!.snapshot.trainer)
    }

    @Test
    fun testTemporalBucketingIntegrity() {
        val nowItems = MemberIntelligenceFixtureUniverse.forTemporalBucket(TemporalBucket.NOW)
        val pastItems = MemberIntelligenceFixtureUniverse.forTemporalBucket(TemporalBucket.PAST)
        val futureItems = MemberIntelligenceFixtureUniverse.forTemporalBucket(TemporalBucket.FUTURE)

        assertTrue("NOW bucket must have fixtures", nowItems.isNotEmpty())
        assertTrue("PAST bucket must have fixtures", pastItems.isNotEmpty())
        assertTrue("FUTURE bucket must have fixtures", futureItems.isNotEmpty())

        val totalBucketCount = nowItems.size + pastItems.size + futureItems.size
        assertEquals(MemberIntelligenceFixtureUniverse.allFixtures.size, totalBucketCount)
    }
}
