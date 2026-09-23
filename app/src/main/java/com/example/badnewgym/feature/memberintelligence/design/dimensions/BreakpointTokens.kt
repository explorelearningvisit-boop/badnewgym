package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ScreenBreakpoint {
    COMPACT,
    MEDIUM,
    EXPANDED
}

data class ResponsiveGeometry(
    val breakpoint: ScreenBreakpoint,
    val railWidth: Dp,
    val portraitWidth: Dp,
    val portraitHeight: Dp,
    val outerPaddingHorizontal: Dp,
    val contentPaddingHorizontal: Dp
)

object BreakpointTokens {
    val Compact = ResponsiveGeometry(
        breakpoint = ScreenBreakpoint.COMPACT,
        railWidth = 52.dp,
        portraitWidth = 72.dp,
        portraitHeight = 84.dp,
        outerPaddingHorizontal = 6.dp,
        contentPaddingHorizontal = 8.dp
    )

    val Medium = ResponsiveGeometry(
        breakpoint = ScreenBreakpoint.MEDIUM,
        railWidth = 58.dp,
        portraitWidth = 82.dp,
        portraitHeight = 94.dp,
        outerPaddingHorizontal = 8.dp,
        contentPaddingHorizontal = 12.dp
    )

    val Expanded = ResponsiveGeometry(
        breakpoint = ScreenBreakpoint.EXPANDED,
        railWidth = 64.dp,
        portraitWidth = 90.dp,
        portraitHeight = 104.dp,
        outerPaddingHorizontal = 12.dp,
        contentPaddingHorizontal = 16.dp
    )
}

@Composable
fun rememberResponsiveGeometry(): ResponsiveGeometry {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidth <= 360 -> BreakpointTokens.Compact
        screenWidth >= 412 -> BreakpointTokens.Expanded
        else -> BreakpointTokens.Medium
    }
}
