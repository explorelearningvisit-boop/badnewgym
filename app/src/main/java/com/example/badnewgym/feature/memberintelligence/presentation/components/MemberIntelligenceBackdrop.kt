package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme

@Composable
fun MemberIntelligenceBackdrop(modifier: Modifier = Modifier) {
    val colors = BADGymTheme.colors
    Canvas(modifier) {
        drawRect(Brush.verticalGradient(listOf(colors.background, colors.surfaceMuted.copy(alpha = 0.48f), colors.background)))
        drawCircle(Brush.radialGradient(listOf(colors.accent.copy(alpha = 0.10f), Color.Transparent)), size.minDimension * 0.42f, Offset(size.width * 0.90f, size.height * 0.16f))
        drawCircle(Brush.radialGradient(listOf(colors.info.copy(alpha = 0.07f), Color.Transparent)), size.minDimension * 0.36f, Offset(size.width * 0.08f, size.height * 0.62f))
    }
}