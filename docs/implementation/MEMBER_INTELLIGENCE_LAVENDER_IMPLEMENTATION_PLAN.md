# BAD GYM Lavender Member Intelligence — Repository-Specific Implementation Plan

Baseline branch: `backup/local-main-2026-11-11`
Baseline SHA: `32fccc7a306d93f4c0216156e81e76b4d70f0f48`
Working branch: `feature/member-intelligence-lavender-rebuild`
Platform: Kotlin / Jetpack Compose / :app

## Phase 0 — Safety and baseline
- Confirm local working tree is committed/stashed; inspect `git status`.
- Fetch the selected baseline and record exact SHA.
- Read AGENTS.md, AI_COLLABORATION_PROTOCOL.md, CURRENT_TASK.md, V2 contract, Antigravity contract and this audit.
- Do not edit OPPO/, device-agent tooling or unrelated modules.
- Create a checkpoint before source changes.

## Phase 1 — Source and data-flow audit
- Trace MainActivity → MemberCheckInScreen → MemberDashboardV2Screen → MasterStoryboardDashboard.
- Trace member identity, attendance, workout, payment and body repositories/ViewModels.
- Identify sample fixtures, static chart arrays, duplicated calculations and unused legacy screens.
- Produce a field-to-source matrix for every displayed number.
- Do not begin visual replacement until every metric has a source or is explicitly marked unavailable.

## Phase 2 — Reference decomposition and design tokens
- Decompose the uploaded reference into shared shell, profile, summary chips, secondary nav, module cards, charts, insights and bottom navigation.
- Define lavender, surface, violet, emerald, amber, rose, neutral, typography, spacing, radius, border and shadow tokens.
- Map all five modules to the same component and type scale.
- Retire the 10-theme selector from the member-facing dashboard; do not delete unrelated theme assets until references are checked.

## Phase 3 — Shared shell and Overview
- Implement shared header/profile/membership summary and navigation.
- Build Overview from existing real domain summaries.
- Keep one source of truth for each metric; remove duplicated values.
- Wire search, notifications, check-in and navigation actions.

## Phase 4 — Attendance
- Reuse existing calendar domain model and day sheet.
- Implement calendar state colors, month navigation, denominator-safe statistics and monthly trend.
- Add tests for month boundaries, future/unknown/closed days and rate calculations.

## Phase 5 — Workout
- Replace static 30-day and monthly arrays with repository-derived session series.
- Implement duration ring/summary, daily trend and monthly pattern.
- Add tests for empty history, zero duration, missing months, peak/lowest and average calculations.

## Phase 6 — Payment
- Replace static monthly payment series with ledger-derived transactions.
- Reconcile lifetime paid, outstanding, cycles, discounts and renewal dates.
- Preserve collection, partial payment, receipt and reminder flows.
- Add tests for paid, partial, overdue, no-record and renewal edge cases.

## Phase 7 — Body
- Reuse Body Intelligence domain analytics, measurements, gallery and privacy state.
- Match the supplied lavender/purple card system without changing metric semantics.
- Test missing readings, units, goal direction and date-range filters.

## Phase 8 — Integration and accessibility
- Integrate five destinations into the existing navigation graph.
- Validate system-bar insets, TalkBack labels, contrast, touch targets, font scale and dynamic content.
- Remove only confirmed-dead legacy UI from the active route; preserve shared domain/data/sheets.

## Phase 9 — Verification
Run from repository root:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```
Also run available Android instrumentation/UI tests.
- Install and launch on Xiaomi 11i-class device or emulator.
- Capture Overview, Attendance, Workout, Payment and Body screens.
- Compare with the supplied reference at the same viewport and density.
- Validate 360, 375, 393 and 412dp widths.
- Check light system mode and forced light palette.
- Verify charts against raw records and payment calculations.
- Rebuild after every visual correction.

## Phase 10 — Handoff and release
- Update docs/IMPLEMENTATION_PROGRESS.md and docs/AGENT_HANDOFF.md.
- Record changed files, SHA, build/test/device status, screenshot paths and unresolved issues.
- Commit only verified work to the feature branch.
- Open a PR after QA; do not push unverified changes to main.

## Agent ownership
- Orchestrator: source-of-truth, dependency graph, integration and checkpoints.
- Visual agent: shared tokens, shell, overview and screenshot comparison.
- Attendance agent: attendance screen + domain tests.
- Workout agent: workout screen + domain tests.
- Payment agent: payment screen + ledger tests.
- Body agent: body workspace visual integration + metric tests.
- QA agent: accessibility, build, regression and device screenshots.

Parallel agents must have non-overlapping file ownership. One integration agent owns shared shell, navigation, tokens and final merges. If independent agents are unavailable, execute the same workstreams sequentially.

## Model fallback and recovery
Requested order: DeepSeek → Nomotron → Antigravity 3.8 Flash.
On quota exhaustion or restart:
1. Save work and stop writes.
2. Update progress and handoff docs.
3. Record modified files and current SHA.
4. New agent reads repository instructions, progress, handoff and git status.
5. Resume at the first unverified task; never regenerate completed files or reset unknown changes.
