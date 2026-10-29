package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlinx.coroutines.launch

/**
 * BAD GYM — Master Storyboard Dashboard V2
 * Architectural Refactoring:
 * 1. Top Section: 8 Member Pass Showcase (Spark, Power, Silver, Gold, Diamond, Platinum, Elite, Basic Overdue)
 * 2. Sticky Menu Bar: Pins permanently below the pass card on scroll with dynamic color blending!
 * 3. Below Expanded Widgets:
 *    - Focused View: Smooth animated tab switching blending color directly from menu into active widget.
 *    - All Expanded View: Full vertical layout of all 4 widgets with scroll-tracking tab illumination.
 * 4. High-Contrast Typography, Unified 44dp Touch Targets & Advanced Data Presentation Techniques:
 *    - Attendance: 30-Day Calendar Strip with filled status tokens & weekly consistency metrics
 *    - Workout: 62dp Volume Bars Canvas with dashed 70m average guideline & intensity hierarchy
 *    - Payment: 12-Month Capsule Bar Chart with status tracks & prominent ₹2,800 due trigger
 *    - Body Progress: Interactive Metric Selector (Weight, Muscle, Fat, Waist) with calibrated Y-axes
 */
@OptIn(ExperimentalFoundationApi::class)
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
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var isAllExpandedMode by remember { mutableStateOf(false) }

    // In All-Expanded mode, automatically detect which widget is visible on screen
    val visibleTabIndex by remember {
        derivedStateOf {
            if (!isAllExpandedMode) {
                selectedTabIndex
            } else {
                val first = lazyListState.firstVisibleItemIndex
                when {
                    first <= 1 -> 0
                    first == 2 -> 0 // Attendance
                    first == 3 -> 1 // Workout
                    first == 4 -> 2 // Payment
                    else -> 3       // Body Progress
                }
            }
        }
    }

    val activeIndex = if (isAllExpandedMode) visibleTabIndex else selectedTabIndex

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // 1. TOP HERO HEADER & 8 MEMBER PASS SHOWCASE
        item(key = "master_hero_header") {
            Box(modifier = Modifier.padding(bottom = 8.dp)) {
                MasterHeroHeader(
                    data = data,
                    onAvatarClick = onAvatarClick
                )
            }
        }

        // 2. STICKY MENU BAR (Blends color seamlessly into active widget below)
        stickyHeader(key = "master_sticky_menu_bar") {
            MasterStoryboardStickyMenuBar(
                selectedTabIndex = activeIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    if (isAllExpandedMode) {
                        coroutineScope.launch {
                            // Item 0 is hero, 1 is sticky header, items 2..5 are widgets
                            lazyListState.animateScrollToItem(index = index + 2)
                        }
                    }
                },
                isAllExpanded = isAllExpandedMode,
                onToggleExpandedMode = {
                    isAllExpandedMode = !isAllExpandedMode
                }
            )
        }

        // 3. BELOW EXPANDED WIDGETS
        if (isAllExpandedMode) {
            // ALL 4 WIDGETS EXPANDED VERTICALLY
            item(key = "widget_attendance") {
                Box(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)) {
                    MasterAttendanceCard(
                        data = data,
                        onClick = onAttendanceClick,
                        isBlendedWithTab = false
                    )
                }
            }
            item(key = "widget_workout") {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    MasterWorkoutTrendCard(
                        data = data,
                        onClick = onWorkoutClick,
                        isBlendedWithTab = false
                    )
                }
            }
            item(key = "widget_payment") {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    MasterPaymentCard(
                        data = data,
                        onClick = onPaymentClick,
                        onViewReceiptClick = onViewReceiptClick,
                        isBlendedWithTab = false
                    )
                }
            }
            item(key = "widget_body") {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    MasterBodyProgressCard(
                        data = data,
                        onClick = onBodyProgressClick,
                        isBlendedWithTab = false
                    )
                }
            }
        } else {
            // FOCUSED WIDGET: CLEAN FLOATING CARD MATCHING MOBILE DESIGN
            item(key = "widget_focused") {
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220, delayMillis = 40)) +
                         slideInVertically(animationSpec = tween(260)) { it / 10 })
                        .togetherWith(
                            fadeOut(animationSpec = tween(180))
                        )
                    },
                    label = "WidgetBlendTransition"
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> MasterAttendanceCard(
                            data = data,
                            onClick = onAttendanceClick,
                            isBlendedWithTab = false
                        )
                        1 -> MasterWorkoutTrendCard(
                            data = data,
                            onClick = onWorkoutClick,
                            isBlendedWithTab = false
                        )
                        2 -> MasterPaymentCard(
                            data = data,
                            onClick = onPaymentClick,
                            onViewReceiptClick = onViewReceiptClick,
                            isBlendedWithTab = false
                        )
                        3 -> MasterBodyProgressCard(
                            data = data,
                            onClick = onBodyProgressClick,
                            isBlendedWithTab = false
                        )
                    }
                }
            }
        }

        // 4. BOTTOM FOOTER BANNER
        item(key = "master_footer_banner") {
            Spacer(modifier = Modifier.height(10.dp))
            MasterFooterBanner(
                onKeepGoingClick = onWorkoutClick
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// =============================================================================
// STICKY STORYBOARD MENU BAR WITH DYNAMIC COLOR BLENDING
// =============================================================================

private data class DashboardTabItem(
    val icon: ImageVector,
    val label: String,
    val badge: String
)

@Composable
fun MasterStoryboardStickyMenuBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    isAllExpanded: Boolean = false,
    onToggleExpandedMode: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeColor = when (selectedTabIndex) {
        0 -> Color(0xFF059669) // Mint Emerald
        1 -> Color(0xFF0284C7) // Sky Blue
        2 -> Color(0xFF059669) // Emerald Green (matching reference design!)
        else -> Color(0xFF7C3AED) // Amethyst Purple
    }

    val activeTabBg = when (selectedTabIndex) {
        0 -> Color(0xFFDCFCE7)
        1 -> Color(0xFFE0F2FE)
        2 -> Color(0xFFDCFCE7)
        else -> Color(0xFFF3E8FF)
    }

    val activeSurfaceBg = when (selectedTabIndex) {
        0 -> Color(0xFFEFFEF2)
        1 -> Color(0xFFE5F1FB)
        2 -> Color(0xFFEFFEF2)
        else -> Color(0xFFF3E8FF)
    }

    val activeBorderColor = when (selectedTabIndex) {
        0 -> Color(0xFF86EFAC)
        1 -> Color(0xFF7DD3FC)
        2 -> Color(0xFF86EFAC)
        else -> Color(0xFFD8B4FE)
    }

    val animatedColor by animateColorAsState(activeColor, tween(300), label = "tabColor")
    val animatedTabBg by animateColorAsState(activeTabBg, tween(300), label = "tabBg")
    val animatedSurfaceBg by animateColorAsState(activeSurfaceBg, tween(300), label = "surfaceBg")
    val animatedBorderColor by animateColorAsState(activeBorderColor, tween(300), label = "borderColor")

    val tabs = remember {
        listOf(
            DashboardTabItem(Icons.Default.CalendarToday, "Attendance", "90%"),
            DashboardTabItem(Icons.Default.FitnessCenter, "Workout", "70m"),
            DashboardTabItem(Icons.Default.AccountBalanceWallet, "Payment", "₹800"),
            DashboardTabItem(Icons.Default.Person, "Body", "69.5kg")
        )
    }

    val menuShape = RoundedCornerShape(16.dp)

    // Top Header Dock
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(menuShape)
            .background(animatedSurfaceBg)
            .border(1.2.dp, animatedBorderColor, menuShape)
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index
                val tabItemColor = when (index) {
                    0 -> Color(0xFF059669)
                    1 -> Color(0xFF0284C7)
                    2 -> Color(0xFF059669)
                    else -> Color(0xFF7C3AED)
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .border(
                            width = if (isSelected) 1.2.dp else 0.dp,
                            color = if (isSelected) tabItemColor.copy(alpha = 0.8f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 1.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (isSelected) tabItemColor else Color(0xFF64748B),
                        modifier = Modifier.size(14.5.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            letterSpacing = (-0.3).sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) tabItemColor else Color(0xFF334155),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = tab.badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) tabItemColor else Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Target Icon Pill on far right of tabs dock
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFCBD5E1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎯", fontSize = 14.sp)
            }
        }
    }
}

