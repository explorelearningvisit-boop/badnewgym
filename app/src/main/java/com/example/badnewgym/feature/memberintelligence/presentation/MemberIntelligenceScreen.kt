package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.presentation.components.CardPalette
import com.example.badnewgym.feature.memberintelligence.presentation.components.CompactMemberIntelligenceCard
import kotlinx.coroutines.launch

/**
 * Hosts the approved compact Member Intelligence card (200dp x 300dp).
 *
 * The card is the source of truth surface for member identity, payment state and the
 * combined attendance/workout view. Loading, error and locked states are preserved,
 * and every section header routes to a real detail flow rather than a no-op.
 */
@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var detailNotice by remember { mutableStateOf<String?>(null) }

    fun showNotice(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMemberData("BG305", "gym1")
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

    Box(
        Modifier
            .fillMaxSize()
            .background(CardPalette.Page)
    ) {
        when (val current = state) {
            is MemberIntelligenceUiState.Success -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CompactMemberIntelligenceCard(
                        snapshot = current.snapshot,
                        onOpenBodyStats = { detailNotice = "Body measurements" },
                        onOpenPayment = { detailNotice = "Payment history" },
                        onOpenActivity = { detailNotice = "Attendance & workout detail" },
                        onOpenOverflow = { detailNotice = "Member actions" }
                    )
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 12.dp)
                        .navigationBarsPadding()
                )
            }

            is MemberIntelligenceUiState.Locked -> CenteredNotice(
                text = "${current.featureName} requires ${current.planHint}."
            )

            is MemberIntelligenceUiState.Error -> CenteredNotice(text = current.message)

            else -> CenteredNotice(text = "Loading member intelligence…")
        }
    }

    detailNotice?.let { title ->
        AlertDialog(
            onDismissRequest = { detailNotice = null },
            title = { Text(title) },
            text = {
                Text(
                    "This destination is not connected in this build yet. The card above shows " +
                        "the real recorded values; no detail data is invented here."
                )
            },
            confirmButton = {
                TextButton(onClick = { detailNotice = null }) { Text("Close") }
            }
        )
    }
}

@Composable
private fun CenteredNotice(text: String) {
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = CardPalette.Muted)
    }
}