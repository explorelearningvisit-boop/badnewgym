package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard

@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG204", "gym1")
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

    val success = state as? MemberIntelligenceUiState.Success
    val theme = success?.themeId ?: ThemeId.NATURAL_FRESH

    if (success == null) {
        BADGymTheme(colors = theme.colors(), shapes = theme.shapes(), motion = theme.motion(), elevation = theme.elevation()) {
            Scaffold(containerColor = theme.colors().background) { padding ->
                Box(Modifier.fillMaxSize().background(theme.colors().background))
            }
        }
        return
    }

    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = theme.colors().background
        ) {
            MemberDashboard(
                snapshot = success.snapshot,
                currentEvent = success.currentEvent,
                signals = success.signals,
                menus = success.menus,
                activeMenu = success.activeMenu,
                onMenuSelected = viewModel::selectMenu,
                primarySignal = success.primarySignal,
                secondarySignals = success.secondarySignals,
                cta = success.cta,
                onCta = viewModel::executeCta,
                theme = theme,
                onThemeSelected = viewModel::selectTheme,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
