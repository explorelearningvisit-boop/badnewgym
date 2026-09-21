package com.example.badnewgym.feature.memberintelligence.domain.model

data class MemberEvent(
    val id: String,
    val memberId: String,
    val gymId: String,
    val eventType: EventType,
    val occurredAt: Long,
    val source: EventSource = EventSource.SYSTEM,
    val actorId: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val createdAt: Long = occurredAt,
    val idempotencyKey: String? = null
)

enum class EventType {
    CHECK_IN,
    CHECK_OUT,
    PAYMENT,
    PAYMENT_FAILED,
    NEW_MEMBER,
    WALK_IN,
    RENEWAL,
    EXPIRED,
    FREEZE,
    REACTIVATION,
    TRAINER_SESSION,
    WORKOUT,
    SUPPLEMENT_PURCHASE,
    NUTRITION,
    SERVICE_PURCHASE,
    COMPLAINT,
    MAINTENANCE,
    UNKNOWN;

    fun displayLabel(): String = when (this) {
        CHECK_IN -> "CHECK-IN"
        CHECK_OUT -> "CHECK-OUT"
        PAYMENT -> "PAYMENT"
        PAYMENT_FAILED -> "PAYMENT FAILED"
        NEW_MEMBER -> "NEW MEMBER"
        WALK_IN -> "WALK-IN"
        RENEWAL -> "RENEWAL"
        EXPIRED -> "EXPIRED"
        FREEZE -> "FREEZE"
        REACTIVATION -> "REACTIVATION"
        TRAINER_SESSION -> "PT SESSION"
        WORKOUT -> "WORKOUT"
        SUPPLEMENT_PURCHASE -> "SUPPLEMENT"
        NUTRITION -> "NUTRITION"
        SERVICE_PURCHASE -> "SERVICE"
        COMPLAINT -> "COMPLAINT"
        MAINTENANCE -> "MAINTENANCE"
        UNKNOWN -> "EVENT"
    }
}
