package com.example.badnewgym.hybridtracking.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.badnewgym.hybridtracking.db.TelemetryDatabase
import com.example.badnewgym.hybridtracking.model.BleBeaconInfo
import com.example.badnewgym.hybridtracking.model.CellTowerInfo
import com.example.badnewgym.hybridtracking.model.LocationResult
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import com.example.badnewgym.hybridtracking.model.WifiAccessPointInfo
import com.example.badnewgym.hybridtracking.network.NetworkObserver
import com.example.badnewgym.hybridtracking.service.NetworkScannerService
import com.example.badnewgym.hybridtracking.service.TelemetrySyncWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HybridTrackingDashboard(
    service: NetworkScannerService?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { TelemetryDatabase.getInstance(context) }

    val latestPayload = service?.latestPayload?.collectAsState()?.value
    val latestLocation = service?.latestLocation?.collectAsState()?.value
    val isScanning = service?.isScanning?.collectAsState()?.value ?: false

    val pendingCountState = db.telemetryDao().getPendingCountFlow().collectAsState(initial = 0)
    val networkObserver = remember { NetworkObserver(context) }
    val networkState by networkObserver.networkState.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var isPermissionsGranted by remember { mutableStateOf(checkPermissions(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        isPermissionsGranted = checkPermissions(context)
    }

    LaunchedEffect(Unit) {
        networkObserver.startObserving()
        TelemetrySyncWorker.schedulePeriodicSync(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Low-GPS Hybrid Positioning",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cellular • Wi-Fi • BLE Beacon Triangulation",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isScanning) Color(0xFF10B981) else Color(0xFFEF4444))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isScanning) "ACTIVE SCAN" else "IDLE",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Permission Request Banner if missing
        if (!isPermissionsGranted) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Location & Bluetooth permissions required for network scanning.",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            val perms = mutableListOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                perms.add(Manifest.permission.BLUETOOTH_SCAN)
                                perms.add(Manifest.permission.BLUETOOTH_CONNECT)
                            }
                            permissionLauncher.launch(perms.toTypedArray())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Grant", fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Control Panel Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Offline Queue: ${pendingCountState.value} records",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = { networkObserver.promptEnableWifi(context) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Wi-Fi Panel", fontSize = 11.sp, color = Color(0xFF38BDF8))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(context, NetworkScannerService::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(intent)
                            } else {
                                context.startService(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start Scanner", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                service?.triggerImmediateScanAndSync()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Scan & Sync", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Location Resolution Result Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2942)),
            modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF1E40AF), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    val loc = latestLocation
                    if (loc != null) {
                        Text(
                            text = "Resolved Position (${loc.locationSource})",
                            color = Color(0xFF93C5FD),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lat: ${String.format("%.5f", loc.lat)}, Lng: ${String.format("%.5f", loc.lng)}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Accuracy Radius: ±${loc.accuracyMeters.toInt()} meters",
                            color = Color(0xFF34D399),
                            fontSize = 11.sp
                        )
                    } else {
                        Text(
                            text = "Location Resolution Pending",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Press 'Scan & Sync' to trigger backend location triangulation.",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs for Cellular, Wi-Fi, BLE metadata view
        val payload = latestPayload
        val cellCount = payload?.cellTowers?.size ?: 0
        val wifiCount = payload?.wifiAccessPoints?.size ?: 0
        val bleCount = payload?.bleBeacons?.size ?: 0

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E293B),
            contentColor = Color(0xFF38BDF8)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Cellular ($cellCount)", fontSize = 12.sp, color = Color.White) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Wi-Fi ($wifiCount)", fontSize = 12.sp, color = Color.White) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("BLE Beacons ($bleCount)", fontSize = 12.sp, color = Color.White) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Metadata list container
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> CellularTowerList(towers = payload?.cellTowers ?: emptyList())
                1 -> WifiAccessPointList(aps = payload?.wifiAccessPoints ?: emptyList())
                2 -> BleBeaconList(beacons = payload?.bleBeacons ?: emptyList())
            }
        }
    }
}

@Composable
private fun CellularTowerList(towers: List<CellTowerInfo>) {
    if (towers.isEmpty()) {
        EmptyStateText("No cell towers scanned yet.")
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(towers) { tower ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CellTower, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cell ID: ${tower.cellId}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (tower.isServing) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("[SERVING]", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("MCC: ${tower.mcc} | MNC: ${tower.mnc} | TAC/LAC: ${tower.lacTac}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${tower.signalStrength} dBm", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("TA Index: ${tower.timingAdvance} (~${(tower.timingAdvance * 78.12).toInt()}m)", color = Color(0xFFA7F3D0), fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WifiAccessPointList(aps: List<WifiAccessPointInfo>) {
    if (aps.isEmpty()) {
        EmptyStateText("No Wi-Fi networks scanned yet.")
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(aps) { ap ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Wifi, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(ap.ssid, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Text("BSSID: ${ap.bssid}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${ap.rssi} dBm", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = if (ap.isHotspot) "MOBILE HOTSPOT" else "FIXED ROUTER",
                                color = if (ap.isHotspot) Color(0xFFF87171) else Color(0xFF34D399),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BleBeaconList(beacons: List<BleBeaconInfo>) {
    if (beacons.isEmpty()) {
        EmptyStateText("No BLE beacons scanned yet.")
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(beacons) { beacon ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bluetooth, contentDescription = null, tint = Color(0xFFA855F7), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Major: ${beacon.major} | Minor: ${beacon.minor}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Text("UUID: ${beacon.uuid}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        }
                        Text("${beacon.rssi} dBm", color = Color(0xFFC084FC), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = Color(0xFF64748B), fontSize = 13.sp)
    }
}

private fun checkPermissions(context: Context): Boolean {
    val fineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    val coarseLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    return fineLoc && coarseLoc
}
