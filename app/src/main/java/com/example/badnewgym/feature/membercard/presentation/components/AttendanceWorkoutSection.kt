package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.ActivityDayType
import com.example.badnewgym.feature.membercard.domain.model.AttendanceWorkoutSummary

@Composable
fun AttendanceWorkoutSection(
    summary: AttendanceWorkoutSummary,
    onMonthClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 1. Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2563EB)),
                    contentAlignment = Alignment.Center
                ) {
                    // Dumbbell / Soundwave Icon
                    Canvas(modifier = Modifier.size(14.dp)) {
                        val w = size.width
                        val h = size.height
                        drawLine(
                            color = Color.White,
                            start = Offset(w * 0.25f, h * 0.2f),
                            end = Offset(w * 0.25f, h * 0.8f),
                            strokeWidth = 2.5.dp.toPx()
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(w * 0.2f, h * 0.5f),
                            end = Offset(w * 0.8f, h * 0.5f),
                            strokeWidth = 2.dp.toPx()
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(w * 0.75f, h * 0.2f),
                            end = Offset(w * 0.75f, h * 0.8f),
                            strokeWidth = 2.5.dp.toPx()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Attendance & Workout",
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onMonthClick)
            ) {
                Text(
                    text = summary.monthLabel,
                    color = Color(0xFF2563EB),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Select Month",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Legend Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = Color(0xFF10B981), label = "Present")
            LegendItem(color = Color(0xFFEF4444), label = "Absent")
            LegendItem(color = Color(0xFF64748B), label = "Off")
            LegendItem(color = Color(0xFF2563EB), label = "Workout (min)")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. 31-Day Bar Chart
        WorkoutBarChart(
            summary = summary,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Quick Highlights (Avg / Peak / Lowest)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avg / Day
            WorkoutHighlightCard(
                iconColor = Color(0xFF10B981),
                isClock = true,
                value = "${summary.avgPerDayMinutes}m",
                label = "Avg / Day",
                modifier = Modifier.weight(1f)
            )
            // Peak
            WorkoutHighlightCard(
                iconColor = Color(0xFF10B981),
                isUpArrow = true,
                value = "${summary.peakMinutes}m",
                label = "Peak (${summary.peakDate})",
                modifier = Modifier.weight(1.15f)
            )
            // Lowest
            WorkoutHighlightCard(
                iconColor = Color(0xFFEF4444),
                isDownArrow = true,
                value = "${summary.lowestMinutes}m",
                label = "Lowest (${summary.lowestDate})",
                modifier = Modifier.weight(1.25f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5. Attendance Summary Breakdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AttendanceStatPill(
                iconType = AttendanceIconType.PRESENT,
                count = summary.presentCount,
                label = "Present",
                percent = "${summary.presentPercent}%",
                percentColor = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatPill(
                iconType = AttendanceIconType.ABSENT,
                count = summary.absentCount,
                label = "Absent",
                percent = "${summary.absentPercent}%",
                percentColor = Color(0xFFEF4444),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatPill(
                iconType = AttendanceIconType.OFF,
                count = summary.offCount,
                label = "Off",
                percent = "${summary.offPercent}%",
                percentColor = Color(0xFF64748B),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatPill(
                iconType = AttendanceIconType.LATE,
                count = summary.lateCount,
                label = "Late",
                percent = "${summary.latePercent}%",
                percentColor = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = MemberCardTokens.SlateBody,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WorkoutBarChart(
    summary: AttendanceWorkoutSummary,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Y-Axis reference lines & labels
            Column(
                modifier = Modifier.matchParentSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "60m",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "30m",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "0m",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Custom Canvas Bars & Gridlines
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .padding(start = 24.dp)
            ) {
                val chartWidth = size.width
                val chartHeight = size.height

                // Draw horizontal guide lines
                val gridY60 = 4.dp.toPx()
                val gridY30 = chartHeight * 0.5f
                val gridY0 = chartHeight - 2.dp.toPx()

                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, gridY60),
                    end = Offset(chartWidth, gridY60),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, gridY30),
                    end = Offset(chartWidth, gridY30),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(0f, gridY0),
                    end = Offset(chartWidth, gridY0),
                    strokeWidth = 1.dp.toPx()
                )

                // 31 Columns
                val totalDays = summary.dailyEntries.size
                if (totalDays > 0) {
                    val step = chartWidth / totalDays
                    val barWidth = (step * 0.65f).coerceAtMost(6.dp.toPx()).coerceAtLeast(3.5f)

                    summary.dailyEntries.forEachIndexed { index, entry ->
                        val barCenterX = (index + 0.5f) * step
                        val barLeft = barCenterX - barWidth / 2f
                        val maxMinutes = 60f
                        val entryMin = entry.minutes.coerceAtMost(60)

                        when (entry.type) {
                            ActivityDayType.PRESENT -> {
                                val barHeight = (entryMin / maxMinutes) * (chartHeight - 8.dp.toPx())
                                val barTop = gridY0 - barHeight
                                val capHeight = (barHeight * 0.5f).coerceAtLeast(barWidth * 1.5f).coerceAtMost(barHeight)
                                val baseHeight = barHeight - capHeight + (barWidth / 2f)

                                // 1. Blue Base Bar (Workout Duration)
                                val blueGradient = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF1D4ED8)
                                    ),
                                    startY = barTop + capHeight - (barWidth / 2f),
                                    endY = gridY0
                                )
                                drawRoundRect(
                                    brush = blueGradient,
                                    topLeft = Offset(barLeft, barTop + capHeight - (barWidth / 2f)),
                                    size = Size(barWidth, baseHeight.coerceAtLeast(barWidth)),
                                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                                )

                                // 2. Emerald Green Top Cap (Attendance / Intensity)
                                val greenGradient = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF00E676),
                                        Color(0xFF10B981)
                                    ),
                                    startY = barTop,
                                    endY = barTop + capHeight
                                )
                                drawRoundRect(
                                    brush = greenGradient,
                                    topLeft = Offset(barLeft, barTop),
                                    size = Size(barWidth, capHeight),
                                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                                )
                            }
                            ActivityDayType.ABSENT -> {
                                val barHeight = (18f / maxMinutes) * (chartHeight - 8.dp.toPx())
                                val barTop = gridY0 - barHeight
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFFF87171), Color(0xFFEF4444)),
                                        startY = barTop,
                                        endY = gridY0
                                    ),
                                    topLeft = Offset(barLeft, barTop),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                                )
                            }
                            ActivityDayType.OFF -> {
                                val barHeight = (30f / maxMinutes) * (chartHeight - 8.dp.toPx())
                                val barTop = gridY0 - barHeight
                                // Soft gray base
                                drawRoundRect(
                                    color = Color(0xFFF1F5F9),
                                    topLeft = Offset(barLeft, barTop),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                                )
                                // Multiple subtle diagonal striped lines
                                var stripeY = barTop + 4.dp.toPx()
                                while (stripeY < gridY0 - 2.dp.toPx()) {
                                    drawLine(
                                        color = Color(0xFFCBD5E1),
                                        start = Offset(barLeft + 0.5f, stripeY + barWidth * 0.5f),
                                        end = Offset(barLeft + barWidth - 0.5f, stripeY - barWidth * 0.5f),
                                        strokeWidth = 1.2.dp.toPx()
                                    )
                                    stripeY += 6.dp.toPx()
                                }
                            }
                            ActivityDayType.LATE -> {
                                val barHeight = (entryMin / maxMinutes) * (chartHeight - 8.dp.toPx())
                                val barTop = gridY0 - barHeight
                                drawRoundRect(
                                    color = Color(0xFFF59E0B),
                                    topLeft = Offset(barLeft, barTop),
                                    size = Size(barWidth, barHeight.coerceAtLeast(barWidth)),
                                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // X-Axis Day Numbers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1", "5", "10", "15", "20", "25", "31").forEach { tick ->
                Text(
                    text = tick,
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WorkoutHighlightCard(
    iconColor: Color,
    isClock: Boolean = false,
    isUpArrow: Boolean = false,
    isDownArrow: Boolean = false,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    val w = size.width
                    val h = size.height
                    when {
                        isClock -> {
                            drawCircle(color = Color.White, style = Stroke(width = 1.2.dp.toPx()))
                            drawLine(
                                color = Color.White,
                                start = Offset(w / 2f, h / 2f),
                                end = Offset(w / 2f, h * 0.25f),
                                strokeWidth = 1.2.dp.toPx()
                            )
                            drawLine(
                                color = Color.White,
                                start = Offset(w / 2f, h / 2f),
                                end = Offset(w * 0.75f, h / 2f),
                                strokeWidth = 1.2.dp.toPx()
                            )
                        }
                        isUpArrow -> {
                            val path = Path().apply {
                                moveTo(w / 2f, h * 0.15f)
                                lineTo(w * 0.85f, h * 0.55f)
                                lineTo(w * 0.60f, h * 0.55f)
                                lineTo(w * 0.60f, h * 0.85f)
                                lineTo(w * 0.40f, h * 0.85f)
                                lineTo(w * 0.40f, h * 0.55f)
                                lineTo(w * 0.15f, h * 0.55f)
                                close()
                            }
                            drawPath(path, Color.White)
                        }
                        isDownArrow -> {
                            val path = Path().apply {
                                moveTo(w / 2f, h * 0.85f)
                                lineTo(w * 0.85f, h * 0.45f)
                                lineTo(w * 0.60f, h * 0.45f)
                                lineTo(w * 0.60f, h * 0.15f)
                                lineTo(w * 0.40f, h * 0.15f)
                                lineTo(w * 0.40f, h * 0.45f)
                                lineTo(w * 0.15f, h * 0.45f)
                                close()
                            }
                            drawPath(path, Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column {
                Text(
                    text = value,
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

enum class AttendanceIconType {
    PRESENT,
    ABSENT,
    OFF,
    LATE
}

@Composable
private fun AttendanceStatPill(
    iconType: AttendanceIconType,
    count: Int,
    label: String,
    percent: String,
    percentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (iconType) {
                AttendanceIconType.PRESENT -> {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                AttendanceIconType.ABSENT -> {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                AttendanceIconType.OFF -> {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF64748B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawLine(
                                color = Color.White,
                                start = Offset(size.width * 0.2f, size.height * 0.8f),
                                end = Offset(size.width * 0.8f, size.height * 0.2f),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                }
                AttendanceIconType.LATE -> {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(11.dp)) {
                            drawCircle(color = Color.White, style = Stroke(width = 1.2.dp.toPx()))
                            drawLine(
                                color = Color.White,
                                start = Offset(size.width / 2f, size.height / 2f),
                                end = Offset(size.width / 2f, size.height * 0.25f),
                                strokeWidth = 1.2.dp.toPx()
                            )
                            drawLine(
                                color = Color.White,
                                start = Offset(size.width / 2f, size.height / 2f),
                                end = Offset(size.width * 0.75f, size.height / 2f),
                                strokeWidth = 1.2.dp.toPx()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(5.dp))

            Column {
                Text(
                    text = count.toString(),
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = percent,
                    color = percentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
