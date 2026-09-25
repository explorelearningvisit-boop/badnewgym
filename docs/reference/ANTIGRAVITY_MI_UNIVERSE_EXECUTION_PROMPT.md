# Google Antigravity Execution Prompt — Member Intelligence Universe

## ROLE

You are the primary implementation/build/test agent for BAD GYM.

Use your strongest available coding capability in the requested **3.7+ / Medium** configuration.

Your job is not to brainstorm. Your job is to inspect the repository, implement the approved Member Intelligence universe, build it, test it, run it on a physical device where available, and push a verified result.

The source-of-truth brief is:

docs/reference/MEMBER_INTELLIGENCE_UNIVERSE_MASTER_BRIEF.md

Repository:
explorelearningvisit-boop/badnewgym

Branch:
member-intelligence-v3

## EXECUTION MODE

Work autonomously and efficiently.

Do not stop after creating a plan.

Do not ask for confirmation for ordinary implementation decisions that are already covered by this prompt.

Do not redesign unrelated screens.

Do not create fake backend data.

Do not replace the current Member Intelligence architecture with a new architecture.

First inspect. Then implement. Then test. Then run. Then document. Then commit. Then push.

---

# PHASE 0 — SYNC AND SAFETY

Run:

git fetch origin
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD

Read:

- AGENTS.md
- STATUS.md
- HANDOFF_STATUS.md
- CURRENT_TASK.md if present
- docs/reference/CHATGPT_ANTIGRAVITY_SYNC.md
- docs/reference/MEMBER_INTELLIGENCE_UNIVERSE_MASTER_BRIEF.md
- existing Member Intelligence specifications
- existing Member Intelligence implementation files

Before modifying code, establish the exact current HEAD.

Verify that the repository is clean enough to work safely.

Do NOT:
- force push
- reset --hard
- discard unrelated user changes
- rewrite history
- delete working functionality

If local uncommitted changes exist, inspect and preserve them.

---

# PHASE 1 — REPOSITORY DEEP INSPECTION

Inspect all relevant files under:

app/src/main/java/com/example/badnewgym/feature/memberintelligence/

Especially inspect:

- MemberIntelligenceScreen
- MemberIntelligenceViewModel
- MemberIntelligenceEngine
- ContextualCtaEngine
- MenuAvailabilityResolver
- MemberIntelligenceRepository
- TemporalIntelligenceRepository
- AdvancedEventMemberCard
- CompactMemberCarousel
- EventCardCatalog
- EventCardRouter
- EventType / CardKind definitions
- ThemeResolver / ThemeId
- all existing event fact renderers
- all tests related to Member Intelligence
- fixture/demo infrastructure, if already present

Also inspect domain/data models used by:
- membership
- attendance
- payment
- trainer/PT
- workout
- nutrition
- services
- facility/issue
- communication
- restrictions/ban/freeze
- member history

Search globally before adding a new enum, data class, repository, or composable.

The repository may already contain a concept under a different name.

REUSE before creating.

---

# PHASE 2 — ESTABLISH THE CURRENT EVENT UNIVERSE

Create a clear mapping:

Business state/event
→ existing domain event/state
→ EventType
→ EventCardCatalog/CardKind
→ renderer
→ signal
→ CTA
→ fixture
→ test

Do not blindly add 70 enum values.

The master brief lists approximately 70 operational card variants to ensure coverage. These are NOT automatically 70 new EventType values.

Collapse related states into existing semantic structures where appropriate.

Examples:

Payment:
- due
- overdue
- failed
- success
- partial
- reminder
- refund
- resolved

may share a Payment event family with different CardKind/state/spec values.

Trainer:
- scheduled
- starting
- in progress
- completed
- missed
- exhausted
- reassigned

may share a Trainer event family.

Machine:
- reported
- assigned
- in progress
- resolved

may share an Issue/Facility event family.

Use the existing repository architecture as the primary constraint.

