package com.example.badnewgym.feature.bodyprogress.domain.intelligence

import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BodyIntelligenceTest {

    private fun reading(
        metric: BodyMetric,
        value: Double,
        privacy: PrivacyCategory,
        delta: String? = "↓ 1.0 kg",
        trend: List<Float> = listOf(0.8f, 0.6f, 0.4f)
    ) = CompositionReading(
        metric = metric,
        value = value,
        unit = metric.unit,
        deltaLabel = delta,
        trend = trend,
        goalLabel = "Goal: 65 kg",
        privacy = privacy
    )

    private fun fullInput() = BodyIntelligenceInput(
        memberId = "BG4187",
        memberName = "Ashwini",
        goal = TrainingGoal.MUSCLE_GAIN,
        readings = listOf(
            reading(BodyMetric.WEIGHT, 69.5, PrivacyCategory.WEIGHT),
            reading(BodyMetric.MUSCLE_MASS, 57.2, PrivacyCategory.MUSCLE_MASS)
        ),
        activityDays = listOf(
            ActivityDay(2, 40), ActivityDay(3, 60), ActivityDay(4, 0), ActivityDay(5, 80)
        ),
        checkInDays = listOf(
            CheckInRecord(2, present = true),
            CheckInRecord(3, present = true),
            CheckInRecord(4, present = true),
            CheckInRecord(5, present = false),
            CheckInRecord(6, present = false, holiday = true)
        ),
        monthLabel = "Oct 2026",
        anchorDay = 31
    )

    // --------------------------------------------------------------- ordering

    @Test
    fun `every goal keeps all eight widgets exactly once`() {
        val view = BodyIntelligenceAnalytics.viewOf(fullInput())
        TrainingGoal.entries.forEach { goal ->
            val order = BodyWidgetPriority.order(view.copy(input = view.input.copy(goal = goal)))
            assertEquals(BodyWidget.entries.size, order.size)
            assertEquals(BodyWidget.entries.toSet(), order.toSet())
        }
    }

    @Test
    fun `goal decides the leading widget when data is present`() {
        val view = BodyIntelligenceAnalytics.viewOf(fullInput().copy(hasSetLevelRecords = true))
        val muscleGain = BodyWidgetPriority.order(view)
        assertEquals(BodyWidget.HUMAN_BODY, muscleGain.first())

        val endurance = BodyWidgetPriority.order(
            view.copy(input = view.input.copy(goal = TrainingGoal.ENDURANCE))
        )
        assertEquals(BodyWidget.ACTIVITY, endurance.first())
    }

    @Test
    fun `order is stable across repeated calls`() {
        val view = BodyIntelligenceAnalytics.viewOf(fullInput())
        val first = BodyWidgetPriority.order(view)
        repeat(5) { assertEquals(first, BodyWidgetPriority.order(view)) }
    }

    @Test
    fun `widgets without records sink but never disappear`() {
        val view = BodyIntelligenceAnalytics.viewOf(fullInput())
        val order = BodyWidgetPriority.order(view)
        assertTrue(order.indexOf(BodyWidget.STRENGTH) > order.indexOf(BodyWidget.COMPOSITION))
        assertTrue(order.contains(BodyWidget.ASSESSMENT))
        assertTrue(order.contains(BodyWidget.MUSCLE_FOCUS))
    }

    // ---------------------------------------------------------------- privacy

    @Test
    fun `hidden category is removed from readings before rendering`() {
        val input = fullInput()
        val privacy = BodyPrivacySettings.Visible.toggle(PrivacyCategory.MUSCLE_MASS)
        val view = BodyIntelligenceAnalytics.viewOf(input, privacy)

        assertNull(view.reading(BodyMetric.MUSCLE_MASS))
        assertEquals(1, view.readings.size)
        assertEquals(listOf(PrivacyCategory.MUSCLE_MASS), view.hiddenCategories)
        // The underlying record still exists - hiding is not deletion.
        assertEquals(2, input.readings.size)
    }

    @Test
    fun `hidden values never reach the insight text`() {
        val input = fullInput()
        val privacy = BodyPrivacySettings.Visible.toggle(PrivacyCategory.MUSCLE_MASS)
        val view = BodyIntelligenceAnalytics.viewOf(input, privacy)

        val text = view.insights.joinToString(" ") { "${it.title} ${it.detail} ${it.evidence}" }
        assertFalse(text.contains("57.2"))
        assertTrue(text.contains("69.5"))
    }

    @Test
    fun `private by default matches the specification`() {
        val defaults = PrivacyCategory.privateDefaults
        assertTrue(PrivacyCategory.BODY_FAT in defaults)
        assertTrue(PrivacyCategory.MUSCLE_MASS in defaults)
        assertTrue(PrivacyCategory.CHEST in defaults)
        assertTrue(PrivacyCategory.WAIST in defaults)
        assertTrue(PrivacyCategory.HIP in defaults)
        assertFalse(PrivacyCategory.WEIGHT in defaults)
        assertFalse(PrivacyCategory.BMI in defaults)
        assertTrue(BodyPrivacySettings.PrivateByDefault.hidden.containsAll(defaults))
    }

    // ------------------------------------------------------------- analytics

    @Test
    fun `activity ignores zero-minute days`() {
        val summary = BodyIntelligenceAnalytics.activitySummary(fullInput())
        assertEquals(3, summary.sessionDays)
        assertEquals(180, summary.totalMinutes)
        assertEquals(60, summary.averageMinutes)
        assertEquals(80, summary.longestMinutes)
        assertEquals(5, summary.longestDay)
    }

    @Test
    fun `check-in without a workout is not counted as training`() {
        val summary = BodyIntelligenceAnalytics.consistencySummary(fullInput())
        assertEquals(3, summary.trainedDays)
        assertEquals(3, summary.checkInDays)
        assertEquals(1, summary.checkInWithoutWorkout)
        assertEquals(DayMarker.CHECKED_IN_ONLY, summary.markers.first { it.first == 4 }.second)
        assertEquals(DayMarker.TRAINED, summary.markers.first { it.first == 5 }.second)
    }

    @Test
    fun `empty member produces empty summaries and no invented numbers`() {
        val view = BodyIntelligenceAnalytics.viewOf(
            BodyIntelligenceInput(memberId = "BG0", memberName = "New Member")
        )
        assertFalse(view.hasAnyRecord)
        assertTrue(view.readings.isEmpty())
        assertFalse(view.activity.hasData)
        assertNull(view.activity.longestDay)
        assertFalse(view.consistency.hasData)
        assertEquals(0, view.consistency.trainedDays)
        // Only the "not enough records" style insights are allowed - never a value.
        assertTrue(view.insights.all { it.tone == InsightTone.DATA })
    }

    @Test
    fun `strength stays not-started until set records exist`() {
        val view = BodyIntelligenceAnalytics.viewOf(fullInput())
        assertFalse(view.input.hasSetLevelRecords)
        assertTrue(view.insights.any { it.metricKey == "strength" && it.tone == InsightTone.DATA })
    }

    @Test
    fun `trend change is null for a single recorded point`() {
        assertNull(BodyIntelligenceAnalytics.trendChangePercent(emptyList()))
        assertNull(BodyIntelligenceAnalytics.trendChangePercent(listOf(0.4f)))
        assertEquals(50.0, BodyIntelligenceAnalytics.trendChangePercent(listOf(0.2f, 0.3f))!!, 0.0001)
    }

    @Test
    fun `anatomy map exposes deduplicated muscle groups`() {
        val front = AnatomyRegions.groups(AnatomyView.FRONT)
        val back = AnatomyRegions.groups(AnatomyView.BACK)
        assertEquals(front.distinct(), front)
        assertEquals(back.distinct(), back)
        assertTrue(front.contains(com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup.CHEST))
        assertTrue(back.contains(com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup.LATS))
        // Shoulders are drawn on both sides but are one selectable group.
        assertEquals(2, AnatomyRegions.regionsFor(AnatomyView.FRONT, com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup.SHOULDERS).size)
    }
}
