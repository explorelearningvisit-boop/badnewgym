package com.example.badnewgym.feature.membercard.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AquaGlassTokens {
    // Ethereal Mint Aurora Canvas Background
    val CanvasBackgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFE3F8ED),
            Color(0xFFDCF6EA),
            Color(0xFFE8FAF1),
            Color(0xFFF3FCF7),
            Color(0xFFE2F5EC)
        )
    )

    // Aurora Glow Orbs
    val AuroraCyanGlow = Color(0x3338EF7D)
    val AuroraEmeraldGlow = Color(0x2611998E)
    val AuroraGoldGlow = Color(0x1AFBBF24)

    // 4 Bento Jewel-Tone Accent Gradients
    // 1. Attendance (Spring Green -> Emerald -> Forest Mint)
    val AttendanceCardGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF00E676),
            Color(0xFF10B981),
            Color(0xFF059669)
        )
    )
    val AttendanceBorderGlow = Color(0x80A7F3D0)

    // 2. Payment (Amber -> Golden Honey -> Flame Orange)
    val PaymentCardGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFFFA000),
            Color(0xFFF59E0B),
            Color(0xFFEA580C)
        )
    )
    val PaymentBorderGlow = Color(0x80FDE68A)
    val PaymentWarningPill = Color(0xE6DC2626)

    // 3. Workout (Electric Cyan -> Sky Blue -> Royal Cobalt)
    val WorkoutCardGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF00E5FF),
            Color(0xFF0284C7),
            Color(0xFF1D4ED8)
        )
    )
    val WorkoutBorderGlow = Color(0x80BAE6FD)

    // 4. Body Progress (Rose Magenta -> Vivid Fuchsia -> Radiant Violet)
    val BodyProgressCardGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFF43F5E),
            Color(0xFFD946EF),
            Color(0xFF8B5CF6)
        )
    )
    val BodyProgressBorderGlow = Color(0x80F5D0FE)

    // Glass Card Surfaces
    val CardSurface = Color(0xF7FFFFFF)
    val CardSurfaceTranslucent = Color(0xD9FFFFFF)
    val CardSurfaceUltra = Color(0xEEFFFFFF)
    val CardBorder = Color(0x80A7F3D0)
    val CardBorderGlow = Color(0x332DD4BF)
    val CardInnerBevel = Color(0x80FFFFFF)
    val CardShadow = Color(0x1A059669)

    // Left Rail (legacy support for AquaGlassComponents)
    val RailSurface = Color(0xD9EBFBF5)
    val RailBorder = Color(0x66A7F3D0)
    val RailActiveBg = Color(0xFF10B981)
    val RailActiveText = Color(0xFFFFFFFF)
    val RailIconBg = Color(0xFFD1FAE5)
    val RailIconTint = Color(0xFF0F766E)

    // Dock
    val DockSurface = Color(0xF2FFFFFF)
    val DockBorder = Color(0x80A7F3D0)
    val DockActiveBg = Color(0xFF10B981)

    // Text Colors
    val TextNavy = Color(0xFF0F172A)
    val TextForest = Color(0xFF0A3F2C)
    val TextSlate = Color(0xFF334155)
    val TextMuted = Color(0xFF64748B)
    val TextLight = Color(0xFF94A3B8)
    val TextPureWhite = Color(0xFFFFFFFF)

    // Accents
    val MintGreen = Color(0xFF10B981)
    val MintNeon = Color(0xFF00E676)
    val MintLight = Color(0xFFD1FAE5)
    val EmeraldDark = Color(0xFF059669)
    val CoralRose = Color(0xFFEF4444)
    val CoralDark = Color(0xFFDC2626)
    val CoralLight = Color(0xFFFFF1F2)
    val AmberWarning = Color(0xFFF59E0B)
    val AmberLight = Color(0xFFFEF3C7)
    val BlueCyan = Color(0xFF0284C7)
    val BlueLight = Color(0xFFE0F2FE)
    val BlueRoyal = Color(0xFF2563EB)
    val PurpleViolet = Color(0xFF8B5CF6)
    val PurpleLight = Color(0xFFF3E8FF)
    val GoldCrown = Color(0xFFF59E0B)

    // Glass Specular Shimmer
    val GlassShimmerBrush = Brush.verticalGradient(
        listOf(
            Color(0x99FFFFFF),
            Color(0x20FFFFFF),
            Color.Transparent
        )
    )

    // Card Glow Brushes
    val CardHeaderShimmer = Brush.horizontalGradient(
        listOf(
            Color(0x40FFFFFF),
            Color(0x15FFFFFF),
            Color.Transparent
        )
    )
}
