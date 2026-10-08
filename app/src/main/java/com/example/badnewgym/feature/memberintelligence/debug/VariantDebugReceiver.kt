package com.example.badnewgym.feature.memberintelligence.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.badnewgym.deviceagent.DeviceAgentRuntime
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import java.io.File
import java.io.FileOutputStream

object VariantDebugBridge {
    @Volatile
    var onCommand: ((ThemeId?, MenuType?) -> Unit)? = null
    @Volatile
    var onFlowCommand: ((String) -> Unit)? = null
}

class VariantDebugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: intent.getStringExtra("action")
        if (action == "com.example.badnewgym.SCREENSHOT" || action == "screenshot") {
            try {
                val bytes = DeviceAgentRuntime.screenshotPng()
                // Save to app files dir (always writable without external permissions)
                val targetFile = File(context.filesDir, "badgym_screenshot.png")
                FileOutputStream(targetFile).use { it.write(bytes) }
                // Also attempt to save to /sdcard/Download
                runCatching {
                    val publicFile = File("/sdcard/Download/badgym_screenshot.png")
                    FileOutputStream(publicFile).use { it.write(bytes) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        val variantName = intent.getStringExtra("variant")
        val menuName = intent.getStringExtra("menu") ?: "HOME"
        val variant = variantName?.let { runCatching { ThemeId.valueOf(it) }.getOrNull() }
        val menu = runCatching { MenuType.valueOf(menuName) }.getOrNull() ?: MenuType.HOME
        VariantDebugBridge.onCommand?.invoke(variant, menu)
        intent.getStringExtra("flow")?.let { flow ->
            VariantDebugBridge.onFlowCommand?.invoke(flow)
        }
    }
}
