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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.MemberTier
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

import com.example.badnewgym.feature.membercard.presentation.components.IntelligenceBodyStatsCard
import com.example.badnewgym.feature.membercard.presentation.components.IntelligenceHeroCard
import com.example.badnewgym.feature.membercard.presentation.components.IntelligencePaymentPlanCard
import com.example.badnewgym.feature.membercard.presentation.components.IntelligenceTodayActivityCard
import com.example.badnewgym.feature.membercard.presentation.components.IntelligenceTopBar
import com.example.badnewgym.feature.membercard.presentation.components.IntelligenceWorkoutAttendanceCard

/**
 * BAD GYM - MEMBER INTELLIGENCE PRODUCTION DASHBOARD
 * World-class mobile UI/UX with large typography, prominent icons,
 * high-contrast semantics, and pixel-perfect fidelity to reference target image.
 */
@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onReceiptClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onTierSwitch: (MemberTier) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 0. App Branding & Action Header
        IntelligenceTopBar(
            monthLabel = data.attendance.monthLabel,
            onMenuClick = onAvatarClick,
            onMonthClick = onAttendanceClick,
            onOptionsClick = onNotificationsClick
        )

        // 1. Hero Profile Identity Card
        IntelligenceHeroCard(
            profile = data.profile,
            onAvatarClick = onAvatarClick
        )

        // 2. Body Stats Bento Grid Card
        IntelligenceBodyStatsCard(
            bodyStats = data.bodyStats,
            onDetailsClick = onAnalyticsClick
        )

        // 3. Workout Trend & Attendance Integrated Card
        IntelligenceWorkoutAttendanceCard(
            bars = data.workoutTrend.bars,
            calendarDays = data.attendance.calendarDays,
            onDetailsClick = onAnalyticsClick
        )

        // 4. Payment & Plan Card
        IntelligencePaymentPlanCard(
            payment = data.paymentPlan,
            onViewReceiptClick = onReceiptClick,
            onCollectClick = onReceiptClick
        )

        // 5. Today's Activity Card
        IntelligenceTodayActivityCard(
            activity = data.todayActivity,
            onDetailsClick = onAnalyticsClick
        )
    }
}

// ==========================================
// 0. APP BRANDING HEADER
// ==========================================
@Composable
fun AppHeader(
    branch: String = "Varanasi",
    unreadCount: Int = 3,
    onSearchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0F2B1D), Color(0xFF14532D))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("\uD83C\uDFCB\uFE0F", fontSize = 20.sp)
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "BAD GYM",
                        color = Color(0xFF0F172A),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE8FDF2))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = branch.uppercase(),
                            color = Color(0xFF059669),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Text(
                    text = "MEMBER INTELLIGENCE",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color(0x10000000))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Notification button with badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color(0x10000000))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .clickable(onClick = onNotificationsClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(20.dp)
                )
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 3.dp, y = (-3).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unreadCount.toString(),
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 1. HERO PROFILE CARD
// ==========================================
@Composable
fun HeroProfileCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit,
    onPlanClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x181A73E8))
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF0F7FF),
                        Color(0xFFE8F2FE),
                        Color(0xFFF9FBFF)
                    )
                )
            )
            .border(1.5.dp, Color(0xFFD3E4FD), RoundedCornerShape(26.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with animated sweep border & status badges
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clickable(onClick = onAvatarClick)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(
                            3.dp,
                            Brush.sweepGradient(
                                listOf(
                                    Color(0xFF00C9A7),
                                    Color(0xFF3B82F6),
                                    Color(0xFF8B5CF6),
                                    Color(0xFF00C9A7)
                                )
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(id = R.drawable.avatar_ashwini),
                    contentDescription = "Member Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                )
                // Crown Badge
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("\uD83D\uDC51", fontSize = 12.sp)
                }
                // Online Indicator
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                        .border(2.5.dp, Color.White, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Member Info & Badges
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.greeting,
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = profile.name,
                    color = Color(0xFF0F172A),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "ID: ${profile.memberCode}  \u00B7  ${profile.age} yrs  \u00B7  ${profile.gender}",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Plan Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF2563EB), Color(0xFF7C3AED))
                                )
                            )
                            .clickable(onClick = onPlanClick)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("\uD83D\uDC51", fontSize = 11.sp)
                        Text(
                            text = profile.tierName,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Active Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF10B981))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("\u26A1", fontSize = 11.sp)
                        Text(
                            text = "Active",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Circular Days Left Gauge
            DaysLeftGauge(
                daysLeft = profile.daysLeft,
                totalDays = profile.totalDays
            )
        }
    }
}

