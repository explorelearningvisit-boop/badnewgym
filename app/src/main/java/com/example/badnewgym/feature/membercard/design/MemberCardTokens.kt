package com.example.badnewgym.feature.membercard.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object MemberCardTokens {
    // Primary Brand & Header
    val BrandEmerald = Color(0xFF00E676)
    val BrandEmeraldLight = Color(0xFF10B981)
    val BrandEmeraldDark = Color(0xFF059669)
    val HeaderDarkBlue = Color(0xFF031A3D)
    val HeaderMidBlue = Color(0xFF082B61)
    val HeaderDeepNavy = Color(0xFF021026)

    // Canvas Background
    val ScreenCanvas = Color(0xFFF1F6FA)
    val CardSurface = Color(0xFFFFFFFF)
    val CardBorder = Color(0xFFE2E8F0)
    val CardInnerBg = Color(0xFFF8FAFC)

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
    val DarkNavy = Color(0xFF0F172A)
    val SlateTitle = Color(0xFF1E293B)
    val SlateBody = Color(0xFF475569)
    val SlateMuted = Color(0xFF64748B)
    val SlateLight = Color(0xFF94A3B8)
    val WhitePrimary = Color(0xFFFFFFFF)
    val WhiteSecondary = Color(0xFFBAE6FD)

    // Badge Colors
    val PillAmberBg = Color(0xFFFEF3C7)
    val PillAmberText = Color(0xFF92400E)
    val PillAmberBorder = Color(0xFFFDE68A)
    val PillGreenBg = Color(0xFF10B981)
    val PillGreenText = Color(0xFFFFFFFF)

    // Semantic Accents
    val AccentRed = Color(0xFFEF4444)
    val AccentRedLight = Color(0xFFFEE2E2)
    val AccentBlue = Color(0xFF2563EB)
    val AccentBlueLight = Color(0xFFEFF6FF)
    val AccentPurple = Color(0xFF8B5CF6)
    val AccentPurpleLight = Color(0xFFF5F3FF)
    val AccentAmber = Color(0xFFF59E0B)
    val AccentAmberLight = Color(0xFFFEF3C7)
    val AccentOff = Color(0xFF64748B)
    val AccentCyan = Color(0xFF06B6D4)

    // Gauge Gradient
    val RadialGaugeGradient = Brush.sweepGradient(
        listOf(
            Color(0xFF00E676),
            Color(0xFF00B0FF),
            Color(0xFF10B981),
            Color(0xFF00E676)
        )
    )
    val AttendanceGradient = RadialGaugeGradient

    // Button Gradient
    val AnalyticsButtonGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF1D4ED8),
            Color(0xFF2563EB),
            Color(0xFF0284C7)
        )
    )
}
