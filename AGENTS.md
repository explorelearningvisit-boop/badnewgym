# USER-APPROVED PLATFORM OVERRIDE — FLUTTER ANDROID + iOS (2026-11-11)

The user has explicitly selected Flutter so BAD GYM can target Android and iPhone from one shared Dart codebase. This decision supersedes the earlier Kotlin-only implementation recommendation.

Read first:
- `docs/reference/FLUTTER_CROSS_PLATFORM_CONTRACT.md`
- `docs/implementation/FLUTTER_CROSS_PLATFORM_PLAN.md`
- `docs/implementation/AGENT_HANDOFF.md`
- `docs/IMPLEMENTATION_PROGRESS.md`

Create the new Flutter app only under `mobile_flutter/`. The existing root Kotlin/Jetpack Compose app is legacy/reference and must remain intact until Flutter reaches feature parity and Android+iOS QA. Do not run `flutter create` at repository root.

Use Flutter stable, Dart null safety, Material 3, Riverpod, GoRouter and Dio as initial defaults. Add packages only when required. The existing FastAPI backend is telemetry-only; Supabase docs are proposals, not proof of a live member/payment backend. Do not invent business endpoints or treat sample fixtures as production data.

iOS build/simulator/signing requires macOS + Xcode. Verify plugin capabilities independently on both platforms; iOS restricts Wi-Fi/BSSID scanning and background work compared with Android.

The previous lavender full-screen visual contract remains active, but its implementation target is Flutter. Preserve existing Kotlin code as a reference only.
---

# TASK-SPECIFIC OVERRIDE — 2026-11-11

For the active full-screen Member Intelligence lavender redesign, use:
`docs/reference/MEMBER_INTELLIGENCE_LAVENDER_REFERENCE_CONTRACT.md`
`docs/audit/REPOSITORY_AUDIT_2026-11-11.md`
`docs/implementation/MEMBER_INTELLIGENCE_LAVENDER_IMPLEMENTATION_PLAN.md`
`docs/implementation/AGENT_HANDOFF.md`
`docs/IMPLEMENTATION_PROGRESS.md`

These task-specific documents supersede older Member Intelligence visual geometry and palette instructions (including the 300×550dp compact-card target and emerald/mint-only palette) for this redesign only. The older V2 contract remains authoritative for non-visual domain rules: real data, payment correctness, attendance semantics, privacy, entitlements, widget authorization and admin/member separation.

The current reference is a full-screen, five-destination mobile experience: Overview, Attendance, Workout, Payment and Body. Use light lavender/pearl surfaces, violet navigation, emerald positive states and semantic amber/rose/grey. Do not introduce dark/neon theme selection, decorative 3D or fake metrics.

Do not implement in `BAD_GYM_MEMBER_INTELLIGENCE_READY/`. Do not modify `OPPO/` or device-agent tooling. Do not overwrite local uncommitted changes.

---

# BAD GYM — Repository Execution Rules

## ACTIVE PROJECT
Production Android project is repository root Gradle project, module /app, package com.example.badnewgym.
Run Gradle commands from repository root.

## LEGACY / OUT OF SCOPE
BAD_GYM_MEMBER_INTELLIGENCE_READY/ is legacy/reference only and must not receive current implementation work.

## CURRENT MEMBER INTELLIGENCE SOURCE OF TRUTH
1. AGENTS.md
2. AI_COLLABORATION_PROTOCOL.md
3. CURRENT_TASK.md
4. docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md
5. docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md
6. docs/reference/BAD_GYM_MOBILE_PIXEL_PERFECT_GUIDE.md
7. actual production source under app/src/main/
8. tests

The old Member Intelligence presentation is obsolete. Do not restore it.

## EXECUTION MODEL
Pull latest main first.
Inspect complete active project.
Implement the current V2 contract atomically.
Build → run → screenshot → compare → fix → rebuild → verify.
Never declare completion from compilation alone.

## GIT
Current implementation work is intended for main.
Record exact SHA, changed files, build/test/device/screenshot evidence at verified milestones.

## BUILD
./gradlew clean testDebugUnitTest assembleDebug