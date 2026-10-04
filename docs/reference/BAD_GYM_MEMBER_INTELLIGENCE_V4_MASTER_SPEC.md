# BAD GYM — Member Intelligence V4
## Production UI/UX + Android Implementation Master Specification

**Status:** ACTIVE DESIGN SOURCE OF TRUTH  
**Target:** Android phone, Kotlin + Jetpack Compose  
**Primary card geometry:** 350dp target width, 500dp preferred height, 600dp hard adaptive ceiling  
**Reference canvas:** 691×1536 px, 20:9  
**Visual direction:** premium light neumorphism / soft surface, emerald-led semantic system  
**Design reference:** the latest approved compact Member Intelligence concept board generated from the user's reference and V4 architecture.

---

## 1. PRODUCT PRINCIPLE

Member Intelligence is not a dashboard dump. It is a compact operational decision surface.

At a glance the gym owner must understand:

1. WHAT EVENT IS THIS?
2. WHO IS THE MEMBER/PERSON?
3. WHAT IS IMPORTANT RIGHT NOW?
4. WHAT ACTION, IF ANY, IS REQUIRED?
5. HOW IS THE MEMBER PERFORMING?
6. WHERE CAN MORE DETAIL be opened?

The runtime card must show only the highest-value information. The complete widget catalog can be large; the runtime composition must be selective.

**Never solve missing information density by adding meaningless widgets.**

---

## 2. PHYSICAL CONSTRAINTS

### Runtime card

- Target width: **350dp**
- Responsive minimum: approximately **320dp**
- Preferred height: **500–540dp**
- Maximum normal adaptive height: **600dp**
- No routine state may exceed 600dp.
- No tablet-sized composition.
- No desktop layout squeezed into a phone.
- No clipped text, overlapping widgets, or unreadable charts.
- If a real detail cannot fit, move it to an expandable detail surface/bottom sheet rather than shrinking it into illegibility.

### Reference screenshot

- 691×1536 px
- 20:9
- Use this for screenshot composition and visual comparison.
- Do not stretch the card to fill the whole screen.

---

## 3. EVENT-FIRST ARCHITECTURE

The first visual element is always the **Hero Event**.

Never use "Member Overview" as the hero title.

Supported hero examples:

- CHECK-IN
- CHECK-OUT
- PAYMENT
- PAYMENT DUE
- PAYMENT OVERDUE
- TRAINER
- WALK-IN
- RENEWAL
- MACHINE ISSUE
- STAFF ALERT
- SERVICE
- MEMBER UPDATE

The hero event contains:

- event icon
- event title
- primary value/status
- time/date
- compact secondary state
- one primary action when action is required

Examples:

### CHECK-IN
CHECK-IN
6:21 PM
ACTIVE · 45 min

### CHECK-OUT
CHECK-OUT
7:06 PM
COMPLETED · 1h 12m

### PAYMENT
PAYMENT
₹2,800
UPI · 10:42 AM

### PAYMENT DUE
PAYMENT DUE
₹2,800
Due in 3 days

### TRAINER
TRAINER
6:00 PM
Strength Session

### MACHINE ISSUE
MACHINE ISSUE
Leg Press
CRITICAL

The hero must be recognizable without reading the rest of the card.

---

## 4. EVENT PRIORITY ENGINE

Each event receives a priority score.

Suggested baseline:

- critical machine/safety/staff issue: 100
- payment overdue: 95
- active check-in: 90
- payment due soon: 85
- trainer/session issue: 80
- check-out: 75
- payment transaction: 72
- walk-in: 70
- attendance concern: 60
- workout insight: 50
- body progress update: 45
- normal member overview: 30

These are defaults, not hard-coded truth. The engine must permit admin configuration.

The layout engine selects the highest-priority relevant event as hero.

Then it chooses supporting widgets based on:

- relevance
- urgency
- available space
- data availability
- entitlement
- member configuration
- admin layout
- event state

Widgets must reflow when the hero changes.

---

