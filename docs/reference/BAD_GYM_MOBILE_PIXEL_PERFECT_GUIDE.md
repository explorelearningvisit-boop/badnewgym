# BAD GYM — Mobile Pixel-Perfect UI/UX Implementation Guide
Reference: Image 1/24 — Complete Member Card Shell
Source canvas: 1536 × 1024
Primary target: 360dp-class Android phone

## 0. Definition of pixel-perfect

Pixel-perfect does NOT mean copying bitmap pixels blindly. It means reproducing the same visual system and geometry at the target device density while keeping the UI native, responsive, accessible and data-driven.

Acceptance requires:
- same hierarchy
- same component boundaries
- same relative geometry
- same spacing rhythm
- same typography hierarchy
- same semantic colors
- same icon scale/weight
- same corner radii
- same elevation/shadow character
- same state behavior
- same responsive reflow
- same interaction affordances
- no hard-coded sample member data
- no fake controls

Compare at the same viewport and density before judging differences.

## 1. Reference geometry

The reference board specifies a mobile shell of approximately:
- total width: 360dp
- safe outer padding: 16dp
- vertical rail: 56dp
- content region: about 288dp
- rounded outer shell
- full-height mobile composition

Treat these as design tokens, not arbitrary values scattered through composables.

Recommended token layer:
- Screen horizontal safe padding: 16dp
- Rail width: 56dp
- Rail icon target: 40dp
- Rail/content gap: 8dp
- Content inner padding: 8–12dp
- Card radius: 18–30dp depending on hierarchy
- Small radius: 10–14dp
- Major vertical gaps: 8dp
- Section gaps: 10–12dp
- Minimum touch target: 48dp

Do not compensate for wrong geometry by adding random padding to child components.

## 2. Mobile viewport architecture

Use this hierarchy:

Root
└── MobileCardShell
    ├── System/status treatment
    ├── AppHeader
    └── MainRow
        ├── VerticalRail
        └── ContentViewport
            ├── MemberIdentity
            ├── CurrentVisit
            ├── WidgetGrid
            └── ActionRow

The rail is persistent. Content changes when the rail selection changes.

The card should occupy the available mobile viewport while maintaining the design's maximum width. Do not force a desktop dashboard into a phone.

## 3. Header

Reference header is compact and visually strong:
- emerald/green primary surface
- BAD GYM identity
- secondary Member Intelligence label
- search affordance
- notification affordance with badge
- member avatar
- sufficient top safe-area breathing room

Header rules:
- keep one visual focal point
- never allow notification/search/avatar to compete with the BAD GYM title
- align icon centers to one baseline
- use consistent 40–44dp icon button containers
- notification badge must remain attached to the icon, not the text
- preserve contrast against the green header

## 4. Vertical rail UX

The rail is the navigation spine.

Order:
Home
Check-In
Check-Out
Payment
Plan
Attendance
Gym Time
Workout
Trainer / PT
Body / Progress
History
Insight
Offers
More

Rules:
- Home selected by default
- icon-first visual recognition
- active item uses emerald/mint selected treatment
- inactive items remain visually quiet
- alert dot indicates actionable state
- locked item is visually distinct but not misleading
- never hide the rail behind a horizontal tab bar
- keep every primary rail target at least 40dp visually and 48dp touch-safe
- rail scrolling must not clip the last menu
- if 14 items cannot fit, use controlled rail scrolling rather than shrinking icons below usability

## 5. Member identity block

This is the identity anchor.

Visual hierarchy:
1. portrait
2. member name
3. member code / demographic metadata
4. plan name
5. remaining days
6. verification
7. plan progress
8. navigation affordance

The portrait should have enough visual mass to be recognized immediately.

Use real MemberSnapshot values. The reference's Aman Tripathi / BG305 / 23 yrs / Male values are visual examples only.

Do not make every line equally bold. The member name is the strongest text element in this block.

## 6. Current Visit block

This is the primary event widget.

For CHECK_IN:
- event icon
- CHECK-IN label
- time context
- punctuality state
- Active pill
- duration
- right-side action/chevron

For CHECK_OUT:
- switch semantics from live to completed
- preserve the same component geometry where possible
- change status language and accent
- expose visit duration/history meaning

The event must come from MemberEvent. Do not infer UI state from hard-coded strings.

## 7. Home widget hierarchy

The Home surface is not a grid of identical cards.

Priority order:
1. current event
2. urgent payment/action
3. attendance
4. plan
5. trainer/PT
6. gym time
7. body progress
8. secondary actions

Use different visual weights.

Recommended reference mapping:
- Current Visit: XL/Hero
- Payment: L/important
- Attendance: M
- Plan: S/M
- Trainer: S/M
- Gym Time: M
- Body Progress: M
- Actions: compact fixed-height row

Do not render every widget with identical height.

## 8. Widget sizing system

Canonical ratios:
- XS = 1×1
- S = 2×1
- M = 2×2
- L = 3×2
- XL = 4×2

Important: the ratio is semantic. On a narrow phone the actual pixel width changes with available content width.

Calculate cell dimensions from the available content width rather than hard-coding screen pixels.

Example concept:
contentWidth = viewportWidth - safePadding - railWidth - gaps
cellWidth = derived from contentWidth and column count
widgetWidth = cellWidth × span + internal gaps

Do not use independent magic widths for every widget.

## 9. Payment Due UX

Payment is an action-oriented widget.

Hierarchy:
- payment status/icon
- title
- outstanding amount
- due/overdue timing
- primary action

