package com.example.badnewgym.feature.membercard.presentation.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

/**
 * Compact Viewport Member Check-In Screen
 * 100% Zero-Scroll Viewport Layout matching the user's reference visual specification.
 * Fits Hero Header, Body Stats, Payment & Plan, Attendance, Workout Trend, and
 * View Detailed Analytics button directly within the viewport height.
 */
@Composable
fun CompactViewportMemberScreen(
    data: MemberCardData,
    onToggleTheme: () -> Unit,
    onReceiptClick: () -> Unit,
    onAttendanceClick: () -> Unit,
    onAttendanceDayClick: (CalendarDay) -> Unit,
    onAnalyticsClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE0F2FE),
                        Color(0xFFF0FAF6),
                        Color(0xFFE8F6FB)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        // Master Capsule Container matching reference mockup
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(26.dp),
                    spotColor = Color(0x350284C7),
                    ambientColor = Color(0x200284C7)
                )
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF031A30),
                            Color(0xFF0A2E4C),
                            Color(0xFFEBF7FF),
                            Color(0xFFF8FCFF)
                        ),
                        startY = 0f,
                        endY = 500f
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF38BDF8),
                            Color(0x4038BDF8),
                            Color(0x20FFFFFF),
                            Color(0x40BAE6FD)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
        ) {
            // 1. Top Hero Header Card: BAD GYM MEMBER + Profile + Radial Gauge
            CompactHeroHeaderCard(
                profile = data.profile,
                onToggleTheme = onToggleTheme,
                onMenuClick = onMenuClick
            )

            // Inner Content Container for White Cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F8FC))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 2. Body Stats Card (4 Micro-Cards with Sparklines)
                CompactBodyStatsCard(
                    stats = data.bodyStats,
                    onClick = onAnalyticsClick
                )

                // 3. Payment & Plan Card (3 Sub-Cards: Paid, Next Due, Receipt)
                CompactPaymentPlanCard(
                    payment = data.paymentPlan,
                    onReceiptClick = onReceiptClick
                )

                // 4. Attendance Card (Donut Gauge + Legend + Dot Matrix)
                CompactAttendanceCard(
                    attendance = data.attendance,
                    onAttendanceClick = onAttendanceClick,
                    onDayClick = onAttendanceDayClick
                )

                // 5. Workout Trend Card (30-day Bar Chart + 3 Metric Chips)
                CompactWorkoutTrendCard(
                    workout = data.workoutTrend,
                    onClick = onAnalyticsClick
                )

                // 6. View Detailed Analytics Action Button
                CompactAnalyticsButton(
                    onClick = onAnalyticsClick
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 1. COMPACT HERO HEADER CARD (BAD GYM MEMBER)
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactHeroHeaderCard(
    profile: MemberProfile,
    onToggleTheme: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF031A30),
                        Color(0xFF063A63),
                        Color(0xFF0284C7)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: BAD GYM + MEMBER + Theme Switcher + Options Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BAD",
                            color = Color(0xFF00E676),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic
                        )
                        Text(
                            text = " GYM",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Text(
                        text = "M E M B E R",
                        color = Color(0xFF67E8F9),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.5.sp,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Theme Switcher Toggle Pill (Dark Aurora / Compact)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0B253A))
                            .border(1.dp, Color(0xFF0284C7), RoundedCornerShape(12.dp))
                            .clickable(onClick = onToggleTheme)
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🌙 Dark Aurora",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }

                    // Options Menu Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onMenuClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Profile Info Row + Radial Gauge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Crown & Status Dot
                Box(
                    modifier = Modifier.size(54.dp)
                ) {
                    // Outer Ring + Image
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                            .border(2.dp, Color(0xFF00E676), CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_aman),
                            contentDescription = profile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Crown (Top-Left)
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(Color(0xFFFBBF24)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 8.sp)
                    }

                    // Green Online Dot (Bottom-Right)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF031A30))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Name & Metadata Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "${profile.memberCode} | ${profile.age} yrs | ${profile.gender}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFBAE6FD)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Prime Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "⚡ Prime",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }

                        // Active Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFD1FAE5))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "✓ Active",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }

                        // Joined Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0A2E4C))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "📅 Joined 01 Sep 26",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7DD3FC)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Radial Gauge (75% • 25 Days Left)
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 5.dp.toPx()
                        // Track
                        drawArc(
                            color = Color(0x3300E676),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00B0FF), Color(0xFF00E676))
                            ),
                            startAngle = -90f,
                            sweepAngle = (profile.attendancePercent / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${profile.attendancePercent}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "${profile.daysLeft} Days Left",
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFBAE6FD)
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 2. COMPACT BODY STATS CARD (4 MICRO-CARDS WITH SPARKLINES)
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactBodyStatsCard(
    stats: List<BodyStatItem>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Body Stats",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4 Micro Cards in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                stats.forEach { item ->
                    CompactBodyStatBox(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactBodyStatBox(
    item: BodyStatItem,
    modifier: Modifier = Modifier
) {
    val iconBg = when (item.type) {
        BodyStatType.WEIGHT -> Color(0xFF10B981)
        BodyStatType.MUSCLE -> Color(0xFF8B5CF6)
        BodyStatType.BODY_FAT -> Color(0xFFEF4444)
        BodyStatType.BMI -> Color(0xFF2563EB)
    }

    val iconSymbol = when (item.type) {
        BodyStatType.WEIGHT -> "⚖"
        BodyStatType.MUSCLE -> "💪"
        BodyStatType.BODY_FAT -> "%"
        BodyStatType.BMI -> "BMI"
    }

    val bgBrush = when (item.type) {
        BodyStatType.WEIGHT -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE8FDF0)))
        BodyStatType.MUSCLE -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF5EEFD)))
        BodyStatType.BODY_FAT -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFEECEE)))
        BodyStatType.BMI -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEDF6FF)))
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgBrush)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconSymbol,
                    fontSize = 7.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.value,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = item.delta,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = item.lineColor
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Micro Sparkline (Smooth Cubic Bezier curve)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {
            if (item.sparklinePoints.size >= 2) {
                val stepX = size.width / (item.sparklinePoints.size - 1)
                val path = Path()
                val points = item.sparklinePoints.mapIndexed { i, pt ->
                    Offset(i * stepX, size.height - (pt * (size.height - 4.dp.toPx())) - 2.dp.toPx())
                }
                path.moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val cx = (p0.x + p1.x) / 2f
                    path.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                }

                drawPath(
                    path = path,
                    color = item.lineColor,
                    style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 3. COMPACT PAYMENT & PLAN CARD (PAID, NEXT DUE, RECEIPT)
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactPaymentPlanCard(
    payment: PaymentPlanState,
    onReceiptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Wallet Icon + Title + Prime Plan Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFF43F5E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💳", fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Payment & Plan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                // Golden Plan Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFFBEB))
                        .border(0.8.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡", fontSize = 8.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Prime Plan (1 Month) 01 Sep – 30 Sep 2026",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 3 Sub-Cards: Paid • On Time, Next Due, Receipt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Paid Status
                Row(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF0FDF4))
                        .border(0.8.dp, Color(0xFFDCFCE7), RoundedCornerShape(10.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Paid • On Time",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "₹2,800",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "01 Sep 2026",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Next Due
                Row(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(0.8.dp, Color(0xFFDBEAFE), RoundedCornerShape(10.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF2563EB)),
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
                    Column {
                        Text(
                            text = "Next Due",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                        Text(
                            text = "30 Sep 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFDBEAFE))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "25 Days Left",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D4ED8)
                            )
                        }
                    }
                }

                // View Receipt Button
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(0.8.dp, Color(0xFFBFDBFE), RoundedCornerShape(10.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📄", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Receipt >",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 4. COMPACT ATTENDANCE CARD (DONUT + LEGEND + 7-COL DOT MATRIX)
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactAttendanceCard(
    attendance: AttendanceSectionData,
    onAttendanceClick: () -> Unit,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAttendanceClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Body: Donut + Legend + Calendar Matrix
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Donut Gauge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clickable(onClick = onAttendanceClick),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 5.5.dp.toPx()
                        drawArc(
                            color = Color(0xFFD1FAE5),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke)
                        )
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = -90f,
                            sweepAngle = (attendance.percent / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${attendance.percent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "${attendance.attendedDays}/${attendance.totalDays}\nDays",
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            lineHeight = 7.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Legend List
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    CompactLegendItem("Present", attendance.presentCount, Color(0xFF10B981))
                    CompactLegendItem("Absent", attendance.absentCount, Color(0xFFEF4444))
                    CompactLegendItem("Late", attendance.lateCount, Color(0xFFF59E0B))
                    CompactLegendItem("Holiday", attendance.holidayCount, Color(0xFF64748B))
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 7-Column Dot Matrix Grid (S M T W T F S)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Day of Week Header
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                            Text(
                                text = day,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                modifier = Modifier.width(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // 4 Weeks of Dots
                    for (week in 0 until 4) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(vertical = 1.5.dp)
                        ) {
                            for (col in 0 until 7) {
                                val dayIndex = week * 7 + col
                                val day = attendance.calendarDays.getOrNull(dayIndex)
                                val dotColor = when (day?.status) {
                                    DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                                    DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                                    DayAttendanceStatus.LATE -> Color(0xFFF59E0B)
                                    DayAttendanceStatus.HOLIDAY -> Color(0xFF94A3B8)
                                    null -> Color(0xFFE2E8F0)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                        .clickable {
                                            if (day != null) onDayClick(day)
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactLegendItem(label: String, count: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B),
            modifier = Modifier.width(36.dp)
        )
        Text(
            text = "$count",
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
    }
}

// -----------------------------------------------------------------------------------------
// 5. COMPACT WORKOUT TREND CARD (30-DAY BAR CHART + 3 STAT CHIPS)
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactWorkoutTrendCard(
    workout: WorkoutTrendData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏋️", fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Workout Trend",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Body: Left Bar Chart + Right 3 Stat Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 30-Day Bar Chart with grid and X-axis day marks
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        // Axis lines (60m, 30m, 0m)
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 0.5.dp.toPx()
                            val gridColor = Color(0xFFE2E8F0)
                            drawLine(gridColor, Offset(16.dp.toPx(), 0f), Offset(size.width, 0f), strokeWidth = stroke)
                            drawLine(gridColor, Offset(16.dp.toPx(), size.height / 2f), Offset(size.width, size.height / 2f), strokeWidth = stroke)
                            drawLine(gridColor, Offset(16.dp.toPx(), size.height), Offset(size.width, size.height), strokeWidth = stroke)
                        }

                        // Axis Labels
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("60m", fontSize = 6.sp, color = Color(0xFF94A3B8))
                            Text("30m", fontSize = 6.sp, color = Color(0xFF94A3B8))
                            Text("0m", fontSize = 6.sp, color = Color(0xFF94A3B8))
                        }

                        // Vertical Bars (30 days)
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 18.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            workout.bars.take(28).forEach { bar ->
                                val heightRatio = (bar.minutes / 60f).coerceIn(0.12f, 1f)
                                Box(
                                    modifier = Modifier
                                        .width(3.5.dp)
                                        .fillMaxHeight(heightRatio)
                                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                        .background(bar.color)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // X-axis day marks: 1, 5, 10, 15, 20, 25, 30
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("1", "5", "10", "15", "20", "25", "30").forEach { day ->
                            Text(
                                text = day,
                                fontSize = 6.5.sp,
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right 3 Metric Chips
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Avg
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD1FAE5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🕒", fontSize = 7.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("48m", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Avg / Day", fontSize = 7.sp, color = Color(0xFF64748B))
                    }

                    // Peak
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD1FAE5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("↑", fontSize = 8.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("52m", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Peak (10 Oct)", fontSize = 7.sp, color = Color(0xFF64748B))
                    }

                    // Lowest
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("↓", fontSize = 8.sp, color = Color(0xFFE11D48), fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("12m", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Lowest (16 Oct)", fontSize = 7.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 6. COMPACT DETAILED ANALYTICS ACTION BUTTON
// -----------------------------------------------------------------------------------------
@Composable
private fun CompactAnalyticsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = Color(0x350284C7)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF0284C7),
                        Color(0xFF2563EB)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "📊", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "View Detailed Analytics  →",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
