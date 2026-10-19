package com.example.badnewgym.deviceagent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.badnewgym.MainActivity

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.i("BootCompletedReceiver", "Received boot/restart broadcast: $action")

        val autostart = TailscalePrefs.isAutostartOnBoot(context)
        if (!autostart) {
            Log.i("BootCompletedReceiver", "Autostart on boot is disabled in preferences")
            return
        }

        // 1. If configured, trigger the app UI to turn on / launch
        if (TailscalePrefs.isTriggerAppOnTurnOn(context)) {
            try {
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                    putExtra("auto_triggered", true)
                }
                context.startActivity(launchIntent)
                Log.i("BootCompletedReceiver", "MainActivity triggered on device turn on")
            } catch (e: Exception) {
                Log.w("BootCompletedReceiver", "Could not launch MainActivity directly on boot: ${e.message}")
            }
        }

        // 2. Start DeviceAgentForegroundService (which starts MCP server and Tailscale tsnet daemon)
        val serviceIntent = Intent(context, DeviceAgentForegroundService::class.java).apply {
            putExtra("from_boot", true)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
            Log.i("BootCompletedReceiver", "DeviceAgentForegroundService started from boot")
        } catch (e: Exception) {
            Log.w("BootCompletedReceiver", "Could not start service directly from broadcast (handled by Activity): ${e.message}")
        }
    }
}
