# BAD GYM — Multi-Agent Execution Prompts (DeepSeek → Nomotron → Antigravity)

Use this document as the shared instruction set for all coding agents. The active task contract and repository audit are mandatory inputs.

## MASTER ORCHESTRATOR PROMPT

You are the principal Android architect and integration lead for BAD GYM. This is an existing Kotlin / Jetpack Compose app, not a new React/Flutter project.

Before editing:
- Read AGENTS.md, CURRENT_TASK.md, docs/IMPLEMENTATION_PROGRESS.md, docs/audit/REPOSITORY_AUDIT_2026-11-11.md, docs/reference/MEMBER_INTELLIGENCE_LAVENDER_REFERENCE_CONTRACT.md, docs/implementation/MEMBER_INTELLIGENCE_LAVENDER_IMPLEMENTATION_PLAN.md and docs/implementation/AGENT_HANDOFF.md.
- Run git status and record branch + HEAD.
- Inspect the local working tree and preserve uncommitted work.
- Confirm the baseline commit and the active production route.
- Build a metric-to-source matrix before changing charts.

Mission:
Replace the active Member Intelligence presentation with five responsive full-screen Compose destinations: Overview, Attendance, Workout, Payment and Body. Match the supplied light lavender / violet / emerald reference. Preserve backend, domain semantics, payment correctness, offline behavior, privacy and entitlement checks.

Never:
- migrate frameworks;
- use sample fixtures as production data;
- hardcode chart history;
- create fake AI predictions;
- merge unrelated old PRs;
- delete domain/data/business logic with old UI;
- add decorative 3D or random skill packs;
- run two agents on the same source files;
- force push or reset unknown work.

Implement in small stages. After each stage: build → run → screenshot → compare → fix → rebuild → verify → checkpoint. Compilation alone is not completion.

## SUBAGENT 1 — VISUAL SYSTEM & SHARED SHELL

Scope:
- Inspect existing design tokens, MasterStoryboardDashboard.kt, InfographicMasterComponents.kt, MemberCheckInScreen.kt and MemberDashboardV2Screen.kt.
- Create the lavender/pearl palette and consistent typography/spacing/radius/elevation tokens.
- Implement the shared app header, member identity/membership block, four summary chips, contextual tabs and bottom navigation.
- Ensure 360dp-first responsive layout and Android system-bar insets.
- Remove the 10-theme selector from the active member dashboard; do not delete theme code still used elsewhere.
- Do not modify attendance/workout/payment/body business calculations.

Deliver:
- Shared tokens and shell components.
- Screenshot of the shell at 360dp.
- Unit/UI tests for selected tab and navigation state.
- File list and exact SHA.

## SUBAGENT 2 — ATTENDANCE

Scope:
- Inspect AttendanceSectionData, CalendarDay, DayAttendanceStatus, AttendanceCalendarSection.kt and existing day-detail sheet.
- Render real month calendar and correct Present/Late/Absent/Holiday/Off/Sunday/Unknown states.
- Derive percentage, denominator, counts, streak and trend from actual records.
- Unknown/future days are not absences.
- Preserve month navigation and day detail behavior.

Deliver:
- Attendance destination and reusable components.
- Date/denominator edge-case unit tests.
- Screenshot comparison and file list.

## SUBAGENT 3 — WORKOUT

Scope:
- Inspect WorkoutTrendData, WorkoutTrendBar, session source and workout detail flow.
- Replace static workout/monthly arrays with actual workout session records.
- Derive total hours, avg/day, best, lowest, active days, streak, goal and monthly pattern.
- Missing months must not be represented as zero unless actual zero records are known.
- Preserve detail navigation and date range controls.

Deliver:
- Workout destination and chart components.
- Tests for empty history, zero sessions, peak/lowest and monthly aggregation.
- Screenshot comparison and file list.

## SUBAGENT 4 — PAYMENT

Scope:
- Inspect PaymentPlanState, transaction/ledger source, receipt sheet, collection flow and reminder flow.
- Replace static payment trend arrays with actual settled/partial transactions.
- Reconcile lifetime paid, cycles, discount, outstanding, payment reliability and renewal date.
- Distinguish Paid, Partial, Due, Overdue and No Record.
- Never show Paid without a settled transaction.
- Collection sheet must not imply success until the real handler confirms it.

Deliver:
- Payment destination and chart.
- Transaction/financial reconciliation tests.
- Receipt/reminder/collection interaction verification.

## SUBAGENT 5 — BODY INTELLIGENCE

Scope:
- Inspect BodyIntelligenceDashboard.kt, BodyDashboardAnalytics, BodyProgressViewModel, measurements, gallery and privacy flows.
- Apply the shared lavender design system without rewriting the domain analytics.
- Use only recorded measurements.
- Preserve metric-aware goal direction and neutral state when goal direction is unknown.
- Keep body chart series and gallery tied to real readings/assets.

Deliver:
- Body destination visual integration.
- Tests for missing metrics, unit display and goal direction.
- Screenshot comparison and file list.

## SUBAGENT 6 — QA / INTEGRATION REVIEW

Scope:
- Review shared shell and module integration.
- Check navigation, duplicate metrics, text clipping, chart labels, accessibility, font scale and system insets.
- Verify data provenance for every displayed number.
- Run all tests and debug build.
- Capture all five module screenshots on a device/emulator.
- Compare against reference at matching viewport and density.
- Do not modify module source without assigning ownership and coordinating with the owner agent.

Deliver:
- Build/test report.
- Responsive matrix: 360/375/393/412dp.
- Screenshots and discrepancy list.
- Final acceptance checklist.

## FILE OWNERSHIP / PARALLELISM

- Shared shell/tokens: Visual agent only.
- Attendance domain/UI/test paths: Attendance agent only.
- Workout domain/UI/test paths: Workout agent only.
- Payment domain/UI/test paths: Payment agent only.
- Body workspace paths: Body agent only.
- Navigation integration, shared state and final merge: Orchestrator only.
- QA agent is read-only until a defect is assigned.

If the agent platform cannot isolate worktrees, do not run conflicting write tasks in parallel. Run them sequentially with explicit handoff.

## MODEL / QUOTA FALLBACK

Use the user's requested order:
1. DeepSeek
2. Nomotron
3. Antigravity 3.8 Flash

When quota is exhausted:
- stop new tasks on that model;
- save source changes;
- update docs/IMPLEMENTATION_PROGRESS.md;
- update docs/implementation/AGENT_HANDOFF.md;
- record current SHA, modified files, tests and next exact action;
- pass the same repository-specific prompt to the next model.

Do not assume model capabilities, tool access or quota. Verify what the current environment actually supports.

## CRASH / RESTART RECOVERY

On IDE restart, laptop hang or new model:
1. Read docs/IMPLEMENTATION_PROGRESS.md and docs/implementation/AGENT_HANDOFF.md.
2. Run git status and record current HEAD.
3. Inspect uncommitted changes.
4. Re-run the last failed command if needed.
5. Resume at the first unchecked item.
6. Never discard uncommitted files, reset branch or regenerate already verified work.

## REQUIRED VERIFICATION

From repository root:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```

Then run on emulator/physical Android device:
- Overview
- Attendance
- Workout
- Payment
- Body
- payment due/paid/partial/no-record states
- attendance present/late/absent/holiday/unknown states
- workout no-history and real-history states
- body with and without measurements
- 360/375/393/412dp widths

Record exact commit SHA, changed files, test counts, build output, device model, screenshots and unresolved issues.