## 5. NORMAL MEMBER DEFAULT COMPOSITION

For a regular paid member with no trainer:

1. Hero: CHECK-IN / CHECK-OUT
2. Member Identity + membership
3. Attendance 30-day intelligence
4. Workout 30-day/month overview with 7-day viewport
5. Body Goals / Progress, up to 3 active metrics
6. Compact progress-photo strip when progress data exists
7. One action only when an action is actually needed

Do not render:

- permanent Payment Completed
- PT: No
- Recent Activity
- generic Achievements
- generic Streak
- fake health metrics
- random offers
- redundant Plan widget
- redundant History widget

---

## 6. MEMBER IDENTITY WIDGET

Purpose: identify the person and membership context.

Show:

- member photo
- full name
- member ID
- age
- gender only where appropriate to the product/account configuration
- current member status
- plan name
- days remaining
- total plan duration
- validity date
- last payment date
- next due date when applicable
- trainer only when assigned

Example:

Aman Tripathi
BG305 · 23 yrs

Gold Plan
18 days left / 30 days
Valid until 20 Oct 2026

Last payment: 02 Sep 2026
Next due: 20 Oct 2026

Use a compact progress bar for membership time.

Do not create a second full Plan card.

---

## 7. COMMUNICATION

Within identity or hero action area:

- Call
- Message/Chat

If there are unread messages, show a small badge such as 1 or 2.

Tap Message opens the member conversation surface.

Tap Call starts the call action.

Do not create a permanent messaging widget.

---

## 8. PAYMENT LOGIC

Payment is conditional.

### If payment is due

Show:

PAYMENT DUE
₹2,800
Due in 3 days
Last payment: 02 Sep 2026

Primary:
COLLECT PAYMENT

Secondary:
REMIND

Optional:
CALL / CHAT

### If overdue

Hero may become:

PAYMENT OVERDUE

Use red semantic treatment.

### If paid

Do NOT render a permanent "PAYMENT COMPLETED" widget.

Instead membership identity continues to show:

Last payment
Next due
Days remaining

Use the released space for attendance/workout/body intelligence.

### Payment transaction event

If the current event itself is a payment:

Hero:
PAYMENT
₹2,800
UPI · date/time

Then show membership/attendance/workout support information.

---

## 9. ATTENDANCE WIDGET

Attendance is one of the core widgets.

### Primary information

- percentage
- visits completed / expected days
- 30-day calendar

Example:

68%
20 / 30

### 30-day visualization

Every date is represented.

Present:
- green filled center

Late:
- green filled center
- amber/yellow border

This is mandatory because yellow-filled dots are too easily confused with another state.

Absent:
- red filled marker

Holiday/Gym Closed:
- neutral marker with clear X

Use real calendar dates and weekday alignment.

### Secondary summary

Present 20
Late 4
Absent 5
Holiday 1

Avoid duplicate "last visit", "average visits", "weekly visits", etc. unless they provide a decision benefit.

### Detail interaction

Tap Attendance → full Attendance Details bottom sheet/page.

It must include:

- full month
- month selector
- calendar
- totals
- target vs actual if configured
- late pattern
- attendance trend
- relevant explanation

---

## 10. WORKOUT INTELLIGENCE

Workout is not just a 7-day chart.

The data model should support at least **30 days**.

### Runtime compact view

Title:

WORKOUT DURATION · September 2026

Show a horizontally paged monthly chart.

The complete month can contain 30/31 daily values.

Because 30 bars cannot be comfortably labeled in 350dp:

- visible viewport: approximately 7 days
- horizontal swipe: next/previous 7-day window
- month-level summary remains visible above/below

Example:

15–21 Sep

Mon 55m
Tue 80m
Wed 40m
Thu 95m
Fri 120m
Sat 0m
Sun 70m

Swipe:

22–28 Sep

Then:

29–30 Sep

### Monthly summary

Always show:

Total:
1,850 min

Average:
62 min

Peak:
120 min · 19 Sep

Lowest:
20 min · 3 Sep

