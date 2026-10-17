# BAD GYM — Member Intelligence

**Single starting point: [PULL_AND_RUN.md](PULL_AND_RUN.md).**

This repository contains the BAD GYM native Android app and the Member Intelligence implementation handoff for AI coding agents.

## One-command setup (Windows PowerShell)

First install Git, Android Studio/Android SDK and the compatible JDK. Install OpenCode if you want the launcher to start the coding agent.

```powershell
git clone --branch member-intelligence-reference-dashboard --single-branch https://github.com/explorelearningvisit-boop/badnewgym.git
Set-Location badnewgym
.\tools\pull-and-start-agent.ps1
```

This pulls the feature branch, checks required instruction files, warns if the canonical reference image is missing, and starts OpenCode with an explicit read-everything-first prompt. If OpenCode is not installed, the script reports that and leaves the repository ready to open in another coding agent.

After the agent's code changes, build/test/install the app with:

```powershell
.\tools\pull-and-run-member-intelligence.ps1
```

## Important links

- [One-entry setup, complete read order, assets and build/run workflow](PULL_AND_RUN.md)
- [AI agent start-here instructions](BAD_GYM_AGENT_START_HERE.md)
- [Product/business/user context](docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md)
- [Hyper-detailed implementation blueprint](docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md)
- [Pixel-perfect acceptance checklist](docs/PIXEL_PERFECT_ACCEPTANCE.md)
- [Pull/build/run guide](docs/OPEN_CODE_PULL_AND_RUN.md)
- [Draft PR #6](https://github.com/explorelearningvisit-boop/badnewgym/pull/6)

## Branch and verification status

The current work is on `member-intelligence-reference-dashboard`, not `main`. Do not expect this work on `main` until the draft PR is reviewed and merged.

The exact approved 921×1536 JPEG reference is required at `docs/reference/assets/member-intelligence-full-screen-reference.jpg`. Documentation indicates that this binary has not yet been committed. The one-command launcher warns if it is absent. Build/runtime and pixel-comparison verification must be performed and recorded before describing the result as production-ready.

## Product boundaries

Use Kotlin + Jetpack Compose and inspect existing architecture before changing dependencies. Preserve the V4 event/domain/entitlement behavior, use real data, avoid repeated metrics and false payment/attendance claims, keep the UI light and mobile-first, and do not modify unrelated projects.
