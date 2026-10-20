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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

/**
 * BAD GYM — Deep & Detailed Member Intelligence Cards Section
 * Renders the 3 core high-contrast frosted white cards:
 * 1. Payment & Plan Card
 * 2. Attendance Donut & Workout Activity Bars Row
 * 3. Body Stats 4-Metric Card
 */
@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onReceiptClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Payment & Plan Card
        PaymentPlanCard(
            paymentPlan = data.paymentPlan,
            onClick = onReceiptClick
        )

        // 2. Attendance & Workout Row (Side-by-Side 2 Columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AttendanceDonutCard(
                attendance = data.attendance,
                onClick = onAttendanceClick,
                modifier = Modifier.weight(1f)
            )

            WorkoutBarCard(
                workout = data.workoutTrend,
                onClick = onAnalyticsClick,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. Body Stats 4-Metric Card
        BodyStatsCard(
            bodyStats = data.bodyStats,
            onClick = onAnalyticsClick
        )
    }
}

// -----------------------------------------------------------------------------------------
// CARD 1: Payment & Plan
// -----------------------------------------------------------------------------------------
@Composable
fun PaymentPlanCard(
    paymentPlan: PaymentPlanState,
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
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Red Wallet Icon + "Payment & Plan" + Chevron >
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

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body Row: Left Paid Amount + Right Next Due Block
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Block: Check Icon + Amount + Subtitle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = paymentPlan.amountPaid,
                            color = Color(0xFF047857),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = paymentPlan.paidOnDate,
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Block: Light Blue Pill Sub-Card
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0FDF4).copy(alpha = 0.6f))
                        .border(1.dp, Color(0xFFE0F2FE), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Next Due",
                            color = Color(0xFF64748B),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = paymentPlan.nextDueDate,
                            color = MemberCardTokens.DarkNavy,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// CARD 2: Attendance Donut Ring Chart Card
// -----------------------------------------------------------------------------------------
@Composable
fun AttendanceDonutCard(
    attendance: AttendanceSectionData,
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
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Green Calendar Icon + "Attendance" + Chevron >
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
                        fontSize = 13.5.sp,
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

            Spacer(modifier = Modifier.height(10.dp))

            // Body: Left Donut + Right Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Donut Chart with Center Percentage
                Box(
                    modifier = Modifier.size(62.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(62.dp)) {
                        val stroke = 7.dp.toPx()
                        val arcSize = size.copy(width = size.width - stroke, height = size.height - stroke)
                        val offset = Offset(stroke / 2f, stroke / 2f)

                        // 24 present / 31 = ~278 degrees (Green)
                        val presentSweep = (24f / 31f) * 360f
                        // 3 absent / 31 = ~35 degrees (Red)
                        val absentSweep = (3f / 31f) * 360f
                        // 2 late / 31 = ~23 degrees (Yellow)
                        val lateSweep = (2f / 31f) * 360f
                        // 1 holiday / 31 = ~12 degrees (Grey)
                        val holidaySweep = (1f / 31f) * 360f

                        var startAngle = -90f

                        // Present segment
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = startAngle,
                            sweepAngle = presentSweep - 3f,
                            useCenter = false,
                            topLeft = offset,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        startAngle += presentSweep

                        // Absent segment
                        drawArc(
                            color = Color(0xFFEF4444),
                            startAngle = startAngle,
                            sweepAngle = absentSweep - 3f,
                            useCenter = false,
                            topLeft = offset,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        startAngle += absentSweep

                        // Late segment
                        drawArc(
                            color = Color(0xFFF59E0B),
                            startAngle = startAngle,
                            sweepAngle = lateSweep - 3f,
                            useCenter = false,
                            topLeft = offset,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        startAngle += lateSweep

                        // Holiday segment
                        drawArc(
                            color = Color(0xFF94A3B8),
                            startAngle = startAngle,
                            sweepAngle = holidaySweep - 3f,
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${attendance.attendedDays}/${attendance.totalDays}",
                            color = Color(0xFF64748B),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Legend Column
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.5.dp),
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    LegendItem(color = Color(0xFF10B981), text = "${attendance.presentCount} Present")
                    LegendItem(color = Color(0xFFEF4444), text = "${attendance.absentCount} Absent")
                    LegendItem(color = Color(0xFFF59E0B), text = "${attendance.lateCount} Late")
                    LegendItem(color = Color(0xFF94A3B8), text = "${attendance.holidayCount} Holiday")
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color(0xFF334155),
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// -----------------------------------------------------------------------------------------
// CARD 3: Workout Activity Bars Card
// -----------------------------------------------------------------------------------------
@Composable
fun WorkoutBarCard(
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
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Blue Squircle + "Workout"
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
                    text = "Workout",
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body: Bar Chart on Left + 48m Avg/Day on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 6 Vertical Gradient Bars
                val barHeights = listOf(22.dp, 30.dp, 44.dp, 18.dp, 36.dp, 38.dp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(46.dp)
                ) {
                    barHeights.forEach { height ->
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(height)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4))
                                    )
                                )
                        )
                    }
                }

                // Avg/Day Column
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${workout.avgPerDayMinutes}m",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Avg/Day",
                        color = Color(0xFF64748B),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Sub-Metrics Row: Peak & Lowest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Peak
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${workout.peakMinutes}m",
                        color = Color(0xFF0F172A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Peak",
                        color = Color(0xFF64748B),
                        fontSize = 8.5.sp
                    )
                }

                // Lowest
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${workout.lowestMinutes}m",
                        color = Color(0xFF0F172A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Lowest",
                        color = Color(0xFF64748B),
                        fontSize = 8.5.sp
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// CARD 4: Body Stats 4-Metric Card
// -----------------------------------------------------------------------------------------
@Composable
fun BodyStatsCard(
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
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Scale Icon + "Body Stats" + Chevron >
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
                            .background(Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            // Weight Scale Graphic
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(size.width * 0.15f, size.height * 0.15f),
                                size = Size(size.width * 0.7f, size.height * 0.7f),
                                cornerRadius = CornerRadius(2.dp.toPx())
                            )
                            drawArc(
                                color = Color(0xFF3B82F6),
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = false,
                                topLeft = Offset(size.width * 0.35f, size.height * 0.25f),
                                size = Size(size.width * 0.3f, size.height * 0.3f),
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                        }
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

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Micro Metrics Grid Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Metric 1: Weight (69.5 kg ↓ 2.5 kg)
                StatColumn(
                    iconBg = Color(0xFFDCFCE7),
                    iconTint = Color(0xFF10B981),
                    value = "69.5 kg",
                    delta = "↓ 2.5 kg",
                    deltaColor = Color(0xFF10B981),
                    iconDraw = {
                        drawRoundRect(color = it, topLeft = Offset(size.width * 0.2f, size.height * 0.2f), size = Size(size.width * 0.6f, size.height * 0.6f), cornerRadius = CornerRadius(2.dp.toPx()))
                    }
                )

                // Metric 2: Muscle (57.2 kg + 1.4 kg)
                StatColumn(
                    iconBg = Color(0xFFF3E8FF),
                    iconTint = Color(0xFF8B5CF6),
                    value = "57.2 kg",
                    delta = "+ 1.4 kg",
                    deltaColor = Color(0xFF10B981),
                    iconDraw = {
                        val w = size.width
                        val h = size.height
                        drawLine(color = it, start = Offset(w * 0.2f, h * 0.3f), end = Offset(w * 0.2f, h * 0.7f), strokeWidth = 2.dp.toPx())
                        drawLine(color = it, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.5.dp.toPx())
                        drawLine(color = it, start = Offset(w * 0.8f, h * 0.3f), end = Offset(w * 0.8f, h * 0.7f), strokeWidth = 2.dp.toPx())
                    }
                )

                // Metric 3: Body Fat (17.6% ↓ 1.2%)
                StatColumn(
                    iconBg = Color(0xFFFEE2E2),
                    iconTint = Color(0xFFEF4444),
                    value = "17.6%",
                    delta = "↓ 1.2%",
                    deltaColor = Color(0xFFEF4444),
                    iconDraw = {
                        drawCircle(color = it, radius = 1.5.dp.toPx(), center = Offset(size.width * 0.35f, size.height * 0.35f))
                        drawLine(color = it, start = Offset(size.width * 0.7f, size.height * 0.3f), end = Offset(size.width * 0.3f, size.height * 0.7f), strokeWidth = 1.5.dp.toPx())
                        drawCircle(color = it, radius = 1.5.dp.toPx(), center = Offset(size.width * 0.65f, size.height * 0.65f))
                    }
                )

                // Metric 4: BMI (22.8 Normal)
                StatColumn(
                    iconBg = Color(0xFFE0F2FE),
                    iconTint = Color(0xFF0284C7),
                    value = "22.8",
                    delta = "Normal",
                    deltaColor = Color(0xFF10B981),
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
private fun StatColumn(
    iconBg: Color,
    iconTint: Color,
    value: String,
    delta: String,
    deltaColor: Color,
    iconDraw: androidx.compose.ui.graphics.drawscope.DrawScope.(Color) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                iconDraw(iconTint)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            color = MemberCardTokens.DarkNavy,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = delta,
            color = deltaColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
