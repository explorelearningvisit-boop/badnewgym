# BAD GYM Member Intelligence — Production Blueprint

## 1. Product contract
The member card is an operational decision surface, not a database dump.
Information hierarchy: EVENT → MEMBER → TIME → STATE → EVIDENCE → PATTERN → FORECAST → DECISION → ACTION.
First glance: who, what happened, current state, attention required, next action.

## 2. Two-rail navigation
LEFT — Core member context: Home, Attendance, Plan, Payment, More.
RIGHT — Contextual / premium / intelligence: PT, Workout/Lift, Supplements, Nutrition/Diet, Services, History/Log, Insight/AI, Offers.
The left rail stays short. The right rail is capability/evidence driven. PT appears when PT data/entitlement exists; Supplements when supplement history/entitlement exists; Nutrition when subscribed; Services for premium/service/Flex capability.

## 3. Home
Home is the cockpit: current event/state, identity/tier/verification, membership, payment attention, attendance, trainer/workout availability, highest-priority signal, one CTA, and a state-specific card variant.

## 4. State-specific card variants
Required variants include: check-in/out, new member, walk-in, trial active/converted/expired, active/expiring/expired/frozen/reactivated membership, ban/lift, due/overdue/failed/partial/success payment, PT assigned/scheduled/live/missed/cancelled, workout started/completed/skipped, PR/goal/body measurement, supplement/service activity, nutrition active/no-data, service booked/active/expired/issue, machine fault/reported/fixed, maintenance started/completed, cleaning started/completed, stock low, complaint/resolved, incident/resolved, announcement and offer.
The repository already contains a typed event taxonomy and event-card catalog. Reuse those typed events and visual archetypes instead of creating one unrelated Composable per event.

## 5. Menu information grammar
Every menu supports NOW → PAST → FUTURE → EVIDENCE → PATTERN → FORECAST → ACTION.
Attendance: visit/access audit, consistency, streak, timing, future sessions; bars/calendar/heatmap when data is sufficient.
Plan: lifecycle, current/previous plans, freezes, expiry and renewal.
Payment: outstanding/due/overdue, transaction history, invoice/ref, partial/refund/failure, future dues.
Trainer: coach, session balance, completed/missed/cancelled history, future schedule.
Workout: routine/session, history, PR/volume when recorded, future program/goal.
Supplements/Nutrition: only recorded purchase, entitlement, plan, renewal and logged nutrition data. Never invent dosage, macros or health scores.
Services: ACTIVE → USAGE → EXPIRY → NEXT BOOKING → ISSUE.
History: unified timestamped event/audit timeline with actor/source/metadata.
Insight: Action, Pattern, Risk, Opportunity, Forecast and Explain, with evidence for every insight.

## 6. Gym operations intelligence
Long-term owner operations should use a gym-level repository, not fake member fields.
Operational chain: REPORT → CLASSIFY → ASSIGN → START → PROGRESS → RESOLVE → VERIFY → COST → HISTORY.
Machine: fault → assignment → repair → verification → downtime/cost.
Cleaning: required → assigned → started → completed → verified.
Installation: scheduled → installed → inspected → accepted.
Utilities: electricity/water/internet/DG/AC expense → bill period → amount → paid → trend/anomaly.

## 7. Flex network
Flex is a cross-gym entitlement.
State machine: REQUESTED → ACCEPTED → CHECKED_IN → CHECKED_OUT → CREDITED, with DECLINED/CANCELLED terminal states.
Settlement rule: a request alone never creates a host-gym credit. Credit is created only after the configured completed-visit event.
Required evidence: Flex plan, allowance, used/remaining, host gym, request, acceptance, check-in/out timestamps, per-visit credit and settlement reference.

## 8. Visual system
Light-first: ivory/white, soft mint, emerald, semantic red/amber/green/cyan, subtle glass/neumorphism, Material 3, rounded 16–28dp surfaces.
No dark dashboard, noisy sci-fi, fake media, giant gradients, or infinite decorative animation.
Motion: 120–180ms micro, 180–260ms menu focus, 220–360ms carousel settle; reduced motion respected.
Charts are suppressed when real data is insufficient.

## 9. Business-state priority
Critical evidence must not be hidden by a routine check-in.
Priority examples: incident → machine fault → ban → failed payment → overdue payment → expired membership → freeze → complaint → service issue → missed PT → due payment → operational warning → scheduled PT → service booking → routine activity.
The resolver only promotes events that actually exist in the snapshot/event stream.

## 10. Production data rule
Unknown = —. Capability unavailable = UNAVAILABLE. No enrollment = NOT ENROLLED. No records = NO DATA. Resolved = RESOLVED.
Optional Flex data and future gym-level operations data remain truthful until real repository mapping exists.

## 11. Current implementation
Implemented this cycle: dual left/right detail navigation; non-scrolling short rails; contextual right-rail availability; business-state priority resolver; state-specific Expired/Frozen/Payment/PT/Service/Operations hero variants; optional Flex access contract; Flex visibility through Services; Services + facility/maintenance/cleaning/stock evidence; state-priority tests.
Still required before claiming production-ready: real remote repository wiring for every capability; gym-level operations repository; Flex request/accept/check-in/out/settlement backend; owner expense/utility workflow; complete device QA; release build/unit/lint verification; RLS/tenant-isolation verification; accessibility review.
Production-ready means those gates are verified, not merely that Compose code compiles.