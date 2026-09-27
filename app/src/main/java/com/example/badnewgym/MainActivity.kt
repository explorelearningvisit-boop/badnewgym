package com.example.badnewgym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.data.repository.StubTemporalIntelligenceRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.data.repository.UnavailableMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.data.repository.UnavailableTemporalIntelligenceRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Preview fixtures are debug-only. Release builds never receive synthetic members.
        val repository = if (BuildConfig.DEBUG) {
            StubMemberRepositoryImpl()
        } else {
            UnavailableMemberRepositoryImpl()
        }
        val temporalRepository = if (BuildConfig.DEBUG) {
            StubTemporalIntelligenceRepositoryImpl()
        } else {
            UnavailableTemporalIntelligenceRepositoryImpl()
        }

        setContent {
            val viewModel: MemberIntelligenceViewModel = viewModel(
                factory = MemberIntelligenceViewModel.provideFactory(
                    repository = repository,
                    temporalRepository = temporalRepository
                )
            )
            MemberIntelligenceScreen(viewModel = viewModel, initialMemberId = "BG204", gymId = "gym1")
        }
    }
}
