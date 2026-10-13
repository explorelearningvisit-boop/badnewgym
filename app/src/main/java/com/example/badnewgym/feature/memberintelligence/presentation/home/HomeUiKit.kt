package com.example.badnewgym.feature.memberintelligence.presentation.home

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors

/** Light glass card. Every Home widget is one of these. */
@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = MiColors.Surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MiColors.Border),
        shadowElevation = 3.dp
    ) {
        val inner: @Composable () -> Unit = {
            Column(Modifier.padding(padding)) { content() }
        }
        if (onClick != null) {
            Box(Modifier.clickable(onClick = onClick)) { inner() }
        } else {
            inner()
        }
    }
}

/** Widget title row with optional trailing affordance (e.g. "View All >"). */
@Composable
fun WidgetTitle(
    title: String,
    accent: String? = null,
    accentColor: Color = MiColors.Blue,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
        if (accent != null) {
            Spacer(Modifier.width(4.dp))
            Text(accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = accentColor)
        }
        Spacer(Modifier.weight(1f))
        trailing?.invoke()
    }
}

@Composable
fun ViewAllChip(onClick: () -> Unit, small: Boolean = false) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MiColors.BlueSoft)
            .clickable(onClick = onClick)
            .padding(horizontal = if (small) 8.dp else 10.dp, vertical = if (small) 4.dp else 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("View All", fontSize = if (small) 10.sp else 11.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue)
        Spacer(Modifier.width(2.dp))
        Text(">", fontSize = if (small) 10.sp else 11.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue)
    }
}

@Composable
fun StatChip(
    label: String,
    value: String,
    valueColor: Color = MiColors.TextPrimary,
    modifier: Modifier = Modifier,
    dense: Boolean = false,
    glyph: String? = null,
    glyphBg: Color = MiColors.Blue
) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(horizontal = if (dense) 8.dp else 10.dp, vertical = if (dense) 4.dp else 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (glyph != null) {
            Box(
                Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(glyphBg),
                contentAlignment = Alignment.Center
            ) { Text(glyph, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White, maxLines = 1) }
            Spacer(Modifier.width(7.dp))
        }
        Column {
            Text(value, fontSize = if (dense) 12.sp else 14.sp, fontWeight = FontWeight.ExtraBold, color = valueColor, maxLines = 1)
            Text(label, fontSize = if (dense) 9.sp else 10.5.sp, fontWeight = FontWeight.Medium, color = MiColors.TextSecondary, maxLines = 1)
        }
    }
}

@Composable
fun Pill(
    text: String,
    bg: Color,
    fg: Color = Color.White,
    fontSize: Int = 11
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = fontSize.sp, fontWeight = FontWeight.Bold, color = fg, maxLines = 1)
    }
}

/** Compact trend sparkline drawn from real readings - never decorative. */
@Composable
fun Sparkline(
    values: List<Double>,
    lineColor: Color,
    modifier: Modifier = Modifier.height(30.dp).fillMaxWidth()
) {
    if (values.size < 2) {
        Box(modifier)
        return
    }
    Canvas(modifier) {
        val lo = values.min().toFloat()
        val hi = values.max().toFloat()
        val span = (hi - lo).takeIf { it > 0.0001f } ?: 1f
        val pad = 3.dp.toPx()
        val stepX = size.width / (values.size - 1)
        val path = Path()
        fun yOf(v: Double): Float {
            val f = v.toFloat()
            return size.height - ((f - lo) / span * (size.height - pad * 2) + pad)
        }
        values.forEachIndexed { i, v ->
            val x = i * stepX
            val y = yOf(v)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, lineColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
        values.forEachIndexed { i, v ->
            drawCircle(
                lineColor,
                radius = 2.2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(i * stepX, yOf(v))
            )
        }
    }
}

/** Key-value row used inside cards. */
@Composable
fun KvRow(label: String, value: String, valueColor: Color = MiColors.TextPrimary, dense: Boolean = false) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = if (dense) 10.5.sp else 11.5.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
        Text(value, fontSize = if (dense) 11.sp else 12.sp, fontWeight = FontWeight.Bold, color = valueColor, maxLines = 1)
    }
}

@Composable
fun EmptyWidgetLine(text: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 12.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AvatarDot(initial: String, bg: Color, size: Dp = 46.dp) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initial.take(1).uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}

@Composable
fun WidgetGap() = Spacer(Modifier.height(8.dp))

@Composable
fun HomeColumnScope(): Arrangement.Vertical = Arrangement.spacedBy(8.dp)