---

# PHASE 3 — DO NOT BREAK THE CORE MODEL

Preserve these invariants:

Event:
what happened / what state exists.

Signal:
why it matters.

CTA:
what the operator should do.

Never combine them.

The event catalog should describe event presentation.

The intelligence engine should calculate signals/priorities.

The CTA engine should determine action.

Compose should render.

Business logic must remain outside Compose.

---

# PHASE 4 — BUILD THE COMPLETE CARD CATALOG

For every supported variant in the master brief, ensure there is a deterministic card representation.

Minimum families:

1. Identity/lifecycle
2. Attendance/access
3. Walk-in/trial
4. Payment/finance
5. Plan/membership state
6. Trainer/coaching
7. Workout/nutrition
8. Services
9. Machine/facility/issue
10. Communication/follow-up
11. Derived intelligence states
12. Clean/healthy state
13. Incomplete-data/verification state
14. History/recent activity

For each family, define:

- semantic event/state
- visual archetype
- priority behavior
- supporting facts
- CTA
- fallback behavior
- fixture
- test

Do not create one giant if/else block.

Prefer a data-driven EventCardSpec/EventCardCatalog pattern.

---

# PHASE 5 — CARD DESIGN RULE

Every card must be decision-first.

The visual hierarchy should answer:

WHAT HAPPENED?
WHO?
WHY DOES IT MATTER?
WHAT DO I DO?

Target above the fold:

- event/state
- member identity
- 3–5 decision facts
- one primary CTA

Everything else is secondary.

Do not add:
- giant dashboards inside cards
- 10+ metric grids
- redundant labels
- duplicate identity blocks
- unnecessary charts
- decorative data

If a chart has no trustworthy evidence, do not render it.

If a field is missing, do not invent it.

---

# PHASE 6 — EVENT-SPECIFIC VISUAL LANGUAGE

Do not make 50 cards visually identical.

They can share:
- shell
- spacing tokens
- typography system
- theme engine
- CTA component
- identity component
- fact row primitives

But event families need distinct visual emphasis.

PAYMENT:
- amount
- due/overdue state
- overdue age
- last paid
- financial CTA

ATTENDANCE:
- check-in/out
- visit time
- recent attendance evidence
- attendance CTA

WALK-IN/TRIAL:
- lifecycle stage
- walk-in time
- trial dates
- conversion/expiry
- next action

FREEZE/BAN:
- access state
- dates
- reason only if real
- restore/lift action

TRAINER:
- coach
- session state
- time
- remaining sessions
- coaching action

WORKOUT:
- last evidence-backed workout
- routine
- gap/milestone if derivable
- workout action

SERVICE:
- active service
- expiry/state
- service opportunity

MACHINE/ISSUE:
- asset/issue
- current state
- report time
- assignee if known
- resolution action

CLEAN MEMBER:
- only evidence-backed positive state
- concise
- no fake praise
- no fabricated streak

---

# PHASE 7 — FIXTURE UNIVERSE

This is a major deliverable.

Create or extend a deterministic Member Intelligence fixture catalog.

Every supported card variant must be reachable.

Fixtures must include:

NOW:
- active events
- current payment due
- current check-in
- current PT
- current issue

PAST:
- completed payment
- completed workout
- resolved issue
- completed PT
- expired trial
- previous walk-in

FUTURE:
- scheduled PT
- upcoming expiry
- upcoming trial expiry
- planned service
- future membership event

EDGE CASES:
- no history
- missing optional fields
- zero amount
- large amount
- long member name
- long event title
- no trainer
- no service
- no attendance history
- no CTA
- multiple signals
- P0 signal
- P1 signal
- P2 signal
- conflicting candidate events
- event with no meaningful visual evidence

The fixture mode must use the SAME production rendering path.

Do not build a separate fake UI just for screenshots.

---

# PHASE 8 — THE ~50+ CARD REQUIREMENT

