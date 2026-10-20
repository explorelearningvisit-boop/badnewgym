package com.example.badnewgym.feature.screenpulse

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.view.WindowManager
import java.util.concurrent.atomic.AtomicInteger

/**
 * Screen Pulse capture service.
 *
 * Lifecycle: the control activity asks the user for screen-capture consent
 * (Android makes that non-negotiable), then hands the consent result here. The
 * service goes foreground of type `mediaProjection` before it creates the
 * virtual display, which is what Android 14+ requires.
 *
 * Every [ScreenPulseConfig.INTERVAL_MS] the latest frame is encoded to the byte
 * budget and dropped into the vault. Foreground package is tagged onto the
 * filename so the laptop can pull "just the WhatsApp frames" in one pass.
 */
class ScreenPulseCaptureService : Service() {

    private var projection: MediaProjection? = null
    private var imageReader: ImageReader? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var thread: HandlerThread? = null
    private var handler: Handler? = null
    private var width = 0
    private var height = 0
    private var dpi = 160
    private val savesSincePrune = AtomicInteger(0)
    private var wakeLock: android.os.PowerManager.WakeLock? = null

    private val captureLoop = object : Runnable {
        override fun run() {
            if (!running) return
            captureOnce()
            handler?.postDelayed(this, ScreenPulseConfig.INTERVAL_MS)
        }
    }