@Composable
private fun DaysLeftGauge(daysLeft: Int, totalDays: Int) {
    val progress = (daysLeft.toFloat() / totalDays.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .size(76.dp)
            .shadow(4.dp, CircleShape, spotColor = Color(0x181A73E8))
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(68.dp)) {
            // Track
            drawArc(
                color = Color(0xFFEFF6FF),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(Color(0xFF2563EB), Color(0xFF00C9A7), Color(0xFF2563EB))
                ),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$daysLeft",
                color = Color(0xFF0F172A),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Days Left",
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "of $totalDays",
                color = Color(0xFF94A3B8),
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// 2. QUICK STATS STRIP
// ==========================================
private data class QuickStatItem(
    val icon: String,
    val label: String,
    val value: String,
    val sub: String,
    val bg: Color
)

@Composable
fun QuickStatsStrip(profile: MemberProfile) {
    val items = listOf(
        QuickStatItem("\uD83D\uDCC5", "Joined", profile.joinedDate, "6 Months", Color(0xFFFFF7ED)),
        QuickStatItem("\uD83C\uDFCB\uFE0F", "Avg. Workout", "48 min", "\u25B2 +12%", Color(0xFFF3E8FF)),
        QuickStatItem("\uD83D\uDD25", "Streak", "${profile.streakDays} Days", "Personal Best", Color(0xFFFEF2F2)),
        QuickStatItem("\uD83C\uDFAF", "Goal", profile.goal, "On Track", Color(0xFFECFDF5)),
        QuickStatItem("\uD83D\uDCCA", "Attendance", "${profile.attendancePercent}%", "${profile.attendedDays}/${profile.totalDays} Days", Color(0xFFEFF6FF))
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(horizontal = 4.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(stat.bg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stat.icon, fontSize = 16.sp)
                }
                Text(
                    text = stat.label,
                    color = Color(0xFF64748B),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = stat.value,
                    color = Color(0xFF0F172A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (stat.sub.isNotEmpty()) {
                    Text(
                        text = stat.sub,
                        color = if (stat.sub.startsWith("\u25B2")) Color(0xFF10B981) else Color(0xFF94A3B8),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. BODY STATS BENTO CARD
// ==========================================
@Composable
fun BodyStatsBentoCard(
    bodyStats: List<BodyStatItem>,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(26.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(26.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\uD83D\uDCAA", fontSize = 24.sp)
                    Text(
                        text = "Body Stats",
                        color = Color(0xFF0F172A),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "\u2022 Last 6 Months",
                            color = Color(0xFF2563EB),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Details",
                        color = Color(0xFF2563EB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 2x2 Bento Tiles Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                bodyStats.take(2).forEach { stat ->
                    BodyStatTile(stat = stat, modifier = Modifier.weight(1f))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                bodyStats.drop(2).take(2).forEach { stat ->
                    BodyStatTile(stat = stat, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BodyStatTile(
    stat: BodyStatItem,
    modifier: Modifier = Modifier
) {
    val accentColor: Color
    val tileBrush: Brush
    val iconEmoji: String

    when (stat.type) {
        BodyStatType.WEIGHT -> {
            accentColor = Color(0xFF7C3AED)
            tileBrush = Brush.verticalGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)))
            iconEmoji = "\u2696\uFE0F"
        }
        BodyStatType.MUSCLE -> {
            accentColor = Color(0xFF2563EB)
            tileBrush = Brush.verticalGradient(listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE)))
            iconEmoji = "\uD83C\uDFCB\uFE0F"
        }
        BodyStatType.BODY_FAT -> {
            accentColor = Color(0xFFEF4444)
            tileBrush = Brush.verticalGradient(listOf(Color(0xFFFEF2F2), Color(0xFFFEE2E2)))
            iconEmoji = "%"
        }
        BodyStatType.BMI -> {
            accentColor = Color(0xFFF59E0B)
            tileBrush = Brush.verticalGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7)))
            iconEmoji = "\uD83E\uDDCD"
        }
    }

    Box(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = accentColor.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(20.dp))
            .background(tileBrush)
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (stat.type == BodyStatType.BODY_FAT) {
                            Text("%", fontSize = 17.sp, fontWeight = FontWeight.Black, color = accentColor)
                        } else {
                            Text(iconEmoji, fontSize = 18.sp)
                        }
                    }
                    Text(
                        text = stat.label,
                        color = Color(0xFF475569),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            val displayValue = if (stat.unit.isNotEmpty()) "${stat.value} ${stat.unit}" else stat.value
            Text(
                text = displayValue,
                color = Color(0xFF0F172A),
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            if (stat.delta != "Normal") {
                Text(
                    text = stat.delta,
                    color = if (stat.isFavorable) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Visual Chart based on type
            when (stat.type) {
                BodyStatType.WEIGHT -> {
                    CurvedAreaSparkline(
                        points = stat.sparklinePoints,
                        lineColor = accentColor,
                        modifier = Modifier.fillMaxWidth().height(34.dp)
                    )
                }
                BodyStatType.MUSCLE -> {
                    EqualizerHistogram(
                        color = accentColor,
                        modifier = Modifier.fillMaxWidth().height(34.dp)
                    )
                }
                BodyStatType.BODY_FAT -> {
                    CurvedAreaSparkline(
                        points = stat.sparklinePoints,
                        lineColor = accentColor,
                        modifier = Modifier.fillMaxWidth().height(34.dp)
                    )
                }
                BodyStatType.BMI -> {
                    BmiGradientBar(
                        modifier = Modifier.fillMaxWidth().height(14.dp)
                    )
                }
            }

            if (stat.goalText.isNotEmpty()) {
                Text(
                    text = stat.goalText,
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CurvedAreaSparkline(
    points: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas
        val w = size.width
        val h = size.height
        val step = w / (points.size - 1).toFloat()

        val path = Path()
        val fillPath = Path()

        points.forEachIndexed { i, v ->
            val x = i * step
            val y = h - (v * (h - 8.dp.toPx())) - 4.dp.toPx()
            if (i == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, h)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }
        fillPath.lineTo(w, h)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                listOf(lineColor.copy(alpha = 0.25f), Color.Transparent)
            )
        )

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        val lastX = (points.size - 1) * step
        val lastY = h - (points.last() * (h - 8.dp.toPx())) - 4.dp.toPx()
        drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(lastX, lastY))
        drawCircle(color = lineColor, radius = 3.5.dp.toPx(), center = Offset(lastX, lastY))
    }
}

@Composable
private fun EqualizerHistogram(
    color: Color,
    modifier: Modifier = Modifier
) {
    val barRatios = listOf(0.4f, 0.5f, 0.65f, 0.55f, 0.8f, 0.75f, 0.95f, 0.85f, 1.0f)
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val n = barRatios.size
        val barW = (w / n * 0.6f).coerceIn(4.dp.toPx(), 8.dp.toPx())
        val spacing = w / n

        barRatios.forEachIndexed { i, ratio ->
            val barH = ratio * h * 0.9f
            val x = i * spacing + (spacing - barW) / 2
            val y = h - barH
            drawRoundRect(
                color = color.copy(alpha = 0.4f + ratio * 0.6f),
                topLeft = Offset(x, y),
                size = Size(barW, barH),
                cornerRadius = CornerRadius(barW / 2)
            )
        }
    }
}

@Composable
private fun BmiGradientBar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            brush = Brush.horizontalGradient(
                listOf(
                    Color(0xFF38BDF8),
                    Color(0xFF4ADE80),
                    Color(0xFFFBBF24),
                    Color(0xFFF87171)
                )
            ),
            cornerRadius = CornerRadius(20f)
        )
        val fraction = ((22.8f - 18.5f) / (24.9f - 18.5f)).coerceIn(0f, 1f)
        drawCircle(
            color = Color.White,
            radius = 8.dp.toPx(),
            center = Offset(size.width * fraction, size.height / 2)
        )
        drawCircle(
            color = Color(0xFF2563EB),
            radius = 5.5.dp.toPx(),
            center = Offset(size.width * fraction, size.height / 2)
        )
    }
}

// ==========================================
// 4. GYM ACTIVITY CARD (media_1792739764173.jpg)
// ==========================================
@Composable
fun GymActivityCard(
    attendance: AttendanceSectionData,
    workoutTrend: WorkoutTrendData,
    onAttendanceClick: () -> Unit,
    onWorkoutTrendClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x1810B981))
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(26.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header: Title with 3D Bar Icon + Trophy Badge Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = Color(0x203B82F6))
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFE0E7FF), Color(0xFFDBEAFE))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\uD83D\uDCCA", fontSize = 24.sp)
                    }
                    Column {
                        Text(
                            text = "Gym Activity",
                            color = Color(0xFF0F172A),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${attendance.monthLabel}  \u00B7  Last 30 Days",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Trophy pill on right
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .clickable(onClick = onAttendanceClick)
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("\uD83C\uDFC6", fontSize = 18.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${attendance.attendedDays}/${attendance.totalDays} Days",
                            color = Color(0xFF0F172A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${attendance.percent}%",
                            color = Color(0xFF10B981),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Interactive Workout Bar Chart
            WorkoutActivityChart(
                bars = workoutTrend.bars.map { it.minutes },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            // Day-by-Day Status Indicator Chips (Row of 30 dots)
            DayStatusDotsRow(
                totalDays = 30,
                modifier = Modifier.fillMaxWidth()
            )

            // Legend Strip
            LegendStrip(modifier = Modifier.fillMaxWidth())

            // 4 KPI Metric Cards in 2x2 Grid (Generous mobile touch & read area)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiMetricCard(
                        icon = "\uD83D\uDCC5",
                        iconBg = Color(0xFFECFDF5),
                        label = "Attendance",
                        value = "${attendance.attendedDays}/${attendance.totalDays}",
                        sub = "${attendance.percent}%",
                        isSubGreen = true,
                        modifier = Modifier.weight(1f)
                    )
                    KpiMetricCard(
                        icon = "\uD83C\uDFCB\uFE0F",
                        iconBg = Color(0xFFEFF6FF),
                        label = "Avg. Workout",
                        value = "48 min",
                        sub = "\u25B2 +12%",
                        isSubGreen = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiMetricCard(
                        icon = "\uD83D\uDD25",
                        iconBg = Color(0xFFFFF7ED),
                        label = "Total Calories",
                        value = "8,960 kcal",
                        sub = "\u25B2 +18%",
                        isSubGreen = true,
                        modifier = Modifier.weight(1f)
                    )
                    KpiMetricCard(
                        icon = "\uD83C\uDFAF",
                        iconBg = Color(0xFFF5F3FF),
                        label = "Active Days",
                        value = "${workoutTrend.daysWithWorkout} Days",
                        sub = "out of ${attendance.totalDays}",
                        isSubGreen = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2 Bottom Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // View Attendance Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8FDF2))
                        .border(1.dp, Color(0xFFB7EBCE), RoundedCornerShape(16.dp))
                        .clickable(onClick = onAttendanceClick)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("\uD83D\uDCC5", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Attendance",
                        color = Color(0xFF059669),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // View Workout Trend Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3E8FF))
                        .border(1.dp, Color(0xFFDDD0FC), RoundedCornerShape(16.dp))
                        .clickable(onClick = onWorkoutTrendClick)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("\uD83D\uDCCA", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Workout Trend",
                        color = Color(0xFF7C3AED),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutActivityChart(
    bars: List<Int>,
    modifier: Modifier = Modifier
) {
    val barValues = if (bars.isEmpty()) {
        listOf(15, 28, 35, 45, 30, 25, 10, 0, 38, 52, 42, 28, 20, 18, 0, 12, 18, 32, 44, 25, 22, 30, 48, 55, 28, 34, 46, 50, 10, 18)
    } else {
        bars
    }

    Box(modifier = modifier) {
        // Grid & Bars Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height - 24.dp.toPx()
            val yOffset = 18.dp.toPx()

            // Draw Y-axis dashed grid lines for 60m, 30m, 0m
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            val gridColor = Color(0xFFE2E8F0)

            // 60m line
            drawLine(gridColor, Offset(30.dp.toPx(), yOffset), Offset(w, yOffset), strokeWidth = 1f, pathEffect = dashEffect)
            // 30m line
            drawLine(gridColor, Offset(30.dp.toPx(), yOffset + h * 0.5f), Offset(w, yOffset + h * 0.5f), strokeWidth = 1f, pathEffect = dashEffect)
            // 0m line
            drawLine(Color(0xFFCBD5E1), Offset(30.dp.toPx(), yOffset + h), Offset(w, yOffset + h), strokeWidth = 1.5f)

            // Draw Bars
            val chartLeft = 32.dp.toPx()
            val chartWidth = w - chartLeft
            val n = barValues.size
            val slotWidth = chartWidth / n
            val barWidth = (slotWidth * 0.65f).coerceIn(4.dp.toPx(), 12.dp.toPx())

            barValues.forEachIndexed { i, mins ->
                val fraction = (mins / 60f).coerceIn(0f, 1f)
                val barH = fraction * h
                val x = chartLeft + i * slotWidth + (slotWidth - barWidth) / 2
                val y = yOffset + h - barH

                val barBrush = when {
                    i == 9 -> Brush.verticalGradient(listOf(Color(0xFF00D2FF), Color(0xFF2563EB))) // Peak day 10
                    i == 15 -> Brush.verticalGradient(listOf(Color(0xFFFB7185), Color(0xFFE11D48))) // Low day 16
                    mins == 0 -> Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                    mins >= 40 -> Brush.verticalGradient(listOf(Color(0xFF2DD4BF), Color(0xFF0284C7)))
                    else -> Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF3B82F6)))
                }

                drawRoundRect(
                    brush = barBrush,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barH.coerceAtLeast(3.dp.toPx())),
                    cornerRadius = CornerRadius(barWidth / 2)
                )
            }
        }

        // Y-axis Labels on Left
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 10.dp)
                .height(100.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("60m", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("30m", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("0m", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        // Peak Tooltip (52m on 10 Oct)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = (-30).dp, y = 0.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0284C7))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("52m", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                Text("10 Oct", color = Color.White.copy(alpha = 0.85f), fontSize = 8.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Low Tooltip (12m on 16 Oct)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 22.dp, y = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE11D48), RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("12m", color = Color(0xFFE11D48), fontSize = 10.sp, fontWeight = FontWeight.Black)
                Text("16 Oct", color = Color(0xFFE11D48).copy(alpha = 0.8f), fontSize = 8.sp, fontWeight = FontWeight.Medium)
            }
        }

        // X-axis day numbers at the bottom (1, 5, 10, 15, 20, 25, 30)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.9f)
                .padding(bottom = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1", "5", "10", "15", "20", "25", "30").forEach { day ->
                Text(
                    text = day,
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun DayStatusDotsRow(
    totalDays: Int = 30,
    modifier: Modifier = Modifier
) {
    val statuses = listOf(
        "P", "P", "P", "A", "P", "P", "C", "H", "P", "P",
        "P", "P", "P", "A", "C", "P", "P", "P", "P", "P",
        "P", "P", "P", "A", "P", "P", "P", "P", "A", "P"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        statuses.take(totalDays).forEach { s ->
            when (s) {
                "P" -> Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                }
                "A" -> Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                }
                "H" -> Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                }
                else -> Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF94A3B8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LegendStrip(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(dotColor = Color(0xFF10B981), iconText = "\u2713", label = "Present")
        LegendItem(dotColor = Color(0xFF0284C7), iconText = "\uD83D\uDCCA", label = "Workout Min")
        LegendItem(dotColor = Color(0xFFEF4444), iconText = "\u2715", label = "Absent")
        LegendItem(dotColor = Color(0xFFF59E0B), iconText = "\u2605", label = "Holiday")
        LegendItem(dotColor = Color(0xFF94A3B8), iconText = "-", label = "Closed")
    }
}

@Composable
private fun LegendItem(
    dotColor: Color,
    iconText: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(dotColor),
            contentAlignment = Alignment.Center
        ) {
            Text(iconText, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = label,
            color = Color(0xFF475569),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun KpiMetricCard(
    icon: String,
    iconBg: Color,
    label: String,
    value: String,
    sub: String,
    isSubGreen: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 22.sp)
            }
            Column {
                Text(
                    text = label,
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = value,
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = sub,
                    color = if (isSubGreen) Color(0xFF10B981) else Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

// ==========================================
// 5. PAYMENT AND PLAN CARD
// ==========================================
@Composable
fun PaymentPlanCard(
    paymentPlan: PaymentPlanState,
    onCollectClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(26.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(26.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFFF7ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\uD83D\uDCB3", fontSize = 22.sp)
                    }
                    Text(
                        text = "Payment & Plan",
                        color = Color(0xFF0F172A),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Details",
                        color = Color(0xFF2563EB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // High Priority Due Alert & Collect Action Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFFFBEB))
                    .border(1.2.dp, Color(0xFFFDE68A), RoundedCornerShape(18.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\u26A0\uFE0F", fontSize = 20.sp)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("\u20B9", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF2563EB))
                            Text(
                                text = paymentPlan.dueAlertAmount,
                                color = Color(0xFF0F172A),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = paymentPlan.dueAlertText,
                                color = Color(0xFFD97706),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Due by ${paymentPlan.dueAlertDate}  \u00B7  Renewal Pending",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Collect Money Action Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF10B981), Color(0xFF059669))
                            )
                        )
                        .clickable(onClick = onCollectClick)
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("\u20B9", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("Collect", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Current Plan & Last Paid 2-Column Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Current Plan Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF5F3FF))
                        .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\uD83D\uDC51", fontSize = 16.sp)
                        Text("Current Plan", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        text = paymentPlan.planName,
                        color = Color(0xFF7C3AED),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = paymentPlan.planPeriodFormatted,
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }

                // Last Paid Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFECFDF5))
                        .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\uD83D\uDCC5", fontSize = 16.sp)
                        Text("Last Paid", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        text = paymentPlan.lastPaymentAmount,
                        color = Color(0xFF059669),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Paid on ${paymentPlan.lastPaymentDate}",
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }
            }

            // 12-Month Payment History Strip
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "12-Month Payment Track",
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "Jan" to "\u2014",
                        "Feb" to "\u2014",
                        "Mar" to "\u2014",
                        "Apr" to "\u20B91.2K",
                        "May" to "\u20B91.2K",
                        "Jun" to "\u20B91.05K",
                        "Jul" to "\u2014",
                        "Aug" to "\u2014",
                        "Sep" to "\u20B92.8K",
                        "Oct" to "Due \u26A0\uFE0F",
                        "Nov" to "\u2014",
                        "Dec" to "\u2014"
                    ).forEach { (m, v) ->
                        val isPaid = v.startsWith("\u20B9")
                        val isDue = v.contains("Due")
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        isDue -> Color(0xFFFFFBEB)
                                        isPaid -> Color(0xFFECFDF5)
                                        else -> Color(0xFFF8FAFC)
                                    }
                                )
                                .border(
                                    1.dp,
                                    when {
                                        isDue -> Color(0xFFFDE68A)
                                        isPaid -> Color(0xFFA7F3D0)
                                        else -> Color(0xFFE2E8F0)
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 9.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(m, color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = v,
                                color = when {
                                    isDue -> Color(0xFFD97706)
                                    isPaid -> Color(0xFF059669)
                                    else -> Color(0xFF94A3B8)
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
