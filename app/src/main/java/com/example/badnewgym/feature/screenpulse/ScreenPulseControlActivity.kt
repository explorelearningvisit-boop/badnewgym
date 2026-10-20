package com.example.badnewgym.feature.screenpulse

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Operator console for Screen Pulse.
 *
 * This is the one place a human tap is unavoidable: Android requires an
 * explicit "start casting / recording" consent that cannot be granted over ADB.
 * Everything after that - status, manifest, pull, purge - is driven from the
 * laptop tool.
 */
class ScreenPulseControlActivity : ComponentActivity() {

    private val consentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            ScreenPulseCaptureService.start(this, result.resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(Modifier.fillMaxSize(), color = Color(0xFF0F172A)) {
                    ScreenPulseConsole(
                        onStart = { requestConsent() },
                        onStop = { ScreenPulseCaptureService.stop(this) }
                    )
                }
            }
        }
    }

    private fun requestConsent() {
        val mpm = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val config = android.media.projection.MediaProjectionConfig.createConfigForDefaultDisplay()
            consentLauncher.launch(mpm.createScreenCaptureIntent(config))
        } else {
            consentLauncher.launch(mpm.createScreenCaptureIntent())
        }
    }
}

@Composable
private fun ScreenPulseConsole(onStart: () -> Unit, onStop: () -> Unit) {
    var frames by remember { mutableStateOf(0) }
    var bytes by remember { mutableStateOf(0L) }
    var running by remember { mutableStateOf(ScreenPulseCaptureService.isRunning()) }
    var confirmPurge by remember { mutableStateOf(false) }
    var purgeResult by remember { mutableStateOf("") }
    val ctx = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(ctx) {
        while (true) {
            val s = ScreenPulseVault.stats(ctx.filesDir)
            frames = s.frames
            bytes = s.bytes
            running = ScreenPulseCaptureService.isRunning()
            delay(1_000)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text("Screen Pulse", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text(
                "Local frame vault - no network, ADB pull only",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Status", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(
                        if (running) "CAPTURING" else "IDLE",
                        color = if (running) Color(0xFF34D399) else Color(0xFFF59E0B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Row {
                    Text("Frames", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text("$frames", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Row {
                    Text("Vault size", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text("${bytes / 1024} KB", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Row {
                    Text("Interval", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(
                        "${ScreenPulseConfig.INTERVAL_MS / 1000}s",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    Text("Per frame", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(
                        "${ScreenPulseConfig.MIN_FRAME_BYTES / 1024}-${ScreenPulseConfig.MAX_FRAME_BYTES / 1024} KB",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onStart,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) { Text(if (running) "Restart capture" else "Start capture", fontWeight = FontWeight.Bold) }
            OutlinedButton(onClick = onStop, modifier = Modifier.weight(1f)) {
                Text("Stop", color = Color(0xFFF87171), fontWeight = FontWeight.Bold)
            }
        }

        OutlinedButton(onClick = { openUsageAccess(ctx) }, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Grant usage access (app tags)",
                color = Color(0xFF93C5FD),
                fontSize = 13.sp
            )
        }

        OutlinedButton(
            onClick = { com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.requestIgnoreBatteryOptimizations(ctx) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "🔋 Allow 24/7 Background (Disable Battery Optimization)",
                color = Color(0xFF34D399),
                fontSize = 13.sp
            )
        }

        OutlinedButton(
            onClick = { com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.openOemAutostartSettings(ctx) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "⚙️ Open Auto-Start Settings (${com.example.badnewgym.deviceagent.BackgroundKeepAliveManager.getOemName()})",
                color = Color(0xFFFBBF24),
                fontSize = 13.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Laptop pull", color = Color(0xFF94A3B8), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(
                    "python OPPO\\screenpulse_pull.py pull",
                    color = Color(0xFF6EE7B7),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    "python OPPO\\screenpulse_pull.py pull --purge --yes",
                    color = Color(0xFF6EE7B7),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Frames are only readable by this app's sandbox. The tool reaches them " +
                        "through adb run-as, verifies every SHA-256 against the on-device " +
                        "manifest, and only then can hard-erase the vault.",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
        }

        if (!confirmPurge) {
            OutlinedButton(
                onClick = { confirmPurge = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171))
            ) { Text("Purge vault now", fontWeight = FontWeight.Bold) }
        } else {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF3F1D1D)), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Overwrite and delete all $frames frames? This cannot be undone.",
                        color = Color(0xFFFECACA),
                        fontSize = 13.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { confirmPurge = false }) {
                            Text("Cancel", color = Color(0xFFCBD5E1))
                        }
                        Button(
                            onClick = {
                                ScreenPulseCaptureService.stop(ctx)
                                val erased = ScreenPulseVault.purge(ctx.filesDir)
                                purgeResult = if (erased >= 0) "Erased $erased files"
                                else "Erase incomplete ($erased)"
                                confirmPurge = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                        ) { Text("Erase", fontWeight = FontWeight.Black) }
                    }
                    if (purgeResult.isNotEmpty()) {
                        Text(purgeResult, color = Color(0xFF94A3B8), fontSize = 12.sp)
                    }
                }
            }
        }

        Text(
            "Vault: files/${ScreenPulseConfig.VAULT_DIR_NAME} - sandbox private, " +
                ".nomedia, excluded from backup.",
            color = Color(0xFF475569),
            fontSize = 11.sp
        )
    }
}

private fun openUsageAccess(context: Context) {
    try {
        val uri = Uri.parse("package:${context.packageName}")
        context.startActivity(
            Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                .setData(uri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (t: Throwable) {
        try {
            context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (ignored: Throwable) {
        }
    }
}