Trend:
UP +12% vs previous month

Best week:
16–22 Sep

If there is a meaningful monthly decline:

DOWN −14%

If mixed:

MIXED TREND

Never fabricate an interpretation.

### Chart requirement

Every bar must have:

- date
- duration
- selectable state
- tooltip/detail

Selected day shows:

19 Sep
120 min

### Detail surface

Tap Workout → Workout Details.

Tabs:

- Duration
- Workout Type
- Muscle Group

Default Duration.

Only show tabs when data exists.

---

## 11. BODY GOALS / PROGRESS

This is a goal-aware intelligence widget, not a weight widget.

Maximum compact card display:

**up to 3 active member goals/metrics**

Possible goals:

- weight loss
- weight gain
- muscle gain
- body-fat reduction
- strength
- bodybuilding
- running
- endurance
- calisthenics
- physique development
- waist reduction
- specific muscle development

### Examples

Weight-loss member:

WEIGHT
72 → 68 kg
−4 kg

BODY FAT
24 → 21%
−3%

WAIST
36 → 33.5 in
−2.5 in

Muscle-gain member:

MUSCLE
31 → 32.4 kg
+1.4 kg

STRENGTH
80 → 100 kg
+20 kg

WEIGHT
67 → 69.5 kg
+2.5 kg

Runner:

5K
32 → 27 min

ENDURANCE
6 → 9 km

LEG PERFORMANCE
72 → 91

### Goal direction

Never assume + is good.

If goal direction is:

GAIN:
positive gain can be green.

LOSS:
negative change can be green.

If goal direction is unknown:
neutral.

### Trend classification

Use real historical observations.

Possible:

UPTREND
DOWNTREND
STABLE
MIXED TREND

Do not predict future outcomes.

### Visualization

Each metric should have:

- current
- previous
- delta
- mini trend
- goal if available
- direction-aware semantic state

Avoid one giant generic line graph.

---

## 12. BODY PRIVACY

Body measurements can be sensitive.

Implement visibility settings.

Member/admin setting:

- show
- hide
- authorized staff only

Sensitive fields can be hidden behind:

- eye icon
- "Show metrics" action

Tap expands/reveals them.

When hidden, do not expose the values in the compact card.

The product must support role-based authorization.

---

## 13. PROGRESS PHOTO TIMELINE

Progress photos are useful secondary intelligence.

Compact runtime:

5–6 chronological thumbnails.

Example:

Apr
May
Jun
Jul
Aug
Sep

Latest highlighted.

Where available, associate photos with:

- weight
- body fat
- muscle
- measurement date
- goal state

Example:

Sep
68 kg
21% BF

Tap → Member Progress detail.

Detail view can show:

Photos
Measurements
Comparison

Use privacy controls.

Do not make this a social-media feed.

---

## 14. TRAINER WIDGET

Only show if trainer is assigned.

Hero example:

TRAINER
Today · 6:00 PM
Strength

Secondary:

Rahul
Session 6 / 12

Actions:

Message
Call

If no trainer:

Do not show "PT: No".

Use the available space for a relevant widget.

---

## 15. OTHER CONDITIONAL WIDGETS

Supported catalog:

- Payment
- Trainer
- Renewal
- Next Workout
- Service
- Offer
- Machine Issue
- Staff Alert
- Walk-In
- Membership
- Attendance
- Workout
- Body Goals
- Progress Photos

Runtime should choose only relevant widgets.

---

## 16. WIDGET VISUAL GRAMMAR

Every widget must communicate its purpose through its own visualization.

### Current Event
Hero banner + status + time/duration

### Member Identity
Photo + identity + membership progress

### Payment
Large currency value + due state + action

### Attendance
Donut + 30-day calendar

### Workout
Daily-duration bar chart + monthly summary

### Body Goals
Multiple mini trend cards

### Trainer
Appointment/session card

### Progress
Chronological photo strip

Do not wrap every widget in identical generic cards.

---

## 17. ACTION MODEL

