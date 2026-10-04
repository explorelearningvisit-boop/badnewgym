package com.example.badnewgym.hybridtracking.scanner

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import com.example.badnewgym.hybridtracking.db.TelemetryDatabase
import com.example.badnewgym.hybridtracking.db.TelemetryEntity
import com.example.badnewgym.hybridtracking.model.ConnectivityInfo
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class TelemetryScannerRepository(private val context: Context) {
    private val cellScanner = CellTowerScanner(context)
    private val wifiScanner = WifiScanner(context)
    private val bleScanner = BleScanner(context)
    private val db = TelemetryDatabase.getInstance(context)
    private val gson = Gson()

    suspend fun captureTelemetry(): TelemetryPayload = withContext(Dispatchers.IO) {
        val deviceId = getDeviceId(context)
        val timestamp = System.currentTimeMillis()
        val connectivityInfo = getConnectivityInfo(context)
        
        val cellTowers = cellScanner.scanCellTowers()
        val wifiAps = wifiScanner.scanWifiNetworks()
        val bleBeacons = bleScanner.scanBleBeaconsSync(1500L)

        val payload = TelemetryPayload(
            deviceId = deviceId,
            timestamp = timestamp,
            connectivity = connectivityInfo,
            cellTowers = cellTowers,
            wifiAccessPoints = wifiAps,
            bleBeacons = bleBeacons
        )

        // Save to Room DB for offline buffer & sync pipeline
        val jsonStr = gson.toJson(payload)
        val entity = TelemetryEntity(
            deviceId = deviceId,
            timestamp = timestamp,
            payloadJson = jsonStr,
            isSynced = false
        )
        db.telemetryDao().insertRecord(entity)

        payload
    }

    private fun getConnectivityInfo(context: Context): ConnectivityInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork ?: return ConnectivityInfo("NONE", isMetered = false)
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return ConnectivityInfo("NONE", isMetered = false)

        val networkType = when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            else -> "OTHER"
        }

        val isMetered = !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        return ConnectivityInfo(networkType = networkType, isMetered = isMetered)
    }

    companion object {
        fun getDeviceId(context: Context): String {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return if (!androidId.isNullOrEmpty() && androidId != "9774d56d682e549c") {
                androidId
            } else {
                "DEVICE_" + UUID.nameUUIDFromBytes("BAD_GYM_DEVICE".toByteArray()).toString().substring(0, 8)
            }
        }
    }
}
