# BAD GYM Flutter Agent Handoff

User-approved direction: build a Flutter/Dart app for Android and iPhone from one shared codebase.

## Repository facts
- Repo: explorelearningvisit-boop/badnewgym
- Existing project: Kotlin/Jetpack Compose Android at root.
- Existing app baseline: backup/local-main-2026-11-11 @ 32fccc7a306d93f4c0216156e81e76b4d70f0f48.
- Flutter app does not yet exist at audited baseline: no pubspec.yaml, lib/, ios/ or Flutter tests.
- New isolated app location: mobile_flutter/.
- Preserve existing Kotlin app unchanged as legacy/reference until Flutter parity and QA.
- backend/ is FastAPI low-GPS telemetry only, not membership/attendance/payment business backend.
- Supabase architecture/schema docs are proposals; no active Supabase business client was found.

## First actions
1. Run git status and inspect local uncommitted changes.
2. Read AGENTS.md, CURRENT_TASK.md, docs/IMPLEMENTATION_PROGRESS.md, docs/audit/REPOSITORY_AUDIT_2026-11-11.md, docs/reference/FLUTTER_CROSS_PLATFORM_CONTRACT.md and docs/implementation/FLUTTER_CROSS_PLATFORM_PLAN.md.
3. Confirm Flutter/Dart SDK, Android SDK, and whether a macOS/Xcode build runner is available.
4. Do not run flutter create at repository root; scaffold only in mobile_flutter/.
5. Do not modify the Kotlin app unless an approved migration adapter is specifically needed.

## Architecture
Flutter stable, Dart null safety, Material 3, Riverpod, GoRouter, Dio. Add Freezed/json_serializable, Drift, secure storage and Supabase only after actual needs/schema are confirmed. No blind dependency bundle.

## Product
Five Member Intelligence destinations: Overview, Attendance, Workout, Payment, Body. Use the supplied lavender/pearl and violet reference. Keep semantic status colors and existing business rules. Production values must come from real backend records, never fixture defaults.

## Critical platform caveats
- iOS build/signing/simulator requires macOS + Xcode.
- iOS restricts Wi-Fi/BSSID scanning and background execution versus Android.
- Validate BLE, GPS, camera, notifications, biometric and background-sync plugins on both platforms.
- Do not claim equal access to native hardware APIs where OS policy differs.

## Data blocker
Get the real BAD GYM business API, Supabase project migrations/RLS or backend repository from the user. Do not invent endpoints, tables, secrets or production data. The current FastAPI telemetry backend is not a membership system.

## Agent fallback
DeepSeek → Nomotron → Antigravity 3.8 Flash. On quota exhaustion/restart, checkpoint current SHA, changed files, tests and next exact action. Preserve uncommitted work. New agent must read progress/handoff before continuing.

## Verification
Run Flutter format, analyze and tests. Build Android on Android toolchain; build iOS only on macOS/Xcode. Run integration tests and capture five screens on both platforms at 360/375/393/412 logical widths. Record evidence and SHA.
