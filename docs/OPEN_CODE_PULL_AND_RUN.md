# Pull and Run — BAD GYM Member Intelligence

This guide is for the branch that contains the latest full-screen reference implementation.

- Repository: https://github.com/explorelearningvisit-boop/badnewgym
- Branch: `member-intelligence-reference-dashboard`
- Draft PR: https://github.com/explorelearningvisit-boop/badnewgym/pull/6
- App module: `:app`
- Package/application ID: `com.example.badnewgym`

> This is a draft branch, not `main`. Do not clone/pull `main` expecting to see this implementation. The branch has not yet been build- or screenshot-verified in the original implementation session.

## 1. First-time setup — Windows PowerShell

Install Git, Android Studio, a compatible JDK (use the JDK configured by Android Studio/Gradle), and Android SDK platforms/build tools required by the project. Open PowerShell in the folder where you keep projects.

```powershell
git clone --branch member-intelligence-reference-dashboard --single-branch https://github.com/explorelearningvisit-boop/badnewgym.git
Set-Location badnewgym
git status
git branch --show-current
```

The last command should print:

```text
member-intelligence-reference-dashboard
```

## 2. Pull latest changes after the first clone

Run from the repository folder:

```powershell
git fetch origin
git switch member-intelligence-reference-dashboard
git pull --ff-only origin member-intelligence-reference-dashboard
git status
```

If you have local changes, do not discard them blindly. Review `git status` and commit/stash your work before pulling.

## 2A. Add the exact visual reference to GitHub (recommended for pixel diff)

The exact 921×1536 JPEG is available as a file attachment from the assistant's response. Download it and save it locally as:

`docs/reference/assets/member-intelligence-full-screen-reference.jpg`

Then from repository root:

```powershell
git add docs/reference/assets/member-intelligence-full-screen-reference.jpg
git commit -m "docs: add approved member intelligence reference image"
git push origin member-intelligence-reference-dashboard
```

This makes the reference reproducible for every agent and reviewer. Do not convert it to PNG merely because an earlier filename used that extension; preserve the original JPEG bytes and 921×1536 dimensions. If the downloaded file is named differently, rename it to the canonical path above before committing.

## 3. Read project instructions before asking an AI to edit

```powershell
Get-Content .\AGENTS.md
Get-Content .\AI_COLLABORATION_PROTOCOL.md
Get-Content .\CURRENT_TASK.md
Get-Content .\BAD_GYM_AGENT_START_HERE.md
Get-Content .\docs\BAD_GYM_PRODUCT_AND_USER_CONTEXT.md
Get-Content .\docs\reference\BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md
Get-Content .\docs\PIXEL_PERFECT_ACCEPTANCE.md
```

Tell OpenCode/Antigravity to read `BAD_GYM_AGENT_START_HERE.md` first and follow the source-of-truth order it declares. Do not ask the agent to “make it beautiful” without requiring build, screenshots, discrepancy fixes and evidence.

## 4. Open the repository in a coding environment

### VS Code

```powershell
code .
```

### OpenCode CLI (if installed)

```powershell
opencode
```

Inside OpenCode, ask it to read `BAD_GYM_AGENT_START_HERE.md`, `CURRENT_TASK.md` and the full implementation blueprint before editing.

### Android Studio

Open Android Studio → **Open** → select the cloned `badnewgym` folder (the one containing `settings.gradle.kts`). Allow Gradle sync to finish before running.

## 5. Build and run tests

From repository root:

```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```

Equivalent macOS/Linux/Git Bash command:

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

Do not start with `clean` if you are debugging an unrelated cache issue; use the full command above for the acceptance build. First-time Gradle sync/build needs internet access to download dependencies.

## 6. Install on a connected Android phone (optional)

Enable Developer Options → USB debugging on the phone, connect it, accept the authorization prompt, and confirm:

```powershell
adb devices
```

After a successful build, install the debug APK:

```powershell
adb install -r .\app\build\outputs\apk\debug\app-debug.apk
```

Launch the app by opening it from the phone's app launcher. If you use Android Studio, select the app run configuration and your connected phone/emulator, then press Run.

If `adb devices` lists no authorized device, device verification is blocked; use an emulator or fix the device connection. Never report a phone test that did not occur.

## 7. Screenshot / pixel-perfect verification

The latest supplied full-screen reference is 921×1536 pixels (portrait ratio 0.600; JPEG image). The older 691×1536 / 20:9 canvas belongs to the earlier compact-card reference and must not be used as the pixel target for this task. Runtime geometry is expressed in dp; record device width, density, font scale and safe areas, and compare screenshots at the same viewport/crop.

Required checks:
- 360dp phone width and approximately 320dp narrow width.
- default font scale and at least 1.3× font scale.
- no clipped member name, payment amount, due date, chart labels or menu text.
- horizontal menu can reach every item.
- correct paid/due/overdue states.
- no fake transaction/workout/body history.
- missing workout data differs from confirmed zero.
- attendance distinguishes absent, late, holiday/closed and unknown.
- payment CTA opens a real payment action; paid state opens receipt/history.
- body metrics respect data availability and privacy.
- all visible actions respond and have accessible descriptions.

Capture screenshots before and after fixes. Store evidence in the location agreed in the current task/CI; do not commit private member screenshots or real health/payment data.

## 8. Useful commands

```powershell
# Confirm branch and exact commit
git branch --show-current
git rev-parse HEAD

# Inspect changes
git status
git diff --stat
git diff

# Build without cleaning
.\gradlew.bat assembleDebug

# Unit tests
.\gradlew.bat testDebugUnitTest

# List connected Android devices
adb devices
```

## 9. What is not verified yet

The original GitHub implementation session did not run Gradle or capture device screenshots. Treat the first build as an integration check, not proof of success. Fix compile/test errors in the feature branch, capture screenshots, compare against the reference and record exact evidence before merging PR #6.

## 10. Do not do this

- Do not pull `main` and assume it contains this draft branch.
- Do not merge the PR before build/test/visual review.
- Do not delete the existing V4 canonical member card or domain/entitlement architecture without inspecting integration.
- Do not replace real data adapters with preview fixtures.
- Do not edit unrelated OPPO, mustard-oil or Banaras Tour Gold projects.
