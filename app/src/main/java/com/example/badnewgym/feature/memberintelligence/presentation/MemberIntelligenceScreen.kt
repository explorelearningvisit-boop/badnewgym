package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.presentation.components.ReferenceMemberIntelligenceDashboard
import kotlinx.coroutines.launch

@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showNotice(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG204", "gym1")
    }

    LaunchedEffect(viewModel) {
        viewModel.notices.collect { showNotice(it) }
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

    Box(Modifier.fillMaxSize()) {
        val success = state as? MemberIntelligenceUiState.Success
        if (success == null) {
            Box(
                Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = when (state) {
                        is MemberIntelligenceUiState.Error -> (state as MemberIntelligenceUiState.Error).message
                        else -> "Loading member intelligence…"
                    }
                )
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                ReferenceMemberIntelligenceDashboard(
                    snapshot = success.snapshot,
                    currentEvent = success.currentEvent,
                    menus = success.menus,
                    activeMenu = success.activeMenu,
                    onMenuSelected = viewModel::selectMenu,
                    paymentAction = success.signals.firstOrNull { it.category == com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory.PAYMENT }?.action,
                    onCta = viewModel::executeCta,
                    onMemberSelected = viewModel::loadMemberData,
                    onNotice = ::showNotice,
                    modifier = Modifier.fillMaxSize()
                )
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 12.dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}
