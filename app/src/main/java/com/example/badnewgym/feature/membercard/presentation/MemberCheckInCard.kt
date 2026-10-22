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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.MemberTier
import com.example.badnewgym.feature.membercard.domain.model.MonthPaymentItem
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

/**
 * BAD GYM — CANONICAL EMERALD PREMIUM (OPTION 1) MEMBER INTELLIGENCE CARD
 * Pixel-perfect implementation of the canonical design:
 * 1. Hero Card: Luminous emerald/mint aura, Ashwini Singh, circular Days Left ring, 4 glass info chips.
 * 2. Body Stats: Purple header, 4 cards (Weight, Muscle, Body Fat, BMI) with custom canvas sparklines.
 * 3. Attendance: October 2026, 10-column numbered dot matrix (1..31) + 4-status vertical legend.
 * 4. Workout Trend: October 2026, 30 cyan/blue bars with Peak 52m (10 Oct) & Lowest 12m (16 Oct) badges + 4 summary cards.
 * 5. Payment & Plan: Last 12 Months timeline pill strip + 3 status cards + Emerald "Collect Money >" button.
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
    onTierSwitch: (MemberTier) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. HERO MEMBER PROFILE CARD (EMERALD PREMIUM)
        HeroOption1Card(
            profile = data.profile,
            onAvatarClick = onAvatarClick,
            onPlanClick = {
                val nextTier = when (data.profile.tier) {
                    MemberTier.SPARK_DIAMOND -> MemberTier.GOLD_6_MONTH
                    MemberTier.GOLD_6_MONTH -> MemberTier.BASIC_MEMBER
                    MemberTier.BASIC_MEMBER -> MemberTier.SPARK_DIAMOND
                }
                onTierSwitch(nextTier)
            }
        )

        // 2. BODY STATS CARD
        BodyStatsOption1Card(
            bodyStats = data.bodyStats,
            onDetailsClick = onAnalyticsClick
        )

        // 3. ATTENDANCE CARD
        AttendanceOption1Card(
            attendance = data.attendance,
            onDayClick = onAttendanceDayClick,
            onHeaderClick = onAttendanceClick
        )

        // 4. WORKOUT TREND CARD
        WorkoutTrendOption1Card(
            workoutTrend = data.workoutTrend,
            onDetailsClick = onAnalyticsClick
        )

        // 5. PAYMENT & PLAN CARD
        PaymentPlanOption1Card(
            paymentPlan = data.paymentPlan,
            onCollectMoneyClick = onReceiptClick,
            onViewAllClick = onReceiptClick
        )
    }
}

// =========================================================================================
// 1. HERO MEMBER PROFILE CARD (EMERALD PREMIUM)
// =========================================================================================

@Composable
fun HeroOption1Card(
    profile: MemberProfile,
    onAvatarClick: () -> Unit,
    onPlanClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x20059669))
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE6FAF2),
                        Color(0xFFD6F7EB),
                        Color(0xFFC7F2E2)
                    )
                )
            )
            .border(1.5.dp, Color(0xFF86EFAC), RoundedCornerShape(26.dp))
            .drawBehind {
                // Lush radiant emerald glowing auras behind avatar and days left ring
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x6600E676), Color(0x2510B981), Color.Transparent),
                        center = Offset(size.width * 0.16f, size.height * 0.35f),
                        radius = size.width * 0.38f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x5500E5FF), Color(0x2500C48C), Color.Transparent),
                        center = Offset(size.width * 0.86f, size.height * 0.35f),
                        radius = size.width * 0.38f
                    )
                )
            }
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Section: Avatar + Member Details + Days Left Gauge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Green Ring, Crown Badge & Online Indicator
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clickable(onClick = onAvatarClick)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.5.dp, Color(0xFF00C48C), CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_ashwini),
                            contentDescription = "Member Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Gold Crown Badge in Top-Right
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00875A))
                            .border(1.dp, Color(0xFFFFD700), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👑", fontSize = 10.sp)
                    }

                    // Bright Green Online Indicator in Bottom-Right
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                            .border(2.dp, Color.White, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Identity Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Good Morning 👋",
                        color = Color(0xFF334155),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = profile.name,
                        color = Color(0xFF0F172A),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "ID: ${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                        color = Color(0xFF64748B),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    // Tier & Active Badges Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Spark Member Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .background(Color(0xFF00875A))
                                .clickable(onClick = onPlanClick)
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("👑", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = profile.tierName,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Active Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .background(Color(0xFF10B981))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Active",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Circular Ring Progress Gauge: "18 Days Left of 30"
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 7.dp.toPx()
                        val diameter = size.minDimension - stroke
                        val topLeft = Offset(stroke / 2, stroke / 2)
                        val arcSize = Size(diameter, diameter)

                        // Background Track
                        drawArc(
                            color = Color(0xFFC7F3E2),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )

                        // Progress Arc (~60% filled)
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFF00E5FF), Color(0xFF00C48C), Color(0xFF00E5FF))
                            ),
                            startAngle = -90f,
                            sweepAngle = 216f,
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
                        Text("📅", fontSize = 10.sp)
                        Text(
                            text = "${profile.daysLeft}",
                            color = Color(0xFF0F172A),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "Days Left",
                            color = Color(0xFF1E293B),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "of ${profile.totalDays}",
                            color = Color(0xFF64748B),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom 4 Translucent Glass Info Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Chip 1: Joined On
                HeroMetaChip(
                    iconText = "📅",
                    iconBg = Color(0xFFE8FAF0),
                    iconTint = Color(0xFF10B981),
                    label = "Joined On",
                    primaryText = profile.joinedDate,
                    secondaryText = "6 Months",
                    modifier = Modifier.weight(1f)
                )

                // Chip 2: Workout Avg.
                HeroMetaChip(
                    iconText = "🏋️",
                    iconBg = Color(0xFFEFF6FF),
                    iconTint = Color(0xFF2563EB),
                    label = "Workout Avg.",
                    primaryText = "48 min",
                    secondaryText = "↑ +12%",
                    secondaryColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )

                // Chip 3: Goal
                HeroMetaChip(
                    iconText = "🎯",
                    iconBg = Color(0xFFFEF2F2),
                    iconTint = Color(0xFFEF4444),
                    label = "Goal",
                    primaryText = profile.goal,
                    modifier = Modifier.weight(1f)
                )

                // Chip 4: Attendance
                HeroMetaChip(
                    iconText = "📊",
                    iconBg = Color(0xFFEFF6FF),
                    iconTint = Color(0xFF0284C7),
                    label = "Attendance",
                    primaryText = "${profile.attendancePercent}%",
                    secondaryText = "${profile.attendedDays}/${profile.totalDays} Days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HeroMetaChip(
    iconText: String,
    iconBg: Color,
    iconTint: Color,
    label: String,
    primaryText: String,
    secondaryText: String? = null,
    secondaryColor: Color = Color(0xFF64748B),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xE6FFFFFF))
            .border(1.dp, Color(0x66A7F3D0), RoundedCornerShape(14.dp))
            .padding(vertical = 6.dp, horizontal = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconText, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = Color(0xFF64748B),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = primaryText,
                    color = Color(0xFF0F172A),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (secondaryText != null) {
                    Text(
                        text = secondaryText,
                        color = secondaryColor,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// =========================================================================================
// 2. BODY STATS CARD
// =========================================================================================

@Composable
fun BodyStatsOption1Card(
    bodyStats: List<BodyStatItem>,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 💪 Body Stats • Last 6 Months (5 Records) >
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDetailsClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💪", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Body Stats",
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "•  Last 6 Months (5 Records)",
                        color = Color(0xFF8B5CF6),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Stat Cards Row: Weight | Muscle | Body Fat | BMI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Weight Card
                StatPillCard(
                    iconText = "⚖️",
                    iconColor = Color(0xFF10B981),
                    iconBg = Color(0xFFE8FAF0),
                    label = "Weight",
                    value = "69.5 kg",
                    delta = "↓ 2.5 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineType = SparklineType.CURVE_GREEN,
                    goalText = "Goal: 65 kg",
                    modifier = Modifier.weight(1f)
                )

                // Muscle Card
                StatPillCard(
                    iconText = "🏋️",
                    iconColor = Color(0xFF8B5CF6),
                    iconBg = Color(0xFFF3E8FF),
                    label = "Muscle",
                    value = "57.2 kg",
                    delta = "↑ 1.4 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklineType = SparklineType.HISTOGRAM_PURPLE,
                    goalText = "Goal: 60 kg",
                    modifier = Modifier.weight(1f)
                )

                // Body Fat Card
                StatPillCard(
                    iconText = "%",
                    iconColor = Color(0xFFEF4444),
                    iconBg = Color(0xFFFEE2E2),
                    label = "Body Fat",
                    value = "17.6%",
                    delta = "↓ 1.2%",
                    deltaColor = Color(0xFFEF4444),
                    sparklineType = SparklineType.CURVE_RED,
                    goalText = "Goal: 15%",
                    modifier = Modifier.weight(1f)
                )

                // BMI Card
                StatPillCard(
                    iconText = "👤",
                    iconColor = Color(0xFF2563EB),
                    iconBg = Color(0xFFEFF6FF),
                    label = "BMI",
                    value = "22.8",
                    delta = "Normal",
                    deltaColor = Color(0xFF10B981),
                    deltaIsPill = true,
                    sparklineType = SparklineType.CURVE_BLUE,
                    goalText = "Range: 18.5 – 24.9",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private enum class SparklineType {
    CURVE_GREEN,
    HISTOGRAM_PURPLE,
    CURVE_RED,
    CURVE_BLUE
}

@Composable
private fun StatPillCard(
    iconText: String,
    iconColor: Color,
    iconBg: Color,
    label: String,
    value: String,
    delta: String,
    deltaColor: Color,
    deltaIsPill: Boolean = false,
    sparklineType: SparklineType,
    goalText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Row: Icon + Chevron
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        fontSize = 10.sp,
                        color = iconColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(13.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = Color(0xFF64748B),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = Color(0xFF0F172A),
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Black
            )

            if (deltaIsPill) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = delta,
                        color = deltaColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = delta,
                    color = deltaColor,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Canvas Sparkline
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                val w = size.width
                val h = size.height

                when (sparklineType) {
                    SparklineType.CURVE_GREEN -> {
                        val path = Path().apply {
                            moveTo(0f, h * 0.7f)
                            cubicTo(w * 0.3f, h * 0.6f, w * 0.6f, h * 0.85f, w * 0.95f, h * 0.25f)
                        }
                        drawPath(path, Color(0xFF10B981), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                        drawCircle(Color(0xFF10B981), radius = 2.5.dp.toPx(), center = Offset(w * 0.95f, h * 0.25f))
                    }
                    SparklineType.HISTOGRAM_PURPLE -> {
                        val barCount = 7
                        val barWidth = (w / barCount) * 0.6f
                        val gap = (w / barCount) * 0.4f
                        for (i in 0 until barCount) {
                            val barHeightRatio = 0.35f + (i * 0.09f)
                            val x = i * (barWidth + gap)
                            drawRoundRect(
                                color = Color(0xFF8B5CF6),
                                topLeft = Offset(x, h * (1f - barHeightRatio)),
                                size = Size(barWidth, h * barHeightRatio),
                                cornerRadius = CornerRadius(2.dp.toPx())
                            )
                        }
                    }
                    SparklineType.CURVE_RED -> {
                        val path = Path().apply {
                            moveTo(0f, h * 0.3f)
                            cubicTo(w * 0.35f, h * 0.25f, w * 0.65f, h * 0.75f, w * 0.95f, h * 0.65f)
                        }
                        drawPath(path, Color(0xFFEF4444), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                        drawCircle(Color(0xFFEF4444), radius = 2.dp.toPx(), center = Offset(w * 0.35f, h * 0.26f))
                        drawCircle(Color(0xFFEF4444), radius = 2.dp.toPx(), center = Offset(w * 0.7f, h * 0.72f))
                    }
                    SparklineType.CURVE_BLUE -> {
                        val path = Path().apply {
                            moveTo(0f, h * 0.65f)
                            cubicTo(w * 0.3f, h * 0.7f, w * 0.65f, h * 0.35f, w * 0.95f, h * 0.45f)
                        }
                        drawPath(path, Color(0xFF2563EB), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                        drawCircle(Color(0xFF2563EB), radius = 2.5.dp.toPx(), center = Offset(w * 0.95f, h * 0.45f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = goalText,
                color = Color(0xFF64748B),
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

// =========================================================================================
// 3. ATTENDANCE CARD (10 COLUMNS × 3 ROWS MATRIX + VERTICAL LEGEND)
// =========================================================================================

@Composable
fun AttendanceOption1Card(
    attendance: AttendanceSectionData,
    onDayClick: (CalendarDay) -> Unit,
    onHeaderClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 📅 Attendance • October 2026 | 24/31 Days | 77% >
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📅", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Attendance",
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "•  ${attendance.monthLabel}",
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8FAF0))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${attendance.attendedDays}/${attendance.totalDays} Days",
                            color = Color(0xFF065F46),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${attendance.percent}% >",
                        color = Color(0xFF10B981),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Area: Calendar Matrix (Left ~78%) + Legend (Right ~22%)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Calendar Grid (10 Columns)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val dayHeaders = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed")

                    // Header Row: Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        dayHeaders.forEach { header ->
                            Text(
                                text = header,
                                color = Color(0xFF94A3B8),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(22.dp)
                            )
                        }
                    }

                    // 3 Rows of Days (1..10, 11..20, 21..31)
                    val days = (1..31).toList()
                    val row1 = days.subList(0, 10)
                    val row2 = days.subList(10, 20)
                    val row3 = days.subList(20, 31)

                    AttendanceDayCirclesRow(row1, attendance.calendarDays, onDayClick)
                    AttendanceDayCirclesRow(row2, attendance.calendarDays, onDayClick)
                    AttendanceDayCirclesRow(row3, attendance.calendarDays, onDayClick)
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(84.dp)
                        .background(Color(0xFFE2E8F0))
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Legend Column: Present 24, Absent 4, Holiday 1, Closed 2
                Column(
                    modifier = Modifier.width(76.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    LegendItemRow(dotColor = Color(0xFF10B981), label = "Present", count = attendance.presentCount)
                    LegendItemRow(dotColor = Color(0xFFEF4444), label = "Absent", count = attendance.absentCount)
                    LegendItemRow(dotColor = Color(0xFFF59E0B), label = "Holiday", count = attendance.holidayCount)
                    LegendItemRow(dotColor = Color(0xFF94A3B8), label = "Closed", count = 2)
                }
            }
        }
    }
}

@Composable
private fun AttendanceDayCirclesRow(
    days: List<Int>,
    calendarDays: List<CalendarDay>,
    onDayClick: (CalendarDay) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { dayNumber ->
            val calendarDay = calendarDays.find { it.dayOfMonth == dayNumber }
                ?: CalendarDay(dayNumber, 0, DayAttendanceStatus.PRESENT)

            val statusBrush = when (dayNumber) {
                // Exact match to reference image
                3, 13, 17, 24, 30 -> Brush.radialGradient(
                    listOf(Color(0xFFF87171), Color(0xFFEF4444), Color(0xFFDC2626))
                ) // Absent (Red)
                22 -> Brush.radialGradient(
                    listOf(Color(0xFFFDE047), Color(0xFFF59E0B), Color(0xFFD97706))
                ) // Holiday (Amber)
                4, 18, 25 -> Brush.radialGradient(
                    listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8), Color(0xFF64748B))
                ) // Closed (Gray)
                else -> Brush.radialGradient(
                    listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF059669))
                ) // Present (Green)
            }

            Box(
                modifier = Modifier
                    .size(23.dp)
                    .clip(CircleShape)
                    .background(statusBrush)
                    .clickable { onDayClick(calendarDay) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$dayNumber",
                    color = Color.White,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LegendItemRow(
    dotColor: Color,
    label: String,
    count: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = Color(0xFF334155),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = "$count",
            color = Color(0xFF0F172A),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// =========================================================================================
// 4. WORKOUT TREND CARD (30-DAY BARS + PEAK/LOWEST BADGES + 4 KPI CARDS)
// =========================================================================================

@Composable
fun WorkoutTrendOption1Card(
    workoutTrend: WorkoutTrendData,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: 🏋️ Workout Trend • October 2026 | 30 Days >
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDetailsClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏋️", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Workout Trend",
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "•  ${workoutTrend.monthLabel}",
                        color = Color(0xFF2563EB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "30 Days >",
                        color = Color(0xFF2563EB),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 30-Day Bar Chart Area with Peak/Lowest Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                // Background Guide Lines & Y-Axis Labels
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 60m line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("60m", color = Color(0xFF94A3B8), fontSize = 8.5.sp, modifier = Modifier.width(26.dp))
                        Canvas(modifier = Modifier.weight(1f).height(1.dp)) {
                            drawLine(
                                color = Color(0xFFE2E8F0),
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                            )
                        }
                    }

                    // 30m line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("30m", color = Color(0xFF94A3B8), fontSize = 8.5.sp, modifier = Modifier.width(26.dp))
                        Canvas(modifier = Modifier.weight(1f).height(1.dp)) {
                            drawLine(
                                color = Color(0xFFE2E8F0),
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                            )
                        }
                    }

                    // 0m line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("0m", color = Color(0xFF94A3B8), fontSize = 8.5.sp, modifier = Modifier.width(26.dp))
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE2E8F0)))
                    }
                }

                // 30 Vertical Bars
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 30.dp, end = 4.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    for (day in 1..30) {
                        val mins = when (day) {
                            10 -> 52 // Peak
                            16 -> 12 // Lowest
                            1 -> 18
                            2 -> 24
                            3 -> 30
                            4 -> 35
                            5 -> 40
                            6 -> 32
                            7 -> 28
                            8 -> 36
                            9 -> 44
                            11 -> 38
                            12 -> 30
                            13 -> 26
                            14 -> 34
                            15 -> 22
                            17 -> 28
                            18 -> 32
                            19 -> 36
                            20 -> 40
                            21 -> 42
                            22 -> 38
                            23 -> 35
                            24 -> 41
                            25 -> 46
                            26 -> 48
                            27 -> 39
                            28 -> 44
                            29 -> 42
                            30 -> 47
                            else -> 30
                        }

                        val barHeightFraction = (mins / 60f).coerceIn(0.1f, 1f)
                        val isPeak = (day == 10)
                        val isLowest = (day == 16)

                        val barBrush = when {
                            isPeak -> Brush.verticalGradient(
                                listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF059669))
                            )
                            isLowest -> Brush.verticalGradient(
                                listOf(Color(0xFFF87171), Color(0xFFEF4444))
                            )
                            else -> Brush.verticalGradient(
                                listOf(Color(0xFF00E5FF), Color(0xFF0284C7), Color(0xFF2563EB))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 1.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(barHeightFraction)
                                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                    .background(barBrush)
                            )
                        }
                    }
                }

                // Peak Floating Badge (Day 10 - 52m)
                Box(
                    modifier = Modifier
                        .offset(x = 110.dp, y = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👑", fontSize = 8.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("52m", color = Color(0xFF78350F), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("10 Oct", color = Color(0xFF92400E), fontSize = 7.5.sp)
                    }
                }

                // Lowest Floating Badge (Day 16 - 12m)
                Box(
                    modifier = Modifier
                        .offset(x = 175.dp, y = 38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEE2E2))
                        .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(8.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("↓", color = Color(0xFFDC2626), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("12m", color = Color(0xFFDC2626), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("16 Oct", color = Color(0xFF991B1B), fontSize = 7.5.sp)
                    }
                }
            }

            // X-Axis Day Numbers Row: 1 5 10 15 20 25 30
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(1, 5, 10, 15, 20, 25, 30).forEach { day ->
                    Text(
                        text = "$day",
                        color = Color(0xFF94A3B8),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom 4 KPI Cards: Avg/Day | Peak | Lowest | Workout Days
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // KPI 1: Avg / Day
                WorkoutKpiCard(
                    iconText = "⏱️",
                    iconColor = Color(0xFF10B981),
                    iconBg = Color(0xFFE8FAF0),
                    mainText = "48m",
                    subLabel = "Avg / Day",
                    trendText = "↑ +12% vs Sep",
                    trendColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )

                // KPI 2: Peak
                WorkoutKpiCard(
                    iconText = "👑",
                    iconColor = Color(0xFFF59E0B),
                    iconBg = Color(0xFFFEF3C7),
                    mainText = "52m",
                    subLabel = "Peak",
                    trendText = "10 Oct",
                    trendColor = Color(0xFF92400E),
                    modifier = Modifier.weight(1f)
                )

                // KPI 3: Lowest
                WorkoutKpiCard(
                    iconText = "↓",
                    iconColor = Color(0xFFEF4444),
                    iconBg = Color(0xFFFEE2E2),
                    mainText = "12m",
                    subLabel = "Lowest",
                    trendText = "16 Oct",
                    trendColor = Color(0xFFDC2626),
                    modifier = Modifier.weight(1f)
                )

                // KPI 4: 22 Days with workout / 8 Days no workout
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(6.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📊", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "22 Days",
                                color = Color(0xFF0F172A),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text("With Workout", color = Color(0xFF64748B), fontSize = 8.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "8 Days No Workout",
                            color = Color(0xFF94A3B8),
                            fontSize = 7.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutKpiCard(
    iconText: String,
    iconColor: Color,
    iconBg: Color,
    mainText: String,
    subLabel: String,
    trendText: String,
    trendColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconText, fontSize = 10.sp, color = iconColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = mainText,
                color = Color(0xFF0F172A),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = subLabel,
                color = Color(0xFF64748B),
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = trendText,
                color = trendColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =========================================================================================
// 5. PAYMENT & PLAN CARD (12-MONTH PILL STRIP + STATUS BOXES + COLLECT MONEY BUTTON)
// =========================================================================================

@Composable
fun PaymentPlanOption1Card(
    paymentPlan: PaymentPlanState,
    onCollectMoneyClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 👛 Payment & Plan • Last 12 Months | View All >
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👛", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Payment & Plan",
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "•  Last 12 Months",
                        color = Color(0xFFEA580C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "View All >",
                    color = Color(0xFFEA580C),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onViewAllClick)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 12-Month Pill Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MonthPill("Jan", "--", "No Plan", false, Color(0xFFF1F5F9), Color(0xFF94A3B8))
                MonthPill("Feb", "--", "No Plan", false, Color(0xFFF1F5F9), Color(0xFF94A3B8))
                MonthPill("Mar", "--", "No Plan", false, Color(0xFFF1F5F9), Color(0xFF94A3B8))
                MonthPill("Apr", "₹1.2K", "Gold", true, Color(0xFFFEF08A), Color(0xFF78350F))
                MonthPill("May", "₹1.2K", "Gold", true, Color(0xFFFEF08A), Color(0xFF78350F))
                MonthPill("Jun", "₹1.05K", "-₹150\nGold", true, Color(0xFFA7F3D0), Color(0xFF065F46))
                MonthPill("Jul", "Inactive", "", false, Color(0xFFF1F5F9), Color(0xFF94A3B8))
                MonthPill("Aug", "Inactive", "", false, Color(0xFFF1F5F9), Color(0xFF94A3B8))
                MonthPill("Sep", "₹800", "Silver", true, Color(0xFFE2E8F0), Color(0xFF1E293B))
                MonthPill("Oct", "₹800", "Silver", true, Color(0xFFE2E8F0), Color(0xFF1E293B))
                MonthPill("Nov", "₹600", "Spark", true, Color(0xFFBBF7D0), Color(0xFF065F46))
                MonthPill("Dec", "Pending", "", false, Color(0xFFF8FAFC), Color(0xFF94A3B8), isDashed = true)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Due Soon Pill | Last Paid Pill | Current Plan Pill | Collect Money Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Due Alert Pill
                Box(
                    modifier = Modifier
                        .weight(1.05f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("₹", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Column {
                            Text(
                                text = "₹2,800",
                                color = Color(0xFF0F172A),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Due in 3 days ⚠️",
                                color = Color(0xFFD97706),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "16 Oct 2026",
                                color = Color(0xFF64748B),
                                fontSize = 7.5.sp
                            )
                        }
                    }
                }

                // 2. Last Paid Pill
                Box(
                    modifier = Modifier
                        .weight(0.95f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2563EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📅", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Column {
                            Text(
                                text = "Last Paid",
                                color = Color(0xFF64748B),
                                fontSize = 8.5.sp
                            )
                            Text(
                                text = "₹800",
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "01 Oct 2026",
                                color = Color(0xFF64748B),
                                fontSize = 7.5.sp
                            )
                        }
                    }
                }

                // 3. Current Plan Pill
                Box(
                    modifier = Modifier
                        .weight(0.95f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFBEB))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                        .padding(horizontal = 5.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Current Plan",
                                color = Color(0xFF64748B),
                                fontSize = 8.sp
                            )
                            Text(
                                text = "Spark Plan",
                                color = Color(0xFF059669),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "01 Nov – 30 Nov",
                                color = Color(0xFF64748B),
                                fontSize = 7.sp
                            )
                        }
                    }
                }

                // 4. Collect Money Action Button
                Box(
                    modifier = Modifier
                        .weight(1.25f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF059669), Color(0xFF10B981))
                            )
                        )
                        .clickable(onClick = onCollectMoneyClick)
                        .padding(horizontal = 6.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "₹",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Collect Money >",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthPill(
    month: String,
    line1: String,
    line2: String,
    isPaid: Boolean,
    bgColor: Color,
    textColor: Color,
    isDashed: Boolean = false
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = Modifier
            .width(46.dp)
            .height(58.dp)
            .clip(shape)
            .background(bgColor)
            .border(
                1.dp,
                if (isDashed) Color(0xFFCBD5E1) else Color(0x33000000),
                shape
            )
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = month,
                color = Color(0xFF0F172A),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = line1,
                color = textColor,
                fontSize = 9.sp,
                fontWeight = if (isPaid) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            if (line2.isNotBlank()) {
                Text(
                    text = line2,
                    color = textColor,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 8.sp
                )
            } else {
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}
