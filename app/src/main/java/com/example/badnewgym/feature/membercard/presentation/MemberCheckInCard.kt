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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * BAD GYM — COMPACT ZERO-SCROLL MEMBER INTELLIGENCE CARD ARCHITECTURE
 * 1. Top bar removed to save vertical space; notification bell embedded inside the Hero card.
 * 2. Only the Hero Member Profile widget reflects the member's tier colors (Emerald for Basic, Sapphire/Gold for 6M Gold, Cyan for Spark).
 * 3. All other widgets retain their natural premium high-contrast theme.
 * 4. Attendance and Workout Trend are placed SIDE-BY-SIDE in a 2-column layout.
 * 5. Attendance day dots display explicit day numbers (1..31) inside each colored badge.
 * 6. Body Stats header is streamlined into a single compact row.
 * 7. Payment & Plan retains ₹2,800 due alert, plan progress, 3 status boxes, Pay Now button, and 12-Month history strip.
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
    val tier = data.profile.tier

    // Outer framing stays in neutral luxury dark slate
    val outerBorderBrush = Brush.linearGradient(
        listOf(Color(0xFF1E293B), Color(0xFF334155), Color(0xFF1E293B))
    )
    val outerBackgroundBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF070F1A),
            Color(0xFF0B1726),
            Color(0xFF070E18)
        )
    )

    // Outer Main Card Frame
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x55000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(outerBackgroundBrush)
            .border(1.5.dp, outerBorderBrush, RoundedCornerShape(22.dp))
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 1. HERO MEMBER PROFILE WIDGET (The ONLY widget adopting Tier color!)
            HeroMemberProfileCard(
                profile = data.profile,
                onAvatarClick = onAvatarClick,
                onNotificationsClick = onNotificationsClick,
                onPlanClick = {
                    val nextTier = when (tier) {
                        MemberTier.GOLD_6_MONTH -> MemberTier.BASIC_MEMBER
                        MemberTier.BASIC_MEMBER -> MemberTier.SPARK_DIAMOND
                        MemberTier.SPARK_DIAMOND -> MemberTier.GOLD_6_MONTH
                    }
                    onTierSwitch(nextTier)
                },
                onNavTabClick = { tabIndex ->
                    when (tabIndex) {
                        1 -> onReceiptClick()
                        2 -> onAttendanceClick()
                        3 -> onAnalyticsClick()
                        4 -> onAnalyticsClick()
                    }
                }
            )

            // 2. BODY STATS WIDGET (Streamlined compact header + 4 stat columns)
            BodyStatsSectionCard(
                bodyStats = data.bodyStats,
                onDetailsClick = onAnalyticsClick
            )

            // 3. SIDE-BY-SIDE: ATTENDANCE (Left) + WORKOUT TREND (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AttendanceCompactSectionCard(
                    attendance = data.attendance,
                    onHeaderClick = onAttendanceClick,
                    onDayClick = onAttendanceDayClick,
                    modifier = Modifier.weight(1.05f)
                )

                WorkoutTrendCompactSectionCard(
                    workout = data.workoutTrend,
                    onClick = onAnalyticsClick,
                    modifier = Modifier.weight(0.95f)
                )
            }

            // 4. PAYMENT & PLAN WIDGET (Due Alert + Progress + Statuses + Pay Now + 12-Month History)
            PaymentPlanSectionCard(
                paymentPlan = data.paymentPlan,
                onReceiptClick = onReceiptClick
            )
        }
    }
}

