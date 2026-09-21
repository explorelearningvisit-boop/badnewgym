package com.example.badnewgym.feature.memberintelligence.design.colors

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ContrastResolverTest {
    @Test
    fun lightBackgroundUsesDarkForeground() {
        assertEquals(Color.Black, ContrastResolver.contentColorFor(Color.White))
    }

    @Test
    fun darkBackgroundUsesLightForeground() {
        assertEquals(Color.White, ContrastResolver.contentColorFor(Color.Black))
    }
}
