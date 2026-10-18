package com.example.badnewgym.feature.membercard.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MemberCardModelsTest {

    @Test
    fun sampleAmanTripathi_hasValidProfileAndAttendance() {
        val sample = MemberCardData.sampleAmanTripathi()

        assertEquals("Aman Tripathi", sample.profile.name)
        assertEquals("BG305", sample.profile.memberCode)
        assertEquals(23, sample.profile.age)
        assertEquals("Male", sample.profile.gender)
        assertTrue(sample.profile.isPrime)
        assertTrue(sample.profile.isActive)
        assertEquals(75, sample.profile.attendancePercent)
        assertEquals(25, sample.profile.attendedDays)
        assertEquals(31, sample.profile.totalDays)
    }

    @Test
    fun sampleAmanTripathi_hasFourBodyStats() {
        val sample = MemberCardData.sampleAmanTripathi()
        assertEquals(4, sample.bodyStats.size)

        val weight = sample.bodyStats.first { it.type == BodyStatType.WEIGHT }
        assertEquals("69.5", weight.value)
        assertEquals("↓ kg 2.5", weight.subText)

        val muscle = sample.bodyStats.first { it.type == BodyStatType.MUSCLE }
        assertEquals("57.2", muscle.value)
        assertEquals("kg ↑ 1.4", muscle.subText)

        val bodyFat = sample.bodyStats.first { it.type == BodyStatType.BODY_FAT }
        assertEquals("17.6%", bodyFat.value)
        assertEquals("↓ 1.2", bodyFat.subText)

        val bmi = sample.bodyStats.first { it.type == BodyStatType.BMI }
        assertEquals("22.8", bmi.value)
        assertEquals("Normal", bmi.subText)
    }

    @Test
    fun sampleAmanTripathi_hasValidPaymentPlan() {
        val sample = MemberCardData.sampleAmanTripathi()
        assertEquals("₹2,800", sample.paymentPlan.amountPaid)
        assertEquals("Paid • On Time", sample.paymentPlan.paymentStatus)
        assertEquals("30 Sep 2026", sample.paymentPlan.nextDueDate)
        assertEquals(25, sample.paymentPlan.daysLeft)
        assertTrue(sample.paymentPlan.isPrime)
    }

    @Test
    fun sampleAmanTripathi_hasThirtyOneDayAttendanceWorkoutSummary() {
        val summary = MemberCardData.sampleAmanTripathi().attendanceWorkout

        assertEquals("Oct 2026", summary.monthLabel)
        assertEquals(31, summary.dailyEntries.size)
        assertEquals(48, summary.avgPerDayMinutes)
        assertEquals(65, summary.peakMinutes)
        assertEquals("10 Oct", summary.peakDate)
        assertEquals(12, summary.lowestMinutes)
        assertEquals("16 Oct", summary.lowestDate)

        assertEquals(24, summary.presentCount)
        assertEquals(77, summary.presentPercent)
        assertEquals(3, summary.absentCount)
        assertEquals(10, summary.absentPercent)
        assertEquals(2, summary.offCount)
        assertEquals(6, summary.offPercent)
        assertEquals(2, summary.lateCount)
        assertEquals(6, summary.latePercent)
    }
}
