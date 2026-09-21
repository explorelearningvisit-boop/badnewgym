package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BADGymDimensions(
    val railWidth: Dp = 60.dp,
    val cardCornerRadius: Dp = 18.dp,
    val innerCornerRadius: Dp = 10.dp,
    val outerPadding: Dp = 12.dp,
    val standardSpacing: Dp = 8.dp,
    val memberPhotoSize: Dp = 64.dp,
    val miniPhotoSize: Dp = 40.dp,
    val strokeWidth: Dp = 1.dp
)

val defaultDimensions = BADGymDimensions()
