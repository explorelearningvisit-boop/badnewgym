package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics.CalendarCell
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics.DayState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Chart toolkit for the Body tab.
 *
 * Every chart is drawn from primitives so there is no bitmap scaling, no asset
 * loading and no chart library to keep in sync with the theme. All of them read
 * pre-computed domain values - a chart never decides what a number means.
 */

// ------------------------------------------------------------------ kpi strip

data class Kpi(
    val label: String,
    val value: String,
    val caption: String? = null,
    val tone: KpiTone = KpiTone.NEUTRAL
)

enum class KpiTone { NEUTRAL, POSITIVE, WARNING }

@Composable
fun KpiStrip(items: List<Kpi>, modifier: Modifier = Modifier) {
    if (items.isEmpty()) return
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEachIndexed { index, kpi ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(BodyIntelPalette.Hairline)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = kpi.value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = when (kpi.tone) {
                        KpiTone.POSITIVE -> BodyIntelPalette.EmeraldDeep
                        KpiTone.WARNING -> BodyIntelPalette.Amber
                        KpiTone.NEUTRAL -> BodyIntelPalette.Ink
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = kpi.label,
                    fontSize = 8.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Muted,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (kpi.caption != null) {
                    Text(
                        text = kpi.caption,
                        fontSize = 7.5.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = BodyIntelPalette.Faint,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------- ring chart

@Composable
fun RingChart(
    percent: Int,
    centerValue: String,
    centerLabel: String,
    modifier: Modifier = Modifier,
    size: Dp = 104.dp,
    stroke: Dp = 11.dp,
    color: Color = BodyIntelPalette.Emerald,
    caption: String? = null
) {
    val clamped = percent.coerceIn(0, 100)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            val strokePx = stroke.toPx()
            val inset = strokePx / 2f
            drawArc(
                color = BodyIntelPalette.Hairline,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - strokePx, this.size.height - strokePx),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(color.copy(alpha = 0.55f), color, BodyIntelPalette.EmeraldDeep, color)
                ),
                startAngle = -90f,
                sweepAngle = 360f * clamped / 100f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - strokePx, this.size.height - strokePx),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = centerValue,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink
            )
            Text(
                text = centerLabel,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = BodyIntelPalette.Muted,
                textAlign = TextAlign.Center
            )
            if (caption != null) {
                Text(
                    text = caption,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.EmeraldDeep,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ---------------------------------------------------------------- donut chart

data class DonutSegment(val label: String, val percent: Int, val color: Color)

@Composable
fun DonutChart(
    segments: List<DonutSegment>,
    centerTop: String,
    centerBottom: String,
    modifier: Modifier = Modifier,
    size: Dp = 108.dp,
    stroke: Dp = 18.dp
) {
    val total = segments.sumOf { it.percent }.coerceAtLeast(1)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            val strokePx = stroke.toPx()
            val inset = strokePx / 2f
            var start = -90f
            segments.forEach { segment ->
                val sweep = 360f * segment.percent / total
                drawArc(
                    color = segment.color,
                    startAngle = start + 1.5f,
                    sweepAngle = (sweep - 3f).coerceAtLeast(0.5f),
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(this.size.width - strokePx, this.size.height - strokePx),
                    style = Stroke(width = strokePx, cap = StrokeCap.Butt)
                )
                start += sweep
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = centerTop,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink
            )
            Text(
                text = centerBottom,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                color = BodyIntelPalette.Muted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DonutLegend(segments: List<DonutSegment>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        segments.forEach { segment ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(segment.color)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = segment.label,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.InkSoft,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${segment.percent}%",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Ink
                )
            }
        }
    }
}

// ------------------------------------------------------------------ line plot

data class TrendLine(
    val label: String,
    val points: List<Float>,
    val color: Color
)

/**
 * Multi-series line chart. Series are normalised independently only when
 * [sharedScale] is false, so unrelated units never imply a false comparison.
 */
@Composable
fun LineTrendChart(
    series: List<TrendLine>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    sharedScale: Boolean = true,
    fillFirst: Boolean = true,
    showDots: Boolean = true
) {
    val visible = series.filter { it.points.size >= 2 }
    if (visible.isEmpty()) return

    val all = visible.flatMap { it.points }
    val max = (all.maxOrNull() ?: 1f).takeIf { it > 0f } ?: 1f
    val min = all.minOrNull() ?: 0f
    val span = (max - min).takeIf { it > 0.0001f } ?: 1f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Horizontal guides make a value readable without a tooltip.
        repeat(4) { index ->
            val y = size.height * index / 3f
            drawLine(
                color = BodyIntelPalette.Hairline,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f))
            )
        }

        val horizontalPadding = 6f
        val usableWidth = size.width - horizontalPadding * 2
        val usableHeight = size.height - 8f

        visible.forEachIndexed { seriesIndex, line ->
            val localMax: Float
            val localMin: Float
            if (sharedScale) {
                localMax = max; localMin = min
            } else {
                localMax = line.points.maxOrNull() ?: 1f
                localMin = line.points.minOrNull() ?: 0f
            }
            val localSpan = (localMax - localMin).takeIf { it > 0.0001f } ?: 1f

            fun pointAt(index: Int): Offset {
                val x = horizontalPadding + usableWidth * index / (line.points.size - 1).coerceAtLeast(1)
                val ratio = (line.points[index] - localMin) / localSpan
                val y = usableHeight - (ratio * usableHeight) + 4f
                return Offset(x, y)
            }

            val path = Path()
            val points = line.points.indices.map { pointAt(it) }
            points.forEachIndexed { index, point ->
                if (index == 0) path.moveTo(point.x, point.y)
                else {
                    val prev = points[index - 1]
                    val midX = (prev.x + point.x) / 2f
                    path.cubicTo(midX, prev.y, midX, point.y, point.x, point.y)
                }
            }

            if (fillFirst && seriesIndex == 0) {
                val fill = Path().apply {
                    addPath(path)
                    lineTo(points.last().x, size.height)
                    lineTo(points.first().x, size.height)
                    close()
                }
                drawPath(
                    path = fill,
                    brush = Brush.verticalGradient(
                        listOf(line.color.copy(alpha = 0.20f), line.color.copy(alpha = 0.01f))
                    )
                )
            }

            drawPath(
                path = path,
                color = line.color,
                style = Stroke(width = if (seriesIndex == 0) 2.6f.dp.toPx() else 1.8f.dp.toPx(), cap = StrokeCap.Round)
            )

            if (showDots) {
                points.forEach { point ->
                    drawCircle(color = Color.White, radius = 2.6f.dp.toPx(), center = point)
                    drawCircle(color = line.color, radius = 1.7f.dp.toPx(), center = point)
                }
            }
        }
    }

    if (labels.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = BodyIntelPalette.Faint,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ChartLegend(series: List<TrendLine>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        series.forEach { line ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(line.color)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = line.label,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Muted
                )
            }
        }
    }
}

