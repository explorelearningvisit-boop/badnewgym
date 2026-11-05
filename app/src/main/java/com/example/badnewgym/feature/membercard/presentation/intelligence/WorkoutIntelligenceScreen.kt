package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlin.math.roundToInt

/** Workout tab body: daily training trend chart, two effort stat strips and the yearly monthly pattern columns. */
@Composable
fun WorkoutIntelligenceScreen(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val series = remember(data.workoutTrend.bars) {
        IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)
    }
    val months = remember { IntelligenceSeries.workoutMonthlyPattern() }

    val monthDays = series.size.coerceAtLeast(1)
    val activeDays = series.count { it.minutes > 0 }
    val totalMinutes = series.sumOf { it.minutes }
    val avgPerDay = (totalMinutes.toFloat() / monthDays).roundToInt()
    val bestMinutes = series.maxOfOrNull { it.minutes } ?: 0
    val lowestMinutes = series.filter { it.minutes > 0 }.minOfOrNull { it.minutes } ?: 0
    val totalHrText = "${totalMinutes / 60}.${totalMinutes % 60 * 10 / 60}"
    val consistency = activeDays * 100 / monthDays
    val monthShort = data.workoutTrend.monthLabel.take(3) + " " + data.workoutTrend.monthLabel.takeLast(4)
    val patternHours = months.sumOf { it.hours }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        IntelligenceSectionCard(
            title = "Workout Analytics",
            icon = Icons.Filled.FitnessCenter,
            subtitle = "Daily Workout Trend • $monthShort",
            trailing = { MonthPill("$monthShort ▾", onClick) }
        ) {
            WorkoutDailyChart(series)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WorkoutStatTile("Avg/day", "$avgPerDay", "min", Modifier.weight(1f))
            WorkoutStatTile("Best", "$bestMinutes", "min", Modifier.weight(1f))
            WorkoutStatTile("Lowest", "$lowestMinutes", "min", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WorkoutStatTile("Total", totalHrText, "hr", Modifier.weight(1f))
            WorkoutStatTile("Active", "$activeDays/$monthDays", "d", Modifier.weight(1f))
            WorkoutStatTile("Consistency", "$consistency", "%", Modifier.weight(1f))
        }
        IntelligenceSectionCard(
            title = "Monthly Pattern (This Year)",
            icon = Icons.Filled.BarChart,
            trailing = {
                Text(
                    "Total: $patternHours.0 hr",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = c.accent,
                    maxLines = 1
                )
            }
        ) {
            MonthlyPatternChart(months)
        }
    }
}

