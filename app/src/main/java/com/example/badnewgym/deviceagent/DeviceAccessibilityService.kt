package com.example.badnewgym.deviceagent

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import android.content.Context
import android.os.SystemClock

class DeviceAccessibilityService : AccessibilityService() {
    companion object {
        @Volatile var instance: DeviceAccessibilityService? = null

        private const val MIN_COOLDOWN_MS = 380L
        private val captureLock = Any()
        private val captureExecutor = Executors.newSingleThreadExecutor()

        @Volatile private var cachedBytes: ByteArray? = null
        @Volatile private var cachedBytesTimestamp: Long = 0L

        @Volatile private var cachedBitmap: Bitmap? = null
        @Volatile private var cachedBitmapTimestamp: Long = 0L

        fun getLastFrame(): ByteArray? = cachedBytes

        fun ensureEnabled(context: Context): Boolean {
            if (instance != null) return true
            return try {
                val cr = context.contentResolver
                val expected = "${context.packageName}/${DeviceAccessibilityService::class.java.name}"
                val current = android.provider.Settings.Secure.getString(cr, android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: ""
                if (!current.contains(expected)) {
                    val updated = if (current.isEmpty()) expected else "$current:$expected"
                    android.provider.Settings.Secure.putString(cr, android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, updated)
                    android.provider.Settings.Secure.putInt(cr, android.provider.Settings.Secure.ACCESSIBILITY_ENABLED, 1)
                    Log.i("DeviceAccessibility", "Auto-enabled accessibility service via Secure Settings")
                }
                true
            } catch (e: Exception) {
                Log.w("DeviceAccessibility", "Could not auto-enable accessibility: ${e.message}")
                false
            }
        }

        fun captureScreenBitmap(timeoutMs: Long = 1800L): Bitmap? {
            val service = instance ?: return cachedBitmap
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return null

            synchronized(captureLock) {
                val now = SystemClock.uptimeMillis()
                val elapsed = now - cachedBitmapTimestamp
                if (elapsed < MIN_COOLDOWN_MS && cachedBitmap != null) {
                    return cachedBitmap
                }

                if (elapsed < 350L) {
                    try {
                        Thread.sleep(350L - elapsed)
                    } catch (_: InterruptedException) {}
                }

                var attempts = 0
                var captured: Bitmap? = null

                while (attempts < 2 && captured == null) {
                    attempts++
                    val latch = CountDownLatch(1)
                    var errorReceived = 0

                    try {
                        service.takeScreenshot(
                            Display.DEFAULT_DISPLAY,
                            captureExecutor,
                            object : TakeScreenshotCallback {
                                override fun onSuccess(screenshotResult: ScreenshotResult) {
                                    try {
                                        val buffer = screenshotResult.hardwareBuffer
                                        val colorSpace = screenshotResult.colorSpace
                                        val hwBitmap = Bitmap.wrapHardwareBuffer(buffer, colorSpace)
                                        if (hwBitmap != null) {
                                            val soft = try {
                                                hwBitmap.copy(Bitmap.Config.ARGB_8888, false)
                                            } catch (_: Throwable) { null }
                                            captured = soft ?: hwBitmap
                                        }
                                        buffer.close()
                                    } catch (e: Throwable) {
                                        Log.e("DeviceAccessibility", "Error processing screenshot bitmap", e)
                                    } finally {
                                        latch.countDown()
                                    }
                                }

                                override fun onFailure(errorCode: Int) {
                                    errorReceived = errorCode
                                    Log.w("DeviceAccessibility", "takeScreenshot failed with code $errorCode")
                                    latch.countDown()
                                }
                            }
                        )
                        latch.await(timeoutMs, TimeUnit.MILLISECONDS)
                    } catch (e: Throwable) {
                        Log.e("DeviceAccessibility", "captureScreenBitmap error", e)
                    }

                    if (captured != null) {
                        cachedBitmap = captured
                        cachedBitmapTimestamp = SystemClock.uptimeMillis()
                        return captured
                    }

                    if (errorReceived == 3 && attempts < 2) {
                        try {
                            Thread.sleep(350L)
                        } catch (_: InterruptedException) {}
                    }
                }

                return cachedBitmap
            }
        }

        fun captureScreen(timeoutMs: Long = 1800L): ByteArray? {
            val service = instance ?: return cachedBytes
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return null

            synchronized(captureLock) {
                val now = SystemClock.uptimeMillis()
                val elapsed = now - cachedBytesTimestamp
                // If a frame was captured very recently, return it immediately to obey system rate-limit
                if (elapsed < MIN_COOLDOWN_MS && cachedBytes != null) {
                    return cachedBytes
                }

                // If we need to wait a bit to safely pass the 333ms cooldown, wait it out
                if (elapsed < 350L) {
                    try {
                        Thread.sleep(350L - elapsed)
                    } catch (_: InterruptedException) {}
                }

                var attempts = 0
                var captured: ByteArray? = null

                while (attempts < 2 && captured == null) {
                    attempts++
                    val latch = CountDownLatch(1)
                    var errorReceived = 0

                    try {
                        service.takeScreenshot(
                            Display.DEFAULT_DISPLAY,
                            captureExecutor,
                            object : TakeScreenshotCallback {
                                override fun onSuccess(screenshotResult: ScreenshotResult) {
                                    try {
                                        val buffer = screenshotResult.hardwareBuffer
                                        val colorSpace = screenshotResult.colorSpace
                                        val hwBitmap = Bitmap.wrapHardwareBuffer(buffer, colorSpace)
                                        if (hwBitmap != null) {
                                            val soft = try {
                                                hwBitmap.copy(Bitmap.Config.ARGB_8888, false)
                                            } catch (_: Throwable) { null }
                                            val target = soft ?: hwBitmap
                                            val stream = ByteArrayOutputStream()
                                            if (target.compress(Bitmap.CompressFormat.JPEG, 70, stream)) {
                                                captured = stream.toByteArray()
                                            }
                                            if (soft != null && soft !== hwBitmap) soft.recycle()
                                            hwBitmap.recycle()
                                        }
                                        buffer.close()
                                    } catch (e: Throwable) {
                                        Log.e("DeviceAccessibility", "Error converting hardware buffer to JPEG", e)
                                    } finally {
                                        latch.countDown()
                                    }
                                }

                                override fun onFailure(errorCode: Int) {
                                    errorReceived = errorCode
                                    Log.w("DeviceAccessibility", "takeScreenshot failed with code $errorCode")
                                    latch.countDown()
                                }
                            }
                        )
                        latch.await(timeoutMs, TimeUnit.MILLISECONDS)
                    } catch (e: Throwable) {
                        Log.e("DeviceAccessibility", "captureScreen error", e)
                    }

                    if (captured != null) {
                        cachedBytes = captured
                        cachedBytesTimestamp = SystemClock.uptimeMillis()
                        return captured
                    }

                    if (errorReceived == 3 && attempts < 2) {
                        try {
                            Thread.sleep(350L)
                        } catch (_: InterruptedException) {}
                    }
                }

                return cachedBytes
            }
        }

        fun tap(x: Int, y: Int) = requireService().dispatchTap(x.toFloat(), y.toFloat())
        fun swipe(x1: Int, y1: Int, x2: Int, y2: Int, durationMs: Int) =
            requireService().dispatchSwipe(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), durationMs)

        fun back() { requireService().performGlobalAction(GLOBAL_ACTION_BACK) }
        fun home() { requireService().performGlobalAction(GLOBAL_ACTION_HOME) }
        fun recents() { requireService().performGlobalAction(GLOBAL_ACTION_RECENTS) }

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
        Log.i("DeviceAccessibility", "onServiceConnected: Accessibility service bound by Android system")
        // Guarantee that MCP Server and background services are running
        BackgroundKeepAliveManager.startAllBackgroundServices(this)
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // If the MCP server was killed or dormant, the first system UI event revives it immediately
        if (DeviceAgentRuntime.server == null) {
            BackgroundKeepAliveManager.startAllBackgroundServices(this)
        }
    }

