# BAD GYM — Repository Execution Rules

## ACTIVE PROJECT — DO NOT GUESS

The production Android project for all current work is the repository-root Gradle project:

- Root: `/`
- Gradle module: `/app`
- Application package: `com.example.badnewgym`
- Main Activity: `app/src/main/java/com/example/badnewgym/MainActivity.kt`

Run Gradle commands from the repository root.

## IMPORTANT: there are legacy/reference folders in this repository

### NEVER use as the production implementation
`BAD_GYM_MEMBER_INTELLIGENCE_READY/` is a legacy/reference package. It is NOT the active Android module and must not receive current UI implementation work.

Do not edit, build, or treat files under that folder as the production BAD GYM app unless a task explicitly says to migrate something from it.

### NEVER modify
`OPPO/` and `Launch_Oppo_Dev_Studio.bat` are outside the active BAD GYM product scope.

Do not modify them for Member Card work.

## Current production source of truth

For the current Member Card work, use:

1. `CURRENT_TASK.md`
2. `docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`
3. `docs/reference/BAD_GYM_REFERENCE_01_SPEC.md`
4. `docs/reference/BAD_GYM_REFERENCE_01_EXECUTION_LEDGER.md`
5. `ANTIGRAVITY_RUN.md`
6. actual production code under `app/src/main/`
7. tests under `app/src/test/` and `app/src/androidTest/`

The supplied reference image is the visual source of truth.

## Required execution model

Pull the latest repository first.

Then inspect the complete active project before editing.

Work only on the current ledger stage:
- inspect
- implement
- build
- run
- screenshot
- compare to reference
- fix
- rebuild
- screenshot again
- mark VERIFIED
- record evidence
- proceed to next stage

Never declare completion from compilation alone.

## Git rule

Current implementation work is intended for `main`.

Do not create an unrelated parallel implementation in `BAD_GYM_MEMBER_INTELLIGENCE_READY/`.

At the end of a verified stage, record the exact commit SHA and changed production files in the execution ledger.

## Reference image

Canonical repository asset path:

`docs/reference/images/reference-01-member-card-design-system.png`

If that binary asset is present, use it directly for visual comparison. If it is absent, do not invent a replacement; use the task/spec and request/restore the canonical asset through the repository workflow.

## Build command

`./gradlew clean testDebugUnitTest assembleDebug`

The only production Android module for this task is `:app`.