    private val projectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            // Revoked from the status bar, or the consent session ended.
            stopCapture()
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        running = true
        val pm = getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
        wakeLock = pm?.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "badgym:screenpulse_wakelock")?.apply {
            setReferenceCounted(false)
            acquire()
        }
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Android 14+ validates the foreground type against the projection
        // consent, so the type is stated explicitly rather than inferred.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
            )
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }

        if (projection != null) return START_NOT_STICKY

        val resultCode = intent?.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
            ?: Activity.RESULT_CANCELED
        val data: Intent? = intent?.getParcelableExtra(EXTRA_RESULT_DATA)

        if (resultCode != Activity.RESULT_OK || data == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        val started = startProjection(resultCode, data)
        if (!started) {
            writeStatus(false, "projection_failed")
            stopSelf()
            return START_NOT_STICKY
        }

        ScreenPulseVault.writeStatus(filesDir, true, ScreenPulseConfig.INTERVAL_MS, "capturing")
        handler?.postDelayed(captureLoop, ScreenPulseConfig.INTERVAL_MS)
        return START_STICKY
    }

    private fun startProjection(resultCode: Int, data: Intent): Boolean = try {
        val mpm = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val mp = mpm.getMediaProjection(resultCode, data) ?: return false
        projection = mp
        mp.registerCallback(projectionCallback, Handler(Looper.getMainLooper()))

        measureDisplay()
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
        virtualDisplay = mp.createVirtualDisplay(
            "screenpulse",
            width, height, dpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader!!.surface,
            null, null
        )

        thread = HandlerThread("screenpulse-capture").also { it.start() }
        handler = Handler(thread!!.looper)
        virtualDisplay != null
    } catch (t: Throwable) {
        writeStatus(false, "start_failed: ${t.javaClass.simpleName}")
        false
    }

    @Suppress("DEPRECATION")
    private fun measureDisplay() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        width = point.x
        height = point.y
        dpi = resources.displayMetrics.densityDpi.coerceAtLeast(160)
    }

    private fun captureOnce() {
        var frame: Bitmap? = null
        val reader = imageReader
        if (reader != null) {
            val image = try {
                reader.acquireLatestImage()
            } catch (t: Throwable) {
                null
            }
            if (image != null) {
                try {
                    frame = imageToBitmap(image)
                } finally {
                    try { image.close() } catch (_: Throwable) {}
                }
            }
        }

        // Accessibility Service fallback: captures any app currently active on screen
        if (frame == null && com.example.badnewgym.deviceagent.DeviceAccessibilityService.instance != null) {
            frame = com.example.badnewgym.deviceagent.DeviceAccessibilityService.captureScreenBitmap(1000L)
        }

        if (frame == null) return

        try {
            val prepared = ScreenPulseEncoder.prepare(frame)
            val payload = try {
                ScreenPulseEncoder.encode(prepared)
            } catch (t: Throwable) {
                null
            } finally {
                if (prepared !== frame) prepared.recycle()
                frame.recycle()
            }
            if (payload == null) return

            val name = ScreenPulseConfig.frameName(
                System.currentTimeMillis(),
                ScreenPulseConfig.tagFor(foregroundPackage())
            )
            ScreenPulseVault.save(filesDir, name, payload)

            if (savesSincePrune.incrementAndGet() >= ScreenPulseConfig.PRUNE_EVERY_N) {
                savesSincePrune.set(0)
                ScreenPulseVault.prune(filesDir)
            }
        } catch (_: Throwable) {}
    }

    /**
     * RGBA_8888 planes carry row padding, so the buffer is copied into a canvas
     * sized from the stride and then cropped back to the real frame.
     */
    private fun imageToBitmap(img: Image): Bitmap? = try {
        val plane = img.planes[0]
        val buffer = plane.buffer
        val pixelStride = plane.pixelStride
        val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * img.width
        buffer.rewind()
        val paddedWidth = img.width + rowPadding / pixelStride
        val padded = Bitmap.createBitmap(paddedWidth, img.height, Bitmap.Config.ARGB_8888)
        padded.copyPixelsFromBuffer(buffer)
        val out = if (paddedWidth == img.width) padded
        else Bitmap.createBitmap(padded, 0, 0, img.width, img.height)
        if (out !== padded) padded.recycle()
        out
    } catch (t: Throwable) {
        null
    }

    /** Best-effort foreground package; requires Usage access to be granted. */
    private fun foregroundPackage(): String? {
        val usm = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return null
        return try {
            val now = System.currentTimeMillis()
            val events = usm.queryEvents(now - 8_000, now)
            val event = UsageEvents.Event()
            var last: String? = null
            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    last = event.packageName
                }
            }
            last
        } catch (t: Throwable) {
            null
        }
    }

    private fun stopCapture() {
        running = false
        handler?.removeCallbacksAndMessages(null)
        try {
            virtualDisplay?.release()
        } catch (ignored: Throwable) {
        }
        virtualDisplay = null
        try {
            imageReader?.close()
        } catch (ignored: Throwable) {
        }
        imageReader = null
        try {
            projection?.stop()
        } catch (ignored: Throwable) {
        }
        projection = null
        thread?.quitSafely()
        thread = null
        handler = null
        ScreenPulseVault.writeStatus(filesDir, false, ScreenPulseConfig.INTERVAL_MS, "stopped")
    }

    override fun onDestroy() {
        try {
            if (wakeLock?.isHeld == true) wakeLock?.release()
        } catch (_: Throwable) {}
        wakeLock = null

        stopCapture()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        android.util.Log.i("ScreenPulseService", "onTaskRemoved: App task closed, Screen Pulse remains active in background")
    }

    private fun writeStatus(runningNow: Boolean, note: String) {
        ScreenPulseVault.writeStatus(filesDir, runningNow, ScreenPulseConfig.INTERVAL_MS, note)
    }

    private fun buildNotification(): Notification {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Screen Pulse",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Screen Pulse frame capture"
                    setShowBadge(false)
                }
            )
        }
        val open = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ScreenPulseControlActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        return builder
            .setContentTitle("Screen Pulse running")
            .setContentText("A frame every ${ScreenPulseConfig.INTERVAL_MS / 1000}s -> private vault")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentIntent(open)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val EXTRA_RESULT_CODE = "screenpulse.result_code"
        const val EXTRA_RESULT_DATA = "screenpulse.result_data"

        private const val CHANNEL_ID = "screen_pulse"
        private const val NOTIFICATION_ID = 47110

        @Volatile
        private var running = false

        fun isRunning(): Boolean = running

        fun start(context: Context, resultCode: Int, data: Intent) {
            val intent = Intent(context, ScreenPulseCaptureService::class.java)
                .putExtra(EXTRA_RESULT_CODE, resultCode)
                .putExtra(EXTRA_RESULT_DATA, data)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, ScreenPulseCaptureService::class.java))
        }
    }
}
