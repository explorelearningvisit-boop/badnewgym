# BAD GYM — Repository Execution Rules

## ACTIVE PROJECT
Production Android project is repository root Gradle project, module /app, package com.example.badnewgym.
Run Gradle commands from repository root.

## LEGACY / OUT OF SCOPE
BAD_GYM_MEMBER_INTELLIGENCE_READY/ is legacy/reference only and must not receive current implementation work.
OPPO/ and Launch_Oppo_Dev_Studio.bat are out of scope.

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