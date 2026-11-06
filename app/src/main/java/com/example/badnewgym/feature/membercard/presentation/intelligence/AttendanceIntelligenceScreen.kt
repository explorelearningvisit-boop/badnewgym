package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceMonthRecord
import com.example.badnewgym.feature.membercard.presentation.components.attendanceMonthHistory

/** Attendance tab: month calendar with status badges, vertical legend, 12-month trend chart and footer stats. */
@Composable
fun AttendanceIntelligenceScreen(
    data: MemberCardData,
    monthRecords: List<AttendanceMonthRecord> = attendanceMonthHistory(data.attendance.percent),
    paidDay: Int? = null,
    dueDay: Int? = null,
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val attendance = data.attendance
    val nonNull = monthRecords.mapNotNull { record -> record.percent?.let { record to it } }
    val best = nonNull.maxByOrNull { it.second }?.first
    val min = nonNull.minByOrNull { it.second }?.first
    val average = if (nonNull.isNotEmpty()) nonNull.sumOf { it.second } / nonNull.size else 0
    val tracked = nonNull.size

    Column(modifier = modifier.fillMaxWidth()) {
        IntelligenceSectionCard(
            title = "Attendance",
            icon = Icons.Filled.EventAvailable,
            trailing = { IntelligencePillOutlined("${attendance.monthLabel} ▾") }
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(
                    modifier = Modifier.width(112.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "${attendance.percent}%",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = c.accent,
                        letterSpacing = (-1f).sp
                    )
                    Text(
                        "${attendance.attendedDays} / ${attendance.totalDays} Days",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = c.ink
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    IntelligenceLegendRow(c.good, "Present", "${attendance.presentCount}")
                    IntelligenceLegendRow(c.warn, "Late", "${attendance.lateCount}")
                    IntelligenceLegendRow(c.bad, "Absent", "${attendance.absentCount}")
                    IntelligenceLegendRow(Color(0xFFCBD5E1), "Holiday", "${attendance.holidayCount}")
                }
                Spacer(modifier = Modifier.width(10.dp))
                CalendarGrid(
                    days = attendance.calendarDays,
                    paidDay = paidDay,
                    dueDay = dueDay,
                    onDayClick = onDayClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val measurer = rememberTextMeasurer()
        IntelligenceSectionCard(
            title = "Attendance Trend",
            icon = Icons.Filled.Insights,
            subtitle = "Nov 2025 – Oct 2026",
            trailing = { IntelligencePillOutlined("12 Months ▾") }
        ) {
            AttendanceTrendChart(records = monthRecords, measurer = measurer)
        }
        Spacer(modifier = Modifier.height(6.dp))
        IntelligenceNote(
            text = "Mar 2026 drop (58%): Holi holidays & annual exam break",
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(7.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatFooter(Icons.Filled.EmojiEvents, Color(0xFFF59E0B), "Best", "${best?.percent ?: 0}%", "(${best?.label ?: "--"})", c)
            StatFooter(Icons.Filled.BarChart, c.accentDeep, "Average", "$average%", "", c)
            StatFooter(Icons.Filled.TrendingDown, c.bad, "Min", "${min?.percent ?: 0}%", "(${min?.label ?: "--"})", c)
            StatFooter(Icons.Filled.CalendarMonth, c.accent, "Tracked", "$tracked/12", "mo", c)
        }
    }
}

/** Mon-first month grid with solid status fills plus paid/due badges. */
@Composable
private fun CalendarGrid(
    days: List<CalendarDay>,
    paidDay: Int?,
    dueDay: Int?,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val firstIndex = days.firstOrNull { it.dayOfMonth == 1 }?.dayOfWeekIndex ?: 0
    val monFirstOffset = ((firstIndex - 1) % 7 + 7) % 7
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { label ->
                Text(
                    label,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = c.accentDeep,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        val weeks = ((monFirstOffset + days.size + 6) / 7).coerceAtLeast(1)
        repeat(weeks) { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(7) { col ->
                    val cell = week * 7 + col
                    val index = cell - monFirstOffset
                    if (cell < monFirstOffset || index >= days.size) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        DayCell(days[index], paidDay == days[index].dayOfMonth, dueDay == days[index].dayOfMonth, onDayClick)
                    }
                }
            }
        }
    }
}

/** One 34dp calendar cell: solid status colour, optional paid ✓ / due ₹ badge. */
@Composable
private fun RowScope.DayCell(
    day: CalendarDay,
    isPaid: Boolean,
    isDue: Boolean,
    onDayClick: (CalendarDay) -> Unit
) {
    val c = intelligenceColors()
    val bg = when (day.status) {
        DayAttendanceStatus.PRESENT -> c.good
        DayAttendanceStatus.LATE -> c.warn
        DayAttendanceStatus.ABSENT -> c.bad
        DayAttendanceStatus.HOLIDAY -> Color(0xFFE2E8F0)
    }
    val fg = if (day.status == DayAttendanceStatus.HOLIDAY) Color(0xFF64748B) else Color.White
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(
                width = if (isDue) 1.5.dp else 0.dp,
                color = if (isDue) c.bad else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
            .clickable { onDayClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Text("${day.dayOfMonth}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = fg)
        if (isDue) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(c.bad),
                contentAlignment = Alignment.Center
            ) {
                Text("₹", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else if (isPaid) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(13.dp)
                    .clip(CircleShape)
                    .background(c.accentDeep),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(8.dp))
            }
        }
    }
}


/** 12-month attendance line/area chart with peak, drop and current callout bubbles. */
@Composable
private fun AttendanceTrendChart(
    records: List<AttendanceMonthRecord>,
    measurer: TextMeasurer
) {
    val c = intelligenceColors()
    Canvas(modifier = Modifier.fillMaxWidth().height(142.dp)) {
        val labelLeft = 34.dp.toPx()
        val plotTop = 48.dp.toPx()
        val plotBottom = size.height - 22.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val slotW = (size.width - labelLeft) / records.size
        val axisStyle = TextStyle(fontSize = 8.sp, color = c.muted, fontWeight = FontWeight.SemiBold)

        listOf(100, 75, 50, 25, 0).forEachIndexed { i, value ->
            val y = plotTop + i * plotH / 4f
            drawLine(
                color = c.ink.copy(alpha = 0.06f),
                start = Offset(labelLeft, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            drawText(measurer, "$value%", Offset(0f, y - 4f), style = axisStyle)
        }
        records.forEachIndexed { i, record ->
            val mx = labelLeft + slotW * (i + 0.5f)
            val text = record.label.take(3)
            val tw = measurer.measure(text, axisStyle, constraints = Constraints(maxWidth = 40)).size.width
            drawText(measurer, text, Offset(mx - tw / 2f, plotBottom + 6f), style = axisStyle)
        }

        val points = records.mapIndexedNotNull { i, record ->
            record.percent?.let {
                Triple(Offset(labelLeft + slotW * (i + 0.5f), plotBottom - it / 100f * plotH), record, i)
            }
        }
        if (points.isEmpty()) return@Canvas

        val dash = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
        points.windowed(2, 1).forEach { pair ->
            val a = pair[0]
            val b = pair[1]
            if (b.third - a.third > 1) {
                drawLine(
                    color = Color(0xFF94A3B8),
                    start = a.first,
                    end = b.first,
                    strokeWidth = 1.4f,
                    pathEffect = dash
                )
            } else {
                drawPath(
                    Path().apply {
                        moveTo(a.first.x, a.first.y)
                        lineTo(b.first.x, b.first.y)
                    },
                    c.accent,
                    style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                )
            }
        }
        val area = Path().apply {
            moveTo(points.first().first.x, plotBottom)
            points.forEach { lineTo(it.first.x, it.first.y) }
            lineTo(points.last().first.x, plotBottom)
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
        points.forEach { drawCircle(center = it.first, radius = 2.5f, color = c.accent) }

        val peak = points.maxByOrNull { it.second.percent ?: 0 }
        val drop = points.minByOrNull { it.second.percent ?: Int.MAX_VALUE }
        val current = points.last()
        peak?.let { trendBubble(it.first.x, it.first.y, it.second, c.accent, crown = true, measurer = measurer) }
        if (drop != null && drop !== peak && (drop.second.percent ?: 100) < 75) {
            trendBubble(drop.first.x, drop.first.y, drop.second, c.bad, crown = false, measurer = measurer)
        }
        if (current !== peak && current !== drop) {
            trendBubble(current.first.x, current.first.y, current.second, c.accent, crown = false, measurer = measurer)
        }
    }
}

/** Rounded percentage callout drawn above a trend point, flipped below when the top is tight. */
private fun DrawScope.trendBubble(
    x: Float,
    y: Float,
    record: AttendanceMonthRecord,
    background: Color,
    crown: Boolean,
    measurer: TextMeasurer
) {
    val boxW = 34.dp.toPx()
    val boxH = 30.dp.toPx()
    val gap = 9.dp.toPx()
    val margin = 2.dp.toPx()
    val crownH = 8.dp.toPx()
    val center = x.coerceIn(boxW / 2f + margin, size.width - boxW / 2f - margin)
    val above = y - boxH - gap
    val fitsAbove = above - (if (crown) crownH + 2.dp.toPx() else 0f) >= 0f
    val top = if (fitsAbove) above else (y + gap).coerceAtMost(size.height - boxH - margin)
    if (crown) {
        val cw = 11.dp.toPx()
        val baseY = if (fitsAbove) top - 1.dp.toPx() else top + boxH + crownH
        val crownPath = Path().apply {
            moveTo(center - cw / 2f, baseY)
            lineTo(center - cw / 2f, baseY - crownH * 0.85f)
            lineTo(center - cw / 4f, baseY - crownH * 0.35f)
            lineTo(center, baseY - crownH)
            lineTo(center + cw / 4f, baseY - crownH * 0.35f)
            lineTo(center + cw / 2f, baseY - crownH * 0.85f)
            lineTo(center + cw / 2f, baseY)
            close()
        }
        drawPath(crownPath, Color(0xFFF2A81D))
    }
    drawRoundRect(
        color = background,
        topLeft = Offset(center - boxW / 2f, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(9.dp.toPx())
    )
    val percent = "${record.percent ?: 0}%"
    val pStyle = TextStyle(fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
    val pw = measurer.measure(percent, pStyle).size.width
    drawText(measurer, percent, Offset(center - pw / 2f, top + 3.dp.toPx()), style = pStyle)
    val days = "${record.attendedDays}d"
    val dStyle = TextStyle(fontSize = 7.5f.sp, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.SemiBold)
    val dw = measurer.measure(days, dStyle).size.width
    drawText(measurer, days, Offset(center - dw / 2f, top + 18.dp.toPx()), style = dStyle)
}

/** Compact white footer stat tile: tinted icon, label, value and inline sub-label. */
@Composable
private fun RowScope.StatFooter(icon: ImageVector, tint: Color, label: String, value: String, sub: String, c: IntelligenceColors) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = tint)
            if (sub.isNotBlank()) {
                Text(sub, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
            }
        }
    }
}
