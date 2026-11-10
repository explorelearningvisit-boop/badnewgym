package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.membercard.presentation.intelligence.NeuPalette
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
    val Background get() = NeuPalette.surface
    val Surface get() = Color.White
    val Text get() = NeuPalette.ink
    val Secondary get() = NeuPalette.muted
    val TextMuted get() = NeuPalette.faint
    val CardBorder get() = accent.copy(alpha = 0.16f)

    // Palette & Glass Surface Aliases
    val Pearl get() = NeuPalette.surface
    val Card get() = Color.White
    val CardSoft get() = accent.copy(alpha = 0.08f)
    val CardInset get() = accent.copy(alpha = 0.05f)

    val Ink get() = NeuPalette.ink
    val InkSoft get() = accentDeep
    val Muted get() = NeuPalette.muted
    val Faint get() = NeuPalette.faint
    val Hairline get() = accent.copy(alpha = 0.14f)
    val HairlineStrong get() = accent.copy(alpha = 0.26f)

    val Emerald get() = accent
    val EmeraldDeep get() = accentDeep
    val EmeraldWash get() = accent.copy(alpha = 0.12f)
    val EmeraldEdge get() = accent

    val Violet = NeuPalette.violet
    val VioletWash = NeuPalette.violetWash
    val Blue = NeuPalette.blue
    val BlueWash = NeuPalette.violetWash
    val Amber = NeuPalette.amberDeep
    val AmberWash = NeuPalette.amberWash
    val Rose = NeuPalette.coralDeep
    val RoseWash = NeuPalette.violetWash
}