The dashboard currently shows only a subset of members/cards while the product concept contains a much broader event universe.

Your task is to make the dashboard/fixture navigation capable of rendering the entire supported catalog.

Do NOT literally force 50 members onto one screen.

The requirement means:
- all supported card states must exist
- all must be reachable
- the carousel must remain performant
- fixture navigation must make full QA practical

The default dashboard may continue showing a sensible operational subset.

A QA/fixture gallery should expose the full universe.

---

# PHASE 9 — THEME QA

All supported cards must survive the 8 existing themes.

Verify:
- Natural Fresh
- Futuristic Neon
- Minimal Dark
- Glassmorphism
- Premium 3D
- Vibrant Gradient
- Gym Beast Mode
- Purple Royal

Do not create new themes.

Do not change semantic colors so much that:
- danger looks success
- warning looks success
- success looks danger
- disabled looks active

The default visual experience remains light-first.

---

# PHASE 10 — COMPACT CAROUSEL QA

Preserve:

- LazyRow
- snap behavior
- focused card emphasis
- adjacent-card depth
- reduced-motion behavior

Verify:
- first card
- middle cards
- last card
- long content
- fast swipe
- slow swipe
- selection state
- CTA visibility
- no clipping
- no unexpected horizontal overflow
- no giant vertical card expansion

If vertical scrolling is currently necessary inside AdvancedEventMemberCard, keep it bounded.

Do not allow event cards to become mini-pages.

---

# PHASE 11 — ROUTING AND PRIORITY

Verify deterministic routing.

Examples already expected:

overdue payment → PAYMENT_OVERDUE
expired membership → MEMBERSHIP_EXPIRED
scheduled PT → TRAINER_SESSION_SCHEDULED
clean check-in → CHECK_IN

Extend routing for every newly covered family.

Priority remains deterministic.

Do not use random ordering.

Do not make visual order silently change business priority.

---

# PHASE 12 — TESTS

Add/update tests for:

A. EventCardCatalog
B. EventCardRouter
C. Event → CardKind mapping
D. Walk-in → Trial story
E. Payment states
F. Trainer states
G. Workout states
H. Service states
I. Facility/Issue states
J. Freeze/Ban states
K. Membership lifecycle
L. NOW/PAST/FUTURE
M. missing-data fallbacks
N. CTA mapping
O. signal priority
P. fixture completeness

Add Compose/UI tests where the current test architecture supports them.

At minimum, assert that every supported fixture resolves to:
- a valid card
- a valid event/state
- an appropriate CTA or explicit no-CTA state
- no crash

---

# PHASE 13 — DATA TRUTH / SUPABASE GATE

Inspect the real repository wiring.

Determine whether MemberIntelligenceViewModel still defaults to stub repositories.

If production repositories are wired:
- verify the data path
- verify null/missing data
- verify event conversion
- verify real data does not fabricate fields

If production repositories are NOT wired:
- do not pretend they are
- do not replace them with fake production data
- keep deterministic fixtures isolated
- document the exact integration boundary

Visual completeness and production data readiness are separate gates.

---

# PHASE 14 — PERFORMANCE

Optimize implementation time and runtime.

Prefer:
- existing tokens
- existing models
- existing event catalog
- existing theme resolver
- existing CTA system
- existing card primitives

Avoid:
- new dependencies
- broad refactors
- duplicate repositories
- duplicate navigation
- duplicate theme systems
- heavy animations
- unnecessary image loading
- large recomposition surfaces

Use stable Compose state/model patterns where appropriate.

---

# PHASE 15 — BUILD

Run the narrowest useful checks first.

Then run the full relevant verification.

At minimum:

./gradlew testDebugUnitTest
./gradlew assembleDebug

If practical:
./gradlew lintDebug

If the repository supports connected-device execution:

./gradlew installDebug

Then launch the relevant Member Intelligence screen.

Do not claim success unless the command actually passes.

