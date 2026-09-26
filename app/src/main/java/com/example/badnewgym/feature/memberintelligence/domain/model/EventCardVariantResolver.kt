package com.example.badnewgym.feature.memberintelligence.domain.model

enum class EventCardVariant {
    DEFAULT,
    LATE_CHECK_IN,
    EARLY_CHECK_IN,
    OVERDUE,
    FAILED,
    PARTIAL,
    ACTIVE,
    RESOLVED,
    CONVERTED,
    EXPIRED,
    BLOCKED,
    REOPENED
}

data class EventCardPresentation(
    val variant: EventCardVariant = EventCardVariant.DEFAULT,
    val headlineOverride: String? = null
)

object EventCardVariantResolver {
    fun resolve(event: MemberEvent): EventCardPresentation {
        val meta = event.metadata
        return when (event.eventType) {
            EventType.CHECK_IN -> {
                val lateMinutes = meta["latenessMinutes"]?.toLongOrNull() ?: 0L
                val isLate = lateMinutes > 0L || meta["late"]?.equals("true", ignoreCase = true) == true
                val earlyMinutes = meta["earlyMinutes"]?.toLongOrNull() ?: 0L
                val isEarly = earlyMinutes > 0L || meta["early"]?.equals("true", ignoreCase = true) == true

                when {
                    isLate -> EventCardPresentation(
                        variant = EventCardVariant.LATE_CHECK_IN,
                        headlineOverride = if (lateMinutes > 0L) "Late check-in ($lateMinutes min)" else "Late check-in"
                    )
                    isEarly -> EventCardPresentation(
                        variant = EventCardVariant.EARLY_CHECK_IN,
                        headlineOverride = if (earlyMinutes > 0L) "Early check-in ($earlyMinutes min)" else "Early check-in"
                    )
                    else -> EventCardPresentation(EventCardVariant.DEFAULT)
                }
            }
            EventType.PAYMENT_OVERDUE -> {
                val overdueDays = meta["overdueDays"] ?: meta["daysOverdue"]
                EventCardPresentation(
                    variant = EventCardVariant.OVERDUE,
                    headlineOverride = if (overdueDays != null) "Payment overdue ($overdueDays days)" else "Payment overdue"
                )
            }
            EventType.PAYMENT_FAILED -> EventCardPresentation(
                variant = EventCardVariant.FAILED,
                headlineOverride = "Payment failed"
            )
            EventType.PAYMENT_PARTIAL -> EventCardPresentation(
                variant = EventCardVariant.PARTIAL,
                headlineOverride = "Partial payment"
            )
            EventType.TRIAL_STARTED -> EventCardPresentation(
                variant = EventCardVariant.ACTIVE,
                headlineOverride = "Trial active"
            )
            EventType.TRIAL_CONVERTED -> EventCardPresentation(
                variant = EventCardVariant.CONVERTED,
                headlineOverride = "Trial converted"
            )
            EventType.TRIAL_EXPIRED -> EventCardPresentation(
                variant = EventCardVariant.EXPIRED,
                headlineOverride = "Trial expired"
            )
            EventType.FREEZE_STARTED, EventType.FREEZE -> EventCardPresentation(
                variant = EventCardVariant.ACTIVE,
                headlineOverride = "Membership frozen"
            )
            EventType.FREEZE_ENDED, EventType.REACTIVATION -> EventCardPresentation(
                variant = EventCardVariant.RESOLVED,
                headlineOverride = "Membership reactivated"
            )
            EventType.BANNED -> EventCardPresentation(
                variant = EventCardVariant.BLOCKED,
                headlineOverride = "Member access banned"
            )
            EventType.BAN_LIFTED -> EventCardPresentation(
                variant = EventCardVariant.RESOLVED,
                headlineOverride = "Ban lifted"
            )
            EventType.EXPIRED, EventType.MEMBERSHIP_EXPIRED, EventType.SERVICE_EXPIRED -> EventCardPresentation(
                variant = EventCardVariant.EXPIRED,
                headlineOverride = if (event.eventType == EventType.SERVICE_EXPIRED) "Service expired" else "Membership expired"
            )
            EventType.MEMBERSHIP_CANCELLED -> EventCardPresentation(
                variant = EventCardVariant.BLOCKED,
                headlineOverride = "Membership cancelled"
            )
            EventType.TRAINER_SESSION_MISSED -> EventCardPresentation(
                variant = EventCardVariant.FAILED,
                headlineOverride = "PT session missed"
            )
            EventType.WORKOUT_SKIPPED -> EventCardPresentation(
                variant = EventCardVariant.FAILED,
                headlineOverride = "Workout skipped"
            )
            EventType.COMPLAINT_RESOLVED,
            EventType.INCIDENT_RESOLVED,
            EventType.MACHINE_FIXED,
            EventType.MAINTENANCE_COMPLETED,
            EventType.CLEANING_COMPLETED -> EventCardPresentation(
                variant = EventCardVariant.RESOLVED,
                headlineOverride = event.eventType.displayLabel()
            )
            else -> {
                if (meta["reopened"]?.equals("true", ignoreCase = true) == true) {
                    EventCardPresentation(
                        variant = EventCardVariant.REOPENED,
                        headlineOverride = "${event.eventType.displayLabel()} (Reopened)"
                    )
                } else {
                    EventCardPresentation(EventCardVariant.DEFAULT)
                }
            }
        }
    }
}
