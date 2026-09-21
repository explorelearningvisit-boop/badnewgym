package com.example.badnewgym.feature.memberintelligence.design.motion

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

data class BADGymMotion(
    val menuSlide: FiniteAnimationSpec<Float>,
    val quickFade: FiniteAnimationSpec<Float>
)

val defaultMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    quickFade = tween(durationMillis = 150)
)

val snappyMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessHigh),
    quickFade = tween(durationMillis = 100)
)

val bouncyMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
    quickFade = tween(durationMillis = 200)
)