// =============================================================================
// HERO PASS HEADER
// =============================================================================

@Composable
fun MasterHeroHeader(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
// 1. MASTER ATTENDANCE CARD (Mint Green #EFFEF2)
// =============================================================================

@Composable
fun MasterAttendanceCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
    } else {
        RoundedCornerShape(20.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(450.dp)
            .clip(cardShape)
            .background(Color(0xFFEFFEF2))
            .border(1.2.dp, Color(0xFF86EFAC), cardShape)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row: 3D Illustration + Detail Column
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
                    contentDescription = "Attendance 3D Calendar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(68.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Title & Month Dropdown
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
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Attendance",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 30 Days",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Oct 2026")
                    }

                    // Key Metric Row: 90% Hero + Delta on left, Status Dots on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "90%",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = "27 / 30 Present Days",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 7.dp, vertical = 2.5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↑ 5%",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "vs Sep",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            LegendDotItem(color = Color(0xFF10B981), label = "Present", value = "(27 d)")
                            LegendDotItem(color = Color(0xFFEF4444), label = "Absent", value = "(1 d)")
                        }
                    }
                }
            }

            // Quick Info Metrics Strip (4 KPIs for Attendance, matching Workout style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFF86EFAC), RoundedCornerShape(10.dp))
                    .padding(horizontal = 7.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Present", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "27d", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                    Text(text = "90% rate", fontSize = 10.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Late", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "1d", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                    Text(text = "3% rate", fontSize = 10.5.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Absent", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "1d", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                    Text(text = "3% rate", fontSize = 10.5.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Holiday", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "1d", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B5CF6))
                    Text(text = "3% rate", fontSize = 10.5.sp, color = Color(0xFF8B5CF6), fontWeight = FontWeight.Bold)
                }
            }

            // 30-Day Attendance Grid (Days 1..30 with Solid Status Badges)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // 1 = Present (Green), 2 = Late (Amber), 0 = Absent (Red), 3 = Holiday (Purple), 4 = Sunday (Gray), 5 = Upcoming (Outline)
                val dayStatuses = remember {
                    listOf(
                        1, 3, 1, 4, 1, 2, 1, 1, 1, 1, 4, 1, 1, 1, 0,
                        1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 1, 1, 5, 5, 5
                    )
                }

                // Row 1: Days 1 to 15
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 1..15) {
                        AttendanceStatusCircle(day = day, status = dayStatuses[day - 1])
                    }
                }

                // Row 2: Days 16 to 30
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 16..30) {
                        AttendanceStatusCircle(day = day, status = dayStatuses[day - 1])
                    }
                }

                // High-Contrast Legend Strip
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
                    AttendanceLegendItem(label = "Sun", count = "4", color = Color(0xFF94A3B8))
                    AttendanceLegendItem(label = "Next", count = "3", color = Color(0xFFCBD5E1), isHollow = true)
                }
            }

            // Weekly Consistency Trend (Low-Width Split Row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Low-Width Weekly Trend Card (only required space, ~145dp)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(0.9.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.width(145.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("📊", fontSize = 11.sp)
                            Text(
                                text = "Weekly Trend",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                        }

                        // 4 Vertical Progress Bars
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val weekData = listOf(
                                Triple("W1", 100, "6/6"),
                                Triple("W2", 85, "5/6"),
                                Triple("W3", 100, "6/6"),
                                Triple("W4", 80, "4/5")
                            )

                            weekData.forEach { (weekLabel, pct, daysText) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "$pct%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (pct >= 85) Color(0xFF047857) else Color(0xFFD97706)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(13.dp)
                                            .height(26.dp)
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        val fillHeightFraction = (pct / 100f).coerceIn(0.15f, 1f)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height((26 * fillHeightFraction).dp)
                                                .clip(RoundedCornerShape(50.dp))
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(
                                                            if (pct >= 85) Color(0xFF34D399) else Color(0xFFFDE047),
                                                            if (pct >= 85) Color(0xFF059669) else Color(0xFFD97706)
                                                        )
                                                    )
                                                )
                                        )
                                    }

                                    Text(
                                        text = weekLabel,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F172A)
                                    )

                                    Text(
                                        text = daysText,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Monthly Target Achievement Card (De-duplicated)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(0.9.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🎯", fontSize = 11.5.sp)
                                Text(
                                    text = "Monthly Goal",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
                            ) {
                                Text("On Track", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                            }
                        }

                        Text(
                            text = "≥85% Target Met",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF047857)
                        )

                        Text(
                            text = "27 of 30 days present (Goal: 26d)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text("🔥", fontSize = 11.sp)
                            Text(
                                text = "6-day active streak running",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            // Unified Attendance Summary Dock (De-duplicated)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🔥", fontSize = 12.sp)
                            Text("Streak", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("6 Days", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Active", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🏆", fontSize = 12.sp)
                            Text("Best", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("12 Days", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Record", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Text("Standing", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("Top 5%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                        Text("Elite Tier", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(26.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("📅", fontSize = 12.sp)
                            Text("Visits", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("27 Days", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Oct 2026", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceBreakdownCol(label: String, count: String, pct: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(4.5.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(2.dp))
            Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), maxLines = 1, softWrap = false)
        }
        Text(count, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
        Text(pct, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// =============================================================================
// 2. MASTER WORKOUT TREND CARD (Pixel-Perfect Target Matching media_1793268101617_bba53a00.jpg)
// =============================================================================

@Composable
fun MasterWorkoutTrendCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // CARD 1: 30-Day Workout Trend Card
        Workout30DayTrendCard()

        // CARD 2: Monthly Pattern Card
        WorkoutMonthlyPatternCard()
    }
}

// -----------------------------------------------------------------------------
// CARD 1: 30-DAY WORKOUT TREND CARD
// -----------------------------------------------------------------------------

@Composable
private fun Workout30DayTrendCard() {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color.White)
            .border(1.2.dp, Color(0xFFBAE6FD), cardShape)
            .padding(horizontal = 10.dp, vertical = 9.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // 1. Header & Hero Metric Section
            WorkoutHeaderSection()

            // 2. 4 Quick KPI Pods Row
            WorkoutQuickKpiRow()

            // 3. 30-Day Daily Bar Chart Section
            Workout30DayBarChartSection()
        }
    }
}

@Composable
private fun WorkoutHeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // 3D Dumbbells Illustration
        Image(
            painter = painterResource(id = R.drawable.illustration_3d_workout_dumbbells),
            contentDescription = "Workout 3D Dumbbells",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(58.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1: Badge + "Workout Trend" + "Last 30 Days" | "Oct 2026 ⌵"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Workout Trend",
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Last 30 Days",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                TimeframeDropdownPill(text = "Oct 2026")
            }

            // Row 2: Hero Metric (70 min, Avg/Session, ↑8% vs Sep) on left | Legend on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column {
                        Text(
                            text = "70 min",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0284C7),
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "Avg / Session",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "↑ 8%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                        }
                        Text(
                            text = "vs Sep",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // 3-Row Legend
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF38BDF8)))
                        Text("Workout", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Text("(22 d)", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                        Text("Rest", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Text("(8 d)", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(Modifier.size(7.dp).clip(CircleShape).border(1.dp, Color(0xFFCBD5E1), CircleShape))
                        Text("No Membership", fontSize = 9.5.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                        Text("(0 d)", fontSize = 9.5.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 4 QUICK KPI PODS ROW
// -----------------------------------------------------------------------------

@Composable
private fun WorkoutQuickKpiRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pod 1: Total Time
        WorkoutKpiPod(
            icon = Icons.Default.Timer,
            iconTint = Color(0xFF10B981),
            value = "823 min",
            label = "Total Time",
            subLabel = null,
            subLabelColor = Color.Transparent,
            showInfoIcon = false,
            modifier = Modifier.weight(1f)
        )

        // Pod 2: Active Days
        WorkoutKpiPod(
            icon = Icons.Default.DateRange,
            iconTint = Color(0xFF0284C7),
            value = "22 Days",
            label = "Active Days",
            subLabel = "73% active",
            subLabelColor = Color(0xFF16A34A),
            showInfoIcon = false,
            modifier = Modifier.weight(1f)
        )

        // Pod 3: Peak Session
        WorkoutKpiPod(
            icon = Icons.Default.EmojiEvents,
            iconTint = Color(0xFFD97706),
            value = "123 min",
            label = "Peak Session",
            subLabel = "15 Oct",
            subLabelColor = Color(0xFF0284C7),
            showInfoIcon = false,
            modifier = Modifier.weight(1f)
        )

        // Pod 4: Energy Burn
        WorkoutKpiPod(
            icon = Icons.Default.LocalFireDepartment,
            iconTint = Color(0xFFEA580C),
            value = "~14.5k",
            label = "Energy Burn",
            subLabel = null,
            subLabelColor = Color.Transparent,
            showInfoIcon = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WorkoutKpiPod(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    subLabel: String?,
    subLabelColor: Color,
    showInfoIcon: Boolean,
    modifier: Modifier = Modifier
) {
    val podShape = RoundedCornerShape(10.dp)
    Row(
        modifier = modifier
            .clip(podShape)
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), podShape)
            .padding(horizontal = 4.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(15.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(0.5.dp)) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                maxLines = 1,
                softWrap = false
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.5.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    softWrap = false
                )
                if (showInfoIcon) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(8.5.dp)
                    )
                }
            }
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = subLabelColor,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 30-DAY DAILY BAR CHART SECTION
// -----------------------------------------------------------------------------

private data class DailyWorkoutBar(
    val day: Int,
    val minutes: Int
)

@Composable
private fun FloatingChartBadge(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(backgroundColor)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 7.5.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 6.2.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 7.sp
                )
            }
        }
        // Speech-bubble downward caret
        Box(
            modifier = Modifier
                .offset(y = (-2.5).dp)
                .size(5.dp)
                .rotate(45f)
                .background(backgroundColor)
        )
    }
}

@Composable
private fun Workout30DayBarChartSection() {
    // 30 days matching target: 8 High (≥90m), 12 Medium (30-89m), 2 Low (<30m), 8 Rest (0m)
    val dailyData = remember {
        listOf(
            DailyWorkoutBar(1, 55),
            DailyWorkoutBar(2, 0),
            DailyWorkoutBar(3, 32),
            DailyWorkoutBar(4, 60),
            DailyWorkoutBar(5, 25), // Min (5 Oct) -> Red Bar & Red Badge
            DailyWorkoutBar(6, 22), // Low (<30m) -> Amber Bar
            DailyWorkoutBar(7, 0),
            DailyWorkoutBar(8, 48),
            DailyWorkoutBar(9, 58),
            DailyWorkoutBar(10, 92), // High (≥90m)
            DailyWorkoutBar(11, 0),
            DailyWorkoutBar(12, 52),
            DailyWorkoutBar(13, 60),
            DailyWorkoutBar(14, 50),
            DailyWorkoutBar(15, 120), // Peak (15 Oct) -> Green Gradient Bar & Green Badge
            DailyWorkoutBar(16, 65),
            DailyWorkoutBar(17, 0),
            DailyWorkoutBar(18, 50),
            DailyWorkoutBar(19, 60),
            DailyWorkoutBar(20, 0),
            DailyWorkoutBar(21, 0),
            DailyWorkoutBar(22, 95), // High (≥90m)
            DailyWorkoutBar(23, 90), // High (≥90m)
            DailyWorkoutBar(24, 92), // High (≥90m)
            DailyWorkoutBar(25, 98), // High (≥90m)
            DailyWorkoutBar(26, 95), // High (≥90m)
            DailyWorkoutBar(27, 0),
            DailyWorkoutBar(28, 90), // High (≥90m)
            DailyWorkoutBar(29, 0),
            DailyWorkoutBar(30, 62)
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
        ) {
            // Left Y-Axis
            Column(
                modifier = Modifier
                    .height(78.dp)
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "120m", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "90m", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "60m", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "30m", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "0m", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            }

            // 30 Daily Bars + Guidelines + Floating Badges
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .height(78.dp)
            ) {
                val labelMarginDp = 36.dp
                val plotWidthDp = maxWidth - labelMarginDp
                val stepDp = plotWidthDp / 30f

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val labelMarginPx = labelMarginDp.toPx()
                    val plotWidthPx = size.width - labelMarginPx
                    val step = plotWidthPx / dailyData.size.toFloat()
                    val barWidth = 3.6.dp.toPx()
                    val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                    // Subtle solid grid lines at 90m, 60m, 30m, 0m (across full width)
                    listOf(90f, 60f, 30f, 0f).forEach { gridMins ->
                        val gridY = size.height * (1f - (gridMins / 125f))
                        drawLine(
                            color = Color(0xFFE2E8F0).copy(alpha = 0.45f),
                            start = Offset(0f, gridY),
                            end = Offset(size.width, gridY),
                            strokeWidth = 0.8.dp.toPx()
                        )
                    }

                    // 1. Max Guideline (120m) - Green dashed line (across full width)
                    val max120Y = size.height * (1f - (120f / 125f))
                    drawLine(
                        color = Color(0xFF10B981).copy(alpha = 0.55f),
                        start = Offset(0f, max120Y),
                        end = Offset(size.width, max120Y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )

                    // 2. Avg Guideline (70m) - Blue dashed line (across full width)
                    val avg70Y = size.height * (1f - (70f / 125f))
                    drawLine(
                        color = Color(0xFF0284C7).copy(alpha = 0.55f),
                        start = Offset(0f, avg70Y),
                        end = Offset(size.width, avg70Y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )

                    // 3. Min Guideline (25m) - Orange dashed line (across full width)
                    val min25Y = size.height * (1f - (25f / 125f))
                    drawLine(
                        color = Color(0xFFF59E0B).copy(alpha = 0.55f),
                        start = Offset(0f, min25Y),
                        end = Offset(size.width, min25Y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )

                    // Draw 30 Bars inside plotWidthPx
                    dailyData.forEachIndexed { i, item ->
                        val x = i * step + (step - barWidth) / 2f
                        if (item.minutes == 0) {
                            val h = 2.5.dp.toPx()
                            val y = size.height - h
                            drawRoundRect(
                                color = Color(0xFFE2E8F0),
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = barRadius
                            )
                        } else {
                            val hRatio = (item.minutes / 125f).coerceIn(0.08f, 0.98f)
                            val h = size.height * hRatio
                            val y = size.height - h

                            if (i == 14) { // Day 15 Peak (Green gradient)
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF34D399), Color(0xFF10B981))
                                    ),
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, h),
                                    cornerRadius = barRadius
                                )
                            } else if (i == 4) { // Day 5 Min (Coral Red bar matching badge)
                                drawRoundRect(
                                    color = Color(0xFFEF4444),
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, h),
                                    cornerRadius = barRadius
                                )
                            } else {
                                val barColor = when {
                                    item.minutes >= 90 -> Color(0xFF0284C7)
                                    item.minutes >= 30 -> Color(0xFF38BDF8)
                                    else -> Color(0xFFFBBF24)
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
                }

                // Guideline Labels on Right Edge (positioned in labelMarginDp zone)
                Text(
                    text = "Max 120m",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16A34A),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-3).dp)
                )

                Text(
                    text = "Avg 70m",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (78f * (1f - (70f / 125f)) - 9f).dp)
                )

                Text(
                    text = "Min 25m",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (78f * (1f - (25f / 125f)) - 9f).dp)
                )

                // Floating Badge 1: Day 15 (Peak 120m - Green Speech Bubble)
                FloatingChartBadge(
                    title = "120m",
                    subtitle = "(15 Oct)",
                    backgroundColor = Color(0xFF10B981),
                    modifier = Modifier.offset(x = (stepDp * 14.5f - 16.dp), y = 0.dp)
                )

                // Floating Badge 2: Day 5 (Min 25m - Red Speech Bubble)
                FloatingChartBadge(
                    title = "25m",
                    subtitle = "(5 Oct)",
                    backgroundColor = Color(0xFFEF4444),
                    modifier = Modifier.offset(
                        x = (stepDp * 4.5f - 14.dp),
                        y = (78f * (1f - (25f / 125f)) - 22f).dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // X-Axis Marks Row (Days 1, 5, 10, 15, 20, 25, 30 precisely aligned under bars)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 36.dp)
                .height(14.dp)
        ) {
            val stepDp = maxWidth / 30f
            listOf(1, 5, 10, 15, 20, 25, 30).forEach { day ->
                val xPos = (stepDp * (day - 0.5f)) - 5.dp
                Text(
                    text = day.toString(),
                    fontSize = 8.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = xPos, y = 0.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // 4-Tier Workout Intensity Legend (Single row, non-wrapping)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WorkoutLegendItem(color = Color(0xFF0284C7), label = "High (≥90m)", value = "8 d")
            WorkoutLegendItem(color = Color(0xFF38BDF8), label = "Medium (30–89m)", value = "12 d")
            WorkoutLegendItem(color = Color(0xFFFBBF24), label = "Low (<30m)", value = "2 d")
            WorkoutLegendItem(color = Color(0xFFCBD5E1), label = "Rest", value = "8 d")
        }
    }
}

@Composable
private fun WorkoutLegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 7.6.sp,
            letterSpacing = (-0.2).sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569),
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = value,
            fontSize = 7.6.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A),
            maxLines = 1,
            softWrap = false
        )
    }
}

