package com.example.badnewgym

import com.example.badnewgym.feature.bodyprogress.domain.model.WorkoutSetTiming
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkoutSetTimerTest {

    @Test
    fun recordsStartStopAndSaveSeparately() {
        val timing = WorkoutSetTiming()
            .start(epochMillis = 1_000L, monotonicMillis = 5_000L)
            .stop(epochMillis = 46_000L, monotonicMillis = 50_000L)
            .save(epochMillis = 51_500L)

        assertEquals(45_000L, timing.recordedDurationMillis)
        assertEquals(1_500L, timing.saveInteractionDelayMillis)
        assertTrue(timing.isSaved)
    }

    @Test
    fun doesNotSubtractSaveInteractionDelayFromSetDuration() {
        val timing = WorkoutSetTiming()
            .start(epochMillis = 10_000L, monotonicMillis = 100L)
            .stop(epochMillis = 55_000L, monotonicMillis = 45_100L)
            .save(epochMillis = 60_000L)

        assertEquals(45_000L, timing.recordedDurationMillis)
        assertEquals(5_000L, timing.saveInteractionDelayMillis)
    }

    @Test
    fun rejectsStopBeforeStart() {
        assertThrows(IllegalArgumentException::class.java) {
            WorkoutSetTiming().stop(epochMillis = 1L, monotonicMillis = 1L)
        }
    }

    @Test
    fun rejectsNegativeMonotonicDuration() {
        val started = WorkoutSetTiming().start(1_000L, 10_000L)
        assertThrows(IllegalArgumentException::class.java) {
            started.stop(epochMillis = 2_000L, monotonicMillis = 9_000L)
        }
    }

    @Test
    fun supportsUnfinishedSetRecoveryState() {
        val timing = WorkoutSetTiming().start(1_000L, 5_000L)
        assertTrue(timing.isStarted)
        assertFalse(timing.isStopped)
        assertEquals(null, timing.recordedDurationMillis)
    }
}
