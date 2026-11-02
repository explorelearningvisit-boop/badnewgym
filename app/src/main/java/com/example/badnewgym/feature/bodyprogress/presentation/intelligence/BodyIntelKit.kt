package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.PrivacyCategory

/**
 * Shared furniture for the Body Intelligence tab.
 *
 * These live here rather than inside the dashboard file because both the page and
 * the detail sheets draw them, and a helper that exists twice drifts the first
 * time one of them is edited.
 */

/**
 * Trend shape used by the composition card and the metric detail sheet.
 *
 * It draws the normalised record shape the member's own values produce; it is
 * not a reconstruction of missing days.
 */
@Composable
internal fun TrendShapeChart(
    points: List<Float>,
    highlight: Color,
    height: Int = 90,
    modifier: Modifier = Modifier
) {
    if (points.size < 2) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BodyIntelPalette.CardInset),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "One record - no trend yet",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.Faint
            )
        }
        return
    }

    val min = points.min()
    val max = points.max()
    val span = (max - min).takeIf { it > 0.0001f } ?: 1f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        val path = Path()
        val step = size.width / (points.size - 1)
        points.forEachIndexed { index, value ->
            val ratio = (value - min) / span
            val x = step * index
            val y = size.height - ratio * (size.height * 0.86f) - size.height * 0.07f
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        val fill = Path().apply {
            addPath(path)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path = fill,
            brush = Brush.verticalGradient(
                listOf(highlight.copy(alpha = 0.22f), highlight.copy(alpha = 0.01f))
            )
        )
        drawPath(
            path = path,
            color = highlight,
            style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
        )
        val lastX = size.width
        val lastRatio = (points.last() - min) / span
        val lastY = size.height - lastRatio * (size.height * 0.86f) - size.height * 0.07f
        drawCircle(color = Color.White, radius = 4f.dp.toPx(), center = Offset(lastX, lastY))
        drawCircle(color = highlight, radius = 2.4f.dp.toPx(), center = Offset(lastX, lastY))
    }
}

/**
 * The designed absence of data for sheets: names the gap, and only offers an
 * action when one can actually be taken.
 */
@Composable
internal fun BodyIntelEmpty(
    title: String,
    body: String,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(horizontal = 11.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.InkSoft
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = body,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted
        )
        if (action != null && onAction != null) {
            Spacer(Modifier.height(7.dp))
            Text(
                text = action,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.EmeraldDeep,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BodyIntelPalette.EmeraldWash)
                    .clickable { onAction() }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
internal fun BodyIntelInfo(text: String) {
    Text(
        text = text,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BodyIntelPalette.Muted,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(horizontal = 9.dp, vertical = 7.dp)
    )
}

@Composable
internal fun StatusPill(text: String, color: Color, wash: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(wash)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
internal fun SourceLabel(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = BodyIntelPalette.Faint,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BodyIntelPalette.Hairline)
    )
}

@Composable
internal fun PrivacyBadge(category: PrivacyCategory) {
    val visible = category !in PrivacyCategory.privateDefaults
    StatusPill(
        text = if (visible) "Visible" else "Private",
        color = if (visible) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Muted,
        wash = if (visible) BodyIntelPalette.EmeraldWash else BodyIntelPalette.CardSoft
    )
}

@Composable
internal fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = BodyIntelPalette.Muted
        )
    }
}