// -----------------------------------------------------------------------------
// CARD 2: MONTHLY PATTERN CARD
// -----------------------------------------------------------------------------

private data class MonthlyWorkoutItem(
    val month: String,
    val daysLabel: String?,
    val heightDp: Float,
    val isNoMembership: Boolean = false,
    val isPeak: Boolean = false,
    val isLow: Boolean = false
)

@Composable
private fun WorkoutMonthlyPatternCard() {
    val cardShape = RoundedCornerShape(16.dp)

    val monthlyData = remember {
        listOf(
            MonthlyWorkoutItem("Jan", "18d", 28f),
            MonthlyWorkoutItem("Feb", "20d", 32f),
            MonthlyWorkoutItem("Mar", "6d", 10f, isLow = true),
            MonthlyWorkoutItem("Apr", null, 0f, isNoMembership = true),
            MonthlyWorkoutItem("May", "12d", 20f),
            MonthlyWorkoutItem("Jun", "16d", 26f),
            MonthlyWorkoutItem("Jul", "22d", 36f, isPeak = true),
            MonthlyWorkoutItem("Aug", null, 0f, isNoMembership = true),
            MonthlyWorkoutItem("Sep", "14d", 23f),
            MonthlyWorkoutItem("Oct", "22d", 36f),
            MonthlyWorkoutItem("Nov", "8d", 13f, isLow = true),
            MonthlyWorkoutItem("Dec", "20d", 32f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), cardShape)
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Header: Icon + Monthly Pattern | 12 Months ⌵
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                        modifier = Modifier.height(13.dp)
                    ) {
                        Box(Modifier.width(3.dp).height(8.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF10B981)))
                        Box(Modifier.width(3.dp).height(13.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF0284C7)))
                        Box(Modifier.width(3.dp).height(10.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF38BDF8)))
                    }
                    Text(
                        text = "Monthly Pattern",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                }

                TimeframeDropdownPill(text = "12 Months")
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 1. 12 Monthly Bars Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                monthlyData.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Value label on top
                        if (item.daysLabel != null) {
                            Text(
                                text = item.daysLabel,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                        } else {
                            Spacer(modifier = Modifier.height(11.dp))
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // Bar or No Membership box
                        if (item.isNoMembership) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(26.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(0.8.dp, Color(0xFFCBD5E1), RoundedCornerShape(3.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No\nMemb",
                                    fontSize = 6.2.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 7.5.sp,
                                    maxLines = 2,
                                    softWrap = false
                                )
                            }
                        } else {
                            val barBrush = when {
                                item.isPeak -> Brush.verticalGradient(
                                    listOf(Color(0xFF34D399), Color(0xFF10B981))
                                )
                                item.isLow -> Brush.verticalGradient(
                                    listOf(Color(0xFFFCD34D), Color(0xFFFBBF24))
                                )
                                else -> Brush.verticalGradient(
                                    listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(item.heightDp.dp)
                                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                                    .background(barBrush)
                            )
                        }
                    }
                }
            }

            // 2. Subtle continuous horizontal baseline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color(0xFFE2E8F0).copy(alpha = 0.7f))
            )

            // 3. Month labels row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                monthlyData.forEach { item ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.month,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 3. MASTER PAYMENT CARD (Clean White Card #FFFFFF with Stacked Monthly Trend)
