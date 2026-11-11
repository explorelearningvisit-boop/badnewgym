# BAD GYM Flutter Cross-Platform Multi-Agent Prompts

## MASTER ORCHESTRATOR
Build BAD GYM as a Flutter/Dart application for Android + iOS in `mobile_flutter/`. The existing Kotlin project at repository root is legacy/reference; preserve it. Do not create Flutter files at repository root.

Read AGENTS.md, CURRENT_TASK.md, docs/IMPLEMENTATION_PROGRESS.md, docs/audit/REPOSITORY_AUDIT_2026-11-11.md, docs/reference/FLUTTER_CROSS_PLATFORM_CONTRACT.md, docs/implementation/FLUTTER_CROSS_PLATFORM_PLAN.md and docs/implementation/AGENT_HANDOFF.md before edits.

Use Flutter stable, Dart null safety, Material 3, Riverpod, GoRouter and Dio as defaults. Add packages only when needed and verify Android/iOS support. Do not assume Supabase is implemented: inspect actual migrations, RLS, auth and backend first. The existing FastAPI backend is telemetry-only.

Build five destinations: Overview, Attendance, Workout, Payment, Body. Use the supplied lavender/pearl reference. All production values derive from real records. Preserve privacy, roles, entitlements and payment correctness.

## AGENT A — FLUTTER FOUNDATION
Own only mobile_flutter scaffold, pubspec, app bootstrap, theme, router and shared widgets. Build a minimal app with loading/error/empty states. Deliver Android/iOS scaffold and screenshots.

## AGENT B — BACKEND / DATA
Own API contract, DTOs, repository interfaces, auth, offline persistence and sync. First map every metric to verified API/schema. Never invent endpoint/table names or use service-role keys. Do not write feature UI.

## AGENT C — ATTENDANCE
Own attendance feature folders and tests. Calendar statuses: Present/Late/Absent/Holiday/Off/Sunday/Unknown. Unknown/future days are not absences. Percent denominator must be explicit.

## AGENT D — WORKOUT
Own workout feature folders and tests. Derive trends only from real session records. Missing months are not zero. Test empty history and aggregation.

## AGENT E — PAYMENT
Own payment feature folders and tests. Reconcile transaction ledger; distinguish Paid/Partial/Due/Overdue/No Record. Success only after confirmed backend settlement. Preserve receipt/reminder flows.

## AGENT F — BODY
Own body feature folders and tests. Use recorded metrics only; preserve units, consent, privacy and metric-aware goal direction.

## AGENT G — QA
Read-only until assigned. Verify Android and iOS, 360/375/393/412 widths, font scaling, safe areas, navigation, accessibility, permissions, offline/realtime and all five screen screenshots. iOS builds require macOS/Xcode.

## FILE OWNERSHIP
No two agents may edit the same file. Shared theme/router/bootstrap belong to foundation/orchestrator. Use isolated worktrees or execute sequentially.

## MODEL FALLBACK
DeepSeek → Nomotron → Antigravity 3.8 Flash. On quota exhaustion/restart, stop writes, update progress/handoff with SHA, changed files, tests and next action. New model reads all handoff docs and git status before editing. Never reset unknown local work.

## REQUIRED VERIFICATION
```bash
cd mobile_flutter
flutter pub get
dart format --set-exit-if-changed .
flutter analyze
flutter test
flutter build apk --debug
```
On macOS with Xcode:
```bash
flutter build ios --no-codesign
```
Then integration test both platforms and record screenshots, device/OS, build status, test counts, SHA and unresolved issues.