// =================================================================================================
// 1. HERO MEMBER PROFILE CARD (The ONLY widget adopting Tier color!)
// =================================================================================================
@Composable
fun HeroMemberProfileCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    onNavTabClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tier = profile.tier

    // Tier-specific surface and glowing border ONLY for Hero Card
    val cardBg = when (tier) {
        MemberTier.GOLD_6_MONTH -> Brush.verticalGradient(
            listOf(
                Color(0xFF0F2744),
                Color(0xFF0A1C33),
                Color(0xFF081526)
            )
        )
        MemberTier.BASIC_MEMBER -> Brush.verticalGradient(
            listOf(
                Color(0xFF063323),
                Color(0xFF09442F),
                Color(0xFF05261B)
            )
        )
        MemberTier.SPARK_DIAMOND -> Brush.verticalGradient(
            listOf(
                Color(0xFF0B253D),
                Color(0xFF081B2E),
                Color(0xFF05121F)
            )
        )
    }

    val cardBorder = when (tier) {
        MemberTier.GOLD_6_MONTH -> Color(0xFFF59E0B)
        MemberTier.BASIC_MEMBER -> Color(0xFF00E676)
        MemberTier.SPARK_DIAMOND -> Color(0xFF00E5FF)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.5.dp, cardBorder.copy(alpha = 0.8f), RoundedCornerShape(18.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Identity Row: Avatar + Name & Badges + Notification Bell & Circular Days Left Ring
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Crown Badge & Active Indicator
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clickable(onClick = onAvatarClick)
                ) {
                    val ringColor = when (tier) {
                        MemberTier.GOLD_6_MONTH -> Color(0xFFF59E0B)
                        MemberTier.BASIC_MEMBER -> Color(0xFF00E676)
                        MemberTier.SPARK_DIAMOND -> Color(0xFF00E5FF)
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center)
                            .border(2.dp, ringColor, CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        val avatarRes = if (profile.name.contains("Aman", ignoreCase = true)) {
                            R.drawable.avatar_aman
                        } else {
                            R.drawable.avatar_ashwini
                        }

                        Image(
                            painter = painterResource(id = avatarRes),
                            contentDescription = profile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    // Crown / Leaf / Diamond icon badge at top-left
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .clip(CircleShape)
                            .background(ringColor)
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (tier) {
                                MemberTier.GOLD_6_MONTH -> "👑"
                                MemberTier.BASIC_MEMBER -> "🌱"
                                MemberTier.SPARK_DIAMOND -> "💎"
                            },
                            fontSize = 9.sp
                        )
                    }

                    // Green Active Indicator Dot at bottom-right
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                            .border(2.dp, Color(0xFF070F1A), CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Middle Info: Greeting, Name, Meta, Badges
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.greeting,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = profile.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )

                    Text(
                        text = "${profile.memberCode} | ${profile.age} yrs | ${profile.gender}",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Plan Badge (Tappable to cycle tier!)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (tier) {
                                        MemberTier.GOLD_6_MONTH -> Color(0xFFF59E0B)
                                        MemberTier.BASIC_MEMBER -> Color(0xFF00E676)
                                        MemberTier.SPARK_DIAMOND -> Color(0xFF00E5FF)
                                    }
                                )
                                .clickable(onClick = onPlanClick)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (tier) {
                                    MemberTier.GOLD_6_MONTH -> "⚡ Prime Plan"
                                    MemberTier.BASIC_MEMBER -> "🌱 Basic Member"
                                    MemberTier.SPARK_DIAMOND -> "💎 Spark Member"
                                },
                                color = Color(0xFF091321),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Active Badge (Green pill)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF00C48C))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Active",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Right: Notification Bell Button + Circular Progress Ring
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Notification Bell (Corner badge requested by user!)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                            .clickable(onClick = onNotificationsClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        // Red badge
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${profile.unreadNotifications.coerceAtLeast(3)}",
                                color = Color.White,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Circular Progress Ring "75% - 25 Days Left"
                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(56.dp)) {
                            val stroke = 4.5.dp.toPx()
                            val diameter = size.minDimension - stroke
                            val topLeft = Offset(stroke / 2, stroke / 2)
                            val arcSize = Size(diameter, diameter)

                            drawArc(
                                color = Color(0x3300E676),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = stroke)
                            )

                            val sweep = 360f * (profile.attendancePercent.toFloat() / 100f)
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(Color(0xFF00E5FF), Color(0xFF00E676), Color(0xFF00E5FF))
                                ),
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${profile.attendancePercent}%",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 14.sp
                            )
                            Text(
                                text = "${profile.daysLeft} Days Left",
                                color = Color(0xFF94A3B8),
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 8.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 3 Info Chips Row: Joined | Goal | Branch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CompactMetaChip(
                    iconText = "📅",
                    label = "Joined",
                    value = profile.joinedDate,
                    modifier = Modifier.weight(1f)
                )
                CompactMetaChip(
                    iconText = "🎯",
                    label = "Goal",
                    value = profile.goal,
                    modifier = Modifier.weight(1.05f)
                )
                CompactMetaChip(
                    iconText = "📍",
                    label = "Branch",
                    value = profile.branch,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            var activeTab by remember { mutableIntStateOf(0) }

            // Navigation Tabs Row (Matching media_1792680736452.png)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x33000000))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tabs = listOf(
                    "🏠 Overview" to 0,
                    "💳 Payment" to 1,
                    "📅 Attendance" to 2,
                    "🏋️ Workout" to 3,
                    "📊 Progress" to 4
                )

                tabs.forEach { (label, tabId) ->
                    val isSelected = activeTab == tabId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color(0xFF00C48C) else Color.Transparent
                            )
                            .clickable {
                                activeTab = tabId
                                onNavTabClick(tabId)
                            }
                            .padding(horizontal = 4.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                            fontSize = 8.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactMetaChip(
    iconText: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x22FFFFFF))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
            .padding(horizontal = 5.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = iconText, fontSize = 9.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Column {
            Text(
                text = label,
                color = Color(0xFF94A3B8),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 8.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                lineHeight = 10.sp
            )
        }
    }
}

