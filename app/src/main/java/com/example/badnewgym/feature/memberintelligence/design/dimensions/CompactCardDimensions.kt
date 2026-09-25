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
    val cardWidth: Dp = 312.dp,
    val minCardWidth: Dp = 290.dp,
    val maxCardWidth: Dp = 340.dp,
    val cardHeight: Dp = 406.dp,
    val detailCardWidth: Dp = 340.dp,
    val detailCardHeight: Dp = 443.dp,
    val railWidth: Dp = 56.dp,
    val cornerRadius: Dp = 24.dp,
    val outerHorizontalPadding: Dp = 12.dp,
    val carouselGap: Dp = 12.dp,
    val innerPaddingHorizontal: Dp = 14.dp,
    val innerPaddingVertical: Dp = 14.dp,
    val portraitWidth: Dp = 100.dp,
    val portraitHeight: Dp = 106.dp,
    val detailPortraitWidth: Dp = 108.dp,
    val detailPortraitHeight: Dp = 114.dp,
    val portraitRadius: Dp = 16.dp,
    val eventHeaderHeight: Dp = 32.dp,
    val identityHeight: Dp = 100.dp,
    val membershipStatusHeight: Dp = 38.dp,
    val metricsHeight: Dp = 72.dp,
    val ctaHeight: Dp = 44.dp,
    val leafAccentSize: Dp = 36.dp,
    val strokeWidth: Dp = 1.dp
)

object CompactCardTokens {
    val Compact = CompactCardDimensions(
        cardWidth = 296.dp,
        cardHeight = 390.dp,
        detailCardWidth = 320.dp,
        detailCardHeight = 420.dp,
        railWidth = 50.dp,
        outerHorizontalPadding = 8.dp,
        carouselGap = 8.dp,
        portraitWidth = 96.dp,
        portraitHeight = 102.dp,
        detailPortraitWidth = 104.dp,
        detailPortraitHeight = 110.dp,
        metricsHeight = 68.dp,
        ctaHeight = 42.dp
    )

    val Default = CompactCardDimensions(
        cardWidth = 312.dp,
        cardHeight = 406.dp,
        detailCardWidth = 340.dp,
        detailCardHeight = 443.dp,
        railWidth = 56.dp,
        outerHorizontalPadding = 12.dp,
        carouselGap = 12.dp,
        portraitWidth = 100.dp,
        portraitHeight = 106.dp,
        detailPortraitWidth = 108.dp,
        detailPortraitHeight = 114.dp,
        metricsHeight = 72.dp,
        ctaHeight = 44.dp
    )

    val Expanded = CompactCardDimensions(
        cardWidth = 328.dp,
        cardHeight = 422.dp,
        detailCardWidth = 360.dp,
        detailCardHeight = 460.dp,
        railWidth = 60.dp,
        outerHorizontalPadding = 16.dp,
        carouselGap = 16.dp,
        portraitWidth = 104.dp,
        portraitHeight = 110.dp,
        detailPortraitWidth = 112.dp,
        detailPortraitHeight = 118.dp,
        metricsHeight = 78.dp,
        ctaHeight = 48.dp
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
