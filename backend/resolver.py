import math
import time
from typing import Dict, Tuple, List, Optional
from models import TelemetryPayload, LocationResult, CellTowerInfo, WifiAccessPointInfo, BleBeaconInfo

# Registered indoor BLE Beacon Registry (Floor / Room coordinates)
REGISTERED_BLE_BEACONS = {
    ("fda50693-a4e2-4fb1-afcf-c6eb07647825", 1, 102): {
        "name": "Gym Training Zone A (Floor 2)",
        "lat": 25.278450,
        "lng": 82.949380,
        "room": "Zone A - Cardio Area"
    },
    ("fda50693-a4e2-4fb1-afcf-c6eb07647825", 1, 103): {
        "name": "Gym Weight Room (Floor 2)",
        "lat": 25.278480,
        "lng": 82.949420,
        "room": "Zone B - Free Weights"
    }
}

# Registered Wi-Fi BSSID Geolocation Database (Fixed Routers)
KNOWN_WIFI_BSSIDS = {
    "98:9D:B2:AB:1C:E0": {"lat": 25.278508, "lng": 82.949438, "ssid": "SITI BRODBAND 5G"},
    "98:9D:B2:AB:1C:E4": {"lat": 25.278508, "lng": 82.949438, "ssid": "SITI BRODBAND 2.4G"},
    "4C:AE:1C:B5:20:95": {"lat": 25.278530, "lng": 82.949460, "ssid": "SULTAN -5G"},
    "34:24:3E:5C:42:3C": {"lat": 25.278560, "lng": 82.949490, "ssid": "Airtel_Lado"}
}

# Registered OpenCelliD / Carrier Cell Towers (Cell ID -> Lat/Lng)
KNOWN_CELL_TOWERS = {
    5657872: {"lat": 25.279200, "lng": 82.950100, "mcc": 405, "mnc": 871},
    64853: {"lat": 25.278000, "lng": 82.948800, "mcc": 404, "mnc": 55},
    64851: {"lat": 25.277800, "lng": 82.948500, "mcc": 404, "mnc": 55},
    12345678: {"lat": 25.279200, "lng": 82.950100, "mcc": 405, "mnc": 871},
    12345679: {"lat": 25.279500, "lng": 82.950500, "mcc": 405, "mnc": 871},
    12345680: {"lat": 25.277500, "lng": 82.948000, "mcc": 404, "mnc": 55}
}

