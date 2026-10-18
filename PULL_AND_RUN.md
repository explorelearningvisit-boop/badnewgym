# One-Entry Pull, Run & AI Handoff

**Start here for every fresh setup.** This file is the single entry point for a human or AI coding agent. The project is the BAD GYM Android Member Intelligence feature.

- Repository: https://github.com/explorelearningvisit-boop/badnewgym
- Required branch: `member-intelligence-reference-dashboard`
- Draft PR: https://github.com/explorelearningvisit-boop/badnewgym/pull/6
- Android app ID: `com.example.badnewgym`
- Canonical design image path: `docs/reference/assets/member-intelligence-full-screen-reference.jpg`

## What “pull” does and does not mean

Git clone/pull downloads the repository files that are actually committed to the branch. It does **not** automatically make an AI read those files, install Android Studio, grant secrets, create missing image binaries, or prove the app is pixel-perfect. To start the coding agent with the complete handoff, run the agent launcher below; it passes an explicit instruction to read the required files in order.

## First-time setup — Windows PowerShell

Install Git, Android Studio with the Android SDK, and the JDK version compatible with this project's Gradle configuration. Install OpenCode separately if you want to use its agent launcher.

```powershell
git clone --branch member-intelligence-reference-dashboard --single-branch https://github.com/explorelearningvisit-boop/badnewgym.git
Set-Location badnewgym
.	oolspull-and-start-agent.ps1
```

The agent launcher safely checks for local changes, fetches/pulls the named branch without force, checks required docs/assets, prints the current branch and commit, and launches OpenCode with the read-first prompt. It does not overwrite or discard local work.

## If already cloned

Run from the repository root:

```powershell
git status --short
git switch member-intelligence-reference-dashboard
git pull --ff-only origin member-intelligence-reference-dashboard
.	oolspull-and-start-agent.ps1 -SkipPull
```

If the working tree is dirty, stop and inspect the changes. Commit or stash intentionally; never use a blind reset/force-pull.

## Required AI reading order

The agent must read these files completely, not just skim filenames:

1. `AGENTS.md`
2. `AI_COLLABORATION_PROTOCOL.md`
3. `CURRENT_TASK.md`
4. `BAD_GYM_AGENT_START_HERE.md`
5. `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`
6. `docs/OPEN_CODE_PULL_AND_RUN.md`
7. `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
8. `docs/PIXEL_PERFECT_ACCEPTANCE.md`
9. `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md`
10. `docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`
11. `docs/reference/assets/README.md`
12. Inspect the actual Kotlin sources, Gradle files, resources, ViewModels, repositories, domain/event/entitlement logic, tests and all tracked image assets before editing.

Then the agent must produce a short inventory of: (a) files read, (b) available/missing assets, (c) architecture found, (d) dependencies already declared versus genuinely needed, (e) build/test status, and (f) planned edits. Only then should it begin implementation.

## Reference image and generated assets

The approved visual reference is a 921×1536 JPEG. The required repository path is:

`docs/reference/assets/member-intelligence-full-screen-reference.jpg`

**Current known limitation:** documentation says the image is available in the original ChatGPT task workspace, but the binary has not been committed to this branch yet. The launcher will warn if it is missing. Add the exact original file to that path and commit/push it before requiring pixel-diff verification. Do not fabricate or silently substitute a similar image. Generated supplementary assets (if truly needed) belong under `docs/reference/generated/` and must be inventoried; prefer Compose-drawn UI for charts, icons and cards rather than rasterizing the interface.

Do not commit private member photos, personal health measurements, payment details, secrets, API keys or credentials without explicit approval and an approved privacy plan.

## Build and run the Android app

Once the agent has made/fixed the code, run:

```powershell
.	oolspull-and-run-member-intelligence.ps1
```

Or manually:

```powershell
.gradlew.bat clean testDebugUnitTest assembleDebug
```

macOS/Linux:

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

Expected debug APK:

`app/build/outputs/apk/debug/app-debug.apk`

A successful build is not a visual pass. A connected/authorized Android device or emulator is needed for runtime checks. Pixel-perfect verification also needs the exact committed reference image, screenshots from the actual app, and documented comparison results.

## Product/design constraints

- Native Android Kotlin + Jetpack Compose; inspect the real code before choosing libraries.
- Target a compact mobile Member Intelligence UI, not a tablet/desktop dashboard.
- Bright, legible light surfaces; no dark-heavy backgrounds.
- Identity and actionable member status first; then payment/plan, attendance, workout trend and body progress, prioritized by the current task and reference.
- Each widget has a clear semantic identity and its own visual language; avoid repeated metrics across widgets.
- Use real data and preserve loading/empty/error states. Missing workout data is not automatically zero; never invent payment dues or attendance.
- No dead buttons, fake success, hardcoded production member records, or unsupported claims of verification.
- Do not delete/bypass the existing V4 event/domain/entitlement architecture. Follow the active task and source-of-truth precedence.
- Keep changes scoped to BAD GYM. Do not edit unrelated projects.

## Completion report required from the agent

Report branch and commit SHA; files changed; reference asset status; dependencies added and why; build/test output; device/emulator and viewport; screenshot paths; visual discrepancies found/fixed; action/data integration status; and any remaining blockers. Never say “production-ready” or “pixel-perfect” unless the corresponding evidence exists.
