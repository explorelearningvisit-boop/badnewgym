package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

/**
 * BAD GYM — Deep & Detailed Member Intelligence Cards Section
 * Renders the exact 3 high-contrast frosted white sections:
 * 1. Payment & Plan Card (with Prime Plan pill + 3-card status row: Paid, Next Due, View Receipt)
 * 2. 2-Column Row: Attendance (Donut + 7-col Dot Calendar + Legend) & Workout Trend (30-day bars + 3 stats)
 * 3. Body Stats Card (4 micro-cards with smooth Sparkline curves & gradients)
 */
@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onReceiptClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Payment & Plan Card
        PaymentPlanDetailedCard(
            paymentPlan = data.paymentPlan,
            onReceiptClick = onReceiptClick
        )

        // 2. Attendance & Workout Trend Row (Side-by-Side 2 Columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AttendanceDotCalendarCard(
                attendance = data.attendance,
                onHeaderClick = onAttendanceClick,
                onDayClick = onAttendanceDayClick,
                modifier = Modifier.weight(1f)
            )

            WorkoutTrendChartCard(
                workout = data.workoutTrend,
                onClick = onAnalyticsClick,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. Body Stats Card with Sparklines
        BodyStatsSparklineCard(
            bodyStats = data.bodyStats,
            onClick = onAnalyticsClick
        )
    }
}

