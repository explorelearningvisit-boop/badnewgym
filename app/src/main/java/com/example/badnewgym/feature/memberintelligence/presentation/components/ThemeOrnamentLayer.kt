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
 * eight-card reference. Nothing is a flattened screenshot: every effect is a drawable
 * layer and therefore scales with the card.
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
                    Brush.radialGradient(listOf(Color(0xFF52B788).copy(alpha = .12f), Color.Transparent)),
                    radius = size.minDimension * .34f,
                    center = Offset(size.width * .12f, size.height * .78f)
                )
                leaf(Offset(size.width * .84f, size.height * .18f), 1.0f, c.accent.copy(alpha = .20f))
                leaf(Offset(size.width * .92f, size.height * .78f), .72f, c.accent.copy(alpha = .16f))
                leaf(Offset(size.width * .10f, size.height * .88f), .68f, c.accent.copy(alpha = .14f))
            }

            ThemeId.FUTURISTIC_NEON -> {
                drawRect(
                    Brush.linearGradient(listOf(Color(0xFF001B34), Color(0xFF02050B))),
                    size = size
                )
                neonLine(Offset(0f, size.height * .76f), Offset(size.width, size.height * .46f), c.accent)
                neonLine(Offset(size.width * .18f, size.height), Offset(size.width * .82f, 0f), c.accent.copy(alpha = .55f))
                neonLine(Offset(size.width * .48f, size.height), Offset(size.width, size.height * .68f), Color(0xFF7C3AED).copy(alpha = .55f))
                for (i in 1..6) {
                    val y = size.height * (i / 7f)
                    drawLine(c.accent.copy(alpha = .045f), Offset(0f, y), Offset(size.width, y), 1f)
                }
            }

            ThemeId.MINIMAL_DARK -> {
                drawRect(
                    Brush.linearGradient(listOf(Color(0xFF20252B), Color(0xFF090B0E))),
                    size = size
                )
                drawCircle(Color.White.copy(alpha = .025f), size.minDimension * .65f, Offset(size.width * .9f, size.height * .05f))
                for (i in 1..8) {
                    val y = size.height * (i / 9f)
                    drawLine(Color.White.copy(alpha = .018f), Offset(0f, y), Offset(size.width, y), 1f)
                }
            }

            ThemeId.GLASSMORPHISM -> {
                drawRect(
                    Brush.linearGradient(
                        listOf(Color(0xFFEAF3FF), Color(0xFFD8E4FF), Color(0xFFF4E9FF))
                    ),
                    size = size
                )
                drawCircle(Color(0xFF60A5FA).copy(alpha = .25f), size.minDimension * .45f, Offset(size.width * .92f, size.height * .16f))
                drawCircle(Color(0xFFC084FC).copy(alpha = .18f), size.minDimension * .38f, Offset(size.width * .12f, size.height * .76f))
                drawCircle(Color.White.copy(alpha = .24f), size.minDimension * .28f, Offset(size.width * .55f, size.height * .50f))
            }

            ThemeId.PREMIUM_3D -> {
                drawRect(
                    Brush.linearGradient(listOf(Color(0xFF3A2A12), Color(0xFF080705))),
                    size = size
                )
                metallicArc(Offset(size.width * .88f, size.height * .15f), size.minDimension * .50f, c.accent)
                metallicArc(Offset(size.width * .08f, size.height * .90f), size.minDimension * .34f, Color(0xFF8C6B24))
                drawCircle(Color(0xFFFFD700).copy(alpha = .07f), size.minDimension * .48f, Offset(size.width * .75f, size.height * .72f))
            }

            ThemeId.VIBRANT_GRADIENT -> {
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFDF4FF), Color(0xFFE0E7FF), Color(0xFFFFE4E6), Color(0xFFFEF3C7))
                    ),
                    size = size
                )
                drawCircle(Color(0xFFEC4899).copy(alpha = .16f), size.minDimension * .42f, Offset(size.width * .08f, size.height * .08f))
                drawCircle(Color(0xFF3B82F6).copy(alpha = .14f), size.minDimension * .46f, Offset(size.width * .94f, size.height * .46f))
                drawCircle(Color(0xFFF59E0B).copy(alpha = .12f), size.minDimension * .40f, Offset(size.width * .25f, size.height * .94f))
            }

            ThemeId.BEAST_MODE -> {
                drawRect(
                    Brush.linearGradient(listOf(Color(0xFF2A0508), Color(0xFF070708))),
                    size = size
                )
                claw(Offset(size.width * .14f, size.height * .88f), c.accent)
                claw(Offset(size.width * .86f, size.height * .20f), c.accent.copy(alpha = .65f))
                neonLine(Offset(0f, size.height * .55f), Offset(size.width, size.height * .35f), c.accent.copy(alpha = .32f))
            }

            ThemeId.PURPLE_ROYAL -> {
                drawRect(
                    Brush.linearGradient(listOf(Color(0xFF54208B), Color(0xFF11071E))),
                    size = size
                )
                drawCircle(Color(0xFFA855F7).copy(alpha = .20f), size.minDimension * .46f, Offset(size.width * .90f, size.height * .12f))
                drawCircle(Color(0xFFE879F9).copy(alpha = .12f), size.minDimension * .38f, Offset(size.width * .12f, size.height * .78f))
                diamond(Offset(size.width * .86f, size.height * .82f), size.minDimension * .10f, c.accent.copy(alpha = .24f))
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
    drawPath(path, Color.White.copy(alpha = .18f), style = Stroke(width = 1.2f))
}
