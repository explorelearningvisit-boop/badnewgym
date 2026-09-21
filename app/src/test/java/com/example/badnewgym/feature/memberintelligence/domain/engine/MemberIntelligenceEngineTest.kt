package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MemberIntelligenceEngineTest {

    private lateinit var engine: MemberIntelligenceEngine

    @Before
    fun setup() {
        engine = MemberIntelligenceEngine()
    }

    private fun createBaseSnapshot(): MemberSnapshot {
        return MemberSnapshot(
            id = "test_member",
            gymId = "test_gym",
            identity = MemberIdentity(
                name = "Test",
                photoUrl = null,
                tier = MembershipTier.NORMAL,
                memberSince = 0
            ),
            membership = null,
            attendance = null,
            payment = null,
            trainer = null,
            workout = null,
            supplements = null,
            nutrition = null,
            services = null
        )
    }

    private fun createBaseEvent(): MemberEvent {
        return MemberEvent(
            id = "test_event",
            memberId = "test_member",
            gymId = "test_gym",
            eventType = EventType.CHECK_IN,
            occurredAt = 0
        )
    }

    @Test
    fun `generateSignals correctly prioritizes overdue payments as P0_CRITICAL`() {
        val snapshot = createBaseSnapshot().copy(
            payment = PaymentSummary(
                totalOutstanding = 1000.0,
                overdueDays = 10, // > 7 days is P0
                dueDate = 0,
                lastPaymentAmount = null,
                lastPaymentDate = null
            )
        )
        
        val signals = engine.generateSignals(snapshot, createBaseEvent(), 0)
        
        assertEquals(1, signals.size)
        assertEquals(SignalCategory.PAYMENT, signals[0].category)
        assertEquals(SignalPriority.P0_CRITICAL, signals[0].priority)
    }

    @Test
    fun `generateSignals correctly prioritizes mildly overdue payments as P1_ACTION_REQUIRED`() {
        val snapshot = createBaseSnapshot().copy(
            payment = PaymentSummary(
                totalOutstanding = 1000.0,
                overdueDays = 3, // <= 7 days is P1
                dueDate = 0,
                lastPaymentAmount = null,
                lastPaymentDate = null
            )
        )
        
        val signals = engine.generateSignals(snapshot, createBaseEvent(), 0)
        
        assertEquals(1, signals.size)
        assertEquals(SignalCategory.PAYMENT, signals[0].category)
        assertEquals(SignalPriority.P1_ACTION_REQUIRED, signals[0].priority)
    }

    @Test
    fun `generateSignals flags expired memberships as P0_CRITICAL`() {
        val snapshot = createBaseSnapshot().copy(
            membership = MembershipStatus(
                planName = "Basic",
                planType = "Prepaid",
                isActive = false, // Expired
                daysRemaining = -5,
                startDate = 0,
                expiryDate = 0
            )
        )
        
        val signals = engine.generateSignals(snapshot, createBaseEvent(), 0)
        
        assertEquals(1, signals.size)
        assertEquals(SignalCategory.MEMBERSHIP, signals[0].category)
        assertEquals(SignalPriority.P0_CRITICAL, signals[0].priority)
    }

    @Test
    fun `generateSignals sorts P0 signals before P1 signals`() {
        // Expired membership (P0) and mildly overdue payment (P1)
        val snapshot = createBaseSnapshot().copy(
            membership = MembershipStatus(
                planName = "Basic",
                planType = "Prepaid",
                isActive = false,
                daysRemaining = -5,
                startDate = 0,
                expiryDate = 0
            ),
            payment = PaymentSummary(
                totalOutstanding = 1000.0,
                overdueDays = 3,
                dueDate = 0,
                lastPaymentAmount = null,
                lastPaymentDate = null
            )
        )
        
        val signals = engine.generateSignals(snapshot, createBaseEvent(), 0)
        
        assertEquals(2, signals.size)
        
        // P0 (Membership) should be first
        assertEquals(SignalPriority.P0_CRITICAL, signals[0].priority)
        assertEquals(SignalCategory.MEMBERSHIP, signals[0].category)
        
        // P1 (Payment) should be second
        assertEquals(SignalPriority.P1_ACTION_REQUIRED, signals[1].priority)
        assertEquals(SignalCategory.PAYMENT, signals[1].category)
    }
}
