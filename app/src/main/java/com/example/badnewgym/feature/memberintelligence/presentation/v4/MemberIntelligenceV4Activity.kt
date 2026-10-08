package com.example.badnewgym.feature.memberintelligence.presentation.v4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors

/**
 * Dedicated V4 host activity.
 *
 * Kept separate from MainActivity so the existing hybrid tracking entry point
 * stays functional - existing functionality is preserved (spec 81).
 */
class MemberIntelligenceV4Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val memberId = intent?.getStringExtra(EXTRA_MEMBER_ID) ?: DEFAULT_MEMBER_ID

        setContent {
            val vm: MemberCardV4ViewModel = viewModel(
                factory = MemberCardV4ViewModel.factory(StubMemberRepositoryImpl())
            )
            val state by vm.state.collectAsState()

            if (state is MemberCardV4UiState.Loading) {
                vm.setInitialTab(
                    if (intent?.getBooleanExtra(EXTRA_PROGRESS_TAB, false) == true) V4Tab.PROGRESS
                    else V4Tab.OVERVIEW
                )
                vm.load(memberId)
            }

            Box(Modifier.fillMaxSize().background(MiColors.Page)) {
                MemberIntelligenceScreenV4(viewModel = vm)
            }
        }
    }

    companion object {
        const val EXTRA_MEMBER_ID = "member_id"
        const val EXTRA_PROGRESS_TAB = "start_progress_tab"
        const val DEFAULT_MEMBER_ID = "BG305"
    }
}