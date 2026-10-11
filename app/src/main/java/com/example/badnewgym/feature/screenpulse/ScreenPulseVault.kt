package com.example.badnewgym.feature.screenpulse

import java.io.File
import java.io.RandomAccessFile
import java.security.MessageDigest

/**
 * The vault.
 *
 * Lives at `<filesDir>/.screenpulse`, i.e. inside the app sandbox. Android's
 * per-app UID sandbox means no other installed app can list, read, modify or
 * delete these files (they cannot even traverse into the directory), and the
 * leading dot plus `.nomedia` keeps the media scanner from indexing it, so the
 * frames never appear in a gallery, a file manager, or a backup.
 *
 * Every function takes the app's `filesDir` (or any root) rather than a
 * hardcoded path so the whole store is unit testable against a temp directory.
 */
object ScreenPulseVault {

    data class Stats(val frames: Int, val bytes: Long, val oldest: Long, val newest: Long)

    private const val ERASE_CHUNK = 128 * 1024

    fun dir(root: File): File = File(root, ScreenPulseConfig.VAULT_DIR_NAME)

    fun ensure(root: File): File {
        val d = dir(root)
        if (!d.exists()) d.mkdirs()
        val nomedia = File(d, ScreenPulseConfig.NOMEDIA_NAME)
        if (!nomedia.exists()) nomedia.createNewFile()
        return d
    }

    /** Per-install token the laptop tool must echo back before it can ask the
     *  app to purge or synthesise a frame. Blocks random apps on the phone from
     *  driving the vault through the exported broadcast receiver. */
    fun token(root: File): String {
        val d = ensure(root)
        val f = File(d, ScreenPulseConfig.TOKEN_NAME)
        if (!f.exists() || f.length() == 0L) {
            f.writeText(java.util.UUID.randomUUID().toString().replace("-", ""))
        }
        return f.readText().trim()
    }

    /** Frames, oldest first (epoch-ms filenames sort chronologically). */
    fun frames(root: File): List<File> =
        dir(root).listFiles { f ->
            f.isFile &&
                f.name.startsWith(ScreenPulseConfig.FRAME_PREFIX) &&
                f.name.endsWith(ScreenPulseConfig.FRAME_SUFFIX)
        }?.sortedBy { it.name } ?: emptyList()

    /** Write a frame atomically (`.part` then rename) so a pull can never see
     *  a half-written JPEG. */
    fun save(root: File, name: String, payload: ByteArray): File? = try {
        val d = ensure(root)
        val tmp = File(d, "$name.part")
        tmp.outputStream().use { it.write(payload) }
        val dst = File(d, name)
        if (dst.exists()) dst.delete()
        if (tmp.renameTo(dst)) dst else null
    } catch (t: Throwable) {
        null
    }

    /**
     * Drop the oldest frames once the vault exceeds any of the three limits.
     *
     * Written as a single oldest-first walk so age, count and byte caps are all
     * handled in one pass.
     */
    fun prune(
        root: File,
        maxFiles: Int = ScreenPulseConfig.MAX_FILES,
        maxBytes: Long = ScreenPulseConfig.MAX_TOTAL_BYTES,
        maxAgeMs: Long = ScreenPulseConfig.MAX_AGE_DAYS * 86_400_000L
    ): Int {
        val d = dir(root)
        val all = d.listFiles() ?: return 0
        val now = System.currentTimeMillis()
        var bytes = all.filter { it.isFile }.sumOf { it.length() }
        var count = all.count { it.isFile && !it.name.startsWith(".") }

        var removed = 0
        // Oldest first: frames, then stale side-car files.
        val ordered = all.sortedBy { it.name }
        for (f in ordered) {
            if (f.name == ScreenPulseConfig.TOKEN_NAME || f.name == ScreenPulseConfig.NOMEDIA_NAME) continue
            val tooOld = f.name.startsWith(ScreenPulseConfig.FRAME_PREFIX) &&
                (now - f.lastModified()) > maxAgeMs
            val overCount = count > maxFiles
            val overBytes = bytes > maxBytes
            if (!tooOld && !overCount && !overBytes) break
            val size = if (f.isFile) f.length() else 0L
            if (f.delete()) {
                removed++
                bytes -= size
                if (f.name.startsWith(ScreenPulseConfig.FRAME_PREFIX)) count--
            }
        }
        return removed
    }

