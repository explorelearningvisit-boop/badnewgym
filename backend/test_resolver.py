import sys
sys.path.append('backend')
from resolver import LocationResolver
from models import TelemetryPayload, ConnectivityInfo, CellTowerInfo, WifiAccessPointInfo, BleBeaconInfo
import json

resolver = LocationResolver()

cell_towers = [
    CellTowerInfo(cell_id=12345678, lac_tac=4512, mcc=404, mnc=45, signal_strength=-85, timing_advance=4, is_serving=True),
    CellTowerInfo(cell_id=12345679, lac_tac=4512, mcc=404, mnc=45, signal_strength=-92, timing_advance=6, is_serving=False),
    CellTowerInfo(cell_id=12345680, lac_tac=4512, mcc=404, mnc=45, signal_strength=-98, timing_advance=8, is_serving=False),
]

wifi_aps = [
    WifiAccessPointInfo(bssid='00:14:22:01:23:45', ssid='Office_Network_5G', rssi=-62, is_hotspot=False),
    WifiAccessPointInfo(bssid='18:E8:29:4F:11:90', ssid='Gym_Member_Wi-Fi', rssi=-71, is_hotspot=False),
    WifiAccessPointInfo(bssid='DA:A1:19:88:44:11', ssid="John's iPhone", rssi=-58, is_hotspot=True)
]

ble_beacons = [
    BleBeaconInfo(uuid='fda50693-a4e2-4fb1-afcf-c6eb07647825', major=1, minor=102, rssi=-55)
]

# 1. Full Payload with Micro BLE
payload_full = TelemetryPayload(
    device_id='DEVICE_TEST_404',
    timestamp=1728000000000,
    connectivity=ConnectivityInfo(network_type='WIFI', is_metered=False),
    cell_towers=cell_towers,
    wifi_access_points=wifi_aps,
    ble_beacons=ble_beacons
)

# 2. Payload with Wi-Fi only (No BLE)
payload_wifi = TelemetryPayload(
    device_id='DEVICE_TEST_404',
    timestamp=1728000000000,
    connectivity=ConnectivityInfo(network_type='WIFI', is_metered=False),
    cell_towers=cell_towers,
    wifi_access_points=wifi_aps,
    ble_beacons=[]
)

# 3. Payload with Cellular only (No Wi-Fi, No BLE)
payload_cell = TelemetryPayload(
    device_id='DEVICE_TEST_404',
    timestamp=1728000000000,
    connectivity=ConnectivityInfo(network_type='CELLULAR', is_metered=True),
    cell_towers=cell_towers,
    wifi_access_points=[],
    ble_beacons=[]
)

print('==================================================================')
print('=== 1. MICRO LEVEL (BLE INDOOR ROOM BEACON RESOLUTION) =========')
print('==================================================================')
loc_ble = resolver.resolve(payload_full)
print(json.dumps(loc_ble.dict(), indent=2))

print('\n==================================================================')
print('=== 2. MESO LEVEL (FIXED WI-FI ACCESS POINT RSSI CENTROID) ======')
print('==================================================================')
loc_wifi = resolver.resolve(payload_wifi)
print(json.dumps(loc_wifi.dict(), indent=2))

print('\n==================================================================')
print('=== 3. MACRO LEVEL (MULTI-CELL TOWER TRILATERATION & TA) ========')
print('==================================================================')
loc_cell = resolver.resolve(payload_cell)
print(json.dumps(loc_cell.dict(), indent=2))
