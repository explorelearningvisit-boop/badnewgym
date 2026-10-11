package com.example.badnewgym.feature.screenpulse

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

/**
 * Frame encoder.
 *
 * The spec asks for ~15-20KB per frame. JPEG size is not directly controllable,
 * so we binary-search the highest quality that still fits the byte ceiling and
 * accept it even when it lands under the soft floor (a plain screen simply does
 * not need more bits). If an unusually noisy screen still overshoots, the image
 * is scaled down and the search repeats.
 *
 * Pure decision logic lives in [qualityFor] so it can be unit tested without an
 * Android device.
 */
object ScreenPulseEncoder {

    private const val MIN_QUALITY = 15
    private const val MAX_QUALITY = 92
    private const val MAX_PASSES = 3
    private const val RESCALE_FACTOR = 0.72f

    /** Highest quality whose encoded size is <= [targetBytes]. */
    fun qualityFor(sizeAt: (Int) -> Int, targetBytes: Int): Int {
        var lo = MIN_QUALITY
        var hi = MAX_QUALITY
        var best = MIN_QUALITY
        var guard = 0
        while (lo <= hi && guard++ < 32) {
            val mid = (lo + hi) / 2
            if (sizeAt(mid) <= targetBytes) {
                best = mid
                lo = mid + 1
            } else {
                hi = mid - 1
            }
        }
        return best
    }

    /**
     * @return JPEG bytes, or null when the bitmap could not be encoded.
     */
    fun encode(
        source: Bitmap,
        targetBytes: Int = ScreenPulseConfig.TARGET_BYTES,
        maxBytes: Int = ScreenPulseConfig.MAX_FRAME_BYTES
    ): ByteArray? {
        var current = source
        var ownsCurrent = false
        try {
            repeat(MAX_PASSES) { pass ->
                val quality = qualityFor({ q -> compress(current, q).size }, targetBytes)
                val out = compress(current, quality)
                if (out.size <= maxBytes || pass == MAX_PASSES - 1) return out
                val scaled = scaleDown(current, RESCALE_FACTOR) ?: return out
                if (ownsCurrent) current.recycle()
                current = scaled
                ownsCurrent = true
            }
            return null
        } finally {
            if (ownsCurrent && current !== source) current.recycle()
        }
    }

    /** Scale so [BASE_WIDTH] maps onto a canvas readable but cheap. */
    fun prepare(source: Bitmap): Bitmap {
        if (source.width <= ScreenPulseConfig.BASE_WIDTH) return source
        val height =
            (source.height.toFloat() * (ScreenPulseConfig.BASE_WIDTH.toFloat() / source.width)).toInt()
                .coerceAtLeast(1)
        return Bitmap.createScaledBitmap(source, ScreenPulseConfig.BASE_WIDTH, height, true)
    }

    private fun compress(bmp: Bitmap, quality: Int): ByteArray {
        val out = ByteArrayOutputStream(16 * 1024)
        bmp.compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(MIN_QUALITY, MAX_QUALITY), out)
        return out.toByteArray()
    }

    private fun scaleDown(bmp: Bitmap, factor: Float): Bitmap? = try {
        val w = (bmp.width * factor).toInt().coerceAtLeast(64)
        val h = (bmp.height * factor).toInt().coerceAtLeast(64)
        Bitmap.createScaledBitmap(bmp, w, h, true)
    } catch (t: Throwable) {
        null
    }
}
