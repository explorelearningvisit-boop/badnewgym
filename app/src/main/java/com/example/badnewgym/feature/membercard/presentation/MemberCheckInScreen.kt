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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceCalendarSection
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.BodyStatsSparklineSection
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsButton
import com.example.badnewgym.feature.membercard.presentation.components.HeroHeaderSection
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.PaymentPlanDetailedSection
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.WorkoutTrendDetailedSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAmanTripathi() },
    initialSheet: String? = null,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf("Overview") }

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
            .background(MemberCardTokens.ScreenCanvas)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Deep Sapphire Blue Hero Header
            HeroHeaderSection(
                profile = data.profile,
                selectedTab = selectedTab,
                onMenuClick = {
                    showTailscaleSheet = true
                    onMenuClick()
                },
                onSearchClick = { showSearchSheet = true },
                onNotificationClick = { showNotificationsSheet = true },
                onTabSelected = { tab ->
                    selectedTab = tab
                    coroutineScope.launch {
                        when (tab) {
                            "Overview" -> scrollState.animateScrollTo(0)
                            "Payment" -> scrollState.animateScrollTo(520)
                            "Attendance" -> scrollState.animateScrollTo(950)
                            "Workout" -> scrollState.animateScrollTo(1450)
                            "Progress" -> showAnalyticsSheet = true
                        }
                    }
                }
            )

            // 2. Main Content Surface Cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                // Body Stats with SVG Sparkline Waves
                BodyStatsSparklineSection(
                    stats = data.bodyStats
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payment & Plan Detailed Section
                PaymentPlanDetailedSection(
                    paymentPlan = data.paymentPlan,
                    onReceiptClick = { showReceiptSheet = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Attendance with Radial Gauge & 7-Column Calendar Month Grid
                AttendanceCalendarSection(
                    data = data.attendance,
                    onDayClick = { day ->
                        selectedAttendanceDay = day
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Workout Trend with 30-Day Multi-Color Intensity Bars
                WorkoutTrendDetailedSection(
                    data = data.workoutTrend
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Floating CTA Button
                DetailedAnalyticsButton(
                    onClick = { showAnalyticsSheet = true }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // --- Bottom Sheets ---

        // Receipt Sheet
        if (showReceiptSheet) {
            ReceiptBottomSheet(
                sheetState = receiptSheetState,
                onDismissRequest = { showReceiptSheet = false }
            )
        }

        // Attendance Day Detail Sheet
        if (selectedAttendanceDay != null) {
            AttendanceDayBottomSheet(
                day = selectedAttendanceDay,
                sheetState = attendanceSheetState,
                onDismissRequest = { selectedAttendanceDay = null }
            )
        }

        // Detailed Analytics Sheet
        if (showAnalyticsSheet) {
            DetailedAnalyticsBottomSheet(
                sheetState = analyticsSheetState,
                onDismissRequest = { showAnalyticsSheet = false }
            )
        }

        // Notifications Sheet
        if (showNotificationsSheet) {
            NotificationsBottomSheet(
                sheetState = notificationsSheetState,
                onDismissRequest = { showNotificationsSheet = false }
            )
        }

        // Search Sheet
        if (showSearchSheet) {
            SearchBottomSheet(
                sheetState = searchSheetState,
                onDismissRequest = { showSearchSheet = false }
            )
        }

        // Tailscale Settings Sheet
        if (showTailscaleSheet) {
            TailscaleSettingsBottomSheet(
                sheetState = tailscaleSheetState,
                onDismissRequest = { showTailscaleSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberCheckInScreenPreview() {
    MemberCheckInScreen()
}
