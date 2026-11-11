# BAD GYM Agent Handoff — Lavender Member Intelligence

You are continuing an existing Kotlin/Jetpack Compose Android application. Do not assume this is a React/Flutter project.

## First actions
1. Run `git status --short --branch`.
2. Confirm branch and HEAD. Expected baseline is `feature/member-intelligence-lavender-rebuild` based on `backup/local-main-2026-11-11` at `32fccc7a306d93f4c0216156e81e76b4d70f0f48`.
3. Read `AGENTS.md`, `AI_COLLABORATION_PROTOCOL.md`, `CURRENT_TASK.md`, `docs/audit/REPOSITORY_AUDIT_2026-11-11.md`, `docs/reference/MEMBER_INTELLIGENCE_LAVENDER_REFERENCE_CONTRACT.md`, and `docs/implementation/MEMBER_INTELLIGENCE_LAVENDER_IMPLEMENTATION_PLAN.md`.
4. Inspect the actual local working tree. Do not overwrite uncommitted work.

## Mission
Replace the active Member Intelligence visual presentation with the user's latest pale-lavender / violet / emerald full-screen reference. Deliver five native screens: Overview, Attendance, Workout, Payment and Body. Preserve existing domain, API, offline, privacy, entitlement, receipt, payment and navigation behavior.

## Hard constraints
- Native Kotlin + Jetpack Compose. No framework migration.
- Real member records only. Sample fixtures are preview/test-only.
- Replace static workout and payment chart arrays with repository-derived series.
- Never count unknown/future days as absences.
- Never imply a payment succeeded before the real transaction handler confirms it.
- No duplicated metrics, fake AI predictions, dark/neon theme selector, decorative 3D or toy UI.
- Keep 360dp first; test 360/375/393/412dp.
- Keep changes in :app, tests and relevant docs. Do not touch OPPO/ or device-agent tooling.

## Agent work allocation
- Visual foundation agent: design tokens, common cards, shared header and typography.
- Attendance agent: attendance page and calculations/tests.
- Workout agent: workout page and session-derived charts/tests.
- Payment agent: payment page, transaction-derived analytics/tests.
- Body agent: Body Intelligence reference styling, privacy and metric integrity.
- Integration/QA agent: navigation, shared state, build, device screenshots and regression.
Do not allow two agents to write the same file. Use separate branches/worktrees if available; otherwise assign exclusive directories and integrate centrally.

## Model fallback
Use DeepSeek for the current task. If quota is exhausted, checkpoint and hand off to Nomotron. If unavailable/exhausted, checkpoint and hand off to Antigravity 3.8 Flash. Every model must read the same handoff and progress documents before editing.

## Recovery
After laptop hang, IDE restart or model switch:
- inspect git status and current SHA;
- read progress and handoff;
- preserve all uncommitted work;
- rerun the last failed verification;
- resume at the first incomplete task;
- never reset, clean away or regenerate unrelated changes.

## Verification
From repository root:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```
Then install/run, capture all five screens, compare with reference, fix discrepancies, rebuild and record evidence. Compilation alone is not completion.

## Reporting
At every checkpoint record exact SHA, changed files, tests, build, device, screenshot paths, known issues and next action.
