package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceMonthRecord
import com.example.badnewgym.feature.membercard.presentation.components.attendanceMonthHistory

/**
 * Attendance Intelligence Screen:
 * Vertically compact, bold-first typography, eye-catching metrics for all age groups.
 * Eliminates letter-by-letter column wrapping and wasted vertical gaps.
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
        verticalArrangement = Arrangement.spacedBy(4.dp)
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // LEFT COLUMN: Donut Gauge + Streak + Check-in Time (width: 104dp)
                Column(
                    modifier = Modifier.width(104.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Donut Gauge (70dp)
                    Box(
                        modifier = Modifier.size(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 6.dp.toPx()
                            val arcSize = size.minDimension - strokeW
                            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
                            drawArc(
                                color = NeuPalette.shadowSoft,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(NeuPalette.green, NeuPalette.green, NeuPalette.green)
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
                            Text(
                                "81%",
                                fontSize = IntelligenceTypography.sizeMetric,
                                fontWeight = IntelligenceTypography.weightValue,
                                color = c.ink,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                "25/31d",
                                fontSize = IntelligenceTypography.sizeSmall,
                                fontWeight = IntelligenceTypography.weightValue,
                                color = NeuPalette.green
                            )
                        }
                    }

                    // Current Streak Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violetDeep)))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                "6d Streak",
                                fontSize = IntelligenceTypography.sizeSmall,
                                fontWeight = IntelligenceTypography.weightValue,
                                color = Color.White
                            )
                        }
                    }

                    // Avg. Check-in Time Pill (Zero wrapping, bold-first)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .neuRaised(radius = 8.dp, depth = 2.dp, blur = 4.dp)
                            .padding(horizontal = 4.dp, vertical = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Schedule,
                                    contentDescription = null,
                                    tint = NeuPalette.violet,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    "07:18 AM",
                                    fontSize = IntelligenceTypography.sizeSmall,
                                    fontWeight = IntelligenceTypography.weightValue,
                                    color = c.ink,
                                    maxLines = 1
                                )
                            }
                            Text(
                                "↓ 12m earlier",
                                fontSize = IntelligenceTypography.sizeMicro,
                                fontWeight = IntelligenceTypography.weightValue,
                                color = NeuPalette.green,
                                maxLines = 1
                            )
                        }
                    }
                }

                // RIGHT COLUMN: 4 Status Tiles + Mon-Sun Calendar Grid
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 4 Status Tiles (1x4 row)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        StatusTile("✔ 23", "Present", NeuPalette.green, Color(0xFFD1FAE5), Modifier.weight(1f))
                        StatusTile("🕒 2", "Late", Color(0xFFD97706), Color(0xFFFEF3C7), Modifier.weight(1f))
                        StatusTile("✖ 2", "Absent", Color(0xFFDC2626), Color(0xFFFEE2E2), Modifier.weight(1f))
                        StatusTile("📅 4", "Holiday", NeuPalette.inkSoft, Color(0xFFF3F4F6), Modifier.weight(1f))
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
            Spacer(modifier = Modifier.height(4.dp))

            // 5-Pod Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                TrendPod("6d", "Streak", Icons.Filled.LocalFireDepartment, Color(0xFFD97706), Modifier.weight(1f))
                TrendPod("81%", "Avg Attn", Icons.AutoMirrored.Filled.ShowChart, NeuPalette.violet, Modifier.weight(1f))
                TrendPod("97%", "Best", Icons.Filled.EmojiEvents, NeuPalette.green, Modifier.weight(1f))
                TrendPod("58%", "Low", Icons.AutoMirrored.Filled.TrendingDown, Color(0xFFDC2626), Modifier.weight(1f))
                TrendPod("8/12", "Tracked", Icons.Filled.CalendarMonth, NeuPalette.violet, Modifier.weight(1f))
            }
        }

        // CARD 3: Smart Insight [AI] Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neuRaised(radius = 12.dp, depth = 3.dp, blur = 8.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = NeuPalette.violet,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Smart Insight", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeuPalette.violet)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("AI", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightValue, color = Color.White)
                            }
                        }
                        Text(
                            "Attendance is good (81%). Keep your 6-day streak to reach 90%+ this month.",
                            fontSize = IntelligenceTypography.sizeSmall,
                            fontWeight = IntelligenceTypography.weightBody,
                            color = NeuPalette.inkSoft,
                            lineHeight = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(NeuPalette.green)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "🎯 Next ≥90%",
                        fontSize = IntelligenceTypography.sizeSmall,
                        fontWeight = IntelligenceTypography.weightValue,
                        color = Color.White
                    )
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
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(topText, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = tint)
            Text(subText, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = tint)
        }
    }
}

@Composable
private fun CalendarGridMonSun(onDayClick: (CalendarDay) -> Unit) {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        // Weekday header (1-letter headers with equal weight so they never wrap)
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    day,
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightValue,
                    color = NeuPalette.inkSoft,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar rows
        val gridData = listOf(
            listOf(null, null, null, DayItem(1, "✓", NeuPalette.green), DayItem(2, "✓", NeuPalette.green), DayItem(3, "✓", NeuPalette.green), DayItem(4, "4", Color(0xFFF3F4F6), NeuPalette.faint)),
            listOf(DayItem(5, "✓", NeuPalette.green), DayItem(6, "✓", NeuPalette.green), DayItem(7, "✓", NeuPalette.green), DayItem(8, "✓", NeuPalette.green), DayItem(9, "✓", NeuPalette.green), DayItem(10, "10", NeuPalette.green), DayItem(11, "11", Color(0xFFF3F4F6), NeuPalette.faint)),
            listOf(DayItem(12, "12", NeuPalette.green), DayItem(13, "13", NeuPalette.green), DayItem(14, "14", NeuPalette.green), DayItem(15, "15", NeuPalette.green), DayItem(16, "16", NeuPalette.green, isActive = true), DayItem(17, "17", Color(0xFFEF4444)), DayItem(18, "18", Color(0xFFF3F4F6), NeuPalette.faint)),
            listOf(DayItem(19, "19", NeuPalette.green), DayItem(20, "20", NeuPalette.green), DayItem(21, "21", NeuPalette.green), DayItem(22, "22", Color(0xFFF59E0B)), DayItem(23, "23", NeuPalette.green), DayItem(24, "24", Color(0xFFEF4444)), DayItem(25, "25", Color(0xFFF3F4F6), NeuPalette.faint)),
            listOf(DayItem(26, "26", NeuPalette.green), DayItem(27, "27", NeuPalette.green), DayItem(28, "28", Color(0xFFF59E0B)), DayItem(29, "29", NeuPalette.green), DayItem(30, "30", NeuPalette.green), DayItem(31, "31", NeuPalette.green), null)
        )

        gridData.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day != null) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(day.bg)
                                    .border(if (day.isActive) 2.dp else 0.dp, NeuPalette.violet, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    day.label,
                                    fontSize = IntelligenceTypography.sizeSmall,
                                    fontWeight = IntelligenceTypography.weightValue,
                                    color = day.fg
                                )
                            }
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
    Canvas(modifier = Modifier.fillMaxWidth().height(132.dp)) {
        val leftPad = 40.dp.toPx()
        val rightPad = 28.dp.toPx()
        val topPad = 18.dp.toPx()
        val bottomPad = 16.dp.toPx()
        val plotW = size.width - leftPad - rightPad
        val plotH = size.height - topPad - bottomPad

        // Grid lines (100%, 50%, 0%)
        val yLabels = listOf("100%", "50%", "0%")
        val yVals = listOf(100f, 50f, 0f)
        val axisStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.inkSoft, fontWeight = IntelligenceTypography.weightBody)

        yVals.forEachIndexed { index, v ->
            val y = topPad + (1f - v / 100f) * plotH
            drawLine(
                color = NeuPalette.shadowSoft,
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
                colors = listOf(NeuPalette.violet.copy(alpha = 0.16f), Color.Transparent),
                startY = topPad,
                endY = topPad + plotH
            )
        )

        // Draw Stroke
        drawPath(path, NeuPalette.violet, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

        // Points & Flags
        coords.forEachIndexed { i, pt ->
            val isJan = i == 2 // 97% best
            val isMar = i == 4 // 58% min
            val isOct = i == 11 // 81% current

            drawCircle(color = NeuPalette.violet, radius = 2.5.dp.toPx(), center = pt)
            drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pt)

            // Month text below
            val mText = months[i]
            val mMeasured = measurer.measure(mText, axisStyle)
            drawText(measurer, mText, Offset(pt.x - mMeasured.size.width / 2f, topPad + plotH + 2.dp.toPx()), style = axisStyle)

            // Floating Flags
            if (isJan) {
                drawFlagBadge(pt.x, pt.y - 12.dp.toPx(), "97% Jan", NeuPalette.green, measurer)
            } else if (isMar) {
                drawFlagBadge(pt.x, pt.y - 12.dp.toPx(), "58% Mar", Color(0xFFEF4444), measurer)
            } else if (isOct) {
                drawFlagBadge(pt.x, pt.y - 12.dp.toPx(), "81% Oct", NeuPalette.violet, measurer)
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
    val style = TextStyle(fontSize = IntelligenceTypography.sizeMicro, color = Color.White, fontWeight = IntelligenceTypography.weightValue)
    val measured = measurer.measure(text, style)
    val padH = 4.dp.toPx()
    val padV = 2.dp.toPx()
    val bw = measured.size.width + padH * 2
    val bh = measured.size.height + padV * 2

    drawRoundRect(
        color = bg,
        topLeft = Offset(cx - bw / 2f, cy - bh / 2f),
        size = Size(bw, bh),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx(), 3.dp.toPx())
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
            .neuRaised(radius = 8.dp, depth = 2.dp, blur = 4.dp)
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
            Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.ink, maxLines = 1)
            Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft, maxLines = 1)
        }
    }
}
