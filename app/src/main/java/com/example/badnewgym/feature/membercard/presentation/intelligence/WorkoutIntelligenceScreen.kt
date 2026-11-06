package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

/** Workout tab: daily minute trend with callout bubbles, six stat tiles and the monthly hours pattern. */
@Composable
fun WorkoutIntelligenceScreen(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val measurer = rememberTextMeasurer()
    val points = IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)
    val totalMinutes = points.sumOf { it.minutes }
    val activeCount = points.count { it.minutes > 0 }
    val maxMinutes = points.maxOfOrNull { it.minutes } ?: 0
    val minMinutes = points.filter { it.minutes > 0 }.minOfOrNull { it.minutes } ?: 0
    val avgDay = totalMinutes / 31
    val consistency = activeCount * 100 / 31
    val totalHr = String.format(Locale.US, "%.1f", totalMinutes / 60f)
    val monthPill = shortMonth(data.workoutTrend.monthLabel)

    Column(modifier = modifier.fillMaxWidth()) {
        IntelligenceSectionCard(
            title = "Workout Analytics",
            icon = Icons.Filled.FitnessCenter,
            subtitle = "Daily Workout Trend • $monthPill",
            trailing = { IntelligencePillOutlined("$monthPill ▾") }
        ) {
            DailyMinutesChart(points = points, measurer = measurer)
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile("Avg/day", "$avgDay", " min", c.ink, Modifier.weight(1f))
                StatTile("Best", "$maxMinutes", " min", c.ink, Modifier.weight(1f))
                StatTile("Lowest", "$minMinutes", " min", c.bad, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile("Total", totalHr, " hr", c.ink, Modifier.weight(1f))
                StatTile("Active", "$activeCount/31", " d", c.ink, Modifier.weight(1f))
                StatTile("Consistency", "$consistency", " %", c.ink, Modifier.weight(1f))
            }
        }

        IntelligenceSectionCard(
            title = "Monthly Pattern",
            icon = Icons.Filled.BarChart,
            subtitle = "(This Year)",
            trailing = { IntelligencePillOutlined("⏱ Total: ${IntelligenceSeries.MONTHLY_PATTERN_TOTAL_LABEL}") }
        ) {
            MonthlyBarsChart(pattern = IntelligenceSeries.workoutMonthlyPattern(), measurer = measurer)
        }
    }
}

/** Renders "October 2026" as the compact "Oct 2026" label used by the reference pills. */
private fun shortMonth(label: String): String {
    val parts = label.trim().split(" ")
    return if (parts.size >= 2 && parts[0].length >= 3) "${parts[0].take(3)} ${parts[1]}" else label
}

/** Daily minutes spline-style line with area fill and six reference callout bubbles. */
@Composable
private fun DailyMinutesChart(
    points: List<IntelligenceSeries.WorkoutDailyPoint>,
    measurer: TextMeasurer
) {
    val c = intelligenceColors()
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val plotLeft = 30.dp.toPx()
        val plotRight = size.width - 6.dp.toPx()
        val plotTop = 40.dp.toPx()
        val plotBottom = size.height - 26.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val span = plotRight - plotLeft
        val axis = TextStyle(fontSize = 8.sp, color = c.muted, fontWeight = FontWeight.SemiBold)
        val xOf = { day: Int -> plotLeft + (day - 1) * span / 30f }
        val yOf = { minutes: Int -> plotBottom - minutes / 90f * plotH }

        listOf("90m", "68m", "45m", "23m", "0m").forEachIndexed { i, label ->
            val y = plotTop + i * plotH / 4f
            drawLine(c.ink.copy(alpha = 0.06f), Offset(plotLeft, y), Offset(plotRight, y), 1f)
            drawText(measurer, label, Offset(0f, y - 5.dp.toPx()), style = axis)
        }
        listOf(1, 5, 10, 15, 20, 25, 31).forEach { day ->
            val text = day.toString()
            val tw = measurer.measure(text, axis).size.width
            val labelX = (xOf(day) - tw / 2f).coerceIn(plotLeft - tw, size.width - tw)
            drawText(measurer, text, Offset(labelX, plotBottom + 4.dp.toPx()), style = axis)
        }

        val line = Path().apply {
            points.forEachIndexed { i, p ->
                val pt = Offset(xOf(p.day), yOf(p.minutes))
                if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
            }
        }
        drawPath(line, c.accent, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

        val area = Path().apply {
            moveTo(xOf(points.first().day), plotBottom)
            points.forEach { lineTo(xOf(it.day), yOf(it.minutes)) }
            lineTo(xOf(points.last().day), plotBottom)
            close()
        }
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(c.accent.copy(alpha = 0.24f), c.accent.copy(alpha = 0.0f)),
                startY = plotTop,
                endY = plotBottom
            )
        )

        points.filter { it.minutes > 0 }.forEach { p ->
            val dotColor = if (p.minutes == 74) c.accent.copy(alpha = 0.65f) else c.accent
            drawCircle(dotColor, radius = 1.8.dp.toPx(), center = Offset(xOf(p.day), yOf(p.minutes)))
        }

        var prevRight = 0f
        points.filter { it.label != null }.forEach { p ->
            val naturalX = xOf(p.day)
            val y = yOf(p.minutes)
            val halfW = 17.dp.toPx()
            val x = if (naturalX - halfW < prevRight) prevRight + halfW + 2.dp.toPx()
            else naturalX.coerceAtLeast(36.dp.toPx())
            prevRight = x + halfW
            val background = if (p.isAlert || p.isLow) c.bad else c.accent
            drawLabelBubble(
                text = p.label!!,
                x = x,
                dotY = y,
                background = background,
                crown = p.isPeak,
                measurer = measurer,
                topLimit = 6.dp.toPx()
            )
        }
    }
}

