package com.example.badnewgym.deviceagent

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object DeviceAgentRuntime {
    private val main = Handler(Looper.getMainLooper())
    @Volatile var activity: androidx.activity.ComponentActivity? = null
    @Volatile var server: RemoteDeviceMcpServer? = null

    fun attach(activity: androidx.activity.ComponentActivity) { this.activity = activity }
    fun detach(activity: androidx.activity.ComponentActivity) { if (this.activity === activity) this.activity = null }

    fun screenshotPng(context: android.content.Context? = null): ByteArray {
        // 0. Auto-ensure accessibility is enabled if context provided
        if (context != null && DeviceAccessibilityService.instance == null) {
            DeviceAccessibilityService.ensureEnabled(context)
        }

        // 1. Accessibility Service real-time hardware screen capture: works across ANY app currently on screen!
        if (DeviceAccessibilityService.instance != null) {
            try {
                val bytes = DeviceAccessibilityService.captureScreen(timeoutMs = 1800)
                if (bytes != null && bytes.isNotEmpty()) {
                    return bytes
                }
            } catch (e: Exception) {
                android.util.Log.w("DeviceAgent", "Accessibility capture error: ${e.message}")
            }
        }

        // 2. Active activity window decorView (STRICTLY ONLY if Bad Gym is the active, focused, resumed foreground window!)
        val a = activity
        if (a != null && a.hasWindowFocus() && a.lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)) {
            var result: ByteArray? = null
            val latch = java.util.concurrent.CountDownLatch(1)
            main.post {
                try {
                    val view = a.window.decorView
                    if (view.width > 0 && view.height > 0) {
                        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                        view.draw(Canvas(bitmap))
                        val stream = java.io.ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        bitmap.recycle()
                        result = stream.toByteArray()
                    }
                } catch (_: Exception) {}
                finally { latch.countDown() }
            }
            if (latch.await(600, TimeUnit.MILLISECONDS) && result != null && result!!.isNotEmpty()) {
                return result!!
            }
        }

        // 3. Fallback: Screen Pulse Vault latest frame (captured via MediaProjection in background)
        if (context != null) {
            try {
                val frames = com.example.badnewgym.feature.screenpulse.ScreenPulseVault.frames(context.filesDir)
                val latest = frames.lastOrNull()
                if (latest != null && latest.length() > 0) {
                    val bytes = latest.readBytes()
                    if (bytes.isNotEmpty()) return bytes
                }
            } catch (_: Exception) {}
        }

        // 4. Fallback: Return last known accessibility frame
        val lastFrame = DeviceAccessibilityService.getLastFrame()
        if (lastFrame != null && lastFrame.isNotEmpty()) {
            return lastFrame
        }

        error("Screenshot failed: Accessibility Service not ready and BAD GYM is in background")
    }

    fun onMain(block: () -> Unit) = main.post(block)
    fun uiToast(message: String) = onMain {
        activity?.let { android.widget.Toast.makeText(it, message, android.widget.Toast.LENGTH_SHORT).show() }
    }
}

