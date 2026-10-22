package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.MonthPaymentItem
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

/**
 * BAD GYM — CANONICAL MEMBER INTELLIGENCE SCREEN & CARDS
 * Faithful pixel-perfect reproduction of the approved Bad Gym design:
 * - Header: Hamburger menu, BAD GYM (italic green) MEMBER INTELLIGENCE, Date badge, Notification bell with red badge.
 * - Member Hero Card: Ashwini Singh, active avatar with green ring & indicator, Spark Member & Active pills,
 *   18 Days Left circular progress ring, and Goal / Branch / Joined On metadata chips.
 * - Quick Action Pills: Check In (solid green), Body, Workout, Payment.
 * - Card 1: Body Stats (Weight 69.5kg, Muscle 57.2kg, Body Fat 17.6%, BMI 22.8 with sparklines & goals).
 * - Card 2: Attendance • Oct 2026 (77% circular gauge, 12-day day-of-week strip with Present/Absent/Closed legend).
 * - Card 3: Workout Trend • Oct 2026 (48m Avg/Day, colorful vertical daily bars with peak 52m and lowest 12m tooltips, 4 stat rows).
 * - Card 4: Payment & Plan • 12 Months (12-month calendar strip, Last Payment, Next Due, Collect Money button).
 */
@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
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
        // 1. TOP APP BAR
        BadGymTopAppBar(
            dateDisplay = data.profile.dateDisplay,
            notificationCount = data.profile.unreadNotifications,
            onMenuClick = onAvatarClick,
            onNotificationsClick = onNotificationsClick
        )

        // 2. HERO MEMBER PROFILE CARD
        HeroMemberProfileCard(
            profile = data.profile,
            onAvatarClick = onAvatarClick,
            onPlanClick = onReceiptClick
        )

        // 3. QUICK ACTION PILLS ROW
        QuickActionPillsRow(
            onCheckInClick = onAvatarClick,
            onBodyClick = onAnalyticsClick,
            onWorkoutClick = onAnalyticsClick,
            onPaymentClick = onReceiptClick
        )

        // 4. BODY STATS CARD
        BodyStatsSectionCard(
            bodyStats = data.bodyStats,
            onDetailsClick = onAnalyticsClick
        )

        // 5. ATTENDANCE CARD
        AttendanceSectionCard(
            attendance = data.attendance,
            onHeaderClick = onAttendanceClick,
            onDayClick = onAttendanceDayClick
        )

        // 6. WORKOUT TREND CARD
        WorkoutTrendSectionCard(
            workout = data.workoutTrend,
            onClick = onAnalyticsClick
        )

        // 7. PAYMENT & PLAN CARD
        PaymentPlanSectionCard(
            paymentPlan = data.paymentPlan,
            onReceiptClick = onReceiptClick
        )
    }
}

