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
import com.example.badnewgym.feature.memberintelligence.presentation.components.EventDetailDialog
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberPhoto
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
    BADGymTheme(colors = theme.colors(), shapes = theme.shapes(), motion = theme.motion(), elevation = theme.elevation()) {
        val colors = BADGymTheme.colors
        val selectedMember = success.snapshot
        val eventTitle = success.currentEvent?.eventType?.name?.replace('_', ' ')?.lowercase()?.replaceFirstChar { it.titlecase() } ?: "Member intelligence"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                BadGymTopBar(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(eventTitle.uppercase(), color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 0.35.sp)
                        Text("Member Intelligence • live decision workspace", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.successSoft)
                            .border(1.dp, colors.success.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(if (success.members.isEmpty()) "0 members" else "${success.members.size} live", color = colors.success, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
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
                        temporalRange = success.temporalRange,
                        onRangeChange = viewModel::updateTemporalRange,
                        onEventClick = viewModel::openEventDetail,
                        onMenuSelected = viewModel::selectMenu,
                        onCloseDetail = viewModel::closeMemberDetail,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (!success.isDetailExpanded) {
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(colors.surface.copy(alpha = 0.96f))
                                .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MemberPhoto(
                                photoUrl = selectedMember.identity.photoUrl,
                                tier = selectedMember.identity.tier,
                                width = 34.dp,
                                height = 34.dp,
                                showVerified = false,
                                memberName = selectedMember.identity.name
                            )
                            Spacer(Modifier.width(9.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(selectedMember.identity.name, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                                Text(
                                    listOfNotNull(selectedMember.membership?.planName, selectedMember.trainer?.trainerName)
                                        .joinToString(" • ").ifBlank { "Open member detail" },
                                    color = colors.textSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                            Text("OPEN", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                BadGymBottomBar(modifier = Modifier.fillMaxWidth(), onItemClick = { })
            }
            success.selectedEventDetail?.let { ev -> EventDetailDialog(event = ev, onDismiss = viewModel::closeEventDetail) }
        }
    }
}
