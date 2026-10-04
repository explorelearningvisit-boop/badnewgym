# Low-GPS Hybrid Tracking & Telemetry Sync Engine

## Architecture & System Overview
This end-to-end system estimates device location using raw Radio Frequency (RF) network metadata—Cellular, Wi-Fi, and Bluetooth LE—without relying primarily on GPS hardware.

---

## 1. Android Client Architecture (Kotlin)
- **Foreground / Background Service (`NetworkScannerService`):**
  - Maintains continuous scanning and runs as a Foreground Service with location foreground service type.
  - Controls scanning loops, updates UI state, and dispatches batches to backend.
- **Background Periodic Sync (`TelemetrySyncWorker`):**
  - Uses `WorkManager` with `NetworkType.CONNECTED` constraint for periodic background scanning and buffer flushing.
- **Metadata Scanner Repository (`TelemetryScannerRepository`):**
  - **Cellular (`CellTowerScanner`):** Queries `TelephonyManager.allCellInfo` across LTE, GSM, WCDMA, NR (5G). Extracts MCC, MNC, LAC/TAC, CID, Signal Strength (RSRP/RSSI), and Timing Advance (TA).
  - **Wi-Fi (`WifiScanner`):** Queries `WifiManager.scanResults`. Applies vendor OUI prefix lookups, locally administered MAC bit checks, and mobility heuristics to distinguish fixed routers from mobile hotspots.
  - **Bluetooth LE (`BleScanner`):** Scans BLE advertising frames via `BluetoothLeScanner`. Parses iBeacon 0x0215 headers to extract UUID (128-bit), Major, Minor, RSSI, and TX Power.
- **Offline Caching & Database (`TelemetryDatabase` / `TelemetryDao`):**
  - Stores un-synced telemetry payloads in Room database with UTC timestamps.
- **Connectivity & Network Panel (`NetworkObserver`):**
  - Observes network type and metered state via `ConnectivityManager.NetworkCallback`.
  - Prompts Android 10+ Wi-Fi settings panel gracefully using `Settings.Panel.ACTION_WIFI`.
- **Batch Dispatcher (`BatchDispatcher`):**
  - Flushes buffered Room DB payloads to Retrofit API `/api/v1/telemetry/report` or `/api/v1/telemetry/batch` with exponential backoff retry.

---

## 2. Backend Processing Engine (Python FastAPI)
- **Location Resolver Hierarchy (`LocationResolver`):**
  1. **Micro Level (BLE Indoor Beacon):**
     - Matches UUID + Major + Minor against registered indoor layout database.
     - Provides room/floor-level precision (**2 – 5 meters**).
  2. **Meso Level (Wi-Fi WPS):**
     - Filters out mobile hotspots (`is_hotspot=true`).
     - Computes RSSI-weighted centroid across fixed router BSSIDs (**10 – 25 meters**).
  3. **Macro Level (Cellular Triangulation & TA Distance):**
     - Calculates cell distance: $\text{Distance (meters)} = \text{Timing Advance} \times 78.12$.
     - Performs weighted trilateration if $\ge 3$ cell towers visible; otherwise falls back to single serving cell position (**150 – 300+ meters**).
- **Endpoints:**
  - `POST /api/v1/telemetry/report`: Ingest single payload.
  - `POST /api/v1/telemetry/batch`: Ingest offline batch array.
  - `GET /api/v1/telemetry/location/{device_id}`: Retrieve latest resolved position.
  - `GET /map`: Dynamic Leaflet.js interactive map visualization rendering live device position, accuracy circle, cell tower coverage, Wi-Fi AP pins, and BLE beacons.

---

## 3. Data Contract (JSON Payload Schema)
```json
{
  "device_id": "DEVICE_UUID_OR_SECURE_ID",
  "timestamp": 1728000000000,
  "connectivity": {
    "network_type": "WIFI",
    "is_metered": false
  },
  "cell_towers": [
    {
      "cell_id": 12345678,
      "lac_tac": 4512,
      "mcc": 404,
      "mnc": 45,
      "signal_strength": -85,
      "timing_advance": 4,
      "is_serving": true
    }
  ],
  "wifi_access_points": [
    {
      "bssid": "00:14:22:01:23:45",
      "ssid": "Office_Network_5G",
      "rssi": -62,
      "is_hotspot": false
    }
  ],
  "ble_beacons": [
    {
      "uuid": "fda50693-a4e2-4fb1-afcf-c6eb07647825",
      "major": 1,
      "minor": 102,
      "rssi": -55
    }
  ]
}
```
