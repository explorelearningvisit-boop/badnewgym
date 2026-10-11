package com.example.badnewgym.feature.screenpulse

/**
 * Screen Pulse - periodic screen frame capture for the developer's own device.
 *
 * Frames are written into the app sandbox only. Nothing here talks to the
 * network: the only way off the phone is an explicit ADB pull from a machine
 * that is physically (or VPN) attached, which keeps the vault offline and
 * private by construction.
 */
object ScreenPulseConfig {

    /** Cadence: one frame every two seconds, as requested. */
    const val INTERVAL_MS = 2_000L

    /** Byte budget for a single frame. The encoder hunts for the highest JPEG
     *  quality that still lands inside [MAX_FRAME_BYTES], preferring [TARGET_BYTES]. */
    const val TARGET_BYTES = 18 * 1024
    const val MIN_FRAME_BYTES = 15 * 1024
    const val MAX_FRAME_BYTES = 20 * 1024

    /** Width the frame is downscaled to before encoding. 640px keeps chat text
     *  legible on a laptop while staying well inside the byte budget. */
    const val BASE_WIDTH = 640

    /** Vault retention so a forgotten capture loop can never fill the phone. */
    const val MAX_FILES = 4_000
    const val MAX_TOTAL_BYTES = 160L * 1024 * 1024
    const val MAX_AGE_DAYS = 7L

    /** Prune cadence - every N saved frames rather than on every frame. */
    const val PRUNE_EVERY_N = 50

    /** Foreground packages that get a short, stable filename tag so the laptop
     *  side can filter "WhatsApp frames" in one pass. */
    const val TAG_WHATSAPP = "wa"
    const val TAG_WHATSAPP_BUSINESS = "wab"
    const val TAG_UNKNOWN = "unk"

    const val VAULT_DIR_NAME = ".screenpulse"
    const val FRAME_PREFIX = "sp_"
    const val FRAME_SUFFIX = ".jpg"
    const val MANIFEST_NAME = "manifest.sha256"
    const val STATUS_NAME = "status.json"
    const val TOKEN_NAME = ".token"
    const val NOMEDIA_NAME = ".nomedia"

    /** Filename: sp_<13 digit epoch ms>_<tag>.jpg - lexicographically sorted
     *  names are therefore oldest first, which makes pruning trivial. */
    fun frameName(epochMillis: Long, tag: String): String =
        "$FRAME_PREFIX${epochMillis.toString().padStart(13, '0')}_${sanitize(tag)}$FRAME_SUFFIX"

    fun sanitize(tag: String): String =
        tag.lowercase().replace(Regex("[^a-z0-9_]"), "").take(16).ifEmpty { TAG_UNKNOWN }

    /** Short, stable tag for a package name. */
    fun tagFor(packageName: String?): String = when {
        packageName.isNullOrBlank() -> TAG_UNKNOWN
        packageName == "com.whatsapp" -> TAG_WHATSAPP
        packageName == "com.whatsapp.w4b" -> TAG_WHATSAPP_BUSINESS
        packageName.startsWith("com.whatsapp") -> TAG_WHATSAPP
        else -> sanitize(packageName.substringAfterLast('.'))
    }

    fun isWhatsappFrame(fileName: String): Boolean =
        fileName.endsWith("_$TAG_WHATSAPP$FRAME_SUFFIX") ||
            fileName.endsWith("_$TAG_WHATSAPP_BUSINESS$FRAME_SUFFIX")
}