// =================================================================================================
// 1. TOP APP BAR
// =================================================================================================
@Composable
fun BadGymTopAppBar(
    dateDisplay: String,
    notificationCount: Int,
    onMenuClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Hamburger Menu + BAD GYM Brand
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Hamburger icon button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(modifier = Modifier.size(24.dp, 18.dp)) {
                    val stroke = 2.6.dp.toPx()
                    val color = Color(0xFF1E293B)
                    drawLine(color, Offset(0f, 2.dp.toPx()), Offset(size.width, 2.dp.toPx()), stroke, StrokeCap.Round)
                    drawLine(color, Offset(0f, size.height * 0.5f), Offset(size.width, size.height * 0.5f), stroke, StrokeCap.Round)
                    drawLine(color, Offset(0f, size.height - 2.dp.toPx()), Offset(size.width, size.height - 2.dp.toPx()), stroke, StrokeCap.Round)
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column {
                Text(
                    text = "BAD GYM",
                    color = Color(0xFF047857),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "MEMBER INTELLIGENCE",
                    color = Color(0xFF1E293B),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp
                )
            }
        }

        // Right: Date Pill + Notification Bell
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Date Card Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F8F0))
                    .border(1.2.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
                    .padding(horizontal = 9.dp, vertical = 4.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini green calendar icon
                Canvas(modifier = Modifier.size(16.dp)) {
                    val w = size.width
                    val h = size.height
                    // Calendar body
                    drawRoundRect(
                        color = Color(0xFF10B981),
                        topLeft = Offset(0f, h * 0.15f),
                        size = Size(w, h * 0.82f),
                        cornerRadius = CornerRadius(2.dp.toPx()),
                        style = Stroke(width = 1.4.dp.toPx())
                    )
                    // Top header fill
                    drawRoundRect(
                        color = Color(0xFF10B981),
                        topLeft = Offset(0f, h * 0.15f),
                        size = Size(w, h * 0.28f),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                    // Binding rings
                    drawCircle(Color(0xFF047857), radius = 1.2.dp.toPx(), center = Offset(w * 0.3f, h * 0.12f))
                    drawCircle(Color(0xFF047857), radius = 1.2.dp.toPx(), center = Offset(w * 0.7f, h * 0.12f))
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = "Tue",
                        color = Color(0xFF0F172A),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 11.sp
                    )
                    Text(
                        text = "22 Oct 2026",
                        color = Color(0xFF0F172A),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Notification Bell with Red Badge
            Box(
                modifier = Modifier.padding(top = 2.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .clickable(onClick = onNotificationsClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Red Badge
                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .offset(x = 3.dp, y = (-3).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$notificationCount",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

// =================================================================================================
// 2. HERO MEMBER PROFILE CARD
// =================================================================================================
@Composable
fun HeroMemberProfileCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit,
    onPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x1F059669),
                ambientColor = Color(0x0F059669)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF7FDF9),
                        Color(0xFFEFFBF4)
                    )
                )
            )
            .border(1.5.dp, Color(0xFFA7F3D0), RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Identity Row: Avatar + Name & Badges + Circular Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Circular Glowing Ring & Active Dot
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clickable(onClick = onAvatarClick)
                ) {
                    // Outer green glowing ring
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .align(Alignment.Center)
                            .border(2.5.dp, Color(0xFF10B981), CircleShape)
                            .padding(2.5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_ashwini),
                            contentDescription = "Ashwini Singh",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    // Green Active Indicator Dot
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFF34D399),
                                        Color(0xFF10B981),
                                        Color(0xFF047857)
                                    )
                                )
                            )
                            .border(2.dp, Color.White, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Middle Info: Name, Subtitle, Badges
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.name,
                        color = Color(0xFF0F172A),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "${profile.memberCode}   |   ${profile.age} yrs   |   ${profile.gender}",
                        color = Color(0xFF64748B),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Spark Member Badge (Green Crown Pill)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF059669))
                                .clickable(onClick = onPlanClick)
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(modifier = Modifier.size(11.dp)) {
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
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = profile.tierName,
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Active Badge (Green Checkmark Pill)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF059669))
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Active",
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Right: Circular Progress Ring "18 Days Left of 30"
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(68.dp)) {
                        val stroke = 5.dp.toPx()
                        val diameter = size.minDimension - stroke
                        val topLeft = Offset(stroke / 2, stroke / 2)
                        val arcSize = Size(diameter, diameter)

                        // Light Base Track
                        drawArc(
                            color = Color(0xFFD1FAE5),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )

                        // Vibrant Emerald Progress Arc
                        val sweep = 360f * (profile.daysLeft.toFloat() / profile.totalDays.toFloat().coerceAtLeast(1f))
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Mini Calendar icon at top
                        Canvas(modifier = Modifier.size(11.dp)) {
                            drawRoundRect(
                                color = Color(0xFF10B981),
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.85f),
                                cornerRadius = CornerRadius(1.dp.toPx()),
                                style = Stroke(width = 1.dp.toPx())
                            )
                            drawRoundRect(
                                color = Color(0xFF10B981),
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.3f),
                                cornerRadius = CornerRadius(1.dp.toPx())
                            )
                        }

                        Text(
                            text = "${profile.daysLeft}",
                            color = Color(0xFF0F172A),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Days Left",
                            color = Color(0xFF1E293B),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                        Text(
                            text = "of ${profile.totalDays}",
                            color = Color(0xFF64748B),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 9.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom 3 Info Chips: Goal, Branch, Joined On
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Goal Chip
                MetadataInfoChip(
                    iconBgColor = Color(0xFF8B5CF6),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawCircle(Color.White, radius = size.width * 0.45f, style = Stroke(width = 1.3.dp.toPx()))
                            drawCircle(Color.White, radius = size.width * 0.22f, style = Stroke(width = 1.3.dp.toPx()))
                            drawCircle(Color.White, radius = size.width * 0.08f)
                        }
                    },
                    label = "Goal",
                    value = profile.goal,
                    modifier = Modifier.weight(1f)
                )

                // Branch Chip
                MetadataInfoChip(
                    iconBgColor = Color(0xFFEF4444),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            val w = size.width
                            val h = size.height
                            val pinPath = Path().apply {
                                moveTo(w * 0.5f, h * 0.95f)
                                cubicTo(w * 0.15f, h * 0.6f, w * 0.1f, h * 0.35f, w * 0.5f, h * 0.1f)
                                cubicTo(w * 0.9f, h * 0.35f, w * 0.85f, h * 0.6f, w * 0.5f, h * 0.95f)
                                close()
                            }
                            drawPath(pinPath, Color.White)
                            drawCircle(color = Color(0xFFEF4444), radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.38f))
                        }
                    },
                    label = "Branch",
                    value = profile.branch,
                    modifier = Modifier.weight(1.05f)
                )

                // Joined On Chip
                MetadataInfoChip(
                    iconBgColor = Color(0xFF1E293B),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.8f),
                                cornerRadius = CornerRadius(1.5.dp.toPx()),
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                            drawLine(
                                color = Color.White,
                                start = Offset(0f, size.height * 0.42f),
                                end = Offset(size.width, size.height * 0.42f),
                                strokeWidth = 1.2.dp.toPx()
                            )
                        }
                    },
                    label = "Joined On",
                    value = profile.joinedDate,
                    modifier = Modifier.weight(1.15f)
                )
            }
        }
    }
}

