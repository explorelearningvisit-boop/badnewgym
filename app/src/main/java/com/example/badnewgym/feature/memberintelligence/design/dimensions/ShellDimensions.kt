package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * BAD GYM Member Card shell dimensions.
 *
 * Geometry is taken from the approved "Complete Member Card Shell" spec:
 * total card 360dp = safe padding 16 + vertical rail 56 + content 288.
 * The rail is part of the card, so content is always card width - 72dp.
 */
data class ShellDimensions(
    val cardWidth: Dp = 360.dp,
    val safePadding: Dp = 12.dp,
    val railWidth: Dp = 58.dp,
    val contentWidth: Dp = 278.dp,

    val headerHeight: Dp = 44.dp,
    val tabBarHeight: Dp = 28.dp,
    val identityHeight: Dp = 102.dp,
    val currentVisitHeight: Dp = 106.dp,
    val actionBarHeight: Dp = 52.dp,

    val gridColumns: Int = 4,
    val gridGap: Dp = 6.dp,

    val cardCornerRadius: Dp = 24.dp,
    val widgetCornerRadius: Dp = 16.dp,
    val innerCornerRadius: Dp = 10.dp,

    val memberPhotoSize: Dp = 48.dp,
    val miniPhotoSize: Dp = 32.dp,
    val touchTarget: Dp = 48.dp,
    val strokeWidth: Dp = 1.dp
) {
    /** Content width actually available after rail and padding. */
    fun contentFor(availableWidth: Dp): Dp = availableWidth - safePadding * 2 - railWidth
}

val defaultShellDimensions = ShellDimensions()

/** Responsive tiers from the shell spec. */
enum class ScreenTier(
    val label: String,
    val minWidthDp: Int,
    val columns: Int,
    val railVisible: Boolean
) {
    SMALL_PHONE("Small Phone", 320, 4, true),
    NORMAL_PHONE("Normal Phone", 360, 4, true),
    LARGE_PHONE("Large Phone", 400, 4, true),
    TABLET("Tablet", 600, 6, true),
    FOLDABLE("Foldable", 800, 8, false);

    companion object {
        fun forWidth(dp: Int): ScreenTier =
            entries.lastOrNull { dp >= it.minWidthDp } ?: SMALL_PHONE
    }
}