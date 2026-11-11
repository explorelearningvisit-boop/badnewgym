# BAD GYM Cross-Platform Migration Notice (2026-11-11)

The approved new mobile client is Flutter/Dart targeting Android and iPhone (iOS) from one codebase. It will live in `mobile_flutter/`.

The existing Kotlin/Jetpack Compose app under `:app` remains the legacy/reference implementation while Flutter reaches feature parity. Do not overwrite or delete it during migration.

Read:
- [Flutter cross-platform contract](docs/reference/FLUTTER_CROSS_PLATFORM_CONTRACT.md)
- [Flutter execution plan](docs/implementation/FLUTTER_CROSS_PLATFORM_PLAN.md)
- [Repository audit](docs/audit/REPOSITORY_AUDIT_2026-11-11.md)
- [Progress tracker](docs/IMPLEMENTATION_PROGRESS.md)
- [Agent handoff](docs/implementation/AGENT_HANDOFF.md)

Important: the existing `backend/` FastAPI project is low-GPS telemetry only, not the BAD GYM membership/payment backend. The existing Supabase architecture/schema files are proposals pending verification. iOS builds and simulator/signing require macOS + Xcode.

---

# Multi-Project Workspace Guide

This workspace hosts **3 completely independent projects**. Each project has its own dedicated codebase, purpose, and execution commands.

---

```text
📁 WORKSPACE ROOT (badnewgym/)
│
├── 🥊 [PROJECT 1] BAD GYM APP
│   └── app/src/main/.../feature/memberintelligence/   # Gym Member Intelligence V4/V2 UI & Cards
│
├── 📡 [PROJECT 2] PHONE TRACKING ENGINE
│   ├── app/src/main/.../hybridtracking/               # Low-GPS Wi-Fi / Cell / BLE Scanner Service
│   └── backend/                                       # FastAPI Server, Resolver & Live Map
│
└── 📱 [PROJECT 3] OPPO DEV STUDIO & HARDWARE TOOLS
    ├── tools/oppo_dev_studio.py                       # Desktop GUI Phone Controller & Streamer
    ├── OPPO/                                          # OPPO A53 Scan Dumps & Telemetry Reports
    └── Launch_Oppo_Dev_Studio.bat                     # Desktop Launcher
```

---

## 🥊 Project 1: BAD GYM (Member Intelligence Android App)

* **Purpose:** Gym membership management, Member Intelligence V4/V2 attendance cards, unit grid layouts, flow sheets, and payment receipts.
* **Key Files:**
  * UI Screens & ViewModels: [`app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/v4/`](app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/v4/)
  * Layout Engine & Packing: [`app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/`](app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/)
  * Specifications: [`docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md`](docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md)
* **Build & Run:**
  ```powershell
  .\gradlew assembleDebug
  .\gradlew testDebugUnitTest --tests "*Member*"
  ```

---

## 📡 Project 2: PHONE TRACKING (Low-GPS Location & Backend)

* **Purpose:** Low-power hybrid tracking combining Wi-Fi BSSID, Cell Tower CID/LAC, BLE, and GPS with real-time FastAPI resolver and Leaflet live map.
* **Key Files:**
  * Android Scanner: [`app/src/main/java/com/example/badnewgym/hybridtracking/`](app/src/main/java/com/example/badnewgym/hybridtracking/)
  * Backend Server: [`backend/main.py`](backend/main.py)
  * Location Resolver: [`backend/resolver.py`](backend/resolver.py)
  * Interactive Live Map: [`backend/live_map.html`](backend/live_map.html)
  * Backend Launcher: [`backend/run_server.bat`](backend/run_server.bat)
  * Documentation: [`docs/LOW_GPS_HYBRID_TRACKING_ENGINE.md`](docs/LOW_GPS_HYBRID_TRACKING_ENGINE.md)
* **Build & Run:**
  ```powershell
  # Start Backend & Live Map:
  .\backend\run_server.bat
  # (Access map at http://localhost:8000/live-map)

  # Run Android Tracking Unit Tests:
  .\gradlew testDebugUnitTest --tests "*HybridTracking*"
  ```

---

## 📱 Project 3: OPPO (Dev Studio & Hardware Testing)

* **Purpose:** Desktop GUI tool to mirror screens, control test phones (OPPO A53 / Xiaomi 11i) over ADB/Tailscale, and inspect device storage/packages.
* **Key Files:**
  * Desktop Studio GUI: [`tools/oppo_dev_studio.py`](tools/oppo_dev_studio.py)
  * 1-Click Launcher: [`Launch_Oppo_Dev_Studio.bat`](Launch_Oppo_Dev_Studio.bat)
  * OPPO Phone Scans: [`OPPO/OPPO_FULL_MOBILE_SCAN.json`](OPPO/OPPO_FULL_MOBILE_SCAN.json)
  * OPPO Device Report: [`OPPO/OPPO_FULL_MOBILE_SCAN_REPORT.md`](OPPO/OPPO_FULL_MOBILE_SCAN_REPORT.md)
* **Run:**
  * Double-click [`Launch_Oppo_Dev_Studio.bat`](Launch_Oppo_Dev_Studio.bat) or run `python tools\oppo_dev_studio.py`.
