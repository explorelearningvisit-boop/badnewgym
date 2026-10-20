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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet

/**
 * BAD GYM — Member Intelligence Comprehensive Screen
 * Pixel-perfect implementation of the dark aurora member intelligence experience.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAmanTripathi() },
    initialSheet: String? = null,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Bottom Sheet States
    var showReceiptSheet by remember { mutableStateOf(false) }
    val receiptSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedAttendanceDay by remember { mutableStateOf<CalendarDay?>(null) }
    val attendanceSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showAnalyticsSheet by remember { mutableStateOf(false) }
    val analyticsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showNotificationsSheet by remember { mutableStateOf(false) }
    val notificationsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showSearchSheet by remember { mutableStateOf(false) }
    val searchSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showTailscaleSheet by remember { mutableStateOf(false) }
    val tailscaleSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Active Bottom Navigation Tab
    var selectedTab by remember { mutableStateOf("Overview") }

    androidx.compose.runtime.LaunchedEffect(initialSheet) {
        when (initialSheet?.lowercase()) {
            "receipt" -> showReceiptSheet = true
            "analytics" -> showAnalyticsSheet = true
            "attendance" -> selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
            "notifications" -> showNotificationsSheet = true
            "search" -> showSearchSheet = true
            "tailscale" -> showTailscaleSheet = true
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MemberCardTokens.AuroraCanvasGradient)
            .statusBarsPadding()
    ) {
        // Scrollable Screen Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .padding(bottom = 80.dp) // padding for floating bottom nav
        ) {
            // 1. Top System & Brand Header
            TopBrandHeader(
                unreadNotifications = data.profile.unreadNotifications,
                onSearchClick = { showSearchSheet = true },
                onNotificationClick = { showNotificationsSheet = true }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Profile Hero Section with Glowing Circular Progress Gauge
            HeroProfileSection(
                profile = data.profile,
                onAvatarClick = {
                    showTailscaleSheet = true
                    onMenuClick()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Four Quick Navigation Action Pills (Payment, Attendance, Workout, Progress)
            QuickActionPillsRow(
                onPaymentClick = { showReceiptSheet = true },
                onAttendanceClick = {
                    selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                },
                onWorkoutClick = { showAnalyticsSheet = true },
                onProgressClick = { showAnalyticsSheet = true }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Detailed Frosted White Cards (Payment & Plan, Attendance, Workout, Body Stats)
            MemberCheckInCard(
                data = data,
                onReceiptClick = { showReceiptSheet = true },
                onAttendanceClick = {
                    selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                },
                onAnalyticsClick = { showAnalyticsSheet = true }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // 5. Floating Dark Glassmorphic Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            FloatingBottomBar(
                selectedTab = selectedTab,
                onTabSelect = { tab ->
                    selectedTab = tab
                    when (tab) {
                        "Payments" -> showReceiptSheet = true
                        "Analytics" -> showAnalyticsSheet = true
                        "Profile" -> {
                            showTailscaleSheet = true
                            onMenuClick()
                        }
                    }
                }
            )
        }

        // --- Bottom Sheets ---
        if (showReceiptSheet) {
            ReceiptBottomSheet(
                sheetState = receiptSheetState,
                onDismissRequest = { showReceiptSheet = false }
            )
        }

        if (selectedAttendanceDay != null) {
            AttendanceDayBottomSheet(
                day = selectedAttendanceDay,
                sheetState = attendanceSheetState,
                onDismissRequest = { selectedAttendanceDay = null }
            )
        }

        if (showAnalyticsSheet) {
            DetailedAnalyticsBottomSheet(
                sheetState = analyticsSheetState,
                onDismissRequest = { showAnalyticsSheet = false }
            )
        }

        if (showNotificationsSheet) {
            NotificationsBottomSheet(
                sheetState = notificationsSheetState,
                onDismissRequest = { showNotificationsSheet = false }
            )
        }

        if (showSearchSheet) {
            SearchBottomSheet(
                sheetState = searchSheetState,
                onDismissRequest = { showSearchSheet = false }
            )
        }

        if (showTailscaleSheet) {
            TailscaleSettingsBottomSheet(
                sheetState = tailscaleSheetState,
                onDismissRequest = { showTailscaleSheet = false }
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 1: Top Brand Header
// -----------------------------------------------------------------------------------------
@Composable
private fun TopBrandHeader(
    unreadNotifications: Int,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Branding: BAD GYM + MEMBER INTELLIGENCE Subtitle
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "BAD ",
                    color = Color(0xFF00E676),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "GYM",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = "MEMBER INTELLIGENCE",
                color = Color(0xFF5EEAD4),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        // Right Actions: Search & Notifications
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Search Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x33134E3F))
                    .border(1.dp, Color(0x3334D399), CircleShape)
                    .clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Notifications Bell Button with Dot
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x33134E3F))
                    .border(1.dp, Color(0x3334D399), CircleShape)
                    .clickable(onClick = onNotificationClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )

                if (unreadNotifications > 0) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = (-2).dp, y = 2.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .border(1.dp, Color(0xFF06231B), CircleShape)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 2: Hero Profile Section & Circular Gauge
// -----------------------------------------------------------------------------------------
@Composable
private fun HeroProfileSection(
    profile: MemberProfile,
    onAvatarClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Profile Block: Avatar + Name + Demographics + Status Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Circular Avatar with Pinned Crown & Verified Check
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clickable(onClick = onAvatarClick)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_aman),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF00E676), CircleShape)
                    )

                    // Pinned Gold Crown at Top-Left
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-1).dp, y = (-1).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD97706))
                            .border(1.dp, Color(0xFFFDE68A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(11.dp)) {
                            val w = size.width
                            val h = size.height
                            val path = Path().apply {
                                moveTo(0f, h * 0.8f)
                                lineTo(0f, h * 0.25f)
                                lineTo(w * 0.3f, h * 0.5f)
                                lineTo(w * 0.5f, 0f)
                                lineTo(w * 0.7f, h * 0.5f)
                                lineTo(w, h * 0.25f)
                                lineTo(w, h * 0.8f)
                                close()
                            }
                            drawPath(path = path, color = Color(0xFFFEF3C7))
                        }
                    }

                    // Pinned Green Verified Checkmark at Bottom-Right
                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                            .border(1.5.dp, Color(0xFF041410), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Name & Metadata Column
                Column {
                    Text(
                        text = profile.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Status Pills Row: Prime & Active
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Amber "Prime" Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFB45309), Color(0xFFD97706))
                                    )
                                )
                                .border(1.dp, Color(0xFFFDE68A).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Prime",
                                color = Color(0xFFFFFBEB),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Emerald "Active" Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF059669))
                                .border(1.dp, Color(0xFF34D399).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Active",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right Glowing Circular Progress Gauge (75% / 25 Days Left)
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(76.dp)) {
                    val strokeWidth = 8.dp.toPx()
                    val arcSize = size.copy(width = size.width - strokeWidth, height = size.height - strokeWidth)
                    val offset = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    // Track Ring
                    drawArc(
                        color = Color(0xFF13382E),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = offset,
                        size = arcSize,
                        style = Stroke(width = strokeWidth)
                    )

                    // Gradient Progress Arc (75% = 270 degrees)
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(Color(0xFF00E5FF), Color(0xFF00E676), Color(0xFF10B981))
                        ),
                        startAngle = -90f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = offset,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "75%",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "25 Days Left",
                        color = Color(0xFF94A3B8),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Date Capsule: 01 - 30 Sep 2026
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33103D31))
                .border(1.dp, Color(0x3334D399), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF34D399),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "01 – 30 Sep 2026",
                color = Color(0xFFE2E8F0),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 3: Four Quick Navigation Action Pills
// -----------------------------------------------------------------------------------------
@Composable
private fun QuickActionPillsRow(
    onPaymentClick: () -> Unit,
    onAttendanceClick: () -> Unit,
    onWorkoutClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Payment Pill
        QuickNavPill(
            title = "Payment",
            iconBg = Color(0x33F43F5E),
            iconTint = Color(0xFFFB7185),
            onClick = onPaymentClick,
            modifier = Modifier.weight(1f),
            iconContent = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    drawRoundRect(
                        color = it,
                        topLeft = Offset(0f, size.height * 0.15f),
                        size = Size(size.width, size.height * 0.7f),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                    drawCircle(
                        color = Color(0xFF0F172A),
                        radius = 1.8.dp.toPx(),
                        center = Offset(size.width * 0.75f, size.height * 0.5f)
                    )
                }
            }
        )

        // 2. Attendance Pill
        QuickNavPill(
            title = "Attendance",
            iconBg = Color(0x3310B981),
            iconTint = Color(0xFF34D399),
            onClick = onAttendanceClick,
            modifier = Modifier.weight(1f),
            iconContent = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = it,
                    modifier = Modifier.size(15.dp)
                )
            }
        )

        // 3. Workout Pill
        QuickNavPill(
            title = "Workout",
            iconBg = Color(0x338B5CF6),
            iconTint = Color(0xFFA78BFA),
            onClick = onWorkoutClick,
            modifier = Modifier.weight(1f),
            iconContent = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    drawRoundRect(color = it, topLeft = Offset(w * 0.1f, h * 0.5f), size = Size(w * 0.2f, h * 0.5f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = it, topLeft = Offset(w * 0.4f, h * 0.2f), size = Size(w * 0.2f, h * 0.8f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = it, topLeft = Offset(w * 0.7f, h * 0.35f), size = Size(w * 0.2f, h * 0.65f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                }
            }
        )

        // 4. Progress Pill
        QuickNavPill(
            title = "Progress",
            iconBg = Color(0x33F59E0B),
            iconTint = Color(0xFFFBBF24),
            onClick = onProgressClick,
            modifier = Modifier.weight(1f),
            iconContent = {
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    drawLine(color = it, start = Offset(w * 0.2f, h * 0.3f), end = Offset(w * 0.2f, h * 0.7f), strokeWidth = 2.dp.toPx())
                    drawLine(color = it, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.5.dp.toPx())
                    drawLine(color = it, start = Offset(w * 0.8f, h * 0.3f), end = Offset(w * 0.8f, h * 0.7f), strokeWidth = 2.dp.toPx())
                }
            }
        )
    }
}

@Composable
private fun QuickNavPill(
    title: String,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: @Composable (Color) -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x38102F26))
            .border(1.dp, Color(0x3334D399), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                iconContent(iconTint)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = Color(0xFFE2E8F0),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 5: Floating Bottom Navigation Bar
// -----------------------------------------------------------------------------------------
@Composable
private fun FloatingBottomBar(
    selectedTab: String,
    onTabSelect: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(26.dp), spotColor = Color(0x33000000))
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xF0051613))
            .border(1.dp, Color(0x3334D399), RoundedCornerShape(26.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = { tint -> Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp)) },
                label = "Overview",
                isSelected = selectedTab == "Overview",
                onClick = { onTabSelect("Overview") }
            )

            BottomNavItem(
                icon = { tint ->
                    Canvas(modifier = Modifier.size(18.dp)) {
                        drawRoundRect(color = tint, topLeft = Offset(0f, size.height * 0.15f), size = Size(size.width, size.height * 0.7f), cornerRadius = CornerRadius(2.dp.toPx()))
                    }
                },
                label = "Payments",
                isSelected = selectedTab == "Payments",
                onClick = { onTabSelect("Payments") }
            )

            BottomNavItem(
                icon = { tint ->
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val w = size.width
                        val h = size.height
                        drawRoundRect(color = tint, topLeft = Offset(w * 0.1f, h * 0.45f), size = Size(w * 0.2f, h * 0.55f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                        drawRoundRect(color = tint, topLeft = Offset(w * 0.4f, h * 0.15f), size = Size(w * 0.2f, h * 0.85f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                        drawRoundRect(color = tint, topLeft = Offset(w * 0.7f, h * 0.35f), size = Size(w * 0.2f, h * 0.65f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    }
                },
                label = "Analytics",
                isSelected = selectedTab == "Analytics",
                onClick = { onTabSelect("Analytics") }
            )

            BottomNavItem(
                icon = { tint -> Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp)) },
                label = "Profile",
                isSelected = selectedTab == "Profile",
                onClick = { onTabSelect("Profile") }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: @Composable (Color) -> Unit,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xFF00E676)
    val inactiveColor = Color(0xFF94A3B8)
    val tint = if (isSelected) activeColor else inactiveColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        icon(tint)
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 9.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
