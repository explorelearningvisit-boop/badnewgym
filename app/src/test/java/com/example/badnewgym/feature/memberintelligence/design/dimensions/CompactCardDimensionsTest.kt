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

        // Browse card ~10% increase baseline
        assertEquals(270.dp, defaultTokens.cardWidth)
        assertEquals(389.dp, defaultTokens.cardHeight)

        // Bounded detail card
        assertEquals(312.dp, defaultTokens.detailCardWidth)
        assertEquals(406.dp, defaultTokens.detailCardHeight)

        // Portrait dimensions
        assertEquals(84.dp, defaultTokens.portraitWidth)
        assertEquals(90.dp, defaultTokens.portraitHeight)
        assertEquals(56.dp, defaultTokens.detailPortraitWidth)
        assertEquals(62.dp, defaultTokens.detailPortraitHeight)

        // Metrics & CTA
        assertEquals(66.dp, defaultTokens.metricsHeight)
        assertEquals(40.dp, defaultTokens.ctaHeight)
    }

    @Test
    fun expandedTokens_matchTargetGeometryDirection() {
        val expandedTokens = CompactCardTokens.Expanded

        assertEquals(286.dp, expandedTokens.cardWidth)
        assertEquals(401.dp, expandedTokens.cardHeight)
        assertEquals(326.dp, expandedTokens.detailCardWidth)
        assertEquals(418.dp, expandedTokens.detailCardHeight)
        assertEquals(88.dp, expandedTokens.portraitWidth)
        assertEquals(96.dp, expandedTokens.portraitHeight)
        assertEquals(60.dp, expandedTokens.detailPortraitWidth)
        assertEquals(66.dp, expandedTokens.detailPortraitHeight)
    }

    @Test
    fun compactTokens_matchTargetGeometryDirection() {
        val compactTokens = CompactCardTokens.Compact

        assertEquals(258.dp, compactTokens.cardWidth)
        assertEquals(374.dp, compactTokens.cardHeight)
        assertEquals(298.dp, compactTokens.detailCardWidth)
        assertEquals(390.dp, compactTokens.detailCardHeight)
        assertEquals(78.dp, compactTokens.portraitWidth)
        assertEquals(84.dp, compactTokens.portraitHeight)
        assertEquals(52.dp, compactTokens.detailPortraitWidth)
        assertEquals(58.dp, compactTokens.detailPortraitHeight)
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
