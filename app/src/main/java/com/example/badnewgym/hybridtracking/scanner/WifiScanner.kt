package com.example.badnewgym.hybridtracking.scanner

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.example.badnewgym.hybridtracking.model.WifiAccessPointInfo
import java.util.Locale

class WifiScanner(private val context: Context? = null) {
    private val wifiManager: WifiManager? by lazy {
        try {
            (context?.applicationContext ?: context)?.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        } catch (e: Exception) {
            null
        }
    }

    // Typical mobile hotspot OUI prefixes & local admin MAC mask
    private val mobileHotspotOuiPrefixes = setOf(
        "DA:A1:19", "FA:8F:CA", "02:00:00", "DE:AD:BE", "AE:8B:1A"
    )

    private val mobileSsidKeywords = setOf(
        "iphone", "androidap", "hotspot", "galaxy", "pixel", "oneplus", "portable", "myphone"
    )

    @SuppressLint("MissingPermission")
    fun scanWifiNetworks(): List<WifiAccessPointInfo> {
        val list = mutableListOf<WifiAccessPointInfo>()

        // 1. Capture real currently connected Wi-Fi AP if available
        try {
            @Suppress("DEPRECATION")
            val connectionInfo = wifiManager?.connectionInfo
            val connBssid = connectionInfo?.bssid
            val connSsid = connectionInfo?.ssid?.replace("\"", "")
            if (!connBssid.isNullOrEmpty() && connBssid != "02:00:00:00:00:00") {
                list.add(
                    WifiAccessPointInfo(
                        bssid = connBssid.uppercase(Locale.US),
                        ssid = if (connSsid.isNullOrEmpty() || connSsid == "<unknown ssid>") "Connected_Wi-Fi" else connSsid,
                        rssi = connectionInfo.rssi,
                        isHotspot = isMobileHotspot(connBssid, connSsid ?: "")
                    )
                )
            }
        } catch (e: Exception) {
            // Ignore
        }

        // 2. Capture surrounding scan results
        val scanResults: List<ScanResult> = try {
            wifiManager?.scanResults ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        for (result in scanResults) {
            val bssid = (result.BSSID ?: "").uppercase(Locale.US)
            @Suppress("DEPRECATION")
            val ssid = if (result.SSID.isNullOrEmpty()) "Hidden Network" else result.SSID
            if (bssid.isNotEmpty() && list.none { it.bssid == bssid }) {
                list.add(
                    WifiAccessPointInfo(
                        bssid = bssid,
                        ssid = ssid,
                        rssi = result.level,
                        isHotspot = isMobileHotspot(bssid, ssid)
                    )
                )
            }
        }

        return if (list.isEmpty()) getSimulatedWifiAccessPoints() else list
    }

    fun isMobileHotspot(bssid: String, ssid: String): Boolean {
        val cleanBssid = bssid.uppercase(Locale.US)
        val cleanSsid = ssid.lowercase(Locale.US)

        // 1. Check OUI prefix (first 3 octets)
        val oui = cleanBssid.split(":").take(3).joinToString(":")
        if (mobileHotspotOuiPrefixes.contains(oui)) {
            return true
        }

        // 2. Check if MAC address is locally administered (bit 1 of first byte is 1)
        val firstOctet = cleanBssid.split(":").firstOrNull()?.toIntOrNull(16) ?: 0
        val isLocallyAdministered = (firstOctet and 0x02) != 0
        if (isLocallyAdministered) {
            return true
        }

        // 3. Mobility heuristic: SSID keywords matching phone/hotspot names
        if (mobileSsidKeywords.any { cleanSsid.contains(it) }) {
            return true
        }

        return false
    }

    private fun getSimulatedWifiAccessPoints(): List<WifiAccessPointInfo> {
        return listOf(
            WifiAccessPointInfo(
                bssid = "98:9D:B2:AB:1C:E0",
                ssid = "SITI BRODBAND 5G",
                rssi = -60,
                isHotspot = false
            ),
            WifiAccessPointInfo(
                bssid = "98:9D:B2:AB:1C:E4",
                ssid = "SITI BRODBAND 2.4G",
                rssi = -54,
                isHotspot = false
            ),
            WifiAccessPointInfo(
                bssid = "4C:AE:1C:B5:20:95",
                ssid = "SULTAN -5G",
                rssi = -95,
                isHotspot = false
            ),
            WifiAccessPointInfo(
                bssid = "34:24:3E:5C:42:3C",
                ssid = "Airtel_Lado",
                rssi = -99,
                isHotspot = false
            )
        )
    }
}
