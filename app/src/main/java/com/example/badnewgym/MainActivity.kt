package com.example.badnewgym

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.badnewgym.deviceagent.DeviceAgentForegroundService
import com.example.badnewgym.deviceagent.DeviceAgentRuntime
import com.example.badnewgym.feature.memberintelligence.presentation.home.HomeViewModel
import com.example.badnewgym.feature.memberintelligence.presentation.home.HomeWidgetId
import com.example.badnewgym.feature.memberintelligence.presentation.home.MemberIntelligenceHome

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeviceAgentRuntime.attach(this)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        @Suppress("DEPRECATION")
        window.addFlags(
            android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        if (DeviceAgentRuntime.server == null) {
            val serviceIntent = Intent(this, DeviceAgentForegroundService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
        setContent {
            val memberId = intent?.getStringExtra(EXTRA_MEMBER_ID)
                ?: HomeViewModel.DEFAULT_MEMBER_ID
            val openModule = intent?.getStringExtra(EXTRA_OPEN_MODULE)
            MemberIntelligenceHome(memberId = memberId, startModule = openModule)
        }
    }

    override fun onDestroy() {
        DeviceAgentRuntime.detach(this)
        super.onDestroy()
    }

    companion object {
        const val EXTRA_MEMBER_ID = "member_id"
        const val EXTRA_OPEN_MODULE = "home_open_module"
    }
}
