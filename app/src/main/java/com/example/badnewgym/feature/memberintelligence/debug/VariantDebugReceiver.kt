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

    @Volatile
    var onCloseDetail: (() -> Unit)? = null

    @Volatile
    var onOpenDetail: ((Int?) -> Unit)? = null

    @Volatile
    var onToggleQaGallery: ((Boolean) -> Unit)? = null

    @Volatile
    var onLoadFixture: ((String) -> Unit)? = null
}

class VariantDebugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val memberIndex = intent.getIntExtra("memberIndex", -1)
        if (memberIndex >= 0) {
            VariantDebugBridge.onMemberIndex?.invoke(memberIndex)
        }

        val closeDetail = intent.getBooleanExtra("closeDetail", false)
        if (closeDetail) {
            VariantDebugBridge.onCloseDetail?.invoke()
        }

        val openDetail = intent.getBooleanExtra("openDetail", false)
        if (openDetail) {
            VariantDebugBridge.onOpenDetail?.invoke(if (memberIndex >= 0) memberIndex else null)
        }

        if (intent.hasExtra("qaGallery")) {
            val open = intent.getBooleanExtra("qaGallery", true)
            VariantDebugBridge.onToggleQaGallery?.invoke(open)
        }

        val fixtureId = intent.getStringExtra("fixtureId")
        if (fixtureId != null) {
            VariantDebugBridge.onLoadFixture?.invoke(fixtureId)
        }

        val variantName = intent.getStringExtra("variant")
        val menuName = intent.getStringExtra("menu")

        val variant = variantName?.let { runCatching { ThemeId.valueOf(it) }.getOrNull() }
        val menu = menuName?.let { runCatching { MenuType.valueOf(it) }.getOrNull() }

        if (variant != null || menu != null) {
            VariantDebugBridge.onCommand?.invoke(variant, menu)
        }
    }
}
