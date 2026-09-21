package com.example.badnewgym.feature.memberintelligence.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A shell container that applies the active theme's background, container shape,
 * and elevation settings to the entire feature module.
 */
@Composable
fun ThemeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val adaptiveContext = BADGymTheme.adaptiveContext
    val isGlass = adaptiveContext?.useGlassmorphism == true

    val surfaceColor = if (isGlass) {
        BADGymTheme.colors.surface.copy(alpha = 0.7f)
    } else {
        BADGymTheme.colors.surface
    }

    Surface(
        modifier = modifier,
        color = surfaceColor,
        shape = BADGymTheme.shapes.container,
        shadowElevation = if (isGlass) 0.dp else BADGymTheme.elevation.level2,
        content = content
    )
}
