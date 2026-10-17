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
import com.example.badnewgym.feature.memberintelligence.presentation.components.ReferenceMemberIntelligenceDashboard

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
    val resolved = success
    if (resolved == null) {
        androidx.compose.foundation.layout.Box(
            Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = when (state) {
                    is MemberIntelligenceUiState.Error -> (state as MemberIntelligenceUiState.Error).message
                    else -> "Loading member intelligence…"
                }
            )
        }
    } else {
        ReferenceMemberIntelligenceDashboard(
            snapshot = resolved.snapshot,
            currentEvent = resolved.currentEvent,
            menus = resolved.menus,
            activeMenu = resolved.activeMenu,
            onMenuSelected = viewModel::selectMenu,
            paymentAction = resolved.signals.firstOrNull { it.category == com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory.PAYMENT }?.action,
            onCta = viewModel::executeCta,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        )
    }
}
