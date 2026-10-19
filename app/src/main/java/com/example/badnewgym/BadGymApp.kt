package com.example.badnewgym

import android.app.Application
import android.util.Log
import com.example.badnewgym.deviceagent.BackgroundKeepAliveManager
import com.example.badnewgym.deviceagent.DeviceAccessibilityService

class BadGymApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("BadGymApp", "Application initialized - guaranteeing 24/7 background services")

        // 1. Ensure Accessibility Service is enabled via secure settings
        DeviceAccessibilityService.ensureEnabled(this)

        // 2. Start all persistent background services (MCP Server, Tailscale)
        BackgroundKeepAliveManager.startAllBackgroundServices(this)

        // 3. Register WorkManager watchdog
        BackgroundKeepAliveManager.setupWatchdogWork(this)

        // 4. Catch unexpected thread crashes and reschedule keepalive
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("BadGymApp", "Uncaught exception in thread ${thread.name}", throwable)
            BackgroundKeepAliveManager.scheduleServiceKeepAlive(this, delayMs = 1000L)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
