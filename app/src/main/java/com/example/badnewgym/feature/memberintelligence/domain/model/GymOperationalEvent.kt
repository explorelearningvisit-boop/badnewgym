package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Gym-wide operational event. It is intentionally separate from MemberEvent:
 * a member screen can surface gym context without pretending that every event
 * belongs to the member.
 */
data class GymOperationalEvent(
    val id: String,
    val gymId: String,
    val category: GymOperationalCategory,
    val status: GymOperationalStatus,
    val severity: SignalPriority,
    val title: String,
    val detail: String,
    val location: String? = null,
    val actor: String? = null,
    val occurredAt: Long,
    val relatedMemberId: String? = null,
    val relatedAssetId: String? = null,
    val amount: Double? = null,
    val metadata: Map<String, String> = emptyMap()
)

enum class GymOperationalCategory {
    ACCESS,
    PAYMENT,
    MEMBERSHIP,
    TRAINER,
    EQUIPMENT,
    MAINTENANCE,
    CLEANING,
    POWER,
    FACILITY,
    FINANCE,
    SECURITY,
    SYSTEM
}

enum class GymOperationalStatus {
    LIVE,
    SCHEDULED,
    IN_PROGRESS,
    RESOLVED,
    SUCCESS,
    FAILED,
    ACKNOWLEDGED
}

data class GymLiveSnapshot(
    val activeMembers: Int = 0,
    val checkInsToday: Int = 0,
    val checkOutsToday: Int = 0,
    val openMaintenance: Int = 0,
    val activeTrainers: Int = 0,
    val powerStatus: String = "ONLINE",
    val todayRevenue: Double = 0.0,
    val todayPayments: Int = 0,
    val unresolvedAlerts: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