For overdue:
- use red as semantic emphasis, not as the entire screen background
- amount should be visually dominant
- CTA must look tappable
- CTA touch target should remain comfortable
- preserve nested sub-visit structure

Nested structure:
PaymentDue
├── Status
├── Amount
├── DueDate
└── CollectPaymentAction

## 10. Charts and data density

The reference uses compact visualizations.

Rules:
- charts must communicate a metric first, decoration second
- labels must remain readable at 360dp
- do not squeeze legends into unreadable text
- use progressive disclosure for detailed analytics
- avoid chart axes when the tiny scale cannot support them
- preserve semantic color consistency across widgets

Attendance:
- dominant percentage
- count
- compact status legend
- compact date indicators

Body Progress:
- current value
- delta
- compact trend line
- no unnecessary axis clutter

Gym Time:
- headline duration
- trend percentage
- compact bar visualization

## 11. Bottom actions

Reference actions:
Message
View Details
Member History
More Actions

Rules:
- all four must be visually equal in importance unless one is contextually primary
- use icon + short label
- maintain a minimum comfortable touch target
- do not make action labels wrap unpredictably
- keep action row visually separated from data widgets
- More Actions opens secondary operations instead of expanding the entire card

## 12. Color system

Primary:
- emerald / mint

Semantic:
- blue = informational/attendance/neutral insight
- red = overdue/urgent/payment action
- amber = plan/due/warning
- purple = analytics/insight/trainer-related secondary accent
- green = active/success/current

Do not use arbitrary per-card colors.

Background:
- light premium surface
- white/frosted cards
- subtle mint gradients
- very soft border
- restrained shadow/glow

No black/dark workspace.

## 13. Typography

Use a deliberate type scale.

Hierarchy:
- App title: strong
- Member name: strong
- Section/widget title: medium/strong
- Primary metric: bold/heavy
- Supporting metadata: regular/medium
- Micro labels: compact but never illegible

Rules:
- never shrink text just to force a widget to fit
- line height must be intentional
- use maxLines/ellipsis where appropriate
- align numerical values consistently
- keep labels and metrics on predictable baselines

## 14. Iconography

Use one coherent icon family.

Rules:
- same optical weight
- same visual bounding box
- 18–24dp icon sizes for compact UI
- 40–44dp container for primary icon actions
- no random emoji as production UI icons
- badges/dots are separate semantic overlays
- icon + label combinations must share alignment

## 15. Surface treatment

Reference uses premium light material:
- rounded surfaces
- white/frosted fill
- subtle green/semantic tint
- 1dp-ish border
- restrained shadow
- occasional gradient for hero/urgent emphasis

Do not stack excessive shadows. The design should feel soft, not inflated.

## 16. Responsive behavior

### 320–360dp small phone
- preserve rail
- reduce content density
- prioritize current visit/payment/attendance
- allow secondary sections to scroll
- do not reduce touch targets

### 360–390dp normal phone
- target reference geometry
- two-column widget opportunities where useful
- retain all major reference hierarchy

### 400–430dp large phone
- increase widget breathing room
- preserve visual proportions
- avoid simply stretching every card

### 600–800dp tablet
- use additional horizontal space for widget spans
- keep rail hierarchy
- do not scale phone UI 2×
- reflow semantic widgets

### 800dp+ foldable/open
- support wider content composition
- preserve one coherent member-card model
- allow more widgets to appear simultaneously
- maintain hierarchy rather than filling empty space arbitrarily

## 17. State matrix

Every visual state must be verified independently where applicable:
- CHECK_IN
- CHECK_OUT
- PAYMENT
- WALK_IN
- TRIAL
- payment current
- payment overdue
- attendance available
- attendance unavailable
- trainer available
- trainer unavailable
- service available
- service unavailable
- loading
- empty
- error
- entitlement granted
- entitlement revoked
- subscription inactive

A widget that is not entitled must not merely become a fake empty card. It should be hidden, locked or replaced according to the product rule.

## 18. Pixel comparison protocol

Never compare screenshots by intuition alone.

For every atomic component record:
- bounding rectangle
- left/right/top/bottom spacing
- component width/height
- radius
- icon bounds
- text baseline
- font size/weight
- color
- border
- shadow
- alignment
- clipping
- scroll position
- interaction state

Compare:
1. global shell
2. rail
3. header
4. identity
5. event
6. widget grid
7. individual widgets
8. bottom actions
9. full screen

Fix the largest geometry mismatch first.

Do not micro-tune colors while the layout width or vertical rhythm is wrong.

## 19. Implementation order

A01 shell
A02 status/header
B01 rail shell
B02 active Home
C01 identity
D01 current visit
E01 attendance
E02 payment
E03 plan
E04 trainer
E05 gym time
E06 body progress
E07 actions
F01–F09 widget system/editor
G01–G06 nested payment
H01–H14 state matrix
I01–I07 integration
J01–J07 production verification

Never jump ahead when an earlier component has a major unresolved mismatch.

## 20. Final acceptance

The final screen is accepted only when:
- the reference hierarchy is preserved
- the shell geometry matches target dimensions
- rail is persistent and usable
- identity and event blocks match
- widgets use the correct semantic sizes
- states are real
- entitlements are authoritative
- interactions work
- responsive layouts reflow correctly
- screenshots have been compared after implementation
- major mismatches have been fixed
- build/tests pass
- final SHA is recorded in the execution ledger

The objective is not “looks similar”.
The objective is a production implementation whose rendered result is demonstrably aligned with the supplied reference.
