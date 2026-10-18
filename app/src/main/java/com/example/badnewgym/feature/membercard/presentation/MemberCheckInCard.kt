package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.AttendanceCalendarSection
import com.example.badnewgym.feature.membercard.presentation.components.BodyStatsSparklineSection
import com.example.badnewgym.feature.membercard.presentation.components.MemberHeaderBlock
import com.example.badnewgym.feature.membercard.presentation.components.PaymentPlanDetailedSection
import com.example.badnewgym.feature.membercard.presentation.components.WorkoutTrendDetailedSection

@Composable
fun MemberCheckInCard(
    data: MemberCardData,
    onMenuClick: () -> Unit = {},
    onBodyStatsClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onMonthClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .widthIn(max = 380.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color(0x3300C853),
                ambientColor = Color(0x2500E676)
            )
            .background(Color.White, RoundedCornerShape(28.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE2F4EC),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Identity & Attendance Ring
            MemberHeaderBlock(
                profile = data.profile,
                onMenuClick = onMenuClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Body Stats Section
            BodyStatsSparklineSection(
                stats = data.bodyStats,
                onDetailsClick = onBodyStatsClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Payment & Plan Section
            PaymentPlanDetailedSection(
                paymentPlan = data.paymentPlan,
                onPlanClick = onPaymentClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Attendance Section
            AttendanceCalendarSection(
                data = data.attendance
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Workout Trend Section
            WorkoutTrendDetailedSection(
                data = data.workoutTrend
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4FEFB)
@Composable
fun MemberCheckInCardPreview() {
    MemberCheckInCard(
        data = MemberCardData.sampleAmanTripathi(),
        modifier = Modifier.padding(16.dp)
    )
}
