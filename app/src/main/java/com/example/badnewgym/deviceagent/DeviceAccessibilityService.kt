package com.example.badnewgym.deviceagent

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.google.gson.GsonBuilder

class DeviceAccessibilityService : AccessibilityService() {
    companion object {
        @Volatile var instance: DeviceAccessibilityService? = null

        fun tap(x: Int, y: Int) = requireService().dispatchTap(x.toFloat(), y.toFloat())
        fun swipe(x1: Int, y1: Int, x2: Int, y2: Int, durationMs: Int) =
            requireService().dispatchSwipe(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), durationMs)

        fun back() { requireService().performGlobalAction(GLOBAL_ACTION_BACK) }
        fun home() { requireService().performGlobalAction(GLOBAL_ACTION_HOME) }

        fun setText(text: String) {
            val service = requireService()
            val root = service.rootInActiveWindow ?: error("No active accessibility window")
            val focused = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT) ?: error("No focused input field")
            val args = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
            }
            if (!focused.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)) {
                error("Focused field rejected ACTION_SET_TEXT")
            }
            focused.recycle()
            root.recycle()
        }

        fun dumpTree(): String {
            val service = requireService()
            val root = service.rootInActiveWindow ?: error("No active accessibility window")
            val output = mutableListOf<Map<String, Any?>>()
            flatten(root, output, 0)
            root.recycle()
            return GsonBuilder().disableHtmlEscaping().create().toJson(output)
        }

        private fun requireService() =
            instance ?: error("Accessibility service is not enabled on the Xiaomi")

        private fun flatten(
            node: AccessibilityNodeInfo,
            out: MutableList<Map<String, Any?>>,
            depth: Int
        ) {
            if (depth > 20) return
            val rect = android.graphics.Rect()
            node.getBoundsInScreen(rect)
            out += mapOf(
                "depth" to depth,
                "class" to node.className?.toString(),
                "text" to node.text?.toString(),
                "contentDescription" to node.contentDescription?.toString(),
                "resourceId" to node.viewIdResourceName,
                "clickable" to node.isClickable,
                "enabled" to node.isEnabled,
                "scrollable" to node.isScrollable,
                "bounds" to mapOf(
                    "left" to rect.left, "top" to rect.top,
                    "right" to rect.right, "bottom" to rect.bottom
                )
            )
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    flatten(child, out, depth + 1)
                    child.recycle()
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit
    override fun onInterrupt() = Unit

    private fun dispatchTap(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 80))
            .build()
        if (!dispatchGesture(gesture, null, null)) error("Tap dispatch failed")
    }

    private fun dispatchSwipe(x1: Float, y1: Float, x2: Float, y2: Float, durationMs: Int) {
        val path = Path().apply { moveTo(x1, y1); lineTo(x2, y2) }
        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    path, 0, durationMs.coerceIn(50, 5000).toLong()
                )
            )
            .build()
        if (!dispatchGesture(gesture, null, null)) error("Swipe dispatch failed")
    }
}