package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.presentation.components.BadGymBottomBar
import com.example.badnewgym.feature.memberintelligence.presentation.components.BadGymTopBar
import com.example.badnewgym.feature.memberintelligence.presentation.components.CompactMemberCarousel
import com.example.badnewgym.feature.memberintelligence.presentation.components.PixelPerfectMemberCard

@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG204", "gym1")
    }

    DisposableEffect(viewModel) {
        VariantDebugBridge.onCommand = { variant, menu ->
            if (variant != null) viewModel.selectTheme(variant)
            if (menu != null) {
                viewModel.openMemberDetail()
                viewModel.selectMenu(menu)
            }
        }
        VariantDebugBridge.onMemberIndex = { index ->
            viewModel.closeMemberDetail()
            viewModel.selectMember(index)
        }
        VariantDebugBridge.onCloseDetail = {
            viewModel.closeMemberDetail()
        }
        VariantDebugBridge.onOpenDetail = { index ->
            viewModel.openMemberDetail(index)
        }
        val receiver = VariantDebugReceiver()
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter("com.example.badnewgym.DEBUG_VARIANT"),
            ContextCompat.RECEIVER_EXPORTED
        )
        onDispose {
            VariantDebugBridge.onCommand = null
            VariantDebugBridge.onMemberIndex = null
            VariantDebugBridge.onCloseDetail = null
            VariantDebugBridge.onOpenDetail = null
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    val success = state as? MemberIntelligenceUiState.Success
    val theme = success?.themeId ?: ThemeId.NATURAL_FRESH

    // System Back Handler
    BackHandler(enabled = success?.isDetailExpanded == true) {
        viewModel.closeMemberDetail()
    }

    if (success == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF16A34A))
        }
        return
    }

    // Root Surface is always the browse surface with carousel context;
    // Bounded detail state lives inside the compact card bounds.
    BrowseMemberIntelligenceSurface(
        success = success,
        theme = theme,
        viewModel = viewModel
    )
}

@Composable
private fun BrowseMemberIntelligenceSurface(
    success: MemberIntelligenceUiState.Success,
    theme: ThemeId,
    viewModel: MemberIntelligenceViewModel
) {
    // The outer dashboard maintains a stable, executive dark foundation (#0C1017)
    // while individual member cards within the carousel apply their own local theme skins.
    val dashboardTheme = ThemeId.MINIMAL_DARK
    val dashboardBackground = Color(0xFF0C1017)

    BADGymTheme(
        colors = dashboardTheme.colors(),
        shapes = dashboardTheme.shapes(),
        motion = dashboardTheme.motion(),
        elevation = dashboardTheme.elevation()
    ) {
        val colors = BADGymTheme.colors

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(dashboardBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // 1. BAD GYM Top App Bar
            BadGymTopBar(modifier = Modifier.fillMaxWidth())

            // 2. Section Header: Title + Active Members Count
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEMBER INTELLIGENCE",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF16A34A).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF16A34A).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${success.members.size} Present",
                            color = Color(0xFF4ADE80),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = if (success.isDetailExpanded)
                        "Bounded Detail Mode • Select menu from side rail • Back to browse"
                    else
                        "Live Member Flow • Swipe cards to inspect • Tap card for details",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(4.dp))

            // 3. Compact Member Intelligence Carousel (snapping horizontal row with side peek & bounded detail)
            CompactMemberCarousel(
                members = success.members,
                selectedIndex = success.selectedMemberIndex,
                onMemberSelected = viewModel::selectMember,
                onMemberClick = { viewModel.openMemberDetail(it) },
                onCta = viewModel::executeCta,
                activeTheme = theme,
                isDetailExpanded = success.isDetailExpanded,
                activeMenu = success.activeMenu,
                menus = success.menus,
                onMenuSelected = viewModel::selectMenu,
                onCloseDetail = viewModel::closeMemberDetail,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // 4. Focused Member Quick Summary / Action Banner
            val selectedMember = success.snapshot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF161F2E))
                    .border(1.dp, Color(0xFF334155).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .clickable {
                        if (success.isDetailExpanded) {
                            viewModel.closeMemberDetail()
                        } else {
                            viewModel.openMemberDetail()
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        com.example.badnewgym.feature.memberintelligence.presentation.components.MemberPhoto(
                            photoUrl = selectedMember.identity.photoUrl,
                            tier = selectedMember.identity.tier,
                            width = 34.dp,
                            height = 34.dp,
                            showVerified = false,
                            memberName = selectedMember.identity.name
                        )

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = selectedMember.identity.name,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "• ${selectedMember.membership?.planName ?: "Plan"}",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = if (success.isDetailExpanded)
                                    "Tap to collapse detail • Back returns to carousel"
                                else
                                    "Trainer: ${selectedMember.trainer?.trainerName ?: "Unassigned"} • ${selectedMember.workout?.currentRoutine ?: "Routine"}",
                                color = Color(0xFF64748B),
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (success.isDetailExpanded) "Collapse" else "Inspect Detail",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (success.isDetailExpanded) Icons.Rounded.Close else Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = if (success.isDetailExpanded) "Collapse profile" else "Open profile",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // 5. Standard BAD GYM Bottom Navigation Bar
            BadGymBottomBar(
                modifier = Modifier.fillMaxWidth(),
                onItemClick = { }
            )
        }
    }
}