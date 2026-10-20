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
        Log.i("BootCompletedReceiver", "Received broadcast: $action")

        if (action == "com.example.badnewgym.SET_TAILSCALE_AUTHKEY") {
            val key = intent.getStringExtra("authkey")?.trim() ?: ""
            if (key.isNotEmpty()) {
                Log.i("BootCompletedReceiver", "Setting Tailscale Auth Key programmatically: ${key.take(12)}...")
                TailscalePrefs.setAuthKey(context, key)
                TailscalePrefs.setEnabled(context, true)
                TailscaleManager.restart(context)
            }
            return
        }

        val isKeepAliveAction = action == "com.example.badnewgym.KEEPALIVE_PING" ||
            action == Intent.ACTION_USER_PRESENT ||
            action == Intent.ACTION_POWER_CONNECTED

        val autostart = TailscalePrefs.isAutostartOnBoot(context)
        if (!autostart && !isKeepAliveAction) {
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
        BackgroundKeepAliveManager.startAllBackgroundServices(context)
        BackgroundKeepAliveManager.setupWatchdogWork(context)
        Log.i("BootCompletedReceiver", "DeviceAgentForegroundService kept alive from broadcast: $action")
    }
}
