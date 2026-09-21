package com.example.badnewgym.feature.memberintelligence.design.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object ContrastResolver {

    /**
     * Determines whether white or black text should be used on top of a given background color
     * to ensure sufficient contrast.
     */
    fun contentColorFor(backgroundColor: Color): Color {
        return if (backgroundColor.luminance() > 0.5f) {
            Color.Black
        } else {
            Color.White
        }
    }

    fun resolvePrimaryText(surfaceColor: Color, themeColors: BADGymColors): Color {
        val lum = surfaceColor.luminance()
        return if (lum > 0.5f) {
            // Light surface, prefer dark text
            if (themeColors.textPrimary.luminance() < 0.5f) themeColors.textPrimary else Color(0xFF1B2A20)
        } else {
            // Dark surface, prefer light text
            if (themeColors.textPrimary.luminance() > 0.5f) themeColors.textPrimary else Color.White
        }
    }

    fun resolveSecondaryText(surfaceColor: Color, themeColors: BADGymColors): Color {
        val lum = surfaceColor.luminance()
        return if (lum > 0.5f) {
            if (themeColors.textSecondary.luminance() < 0.5f) themeColors.textSecondary else Color(0xFF4B6354)
        } else {
            if (themeColors.textSecondary.luminance() > 0.5f) themeColors.textSecondary else Color(0xFF94A3B8)
        }
    }
}
