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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.design.AquaGlassTokens
import com.example.badnewgym.feature.membercard.design.MemberCardThemeManager
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DashboardTopBar
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.FloatingGlassBottomDock
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ThemePickerDialog
import com.example.badnewgym.feature.membercard.presentation.components.ThemedMemberIntelligenceCard

/**
 * BAD GYM — Master Member Intelligence Dashboard
 * Unified 1:1 pixel-perfect reproduction of reference design:
 * - Top App Bar: [ ☰ ], BAD GYM / Member Intelligence, [ 📅 02 Oct 2026 ], [ 🔔 ], [ ••• ]
 * - Hero Member Card: Avatar halo, crown, details, quote pill, 4 chips
 * - 2x2 Bento Intelligence Grid: Attendance, Workout Trend, Payment, Body Progress
 * - Detailed View Section: Tabs, dual overview, donut gauge, streak & metadata
 * - Floating Glass Bottom Dock: 5 items (Home, Attendance, Workout, Payment, More)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDashboardV2Screen(
    data: MemberCardData = remember { MemberCardData.sampleAshwiniSingh() },
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

    var showThemePicker by remember { mutableStateOf(false) }

    var activeBottomTab by remember { mutableStateOf("Home") }
    val scrollState = rememberScrollState()

    LaunchedEffect(initialSheet) {
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
            .background(AquaGlassTokens.CanvasBackgroundGradient)
    ) {
        // Main Vertical Scrollable Content - Full Width on Xiaomi Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            // Themed Member Intelligence Card (Hero + 2x2 Bento + Detailed View)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                ThemedMemberIntelligenceCard(
                    data = data,
                    onMenuClick = {
                        showTailscaleSheet = true
                        onMenuClick()
                    },
                    onCardClick = { card ->
                        when (card) {
                            "Payment" -> showReceiptSheet = true
                            "Attendance" -> selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                            else -> showAnalyticsSheet = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Modal Theme Picker Dialog (Invoked by [ ••• ] or More tab)
        if (showThemePicker) {
            ThemePickerDialog(
                currentTheme = MemberCardThemeManager.activeTheme,
                onThemeSelected = {
                    MemberCardThemeManager.activeTheme = it
                    showThemePicker = false
                },
                onDismiss = { showThemePicker = false }
            )
        }

        // Bottom Sheets
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
