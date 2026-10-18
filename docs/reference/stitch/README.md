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
HTML/Tailwind output describing a *full phone screen*; the product requirement is a
single self-contained **200 dp x 300 dp** card, so the markup is not a drop-in asset.
It is committed to make the translation auditable, not to be rendered by the app.

Do not treat the numbers inside it as data. They are the mockup's design content
(`75%`, `25 / 31 Days`, `48m` average, `65m` peak, `24 / 77%` present, ...). The app
derives every value from real state and will legitimately differ.

## Fidelity map: Stitch design -> Compose card

| Stitch block | Compose implementation |
| --- | --- |
| `TopBar` — "BAD / GYM" + "MEMBER" caps + 3-dot menu | `CardBrandStrip` |
| `MemberProfileCard` — gradient avatar ring, crown, active dot, name, `BG305 \| 23 yrs \| Male`, Prime + Active badges | `MemberIdentityBlock` |
| Radial 75% meter with `25 / 31` + `Days` | `MembershipRing` (percentage is elapsed plan share; caption is `daysLeft / planTotalDays`) |
| `BodyStatsSection` — 4 tiles (weight / muscle / fat % / BMI band) | `BodyStatsRow` + `MetricTile` |
| `PaymentPlanSection` — amount, `Paid • On Time`, `Next Due`, days left, Prime chip | `PaymentPlanRow` |
| `AttendanceWorkoutSection` — legend, 31-day bar chart, `60m/30m/0m` axis, avg/peak/lowest, 4 counters | `ActivityBlock` -> `ActivityLegend`, `CombinedActivityChart`, `SummaryPill`, `AttendanceSummaryRow` |
| Colours `#00E676 / #00B0FF / #0B1C33` | `CardPalette.Mint / Cobalt / Ink` (light-theme equivalents) |

### Deliberate divergences (intentional, not defects)

1. **Geometry.** Stitch draws a full 430 px-wide phone screen. The card is locked to
   200 dp x 300 dp and is the only surface.
2. **No invented data.** Stitch hardcodes mock figures. The card derives all values
   from `MemberSnapshot`; missing data renders `—`, never `0`.
3. **Attendance denominators.** Stitch divides the counters by 31 (month length) and
   shows `77%` under "Present" while counting `Late` separately, which is internally
   inconsistent. The card takes every counter share against the month, and surfaces
   the visit rate separately as a labelled `Overall n%`.
4. **Closures.** Stitch paints off-days with a striped pattern. The card marks
   schedule closures in slate on the chart strip and excludes them from both the
   attendance rate and the workout averages.
5. **Money spacing.** Stitch shows `₹2,800`; the app-wide `MoneyFormat.inr` renders
   `₹ 2,800` (pre-existing formatter, unchanged here).
