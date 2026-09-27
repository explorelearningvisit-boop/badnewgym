package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import org.junit.Assert.assertEquals
import org.junit.Test

class MemberBusinessStateResolverTest {

    private fun snapshot(events: List<MemberEvent> = emptyList()): MemberSnapshot =
        MemberSnapshot(
            id = "m1",
            gymId = "g1",
            identity = MemberIdentity("A", null, MembershipTier.NORMAL, 0, "BG1"),
            membership = null,
            attendance = null,
            payment = null,
            trainer = null,
            workout = null,
            supplements = null,
            nutrition = null,
            services = null,
            recentEvents = events
        )

    private fun event(type: EventType, at: Long): MemberEvent =
        MemberEvent(
            id = type.name + at,
            memberId = "m1",
            gymId = "g1",
            eventType = type,
            occurredAt = at
        )

    @Test
    fun expiredStateWinsOverLaterCheckIn() {
        val expired = event(EventType.MEMBERSHIP_EXPIRED, 100)
        val checkIn = event(EventType.CHECK_IN, 200)
        assertEquals(
            EventType.MEMBERSHIP_EXPIRED,
            MemberBusinessStateResolver.resolveDisplayEvent(snapshot(listOf(expired, checkIn)), checkIn)?.eventType
        )
    }

    @Test
    fun machineFaultWinsOverRoutineActivity() {
        val fault = event(EventType.MACHINE_FAULT, 100)
        val checkIn = event(EventType.CHECK_IN, 200)
        assertEquals(
            EventType.MACHINE_FAULT,
            MemberBusinessStateResolver.resolveDisplayEvent(snapshot(listOf(fault, checkIn)), checkIn)?.eventType
        )
    }

    @Test
    fun currentEventIsUsedWhenNoHigherPriorityStateExists() {
        val checkIn = event(EventType.CHECK_IN, 200)
        assertEquals(
            EventType.CHECK_IN,
            MemberBusinessStateResolver.resolveDisplayEvent(snapshot(), checkIn)?.eventType
        )
    }
}
