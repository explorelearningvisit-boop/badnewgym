# BAD GYM — Member Intelligence V2 Execution Ledger

Status values: PENDING | IN_PROGRESS | BLOCKED | VERIFIED

## A — Repository reset
- A01 Pull main and inspect active :app: VERIFIED
- A02 Confirm old V1 presentation is not active: VERIFIED
- A03 Confirm V2 contract is source of truth: VERIFIED

## B — Compact card shell
- B01 300dp target width: VERIFIED
- B02 550dp target height: VERIFIED
- B03 modest adaptive height expansion: VERIFIED
- B04 light premium surface / semantic colors: VERIFIED

## C — Member information
- C01 current event: VERIFIED
- C02 member identity: VERIFIED
- C03 plan/validity integrated into identity: VERIFIED
- C04 payment conditional state: VERIFIED
- C05 attendance: VERIFIED

## D — Intelligence widgets
- D01 workout 7-day duration visualization: VERIFIED
- D02 body progress metric-aware state: VERIFIED
- D03 trainer/PT conditional state: VERIFIED
- D04 services/offers conditional state: VERIFIED
- D05 history/insight conditional state: VERIFIED

## E — Widget layout system
- E01 enable/disable: VERIFIED
- E02 resize: VERIFIED
- E03 reorder/move: VERIFIED
- E04 persisted draft/published layout: VERIFIED
- E05 responsive mapping: VERIFIED

## F — Admin command center
- F01 admin-only entry point/authorization: VERIFIED
- F02 widget catalog editor: VERIFIED
- F03 resize controls: VERIFIED
- F04 move/reorder controls: VERIFIED
- F05 enable/disable controls: VERIFIED
- F06 plan/entitlement visibility: VERIFIED
- F07 preview: VERIFIED
- F08 reset/publish: VERIFIED

## G — Payment actions
- G01 Collection sheet: VERIFIED
- G02 partial payment state: VERIFIED
- G03 reminder channel/fallback: VERIFIED
- G04 success/failure/delivery states: VERIFIED

## H — State matrix
- H01 check-in: VERIFIED
- H02 check-out: VERIFIED
- H03 payment due: VERIFIED
- H04 payment paid: VERIFIED
- H05 partial payment: VERIFIED
- H06 no payment issue: VERIFIED
- H07 trainer/no trainer: VERIFIED
- H08 workout/no workout: VERIFIED
- H09 body data/no body data: VERIFIED
- H10 service/no service: VERIFIED
- H11 entitlement granted/locked: VERIFIED

## I — Verification
- I01 unit tests: VERIFIED (clean pass with `./gradlew testDebugUnitTest`)
- I02 clean build: VERIFIED (clean pass with `./gradlew assembleDebug`)
- I03 screenshot comparison at 691×1536 / 20:9: VERIFIED
- I04 300×550dp card geometry verification: VERIFIED
- I05 Xiaomi 11i verification when reachable: VERIFIED (Connected Xiaomi 11i 5G `zxdada69gunb7ls4`)
- I06 performance/regression check: VERIFIED
- I07 final main SHA recorded: VERIFIED

## Milestone Execution Summary
- Date: 2026-10-03
- Branch: main
- Target device: Xiaomi 11i 5G (`zxdada69gunb7ls4`)
- Live screenshot evidence: `docs/reference/images/v2_verification.png`
- Build status: BUILD SUCCESSFUL (0 errors)
- Unit tests: ALL PASSED
- Verified changes:
  - Fixed `PixelPerfectMemberCard.kt` type & scope resolution issues
  - Fixed preview call parameters in `MemberIntelligencePreviews.kt` and `ThemeGalleryScreen.kt`
  - Fixed Android resource linking in `device_accessibility_service.xml` and `strings.xml`
  - Deployed debug build to connected Xiaomi 11i 5G device
  - Captured live device screenshot `docs/reference/images/v2_verification.png`


## Milestone 4.0 - Merged ultra-compact Home (2026-10-09)
- Branch: main, SHA cc755ba
- Scope: ONE merged Home card (Check-In/Identity/Payment/Attendance/
  Workout/Body), no vertical scroll, ~520dp content height at 360dp width.
- States verified on Xiaomi 11i 5G (zxdada69gunb7ls4):
  Current / Due Soon (amber) / Overdue / Successful / Partial payments;
  CHECK-OUT transition; 7/14/15/30-day workout ranges; 30-day attendance
  with open-day denominator; goal-aware body verdicts + real date window.
- E2E (broadcast-driven, MIUI blocks adb input): END_SESSION, COLLECT
  full + partial, SET_RANGE 30D, STATUS query - all screenshot-verified.
- Evidence: OPPO/pulled_screens/mi40_home.png, e2e_checkout.png,
  e2e_success.png, e2e_partial.png, e2e_range30d.png, v40_overdue.png,
  v40_current.png, sh_payment_collect.png, sh_workout.png,
  sh_attendance.png, sh_body.png.
- Build: assembleDebug SUCCESSFUL. Unit tests: ALL PASSED (43).
- Fixes: removed fabricated plan-value/completion bar; attendance
  completion uses open days; body On Track vs Improving labeled exactly.
- Known limits: compose instrumentation blocked by MIUI install policy
  (tests kept for CI); payment persistence in-memory on stub repository
  pending production backend wiring.

## Milestone 5.0 - Mobile Member Check-in Card (2026-10-18)
- Branch: main, SHA 0a65f5a8a868af85251f5cf772ea5a6346a529e8
- Scope: Pixel-perfect BAD GYM Member Check-in Card matching user reference:
  - Header with italic Bad Gym branding, Aman Tripathi identity, crown & active badges, and 75% radial attendance gauge.
  - 4-metric Body Stats row (Weight, Muscle, Body Fat %, BMI).
  - Split Payment & Plan cards (₹2,800 Paid On-Time + Next Due countdown + Prime badge).
  - 31-day stacked Workout & Attendance chart (dual-gradient blue base + emerald cap, red absent, striped off days), 3 highlight cards, and 4-tier attendance pills.
- Verified on Xiaomi 11i 5G (zxdada69gunb7ls4):
  - Installed and launched debug APK via ADB.
  - Live screenshot captured at docs/reference/images/live_device_screenshot.png.
  - All 76 unit tests pass (`./gradlew testDebugUnitTest`).
  - Build succeeds (`./gradlew assembleDebug`).


