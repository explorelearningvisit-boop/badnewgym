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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus

@Composable
fun AttendanceCalendarSection(
    data: AttendanceSectionData,
    onPrevMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Icon + "Attendance" + Month Navigation
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
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Attendance",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Month Picker: < October 2026 >
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onPrevMonth)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = data.monthLabel,
                        color = Color(0xFF2563EB),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onNextMonth)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two-Column Layout: Left (Radial Gauge + Legend) | Right (7-Col Calendar Grid)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Radial Ring & Legend
                Column(
                    modifier = Modifier.weight(1.05f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Circular Gauge
                    Box(
                        modifier = Modifier.size(86.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            val strokeWidth = 7.dp.toPx()
                            val arcPadding = strokeWidth / 2f
                            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                            drawArc(
                                color = Color(0xFFD1FAE5),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = Offset(arcPadding, arcPadding),
                                size = arcSize,
                                style = Stroke(width = strokeWidth)
                            )

                            val sweep = (data.percent / 100f) * 360f
                            drawArc(
                                brush = MemberCardTokens.RadialGaugeGradient,
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = Offset(arcPadding, arcPadding),
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${data.percent}%",
                                color = MemberCardTokens.DarkNavy,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${data.attendedDays} / ${data.totalDays}",
                                color = MemberCardTokens.SlateTitle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Days",
                                color = MemberCardTokens.SlateMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Legend with Counts
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        LegendRow(color = Color(0xFF10B981), label = "Present", count = data.presentCount)
                        LegendRow(color = Color(0xFFEF4444), label = "Absent", count = data.absentCount)
                        LegendRow(color = Color(0xFFF59E0B), label = "Late", count = data.lateCount)
                        LegendRow(color = Color(0xFF64748B), label = "Holiday", count = data.holidayCount)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Column: 7-Column Calendar Month Grid
                Column(modifier = Modifier.weight(1.35f)) {
                    // Weekday Headers: S M T W T F S
                    val weekdays = listOf("S", "M", "T", "W", "T", "F", "S")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weekdays.forEach { day ->
                            Text(
                                text = day,
                                color = if (day == "S") Color(0xFF10B981) else Color(0xFF2563EB),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 4 Weeks of Dates (7 days per row)
                    val days = data.calendarDays
                    val weeks = days.chunked(7)

                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { onDayClick(day) }
                                        .padding(vertical = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Status Dot
                                    val dotColor = when (day.status) {
                                        DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                                        DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                                        DayAttendanceStatus.LATE -> Color(0xFFF59E0B)
                                        DayAttendanceStatus.HOLIDAY -> Color(0xFF94A3B8)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(7.5.dp)
                                            .clip(CircleShape)
                                            .background(dotColor)
                                    )

                                    Spacer(modifier = Modifier.height(1.dp))

                                    // Day Number
                                    Text(
                                        text = day.dayOfMonth.toString(),
                                        color = MemberCardTokens.DarkNavy,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendRow(
    color: Color,
    label: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = MemberCardTokens.SlateBody,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = count.toString(),
            color = MemberCardTokens.DarkNavy,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
