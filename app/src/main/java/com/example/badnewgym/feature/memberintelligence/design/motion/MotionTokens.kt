package com.example.badnewgym.feature.memberintelligence.design.motion

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

data class BADGymMotion(
    val menuSlide: FiniteAnimationSpec<Float>,
    val quickFade: FiniteAnimationSpec<Float>,
    // Stage 7 — depth and press animation specs
    val pressFeedback: FiniteAnimationSpec<Float> = tween(durationMillis = 100),
    val pressRelease: FiniteAnimationSpec<Float> = tween(durationMillis = 140),
    val microState: FiniteAnimationSpec<Float> = tween(durationMillis = 150),
    val cardFocus: FiniteAnimationSpec<Float> = tween(durationMillis = 220),
    val carouselSettle: FiniteAnimationSpec<Float> = tween(durationMillis = 260),
    val depthParallax: FiniteAnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
)

val defaultMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    quickFade = tween(durationMillis = 150)
)

val snappyMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessHigh),
    quickFade = tween(durationMillis = 100),
    pressFeedback = tween(durationMillis = 80),
    pressRelease = tween(durationMillis = 120),
    microState = tween(durationMillis = 120),
    cardFocus = tween(durationMillis = 200),
    carouselSettle = tween(durationMillis = 220)
)

val bouncyMotion = BADGymMotion(
    menuSlide = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
    quickFade = tween(durationMillis = 200),
    pressFeedback = tween(durationMillis = 120),
    pressRelease = tween(durationMillis = 160),
    microState = tween(durationMillis = 180),
    cardFocus = tween(durationMillis = 250),
    carouselSettle = tween(durationMillis = 300)
)

