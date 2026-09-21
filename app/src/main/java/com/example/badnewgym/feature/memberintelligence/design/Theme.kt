package com.example.badnewgym.feature.memberintelligence.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.badnewgym.feature.memberintelligence.design.colors.BADGymColors
import com.example.badnewgym.feature.memberintelligence.design.colors.NaturalFreshColors
import com.example.badnewgym.feature.memberintelligence.design.dimensions.BADGymDimensions
import com.example.badnewgym.feature.memberintelligence.design.dimensions.defaultDimensions
import com.example.badnewgym.feature.memberintelligence.design.motion.BADGymMotion
import com.example.badnewgym.feature.memberintelligence.design.motion.defaultMotion
import com.example.badnewgym.feature.memberintelligence.design.shapes.BADGymShapes
import com.example.badnewgym.feature.memberintelligence.design.shapes.defaultShapes
import com.example.badnewgym.feature.memberintelligence.design.dimensions.BADGymElevation
import com.example.badnewgym.feature.memberintelligence.design.dimensions.defaultElevation
import com.example.badnewgym.feature.memberintelligence.design.typography.BADGymTypography
import com.example.badnewgym.feature.memberintelligence.design.typography.defaultTypography

import com.example.badnewgym.feature.memberintelligence.design.colors.AdaptiveThemeContext

val LocalBADGymColors = staticCompositionLocalOf<BADGymColors> { error("No colors provided") }
val LocalBADGymTypography = staticCompositionLocalOf<BADGymTypography> { error("No typography provided") }
val LocalBADGymDimensions = staticCompositionLocalOf<BADGymDimensions> { error("No dimensions provided") }
val LocalBADGymMotion = staticCompositionLocalOf<BADGymMotion> { error("No motion provided") }
val LocalBADGymShapes = staticCompositionLocalOf<BADGymShapes> { error("No shapes provided") }
val LocalBADGymElevation = staticCompositionLocalOf<BADGymElevation> { error("No elevation provided") }
val LocalAdaptiveContext = staticCompositionLocalOf<AdaptiveThemeContext?> { null }

object BADGymTheme {
    val colors: BADGymColors
        @Composable get() = LocalBADGymColors.current
    val typography: BADGymTypography
        @Composable get() = LocalBADGymTypography.current
    val dimensions: BADGymDimensions
        @Composable get() = LocalBADGymDimensions.current
    val motion: BADGymMotion
        @Composable get() = LocalBADGymMotion.current
    val shapes: BADGymShapes
        @Composable get() = LocalBADGymShapes.current
    val elevation: BADGymElevation
        @Composable get() = LocalBADGymElevation.current
    val adaptiveContext: AdaptiveThemeContext?
        @Composable get() = LocalAdaptiveContext.current
}

@Composable
fun BADGymTheme(
    colors: BADGymColors = NaturalFreshColors,
    typography: BADGymTypography = defaultTypography,
    dimensions: BADGymDimensions = defaultDimensions,
    motion: BADGymMotion = defaultMotion,
    shapes: BADGymShapes = defaultShapes,
    elevation: BADGymElevation = defaultElevation,
    adaptiveContext: AdaptiveThemeContext? = null,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalBADGymColors provides colors,
        LocalBADGymTypography provides typography,
        LocalBADGymDimensions provides dimensions,
        LocalBADGymMotion provides motion,
        LocalBADGymShapes provides shapes,
        LocalBADGymElevation provides elevation,
        LocalAdaptiveContext provides adaptiveContext,
        content = content
    )
}
