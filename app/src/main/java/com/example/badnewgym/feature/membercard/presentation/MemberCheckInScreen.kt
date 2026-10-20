package com.example.badnewgym.feature.membercard.presentation

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceDayBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.NotificationsBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.SearchBottomSheet
import com.example.badnewgym.feature.membercard.presentation.components.TailscaleSettingsBottomSheet

/**
 * BAD GYM — Member Check-In & Activity Screen
 * Fits the full individual person check-in and activity card in <= 600dp height.
 * NO vertical scrolling required. Everything is immediately actionable at a glance.
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
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF4FEFB),
                        Color(0xFFEBF7F2),
                        Color(0xFFF0FDF8)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Sleek Top Bar (Search, Notifications with badge, Reception info)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gym Location / Kiosk tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BAD GYM • ${data.profile.branch.uppercase()} KIOSK",
                        color = MemberCardTokens.SlateMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Quick Action Buttons (Search & Notifications)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Search Button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { showSearchSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MemberCardTokens.DarkNavy,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Notification Bell Button with Counter
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { showNotificationsSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MemberCardTokens.DarkNavy,
                            modifier = Modifier.size(17.dp)
                        )

                        if (data.profile.unreadNotifications > 0) {
                            Box(
                                modifier = Modifier
                                    .size(13.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 1.dp, y = (-1).dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = data.profile.unreadNotifications.toString(),
                                    color = Color.White,
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 2. The Member Intelligence Check-In & Activity Card (<= 600dp, NO SCROLL)
            MemberCheckInCard(
                data = data,
                onMenuClick = {
                    showTailscaleSheet = true
                    onMenuClick()
                },
                onReceiptClick = { showReceiptSheet = true },
                onAttendanceClick = {
                    selectedAttendanceDay = data.attendance.calendarDays.firstOrNull { it.dayOfMonth == 17 }
                },
                onAttendanceDayClick = { day ->
                    selectedAttendanceDay = day
                },
                onAnalyticsClick = { showAnalyticsSheet = true }
            )

            // 3. Subtle Reception Desk Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BAD GYM ACCESS SYSTEM  •  DESK #1 READY",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
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

@Preview(showBackground = true)
@Composable
fun MemberCheckInScreenPreview() {
    MemberCheckInScreen()
}