class RemoteDeviceMcpServer(
    private val context: android.content.Context,
    private val port: Int = 8787,
    private val token: String = DeviceAgentToken.get(context),
) {
    private val executor = Executors.newCachedThreadPool()
    @Volatile private var running = false
    private var serverSocket: ServerSocket? = null
    private val gson = Gson()

    fun start() {
        if (running) return
        running = true
        executor.execute {
            try {
                serverSocket = ServerSocket(port)
                while (running) {
                    val socket = serverSocket?.accept() ?: break
                    executor.execute {
                        try {
                            handle(socket)
                        } catch (t: Throwable) {
                            Log.w("RemoteDeviceMcpServer", "Socket handle error: ${t.message}")
                        }
                    }
                }
            } catch (_: Exception) {
                if (running) DeviceAgentRuntime.uiToast("Device Agent server stopped")
            }
        }
    }

    fun stop() {
        running = false
        try { serverSocket?.close() } catch (_: Exception) {}
        serverSocket = null
    }

    private fun handle(socket: Socket) {
        socket.use { s ->
            s.soTimeout = 15_000
            val reader = BufferedReader(InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8))
            val requestLine = reader.readLine() ?: return
            val headers = linkedMapOf<String, String>()
            while (true) {
                val line = reader.readLine() ?: return
                if (line.isEmpty()) break
                val p = line.indexOf(':')
                if (p > 0) headers[line.substring(0, p).trim().lowercase()] = line.substring(p + 1).trim()
            }
            val path = requestLine.split(' ').getOrNull(1) ?: "/"
            val cleanPath = path.substringBefore('?')
            val query = if (path.contains('?')) path.substringAfter('?') else ""

            if (cleanPath == "/" || cleanPath == "/live" || cleanPath == "/stream" || cleanPath == "/web") {
                respondHtml(s, 200, buildLiveWebPlayerHtml())
                return
            }
            if (cleanPath == "/health") {
                respond(s, 200, "application/json", """{"ok":true,"service":"badgym-device-agent","model":"${android.os.Build.MODEL}"}""")
                return
            }
            if (cleanPath == "/screenshot" || cleanPath == "/screenshot.png" || cleanPath == "/screen.png" || cleanPath == "/screen.jpg") {
                try {
                    val bytes = DeviceAgentRuntime.screenshotPng(context)
                    val mime = if (bytes.firstOrNull() == 0xFF.toByte()) "image/jpeg" else "image/png"
                    val isDownload = query.contains("download=1")
                    val extra = if (isDownload) {
                        mapOf("Content-Disposition" to "attachment; filename=\"badgym_screen_${System.currentTimeMillis()}.jpg\"")
                    } else emptyMap()
                    respondBytes(s, 200, mime, bytes, extra)
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/screen/stream" || cleanPath == "/live_stream") {
                s.soTimeout = 0
                streamMjpeg(s, context)
                return
            }
            if (cleanPath == "/home") {
                try {
                    if (DeviceAccessibilityService.instance != null) DeviceAccessibilityService.home()
                    else Runtime.getRuntime().exec(arrayOf("input", "keyevent", "3"))
                    respond(s, 200, "application/json", """{"ok":true,"action":"home"}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/back") {
                try {
                    if (DeviceAccessibilityService.instance != null) DeviceAccessibilityService.back()
                    else Runtime.getRuntime().exec(arrayOf("input", "keyevent", "4"))
                    respond(s, 200, "application/json", """{"ok":true,"action":"back"}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/recents") {
                try {
                    if (DeviceAccessibilityService.instance != null) DeviceAccessibilityService.recents()
                    else Runtime.getRuntime().exec(arrayOf("input", "keyevent", "187"))
                    respond(s, 200, "application/json", """{"ok":true,"action":"recents"}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/launch") {
                try {
                    DeviceAgentRuntime.onMain {
                        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let {
                            context.startActivity(it.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        }
                    }
                    respond(s, 200, "application/json", """{"ok":true,"action":"launch"}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/battery_optimization") {
                try {
                    DeviceAgentRuntime.onMain {
                        BackgroundKeepAliveManager.requestIgnoreBatteryOptimizations(context)
                    }
                    respond(s, 200, "application/json", """{"ok":true,"ignored":${BackgroundKeepAliveManager.isIgnoringBatteryOptimizations(context)}}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/autostart") {
                try {
                    var opened = false
                    DeviceAgentRuntime.onMain {
                        opened = BackgroundKeepAliveManager.openOemAutostartSettings(context)
                    }
                    respond(s, 200, "application/json", """{"ok":true,"oem":"${BackgroundKeepAliveManager.getOemName()}"}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/accessibility") {
                try {
                    DeviceAgentRuntime.onMain {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                    respond(s, 200, "application/json", """{"ok":true,"active":${DeviceAccessibilityService.instance != null}}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/tap" || cleanPath == "/click") {
                try {
                    val qMap = query.split('&').mapNotNull {
                        val p = it.indexOf('=')
                        if (p > 0) it.substring(0, p) to it.substring(p + 1) else null
                    }.toMap()
                    val px = qMap["px"]?.toFloatOrNull()
                    val py = qMap["py"]?.toFloatOrNull()
                    val metrics = context.resources.displayMetrics
                    val x = if (px != null) (px * metrics.widthPixels).toInt() else qMap["x"]?.toIntOrNull() ?: 500
                    val y = if (py != null) (py * metrics.heightPixels).toInt() else qMap["y"]?.toIntOrNull() ?: 500
                    if (DeviceAccessibilityService.instance != null) {
                        DeviceAccessibilityService.tap(x, y)
                    } else {
                        Runtime.getRuntime().exec(arrayOf("input", "tap", x.toString(), y.toString()))
                    }
                    respond(s, 200, "application/json", """{"ok":true,"tapped":{"x":$x,"y":$y}}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/swipe") {
                try {
                    val qMap = query.split('&').mapNotNull {
                        val p = it.indexOf('=')
                        if (p > 0) it.substring(0, p) to it.substring(p + 1) else null
                    }.toMap()
                    val px1 = qMap["px1"]?.toFloatOrNull()
                    val py1 = qMap["py1"]?.toFloatOrNull()
                    val px2 = qMap["px2"]?.toFloatOrNull()
                    val py2 = qMap["py2"]?.toFloatOrNull()
                    val duration = qMap["duration"]?.toIntOrNull() ?: 300
                    val metrics = context.resources.displayMetrics
                    val x1 = if (px1 != null) (px1 * metrics.widthPixels).toInt() else qMap["x1"]?.toIntOrNull() ?: 500
                    val y1 = if (py1 != null) (py1 * metrics.heightPixels).toInt() else qMap["y1"]?.toIntOrNull() ?: 1200
                    val x2 = if (px2 != null) (px2 * metrics.widthPixels).toInt() else qMap["x2"]?.toIntOrNull() ?: 500
                    val y2 = if (py2 != null) (py2 * metrics.heightPixels).toInt() else qMap["y2"]?.toIntOrNull() ?: 400
                    if (DeviceAccessibilityService.instance != null) {
                        DeviceAccessibilityService.swipe(x1, y1, x2, y2, duration)
                    } else {
                        Runtime.getRuntime().exec(arrayOf("input", "swipe", x1.toString(), y1.toString(), x2.toString(), y2.toString(), duration.toString()))
                    }
                    respond(s, 200, "application/json", """{"ok":true,"swiped":{"from":{"x":$x1,"y":$y1},"to":{"x":$x2,"y":$y2}}}""")
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath == "/open_app" || cleanPath == "/launch_app") {
                try {
                    val qMap = query.split('&').mapNotNull {
                        val p = it.indexOf('=')
                        if (p > 0) it.substring(0, p) to it.substring(p + 1) else null
                    }.toMap()
                    val targetPkg = qMap["package"] ?: qMap["pkg"] ?: context.packageName
                    var started = false
                    try {
                        val intent = context.packageManager.getLaunchIntentForPackage(targetPkg)?.apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        }
                        if (intent != null) {
                            context.startActivity(intent)
                            started = true
                        }
                    } catch (_: Exception) {}

                    if (!started) {
                        try {
                            Runtime.getRuntime().exec(arrayOf("monkey", "-p", targetPkg, "-c", "android.intent.category.LAUNCHER", "1"))
                            started = true
                        } catch (_: Exception) {}
                    }

                    if (started) {
                        respond(s, 200, "application/json", """{"ok":true,"launched":"$targetPkg"}""")
                    } else {
                        respond(s, 404, "application/json", """{"error":"could not launch package: $targetPkg"}""")
                    }
                } catch (e: Exception) {
                    respond(s, 500, "application/json", """{"error":"${e.message}"}""")
                }
                return
            }
            if (cleanPath != "/mcp" || headers["authorization"] != "Bearer $token") {
                respond(s, 401, "application/json", """{"error":"unauthorized"}""")
                return
            }
            val length = headers["content-length"]?.toIntOrNull() ?: 0
            if (length <= 0 || length > 1_000_000) {
                respond(s, 400, "application/json", """{"error":"invalid body"}""")
                return
            }
            val body = CharArray(length)
            var read = 0
            while (read < length) {
                val n = reader.read(body, read, length - read)
                if (n < 0) break
                read += n
            }
            val request = gson.fromJson(String(body, 0, read), JsonObject::class.java)
            val response = dispatch(request)
            if (response == null) respond(s, 202, "application/json", "")
            else respond(s, 200, "application/json", gson.toJson(response))
        }
    }

    private fun dispatch(req: JsonObject): JsonObject? {
        val method = req.get("method")?.asString ?: return errorResponse(req, -32600, "Invalid request")
        if (method == "notifications/initialized" || method.startsWith("notifications/")) return null
        return when (method) {
            "initialize" -> result(req, JsonObject().apply {
                addProperty("protocolVersion", "2025-06-18")
                add("capabilities", JsonObject().apply { add("tools", JsonObject()) })
                add("serverInfo", JsonObject().apply {
                    addProperty("name", "badgym-xiaomi-device-agent")
                    addProperty("version", "1.0.0")
                })
            })
            "tools/list" -> result(req, toolsList())
            "tools/call" -> callTool(req)
            else -> errorResponse(req, -32601, "Method not found: $method")
        }
    }

    private fun toolsList() = JsonObject().apply {
        add("tools", gson.toJsonTree(listOf(
            tool("device_status", "Return phone, app and agent status.", emptyMap()),
            tool("screenshot", "Capture the current BAD GYM screen.", emptyMap()),
            tool("ui_tree", "Return the current Android accessibility hierarchy.", emptyMap()),
            tool("tap", "Tap a screen coordinate.", mapOf("x" to "integer", "y" to "integer")),
            tool("swipe", "Swipe between two screen coordinates.", mapOf(
                "x1" to "integer", "y1" to "integer", "x2" to "integer", "y2" to "integer", "duration_ms" to "integer"
            )),
            tool("back", "Press Android Back.", emptyMap()),
            tool("home", "Press Android Home.", emptyMap()),
            tool("set_text", "Set text in the focused accessibility field.", mapOf("text" to "string")),
            tool("launch_badgym", "Bring BAD GYM to the foreground.", emptyMap()),
            tool("open_accessibility_settings", "Open Android Accessibility settings for first-time setup.", emptyMap()),
            tool("tailscale_status", "Return current embedded Tailscale connection, IP, and state.", emptyMap()),
            tool("tailscale_restart", "Restart the embedded Tailscale daemon.", emptyMap()),
            tool("tailscale_set_authkey", "Set Tailscale auth key and reconnect.", mapOf("authkey" to "string")),
            tool("request_battery_optimization", "Prompt mobile device to grant battery optimization exemption (Doze mode bypass) like WhatsApp.", emptyMap()),
            tool("open_autostart_settings", "Open manufacturer Auto-Start and Background Activity management settings (Xiaomi/OPPO/Vivo/Samsung).", emptyMap())
        )))
    }

    private fun tool(name: String, description: String, fields: Map<String, String>): Map<String, Any> {
        val properties = fields.mapValues { mapOf("type" to it.value) }
        return mapOf(
            "name" to name,
            "description" to description,
            "inputSchema" to mapOf("type" to "object", "properties" to properties, "required" to fields.keys.toList())
        )
    }

    private fun callTool(req: JsonObject): JsonObject {
        val params = req.getAsJsonObject("params") ?: JsonObject()
        val args = params.getAsJsonObject("arguments") ?: JsonObject()
        val name = params.get("name")?.asString ?: return errorResponse(req, -32602, "Missing tool name")
        return try {
            val content = when (name) {
                "device_status" -> textResult(status())
                "screenshot" -> imageResult(DeviceAgentRuntime.screenshotPng(context))
                "ui_tree" -> textResult(DeviceAccessibilityService.dumpTree())
                "tap" -> { DeviceAccessibilityService.tap(args["x"].asInt, args["y"].asInt); textResult("tap dispatched") }
                "swipe" -> { DeviceAccessibilityService.swipe(args["x1"].asInt, args["y1"].asInt, args["x2"].asInt, args["y2"].asInt, args["duration_ms"]?.asInt ?: 350); textResult("swipe dispatched") }
                "back" -> { DeviceAccessibilityService.back(); textResult("back dispatched") }
                "home" -> { DeviceAccessibilityService.home(); textResult("home dispatched") }
                "set_text" -> { DeviceAccessibilityService.setText(args["text"].asString); textResult("text set") }
                "launch_badgym" -> {
                    DeviceAgentRuntime.onMain {
                        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let {
                            context.startActivity(it.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        }
                    }
                    textResult("BAD GYM launch requested")
                }
                "open_accessibility_settings" -> {
                    DeviceAgentRuntime.onMain {
                        context.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK))
                    }
                    textResult("Accessibility settings opened")
                }
                "request_battery_optimization" -> {
                    DeviceAgentRuntime.onMain {
                        BackgroundKeepAliveManager.requestIgnoreBatteryOptimizations(context)
                    }
                    textResult("Battery optimization ignore prompt requested")
                }
                "open_autostart_settings" -> {
                    DeviceAgentRuntime.onMain {
                        BackgroundKeepAliveManager.openOemAutostartSettings(context)
                    }
                    textResult("Opened OEM Autostart settings for: ${BackgroundKeepAliveManager.getOemName()}")
                }
                "tailscale_status" -> {
                    val ts = TailscaleManager.statusFlow.value
                    textResult(gson.toJson(ts))
                }
                "tailscale_restart" -> {
                    TailscaleManager.restart(context)
                    textResult("Tailscale restart initiated")
                }
                "tailscale_set_authkey" -> {
                    val key = args["authkey"]?.asString ?: ""
                    TailscalePrefs.setAuthKey(context, key)
                    TailscaleManager.restart(context)
                    textResult("Tailscale authkey saved and daemon restarted")
                }
                else -> return errorResponse(req, -32601, "Unknown tool: $name")
            }
            result(req, JsonObject().apply { add("content", gson.toJsonTree(content)) })
        } catch (e: Exception) {
            errorResponse(req, -32000, e.message ?: "Tool failed")
        }
    }

    private fun status(): String {
        val ts = TailscaleManager.statusFlow.value
        val batteryIgnored = BackgroundKeepAliveManager.isIgnoringBatteryOptimizations(context)
        val oem = BackgroundKeepAliveManager.getOemName()
        return "device=Android\nmodel=${android.os.Build.MODEL}\nandroid=${android.os.Build.VERSION.RELEASE}\npackage=${context.packageName}\nagent=running\nport=$port\naccessibility=${DeviceAccessibilityService.instance != null}\nbattery_optimization_ignored=$batteryIgnored\noem=$oem\ntailscale_connected=${ts.connected}\ntailscale_ip=${ts.ip}\ntailscale_dns=${ts.dnsName}\ntailscale_state=${ts.backendState}"
    }

    private fun textResult(value: String) = listOf(mapOf("type" to "text", "text" to value))
    private fun imageResult(bytes: ByteArray) = listOf(mapOf(
        "type" to "image",
        "data" to Base64.encodeToString(bytes, Base64.NO_WRAP),
        "mimeType" to "image/png"
    ))

    private fun result(req: JsonObject, value: JsonObject) = JsonObject().apply {
        if (req.has("id")) add("id", req["id"])
        addProperty("jsonrpc", "2.0")
        add("result", value)
    }

    private fun errorResponse(req: JsonObject, code: Int, message: String) = JsonObject().apply {
        if (req.has("id")) add("id", req["id"])
        addProperty("jsonrpc", "2.0")
        add("error", JsonObject().apply {
            addProperty("code", code)
            addProperty("message", message)
        })
    }

    private fun respond(socket: Socket, status: Int, contentType: String, body: String) {
        try {
            val bytes = body.toByteArray(StandardCharsets.UTF_8)
            val w = OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
            val phrase = when (status) { 200 -> "OK"; 202 -> "Accepted"; 401 -> "Unauthorized"; 404 -> "Not Found"; else -> "Bad Request" }
            w.write("HTTP/1.1 $status $phrase\r\n")
            w.write("Content-Type: $contentType\r\nContent-Length: ${bytes.size}\r\nConnection: close\r\n\r\n")
            w.flush()
            socket.getOutputStream().write(bytes)
            socket.getOutputStream().flush()
        } catch (_: Throwable) {}
    }

    private fun streamMjpeg(socket: Socket, context: android.content.Context) {
        try {
            socket.tcpNoDelay = true
            socket.soTimeout = 0
            val out = socket.getOutputStream()
            val w = OutputStreamWriter(out, StandardCharsets.UTF_8)
            w.write("HTTP/1.1 200 OK\r\n")
            w.write("Content-Type: multipart/x-mixed-replace; boundary=frame\r\n")
            w.write("Cache-Control: no-cache, no-store, must-revalidate\r\n")
            w.write("Pragma: no-cache\r\n")
            w.write("Access-Control-Allow-Origin: *\r\n")
            w.write("Connection: close\r\n\r\n")
            w.flush()

            var lastData: ByteArray? = null
            var lastSentTime = 0L
            while (!socket.isClosed && socket.isConnected) {
                val now = System.currentTimeMillis()
                val bytes = try {
                    DeviceAgentRuntime.screenshotPng(context)
                } catch (_: Exception) {
                    null
                }
                val shouldSend = bytes != null && (
                    lastData == null || 
                    !bytes.contentEquals(lastData) || 
                    (now - lastSentTime > 1000L)
                )
                if (bytes != null && shouldSend) {
                    lastData = bytes
                    lastSentTime = now
                    val mime = if (bytes.firstOrNull() == 0xFF.toByte()) "image/jpeg" else "image/png"
                    val header = "--frame\r\nContent-Type: $mime\r\nContent-Length: ${bytes.size}\r\n\r\n"
                    out.write(header.toByteArray(StandardCharsets.UTF_8))
                    out.write(bytes)
                    out.write("\r\n".toByteArray(StandardCharsets.UTF_8))
                    out.flush()
                }
                Thread.sleep(400)
            }
        } catch (_: Exception) {}
    }

    private fun respondBytes(socket: Socket, status: Int, contentType: String, bytes: ByteArray, extraHeaders: Map<String, String> = emptyMap()) {
        try {
            val w = OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
            val phrase = when (status) { 200 -> "OK"; 401 -> "Unauthorized"; else -> "Internal Error" }
            w.write("HTTP/1.1 $status $phrase\r\n")
            w.write("Content-Type: $contentType\r\nContent-Length: ${bytes.size}\r\n")
            for ((k, v) in extraHeaders) {
                w.write("$k: $v\r\n")
            }
            w.write("Connection: close\r\n\r\n")
            w.flush()
            socket.getOutputStream().write(bytes)
            socket.getOutputStream().flush()
        } catch (_: Throwable) {}
    }

    private fun respondHtml(socket: Socket, status: Int, html: String) {
        try {
            val bytes = html.toByteArray(StandardCharsets.UTF_8)
            val w = OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
            w.write("HTTP/1.1 $status OK\r\n")
            w.write("Content-Type: text/html; charset=UTF-8\r\nContent-Length: ${bytes.size}\r\nConnection: close\r\n\r\n")
            w.flush()
            socket.getOutputStream().write(bytes)
            socket.getOutputStream().flush()
        } catch (_: Throwable) {}
    }

    private fun buildLiveWebPlayerHtml(): String {
        val model = android.os.Build.MODEL
        val ts = TailscaleManager.statusFlow.value
        val ip = ts.ip ?: "Tailscale"
        return """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BAD GYM — Live Screen & Remote Control</title>
<style>
  * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
  body { background: #090d16; color: #f1f5f9; min-height: 100vh; display: flex; flex-direction: column; align-items: center; padding: 16px; }
  .header { display: flex; align-items: center; justify-content: space-between; width: 100%; max-width: 900px; margin-bottom: 12px; }
  .title { font-size: 1.1rem; font-weight: 700; color: #38bdf8; display: flex; align-items: center; gap: 8px; }
  .badge { background: #064e3b; color: #34d399; font-size: 0.75rem; font-weight: 700; padding: 4px 10px; border-radius: 9999px; display: inline-flex; align-items: center; gap: 6px; }
  .badge::before { content: ""; width: 8px; height: 8px; border-radius: 50%; background: #10b981; display: inline-block; animation: pulse 1.5s infinite; }
  @keyframes pulse { 0%, 100% { opacity: 1; transform: scale(1); } 50% { opacity: 0.4; transform: scale(0.85); } }
  .card { background: #1e293b; border: 1px solid #334155; border-radius: 12px; overflow: hidden; width: 100%; max-width: 900px; display: flex; flex-direction: column; align-items: center; box-shadow: 0 10px 30px rgba(0,0,0,0.4); }
  .stream-container { position: relative; background: #000; width: 100%; display: flex; justify-content: center; align-items: center; min-height: 480px; max-height: 75vh; overflow: hidden; cursor: crosshair; user-select: none; }
  #stream { max-width: 100%; max-height: 75vh; object-fit: contain; display: block; user-select: none; pointer-events: auto; }
  .toolbar { display: flex; flex-wrap: wrap; gap: 8px; width: 100%; padding: 12px 16px; background: #0f172a; border-top: 1px solid #334155; justify-content: center; align-items: center; }
  .btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 14px; border-radius: 8px; font-size: 0.82rem; font-weight: 600; cursor: pointer; border: none; transition: all 0.15s ease; color: #fff; }
  .btn:hover { filter: brightness(1.15); transform: translateY(-1px); }
  .btn:active { transform: translateY(1px); }
  .btn-primary { background: #0284c7; }
  .btn-success { background: #16a34a; }
  .btn-warning { background: #d97706; }
  .btn-secondary { background: #334155; }
  .toast { position: fixed; bottom: 24px; background: #059669; color: white; padding: 10px 18px; border-radius: 8px; font-weight: 600; font-size: 0.85rem; box-shadow: 0 4px 15px rgba(0,0,0,0.3); opacity: 0; transition: opacity 0.3s; pointer-events: none; z-index: 1000; }
  .toast.show { opacity: 1; }
  .info-bar { margin-top: 12px; font-size: 0.75rem; color: #94a3b8; text-align: center; }
</style>
</head>
<body>
  <div class="header">
    <div class="title">🏋️ BAD GYM Live — $model ($ip)</div>
    <div class="badge">LIVE OVER TAILSCALE</div>
  </div>
  <div class="card">
    <div class="stream-container" id="streamBox" title="Click to Tap, Drag up/down to Scroll on phone!">
      <img id="stream" src="/screen/stream" alt="Live Screen Stream" />
    </div>
    <div class="toolbar">
      <button class="btn btn-primary" onclick="saveScreenshot()">📸 Save Screenshot</button>
      <button class="btn btn-success" onclick="refreshStream()">🔄 Refresh Stream</button>
      <button class="btn btn-secondary" onclick="toggleFullscreen()">⚡ Fullscreen</button>
      <button class="btn btn-secondary" onclick="sendNav('back')">◀ Back</button>
      <button class="btn btn-secondary" onclick="sendNav('home')">🏠 Home</button>
      <button class="btn btn-secondary" onclick="sendNav('recents')">📱 Recents</button>
      <button class="btn btn-secondary" onclick="sendSwipe(0.5, 0.75, 0.5, 0.25)">⬆️ Scroll Down</button>
      <button class="btn btn-secondary" onclick="sendSwipe(0.5, 0.25, 0.5, 0.75)">⬇️ Scroll Up</button>
      <button class="btn btn-success" onclick="launchApp('com.whatsapp.w4b')">💬 WhatsApp</button>
      <button class="btn btn-warning" onclick="launchApp('com.google.android.youtube')">▶ YouTube</button>
      <button class="btn btn-secondary" onclick="launchApp('com.android.settings')">⚙️ Settings</button>
      <button class="btn btn-warning" onclick="sendNav('launch')">🏋️ BAD GYM</button>
      <button class="btn btn-secondary" onclick="sendNav('battery_optimization')">🔋 KeepAlive / Battery</button>
      <button class="btn btn-secondary" onclick="sendNav('autostart')">⚙️ AutoStart</button>
    </div>
  </div>
  <div style="display:flex; gap:8px; width:100%; max-width:900px; margin-top:8px;">
    <input id="pkgInput" type="text" placeholder="Package name (e.g. com.whatsapp.w4b or com.google.android.youtube)" style="flex:1; padding:8px 12px; border-radius:6px; border:1px solid #334155; background:#0f172a; color:#fff; font-size:0.85rem;" />
    <button class="btn btn-primary" onclick="launchCustomApp()">🚀 Open App</button>
  </div>
  <div class="info-bar">💡 Pro Tip: Tap on the live screen to click, or drag up/down with mouse to scroll through any app (WhatsApp, YouTube, etc.)!</div>
  <div id="toast" class="toast">📸 Screenshot saved to your Downloads!</div>
<script>
function launchApp(pkg) {
  fetch('/open_app?pkg=' + encodeURIComponent(pkg)).then(() => showToast('Opening ' + pkg)).catch(()=>{});
}
function launchCustomApp() {
  const p = document.getElementById('pkgInput').value.trim();
  if (p) launchApp(p);
}
function showToast(msg) {
  const t = document.getElementById('toast');
  t.innerText = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 2500);
}
function saveScreenshot() {
  const a = document.createElement('a');
  const now = new Date();
  const ts = now.toISOString().replace(/[:.]/g, '-').slice(0, 19);
  a.href = '/screen.jpg?download=1&ts=' + Date.now();
  a.download = 'badgym_screenshot_' + ts + '.jpg';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  showToast('📸 Screenshot saved to your Downloads!');
}
function toggleFullscreen() {
  const elem = document.getElementById('streamBox');
  if (!document.fullscreenElement) elem.requestFullscreen().catch(()=>{});
  else document.exitFullscreen().catch(()=>{});
}
function sendNav(act) {
  fetch('/' + act, { method: 'POST' }).then(() => showToast('Dispatched: ' + act)).catch(() => {});
}
function sendSwipe(px1, py1, px2, py2) {
  fetch('/swipe?px1=' + px1.toFixed(3) + '&py1=' + py1.toFixed(3) + '&px2=' + px2.toFixed(3) + '&py2=' + py2.toFixed(3) + '&duration=300', { method: 'POST' })
    .then(() => showToast('👆 Scrolled'))
    .catch(() => {});
}
function refreshStream() {
  const img = document.getElementById('stream');
  img.src = '/screen/stream?ts=' + Date.now();
  showToast('🔄 Stream refreshed');
}
const img = document.getElementById('stream');
let isMouseDown = false;
let startX = 0, startY = 0;

img.addEventListener('mousedown', function(e) {
  isMouseDown = true;
  startX = e.clientX;
  startY = e.clientY;
  e.preventDefault();
});

img.addEventListener('mouseup', function(e) {
  if (!isMouseDown) return;
  isMouseDown = false;
  const rect = img.getBoundingClientRect();
  const dx = e.clientX - startX;
  const dy = e.clientY - startY;
  const dist = Math.hypot(dx, dy);
  
  if (dist > 20) {
    // Drag gesture -> swipe
    const px1 = (startX - rect.left) / rect.width;
    const py1 = (startY - rect.top) / rect.height;
    const px2 = (e.clientX - rect.left) / rect.width;
    const py2 = (e.clientY - rect.top) / rect.height;
    sendSwipe(px1, py1, px2, py2);
  } else {
    // Tap gesture
    const px = (startX - rect.left) / rect.width;
    const py = (startY - rect.top) / rect.height;
    fetch('/tap?px=' + px.toFixed(4) + '&py=' + py.toFixed(4), { method: 'POST' })
      .then(() => showToast('👆 Tapped at ' + Math.round(px*100) + '%, ' + Math.round(py*100) + '%'))
      .catch(() => {});
  }
});

img.onerror = function() {
  setTimeout(() => { img.src = '/screen/stream?retry=' + Date.now(); }, 1500);
};
</script>
</body>
</html>"""
    }
}

object DeviceAgentToken {
    private const val PREFS = "device_agent"
    private const val KEY = "token"

    fun get(context: android.content.Context): String =
        context.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE).getString(KEY, null)
            ?: UUID.randomUUID().toString().replace("-", "").also {
                context.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
                    .edit().putString(KEY, it).apply()
            }
}