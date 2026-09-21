package com.example.badnewgym.feature.memberintelligence.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemberIntelligenceScreen(
    viewModel: MemberIntelligenceViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allThemes = ThemeId.entries
    val currentTheme = (state as? MemberIntelligenceUiState.Success)?.themeId ?: ThemeId.NATURAL_FRESH
    val pagerState = rememberPagerState(
        initialPage = allThemes.indexOf(currentTheme).coerceAtLeast(0),
        pageCount = { allThemes.size }
    )

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("1", "gym1")
    }

    LaunchedEffect(pagerState.currentPage) {
        val targetTheme = allThemes[pagerState.currentPage]
        if (targetTheme != currentTheme) {
            viewModel.selectTheme(targetTheme)
        }
    }

    LaunchedEffect(currentTheme) {
        val targetIndex = allThemes.indexOf(currentTheme)
        if (targetIndex >= 0 && targetIndex != pagerState.currentPage) {
            pagerState.scrollToPage(targetIndex)
        }
    }

    DisposableEffect(viewModel) {
        VariantDebugBridge.onCommand = { variant, menu ->
            if (variant != null) viewModel.selectTheme(variant)
            if (menu != null) viewModel.selectMenu(menu)
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
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    val adaptiveContext = (state as? MemberIntelligenceUiState.Success)?.let { s ->
        com.example.badnewgym.feature.memberintelligence.design.colors.AdaptiveThemeEngine.resolve(
            themeId = s.themeId,
            snapshot = s.snapshot,
            currentEventType = s.currentEvent.eventType,
            highestPrioritySignal = s.signals.firstOrNull()
        )
    }

    val outerBackground = when (currentTheme) {
        ThemeId.NATURAL_FRESH -> Color(0xFFF2FBF5)
        ThemeId.FUTURISTIC_NEON -> Color(0xFF020617)
        ThemeId.MINIMAL_DARK -> Color(0xFF0B0D11)
        ThemeId.GLASSMORPHISM -> Color(0xFFEFF6FF)
        ThemeId.PREMIUM_3D -> Color(0xFF070604)
        ThemeId.VIBRANT_GRADIENT -> Color(0xFFFFF1F2)
        ThemeId.BEAST_MODE -> Color(0xFF0B0102)
        ThemeId.PURPLE_ROYAL -> Color(0xFF090312)
    }

    BADGymTheme(
        colors = currentTheme.colors(),
        shapes = currentTheme.shapes(),
        motion = currentTheme.motion(),
        elevation = currentTheme.elevation(),
        adaptiveContext = adaptiveContext
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(outerBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // Theme Switcher Chips
            ThemePicker(
                selected = currentTheme,
                onSelected = { theme ->
                    val index = allThemes.indexOf(theme)
                    if (index >= 0) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    viewModel.selectTheme(theme)
                }
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Category & Theme Name Sub-banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentTheme.title.uppercase(),
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = currentTheme.category,
                    color = BADGymTheme.colors.textSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Card Swiper
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val pageTheme = allThemes[page]
                val (scenarioSnapshot, scenarioEvent) = com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios.getScenarioForTheme(pageTheme)
                val engineResult = com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine().evaluate(
                    scenarioSnapshot,
                    scenarioEvent,
                    scenarioEvent.occurredAt
                )
                val resolvedMenus = com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver.resolve(
                    scenarioSnapshot,
                    engineResult.signals
                )

                BADGymTheme(
                    colors = pageTheme.colors(),
                    shapes = pageTheme.shapes(),
                    motion = pageTheme.motion(),
                    elevation = pageTheme.elevation(),
                    adaptiveContext = adaptiveContext
                ) {
                    MemberDashboard(
                        snapshot = scenarioSnapshot,
                        currentEvent = scenarioEvent,
                        signals = engineResult.signals,
                        menus = resolvedMenus,
                        activeMenu = (state as? MemberIntelligenceUiState.Success)?.activeMenu ?: com.example.badnewgym.feature.memberintelligence.domain.model.MenuType.HOME,
                        onMenuSelected = viewModel::selectMenu,
                        primarySignal = engineResult.primary,
                        secondarySignals = engineResult.secondary,
                        cta = engineResult.cta,
                        onCta = viewModel::executeCta,
                        theme = pageTheme,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemePicker(
    selected: ThemeId,
    onSelected: (ThemeId) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ThemeId.entries.forEach { item ->
            val on = item == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (on) BADGymTheme.colors.accent else BADGymTheme.colors.surface)
                    .border(
                        1.dp,
                        if (on) BADGymTheme.colors.accent else BADGymTheme.colors.border,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelected(item) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = item.title,
                    color = if (on) BADGymTheme.colors.textOnAccent else BADGymTheme.colors.textSecondary,
                    fontSize = 10.sp,
                    fontWeight = if (on) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}
