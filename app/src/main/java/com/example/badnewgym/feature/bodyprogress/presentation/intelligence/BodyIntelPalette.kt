package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.membercard.design.BadGymThemeManager

/**
 * Theme-reactive palette for the full Body Intelligence workspace.
 *
 * Every accent derives from the active [BadGymThemeManager] theme, so switching
 * the dashboard theme re-skins this whole surface too; neutral ink stays legible
 * and status hues stay fixed so "good/warn" never changes meaning per theme.
 */
internal object BodyIntelPalette {
    private val theme get() = BadGymThemeManager.activeTheme
    private val accent get() = theme.primaryColor
    private val accentDeep get() = theme.secondaryColor

    val Primary get() = accent
    val DeepEmerald get() = accentDeep
    val FreshGreen get() = accent
    val SoftMint get() = accent.copy(alpha = 0.12f)
    val Background get() = Color(0xFFFAFBFC)
    val Surface get() = Color.White
    val Text get() = Color(0xFF1E2A24)
    val Secondary get() = Color(0xFF6B7A72)
    val TextMuted get() = Color(0xFF9AA6A0)
    val CardBorder get() = accent.copy(alpha = 0.16f)

    // Palette & Glass Surface Aliases
    val Pearl get() = Color(0xFFFAFBFC)
    val Card get() = Color.White
    val CardSoft get() = accent.copy(alpha = 0.08f)
    val CardInset get() = accent.copy(alpha = 0.05f)

    val Ink get() = Color(0xFF1E2A24)
    val InkSoft get() = accentDeep
    val Muted get() = Color(0xFF6B7A72)
    val Faint get() = Color(0xFF9AA6A0)
    val Hairline get() = accent.copy(alpha = 0.14f)
    val HairlineStrong get() = accent.copy(alpha = 0.26f)

    val Emerald get() = accent
    val EmeraldDeep get() = accentDeep
    val EmeraldWash get() = accent.copy(alpha = 0.12f)
    val EmeraldEdge get() = accent

    val Violet = Color(0xFF6B5BD2)
    val VioletWash = Color(0xFFF0EEFB)
    val Blue = Color(0xFF2C7BD4)
    val BlueWash = Color(0xFFE8F1FC)
    val Amber = Color(0xFFB4730E)
    val AmberWash = Color(0xFFFDF3E1)
    val Rose = Color(0xFFBE3A5E)
    val RoseWash = Color(0xFFFDEBF0)
}
