# BAD GYM Member Intelligence V5 — Visual Master Prompt for Antigravity

## Mission

Redesign the **existing BAD GYM Member Intelligence Android/Jetpack Compose UI in-place** so every member card follows one coherent, pixel-disciplined visual system derived from the supplied 8-style BAD GYM reference board.

This is NOT a request to create a mockup, a separate demo screen, a second dashboard, or fake data.

The reference board shows the same information architecture expressed through different visual personalities:

1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

The production app must support these visual personalities through the existing ThemeId/design-token architecture while preserving one canonical member-card information architecture.

---

# 1. SOURCE OF TRUTH

Use, in this order:

1. Existing GitHub repository code and domain/data contracts.
2. Existing Member Intelligence architecture and current branch.
3. The supplied 8-style BAD GYM reference board.
4. Existing real member snapshot/event/menu data.
5. Existing design/token infrastructure.

Never invent backend fields merely to make the screen look complete.

Do not replace working domain logic with demo models.

Do not hardcode example people, amounts, attendance, PT sessions, workout counts, payment balances, or dates from the reference image into production.

Reference-image people and numbers are **visual examples only**.

---

# 2. CURRENT PRODUCT CONTRACT

The card is a compact operational cockpit for one member.

Core law:

EVENT → MEMBER → TIME → STATE → EVIDENCE → PATTERN → FORECAST → DECISION → ACTION

Every rendered state must make these questions answerable:

- Who is this member?
- What just happened?
- When did it happen?
- What is the current state?
- What evidence supports the state?
- Is this NOW, PAST, or FUTURE?
- Does the owner need to act?
- What is the one next action?
- Where can the owner drill down for evidence?

---

# 3. CANONICAL CARD GEOMETRY

Do NOT create multiple unrelated card shells.

One canonical card contains:

- top application/header context
- compact event/time header
- member identity block
- plan/state chips
- KPI row
- evidence/decision content
- one primary CTA
- short LEFT rail
- short RIGHT capability rail

Both rails must be visible **inside the same card**.

### LEFT CORE RAIL

Fixed conceptual order:

1. Home
2. Attendance
3. Plan
4. Payment
5. More

### RIGHT CONTEXT/PREMIUM RAIL

Capability/data driven:

1. PT
2. Workout/Lift
3. Supplements
4. Nutrition/Diet
5. Services
6. History/Log
7. Insight/AI
8. Offers

Do not allow either rail to become a long scrolling navigation column.

If the physical viewport cannot fit all enabled contextual entries, redesign spacing/icon-label treatment rather than introducing an ugly scroll rail.

---

# 4. REFERENCE BOARD — VISUAL LANGUAGE

The reference board is showing a **design system**, not eight unrelated designs.

All eight personalities must share:

- same card proportions
- same rail geometry
- same information hierarchy
- same member identity placement
- same KPI structure
- same CTA position
- same interaction model
- same data semantics

Only the visual personality changes.

## A. NATURAL FRESH

Mood:
- clean
- wellness
- friendly
- organic
- trustworthy

Materials:
- ivory/white
- soft mint
- fresh green
- translucent botanical overlays
- subtle leaf/organic motifs

Do not turn this into a literal gardening UI.

## B. FUTURISTIC NEON

Mood:
- energetic
- high-tech
- athletic
- premium

Materials:
- controlled dark/navy surface
- cyan/electric blue/teal neon
- restrained glow
- glass panels
- fine luminous borders
- angular energy accents

Dark is allowed ONLY for this theme personality, not as the default BAD GYM product background.

## C. MINIMAL DARK

Mood:
- focused
- restrained
- premium
- professional

Materials:
- charcoal/graphite
- white/soft-gray typography
- low-noise surfaces
- limited accent color
- no excessive glow

## D. GLASSMORPHISM

Mood:
- translucent
- modern
- elegant
- premium

Materials:
- light translucent panels
- frosted surfaces
- subtle blur
- cool blue/lavender highlights
- controlled shadows
- high readability

## E. PREMIUM 3D

Mood:
- luxury membership
- elite
- high-value

