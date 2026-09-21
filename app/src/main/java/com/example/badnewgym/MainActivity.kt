package com.example.badnewgym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen

import androidx.room.Room
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.memberintelligence.data.local.MemberDatabase
import com.example.badnewgym.feature.memberintelligence.data.repository.OfflineFirstMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl()
        
        enableEdgeToEdge()
        setContent {
            val viewModel: MemberIntelligenceViewModel = viewModel(
                factory = MemberIntelligenceViewModel.provideFactory(repository)
            )
            Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}