// =================================================================================================
// 2. BODY STATS WIDGET (Streamlined compact header saving vertical space)
// =================================================================================================
@Composable
fun BodyStatsSectionCard(
    bodyStats: List<BodyStatItem>,
    onDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0B1726))
            .border(1.2.dp, Color(0x33A855F7), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Slim Header Row (16dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDetailsClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏋️", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Body Stats",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Last 6 Months",
                        color = Color(0xFFA855F7),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4 Compact Columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val weightItem = bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT }
                val muscleItem = bodyStats.firstOrNull { it.type == BodyStatType.MUSCLE }
                val fatItem = bodyStats.firstOrNull { it.type == BodyStatType.BODY_FAT }
                val bmiItem = bodyStats.firstOrNull { it.type == BodyStatType.BMI }

                CompactStatCol(
                    label = "Weight",
                    value = weightItem?.value ?: "69.5 kg",
                    delta = weightItem?.delta ?: "↓ 2.5 kg",
                    deltaColor = Color(0xFF10B981),
                    goal = "Goal: 68kg",
                    modifier = Modifier.weight(1f)
                )

                CompactStatCol(
                    label = "Muscle",
                    value = muscleItem?.value ?: "57.2 kg",
                    delta = muscleItem?.delta ?: "↑ 1.4 kg",
                    deltaColor = Color(0xFFA855F7),
                    goal = "Goal: 60kg",
                    modifier = Modifier.weight(1f)
                )

                CompactStatCol(
                    label = "Body Fat",
                    value = fatItem?.value ?: "17.6%",
                    delta = fatItem?.delta ?: "↓ 1.2%",
                    deltaColor = Color(0xFFEF4444),
                    goal = "Goal: 15% 🏆",
                    modifier = Modifier.weight(1f)
                )

                CompactStatCol(
                    label = "BMI",
                    value = bmiItem?.value ?: "22.8",
                    delta = bmiItem?.delta ?: "Normal",
                    deltaColor = Color(0xFF00E676),
                    goal = "18.5 – 24.9",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CompactStatCol(
    label: String,
    value: String,
    delta: String,
    deltaColor: Color,
    goal: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x33FFFFFF))
            .padding(horizontal = 3.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color(0xFF94A3B8),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = delta,
            color = deltaColor,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(1.dp))

        // Mini Wave Sparkline
        Canvas(modifier = Modifier.fillMaxWidth().height(10.dp)) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(0f, h * 0.7f)
                cubicTo(w * 0.3f, h * 0.2f, w * 0.6f, h * 0.8f, w, h * 0.3f)
            }
            drawPath(path, deltaColor, style = Stroke(width = 1.3.dp.toPx(), cap = StrokeCap.Round))
        }

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = goal,
            color = Color(0xFF94A3B8),
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

