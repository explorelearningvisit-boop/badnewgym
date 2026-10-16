# Member Intelligence — Implementation Plan (MASTER CONTRACT pass)

Reference image: the final approved image attached to the contract conversation
(visually inspected; composition recorded below). Repository:
`explorelearningvisit-boop/badnewgym`, module `:app`, package
`com.example.badnewgym`.

## 1. Observed reference composition

Portrait, roughly 1024x1536 — about 1.9x taller than a 393x873dp phone viewport,
i.e. a scrolling mockup, not a single-screen layout.

1. **Integrated left rail** (~8% width) — stacked white pill cards, emerald icon,
   label and a green count: Home, Members 142, Check-In 8, Payments 12,
   Attendance 92%, Workout 68, Trainer 6, Nutrition 12, Supplements 4, History,
   Insights, More.
2. **Event header** — green gradient card, running-man icon, "Check-In" with
   subtitle "Tap to check-in this member", chevron action right. No app logo bar.
3. **Profile** — photo, name, "BG305 | 23 yrs | Male", plan badge + Active badge,
   day-progress bar ("12/30 days used", "18 days left"), plus three stat tiles:
   Streak, Total Visits, Avg. Visit Time, each with a vs-previous-month delta.
4. **Payment & Plan** — wallet icon, "Plan History", four columns (Current Plan /
   Next Payment / Previous Plan / Amount Due + Collect Payment), Send Reminder +
   View Details, a 12-month payment timeline, and a plan-tier legend.
5. **Attendance** — month nav + "This Month", a true 7-column Sun-Sat grid with
   colour-coded dates, a 90% donut, "27 / 30 Present Days", vs-previous delta,
   and a status legend with counts.
6. **Workout** — month nav + "This Month", bar chart on a 0 / 1.0h / 2.0h scale
   with all 31 days, a three-state legend (workout / rest day / no record), and
   five stat rows with mini bars.
7. **Body Progress** — "Last 6 Months" range, four metric tiles each with a
   sparkline and a status badge (Weight, Muscle Mass, Body Fat, BMI).

Palette: white and pale-mint surfaces, emerald accents, blue date labels, red
absences, dark-teal headings. No dark theme.

## 2. Repository audit

- Branch `main`. Unrelated uncommitted user work exists and is preserved
  (`AGENTS.md`, `README.md`, `backend/*`, `hybridtracking/*`, `tools/*`, res/xml).
- Canonical production screen is `MemberIntelligenceHome`, rendered by
  `MainActivity`. `MemberIntelligenceV4Activity` is a thin host that renders the
  same composable, so there is **one** implementation and no competing variant.
- Real data available: `MemberIdentity.age/gender/memberSince`,
  `MembershipStatus.daysRemaining/previousPlanName`,
  `MembershipHistory(planName, startDate, endDate)`,
  `PaymentSummary.history: List<PaymentTransaction>`, `lifetimePaid`,
  `AttendanceSummary`, and the attendance/workout/body analytics layer.
- **Gap:** `MemberEntity` has no `membershipHistory` or `promotion` column, so
  `toDomain()` silently drops plan history on the Room path.
- Fixture populates exactly one `PaymentTransaction`, and leaves
  `membershipHistory` empty.

## 3. Deviation from the reference, forced by data integrity

The reference shows an "Apex Plan" with a six-tier legend (Spark/Pulse/Rise/
Apex/Titan/Diamond) and a populated 12-month timeline. This repository's actual
business configuration uses **"Gold Plan"** and contains no plan-tier catalogue.
Per contract section 8, established plan names and commercial configuration are
not replaced with invented alternatives, and a 12-month history is not
fabricated to fill space. The 12-month timeline is therefore built to render
twelve real calendar months, marking only months that contain a real
transaction and showing the rest as no-record.

Deliberately not implemented, because no data source exists:
plan-tier legend, BMI tile (no height or BMI record is ever written), height,
calorie intake, unread message counts, trainer scheduling, session pause.

## 4. Phased work

- **P0 — contract violations in the current build.**
  1. Remove the decorative "BAD GYM / MEMBER INTELLIGENCE" logo bar (section 2
     forbids a decorative top logo header) and replace it with a real
     event header driven by actual check-in state.
  2. Rail restyled to white pills on the pale-mint canvas instead of a dark
     chrome frame.
  3. Attendance rebuilt as a true 7-column Sun-Sat grid with correct weekday
     alignment and leap-year handling.
- **P1 — reference modules that can be built honestly.**
  4. Payment 12-month timeline from real transactions, with no-record months
     marked explicitly.
  5. Workout three-state chart (workout / rest day / no record) and stat rows.
  6. Profile age, gender, joined date and the three stat tiles.
  7. Body Progress tiles with sparklines.
- **P2 — reported, not faked.** Height, BMI, plan-tier legend, nutrition
  intake, message badge, pause/resume.

## 5. Verification method

`./gradlew clean testDebugUnitTest assembleDebug`, install on the connected
device, capture screenshots, compare against the reference, and read the UI dump
for bounds and text. Input injection is blocked on this MIUI device, so
interactive states are driven through the app's own debug bridge and deep links
and read back from device screenshots.