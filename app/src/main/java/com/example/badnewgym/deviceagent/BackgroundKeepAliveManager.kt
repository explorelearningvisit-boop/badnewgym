package com.example.badnewgym.deviceagent

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object BackgroundKeepAliveManager {
    private const val TAG = "BackgroundKeepAlive"
    private const val KEEP_ALIVE_ALARM_REQ_CODE = 9911

    /**
     * Checks whether this app is exempt from system battery optimizations (Doze mode).
     * WhatsApp and persistent messaging services require this to avoid being killed
     * when the app is minimized, closed, or the screen is turned off.
     */
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return true
            return pm.isIgnoringBatteryOptimizations(context.packageName)
        }
        return true
    }

    /**
     * Prompts the user to exempt BAD GYM from battery optimizations so it can run 24/7
     * in the background, like WhatsApp.
     */
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (!isIgnoringBatteryOptimizations(context)) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Direct ignore battery optimization request failed, falling back to settings", e)
                try {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                } catch (t: Throwable) {
                    Log.e(TAG, "Could not open battery optimization settings", t)
                }
            }
        }
    }

    /**
     * Schedules an immediate alarm to resurrect background services if the app
     * was swiped away from the Recent Apps list (Task removed).
     */
    fun scheduleServiceKeepAlive(context: Context, delayMs: Long = 1000L) {
        try {
            val restartIntent = Intent(context, DeviceAgentForegroundService::class.java).apply {
                putExtra("from_keepalive", true)
            }
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.getForegroundService(
                    context,
                    KEEP_ALIVE_ALARM_REQ_CODE,
                    restartIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getService(
                    context,
                    KEEP_ALIVE_ALARM_REQ_CODE,
                    restartIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val triggerTime = SystemClock.elapsedRealtime() + delayMs

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager?.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.i(TAG, "Scheduled keepalive alarm for +${delayMs}ms")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule keepalive alarm", e)
        }
    }

    /**
     * Enqueues a periodic WorkManager watchdog to guarantee that even if the app
     * is killed after days of idle or OS memory compaction, it automatically wakes up.
     */
    fun setupWatchdogWork(context: Context) {
        try {
            val request = PeriodicWorkRequestBuilder<KeepAliveWorker>(
                15, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                KeepAliveWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
            Log.i(TAG, "WorkManager periodic keepalive watchdog registered")
        } catch (e: Exception) {
            Log.e(TAG, "Could not register WorkManager watchdog", e)
        }
    }

    /**
     * Ensures all core foreground background services (Device Agent MCP, Tailscale) are running.
     */
    fun startAllBackgroundServices(context: Context) {
        val serviceIntent = Intent(context, DeviceAgentForegroundService::class.java).apply {
            putExtra("from_keepalive", true)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start DeviceAgentForegroundService", e)
        }
    }

    /**
     * Opens manufacturer-specific Auto-Start / Background Activity settings
     * (crucial for Xiaomi MIUI/HyperOS, OPPO ColorOS, Vivo, Samsung).
     */
    fun openOemAutostartSettings(context: Context): Boolean {
        val brand = Build.MANUFACTURER.lowercase()
        val intents = mutableListOf<Intent>()

        when {
            brand.contains("xiaomi") || brand.contains("redmi") || brand.contains("poco") -> {
                intents.add(Intent().setComponent(ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")))
                intents.add(Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT))
            }
            brand.contains("oppo") || brand.contains("realme") || brand.contains("oneplus") -> {
                intents.add(Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")))
                intents.add(Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")))
                intents.add(Intent().setComponent(ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")))
                intents.add(Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startupapp.StartupAppListActivity")))
            }
            brand.contains("vivo") || brand.contains("iqoo") -> {
                intents.add(Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")))
                intents.add(Intent().setComponent(ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity")))
            }
            brand.contains("samsung") -> {
                intents.add(Intent().setComponent(ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")))
                intents.add(Intent().setComponent(ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity")))
            }
            brand.contains("huawei") || brand.contains("honor") -> {
                intents.add(Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")))
                intents.add(Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")))
            }
        }

        // Generic fallback: App Details Settings
        intents.add(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        )

        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return true
            } catch (_: Exception) {
                // Try next
            }
        }
        return false
    }

    fun getOemName(): String {
        val brand = Build.MANUFACTURER.lowercase()
        return when {
            brand.contains("xiaomi") || brand.contains("redmi") || brand.contains("poco") -> "Xiaomi (MIUI/HyperOS)"
            brand.contains("oppo") || brand.contains("realme") || brand.contains("oneplus") -> "OPPO (ColorOS)"
            brand.contains("vivo") || brand.contains("iqoo") -> "Vivo (OriginOS/Funtouch)"
            brand.contains("samsung") -> "Samsung (OneUI)"
            brand.contains("huawei") || brand.contains("honor") -> "Huawei (EMUI)"
            else -> Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        }
    }
}
