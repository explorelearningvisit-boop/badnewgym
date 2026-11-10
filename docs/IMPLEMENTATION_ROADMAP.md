# BAD GYM — Implementation Roadmap

Branch: `feature/member-intelligence-v3` (never commit this work to `main` directly).
Build gate for every phase: `./gradlew assembleDebug testDebugUnitTest` **and** a device
screenshot comparison on `zxdada69gunb7ls4`.

| Phase | Deliverable | Status |
|---|---|---|
| 0 | Repository audit + feature inventory + design system + recovery docs | ✅ this checkpoint |
| 1 | Design tokens: bold type scale, density tokens, Lavender primary skin | ✅ |
| 2 | Header & navigation: wordmark, CHECK-IN pill, identity, progress, KPI tabs | ✅ |
| 3 | Attendance Intelligence reskin | ✅ |
| 4 | Workout Intelligence reskin | ✅ |
| 5 | Payment Intelligence reskin + sub-nav grid | ✅ |
| 6 | Body Intelligence reskin + workspace nav | ✅ |
| 7 | Appearance panel (7 skins + 4 sliders) | ✅ built, needs a final tap-through pass |
| 8 | Overview module (fitness score, cross-module summary, next actions) | ⬜ not started |
| 9 | Real data integration (replace `sampleAshwiniSingh`) | ⬜ needs a data source decision |
| 10 | Chart polish: tooltips, empty states, loading skeletons | ⬜ |
| 11 | Accessibility pass: TalkBack labels, contrast audit, 360/393/412 dp sweeps | ⬜ |
| 12 | Release build + final screenshot set + ledger | ⬜ |

## Phase 0 acceptance (this checkpoint)

- [x] Project structure, stack and entry point documented.
- [x] Every existing member-intelligence feature catalogued with a KEEP/RESKIN/DEFER decision.
- [x] Token set documented with the compiled values.
- [x] Defects found during the audit listed in `docs/PROJECT_AUDIT.md`.
- [x] `assembleDebug` green after the typography/colour re-application.
- [x] Four tab screenshots recaptured from the device.

## Known gaps carried forward

1. The Overview module required by the brief does not exist yet; the four-tab dashboard is
   still the entry surface.
2. Payment and Body tabs scroll slightly at default scale; Attendance and Workout fit one screen.
3. The ☰ appearance panel was verified visually in a previous session but has not been
   re-verified since the typography pass.
4. `testDebugUnitTest` has no coverage for the new typography/density tokens.