Every actionable widget gets one primary CTA.

Examples:

Payment Due:
COLLECT PAYMENT

Check-In:
END SESSION

Check-Out:
VIEW SESSION

Trainer:
MESSAGE

Machine Issue:
MARK IN PROGRESS

Staff Alert:
CALL

Do not create multiple equal-weight buttons.

Secondary actions can live under More/detail.

---

## 18. PAYMENT ACTION FLOW

Collect Payment opens bottom sheet:

- due amount
- received amount
- payment method
- note
- Record Payment
- Remind Instead

Methods:

Cash
UPI
Card
Bank Transfer

After recording:

PAYMENT RECEIVED
amount
method
time
receipt number

Partial payment:

Due
Received
Remaining

Reminder:

Preferred channel
WhatsApp
SMS fallback
Email optional

Show delivery state.

---

## 19. NEUMORPHIC VISUAL SYSTEM

Use modern, restrained neumorphism.

Base:

#F4FBF8 / soft warm-white family

Primary emerald:
#00B86B

Mint:
#E6F8F0

Blue:
#367FE8

Amber:
#E59A1A

Red:
#E54848

Purple:
#7659E8

Primary text:
#102B26

Muted text:
#6C8580

Border:
#DCEDE7

Use:

- soft raised surfaces
- subtle inset surfaces
- low-opacity borders
- controlled elevation
- rounded corners
- clean spacing

Avoid:

- black backgrounds
- dark glass
- cyan overload
- neon everywhere
- huge shadows
- heavy blur
- toy-like 3D
- decorative objects
- rainbow gradients

Color must communicate semantics, not decoration.

---

## 20. GEOMETRY TOKENS

Use centralized Compose design tokens.

Suggested starting values:

card width: 350dp
card min width: 320dp
preferred height: 520dp
adaptive max: 600dp

outer padding: 12–16dp
widget gap: 8dp
inner widget padding: 10–12dp
major radius: 18–24dp
small radius: 10–14dp
touch target: minimum 48dp
hero height: 56–68dp
identity: 78–92dp
compact action: 44–48dp

Do not hard-code geometry throughout individual composables.

Create a design-token layer.

---

## 21. WIDGET LAYOUT MODEL

The widget model must support:

- id
- priority
- enabled
- size
- order
- position
- visibility condition
- entitlement
- data availability
- hero eligibility
- compact renderer
- detail renderer
- primary action

Logical sizes:

XS
S
M
L
XL

But actual rendering must be constrained by the 350dp card.

If a size does not fit:

reflow or open detail.

Do not clip.

---

## 22. ADMIN COMMAND CENTER

Separate admin-only configuration from runtime member state.

Admin can:

- enable/disable widget
- change priority
- resize
- drag
- reorder
- configure visibility rules
- configure event priorities
- configure plan/entitlement
- preview
- reset
- save draft
- publish

### Drag/drop behavior

Use long press on a widget in admin mode.

Show:

- drag handle
- insertion indicator
- drop target
- live preview
- collision/reflow behavior

Do not allow overlapping widgets.

When a widget moves:

- surrounding widgets reflow
- no content is clipped
- order is deterministic

### Configuration layers

SYSTEM DEFAULT
↓
GYM DEFAULT
↓
ADMIN OVERRIDE
↓
RUNTIME DATA / EVENT PRIORITY

Server entitlement remains authoritative.

---

## 23. ANDROID IMPLEMENTATION

Use Kotlin + Jetpack Compose.

Architecture:

- domain models
- repository/data layer
- ViewModel/state
- UI components
- layout engine
- entitlement resolver
- event priority resolver
- widget condition resolver
- admin configuration
- detail sheets/screens

Avoid one giant composable.

Suggested components:

MemberIntelligenceCard
HeroEventWidget
MemberIdentityWidget
MembershipSummary
PaymentWidget
AttendanceWidget
WorkoutDurationWidget
BodyGoalsWidget
ProgressPhotoWidget
TrainerWidget
ConditionalWidgetHost
WidgetLayoutEngine
WidgetDetailSheet
AdminWidgetEditor
AdminWidgetPreview
WidgetPriorityResolver
MemberProgressPrivacyController

