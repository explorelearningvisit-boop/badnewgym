package com.example.badnewgym.feature.membercard.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object MemberCardTokens {
    // Primary Brand & Header
    val BrandEmerald = Color(0xFF00E676)
    val BrandEmeraldLight = Color(0xFF21A87D)
    val BrandEmeraldDark = Color(0xFF178A66)
    val HeaderDarkBlue = Color(0xFF031A3D)
    val HeaderMidBlue = Color(0xFF082B61)
    val HeaderDeepNavy = Color(0xFF021026)

    // Canvas Background
    val ScreenCanvas = Color(0xFFF1F6FA)
    val CardSurface = Color(0xFFFFFFFF)
    val CardBorder = Color(0xFFDCDFE6)
    val CardInnerBg = Color(0xFFEDF0F4)

    // Header Gradients
    val HeaderBackgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF02132E),
            Color(0xFF072B63),
            Color(0xFF0A3B82),
            Color(0xFF0D47A1)
        )
    )

    val HeaderGlowGradient = Brush.radialGradient(
        listOf(
            Color(0x3500E676),
            Color(0x2000B0FF),
            Color.Transparent
        )
    )

    // Typography Colors
    val DarkNavy = Color(0xFF343434)
    val SlateTitle = Color(0xFF1E293B)
    val SlateBody = Color(0xFF6E7887)
    val SlateMuted = Color(0xFF8C94A0)
    val SlateLight = Color(0xFFA1AEBF)
    val WhitePrimary = Color(0xFFFFFFFF)
    val WhiteSecondary = Color(0xFFD3E3F0)

    // Badge Colors
    val PillAmberBg = Color(0xFFFBEFD4)
    val PillAmberText = Color(0xFF8A6410)
    val PillAmberBorder = Color(0xFFF7E3B2)
    val PillGreenBg = Color(0xFF21A87D)
    val PillGreenText = Color(0xFFFFFFFF)

    // Semantic Accents
    val AccentRed = Color(0xFFFF7272)
    val AccentRedLight = Color(0xFFFFE3E3)
    val AccentBlue = Color(0xFF6B92E5)
    val AccentBlueLight = Color(0xFFEFF6FF)
    val AccentPurple = Color(0xFF7B81BE)
    val AccentPurpleLight = Color(0xFFE9EDF0)
    val AccentAmber = Color(0xFFFFBB0D)
    val AccentAmberLight = Color(0xFFFBEFD4)
    val AccentOff = Color(0xFF8C94A0)
    val AccentCyan = Color(0xFF489CC1)

    // Gauge Gradient
    val RadialGaugeGradient = Brush.sweepGradient(
        listOf(
            Color(0xFF00E676),
            Color(0xFF00B0FF),
            Color(0xFF21A87D),
            Color(0xFF00E676)
        )
    )
    val AttendanceGradient = RadialGaugeGradient

    // Button Gradient
    val AnalyticsButtonGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF1D4ED8),
            Color(0xFF6B92E5),
            Color(0xFF489CC1)
        )
    )

    // Luxury Light Pearl Canvas Tokens
    val LightPearlCanvasGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFEAF3EF), // soft luminous emerald pearl at top
            Color(0xFFEDF0F4), // pristine porcelain slate
            Color(0xFFE6E9EF), // clean airy mist
            Color(0xFFEFF6FF)  // subtle azure touch at bottom
        )
    )

    val LightNavSurface = Color(0xF8FFFFFF)
    val LightNavBorder = Color(0xFFDCDFE6)

    val QuickPillPink = Color(0xFFFB7185)
    val QuickPillGreen = Color(0xFF42CD8C)
    val QuickPillPurple = Color(0xFF9AA1D2)
    val QuickPillGold = Color(0xFFFBBF24)
}
