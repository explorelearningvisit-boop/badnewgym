package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.presentation.components.PixelPerfectMemberCard

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding

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
        PixelPerfectMemberCard(
            snapshot = null,
            currentEvent = null,
            signals = emptyList(),
            menus = emptyList(),
            activeMenu = null,
            theme = theme,
            onThemeSelected = viewModel::selectTheme,
            onBack = { backDispatcher?.onBackPressed() },
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        )
        return
    }

    AnimatedContent(
        targetState = theme,
        transitionSpec = {
            fadeIn(tween(220)) + slideInHorizontally(tween(260)) togetherWith
                fadeOut(tween(150)) + slideOutHorizontally(tween(180))
        },
        label = "theme-shell",
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { animatedTheme ->
        PixelPerfectMemberCard(
            snapshot = success.snapshot,
            currentEvent = success.currentEvent,
            signals = success.signals,
            menus = success.menus,
            activeMenu = success.activeMenu,
            primarySignal = success.primarySignal,
            secondarySignals = success.secondarySignals,
            cta = success.cta,
            theme = animatedTheme,
            onMenuSelected = viewModel::selectMenu,
            onThemeSelected = viewModel::selectTheme,
            onCta = viewModel::executeCta,
            onBack = { backDispatcher?.onBackPressed() },
            modifier = Modifier.fillMaxSize()
        )
    }
}