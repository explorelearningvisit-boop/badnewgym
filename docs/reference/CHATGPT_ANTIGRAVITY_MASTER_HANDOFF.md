# BAD GYM — MASTER CHATGPT ↔ ANTIGRAVITY EXECUTION HANDOFF

## MASTER STATUS
**ACTIVE — THIS IS THE CURRENT CROSS-AGENT EXECUTION CONTRACT**

Date: 2026-09-26
Repository: `explorelearningvisit-boop/badnewgym`
Branch: `member-intelligence-v3`

This file consolidates the decisions, corrections, non-negotiables, implementation scope, communication protocol, and verification gates produced by the recent ChatGPT ↔ user discussion.

**GitHub is the shared source of truth.**
ChatGPT writes intent/specification to GitHub.
Antigravity pulls, inspects, implements, verifies, and writes evidence back to GitHub.
ChatGPT then reviews that evidence and the exact Git state.

---

# 1. CURRENT PRODUCT OBJECTIVE

Build a production-quality **Member Intelligence** card for BAD GYM that lets a gym operator understand a member in seconds:

1. WHO is this member?
2. WHAT happened / what state exists?
3. WHY does it matter?
4. WHAT should the operator do now?

The product must support a broad and extensible menu universe. **Do not reduce the product to three representative menus.**

The system may grow beyond 15 menus when a distinct operational workflow requires it.

---

# 2. HARD UX DECISIONS — LOCKED

## 2.1 Persistent member context

### HOME
Use the larger identity treatment:
- member photo when actual photo exists;
- name;
- member identifier when available;
- tier/plan;
- status;
- current event/time;
- primary attention state.

### NON-HOME
Always retain a compact identity/context header:
- small photo when actual photo exists;
- smaller member name;
- identifier;
- plan/tier;
- current event/state;
- attention indicator.

**Menu switching must never make the operator forget which member is open.**

---

## 2.2 Right-side rail is NOT the final UX

The previous dual-side vertical rail squeezed the center content on compact cards.

**Do not preserve the right-side vertical rail as the final design.**

Move the contextual/right-side menu set to the **bottom** of the canonical card, or to a bottom contextual/expandable navigation pattern.

The exact bottom variant remains a design-selection item; see:
`docs/reference/MEMBER_INTELLIGENCE_MENU_CONCEPT_BOARD.md`

Do not simply move 15 buttons into one overcrowded horizontal row.

---

## 2.3 Full menu universe, not three menus

The implementation must reason over the complete candidate universe:

### Core
- Home
- Attendance
- Plan / Membership
- Payment

### Training / Fitness
- Trainer
- Workout
- Progress
- Goals

### Lifestyle / Add-ons
- Nutrition
- Supplements
- Services

### Intelligence / Operations
- Insight
- Issues / Resolution
- History
- Communication
- Offers
- More

### Contextual
- Walk-in / Lead
- Trial Journey
- Access
- Facility / Operations
- Documents

This is a **candidate universe**, not a command to show every item simultaneously.

Actual visibility should be capability/data driven.

---

# 3. MENU CONCEPT SELECTION GATE

The user explicitly wants to review menu varieties/concepts before the final menu IA is locked.

Available concepts are documented in:
`docs/reference/MEMBER_INTELLIGENCE_MENU_CONCEPT_BOARD.md`

Concepts currently under review:
- Classic Vertical
- Intelligence-First Vertical
- Adaptive Priority
- Bottom Contextual
- Bottom Primary + Expandable Context
- Hybrid Bottom Navigation

### REQUIRED
Before hard-locking the final IA:
1. prepare a compact visual/fixture comparison of the viable concepts;
2. let the user select which concept(s)/menus to retain;
3. record the decision in the menu concept board;
4. then implement the selected navigation structure.

Do not invent that the user has already selected a concept.

---

# 4. ADAPTIVE MENU OPENING — LOCKED

When a member has a meaningful actionable problem, the relevant menu should auto-open.

Priority should be deterministic, evidence-based, and capability-aware.

Examples:
- payment due/overdue/failed → Payment
- membership expired/cancelled/expiring → Plan
- trainer session state → Trainer
- workout state → Workout
- service issue → Services/Issues as appropriate
- facility/member issue → Issues/Insight as appropriate
- otherwise → Home

