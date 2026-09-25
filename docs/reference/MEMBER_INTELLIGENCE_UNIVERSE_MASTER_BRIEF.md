# Member Intelligence Universe — Master Execution Brief

## Objective

Complete the BAD GYM Member Intelligence card universe so the dashboard can render the full supported operational event/state catalog instead of only the currently implemented subset.

This is a PRODUCTION IMPLEMENTATION BRIEF, not a request to redesign Member Intelligence from scratch.

Primary product principle:

> Member Intelligence is a decision surface, not a member profile.
> Home = intelligence. Other menus = evidence.

Every card must answer, in order:
1. WHAT HAPPENED / CURRENT STATE?
2. WHO IS THIS MEMBER?
3. WHY DOES IT MATTER RIGHT NOW?
4. WHAT SHOULD THE OWNER/STAFF DO NEXT?

## Current state

The repository already has:
- EventCardCatalog / EventCardRouter architecture.
- AdvancedEventMemberCard.
- CompactMemberCarousel.
- MemberIntelligenceEngine.
- Contextual CTA architecture.
- Event, Signal and CTA separation.
- 8 visual themes.
- Deterministic routing tests.
- Walk-in → Trial lifecycle support.
- Multiple event-specific fact renderers.
- Stage 7.x implementation and visual QA.

Do NOT throw this away or create a second parallel card architecture.

The missing work is breadth + fixture completeness + truthful data wiring + runtime proof.

## Non-negotiable rules

### 1. No fake intelligence

Never manufacture:
- payment amounts
- overdue days
- trainer names
- session times
- workout duration
- service state
- machine/asset state
- ban/freeze reasons
- trial dates
- attendance history

If data is absent, render a truthful empty/unknown state or omit the visualization.

### 2. Event ≠ Signal ≠ CTA

Event = what happened.
Signal = what it means / what needs attention.
CTA = what the user can do.

Do not merge these concepts.

### 3. Do not create dozens of unrelated composables

Use reusable card archetypes and event specifications. Event-specific visual identity is required, but architecture must remain maintainable.

### 4. Do not turn the card into a mini-dashboard

Above the fold target:
- event/state
- identity
- 3–5 decision facts
- one primary CTA

Optional secondary evidence must remain visually subordinate.

### 5. Semantic truth survives every theme

Theme changes only visual personality. They must never change:
- event meaning
- priority
- CTA
- business rules
- data
- accessibility semantics

### 6. Light-first visual system

Default experience should remain light/premium. Minimal Dark remains an optional theme, not the product default.

## Member Intelligence Universe

Implement/reconcile the following operational event/state variants. Prefer existing EventType/CardKind values and existing domain concepts. Add a new enum only when the repository genuinely has no existing semantic representation.

### Identity / lifecycle
1. New member
2. Member profile completed
3. Membership activated
4. Membership expiring
5. Membership expired
6. Membership renewed
7. Membership cancelled

### Attendance / access
8. Check-in
9. Check-out
10. Late check-in
11. Early/short visit
12. Attendance streak/milestone
13. Attendance drop / recovery-needed
14. Walk-in
15. Walk-in converted to trial
16. Walk-in trial expired
17. Trial active
18. Trial converted
19. Trial expired
20. Access restricted

### Payment / finance
21. Payment due
22. Payment overdue
23. Payment failed
24. Payment successful
25. Partial payment
26. Payment reminder
27. Refund / payment reversal
28. Outstanding balance resolved

### Plan / membership state
29. Plan upgrade
30. Plan downgrade
31. Freeze requested
32. Freeze active
33. Freeze ending / membership resuming
34. Freeze cancelled
35. Ban/access block active
36. Ban/access block lifted

### Trainer / coaching
37. PT session scheduled
38. PT session starting
39. PT session in progress
40. PT session completed
41. PT session missed
42. PT sessions exhausted
43. Trainer reassignment
44. Coaching follow-up required

### Workout / nutrition
45. Workout completed
46. Workout gap / inactivity
47. Workout milestone
48. Workout routine assigned/updated
49. Nutrition plan assigned/updated
50. Nutrition follow-up required

### Services / operations
51. Service activated
52. Service expired
53. No active service / service opportunity
54. Machine fault reported
55. Machine fault assigned/in progress
56. Machine fault resolved
57. Member complaint/issue raised
58. Member issue resolved
59. Facility/ops incident affecting member
60. Communication/follow-up required

### Intelligence / derived operational states
61. High-priority issue
62. Multiple unresolved signals
63. Member needs attention
64. Healthy/clean member state
65. Recovery opportunity
66. Retention risk signal
67. Re-engagement opportunity
68. Milestone/achievement
69. Data incomplete / verification needed
70. Event history / recent activity summary

These are CARD VARIANTS, not a mandate to create 70 new domain event enum values. Collapse semantically related states into existing EventType/CardKind where possible.


## CRITICAL VISUAL-QA REQUIREMENT — UNIQUE RAW CARD UNIVERSE

The immediate goal is visual UX verification, not merely backend taxonomy completeness.

The current dashboard already has roughly 8 visible card examples. That is NOT sufficient. Create a RAW, deterministic visual fixture gallery of at least 50 unique card variants; target 70–100 if the existing domain supports it without inventing semantics.

### One fixture = one unique operational state

Do NOT create duplicate cards such as Check-in / Member A, Check-in / Member B, Check-in / Member C. Those are duplicates and provide almost no additional UX value.

