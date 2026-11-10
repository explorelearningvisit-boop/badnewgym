# BAD GYM — Test Report

Checkpoint: Member Intelligence bold/dense redesign + Phase 0 documentation
Date: 2026-11-10
Branch: `feature/member-intelligence-v3`
Device: Xiaomi 11i 5G `zxdada69gunb7ls4` — 1080×2400 @ 440 dpi, Android 13, MIUI/CIUI

## 1. Build gate

```
./gradlew clean testDebugUnitTest assembleDebug
→ BUILD SUCCESSFUL in 5m 17s
```

- No Kotlin compile errors.
- No failing JVM unit tests.
- Debug APK produced and installed over the previous build (`install -r` → Success).

## 2. Manual device verification

| Screen | Capture | Result |
|---|---|---|
| Attendance | `docs/reference/images/f1_attendance.png` | PASS — gauge, streak, check-in time, 4 status tiles, calendar, taller trend with Jan/Mar/Oct flags, 5 pods, insight |
| Workout | `docs/reference/images/f2_workout.png` | PASS with one cosmetic overlap — peak crown sits close to the chart title |
| Payment | `docs/reference/images/g3_payment.png` | PASS — 3+2 sub-nav grid, status hero, 4 pods, behaviour, timeline, trend (1.5k/0 axis), insight |
| Body | `docs/reference/images/g4_body.png` | PASS — goal ring 46 %, delta pills, composition, measurements, trend, gallery, insights |
| ☰ sheet | `docs/reference/images/x1_sheet.png` | PASS — 4 card colours, 7 skins, 4 sliders, reset |

## 3. Defect regression list (all fixed in this checkpoint)

| # | Defect | Verified fix |
|---|---|---|
| 1 | `BAD GYM` wrapped to 2 lines at raised text scale | single line + ellipsis; left column weight raised |
| 2 | `CHECKED-IN` pill clipped | label shortened to `CHECKED`, green pill, no clip |
| 3 | 5 payment pills hard-cut mid-word | 3 + 2 equal-width grid, no horizontal scroll |
| 4 | Payment trend badge drawn on every point | badge limited to May, sized to its text |
| 5 | Payment Y labels `1.5k`/`1.0k` overlapping | reduced to 2 labels (`1.5k`, `0`) |
| 6 | Chart Y labels drawn at x = 0 (clipped) | left gutter 34–40 dp, labels right-aligned into it |
| 7 | Callout badges cut by plot right edge | right gutter 20–30 dp + badge clamping |
| 8 | Body trend start badge colliding with axis | start value removed from chart (kept in goal card) |
| 9 | Body workspace nav stuck scrolled after tab change | `LaunchedEffect(primaryTab)` reset + auto-scroll to selection |
| 10 | Body secondary nav hard-cut | edge fade + chevron affordance |
| 11 | "Midnight" swatch label clipped in ☰ sheet | swatches laid out 4 + 3 grid, label ellipsised |
| 12 | `reset()` restored the Snow skin instead of the default | reset now targets Lavender |
| 13 | Decorative 3D athlete back in the goal card | removed; donut enlarged to 88 dp |

## 4. Typography / contrast checks

- All text renders at Bold or heavier — no `FontWeight.Normal` remains in the intelligence
  package (verified by inspection of `IntelligenceTypography` usage).
- KPI values use `weightValue` (Black) at 22 sp; card titles 16 sp ExtraBold.
- Semantic colours verified on device: green present/paid, coral absent/late, amber warning,
  violet selection/primary.

## 5. Known gaps (see docs/KNOWN_ISSUES.md)

- Payment and Body tabs scroll a few dozen dp at default scale (content exceeds one screen
  after the type increase); Attendance and Workout fit.
- No instrumented tests; `testDebugUnitTest` covers pre-existing logic only.
- Responsive sweep at 375/393/412 dp not yet executed on this checkpoint.
