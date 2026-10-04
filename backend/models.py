from typing import List, Optional, Dict, Any
try:
    from pydantic import BaseModel, Field
except ImportError:
    class BaseModel:
        def __init__(self, **kwargs):
            for k, v in kwargs.items():
                setattr(self, k, v)
        def dict(self) -> Dict[str, Any]:
            res = {}
            for k, v in self.__dict__.items():
                if isinstance(v, list):
                    res[k] = [item.dict() if hasattr(item, 'dict') else item for item in v]
                elif hasattr(v, 'dict'):
                    res[k] = v.dict()
                else:
                    res[k] = v
            return res
        def model_dump(self) -> Dict[str, Any]:
            return self.dict()

class ConnectivityInfo(BaseModel):
    network_type: str = "NONE"
    is_metered: bool = False

class CellTowerInfo(BaseModel):
    cell_id: int = 0
    lac_tac: int = 0
    mcc: int = 0
    mnc: int = 0
    signal_strength: int = 0
    timing_advance: int = 0
    is_serving: bool = False

class WifiAccessPointInfo(BaseModel):
    bssid: str = ""
    ssid: str = ""
    rssi: int = 0
    is_hotspot: bool = False

class BleBeaconInfo(BaseModel):
    uuid: str = ""
    major: int = 0
    minor: int = 0
    rssi: int = 0

class TelemetryPayload(BaseModel):
    device_id: str = ""
    timestamp: int = 0
    connectivity: ConnectivityInfo = ConnectivityInfo()
    cell_towers: List[CellTowerInfo] = []
    wifi_access_points: List[WifiAccessPointInfo] = []
    ble_beacons: List[BleBeaconInfo] = []

class LocationResult(BaseModel):
    device_id: str = ""
    lat: float = 0.0
    lng: float = 0.0
    accuracy_meters: float = 0.0
    location_source: str = ""
    resolved_at: int = 0
