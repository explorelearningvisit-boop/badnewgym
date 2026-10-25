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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.AquaGlassTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlinx.coroutines.launch

/**
 * Aqua Glass (Mint Frost) Complete View
 * Pixel-perfect implementation matching reference media_1792695777880.png:
 * - Left vertical rail dock with top close button and 10 items
 * - Right scrollable feed with Hero, Payment, Attendance, Workout, Body Progress
 * - Subtle athletic gym header atmosphere fading into luminous mint frosted canvas
 */
@Composable
fun AquaGlassMemberView(
    data: MemberCardData,
    onToggleTheme: () -> Unit = {},
    onReceiptClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedRailItem by remember { mutableStateOf("Check-In") }
    val mainScrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AquaGlassTokens.CanvasBackgroundGradient)
    ) {
        // Top gym atmosphere background banner (FREEDOM header effect)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0F2B1D),
                            Color(0xFF14532D),
                            Color(0x8015803D),
                            Color.Transparent
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Text(
                text = "FREEDOM FITNESS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.25f),
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        // Main Screen Content: Status bars padded, split left rail + right scrollable cards
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(start = 6.dp, end = 8.dp, top = 2.dp, bottom = 4.dp),
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
                },
                onCloseClick = {
                    onToggleTheme()
                }
            )

            // Right Scrollable Cards Feed
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(mainScrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Hero Profile Card
                AquaHeroCard(
                    profile = data.profile,
                    onAvatarClick = onMenuClick,
                    onMenuClick = onMenuClick,
                    onDownloadClick = onReceiptClick
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

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