// =================================================================================================
// 3. ATTENDANCE COMPACT WIDGET (Left Column: Dot Matrix with DAY NUMBERS INSIDE + Gauge)
// =================================================================================================
@Composable
fun AttendanceCompactSectionCard(
    attendance: AttendanceSectionData,
    onHeaderClick: () -> Unit = {},
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0B1C2A))
            .border(1.2.dp, Color(0x3300E676), RoundedCornerShape(16.dp))
            .padding(6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 📅 Attendance + 77% (24/31d) + Arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📅", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Attendance",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${attendance.percent}%",
                        color = Color(0xFF00E676),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Sub-row: 24/31 Days + 4 compact status chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${attendance.attendedDays}/${attendance.totalDays} Days",
                    color = Color(0xFF94A3B8),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    MiniLegendChip(color = Color(0xFF00E676), count = attendance.presentCount)
                    MiniLegendChip(color = Color(0xFFEF4444), count = attendance.absentCount)
                    MiniLegendChip(color = Color(0xFFFBBF24), count = attendance.lateCount)
                    MiniLegendChip(color = Color(0xFF64748B), count = attendance.holidayCount)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 7-Column Dot Matrix Calendar WITH DAY NUMBERS (1..31) INSIDE!
            val columnHeaders = listOf("S", "M", "T", "W", "T", "F", "S")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                columnHeaders.forEach { initial ->
                    Text(
                        text = initial,
                        color = Color(0xFF94A3B8),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(18.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            val daysList = attendance.calendarDays
            val totalDays = daysList.size.coerceAtLeast(31)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (row in 0 until 5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (col in 0 until 7) {
                            val dayIndex = row * 7 + col
                            if (dayIndex < totalDays) {
                                val day = daysList.getOrNull(dayIndex)
                                val dayNum = day?.dayOfMonth ?: (dayIndex + 1)
                                val dotColor = when (day?.status) {
                                    DayAttendanceStatus.PRESENT -> Color(0xFF00E676)
                                    DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                                    DayAttendanceStatus.LATE -> Color(0xFFFBBF24)
                                    DayAttendanceStatus.HOLIDAY -> Color(0xFF475569)
                                    null -> if (col == 0) Color(0xFF475569) else Color(0xFF00E676)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                        .clickable { if (day != null) onDayClick(day) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        color = Color.White,
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniLegendChip(color: Color, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(1.5.dp))
        Text(
            text = "$count",
            color = Color(0xFFCBD5E1),
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// =================================================================================================
// 4. WORKOUT TREND COMPACT WIDGET (Right Column: 30-Day Bar Chart + 3 KPI Cards)
// =================================================================================================
@Composable
fun WorkoutTrendCompactSectionCard(
    workout: WorkoutTrendData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF091B2E))
            .border(1.2.dp, Color(0x3300E5FF), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 📊 Workout (30d) + Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📊", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Workout",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "(30d)",
                        color = Color(0xFF38BDF8),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 30-Day Bar Chart Area with Baseline Guidelines & Callout Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                // Background Track Gridlines (60m, 30m, 0m)
                Canvas(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    val w = size.width
                    val h = size.height
                    val linePaint = Color(0x22FFFFFF)

                    drawLine(
                        linePaint,
                        Offset(14.dp.toPx(), 2.dp.toPx()),
                        Offset(w, 2.dp.toPx()),
                        1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    )
                    drawLine(
                        linePaint,
                        Offset(14.dp.toPx(), h * 0.5f),
                        Offset(w, h * 0.5f),
                        1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    )
                    drawLine(linePaint, Offset(14.dp.toPx(), h), Offset(w, h), 1.dp.toPx())
                }

                // Y-Axis Labels Column on left
                Column(
                    modifier = Modifier
                        .height(56.dp)
                        .width(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "60m", color = Color(0xFF94A3B8), fontSize = 6.5.sp, fontWeight = FontWeight.Bold)
                    Text(text = "30m", color = Color(0xFF94A3B8), fontSize = 6.5.sp, fontWeight = FontWeight.Bold)
                    Text(text = "0m", color = Color(0xFF94A3B8), fontSize = 6.5.sp, fontWeight = FontWeight.Bold)
                }

                // Bar Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(start = 16.dp, end = 2.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val barCount = workout.bars.size.coerceAtLeast(30)
                    val slotWidth = w / barCount.toFloat()
                    val barWidth = (slotWidth * 0.65f).coerceIn(1.8.dp.toPx(), 4.dp.toPx())

                    workout.bars.forEachIndexed { i, bar ->
                        val barHeight = ((bar.minutes.toFloat() / 60f).coerceIn(0.05f, 1f)) * h
                        val x = i * slotWidth + (slotWidth - barWidth) / 2f
                        val y = h - barHeight

                        val barBrush = if (i == 7) { // Peak Day
                            Brush.verticalGradient(listOf(Color(0xFFEF4444), Color(0xFFDC2626)))
                        } else if (i == 25) { // High Day
                            Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF0284C7)))
                        } else {
                            Brush.verticalGradient(listOf(Color(0xFF00E676), Color(0xFF0284C7)))
                        }

                        drawRoundRect(
                            brush = barBrush,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(1.5.dp.toPx())
                        )
                    }
                }

                // Peak Badge "52m" Pin on Day 8
                Box(
                    modifier = Modifier
                        .offset(x = 42.dp, y = 0.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(text = "52m", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Black)
                }

                // 48m Badge Pin on Day 26
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-14).dp, y = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF0070F3))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(text = "48m", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            // 3 Bottom KPI Pills: Peak | Lowest | Avg
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                CompactKpiPill(
                    icon = "⬆️",
                    value = "${workout.peakMinutes}m",
                    sub = "Peak",
                    valueColor = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                CompactKpiPill(
                    icon = "⬇️",
                    value = "${workout.lowestMinutes}m",
                    sub = "Low",
                    valueColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                CompactKpiPill(
                    icon = "📊",
                    value = "${workout.avgMinutes}m",
                    sub = "Avg",
                    valueColor = Color(0xFF00E676),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CompactKpiPill(
    icon: String,
    value: String,
    sub: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x33FFFFFF))
            .padding(horizontal = 2.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 8.sp)
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                text = value,
                color = valueColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 10.sp
            )
        }
        Text(
            text = sub,
            color = Color(0xFF94A3B8),
            fontSize = 7.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 8.sp
        )
    }
}

// =================================================================================================
// 5. PAYMENT & PLAN WIDGET (Due Alert + Progress + Statuses + Pay Now + 12-Month History)
// =================================================================================================
@Composable
fun PaymentPlanSectionCard(
    paymentPlan: PaymentPlanState,
    onReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0E1A29))
            .border(1.2.dp, Color(0x33EF4444), RoundedCornerShape(16.dp))
            .padding(7.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: 💳 Payment & Plan + DUE SOON Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💳", fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Payment & Plan",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Due Soon Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEE2E2))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "DUE SOON ⚠️",
                        color = Color(0xFFDC2626),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Top Row: Big Red Amount + Gold Plan (6 Months) Progress Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Due Alert Amount
                Column {
                    Text(
                        text = paymentPlan.dueAlertAmount.ifEmpty { "₹2,800" },
                        color = Color(0xFFEF4444),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = paymentPlan.dueAlertText.ifEmpty { "Due in 3 days ⚠️" },
                        color = Color(0xFFEF4444),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 10.sp
                    )
                    Text(
                        text = paymentPlan.dueAlertDate.ifEmpty { "16 Oct 2026" },
                        color = Color(0xFF94A3B8),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: Gold Plan (6 Months) with Progress Bar
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33FFFFFF))
                        .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👑", fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = paymentPlan.planName.ifEmpty { "Gold Plan (6 Months)" },
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }

                    Text(
                        text = paymentPlan.planPeriodFormatted.ifEmpty { "01 May 2026 – 31 Oct 2026" },
                        color = Color(0xFF94A3B8),
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Progress Bar
                    Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                        val w = size.width
                        val h = size.height
                        drawRoundRect(
                            color = Color(0x33FFFFFF),
                            size = Size(w, h),
                            cornerRadius = CornerRadius(2.dp.toPx())
                        )
                        val fraction = (paymentPlan.planDaysUsed.toFloat() / paymentPlan.planTotalDays.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
                        drawRoundRect(
                            color = Color(0xFF0070F3),
                            size = Size(w * fraction, h),
                            cornerRadius = CornerRadius(2.dp.toPx())
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${paymentPlan.planDaysUsed}/${paymentPlan.planTotalDays} days",
                            color = Color(0xFF94A3B8),
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${paymentPlan.planDaysRemaining} days left",
                            color = Color(0xFF00E5FF),
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Middle: 3 Status Mini-Boxes (Last Paid, Current Due, Next Due)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PaymentStatusMiniBox(
                    iconText = "🟢",
                    label = "Last Paid",
                    amount = paymentPlan.lastPaidAmount.ifEmpty { "₹2,800" },
                    date = paymentPlan.lastPaidDate.ifEmpty { "01 Sep 2026" },
                    modifier = Modifier.weight(1f)
                )

                PaymentStatusMiniBox(
                    iconText = "⏰",
                    label = "Current Due",
                    amount = paymentPlan.currentDueAmount.ifEmpty { "₹2,800" },
                    date = paymentPlan.currentDueDate.ifEmpty { "16 Oct 2026" },
                    amountColor = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )

                PaymentStatusMiniBox(
                    iconText = "📅",
                    label = "Next Due",
                    amount = paymentPlan.nextDueAmount.ifEmpty { "₹2,800" },
                    date = paymentPlan.nextDueDateFormatted.ifEmpty { "16 Nov 2026" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Action Row: Pay Now Button + Promo Gift Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pay Now Button (Clickable, opens Official Receipt Sheet!)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEF4444))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "💳", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Pay Now",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Promo Gift Banner
                Row(
                    modifier = Modifier
                        .weight(1.35f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33F59E0B))
                        .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .clickable(onClick = onReceiptClick)
                        .padding(horizontal = 6.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "👑", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(
                            text = paymentPlan.promoTitle.ifEmpty { "Stay Fit Stay Active" },
                            color = Color(0xFFFCD34D),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 9.sp
                        )
                        Text(
                            text = paymentPlan.promoSubtitle.ifEmpty { "Renew Now & Get 1 Month Free 🎁" },
                            color = Color.White,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            lineHeight = 8.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 12-Month Payment History Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payment History (Last 12 Months)",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                paymentPlan.monthlyHistory.forEach { item ->
                    MonthPillItem(item = item)
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusMiniBox(
    iconText: String,
    label: String,
    amount: String,
    date: String,
    amountColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x33FFFFFF))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = iconText, fontSize = 7.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = label,
                color = Color(0xFF94A3B8),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            text = amount,
            color = amountColor,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = date,
            color = Color(0xFF94A3B8),
            fontSize = 7.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MonthPillItem(item: MonthPaymentItem) {
    val borderColor = when {
        item.isCurrent -> Color(0xFF38BDF8)
        item.tierOrDiscount == "Gold" -> Color(0xFFF59E0B)
        item.isDiscount -> Color(0xFF00E676)
        else -> Color(0x22FFFFFF)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(36.dp)
    ) {
        Text(
            text = item.month,
            color = if (item.isCurrent) Color(0xFF38BDF8) else Color(0xFF94A3B8),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x33FFFFFF))
                .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                .padding(horizontal = 2.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.amount,
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 9.sp
                )
                if (item.isDiscount) {
                    Text(
                        text = item.tierOrDiscount,
                        color = Color(0xFF00E676),
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 8.sp
                    )
                } else if (item.tierOrDiscount.isNotEmpty()) {
                    Text(
                        text = item.tierOrDiscount,
                        color = when (item.tierOrDiscount) {
                            "Gold" -> Color(0xFFF59E0B)
                            "Silver" -> Color(0xFF38BDF8)
                            else -> Color(0xFF00E676)
                        },
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 8.sp
                    )
                }
            }
        }
    }
}
