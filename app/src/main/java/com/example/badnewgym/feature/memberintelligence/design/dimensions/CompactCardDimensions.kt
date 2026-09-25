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
    val cardWidth: Dp = 270.dp,
    val minCardWidth: Dp = 245.dp,
    val maxCardWidth: Dp = 295.dp,
    val cardHeight: Dp = 389.dp,
    val detailCardWidth: Dp = 312.dp,
    val detailCardHeight: Dp = 406.dp,
    val railWidth: Dp = 50.dp,
    val cornerRadius: Dp = 22.dp,
    val outerHorizontalPadding: Dp = 14.dp,
    val carouselGap: Dp = 12.dp,
    val innerPaddingHorizontal: Dp = 13.dp,
    val innerPaddingVertical: Dp = 11.dp,
    val portraitWidth: Dp = 84.dp,
    val portraitHeight: Dp = 90.dp,
    val detailPortraitWidth: Dp = 56.dp,
    val detailPortraitHeight: Dp = 62.dp,
    val portraitRadius: Dp = 12.dp,
    val eventHeaderHeight: Dp = 28.dp,
    val identityHeight: Dp = 86.dp,
    val membershipStatusHeight: Dp = 34.dp,
    val metricsHeight: Dp = 66.dp,
    val ctaHeight: Dp = 40.dp,
    val leafAccentSize: Dp = 30.dp,
    val strokeWidth: Dp = 1.dp
)

object CompactCardTokens {
    val Compact = CompactCardDimensions(
        cardWidth = 258.dp,
        cardHeight = 374.dp,
        detailCardWidth = 298.dp,
        detailCardHeight = 390.dp,
        railWidth = 48.dp,
        outerHorizontalPadding = 12.dp,
        carouselGap = 10.dp,
        portraitWidth = 78.dp,
        portraitHeight = 84.dp,
        detailPortraitWidth = 52.dp,
        detailPortraitHeight = 58.dp,
        metricsHeight = 62.dp,
        ctaHeight = 38.dp
    )

    val Default = CompactCardDimensions(
        cardWidth = 270.dp,
        cardHeight = 389.dp,
        detailCardWidth = 312.dp,
        detailCardHeight = 406.dp,
        railWidth = 50.dp,
        outerHorizontalPadding = 14.dp,
        carouselGap = 12.dp,
        portraitWidth = 84.dp,
        portraitHeight = 90.dp,
        detailPortraitWidth = 56.dp,
        detailPortraitHeight = 62.dp,
        metricsHeight = 66.dp,
        ctaHeight = 40.dp
    )

    val Expanded = CompactCardDimensions(
        cardWidth = 286.dp,
        cardHeight = 401.dp,
        detailCardWidth = 326.dp,
        detailCardHeight = 418.dp,
        railWidth = 52.dp,
        outerHorizontalPadding = 16.dp,
        carouselGap = 14.dp,
        portraitWidth = 88.dp,
        portraitHeight = 96.dp,
        detailPortraitWidth = 60.dp,
        detailPortraitHeight = 66.dp,
        metricsHeight = 70.dp,
        ctaHeight = 42.dp
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
