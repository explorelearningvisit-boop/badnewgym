# BAD GYM Member Intelligence — Production Blueprint

## 0. Product intent

Build a real Android/Jetpack Compose Member Intelligence screen for a gym owner who scans a member for 2–4 seconds. The screen must answer, in this order:

1. Who is this member and is the membership active?
2. Is there a payment or membership action required now?
3. Is attendance healthy, declining, or irregular?
4. Is workout consistency/duration improving?
5. Are body measurements moving toward the member's declared goal?

This is a real component system, not a screenshot background, HTML page, tablet mockup, or decorative Dribbble concept.

## 1. Source of truth and constraints

- Platform: native Android.
- Language: Kotlin.
- UI: Jetpack Compose + Material 3.
- Package: `com.example.badnewgym`.
- Active module: `:app`.
- Existing project and domain models are the integration base.
- Use the member's actual `MemberSnapshot`, `MemberEvent`, menu state and action callbacks.
- Never hardcode a generated portrait as a real member. Render `identity.photoUrl` using Coil; show initials when absent.
- Never fabricate production payment, attendance, workout, or body data. If a history list is absent, show a compact honest empty state or omit that metric.
- Do not use a screenshot as the UI.
- No dark or black-heavy theme. The user explicitly dislikes dark backgrounds.
- Mobile-first. Do not squeeze a tablet design into a phone.
- Use a horizontal member menu, not a permanent vertical rail.
- No live session timer and no “End Session” button on the member intelligence summary card.
- No trainer widget unless the member has an actual trainer/session relationship.
- No permanent payment-completed alert. A fully paid member gets receipt/history/renewal context, not a false “Due” label.
- No duplicate facts across cards. Membership days remaining belongs in the member identity/membership ring; do not repeat the same day count in Payment & Plan.
- One primary CTA per actionable widget.
- Every chart must encode a real measure, period, value and direction.

## 2. Layout target

The attached reference is a long mobile dashboard, not a desktop dashboard. Implement a responsive screen whose content width is the available phone width, with a compact member intelligence surface and vertical scroll for the full set of analytics.

Recommended values:
- Reference device width: 360dp.
- Outer horizontal padding: 12dp on 360dp devices; reduce to 8dp if available width is below 340dp.
- Main content max width: 440dp; center only on wider screens.
- Card corner radius: 20dp (major), 14dp (nested metric tiles).
- Main card spacing: 10dp.
- Inner card padding: 12dp.
- Header photo: 68–76dp circle; crop to face, not whole body.
- Header identity name: 22sp bold, max 1 line.
- Member code / tenure / status: 13–14sp.
- Section heading: 18–20sp bold.
- Primary KPI: 26–32sp bold.
- Secondary KPI: 16–18sp semibold.
- Labels: 12–13sp; never below 11sp for meaningful data.
- Chart labels: 10–12sp only when they remain legible at 360dp.
- Minimum touch target: 44dp; icons can be 20–24dp inside the target.
- Use `sp` for text and `dp` for geometry. Do not scale text by bitmap scaling.
- Respect Android font scaling and screen density.
- The full dashboard may scroll vertically; individual compact charts can scroll horizontally where necessary. Do not force every analytics card into a 500dp-tall single view if doing so makes text illegible.

## 3. Visual system

### Base surfaces
- Page: `#F4FBFF` very pale blue-white.
- Card: `#FFFFFF`.
- Primary text: `#10224A` (deep navy text, not a dark background).
- Secondary text: `#526681`.
- Hairline border: `#DCE8F2`.
- Shadows: soft and short; use 2–6dp elevation, not heavy neumorphic bevels.
- Use subtle gradients only inside status/header panels; keep charts flat and readable.

### Semantic colors
- Member active / healthy attendance / payment received: emerald `#0AA86B`; pale surface `#E7F8EF`.
- Payment action required: coral-red `#E84B5B`; pale surface `#FFF0F1`.
- Workout information: cobalt `#267DE8`; pale surface `#EDF5FF`. When workout performance is below the member's baseline, use amber/red for the low data point; do not color all workout data blue regardless of meaning.
- Body metrics: purple `#8155E8` for section identity, but each metric trend uses semantic direction colors.
- Plan tier: amber/gold `#D99816`; pale surface `#FFF7DF`.
- Warning/late payment: amber `#E7A126`.
- Absent: red `#E84855`.
- Holiday/closed: neutral slate `#A3B2C4` with a visible X.
- Late attendance: green center with amber outline if that is the product rule; don't confuse it with absent.
- Avoid blue for successful payment. Payment received is green. Blue may identify a neutral date/calendar action only.

