package com.example.badnewgym.feature.bodyprogress.domain

import com.example.badnewgym.feature.bodyprogress.data.repository.BodyProgressFixtureSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Home muscle-focus grouping contracts: five stable display groups whose
 * shares always describe the same whole (sum to 100%).
 */
class BodyProgressGroupsTest {

    private val dataset = BodyProgressFixtureSource().dataset(LocalDate.of(2026, 10, 11))

    @Test
    fun groupsCoverChestBackArmsLegsOthersInOrder() {
        val summary = BodyProgressAggregator.summarize(
            dataset,
            com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange.D30
        )
        val groups = BodyProgressAggregator.focusGroups(summary.muscleFocus)
        assertEquals(listOf("Chest", "Back", "Arms", "Legs", "Others"), groups.map { it.label })
    }

    @Test
    fun groupSharesSumTo100() {
        val summary = BodyProgressAggregator.summarize(
            dataset,
            com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange.D30
        )
        val groups = BodyProgressAggregator.focusGroups(summary.muscleFocus)
        assertTrue(groups.isNotEmpty())
        assertEquals(100, groups.sumOf { it.sharePercent })
    }

    @Test
    fun emptyFocusYieldsNoGroups() {
        val summary = BodyProgressAggregator.summarize(
            BodyProgressFixtureSource().sparseDataset(LocalDate.of(2026, 10, 11)),
            com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange.D30
        )
        assertTrue(BodyProgressAggregator.focusGroups(summary.muscleFocus).isEmpty())
    }

    @Test
    fun everyCataloguedMuscleBelongsToExactlyOneGroup() {
        val summary = BodyProgressAggregator.summarize(
            dataset,
            com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange.ALL
        )
        val groupedSets = BodyProgressAggregator.focusGroups(summary.muscleFocus).sumOf { it.sets }
        // Grouping only re-buckets the per-muscle set counts (one set can
        // touch several muscles); the bucketed total is preserved exactly.
        assertEquals(summary.muscleFocus.shares.sumOf { it.sets }, groupedSets)
    }
}
