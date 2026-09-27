package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot

/**
 * Chooses the event/state that deserves the member card's visual foreground.
 *
 * A check-in is not always the most important thing that happened to a member.
 * A current expired plan, overdue payment, ban, machine/service issue or active
 * freeze must remain visible when it is present in the loaded evidence window.
 *
 * This resolver never invents an event: it only promotes an event that already
 * exists in the snapshot/recent event stream.
 */
object MemberBusinessStateResolver {

    fun resolveDisplayEvent(
        snapshot: MemberSnapshot,
        currentEvent: MemberEvent?
    ): MemberEvent? {
        val candidates = buildList {
            currentEvent?.let(::add)
            addAll(snapshot.recentEvents)
        }
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<MemberEvent> { statePriority(it, snapshot) }
                    .thenByDescending { it.occurredAt }
            )

        return candidates.firstOrNull()
    }

    private fun statePriority(
        event: MemberEvent,
        snapshot: MemberSnapshot
    ): Int {
        return when (event.eventType) {
            EventType.INCIDENT_REPORTED -> 1000
            EventType.MACHINE_FAULT -> 990
            EventType.BANNED -> 980
            EventType.PAYMENT_FAILED -> 970
            EventType.PAYMENT_OVERDUE -> 960
            EventType.MEMBERSHIP_EXPIRED,
            EventType.EXPIRED -> 950
            EventType.FREEZE_STARTED,
            EventType.FREEZE -> 940
            EventType.COMPLAINT -> 930
            EventType.SERVICE_ISSUE -> 920
            EventType.SERVICE_EXPIRED -> 910
            EventType.TRAINER_SESSION_MISSED -> 900
            EventType.PAYMENT_DUE -> 880
            EventType.TRIAL_EXPIRED -> 870
            EventType.MACHINE_REPORTED,
            EventType.MAINTENANCE_STARTED,
            EventType.CLEANING_STARTED,
            EventType.STOCK_LOW -> 850
            EventType.TRAINER_SESSION_SCHEDULED -> 700
            EventType.SERVICE_BOOKED -> 680
            EventType.WORKOUT_SKIPPED -> 650
            EventType.CHECK_IN -> {
                when {
                    snapshot.payment?.totalOutstanding?.let { it > 0 } == true -> 875
                    snapshot.membership?.isActive == false -> 875
                    else -> 400
                }
            }
            else -> 500
        }
    }
}
