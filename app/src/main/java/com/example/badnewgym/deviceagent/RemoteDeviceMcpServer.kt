package com.example.badnewgym.deviceagent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.Base64
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

    fun screenshotPng(): ByteArray {
        val a = activity ?: error("BAD GYM activity is not visible")
        var result: ByteArray? = null
        val latch = java.util.concurrent.CountDownLatch(1)
        main.post {
            try {
                val view = a.window.decorView
                if (view.width <= 0 || view.height <= 0) error("BAD GYM window has no size")
                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                view.draw(Canvas(bitmap))
                val stream = java.io.ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                bitmap.recycle()
                result = stream.toByteArray()
            } finally { latch.countDown() }
        }
        if (!latch.await(5, TimeUnit.SECONDS)) error("Screenshot timed out")
        return result ?: error("Screenshot failed")
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
                    executor.execute { handle(socket) }
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
            if (path == "/health") {
                respond(s, 200, "application/json", """{"ok":true,"service":"badgym-device-agent"}""")
                return
            }
            if (path != "/mcp" || headers["authorization"] != "Bearer $token") {
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
            tool("open_accessibility_settings", "Open Android Accessibility settings for first-time setup.", emptyMap())
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
                "screenshot" -> imageResult(DeviceAgentRuntime.screenshotPng())
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
                else -> return errorResponse(req, -32601, "Unknown tool: $name")
            }
            result(req, JsonObject().apply { add("content", gson.toJsonTree(content)) })
        } catch (e: Exception) {
            errorResponse(req, -32000, e.message ?: "Tool failed")
        }
    }

    private fun status() =
        "device=Android\nmodel=${android.os.Build.MODEL}\nandroid=${android.os.Build.VERSION.RELEASE}\npackage=${context.packageName}\nagent=running\nport=$port\naccessibility=${DeviceAccessibilityService.instance != null}"

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
        val bytes = body.toByteArray(StandardCharsets.UTF_8)
        val w = OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
        val phrase = when (status) { 200 -> "OK"; 202 -> "Accepted"; 401 -> "Unauthorized"; else -> "Bad Request" }
        w.write("HTTP/1.1 $status $phrase\r\n")
        w.write("Content-Type: $contentType\r\nContent-Length: ${bytes.size}\r\nConnection: close\r\n\r\n")
        w.flush()
        socket.getOutputStream().write(bytes)
        socket.getOutputStream().flush()
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