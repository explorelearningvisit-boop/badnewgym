package com.example.badnewgym.deviceagent

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

object TailscalePrefs {
    private const val PREFS_NAME = "badgym_tailscale_prefs"
    private const val KEY_ENABLED = "tailscale_enabled"
    private const val KEY_AUTH_KEY = "tailscale_auth_key"
    private const val KEY_HOSTNAME = "tailscale_hostname"
    private const val KEY_PORT = "tailscale_port"
    private const val KEY_AUTOSTART_ON_BOOT = "autostart_on_boot"
    private const val KEY_TRIGGER_APP = "trigger_app_on_turn_on"

    // Default Pre-authorized Reusable Auth Key for explorelearningvisit@gmail.com (expires Jan 17, 2027)
    const val DEFAULT_AUTH_KEY = "tskey-auth-kYUKvTa2YC11CNTRL-AxHjCXPUGgFBat4YtN9yfF1WWcXLST3Aj"

    private fun getPrefs(context: Context): SharedPreferences {
        val safeContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !context.isDeviceProtectedStorage) {
            try {
                context.createDeviceProtectedStorageContext()
            } catch (_: Exception) {
                context
            }
        } else {
            context
        }
        return safeContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_ENABLED, true)

    fun setEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun getAuthKey(context: Context): String {
        val stored = getPrefs(context).getString(KEY_AUTH_KEY, "") ?: ""
        return if (stored.isNotBlank()) stored else DEFAULT_AUTH_KEY
    }

    fun setAuthKey(context: Context, authKey: String) {
        getPrefs(context).edit().putString(KEY_AUTH_KEY, authKey.trim()).apply()
    }

    fun getHostname(context: Context): String {
        val brand = Build.MANUFACTURER.lowercase()
        val modelClean = Build.MODEL.replace("[^a-zA-Z0-9-]".toRegex(), "-").lowercase()
        val defaultName = if (brand.contains("oppo") || brand.contains("realme") || modelClean.contains("cph")) {
            "badgym-oppo"
        } else {
            "badgym-$modelClean"
        }
        return getPrefs(context).getString(KEY_HOSTNAME, defaultName) ?: defaultName
    }

    fun setHostname(context: Context, hostname: String) {
        getPrefs(context).edit().putString(KEY_HOSTNAME, hostname.trim()).apply()
    }

    fun getPort(context: Context): Int =
        getPrefs(context).getInt(KEY_PORT, 8787)

    fun setPort(context: Context, port: Int) {
        getPrefs(context).edit().putInt(KEY_PORT, port).apply()
    }

    fun isAutostartOnBoot(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_AUTOSTART_ON_BOOT, true)

    fun setAutostartOnBoot(context: Context, autostart: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_AUTOSTART_ON_BOOT, autostart).apply()
    }

    fun isTriggerAppOnTurnOn(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_TRIGGER_APP, true)

    fun setTriggerAppOnTurnOn(context: Context, trigger: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_TRIGGER_APP, trigger).apply()
    }
}

