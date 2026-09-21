package com.example.badnewgym.feature.memberintelligence.design

import com.example.badnewgym.feature.memberintelligence.design.dimensions.BADGymElevation
import com.example.badnewgym.feature.memberintelligence.design.dimensions.defaultElevation
import com.example.badnewgym.feature.memberintelligence.design.motion.BADGymMotion
import com.example.badnewgym.feature.memberintelligence.design.motion.bouncyMotion
import com.example.badnewgym.feature.memberintelligence.design.motion.defaultMotion
import com.example.badnewgym.feature.memberintelligence.design.motion.snappyMotion
import com.example.badnewgym.feature.memberintelligence.design.shapes.BADGymShapes
import com.example.badnewgym.feature.memberintelligence.design.shapes.defaultShapes
import com.example.badnewgym.feature.memberintelligence.design.shapes.roundedShapes
import com.example.badnewgym.feature.memberintelligence.design.shapes.sharpShapes

/**
 * Acts as the centralized registry for mapping [ThemeId]s to their concrete visual properties.
 * 
 * Provides shapes, elevation layers, and motion specifications for each
 * of the 8 distinct themes used in the Member Intelligence feature.
 */
object ThemeDefinition

fun ThemeId.elevation(): BADGymElevation = when (this) {
    ThemeId.PREMIUM_3D -> defaultElevation
    ThemeId.MINIMAL_DARK -> defaultElevation
    else -> defaultElevation
}

fun ThemeId.shapes(): BADGymShapes = when (this) {
    ThemeId.NATURAL_FRESH -> roundedShapes
    ThemeId.MINIMAL_DARK, ThemeId.BEAST_MODE -> sharpShapes
    ThemeId.PREMIUM_3D -> defaultShapes
    else -> defaultShapes
}

fun ThemeId.motion(): BADGymMotion = when (this) {
    ThemeId.FUTURISTIC_NEON, ThemeId.VIBRANT_GRADIENT -> bouncyMotion
    ThemeId.MINIMAL_DARK, ThemeId.BEAST_MODE -> snappyMotion
    else -> defaultMotion
}