### Typography
- Prefer system sans-serif / Roboto for predictable Android rendering.
- Use a strong weight hierarchy: 800–900 for member name and key KPI; 700 for section title; 500–600 for labels.
- Never place thin type over gradients.
- Numeric data uses tabular figures where possible.
- Truncate only secondary labels; never truncate the amount, status, member name or due date.

### Shape and motion
- Use rounded rectangle cards, 1dp semantic border, gentle shadow.
- Motion should clarify state changes: 160–220ms, fade/size/color only.
- Avoid continuously animated charts or decorative floating effects.
- Animate ring progress only when entering the screen; respect reduced-motion/system animator settings.
- No excessive glass blur, glossy 3D icons, fake holograms, ornamental gym photos or meaningless shapes.

## 4. Component hierarchy

### A. Top application strip
- Brand: BAD GYM / MEMBER INTELLIGENCE.
- Search, notifications, overflow.
- This is a small top strip; never let it consume the member card's useful height.
- Notification count is shown only when real.

### B. Member identity and membership card
Purpose: identify the person immediately and show the membership state once.

Layout:
- Left: circular member photo, 68–76dp.
- Center: name, member code, optional age/gender only if actual fields exist.
- Beneath name: plan chip and active/expired/frozen status chip.
- Right: circular membership-progress ring. Center shows remaining percentage or days remaining; choose one primary number and keep the other as its small caption.
- Bottom micro-row: join date and branch/goal only when those fields exist.
- Membership ring is based on elapsed membership duration: `elapsedDays / totalPlanDays`. The text should say “X days left”; the ring must not misleadingly label remaining days as progress completed.
- No duplicate “135/180 days” text elsewhere if the ring already explains plan progress.
- Use member identity's actual image. Do not put a generic gym athlete in the hero area.

### C. Horizontal menu
- Horizontally scrollable chips/icons: Overview, Payment, Attendance, Workout, Progress, Nutrition, Services/Machine, More.
- At 360dp show roughly 4–5 compact destinations, with horizontal scroll for the rest.
- Selected state uses a pale mint fill and emerald icon/underline; unselected states use white surface and navy icon.
- Each menu item has a 44dp minimum touch area.
- Tapping a destination scrolls to the matching section and updates selected state. For deeper features, navigate to a dedicated detail screen; do not cram the full detail into this summary.
- Use labels with icons; don't rely on color alone.

### D. Payment & Plan
This is the most financially actionable widget, placed before attendance when payment is due. For a fully paid member, it remains compact and does not show “Due” or “Collect Payment”.

If payment is due:
- Heading: Payment & Plan.
- Red/pale-coral status: “PAYMENT DUE” / “OVERDUE X DAYS”.
- Primary amount: outstanding amount.
- Secondary: due date / overdue duration.
- CTA: “Collect payment” or “Send reminder”, selected by the available business action.
- Plan chip and membership period.
- Show last successful payment as a small contextual line, not a competing KPI.
- Next due is shown only when meaningful and known.
- If outstanding amount is zero, do not render a due alert or collection button.

If payment is clear:
- Green “PAID • ON TIME” state and last received amount/date.
- Primary CTA: “View receipt” or “Payment history”.
- Show next due date only if it is known and relevant.
- Keep the card's top row compact to make room for history.

12-month history:
- Use one 12-column monthly chart with labels Nov–Oct or actual months in the selected period.
- Each bar height is the amount actually paid that month; no transaction means no bar, not a made-up amount.
- Color encodes transaction behavior: green = paid on time, amber = late, red = very late, slate/dashed = no payment recorded, gold crown marker = plan upgrade.
- If multiple transactions exist in one month, aggregate amount for bar height but preserve payment status via color/marker only if status is known.
- Legend uses small dots and short labels. It must not consume more height than the chart.
- Tap chart/month opens detailed payment ledger.
- Do not repeat the membership progress ring/days in this widget.

### E. Attendance
- Heading: Attendance + selected month.
- Right-hand analytical KPI: circular percentage ring, e.g. 77%, with visits/open days caption.
- Calendar: 30/31 tiny but tappable dots, 7-column grid.
- Green = present, red = absent, amber outline/center = late, slate with X = holiday/closed.
- Sunday/holiday gets X only when gym calendar confirms it is closed; never assume every Sunday is a holiday if the gym operates on Sundays.
- Show unique analytical facts below/alongside the grid: current streak, best streak, and change vs previous month, only if computable from daily records.
- Do not repeat the same “24/31” number in multiple places.
- If daily history is unavailable, show the monthly attendance aggregate and a “Daily history unavailable” state instead of fabricating 31 dots.

