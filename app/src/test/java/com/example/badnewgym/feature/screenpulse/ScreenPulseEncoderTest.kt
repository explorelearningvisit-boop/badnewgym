package com.example.badnewgym.feature.screenpulse

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Encoder decision-logic contracts.
 *
 * [ScreenPulseEncoder.qualityFor] is pure (size as a function of quality),
 * so the binary search is pinned here without needing a Bitmap or a device.
 * The Android-only [ScreenPulseEncoder.encode] path is verified on-device
 * through the SNAPSHOT self-test instead.
 */
class ScreenPulseEncoderTest {

    @Test
    fun picksHighestQualityThatFits() {
        // Size grows with quality: only qualities <= 60 fit a 1000-byte budget.
        val q = ScreenPulseEncoder.qualityFor({ quality -> quality * 20 }, 1_200)
        assertEquals(60, q)
    }

    @Test
    fun returnsMaxWhenEverythingFits() {
        val q = ScreenPulseEncoder.qualityFor({ _ -> 10 }, 1_000)
        assertEquals(92, q)
    }

    @Test
    fun returnsMinWhenNothingFits() {
        val q = ScreenPulseEncoder.qualityFor({ _ -> Int.MAX_VALUE / 2 }, 1_000)
        assertEquals(15, q)
    }

    @Test
    fun handlesFlatSizeFunction() {
        val q = ScreenPulseEncoder.qualityFor({ _ -> 500 }, 1_000)
        assertEquals(92, q)
    }
}
