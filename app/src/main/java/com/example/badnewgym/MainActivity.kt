package com.example.badnewgym

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.badnewgym.deviceagent.DeviceAgentForegroundService
import com.example.badnewgym.deviceagent.DeviceAgentRuntime
import com.example.badnewgym.hybridtracking.service.NetworkScannerService
import com.example.badnewgym.hybridtracking.ui.HybridTrackingDashboard

class MainActivity : ComponentActivity() {

    private var scannerService by mutableStateOf<NetworkScannerService?>(null)
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? NetworkScannerService.LocalBinder
            scannerService = localBinder?.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            scannerService = null
            isBound = false
        }
    }

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

        // Start and bind NetworkScannerService
        val scannerIntent = Intent(this, NetworkScannerService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(scannerIntent)
        } else {
            startService(scannerIntent)
        }
        bindService(scannerIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        setContent {
            HybridTrackingDashboard(service = scannerService)
        }
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        DeviceAgentRuntime.detach(this)
        super.onDestroy()
    }
}