Materials:
- deep warm neutral base
- champagne/gold accents
- dimensional cards
- subtle metallic highlights
- restrained 3D depth

Do not make it look like a gaming casino.

## F. VIBRANT GRADIENT

Mood:
- youthful
- energetic
- optimistic

Materials:
- controlled gradient backgrounds
- pink/coral/blue/purple transitions
- white surfaces
- bright semantic accents

Gradients must remain behind content, never behind critical text.

## G. GYM BEAST MODE

Mood:
- powerful
- intense
- motivational

Materials:
- red/black/dark graphite
- aggressive but disciplined geometry
- high contrast
- controlled diagonal energy marks

Do not use this as the default theme.

## H. PURPLE ROYAL

Mood:
- premium
- exclusive
- sophisticated

Materials:
- violet/plum
- lavender
- premium gradients
- subtle metallic/dimensional treatment

---

# 5. PIXEL-PERFECT RULE

The goal is not merely "similar".

Treat the reference board as a visual grammar.

Measure and normalize:

- card width
- card height
- rail width
- center content width
- corner radius
- border thickness
- internal padding
- portrait dimensions
- badge height
- KPI height
- CTA height
- icon size
- typography scale
- baseline spacing
- divider spacing

Create centralized design tokens.

Do NOT scatter magic numbers across composables.

Use density-independent dimensions appropriate to the actual Android viewport.

Verify on the real Xiaomi Redmi Note 11.

---

# 6. MEMBER IDENTITY

Identity must remain stable while menus change.

Required hierarchy:

MEMBER PHOTO
→ verification indicator
→ tier/status badge
→ member name
→ member code
→ concise member-specific headline

Never truncate a name so aggressively that it becomes unreadable.

Use a deterministic strategy:

- available width calculation
- single-line ellipsis only where necessary
- meaningful fallback
- accessibility content description

Never fake a portrait.

Use the existing real photo/fallback pipeline.

---

# 7. EVENT-DRIVEN VISUAL VARIETY

The card must visually react to the actual current/recent business state.

Examples:

- CHECK_IN
- WORKOUT_STARTED
- WORKOUT_COMPLETED
- TRAINER_SESSION_SCHEDULED
- TRAINER_SESSION_STARTED
- TRAINER_SESSION_MISSED
- PAYMENT_DUE
- PAYMENT_OVERDUE
- PAYMENT_FAILED
- PAYMENT_PARTIAL
- MEMBERSHIP_EXPIRED
- FREEZE_STARTED
- FREEZE_ENDED
- BANNED
- SERVICE_BOOKED
- SERVICE_USED
- SERVICE_PURCHASE
- MACHINE_FAULT
- MAINTENANCE
- CLEANING
- INCIDENT
- ISSUE_RESOLUTION
- INSTALLATION
- EQUIPMENT_OPERATION
- other real recurring gym events already represented by the domain model

The visual variant must be generated from the event/state.

Do NOT build 40 disconnected composables.

Build reusable visual archetypes with state/theme configuration.

---

# 8. SEMANTIC COLOR LAW

Color communicates business meaning.

Examples:

GREEN:
- active
- healthy/clear state
- successful completion

RED:
- overdue
- failed payment
- access block
- critical issue

AMBER:
- warning
- expiring
- maintenance attention

PURPLE:
- PT/trainer
- premium fitness/training context

BLUE/CYAN:
- information
- workout/technology where appropriate

Never choose a color merely because it looks attractive if it contradicts the semantic state.

---

# 9. KPI SYSTEM

The reference board uses compact, high-signal KPI cards.

Preserve that pattern, but use real data.

Typical dimensions:

Attendance:
- current visits / target
- attendance rate
- streak/consistency when derivable

Payment:
- outstanding amount
- next due
- payment state

PT:
- sessions remaining
- coach
- next session

Workout:
- workout count
- duration
- current routine
- progression when real

Do not show "0", "1", "2" just to fill space.

Use truthful:
- NO_DATA
- NOT_ENROLLED
- UNAVAILABLE
- ACTIVE
- EXPIRED
- WARNING
- CRITICAL
- RESOLVED

---

# 10. MENU SURFACES

Every menu needs its own information personality.