/** Canvas line/area chart of the normalised daily minutes with gridlines and per-point labels. */
@Composable
private fun WorkoutDailyChart(series: List<IntelligenceSeries.WorkoutDailyPoint>) {
    val c = intelligenceColors()
    val textMeasurer = rememberTextMeasurer()
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val left = 26.dp.toPx()
        val rightPad = 4.dp.toPx()
        val topPad = 18.dp.toPx()
        val bottomPad = 2.dp.toPx()
        val plotW = (size.width - left - rightPad).coerceAtLeast(1f)
        val plotH = (size.height - topPad - bottomPad).coerceAtLeast(1f)
        val yMax = 90f
        val firstDay = series.firstOrNull()?.day ?: 1
        val lastDay = series.lastOrNull()?.day ?: 31
        val daySpan = (lastDay - firstDay).coerceAtLeast(1)
        val dayX = { day: Int -> left + plotW * (day - firstDay) / daySpan }
        val minY = { minutes: Float -> topPad + plotH * (1f - minutes / yMax) }
        val firstLabeledDay = series.firstOrNull { it.label != null }?.day ?: -1

        listOf(0, 23, 45, 68, 90).forEach { mark ->
            val y = minY(mark.toFloat())
            drawLine(c.faint.copy(alpha = 0.45f), Offset(left, y), Offset(left + plotW, y), 1.dp.toPx())
            val grid = textMeasurer.measure(
                "${mark}m",
                TextStyle(fontSize = 8.sp, fontWeight = FontWeight.Medium, color = c.muted)
            )
            drawText(grid, topLeft = Offset(left - grid.size.width - 4.dp.toPx(), y - grid.size.height / 2f))
        }

        val linePath = Path()
        series.forEach { p ->
            val x = dayX(p.day)
            val y = minY(p.minutes.toFloat())
            if (p.day == firstDay) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }
        val areaPath = Path().apply {
            addPath(linePath)
            lineTo(dayX(lastDay), minY(0f))
            lineTo(dayX(firstDay), minY(0f))
            close()
        }
        drawPath(
            areaPath,
            brush = Brush.verticalGradient(
                listOf(c.accentWash, Color.Transparent),
                startY = topPad,
                endY = topPad + plotH
            )
        )
        drawPath(
            linePath,
            color = c.accent,
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        series.forEach { p ->
            if (p.minutes <= 0) return@forEach
            val x = dayX(p.day)
            val y = minY(p.minutes.toFloat())
            val labelColor = when {
                p.isPeak -> c.warn
                p.isLow || p.isAlert -> c.bad
                p.minutes == 74 -> c.info
                p.day == firstLabeledDay -> c.ink
                else -> c.muted
            }
            val dotColor = when {
                p.isPeak -> c.warn
                p.isLow || p.isAlert -> c.bad
                p.minutes == 74 -> c.info
                else -> c.accent
            }
            val r = (if (p.isPeak) 3.2f else 2.2f).dp.toPx()
            drawCircle(c.cardFill, r + 1.6.dp.toPx(), Offset(x, y))
            drawCircle(dotColor, r, Offset(x, y))
            p.label?.let { text ->
                val layout = textMeasurer.measure(
                    text,
                    TextStyle(
                        fontSize = 8.5.sp,
                        fontWeight = if (p.isPeak) FontWeight.Bold else FontWeight.SemiBold,
                        color = labelColor
                    )
                )
                val lx = (x - layout.size.width / 2f).coerceIn(0f, (size.width - layout.size.width).coerceAtLeast(0f))
                val above = y - r - 3.dp.toPx() - layout.size.height
                drawText(layout, topLeft = Offset(lx, if (above >= 0f) above else y + r + 3.dp.toPx()))
            }
        }
    }
}

/** Small white metric tile with a muted label and a bold value + unit. */
@Composable
private fun WorkoutStatTile(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = c.ink, maxLines = 1)
            Text(unit, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        }
    }
}

/** AccentWash month selector pill shown in the section header trailing slot. */
@Composable
private fun MonthPill(label: String, onClick: () -> Unit) {
    val c = intelligenceColors()
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(c.accentWash)
            .border(1.dp, c.accentSoft, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = c.accentDeep, maxLines = 1)
    }
}

/** Ten Jan..Oct column bars sized by training hours, with peak/drop emphasis and hour/day labels. */
@Composable
private fun MonthlyPatternChart(months: List<IntelligenceSeries.WorkoutMonthPoint>) {
    val c = intelligenceColors()
    val maxHours = months.maxOf { it.hours }.coerceAtLeast(1)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        months.forEach { m ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (m.isPeak) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(c.warn))
                }
                Text("${m.hours}h", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = c.ink, maxLines = 1)
                val barBrush = when {
                    m.isPeak -> Brush.verticalGradient(listOf(Color(0xFFFBBF24), c.warn))
                    m.isDrop -> Brush.verticalGradient(listOf(c.bad, c.bad.copy(alpha = 0.65f)))
                    else -> Brush.verticalGradient(listOf(c.accent, c.accentDeep))
                }
                Box(
                    Modifier
                        .fillMaxWidth(0.62f)
                        .height(72.dp * (m.hours.toFloat() / maxHours))
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                        .background(barBrush)
                )
                Text("${m.days}d", fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
                Text(m.month, fontSize = 8.sp, fontWeight = FontWeight.Medium, color = c.faint, maxLines = 1)
            }
        }
    }
}
