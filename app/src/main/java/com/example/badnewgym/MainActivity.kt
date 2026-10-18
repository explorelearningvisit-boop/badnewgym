package com.example.badnewgym

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import com.example.badnewgym.deviceagent.DeviceAgentForegroundService
import com.example.badnewgym.deviceagent.DeviceAgentRuntime
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceScreen
import com.example.badnewgym.feature.memberintelligence.presentation.MemberIntelligenceViewModel
import com.example.badnewgym.feature.memberintelligence.presentation.components.CardGallery
import com.example.badnewgym.feature.memberintelligence.presentation.components.CompactCardComponentGallery
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios

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
        val isDebuggable =
            (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val galleryIndex = if (isDebuggable) intent.getIntExtra("gallery_index", -1) else -1
        setContent {
            if (galleryIndex in 0 until CardGallery.COUNT) {
                // Debug-only design verification harness: one component per launch.
                val snapshot = remember { MemberScenarios.compactSummaryAman().first }
                // Sample the clock once so the captured component is reproducible.
                val galleryNow = remember { System.currentTimeMillis() }
                CompactCardComponentGallery(
                    snapshot = snapshot,
                    index = galleryIndex,
                    now = galleryNow
                )
            } else {
                val viewModel: MemberIntelligenceViewModel = viewModel(
                    factory = MemberIntelligenceViewModel.provideFactory(repository)
                )
                MemberIntelligenceScreen(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        DeviceAgentRuntime.detach(this)
        super.onDestroy()
    }
}