package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BADGymDimensions(
    val shellCornerRadius: Dp = 24.dp,
    val cardCornerRadius: Dp = 18.dp,
    val innerCornerRadius: Dp = 10.dp,
    val innerCardRadius: Dp = 12.dp,
    val pillRadius: Dp = 14.dp,
    val ctaRadius: Dp = 14.dp,
    val smallIconCornerRadius: Dp = 10.dp,
    val railWidth: Dp = 58.dp,
    val portraitWidth: Dp = 82.dp,
    val portraitHeight: Dp = 94.dp,
    val portraitRadius: Dp = 14.dp,
    val memberPhotoSize: Dp = 64.dp,
    val miniPhotoSize: Dp = 40.dp,
    val headerButtonSize: Dp = 30.dp,
    val headerBrandIconSize: Dp = 32.dp,
    val metricTileHeight: Dp = 82.dp,
    val metricProgressRingSize: Dp = 38.dp,
    val ctaHeight: Dp = 48.dp,
    val outerPadding: Dp = 12.dp,
    val outerPaddingHorizontal: Dp = 8.dp,
    val outerPaddingVertical: Dp = 6.dp,
    val contentPaddingHorizontal: Dp = 12.dp,
    val contentPaddingVertical: Dp = 10.dp,
    val standardSpacing: Dp = 8.dp,
    val spacingSmall: Dp = 6.dp,
    val spacingMedium: Dp = 8.dp,
    val spacingStandard: Dp = 10.dp,
    val spacingLarge: Dp = 14.dp,
    val strokeWidth: Dp = 1.dp
)

val defaultDimensions = BADGymDimensions()
