package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

object MembershipStatusCalculator {
    fun resolve(membership: MembershipStatus?): MembershipLifecycle {
        if (membership == null) return MembershipLifecycle.PENDING
        membership.lifecycle?.let { return it }
        return when {
            !membership.isActive && membership.daysRemaining < 0 -> MembershipLifecycle.EXPIRED
            !membership.isActive -> MembershipLifecycle.EXPIRED
            membership.daysRemaining in 0..7 -> MembershipLifecycle.EXPIRING
            else -> MembershipLifecycle.ACTIVE
        }
    }

    fun statusLabel(membership: MembershipStatus?): String {
        return when (resolve(membership)) {
            MembershipLifecycle.ACTIVE -> "ACTIVE · ${membership?.daysRemaining ?: 0} DAYS LEFT"
            MembershipLifecycle.EXPIRING -> "EXPIRING SOON · ${membership?.daysRemaining ?: 0} DAYS LEFT"
            MembershipLifecycle.EXPIRED -> "EXPIRED"
            MembershipLifecycle.FROZEN -> "FROZEN"
            MembershipLifecycle.REACTIVATED -> "REACTIVATED · ${membership?.daysRemaining ?: 0} DAYS LEFT"
            MembershipLifecycle.CANCELLED -> "CANCELLED"
            MembershipLifecycle.PENDING -> "PENDING"
        }
    }
}