The existing `MemberIntelligenceEngine`, `MenuAvailabilityResolver`, and `ContextualCtaEngine` architecture must be reused rather than duplicated.

### Cross-menu consistency
If Payment is auto-opened because of an overdue payment:
- Home must show the same underlying priority.
- Payment must provide the supporting evidence and relevant CTA.
- Do not create two contradictory stories from the same member state.

---

# 5. MENU CONTENT RULE

Every menu must have its own **information personality**.

A menu label plus a generic empty card is not an implementation.

Minimum intended personalities:

- Attendance → consistency, recent pattern, timing, missed/recovery opportunity
- Plan → entitlement, expiry, access, freeze, renewal
- Payment → amount/state, due age, last payment, timeline, collection action
- Trainer → coach, session state, next/missed, remaining sessions, action
- Workout → routine, recent evidence, frequency/gap, milestone
- Progress → measurements/strength/PR only when real
- Goals → goal, progress, target, deadline, action
- Nutrition → plan/macros/adherence only when connected
- Supplements → actual stack/purchase/renewal data
- Services → active services, usage, expiry, entitlement/action
- History → evidence timeline and filters
- Insight → signals, evidence, priority, decision, CTA
- Issues → problem, state, owner, reported time, resolution
- Communication → actual contact/message/feedback state
- Offers → actual promotion, eligibility, expiry
- More → grouped low-frequency operations
- Walk-in/Trial/Access/Facility/Documents → contextual operational stories when applicable

---

# 6. SPACE / SCROLL LAW

The canonical card is compact.

Required:
- maximize useful above-fold information;
- minimize vertical scrolling;
- minimize horizontal scrolling;
- avoid giant empty areas;
- use responsive micro-layouts where the actual constraints allow;
- collapse gracefully on narrow devices;
- do not turn a card into a mini-page;
- do not allow rails to consume excessive content width.

Charts are allowed only when they answer a decision question and actual data exists.

No chart is better than a fabricated or irrelevant chart.

---

# 7. DATA TRUTH

**NO FAKE DATA.**

Never invent:
- photos
- names
- payment values
- dates
- trainer data
- workout data
- nutrition metrics
- supplement history
- service usage
- promotions
- attendance trends
- forecasts
- analytics

If a field is unsupported:
- hide it, or
- show an explicit unavailable/connection state.

If production repositories are not wired:
- do not pretend they are;
- keep fixtures/demo data isolated;
- document the integration boundary.

---

# 8. ARCHITECTURE — PRESERVE

Maintain the separation:

**EVENT → SIGNAL → CTA → UI**

- Event = what happened / what state exists
- Signal = why it matters / attention priority
- CTA = what action is available
- UI = presentation

Do not move business logic into Compose.
Do not create a duplicate Member Intelligence architecture.
Do not create a second detail/dashboard shell.

Reuse:
- MemberIntelligenceViewModel
- MemberIntelligenceEngine
- MenuAvailabilityResolver
- ContextualCtaEngine
- MemberIntelligenceRepository
- TemporalIntelligenceRepository
- EventCardCatalog
- AdvancedEventMemberCard
- CompactMemberCarousel
- existing theme/token system
- existing fixture infrastructure where possible

---

# 9. CURRENT KNOWN IMPLEMENTATION BOUNDARY

ChatGPT has already added the persistent member context and initial-menu routing.

Relevant files:
- `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/PersistentMemberContextHeader.kt`
- `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCard.kt`
- `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceViewModel.kt`

The existing menu panels still require substantial UX work.

Do not treat the current three screenshot examples (Supplements, Nutrition, Services) as the whole task. They are evidence of the larger menu-density problem.

---

# 10. WHAT MUST BE FIXED

Fix all of the following when encountered:

- vertical letter stacking;
- clipped transaction/training content;
- content hidden behind rails;
- huge empty Plan/Supplements/Nutrition/Services canvases;
- weak information density;
- generic repeated panels;
- duplicate identity/header;
- unnecessary scrolling;
- excessive rail width;
- unclear active-menu state;
- menu content that does not help a daily gym operator decide;
- inconsistent hierarchy;
- irrelevant charts;
- fake or placeholder analytics;
- menus visible without underlying capability when they should be contextual;
- menus missing when a distinct supported operational workflow needs them.

---

# 11. VISUAL DIRECTION

