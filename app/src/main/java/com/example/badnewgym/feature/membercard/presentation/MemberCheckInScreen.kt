package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet

/**
 * BAD GYM — COMPACT MEMBER INTELLIGENCE SCREEN
 * Faithful implementation of the approved reference UI/UX:
 * - Packed vertically to consume minimal viewport height (~500-550dp)
 * - Leaves remaining space for additional custom widgets
 * - High readability with bold numbers & metrics
 * - Light mint and white canvas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAshwiniSingh() },
    initialSheet: String? = null,
    onMenuClick: () -> Unit = {},
    customWidgetsContent: (@Composable () -> Unit)? = null,
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

    // Active Member Data (defaults to Aman Tripathi 6-Month Gold Tier from user mockup)
    var currentMemberData by remember { mutableStateOf(MemberCardData.sampleAmanTripathi()) }

    LaunchedEffect(initialSheet) {
        when (initialSheet?.lowercase()) {
            "receipt" -> showReceiptSheet = true
            "analytics" -> showAnalyticsSheet = true
            "attendance" -> selectedAttendanceDay = currentMemberData.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
            "notifications" -> showNotificationsSheet = true
            "search" -> showSearchSheet = true
            "tailscale" -> showTailscaleSheet = true
            else -> {}
        }
    }

    val isDark = currentMemberData.profile.tier == com.example.badnewgym.feature.membercard.domain.model.MemberTier.GOLD_6_MONTH ||
            currentMemberData.profile.tier == com.example.badnewgym.feature.membercard.domain.model.MemberTier.SPARK_DIAMOND

    val screenBg = if (isDark) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF060E18),
                Color(0xFF0A1828),
                Color(0xFF07101C)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFE6F7F0),
                Color(0xFFF3FBF8),
                Color(0xFFEDF8F4)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .navigationBarsPadding()
        ) {
            // Main Member Intelligence Compact Card Architecture
            MemberCheckInCard(
                data = currentMemberData,
                onAvatarClick = {
                    showTailscaleSheet = true
                    onMenuClick()
                },
                onNotificationsClick = { showNotificationsSheet = true },
                onReceiptClick = { showReceiptSheet = true },
                onAttendanceClick = {
                    selectedAttendanceDay = currentMemberData.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                },
                onAttendanceDayClick = { day ->
                    selectedAttendanceDay = day
                },
                onAnalyticsClick = { showAnalyticsSheet = true },
                onTierSwitch = { newTier ->
                    currentMemberData = when (newTier) {
                        com.example.badnewgym.feature.membercard.domain.model.MemberTier.GOLD_6_MONTH ->
                            MemberCardData.sampleAmanTripathi().copy(
                                profile = MemberCardData.sampleAmanTripathi().profile.copy(
                                    tier = com.example.badnewgym.feature.membercard.domain.model.MemberTier.GOLD_6_MONTH,
                                    tierName = "Gold Plan (6 Months)"
                                )
                            )
                        com.example.badnewgym.feature.membercard.domain.model.MemberTier.BASIC_MEMBER ->
                            MemberCardData.sampleAmanTripathi().copy(
                                profile = MemberCardData.sampleAmanTripathi().profile.copy(
                                    tier = com.example.badnewgym.feature.membercard.domain.model.MemberTier.BASIC_MEMBER,
                                    tierName = "Basic Member"
                                )
                            )
                        com.example.badnewgym.feature.membercard.domain.model.MemberTier.SPARK_DIAMOND ->
                            MemberCardData.sampleAshwiniSingh().copy(
                                profile = MemberCardData.sampleAshwiniSingh().profile.copy(
                                    tier = com.example.badnewgym.feature.membercard.domain.model.MemberTier.SPARK_DIAMOND,
                                    tierName = "Spark Member"
                                )
                            )
                    }
                }
            )

            // Slot for custom widgets in the remaining vertical space
            if (customWidgetsContent != null) {
                Spacer(modifier = Modifier.height(6.dp))
                customWidgetsContent()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // --- Bottom Sheet Dialogs ---
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
