package com.example.badnewgym.feature.memberintelligence.design.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BADGymElevation(
    val level0: Dp,
    val level1: Dp,
    val level2: Dp,
    val level3: Dp,
    val level4: Dp,
    val level5: Dp
)

val defaultElevation = BADGymElevation(
    level0 = 0.dp,
    level1 = 1.dp,
    level2 = 3.dp,
    level3 = 6.dp,
    level4 = 8.dp,
    level5 = 12.dp
)
