package com.example.badnewgym.hybridtracking.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.badnewgym.MainActivity
import com.example.badnewgym.R
import com.example.badnewgym.hybridtracking.model.LocationResult
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import com.example.badnewgym.hybridtracking.network.BatchDispatcher
import com.example.badnewgym.hybridtracking.scanner.TelemetryScannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NetworkScannerService : Service() {
    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var scanJob: Job? = null

    private lateinit var repository: TelemetryScannerRepository
    private lateinit var dispatcher: BatchDispatcher

    private val _latestPayload = MutableStateFlow<TelemetryPayload?>(null)
    val latestPayload: StateFlow<TelemetryPayload?> = _latestPayload.asStateFlow()

    private val _latestLocation = MutableStateFlow<LocationResult?>(null)
    val latestLocation: StateFlow<LocationResult?> = _latestLocation.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): NetworkScannerService = this@NetworkScannerService
    }

    override fun onCreate() {
        super.onCreate()
        repository = TelemetryScannerRepository(applicationContext)
        dispatcher = BatchDispatcher(applicationContext)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification("Hybrid RF Network Scanner running")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } catch (e: Exception) {
                startForeground(NOTIFICATION_ID, notification)
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        startScanningLoop()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun startScanningLoop(intervalMs: Long = 10000L) {
        if (scanJob?.isActive == true) return
        _isScanning.value = true
        scanJob = serviceScope.launch {
            while (isActive) {
                try {
                    val payload = repository.captureTelemetry()
                    _latestPayload.value = payload

                    // Flush batch telemetry to backend
                    val result = dispatcher.flushPendingTelemetry()
                    result.onSuccess { location ->
                        if (location != null) {
                            _latestLocation.value = location
                            updateNotification("Location: ${location.locationSource} (${String.format("%.4f, %.4f", location.lat, location.lng)})")
                        }
                    }
                } catch (e: Exception) {
                    // Handle scan failure gracefully
                }
                delay(intervalMs)
            }
        }
    }

    fun stopScanningLoop() {
        scanJob?.cancel()
        _isScanning.value = false
    }

    suspend fun triggerImmediateScanAndSync(): LocationResult? {
        val payload = repository.captureTelemetry()
        _latestPayload.value = payload
        val result = dispatcher.flushPendingTelemetry()
        return result.getOrNull()?.also {
            _latestLocation.value = it
        }
    }

    override fun onDestroy() {
        stopScanningLoop()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Network Scanner Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors cellular, Wi-Fi, and BLE network telemetry for positioning"
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm?.createNotificationChannel(channel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun buildNotification(contentText: String): Notification {
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingIntentFlags)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Low-GPS Positioning Engine")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(text: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, buildNotification(text))
    }

    companion object {
        const val CHANNEL_ID = "network_scanner_channel"
        const val NOTIFICATION_ID = 4040
    }
}