### F. Workout Trend
- Heading: Workout Trend + month.
- Main chart: 30 thin vertical bars or a line chart, one point per day.
- Y-axis unit is minutes; label the scale clearly (0m, 30m, 60m).
- Zero means no recorded workout only when a reliable attendance/workout record proves it; missing data must be shown as unknown, not zero.
- Green = above personal baseline; pale green = near baseline; amber = low duration; red = no-show/zero only when confirmed; neutral = unknown.
- Right side or compact adjacent ring: average minutes/day.
- Peak, minimum, and month-over-month change are shown as three small summary cells, not large separate cards.
- Do not show a live timer on a member summary card.
- Tapping opens detailed workout logs, exercise/body-part focus, machine/exercise-level analytics and session minutes.
- If only latest workout duration exists, show that value with date and do not invent a 30-day series.

### G. Body Progress
- Heading: Body Progress + period label (“Since joined” or “Last 30 days”).
- 2×2 compact metric tiles on phone, not a four-column layout that makes each tile unreadable.
- Metrics: Weight, Muscle Mass, Body Fat, BMI only if actual measurements exist.
- Each tile contains label, current value, delta vs prior measurement, and a small sparkline only when there are at least two real measurements.
- Metric direction is goal-aware. Weight loss is not always good; muscle gain is not always good in every context. Use the member's goal when known; otherwise say “change” neutrally.
- BMI classification must use correct ranges and appropriate context.
- Use accessible colors: green for positive goal-aligned progress, amber for neutral/uncertain, red for goal-opposed trend. Purple is the section identity, not the default result color.
- Body data is sensitive: honor role/consent/visibility controls. If unauthorized, show a privacy-locked state.
- Tapping opens full measurements and progress photos.

## 5. Interaction contract

- Menu taps scroll to the section or navigate to a detail route.
- Payment CTA opens the actual payment collection/reminder/receipt flow. No dead buttons.
- Attendance dots open the date record if available.
- Workout chart point opens that date's session details.
- Body metric opens its measurement history.
- The member photo/profile opens full profile only when explicitly requested by an action; don't waste every widget's primary CTA on “View Profile”.
- Use loading, empty, error and stale/offline states.
- No success toast until the operation confirms success.
- Every clickable element must have semantics/contentDescription and keyboard/accessibility support.
- Use minimum 44dp touch targets and sufficient contrast.
- Respect TalkBack and font scale; verify at 1.0×, 1.3× and 1.5× text scale.

## 6. Native implementation architecture

Use the existing project:
- Kotlin + Jetpack Compose + Material 3.
- Existing `MemberIntelligenceScreen`, `MemberIntelligenceViewModel`, `MemberSnapshot`, `MemberEvent`, and repository interfaces.
- UI state is driven from `StateFlow`; do not put business rules inside composables.
- Keep one source of truth in the ViewModel/repository.
- Keep demo fixtures in the preview/stub repository only. Never treat demo fixture values as production data.

Recommended component split:
- `MemberIdentityCard.kt`
- `MemberHorizontalMenu.kt`
- `PaymentPlanCard.kt`
- `PaymentHistoryChart.kt`
- `AttendanceCard.kt`
- `AttendanceDotCalendar.kt`
- `WorkoutTrendCard.kt`
- `WorkoutMonthChart.kt`
- `BodyProgressCard.kt`
- `MiniSparkline.kt`
- `MembershipProgressRing.kt`
- `MemberIntelligenceDashboard.kt`

Recommended data contract extensions when real sources provide them:
- `AttendanceDay(date, status)`
- `WorkoutDay(date, durationMinutes?, sessionCount?, sourceStatus)`
- `BodyMeasurement(date, weightKg?, muscleKg?, bodyFatPercent?, bmi?)`
- Payment transaction status / plan-upgrade marker.
Keep new fields optional with safe defaults so older data adapters do not crash. Map real backend data into these fields before rendering daily charts.

## 7. Chart and asset strategy

