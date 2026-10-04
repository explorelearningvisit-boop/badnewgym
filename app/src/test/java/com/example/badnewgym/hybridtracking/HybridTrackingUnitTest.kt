package com.example.badnewgym.hybridtracking

import com.example.badnewgym.hybridtracking.model.BleBeaconInfo
import com.example.badnewgym.hybridtracking.model.CellTowerInfo
import com.example.badnewgym.hybridtracking.model.ConnectivityInfo
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import com.example.badnewgym.hybridtracking.model.WifiAccessPointInfo
import com.example.badnewgym.hybridtracking.scanner.WifiScanner
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HybridTrackingUnitTest {

    private val gson = Gson()

    @Test
    fun testWifiHotspotDetectionHeuristics() {
        val scanner = WifiScanner()

        // Fixed router BSSID & SSID
        assertFalse(scanner.isMobileHotspot("00:14:22:01:23:45", "Office_Network_5G"))

        // Mobile SSID keyword
        assertTrue(scanner.isMobileHotspot("00:14:22:01:23:45", "John's iPhone"))
        assertTrue(scanner.isMobileHotspot("00:14:22:01:23:45", "AndroidAP_9912"))

        // Locally administered MAC address bit set
        assertTrue(scanner.isMobileHotspot("02:00:00:12:34:56", "Random_Net"))
    }

    @Test
    fun testTelemetryPayloadJsonContractSchema() {
        val payload = TelemetryPayload(
            deviceId = "DEVICE_UUID_OR_SECURE_ID",
            timestamp = 1728000000000L,
            connectivity = ConnectivityInfo(
                networkType = "WIFI",
                isMetered = false
            ),
            cellTowers = listOf(
                CellTowerInfo(
                    cellId = 12345678L,
                    lacTac = 4512,
                    mcc = 404,
                    mnc = 45,
                    signalStrength = -85,
                    timingAdvance = 4,
                    isServing = true
                )
            ),
            wifiAccessPoints = listOf(
                WifiAccessPointInfo(
                    bssid = "00:14:22:01:23:45",
                    ssid = "Office_Network_5G",
                    rssi = -62,
                    isHotspot = false
                )
            ),
            bleBeacons = listOf(
                BleBeaconInfo(
                    uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825",
                    major = 1,
                    minor = 102,
                    rssi = -55
                )
            )
        )

        val json = gson.toJson(payload)
        assertTrue(json.contains("\"device_id\":\"DEVICE_UUID_OR_SECURE_ID\""))
        assertTrue(json.contains("\"timing_advance\":4"))
        assertTrue(json.contains("\"bssid\":\"00:14:22:01:23:45\""))
        assertTrue(json.contains("\"uuid\":\"fda50693-a4e2-4fb1-afcf-c6eb07647825\""))

        // Deserialize back
        val deserialized = gson.fromJson(json, TelemetryPayload::class.java)
        assertEquals("DEVICE_UUID_OR_SECURE_ID", deserialized.deviceId)
        assertEquals(12345678L, deserialized.cellTowers.first().cellId)
        assertEquals(4, deserialized.cellTowers.first().timingAdvance)
        assertEquals("00:14:22:01:23:45", deserialized.wifiAccessPoints.first().bssid)
        assertEquals("fda50693-a4e2-4fb1-afcf-c6eb07647825", deserialized.bleBeacons.first().uuid)
    }
}
