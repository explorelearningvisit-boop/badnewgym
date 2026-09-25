package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId

/**
 * Native Compose reconstruction of the ornamental/background language visible in the
 * eight-card reference. Zero pitch black; rich neumorphic and glassmorphic depth.
 */
@Composable
fun ThemeOrnamentLayer(
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val c = BADGymTheme.colors
    Canvas(modifier = modifier.fillMaxSize()) {
        when (theme) {
            ThemeId.NATURAL_FRESH -> {
                drawCircle(
                    Brush.radialGradient(listOf(c.accent.copy(alpha = .18f), Color.Transparent)),
                    radius = size.minDimension * .42f,
                    center = Offset(size.width * .88f, size.height * .42f)
                )
                drawCircle(
                    Brush.radialGradient(listOf(c.success.copy(alpha = .12f), Color.Transparent)),
                    radius = size.minDimension * .34f,
                    center = Offset(size.width * .12f, size.height * .78f)
                )
                leaf(Offset(size.width * .84f, size.height * .18f), 1.0f, c.accent.copy(alpha = .20f))
                leaf(Offset(size.width * .92f, size.height * .78f), .72f, c.accent.copy(alpha = .16f))
                leaf(Offset(size.width * .10f, size.height * .88f), .68f, c.accent.copy(alpha = .14f))
            }

            ThemeId.FUTURISTIC_NEON -> {
                drawRect(
                    Brush.linearGradient(listOf(c.surfaceElevated, c.background)),
                    size = size
                )
                neonLine(Offset(0f, size.height * .76f), Offset(size.width, size.height * .46f), c.accent)
                neonLine(Offset(size.width * .18f, size.height), Offset(size.width * .82f, 0f), c.accent.copy(alpha = .55f))
                neonLine(Offset(size.width * .48f, size.height), Offset(size.width, size.height * .68f), c.info.copy(alpha = .55f))
                for (i in 1..6) {
                    val y = size.height * (i / 7f)
                    drawLine(c.accent.copy(alpha = .06f), Offset(0f, y), Offset(size.width, y), 1f)
                }
            }

            ThemeId.MINIMAL_DARK -> {
                drawRect(
                    Brush.linearGradient(listOf(c.surface, c.background)),
                    size = size
                )
                drawCircle(c.textPrimary.copy(alpha = .05f), size.minDimension * .65f, Offset(size.width * .9f, size.height * .05f))
                for (i in 1..8) {
                    val y = size.height * (i / 9f)
                    drawLine(c.border.copy(alpha = .35f), Offset(0f, y), Offset(size.width, y), 1f)
                }
            }

            ThemeId.GLASSMORPHISM -> {
                drawRect(
                    Brush.linearGradient(
                        listOf(c.surface, c.surfaceMuted, c.background)
                    ),
                    size = size
                )
                drawCircle(c.info.copy(alpha = .25f), size.minDimension * .45f, Offset(size.width * .92f, size.height * .16f))
                drawCircle(c.vip.copy(alpha = .18f), size.minDimension * .38f, Offset(size.width * .12f, size.height * .76f))
                drawCircle(c.textPrimary.copy(alpha = .35f), size.minDimension * .28f, Offset(size.width * .55f, size.height * .50f))
            }

            ThemeId.PREMIUM_3D -> {
                drawRect(
                    Brush.linearGradient(listOf(c.background, c.surfaceMuted)),
                    size = size
                )
                metallicArc(Offset(size.width * .88f, size.height * .15f), size.minDimension * .50f, c.accent)
                metallicArc(Offset(size.width * .08f, size.height * .90f), size.minDimension * .34f, c.accent)
                drawCircle(c.vip.copy(alpha = .12f), size.minDimension * .48f, Offset(size.width * .75f, size.height * .72f))
            }

            ThemeId.VIBRANT_GRADIENT -> {
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(c.background, c.surface, c.surfaceMuted, c.vipSoft)
                    ),
                    size = size
                )
                drawCircle(c.accent.copy(alpha = .16f), size.minDimension * .42f, Offset(size.width * .08f, size.height * .08f))
                drawCircle(c.info.copy(alpha = .14f), size.minDimension * .46f, Offset(size.width * .94f, size.height * .46f))
                drawCircle(c.warning.copy(alpha = .12f), size.minDimension * .40f, Offset(size.width * .25f, size.height * .94f))
            }

            ThemeId.BEAST_MODE -> {
                drawRect(
                    Brush.linearGradient(listOf(c.surface, c.background)),
                    size = size
                )
                claw(Offset(size.width * .14f, size.height * .88f), c.accent)
                claw(Offset(size.width * .86f, size.height * .20f), c.accent.copy(alpha = .65f))
                neonLine(Offset(0f, size.height * .55f), Offset(size.width, size.height * .35f), c.accent.copy(alpha = .32f))
            }

            ThemeId.PURPLE_ROYAL -> {
                drawRect(
                    Brush.linearGradient(listOf(c.surface, c.background)),
                    size = size
                )
                drawCircle(c.accent.copy(alpha = .22f), size.minDimension * .46f, Offset(size.width * .90f, size.height * .12f))
                drawCircle(c.vip.copy(alpha = .16f), size.minDimension * .38f, Offset(size.width * .12f, size.height * .78f))
                diamond(Offset(size.width * .86f, size.height * .82f), size.minDimension * .10f, c.accent.copy(alpha = .28f))
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.neonLine(
    a: Offset,
    b: Offset,
    color: Color
) {
    drawLine(color.copy(alpha = .12f), a, b, strokeWidth = 12f, cap = StrokeCap.Round)
    drawLine(color.copy(alpha = .30f), a, b, strokeWidth = 6f, cap = StrokeCap.Round)
    drawLine(color, a, b, strokeWidth = 1.5f, cap = StrokeCap.Round)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.leaf(
    center: Offset,
    scale: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y)
        cubicTo(
            center.x + 24f * scale, center.y - 28f * scale,
            center.x + 54f * scale, center.y - 14f * scale,
            center.x + 62f * scale, center.y + 6f * scale
        )
        cubicTo(
            center.x + 30f * scale, center.y + 12f * scale,
            center.x + 10f * scale, center.y + 10f * scale,
            center.x, center.y
        )
        close()
    }
    drawPath(path, color)
    drawLine(color.copy(alpha = .45f), center, Offset(center.x + 54f * scale, center.y - 3f * scale), 1.5f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.metallicArc(
    center: Offset,
    radius: Float,
    color: Color
) {
    drawArc(
        Brush.sweepGradient(listOf(Color.Transparent, color.copy(alpha = .55f), Color.Transparent)),
        startAngle = 210f,
        sweepAngle = 105f,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = 7f)
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.claw(
    origin: Offset,
    color: Color
) {
    repeat(3) { i ->
        val x = origin.x + i * 12f
        drawLine(
            color.copy(alpha = .28f),
            Offset(x, origin.y + 22f),
            Offset(x + 18f, origin.y - 22f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color,
            Offset(x, origin.y + 22f),
            Offset(x + 18f, origin.y - 22f),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.diamond(
    center: Offset,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        lineTo(center.x + radius * .72f, center.y)
        lineTo(center.x, center.y + radius)
        lineTo(center.x - radius * .72f, center.y)
        close()
    }
    drawPath(path, color)
    drawPath(path, color.copy(alpha = .18f), style = Stroke(width = 1.2f))
}
