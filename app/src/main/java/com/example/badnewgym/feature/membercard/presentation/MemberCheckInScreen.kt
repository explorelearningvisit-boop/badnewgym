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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceCalendarSection
import com.example.badnewgym.feature.membercard.presentation.components.BodyStatsSparklineSection
import com.example.badnewgym.feature.membercard.presentation.components.DetailedAnalyticsButton
import com.example.badnewgym.feature.membercard.presentation.components.HeroHeaderSection
import com.example.badnewgym.feature.membercard.presentation.components.PaymentPlanDetailedSection
import com.example.badnewgym.feature.membercard.presentation.components.WorkoutTrendDetailedSection

@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAmanTripathi() },
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onViewAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick,
                onNotificationClick = onNotificationClick
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
                    paymentPlan = data.paymentPlan
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Attendance with Radial Gauge & 7-Column Calendar Month Grid
                AttendanceCalendarSection(
                    data = data.attendance
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Workout Trend with 30-Day Multi-Color Intensity Bars
                WorkoutTrendDetailedSection(
                    data = data.workoutTrend
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Floating CTA Button
                DetailedAnalyticsButton(
                    onClick = onViewAnalyticsClick
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberCheckInScreenPreview() {
    MemberCheckInScreen()
}
