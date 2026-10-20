package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.design.AquaGlassTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlinx.coroutines.launch

/**
 * Aqua Glass (Mint Frost) Complete View
 * Replicates the reference neumorphic glass layout with left vertical rail dock
 * and stacked cards for Hero, Payment, Attendance, Workout, and Body Progress.
 */
@Composable
fun AquaGlassMemberView(
    data: MemberCardData,
    onToggleTheme: () -> Unit,
    onReceiptClick: () -> Unit,
    onAttendanceDayClick: (CalendarDay) -> Unit,
    onAnalyticsClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRailItem by remember { mutableStateOf("Check-In") }
    val mainScrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AquaGlassTokens.CanvasBackgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // Top Header with Theme Switcher Pill
            AquaTopHeader(
                currentThemeTitle = "Aqua Glass",
                onToggleTheme = onToggleTheme,
                onMenuClick = onMenuClick,
                onNotificationClick = onNotificationClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Split View: Left Vertical Rail + Right Scrollable Card Feed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Left Vertical Navigation Rail
                AquaLeftNavRail(
                    selectedItem = selectedRailItem,
                    onSelectItem = { item ->
                        selectedRailItem = item
                        when (item.lowercase()) {
                            "payment" -> onReceiptClick()
                            "attendance" -> onAttendanceDayClick(data.attendance.calendarDays.first { it.dayOfMonth == 17 })
                            "workout", "body", "insights" -> onAnalyticsClick()
                            "check-in" -> {
                                scope.launch {
                                    mainScrollState.animateScrollTo(0)
                                }
                            }
                            else -> {}
                        }
                    }
                )

                // Main Scrollable Cards Feed
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(mainScrollState),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1. Hero Profile Card
                    AquaHeroCard(
                        profile = data.profile,
                        onAvatarClick = onMenuClick
                    )

                    // 2. Payment & Plan Card
                    AquaPaymentCard(
                        payment = data.paymentPlan,
                        onReceiptClick = onReceiptClick
                    )

                    // 3. Attendance Card
                    AquaAttendanceCard(
                        onDayClick = onAttendanceDayClick
                    )

                    // 4. Workout (Last 15 Days) Card
                    AquaWorkoutCard(
                        onAnalyticsClick = onAnalyticsClick
                    )

                    // 5. Body Progress (30D) Card
                    AquaBodyProgressCard(
                        onAnalyticsClick = onAnalyticsClick
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
