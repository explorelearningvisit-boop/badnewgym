package com.example.badnewgym.feature.memberintelligence.design.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object ContrastResolver {

    /**
     * Calculates the WCAG contrast ratio between two colors.
     * Ratio ranges from 1.0 (no contrast) to 21.0 (maximum contrast).
     */
    fun calculateContrastRatio(foreground: Color, background: Color): Float {
        val l1 = foreground.luminance()
        val l2 = background.luminance()
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    /**
     * Determines whether white or black text should be used on top of a given background color
     * to ensure sufficient contrast (target > 4.5:1).
     */
    fun contentColorFor(backgroundColor: Color): Color {
        val whiteContrast = calculateContrastRatio(Color.White, backgroundColor)
        val blackContrast = calculateContrastRatio(Color.Black, backgroundColor)
        return if (whiteContrast >= blackContrast) Color.White else Color.Black
    }

    fun resolvePrimaryText(surfaceColor: Color, themeColors: BADGymColors): Color {
        val primaryContrast = calculateContrastRatio(themeColors.textPrimary, surfaceColor)
        // Target: 4.5:1 for normal body text
        if (primaryContrast >= 4.5f) {
            return themeColors.textPrimary
        }
        
        // Fallback to absolute white or black if theme color fails contrast
        return contentColorFor(surfaceColor)
    }

    fun resolveSecondaryText(surfaceColor: Color, themeColors: BADGymColors): Color {
        val secondaryContrast = calculateContrastRatio(themeColors.textSecondary, surfaceColor)
        // Target: 4.5:1 for normal body text
        if (secondaryContrast >= 4.5f) {
            return themeColors.textSecondary
        }
        
        // If secondary fails, try primary
        val primaryContrast = calculateContrastRatio(themeColors.textPrimary, surfaceColor)
        if (primaryContrast >= 4.5f) {
            return themeColors.textPrimary.copy(alpha = 0.8f) // Muted primary as secondary
        }

        // Fallback to absolute white/black muted
        return contentColorFor(surfaceColor).copy(alpha = 0.8f)
    }
    
    fun resolveTextOnAccent(accentColor: Color, themeColors: BADGymColors): Color {
        val themeContrast = calculateContrastRatio(themeColors.textOnAccent, accentColor)
        if (themeContrast >= 4.5f) {
            return themeColors.textOnAccent
        }
        return contentColorFor(accentColor)
    }
}