@Composable
private fun MetadataInfoChip(
    iconBgColor: Color,
    iconContent: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            iconContent()
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                color = Color(0xFF64748B),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 11.sp
            )
            Text(
                text = value,
                color = Color(0xFF0F172A),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                lineHeight = 13.sp
            )
        }
    }
}

// =================================================================================================
// 3. QUICK ACTION PILLS ROW
// =================================================================================================
@Composable
fun QuickActionPillsRow(
    onCheckInClick: () -> Unit,
    onBodyClick: () -> Unit,
    onWorkoutClick: () -> Unit,
    onPaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Check In (Solid Emerald Green)
        Row(
            modifier = Modifier
                .weight(1.1f)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF059669))
                .clickable(onClick = onCheckInClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier.size(15.dp)) {
                // Exit/Door frame
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width * 0.45f, size.height),
                    cornerRadius = CornerRadius(1.5.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx())
                )
                // Arrow right
                val arrowPath = Path().apply {
                    moveTo(size.width * 0.35f, size.height * 0.5f)
                    lineTo(size.width * 0.95f, size.height * 0.5f)
                    moveTo(size.width * 0.7f, size.height * 0.25f)
                    lineTo(size.width * 0.95f, size.height * 0.5f)
                    lineTo(size.width * 0.7f, size.height * 0.75f)
                }
                drawPath(arrowPath, Color.White, style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Check In",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2. Body (White Frosted, Purple Dumbbell)
        QuickActionFrostedPill(
            icon = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    drawLine(color = Color(0xFF8B5CF6), start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.8.dp.toPx())
                    drawRoundRect(color = Color(0xFF8B5CF6), topLeft = Offset(w * 0.1f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                    drawRoundRect(color = Color(0xFF8B5CF6), topLeft = Offset(w * 0.76f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                }
            },
            label = "Body",
            onClick = onBodyClick,
            modifier = Modifier.weight(1f)
        )

        // 3. Workout (White Frosted, Blue Chart)
        QuickActionFrostedPill(
            icon = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(w * 0.1f, h * 0.5f), size = Size(w * 0.22f, h * 0.5f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(w * 0.4f, h * 0.25f), size = Size(w * 0.22f, h * 0.75f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(w * 0.7f, h * 0.05f), size = Size(w * 0.22f, h * 0.95f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                }
            },
            label = "Workout",
            onClick = onWorkoutClick,
            modifier = Modifier.weight(1.05f)
        )

        // 4. Payment (White Frosted, Amber Wallet)
        QuickActionFrostedPill(
            icon = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    drawRoundRect(
                        color = Color(0xFFF59E0B),
                        topLeft = Offset(0f, size.height * 0.18f),
                        size = Size(size.width, size.height * 0.68f),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                    drawCircle(color = Color.White, radius = 1.5.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.52f))
                }
            },
            label = "Payment",
            onClick = onPaymentClick,
            modifier = Modifier.weight(1.05f)
        )
    }
}

