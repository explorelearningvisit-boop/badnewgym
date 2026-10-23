# Stitch-generated design reference (Member Intelligence card)

This folder holds the **raw Stitch output** for the BAD GYM member card, kept so the
Compose implementation can be audited against the design that generated it.

## Provenance

| Property | Value |
| --- | --- |
| Tool | Google Stitch (`stitch` MCP tools, Code Mode) |
| Stitch project | `projects/12990602487065360846` title "UI Design Replication" — used only for cross-reference |
| **Source-of-truth project** | `projects/15870444005236885498` title "Complete Kotlin Android App" |
| Uploaded reference | screen `1287801399245057434`, title `image.png`, 1024 x 1536 |
| Generated screen | screen `3c562c1b3af34d7288fb59672eacb5c6`, title **"Bad Gym Member Profile Dashboard"**, MOBILE 780 x 1950 |
| Retrieved | 2026-10-18 |
| Files | `badgym_member_profile.stitch.html` (generated markup, 27,409 B), `badgym_member_profile.stitch.png` (Stitch's own render, 65,047 B) |

The uploaded 1024 x 1536 reference was confirmed to be the design input by
downscale-and-correlate: its mean per-pixel difference against that screen's
thumbnail is **1.67 / 255**, versus 24.7-38.0 for every other candidate image in the
repository. That is the basis for treating this Stitch screen as the design source.

### Status of this markup

`badgym_member_profile.stitch.html` is **design reference only, never shipped**. It is
HTML/Tailwind output describing a *full phone screen*.

**Update (2026-10-23):** the product surface is now that full screen. The user asked to
"implement nd design this ui ux in mobile viewport", so
`presentation/components/MemberDashboard.kt` implements this reference at real mobile
type sizes. The earlier 200 dp x 300 dp card (`CompactMemberIntelligenceCard.kt`) is kept
only as a debug component gallery surface and is no longer rendered by the production
screen.

Do not treat the numbers inside it as data. They are the mockup's design content
(`75%`, `25 / 31 Days`, `48m` average, `65m` peak, `24 / 77%` present, ...). The app
derives every value from real state and will legitimately differ.

## Fidelity map: Stitch design -> Compose

| Stitch block | Compose implementation (`MemberDashboard.kt`) |
| --- | --- |
| `TopBar` — "BAD / GYM" + "Member Intelligence" + month chip + 3-dot menu | `DashboardTopBar` |
| `MemberProfileCard` — gradient avatar ring, crown, active dot, name, `BG305 \| 23 yrs \| Male`, Prime + Active badges | `HeroCard`, `AvatarBlock`, `PlanPill`, `StatusPill` |
| Radial 75% meter with `25 / 100` + `Days Left` | `MembershipRing` (elapsed plan share; caption is `daysLeft / planTotalDays`) |
| Joined / Goal / Branch chips | `MetaChip` (Goal and Branch disappear when the data is absent) |
| `BodyStatsSection` — 4 tiles (weight / muscle / fat % / BMI band) | `BodyStatsCard` + `BodyTile` + `Sparkline` + `BmiScale` |
| `AttendanceWorkoutSection` — band legend, 31-day bar chart, `60m/45m/30m/15m/0m` axis, avg/peak/lowest, counters | `ActivityCard` -> `WorkoutChart`, `BandLegend`, `Callout`, `ActivityStats` |
| `PaymentPlanSection` — amount, `Paid · On Time`, next due, days left, Prime chip | `PaymentCard` |
| `Today's Activity` — check-in / workout / check-out | `TodayCard` (attendance status + recorded minutes; the model stores no clock times) |
| Colours `#00E676 / #00B0FF / #0B1C33` | `DashPalette.Mint / Cobalt / Ink` (light-theme equivalents) |

### Deliberate divergences (intentional, not defects)

1. **Data honesty beats mockup copy.** Stitch hardcodes figures and shows a check-in
   clock time the domain does not store. The screen derives every value from
   `MemberSnapshot`; missing data renders `—`, never `0`, and Today's Activity shows only
   what the model can prove.
2. **Attendance denominators.** Stitch divides counters by 31 and shows `77%` under
   "Present" while counting `Late` separately, which is internally inconsistent. Here
   `Present` includes late visits and its share uses the same closure-excluded base as
   the attendance rate shown above it, so the two numbers can never disagree. `Gym Off`
   carries no percentage because closures are excluded from that base by definition.
3. **Closures.** Stitch paints off-days with a striped pattern. The chart marks schedule
   closures in slate and excludes them from both the attendance rate and the workout
   averages.
4. **Chart encoding.** Bars are coloured by the declared duration bands
   (High / Good / Normal / Low) rather than one flat accent, so the legend explains the
   colours instead of being decorative.
5. **Money.** `MoneyFormat.inr` renders `₹2,800` with no space.
