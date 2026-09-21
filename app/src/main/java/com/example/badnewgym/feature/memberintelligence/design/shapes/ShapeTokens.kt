package com.example.badnewgym.feature.memberintelligence.design.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class BADGymShapes(
    val card: Shape,
    val container: Shape,
    val button: Shape,
    val badge: Shape,
    val avatar: Shape
)

val defaultShapes = BADGymShapes(
    card = RoundedCornerShape(18.dp),
    container = RoundedCornerShape(24.dp),
    button = RoundedCornerShape(10.dp),
    badge = RoundedCornerShape(4.dp),
    avatar = RoundedCornerShape(12.dp)
)

val sharpShapes = BADGymShapes(
    card = RoundedCornerShape(0.dp),
    container = RoundedCornerShape(0.dp),
    button = RoundedCornerShape(0.dp),
    badge = RoundedCornerShape(0.dp),
    avatar = RoundedCornerShape(4.dp)
)

val roundedShapes = BADGymShapes(
    card = RoundedCornerShape(24.dp),
    container = RoundedCornerShape(32.dp),
    button = RoundedCornerShape(100.dp), // Fully rounded pill
    badge = RoundedCornerShape(8.dp),
    avatar = RoundedCornerShape(100.dp) // Circular
)