@Composable
private fun QuickActionFrostedPill(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            color = Color(0xFF0F172A),
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// =================================================================================================
// 4. CARD 1: BODY STATS
// =================================================================================================
@Composable
fun BodyStatsSectionCard(
    bodyStats: List<BodyStatItem>,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x148B5CF6)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE9D5FF), RoundedCornerShape(22.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Purple Icon + Body Stats + Last 6 Months >
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
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.8.dp.toPx())
                            drawRoundRect(color = Color.White, topLeft = Offset(w * 0.1f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                            drawRoundRect(color = Color.White, topLeft = Offset(w * 0.76f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "Body Stats",
                        color = Color(0xFF0F172A),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFF3E8FF))
                        .clickable(onClick = onDetailsClick)
                        .padding(horizontal = 8.dp, vertical = 3.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last 6 Months",
                        color = Color(0xFF7C3AED),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Stat Columns in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 1. Weight (69.5 kg, Goal: 65 kg)
                val weightItem = bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT }
                BodyStatColumn(
                    iconBg = Color(0xFFDCFCE7),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawRoundRect(
                                color = Color(0xFF10B981),
                                topLeft = Offset(0f, 0f),
                                size = Size(size.width, size.height),
                                cornerRadius = CornerRadius(2.dp.toPx()),
                                style = Stroke(width = 1.3.dp.toPx())
                            )
                            drawCircle(Color(0xFF10B981), radius = 2.dp.toPx(), center = Offset(size.width * 0.5f, size.height * 0.35f))
                        }
                    },
                    label = "Weight",
                    value = weightItem?.value ?: "69.5",
                    unit = "kg",
                    delta = "↓ 2.5 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineContent = {
                        SmoothSparklineWave(color = Color(0xFF10B981))
                    },
                    goalText = "Goal: 65 kg",
                    modifier = Modifier.weight(1f)
                )

                // 2. Muscle (57.2, Goal: 60 kg)
                val muscleItem = bodyStats.firstOrNull { it.type == BodyStatType.MUSCLE }
                BodyStatColumn(
                    iconBg = Color(0xFFF3E8FF),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawLine(color = Color(0xFF8B5CF6), start = Offset(size.width * 0.2f, size.height * 0.5f), end = Offset(size.width * 0.8f, size.height * 0.5f), strokeWidth = 1.5.dp.toPx())
                            drawRoundRect(color = Color(0xFF8B5CF6), topLeft = Offset(size.width * 0.1f, size.height * 0.25f), size = Size(size.width * 0.15f, size.height * 0.5f), cornerRadius = CornerRadius(1.dp.toPx()))
                            drawRoundRect(color = Color(0xFF8B5CF6), topLeft = Offset(size.width * 0.75f, size.height * 0.25f), size = Size(size.width * 0.15f, size.height * 0.5f), cornerRadius = CornerRadius(1.dp.toPx()))
                        }
                    },
                    label = "Muscle",
                    value = muscleItem?.value?.replace(" kg", "") ?: "57.2",
                    unit = "",
                    delta = "↑ 1.4 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineContent = {
                        MiniVerticalBarsSparkline(color = Color(0xFF8B5CF6))
                    },
                    goalText = "Goal: 60 kg",
                    modifier = Modifier.weight(1f)
                )

                // 3. Body Fat (17.6%, Goal: 15%)
                val fatItem = bodyStats.firstOrNull { it.type == BodyStatType.BODY_FAT }
                BodyStatColumn(
                    iconBg = Color(0xFFFEE2E2),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawCircle(Color(0xFFEF4444), radius = 2.dp.toPx(), center = Offset(size.width * 0.3f, size.height * 0.3f))
                            drawCircle(Color(0xFFEF4444), radius = 2.dp.toPx(), center = Offset(size.width * 0.7f, size.height * 0.7f))
                            drawLine(Color(0xFFEF4444), start = Offset(size.width * 0.8f, size.height * 0.2f), end = Offset(size.width * 0.2f, size.height * 0.8f), strokeWidth = 1.3.dp.toPx())
                        }
                    },
                    label = "Body Fat",
                    value = fatItem?.value ?: "17.6%",
                    unit = "",
                    delta = "↓ 1.2%",
                    deltaColor = Color(0xFFEF4444),
                    sparklineContent = {
                        SmoothSparklineWave(color = Color(0xFFEF4444))
                    },
                    goalText = "Goal: 15%",
                    hasTrophy = true,
                    modifier = Modifier.weight(1f)
                )

                // 4. BMI (22.8, Normal, 18.5 - 24.9)
                val bmiItem = bodyStats.firstOrNull { it.type == BodyStatType.BMI }
                BodyStatColumn(
                    iconBg = Color(0xFFDBEAFE),
                    iconContent = {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawCircle(Color(0xFF2563EB), radius = 3.dp.toPx(), center = Offset(size.width * 0.5f, size.height * 0.3f))
                            drawArc(
                                color = Color(0xFF2563EB),
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = false,
                                topLeft = Offset(size.width * 0.2f, size.height * 0.55f),
                                size = Size(size.width * 0.6f, size.height * 0.45f),
                                style = Stroke(width = 1.3.dp.toPx())
                            )
                        }
                    },
                    label = "BMI",
                    value = bmiItem?.value ?: "22.8",
                    unit = "",
                    delta = "Normal",
                    deltaColor = Color(0xFF059669),
                    isPillBadge = true,
                    sparklineContent = {
                        SmoothSparklineWave(color = Color(0xFF2563EB))
                    },
                    goalText = "18.5 – 24.9",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BodyStatColumn(
    iconBg: Color,
    iconContent: @Composable () -> Unit,
    label: String,
    value: String,
    unit: String,
    delta: String,
    deltaColor: Color,
    isPillBadge: Boolean = false,
    hasTrophy: Boolean = false,
    sparklineContent: @Composable () -> Unit,
    goalText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Icon + Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                iconContent()
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Main Metric (e.g. 69.5 kg)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = Color(0xFF0F172A),
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
            )
            if (unit.isNotEmpty()) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    color = Color(0xFF0F172A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Delta or Status Badge
        if (isPillBadge) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFDCFCE7))
                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
            ) {
                Text(
                    text = delta,
                    color = deltaColor,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text(
                text = delta,
                color = deltaColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Sparkline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            sparklineContent()
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Goal Text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (hasTrophy) {
                Canvas(modifier = Modifier.size(10.dp)) {
                    drawCircle(Color(0xFFF59E0B), radius = size.width * 0.45f)
                }
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text(
                text = goalText,
                color = Color(0xFF64748B),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SmoothSparklineWave(color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(18.dp)) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, h * 0.6f)
            cubicTo(w * 0.25f, h * 0.3f, w * 0.5f, h * 0.8f, w * 0.75f, h * 0.35f)
            lineTo(w, h * 0.5f)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
private fun MiniVerticalBarsSparkline(color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(18.dp)) {
        val count = 8
        val barWidth = 2.5.dp.toPx()
        val totalSpace = size.width
        val step = totalSpace / (count + 1)
        val heights = listOf(0.3f, 0.5f, 0.45f, 0.7f, 0.6f, 0.85f, 0.75f, 0.95f)

        for (i in 0 until count) {
            val barH = size.height * heights[i]
            val x = (i + 1) * step - barWidth / 2
            val y = size.height - barH
            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barH),
                cornerRadius = CornerRadius(1.dp.toPx())
            )
        }
    }
}

// =================================================================================================
// 5. CARD 2: ATTENDANCE • OCT 2026
// =================================================================================================
@Composable
fun AttendanceSectionCard(
    attendance: AttendanceSectionData,
    onHeaderClick: () -> Unit,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x14059669)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFBBF7D0), RoundedCornerShape(22.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Calendar Icon + Attendance • Oct 2026 + 24/31 >
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
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.8f),
                                cornerRadius = CornerRadius(1.5.dp.toPx()),
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                            drawLine(Color.White, Offset(0f, size.height * 0.42f), Offset(size.width, size.height * 0.42f), 1.2.dp.toPx())
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "Attendance",
                        color = Color(0xFF0F172A),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " • Oct 2026",
                        color = Color(0xFF047857),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFDCFCE7))
                        .clickable(onClick = onHeaderClick)
                        .padding(horizontal = 8.dp, vertical = 3.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "24/31",
                        color = Color(0xFF047857),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body: Circular Gauge (Left) + 12-Day Calendar Strip (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Ring 77%
                Box(
                    modifier = Modifier.size(66.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(66.dp)) {
                        val stroke = 6.dp.toPx()
                        val diameter = size.minDimension - stroke
                        val topLeft = Offset(stroke / 2, stroke / 2)
                        val arcSize = Size(diameter, diameter)

                        // Base track
                        drawArc(
                            color = Color(0xFFD1FAE5),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )

                        // 77% Arc
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = -90f,
                            sweepAngle = 360f * (attendance.percent.toFloat() / 100f),
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${attendance.percent}%",
                            color = Color(0xFF0F172A),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "This Month",
                            color = Color(0xFF64748B),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right Calendar Day Strip
                Column(modifier = Modifier.weight(1f)) {
                    val days = attendance.calendarDays.take(12)

                    // Day of Week Initials Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEach { day ->
                            Text(
                                text = day.dayOfWeekInitial.ifEmpty { "D" },
                                color = Color(0xFF64748B),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(19.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date Number Pills Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEach { day ->
                            val (bgColor, textColor) = when {
                                day.dayOfMonth == 0 -> Color(0xFFE2E8F0) to Color(0xFF94A3B8)
                                day.dayOfMonth == 22 -> Color(0xFFF59E0B) to Color.White // Today/Active Amber
                                day.status == DayAttendanceStatus.PRESENT -> Color(0xFF10B981) to Color.White
                                day.status == DayAttendanceStatus.ABSENT -> Color(0xFFEF4444) to Color.White
                                else -> Color(0xFF94A3B8) to Color.White // Closed / Sun
                            }

                            Box(
                                modifier = Modifier
                                    .size(19.dp, 20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(bgColor)
                                    .clickable { onDayClick(day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (day.dayOfMonth > 0) "${day.dayOfMonth}" else "-",
                                    color = textColor,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Legend: Present, Absent, Closed (Sun)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Present", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Absent", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Closed (Sun)", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

// =================================================================================================
// 6. CARD 3: WORKOUT TREND • OCT 2026
// =================================================================================================
@Composable
fun WorkoutTrendSectionCard(
    workout: WorkoutTrendData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x142563EB)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFBFDBFE), RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Blue Icon + Workout Trend • Oct 2026 + 30 Days >
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
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.8.dp.toPx())
                            drawRoundRect(color = Color.White, topLeft = Offset(w * 0.1f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                            drawRoundRect(color = Color.White, topLeft = Offset(w * 0.76f, h * 0.22f), size = Size(w * 0.14f, h * 0.56f), cornerRadius = CornerRadius(1.dp.toPx()))
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "Workout Trend",
                        color = Color(0xFF0F172A),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " • Oct 2026",
                        color = Color(0xFF2563EB),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFDBEAFE))
                        .padding(horizontal = 8.dp, vertical = 3.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "30 Days",
                        color = Color(0xFF2563EB),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body: Left (48m Avg), Middle (Bar Chart with Tooltips), Right (4 Stats)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: 48m Avg / Day +12% vs Sep
                Column(
                    modifier = Modifier.width(78.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "${workout.avgPerDayMinutes}m",
                        color = Color(0xFF0F172A),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 25.sp
                    )
                    Text(
                        text = "Avg / Day",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = workout.trendVsLastMonth,
                        color = Color(0xFF10B981),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Middle: Daily Bars with Peak & Lowest Tooltips
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(82.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    // Bars Canvas
                    DailyWorkoutBarsCanvas(
                        bars = workout.bars,
                        modifier = Modifier.fillMaxWidth().height(52.dp).align(Alignment.BottomCenter)
                    )

                    // 10 Oct Peak Tooltip (52m)
                    Box(
                        modifier = Modifier
                            .offset(x = 24.dp, y = 0.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏆", fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Column {
                                Text(text = "${workout.peakMinutes}m", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF92400E), lineHeight = 10.sp)
                                Text(text = workout.peakDate, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), lineHeight = 8.sp)
                            }
                        }
                    }

                    // 16 Oct Lowest Tooltip (12m)
                    Box(
                        modifier = Modifier
                            .offset(x = 64.dp, y = 14.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEE2E2))
                            .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⬇", fontSize = 8.sp, color = Color(0xFFDC2626))
                            Spacer(modifier = Modifier.width(2.dp))
                            Column {
                                Text(text = "${workout.lowestMinutes}m", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626), lineHeight = 10.sp)
                                Text(text = workout.lowestDate, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626), lineHeight = 8.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: 4 Summary Items
                Column(
                    modifier = Modifier.width(115.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Row 1: Peak
                    WorkoutSummaryRow(
                        iconEmoji = "🏆",
                        highlightText = "${workout.peakMinutes}m",
                        label = "Peak (${workout.peakDate})"
                    )

                    // Row 2: Lowest
                    WorkoutSummaryRow(
                        iconEmoji = "⬇",
                        highlightText = "${workout.lowestMinutes}m",
                        label = "Lowest (${workout.lowestDate})"
                    )

                    // Row 3: With Workout
                    WorkoutSummaryRow(
                        iconEmoji = "📊",
                        highlightText = "${workout.daysWithWorkout} Days",
                        label = "With Workout"
                    )

                    // Row 4: No Workout
                    WorkoutSummaryRow(
                        iconEmoji = "⏸",
                        highlightText = "${workout.daysNoWorkout} Days",
                        label = "No Workout"
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyWorkoutBarsCanvas(
    bars: List<WorkoutTrendBar>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (bars.isEmpty()) return@Canvas
        val count = bars.size
        val barWidth = 4.dp.toPx()
        val spacing = (size.width - (count * barWidth)) / (count - 1).coerceAtLeast(1)

        bars.forEachIndexed { index, item ->
            val barHeight = size.height * (item.minutes.toFloat() / 55f).coerceIn(0.15f, 1f)
            val x = index * (barWidth + spacing)
            val y = size.height - barHeight

            drawRoundRect(
                color = item.color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

@Composable
private fun WorkoutSummaryRow(
    iconEmoji: String,
    highlightText: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = iconEmoji, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Row {
                Text(
                    text = highlightText,
                    color = Color(0xFF0F172A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = label,
                    color = Color(0xFF64748B),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// =================================================================================================
// 7. CARD 4: PAYMENT & PLAN • 12 MONTHS
// =================================================================================================
@Composable
fun PaymentPlanSectionCard(
    paymentPlan: PaymentPlanState,
    onReceiptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x14EA580C)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFFED7AA), RoundedCornerShape(22.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Red Wallet + Payment & Plan • 12 Months + View All >
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
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, size.height * 0.18f),
                                size = Size(size.width, size.height * 0.68f),
                                cornerRadius = CornerRadius(2.dp.toPx())
                            )
                            drawCircle(Color(0xFFEF4444), radius = 1.3.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.52f))
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "Payment & Plan",
                        color = Color(0xFF0F172A),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " • 12 Months",
                        color = Color(0xFFEA580C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View All",
                        color = Color(0xFFEA580C),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFEA580C),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 12-Month Calendar Grid / Strip
            val months = paymentPlan.monthlyHistory.ifEmpty {
                listOf(
                    MonthPaymentItem("Jan", "--"),
                    MonthPaymentItem("Feb", "--"),
                    MonthPaymentItem("Mar", "--"),
                    MonthPaymentItem("Apr", "₹1.2K", "Gold"),
                    MonthPaymentItem("May", "₹1.2K", "Gold"),
                    MonthPaymentItem("Jun", "₹1.05K", "-₹150", isDiscount = true),
                    MonthPaymentItem("Jul", "Off", isOff = true),
                    MonthPaymentItem("Aug", "Off", isOff = true),
                    MonthPaymentItem("Sep", "₹800", "Silver"),
                    MonthPaymentItem("Oct", "₹600", "Spark", isCurrent = true),
                    MonthPaymentItem("Nov", "--"),
                    MonthPaymentItem("Dec", "--")
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                months.forEach { m ->
                    MonthPillItem(item = m)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Summary Row: Last Payment + Next Due + Collect Money >
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Last Payment Box
                Row(
                    modifier = Modifier
                        .weight(1.05f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0FDF4))
                        .border(1.dp, Color(0xFFDCFCE7), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(text = "Last Payment", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.SemiBold, lineHeight = 11.sp)
                        Text(text = paymentPlan.lastPaymentAmount, color = Color(0xFF0F172A), fontSize = 15.sp, fontWeight = FontWeight.Black, lineHeight = 16.sp)
                        Text(text = paymentPlan.lastPaymentDate, color = Color(0xFF64748B), fontSize = 9.5.sp, fontWeight = FontWeight.Medium, lineHeight = 10.sp)
                    }
                }

                // Next Due Box
                Row(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFEE2E2), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(13.dp)) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(0f, size.height * 0.15f),
                                size = Size(size.width, size.height * 0.8f),
                                cornerRadius = CornerRadius(1.5.dp.toPx()),
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                            drawLine(Color.White, Offset(0f, size.height * 0.42f), Offset(size.width, size.height * 0.42f), 1.2.dp.toPx())
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(text = "Next Due", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.SemiBold, lineHeight = 11.sp)
                        Text(text = paymentPlan.nextDueDate, color = Color(0xFFDC2626), fontSize = 13.5.sp, fontWeight = FontWeight.Black, lineHeight = 15.sp)
                        Text(text = "${paymentPlan.daysLeft} Days Left", color = Color(0xFFDC2626), fontSize = 9.5.sp, fontWeight = FontWeight.Bold, lineHeight = 10.sp)
                    }
                }

                // Collect Money > Button
                Row(
                    modifier = Modifier
                        .weight(1.25f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF059669))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 10.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "₹",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Collect Money >",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthPillItem(item: MonthPaymentItem) {
    Column(
        modifier = Modifier.width(42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.month,
            color = Color(0xFF64748B),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(3.dp))

        val (bgColor, borderColor) = when {
            item.isCurrent -> Color(0xFFDCFCE7) to Color(0xFF10B981) // Current Oct
            item.tierOrDiscount == "Gold" -> Color(0xFFFEF3C7) to Color(0xFFF59E0B) // Apr, May
            item.isDiscount -> Color(0xFFFEF3C7) to Color(0xFFF59E0B) // Jun
            item.tierOrDiscount == "Silver" -> Color(0xFFDBEAFE) to Color(0xFF3B82F6) // Sep
            item.isOff -> Color(0xFFF1F5F9) to Color(0xFFCBD5E1) // Jul, Aug
            else -> Color(0xFFF8FAFC) to Color(0xFFE2E8F0) // Empty
        }

        Box(
            modifier = Modifier
                .size(42.dp, 36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (item.amount == "--") {
                    Text(text = "---", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else if (item.isOff) {
                    Text(text = "Off", color = Color(0xFF64748B), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text(
                        text = item.amount,
                        color = Color(0xFF0F172A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 11.sp
                    )
                    if (item.isDiscount) {
                        Text(
                            text = item.tierOrDiscount,
                            color = Color(0xFF047857),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 9.sp
                        )
                    } else if (item.tierOrDiscount.isNotEmpty()) {
                        Text(
                            text = item.tierOrDiscount,
                            color = when (item.tierOrDiscount) {
                                "Gold" -> Color(0xFF92400E)
                                "Silver" -> Color(0xFF1E40AF)
                                "Spark" -> Color(0xFF047857)
                                else -> Color(0xFF64748B)
                            },
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 9.sp
                        )
                    }
                }
            }
        }
    }
}
