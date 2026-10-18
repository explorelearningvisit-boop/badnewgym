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
        assertEquals(28, attendance.calendarDays.size)
    }

    @Test
    fun sampleAmanTripathi_hasWorkoutTrendData() {
        val workout = MemberCardData.sampleAmanTripathi().workoutTrend

        assertEquals("October 2026", workout.monthLabel)
        assertEquals(30, workout.bars.size)
        assertEquals(48, workout.avgPerDayMinutes)
        assertEquals(52, workout.peakMinutes)
        assertEquals("10 Oct", workout.peakDate)
        assertEquals(12, workout.lowestMinutes)
        assertEquals("16 Oct", workout.lowestDate)
    }
}
