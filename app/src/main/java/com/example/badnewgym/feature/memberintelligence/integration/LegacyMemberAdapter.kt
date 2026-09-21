package com.example.badnewgym.feature.memberintelligence.integration

import com.example.badnewgym.feature.memberintelligence.domain.model.*

/**
 * Hypothetical Data Transfer Object representing the old monolithic user structure.
 * When integrating into the legacy codebase, this represents the incoming data.
 */
data class LegacyUserDTO(
    val userId: String,
    val locationId: String,
    val firstName: String,
    val lastName: String,
    val avatar: String?,
    val isVip: Boolean,
    val activeSubscription: String?,
    val activeSubEndDate: Long?,
    val pastDueAmount: Double,
    val checkInsThisMonth: Int,
    val totalCheckIns: Int
)

/**
 * An adapter used for the transition phase. It allows the legacy app
 * to supply a `LegacyUserDTO` and easily convert it into the new `MemberSnapshot` 
 * that powers the Jetpack Compose Member Intelligence V2 Engine.
 */
object LegacyMemberAdapter {
    fun toMemberSnapshot(legacyUser: LegacyUserDTO): MemberSnapshot {
        // Compute Membership Status
        val membershipStatus = legacyUser.activeSubscription?.let { plan ->
            val expiry = legacyUser.activeSubEndDate ?: 0L
            val daysRemaining = if (expiry > 0) {
                ((expiry - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
            } else 0
            
            MembershipStatus(
                planName = plan,
                planType = if (plan.contains("Annual", true)) "YEARLY" else "MONTHLY",
                startDate = 0L, // Assuming legacy didn't store start date locally
                expiryDate = expiry,
                daysRemaining = daysRemaining,
                isActive = daysRemaining > 0,
                previousPlanName = null
            )
        }

        return MemberSnapshot(
            id = legacyUser.userId,
            gymId = legacyUser.locationId,
            identity = MemberIdentity(
                name = "${legacyUser.firstName} ${legacyUser.lastName}",
                photoUrl = legacyUser.avatar,
                tier = if (legacyUser.isVip) MembershipTier.VIP else MembershipTier.NORMAL,
                memberSince = 0L 
            ),
            membership = membershipStatus,
            attendance = AttendanceSummary(
                periodName = "Current Month",
                visits = legacyUser.checkInsThisMonth,
                target = null,
                lifetimeVisits = legacyUser.totalCheckIns
            ),
            payment = if (legacyUser.pastDueAmount > 0) {
                PaymentSummary(
                    totalOutstanding = legacyUser.pastDueAmount,
                    overdueDays = 1, // Need logic if available
                    dueDate = null,
                    lastPaymentAmount = null,
                    lastPaymentDate = null
                )
            } else null,
            // The following fields were not present in the monolithic DTO 
            // and must be handled carefully. We will initialize them to null 
            // or empty and let the new engine fetch them if required.
            trainer = null,
            workout = null,
            supplements = null,
            nutrition = null,
            services = emptyList(),
            recentEvents = emptyList(),
            issues = emptyList()
        )
    }
}
