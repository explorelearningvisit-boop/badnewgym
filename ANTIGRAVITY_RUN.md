# BAD GYM — Google Antigravity Execution Handoff

## STOP: choose the correct project first

The repository contains legacy/reference material. **The active production project is the repository-root Gradle project with module `:app`.**

Read `AGENTS.md` first.

### Production code
- `app/src/main/`
- package: `com.example.badnewgym`

### Do NOT implement current work here
- `BAD_GYM_MEMBER_INTELLIGENCE_READY/` — legacy/reference package
- `OPPO/` — unrelated mobile-control tooling
- `Launch_Oppo_Dev_Studio.bat` — unrelated launcher

Do not confuse the existence of those folders with the production module.

## First operation

From the repository root:

```bash
git checkout main
git pull --ff-only origin main
git status
```

Then inspect the full active project and read, in this order:

1. `AGENTS.md`
2. `AI_COLLABORATION_PROTOCOL.md`
3. `CURRENT_TASK.md`
4. `docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`
5. `docs/reference/BAD_GYM_REFERENCE_01_SPEC.md`
6. `docs/reference/BAD_GYM_REFERENCE_01_EXECUTION_LEDGER.md`
7. this file
8. relevant production source under `app/src/main/`

## Current task

Implement the supplied BAD GYM Member Card Design System reference, Image 1/24.

The canonical visual reference path is:

`docs/reference/images/reference-01-member-card-design-system.png`

If the file exists, compare screenshots against it directly. Never substitute a newly invented reference.

## Atomic workflow

Do NOT implement the complete screen in one speculative pass.

For each ledger stage:

1. Read the complete current production context.
2. Inspect the exact source files.
3. Implement only that stage.
4. Build.
5. Run the target screen/state.
6. Capture a screenshot.
7. Compare screenshot against the reference.
8. Record discrepancies:
   - geometry
   - spacing
   - typography
   - colors
   - icons/assets
   - content
   - state
   - interaction
   - animation
   - responsive behavior
9. Fix all material discrepancies.
10. Rebuild.
11. Capture again.
12. Compare again.
13. Mark the stage `VERIFIED` only after verification.
14. Update `docs/reference/BAD_GYM_REFERENCE_01_EXECUTION_LEDGER.md`.
15. Continue to the next stage.

## Required build

From repository root:

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

The production Android module is **only `:app`**.

## Reference requirements

The image defines:
- persistent vertical rail
- Home default
- member identity
- current visit
- conditional widgets
- Attendance
- Payment Due
- Plan
- Trainer/PT
- Gym Time
- Body Progress
- bottom actions
- XS/S/M/L/XL widget ratios
- nested widgets
- resize/drag editor behavior where supported
- responsive 320–360, 360–390, 400–430, 600–800 and 800+ dp behavior
- event/visit states
- entitlement-driven visibility
- premium light visual language

Use real BAD GYM domain data. Do not hard-code example names or numbers from the design board.

## Git

All current implementation work is for BAD GYM `main`.

Do not create a second implementation in the legacy folder.

After every verified milestone, record:
- exact HEAD SHA
- changed production files
- build result
- test result
- device/screenshot result
- visual discrepancies
- fixes
- next stage

Completion means the final integrated screen has been built, run, screenshot-compared and verified as far as the available device tooling permits.
