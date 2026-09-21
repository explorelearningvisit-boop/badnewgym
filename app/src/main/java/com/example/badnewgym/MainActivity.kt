package com.example.badnewgym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = StubMemberRepositoryImpl()
        setContent {
            val viewModel: MemberIntelligenceViewModel = viewModel(
                factory = MemberIntelligenceViewModel.provideFactory(repository)
            )
            MemberIntelligenceScreen(viewModel = viewModel)
        }
    }
}