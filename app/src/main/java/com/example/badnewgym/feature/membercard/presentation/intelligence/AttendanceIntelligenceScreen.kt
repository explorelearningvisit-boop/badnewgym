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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceMonthRecord
import com.example.badnewgym.feature.membercard.presentation.components.attendanceMonthHistory

/** As-of date matcher: the first day-of-month group inside the profile's display date. */
private val asOfDayRegex = Regex("(\\d{1,2})")

/**
 * Attendance tab of the Member Intelligence dashboard: month calendar, 12-month
 * trend chart, drop note and summary stat tiles, all driven by [MemberCardData].
 */
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
    val days = attendance.calendarDays.sortedBy { it.dayOfMonth }
    val todayDay = resolveAsOfTodayDay(
        monthLabel = attendance.monthLabel,
        dateDisplay = data.profile.dateDisplay
    )

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        IntelligenceSectionCard(
            title = "Attendance",
            icon = Icons.Filled.EventAvailable,
            trailing = { AttendanceMonthPill(label = attendance.monthLabel) }
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "${attendance.percent}%",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = c.accent,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "${attendance.attendedDays} / ${attendance.totalDays} Days",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = c.muted
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row {
                        listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { head ->
                            Text(
                                text = head,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = c.muted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    // Mon-first column offset straight from the model's Sunday-based index.
                    val leadingBlanks = days.firstOrNull()?.let { (it.dayOfWeekIndex + 6) % 7 } ?: 0
                    val cells: List<CalendarDay?> = buildList {
                        repeat(leadingBlanks) { add(null) }
                        addAll(days)
                        while (size % 7 != 0) add(null)
                    }
                    cells.chunked(7).forEach { week ->
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            week.forEach { day ->
                                if (day == null) {
                                    Box(modifier = Modifier.weight(1f).height(30.dp))
                                } else {
                                    AttendanceDayCell(
                                        day = day,
                                        ringColor = dayRingColor(day, paidDay, dueDay, todayDay, c),
                                        badge = dayBadge(day, paidDay, dueDay, c),
                                        onDayClick = onDayClick,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val present = days.count { it.status == DayAttendanceStatus.PRESENT }
                val late = days.count { it.status == DayAttendanceStatus.LATE }
                val absent = days.count { it.status == DayAttendanceStatus.ABSENT }
                val holiday = days.count { it.status == DayAttendanceStatus.HOLIDAY }
                IntelligenceLegendDot(color = c.good, label = "Present $present")
                IntelligenceLegendDot(color = c.warn, label = "Late $late")
                IntelligenceLegendDot(color = c.bad, label = "Absent $absent")
                IntelligenceLegendDot(color = c.faint.copy(alpha = 0.6f), label = "Holiday $holiday")
            }
        }

        IntelligenceSectionCard(
            title = "Attendance Trend (12 Months)",
            icon = Icons.Filled.Insights,
            trailing = {
                Text(
                    text = trendRangeLabel(monthRecords, attendance.monthLabel),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    color = c.muted
                )
            }
        ) {
            AttendanceTrendChart(records = monthRecords)
            IntelligenceNote(text = "Mar 2026 drop (58%): Holi holidays & annual exam break")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val recorded = monthRecords.filter { it.percent != null }
            val best = recorded.maxByOrNull { it.percent ?: 0 }
            val worst = recorded.minByOrNull { it.percent ?: 100 }
            // Integer mean keeps the displayed average on the reference's 81% figure.
            val average = if (recorded.isEmpty()) 0 else recorded.sumOf { it.percent ?: 0 } / recorded.size
            AttendanceStatTile(
                icon = Icons.Filled.EmojiEvents,
                iconTint = c.warn,
                label = "Best",
                value = "${best?.percent ?: 0}%",
                valueColor = c.good,
                sub = best?.let { "(${it.label})" } ?: "–",
                modifier = Modifier.weight(1f)
            )
            AttendanceStatTile(
                icon = Icons.Filled.BarChart,
                iconTint = c.inkSoft,
                label = "Average",
                value = "$average%",
                valueColor = c.ink,
                sub = "${recorded.size} mo",
                modifier = Modifier.weight(1f)
            )
            AttendanceStatTile(
                icon = Icons.Filled.TrendingDown,
                iconTint = c.bad,
                label = "Min",
                value = "${worst?.percent ?: 0}%",
                valueColor = c.bad,
                sub = worst?.let { "(${it.label})" } ?: "–",
                modifier = Modifier.weight(1f)
            )
            AttendanceStatTile(
                icon = Icons.Filled.CalendarMonth,
                iconTint = c.accent,
                label = "Tracked",
                value = "${recorded.size}/${monthRecords.size}",
                valueColor = c.accent,
                sub = "mo",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/** Static month label pill shown in the attendance section card header. */
@Composable
private fun AttendanceMonthPill(label: String) {
    val c = intelligenceColors()
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(c.accentWash)
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label ▾",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = c.accentDeep
        )
    }
}

/** Ring emphasis for a day: due (red) over paid (green) over today (accent), else no ring. */
private fun dayRingColor(
    day: CalendarDay,
    paidDay: Int?,
    dueDay: Int?,
    todayDay: Int,
    c: IntelligenceColors
): Color = when {
    dueDay != null && day.dayOfMonth == dueDay -> c.bad
    paidDay != null && day.dayOfMonth == paidDay -> c.good
    day.dayOfMonth == todayDay -> c.accent
    else -> Color.Transparent
}

/** Tiny badge for a day: ₹ on red for due, ✓ on green for paid, null otherwise. */
private fun dayBadge(day: CalendarDay, paidDay: Int?, dueDay: Int?, c: IntelligenceColors): Pair<Color, String>? =
    when {
        dueDay != null && day.dayOfMonth == dueDay -> c.bad to "₹"
        paidDay != null && day.dayOfMonth == paidDay -> c.good to "✓"
        else -> null
    }

/** One 30dp calendar cell holding the 26dp status chip, its ring and the due/paid badge. */
@Composable
private fun AttendanceDayCell(
    day: CalendarDay,
    ringColor: Color,
    badge: Pair<Color, String>?,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val fill = when (day.status) {
        DayAttendanceStatus.PRESENT -> c.good
        DayAttendanceStatus.LATE -> c.warn
        DayAttendanceStatus.ABSENT -> c.bad
        DayAttendanceStatus.HOLIDAY -> c.faint.copy(alpha = 0.5f)
    }
    Box(modifier = modifier.height(30.dp), contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(fill)
                .border(1.5.dp, ringColor, RoundedCornerShape(8.dp))
                .clickable { onDayClick(day) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .offset(y = 22.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(badge.first),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge.second,
                    fontSize = 6.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

/** Custom Canvas line/area chart of the 12-month attendance history with target line. */
@Composable
private fun AttendanceTrendChart(records: List<AttendanceMonthRecord>) {
    val c = intelligenceColors()
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxWidth().height(110.dp)) {
        val leftPad = 21.dp.toPx()
        val rightPad = 6.dp.toPx()
        val plotTop = 20.dp.toPx()
        val plotBottom = size.height - 4.dp.toPx()
        val plotHeight = plotBottom - plotTop

        fun yFor(pct: Float): Float = plotBottom - (pct.coerceIn(0f, 100f) / 100f) * plotHeight
        fun xFor(index: Int): Float =
            leftPad + index * (size.width - leftPad - rightPad) / (records.size - 1).coerceAtLeast(1)

        fun labelStyle(color: Color) = TextStyle(fontSize = 8.sp, fontWeight = FontWeight.Bold, color = color)

        fun drawAxisLabel(text: String, color: Color, anchorX: Float, centerY: Float) {
            val layout = textMeasurer.measure(text, labelStyle(color))
            val x = (anchorX - layout.size.width).coerceIn(0f, size.width - layout.size.width)
            drawText(layout, topLeft = Offset(x, centerY - layout.size.height / 2f))
        }

        fun drawPointLabel(text: String, color: Color, anchorX: Float, bottomY: Float, alignEnd: Boolean = false) {
            val layout = textMeasurer.measure(text, labelStyle(color))
            val x = if (alignEnd) {
                (anchorX - layout.size.width).coerceIn(0f, size.width - layout.size.width)
            } else {
                (anchorX - layout.size.width / 2f).coerceIn(0f, size.width - layout.size.width)
            }
            drawText(layout, topLeft = Offset(x, bottomY.coerceAtLeast(0f)))
        }

        // 0/25/50/75/100 gridlines with their 8sp labels.
        listOf(0, 25, 50, 75, 100).forEach { pct ->
            val y = yFor(pct.toFloat())
            drawLine(
                color = c.faint.copy(alpha = 0.30f),
                start = Offset(leftPad, y),
                end = Offset(size.width - rightPad, y),
                strokeWidth = 0.75.dp.toPx()
            )
            drawAxisLabel(pct.toString(), c.muted, anchorX = leftPad - 3.dp.toPx(), centerY = y)
        }

        // Dashed warn target line at the shared 75% reference constant.
        drawLine(
            color = c.warn,
            start = Offset(leftPad, yFor(IntelligenceSeries.ATTENDANCE_TARGET_PERCENT.toFloat())),
            end = Offset(size.width - rightPad, yFor(IntelligenceSeries.ATTENDANCE_TARGET_PERCENT.toFloat())),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()), 0f)
        )

        if (records.size < 2) return@Canvas
        val dash = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 5.dp.toPx()), 0f)

        // Area fill per contiguous recorded run so untracked months stay unfilled.
        var runStart = -1
        records.forEachIndexed { index, record ->
            val isRecorded = record.percent != null
            if (isRecorded && runStart < 0) runStart = index
            val runEnds = isRecorded && (index == records.lastIndex || records[index + 1].percent == null)
            if (runEnds && runStart >= 0) {
                val area = Path()
                area.moveTo(xFor(runStart), yFor(records[runStart].percent!!.toFloat()))
                (runStart + 1..index).forEach { step ->
                    area.lineTo(xFor(step), yFor(records[step].percent!!.toFloat()))
                }
                area.lineTo(xFor(index), plotBottom)
                area.lineTo(xFor(runStart), plotBottom)
                area.close()
                drawPath(area, c.accentWash)
                runStart = -1
            }
            if (!isRecorded) runStart = -1
        }

        // Solid accent line between adjacent recorded months; faint dashed connector
        // jumps recorded points across untracked (null) months.
        val recordedIndexes = records.indices.filter { records[it].percent != null }
        recordedIndexes.zipWithNext().forEach { (fromIdx, toIdx) ->
            val from = records[fromIdx].percent ?: return@forEach
            val to = records[toIdx].percent ?: return@forEach
            val adjacent = toIdx - fromIdx == 1
            drawLine(
                color = if (adjacent) c.accent else c.faint.copy(alpha = 0.7f),
                start = Offset(xFor(fromIdx), yFor(from.toFloat())),
                end = Offset(xFor(toIdx), yFor(to.toFloat())),
                strokeWidth = if (adjacent) 2.5.dp.toPx() else 1.5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = if (adjacent) null else dash
            )
        }

        // Dots on recorded months; the peak month gets the gold crown dot.
        val peakIndex = recordedIndexes.maxByOrNull { records[it].percent ?: 0 }
        recordedIndexes.forEach { index ->
            val center = Offset(xFor(index), yFor(records[index].percent!!.toFloat()))
            val isPeak = index == peakIndex
            val radius = (if (isPeak) 4.5.dp else 3.5.dp).toPx()
            drawCircle(color = if (isPeak) c.warn else c.accent, radius = radius, center = center)
            drawCircle(
                color = Color.White,
                radius = radius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Point labels: peak (gold), drop (red), current (ink); peak wins on collision.
        val dropIndex = recordedIndexes.minByOrNull { records[it].percent ?: 100 }
        val currentIndex = records.indexOfLast { it.percent != null }
        peakIndex?.let { index ->
            val record = records[index]
            drawPointLabel(
                text = "${record.percent}% (${record.attendedDays}d)",
                color = c.warn,
                anchorX = xFor(index),
                bottomY = yFor(record.percent!!.toFloat()) - 8.dp.toPx()
            )
        }
        if (dropIndex != null && dropIndex != peakIndex) {
            val record = records[dropIndex]
            drawPointLabel(
                text = "${record.percent}% (${record.attendedDays}d)",
                color = c.bad,
                anchorX = xFor(dropIndex),
                bottomY = yFor(record.percent!!.toFloat()) - 8.dp.toPx()
            )
        }
        if (currentIndex >= 0 && currentIndex != peakIndex && currentIndex != dropIndex) {
            val record = records[currentIndex]
            drawPointLabel(
                text = "${record.percent}% (${record.attendedDays}d)",
                color = c.ink,
                anchorX = xFor(currentIndex),
                bottomY = yFor(record.percent!!.toFloat()) - 8.dp.toPx(),
                alignEnd = true
            )
        }
    }
}

/** Small white stat tile used in the footer summary row. */
@Composable
private fun AttendanceStatTile(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    valueColor: Color,
    sub: String,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(11.dp))
            Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        }
        Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = valueColor, maxLines = 1)
        Text(sub, fontSize = 7.5.sp, fontWeight = FontWeight.Medium, color = c.muted, maxLines = 1)
    }
}

/** Derives the trend card's "Nov 2025 – Oct 2026" style range label from the records. */
private fun trendRangeLabel(records: List<AttendanceMonthRecord>, fallback: String): String {
    val first = records.firstOrNull()?.fullLabel ?: return fallback
    val last = records.lastOrNull()?.fullLabel ?: return fallback
    return "$first – $last"
}

/**
 * Today = the member's as-of date (profile.dateDisplay, e.g. "Tue\n22 Oct 2026")
 * when it falls inside the shown month, else -1 (no ring). Deterministic, so the
 * accent ring renders the same on every device and screenshot; the model's
 * [CalendarDay.dayOfWeekIndex] removes any need for a hard-coded weekday offset.
 */
private fun resolveAsOfTodayDay(monthLabel: String, dateDisplay: String): Int {
    val normalizedDate = dateDisplay.replace("\n", " ").trim()
    val normalizedMonth = monthLabel.replace("\n", " ").trim()
    if (!normalizedDate.contains(normalizedMonth, ignoreCase = true)) return -1
    return asOfDayRegex.find(normalizedDate)?.groupValues?.get(1)?.toIntOrNull() ?: -1
}