// -----------------------------------------------------------------------------------------
// CARD 1: Payment & Plan (Exact Reference with 3 Sub-Cards)
// -----------------------------------------------------------------------------------------
@Composable
fun PaymentPlanDetailedCard(
    paymentPlan: PaymentPlanState,
    onReceiptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0x18000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Red Wallet Icon + "Payment & Plan" + Prime Plan Banner
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
                            .background(Color(0xFFF43F5E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.7f),
                                cornerRadius = CornerRadius(2.dp.toPx())
                            )
                            drawCircle(
                                color = Color(0xFFF43F5E),
                                radius = 2.dp.toPx(),
                                center = Offset(size.width * 0.75f, size.height * 0.5f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Payment & Plan",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Amber "Prime Plan (1 Month)" Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFBEB))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", color = Color.White, fontSize = 8.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Prime Plan (1 Month)",
                            color = Color(0xFF92400E),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "01 Sep – 30 Sep 2026",
                            color = Color(0xFF2563EB),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3-Card Row: Paid Status, Next Due, View Receipt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sub-Card 1: Paid Status
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0FDF4))
                        .border(1.dp, Color(0xFFDCFCE7), RoundedCornerShape(12.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "PAID • ON TIME",
                                color = Color(0xFF10B981),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = paymentPlan.amountPaid,
                                color = Color(0xFF047857),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = paymentPlan.paidOnDate,
                                color = Color(0xFF64748B),
                                fontSize = 8.5.sp
                            )
                        }
                    }
                }

                // Sub-Card 2: Next Due
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(12.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Next Due",
                                color = Color(0xFF64748B),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = paymentPlan.nextDueDate,
                                    color = MemberCardTokens.DarkNavy,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MemberCardTokens.DarkNavy,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                // Sub-Card 3: View Receipt
                Box(
                    modifier = Modifier
                        .weight(0.95f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(12.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2563EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(12.dp)) {
                                val w = size.width
                                val h = size.height
                                drawRoundRect(color = Color.White, topLeft = Offset(0f, 0f), size = Size(w, h), cornerRadius = CornerRadius(1.5.dp.toPx()))
                                drawLine(color = Color(0xFF2563EB), start = Offset(w * 0.2f, h * 0.35f), end = Offset(w * 0.8f, h * 0.35f), strokeWidth = 1.2.dp.toPx())
                                drawLine(color = Color(0xFF2563EB), start = Offset(w * 0.2f, h * 0.65f), end = Offset(w * 0.6f, h * 0.65f), strokeWidth = 1.2.dp.toPx())
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "View\nReceipt >",
                            color = Color(0xFF2563EB),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// CARD 2: Attendance Card with Donut Gauge + 7-Column Dot Calendar Grid
// -----------------------------------------------------------------------------------------
@Composable
fun AttendanceDotCalendarCard(
    attendance: AttendanceSectionData,
    onHeaderClick: () -> Unit,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(11.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Green Calendar Icon + "Attendance" + Chevron >
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Attendance",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body: Left Donut Ring + Right 7-Col Calendar Dot Matrix
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut Gauge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clickable(onClick = onHeaderClick),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(56.dp)) {
                        val stroke = 6.dp.toPx()
                        val arcSize = size.copy(width = size.width - stroke, height = size.height - stroke)
                        val offset = Offset(stroke / 2f, stroke / 2f)

                        // Track
                        drawArc(
                            color = Color(0xFFD1FAE5),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = offset,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )

                        // Progress Arc
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = -90f,
                            sweepAngle = (attendance.percent / 100f) * 360f,
                            useCenter = false,
                            topLeft = offset,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${attendance.percent}%",
                            color = MemberCardTokens.DarkNavy,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${attendance.attendedDays}/${attendance.totalDays}",
                            color = Color(0xFF10B981),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Days",
                            color = Color(0xFF64748B),
                            fontSize = 7.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: 7-Column Dot Matrix Calendar
                Column(modifier = Modifier.weight(1f)) {
                    // Weekday headers: S M T W T F S
                    val weekdays = listOf("S", "M", "T", "W", "T", "F", "S")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weekdays.forEach { day ->
                            Text(
                                text = day,
                                color = Color(0xFF64748B),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 4-5 Rows of Dots
                    val days = attendance.calendarDays.take(28)
                    val weeks = days.chunked(7)
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                val dotColor = when (day.status) {
                                    DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                                    DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                                    DayAttendanceStatus.LATE -> Color(0xFFF59E0B)
                                    DayAttendanceStatus.HOLIDAY -> Color(0xFF94A3B8)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(10.dp)
                                        .clickable { onDayClick(day) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(dotColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Horizontal Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DotLegend(color = Color(0xFF10B981), text = "Present ${attendance.presentCount}")
                DotLegend(color = Color(0xFFEF4444), text = "Absent ${attendance.absentCount}")
                DotLegend(color = Color(0xFFF59E0B), text = "Late ${attendance.lateCount}")
                DotLegend(color = Color(0xFF94A3B8), text = "Holiday ${attendance.holidayCount}")
            }
        }
    }
}

@Composable
private fun DotLegend(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(2.5.dp))
        Text(
            text = text,
            color = Color(0xFF475569),
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

// -----------------------------------------------------------------------------------------
// CARD 3: Workout Trend Card (Full 30-Day Bar Chart + 3 Bottom Metrics)
// -----------------------------------------------------------------------------------------
@Composable
fun WorkoutTrendChartCard(
    workout: WorkoutTrendData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(11.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Blue Squircle + "Workout Trend" + Chevron >
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(w * 0.25f, h * 0.2f), end = Offset(w * 0.25f, h * 0.8f), strokeWidth = 1.6.dp.toPx())
                            drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.2.dp.toPx())
                            drawLine(color = Color.White, start = Offset(w * 0.75f, h * 0.2f), end = Offset(w * 0.75f, h * 0.8f), strokeWidth = 1.6.dp.toPx())
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Workout Trend",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Body: Y-Axis + 30-Day Bar Chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Y-Axis labels: 60m, 30m, 0m
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "60m", color = Color(0xFF94A3B8), fontSize = 7.sp)
                    Text(text = "30m", color = Color(0xFF94A3B8), fontSize = 7.sp)
                    Text(text = "0m", color = Color(0xFF94A3B8), fontSize = 7.sp)
                }

                // 25-30 Bars
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val barValues = listOf(
                        15, 25, 45, 52, 38, 48, 55, 30, 42, 60,
                        50, 48, 35, 40, 52, 12, 45, 50, 48, 55,
                        38, 42, 58, 46, 50
                    )
                    barValues.forEach { minutes ->
                        val barHeight = ((minutes / 60f) * 44).dp
                        val barColor = when {
                            minutes >= 50 -> Color(0xFF10B981)
                            minutes >= 35 -> Color(0xFF06B6D4)
                            minutes >= 20 -> Color(0xFFF59E0B)
                            else -> Color(0xFFEF4444)
                        }

                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(barColor)
                        )
                    }
                }
            }

            // X-Axis labels: 1, 5, 10, 15, 20, 25, 30
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("1", "5", "10", "15", "20", "25").forEach { label ->
                    Text(text = label, color = Color(0xFF94A3B8), fontSize = 7.5.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom 3 Metric Chips: Avg/Day, Peak, Lowest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avg/Day
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏱", fontSize = 8.sp)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(text = "${workout.avgPerDayMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Avg / Day", color = Color(0xFF64748B), fontSize = 6.5.sp)
                    }
                }

                // Peak
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "↗", color = Color(0xFF10B981), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(text = "${workout.peakMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Peak (${workout.peakDate})", color = Color(0xFF64748B), fontSize = 6.5.sp)
                    }
                }

                // Lowest
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "↘", color = Color(0xFFEF4444), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(text = "${workout.lowestMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Lowest (${workout.lowestDate})", color = Color(0xFF64748B), fontSize = 6.5.sp)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// CARD 4: Body Stats 4-Metric Card with Smooth Sparkline Curves & Gradients
// -----------------------------------------------------------------------------------------
@Composable
fun BodyStatsSparklineCard(
    bodyStats: List<BodyStatItem>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0x18000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Purple Body Icon + "Body Stats" + Chevron >
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
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Body Stats",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Micro Metrics Grid Row with Sparkline Graphs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Weight (69.5 kg ↓ 2.5 kg)
                SparklineMetricCard(
                    iconBg = Color(0xFFDCFCE7),
                    iconTint = Color(0xFF10B981),
                    value = "69.5 kg",
                    delta = "↓ 2.5 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineColor = Color(0xFF10B981),
                    sparklinePoints = listOf(72f, 71.5f, 71f, 70.8f, 70.2f, 69.8f, 69.5f),
                    modifier = Modifier.weight(1f),
                    iconDraw = {
                        drawRoundRect(color = it, topLeft = Offset(size.width * 0.15f, size.height * 0.15f), size = Size(size.width * 0.7f, size.height * 0.7f), cornerRadius = CornerRadius(2.dp.toPx()))
                    }
                )

                // Metric 2: Muscle (57.2 kg ↑ 1.4 kg)
                SparklineMetricCard(
                    iconBg = Color(0xFFF3E8FF),
                    iconTint = Color(0xFF8B5CF6),
                    value = "57.2 kg",
                    delta = "↑ 1.4 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineColor = Color(0xFF8B5CF6),
                    sparklinePoints = listOf(55.8f, 56.0f, 56.3f, 56.6f, 56.9f, 57.0f, 57.2f),
                    modifier = Modifier.weight(1f),
                    iconDraw = {
                        val w = size.width
                        val h = size.height
                        drawLine(color = it, start = Offset(w * 0.2f, h * 0.3f), end = Offset(w * 0.2f, h * 0.7f), strokeWidth = 2.dp.toPx())
                        drawLine(color = it, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.5.dp.toPx())
                        drawLine(color = it, start = Offset(w * 0.8f, h * 0.3f), end = Offset(w * 0.8f, h * 0.7f), strokeWidth = 2.dp.toPx())
                    }
                )

                // Metric 3: Body Fat (17.6% ↓ 1.2%)
                SparklineMetricCard(
                    iconBg = Color(0xFFFEE2E2),
                    iconTint = Color(0xFFEF4444),
                    value = "17.6%",
                    delta = "↓ 1.2%",
                    deltaColor = Color(0xFFEF4444),
                    sparklineColor = Color(0xFFEF4444),
                    sparklinePoints = listOf(18.8f, 18.5f, 18.2f, 18.0f, 17.8f, 17.7f, 17.6f),
                    modifier = Modifier.weight(1f),
                    iconDraw = {
                        drawCircle(color = it, radius = 1.5.dp.toPx(), center = Offset(size.width * 0.35f, size.height * 0.35f))
                        drawLine(color = it, start = Offset(size.width * 0.7f, size.height * 0.3f), end = Offset(size.width * 0.3f, size.height * 0.7f), strokeWidth = 1.5.dp.toPx())
                        drawCircle(color = it, radius = 1.5.dp.toPx(), center = Offset(size.width * 0.65f, size.height * 0.65f))
                    }
                )

                // Metric 4: BMI (22.8 Normal)
                SparklineMetricCard(
                    iconBg = Color(0xFFE0F2FE),
                    iconTint = Color(0xFF0284C7),
                    value = "22.8",
                    delta = "Normal",
                    deltaColor = Color(0xFF10B981),
                    sparklineColor = Color(0xFF0284C7),
                    sparklinePoints = listOf(23.4f, 23.2f, 23.1f, 23.0f, 22.9f, 22.8f, 22.8f),
                    modifier = Modifier.weight(1f),
                    iconDraw = {
                        drawCircle(color = it, radius = 2.dp.toPx(), center = Offset(size.width * 0.5f, size.height * 0.3f))
                        drawArc(color = it, startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = Offset(size.width * 0.3f, size.height * 0.45f), size = Size(size.width * 0.4f, size.height * 0.4f))
                    }
                )
            }
        }
    }
}