// =============================================================================

@Composable
fun MasterPaymentCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
    } else {
        RoundedCornerShape(20.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(450.dp)
            .clip(cardShape)
            .background(Color.White)
            .border(1.2.dp, Color(0xFFFDA4AF), cardShape)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 9.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // SECTION 1: HEADER & DUE AMOUNT
            PaymentHeaderSection(onPlanHistoryClick = onClick)

            // SECTION 2: 3 ACTION BUTTONS (Send Reminder, Collect Payment, Send Receipt)
            PaymentActionButtonsRow(
                onSendReminder = { /* WhatsApp */ },
                onCollectPayment = onClick,
                onSendReceipt = onViewReceiptClick
            )

            // SECTION 3: PAYMENT TREND (Stacked 12-Month Bar Chart & Legend)
            PaymentTrendSection()

            // SECTION 4: BOTTOM 3 METRIC CARDS (Collected, Unpaid Balance, Waived by Owner)
            PaymentSummaryMetricsRow()
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 1: HEADER & DUE AMOUNT
// -----------------------------------------------------------------------------

@Composable
private fun PaymentHeaderSection(
    onPlanHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: 3D Cute Wallet Illustration
        Image(
            painter = painterResource(id = R.drawable.illustration_3d_payment_wallet),
            contentDescription = "Payment 3D Wallet",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Header Row: Red icon + Payment / Monthly Plan ₹1,200 + Plan History >
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Payment",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "Monthly Plan ₹1,200",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            lineHeight = 13.sp
                        )
                    }
                }

                // Plan History > Pill Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFFFF1F2))
                        .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(50.dp))
                        .clickable { onPlanHistoryClick() }
                        .padding(horizontal = 9.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Plan History",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE11D48)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFFE11D48),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Middle: ₹ 800 + [ Pending ] Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "₹ 800",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFE11D48),
                    lineHeight = 32.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Pending",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Subtext: 📅 Due on 12 Oct 2026 (4 days left)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Due on 12 Oct 2026 ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
                Text(
                    text = "(4 days left)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 2: 3 ACTION BUTTONS (Send Reminder, Collect Payment, Send Receipt)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentActionButtonsRow(
    onSendReminder: () -> Unit = {},
    onCollectPayment: () -> Unit = {},
    onSendReceipt: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Send Reminder on WhatsApp
        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF16A34A))
                .clickable { onSendReminder() }
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "💬", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Send Reminder\non WhatsApp",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 12.sp
            )
        }

        // 2. Collect Payment
        Row(
            modifier = Modifier
                .weight(1.15f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFDC2626))
                .clickable { onCollectPayment() }
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Collect Payment",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        // 3. Send Receipt on WhatsApp
        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                .clickable { onSendReceipt() }
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Send Receipt\non WhatsApp",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 12.sp
            )
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 3: PAYMENT TREND STACKED 12-MONTH BAR CHART & LEGEND
// -----------------------------------------------------------------------------

