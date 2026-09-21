package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AttendancePeriodCalculatorTest {
    @Test
    fun expiredMemberDoesNotUseCurrentPeriod() {
        val membership = MembershipStatus(
            planName = "Gold",
            planType = "12 Months",
            startDate = 0,
            expiryDate = 0,
            daysRemaining = -12,
            isActive = false
        )
        assertFalse(AttendancePeriodCalculator.shouldShowCurrentPeriod(membership))
    }

    @Test
    fun activeMemberUsesCurrentPeriod() {
        val membership = MembershipStatus(
            planName = "Gold",
            planType = "12 Months",
            startDate = 0,
            expiryDate = 1,
            daysRemaining = 48,
            isActive = true
        )
        assertTrue(AttendancePeriodCalculator.shouldShowCurrentPeriod(membership))
    }

    @Test
    fun consistencyMatchesTruncatedPercent() {
        assertEquals(61, AttendancePeriodCalculator.consistencyPercent(16, 26))
    }
}
