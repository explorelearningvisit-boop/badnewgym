package com.example.badnewgym.deviceagent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.badnewgym.MainActivity
import com.example.badnewgym.R

class DeviceAgentForegroundService : Service() {
    companion object {
        private const val CHANNEL_ID = "badgym_device_agent"
        private const val NOTIFICATION_ID = 8701
    }

    private var server: RemoteDeviceMcpServer? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        val token = DeviceAgentToken.get(this)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("BAD GYM Device Agent")
            .setContentText("MCP :8787 • token §{token.take(8)}…")
            .setContentIntent(PendingIntent.getActivity(
                this, 0, Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ))
            .setOngoing(true)
            .build()

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
    }

    override fun onDestroy() {
        server?.stop()
        DeviceAgentRuntime.server = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "BAD GYM Device Agent",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }
}