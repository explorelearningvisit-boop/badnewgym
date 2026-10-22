package com.example.badnewgym.deviceagent

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.example.badnewgym.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

data class TailscaleNodeStatus(
    val connected: Boolean = false,
    val ip: String = "",
    val ips: List<String>? = emptyList(),
    val hostname: String = "",
    val dnsName: String = "",
    val backendState: String = "Stopped",
    val authUrl: String = "",
    val listenPort: Int = 8787,
    val targetAddr: String = "127.0.0.1:8787",
    val error: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)

object TailscaleManager {
    private const val TAG = "TailscaleManager"
    private const val BINARY_NAME = "libbadgym_tsnet.so"
    private const val CONTROL_PORT = 8788

    private val _statusFlow = MutableStateFlow(TailscaleNodeStatus())
    val statusFlow: StateFlow<TailscaleNodeStatus> = _statusFlow.asStateFlow()

    @Volatile
    private var process: Process? = null

    @Volatile
    private var isRunning = false

    private val executor = Executors.newCachedThreadPool()
    private var monitorJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gson = Gson()

    fun isDaemonRunning(): Boolean = isRunning && process?.isAlive == true

    @Synchronized
    fun start(context: Context) {
        if (isRunning) {
            Log.d(TAG, "TailscaleManager is already running")
            return
        }

        if (!TailscalePrefs.isEnabled(context)) {
            Log.i(TAG, "Embedded Tailscale is disabled in preferences")
            _statusFlow.value = TailscaleNodeStatus(backendState = "Disabled")
            return
        }

        val binaryFile = findBinary(context)
        if (binaryFile == null || !binaryFile.exists()) {
            val errorMsg = "Tailscale native binary not found: $BINARY_NAME"
            Log.e(TAG, errorMsg)
            _statusFlow.value = TailscaleNodeStatus(error = errorMsg, backendState = "BinaryNotFound")
            return
        }

        try {
            binaryFile.setExecutable(true, false)
        } catch (e: Exception) {
            Log.w(TAG, "Could not set executable flag on binary: ${e.message}")
        }

        isRunning = true
        _statusFlow.value = TailscaleNodeStatus(backendState = "Starting")

        executor.execute {
            runProcessLoop(context, binaryFile)
        }

        startStatusMonitor(context)
    }

    @Synchronized
    fun stop() {
        isRunning = false
        monitorJob?.cancel()
        monitorJob = null

        process?.let { p ->
            try {
                p.destroy()
            } catch (e: Exception) {
                Log.w(TAG, "Error destroying process: ${e.message}")
            }
        }
        process = null
        _statusFlow.value = TailscaleNodeStatus(backendState = "Stopped")
        Log.i(TAG, "TailscaleManager stopped")
    }

    @Synchronized
    fun restart(context: Context) {
        stop()
        start(context)
    }

