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
import com.example.badnewgym.feature.membercard.presentation.components.AquaGlassMemberView
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet

/**
 * BAD GYM — MEMBER INTELLIGENCE SCREEN
 * Canonical implementation matching the reference liquid glass dashboard:
 * - Fluid mint-aurora canvas
 * - Centered member hero with neon cyan/mint halo ring and 3D crown badge
 * - 2x2 Bento Intelligence cards (Attendance, Payment, Workout, Body Progress)
 * - Detailed Overview Card with dual bar/spline chart, 4 metric pills & 3 fitness targets
 * - Floating glass bottom navigation dock
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
    var isV2Dashboard by remember { mutableStateOf(true) }
    var currentMemberData by remember { mutableStateOf(data) }

    if (isV2Dashboard) {
        MemberDashboardV2Screen(
            data = currentMemberData,
            initialSheet = initialSheet,
            onMenuClick = onMenuClick,
            modifier = modifier
        )
    } else {
        // Fallback Compact Card View
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

        val screenBg = Brush.verticalGradient(
            listOf(
                Color(0xFFE0F7EF),
                Color(0xFFE8F4FD),
                Color(0xFFF3EDFD),
                Color(0xFFF0FDF4),
                Color(0xFFF8FAFC)
            )
        )

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
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .navigationBarsPadding()
            ) {
                MemberCheckInCard(
                    data = currentMemberData,
                    onAvatarClick = {
                        showTailscaleSheet = true
                        onMenuClick()
                    },
                    onSearchClick = { showSearchSheet = true },
                    onNotificationsClick = { showNotificationsSheet = true },
                    onReceiptClick = { showReceiptSheet = true },
                    onAttendanceClick = {
                        selectedAttendanceDay = currentMemberData.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                    },
                    onAttendanceDayClick = { day ->
                        selectedAttendanceDay = day
                    },
                    onAnalyticsClick = { showAnalyticsSheet = true },
                    onTierSwitch = {}
                )

                if (customWidgetsContent != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    customWidgetsContent()
                }
            }

            if (showReceiptSheet) {
                ReceiptBottomSheet(
                    sheetState = receiptSheetState,
                    onDismissRequest = { showReceiptSheet = false }
                )
            }

            selectedAttendanceDay?.let { day ->
                AttendanceDayBottomSheet(
                    day = day,
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
}