    /**
     * SHA-256 of every frame, one `hash  name` line each.
     *
     * This is the contract the laptop verifies against: it proves the bytes on
     * the laptop are byte-identical to the bytes that were on the phone.
     */
    fun writeManifest(root: File): File? = try {
        val d = ensure(root)
        val dst = File(d, ScreenPulseConfig.MANIFEST_NAME)
        val tmp = File(d, "${ScreenPulseConfig.MANIFEST_NAME}.part")
        tmp.bufferedWriter().use { w ->
            frames(root).forEach { f ->
                w.write(sha256Hex(f))
                w.write("  ")
                w.write(f.name)
                w.newLine()
            }
        }
        if (dst.exists()) dst.delete()
        if (tmp.renameTo(dst)) dst else null
    } catch (t: Throwable) {
        null
    }

    fun readManifest(root: File): List<Pair<String, String>> {
        val f = File(dir(root), ScreenPulseConfig.MANIFEST_NAME)
        if (!f.exists()) return emptyList()
        return f.readLines().mapNotNull { line ->
            val parts = line.trim().split(Regex("\\s+"))
            if (parts.size >= 2) parts[0] to parts[1] else null
        }
    }

    /** Small status blob so the laptop tool can show state without guessing. */
    fun writeStatus(root: File, running: Boolean, intervalMs: Long, note: String = ""): File? = try {
        val s = stats(root)
        val json = buildString {
            append("{")
            append("\"running\":").append(running).append(',')
            append("\"intervalMs\":").append(intervalMs).append(',')
            append("\"frames\":").append(s.frames).append(',')
            append("\"bytes\":").append(s.bytes).append(',')
            append("\"oldest\":").append(s.oldest).append(',')
            append("\"newest\":").append(s.newest).append(',')
            append("\"note\":\"").append(note.replace("\"", "'")).append('"')
            append("}")
        }
        val d = ensure(root)
        val tmp = File(d, "${ScreenPulseConfig.STATUS_NAME}.part")
        tmp.writeText(json)
        val dst = File(d, ScreenPulseConfig.STATUS_NAME)
        if (dst.exists()) dst.delete()
        if (tmp.renameTo(dst)) dst else null
    } catch (t: Throwable) {
        null
    }

    fun stats(root: File): Stats {
        val f = frames(root)
        return Stats(
            frames = f.size,
            bytes = f.sumOf { it.length() },
            oldest = f.minOfOrNull { it.lastModified() } ?: 0L,
            newest = f.maxOfOrNull { it.lastModified() } ?: 0L
        )
    }

    /**
     * Overwrite every payload with zeros, fsync, then unlink - and only then
     * report success. This is the strongest erasure an unrooted app can do on
     * flash storage; the caller should check the returned count reaches zero.
     *
     * The install token is deliberately kept so a later session can still be
     * verified; pass [keepToken] = false for a full wipe.
     */
    fun purge(root: File, keepToken: Boolean = true): Int {
        val d = dir(root)
        val targets = d.listFiles() ?: return 0
        var erased = 0
        var failed = 0
        for (f in targets) {
            if (keepToken && f.name == ScreenPulseConfig.TOKEN_NAME) continue
            when {
                f.isFile && f.name == ScreenPulseConfig.NOMEDIA_NAME -> {
                    if (f.delete()) erased++
                }
                f.isFile -> if (secureOverwrite(f) && f.delete()) erased++ else failed++
                f.isDirectory -> if (f.deleteRecursively()) erased++ else failed++
            }
        }
        // A purge that silently failed must never look successful.
        return if (failed == 0) erased else -erased - failed
    }

    /** Zero-fill the file's allocated bytes and force them to disk. */
    private fun secureOverwrite(f: File): Boolean {
        return try {
            val len = f.length()
            if (len == 0L) return true
            RandomAccessFile(f, "rw").use { raf ->
                raf.seek(0)
                val chunk = ByteArray(minOf(len.toLong(), ERASE_CHUNK.toLong()).toInt())
                var written = 0L
                while (written < len) {
                    val n = minOf(chunk.size.toLong(), len - written).toInt()
                    raf.write(chunk, 0, n)
                    written += n
                }
                raf.fd.sync()
            }
            true
        } catch (t: Throwable) {
            false
        }
    }

    fun sha256Hex(f: File): String = sha256Hex(f.readBytes())

    fun sha256Hex(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        val sb = StringBuilder(digest.size * 2)
        digest.forEach { b ->
            val v = b.toInt() and 0xFF
            sb.append(Character.forDigit(v ushr 4, 16))
            sb.append(Character.forDigit(v and 0x0F, 16))
        }
        return sb.toString()
    }
}
