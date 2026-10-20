package com.example.badnewgym.feature.membercard.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AquaGlassTokens {
    // Canvas Background
    val CanvasBackgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFE8FDF5),
            Color(0xFFD4F8EB),
            Color(0xFFDEF9EF),
            Color(0xFFCEF6E7)
        )
    )

    // Glass Card Surfaces
    val CardSurface = Color(0xEDFFFFFF)
    val CardBorder = Color(0x66A7F3D0)
    val CardInnerBevel = Color(0x80FFFFFF)
    val CardShadow = Color(0x14059669)

    // Left Rail
    val RailSurface = Color(0xD6ECFDF5)
    val RailBorder = Color(0x4DA7F3D0)
    val RailActiveBg = Color(0xFF10B981)
    val RailActiveText = Color(0xFFFFFFFF)

    // Text Colors
    val TextNavy = Color(0xFF0F172A)
    val TextSlate = Color(0xFF334155)
    val TextMuted = Color(0xFF64748B)
    val TextLight = Color(0xFF94A3B8)

    // Accents
    val MintGreen = Color(0xFF10B981)
    val MintLight = Color(0xFFD1FAE5)
    val EmeraldDark = Color(0xFF059669)
    val CoralRose = Color(0xFFF43F5E)
    val CoralLight = Color(0xFFFFF1F2)
    val AmberWarning = Color(0xFFF59E0B)
    val AmberLight = Color(0xFFFEF3C7)
    val BlueCyan = Color(0xFF0284C7)
    val BlueLight = Color(0xFFE0F2FE)
    val PurpleViolet = Color(0xFF8B5CF6)
    val PurpleLight = Color(0xFFF3E8FF)

    // Glass Specular Shimmer
    val GlassShimmerBrush = Brush.verticalGradient(
        listOf(
            Color(0x80FFFFFF),
            Color(0x20FFFFFF),
            Color.Transparent
        )
    )

    // Coral Payment Glass Shimmer
    val CoralGlassWash = Brush.verticalGradient(
        listOf(
            Color(0x33FEE2E2),
            Color(0x10FFF1F2),
            Color.Transparent
        )
    )
}
