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
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberIntelligenceQaGallery
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
        VariantDebugBridge.onToggleQaGallery = { open ->
            viewModel.toggleQaGallery(open)
        }
        VariantDebugBridge.onLoadFixture = { fixtureId ->
            viewModel.loadFixtureById(fixtureId)
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
            VariantDebugBridge.onToggleQaGallery = null
            VariantDebugBridge.onLoadFixture = null
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    val success = state as? MemberIntelligenceUiState.Success
    val theme = success?.themeId ?: ThemeId.NATURAL_FRESH

    // System Back Handler
    BackHandler(enabled = success?.isQaGalleryOpen == true) {
        viewModel.closeQaGallery()
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

    if (success.isQaGalleryOpen) {
        BADGymTheme(colors = theme.colors(), shapes = theme.shapes(), motion = theme.motion(), elevation = theme.elevation()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                MemberIntelligenceQaGallery(
                    onSelectFixture = { viewModel.loadFixture(it) },
                    onClose = { viewModel.closeQaGallery() },
                    activeFixtureId = success.activeFixtureId
                )
            }
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
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(colors.accent.copy(alpha = 0.12f))
                                .border(1.dp, colors.accent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                                .clickable { viewModel.openQaGallery() }
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Text("QA LAB [69]", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(colors.successSoft)
                                .border(1.dp, colors.success.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Text(if (success.members.isEmpty()) "0 members" else "${success.members.size} live", color = colors.success, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if (success.activeFixtureId != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "DEMO FIXTURE: ${success.activeFixtureId}",
                            color = Color(0xFF92400E),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "OPEN QA LAB",
                            color = Color(0xFFB45309),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.clickable { viewModel.openQaGallery() }
                        )
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
                Spacer(Modifier.height(4.dp))
                BadGymBottomBar(modifier = Modifier.fillMaxWidth(), onItemClick = { })
            }
            success.selectedEventDetail?.let { ev -> EventDetailDialog(event = ev, onDismiss = viewModel::closeEventDetail) }
        }
    }
}