private data class MonthlyPaymentData(
    val month: String,
    val totalLabel: String?,
    val blocks: List<PaymentBlock> = emptyList(),
    val specialText: String? = null
)

private data class PaymentBlock(
    val amountText: String,
    val color: Color,
    val heightDp: Int
)

@Composable
private fun PaymentTrendSection(modifier: Modifier = Modifier) {
    val monthsData = remember {
        listOf(
            MonthlyPaymentData("Jan", "₹1.2k", listOf(PaymentBlock("₹600", Color(0xFF10B981), 16), PaymentBlock("₹600", Color(0xFF10B981), 16))),
            MonthlyPaymentData("Feb", "₹600", listOf(PaymentBlock("₹600", Color(0xFF10B981), 18))),
            MonthlyPaymentData("Mar", null, specialText = "No\nADM"),
            MonthlyPaymentData("Apr", null, specialText = "No\nADM"),
            MonthlyPaymentData("May", "₹1.2k", listOf(PaymentBlock("₹1.2k", Color(0xFF10B981), 32))),
            MonthlyPaymentData("Jun", "₹840", listOf(PaymentBlock("₹440", Color(0xFFEF4444), 16), PaymentBlock("₹400", Color(0xFFF59E0B), 15))),
            MonthlyPaymentData("Jul", "₹2.0k", listOf(
                PaymentBlock("₹500", Color(0xFF10B981), 13),
                PaymentBlock("₹500", Color(0xFF10B981), 13),
                PaymentBlock("₹500", Color(0xFF10B981), 13),
                PaymentBlock("₹500", Color(0xFF8B5CF6), 13)
            )),
            MonthlyPaymentData("Aug", null, specialText = "No\nVisit"),
            MonthlyPaymentData("Sep", "₹1.2k", listOf(PaymentBlock("₹1.2k", Color(0xFF10B981), 32))),
            MonthlyPaymentData("Oct", "₹400", listOf(PaymentBlock("₹400", Color(0xFFEF4444), 16))),
            MonthlyPaymentData("Nov", null, specialText = "₹100\nWaived"),
            MonthlyPaymentData("Dec", "₹2.4k", listOf(
                PaymentBlock("₹800", Color(0xFF10B981), 18),
                PaymentBlock("₹800", Color(0xFF10B981), 18),
                PaymentBlock("₹800", Color(0xFF8B5CF6), 18)
            ))
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // Header Row: 📊 Payment Trend ℹ️ + 12 Months ⌵
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "📊", fontSize = 13.sp)
                Text(
                    text = "Payment Trend",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
            }

            // Dropdown Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(0.7.dp, Color(0xFFCBD5E1), RoundedCornerShape(50.dp))
                    .padding(horizontal = 7.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "12 Months",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF334155),
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        // Stacked Bar Chart with Y-Axis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Y-Axis Labels
            Column(
                modifier = Modifier.height(68.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "₹3k", fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "₹2k", fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "₹1k", fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                Text(text = "0", fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(3.dp))

            // 12 Monthly Stacked Bars Row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                monthsData.forEach { data ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Total amount label on top
                        if (data.totalLabel != null) {
                            Text(
                                text = data.totalLabel,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(1.5.dp))

                        // Middle: Stacked blocks OR Special outline box
                        if (data.specialText != null) {
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(26.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(0.8.dp, Color(0xFFCBD5E1), RoundedCornerShape(3.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = data.specialText,
                                    fontSize = 6.2.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 7.5.sp,
                                    maxLines = 2,
                                    softWrap = false
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.width(22.dp),
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                data.blocks.reversed().forEach { block ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(block.heightDp.dp)
                                            .clip(RoundedCornerShape(2.5.dp))
                                            .background(block.color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = block.amountText,
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // Month label at bottom
                        Text(
                            text = data.month,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }

        // Legend Rows (2 clean rows of 3 items matching reference)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 1.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendColorItem(color = Color(0xFF10B981), label = "On Time (6 months)")
                LegendColorItem(color = Color(0xFFF59E0B), label = "Partial (1 month)")
                LegendColorItem(color = Color(0xFFEF4444), label = "Late (1 month)")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendColorItem(color = Color(0xFF8B5CF6), label = "Extra (1 month)")
                LegendBorderItem(label = "No Admission (2 months)")
                LegendHatchedItem(label = "Waived (1 month)")
            }
        }
    }
}

@Composable
private fun LegendColorItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LegendBorderItem(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.5.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(Color(0xFFF8FAFC))
                .border(0.8.dp, Color(0xFF94A3B8), RoundedCornerShape(1.5.dp))
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LegendHatchedItem(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.5.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(Color(0xFFE2E8F0))
                .border(0.8.dp, Color(0xFF94A3B8), RoundedCornerShape(1.5.dp))
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Bold
        )
    }
}

// -----------------------------------------------------------------------------
// SECTION 4: BOTTOM 3 METRIC CARDS (Collected, Unpaid Balance, Waived by Owner)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentSummaryMetricsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Card 1: Collected (Jan - Dec)
        Box(
            modifier = Modifier
                .weight(1.15f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(0.9.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Donut Chart Canvas
                Canvas(modifier = Modifier.size(28.dp)) {
                    val stroke = 3.8.dp.toPx()
                    // Green segment: 75%
                    drawArc(
                        color = Color(0xFF10B981),
                        startAngle = -90f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    // Amber segment: 15%
                    drawArc(
                        color = Color(0xFFF59E0B),
                        startAngle = 180f,
                        sweepAngle = 54f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    // Red segment: 10%
                    drawArc(
                        color = Color(0xFFEF4444),
                        startAngle = 234f,
                        sweepAngle = 36f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = "Collected (Jan – Dec)",
                        fontSize = 8.5.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "₹11,400",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF059669)
                    )
                    Text(
                        text = "(9 payments)",
                        fontSize = 8.5.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Card 2: Unpaid Balance
        Box(
            modifier = Modifier
                .weight(0.92f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(0.9.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "🎯", fontSize = 15.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "Unpaid Balance",
                            fontSize = 8.5.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "₹100",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                        Text(
                            text = "Nov 2026 Fee",
                            fontSize = 8.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        // Card 3: Waived by Owner
        Box(
            modifier = Modifier
                .weight(0.93f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(0.9.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Percent,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "Waived by Owner",
                            fontSize = 8.5.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "₹100",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669)
                        )
                        Text(
                            text = "Nov 2026 Fee",
                            fontSize = 8.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(11.dp)
                )
            }
        }
    }
}

// =============================================================================
// 4. MASTER BODY PROGRESS CARD (Clean White Card #FFFFFF with Goal-Based Tracking)
// =============================================================================

@Composable
fun MasterBodyProgressCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedMetricIndex by remember { mutableIntStateOf(0) }
    var selectedTimeIndex by remember { mutableIntStateOf(2) }

    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
    } else {
        RoundedCornerShape(20.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(450.dp)
            .clip(cardShape)
            .background(Color.White)
            .border(1.2.dp, Color(0xFFDDD6FE), cardShape)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // SECTION 1: HEADER & KEY METRIC ROW
            BodyProgressHeaderSection()

            // SECTION 2: 4-CARD GOAL GRID (Weight, Muscle Mass, Body Fat, BMI)
            BodyGoalCardsGrid()

            // SECTION 3: PROGRESS TREND (Metric Chips + Spline Chart with Floating December Badge)
            BodyProgressTrendSection(
                selectedMetricIndex = selectedMetricIndex,
                onSelectMetric = { selectedMetricIndex = it },
                selectedTimeIndex = selectedTimeIndex,
                onSelectTime = { selectedTimeIndex = it }
            )

            // SECTION 4: BOTTOM PODS (Workout Distribution + Activity Type)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Pod: Workout Distribution (6 Anatomical Figures)
                WorkoutDistributionPod(
                    modifier = Modifier.weight(1.22f)
                )

                // Right Pod: Activity Type (4 Donut Rings)
                ActivityTypePod(
                    modifier = Modifier.weight(0.98f)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 1: HEADER & KEY METRIC ROW
// -----------------------------------------------------------------------------

@Composable
private fun BodyProgressHeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: 3D Cute Scale Illustration
        Image(
            painter = painterResource(id = R.drawable.illustration_3d_body_scale),
            contentDescription = "Body 3D Scale",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(52.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Header: 🧘 Body Progress / Goal Based Tracking + Dropdown Pill
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
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF8B5CF6), Color(0xFFC084FC))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧘", fontSize = 13.sp)
                    }
                    Column {
                        Text(
                            text = "Body Progress",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "Goal Based Tracking",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            lineHeight = 13.sp
                        )
                    }
                }

                // Timeframe Dropdown Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF3E8FF))
                        .border(0.8.dp, Color(0xFFD8B4FE), RoundedCornerShape(50.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Last 6 Months",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7C3AED)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Key KPI Row: Current Weight + Delta vs Aug + Goal Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Weight + Delta
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column {
                        Text(
                            text = "69.5 kg",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF6D28D9),
                            lineHeight = 28.sp
                        )
                        Text(
                            text = "Current Weight",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(1.5.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 7.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "↓ 2.5 kg",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF15803D)
                            )
                        }
                        Text(
                            text = "vs Aug (-3.5%)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Goal Block
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(1.5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = "🎯", fontSize = 12.sp)
                        Text(
                            text = "Goal: ",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "66.0 kg",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D)
                        )
                    }

                    // Progress Bar + 67%
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.67f)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFF10B981))
                            )
                        }
                        Text(
                            text = "67%",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Text(
                        text = "↓ 3.5 kg to go",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 2: 4-CARD GOAL GRID (Weight, Muscle Mass, Body Fat, BMI)
// -----------------------------------------------------------------------------

@Composable
private fun BodyGoalCardsGrid(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Card 1: Weight
        BodySingleGoalCard(
            emojiIcon = "⚖️",
            iconBg = Color(0xFFEDE9FE),
            title = "Weight",
            value = "69.5 kg",
            delta = "↓ 2.5 kg (3.5%)",
            deltaColor = Color(0xFF16A34A),
            progress = 0.67f,
            progressBarColor = Color(0xFF10B981),
            statusText = "✓ On Track",
            modifier = Modifier.weight(1f)
        )

        // Card 2: Muscle Mass
        BodySingleGoalCard(
            emojiIcon = "💪",
            iconBg = Color(0xFFFFE4E6),
            title = "Muscle Mass",
            value = "57.2 kg",
            delta = "↑ 1.4 kg (2.5%)",
            deltaColor = Color(0xFF16A34A),
            progress = 0.75f,
            progressBarColor = Color(0xFF8B5CF6),
            statusText = "✓ Good",
            modifier = Modifier.weight(1f)
        )

        // Card 3: Body Fat
        BodySingleGoalCard(
            emojiIcon = "🔥",
            iconBg = Color(0xFFFFEDD5),
            title = "Body Fat",
            value = "17.6 %",
            delta = "↓ 1.2 %",
            deltaColor = Color(0xFF16A34A),
            progress = 0.60f,
            progressBarColor = Color(0xFFF97316),
            statusText = "✓ Target Met",
            modifier = Modifier.weight(1f)
        )

        // Card 4: BMI
        BodySingleGoalCard(
            emojiIcon = "📊",
            iconBg = Color(0xFFE0F2FE),
            title = "BMI",
            value = "22.8",
            delta = "Target: 22.0",
            deltaColor = Color(0xFF64748B),
            progress = 0.70f,
            progressBarColor = Color(0xFF0284C7),
            statusText = "✓ Normal",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BodySingleGoalCard(
    emojiIcon: String,
    iconBg: Color,
    title: String,
    value: String,
    delta: String,
    deltaColor: Color,
    progress: Float,
    progressBarColor: Color,
    statusText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFAFAFC))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 4.5.dp, vertical = 3.5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.5.dp)
        ) {
            // Icon + Title Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emojiIcon, fontSize = 10.sp)
                }
                Text(
                    text = title,
                    fontSize = if (title.contains("Muscle")) 8.2.sp else 9.5.sp,
                    letterSpacing = (-0.2).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    maxLines = 1,
                    softWrap = false
                )
            }

            // Value
            Text(
                text = value,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                maxLines = 1
            )

            // Delta
            Text(
                text = delta,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = deltaColor,
                maxLines = 1
            )

            // Capsule Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(50.dp))
                        .background(progressBarColor)
                )
            }

            // Status Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFDCFCE7))
                    .border(0.6.dp, Color(0xFF86EFAC), RoundedCornerShape(50.dp))
                    .padding(vertical = 1.5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusText,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF15803D),
                    maxLines = 1
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 3: PROGRESS TREND (Metric Chips + Spline Chart with Floating December Badge)
// -----------------------------------------------------------------------------

@Composable
private fun BodyProgressTrendSection(
    selectedMetricIndex: Int,
    onSelectMetric: (Int) -> Unit,
    selectedTimeIndex: Int,
    onSelectTime: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // Header Row: 📊 Progress Trend + Metric Chips + Time Range Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Icon + Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "📊", fontSize = 12.sp)
                Text(
                    text = "Progress Trend",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }

            // Middle: Metric chips (Weight, Muscle, Body Fat, BMI)
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Weight", "Muscle", "Body Fat", "BMI").forEachIndexed { i, label ->
                    val isSelected = selectedMetricIndex == i
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFF7C3AED) else Color.Transparent)
                            .border(
                                width = if (isSelected) 0.dp else 0.7.dp,
                                color = if (isSelected) Color.Transparent else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onSelectMetric(i) }
                            .padding(horizontal = 4.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 8.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF64748B)
                        )
                    }
                }
            }

            // Right: Time chips (1M, 3M, 6M, 12M)
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("1M", "3M", "6M", "12M").forEachIndexed { i, label ->
                    val isSelected = selectedTimeIndex == i
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFF7C3AED) else Color.Transparent)
                            .clickable { onSelectTime(i) }
                            .padding(horizontal = 3.5.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 8.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Chart Area: Y-Axis + Spline Canvas + Floating Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Y-Axis labels
                val yLabels = when (selectedMetricIndex) {
                    0 -> listOf("76", "72", "68", "64 (kg)")
                    1 -> listOf("60", "57", "54", "51 (kg)")
                    2 -> listOf("22", "18", "14", "10 (%)")
                    else -> listOf("26", "24", "22", "20")
                }

                Column(
                    modifier = Modifier.height(38.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    yLabels.forEach { label ->
                        Text(
                            text = label,
                            fontSize = 8.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Canvas for Spline & Dots
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                ) {
                    val count = 12
                    val step = size.width / (count - 1).toFloat()

                    // Normalized points (0f at bottom, 1f at top)
                    val points = when (selectedMetricIndex) {
                        0 -> listOf(0.917f, 0.850f, 0.792f, 0.750f, 0.717f, 0.683f, 0.817f, 0.667f, 0.617f, 0.650f, 0.542f, 0.458f)
                        1 -> listOf(0.40f, 0.42f, 0.46f, 0.48f, 0.50f, 0.52f, 0.51f, 0.55f, 0.58f, 0.60f, 0.62f, 0.65f)
                        2 -> listOf(0.78f, 0.75f, 0.70f, 0.65f, 0.60f, 0.55f, 0.58f, 0.52f, 0.48f, 0.46f, 0.44f, 0.40f)
                        else -> listOf(0.60f, 0.58f, 0.55f, 0.52f, 0.50f, 0.48f, 0.50f, 0.46f, 0.44f, 0.42f, 0.40f, 0.38f)
                    }

                    // Horizontal Grid Lines
                    listOf(0.0f, 0.33f, 0.66f, 1.0f).forEach { r ->
                        val y = size.height * (1f - r)
                        drawLine(
                            color = Color(0x14000000),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 0.8.dp.toPx()
                        )
                    }

                    // Coordinates
                    val coords = points.mapIndexed { i, p ->
                        Offset(i * step, size.height * (1f - p))
                    }

                    if (coords.isNotEmpty()) {
                        val path = Path()
                        val fillPath = Path()

                        path.moveTo(coords[0].x, coords[0].y)
                        fillPath.moveTo(coords[0].x, size.height)
                        fillPath.lineTo(coords[0].x, coords[0].y)

                        for (i in 0 until coords.size - 1) {
                            val p0 = coords[i]
                            val p1 = coords[i + 1]
                            val cx = (p0.x + p1.x) / 2f
                            path.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            fillPath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                        }

                        fillPath.lineTo(coords.last().x, size.height)
                        fillPath.close()

                        // Gradient Area Fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF8B5CF6).copy(alpha = 0.22f), Color.Transparent)
                            )
                        )

                        // Smooth Line
                        drawPath(
                            path = path,
                            color = Color(0xFF7C3AED),
                            style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        // Dots at points
                        coords.forEach { c ->
                            drawCircle(color = Color(0xFF7C3AED), radius = 2.8.dp.toPx(), center = c)
                            drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = c)
                        }
                    }
                }
            }

            // Floating Badge for December Point
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF7C3AED))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = when (selectedMetricIndex) {
                        0 -> "69.5 kg"
                        1 -> "57.2 kg"
                        2 -> "17.6 %"
                        else -> "22.8"
                    },
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        // X-Axis Month Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { m ->
                Text(
                    text = m,
                    fontSize = 8.5.sp,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 4: BOTTOM PODS (Workout Distribution + Activity Type)
// -----------------------------------------------------------------------------

@Composable
private fun WorkoutDistributionPod(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 5.dp, vertical = 4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Header Row: 🏋️ Workout Distribution + This Month ⌵
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "Workout Distribution",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.6.dp, Color(0xFFCBD5E1), RoundedCornerShape(50.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = "This Month",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            // 6 Anatomical Body Figures Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val distribution = listOf(
                    Triple("Chest", "12%", Color(0xFF2563EB)),
                    Triple("Back", "18%", Color(0xFF10B981)),
                    Triple("Shoulder", "14%", Color(0xFFF59E0B)),
                    Triple("Biceps", "16%", Color(0xFF8B5CF6)),
                    Triple("Triceps", "10%", Color(0xFFEF4444)),
                    Triple("Legs", "30%", Color(0xFF10B981))
                )

                distribution.forEach { (name, pct, color) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        AnatomicalMuscleSilhouette(
                            muscle = name,
                            highlightColor = color,
                            modifier = Modifier.size(width = 16.dp, height = 26.dp)
                        )
                        Text(
                            text = name,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155),
                            maxLines = 1
                        )
                        Text(
                            text = pct,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = color,
                            maxLines = 1
                        )
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnatomicalMuscleSilhouette(
    muscle: String,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val baseColor = Color(0xFFCBD5E1)

        if (muscle.equals("Legs", ignoreCase = true)) {
            // Pelvis
            drawRoundRect(
                color = baseColor,
                topLeft = Offset(w * 0.22f, h * 0.05f),
                size = Size(w * 0.56f, h * 0.22f),
                cornerRadius = CornerRadius(2.dp.toPx())
            )
            // Left Leg Quad & Calf
            val leftLegPath = Path().apply {
                moveTo(w * 0.25f, h * 0.26f)
                lineTo(w * 0.46f, h * 0.26f)
                lineTo(w * 0.44f, h * 0.95f)
                lineTo(w * 0.22f, h * 0.95f)
                close()
            }
            drawPath(path = leftLegPath, color = highlightColor)

            // Right Leg Quad & Calf
            val rightLegPath = Path().apply {
                moveTo(w * 0.54f, h * 0.26f)
                lineTo(w * 0.75f, h * 0.26f)
                lineTo(w * 0.78f, h * 0.95f)
                lineTo(w * 0.56f, h * 0.95f)
                close()
            }
            drawPath(path = rightLegPath, color = highlightColor)
            return@Canvas
        }

        // Head
        drawOval(
            color = baseColor,
            topLeft = Offset(w * 0.33f, 0f),
            size = Size(w * 0.34f, h * 0.22f)
        )

        // Torso Base
        val torsoPath = Path().apply {
            moveTo(w * 0.16f, h * 0.28f)
            lineTo(w * 0.84f, h * 0.28f)
            lineTo(w * 0.76f, h * 0.82f)
            lineTo(w * 0.24f, h * 0.82f)
            close()
        }
        drawPath(path = torsoPath, color = baseColor.copy(alpha = 0.5f))

        // Left Arm Base
        drawRoundRect(
            color = baseColor,
            topLeft = Offset(w * 0.05f, h * 0.32f),
            size = Size(w * 0.14f, h * 0.52f),
            cornerRadius = CornerRadius(2.dp.toPx())
        )
        // Right Arm Base
        drawRoundRect(
            color = baseColor,
            topLeft = Offset(w * 0.81f, h * 0.32f),
            size = Size(w * 0.14f, h * 0.52f),
            cornerRadius = CornerRadius(2.dp.toPx())
        )

        // Highlight Specific Muscle Groups
        when (muscle.lowercase()) {
            "chest" -> {
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.25f, h * 0.32f),
                    size = Size(w * 0.22f, h * 0.18f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.53f, h * 0.32f),
                    size = Size(w * 0.22f, h * 0.18f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
            "back" -> {
                val backPath = Path().apply {
                    moveTo(w * 0.24f, h * 0.30f)
                    lineTo(w * 0.76f, h * 0.30f)
                    lineTo(w * 0.68f, h * 0.72f)
                    lineTo(w * 0.32f, h * 0.72f)
                    close()
                }
                drawPath(path = backPath, color = highlightColor)
            }
            "shoulder" -> {
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.08f, h * 0.26f),
                    size = Size(w * 0.18f, h * 0.20f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.74f, h * 0.26f),
                    size = Size(w * 0.18f, h * 0.20f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
            "biceps" -> {
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.05f, h * 0.38f),
                    size = Size(w * 0.15f, h * 0.26f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.80f, h * 0.38f),
                    size = Size(w * 0.15f, h * 0.26f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
            "triceps" -> {
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.03f, h * 0.36f),
                    size = Size(w * 0.14f, h * 0.28f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                drawRoundRect(
                    color = highlightColor,
                    topLeft = Offset(w * 0.83f, h * 0.36f),
                    size = Size(w * 0.14f, h * 0.28f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
        }
    }
}

@Composable
private fun ActivityTypePod(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 5.dp, vertical = 4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Header Row: 🏃 Activity Type + >
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Activity Type",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(13.dp)
                )
            }

            // 4 Donut Rings Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val activities = listOf(
                    ActivityData("Gym", 65, "18 Days", Color(0xFF7C3AED), Icons.Default.FitnessCenter),
                    ActivityData("Cardio", 25, "7 Days", Color(0xFF10B981), Icons.Default.DirectionsRun),
                    ActivityData("Running", 5, "2 Days", Color(0xFF0284C7), Icons.Default.DirectionsWalk),
                    ActivityData("Mixed", 5, "2 Days", Color(0xFFF97316), Icons.Default.FitnessCenter)
                )

                activities.forEach { act ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        // Donut Ring with Icon
                        Box(
                            modifier = Modifier.size(26.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeWidth = 2.4.dp.toPx()
                                drawCircle(
                                    color = Color(0xFFF1F5F9),
                                    style = Stroke(width = strokeWidth)
                                )
                                val sweep = (act.percent / 100f) * 360f
                                drawArc(
                                    color = act.color,
                                    startAngle = -90f,
                                    sweepAngle = sweep.coerceAtLeast(18f),
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                            Icon(
                                imageVector = act.icon,
                                contentDescription = act.name,
                                tint = act.color,
                                modifier = Modifier.size(11.dp)
                            )
                        }

                        Text(
                            text = act.name,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            maxLines = 1
                        )
                        Text(
                            text = "${act.percent}%",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = act.color,
                            maxLines = 1
                        )
                        Text(
                            text = act.days,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

private data class ActivityData(
    val name: String,
    val percent: Int,
    val days: String,
    val color: Color,
    val icon: ImageVector
)

// =============================================================================
// 5. MASTER FOOTER BANNER ("Consistent Steps, Brighter Days")
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                    contentDescription = "Footer Dumbbell",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Consistent Steps, ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Brighter Days 彡",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15)
                    )
                }
            }

            // Tactile 40dp Gold Action Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .clickable { onKeepGoingClick() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "➔",
                    fontSize = 12.5.sp,
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
            .border(0.8.dp, Color(0xFFCBD5E1), RoundedCornerShape(50.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF475569),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
private fun LegendDotItem(color: Color, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontSize = 11.5.sp,
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
            Text(text = icon, fontSize = 14.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = valueColor,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun AttendanceStatusCircle(day: Int, status: Int) {
    val (bgColor, textColor, hasBorder) = when (status) {
        1 -> Triple(Color(0xFF10B981), Color.White, false) // Present (Solid Emerald)
        2 -> Triple(Color(0xFFF59E0B), Color.White, false) // Late (Solid Amber)
        0 -> Triple(Color(0xFFEF4444), Color.White, false) // Absent (Solid Red)
        3 -> Triple(Color(0xFF8B5CF6), Color.White, false) // Holiday (Solid Purple)
        4 -> Triple(Color(0xFFF1F5F9), Color(0xFF94A3B8), true) // Sunday (Soft Gray)
        else -> Triple(Color.Transparent, Color(0xFF94A3B8), true) // Upcoming (Faint Outline)
    }

    Box(
        modifier = Modifier
            .size(17.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (hasBorder) Modifier.border(0.9.dp, Color(0xFFCBD5E1), CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$day",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun AttendanceLegendItem(label: String, count: String, color: Color, isHollow: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(if (isHollow) Color.Transparent else color)
                .then(if (isHollow) Modifier.border(0.9.dp, color, CircleShape) else Modifier)
        )
        Text(
            text = "$label ($count)",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )
    }
}