// ------------------------------------------------------------------- bar rows

data class BarRow(
    val label: String,
    val percent: Int,
    val valueLabel: String,
    val trendPercent: Int? = null,
    val color: Color = BodyIntelPalette.Emerald
)

@Composable
fun BarRowList(rows: List<BarRow>, modifier: Modifier = Modifier, showTrend: Boolean = true) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        rows.forEach { row ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = row.label,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.InkSoft,
                    modifier = Modifier.width(58.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(BodyIntelPalette.CardSoft)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (row.percent / 100f).coerceIn(0.02f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(row.color.copy(alpha = 0.75f), row.color)
                                )
                            )
                    )
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    text = row.valueLabel,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Ink,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
                if (showTrend && row.trendPercent != null) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = when {
                            row.trendPercent > 0 -> "+${row.trendPercent}%"
                            row.trendPercent < 0 -> "${row.trendPercent}%"
                            else -> "0%"
                        },
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (row.trendPercent >= 0) BodyIntelPalette.EmeraldDeep
                        else BodyIntelPalette.Rose,
                        modifier = Modifier.width(26.dp),
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------- week markers

data class DayMark(val label: String, val trained: Boolean, val missed: Boolean)

@Composable
fun WeekFrequencyStrip(marks: List<DayMark>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        marks.forEach { mark ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                mark.trained -> BodyIntelPalette.Emerald
                                mark.missed -> BodyIntelPalette.RoseWash
                                else -> BodyIntelPalette.CardSoft
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = when {
                                mark.trained -> BodyIntelPalette.EmeraldDeep
                                mark.missed -> BodyIntelPalette.Rose.copy(alpha = 0.35f)
                                else -> BodyIntelPalette.Hairline
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (mark.trained) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    } else if (mark.missed) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = BodyIntelPalette.Rose.copy(alpha = 0.6f),
                            modifier = Modifier.size(9.dp)
                        )
                    }
                }
                Text(
                    text = mark.label,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Muted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// ------------------------------------------------------------- month calendar

@Composable
fun MonthCalendar(
    cells: List<CalendarCell>,
    modifier: Modifier = Modifier,
    startOffset: Int = 0,
    onDayClick: (CalendarCell) -> Unit = {}
) {
    val weekLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekLabels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.Faint,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // Compose the month into weeks, leaving the real leading offset blank.
        val ordered = cells.sortedBy { it.day }
        if (ordered.isEmpty()) return@Column
        val padded = List(startOffset.coerceIn(0, 6)) {
            CalendarCell(0, DayState.REST, false)
        } + ordered
        padded.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                week.forEach { cell ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                when {
                                    cell.day == 0 -> Color.Transparent
                                    cell.state == DayState.DONE -> BodyIntelPalette.EmeraldWash
                                    cell.state == DayState.MISSED -> BodyIntelPalette.RoseWash
                                    cell.state == DayState.FUTURE -> Color.Transparent
                                    else -> BodyIntelPalette.CardInset
                                }
                            )
                            .border(
                                width = if (cell.isToday) 1.5.dp else 0.75.dp,
                                color = when {
                                    cell.day == 0 -> Color.Transparent
                                    cell.isToday -> BodyIntelPalette.EmeraldDeep
                                    cell.state == DayState.FUTURE -> BodyIntelPalette.Hairline
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(7.dp)
                            )
                            .clickable(enabled = cell.day != 0) { onDayClick(cell) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (cell.day != 0) {
                            Text(
                                text = cell.day.toString(),
                                fontSize = 9.sp,
                                fontWeight = if (cell.isToday) FontWeight.Black else FontWeight.SemiBold,
                                color = when (cell.state) {
                                    DayState.DONE -> BodyIntelPalette.EmeraldDeep
                                    DayState.MISSED -> BodyIntelPalette.Rose
                                    DayState.FUTURE -> BodyIntelPalette.Faint
                                    else -> BodyIntelPalette.Muted
                                }
                            )
                        }
                    }
                }
                // Pad the final short week so cells keep their square size.
                repeat(7 - week.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

// -------------------------------------------------------------- progress rows

@Composable
fun ProgressRow(
    label: String,
    percent: Int,
    valueLabel: String,
    modifier: Modifier = Modifier,
    warn: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.InkSoft,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = valueLabel,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Black,
                color = if (warn) BodyIntelPalette.Amber else BodyIntelPalette.EmeraldDeep
            )
        }
        Spacer(Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.CardSoft)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (percent / 100f).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (warn) BodyIntelPalette.Amber else BodyIntelPalette.Emerald
                    )
            )
        }
    }
}

