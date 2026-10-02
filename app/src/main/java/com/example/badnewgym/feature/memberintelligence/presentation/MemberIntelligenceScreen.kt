package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.presentation.components.PixelPerfectMemberCard

@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG204", "gym1")
    }

    DisposableEffect(viewModel) {
        VariantDebugBridge.onCommand = { _, menu ->
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
    PixelPerfectMemberCard(
        snapshot = success?.snapshot,
        currentEvent = success?.currentEvent,
        signals = success?.signals.orEmpty(),
        menus = success?.menus.orEmpty(),
        activeMenu = success?.activeMenu,
        primarySignal = success?.primarySignal,
        secondarySignals = success?.secondarySignals.orEmpty(),
        cta = success?.cta,
        visitWidgetEntitlements = success?.visitWidgetEntitlements
            ?: com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetEntitlements(),
        onMenuSelected = viewModel::selectMenu,
        onCta = viewModel::executeCta,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    )
}
