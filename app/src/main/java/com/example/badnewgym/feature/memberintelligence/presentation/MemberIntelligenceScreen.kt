package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine
import com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard
import kotlinx.coroutines.launch

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val themes = ThemeId.entries
    val currentTheme = (state as? MemberIntelligenceUiState.Success)?.themeId ?: ThemeId.NATURAL_FRESH

    val pagerState = rememberPagerState(
        initialPage = themes.indexOf(currentTheme).coerceAtLeast(0),
        pageCount = { themes.size }
    )

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG204", "gym1")
    }

    LaunchedEffect(pagerState.currentPage) {
        val target = themes[pagerState.currentPage]
        if (target != currentTheme) viewModel.selectTheme(target)
    }

    LaunchedEffect(currentTheme) {
        val targetIndex = themes.indexOf(currentTheme)
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
        ThemeId.GLASSMORPHISM -> Color(0xFFEAF4FF)
        ThemeId.PREMIUM_3D -> Color(0xFF070604)
        ThemeId.VIBRANT_GRADIENT -> Color(0xFFFFF4FA)
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
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = outerBackground
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(outerBackground)
                    .padding(padding)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 12.dp,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val pageTheme = themes[page]
                    val (scenarioSnapshot, scenarioEvent) =
                        com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios
                            .getScenarioForTheme(pageTheme)

                    val engineResult = MemberIntelligenceEngine().evaluate(
                        scenarioSnapshot,
                        scenarioEvent,
                        scenarioEvent.occurredAt
                    )
                    val resolvedMenus = MenuAvailabilityResolver.resolve(
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
                            activeMenu = (state as? MemberIntelligenceUiState.Success)?.activeMenu ?: MenuType.HOME,
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
}