---

# PHASE 16 — PHYSICAL DEVICE QA

If a connected device is available, verify:

1. dashboard opens
2. carousel renders
3. card selection works
4. event cards render
5. CTA is visible
6. CTA does not crash
7. long text does not clip
8. theme switching works
9. reduced motion does not break layout
10. no runtime exception
11. no obvious jank
12. back navigation works

Capture screenshots for representative families:

- Walk-in
- Trial Active
- Trial Expired
- Trial Converted
- Check-in
- Overdue Payment
- Payment Success
- Freeze Active
- Ban Active
- PT Scheduled
- PT In Progress
- PT Completed/Missed
- Workout
- Service Active
- No Service
- Machine Fault
- Machine Resolved
- Membership Expired
- Clean/Healthy state
- Incomplete-data state

Do not need a screenshot for every individual fixture if the same renderer is proven through deterministic fixture tests, but every supported family must be reachable.

---

# PHASE 17 — DOCUMENTATION

Update:

STATUS.md

and, if present:

CURRENT_TASK.md
HANDOFF_STATUS.md

Create/update:

docs/reference/ANTIGRAVITY_SYNC_ACK.md

The ACK must contain:

- pulled HEAD
- final HEAD
- exact files changed
- implementation summary
- fixture count
- supported card-family count
- test commands
- test results
- build result
- lint result if run
- physical device result
- screenshots/QA result
- production repository wiring status
- remaining blockers
- exact pushed commit SHA

Never claim a blocker is solved if it was not actually solved.

---

# PHASE 18 — GIT DISCIPLINE

Before commit:

git status --short
git diff --stat
git diff --check

Review the changed files.

Do not commit:
- secrets
- generated junk
- unrelated changes
- local machine artifacts

Commit with a clear message, for example:

feat(member-intelligence): complete event card universe and QA fixtures

Push:

git push origin member-intelligence-v3

Then verify:

git rev-parse HEAD

The final report MUST state:
FINAL_PUSH_SHA=<exact SHA>

and:

CHANGED_FILES:
- exact/path/one
- exact/path/two
- ...

Commit count is NOT sufficient evidence.

---

# FINAL SUCCESS CRITERIA

Do not say "complete" merely because the app compiles.

The implementation is complete for this task only if:

[ ] Existing architecture preserved
[ ] Full supported event/card universe mapped
[ ] No duplicate architecture created
[ ] Every supported variant reachable
[ ] Deterministic fixture catalog exists
[ ] NOW/PAST/FUTURE covered
[ ] Missing-data cases covered
[ ] Event/Signal/CTA separation preserved
[ ] Routing tests pass
[ ] Catalog tests pass
[ ] Priority tests pass
[ ] CTA tests pass
[ ] Build passes
[ ] Unit tests pass
[ ] Lint checked where practical
[ ] Physical device checked where available
[ ] Compact carousel remains usable
[ ] 8 themes do not break semantics
[ ] No fake data
[ ] Production data wiring status documented
[ ] STATUS updated
[ ] ANTIGRAVITY_SYNC_ACK updated
[ ] Exact SHA recorded
[ ] Exact changed-file inventory recorded
[ ] Pushed to member-intelligence-v3

## IMPORTANT STOP CONDITION

If you discover that completing the entire universe requires a major unrelated backend migration, do not silently expand scope.

Implement the maximum safe Member Intelligence coverage using the existing architecture, document the exact missing integration boundary, and finish with a precise blocker.

Do not start unrelated Stage 8 work.

## PRODUCT NORTH STAR

A gym owner should be able to look at a card for two seconds and understand:

"Who is this?"
"What just happened?"
"Why should I care?"
"What should I do now?"

If the card cannot answer those four questions quickly, simplify it.

Build breadth through data-driven event specifications, not through UI duplication.

Build intelligence through truthful signals, not decorative metrics.

Build trust through evidence, not invented data.
