package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Locks the card's declared geometry.
 *
 * The composition is authored in a 200dp x 320dp design space and displayed at
 * [CARD_SCALE]x, so the rendered constants must stay in that exact ratio. dp is
 * density independent, so the assertion is valid on every device: physical pixels are
 * `dp * density` and are measured separately at screenshot time.
 */
class CompactMemberIntelligenceCardGeometryTest {

    @Test
    fun `design space keeps the approved 200 by 320 ratio`() {
        assertEquals(200f, DESIGN_WIDTH.value, 0.001f)
        assertEquals(320f, DESIGN_HEIGHT.value, 0.001f)
    }

    @Test
    fun `displayed card is the design space enlarged by the display scale`() {
        assertEquals(DESIGN_WIDTH.value * CARD_SCALE, CARD_WIDTH.value, 0.001f)
        assertEquals(DESIGN_HEIGHT.value * CARD_SCALE, CARD_HEIGHT.value, 0.001f)
        // The card must be big enough to read on a phone, not a thumbnail.
        assertTrue("card width must exceed a half-screen 180dp", CARD_WIDTH.value > 180f)
        assertTrue("card height must exceed 300dp", CARD_HEIGHT.value > 300f)
    }
}