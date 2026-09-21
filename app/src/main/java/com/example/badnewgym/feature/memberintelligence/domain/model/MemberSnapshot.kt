package com.example.badnewgym.feature.memberintelligence.domain.model

import java.util.UUID

data class MemberSnapshot(
    val id: String = UUID.randomUUID().toString(),
    val gymId: String,
    val identity: MemberIdentity,
    val membership: MembershipStatus?,
    val attendance: AttendanceSummary?,
    val payment: PaymentSummary?,
    val trainer: TrainerSummary?,
    val workout: WorkoutSummary?,
    val supplements: SupplementSummary?,
    val nutrition: NutritionSummary?,
    val services: List<ServiceSummary>?,
    val recentEvents: List<MemberEvent> = emptyList(),
    val issues: List<MemberIssue> = emptyList(),
    val promotion: PromotionSlot? = null,
    val membershipHistory: List<MembershipHistory> = emptyList()
)

data class MembershipHistory(
    val planName: String,
    val startDate: Long,
    val endDate: Long
)

data class MemberIdentity(
    val name: String,
    val photoUrl: String?,
    val tier: MembershipTier,
    val memberSince: Long,
    val code: String? = null,
    val isVerified: Boolean = false
)

enum class MembershipTier {
    NORMAL, PREMIUM, VIP, CORPORATE, TRIAL, WALK_IN
}

data class MembershipStatus(
    val planName: String,
    val planType: String,
    val startDate: Long,
    val expiryDate: Long,
    val daysRemaining: Int,
    val isActive: Boolean,
    val previousPlanName: String? = null,
    val previousCost: Double? = null,
    val currentCost: Double? = null,
    val freezeAllowanceDays: Int? = null,
    val freezeUsedDays: Int? = null,
    val renewalCount: Int = 0,
    val lifecycle: MembershipLifecycle? = null
)

data class AttendanceSummary(
    val periodName: String,
    val visits: Int,
    val target: Int?,
    val lifetimeVisits: Int,
    val streakDays: Int? = null,
    val avgVisitsPerWeek: Double? = null,
    val weeklyPattern: List<Int>? = null,
    val lastVisitAt: Long? = null,
    val isHistoricalPeriod: Boolean = false,
    val preferredSlot: String? = null
)

data class PaymentSummary(
    val totalOutstanding: Double,
    val overdueDays: Int,
    val dueDate: Long?,
    val lastPaymentAmount: Double?,
    val lastPaymentDate: Long?,
    val lastPaymentMethod: String? = null,
    val lifetimePaid: Double? = null,
    val lifecycle: PaymentLifecycle? = null,
    val breakdown: List<PaymentBreakdownLine> = emptyList(),
    val history: List<PaymentTransaction> = emptyList()
)

data class TrainerSummary(
    val trainerName: String,
    val trainerPhotoUrl: String?,
    val sessionsTotal: Int,
    val sessionsUsed: Int,
    val nextSessionDate: Long?,
    val focus: String?,
    val lastSessionDate: Long? = null,
    val rating: Double? = null
)

data class WorkoutSummary(
    val lastWorkoutDate: Long?,
    val currentRoutine: String?,
    val durationMinutes: Int? = null,
    val calories: Int? = null
)

data class SupplementSummary(
    val hasHistory: Boolean,
    val lastPurchaseName: String?,
    val lastPurchaseDate: Long?,
    val lastPurchasePrice: Double? = null,
    val brand: String? = null,
    val imageUrl: String? = null
)

data class NutritionSummary(
    val isSubscribed: Boolean,
    val planName: String?,
    val renewalDate: Long?,
    val monthlyPrice: Double? = null
)

data class ServiceSummary(
    val serviceName: String,
    val isActive: Boolean,
    val expiryDate: Long?,
    val price: Double? = null
)

data class MemberIssue(
    val id: String,
    val description: String,
    val severity: IssueSeverity
)

enum class IssueSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}
