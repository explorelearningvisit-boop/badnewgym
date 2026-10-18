# BAD GYM — Current Task

**Task:** Implement and verify the latest user-supplied full Member Intelligence dashboard reference (Aman Tripathi).
**Branch:** `member-summary-card`
**Base:** `main` (prior dashboard PR #6 merged at `facb56f`).
**Status:** Implemented, clean-built and device-verified on Aman BG305 (52 tests green). The former `PixelPerfectMemberCard` UI is removed at user request. Evidence: `docs/PIXEL_PERFECT_ACCEPTANCE.md` (2026-10-18 follow-up record); final captures `OPPO/pulled_screens/ref_final2_top.png`, `ref_final2_bottom.png`, and `ref_receipt.png`.

## Goal
Deliver a premium native Android Member Intelligence experience that lets a gym owner quickly understand member identity, plan status, payment state, attendance behavior, workout consistency and body-progress trends. The reference must become real Compose UI connected to actual domain/repository data, not a screenshot background or decorative mockup.

## Visual source for this task
Use the latest user-supplied image from the conversation plus:
- `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
- `docs/PIXEL_PERFECT_ACCEPTANCE.md`

The reference contains: brand/header, member identity and membership ring, horizontal navigation, Payment & Plan with 12-month payment history, attendance calendar, monthly workout trend, and body progress metrics. It is a long mobile dashboard; implement it as a scrollable responsive screen rather than compressing the entire image into a tiny card.

The exact reference screenshot is 921×1536 pixels, portrait ratio 0.600, and is committed at `docs/reference/assets/member-intelligence-full-screen-reference.jpg` (with `docs/reference/assets/manifest.md`) for repeatable comparison. This new full-screen screenshot supersedes the older V4 reference canvas of 691×1536 for this expanded-dashboard task only. Measure runtime dp dimensions separately; pixel dimensions are not dp.

## Architecture integration decision
Existing V4 documentation defines a compact event-first canonical visit/member card. The active UI now follows the user-approved full dashboard reference with a compact Aman-style member header. At the user's explicit request, the former `PixelPerfectMemberCard` presentation component was removed and its preview hosts now render `MemberSummaryCard`; domain engines, event priority, repository/ViewModel flows, privacy and entitlement rules remain intact.

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
