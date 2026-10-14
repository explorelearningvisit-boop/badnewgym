# Universal Master Prompt — Gap Analysis & Decision Record

Date: 2026-10-14. Author: OpenCode (Muse Spark). Status: implemented items committed; theme decision resolved — light mint/emerald system retained and now extended with the weekly workout trend and the member context strip (see §6).

## 1. Reference element inventory (new blue reference)

Phone canvas (~880px wide portrait): status bar; header (BAD GYM MEMBER INTELLIGENCE, search, bell, settings); rail (Home/Members/Check-In/Payment/Attendance/Workout/Body/Supplements/Services/History/Insights/Messages/More); live-session hero with athlete imagery + timer + End Session; member card (photo, name, ID, age/gender, plan, Active, days left, Valid-till, visits bar); payment card (outstanding, due-in, last-paid + Paid, current-due, Plan/History/Invoice/Auto Pay); attendance (donut, month nav, Today, weekday grid, status counts, Monthly Trend, W1–W4, streak boxes); workout (month nav, This Month, full-month bars, intensity legend High/Medium/Low/No-Workout, Sessions/Peak); body (month nav, metric tiles + sparklines, Top Exercise, Muscle Focus, anatomy art). Side panels: asset/typography/palette/impl-plan guides (documentation, not app UI).

## 2. Repo mapping (what already exists, all verified on device)

| Reference element | Repo state |
|---|---|
| Rail + 13 destinations | Done (commit 8efeffd), badges added (0e5829a) |
| Live hero + timer + checkout | Done, persistence + duplicate guards verified |
| Member card | Done (photo ring, plan, days bar) |
| Payment card + sub-nav | Done, collect verified Due→Settled |
| Full-month attendance + nav + Today + counts + payment markers + prev comparison | Done (beef017) |
| Workout month mode (THIS_MONTH default + month nav) | Done, this change (123 tests green) |
| Workout intensity legend | NOT done — sessions carry type (strength/cardio), not intensity; encoding intensity would be fabrication |
| Body tiles/trends/focus | Done (dashboard + analytics screen) |
| Header search/bell/settings | NOT done — no backend; a decorative search is banned by the same prompt (§11) |
| Hero/3D/anatomy imagery | NOT done — no generation tooling in this environment; vector/icon language used instead |
| Messages badge "3" | NOT done — no message source; would be fabrication |

## 3. Blocking conflict: dark-blue theme vs standing repo orders

The new reference demands a dark navy visual identity. This conflicts with binding repo rules that outrank a task prompt in this project's own hierarchy (AGENTS.md §Source of Truth):

- `AGENTS.md`: production package, V2 contract as source of truth.
- `CURRENT_TASK.md`: "Professional light premium gym-management UI", "No black/dark-heavy background", "No toy-like 3D decoration, fake AI ornaments".
- Three verified light-theme milestones (87fb2ab, 8efeffd, 0e5829a) + light bodyprogress dashboard.

A dark-theme rewrite would invalidate all verified milestones and violate standing orders; the master prompt itself (§10, §18) requires approval for redirections and honest reporting of limitations. **Decision: theme change NOT applied; light design system retained. Proceeding requires explicit user approval.** Cost if approved: full token/widget/chart rework across memberintelligence + bodyprogress + sheets, with new verify loops.

## 4. Interaction inventory delta (this change)

- Workout range chips: Month (default) / 7D / 15D / 30D; month arrows + clamp to current month; LazyRow paging preserved for 31-day months; summary tiles + rail badge + insights sheet + detail sheet all month-aware.
- Debug bridge `SET_RANGE` accepts month/thismonth.

## 5. Ambiguities in the reference

- Reference attendance shows "27/30 Open Days" with 22+3+2 present/late/absent = 27 — consistent; ours computes the same way over real records.
- Reference body "Focus Legs 43%" vs panel "Chest 43%" is internally inconsistent; ours uses one aggregation for both.
- Small-print side panels are process documentation, not implementable UI.

## 6. Green/emerald pass — weekly workout trend + member context strip

The green/emerald reference matches the repo's existing light mint system, so no
re-theme was needed. This pass adds the two elements the reference has that the
dashboard lacked, without touching navigation or business logic.

- **Weekly workout trend** (`HomeWidgetsBottom.kt` `WeeklyTrendRow`, logic in
  `WorkoutAnalytics.weeksOfMonth` / `weekOverWeek`). A compact weekly-volume
  strip sits above the full-month daily chart, so the month itself is never
  hidden. Bars are Monday-start weeks of the selected month; buckets hold
  recorded minutes only.
- **Member context strip** (`HomeWidgetsTop.kt` `ContextStrip`, logic in
  `HomeModels.memberContext`). A slim verified-context line above the member
  name: Just checked in / Visit completed / No active visit.

### Honesty rules encoded (and tested)

- Weeks that start after the last recorded day are **dropped**, never charted as
  zeros: a week that has not happened is "no record", not "recorded zero".
- The `wk/wk` delta is **omitted unless the final recorded week has finished**,
  because a week still in progress is not a comparable period. For the current
  month this means no delta is shown at all — by design.
