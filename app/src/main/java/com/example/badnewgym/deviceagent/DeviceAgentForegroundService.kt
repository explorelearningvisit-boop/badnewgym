package com.example.badnewgym.deviceagent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.badnewgym.MainActivity
import com.example.badnewgym.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DeviceAgentForegroundService : Service() {
    companion object {
        private const val TAG = "DeviceAgentService"
        private const val CHANNEL_ID = "badgym_device_agent"
        private const val NOTIFICATION_ID = 8701
    }

    private var server: RemoteDeviceMcpServer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        createChannel()

        val token = DeviceAgentToken.get(this)
        val initialText = "MCP :8787 • token ${token.take(8)}…"
        val notification = buildNotification(initialText)

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= 34) {
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else 0
        )

        server = RemoteDeviceMcpServer(this).also { it.start() }
        DeviceAgentRuntime.server = server

        // Start embedded Tailscale daemon if enabled
        if (TailscalePrefs.isEnabled(this)) {
            TailscaleManager.start(this)
        }

        // Dynamically update foreground notification based on Tailscale connection status
        serviceScope.launch {
            TailscaleManager.statusFlow.collectLatest { status ->
                val statusText = when {
                    status.connected -> "Tailscale: ${status.ip}:${status.listenPort} • Connected"
                    status.backendState == "NeedsLogin" -> "Tailscale: Login required • Click to view"
                    status.backendState == "Starting" -> "Tailscale: Connecting to tailnet..."
                    status.backendState == "Disabled" -> "Tailscale: Disabled • Local MCP :8787"
                    status.error != null -> "Tailscale: ${status.error}"
                    else -> "Tailscale: ${status.backendState} • Port :${status.listenPort}"
                }
                notificationManager?.notify(NOTIFICATION_ID, buildNotification(statusText))
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fromBoot = intent?.getBooleanExtra("from_boot", false) == true
        if (fromBoot) {
            Log.i(TAG, "DeviceAgentForegroundService started via BOOT_COMPLETED")
        }

        if (TailscalePrefs.isEnabled(this) && !TailscaleManager.isDaemonRunning()) {
            TailscaleManager.start(this)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel()
        TailscaleManager.stop()
        server?.stop()
        DeviceAgentRuntime.server = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(contentText: String) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("BAD GYM Device Agent")
            .setContentText(contentText)
            .setContentIntent(PendingIntent.getActivity(
                this, 0, Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ))
            .setOngoing(true)
            .build()

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "BAD GYM Device Agent",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }
}