Use stable keys for Lazy layouts.

Avoid unnecessary recomposition.

Use state hoisting.

---

## 24. CHART IMPLEMENTATION

Prefer the least dependency-heavy solution that provides production reliability.

For compact charts:

- Compose Canvas
- Path/Line
- drawRoundRect
- drawCircle
- custom calendar grid
- animated progress
- LazyRow for monthly workout paging

If an external chart library materially improves accessibility, tooltip interaction or maintainability, use a currently compatible library after inspecting the project's existing Gradle/version catalog.

Do not add a chart library merely for decoration.

Do not introduce duplicate chart libraries.

---

## 25. IMAGE / PHOTO ASSETS

If member photos are needed for the visual reference:

- use generated placeholder assets only for design fixtures
- do not hard-code fake photos as real member data
- store assets in an appropriate drawable/resource path
- prefer WebP where suitable
- use content descriptions
- use circular crop only where required
- maintain consistent aspect ratio

If an AI-generated transparent icon/asset is required:

- generate at exact required dimensions
- transparent background
- no baked-in UI text unless the text is truly part of the asset
- no unnecessary shadow baked into the image
- Android applies elevation/shadow itself

Do not generate bitmap charts when the chart can be rendered from real data.

---

## 26. ACCESSIBILITY

Minimum touch target:
48dp.

Provide:

- content descriptions
- semantic labels
- chart summaries
- color-independent state indicators

Do not communicate Late only through yellow.

Do not communicate Absent only through red.

Holiday uses X.

Selected states use shape/border/icon in addition to color.

Support font scaling without catastrophic clipping.

---

## 27. DATA INTEGRITY

No fake runtime data.

Fixtures are allowed only in preview/debug/test environments.

Production UI must use:

MemberSnapshot
MemberEvent
payment records
attendance records
workout records
body measurements/goals
trainer assignment
media/progress records
entitlements

The UI must gracefully handle missing data.

Example:

No workout data:
do not render a fake chart.

No body goal:
show a compact neutral state or omit Body Goals.

No trainer:
omit Trainer.

No payment due:
omit Payment Alert.

---

## 28. PERFORMANCE

Target smooth interaction on Xiaomi 11i-class hardware.

Avoid:

- nested unnecessary LazyColumns
- huge bitmap assets
- continuous animations
- per-frame database reads
- recomposition caused by timers across the whole card

For active session timer:

update only the timer state.

For charts:

animate only on entry/state change.

Do not continuously animate charts.

Use image loading/caching appropriately.

---

## 29. TEST MATRIX

Create automated tests for:

### Hero
- check-in
- check-out
- payment
- payment due
- trainer
- machine issue
- staff alert

### Membership
- 30/30
- 18/30
- 1/30
- expired

### Payment
- paid
- due
- overdue
- partial
- reminder
- failed reminder

### Attendance
- present
- late
- absent
- holiday
- mixed month
- empty month

### Workout
- 0 data
- 7 days
- 15 days
- 30 days
- peak
- lowest
- mixed trend

### Body goals
- weight loss
- weight gain
- muscle gain
- body fat
- strength
- running
- multiple goals
- no goal
- hidden sensitive data

### Trainer
- assigned
- unassigned

### Layout
- widget disabled
- widget reordered
- widget resized
- widget moved
- insufficient space
- entitlement denied

---

## 30. VISUAL REGRESSION

For every meaningful UI stage:

1. build
2. install/run
3. capture screenshot
4. compare against approved visual reference
5. identify geometry/color/hierarchy discrepancy
6. fix
7. rebuild
8. recapture
9. verify

Compilation alone is NOT visual verification.

For the compact card specifically verify:

- width
- height
- hero position
- identity position
- attendance geometry
- workout chart readability
- body goal readability
- CTA position
- corner radius
- spacing
- colors
- typography
- clipping
- overflow