// ------------------------------------------------------------- segmented tabs

@Composable
fun SegmentedTabs(
    options: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(BodyIntelPalette.CardInset)
            .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(50))
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) BodyIntelPalette.Emerald else Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = option,
                    fontSize = 8.5.sp,
                    fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                    color = if (selected) Color.White else BodyIntelPalette.Muted
                )
            }
        }
    }
}

// -------------------------------------------------------------- footer actions

/**
 * The three-button row every reference section closes with. Actions are text
 * buttons rather than filled CTAs so three of them can share a phone width
 * without one dominating the other two.
 */
@Composable
fun FooterActions(
    labels: List<String>,
    modifier: Modifier = Modifier,
    onAction: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val primary = index == 0
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (primary) BodyIntelPalette.EmeraldWash else BodyIntelPalette.Card)
                    .border(
                        width = 1.dp,
                        color = if (primary) BodyIntelPalette.EmeraldEdge else BodyIntelPalette.HairlineStrong,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .clickable { onAction(index) }
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 8.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (primary) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.InkSoft,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ------------------------------------------------------------------- bullets

@Composable
fun BulletLine(text: String, ok: Boolean?, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(5.5.dp)
                .clip(CircleShape)
                .background(
                    when (ok) {
                        true -> BodyIntelPalette.Emerald
                        false -> BodyIntelPalette.Amber
                        null -> BodyIntelPalette.Faint
                    }
                )
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 9.5.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.InkSoft
        )
    }
}

// ----------------------------------------------------------------- table rows

@Composable
fun TableRow(
    cells: List<String>,
    weights: List<Float>,
    modifier: Modifier = Modifier,
    header: Boolean = false,
    highlight: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(
                when {
                    header -> BodyIntelPalette.CardInset
                    highlight -> BodyIntelPalette.EmeraldWash.copy(alpha = 0.55f)
                    else -> Color.Transparent
                }
            )
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        cells.forEachIndexed { index, cell ->
            val last = index == cells.lastIndex
            Text(
                text = cell,
                fontSize = if (header) 8.sp else 9.sp,
                fontWeight = if (header) FontWeight.Bold else FontWeight.SemiBold,
                color = when {
                    header -> BodyIntelPalette.Muted
                    last && highlight -> BodyIntelPalette.EmeraldDeep
                    else -> BodyIntelPalette.InkSoft
                },
                textAlign = if (last) TextAlign.End else TextAlign.Start,
                modifier = Modifier.weight(weights.getOrElse(index) { 1f }),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DividerLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BodyIntelPalette.Hairline)
    )
}

// ------------------------------------------------------------------ test tile

@Composable
fun TestTile(
    label: String,
    value: String,
    changePercent: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BodyIntelPalette.CardInset)
            .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (changePercent != null) {
            Text(
                text = if (changePercent >= 0) "▲ ${changePercent}% vs prev" else "▼ ${-changePercent}% vs prev",
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                color = if (changePercent >= 0) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Rose,
                maxLines = 1
            )
        }
    }
}

// ------------------------------------------------------------- polar geometry

/** Shared helper so ring and donut cannot diverge in their angle maths. */
internal fun polar(center: Offset, radius: Float, degrees: Float): Offset {
    val radians = (degrees - 90f) * PI.toFloat() / 180f
    return Offset(center.x + radius * cos(radians), center.y + radius * sin(radians))
}
