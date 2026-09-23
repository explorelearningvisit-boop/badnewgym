package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * BAD GYM Stage 2 — Compact Member Card Geometry Tokens.
 *
 * Dedicated geometry token set for the compact member card carousel browse surface.
 * Strictly decoupled from the full-screen reference geometry tokens.
 */
data class CompactCardDimensions(
    val cardWidth: Dp = 232.dp,
    val minCardWidth: Dp = 210.dp,
    val maxCardWidth: Dp = 250.dp,
    val cardHeight: Dp = 356.dp,
    val detailCardWidth: Dp = 276.dp,
    val detailCardHeight: Dp = 372.dp,
    val railWidth: Dp = 50.dp,
    val cornerRadius: Dp = 22.dp,
    val outerHorizontalPadding: Dp = 16.dp,
    val carouselGap: Dp = 12.dp,
    val innerPaddingHorizontal: Dp = 12.dp,
    val innerPaddingVertical: Dp = 10.dp,
    val portraitWidth: Dp = 72.dp,
    val portraitHeight: Dp = 78.dp,
    val portraitRadius: Dp = 12.dp,
    val eventHeaderHeight: Dp = 26.dp,
    val identityHeight: Dp = 78.dp,
    val membershipStatusHeight: Dp = 32.dp,
    val metricsHeight: Dp = 62.dp,
    val ctaHeight: Dp = 38.dp,
    val leafAccentSize: Dp = 28.dp,
    val strokeWidth: Dp = 1.dp
)

object CompactCardTokens {
    val Compact = CompactCardDimensions(
        cardWidth = 224.dp,
        cardHeight = 346.dp,
        detailCardWidth = 264.dp,
        detailCardHeight = 362.dp,
        railWidth = 48.dp,
        outerHorizontalPadding = 14.dp,
        carouselGap = 10.dp,
        portraitWidth = 66.dp,
        portraitHeight = 72.dp,
        metricsHeight = 58.dp,
        ctaHeight = 36.dp
    )

    val Default = CompactCardDimensions(
        cardWidth = 232.dp,
        cardHeight = 356.dp,
        detailCardWidth = 276.dp,
        detailCardHeight = 372.dp,
        railWidth = 50.dp,
        outerHorizontalPadding = 16.dp,
        carouselGap = 12.dp,
        portraitWidth = 72.dp,
        portraitHeight = 78.dp,
        metricsHeight = 62.dp,
        ctaHeight = 38.dp
    )

    val Expanded = CompactCardDimensions(
        cardWidth = 246.dp,
        cardHeight = 366.dp,
        detailCardWidth = 288.dp,
        detailCardHeight = 380.dp,
        railWidth = 52.dp,
        outerHorizontalPadding = 18.dp,
        carouselGap = 14.dp,
        portraitWidth = 78.dp,
        portraitHeight = 84.dp,
        metricsHeight = 66.dp,
        ctaHeight = 40.dp
    )
}

@Composable
fun rememberCompactCardDimensions(): CompactCardDimensions {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidth <= 360 -> CompactCardTokens.Compact
        screenWidth >= 412 -> CompactCardTokens.Expanded
        else -> CompactCardTokens.Default
    }
}
