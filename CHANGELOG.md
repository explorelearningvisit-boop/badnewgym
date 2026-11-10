# CHANGELOG

All notable changes to the BAD GYM Member Intelligence dashboard.
Format: phase-scoped, newest first. Evidence lives in `docs/reference/images/`.

## [Unreleased] — feature/member-intelligence-v3

### Phase 0 — Audit & documentation
- Added `docs/PROJECT_AUDIT.md`, `docs/EXISTING_FEATURES_INVENTORY.md`,
  `docs/DESIGN_SYSTEM.md`, `docs/IMPLEMENTATION_ROADMAP.md`, `docs/PROJECT_ARCHITECTURE.md`,
  `docs/TEST_REPORT.md`, `docs/IMPLEMENTATION_PROGRESS.md`, `docs/AGENT_HANDOFF.md`,
  `docs/KNOWN_ISSUES.md`, `TASKS.md`.

### Phase 1 — Tokens
- Added `IntelligenceTypography` (Micro 9 sp → Hero 26 sp, nothing below SemiBold).
- Added `IntelligenceDensity` (4–10 dp gaps, 18 dp card radius, 44 dp KPI tiles, 11–17 dp icons).
- Added the **Lavender** skin (`#8239E8` violet on `#F5F3F8`, white cards, `#079B70` success,
  `#E34E58` danger, `#E9AA35` warning) and made it the default; `reset()` now returns to it.

### Phase 2 — Header
- Wordmark locked to one line with ellipsis; left column widened.
- `CHECK-IN` / `CHECKED` pill no longer clips at raised text scale.
- Identity chips, membership progress and KPI tiles moved to the bold scale.

### Phases 3–6 — Modules
- Attendance: taller 12-month trend (132 dp), wider label gutters, badges fully inside.
- Workout: 96 dp daily trend, monthly capsule chart 92 dp, axis labels moved into the gutter.
- Payment: five-pill scroller replaced by a **3 + 2 equal-width grid**; status hero, stat pods,
  behaviour ring (78 dp), plan timeline and trend rescaled; `1.5k/1.0k` axis overlap removed.
- Body: goal ring enlarged to 88 dp, decorative 3D athlete removed, trend gutters widened,
  start-value badge removed from the chart.

### Fixes
- Payment trend badge drawn on every point (mis-ordered `if/else`) → May only, sized to text.
- Behaviour rows and plan-timeline rows no longer truncate into their values.
- Body primary/secondary nav: auto-scroll to the selected tab, edge fades and a chevron.
- ☰ sheet: skins laid out 4 + 3 so "Midnight" is not clipped.

### Verification
- `./gradlew clean testDebugUnitTest assembleDebug` → BUILD SUCCESSFUL.
- Device captures: `f1_attendance`, `f2_workout`, `g3_payment`, `g4_body`, `x1_sheet`.
