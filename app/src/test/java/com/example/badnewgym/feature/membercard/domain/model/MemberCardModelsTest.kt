package com.example.badnewgym.feature.membercard.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MemberCardModelsTest {

    @Test
    fun sampleAmanTripathi_hasValidProfileAndBranding() {
        val sample = MemberCardData.sampleAmanTripathi()

        assertEquals("Good Morning 👋", sample.profile.greeting)
        assertEquals("Aman Tripathi", sample.profile.name)
        assertEquals("BG305", sample.profile.memberCode)
        assertEquals(23, sample.profile.age)
        assertEquals("Male", sample.profile.gender)
        assertTrue(sample.profile.isPrime)
        assertTrue(sample.profile.isActive)
        assertEquals(75, sample.profile.attendancePercent)
        assertEquals(25, sample.profile.daysLeft)
        assertEquals("01 Sep 2026", sample.profile.joinedDate)
        assertEquals("Muscle Gain", sample.profile.goal)
        assertEquals("Varanasi", sample.profile.branch)
        assertEquals(3, sample.profile.unreadNotifications)
    }

    @Test
    fun sampleAmanTripathi_hasFourBodyStatsWithSparklines() {
        val sample = MemberCardData.sampleAmanTripathi()
        assertEquals(4, sample.bodyStats.size)

        val weight = sample.bodyStats.first { it.type == BodyStatType.WEIGHT }
        assertEquals("69.5 kg", weight.value)
        assertEquals("↓ 2.5 kg", weight.delta)
        assertEquals("Weight (kg)", weight.label)
        assertTrue(weight.sparklinePoints.isNotEmpty())

        val muscle = sample.bodyStats.first { it.type == BodyStatType.MUSCLE }
        assertEquals("57.2 kg", muscle.value)
        assertEquals("↑ 1.4 kg", muscle.delta)
        assertEquals("Muscle (kg)", muscle.label)
        assertTrue(muscle.sparklinePoints.isNotEmpty())

        val bodyFat = sample.bodyStats.first { it.type == BodyStatType.BODY_FAT }
        assertEquals("17.6%", bodyFat.value)
        assertEquals("↓ 1.2%", bodyFat.delta)
        assertEquals("Body Fat (%)", bodyFat.label)
        assertTrue(bodyFat.sparklinePoints.isNotEmpty())

        val bmi = sample.bodyStats.first { it.type == BodyStatType.BMI }
        assertEquals("22.8", bmi.value)
        assertEquals("Normal", bmi.delta)
        assertEquals("BMI", bmi.label)
        assertTrue(bmi.sparklinePoints.isNotEmpty())
    }

    @Test
    fun sampleAmanTripathi_hasDetailedPaymentPlan() {
        val sample = MemberCardData.sampleAmanTripathi()
        assertEquals("Prime Plan (1 Month)", sample.paymentPlan.planDuration)
        assertEquals("01 Sep – 30 Sep 2026", sample.paymentPlan.planPeriod)
        assertEquals("₹2,800", sample.paymentPlan.amountPaid)
        assertEquals("PAID • ON TIME", sample.paymentPlan.paymentStatus)
        assertEquals("Paid on 01 Sep 2026", sample.paymentPlan.paidOnDate)
        assertEquals("30 Sep 2026", sample.paymentPlan.nextDueDate)
        assertEquals(25, sample.paymentPlan.daysLeft)
        assertTrue(sample.paymentPlan.isPrime)
    }

    @Test
    fun sampleAmanTripathi_hasAttendanceCalendarData() {
        val attendance = MemberCardData.sampleAmanTripathi().attendance

        assertEquals("October 2026", attendance.monthLabel)
        assertEquals(77, attendance.percent)
        assertEquals(24, attendance.attendedDays)
        assertEquals(31, attendance.totalDays)
        assertEquals(24, attendance.presentCount)
        assertEquals(3, attendance.absentCount)
        assertEquals(2, attendance.lateCount)
        assertEquals(1, attendance.holidayCount)
        assertEquals(31, attendance.calendarDays.size)
    }

    @Test
    fun sampleAmanTripathi_hasWorkoutTrendData() {
        val workout = MemberCardData.sampleAmanTripathi().workoutTrend

        assertEquals("October 2026", workout.monthLabel)
        assertEquals(31, workout.bars.size)
        assertEquals(48, workout.avgPerDayMinutes)
        assertEquals(52, workout.peakMinutes)
        assertEquals("10 Oct", workout.peakDate)
        assertEquals(12, workout.lowestMinutes)
        assertEquals("16 Oct", workout.lowestDate)
    }

    @Test
    fun sampleAshwiniSingh_matchesCanonicalMockup() {
        val sample = MemberCardData.sampleAshwiniSingh()

        assertEquals("Ashwini Kumar Sharma", sample.profile.name)
        assertEquals("BG4187", sample.profile.memberCode)
        assertEquals(25, sample.profile.age)
        assertEquals("Male", sample.profile.gender)
        assertEquals("Spark Member", sample.profile.tierName)
        assertTrue(sample.profile.isActive)
        assertEquals(18, sample.profile.daysLeft)
        assertEquals(30, sample.profile.totalDays)
        assertEquals("Muscle Gain", sample.profile.goal)
        assertEquals("Main Branch", sample.profile.branch)
        assertEquals("14 Apr 2026", sample.profile.joinedDate)
        assertEquals(12, sample.profile.streakDays)

        assertEquals(4, sample.bodyStats.size)
        assertEquals("69.5", sample.bodyStats[0].value)
        assertEquals("57.2", sample.bodyStats[1].value)
        assertEquals("17.6%", sample.bodyStats[2].value)
        assertEquals("22.8", sample.bodyStats[3].value)

        assertEquals("Oct 2026", sample.attendance.monthLabel)
        assertEquals(77, sample.attendance.percent)

        assertEquals("Oct 2026", sample.workoutTrend.monthLabel)
        assertEquals(48, sample.workoutTrend.avgPerDayMinutes)
        assertEquals(52, sample.workoutTrend.peakMinutes)
        assertEquals(12, sample.workoutTrend.lowestMinutes)

        assertEquals("₹800", sample.paymentPlan.lastPaymentAmount)
        assertEquals("31 Oct 2026", sample.paymentPlan.nextDueDate)
        assertEquals(9, sample.paymentPlan.daysLeft)
    }
}
