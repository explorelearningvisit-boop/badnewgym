package com.example.badnewgym

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.badnewgym.deviceagent.DeviceAgentForegroundService
import com.example.badnewgym.deviceagent.DeviceAgentRuntime

class MainActivity : ComponentActivity() {
    private val initialSheetState = androidx.compose.runtime.mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSheetIntent(intent)
        DeviceAgentRuntime.attach(this)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
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
        com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.startAllBackgroundServices(this)
        com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.setupWatchdogWork(this)
        if (!com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.isIgnoringBatteryOptimizations(this)) {
            com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.requestIgnoreBatteryOptimizations(this)
        }
        setContent {
            com.example.badnewgym.feature.membercard.presentation.MemberCheckInScreen(
                initialSheet = initialSheetState.value
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSheetIntent(intent)
    }

    private fun handleSheetIntent(intent: Intent?) {
        initialSheetState.value = intent?.getStringExtra("sheet")
    }

    override fun onDestroy() {
        DeviceAgentRuntime.detach(this)
        super.onDestroy()
    }
}