Default:
- light-first;
- premium;
- futuristic but understandable;
- soft/neumorphic/glass/material personality where appropriate;
- semantic accents;
- rounded components;
- clear hierarchy;
- no black/dark default workspace.

Dark theme remains a supported theme only where the existing theme system requires it.

Do not sacrifice readability for visual effects.

---

# 12. MODEL / AGENT COMMUNICATION CONTRACT

## ChatGPT
Current model: **GPT-5.6 Luna**

Primary role:
- product/UX reasoning;
- architecture;
- cross-file specification;
- acceptance criteria;
- deterministic routing/priority logic;
- code review;
- visual critique;
- handoff definition.

Limitations that must be respected:
- ChatGPT must not claim physical-device verification unless it actually has evidence.
- ChatGPT must not invent Antigravity's model/provider/configuration.

## Antigravity
Exact model/provider/configuration: **UNKNOWN until Antigravity reports it.**

Antigravity MUST report:
- exact model;
- provider;
- configuration/effort if visible;
- what the model was used for;
- known limitations;
- model changes during the execution cycle.

## Cursor / other coding agents
The user has reported that a Cursor/Grok coding model has performed large implementation work effectively.

This is a user observation, not a benchmark claim.

If another coding agent is used:
- report the exact model/configuration if known;
- report which files it changed;
- preserve the same GitHub audit protocol;
- do not attribute work to the wrong agent.

### Task-to-agent guidance
- Deep product/UX/architecture reasoning → ChatGPT
- Large code edits/refactors → strongest available coding agent
- Android/Compose build/debug/device → Antigravity or another agent with actual runtime access
- Visual QA → physical-device-capable agent
- Final reasoning/review → ChatGPT + Git evidence

No agent should claim work it did not perform.

---

# 13. MANDATORY TWO-WAY GITHUB LOOP

## ChatGPT → GitHub → Antigravity

ChatGPT must put:
- current objective;
- locked decisions;
- pending decisions;
- exact files;
- expected work;
- acceptance criteria;
- verification gates;
- model information;
- blockers/unknowns

into GitHub.

## Antigravity → GitHub → ChatGPT

Antigravity must put:
- exact pulled HEAD;
- model/provider/config;
- files found;
- files inspected;
- files added/modified/deleted;
- implementation summary;
- build/test/lint results;
- physical-device result;
- screenshots/evidence;
- deviations;
- blockers;
- final SHA

into:
`docs/reference/ANTIGRAVITY_SYNC_ACK.md`

## User visibility

Every meaningful cycle must be understandable as:

**FOUND → PLAN → CHANGED → VERIFIED → SHA → BLOCKERS**

The user must not need to inspect raw Git history to understand what happened.

---

# 14. ANTIGRAVITY PULL GATE

Before changing code:

```bash
git fetch origin
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD
```

Then inspect:
- this master handoff;
- `CURRENT_TASK.md`;
- `docs/reference/CHATGPT_ANTIGRAVITY_SYNC.md`;
- `docs/reference/MEMBER_INTELLIGENCE_MENU_CONCEPT_BOARD.md`;
- the UX V2 blueprint;
- existing Member Intelligence implementation.

Report:
```
MODEL
BRANCH
PULLED_HEAD
FILES_FOUND
EXPECTED_WORK
EXECUTION_PLAN
BLOCKERS
```

If an expected file is missing, stop and reconcile.

---

# 15. IMPLEMENTATION ORDER

### Phase 1 — Inspect
Do not code first.

### Phase 2 — Concept prototype
Prepare viable bottom/contextual menu concepts and menu variants for user review.

### Phase 3 — Navigation decision
Record the user's selected menu concept(s).

### Phase 4 — Canonical card shell
Persistent member context + selected menu content + bottom navigation.

### Phase 5 — Full menu universe
Implement retained menus and contextual menus based on actual capabilities/data.

### Phase 6 — Auto-open
Verify deterministic problem → menu routing.

### Phase 7 — Density / responsive QA
Fix narrow-card constraints, clipping, empty space, and scroll.

### Phase 8 — Tests
Routing, catalog, menu availability, CTA, fixture coverage.

### Phase 9 — Build
Debug build + unit tests + lint where configured.

### Phase 10 — Physical device
Install, run, interact, screenshot.

### Phase 11 — Git evidence
Commit, push, ACK.

### Phase 12 — ChatGPT review
ChatGPT reads exact SHA + changed files + evidence before declaring the cycle complete.

