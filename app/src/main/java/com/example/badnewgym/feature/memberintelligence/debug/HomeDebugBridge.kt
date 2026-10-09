package com.example.badnewgym.feature.memberintelligence.debug

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * No-tap verification bridge for Home interactions.
 *
 * MIUI blocks `adb input` injection, so taps cannot be scripted from the
 * laptop. This bridge exposes the exact same ViewModel handlers a finger
 * would reach (end session, collect, range switch) through broadcasts, and
 * every transition is then verified with on-device screenshots. Same pattern
 * as the existing [VariantDebugReceiver]; production code paths are shared,
 * never duplicated.
 */
object HomeDebugBridge {
    @Volatile
    var onEndSession: (() -> Unit)? = null

    @Volatile
    var onCollect: ((amount: Double, method: String) -> Unit)? = null

    @Volatile
    var onSetRange: ((range: String) -> Unit)? = null

    @Volatile
    var onOpenModule: ((module: String) -> Unit)? = null

    @Volatile
    var onStatusQuery: (() -> String)? = null
}

class HomeDebugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_END_SESSION -> {
                HomeDebugBridge.onEndSession?.invoke()
                finish("ended")
            }
            ACTION_COLLECT -> {
                val amount = intent.getDoubleExtra(EXTRA_AMOUNT, -1.0)
                val method = intent.getStringExtra(EXTRA_METHOD) ?: "UPI"
                HomeDebugBridge.onCollect?.invoke(amount, method)
                finish("collect amount=$amount method=$method")
            }
            ACTION_SET_RANGE -> {
                val range = intent.getStringExtra(EXTRA_RANGE) ?: "7D"
                HomeDebugBridge.onSetRange?.invoke(range)
                finish("range=$range")
            }
            ACTION_OPEN_MODULE -> {
                val module = intent.getStringExtra(EXTRA_MODULE) ?: ""
                HomeDebugBridge.onOpenModule?.invoke(module)
                finish("module=$module")
            }
            ACTION_STATUS -> {
                finish(HomeDebugBridge.onStatusQuery?.invoke() ?: "no_home")
            }
            else -> finish("unknown")
        }
    }

    private fun finish(data: String) {
        resultCode = Activity.RESULT_OK
        resultData = data
    }

    companion object {
        const val ACTION_END_SESSION = "com.example.badnewgym.HOME.END_SESSION"
        const val ACTION_COLLECT = "com.example.badnewgym.HOME.COLLECT"
        const val ACTION_SET_RANGE = "com.example.badnewgym.HOME.SET_RANGE"
        const val ACTION_OPEN_MODULE = "com.example.badnewgym.HOME.OPEN_MODULE"
        const val ACTION_STATUS = "com.example.badnewgym.HOME.STATUS"
        const val EXTRA_AMOUNT = "amount"
        const val EXTRA_METHOD = "method"
        const val EXTRA_RANGE = "range"
        const val EXTRA_MODULE = "module"
    }
}
