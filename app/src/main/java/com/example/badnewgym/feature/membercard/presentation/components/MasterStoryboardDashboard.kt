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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
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
            // FOCUSED WIDGET: BLENDS COLOR WITH MENU BAR SEAMLESSLY (ZERO GAP)
            item(key = "widget_focused") {
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
                            isBlendedWithTab = true
                        )
                        1 -> MasterWorkoutTrendCard(
                            data = data,
                            onClick = onWorkoutClick,
                            isBlendedWithTab = true
                        )
                        2 -> MasterPaymentCard(
                            data = data,
                            onClick = onPaymentClick,
                            onViewReceiptClick = onViewReceiptClick,
                            isBlendedWithTab = true
                        )
                        3 -> MasterBodyProgressCard(
                            data = data,
                            onClick = onBodyProgressClick,
                            isBlendedWithTab = true
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
        2 -> Color(0xFFDC2626) // Coral Red
        else -> Color(0xFF7C3AED) // Amethyst Purple
    }

    val activeTabBg = when (selectedTabIndex) {
        0 -> Color(0xFFDCFCE7)
        1 -> Color(0xFFE0F2FE)
        2 -> Color(0xFFFFE4E6)
        else -> Color(0xFFF3E8FF)
    }

    val activeSurfaceBg = when (selectedTabIndex) {
        0 -> Color(0xFFEFFEF2)
        1 -> Color(0xFFE5F1FB)
        2 -> Color(0xFFFEEDED)
        else -> Color(0xFFE7D6F5)
    }

    val activeBorderColor = when (selectedTabIndex) {
        0 -> Color(0xFF86EFAC)
        1 -> Color(0xFF7DD3FC)
        2 -> Color(0xFFFDA4AF)
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
            DashboardTabItem(Icons.Default.AccountBalanceWallet, "Payment", "₹2.8k"),
            DashboardTabItem(Icons.Default.Person, "Body", "69.5kg")
        )
    }

    val menuShape = if (!isAllExpanded) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(16.dp)
    }

    // Top Header Dock: Seamlessly forms the top header of the active widget container below
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
                    2 -> Color(0xFFDC2626)
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
                        .padding(horizontal = 1.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (isSelected) tabItemColor else Color(0xFF64748B),
                        modifier = Modifier.size(13.5.dp)
                    )
                    Spacer(modifier = Modifier.width(1.5.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tab.label,
                            fontSize = 11.5.sp,
                            letterSpacing = (-0.3).sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) tabItemColor else Color(0xFF334155),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = tab.badge,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) tabItemColor else Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Compact View Mode Toggle Button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .border(1.dp, animatedBorderColor.copy(alpha = 0.7f), CircleShape)
                    .clickable { onToggleExpandedMode() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isAllExpanded) "📜" else "🎯",
                    fontSize = 15.sp
                )
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
            .clip(cardShape)
            .background(Color(0xFFEFFEF2))
            .border(1.2.dp, Color(0xFF86EFAC), cardShape)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    modifier = Modifier.size(74.dp)
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
                // Left: Low-Width Weekly Trend Card (only required space, ~168dp)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(0.9.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.width(168.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("📊", fontSize = 12.sp)
                            Text(
                                text = "Weekly Trend",
                                fontSize = 12.sp,
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
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (pct >= 85) Color(0xFF047857) else Color(0xFFD97706)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(18.dp)
                                            .height(36.dp)
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        val fillHeightFraction = (pct / 100f).coerceIn(0.15f, 1f)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height((36 * fillHeightFraction).dp)
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
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F172A)
                                    )

                                    Text(
                                        text = daysText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Monthly Target Achievement Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(0.9.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
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
                                Text("🎯", fontSize = 12.sp)
                                Text(
                                    text = "Target Status",
                                    fontSize = 12.sp,
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
                                Text("On Track", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "90%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF047857)
                            )
                            Text(
                                text = "Target ≥85% Met",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Text(
                            text = "3 of 4 weeks on track (↑5% vs Sep)",
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
                                text = "Active 6-day streak maintained",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            // Unified Attendance Summary Dock
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🔥", fontSize = 13.sp)
                            Text("Streak", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("6 Days", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Active", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🏆", fontSize = 13.sp)
                            Text("Best", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("12 Days", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Record", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Text("Rate", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("90%", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                        Text("Target ≥85%", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("📅", fontSize = 13.sp)
                            Text("Visits", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("27 Days", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
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
// 2. MASTER WORKOUT TREND CARD (Sky Blue #E5F1FB)
// =============================================================================

@Composable
fun MasterWorkoutTrendCard(
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
            .clip(cardShape)
            .background(Color(0xFFE5F1FB))
            .border(1.2.dp, Color(0xFF7DD3FC), cardShape)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: 3D Dumbbells Illustration + Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_workout_dumbbells),
                    contentDescription = "Workout 3D Dumbbells",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(74.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
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
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Workout Trend",
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

                    // Key Metric: 70 min + Avg/Session on left, Legend Column on right
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
                                    text = "70 min",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0284C7)
                                )
                                Text(
                                    text = "Avg / Session",
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
                                        text = "↑ 8%",
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
                            LegendDotItem(color = Color(0xFF0284C7), label = "Workout", value = "(22 d)")
                            LegendDotItem(color = Color(0xFF94A3B8), label = "Rest", value = "(8 d)")
                        }
                    }
                }
            }

            // Quick Info Metrics Strip (4 KPIs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFBAE6FD), RoundedCornerShape(10.dp))
                    .padding(horizontal = 7.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Total Duration", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "823 min", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 14% vs Sep", fontSize = 10.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Sessions", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "22", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "↑ 2 vs prev", fontSize = 10.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Avg Duration", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "74 min", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 8%", fontSize = 10.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Peak Duration", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "123 min", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                    Text(text = "↑ 12%", fontSize = 10.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
            }

            // 30-Day Workout Volume Bars Chart with Dashed Average Guideline
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(60.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "120m", fontSize = 10.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "60m", fontSize = 10.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "0m", fontSize = 10.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 30 Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    ) {
                        val workoutMins = listOf(
                            60, 20, 75, 20, 85, 90, 45, 60, 70, 80,
                            20, 65, 70, 20, 120, 80, 50, 60, 75, 85,
                            20, 40, 90, 70, 75, 80, 95, 70, 55, 85
                        )
                        val count = workoutMins.size
                        val barWidth = 3.8.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        // Draw dashed horizontal guideline for Average 70m
                        val avgY = size.height * (1f - (70f / 125f))
                        drawLine(
                            color = Color(0x600284C7),
                            start = Offset(0f, avgY),
                            end = Offset(size.width, avgY),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )

                        workoutMins.forEachIndexed { i, mins ->
                            val x = i * step + (step - barWidth) / 2f
                            val hRatio = (mins / 125f).coerceIn(0.12f, 0.96f)
                            val h = size.height * hRatio
                            val y = size.height - h

                            val barColor = when {
                                mins >= 90 -> Color(0xFF0284C7) // High
                                mins >= 30 -> Color(0xFF38BDF8) // Medium
                                else -> Color(0xFFCBD5E1)       // Low / Rest
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
                        Text(text = mark, fontSize = 10.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
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

            // Unified Workout Summary Dock
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("⏱️", fontSize = 13.sp)
                            Text("Total", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("22", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Sessions", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1.05f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🏆", fontSize = 13.sp)
                            Text("Best", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("120 m", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("2.0 hrs", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🔥", fontSize = 13.sp)
                            Text("Streak", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("8 Days", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Active", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("📊", fontSize = 13.sp)
                            Text("Avg", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("3.4 d", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Target Met", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }
                }
            }
        }
    }
}

// =============================================================================
// 3. MASTER PAYMENT CARD (Coral Pink #FEEDED)
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
            .clip(cardShape)
            .background(Color(0xFFFEEDED))
            .border(1.2.dp, Color(0xFFFDA4AF), cardShape)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: 3D Wallet Illustration + Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_payment_wallet),
                    contentDescription = "Payment 3D Wallet",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(74.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
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
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        // Plan History Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .border(0.8.dp, Color(0xFFFDA4AF), RoundedCornerShape(50.dp))
                                .clickable { onClick() }
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Plan History",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                            Text(
                                text = "›",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFE11D48)
                            )
                        }
                    }

                    // Key Metric: ₹2,800 + [ ! Due Now ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "₹2,800",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFEF4444))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "! Due Now",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Due on 16 Oct 2026",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                }
            }

            // PROMINENT FULL-WIDTH 48dp ACTION BUTTONS: WhatsApp, Collect Payment, Receipt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // WhatsApp Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16A34A))
                        .clickable { /* WhatsApp */ }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "💬", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "WhatsApp",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // Collect Payment CTA Button (Elevated Gradient)
                Row(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                            )
                        )
                        .clickable { onClick() }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "₹ Collect Payment",
                        fontSize = 14.5.sp,
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
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.2.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                        .clickable { onViewReceiptClick() }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Receipt",
                        fontSize = 14.sp,
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
                    .background(Color.White.copy(alpha = 0.90f))
                    .border(0.8.dp, Color(0xFFFDA4AF), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "👑 Gold Plan", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "• Monthly (Valid till 31 Oct 2026)", fontSize = 11.sp, color = Color(0xFF475569))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                        Text(text = "Auto Pay: Enabled", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFFF1F5F9)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Last Paid (Paid)", fontSize = 11.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "15 Sep 2026", fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Current Due (Due)", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                        Text(text = "16 Oct 2026", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Next Due (Scheduled)", fontSize = 11.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF334155))
                        Text(text = "16 Nov 2026", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    }
                }
            }

            // 12-Month Capsule Bar Chart
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(58.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(58.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "₹3k", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹2k", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹1k", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "0", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 12 Capsule Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        val months = listOf(
                            Pair(1, 0.75f), Pair(1, 0.85f), Pair(1, 0.80f), Pair(1, 0.80f),
                            Pair(1, 0.70f), Pair(2, 0.65f), Pair(1, 0.85f), Pair(1, 0.70f),
                            Pair(2, 0.75f), Pair(4, 0.88f), Pair(3, 0.50f), Pair(0, 0.30f)
                        )
                        val count = months.size
                        val barWidth = 8.5.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        months.forEachIndexed { i, pair ->
                            val x = i * step + (step - barWidth) / 2f
                            val h = size.height * pair.second
                            val y = size.height - h

                            // Background Track
                            drawRoundRect(
                                color = Color(0x15000000),
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, size.height),
                                cornerRadius = barRadius
                            )

                            val color = when (pair.first) {
                                1 -> Color(0xFF10B981) // Paid
                                2 -> Color(0xFF6366F1) // Plan Upgrade
                                3 -> Color(0xFFF59E0B) // Due Soon
                                4 -> Color(0xFFEF4444) // Due Now
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
                        Text(text = m, fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(color = Color(0xFF10B981), label = "Paid", value = "8m")
                LegendDotItem(color = Color(0xFF6366F1), label = "Upgrade", value = "2m")
                LegendDotItem(color = Color(0xFFEF4444), label = "Due Now", value = "1m")
                LegendDotItem(color = Color(0xFFF59E0B), label = "Due Soon", value = "1m")
            }

            // Payment Bottom Summary Container
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = BorderStroke(1.dp, Color(0xFFFDA4AF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("👛", fontSize = 13.sp)
                            Text("Total Paid", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("₹12,600", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Last 12 mo", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("📅", fontSize = 13.sp)
                            Text("Avg / Mo", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("₹1,050", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text("Regular", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1.15f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Text("🕒", fontSize = 13.sp)
                            Text("Next Due", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                        Text("16 Oct 2026", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                        Text("Due Now", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Text("On Time", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                        Text("91%", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                        Text("10/11 mo", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                    }
                }
            }
        }
    }
}

// =============================================================================
// 4. MASTER BODY PROGRESS CARD (Soft Lavender #E7D6F5)
// =============================================================================

@Composable
fun MasterBodyProgressCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedMetricIndex by remember { mutableIntStateOf(0) }

    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
    } else {
        RoundedCornerShape(20.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFFE7D6F5))
            .border(1.2.dp, Color(0xFFD8B4FE), cardShape)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: 3D Scale Illustration + Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_body_scale),
                    contentDescription = "Body 3D Scale",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(74.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                                    .background(Color(0xFF8B5CF6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🧘", fontSize = 13.sp)
                            }
                            Column {
                                Text(
                                    text = "Body Progress",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF475569)
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "69.5 kg",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF7C3AED)
                                )
                                Text(
                                    text = "Current Weight",
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
                                        text = "↓ 2.5 kg",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                                Text(
                                    text = "vs Aug (-3.5%)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        // Right: BMI & Goal Status
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 8.dp, vertical = 2.5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("BMI 22.8", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF15803D))
                            }
                            Text(
                                text = "Target: -3 kg",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C3AED)
                            )
                        }
                    }
                }
            }

            // Quick Info Metrics Strip (4 KPIs for Body Progress, matching Workout & Attendance style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(0.8.dp, Color(0xFFDDD6FE), RoundedCornerShape(10.dp))
                    .padding(horizontal = 7.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Weight", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "69.5 kg", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "↓ 2.5 kg", fontSize = 10.5.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Muscle", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "57.2 kg", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 1.4 kg", fontSize = 10.5.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Body Fat", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "17.6 %", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFF97316))
                    Text(text = "↓ 1.2 %", fontSize = 10.5.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "Waist", fontSize = 11.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(text = "81 cm", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B5CF6))
                    Text(text = "↓ 4 cm", fontSize = 10.5.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                }
            }

            // INTERACTIVE METRIC SELECTOR CHIPS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val metricChips = listOf(
                    Triple("Weight", "69.5kg", Color(0xFF10B981)),
                    Triple("Muscle", "57.2kg", Color(0xFF0284C7)),
                    Triple("Body Fat", "17.6%", Color(0xFFF97316)),
                    Triple("Waist", "81cm", Color(0xFF8B5CF6)),
                    Triple("All 4", "Splines", Color(0xFF7C3AED))
                )

                metricChips.forEachIndexed { i, (label, valStr, chipColor) ->
                    val isSelected = selectedMetricIndex == i
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) chipColor else Color.White.copy(alpha = 0.8f))
                            .border(0.8.dp, if (isSelected) chipColor else Color(0xFFDDD6FE), RoundedCornerShape(8.dp))
                            .clickable { selectedMetricIndex = i }
                            .padding(vertical = 2.dp, horizontal = 2.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = valStr,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSelected) Color.White.copy(alpha = 0.95f) else chipColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // 12-Month Multi-Line Spline Chart with Calibrated Real-World Scaling
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(68.dp)) {
                    // Left Y-Axis (Scales to active metric)
                    val yLabels = when (selectedMetricIndex) {
                        0 -> listOf("76kg", "72kg", "68kg", "64kg")
                        1 -> listOf("60kg", "57kg", "54kg", "51kg")
                        2 -> listOf("22%", "18%", "14%", "10%")
                        3 -> listOf("86cm", "82cm", "78cm", "74cm")
                        else -> listOf("100%", "75%", "50%", "25%")
                    }

                    Column(
                        modifier = Modifier.height(68.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        yLabels.forEach { label ->
                            Text(text = label, fontSize = 9.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Spline Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                    ) {
                        val count = 12
                        val step = size.width / (count - 1).toFloat()

                        val weightPts = listOf(0.75f, 0.78f, 0.74f, 0.73f, 0.76f, 0.74f, 0.72f, 0.73f, 0.72f, 0.70f, 0.70f, 0.72f)
                        val waistPts = listOf(0.68f, 0.69f, 0.70f, 0.68f, 0.67f, 0.66f, 0.65f, 0.64f, 0.65f, 0.64f, 0.63f, 0.64f)
                        val musclePts = listOf(0.56f, 0.55f, 0.58f, 0.59f, 0.57f, 0.56f, 0.58f, 0.57f, 0.56f, 0.58f, 0.57f, 0.58f)
                        val bodyFatPts = listOf(0.35f, 0.38f, 0.36f, 0.34f, 0.32f, 0.30f, 0.28f, 0.30f, 0.27f, 0.25f, 0.25f, 0.26f)

                        fun drawSplineCurve(points: List<Float>, color: Color, fillArea: Boolean) {
                            val path = Path()
                            val fillPath = Path()
                            val coords = points.mapIndexed { i, p ->
                                Offset(i * step, size.height * (1f - p))
                            }
                            if (coords.isNotEmpty()) {
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

                                if (fillArea) {
                                    drawPath(
                                        path = fillPath,
                                        brush = Brush.verticalGradient(
                                            listOf(color.copy(alpha = 0.28f), Color.Transparent)
                                        )
                                    )
                                }

                                drawPath(
                                    path = path,
                                    color = color,
                                    style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )

                                coords.forEach { c ->
                                    drawCircle(color = color, radius = 2.5.dp.toPx(), center = c)
                                    drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = c)
                                }
                            }
                        }

                        // Reference Horizontal Gridlines
                        listOf(0.33f, 0.66f, 1.0f).forEach { r ->
                            val y = size.height * (1f - r)
                            drawLine(
                                color = Color(0x18000000),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 0.8.dp.toPx()
                            )
                        }

                        when (selectedMetricIndex) {
                            0 -> drawSplineCurve(weightPts, Color(0xFF10B981), fillArea = true)
                            1 -> drawSplineCurve(musclePts, Color(0xFF0284C7), fillArea = true)
                            2 -> drawSplineCurve(bodyFatPts, Color(0xFFF97316), fillArea = true)
                            3 -> drawSplineCurve(waistPts, Color(0xFF8B5CF6), fillArea = true)
                            else -> {
                                drawSplineCurve(weightPts, Color(0xFF10B981), fillArea = false)
                                drawSplineCurve(waistPts, Color(0xFF8B5CF6), fillArea = false)
                                drawSplineCurve(musclePts, Color(0xFF0284C7), fillArea = false)
                                drawSplineCurve(bodyFatPts, Color(0xFFF97316), fillArea = false)
                            }
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
                        Text(text = m, fontSize = 9.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 1. TOP PRIORITY: Bench Press Top Lift & Muscle Focus Distribution Strip
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
                Column {
                    Text(text = "🏋️ Top Lift: Bench Press", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "65 ➔ 67.5 kg (+4% PR)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "🎯 Current Focus:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = "Legs 43%", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                    }
                    Text(
                        text = "Arms 18% • Delts 12% • Back 11% • Chest 7%",
                        fontSize = 9.5.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // 3. BOTTOM PRIORITY: Conditioning, Overall Status & Goal Strip
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "📊 Strength", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = "↑ 18%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "Est. 1RM", fontSize = 10.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.SemiBold)
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1.05f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "❤️ Endurance", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = "↑ 22%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "Cardio Time", fontSize = 10.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.SemiBold)
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1.05f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "📈 Progress", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF10B981))
                                .padding(horizontal = 7.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "On Track",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

                    Column(
                        modifier = Modifier.weight(1.1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "🎯 Goal", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = "Lean Muscle", fontSize = 14.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                        Text(text = "3 months", fontSize = 10.5.sp, color = Color(0xFF475569), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

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
            .size(22.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (hasBorder) Modifier.border(1.2.dp, Color(0xFFCBD5E1), CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$day",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
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
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isHollow) Color.Transparent else color)
                .then(if (isHollow) Modifier.border(1.dp, color, CircleShape) else Modifier)
        )
        Text(
            text = "$label ($count)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )
    }
}
