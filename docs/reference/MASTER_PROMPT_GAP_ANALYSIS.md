# Universal Master Prompt — Gap Analysis & Decision Record

Date: 2026-10-14. Author: OpenCode (Muse Spark). Status: theme rewrite APPROVED by user and IMPLEMENTED (commit follows); month mode done.

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

A dark-theme rewrite would invalidate all verified milestones and violate standing orders; the master prompt itself (§10, §18) requires approval for redirections and honest reporting of limitations. **Decision: APPROVED by the user on 2026-10-14; implemented as a token-driven re-theme (MiColors → midnight navy, plan hero → electric blue, Material dark scheme for sheets/dialogs/menus, dark system bars). Verified on device; see commit.** Cost note: future light-theme work must now target the dark tokens.

## 4. Interaction inventory delta (this change)

- Workout range chips: Month (default) / 7D / 15D / 30D; month arrows + clamp to current month; LazyRow paging preserved for 31-day months; summary tiles + rail badge + insights sheet + detail sheet all month-aware.
- Debug bridge `SET_RANGE` accepts month/thismonth.

## 5. Ambiguities in the reference

- Reference attendance shows "27/30 Open Days" with 22+3+2 present/late/absent = 27 — consistent; ours computes the same way over real records.
- Reference body "Focus Legs 43%" vs panel "Chest 43%" is internally inconsistent; ours uses one aggregation for both.
- Small-print side panels are process documentation, not implementable UI.
