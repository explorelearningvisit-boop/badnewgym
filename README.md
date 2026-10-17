# BAD GYM — Visit-Based Member Intelligence

BAD GYM is a native Android gym-management application built with Kotlin and Jetpack Compose. It supports gym operations such as member identity, check-in/check-out, membership, payment, attendance, workout, body progress and conditional operational widgets.

## Start here — AI agents and developers

**Read `BAD_GYM_AGENT_START_HERE.md` first.** It contains the context and file reading order for OpenCode, Google Antigravity, Gemini and other coding agents.

Current expanded Member Intelligence reference task:
- Branch: `member-intelligence-reference-dashboard`
- Draft PR: https://github.com/explorelearningvisit-boop/badnewgym/pull/6
- Pull/build/run instructions: `docs/OPEN_CODE_PULL_AND_RUN.md`
- Product/business/user context: `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`
- Hyper-detailed implementation blueprint and master coding prompt: `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
- Pixel-perfect acceptance checklist: `docs/PIXEL_PERFECT_ACCEPTANCE.md`
- Automated Windows pull/build/optional install helper: `tools/pull-and-run-member-intelligence.ps1`

> **Branch warning:** this implementation is currently a draft feature branch, not `main`. Clone or switch to the branch above to see these changes. Build, runtime and screenshot verification remain outstanding.

## Pull and run (Windows PowerShell)

First time:

```powershell
git clone --branch member-intelligence-reference-dashboard --single-branch https://github.com/explorelearningvisit-boop/badnewgym.git
Set-Location badnewgym
.\tools\pull-and-run-member-intelligence.ps1
```

Already cloned:

```powershell
git switch member-intelligence-reference-dashboard
git pull --ff-only origin member-intelligence-reference-dashboard
.\tools\pull-and-run-member-intelligence.ps1
```

The helper checks for local changes, pulls without force, runs unit tests/build, and installs/requests launch only if an authorized Android device is detected. It does not perform screenshot comparison automatically.

## Build

Run from the repository root:

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

Windows PowerShell:

```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```

## Product and design contract

The current full-screen user reference is the visual target for the expanded dashboard. The older V4 compact event-first member card and its event/entitlement/domain architecture remain part of the product and must not be silently deleted or bypassed. See `CURRENT_TASK.md` and `BAD_GYM_AGENT_START_HERE.md` for the integration rule.

Design intent: premium light surfaces, mint/white/emerald-led colors, semantic coral/red/blue/purple/gold accents, readable mobile charts, real data, accessible interactions and no dark-heavy background.

## Pixel-perfect workflow

Reference image → component decomposition → inspect existing architecture → implement one component → build/test → run → capture screenshot → compare → record discrepancies → fix → rebuild → capture again → verify states/actions → document evidence.

A successful compile alone is not a visual pass. If the exact reference image is not present in the repository, request it as a tracked asset under `docs/reference/assets/`; never claim it has been committed if it has not.

## Scope boundaries

Only modify BAD GYM app code, tests, assets and documentation. Do not modify `OPPO/`, `Launch_Oppo_Dev_Studio.bat`, unrelated mobile-control projects, Banaras Tour Gold work or Banaras Pure Gold mustard-oil business files.
