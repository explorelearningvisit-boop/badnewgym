package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyRegion
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyRegions
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyView
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup

/**
 * Interactive front/back body map.
 *
 * The illustration is drawn from primitives so the surface has no asset-loading
 * cost and scales to any width. Every selectable muscle group also appears in
 * the chip row below, which is the accessible alternative to tapping the map -
 * muscle selection is never a small-hit-target-only interaction.
 */
@Composable
fun BodyAnatomyMap(
    view: AnatomyView,
    selected: MuscleGroup,
    onSelect: (MuscleGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    val regions = AnatomyRegions.of(view)
    val front = view == AnatomyView.FRONT
    val selectedRegions = regions.filter { it.muscle == selected }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(248.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BodyIntelPalette.CardInset)
            .semantics {
                contentDescription = "${view.label} body map. Selected muscle: ${selected.label}."
            }
            .pointerInput(view) {
                detectTapGestures { offset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    if (w <= 0f || h <= 0f) return@detectTapGestures
                    val nx = offset.x / w
                    val ny = offset.y / h
                    // Nearest region wins, so a tap just outside a small region
                    // still selects the muscle the member aimed at.
                    val hit = regions.minByOrNull { region ->
                        val cx = (region.left + region.right) / 2f
                        val cy = (region.top + region.bottom) / 2f
                        val dx = nx - cx
                        val dy = ny - cy
                        dx * dx + dy * dy
                    }
                    if (hit != null) onSelect(hit.muscle)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(248.dp)) {
            drawSilhouette(front = front)

            regions.forEach { region ->
                val isSelected = region.muscle == selected
                val left = region.left * size.width
                val top = region.top * size.height
                val width = (region.right - region.left) * size.width
                val height = (region.bottom - region.top) * size.height
                val corner = CornerRadius(width.coerceAtMost(height) * 0.42f)

                drawRoundRect(
                    color = if (isSelected) BodyIntelPalette.Emerald.copy(alpha = 0.22f)
                    else BodyIntelPalette.Emerald.copy(alpha = 0.03f),
                    topLeft = Offset(left, top),
                    size = Size(width, height),
                    cornerRadius = corner
                )
                if (isSelected) {
                    drawRoundRect(
                        color = BodyIntelPalette.EmeraldDeep,
                        topLeft = Offset(left, top),
                        size = Size(width, height),
                        cornerRadius = corner,
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                }
            }

            // Connector + marker for the selected group, front and back.
            if (selectedRegions.isNotEmpty()) {
                val anchor = selectedRegions.first()
                val cx = ((anchor.left + anchor.right) / 2f) * size.width
                val cy = anchor.top * size.height
                drawCircle(
                    color = BodyIntelPalette.EmeraldDeep,
                    radius = 3.dp.toPx(),
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

private fun DrawScope.drawSilhouette(front: Boolean) {
    val base = BodyIntelPalette.CardSoft
    val edge = BodyIntelPalette.HairlineStrong
    val w = size.width
    val h = size.height

    // A human figure is roughly 1:3.5 wide-to-tall. Keeping that ratio is what
    // stops the drawing from looking like a stack of blobs on a wide card, so
    // the figure is centred and scaled from the height, not the width.
    val figureW = h * 0.52f

    fun x(unit: Float): Float = w / 2f + unit * figureW
    fun y(unit: Float): Float = unit * h

    fun limb(fromX: Float, fromY: Float, toX: Float, toY: Float, thickness: Float) {
        val start = Offset(x(fromX), y(fromY))
        val end = Offset(x(toX), y(toY))
        drawLine(
            color = edge,
            start = start,
            end = end,
            strokeWidth = thickness * figureW + 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = base,
            start = start,
            end = end,
            strokeWidth = thickness * figureW,
            cap = StrokeCap.Round
        )
    }

    // Limbs first; the torso is painted over the shoulder and hip joins.
    limb(-0.33f, 0.185f, -0.42f, 0.360f, 0.20f)
    limb(-0.42f, 0.360f, -0.46f, 0.560f, 0.15f)
    limb(0.33f, 0.185f, 0.42f, 0.360f, 0.20f)
    limb(0.42f, 0.360f, 0.46f, 0.560f, 0.15f)

    limb(-0.15f, 0.500f, -0.17f, 0.720f, 0.26f)
    limb(-0.17f, 0.720f, -0.16f, 0.930f, 0.17f)
    limb(0.15f, 0.500f, 0.17f, 0.720f, 0.26f)
    limb(0.17f, 0.720f, 0.16f, 0.930f, 0.17f)

    // Neck.
    limb(0f, 0.115f, 0f, 0.160f, 0.12f)

    // Torso: shoulders taper to the waist, then flare at the hips.
    val torso = Path().apply {
        moveTo(x(0f), y(0.140f))
        quadraticTo(x(-0.20f), y(0.150f), x(-0.32f), y(0.175f))
        quadraticTo(x(-0.36f), y(0.215f), x(-0.30f), y(0.320f))
        quadraticTo(x(-0.24f), y(0.400f), x(-0.20f), y(0.445f))
        lineTo(x(-0.27f), y(0.520f))
        lineTo(x(0.27f), y(0.520f))
        lineTo(x(0.20f), y(0.445f))
        quadraticTo(x(0.24f), y(0.400f), x(0.30f), y(0.320f))
        quadraticTo(x(0.36f), y(0.215f), x(0.32f), y(0.175f))
        quadraticTo(x(0.20f), y(0.150f), x(0f), y(0.140f))
        close()
    }
    drawPath(torso, base)
    drawPath(torso, edge, style = Stroke(width = 1.dp.toPx()))

    // Head.
    val headRadius = 0.058f * h
    val headCenter = Offset(x(0f), y(0.078f))
    drawCircle(color = base, radius = headRadius, center = headCenter)
    drawCircle(
        color = edge,
        radius = headRadius,
        center = headCenter,
        style = Stroke(width = 1.dp.toPx())
    )

    // Feet.
    limb(-0.17f, 0.955f, -0.17f, 0.978f, 0.10f)
    limb(0.17f, 0.955f, 0.17f, 0.978f, 0.10f)

    // A single seam separates front from back without adding fake anatomy.
    drawLine(
        color = edge,
        start = Offset(x(0f), y(0.20f)),
        end = Offset(x(0f), if (front) 0.440f * h else 0.510f * h),
        strokeWidth = 0.8.dp.toPx()
    )
}

/** Accessible muscle selector - the list equivalent of tapping the map. */
@Composable
fun BodyMuscleChips(
    view: AnatomyView,
    selected: MuscleGroup,
    onSelect: (MuscleGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AnatomyRegions.groups(view).forEach { muscle ->
            val isSelected = muscle == selected
            Text(
                text = muscle.label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else BodyIntelPalette.InkSoft,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.CardSoft)
                    .selectable(selected = isSelected, onClick = { onSelect(muscle) })
                    .padding(horizontal = 11.dp, vertical = 6.dp)
            )
        }
    }
}

/** Region lookup used by the muscle detail sheet. */
internal fun regionsForMuscle(view: AnatomyView, muscle: MuscleGroup): List<AnatomyRegion> =
    AnatomyRegions.regionsFor(view, muscle)