@Composable
private fun SparklineMetricCard(
    iconBg: Color,
    iconTint: Color,
    value: String,
    delta: String,
    deltaColor: Color,
    sparklineColor: Color,
    sparklinePoints: List<Float>,
    modifier: Modifier = Modifier,
    iconDraw: androidx.compose.ui.graphics.drawscope.DrawScope.(Color) -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .padding(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(13.dp)) {
                    iconDraw(iconTint)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = MemberCardTokens.DarkNavy,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = delta,
                color = deltaColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mini Sparkline Curve Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                if (sparklinePoints.size < 2) return@Canvas
                val min = sparklinePoints.minOrNull() ?: 0f
                val max = sparklinePoints.maxOrNull() ?: 1f
                val range = (max - min).coerceAtLeast(0.001f)

                val stepX = size.width / (sparklinePoints.size - 1)
                val strokePath = Path()
                val fillPath = Path()

                sparklinePoints.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val y = size.height - ((pt - min) / range * (size.height * 0.75f)) - (size.height * 0.1f)
                    if (i == 0) {
                        strokePath.moveTo(x, y)
                        fillPath.moveTo(x, size.height)
                        fillPath.lineTo(x, y)
                    } else {
                        strokePath.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }

                fillPath.lineTo(size.width, size.height)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(sparklineColor.copy(alpha = 0.25f), Color.Transparent)
                    )
                )

                drawPath(
                    path = strokePath,
                    color = sparklineColor,
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}
