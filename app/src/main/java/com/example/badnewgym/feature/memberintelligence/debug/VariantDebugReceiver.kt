package com.example.badnewgym.feature.memberintelligence.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

object VariantDebugBridge {
    @Volatile
    var onCommand: ((ThemeId?, MenuType?) -> Unit)? = null

    @Volatile
    var onMemberIndex: ((Int) -> Unit)? = null
}

class VariantDebugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val variantName = intent.getStringExtra("variant")
        val menuName = intent.getStringExtra("menu")
        val memberIndex = intent.getIntExtra("memberIndex", -1)

        if (memberIndex >= 0) {
            VariantDebugBridge.onMemberIndex?.invoke(memberIndex)
        }

        val variant = variantName?.let { runCatching { ThemeId.valueOf(it) }.getOrNull() }
        val menu = menuName?.let { runCatching { MenuType.valueOf(it) }.getOrNull() }

        if (variant != null || menu != null) {
            VariantDebugBridge.onCommand?.invoke(variant, menu)
        }
    }
}
