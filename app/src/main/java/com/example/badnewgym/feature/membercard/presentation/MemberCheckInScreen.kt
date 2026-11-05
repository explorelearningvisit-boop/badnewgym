package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * BAD GYM — MEMBER INTELLIGENCE SCREEN
 * Single entry point: renders the V2 Member Intelligence dashboard
 * (theme strip + identity header + KPI tabs + Attendance/Workout/Payment/Body
 * intelligence screens) backed by the signed-in member pass.
 */
@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAshwiniSingh() },
    initialSheet: String? = null,
    onMenuClick: () -> Unit = {},
    customWidgetsContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    MemberDashboardV2Screen(
        data = data,
        initialSheet = initialSheet,
        onMenuClick = onMenuClick,
        modifier = modifier
    )
}
