package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar

/**
 * BAD GYM — Member Intelligence / Individual Activity Check-In Pass
 * Compact card target: <= 600dp height without any vertical scrolling required.
 * Designed with a professional gym check-in pass vibe.
 */
@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onMenuClick: () -> Unit = {},
    onReceiptClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 380.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x2800C853),
                ambientColor = Color(0x1800E676)
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE2F4EC),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Branding & Live Check-In Badge (~30dp)
            CheckInCardHeader(
                onMenuClick = onMenuClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 2. Member Profile Identity & Circular Attendance Gauge (~72dp)
            CheckInProfileRow(
                profile = data.profile
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 3. Compact Payment & Membership Strip (~44dp)
            CheckInPaymentStrip(
                paymentPlan = data.paymentPlan,
                onReceiptClick = onReceiptClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Body Progress 4 Micro-Cards (~58dp)
            CheckInBodyStatsRow(
                stats = data.bodyStats,
                onClick = onAnalyticsClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 5. Attendance Activity Timeline & Summary (~72dp)
            CheckInAttendanceStrip(
                data = data.attendance,
                onHeaderClick = onAttendanceClick,
                onDayClick = onAttendanceDayClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 6. Workout Trend 7-Day Performance Bars (~70dp)
            CheckInWorkoutTrendRow(
                bars = data.workoutTrend.bars.takeLast(7),
                avgMinutes = data.workoutTrend.avgPerDayMinutes,
                peakMinutes = data.workoutTrend.peakMinutes,
                onClick = onAnalyticsClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 7. Verified Pass Footer & Analytics Action (~34dp)
            CheckInPassFooter(
                memberCode = data.profile.memberCode,
                onAnalyticsClick = onAnalyticsClick
            )
        }
    }
}

// ---------------------------------------------------------------------------
// 1. Header with Brand & Live Check-in Badge
// ---------------------------------------------------------------------------
@Composable
private fun CheckInCardHeader(
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Branding
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "BAD",
                color = MemberCardTokens.BrandEmerald,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = " GYM",
                color = MemberCardTokens.DarkNavy,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "PASS",
                    color = MemberCardTokens.SlateTitle,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Live Check-in Status Badge + Options Menu
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8FDF3))
                    .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(12.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live Green Pulse Dot
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "CHECKED IN • 06:45 AM",
                    color = Color(0xFF047857),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MemberCardTokens.DarkNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 2. Member Profile Row + Attendance Circular Gauge
// ---------------------------------------------------------------------------
@Composable
private fun CheckInProfileRow(
    profile: MemberProfile
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Photo with emerald ring and crown badge
        Box(modifier = Modifier.size(56.dp)) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .align(Alignment.Center)
                    .border(2.dp, MemberCardTokens.BrandEmeraldLight, CircleShape)
                    .padding(2.5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_aman),
                    contentDescription = "Member Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }

            // Crown Badge (Top-Left)
            Box(
                modifier = Modifier
                    .size(17.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(Color(0xFFFBBF24))
                    .border(0.8.dp, Color(0xFFF59E0B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(9.dp)) {
                    val path = Path().apply {
                        moveTo(size.width * 0.15f, size.height * 0.75f)
                        lineTo(size.width * 0.85f, size.height * 0.75f)
                        lineTo(size.width * 0.85f, size.height * 0.35f)
                        lineTo(size.width * 0.65f, size.height * 0.55f)
                        lineTo(size.width * 0.50f, size.height * 0.20f)
                        lineTo(size.width * 0.35f, size.height * 0.55f)
                        lineTo(size.width * 0.15f, size.height * 0.35f)
                        close()
                    }
                    drawPath(path, Color.White)
                }
            }

            // Active Green Indicator (Bottom-Right)
            Box(
                modifier = Modifier
                    .size(13.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-1).dp, y = (-1).dp)
                    .clip(CircleShape)
                    .background(MemberCardTokens.BrandEmerald)
                    .border(1.8.dp, Color.White, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Center: Name, ID, Age, Gender, Plan Pill
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.name,
                color = MemberCardTokens.DarkNavy,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = "${profile.memberCode}  •  ${profile.age} yrs  •  ${profile.gender}",
                color = MemberCardTokens.SlateMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Prime Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MemberCardTokens.PillAmberBg)
                        .border(1.dp, MemberCardTokens.PillAmberBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(8.dp)) {
                        val boltPath = Path().apply {
                            moveTo(size.width * 0.6f, 0f)
                            lineTo(size.width * 0.1f, size.height * 0.55f)
                            lineTo(size.width * 0.5f, size.height * 0.55f)
                            lineTo(size.width * 0.4f, size.height)
                            lineTo(size.width * 0.9f, size.height * 0.45f)
                            lineTo(size.width * 0.5f, size.height * 0.45f)
                            close()
                        }
                        drawPath(boltPath, MemberCardTokens.PillAmberText)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Prime",
                        color = MemberCardTokens.PillAmberText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                // Active Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MemberCardTokens.PillGreenBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(9.dp)
                    )
                    Spacer(modifier = Modifier.width(2.5.dp))
                    Text(
                        text = "Active",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right: Attendance Ring Gauge (56dp)
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val strokeWidth = 5.dp.toPx()
                val arcPadding = strokeWidth / 2f
                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                // Track
                drawArc(
                    color = Color(0xFFD1FAE5),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(arcPadding, arcPadding),
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )

                // Progress Arc
                val sweep = (profile.attendancePercent / 100f) * 360f
                drawArc(
                    brush = MemberCardTokens.AttendanceGradient,
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
                    text = "${profile.attendancePercent}%",
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${profile.attendedDays}/${profile.totalDays}d",
                    color = MemberCardTokens.SlateTitle,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 3. Compact Payment & Membership Strip
// ---------------------------------------------------------------------------
@Composable
private fun CheckInPaymentStrip(
    paymentPlan: PaymentPlanState,
    onReceiptClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Paid status & amount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Paid",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Paid ${paymentPlan.amountPaid}",
                        color = Color(0xFF047857),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "01 Sep '26",
                        color = MemberCardTokens.SlateMuted,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Center: Due Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Due: 30 Sep",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(${paymentPlan.daysLeft}d left)",
                        color = Color(0xFF2563EB),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right: View Receipt Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF6FF))
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(8.dp))
                    .clickable(onClick = onReceiptClick)
                    .padding(horizontal = 7.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Receipt ›",
                    color = Color(0xFF2563EB),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 4. Body Progress 4 Micro-Cards Row
// ---------------------------------------------------------------------------
@Composable
private fun CheckInBodyStatsRow(
    stats: List<BodyStatItem>,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        stats.forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                    .padding(horizontal = 4.dp, vertical = 5.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Top: Mini Badge + Label
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(item.lineColor)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = when (item.type) {
                                BodyStatType.WEIGHT -> "Weight"
                                BodyStatType.MUSCLE -> "Muscle"
                                BodyStatType.BODY_FAT -> "Fat"
                                BodyStatType.BMI -> "BMI"
                            },
                            color = MemberCardTokens.SlateMuted,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Value
                    Text(
                        text = item.value,
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    // Delta + Mini Sparkline
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val deltaColor = if (item.type == BodyStatType.BODY_FAT) Color(0xFFEF4444) else Color(0xFF10B981)
                        Text(
                            text = item.delta,
                            color = deltaColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Mini Sparkline Wave (height 10dp)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    ) {
                        if (item.sparklinePoints.isNotEmpty()) {
                            val points = item.sparklinePoints
                            val stepX = size.width / (points.size - 1)
                            val path = Path()

                            val startX = 0f
                            val startY = size.height * (1f - points[0])
                            path.moveTo(startX, startY)

                            for (i in 1 until points.size) {
                                val prevX = (i - 1) * stepX
                                val prevY = size.height * (1f - points[i - 1])
                                val curX = i * stepX
                                val curY = size.height * (1f - points[i])
                                val midX = (prevX + curX) / 2f
                                path.cubicTo(midX, prevY, midX, curY, curX, curY)
                            }

                            drawPath(
                                path = path,
                                color = item.lineColor,
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 5. Attendance Activity Timeline & Summary
// ---------------------------------------------------------------------------
@Composable
private fun CheckInAttendanceStrip(
    data: com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData,
    onHeaderClick: () -> Unit,
    onDayClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Title & Month Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Attendance Activity",
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${data.monthLabel} ›",
                    color = Color(0xFF2563EB),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onHeaderClick)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // 7 Recent Activity Days
            val recentDays = data.calendarDays.take(7)
            val weekdays = listOf("S", "M", "T", "W", "T", "F", "S")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                recentDays.forEachIndexed { idx, day ->
                    val isToday = (day.dayOfMonth == 17 || idx == 6)
                    val dotColor = when (day.status) {
                        DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                        DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                        DayAttendanceStatus.LATE -> Color(0xFFF59E0B)
                        DayAttendanceStatus.HOLIDAY -> Color(0xFF94A3B8)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isToday) Color(0xFFEFF6FF) else Color.Transparent)
                            .border(
                                width = if (isToday) 1.dp else 0.dp,
                                color = if (isToday) Color(0xFF93C5FD) else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onDayClick(day) }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = weekdays.getOrElse(idx) { "" },
                            color = if (isToday) Color(0xFF2563EB) else MemberCardTokens.SlateMuted,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = day.dayOfMonth.toString(),
                            color = MemberCardTokens.DarkNavy,
                            fontSize = 8.5.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Summary Count Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AttendanceSummaryItem(color = Color(0xFF10B981), label = "Present", count = data.presentCount)
                AttendanceSummaryItem(color = Color(0xFFEF4444), label = "Absent", count = data.absentCount)
                AttendanceSummaryItem(color = Color(0xFFF59E0B), label = "Late", count = data.lateCount)
                AttendanceSummaryItem(color = Color(0xFF64748B), label = "Holiday", count = data.holidayCount)
            }
        }
    }
}

@Composable
private fun AttendanceSummaryItem(
    color: Color,
    label: String,
    count: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(5.5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "$count $label",
            color = MemberCardTokens.SlateBody,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ---------------------------------------------------------------------------
// 6. Workout Trend 7-Day Performance Bars
// ---------------------------------------------------------------------------
@Composable
private fun CheckInWorkoutTrendRow(
    bars: List<WorkoutTrendBar>,
    avgMinutes: Int,
    peakMinutes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Workout Trend (7 Days)",
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Avg: ${avgMinutes}m  •  Peak: ${peakMinutes}m",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 7-Bar Chart Canvas
            val weekdays = listOf("M", "T", "W", "T", "F", "S", "S")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                bars.forEachIndexed { index, bar ->
                    val maxMinutes = 60f
                    val fraction = (bar.minutes / maxMinutes).coerceIn(0.15f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Value text
                        Text(
                            text = "${bar.minutes}m",
                            color = MemberCardTokens.SlateMuted,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        // Bar
                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .height((20 * fraction).dp)
                                .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                .background(bar.color)
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        // Weekday label
                        Text(
                            text = weekdays.getOrElse(index) { "" },
                            color = MemberCardTokens.SlateTitle,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 7. Verified Pass Footer & Quick Analytics Action
// ---------------------------------------------------------------------------
@Composable
private fun CheckInPassFooter(
    memberCode: String,
    onAnalyticsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // RFID / Pass text
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(12.dp)) {
                // Mini shield / RFID icon
                val stroke = Stroke(width = 1.2.dp.toPx())
                drawCircle(color = Color(0xFF10B981), radius = size.width * 0.45f, style = stroke)
                drawCircle(color = Color(0xFF10B981), radius = size.width * 0.2f)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "ID: $memberCode • RFID VERIFIED",
                color = MemberCardTokens.SlateBody,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Action Link
        Row(
            modifier = Modifier
                .clickable(onClick = onAnalyticsClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Analytics",
                color = Color(0xFF2563EB),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4FEFB)
@Composable
fun MemberCheckInCardPreview() {
    MemberCheckInCard(
        data = MemberCardData.sampleAmanTripathi(),
        modifier = Modifier.padding(16.dp)
    )
}

