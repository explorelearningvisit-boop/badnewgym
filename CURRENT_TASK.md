# BAD GYM — Current Task

**Task:** Implement the approved **mobile Member Intelligence dashboard** from the latest light-theme reference image — a scrollable full-phone-viewport screen.
**Branch:** `member-intelligence-reference-dashboard` (rebased onto `main` @ `66226c6`, which merged PR #11 deleting the previous presentation).
**Status:** Implemented and device-verified. `clean testDebugUnitTest assembleDebug` green; screenshots at 392dp, 360dp, 320dp widths and at 130% font scale on `21091116I` (1080×2400, 440 dpi).

## Current scope (supersedes the compact-card-only scope)

The user supplied a light, full-viewport member dashboard (1024×1536) and asked to "implement nd design this ui ux in mobile viewport". The compact 200×320dp card is therefore no longer the product surface: `MemberIntelligenceScreen` now hosts `MemberDashboard.kt`, a native Compose screen rendered at real mobile text sizes (no density trickery):

1. **BAD GYM header** — menu, month selector, overflow.
2. **Member hero** — avatar with tier + presence badges, name, code/age/gender, plan and status pills, elapsed ring (`25 / 100` days left), joined / goal / branch chips.
3. **Body Stats** — weight, muscle, body fat and BMI tiles with goal-aware deltas, sparklines built from real measurements, and a WHO BMI band scale.
4. **Workout Trend & Attendance** — 31-day bar chart coloured by duration band, dashed average line, peak/avg/lowest callouts, every day labelled, closure-aware attendance tiles.
5. **Payment & Plan** — paid vs outstanding branching, last payment, real due date vs plan renewal, receipt + collect actions.
6. **Today's Activity** — attendance status and recorded workout minutes, today only.

The compact card (`CompactMemberIntelligenceCard.kt`) and its component gallery remain in the tree as the debug surface and keep their geometry tests; they are no longer rendered by the production screen.

## Data rules enforced for this card

- Gym closure is never inferred from the weekday; Sunday is an ordinary operating day. Closure comes from `GymWeeklySchedule` (configured) or an explicit closure record.
- `Gym Off` is a distinct domain state and is excluded from the attendance denominator and from workout averages, so a closure can never read as inactivity.
- Missing workout data stays `null` (never a 0-minute workout); a confirmed no-workout renders a small explicit tick, never a tall zero bar.
- The pencil/closure marker means "gym recorded as closed", not an attendance failure.
- Attendance and workout are calculated separately in the domain layer even though they share one chart.

## Goal
Deliver a premium native Android Member Intelligence experience that lets a gym owner quickly understand member identity, plan status, payment state, attendance behavior, workout consistency and body-progress trends. The reference must become real Compose UI connected to actual domain/repository data, not a screenshot background or decorative mockup.

## Visual source for this task
Use the latest user-supplied image from the conversation plus:
- `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
- `docs/PIXEL_PERFECT_ACCEPTANCE.md`

The current reference is the user-attached **light-theme** full-screen dashboard: a pastel mint/blue/lilac page background, white rounded section cards, and this order — **member hero → Body Stats → Workout Trend & Attendance → Payment & Plan → Today's Activity**. Accents: emerald for active/attendance, gold for the Prime plan, blue for workout/actions, purple for body stats, coral for negatives.

The exact reference screenshot is 1024×1536 pixels, portrait ratio 0.667. Its Stitch render and markup are committed at `docs/reference/stitch/` (`badgym_member_profile.stitch.png`, `badgym_member_profile.stitch.html`). Measure runtime dp dimensions separately; pixel dimensions are not dp.

## Architecture integration decision
Existing V4 documentation defines a compact event-first canonical visit/member card. The active UI now follows the user-approved full dashboard reference with a compact Aman-style member header. At the user's explicit request, the former `PixelPerfectMemberCard` presentation component was removed and its preview hosts now render `MemberSummaryCard`; domain engines, event priority, repository/ViewModel flows, privacy and entitlement rules remain intact.

The latest user reference supersedes older visual assumptions for this screen. **Decision (current reference):** the dashboard is a light surface — pastel gradient page, white cards, emerald/gold/blue/purple/coral accents. No dark shell and no bottom navigation bar: the screen is one scrollable member view, and every visible control either changes state or opens a real flow.

## Required regions
1. Compact BAD GYM application header with month context and overflow.
2. Member identity with real photo/name/ID/plan/status, elapsed ring and joined/goal/branch context.
3. Body Stats: weight, muscle, body fat and BMI; deltas only when two measurements exist; sparklines only from real points.
4. Workout Trend & Attendance: actual month days, present/absent/late/holiday/closed/unknown, duration bands, average/peak/lowest and closure-aware attendance rate.
5. Payment & Plan: correct paid/due/overdue branching, last payment, real due date vs plan renewal, receipt/collect actions.
6. Today's Activity: today's attendance status and recorded workout only.

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