    override fun onInterrupt() = Unit

    private fun dispatchTap(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 80))
            .build()
        val dispatched = try {
            dispatchGesture(gesture, null, null)
        } catch (e: Exception) {
            Log.w("DeviceAccessibility", "dispatchGesture exception: ${e.message}")
            false
        }
        if (!dispatched) {
            Log.i("DeviceAccessibility", "dispatchGesture returned false, falling back to input tap $x $y")
            try {
                Runtime.getRuntime().exec(arrayOf("input", "tap", x.toInt().toString(), y.toInt().toString()))
            } catch (e: Exception) {
                Log.e("DeviceAccessibility", "Fallback input tap failed", e)
            }
        }
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
        val dispatched = try {
            dispatchGesture(gesture, null, null)
        } catch (e: Exception) {
            Log.w("DeviceAccessibility", "dispatchGesture exception: ${e.message}")
            false
        }
        if (!dispatched) {
            Log.i("DeviceAccessibility", "dispatchGesture returned false, falling back to input swipe")
            try {
                Runtime.getRuntime().exec(arrayOf("input", "swipe", x1.toInt().toString(), y1.toInt().toString(), x2.toInt().toString(), y2.toInt().toString(), durationMs.toString()))
            } catch (e: Exception) {
                Log.e("DeviceAccessibility", "Fallback input swipe failed", e)
            }
        }
    }
}