    @Suppress("DEPRECATION")
    fun triggerApp(context: Context) {
        try {
            Log.i(TAG, "Triggering BAD GYM application launch / wake")
            val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            val wakeLock = pm?.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "badgym:tailscale_trigger"
            )
            wakeLock?.acquire(3000)

            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                )
                putExtra("triggered_via_tailscale", true)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to trigger MainActivity: ${e.message}")
        }
    }

    private fun runProcessLoop(context: Context, binaryFile: File) {
        val safeDirContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !context.isDeviceProtectedStorage) {
            try { context.createDeviceProtectedStorageContext() } catch (_: Exception) { context }
        } else {
            context
        }
        val stateDir = File(safeDirContext.filesDir, "tailscale_state").apply { mkdirs() }.absolutePath
        val hostname = TailscalePrefs.getHostname(context)
        val authKey = TailscalePrefs.getAuthKey(context)
        val port = TailscalePrefs.getPort(context)
        val target = "127.0.0.1:8787"
        val triggerUrl = "http://127.0.0.1:8787/health"

        while (isRunning) {
            val cmd = mutableListOf(
                binaryFile.absolutePath,
                "-dir", stateDir,
                "-hostname", hostname,
                "-listen", port.toString(),
                "-target", target,
                "-control-port", CONTROL_PORT.toString(),
                "-trigger-url", triggerUrl
            )
            if (authKey.isNotBlank()) {
                cmd.add("-authkey")
                cmd.add(authKey)
            }

            Log.i(TAG, "Starting tsnet daemon: ${cmd.joinToString(" ")}")
            try {
                val pb = ProcessBuilder(cmd)
                pb.directory(context.filesDir)
                pb.redirectErrorStream(true)
                val p = pb.start()
                process = p

                // Read output stream
                val reader = BufferedReader(InputStreamReader(p.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val logLine = line ?: break
                    Log.d(TAG, "[tsnet] $logLine")
                    if (logLine.contains("https://login.tailscale.com/")) {
                        val url = logLine.substringAfter("https://login.tailscale.com/")
                        val fullUrl = "https://login.tailscale.com/$url".split(" ")[0].trim()
                        _statusFlow.value = _statusFlow.value.copy(
                            backendState = "NeedsLogin",
                            authUrl = fullUrl
                        )
                    }
                }

                val exitCode = p.waitFor()
                Log.w(TAG, "tsnet process exited with code $exitCode")
            } catch (e: Exception) {
                Log.e(TAG, "Error executing tsnet daemon: ${e.message}", e)
                _statusFlow.value = _statusFlow.value.copy(
                    error = e.message,
                    backendState = "Error"
                )
            }

            if (isRunning) {
                Log.i(TAG, "tsnet exited while isRunning=true; restarting in 3 seconds...")
                try {
                    Thread.sleep(3000)
                } catch (_: InterruptedException) {
                    break
                }
            }
        }
    }

    private fun startStatusMonitor(context: Context) {
        monitorJob?.cancel()
        monitorJob = scope.launch {
            while (isActive && isRunning) {
                delay(2500)
                try {
                    val status = fetchControlStatus()
                    if (status != null) {
                        _statusFlow.value = status
                    }
                } catch (e: Exception) {
                    Log.v(TAG, "Status fetch tick: ${e.message}")
                }
            }
        }
    }

    private fun fetchControlStatus(): TailscaleNodeStatus? {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL("http://127.0.0.1:$CONTROL_PORT/status")
            conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 1500
                readTimeout = 1500
                requestMethod = "GET"
            }
            if (conn.responseCode == 200) {
                val json = conn.inputStream.bufferedReader().use { it.readText() }
                val parsed = gson.fromJson(json, TailscaleNodeStatus::class.java)
                parsed?.copy(
                    ips = parsed.ips ?: emptyList(),
                    ip = parsed.ip ?: "",
                    hostname = parsed.hostname ?: "",
                    dnsName = parsed.dnsName ?: "",
                    backendState = parsed.backendState ?: "Stopped",
                    authUrl = parsed.authUrl ?: "",
                    targetAddr = parsed.targetAddr ?: "127.0.0.1:8787"
                )
            } else {
                null
            }
        } catch (_: Exception) {
            null
        } finally {
            conn?.disconnect()
        }
    }

    private fun findBinary(context: Context): File? {
        // 1. Primary location: nativeLibraryDir extracted by Android PackageManager
        val nativeDir = File(context.applicationInfo.nativeLibraryDir)
        val fileInNativeDir = File(nativeDir, BINARY_NAME)
        if (fileInNativeDir.exists() && fileInNativeDir.canExecute()) {
            return fileInNativeDir
        }

        // 2. Secondary fallback: filesDir
        val fileInFilesDir = File(context.filesDir, BINARY_NAME)
        if (fileInFilesDir.exists()) {
            try { fileInFilesDir.setExecutable(true, false) } catch (_: Exception) {}
            return fileInFilesDir
        }

        // 3. Auto-extract from APK if not yet extracted by Android
        try {
            val apk = java.util.zip.ZipFile(context.applicationInfo.sourceDir)
            val abis = android.os.Build.SUPPORTED_ABIS
            var entry: java.util.zip.ZipEntry? = null
            for (abi in abis) {
                entry = apk.getEntry("lib/$abi/$BINARY_NAME")
                if (entry != null) break
            }
            if (entry == null) {
                entry = apk.getEntry("lib/arm64-v8a/$BINARY_NAME")
            }
            if (entry != null) {
                apk.getInputStream(entry).use { input ->
                    fileInFilesDir.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                fileInFilesDir.setExecutable(true, false)
                Log.i(TAG, "Auto-extracted $BINARY_NAME from APK (${fileInFilesDir.length()} bytes)")
                return fileInFilesDir
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to auto-extract binary from APK: ${e.message}")
        }

        return if (fileInNativeDir.exists()) fileInNativeDir else fileInFilesDir
    }
}
