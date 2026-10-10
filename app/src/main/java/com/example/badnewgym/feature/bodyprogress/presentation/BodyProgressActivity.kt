package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors

/**
 * Body Progress analytics host.
 *
 * Opened from the Home widget and from the rail. The analytics screen owns its
 * own scrolling; the Home card itself stays non-scrolling by contract.
 */
class BodyProgressActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val scenario = runCatching {
            BodyProgressViewModel.Scenario.valueOf(intent?.getStringExtra(EXTRA_SCENARIO) ?: "FULL")
        }.getOrDefault(BodyProgressViewModel.Scenario.FULL)
        val memberId = intent?.getStringExtra(EXTRA_MEMBER_ID) ?: "BG305"
        setContent {
            Box(Modifier.fillMaxSize().background(MiColors.Page)) {
                BodyProgressAnalyticsRoute(scenario, memberId) { finish() }
            }
        }
    }

    companion object {
        const val EXTRA_SCENARIO = "body_scenario"
        const val EXTRA_MEMBER_ID = "member_id"
    }
}

@Composable
fun BodyProgressAnalyticsRoute(
    scenario: BodyProgressViewModel.Scenario,
    memberId: String,
    onBack: () -> Unit
) {
    val vm: BodyProgressViewModel = viewModel(factory = BodyProgressViewModel.factory(scenario, memberId))
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    when (val s = state) {
        BodyProgressUiState.Loading ->
            // Insets are applied by the parent surface, so the spinner must not
            // add them twice.
            Box(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MiColors.Emerald)
            }

        BodyProgressUiState.InsufficientData ->
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    "No recorded body or workout data for this member.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MiColors.TextSecondary
                )
            }

        is BodyProgressUiState.Error ->
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(s.message, fontSize = 13.sp, color = MiColors.Red)
            }

        is BodyProgressUiState.Ready -> BodyProgressAnalyticsScreen(
            summarize = { range, metric, muscle ->
                vm.summaryFor(range, metric, muscle)
            },
            initialRange = s.range,
            viewerRole = s.viewerRole,
            onBack = onBack
        )
    }
}

/** Default range for direct launches. */
val DEFAULT_BODY_RANGE = BodyRange.D30
