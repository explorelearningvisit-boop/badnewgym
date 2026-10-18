package com.example.badnewgym.feature.memberintelligence.preview.scenarios

import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyMeasurement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.Calendar

class AmanReferenceScenarioTest {
    @Test
    fun `Aman demo analytics derive the displayed reference summaries`() {
        val (snapshot, _) = MemberScenarios.compactSummaryAman()
        val now = System.currentTimeMillis()
        val monthKey = DashboardMath.monthKey(now)
        val current = snapshot.workoutHistory.filter { DashboardMath.monthKey(it.date) == monthKey }

        assertEquals(48, DashboardMath.recordedAverageMinutes(current))
        assertEquals(52, DashboardMath.peakRecordedDay(current)?.durationMinutes)
        assertEquals(12, DashboardMath.lowestRecordedDay(current)?.durationMinutes)
        assertEquals(
            12.0,
            DashboardMath.monthOverMonthPercent(
                DashboardMath.averageForMonth(snapshot.workoutHistory, monthKey),
                DashboardMath.averageForMonth(snapshot.workoutHistory, DashboardMath.previousMonthKey(monthKey))
            )!!,
            0.01
        )

        val dayOfMonth = Calendar.getInstance()
        dayOfMonth.timeInMillis = DashboardMath.peakRecordedDay(current)!!.date
        assertEquals(10, dayOfMonth.get(Calendar.DAY_OF_MONTH))
        dayOfMonth.timeInMillis = DashboardMath.lowestRecordedDay(current)!!.date
        assertEquals(16, dayOfMonth.get(Calendar.DAY_OF_MONTH))

        val latest: BodyMeasurement = snapshot.bodyProgress.maxBy { it.date }
        assertEquals(69.5, latest.weightKg!!, 0.001)
        assertEquals(57.2, latest.muscleKg!!, 0.001)
        assertEquals(17.6, latest.bodyFatPercent!!, 0.001)
        assertEquals(22.8, latest.bmi!!, 0.001)
        assertNotNull(snapshot.identity.ageYears)
        assertEquals("Male", snapshot.identity.gender)
    }
}
