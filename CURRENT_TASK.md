# BAD GYM — Current Task

**Task:** Implement and verify the latest user-supplied full-screen Member Intelligence reference.
**Branch:** `member-intelligence-reference-dashboard`
**Review:** https://github.com/explorelearningvisit-boop/badnewgym/pull/6
**Status:** Draft implementation committed; build, runtime and pixel-level screenshot verification remain outstanding.

## Goal
Deliver a premium native Android Member Intelligence experience that lets a gym owner quickly understand member identity, plan status, payment state, attendance behavior, workout consistency and body-progress trends. The reference must become real Compose UI connected to actual domain/repository data, not a screenshot background or decorative mockup.

## Visual source for this task
Use the latest user-supplied image from the conversation plus:
- `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
- `docs/PIXEL_PERFECT_ACCEPTANCE.md`

The reference contains: brand/header, member identity and membership ring, horizontal navigation, Payment & Plan with 12-month payment history, attendance calendar, monthly workout trend, and body progress metrics. It is a long mobile dashboard; implement it as a scrollable responsive screen rather than compressing the entire image into a tiny card.

If the original image file is not present in this branch, do not pretend it is an asset in Git. Ask for the exact reference image to be committed under `docs/reference/assets/` for repeatable screenshot comparison.

## Architecture integration decision
Existing V4 documentation defines a compact event-first canonical visit/member card (approximately 350dp width, preferred 500–540dp height, event-first hero, conditional widgets, server-authoritative entitlements). The new full-screen reference is an expanded dashboard surface and may be taller/scrollable. Preserve the compact canonical card, event priority/domain engine, repository/ViewModel patterns, privacy, and entitlement rules. Inspect navigation and decide how the expanded dashboard is reached. Do not silently replace or delete the canonical V4 surface or bypass its business rules just to match the screenshot.

The latest user reference supersedes older visual assumptions for this expanded dashboard only. Keep the visual language light, mint/white/emerald-led, with coral/red urgent states, blue workout/info, purple body-progress and gold plan accents. No black/dark-heavy background.

## Required regions
1. Compact BAD GYM application header.
2. Member identity with real member photo/name/ID/plan/status and membership remaining/elapsed progress.
3. Horizontally scrollable navigation.
4. Payment & Plan: correct paid/due/overdue branching, last/next due context, real monthly transaction chart and truthful timing/upgrade markers.
5. Attendance: actual month dates/statuses, present/absent/late/holiday/closed/unknown, percentage and available streak summaries.
6. Workout: real daily duration data, average/peak/lowest and comparison only when computable.
7. Body Progress: only recorded and authorized metrics, sparklines only when at least two real measurements exist, goal-aware interpretation.

## Non-negotiable data logic
- Paid balance zero must not show a false Due/Collect Payment state.
- No transaction is not automatically a missed payment.
- Unknown payment timing is not “on time”.
- Missing workout is not zero workout.
- Sundays are not automatically holidays.
- Body change is not inherently good/bad without a relevant member goal.
- No fake production data or generic athlete photos as real members.
- Empty/loading/error/unauthorized states must be intentional.
- All visible primary actions must be connected to real workflows; no no-op buttons.
- Server entitlements remain authoritative.

## Build/run
From repository root:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```
Windows:
```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```
Windows convenience pull/build/install helper:
```powershell
.\tools\pull-and-run-member-intelligence.ps1
```

## Definition of done
- Existing project builds and tests pass.
- The correct expanded screen is reachable without breaking canonical visit/member-card flows.
- Real data adapters populate supported analytics.
- All states/actions are functional.
- Screenshots at approximately 360dp and 320dp are compared with the reference; major discrepancies are fixed.
- Font scaling, clipping, scroll/navigation, privacy and entitlement behavior are checked.
- Device verification is recorded as PASS or BLOCKED with reason.
- Pixel-perfect checklist is filled with evidence; branch SHA and unresolved issues are reported.
- No claim of production-ready until the above evidence exists.