Instead create genuinely distinct states: Check-in, Late check-in, Check-out, Payment due, Payment overdue, Payment failed, Payment successful, Partial payment, Refund/reversal, Balance resolved, Membership expiring, Membership expired, Membership renewed, Freeze active, Freeze ending, Ban active, Ban lifted, Walk-in, Trial active, Trial expired, Trial converted, PT scheduled, PT starting, PT in progress, PT completed, PT missed, Trainer reassignment, Workout completed, Workout gap, Workout milestone, Service active, Service expired, No service, Machine fault reported, Machine fault assigned, Machine fault resolved, Member issue raised, Member issue resolved, Facility incident, Communication follow-up, High-priority signal, Multiple unresolved signals, Retention risk, Re-engagement opportunity, Recovery opportunity, Healthy/clean state, Data incomplete, History summary, plus every other supported unique state.

### Purpose of the raw gallery

The gallery exists to inspect card composition, spacing, typography, hierarchy, semantic colors, contrast, icon treatment, CTA placement, long-text behavior, amount/date formatting, empty states, warning/danger/success states, theme behavior, clipping/overflow, carousel depth/focus, and whether each operational state is visually distinguishable.

The gallery MUST use the same production card renderer/components. Do not create a fake screenshot-only UI.

### Raw QA navigation

Provide a deterministic way to open the entire fixture universe sequentially. It should support previous/next fixture, fixture index such as 17 / 70, fixture name/state, and direct access from an existing Member Intelligence debug/demo entry point where possible. Do not render 70–100 cards simultaneously if that harms performance. The requirement is 70–100 unique reachable fixtures, not simultaneous rendering.

### No duplicate-state padding

Do not inflate fixture count by cloning one event for different member names. A fixture is countable only when a meaningful operational dimension changes: event/state, lifecycle stage, payment condition, access condition, trainer session state, workout state, service state, issue state, intelligence state, or a materially different temporal state.

The goal is a card UX laboratory, not a fake member database.

## Visual archetype rules

Do not make all cards look identical with only title changes.

### Payment
Visual emphasis:
- amount
- due/overdue status
- age of debt
- last payment
- one financial CTA

### Attendance
Visual emphasis:
- current check-in/check-out
- visit time
- attendance pulse only when evidence exists
- recent attendance context
- one attendance CTA

### Walk-in / Trial
Visual emphasis:
- lifecycle stage
- walk-in time
- trial start/end
- conversion outcome
- next action

Preserve:
WALK-IN → TRIAL → CONVERTED / EXPIRED

### Freeze / Ban
Visual emphasis:
- access state
- effective dates
- reason if real
- actor/source if real
- restore/lift CTA

Never imply a ban when there is no restriction record.

### Trainer
Visual emphasis:
- coach
- session state
- time
- sessions remaining
- coaching CTA

### Workout / Nutrition
Visual emphasis:
- last evidence-backed activity
- routine/plan
- gap/milestone when derivable
- next action

### Service
Visual emphasis:
- active service
- expiry/state
- service opportunity
- service CTA

### Machine / Facility / Issue
Visual emphasis:
- asset/issue
- state
- reported time
- assignee if known
- resolution action

### Clean / healthy member
Do not fabricate a positive story. If the repository has sufficient evidence, show a concise healthy state with supporting facts.

## Fixture universe

Create a deterministic fixture catalog that covers every supported card variant above.

Fixtures must:
- be synthetic/demo-only
- use the exact same rendering path as production
- contain explicit NOW / PAST / FUTURE examples where applicable
- include missing-data cases
- include boundary cases
- include long names
- include long event labels
- include zero values
- include large monetary values
- include no-history states
- include active/inactive states
- include CTA unavailable cases
- never leak into production data

The fixture system should make it possible to navigate to every supported card in a repeatable QA session.

## Acceptance matrix

For every supported variant verify:

1. correct EventType/CardKind
2. correct event title
3. correct story label
4. correct identity
5. correct facts
6. correct signal priority
7. correct CTA
8. CTA is executable or correctly disabled
9. no fake data
10. no semantic mismatch
11. light theme
12. at least one alternate theme
13. accessibility labels/content descriptions
14. long text behavior
15. clipping/overflow behavior
16. compact carousel behavior
17. selected/focused state
18. reduced-motion behavior
19. NOW/PAST/FUTURE behavior
20. routing unit test
21. card catalog test
22. Compose/UI test where appropriate
23. debug build
24. unit tests
25. physical-device runtime check

## Data layer gate

Inspect the current repository wiring before claiming production readiness.

The ViewModel currently has transitional repository defaults in the known architecture. Verify whether production Supabase-backed repositories are actually wired.

If production data is not wired:
- do NOT fake that it is.
- keep fixture/demo mode isolated.
- document the exact remaining integration boundary.
- do not block visual QA on real backend data, but do block the final production-readiness claim.

## Performance rules

Optimize for fast execution:
- inspect existing architecture first
- reuse existing components/tokens
- avoid unnecessary refactors
- avoid dependency changes unless essential
- avoid rebuilding working event infrastructure
- prefer data-driven specs/catalogs over repetitive UI code
- keep Compose recomposition controlled
- use stable models where appropriate
- do not introduce expensive animations into every card
- keep LazyRow/card rendering bounded

## Definition of done

Member Intelligence is considered complete for this scope only when:
- every supported event/state variant has a reachable card representation
- every variant has deterministic fixture coverage
- routing/catalog tests pass
- no fake data is introduced
- all supported cards render through the same production rendering path
- compact carousel remains usable
- visual hierarchy remains decision-first
- build and tests pass
- physical device runtime is checked
- exact commit SHA and changed-file inventory are recorded
- production data integration status is explicitly documented

Do not start unrelated Stage 8 work until these gates are satisfied.
