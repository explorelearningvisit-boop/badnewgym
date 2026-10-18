# BAD GYM — Current Task

**Task:** Implement the approved **compact Member Intelligence card** (200dp x 300dp) against the attached final reference image.
**Branch:** `member-intelligence-reference-dashboard` (rebased onto `main` @ `66226c6`, which merged PR #11 deleting the previous presentation).
**Status:** In progress. New `CompactMemberIntelligenceCard.kt` (200x300dp, light mint/white), new closure-aware domain layer (`GymSchedule.kt`, `AttendanceWorkoutAnalytics.kt`) plus 13 new data-rule tests. Build/test/device evidence pending this pass.

## Architecture decision (supersedes the full-screen dashboard direction)

The user supplied a compact **200dp x 300dp single-card** reference and ruled out the earlier output explicitly: "Do not introduce a full-screen dashboard when the requested output is a compact card" and "Dark navy ... never as a black/dark background theme". The repository rules agree ("Light, premium, mint/white/emerald-led ... user dislikes dark/black backgrounds").

Therefore the previous full-screen navy dashboard was removed (PR #11, tag `pre-mi-reset` @ `e8b75ce`) and replaced by a single self-contained card. The canonical compact visit/member card remains the V4 runtime surface; this card is the Member Intelligence summary surface hosted by `MemberIntelligenceScreen`, which preserves loading/error/locked states and routes section headers to real flows.

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

The current reference is the user-attached **dark-theme** full-screen dashboard (not committed to the repo): navy shell, "Good Morning 👋" member card with green progress ring, pill nav rail (Overview active in green), then **Body Stats → Payment & Plan → Attendance → Workout Trend** order, month navigation arrows on Attendance/Workout, workout stat tiles (Avg/Peak/Lowest) under a full-width chart, and a "View Detailed Analytics" action at the bottom. It is a long mobile dashboard; implement it as a scrollable responsive screen rather than compressing the entire image into a tiny card.

The exact reference screenshot is 921×1536 pixels, portrait ratio 0.600, and is committed at `docs/reference/assets/member-intelligence-full-screen-reference.jpg` (with `docs/reference/assets/manifest.md`) for repeatable comparison. This new full-screen screenshot supersedes the older V4 reference canvas of 691×1536 for this expanded-dashboard task only. Measure runtime dp dimensions separately; pixel dimensions are not dp.

## Architecture integration decision
Existing V4 documentation defines a compact event-first canonical visit/member card. The active UI now follows the user-approved full dashboard reference with a compact Aman-style member header. At the user's explicit request, the former `PixelPerfectMemberCard` presentation component was removed and its preview hosts now render `MemberSummaryCard`; domain engines, event priority, repository/ViewModel flows, privacy and entitlement rules remain intact.

The latest user reference supersedes older visual assumptions for this expanded dashboard only. **Decision (current reference):** the user explicitly supplied a dark-theme reference (deep navy shell, white data cards, green/blue/purple/red accents) and asked to implement it — the dashboard shell (page, header, member card, nav rail) is now dark navy with white section cards; the old "light/mint only, no dark background" rule is retired for this screen. Section accents stay: green attendance/active, red payment, blue workout, purple body stats, gold plan.

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
