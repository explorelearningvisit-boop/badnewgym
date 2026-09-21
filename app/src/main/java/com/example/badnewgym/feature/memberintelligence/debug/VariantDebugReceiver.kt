package com.example.badnewgym.feature.memberintelligence.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

object VariantDebugBridge {
    @Volatile
    var onCommand: ((ThemeId?, MenuType?) -> Unit)? = null
}

class VariantDebugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val variantName = intent.getStringExtra("variant")
        val menuName = intent.getStringExtra("menu") ?: "HOME"
        val variant = variantName?.let { runCatching { ThemeId.valueOf(it) }.getOrNull() }
        val menu = runCatching { MenuType.valueOf(menuName) }.getOrNull() ?: MenuType.HOME
        VariantDebugBridge.onCommand?.invoke(variant, menu)
    }
}