- A zero baseline yields no delta (no division-by-zero percentage).
- The context strip returns **null for an older live session**, so it never
  repeats the LIVE state the session hero already shows; and it no longer
  repeats the check-in time that the adjacent hero already prints.

### Evidence (this milestone)

- `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest` — BUILD SUCCESSFUL,
  133 tests, 0 failures (was 123; +10 for week geometry, week-over-week
  comparability, and member context).
- Device `zxdada69gunb7ls4` (1080x2400 @ 440dpi ≈ 393dp wide): screenshot
  `OPPO/pulled_screens/h2_top.png`, UI dump `OPPO/h2.xml`. Confirmed present and
  correct: `Just checked in`, `Weekly recorded minutes: W1 223, W2 405, W3 223`
  (no W4/W5), no `wk/wk` label. No horizontal overflow (max x = 1080 = screen).

### Honest limitations (not verified)

- **The coding agent cannot read images.** The reference image itself was never
  analysed; this pass was built from the written specification only. "Pixel
  perfect" against the reference is therefore not claimed.
- **360dp could not be device-verified.** MIUI rejects `wm size`/`wm density`
  (no WRITE_SECURE_SETTINGS), so the narrowest tested width is the device's
  ~393dp. The layout is weight-based with no fixed content widths, which is why
  it should reflow at 360dp, but that is a structural argument, not a
  measurement.
- Screenshot comparison was done by the user viewing `h2_top.png`; the agent
  verified structure via the UI dump (bounds, text presence) instead of vision.

## 7. Single-screen dashboard pass

The user asked for the whole dashboard to fit one screen with no vertical
scrolling, and for the redesign to be finished autonomously.

### Measured budget (device zxdada69gunb7ls4, 1080x2400 @ 440dpi)

393dp x 873dp physical; 796dp usable height after system bars. Final layout:

| Region | Height |
|---|---|
| Header | 52dp |
| Live session hero | 45dp |
| Member identity | 78dp |
| Payment and plan | 96dp |
| Attendance | 196dp |
| Workout | 172dp |
| Body Progress (compact) | 42dp |
| Widget gaps | 5 x 4dp |

Every reduction removed duplicated chrome, never information. The workout
sparkline moved into dead space beside the range chips; the identity plan
progress moved under the Days Left countdown; payment columns dropped from four
lines to two.

### Defects found and fixed during the pass

- **Hero overflow.** Separate IN/VISIT chips did not fit beside the timer and
  clipped the title. The visit number moved onto the hero second line.
- **Payment text collision.** The plan cell rendered its validity text straight
  through the Amount column. Now two lines with ellipsis.
- **Body focus was wrong.** The compact tile took the first entry of
  focusGroups, which reported an arbitrary muscle (Chest 7%) instead of the
  most trained one. Now maxBy sharePercent, giving Legs 43%.
- **Off-brand sheet.** The module bottom sheet never set containerColor and
  inherited a lavender default from the Material theme.
- **Sub-minute duration.** A 44-second visit rendered as 00h 00m, reading as
  no duration recorded. Now under 1 min.
- **Debug bridge could not carry an amount.** adb delivers strings via --es,
  and the bridge only accepted Number, so every scripted collect silently
  became -1.0. Now parses strings too.

### Interaction evidence (no-tap: MIUI blocks adb input)

Driven through the app debug bridge and deep links, then read back from device
screenshots:

- STATUS -> live=true, visits=1, outstanding=2800.0, situation=Due
- COLLECT 2800 UPI -> badge DUE SOON becomes SUCCESSFUL, receipt RCP-BG305,
  Remaining 0, Payment rail badge cleared, History 1 to 2.
- END_SESSION -> hero LIVE SESSION/LIVE becomes LAST SESSION/DONE, timer frozen
  at the real elapsed time, End button removed, strip becomes Visit completed
  under 1 min, rail badge LIVE becomes #1.
- Workout sheet lists 11 sessions summing to 851m, matching the card.

140 unit tests green.

## 8. Reference-visual pass (image comparison)

With image reading available, the attached reference was compared directly
against the running app. Key findings:

- The reference is 1024x1536, about 1.9x taller than the phone's usable
  393x873dp. It is a tall scrolling mockup and was never intended to fit one
  screen.
- Its background is a rich emerald field with light bloom, not flat white.
- Its hero is a tall gym photo with "GOOD WORKOUT / A HEALTHY YOU" and
  "Discipline Builds Freedom".

Three changes brought the build in line:

- Emerald depth field behind the widgets (PageGradient + PageGlow).
- Workout range chips replaced by one compact "This Month" dropdown (a superset
  of the chip ranges).
- Photographic hero at 118dp with the quote, live timer and End Session intact.

Trade-off: the hero now scrolls the dashboard, matching the reference. The
previous single-screen build is preserved in git history; `compact = true` on
the hero widget restores the 45dp strip and the no-scroll fit.

Still not implemented, and not faked, because no data source exists in this
repo: height, calorie intake, unread message counts, trainer scheduling,
session pause/resume, and a seeded BMI series (BMI is modeled as a metric but
no record is ever written for it). Each needs a real model or an honest empty
state, not invented values.