- Use Compose Canvas for tiny custom ring charts, dot calendars, sparklines and 12-month bars. This avoids the overhead and visual mismatch of multiple chart libraries for small graphics.
- If the project later needs larger interactive charts, choose one Compose-native library such as Vico; do not install Vico and MPAndroidChart together.
- Icons: Material Symbols/Material Icons Extended for UI icons. Use consistent 20–24dp glyphs; no mixed icon packs.
- Member images: Coil Compose; crop with `ContentScale.Crop`, circular clipping and a neutral fallback.
- Font: Roboto/system sans for baseline; optionally bundle one approved variable font only if the project has licensing and asset review.
- Asset pack should contain only real brand assets and member-authorized photos: `bad_gym_wordmark`, `member_photo` (not a generic athlete), optional branch logo. Charts and icons are code-rendered, not rasterized into images.
- No AI-generated charts/text as assets. Text and chart labels must be actual Compose text so they remain sharp and accessible.
- For generated background texture, if any, use a very subtle abstract mint/white gradient only; never use it to represent data.

## 8. Build and verification checklist

Run:
```bash
./gradlew clean testDebugUnitTest assembleDebug
```

Then:
1. Open the screen on 360dp width and a 320dp narrow device profile.
2. Capture screenshots at default font scale and 1.3×.
3. Check that the name, payment status, amount, month labels and KPI remain readable.
4. Check all horizontal menu items can be reached.
5. Verify no repeated membership-day metric between identity and payment.
6. Verify a paid member never sees “Due” or “Collect payment”.
7. Verify absent/holiday/unknown attendance states are different.
8. Verify missing workout data is not drawn as zero.
9. Verify body metrics disappear or lock when data is unavailable/unauthorized.
10. Test every CTA and detail navigation.
11. Compare screenshot against the approved reference, then fix and recapture.
12. Report build, tests, screenshot/device verification honestly. Do not claim a device test if no device was connected.

## 9. Hyper-detailed AI coding prompt

You are the senior Android UI engineer implementing BAD GYM Member Intelligence in the existing Kotlin/Jetpack Compose project. Implement the approved attached reference as real native Compose components; never place the screenshot as a background and never build a tablet layout. First inspect `AGENTS.md`, `CURRENT_TASK.md`, the V4 spec, the current `MemberIntelligenceScreen`, ViewModel, domain models, stub scenarios and repository contracts. Keep package `com.example.badnewgym` and module `:app`. Preserve working business logic and device-agent code; do not touch unrelated projects or files.

Create a bright premium light dashboard with white cards, pale mint/blue/pink/lavender surfaces, emerald success, coral-red payment alerts, cobalt workout data, purple body-progress identity and gold plan-tier accents. No dark background. Build the member identity panel first, then a horizontally scrollable menu, then Payment & Plan, Attendance, Workout Trend and Body Progress. Keep member identity at the top; do not show a live timer or End Session. Use actual member photo via Coil, name/code/plan/status from domain data and a membership ring that correctly expresses elapsed/remaining plan duration. Do not repeat membership days in payment.

Payment must branch by real lifecycle: if outstanding amount > 0, show amount, due date, overdue status and one valid CTA; if outstanding is zero, show paid state, receipt/history action and meaningful next due only when known. Add a 12-month chart only from real transactions, with green on-time, amber late, red very late, slate missing and a gold plan-upgrade marker when the data source supports those facts. Do not make up amounts or payment status.

Attendance should use a real daily attendance list to render a 30/31-day calendar. Present=green, absent=red, late=amber semantics, holiday/closed=slate X. Sunday is not automatically a holiday unless the gym calendar says it is closed. Show a right-aligned circular attendance KPI and only compute streaks/trends when source data supports them. Unknown data must not become absence.

Workout Trend should render real daily session minutes, with thin bars/line, a readable minute scale, and compact average/peak/minimum/month-change summaries. A missing session record is not automatically zero; zero is allowed only when the data confirms no workout. Do not display a live timer. Body Progress should render only actual available metrics, with goal-aware trend direction and sparklines only when at least two measurements exist. Never assume weight loss or gain is automatically good. Respect privacy and role access.

Use Compose Canvas for custom rings, bars, dot calendars and sparklines; use Material Icons Extended for icons and Coil for photos. Do not add multiple chart libraries. Use 44dp touch targets, responsive width, semantic labels, contrast-safe text, font scaling, loading/error/empty states, and no dead actions. Move business logic into ViewModel/domain helpers. Add unit tests for payment branching, membership progress math, attendance status mapping, workout missing-vs-zero behavior and body trend direction. Build with `./gradlew clean testDebugUnitTest assembleDebug`; run the app, capture screenshots at 360dp and 320dp, compare to the reference, fix all clipping/overflow/illegible labels, and only then report completion. If a device or screenshot runner is unavailable, explicitly mark that verification as blocked. Finish with changed files, tests/build status, screenshot/device status and commit SHA.
