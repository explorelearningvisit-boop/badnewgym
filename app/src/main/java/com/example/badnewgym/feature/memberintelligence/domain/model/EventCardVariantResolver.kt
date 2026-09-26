package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Contextual presentation variants layered on top of the 68 typed event variants.
 *
 * We intentionally do NOT create a new Composable for every variant. A late check-in
 * is still a Check-in card; its evidence/state/CTA/emphasis changes. This keeps the
 * UI reusable while preserving strong visual and semantic variety.
 */
enum class EventCardVariant {
    DEFAULT,
    LATE,
    EARLY,
    OVERDUE,
    FAILED,
    PARTIAL,
    RESOLVED,
    ACTIVE,
    EXPIRING,
    CONVERTED,
    EXPIRED,
    BLOCKED,
    REOPENED
}

data class EventCardPresentation(
    val variant: EventCardVariant,
    val headlineOverride: String? = null,
    val emphasisKey: String? = null
)

object EventCardVariantResolver {

    fun resolve(event: MemberEvent): EventCardPresentation {
        val metadata = event.metadata

        fun bool(key: String): Boolean = metadata[key]?.equals("true", ignoreCase = true) == true
        fun minutes(key: String): Long = metadata[key]?.toLongOrNull() ?: 0L

        return when (event.eventType) {
            EventType.CHECK_IN -> when {
                minutes("latenessMinutes") > 0L || bool("late") -> EventCardPresentation(
                    EventCardVariant.LATE,
                    "Late check-in",
                    "late-arrival"
                )
                minutes("earlinessMinutes") > 0L || bool("early") -> EventCardPresentation(
                    EventCardVariant.EARLY,
                    "Early check-in",
                    "early-arrival"
                )
                else -> EventCardPresentation(EventCardVariant.DEFAULT)
            }

            EventType.PAYMENT_SUCCESS, EventType.PAYMENT -> when {
                bool("wasOverdue") || bool("latePayment") || minutes("overdueDays") > 0L -> EventCardPresentation(
                    EventCardVariant.OVERDUE,
                    "Late payment received",
                    "late-payment"
                )
                else -> EventCardPresentation(EventCardVariant.DEFAULT)
            }

            EventType.PAYMENT_FAILED -> EventCardPresentation(
                EventCardVariant.FAILED,
                "Payment failed",
                "payment-failed"
            )

            EventType.PAYMENT_PARTIAL -> EventCardPresentation(
                EventCardVariant.PARTIAL,
                "Partial payment",
                "payment-partial"
            )

            EventType.PAYMENT_OVERDUE -> EventCardPresentation(
                EventCardVariant.OVERDUE,
                "Payment overdue",
                "payment-overdue"
            )

            EventType.MACHINE_FIXED,
            EventType.MAINTENANCE_COMPLETED,
            EventType.CLEANING_COMPLETED,
            EventType.INCIDENT_RESOLVED,
            EventType.COMPLAINT_RESOLVED,
            EventType.SERVICE_DEACTIVATED -> EventCardPresentation(
                EventCardVariant.RESOLVED,
                emphasisKey = "resolved"
            )

            EventType.MACHINE_FAULT,
            EventType.MACHINE_REPORTED,
            EventType.SERVICE_ISSUE,
            EventType.INCIDENT_REPORTED,
            EventType.COMPLAINT -> EventCardPresentation(
                EventCardVariant.ACTIVE,
                emphasisKey = "issue-active"
            )

            EventType.TRIAL_STARTED,
            EventType.FREEZE_STARTED,
            EventType.FREEZE -> EventCardPresentation(
                EventCardVariant.ACTIVE,
                emphasisKey = "state-active"
            )

            EventType.TRIAL_CONVERTED -> EventCardPresentation(
                EventCardVariant.CONVERTED,
                "Trial converted",
                "trial-converted"
            )

            EventType.TRIAL_EXPIRED,
            EventType.MEMBERSHIP_EXPIRED,
            EventType.EXPIRED,
            EventType.SERVICE_EXPIRED -> EventCardPresentation(
                EventCardVariant.EXPIRED,
                emphasisKey = "expired"
            )

            EventType.BANNED -> EventCardPresentation(
                EventCardVariant.BLOCKED,
                "Access banned",
                "access-blocked"
            )

            EventType.BAN_LIFTED,
            EventType.FREEZE_ENDED,
            EventType.REACTIVATION -> EventCardPresentation(
                EventCardVariant.REOPENED,
                emphasisKey = "access-restored"
            )

            else -> EventCardPresentation(EventCardVariant.DEFAULT)
        }
    }
}
