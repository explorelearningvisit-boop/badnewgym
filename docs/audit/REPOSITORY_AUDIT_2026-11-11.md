# BAD GYM Repository Audit — 2026-11-11

## Scope and access
- Repository: `explorelearningvisit-boop/badnewgym`
- Production module: `:app`
- Package: `com.example.badnewgym`
- Audited remote baseline: `backup/local-main-2026-11-11`
- Baseline SHA: `32fccc7a306d93f4c0216156e81e76b4d70f0f48`
- Remote `main`: `eefe61e7e12cf42dcb3a8c2fc1e4103df07660f4`
- Backup comparison: 2 commits ahead, 0 behind main.
- Audit is read-only with respect to application source. No Kotlin or Gradle files were changed.
- The connected runtime exposed uploaded reference images, not the user's laptop working directory. Local uncommitted changes and device state have not been inspected.

## Confirmed technology
- Native Android, Kotlin, Jetpack Compose, Material 3.
- Gradle Kotlin DSL; AGP 9.4.0, Kotlin 2.2.10, Gradle 9.6.0.
- compileSdk 37, targetSdk 37, minSdk 24.
- Compose BOM 2026.02.01.
- Existing dependencies: Material 3, extended Material icons, Room 2.6.1, Coil 2.7.0, Retrofit 2.9.0, Gson 2.10.1, OkHttp 4.12.0, WorkManager 2.9.0, JUnit 4.13.2 and Android UI test dependencies.
- Existing charts are largely custom Compose Canvas implementations.
- No additional chart, icon, 3D or animation dependency is justified before the current components are audited.

## Runtime entry path
`MainActivity.kt` sets content to `MemberCheckInScreen(initialSheet = ...)`.
`MemberCheckInScreen.kt` defaults its data argument to `MemberCardData.sampleAshwaniSingh()` and routes to `MemberDashboardV2Screen`.
`MemberDashboardV2Screen.kt` composes `MasterStoryboardDashboard` and the existing receipt, analytics, attendance-day, search, notification and quick-action sheets.

## Existing feature inventory
- Shared BAD GYM header and member identity.
- Four top-level summary tabs: Attendance, Workout, Payment and Body.
- Overview / History / Plan & Changes / Behaviour secondary navigation patterns.
- Attendance calendar, attendance trend and day detail sheet.
- Workout daily trend and monthly pattern.
- Payment status, plan, renewal, behaviour, plan history, receipt and payment trend.
- Body Intelligence is a separate modular workspace under `feature/bodyprogress/presentation/intelligence/BodyIntelligenceDashboard.kt`.
- Existing bottom sheets and navigation affordances should be retained and audited rather than replaced blindly.
- Existing 10-theme engine was added on the backup branch. It includes multiple dark/neon themes and a quick selector; the new lavender reference should become the default and sole active presentation for this task. Do not show a theme picker in the member-facing dashboard unless separately requested.

## Confirmed risks / defects to resolve
1. **Demo-data default:** the production screen signature currently defaults to `MemberCardData.sampleAshwaniSingh()`. Trace the complete runtime data flow and replace the production default with repository/ViewModel state. Sample fixtures may remain preview/test-only.
2. **Unused static workout array:** `MasterStoryboardDashboard.kt` defines a fixed 30-day `workoutBars` list, but repository-wide usage within that file shows it is not referenced beyond its declaration. Confirm no external use before removing it.
3. **Hard-coded monthly workout:** `monthlyWorkoutPattern` is a fixed Jan–Oct series consumed by the monthly workout card; replace it with aggregation from actual session records.
4. **Hard-coded payment trend:** `paymentTrendData()` returns a fixed Jan–Oct series consumed by the payment chart; replace it with aggregation from actual settled/partial transaction records.
5. **Identity consistency:** `MemberDashboardV2Screen` derives the sheet identity from `BadGymMemberPass.getAll9Passes().first()`; verify it matches the active member snapshot before retaining this behavior.
6. **Visual-contract drift:** repository docs still prescribe a 300×550dp compact emerald/mint card. The newly supplied four-screen lavender/purple reference explicitly changes this task to a full-screen multi-tab dashboard. A task-specific contract and overrides have now been added on the feature branch.
7. **Theme drift:** the backup contains a 10-theme selector including dark/neon variants. Retire it from the member-facing screen for this design; keep source assets only if another feature still depends on them.
8. **Duplicate/legacy presentations:** inspect the active route and references before deleting components. Preserve domain, data, payment, navigation, privacy and entitlement behavior.

## Git / CI findings
- Remote `main` latest inspected commit: `eefe61e7...` (2026-11-02).
- Latest inspected main-branch Android Build workflow for that SHA: completed successfully.
- The backup branch commit message records a Xiaomi 11i build/device verification for the 10-theme work; no GitHub Actions run was returned for the backup branch.
- Open PR #12 targets a compact 200×300dp card and is not the new full-screen reference target.
- Open PR #13 enforces a light-only/no-black UI; its no-dark principle is compatible, but do not merge it blindly.
- Do not merge either open PR as a shortcut. Review their changes against the new contract.

## Visual source of truth for this task
The latest user-supplied composite screenshot is authoritative:
- Overview screen
- Attendance Intelligence screen
- Workout Intelligence screen
- Payment Intelligence screen
The previously supplied Body Intelligence screen remains the fifth module reference.
Use a light lavender/pearl canvas, white/lilac neumorphic cards, violet active navigation, emerald positive states, semantic amber/red/grey, compact readable typography, fine chart axes, soft shadows and consistent rounded geometry.
This replaces the previous compact-card/emerald-only visual target for this task only.

## Audit limitations
- The laptop's local filesystem is not mounted in this session; only conversation reference images are mounted.
- No Gradle build, emulator launch, physical-device screenshot or local Git working-tree status was executed in this session.
- Before implementation, the local owner must sync/commit/stash uncommitted work or provide the current working tree to the coding agent. Never overwrite unknown local changes.
