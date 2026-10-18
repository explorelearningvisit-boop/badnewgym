package com.example.badnewgym.feature.membercard.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object MemberCardTokens {
    // Primary Brand & Surfaces
    val BrandEmerald = Color(0xFF00C853)
    val BrandEmeraldDark = Color(0xFF059669)
    val BrandEmeraldLight = Color(0xFF10B981)
    val BrandEmeraldBg = Color(0xFFE8FDF2)
    val MintBackground = Color(0xFFF4FEFB)
    val CardBackground = Color(0xFFFFFFFF)
    val CardBorder = Color(0xFFE2F4EC)
    val CardGlow = Color(0x3300C853)

    // Typography Colors
    val DarkNavy = Color(0xFF0F172A)
    val SlateTitle = Color(0xFF1E293B)
    val SlateBody = Color(0xFF475569)
    val SlateMuted = Color(0xFF64748B)
    val SlateLight = Color(0xFF94A3B8)

    // Badge Colors
    val PillAmberBg = Color(0xFFFEF3C7)
    val PillAmberText = Color(0xFFB45309)
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
    val AccentOff = Color(0xFF64748B)
    val AccentOffLight = Color(0xFFF1F5F9)
    val AccentLate = Color(0xFFF59E0B)
    val AccentLateLight = Color(0xFFFEF3C7)

    // Chart & Widget Gradients
    val AttendanceGradient = Brush.sweepGradient(
        listOf(
            Color(0xFF06B6D4),
            Color(0xFF10B981),
            Color(0xFF00C853),
            Color(0xFF06B6D4)
        )
    )

    val WorkoutBarGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF00E676),
            Color(0xFF10B981),
            Color(0xFF2563EB),
            Color(0xFF1D4ED8)
        )
    )

    val AbsentBarGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFF87171),
            Color(0xFFEF4444)
        )
    )
}
