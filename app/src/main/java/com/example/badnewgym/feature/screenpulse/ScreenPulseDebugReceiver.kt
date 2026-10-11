package com.example.badnewgym.feature.screenpulse

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.format.DateFormat
import java.io.File

/**
 * Laptop-facing control surface.
 *
 * The laptop tool drives the vault through these four broadcasts instead of
 * trying to reach into the sandbox directly, so erasure and manifest
 * generation are always performed by the app that owns the files.
 *
 * Anything that mutates the vault requires the per-install token, which the
 * tool can only read back out of the sandbox over `adb run-as`. That keeps
 * other apps on the phone from driving the vault.
 */
class ScreenPulseDebugReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val root = context.filesDir
        val vault = ScreenPulseVault

        when (intent.action) {
            ACTION_STATUS -> {
                val s = vault.stats(root)
                vault.writeStatus(
                    root,
                    ScreenPulseCaptureService.isRunning(),
                    ScreenPulseConfig.INTERVAL_MS,
                    "queried"
                )
                finish(root, "frames=${s.frames},bytes=${s.bytes},running=${ScreenPulseCaptureService.isRunning()}")
            }

            ACTION_VERIFY -> {
                val manifest = vault.writeManifest(root)
                val s = vault.stats(root)
                finish(
                    root,
                    if (manifest != null) "manifest=ok,frames=${s.frames},bytes=${s.bytes}"
                    else "manifest=failed"
                )
            }

            ACTION_STOP -> if (requireToken(context, intent)) {
                ScreenPulseCaptureService.stop(context)
                vault.writeStatus(root, false, ScreenPulseConfig.INTERVAL_MS, "stopped_via_adb")
                finish(root, "stopped")
            } else finish(root, "denied")

            ACTION_SNAPSHOT -> if (requireToken(context, intent)) {
                val bytes = synthesiseFrame()
                val name = ScreenPulseConfig.frameName(System.currentTimeMillis(), "test")
                val saved = if (bytes != null) vault.save(root, name, bytes) else null
                vault.writeManifest(root)
                if (saved != null) {
                    finish(root, "saved=${saved.name},bytes=${saved.length()}")
                } else {
                    finish(root, "saved=nothing")
                }
            } else finish(root, "denied")

            ACTION_PURGE -> if (requireToken(context, intent)) {
                // Stop first: purging under an active loop would race with new frames.
                ScreenPulseCaptureService.stop(context)
                val erased = vault.purge(root)
                val remaining = vault.stats(root).frames
                vault.writeStatus(root, false, ScreenPulseConfig.INTERVAL_MS, "purged")
                finish(root, if (erased >= 0 && remaining == 0) "erased=$erased,remaining=0" else "erased=$erased,remaining=$remaining")
            } else finish(root, "denied")

            else -> finish(root, "unknown_action")
        }
    }

    private fun requireToken(context: Context, intent: Intent): Boolean {
        val provided = intent.getStringExtra(EXTRA_TOKEN) ?: return false
        val expected = try {
            ScreenPulseVault.token(context.filesDir)
        } catch (t: Throwable) {
            return false
        }
        return expected.isNotEmpty() && expected == provided.trim()
    }

    /** A deterministic frame so the whole pipeline can be proven without
     *  granting screen-capture consent (which needs a real human tap). */
    private fun synthesiseFrame(): ByteArray? {
        val w = 720
        val h = 1600
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.parseColor("#0F172A"))
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = LinearGradient(
            0f, 0f, 0f, h.toFloat(),
            Color.parseColor("#10B981"), Color.parseColor("#0EA5E9"),
            Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, w.toFloat(), h * 0.45f, paint)
        paint.shader = null
        paint.color = Color.WHITE
        paint.textSize = 46f
        canvas.drawText("ScreenPulse", 48f, 190f, paint)
        paint.textSize = 28f
        canvas.drawText(
            DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString(),
            48f, 250f, paint
        )
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 24f
        canvas.drawText("synthetic frame - pipeline self test", 48f, 300f, paint)
        val prepared = ScreenPulseEncoder.prepare(bmp)
        val out = ScreenPulseEncoder.encode(prepared)
        if (prepared !== bmp) prepared.recycle()
        bmp.recycle()
        return out
    }

    private fun finish(root: File, data: String) {
        setResult(Activity.RESULT_OK, data, null)
        // `am broadcast` is delivered unordered, and unordered result extras
        // never travel back to the shell - so the verdict is also left in the
        // vault, where the laptop tool reads it over run-as. Timestamped so a
        // hurried second command can never mistake it for a fresh answer.
        try {
            val d = ScreenPulseVault.ensure(root)
            val tmp = File(d, "last_result.txt.part")
            tmp.writeText("${System.currentTimeMillis()} $data")
            val dst = File(d, "last_result.txt")
            if (dst.exists()) dst.delete()
            tmp.renameTo(dst)
        } catch (t: Throwable) {
        }
    }

    companion object {
        const val ACTION_STATUS = "com.example.badnewgym.SCREENPULSE.STATUS"
        const val ACTION_VERIFY = "com.example.badnewgym.SCREENPULSE.VERIFY"
        const val ACTION_SNAPSHOT = "com.example.badnewgym.SCREENPULSE.SNAPSHOT"
        const val ACTION_PURGE = "com.example.badnewgym.SCREENPULSE.PURGE"
        const val ACTION_STOP = "com.example.badnewgym.SCREENPULSE.STOP"
        const val EXTRA_TOKEN = "token"
    }
}
