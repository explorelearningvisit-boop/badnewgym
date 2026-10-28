package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * BAD GYM — Master Storyboard Dashboard
 * 100% Pixel-Perfect Representation matching Master Visual Mockup:
 * 1. Top App Bar & Deep Purple Hero ("Stronger Happier You!" + 3D Flexing Character)
 * 2. Floating 4-Metric Bar (Height, Weight, BMI, Goal)
 * 3. Attendance Card (3D Calendar & Sun, 85%, 30-Day Green Bar Chart, 4-Card Stats)
 * 4. Workout Trend Card (3D Dumbbells & Cloud, 47 min, 30-Day Blue Bar Chart, 4-Card Stats)
 * 5. Payment Card (3D Wallet & Coins, ₹2,800 Due, 12-Month Capsule Bar Chart, 4-Card Stats)
 * 6. Body Progress Card (3D Scale & Cloud, 12-Month 4-Line Spline Chart, 8-Card Stats)
 * 7. Bottom Banner ("Consistent Steps, Brighter Days" + 3D Dumbbell + Keep Going Button)
 */
@Composable
fun MasterStoryboardDashboard(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    onBodyProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    var isPausedByUser by remember { mutableStateOf(false) }
    var userPauseUntil by remember { mutableStateOf(0L) }

    // Auto-advance loop: 3.0 seconds per widget (switches right to left)
    LaunchedEffect(pagerState, isPausedByUser, userPauseUntil) {
        while (true) {
            delay(3000L)
            val now = System.currentTimeMillis()
            if (!isPausedByUser && now >= userPauseUntil && !pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % 4
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. TOP HERO HEADER & CHARACTER SECTION (Stable & Space-Optimized Light Green Card)
        MasterHeroHeader(
            data = data,
            onAvatarClick = onAvatarClick
        )

        // 2. HORIZONTAL STORYBOARD TAB MENU (Attendance, Workout, Payment, Body Progress)
        MasterStoryboardTabBar(
            currentPage = pagerState.currentPage,
            onTabSelected = { page ->
                userPauseUntil = System.currentTimeMillis() + 5000L // Pause auto-advance for 5s on manual tab tap
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = page,
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    )
                }
            }
        )

        // 3. AUTO-PLAY STORY PROGRESS & PAUSE STATUS STRIP
        StoryboardProgressIndicator(
            currentPage = pagerState.currentPage,
            isPaused = isPausedByUser || System.currentTimeMillis() < userPauseUntil,
            onTogglePause = {
                if (isPausedByUser) {
                    isPausedByUser = false
                    userPauseUntil = 0L
                } else {
                    isPausedByUser = true
                }
            }
        )

        // 4. HORIZONTAL PAGER: 4 STORYBOARD WIDGET CARDS (Swipeable & Auto-Slide Right-to-Left)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            userPauseUntil = System.currentTimeMillis() + 4000L
                            tryAwaitRelease()
                            userPauseUntil = System.currentTimeMillis() + 2500L
                        }
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 10.dp
            ) { page ->
                when (page) {
                    0 -> MasterAttendanceCard(data = data, onClick = onAttendanceClick)
                    1 -> MasterWorkoutTrendCard(data = data, onClick = onWorkoutClick)
                    2 -> MasterPaymentCard(data = data, onClick = onPaymentClick, onViewReceiptClick = onViewReceiptClick)
                    3 -> MasterBodyProgressCard(data = data, onClick = onBodyProgressClick)
                }
            }
        }

        // 5. BOTTOM FOOTER BANNER ("Consistent Steps, Brighter Days")
        MasterFooterBanner(
            onKeepGoingClick = onWorkoutClick
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// =============================================================================
// STORYBOARD INTERACTIVE TAB MENU & PROGRESS BAR
// =============================================================================

@Composable
fun MasterStoryboardTabBar(
    currentPage: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        Triple("📅", "Attendance", "90%"),
        Triple("🏋️", "Workout", "70m"),
        Triple("💳", "Payment", "₹2.8k"),
        Triple("⚖️", "Body", "69.5kg")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, (icon, label, badge) ->
            val isSelected = currentPage == index
            val activeColor = when (index) {
                0 -> Color(0xFF059669) // Mint
                1 -> Color(0xFF0284C7) // Sky Blue
                2 -> Color(0xFFDC2626) // Coral Red
                else -> Color(0xFF7C3AED) // Lavender Purple
            }
            val activeBg = when (index) {
                0 -> Color(0xFFDCFCE7)
                1 -> Color(0xFFE0F2FE)
                2 -> Color(0xFFFFE4E6)
                else -> Color(0xFFF3E8FF)
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) activeBg else Color.Transparent)
                    .border(
                        width = if (isSelected) 1.2.dp else 0.dp,
                        color = if (isSelected) activeColor else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 7.dp, horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = icon, fontSize = 11.5.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = label,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = if (isSelected) activeColor else Color(0xFF64748B),
                        maxLines = 1,
                        softWrap = false
                    )
                    if (isSelected) {
                        Text(
                            text = badge,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = activeColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryboardProgressIndicator(
    currentPage: Int,
    isPaused: Boolean,
    onTogglePause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 4-Segment Progress Bar
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..3) {
                val isCurrent = currentPage == i
                val barColor = when (i) {
                    0 -> Color(0xFF10B981)
                    1 -> Color(0xFF0284C7)
                    2 -> Color(0xFFEF4444)
                    else -> Color(0xFF8B5CF6)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isCurrent) barColor else Color(0xFFE2E8F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status Pill: Auto-Switching or Paused
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                .clickable { onTogglePause() }
                .padding(horizontal = 7.dp, vertical = 2.5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = if (isPaused) "⏸" else "⚡", fontSize = 8.sp, color = if (isPaused) Color(0xFFDC2626) else Color(0xFF059669))
            Text(
                text = if (isPaused) "Paused (Tap card)" else "Auto-Switch (3s)",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPaused) Color(0xFFDC2626) else Color(0xFF059669)
            )
        }
    }
}


@Composable
fun MasterHeroHeader(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 8-Pass Interactive Showcase matching Master Reference Mockup
    BadGymPassShowcase(
        selectedIndex = com.example.badnewgym.feature.membercard.design.MemberCardTheme.entries.indexOf(
            com.example.badnewgym.feature.membercard.design.MemberCardThemeManager.activeTheme
        ).coerceAtLeast(0),
        onPassSelected = { index ->
            com.example.badnewgym.feature.membercard.design.MemberCardThemeManager.activeTheme =
                com.example.badnewgym.feature.membercard.design.MemberCardTheme.entries[index]
        },
        onAvatarClick = { onAvatarClick() },
        modifier = modifier
    )
}

// =============================================================================
// 2. MASTER ATTENDANCE CARD
// =============================================================================
@Composable
fun MasterAttendanceCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFEFFEF2))
            .border(1.2.dp, Color(0xFFC6F6D5), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Calendar & Sun Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
                    contentDescription = "Attendance 3D Calendar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Star Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Attendance",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 30 Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Oct 2026")
                    }

                    // Key Metric: 90% + 27 / 30 Present Days + Pill on left, 4 Breakdown Columns on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "90%",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = "27 / 30 Present Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↑ 5%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "vs Sep",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Breakdown Columns: Present 27, Late 1, Absent 1, Holiday 1 (Compact & No Wrapping)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                                    Spacer(Modifier.width(2.5.dp))
                                    Text("Present", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), maxLines = 1, softWrap = false)
                                }
                                Text("27", fontSize = 13.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("90%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.5.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                                    Spacer(Modifier.width(2.5.dp))
                                    Text("Late", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), maxLines = 1, softWrap = false)
                                }
                                Text("1", fontSize = 13.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.5.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                                    Spacer(Modifier.width(2.5.dp))
                                    Text("Absent", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), maxLines = 1, softWrap = false)
                                }
                                Text("1", fontSize = 13.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.5.dp).clip(CircleShape).background(Color(0xFF8B5CF6)))
                                    Spacer(Modifier.width(2.5.dp))
                                    Text("Holiday", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), maxLines = 1, softWrap = false)
                                }
                                Text("1", fontSize = 13.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                            }
                        }
                    }
                }
            }

            // 30-Day Attendance Numbered Hollow Circle Grid (1..30)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Day statuses for Oct 2026 (1..30):
                // 1 = Present (Green), 2 = Late (Amber), 0 = Absent (Red), 3 = Holiday (Purple), 4 = Sunday (Disabled/Gray), 5 = Upcoming (Faint Outline)
                val dayStatuses = remember {
                    listOf(
                        1, 3, 1, 4, 1, 2, 1, 1, 1, 1, 4, 1, 1, 1, 0, // Days 1..15
                        1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 1, 1, 5, 5, 5  // Days 16..30
                    )
                }

                // Row 1: Days 1 to 15
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 1..15) {
                        val status = dayStatuses[day - 1]
                        AttendanceHollowCircle(day = day, status = status)
                    }
                }

                // Row 2: Days 16 to 30
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 16..30) {
                        val status = dayStatuses[day - 1]
                        AttendanceHollowCircle(day = day, status = status)
                    }
                }

                // Micro Legend Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AttendanceLegendItem(label = "Present", count = "20", color = Color(0xFF10B981))
                    AttendanceLegendItem(label = "Late", count = "1", color = Color(0xFFF59E0B))
                    AttendanceLegendItem(label = "Absent", count = "1", color = Color(0xFFEF4444))
                    AttendanceLegendItem(label = "Holiday", count = "1", color = Color(0xFF8B5CF6))
                    AttendanceLegendItem(label = "Sunday", count = "4", color = Color(0xFF94A3B8))
                    AttendanceLegendItem(label = "Upcoming", count = "3", color = Color(0xFFCBD5E1), isHollow = true)
                }
            }

            // Weekly Progress Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Weekly:", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = "W1 100%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 8.sp, color = Color(0xFF94A3B8))
                    Text(text = "W2 85%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 8.sp, color = Color(0xFF94A3B8))
                    Text(text = "W3 100%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 8.sp, color = Color(0xFF94A3B8))
                    Text(text = "W4 80%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }
                Text(
                    text = "Monthly: 90% (↑5%)",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF047857)
                )
            }

            // Bottom 4-Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniStatCard(
                    icon = "🔥",
                    title = "Current Streak",
                    value = "6 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🏆",
                    title = "Best Streak",
                    value = "12 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🟢",
                    title = "Attendance Rate",
                    value = "90%",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "📅",
                    title = "Total Visits",
                    value = "27 Days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =============================================================================
// 3. MASTER WORKOUT TREND CARD (Sky Blue #E5F1FB)
// =============================================================================
@Composable
fun MasterWorkoutTrendCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE5F1FB))
            .border(1.2.dp, Color(0xFFBAE6FD), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Dumbbells & Cloud Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_workout_dumbbells),
                    contentDescription = "Workout 3D Dumbbells",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Star Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Workout Trend",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 30 Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Oct 2026")
                    }

                    // Key Metric: 70 min + Avg / Session + Pill on left, Legend Column on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "70 min",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0284C7)
                                )
                                Text(
                                    text = "Avg / Session",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↑ 8%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "vs Sep",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Right Legend Column
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            LegendDotItem(color = Color(0xFF0284C7), label = "Workout", value = "(22 d)")
                            LegendDotItem(color = Color(0xFF94A3B8), label = "Rest", value = "(8 d)")
                        }
                    }
                }
            }

            // Quick Info Metrics Strip (4 KPIs from Image)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(horizontal = 7.dp, vertical = 4.5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Total Duration", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "823 min", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 14% vs Sep", fontSize = 7.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Sessions", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "22", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "↑ 2 vs prev", fontSize = 7.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Avg Duration", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "74 min", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 8%", fontSize = 7.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Peak Duration", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "123 min", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                    Text(text = "↑ 12%", fontSize = 7.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
            }

            // 30-Day Workout Bars Chart (0m, 60m, 120m)
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(52.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "120m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "60m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 30 Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        val workoutMins = listOf(
                            60, 20, 75, 20, 85, 90, 45, 60, 70, 80,
                            20, 65, 70, 20, 120, 80, 50, 60, 75, 85,
                            20, 40, 90, 70, 75, 80, 95, 70, 55, 85
                        )
                        val count = workoutMins.size
                        val barWidth = 3.5.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        workoutMins.forEachIndexed { i, mins ->
                            val x = i * step + (step - barWidth) / 2f
                            val hRatio = (mins / 125f).coerceIn(0.15f, 0.95f)
                            val h = size.height * hRatio
                            val y = size.height - h

                            val barColor = when {
                                mins >= 90 -> Color(0xFF0284C7)
                                mins >= 30 -> Color(0xFF38BDF8)
                                else -> Color(0xFFCBD5E1)
                            }

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = barRadius
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("1", "5", "10", "15", "20", "25", "30").forEach { mark ->
                        Text(text = mark, fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // 4-Tier Workout Intensity Legend
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(color = Color(0xFF0284C7), label = "High (≥90m)", value = "12d")
                LegendDotItem(color = Color(0xFF38BDF8), label = "Med (30-89m)", value = "14d")
                LegendDotItem(color = Color(0xFFF59E0B), label = "Low (<30m)", value = "3d")
                LegendDotItem(color = Color(0xFFCBD5E1), label = "Rest", value = "1d")
            }

            // Bottom 4-Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniStatCard(
                    icon = "⏱️",
                    title = "Total Workouts",
                    value = "22",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🏆",
                    title = "Best Session",
                    value = "2.0 hr",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🔥",
                    title = "Current Streak",
                    value = "8 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "📊",
                    title = "Weekly Avg",
                    value = "3.4 days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =============================================================================
// 4. MASTER PAYMENT CARD (Coral Pink #FEEDED)
// =============================================================================
@Composable
fun MasterPaymentCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFEEDED))
            .border(1.2.dp, Color(0xFFFECDD3), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Wallet & Coins Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_payment_wallet),
                    contentDescription = "Payment 3D Wallet",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Wallet Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Payment",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Plan History Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(50.dp))
                                .clickable { onClick() }
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Plan History",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                            Text(
                                text = "›",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                        }
                    }

                    // Key Metric: ₹2,800 + [ ! Due Now ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹2,800",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFEF4444))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "! Due Now",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Action Status & Subtitle
                    Text(
                        text = "Due on 16 Oct 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                }
            }

            // PROMINENT FULL-WIDTH ACTION BUTTONS: WhatsApp, Collect Payment, Receipt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // WhatsApp Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF22C55E))
                        .clickable { /* WhatsApp */ }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "💬", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "WhatsApp",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // Collect Payment CTA Button
                Row(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEF4444))
                        .clickable { onClick() }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "₹ Collect Payment",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // View Receipt Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.2.dp, Color(0xFFFDA4AF), RoundedCornerShape(10.dp))
                        .clickable { onViewReceiptClick() }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Receipt",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // Plan Intelligence & 3-Month Payment Schedule Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "👑 Gold Plan", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "• Monthly (Valid till 31 Oct 2026)", fontSize = 7.5.sp, color = Color(0xFF64748B))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Box(Modifier.size(4.5.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                        Text(text = "Auto Pay: Enabled", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFFF1F5F9)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Last Paid (Paid)", fontSize = 8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "15 Sep 2026", fontSize = 8.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Current Due (Due)", fontSize = 8.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                        Text(text = "16 Oct 2026", fontSize = 8.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Next Due (Scheduled)", fontSize = 8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF475569))
                        Text(text = "16 Nov 2026", fontSize = 8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    }
                }
            }

            // 12-Month Capsule Bar Chart (Jan to Dec, ₹3k, ₹2k, ₹1.5k, 0)
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(52.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "₹3k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹2k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹1.5k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 12 Capsule Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        // Status: 1 = Paid (green), 2 = Plan Changed (indigo), 3 = Due Soon (amber), 4 = Due Now (red), 0 = No Data (grey)
                        val months = listOf(
                            Pair(1, 0.75f), // Jan Paid
                            Pair(1, 0.85f), // Feb Paid
                            Pair(1, 0.80f), // Mar Paid
                            Pair(1, 0.80f), // Apr Paid
                            Pair(1, 0.70f), // May Paid
                            Pair(2, 0.65f), // Jun Plan Changed
                            Pair(1, 0.85f), // Jul Paid
                            Pair(1, 0.70f), // Aug Paid
                            Pair(2, 0.75f), // Sep Plan Changed
                            Pair(4, 0.85f), // Oct Due Now
                            Pair(3, 0.50f), // Nov Due Soon
                            Pair(0, 0.30f)  // Dec Unpaid
                        )
                        val count = months.size
                        val barWidth = 8.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        months.forEachIndexed { i, pair ->
                            val x = i * step + (step - barWidth) / 2f
                            val h = size.height * pair.second
                            val y = size.height - h

                            // Draw subtle background track
                            drawRoundRect(
                                color = Color(0x15000000),
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, size.height),
                                cornerRadius = barRadius
                            )

                            val color = when (pair.first) {
                                1 -> Color(0xFF10B981)
                                2 -> Color(0xFF6366F1)
                                3 -> Color(0xFFF59E0B)
                                4 -> Color(0xFFEF4444)
                                else -> Color(0xFFCBD5E1)
                            }

                            drawRoundRect(
                                color = color,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = barRadius
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks: Jan..Dec
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { m ->
                        Text(text = m, fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // Legend Row: Paid 8 months  Partial 2 months  Due 2 months  No Data -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(color = Color(0xFF10B981), label = "Paid & Active", value = "(8 mo)")
                LegendDotItem(color = Color(0xFF6366F1), label = "Plan Changed", value = "(2 mo)")
                LegendDotItem(color = Color(0xFFEF4444), label = "Due Now", value = "(1 mo)")
                LegendDotItem(color = Color(0xFFF59E0B), label = "Due Soon", value = "(1 mo)")
            }

            // Unified Payment Bottom Summary Container (Identical Height & Cohesive Rectangle)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Col 1: Total Paid
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("👛", fontSize = 10.sp)
                        Text("Total Paid", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                    Text("₹12,600", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text("Last 12 mo", fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                // Col 2: Avg / Month
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("📅", fontSize = 10.sp)
                        Text("Avg / Month", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                    Text("₹1,050", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text("Regular", fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                // Col 3: Next Due
                Column(
                    modifier = Modifier.weight(1.15f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("🕒", fontSize = 10.sp)
                        Text("Next Due", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                    Text("16 Oct 2026", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                    Text("Due Now", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                // Col 4: On Time
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Box(Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        Text("On Time", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                    Text("91%", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                    Text("10/11 mo", fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                }
            }
        }
    }
}

// =============================================================================
// 5. MASTER BODY PROGRESS CARD (Soft Lavender #E7D6F5)
// =============================================================================
@Composable
fun MasterBodyProgressCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE7D6F5))
            .border(1.2.dp, Color(0xFFDDD6FE), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Scale & Cloud Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_body_scale),
                    contentDescription = "Body 3D Scale",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Top: Figure Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8B5CF6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🧘", fontSize = 12.sp)
                            }
                            Column {
                                Text(
                                    text = "Body Progress",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Last 6 Mo")
                    }


                    // Key Metric Row: 69.5 kg Current Weight + Delta Pill on left, 3 Numerical Breakdowns on right
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "69.5 kg",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF7C3AED)
                                )
                                Text(
                                    text = "Current Weight",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↓ 2.5 kg",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                                Text(
                                    text = "vs Aug (-3.5%)",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Right 3 Key Numerical Pills (Muscle, Body Fat, Waist)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Muscle", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("57.2 kg", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                                Text("↑ 1.4 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Body Fat", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("17.6 %", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFF97316))
                                Text("↓ 1.2 %", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Waist", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("81 cm", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B5CF6))
                                Text("↓ 4 cm", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Legend Row: Weight (kg)  Muscle (kg)  Body Fat (%)  Waist (cm)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDotItem(color = Color(0xFF10B981), label = "Weight (kg)", value = "")
                        LegendDotItem(color = Color(0xFF0284C7), label = "Muscle (kg)", value = "")
                        LegendDotItem(color = Color(0xFFF97316), label = "Body Fat (%)", value = "")
                        LegendDotItem(color = Color(0xFF8B5CF6), label = "Waist (cm)", value = "")
                    }
                }
            }

            // 12-Month Multi-Line Spline Chart
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(65.dp)) {
                    // Left Y-Axis (90, 60, 30, 0)
                    Column(
                        modifier = Modifier.height(65.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "90", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "60", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "30", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 4 Multi-Line Splines Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp)
                    ) {
                        val count = 12
                        val step = size.width / (count - 1).toFloat()

                        // Normalized data points (0..1 where 0 is bottom, 1 is top)
                        val weightPts = listOf(0.72f, 0.78f, 0.74f, 0.73f, 0.76f, 0.74f, 0.72f, 0.73f, 0.72f, 0.70f, 0.70f, 0.72f)
                        val waistPts = listOf(0.68f, 0.69f, 0.70f, 0.68f, 0.67f, 0.66f, 0.65f, 0.64f, 0.65f, 0.64f, 0.63f, 0.64f)
                        val musclePts = listOf(0.56f, 0.55f, 0.58f, 0.59f, 0.57f, 0.56f, 0.58f, 0.57f, 0.56f, 0.58f, 0.57f, 0.58f)
                        val bodyFatPts = listOf(0.20f, 0.22f, 0.21f, 0.20f, 0.19f, 0.18f, 0.18f, 0.19f, 0.18f, 0.17f, 0.17f, 0.18f)

                        fun drawSpline(points: List<Float>, color: Color) {
                            val path = Path()
                            val coords = points.mapIndexed { i, p ->
                                Offset(i * step, size.height * (1f - p))
                            }
                            if (coords.isNotEmpty()) {
                                path.moveTo(coords[0].x, coords[0].y)
                                for (i in 0 until coords.size - 1) {
                                    val p0 = coords[i]
                                    val p1 = coords[i + 1]
                                    val cx = (p0.x + p1.x) / 2f
                                    path.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                                }
                                drawPath(
                                    path = path,
                                    color = color,
                                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )
                                // Draw circular data dots
                                coords.forEach { c ->
                                    drawCircle(color = color, radius = 2.5.dp.toPx(), center = c)
                                    drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = c)
                                }
                            }
                        }

                        // Draw horizontal subtle reference lines
                        listOf(0.33f, 0.66f, 1.0f).forEach { r ->
                            val y = size.height * (1f - r)
                            drawLine(
                                color = Color(0x20000000),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 0.8.dp.toPx()
                            )
                        }

                        drawSpline(weightPts, Color(0xFF10B981))
                        drawSpline(waistPts, Color(0xFF8B5CF6))
                        drawSpline(musclePts, Color(0xFF0284C7))
                        drawSpline(bodyFatPts, Color(0xFFF97316))
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks: Jan..Dec
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { m ->
                        Text(text = m, fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // --- HIERARCHICAL INTEGRATED PANELS (TOP / MIDDLE / BOTTOM PRIORITY) ---

            // 1. TOP PRIORITY: Primary Key Metrics Integrated Strip
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFDDD6FE), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header Row: Consistency status & BMI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "✓", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
                        Text(
                            text = "Good consistency this month",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "BMI: 22.8",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B21A8)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "Normal",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFFF1F5F9)))

                // 4 Metric Columns (Weight, Muscle Mass, Body Fat, Waist)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Col 1: Weight
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("🏷️", fontSize = 9.sp)
                            Text("Weight", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                        Text("69.5 kg", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("↓ 2.5 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    // Col 2: Muscle Mass
                    Column(
                        modifier = Modifier.weight(1.05f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("💪", fontSize = 9.sp)
                            Text("Muscle", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                        Text("57.2 kg", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                        Text("↑ 1.4 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    // Col 3: Body Fat
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFF97316))
                            Text("Body Fat", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                        Text("17.6 %", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("↓ 1.2 %", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    // Col 4: Waist
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("📏", fontSize = 9.sp)
                            Text("Waist", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                        Text("81 cm", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("↓ 4 cm", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                    }
                }
            }

            // 2. MIDDLE PRIORITY: Top PR Lift & Muscle Focus Distribution Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFDDD6FE), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Top PR Lift
                Column {
                    Text(text = "🏋️ Top Lift: Bench Press", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "65 ➔ 67.5 kg (+4% PR)", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))

                // Right: Muscle Focus Sets Distribution
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "🎯 Current Focus:", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Text(text = "Legs 43%", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                    }
                    Text(
                        text = "Arms 18% • Shoulders 12% • Back 11% • Chest 7%",
                        fontSize = 7.2.sp,
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 3. BOTTOM PRIORITY: Conditioning, Overall Status & Goal Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFDDD6FE), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Strength
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(text = "📊 Strength", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = "↑ 18%", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "Est. 1RM", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                }

                Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                // Endurance
                Column(
                    modifier = Modifier.weight(1.05f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(text = "❤️ Endurance", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = "↑ 22%", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "Cardio Time", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                }

                Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                // Overall Status
                Column(
                    modifier = Modifier.weight(1.05f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(text = "📈 Progress", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF10B981))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "On Track",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                // Goal
                Column(
                    modifier = Modifier.weight(1.1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(text = "🎯 Goal", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = "Lean Muscle", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                    Text(text = "3 months", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// =============================================================================
// 6. MASTER FOOTER BANNER ("Consistent Steps, Brighter Days")
// =============================================================================
@Composable
fun MasterFooterBanner(
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF5D22B6),
                        Color(0xFF4A188C),
                        Color(0xFF6D28D9)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left 3D Dumbbell & Slogan
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                    contentDescription = "Footer Dumbbell",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(42.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Consistent Steps, ",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Brighter Days 彡",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15)
                    )
                }
            }

            // Right "Keep Going! ->" Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .clickable { onKeepGoingClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "➔",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

// =============================================================================
// REUSABLE HELPER ATOMS
// =============================================================================

@Composable
private fun TimeframeDropdownPill(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(10.dp)
        )
    }
}

@Composable
private fun LegendDotItem(color: Color, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 8.5.sp,
            color = Color(0xFF64748B)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

@Composable
private fun MiniStatCard(
    icon: String,
    title: String,
    value: String,
    valueColor: Color = Color(0xFF0F172A),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 13.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = valueColor,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
private fun MetricCardWithDelta(
    icon: String,
    value: String,
    delta: String,
    deltaColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 12.sp)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = value,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = delta,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = deltaColor,
                        maxLines = 1
                    )
                }
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun MiniStatBadgeCard(
    icon: String,
    title: String,
    badgeText: String,
    badgeBg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 12.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(badgeBg)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceHollowCircle(day: Int, status: Int) {
    val (borderColor, bgColor, textColor) = when (status) {
        1 -> Triple(Color(0xFF10B981), Color(0x1810B981), Color(0xFF047857)) // Present (Green)
        2 -> Triple(Color(0xFFF59E0B), Color(0x18F59E0B), Color(0xFFB45309)) // Late (Amber)
        0 -> Triple(Color(0xFFEF4444), Color(0x18EF4444), Color(0xFFDC2626)) // Absent (Red)
        3 -> Triple(Color(0xFF8B5CF6), Color(0x188B5CF6), Color(0xFF6D28D9)) // Holiday (Purple)
        4 -> Triple(Color(0xFFCBD5E1), Color(0xFFF1F5F9), Color(0xFF94A3B8)) // Sunday (Disabled)
        else -> Triple(Color(0xFFCBD5E1), Color.Transparent, Color(0xFFCBD5E1)) // Upcoming (Faint Outline)
    }

    Box(
        modifier = Modifier
            .size(21.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.3.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$day",
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun AttendanceLegendItem(label: String, count: String, color: Color, isHollow: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.5.dp)
                .clip(CircleShape)
                .background(if (isHollow) Color.Transparent else color)
                .then(if (isHollow) Modifier.border(1.dp, color, CircleShape) else Modifier)
        )
        Text(
            text = "$label ($count)",
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569)
        )
    }
}
