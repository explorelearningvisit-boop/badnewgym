# BAD GYM — Current Task

## ACTIVE ANTIGRAVITY EXECUTION PACKET
- Task: Stage 7.6 Production Visual QA & Finalization
- Prompt: `docs/reference/AGY_STAGE_7_6_PRODUCTION_VISUAL_QA_PROMPT.md`
- Execution owner: Google Antigravity (visible GUI/runtime)
- ChatGPT role: review the pushed result after Antigravity verification; do not edit production source during this execution phase.
- Mandatory: screenshot every Member Intelligence menu, inspect vertical/horizontal scrolling, fix clipped CTA, remove repetitive information, prioritize menu-specific data, verify build/runtime, push exact SHA.


STATUS: COMPLETED
TASK_ID: MI-STAGE-7.8-EVENT-CARD-VARIETY-MATRIX
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
