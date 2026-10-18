# BAD GYM — Repository Execution Rules

## ACTIVE PROJECT
Production Android project is the repository-root Gradle project, module `:app`, package `com.example.badnewgym`. Run Gradle commands from repository root.

## FIRST ACTION FOR AI AGENTS
Read `BAD_GYM_AGENT_START_HERE.md` first. Then read `AI_COLLABORATION_PROTOCOL.md`, `CURRENT_TASK.md`, `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`, `docs/OPEN_CODE_PULL_AND_RUN.md`, the hyper-detailed implementation blueprint and `docs/PIXEL_PERFECT_ACCEPTANCE.md`.

## ACTIVE BRANCH TASK
The current feature task is `member-intelligence-reference-dashboard` and is tracked by draft PR #6:
https://github.com/explorelearningvisit-boop/badnewgym/pull/6

The latest user-supplied full-screen reference and the hyper-detailed blueprint define the visual target for the expanded Member Intelligence screen in this task. Preserve established V4 domain, event, privacy, entitlement and canonical-card behavior unless a deliberate, documented integration decision is required. Do not blindly replace working architecture.

## SOURCE-OF-TRUTH ORDER
1. `AGENTS.md`
2. `BAD_GYM_AGENT_START_HERE.md`
3. `CURRENT_TASK.md`
4. `AI_COLLABORATION_PROTOCOL.md`
5. `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`
6. `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
7. `docs/PIXEL_PERFECT_ACCEPTANCE.md`
8. Existing V4 architecture contracts and actual production source/tests.

If instructions conflict, stop and document the conflict before making destructive edits. A newer explicit user reference can change the visual target; it does not silently remove real-data, privacy, accessibility, entitlement or regression requirements.

## LEGACY / OUT OF SCOPE
- `BAD_GYM_MEMBER_INTELLIGENCE_READY/` is legacy/reference only.
- `OPPO/` and `Launch_Oppo_Dev_Studio.bat` are out of scope.
- Banaras Tour Gold, Manchester-company work and Banaras Pure Gold mustard-oil business are unrelated projects.
Do not modify these paths or mix their business data into BAD GYM.

## IMPLEMENTATION RULES
- Kotlin + Jetpack Compose + Material 3.
- Use actual member/payment/attendance/workout/body data.
- Stub/preview fixture data must remain isolated from production adapters.
- No fake values, dead buttons, no-op actions, duplicate metrics or dark-heavy background.
- Missing data must not be misrepresented as zero, absence, holiday or missed payment.
- Server entitlement remains authoritative.
- Respect privacy and accessibility.
- Inspect current architecture before editing. Keep the implementation scoped and incremental.

## REQUIRED VERIFICATION
Inspect → implement atomic component → build → test → run → capture screenshot → compare → fix → rebuild → re-capture → verify interactions and state matrix → record evidence.

Run from repository root:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```
Windows PowerShell:
```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```

Convenience script (Windows PowerShell):
```powershell
.\tools\pull-and-run-member-intelligence.ps1
```

A successful compile is not proof of pixel-perfect quality. Do not claim device/screenshot verification without evidence. Record exact branch/SHA, changed files, build/test results, visual/device status and unresolved issues.

## GIT SAFETY
- Work on `member-intelligence-reference-dashboard` until PR review and verification are complete.
- Do not force-push, reset away user changes, or merge this draft PR without build/test/screenshot review.
- Do not tell the user to pull `main` expecting unmerged feature changes.
