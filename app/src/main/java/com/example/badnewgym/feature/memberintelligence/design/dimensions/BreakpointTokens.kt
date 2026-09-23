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
        railWidth = 42.dp,
        portraitWidth = 80.dp,
        portraitHeight = 92.dp,
        outerPaddingHorizontal = 2.dp,
        contentPaddingHorizontal = 4.dp
    )

    val Medium = ResponsiveGeometry(
        breakpoint = ScreenBreakpoint.MEDIUM,
        railWidth = 46.dp,
        portraitWidth = 88.dp,
        portraitHeight = 100.dp,
        outerPaddingHorizontal = 3.dp,
        contentPaddingHorizontal = 6.dp
    )

    val Expanded = ResponsiveGeometry(
        breakpoint = ScreenBreakpoint.EXPANDED,
        railWidth = 50.dp,
        portraitWidth = 96.dp,
        portraitHeight = 108.dp,
        outerPaddingHorizontal = 4.dp,
        contentPaddingHorizontal = 8.dp
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
