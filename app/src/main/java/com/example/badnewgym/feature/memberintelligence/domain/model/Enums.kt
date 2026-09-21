package com.example.badnewgym.feature.memberintelligence.domain.model

enum class EventSource {
    GATE, STAFF, OWNER, ADMIN, TRAINER, PAYMENT, MEMBER, SYSTEM, IMPORT, API
}

enum class MembershipLifecycle {
    ACTIVE, EXPIRING, EXPIRED, FROZEN, REACTIVATED, CANCELLED, PENDING
}

enum class PaymentLifecycle {
    PAID, DUE, OVERDUE, FAILED, PARTIAL, REFUNDED
}

enum class FeatureKey {
    MEMBER_INTELLIGENCE,
    ADVANCED_INSIGHTS,
    PAYMENT_AUTOMATION,
    TRAINER_MANAGEMENT,
    NUTRITION,
    SUPPLEMENTS,
    MARKETING,
    ANALYTICS
}

enum class SyncStatus {
    IDLE, QUEUED, SYNCING, SYNCED, FAILED, OFFLINE
}

enum class GymRole {
    OWNER, ADMIN, MANAGER, TRAINER, STAFF, READ_ONLY
}