class LocationResolver:
    def resolve(self, payload: TelemetryPayload) -> LocationResult:
        now_ts = int(time.time() * 1000)

        # 1. Micro Level (BLE Indoor Beacon Matching)
        ble_result = self._resolve_ble(payload.ble_beacons)
        if ble_result:
            lat, lng, accuracy = ble_result
            return LocationResult(
                device_id=payload.device_id,
                lat=lat,
                lng=lng,
                accuracy_meters=accuracy,
                location_source="BLE_INDOOR",
                resolved_at=now_ts
            )

        # 2. Meso Level (Wi-Fi WPS Geolocation)
        wifi_result = self._resolve_wifi(payload.wifi_access_points)
        if wifi_result:
            lat, lng, accuracy = wifi_result
            return LocationResult(
                device_id=payload.device_id,
                lat=lat,
                lng=lng,
                accuracy_meters=accuracy,
                location_source="WIFI_WPS",
                resolved_at=now_ts
            )

        # 3. Macro Level (Cellular Triangulation & Serving Cell TA Distance)
        cell_result = self._resolve_cellular(payload.cell_towers)
        if cell_result:
            lat, lng, accuracy, source = cell_result
            return LocationResult(
                device_id=payload.device_id,
                lat=lat,
                lng=lng,
                accuracy_meters=accuracy,
                location_source=source,
                resolved_at=now_ts
            )

        # Default fallback location if no signals match
        return LocationResult(
            device_id=payload.device_id,
            lat=25.278508,
            lng=82.949438,
            accuracy_meters=100.0,
            location_source="DEFAULT_FALLBACK",
            resolved_at=now_ts
        )

    def _resolve_ble(self, beacons: List[BleBeaconInfo]) -> Optional[Tuple[float, float, float]]:
        """Matches indoor beacon UUID, Major, Minor against indoor room registry."""
        matched = []
        for b in beacons:
            key = (b.uuid.lower(), b.major, b.minor)
            if key in REGISTERED_BLE_BEACONS:
                info = REGISTERED_BLE_BEACONS[key]
                # Weight by RSSI (signal strength)
                weight = 10 ** (b.rssi / 20.0)
                matched.append((info["lat"], info["lng"], weight, b.rssi))

        if not matched:
            return None

        total_weight = sum(m[2] for m in matched)
        weighted_lat = sum(m[0] * m[2] for m in matched) / total_weight
        weighted_lng = sum(m[1] * m[2] for m in matched) / total_weight
        # Indoor room accuracy (2 to 5 meters)
        accuracy = 3.5
        return weighted_lat, weighted_lng, accuracy

    def _resolve_wifi(self, aps: List[WifiAccessPointInfo]) -> Optional[Tuple[float, float, float]]:
        """Calculates RSSI weighted centroid across non-hotspot fixed Wi-Fi access points."""
        fixed_aps = [ap for ap in aps if not ap.is_hotspot]
        if not fixed_aps:
            return None

        matched = []
        for ap in fixed_aps:
            bssid_upper = ap.bssid.upper()
            if bssid_upper in KNOWN_WIFI_BSSIDS:
                pos = KNOWN_WIFI_BSSIDS[bssid_upper]
                # Signal weight conversion from RSSI
                weight = 10 ** ((ap.rssi + 100) / 20.0)
                matched.append((pos["lat"], pos["lng"], weight))

        if not matched:
            return None

        total_weight = sum(m[2] for m in matched)
        lat = sum(m[0] * m[2] for m in matched) / total_weight
        lng = sum(m[1] * m[2] for m in matched) / total_weight
        # Wi-Fi positioning accuracy (10 to 25 meters)
        accuracy = 12.0
        return lat, lng, accuracy

    def _resolve_cellular(self, towers: List[CellTowerInfo]) -> Optional[Tuple[float, float, float, str]]:
        """
        Calculates location via cellular network metadata.
        Timing Advance distance formula: Distance (meters) = TA * 78.12
        Trilateration performed when >= 3 towers visible.
        """
        if not towers:
            return None

        serving = next((t for t in towers if t.is_serving), towers[0])

        # Distance calculation based on Timing Advance (TA)
        ta_distance = serving.timing_advance * 78.12
        if ta_distance < 100.0:
            ta_distance = 312.48 # Minimum TA resolution (~4 * 78.12)

        matched_towers = []
        for t in towers:
            if t.cell_id in KNOWN_CELL_TOWERS:
                pos = KNOWN_CELL_TOWERS[t.cell_id]
                dist = t.timing_advance * 78.12 if t.timing_advance > 0 else 500.0
                matched_towers.append((pos["lat"], pos["lng"], dist, t.signal_strength))

        if len(matched_towers) >= 3:
            # Multi-cell Trilateration
            lat, lng = self._trilaterate(matched_towers)
            return lat, lng, 150.0, "CELLULAR_TRILATERATION"

        # Single serving cell fallback
        if serving.cell_id in KNOWN_CELL_TOWERS:
            pos = KNOWN_CELL_TOWERS[serving.cell_id]
            return pos["lat"], pos["lng"], max(ta_distance, 200.0), "CELLULAR_SINGLE"

        return 25.279200, 82.950100, max(ta_distance, 300.0), "CELLULAR_ESTIMATED"

    def _trilaterate(self, towers: List[Tuple[float, float, float, int]]) -> Tuple[float, float]:
        """Weighted centroid approximation for multi-tower trilateration."""
        total_w = 0.0
        w_lat = 0.0
        w_lng = 0.0
        for lat, lng, dist, rssi in towers:
            weight = 1.0 / (dist + 1.0)
            total_w += weight
            w_lat += lat * weight
            w_lng += lng * weight
        return w_lat / total_w, w_lng / total_w