## Home
Cockpit:
- current event
- current state
- top signal
- member context
- critical payment/access/issue state
- attendance snapshot
- one useful trend
- one primary CTA

## Attendance
Use:
- Pattern
- Calendar
- Timing
- Day/Week/Month/Year
- real visit evidence
- useful chart only when enough data exists

## Plan
Use:
- lifecycle
- current plan
- start/end
- remaining days
- freeze/history/renewal
- benefits only when real

## Payment
Use:
- outstanding
- due date
- overdue state
- payment timeline
- transaction evidence
- recurring/scheduled state
- receipt drilldown

## PT
Use:
- coach
- sessions
- next session
- completed/missed/cancelled
- package validity

## Workout
Use:
- routine
- recent workout
- duration
- progression
- PR/history when available

## Supplements
Use:
- active purchases
- usage
- expiry/reorder
- inventory/offer only when real

## Nutrition
Use:
- plan
- meal/log data
- adherence/trends
- consultation/next action

## Services
Use:
- active entitlements
- usage
- bookings
- expiry
- Flex when real

## History
Use:
- unified evidence timeline
- timestamp
- actor/source
- drilldown

## Insight / AI
Only real intelligence:
- Action
- Pattern
- Risk
- Opportunity
- Forecast
- Explain

Every insight must expose evidence.

## Offers
Commercial content must never outrank:
- critical payment
- access problem
- safety
- operational issue

No fake offers.

---

# 11. MOTION

Motion should make the card feel alive, not distracting.

Use:

- 120–180ms micro interactions
- 180–260ms menu transitions
- 220–360ms carousel settling
- subtle scale/fade/slide
- state-specific entrance motion
- CTA press feedback

No infinite decorative animation.

Respect reduced-motion settings.

Do not animate the entire card unnecessarily on every recomposition.

---

# 12. MATERIAL SYSTEM

Use a unified material vocabulary:

- Soft Surface
- Elevated Surface
- Frosted Surface
- Glass Surface
- Semantic State Surface
- Premium Surface
- Neon Surface
- Dimensional Surface

All material variants must derive from tokens.

Avoid:
- random shadows
- random gradients
- random corner radii
- inconsistent borders
- excessive blur
- tiny unreadable text
- giant empty areas

---

# 13. RESPONSIVE BEHAVIOUR

The card must remain usable on the actual target phone.

Priority:

1. critical state
2. member identity
3. event
4. decision facts
5. CTA
6. secondary evidence

If space becomes constrained:

- reduce decorative layers first
- reduce secondary copy
- reduce spacing
- collapse low-priority evidence
- NEVER hide critical state
- NEVER make the rails scroll
- NEVER destroy readability

---

# 14. IMPLEMENTATION ARCHITECTURE

Before editing:

1. Pull exact current branch.
2. Inspect all Member Intelligence files.
3. Inspect design/token/theme files.
4. Inspect event model/catalog/router.
5. Inspect MenuAvailabilityResolver.
6. Inspect all menu panels.
7. Inspect MemberSnapshot and repository implementations.
8. Inspect tests.
9. Inspect existing screenshot/device evidence.
10. Identify duplicate/obsolete UI implementations.

Then create/refactor reusable primitives instead of duplicating UI.

Preferred architecture:

- MemberIntelligenceCardShell
- MemberIdentityHeader
- EventHeader
- CoreRail
- ContextRail
- MemberKpiRow
- StateBadge
- DecisionEvidenceCard
- Timeline
- TrendChart
- UsageMeter
- InsightEvidence
- PrimaryAction
- MaterialVariant
- ThemeTokens

Names may differ if equivalent existing primitives already exist.

Do not create duplicate abstractions if the repository already contains the correct reusable component.

---

# 15. DATA / BACKEND SAFETY

Absolutely no fake production data.

The following are prohibited:

- hardcoded member names
- hardcoded balances
- hardcoded attendance
- hardcoded PT sessions
- hardcoded workout totals
- fabricated issue records
- fabricated services
- fabricated Flex credits
- generated random values

QA fixture data is allowed ONLY inside the existing explicit QA/debug fixture system and must never leak into release repositories.

