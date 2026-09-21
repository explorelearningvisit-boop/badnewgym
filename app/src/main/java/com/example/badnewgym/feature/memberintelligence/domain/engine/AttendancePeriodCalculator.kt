package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

object AttendancePeriodCalculator {
    fun consistencyPercent(visits: Int, target: Int?): Int {
        if (target == null || target <= 0) return 0
        return (visits * 100) / target
    }

    fun shouldShowCurrentPeriod(membership: MembershipStatus?): Boolean {
        val lifecycle = MembershipStatusCalculator.resolve(membership)
        return lifecycle == MembershipLifecycle.ACTIVE ||
            lifecycle == MembershipLifecycle.EXPIRING ||
            lifecycle == MembershipLifecycle.REACTIVATED ||
            lifecycle == MembershipLifecycle.FROZEN
    }

    fun labeledSummary(attendance: AttendanceSummary?, membership: MembershipStatus?): AttendanceSummary? {
        if (attendance == null) return null
        val showCurrent = shouldShowCurrentPeriod(membership)
        return if (showCurrent) {
            attendance.copy(isHistoricalPeriod = false)
        } else {
            attendance.copy(
                periodName = "Historical · ${attendance.periodName}",
                isHistoricalPeriod = true
            )
        }
    }
}
