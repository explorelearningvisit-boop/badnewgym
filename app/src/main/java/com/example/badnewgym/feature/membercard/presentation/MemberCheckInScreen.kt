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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.presentation.components.AquaGlassMemberView
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.CompactViewportMemberScreen
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet

enum class MemberIntelligenceTheme {
    LIGHT_LUXURY,
    COMPACT_VIEWPORT,
    AQUA_GLASS
}

/**
 * BAD GYM — Member Intelligence Comprehensive Screen
 * Ultra-Luxury Light Pearl Theme with Zero-Scroll Viewport Precision Architecture.
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

    // Active Navigation Tab
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
            .background(MemberCardTokens.LightPearlCanvasGradient)
            .statusBarsPadding()
    ) {
        // Zero-Scroll Viewport Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 2.dp)
                .padding(bottom = 58.dp), // padding for floating bottom bar
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 1. Top System & Brand Header
            TopBrandHeader(
                unreadNotifications = data.profile.unreadNotifications,
                onSearchClick = { showSearchSheet = true },
                onNotificationClick = { showNotificationsSheet = true }
            )

            // 2. Profile Hero Section with Greeting, 3 Metadata Chips & Circular Progress Gauge
            HeroProfileSection(
                profile = data.profile,
                onAvatarClick = {
                    showTailscaleSheet = true
                    onMenuClick()
                }
            )

            // 3. Five Quick Navigation Action Pills (Overview, Payment, Attendance, Workout, Progress)
            QuickActionPillsRow(
                selectedTab = selectedTab,
                onTabSelect = { tab ->
                    selectedTab = tab
                    when (tab) {
                        "Payment" -> showReceiptSheet = true
                        "Attendance" -> selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                        "Workout" -> showAnalyticsSheet = true
                        "Progress" -> showAnalyticsSheet = true
                    }
                }
            )

            // 4. Detailed Frosted White Cards (Payment & Plan, Attendance, Workout Trend, Body Stats)
            MemberCheckInCard(
                data = data,
                onReceiptClick = { showReceiptSheet = true },
                onAttendanceClick = {
                    selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                },
                onAttendanceDayClick = { day ->
                    selectedAttendanceDay = day
                },
                onAnalyticsClick = { showAnalyticsSheet = true }
            )
        }

        // 5. Floating Frosted White Glassmorphic Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
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
    onNotificationClick: () -> Unit,
    onToggleTheme: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Branding: BAD GYM + MEMBER INTELLIGENCE Subtitle
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "BAD ",
                    color = Color(0xFF059669),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "GYM",
                    color = Color(0xFF0F172A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = "MEMBER INTELLIGENCE",
                color = Color(0xFF0D9488),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        // Right Actions: Live Status Pill, Search & Notifications
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Live Status Pill (Luminous Light Emerald)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFECFDF5))
                    .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "LIVE",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF047857)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Search Button (Pure White Floating Circle)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color(0x100F172A))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Notifications Bell Button with Dot (Pure White Floating Circle)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color(0x100F172A))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .clickable(onClick = onNotificationClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF1E293B),
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
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 2: Hero Profile Section & Circular Gauge (With Greeting & 3 Chips)
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
            // Left Profile Block: Avatar + Greeting + Name + Demographics + Status Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Circular Avatar with Pinned Crown & Verified Check
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clickable(onClick = onAvatarClick)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_aman),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF10B981), CircleShape)
                    )

                    // Pinned Gold Crown at Top-Left
                    Box(
                        modifier = Modifier
                            .size(17.dp)
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
                            .border(1.5.dp, Color.White, CircleShape),
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
                    // "Good Morning 👋" greeting
                    Text(
                        text = "Good Morning 👋",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = profile.name,
                        color = Color(0xFF0F172A),
                        fontSize = 18.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                        color = Color(0xFF64748B),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Status Pills Row: Prime Plan & Active
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Amber "Prime Plan" Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFFBEB))
                                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Prime Plan",
                                color = Color(0xFF92400E),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Emerald "Active" Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Active",
                                color = Color(0xFF15803D),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Glowing Circular Progress Gauge (75% / 25 Days Left) - Light Theme
            Box(
                modifier = Modifier.size(62.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(62.dp)) {
                    val strokeWidth = 6.dp.toPx()
                    val arcSize = size.copy(width = size.width - strokeWidth, height = size.height - strokeWidth)
                    val offset = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    // Track Ring
                    drawArc(
                        color = Color(0xFFDCFCE7),
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
                            listOf(Color(0xFF10B981), Color(0xFF06B6D4), Color(0xFF3B82F6), Color(0xFF10B981))
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
                        color = Color(0xFF0F172A),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "25 Days Left",
                        color = Color(0xFF64748B),
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 3 Metadata Chips Below Avatar Row: Joined, Goal, Branch (Crisp Pure White Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            HeroDetailChip(
                icon = "📅",
                label = "Joined",
                value = profile.joinedDate,
                modifier = Modifier.weight(1f)
            )

            HeroDetailChip(
                icon = "🎯",
                label = "Goal",
                value = profile.goal,
                modifier = Modifier.weight(1f)
            )

            HeroDetailChip(
                icon = "📍",
                label = "Branch",
                value = profile.branch,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HeroDetailChip(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp), spotColor = Color(0x0C0F172A))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(text = label, color = Color(0xFF64748B), fontSize = 7.5.sp, fontWeight = FontWeight.Medium)
            Text(text = value, color = Color(0xFF0F172A), fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 3: Five Quick Navigation Action Pills (Overview, Payment, Attendance, Workout, Progress)
// -----------------------------------------------------------------------------------------
@Composable
private fun QuickActionPillsRow(
    selectedTab: String,
    onTabSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 1. Overview Pill (Active Green)
        QuickNavPill(
            title = "Overview",
            iconContent = { Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = it, modifier = Modifier.size(13.dp)) },
            iconTint = Color(0xFF10B981),
            isSelected = selectedTab == "Overview",
            onClick = { onTabSelect("Overview") }
        )

        // 2. Payment Pill
        QuickNavPill(
            title = "Payment",
            iconContent = {
                Canvas(modifier = Modifier.size(13.dp)) {
                    drawRoundRect(color = it, topLeft = Offset(0f, size.height * 0.15f), size = Size(size.width, size.height * 0.7f), cornerRadius = CornerRadius(2.dp.toPx()))
                    drawCircle(color = Color.White, radius = 1.6.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.5f))
                }
            },
            iconTint = Color(0xFFF43F5E),
            isSelected = selectedTab == "Payment",
            onClick = { onTabSelect("Payment") }
        )

        // 3. Attendance Pill
        QuickNavPill(
            title = "Attendance",
            iconContent = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = it, modifier = Modifier.size(13.dp)) },
            iconTint = Color(0xFF10B981),
            isSelected = selectedTab == "Attendance",
            onClick = { onTabSelect("Attendance") }
        )

        // 4. Workout Pill
        QuickNavPill(
            title = "Workout",
            iconContent = {
                Canvas(modifier = Modifier.size(13.dp)) {
                    val w = size.width
                    val h = size.height
                    drawRoundRect(color = it, topLeft = Offset(w * 0.1f, h * 0.5f), size = Size(w * 0.2f, h * 0.5f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = it, topLeft = Offset(w * 0.4f, h * 0.2f), size = Size(w * 0.2f, h * 0.8f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                    drawRoundRect(color = it, topLeft = Offset(w * 0.7f, h * 0.35f), size = Size(w * 0.2f, h * 0.65f), cornerRadius = CornerRadius(1.5.dp.toPx()))
                }
            },
            iconTint = Color(0xFF8B5CF6),
            isSelected = selectedTab == "Workout",
            onClick = { onTabSelect("Workout") }
        )

        // 5. Progress Pill
        QuickNavPill(
            title = "Progress",
            iconContent = {
                Canvas(modifier = Modifier.size(13.dp)) {
                    val w = size.width
                    val h = size.height
                    drawLine(color = it, start = Offset(w * 0.2f, h * 0.3f), end = Offset(w * 0.2f, h * 0.7f), strokeWidth = 1.8.dp.toPx())
                    drawLine(color = it, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.4.dp.toPx())
                    drawLine(color = it, start = Offset(w * 0.8f, h * 0.3f), end = Offset(w * 0.8f, h * 0.7f), strokeWidth = 1.8.dp.toPx())
                }
            },
            iconTint = Color(0xFFF59E0B),
            isSelected = selectedTab == "Progress",
            onClick = { onTabSelect("Progress") }
        )
    }
}

@Composable
private fun QuickNavPill(
    title: String,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconContent: @Composable (Color) -> Unit
) {
    val bgModifier = if (isSelected) {
        Modifier
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x3310B981))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF059669), Color(0xFF10B981))
                )
            )
    } else {
        Modifier
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x0C0F172A))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .then(bgModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContent(if (isSelected) Color.White else iconTint)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else Color(0xFF334155),
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

// -----------------------------------------------------------------------------------------
// COMPONENT 5: Floating Bottom Navigation Bar (Frosted Pure White Luxury Glass)
// -----------------------------------------------------------------------------------------
@Composable
private fun FloatingBottomBar(
    selectedTab: String,
    onTabSelect: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(26.dp), spotColor = Color(0x1F0F172A))
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xF8FFFFFF))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(26.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
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
    val activeColor = Color(0xFF059669)
    val inactiveColor = Color(0xFF64748B)
    val tint = if (isSelected) activeColor else inactiveColor

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFFECFDF5) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon(tint)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = tint,
                fontSize = 9.5.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
            )
        }
    }
}
