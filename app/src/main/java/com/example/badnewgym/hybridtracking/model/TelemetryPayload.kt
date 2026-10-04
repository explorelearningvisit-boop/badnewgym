package com.example.badnewgym.hybridtracking.model

import com.google.gson.annotations.SerializedName

data class TelemetryPayload(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("connectivity")
    val connectivity: ConnectivityInfo,
    
    @SerializedName("cell_towers")
    val cellTowers: List<CellTowerInfo>,
    
    @SerializedName("wifi_access_points")
    val wifiAccessPoints: List<WifiAccessPointInfo>,
    
    @SerializedName("ble_beacons")
    val bleBeacons: List<BleBeaconInfo>
)

data class ConnectivityInfo(
    @SerializedName("network_type")
    val networkType: String, // "WIFI", "CELLULAR", "NONE"
    
    @SerializedName("is_metered")
    val isMetered: Boolean
)

data class CellTowerInfo(
    @SerializedName("cell_id")
    val cellId: Long,
    
    @SerializedName("lac_tac")
    val lacTac: Int,
    
    @SerializedName("mcc")
    val mcc: Int,
    
    @SerializedName("mnc")
    val mnc: Int,
    
    @SerializedName("signal_strength")
    val signalStrength: Int, // RSRP / RSSI
    
    @SerializedName("timing_advance")
    val timingAdvance: Int, // TA index
    
    @SerializedName("is_serving")
    val isServing: Boolean
)

data class WifiAccessPointInfo(
    @SerializedName("bssid")
    val bssid: String,
    
    @SerializedName("ssid")
    val ssid: String,
    
    @SerializedName("rssi")
    val rssi: Int,
    
    @SerializedName("is_hotspot")
    val isHotspot: Boolean
)

data class BleBeaconInfo(
    @SerializedName("uuid")
    val uuid: String,
    
    @SerializedName("major")
    val major: Int,
    
    @SerializedName("minor")
    val minor: Int,
    
    @SerializedName("rssi")
    val rssi: Int
)

data class LocationResult(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("lat")
    val lat: Double,
    
    @SerializedName("lng")
    val lng: Double,
    
    @SerializedName("accuracy_meters")
    val accuracyMeters: Double,
    
    @SerializedName("location_source")
    val locationSource: String, // "BLE_INDOR", "WIFI_WPS", "CELLULAR_TRILATERATION", "CELLULAR_SINGLE"
    
    @SerializedName("resolved_at")
    val resolvedAt: Long
)
