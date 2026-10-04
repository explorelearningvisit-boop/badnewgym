from fastapi import FastAPI, HTTPException
from fastapi.responses import HTMLResponse
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Dict
import time

from models import TelemetryPayload, LocationResult
from resolver import LocationResolver

app = FastAPI(
    title="Low-GPS Hybrid Positioning & Telemetry Sync Engine",
    version="1.0.0",
    description="Cellular, Wi-Fi, and BLE Beacon Triangulation Backend Engine"
)

# Allow CORS for mobile app & web dashboard
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

resolver = LocationResolver()

# In-memory store of latest device reports & resolved locations
device_reports: Dict[str, TelemetryPayload] = {}
device_locations: Dict[str, LocationResult] = {}

@app.post("/api/v1/telemetry/report")
async def report_telemetry(payload: TelemetryPayload):
    """
    Ingestion endpoint for single raw telemetry payload.
    Resolves position and returns location estimate.
    """
    location = resolver.resolve(payload)
    device_reports[payload.device_id] = payload
    device_locations[payload.device_id] = location
    return {
        "status": "success",
        "received_count": 1,
        "location": location
    }

@app.post("/api/v1/telemetry/batch")
async def report_telemetry_batch(payloads: List[TelemetryPayload]):
    """
    Ingestion endpoint for buffered offline batch reports.
    """
    if not payloads:
        raise HTTPException(status_code=400, detail="Empty payload batch")

    latest_location = None
    for payload in payloads:
        location = resolver.resolve(payload)
        device_reports[payload.device_id] = payload
        device_locations[payload.device_id] = location
        latest_location = location

    return {
        "status": "success",
        "received_count": len(payloads),
        "location": latest_location
    }

@app.get("/api/v1/telemetry/location/{device_id}", response_model=LocationResult)
async def get_device_location(device_id: str):
    """
    Retrieves the latest resolved coordinates & accuracy for a given device.
    """
    if device_id in device_locations:
        return device_locations[device_id]
    
    # Return default initial location if device hasn't reported yet
    return LocationResult(
        device_id=device_id,
        lat=28.613930,
        lng=77.209020,
        accuracy_meters=25.0,
        location_source="INITIAL_ESTIMATE",
        resolved_at=int(time.time() * 1000)
    )

@app.get("/map", response_class=HTMLResponse)
async def get_map_view():
    """
    Interactive Leaflet map visualization endpoint displaying device position,
    accuracy radius, cell tower coverage, Wi-Fi access points, and BLE beacons.
    """
    html_content = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Hybrid Positioning & RF Telemetry Map</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        <style>
            body { margin: 0; padding: 0; font-family: 'Segoe UI', sans-serif; background: #0F172A; color: white; }
            #map { width: 100vw; height: calc(100vh - 60px); }
            .header { height: 60px; background: #1E293B; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; border-bottom: 1px solid #334155; }
            .title { font-size: 18px; font-weight: bold; color: #38BDF8; }
            .badge { background: #059669; padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: bold; }
        </style>
    </head>
    <body>
        <div class="header">
            <div class="title">🛰️ Low-GPS Hybrid Positioning & RF Map</div>
            <div class="badge">Live Sync Active</div>
        </div>
        <div id="map"></div>
        <script>
            const map = L.map('map').setView([28.613930, 77.209020], 17);
            
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            // 1. BLE Indoor Beacon Marker
            const bleBeacon = L.circleMarker([28.613930, 77.209020], {
                radius: 8,
                color: '#A855F7',
                fillColor: '#C084FC',
                fillOpacity: 0.9
            }).addTo(map).bindPopup("<b>BLE Beacon (Micro)</b><br>UUID: fda50693-a4e2-4fb1-afcf-c6eb07647825<br>Room: Cardio Zone A");

            // 2. Wi-Fi Access Point Marker
            const wifiAp = L.circleMarker([28.613950, 77.209050], {
                radius: 10,
                color: '#3B82F6',
                fillColor: '#60A5FA',
                fillOpacity: 0.8
            }).addTo(map).bindPopup("<b>Fixed Wi-Fi Router (Meso)</b><br>SSID: Office_Network_5G<br>BSSID: 00:14:22:01:23:45");

            // 3. Cell Tower & Coverage Circle
            const cellTower = L.marker([28.615000, 77.210000]).addTo(map)
                .bindPopup("<b>Serving Cell Tower (Macro)</b><br>Cell ID: 12345678<br>MCC: 404, MNC: 45");

            const cellCoverage = L.circle([28.615000, 77.210000], {
                color: '#F59E0B',
                fillColor: '#FBBF24',
                fillOpacity: 0.15,
                radius: 312
            }).addTo(map).bindPopup("Cell Tower TA Distance Radius (~312m)");

            // 4. Device Resolved Position & Accuracy Radius
            const deviceMarker = L.marker([28.613930, 77.209020]).addTo(map)
                .bindPopup("<b>Resolved Device Location</b><br>Source: BLE_INDOOR<br>Accuracy: ±3.5 meters").openPopup();

            const accuracyCircle = L.circle([28.613930, 77.209020], {
                color: '#10B981',
                fillColor: '#34D399',
                fillOpacity: 0.25,
                radius: 3.5
            }).addTo(map);

            // Periodically refresh position from backend API
            async function refreshLocation() {
                try {
                    const res = await fetch('/api/v1/telemetry/location/default');
                    if (res.ok) {
                        const data = await res.json();
                        deviceMarker.setLatLng([data.lat, data.lng]);
                        accuracyCircle.setLatLng([data.lat, data.lng]);
                        accuracyCircle.setRadius(data.accuracy_meters);
                        deviceMarker.setPopupContent(`<b>Resolved Device Location</b><br>Source: ${data.location_source}<br>Accuracy: ±${data.accuracy_meters}m`);
                    }
                } catch(e) {}
            }
            setInterval(refreshLocation, 3000);
        </script>
    </body>
    </html>
    """
    return html_content
