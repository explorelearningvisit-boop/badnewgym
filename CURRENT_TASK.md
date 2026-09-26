# BAD GYM — Current Task

## ACTIVE ANTIGRAVITY EXECUTION PACKET
- Task: Stage 7.9 Complete Fixture Coverage + Production Readiness
- Specification: `docs/reference/AGY_STAGE_7_9_FIXTURE_COVERAGE_PRODUCTION_READINESS.md`
- Execution owner: Google Antigravity (visible GUI/runtime)
- Status: COMPLETED
- Verification: `./gradlew testDebugUnitTest assembleDebug` passed; physical device runtime verification on Xiaomi Redmi Note 11 (`zxdada69gunb7ls4`) with interactive QA Fixture Lab (69/69 EventType taxonomy coverage, 16 archetypes, 10 edge cases, temporal filters, and direct fixture injection).

STATUS: COMPLETED
TASK_ID: MI-STAGE-7.9-FIXTURE-COVERAGE-PRODUCTION-READINESS
BRANCH: member-intelligence-v3
BASE: 010ea3a5a25fd4ed459d6bbb2b28116c27427538

Mission: substantially improve Member Intelligence visual quality after Stage 7.4 without changing backend truth.

Implemented:
1. Light active-theme workspace.
2. Event-first title strip.
3. Member Pulse panel with recorded weekly attendance pattern when available.
4. Animated attendance goal ring and bar reveal.
5. Larger Home decision metrics.
6. Removed key fake Home fallback values.
7. Data-driven motion only.
8. Preserved on-demand temporal architecture and semantic states.

Do not start Stage 8.

Verification:
- GitHub Actions assembleDebug must pass.
- Runtime review on Xiaomi Redmi Note 11 remains required.


## Current — Stage 7.6 Compact Fusion Card

ChatGPT directly implemented the visual fusion layer on `member-intelligence-v3`.

Goal: merge the strongest visual ideas from the three concept boards into the existing compact member-intelligence card without increasing its footprint.

Implemented: `CompactIntelligenceStrip`, truthful metric fallbacks, two-line member-name support, and menu-specific signal routing.

Next verification: GitHub Actions `assembleDebug`, then physical Redmi runtime screenshot review. Do not claim device verification until evidence exists.


## Stage 7.7 — Event Card + Gym Capability Engine
- Specification: `docs/reference/STAGE_7_7_EVENT_CARD_AND_GYM_CAPABILITY_ENGINE.md`
- Scope: typed event taxonomy, reusable event card archetypes, priority routing, capability enable/disable, plan entitlements, custom services, expandable sections, NOW/PAST/FUTURE.
- Execution owner: Google Antigravity (visible GUI/runtime).
- ChatGPT role: product architecture/specification and post-push review; production source must not be edited simultaneously.
- Required evidence: representative event screenshots, menu routing, scrolling, CTA visibility, build/tests, exact pushed SHA.


## CURRENT — Stage 7.8 Event Card Variety + Story Implementation
- Specification: `docs/reference/STAGE_7_8_EVENT_CARD_VARIETY_MATRIX.md`
- Registry: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/EventCardCatalog.kt`
- Event taxonomy expanded in `MemberEvent.kt`.
- Goal: replace generic repeated event presentation with distinct semantic card variants while keeping reusable Compose archetypes.
- Required visible variants: Walk-in, Trial, Freeze, Ban, Check-in, Check-out, Membership, Payment, Trainer, Workout, Service, Facility/Ops, Issue/Resolution, Communication, Insight.
- Walk-in must preserve the journey: WALK-IN → TRIAL → CONVERTED or EXPIRED.
- Trial must be a separate card and show its lifecycle.
- Freeze must be a separate member-state card.
- Ban must be a separate access-restriction card and only appear for a real ban/restriction record.
- Media is evidence-backed only: no fake member/equipment/service photos; use stored references when present.
- Above fold target: event/state + identity + 3–5 decision facts + one visible primary CTA.
- NOW/PAST/FUTURE is contextual and compact.
- Do not create dozens of unrelated Composables; use reusable archetypes configured by `EventCardSpec`.
- Integrate `EventCardCatalog.forEvent()` and `EventCardRouter.route()` into the current Member Intelligence rendering/routing path.
- Ensure check-in state overrides remain deterministic: overdue payment → Payment; expired membership → Plan; scheduled PT → Trainer; otherwise Attendance.
- Verify all newly added enum values compile and existing legacy event values remain supported.
- Add unit tests for card mapping and routing, including the Walk-in → Trial story and check-in overrides.
- Capture runtime screenshots for at least: Walk-in, Trial Active, Trial Expired, Trial Converted, Freeze Active, Ban Active, Check-in, Overdue Payment, Payment Success, PT Started, Workout, Service Active/No Service, Machine Fault.
- Inspect scroll and CTA visibility on every representative card.
- Build/test/device verification required before claiming completion.
- Antigravity remains execution owner for visible UI integration and runtime evidence.


## CURRENT — Stage 7.9 Complete Fixture Coverage + Production Readiness
- Specification: `docs/reference/AGY_STAGE_7_9_FIXTURE_COVERAGE_PRODUCTION_READINESS.md`
- Execution owner: Google Antigravity.
- Mission: create a deterministic fixture universe for EVERY supported EventType/card variant so the entire Member Intelligence system can be tested, not just the currently visible 9 cards.
- Required: typed synthetic raw event data, member snapshots, attendance/payment/trainer/workout/service/plan/operations datasets, fixture-only media/assets, QA gallery, NOW/PAST/FUTURE coverage, edge cases, routing/card-kind tests, Compose/UI coverage, build/lint/device verification and evidence.
- Fixture data must be explicitly synthetic/demo-only and must flow through the same renderer/mapping path as production data. Never fabricate real member/biometric/payment/evidence records.
- Completion gate: do not call Member Intelligence production-ready until the full supported taxonomy is reachable in the QA gallery and build/tests/device evidence are committed with the exact SHA.
- Previous latest verification commit before this packet: `d7d65cff3aff7e45f216f55c9e12aefb167b8b2c`.
- Packet commit: `985d4990bd98bf44c031adce579859c1dbf4a984`.


## REMOTE SYNC MANIFEST — 2026-09-25

**Purpose:** This section is the GitHub handoff contract for Antigravity. Do not assume that a commit count equals a file count. Pull the entire `origin/member-intelligence-v3` ref and verify the resulting HEAD SHA before running.

**Remote HEAD:** `ac3545396b6d4fb53fb1ad525f2004ad03f370d3`

### Latest 3 commits currently above the previous local state
1. `e5e653cbd519a4957a51a24bddb0ad904ea5e063` — advanced event-first card renderer
   - ADDED: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/AdvancedEventMemberCard.kt`
