package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test verifying BAD GYM Member Intelligence compact card geometry tokens
 * and readability invariants (MI-V5-CARD-READABILITY-GEOMETRY-FIX).
 */
class CompactCardDimensionsTest {

    @Test
    fun defaultTokens_matchTargetGeometryDirection() {
        val defaultTokens = CompactCardTokens.Default

        // Browse card Stage 7.1 +20dp adjustment
        assertEquals(312.dp, defaultTokens.cardWidth)
        assertEquals(426.dp, defaultTokens.cardHeight)

        // Bounded detail card
        assertEquals(340.dp, defaultTokens.detailCardWidth)
        assertEquals(463.dp, defaultTokens.detailCardHeight)

        // Portrait dimensions
        assertEquals(100.dp, defaultTokens.portraitWidth)
        assertEquals(106.dp, defaultTokens.portraitHeight)
        assertEquals(108.dp, defaultTokens.detailPortraitWidth)
        assertEquals(114.dp, defaultTokens.detailPortraitHeight)

        // Metrics & CTA
        assertEquals(72.dp, defaultTokens.metricsHeight)
        assertEquals(44.dp, defaultTokens.ctaHeight)
    }

    @Test
    fun expandedTokens_matchTargetGeometryDirection() {
        val expandedTokens = CompactCardTokens.Expanded

        assertEquals(328.dp, expandedTokens.cardWidth)
        assertEquals(442.dp, expandedTokens.cardHeight)
        assertEquals(360.dp, expandedTokens.detailCardWidth)
        assertEquals(480.dp, expandedTokens.detailCardHeight)
        assertEquals(104.dp, expandedTokens.portraitWidth)
        assertEquals(110.dp, expandedTokens.portraitHeight)
        assertEquals(112.dp, expandedTokens.detailPortraitWidth)
        assertEquals(118.dp, expandedTokens.detailPortraitHeight)
    }

    @Test
    fun compactTokens_matchTargetGeometryDirection() {
        val compactTokens = CompactCardTokens.Compact

        assertEquals(296.dp, compactTokens.cardWidth)
        assertEquals(410.dp, compactTokens.cardHeight)
        assertEquals(320.dp, compactTokens.detailCardWidth)
        assertEquals(440.dp, compactTokens.detailCardHeight)
        assertEquals(96.dp, compactTokens.portraitWidth)
        assertEquals(102.dp, compactTokens.portraitHeight)
        assertEquals(104.dp, compactTokens.detailPortraitWidth)
        assertEquals(110.dp, compactTokens.detailPortraitHeight)
    }

    @Test
    fun boundedDetailState_preservesProportionalHierarchyAndSidePeek() {
        listOf(
            CompactCardTokens.Compact,
            CompactCardTokens.Default,
            CompactCardTokens.Expanded
        ).forEach { tokens ->
            // Detail card width must be larger than browse card width
            assertTrue(
                "Detail width must be strictly larger than browse width",
                tokens.detailCardWidth > tokens.cardWidth
            )
            // Detail card height must be larger than browse card height
            assertTrue(
                "Detail height must be strictly larger than browse height",
                tokens.detailCardHeight > tokens.cardHeight
            )
            // Detail portrait must be >= 50dp
            assertTrue(
                "Detail portrait width must be >= 50dp",
                tokens.detailPortraitWidth >= 50.dp
            )
            // Detail portrait height must be >= 56dp
            assertTrue(
                "Detail portrait height must be >= 56dp",
                tokens.detailPortraitHeight >= 56.dp
            )
            // Rail width must be >= 48dp for accessibility touch targets
            assertTrue(
                "Rail width must be >= 48dp",
                tokens.railWidth >= 48.dp
            )
        }
    }
}