---

# 16. VERIFICATION MATRIX

At minimum inspect:

- Home
- Attendance
- Plan
- Payment
- Trainer
- Workout
- Supplements
- Nutrition
- Services
- History
- Insight
- More
- Offers

And where supported:
- Progress
- Goals
- Issues
- Communication
- Walk-in
- Trial
- Access
- Facility
- Documents

Required scenarios:
- overdue payment;
- payment failed;
- membership expired/expiring;
- scheduled/missed trainer session;
- workout state;
- active service;
- service issue;
- unresolved issue;
- walk-in/trial;
- freeze;
- ban/access restriction;
- clean/healthy member;
- incomplete-data member;
- long member name;
- large monetary value;
- missing optional fields.

---

# 17. BUILD / RUNTIME GATE

Run:

```bash
git fetch origin
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD

./gradlew assembleDebug
./gradlew testDebugUnitTest
```

Run lint if configured.

Install/run on the physical device if available.

Do not report "production-ready", "verified", or "complete" unless the relevant evidence exists.

---

# 18. SCREENSHOT QA

Capture enough screenshots to prove:
- member identity remains visible;
- bottom navigation is usable;
- selected menu is obvious;
- payment problem auto-opens correctly;
- Home and Payment agree;
- no vertical text;
- no clipping;
- no giant empty areas;
- CTA visible;
- no unnecessary scrolling;
- multiple menu personalities are genuinely distinct.

Screenshots must be committed or referenced in GitHub documentation where practical.

---

# 19. GIT DISCIPLINE

Never:
- force push;
- reset --hard to erase work;
- discard unrelated user changes;
- claim commit count equals changed-file count;
- fabricate QA evidence;
- silently modify unrelated architecture.

Before push:

```bash
git status --short
git diff --check
git diff --stat
git diff --name-only
```

Then commit and push.

Final report must contain:
- exact final SHA;
- exact changed-file inventory;
- build/test/lint/device evidence;
- screenshots;
- blockers;
- deviations.

---

# 20. DEFINITION OF DONE

This UX cycle is NOT done when only three menus look good.

It is done for the agreed scope only when:

- [ ] full candidate menu universe is mapped;
- [ ] user menu concept selection is recorded;
- [ ] right-side vertical rail is removed/replaced by the selected bottom/contextual pattern;
- [ ] persistent compact member context works;
- [ ] HOME remains the executive summary;
- [ ] retained menus have distinct information personalities;
- [ ] contextual menus are capability/data driven;
- [ ] auto-open routing is deterministic;
- [ ] Home and problem menu agree on priority;
- [ ] no fake data;
- [ ] no giant empty canvases;
- [ ] no vertical text;
- [ ] no clipping;
- [ ] scrolling is minimized;
- [ ] routing/menu/CTA tests pass;
- [ ] build passes;
- [ ] physical device QA is performed where available;
- [ ] exact changed-file inventory is recorded;
- [ ] Antigravity ACK is committed;
- [ ] final SHA is recorded;
- [ ] remaining blockers are explicit.

---

# 21. CURRENT DECISION STATE

### LOCKED
- One canonical Member Intelligence card.
- Persistent compact member context on non-HOME.
- HOME has larger identity treatment.
- Full menu universe must be supported; do not stop at three menus.
- Right-side vertical rail is not final and must move to bottom/contextual navigation.
- Menu visibility can be contextual/capability driven.
- Auto-open important problems deterministically.
- Home and problem menu share the same underlying priority.
- Minimize scrolling and empty space.
- Real data only.
- GitHub is the communication/source-of-truth channel.
- Every cycle reports model → files → work → verification → SHA.

### PENDING USER SELECTION
- exact bottom navigation concept;
- final retained menu set;
- final More grouping;
- any additional menu the user wants after reviewing concepts.

### UNKNOWN UNTIL ANTIGRAVITY REPORTS
- exact Antigravity model/provider/configuration;
- final runtime/build state after this handoff;
- final physical-device evidence.

---

# 22. NEXT ACTION — ANTIGRAVITY

**Do not start by implementing only three menus.**

Pull this branch, inspect this master handoff and the menu concept board, report the exact model/configuration and HEAD, then prepare the menu concept comparison and proceed through the implementation/verification loop.

The next ChatGPT review will use the GitHub ACK and exact SHA as the evidence source.
