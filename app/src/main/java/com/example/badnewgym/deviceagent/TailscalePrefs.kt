package com.example.badnewgym.deviceagent

import android.content.Context
import android.os.Build

object TailscalePrefs {
    private const val PREFS_NAME = "badgym_tailscale_prefs"
    private const val KEY_ENABLED = "tailscale_enabled"
    private const val KEY_AUTH_KEY = "tailscale_auth_key"
    private const val KEY_HOSTNAME = "tailscale_hostname"
    private const val KEY_PORT = "tailscale_port"
    private const val KEY_AUTOSTART_ON_BOOT = "autostart_on_boot"
    private const val KEY_TRIGGER_APP = "trigger_app_on_turn_on"

    fun isEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, true)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun getAuthKey(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_AUTH_KEY, "") ?: ""

    fun setAuthKey(context: Context, authKey: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_AUTH_KEY, authKey.trim()).apply()
    }

    fun getHostname(context: Context): String {
        val modelClean = Build.MODEL.replace("[^a-zA-Z0-9-]".toRegex(), "-").lowercase()
        val defaultName = "badgym-$modelClean"
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_HOSTNAME, defaultName) ?: defaultName
    }

    fun setHostname(context: Context, hostname: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_HOSTNAME, hostname.trim()).apply()
    }

    fun getPort(context: Context): Int =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_PORT, 8787)

    fun setPort(context: Context, port: Int) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_PORT, port).apply()
    }

    fun isAutostartOnBoot(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_AUTOSTART_ON_BOOT, true)

    fun setAutostartOnBoot(context: Context, autostart: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_AUTOSTART_ON_BOOT, autostart).apply()
    }

    fun isTriggerAppOnTurnOn(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_TRIGGER_APP, true)

    fun setTriggerAppOnTurnOn(context: Context, trigger: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_TRIGGER_APP, trigger).apply()
    }
}
