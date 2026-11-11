# BAD GYM Flutter Cross-Platform Execution Plan

User-approved target: Flutter app for Android + iPhone from one Dart codebase.
Repository: `explorelearningvisit-boop/badnewgym`
New app root: `mobile_flutter/`
Existing Kotlin app: preserve as reference/legacy until parity and cross-platform QA.

## Phase 0 — Protect current repository
- Check local `git status`, branch, uncommitted changes and current SHA.
- Keep the existing Kotlin `:app` module untouched.
- Use the feature branch and create `mobile_flutter/` as an isolated Flutter app.
- Confirm Flutter SDK, Dart SDK, Android SDK and (on Mac) Xcode versions.
- Do not run `flutter create` at repository root because it is already an Android Gradle project.

## Phase 1 — Backend and capability audit
- Identify actual authentication, member, membership, attendance, workout, payment, body and entitlement APIs/tables.
- The current `backend/` FastAPI project only exposes low-GPS telemetry/location endpoints; it is not the gym business API.
- Current Supabase docs are proposals; verify actual Supabase project/migrations/RLS/auth before adding the Flutter SDK.
- Create a field-to-source matrix for every metric and action.
- Record Android/iOS capability differences for GPS, BLE, camera, notifications and background work.
- No production UI may use fixtures as member data.

## Phase 2 — Flutter scaffold and engineering baseline
- Create `mobile_flutter/` with Android and iOS platform folders.
- Pin Flutter SDK/toolchain and configure lint, formatting, flavors and app IDs.
- Add only baseline dependencies; preferred defaults: Riverpod, GoRouter, Dio, Freezed/json_serializable. Add Drift and secure storage once persistence/auth contract is confirmed.
- Configure CI for Flutter format, analyze and test; Android build on supported runner; iOS build on macOS runner.
- Keep app buildable with a clear loading/empty/error state.

## Phase 3 — Design system and shared shell
- Implement lavender/pearl Material 3 theme from the supplied reference.
- Create shared typography, spacing, radius, elevation, semantic status colors, buttons, chips, cards, chart styles and safe-area layout.
- Implement shared BAD GYM header, member identity, membership summary and bottom navigation.
- Validate 360/375/393/412 logical-pixel layouts on Android and iOS.

## Phase 4 — Data architecture
- Define DTOs and domain models from verified backend schema/API only.
- Implement repository interfaces, Dio/Supabase adapters (whichever backend is actually present), typed errors and auth.
- Add Drift offline cache only after schema mapping.
- Add secure token persistence and role/entitlement checks.
- Realtime and background sync must use platform-appropriate mechanisms and conflict resolution.

## Phase 5 — Overview
- Compose summary cards from real repository state.
- Smart insights must be rule-backed and explainable.
- No fabricated fitness scores or predictions.
- Wire navigation and refresh/loading/empty/error states.

## Phase 6 — Attendance
- Calendar with Present, Late, Absent, Holiday/Off, Sunday and Unknown.
- Unknown/future/unrecorded days must not count as absent.
- Denominator-safe percentages, streaks and trends.
- Preserve check-in and detail flows.

## Phase 7 — Workout
- Aggregate from actual workout session records.
- Daily and monthly trends, duration, averages, best/lowest, active days and streak.
- Missing months are not zero unless the backend confirms zero.
- Preserve workout detail/history actions.

## Phase 8 — Payment
- Use real ledger/transaction records.
- Distinguish Paid, Partial, Due, Overdue and No Record.
- Reconcile totals, discounts, cycles, outstanding and renewal dates.
- Payment success is displayed only after the backend confirms settlement.
- Preserve receipts, collection and reminders.

## Phase 9 — Body
- Use only recorded measurements and consent-authorized records.
- Preserve metric-aware goal direction, units, history, privacy and gallery.
- No inferred health data or fake interpolated series.

## Phase 10 — Platform integrations
- Camera/ML face recognition: verify plugin and privacy behavior on Android/iOS.
- BLE: permission/lifecycle checks on both platforms.
- Wi-Fi/BSSID: document iOS restrictions; do not promise Android-equivalent scanning.
- Notifications, biometrics, location, file/photo picker and background sync: test separately per platform.
- Use MethodChannels/platform interfaces only for genuinely platform-specific gaps.

## Phase 11 — QA and release
- `flutter pub get`
- `dart format --set-exit-if-changed .`
- `flutter analyze`
- `flutter test`
- `flutter build apk --debug` (Android)
- `flutter build ios --no-codesign` on macOS (iOS compile verification)
- Run integration tests on Android emulator/device and iOS simulator/device.
- Capture all five screens on both platforms and compare against reference.
- Verify 360/375/393/412 widths, font scaling, keyboard, safe areas, back gestures and offline/realtime states.
- Do not claim iOS build verification from Windows.

## Agent ownership
- Flutter foundation agent: scaffold, dependencies, theme, routing and shared shell.
- Backend/data agent: API/schema audit, auth, repositories, DTOs and persistence.
- Attendance agent: attendance feature.
- Workout agent: workout feature.
- Payment agent: payment feature and financial tests.
- Body agent: body feature and privacy.
- QA agent: cross-platform test matrix and screenshot comparisons.
- Integration owner: shared contracts, conflict resolution and final merges.
Agents must have non-overlapping file ownership. If isolated worktrees are unavailable, execute sequentially.

## Model fallback
Use DeepSeek → Nomotron → Antigravity 3.8 Flash as requested. At quota exhaustion or restart, stop writes, checkpoint SHA/files/tests/next action in `docs/IMPLEMENTATION_PROGRESS.md` and `docs/implementation/AGENT_HANDOFF.md`. New agent reads these before editing. Never reset unknown local work.

## Current blocker
The actual BAD GYM business backend/schema and local working tree are not accessible from the audited remote source. Existing FastAPI code is telemetry-only. The existing Kotlin app is sample/fixture-driven in the audited route. Obtain the real backend project/schema before production data integration. Scaffold and visual shell can proceed independently.
