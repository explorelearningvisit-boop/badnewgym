package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Locks the card's declared geometry.
 *
 * The prompt fixes the component at 200dp x 300dp. dp is density independent, so the
 * assertion is valid on every device: physical pixels are `dp * density` and are
 * measured separately at screenshot time.
 */
class CompactMemberIntelligenceCardGeometryTest {

    @Test
    fun `card is locked to 200dp by 300dp`() {
        assertEquals(200f, CARD_WIDTH.value, 0.001f)
        assertEquals(300f, CARD_HEIGHT.value, 0.001f)
    }
}