/** Rounded callout pinned above a chart point, flipped below when the top is too tight. */
private fun DrawScope.drawLabelBubble(
    text: String,
    x: Float,
    dotY: Float,
    background: Color,
    crown: Boolean,
    measurer: TextMeasurer,
    topLimit: Float
) {
    val style = TextStyle(fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
    val measured = measurer.measure(text, style)
    val padH = 4.dp.toPx()
    val padV = 2.5.dp.toPx()
    val boxW = measured.size.width + padH * 2f
    val boxH = measured.size.height + padV * 2f
    val gap = 6.dp.toPx()
    val crownH = 8.dp.toPx()
    var top = dotY - boxH - gap
    if (top - (if (crown) crownH + 2.dp.toPx() else 0f) < topLimit) top = dotY + gap
    val left = (x - boxW / 2f).coerceIn(1.dp.toPx(), size.width - boxW - 1.dp.toPx())
    drawRoundRect(
        color = background,
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(7.dp.toPx())
    )
    if (crown) drawCrown(centerX = left + boxW / 2f, baseY = top - 2.dp.toPx())
    drawText(measurer, text, Offset(left + padH, top + padV), style = style)
}

/** Jan–Oct training hours as rounded-top gradient bars with gold/pink peak highlights. */
@Composable
private fun MonthlyBarsChart(
    pattern: List<IntelligenceSeries.WorkoutMonthPoint>,
    measurer: TextMeasurer
) {
    val c = intelligenceColors()
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val plotBottom = size.height - 16.dp.toPx()
        val topPad = 34.dp.toPx()
        val maxHr = pattern.maxOfOrNull { it.hours }?.coerceAtLeast(1) ?: 1
        val slotW = size.width / pattern.size
        val barW = slotW * 0.56f
        val monthStyle = TextStyle(fontSize = 9.sp, color = c.ink, fontWeight = FontWeight.SemiBold)
        val hoursStyle = TextStyle(fontSize = 10.sp, color = c.ink, fontWeight = FontWeight.ExtraBold)
        val daysOnStyle = TextStyle(fontSize = 9.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
        val daysOffStyle = TextStyle(fontSize = 9.5.sp, color = c.muted, fontWeight = FontWeight.Bold)

        pattern.forEachIndexed { i, m ->
            val cx = slotW * (i + 0.5f)
            val mw = measurer.measure(m.month, monthStyle).size.width
            drawText(measurer, m.month, Offset(cx - mw / 2f, plotBottom + 4.dp.toPx()), style = monthStyle)
            if (m.hours <= 0) return@forEachIndexed

            val barH = m.hours / maxHr.toFloat() * (plotBottom - topPad)
            val barTop = plotBottom - barH
            val brush = when {
                m.isPeak -> Brush.verticalGradient(listOf(Color(0xFFFBBF24), Color(0xFFD97706)))
                m.isAlert -> Brush.verticalGradient(listOf(Color(0xFFF87171), Color(0xFFDC2626)))
                else -> Brush.verticalGradient(listOf(c.accent, darken(c.accent, 0.8f)))
            }
            val left = cx - barW / 2f
            val rect = Rect(left, barTop, left + barW, plotBottom)
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = rect,
                            topLeft = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                            topRight = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    )
                },
                brush,
                style = Fill
            )

            val hours = "${m.hours}h"
            val hw = measurer.measure(hours, hoursStyle).size.width
            val labelY = barTop - 6.dp.toPx() - measurer.measure(hours, hoursStyle).size.height
            if (m.isPeak) drawCrown(centerX = cx, baseY = labelY - 1.dp.toPx())
            drawText(measurer, hours, Offset(cx - hw / 2f, labelY), style = hoursStyle)

            val days = "${m.days}d"
            if (barH >= 22.dp.toPx()) {
                val dw = measurer.measure(days, daysOnStyle).size.width
                drawText(measurer, days, Offset(cx - dw / 2f, plotBottom - 12.dp.toPx()), style = daysOnStyle)
            } else {
                val dw = measurer.measure(days, daysOffStyle, constraints = Constraints(maxWidth = 44)).size.width
                drawText(measurer, days, Offset(cx - dw / 2f, barTop + 3f), style = daysOffStyle)
            }
        }
    }
}

/** Compact white stat tile: muted label over a bold value with an inline unit. */
@Composable
private fun StatTile(label: String, value: String, unit: String, valueColor: Color, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(14.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
            Text(unit, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        }
    }
}