2. `902cd522b4fed770a6e612dffe7e2d11890eb97e` — browse surface uses advanced event card
   - MODIFIED: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCarousel.kt`
3. `ac3545396b6d4fb53fb1ad525f2004ad03f370d3` — simplify workspace background
   - MODIFIED: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceScreen.kt`

**Expected file delta for these 3 commits:** 3 unique files (1 added + 2 modified).

### What each file does
- `AdvancedEventMemberCard.kt`: event-specific light card renderer; Walk-in/Trial, Freeze, Ban, Payment, Trainer, Workout, Service and operations/issue variants; one primary CTA; truthful-data-only attendance pulse (requires 7+ pattern values).
- `CompactMemberCarousel.kt`: browse mode now renders the advanced event card; expanded/detail mode retains the existing detail card and interaction flow.
- `MemberIntelligenceScreen.kt`: removes the noisy workspace vertical gradient and uses the active light background directly.

### Pull/verify contract for Antigravity
1. `git fetch origin`
2. `git pull --ff-only origin member-intelligence-v3`
3. Confirm HEAD exactly equals `ac3545396b6d4fb53fb1ad525f2004ad03f370d3` (or a later explicitly documented commit).
4. Confirm the three paths above are present at the pulled HEAD.
5. Run build/tests before reporting success. Do not report a partial pull as complete.
6. Push any runtime/build corrections back to `member-intelligence-v3` with an exact SHA and list of changed files.

**Important:** GitHub commit count and changed-file count are different. The authoritative check is the HEAD SHA + changed-file list, not the number shown by the IDE alone.


## CURRENT — Stage 7.8/7.9 Event Card Variety Lab — ChatGPT Source Packet 2026-09-26
- Direct GitHub implementation completed on `member-intelligence-v3`.
- Verified taxonomy: 68 typed `EventType` values + `UNKNOWN` sentinel.
- Catalog coverage: 68/68 typed events.
- Reusable archetypes: 16.
- Added contextual variant resolver: Late Check-in, Early Check-in, Late Payment/Overdue, Failed, Partial, Active, Resolved, Converted, Expired, Blocked, Reopened.
- Added visible contextual variant treatment to `AdvancedEventMemberCard.kt`.
- Durable Antigravity handoff: `docs/reference/EVENT_CARD_VARIETY_LAB_HANDOFF.md`.
- Shared state updated: `AI_SYNC_STATE.md`.
- ChatGPT packet commits: `7112c48f090ab57d0abe05780c03742ad5feb2b2`, `e43fac3dd6e3b93d6effbdde2d2433884cd1c6e5`, `d5138a056c65d1995e10143491b523bfb142d2f3`, `41b4db9de51640a8fcfdab1e4ec162f1c4e08d22`, `dac6cf9946613b4e053181666f391620a02470b3`.

### Antigravity next action
Pull `origin/member-intelligence-v3` fully, verify the latest HEAD, build/test, run the complete event-card fixture/gallery, inspect visual hierarchy and CTA visibility, capture runtime screenshots, and push any required corrections with exact SHA + changed-file list. Do not report completion from commit count alone.