Target comparison canvas:
691×1536, 20:9.

---

## 31. DEVICE VERIFICATION

If the Xiaomi 11i/device agent is reachable:

- install debug APK
- launch BAD GYM
- navigate to Member Intelligence
- exercise each event state
- capture screenshots
- test touch targets
- test horizontal workout paging
- test bottom sheets
- test chat/call action entry
- test admin drag/drop
- test widget resize
- test widget enable/disable
- test privacy reveal/hide
- test rotation/configuration changes if supported

If the device is not reachable, do not falsely report device verification. Record it as BLOCKED.

---

## 32. BUILD / TEST COMMAND

Use the repository's current Gradle setup.

Baseline:

`./gradlew clean testDebugUnitTest assembleDebug`

Then use the project's connected-device/instrumentation command if available.

Do not invent a successful result.

Record:

- commit SHA
- changed files
- build result
- unit test result
- instrumentation result
- screenshot evidence
- device result
- unresolved issues

---

## 33. PRODUCTION ACCEPTANCE

The feature is production-ready only when:

- card fits 350dp target
- normal height stays around 500–540dp
- no normal state exceeds 600dp
- hero event is immediately recognizable
- member identity is clear
- days remaining and plan duration are visible
- last payment is visible
- next due is visible where relevant
- attendance shows 30-day data
- late state is green center + amber border
- holiday is X
- workout supports monthly data
- workout uses 7-day horizontal viewport over a 30-day dataset
- monthly peak/lowest/trend are understandable
- body goals are member-specific
- up to three active body metrics are supported
- goal direction changes semantic interpretation
- progress photos can be associated with measurements
- sensitive body data can be hidden/revealed
- conditional widgets are actually conditional
- payment action is functional
- communication actions are functional
- admin can configure layout
- entitlement remains server-authoritative
- no fake production data
- no giant empty spaces
- no tablet layout
- no duplicated information
- no meaningless widgets
- no clipping/overlap
- screenshot comparison passes
- tests pass
- device verification passes when reachable

---

## 34. IMPLEMENTATION ORDER

Do not implement everything in one giant change.

Stage 1:
Design tokens + compact card shell

Stage 2:
Hero event engine

Stage 3:
Member identity + membership summary

Stage 4:
Attendance 30-day widget

Stage 5:
Workout 30-day data + 7-day paging

Stage 6:
Body goals + multi-metric trends

Stage 7:
Progress photos + measurement linkage

Stage 8:
Payment states/actions

Stage 9:
Trainer/conditional widgets

Stage 10:
Widget layout engine

Stage 11:
Admin command center

Stage 12:
Privacy/accessibility

Stage 13:
Automated tests

Stage 14:
Visual regression

Stage 15:
Device verification

Stage 16:
Performance + production hardening

Do not proceed to the next stage if the previous stage has an unresolved visual or functional regression.

---

## 35. AI EXECUTION RULE

The implementing AI must inspect the existing repository before changing code.

It must not blindly replace working domain logic.

It must:

1. read repository instructions
2. inspect existing Member Intelligence architecture
3. inspect current models/repositories
4. identify reusable code
5. identify obsolete UI code
6. create/update design tokens
7. implement one component at a time
8. build after each meaningful component
9. run tests
10. capture screenshot
11. compare visually
12. fix
13. update execution ledger
14. continue

Never claim completion because compilation succeeds.

Never claim device verification without actual device evidence.

Never invent unavailable libraries, APIs, models or device capabilities.

---

## 36. FINAL UX RULE

**LESS RUNTIME WIDGETS. MORE INTELLIGENCE.**

The widget catalog may be large.

The runtime card must remain compact.

One hero event.
One member.
A few high-value supporting widgets.
One meaningful action.

Everything else belongs behind interaction.

The user should understand the member before reading the member.

The chart should explain the number.

The number should explain the state.

The state should suggest the action.

That is the BAD GYM Member Intelligence V4 contract.
