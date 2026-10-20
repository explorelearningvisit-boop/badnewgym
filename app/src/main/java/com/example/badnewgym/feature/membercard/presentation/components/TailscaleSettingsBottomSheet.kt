package com.example.badnewgym.feature.membercard.presentation.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.deviceagent.BackgroundKeepAliveManager
import com.example.badnewgym.deviceagent.DeviceAccessibilityService
import com.example.badnewgym.deviceagent.TailscaleManager
import com.example.badnewgym.deviceagent.TailscaleNodeStatus
import com.example.badnewgym.deviceagent.TailscalePrefs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailscaleSettingsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val status by TailscaleManager.statusFlow.collectAsState()

    var isEnabled by remember { mutableStateOf(TailscalePrefs.isEnabled(context)) }
    var authKeyInput by remember { mutableStateOf(TailscalePrefs.getAuthKey(context)) }
    var hostnameInput by remember { mutableStateOf(TailscalePrefs.getHostname(context)) }
    var autostartOnBoot by remember { mutableStateOf(TailscalePrefs.isAutostartOnBoot(context)) }
    var triggerAppOnTurnOn by remember { mutableStateOf(TailscalePrefs.isTriggerAppOnTurnOn(context)) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFFF8FAFC),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFCBD5E1))
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnLock,
                            contentDescription = "Tailscale",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tailscale Integration",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Embedded Node • No Mobile App Required",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Status Card
            StatusCard(status = status, onCopyIp = { ip ->
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Tailscale IP", ip))
                Toast.makeText(context, "Copied IP: $ip", Toast.LENGTH_SHORT).show()
            })

            // Login Link if needed
            if (status.backendState == "NeedsLogin" && status.authUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(status.authUrl))
                            context.startActivity(browserIntent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error opening browser", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Authenticate with Tailscale", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Configuration section
            Text(
                text = "CONFIGURATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Auth Key input
            OutlinedTextField(
                value = authKeyInput,
                onValueChange = { authKeyInput = it },
                label = { Text("Tailscale Auth Key (tskey-auth-...)") },
                placeholder = { Text("Optional if already logged in") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hostname input
            OutlinedTextField(
                value = hostnameInput,
                onValueChange = { hostnameInput = it },
                label = { Text("Tailscale Hostname") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Toggles
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enable Embedded Tailscale", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            Text("Registers port 8787 in Tailnet", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Start on Mobile Restart", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            Text("Auto-connects on device boot", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Switch(
                            checked = autostartOnBoot,
                            onCheckedChange = { autostartOnBoot = it }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Trigger App on Restart / Request", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            Text("Brings BAD GYM to front when triggered", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Switch(
                            checked = triggerAppOnTurnOn,
                            onCheckedChange = { triggerAppOnTurnOn = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // WhatsApp-Style Background Resilience Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Always Run in Background", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("WhatsApp mode: survives app close & recent swipe", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        val isBatteryIgnoring = BackgroundKeepAliveManager.isIgnoringBatteryOptimizations(context)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isBatteryIgnoring) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                if (isBatteryIgnoring) "UNRESTRICTED" else "OPTIMIZED",
                                color = if (isBatteryIgnoring) Color(0xFF166534) else Color(0xFF92400E),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!BackgroundKeepAliveManager.isIgnoringBatteryOptimizations(context)) {
                        Button(
                            onClick = { BackgroundKeepAliveManager.requestIgnoreBatteryOptimizations(context) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Exempt from Battery Optimization (Recommended)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    OutlinedButton(
                        onClick = {
                            val opened = BackgroundKeepAliveManager.openOemAutostartSettings(context)
                            if (!opened) {
                                Toast.makeText(context, "Please allow Auto-start in App settings", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text(
                            "Open ${BackgroundKeepAliveManager.getOemName()} Autostart Settings",
                            fontSize = 12.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Screen Capture Across Other Apps", fontSize = 12.sp, color = Color(0xFF475569))
                        val a11yActive = DeviceAccessibilityService.instance != null
                        Text(
                            if (a11yActive) "Active (Ready)" else "Accessibility Off",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (a11yActive) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save & Restart Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        TailscalePrefs.setEnabled(context, isEnabled)
                        TailscalePrefs.setAuthKey(context, authKeyInput)
                        TailscalePrefs.setHostname(context, hostnameInput)
                        TailscalePrefs.setAutostartOnBoot(context, autostartOnBoot)
                        TailscalePrefs.setTriggerAppOnTurnOn(context, triggerAppOnTurnOn)

                        Toast.makeText(context, "Settings saved. Restarting Tailscale...", Toast.LENGTH_SHORT).show()
                        if (isEnabled) {
                            TailscaleManager.restart(context)
                        } else {
                            TailscaleManager.stop()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save & Apply", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = {
                        TailscaleManager.restart(context)
                        Toast.makeText(context, "Reconnecting...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Text("Restart Daemon", color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    status: TailscaleNodeStatus,
    onCopyIp: (String) -> Unit
) {
    val isConnected = status.connected
    val badgeBg = when {
        isConnected -> Color(0xFFDCFCE7)
        status.backendState == "NeedsLogin" -> Color(0xFFFEF3C7)
        status.backendState == "Starting" -> Color(0xFFE0F2FE)
        else -> Color(0xFFF1F5F9)
    }
    val badgeTextColor = when {
        isConnected -> Color(0xFF166534)
        status.backendState == "NeedsLogin" -> Color(0xFF92400E)
        status.backendState == "Starting" -> Color(0xFF075985)
        else -> Color(0xFF64748B)
    }
    val dotColor = when {
        isConnected -> Color(0xFF22C55E)
        status.backendState == "NeedsLogin" -> Color(0xFFF59E0B)
        status.backendState == "Starting" -> Color(0xFF0EA5E9)
        else -> Color(0xFF94A3B8)
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isConnected) "Connected & Registered" else status.backendState,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Port ${status.listenPort}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // IP row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Tailscale IP", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                    Text(
                        text = if (status.ip.isNotBlank()) status.ip else "Pending registration...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (status.ip.isNotBlank()) Color(0xFF0F172A) else Color(0xFF94A3B8)
                    )
                }
                if (status.ip.isNotBlank()) {
                    IconButton(onClick = { onCopyIp(status.ip) }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy IP",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (status.dnsName.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("MagicDNS", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                Text(
                    text = status.dnsName,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF334155)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Forwarding To", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                    Text("127.0.0.1:8787 (MCP Server)", fontSize = 13.sp, color = Color(0xFF334155))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Node Name", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                    Text(status.hostname.ifBlank { "badgym-device" }, fontSize = 13.sp, color = Color(0xFF334155))
                }
            }

            if (status.error != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Error: ${status.error}",
                    fontSize = 12.sp,
                    color = Color(0xFFDC2626)
                )
            }
        }
    }
}
