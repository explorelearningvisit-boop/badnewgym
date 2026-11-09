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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceMonthRecord
import com.example.badnewgym.feature.membercard.presentation.components.attendanceMonthHistory

/**
 * Attendance Intelligence Screen
 * 100% 1:1 Pixel-Perfect Reproduction of Reference: media_1794217901333_7e53442c.jpg
 * - 81% Dual-Glow Donut Gauge with Streak & Check-in Time capsules
 * - 4 Status Pods (Present 23, Late 2, Absent 2, Holiday 4)
 * - 7-Column Mon–Sun Calendar Grid with Green Checkmark Circles
 * - 12-Month Area Spline Trend with Jan 97% (best), Mar 58% (drop), Oct 81% (current)
 * - 5-Pod Bottom Summary Row
 * - Smart Insight [AI] Card with Next Goal ≥ 90% button
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
    val measurer = rememberTextMeasurer()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // CARD 1: Attendance Intelligence (Gauge + Calendar Grid)
        IntelligenceSectionCard(
            title = "Attendance Intelligence",
            icon = Icons.Filled.EventAvailable,
            trailing = { IntelligencePillOutlined("Oct 2026 ▾") }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // LEFT COLUMN: Donut Gauge + Streak + Check-in Time
                Column(
                    modifier = Modifier.width(102.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // 81% Donut Gauge
                    Box(
                        modifier = Modifier.size(86.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 7.dp.toPx()
                            val arcSize = size.minDimension - strokeW
                            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
                            // Track
                            drawArc(
                                color = Color(0xFFF1F5F9),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                            // 81% Emerald Arc
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF34D399), Color(0xFF10B981))
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * 0.81f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("81%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = c.ink, letterSpacing = (-0.5).sp)
                            Text("Attendance", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text("25 / 31 Days", fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = c.ink)
                        }
                    }

                    // Current Streak Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED))))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFACC15), modifier = Modifier.size(13.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Current Streak", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f))
                                Text("6 Days", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }

                    // Avg. Check-in Time Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                            .padding(horizontal = 5.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(11.dp))
                            Column {
                                Text("Avg. Check-in Time", fontSize = 6.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text("07:18 AM", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                                    Text("(↓ -12m)", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                                }
                            }
                        }
                    }
                }

                // RIGHT COLUMN: 4 Status Tiles + Mon-Sun Calendar Grid
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // 4 Status Tiles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        StatusTile("✔ 23", "Present", Color(0xFF10B981), Color(0xFFDCFCE7), Modifier.weight(1f))
                        StatusTile("🕒 2", "Late", Color(0xFFF59E0B), Color(0xFFFEF3C7), Modifier.weight(1f))
                        StatusTile("✖ 2", "Absent", Color(0xFFEF4444), Color(0xFFFEE2E2), Modifier.weight(1f))
                        StatusTile("📅 4", "Holiday", Color(0xFF64748B), Color(0xFFF1F5F9), Modifier.weight(1f))
                    }

                    // Mon-Sun Calendar Grid
                    CalendarGridMonSun(onDayClick = onDayClick)
                }
            }
        }

        // CARD 2: Attendance Trend (Area Spline + 5 Bottom Pods)
        IntelligenceSectionCard(
            title = "Attendance Trend",
            icon = Icons.Filled.Insights,
            subtitle = "Nov 2025 – Oct 2026",
            trailing = { IntelligencePillOutlined("12 Months ▾") }
        ) {
            AttendanceTrendSpline(measurer = measurer, c = c)
            Spacer(modifier = Modifier.height(6.dp))

            // 5-Pod Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TrendPod("6 Days", "Current Streak", Icons.Filled.LocalFireDepartment, Color(0xFFF59E0B), Modifier.weight(1f))
                TrendPod("81%", "Avg. Attendance", Icons.AutoMirrored.Filled.ShowChart, Color(0xFF8B5CF6), Modifier.weight(1f))
                TrendPod("97%", "Best (Jan)", Icons.Filled.EmojiEvents, Color(0xFF10B981), Modifier.weight(1f))
                TrendPod("58%", "Lowest (Mar)", Icons.AutoMirrored.Filled.TrendingDown, Color(0xFFEF4444), Modifier.weight(1f))
                TrendPod("8 / 12", "Tracked Months", Icons.Filled.CalendarMonth, Color(0xFF8B5CF6), Modifier.weight(1f))
            }
        }

        // CARD 3: Smart Insight [AI] Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x188B5CF6), spotColor = Color(0x188B5CF6))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(14.dp))
                .padding(9.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(17.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Smart Insight", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFEDE9FE))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("AI", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                            }
                        }
                        Text(
                            "Attendance is good (81%), Keep your 6-day streak to reach 90%+ this month.",
                            fontSize = 9.sp,
                            color = Color(0xFF475569),
                            lineHeight = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFDCFCE7))
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎯 Next Goal ≥ 90%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }
            }
        }
    }
}

@Composable
private fun StatusTile(
    topText: String,
    subText: String,
    tint: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(topText, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = tint)
            Text(subText, fontSize = 7.sp, fontWeight = FontWeight.SemiBold, color = tint)
        }
    }
}

@Composable
private fun CalendarGridMonSun(onDayClick: (CalendarDay) -> Unit) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        // Weekday header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            daysOfWeek.forEach { day ->
                Text(
                    day,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    modifier = Modifier.width(19.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Calendar rows (Oct 2026 starts on Thu = day 4)
        val gridData = listOf(
            listOf(null, null, null, DayItem(1, "✓", Color(0xFF10B981)), DayItem(2, "✓", Color(0xFF10B981)), DayItem(3, "✓", Color(0xFF10B981)), DayItem(4, "4", Color(0xFFF1F5F9), Color(0xFF64748B))),
            listOf(DayItem(5, "✓", Color(0xFF10B981)), DayItem(6, "✓", Color(0xFF10B981)), DayItem(7, "✓", Color(0xFF10B981)), DayItem(8, "✓", Color(0xFF10B981)), DayItem(9, "✓", Color(0xFF10B981)), DayItem(10, "10", Color(0xFF10B981)), DayItem(11, "11", Color(0xFFF1F5F9), Color(0xFF64748B))),
            listOf(DayItem(12, "12", Color(0xFF10B981)), DayItem(13, "13", Color(0xFF10B981)), DayItem(14, "14", Color(0xFF10B981)), DayItem(15, "15", Color(0xFF10B981)), DayItem(16, "16", Color(0xFF10B981), isActive = true), DayItem(17, "17", Color(0xFFEF4444)), DayItem(18, "18", Color(0xFFF1F5F9), Color(0xFF64748B))),
            listOf(DayItem(19, "19", Color(0xFF10B981)), DayItem(20, "20", Color(0xFF10B981)), DayItem(21, "21", Color(0xFF10B981)), DayItem(22, "22", Color(0xFFF59E0B)), DayItem(23, "23", Color(0xFF10B981)), DayItem(24, "24", Color(0xFFEF4444)), DayItem(25, "25", Color(0xFFF1F5F9), Color(0xFF64748B))),
            listOf(DayItem(26, "26", Color(0xFF10B981)), DayItem(27, "27", Color(0xFF10B981)), DayItem(28, "28", Color(0xFFF59E0B)), DayItem(29, "29", Color(0xFF10B981)), DayItem(30, "30", Color(0xFF10B981)), DayItem(31, "31", Color(0xFF10B981)), null)
        )

        gridData.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { day ->
                    if (day == null) {
                        Box(modifier = Modifier.size(19.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .clip(CircleShape)
                                .background(day.bg)
                                .border(if (day.isActive) 1.8.dp else 0.dp, Color(0xFF8B5CF6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day.label,
                                fontSize = if (day.label == "✓") 10.sp else 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = day.fg
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class DayItem(
    val dayNum: Int,
    val label: String,
    val bg: Color,
    val fg: Color = Color.White,
    val isActive: Boolean = false
)

@Composable
private fun AttendanceTrendSpline(measurer: TextMeasurer, c: IntelligenceColors) {
    Canvas(modifier = Modifier.fillMaxWidth().height(94.dp)) {
        val leftPad = 26.dp.toPx()
        val rightPad = 14.dp.toPx()
        val topPad = 24.dp.toPx()
        val bottomPad = 18.dp.toPx()
        val plotW = size.width - leftPad - rightPad
        val plotH = size.height - topPad - bottomPad

        // Grid lines (100%, 75%, 50%, 25%, 0%)
        val yLabels = listOf("100%", "75%", "50%", "25%", "0%")
        val yVals = listOf(100f, 75f, 50f, 25f, 0f)
        val axisStyle = TextStyle(fontSize = 7.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)

        yVals.forEachIndexed { index, v ->
            val y = topPad + (1f - v / 100f) * plotH
            drawLine(
                color = Color(0xFFF1F5F9),
                start = Offset(leftPad, y),
                end = Offset(size.width - rightPad, y),
                strokeWidth = 0.8.dp.toPx()
            )
            val measured = measurer.measure(yLabels[index], axisStyle)
            drawText(
                measurer,
                yLabels[index],
                Offset(leftPad - measured.size.width - 3.dp.toPx(), y - measured.size.height / 2f),
                style = axisStyle
            )
        }

        // 12 months data
        val months = listOf("Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct")
        val values = listOf(72f, 70f, 97f, 85f, 58f, 75f, 80f, 72f, 85f, 78f, 80f, 81f)
        val stepX = plotW / (months.size - 1)

        val coords = values.mapIndexed { i, pct ->
            val x = leftPad + i * stepX
            val y = topPad + (1f - pct / 100f) * plotH
            Offset(x, y)
        }

        // Spline
        val path = Path().apply {
            moveTo(coords.first().x, coords.first().y)
            for (i in 0 until coords.size - 1) {
                val p0 = coords[i]
                val p1 = coords[i + 1]
                val midX = (p0.x + p1.x) / 2f
                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
            }
        }

        // Shaded area
        val area = Path().apply {
            addPath(path)
            lineTo(coords.last().x, topPad + plotH)
            lineTo(coords.first().x, topPad + plotH)
            close()
        }
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF8B5CF6).copy(alpha = 0.28f), Color.Transparent),
                startY = topPad,
                endY = topPad + plotH
            )
        )

        // Draw Stroke
        drawPath(path, Color(0xFF8B5CF6), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

        // Points & Flags
        coords.forEachIndexed { i, pt ->
            val pct = values[i]
            val isJan = i == 2 // 97% best
            val isMar = i == 4 // 58% min
            val isOct = i == 11 // 81% current

            drawCircle(color = Color(0xFF8B5CF6), radius = 2.5.dp.toPx(), center = pt)
            drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pt)

            // Month text below
            val mText = months[i]
            val mMeasured = measurer.measure(mText, axisStyle)
            drawText(measurer, mText, Offset(pt.x - mMeasured.size.width / 2f, topPad + plotH + 3.dp.toPx()), style = axisStyle)

            // Floating Flags
            if (isJan) {
                // Green flag: 👑 97% Jan
                drawFlagBadge(pt.x, pt.y - 14.dp.toPx(), "97% Jan", Color(0xFF10B981), measurer)
            } else if (isMar) {
                // Red flag: 58% Mar
                drawFlagBadge(pt.x, pt.y - 14.dp.toPx(), "58% Mar", Color(0xFFEF4444), measurer)
            } else if (isOct) {
                // Purple flag: 81% Oct
                drawFlagBadge(pt.x, pt.y - 14.dp.toPx(), "81% Oct", Color(0xFF8B5CF6), measurer)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFlagBadge(
    cx: Float,
    cy: Float,
    text: String,
    bg: Color,
    measurer: TextMeasurer
) {
    val style = TextStyle(fontSize = 7.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
    val measured = measurer.measure(text, style)
    val padH = 5.dp.toPx()
    val padV = 2.dp.toPx()
    val bw = measured.size.width + padH * 2
    val bh = measured.size.height + padV * 2

    drawRoundRect(
        color = bg,
        topLeft = Offset(cx - bw / 2f, cy - bh / 2f),
        size = Size(bw, bh),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
    )
    drawText(measurer, text, Offset(cx - measured.size.width / 2f, cy - measured.size.height / 2f), style = style)
}

@Composable
private fun TrendPod(
    value: String,
    label: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .padding(vertical = 5.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(12.dp))
            Text(value, fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E1B4B))
            Text(label, fontSize = 6.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1)
        }
    }
}