Release builds must not silently use demo repositories.

---

# 16. FLEX

Preserve the existing Flex domain direction.

State machine:

REQUESTED
→ ACCEPTED
→ CHECKED_IN
→ CHECKED_OUT
→ CREDITED

Terminal alternatives:

DECLINED
CANCELLED

Request alone must never create a credit.

Settlement must be idempotent and auditable.

Do not invent Flex UI fields if the backend contract does not provide them.

---

# 17. ADVANCED EVENT CARD VARIETY

Use the same visual system to produce meaningful variants for:

- expired member
- frozen member
- overdue payment
- failed payment
- new member
- check-in
- workout
- PT
- missed PT
- service
- machine fault
- machine maintenance
- cleaning
- equipment installation
- equipment repair
- facility issue
- complaint
- issue resolution
- utility/expense operational event
- membership renewal
- trial
- walk-in
- Flex visit

Each variant should answer:

WHAT → WHEN → STATE → EVIDENCE → ACTION

---

# 18. QA / VISUAL ACCEPTANCE

Do not declare success because Gradle compiles.

Required:

### Build
- exact SHA pulled
- unit tests
- debug APK
- lint if configured

### Device
Run on Xiaomi Redmi Note 11.

### Screenshots
Capture exact current SHA for:

1. Home / Check-in
2. Home / Workout
3. Attendance
4. Payment clear
5. Payment overdue
6. PT active
7. Workout
8. Nutrition
9. Services
10. History
11. Insight
12. expired
13. frozen
14. machine fault
15. maintenance/cleaning
16. Flex when real capability exists
17. each enabled theme personality

Compare screenshots against the supplied reference board.

Check:

- geometry
- alignment
- typography
- card proportions
- rail symmetry
- icon scale
- portrait crop
- CTA placement
- semantic colors
- clipping
- overflow
- touch targets
- animation
- empty-state quality
- real-data integrity

---

# 19. ANTIGRAVITY MODEL / SKILL SELECTION

Do NOT blindly use the default model.

Before implementation, inspect the available Antigravity model/provider/configuration options.

Choose the **strongest available coding/reasoning model with the highest practical context and Android/Compose implementation capability** for this task.

Do not downgrade to a fast/flash model when a stronger coding/reasoning model is available.

Use specialist capabilities/tools where available:

- Android/Jetpack Compose implementation
- repository/code search
- visual/UI inspection
- Gradle build/debug
- physical-device execution
- screenshot capture
- Git/GitHub operations
- automated test execution

Use visual reasoning for screenshot comparison, not only text/code inspection.

If Antigravity exposes separate model choices for architecture, coding, visual QA, and device debugging:

- strongest reasoning model → architecture + complex UI decisions
- strongest coding model → implementation/refactor
- device-capable Android agent → build/install/runtime debugging
- visual-capable agent → screenshot comparison
- strongest available model → final integration review

The exact selected model/provider/configuration MUST be written into the final ACK.

Never claim a model name from memory.

---

# 20. GIT CONTRACT

Before work:

git fetch origin
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD

After work:

git status --short
git diff --stat
git diff --name-only
git rev-parse HEAD

Do not confuse commit count with changed-file count.

Final report MUST include:

MODEL
PROVIDER / CONFIGURATION
PULLED_HEAD
FILES_FOUND
FILES_CHANGED
IMPLEMENTATION_SUMMARY
UNIT_TEST
BUILD
LINT
DEVICE
SCREENSHOTS
FINAL_SHA
BLOCKERS
OPEN_QUESTIONS

No "done" without evidence.

---

# 21. NON-NEGOTIABLE DESIGN RESULT

The final app should feel like:

**BAD GYM — one member, one intelligent card, many visual personalities, one consistent product language.**

The eight reference styles are the visual inspiration.

The existing repository/domain model is the data truth.

The final implementation must be:

- production Android/Compose code
- reusable
- theme-driven
- event-driven
- data-driven
- responsive
- animated
- accessible
- visually disciplined
- free of fake production data
- verified on the physical device

Do not create another prototype.

Do not create another dashboard.

Redesign the existing Member Intelligence experience deeply and integrate it into the existing product.
