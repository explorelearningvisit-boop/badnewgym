package com.example.badnewgym

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.deviceagent.DeviceAgentForegroundService
import com.example.badnewgym.deviceagent.DeviceAgentRuntime
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeviceAgentRuntime.attach(this)
        enableEdgeToEdge()
        if (DeviceAgentRuntime.server == null) {
            val serviceIntent = Intent(this, DeviceAgentForegroundService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
        val repository = StubMemberRepositoryImpl()
        setContent {
            val viewModel: MemberIntelligenceViewModel = viewModel(
                factory = MemberIntelligenceViewModel.provideFactory(repository)
            )
            MemberIntelligenceScreen(viewModel = viewModel)
        }
    }

    override fun onDestroy() {
        DeviceAgentRuntime.detach(this)
        super.onDestroy()
    }
}