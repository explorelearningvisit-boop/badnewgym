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
