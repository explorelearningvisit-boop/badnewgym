# BAD GYM — Stage 7.8 Event Card Variety + Story System

## Decision

We should NOT build 50 unrelated Composables. We should build **16 reusable card archetypes** and configure them for **54+ event variants**.

The user-visible result is still a large variety of cards because the information hierarchy, state, evidence, media, timeline and primary action change per event.

### 16 card archetypes
1. Home Decision
2. Check-in
3. Check-out
4. Walk-in
5. Trial
6. Freeze
7. Ban / Access Restriction
8. Membership
9. Payment
10. Trainer
11. Workout / Progress
12. Service
13. Facility / Operations
14. Issue / Resolution
15. Communication / Promotion
16. Insight

## 1. Walk-in must be its own card

A walk-in is a lead/visit event, not a generic member card.

Default card story:

**WALK-IN → TRIAL → CONVERTED / EXPIRED**

Above fold:
- member/visitor photo if actually recorded
- Walk-in badge
- visit time
- source (staff/QR/etc.) only if recorded
- trial status
- trial duration
- one primary CTA

Expanded story:
- walk-in timestamp
- trial started timestamp
- trial expiry timestamp
- conversion timestamp
- plan selected
- staff/actor
- visits during trial
- last interaction

Primary actions:
- Start trial
- Convert trial
- Extend trial
- Open history

Do not invent trial days. Show the configured trial duration only when recorded.

## 2. Trial is its own card

Trial card states:
- TRIAL ACTIVE
- TRIAL EXPIRING
- TRIAL EXPIRED
- TRIAL CONVERTED

Visual:
- member photo/lead photo when available
- compact expiry progress
- visits during trial
- next decision
- one CTA

The card must preserve the journey from the original walk-in rather than pretending the trial appeared independently.

## 3. Freeze is its own member card

Show:
- member identity
- plan
- freeze state
- freeze start/end
- remaining frozen days
- reason only if recorded
- reactivation state

CTA:
- View freeze
- Unfreeze/reactivate

No red for a normal active freeze. Use informational/amber semantic state.

## 4. Ban is its own access-restriction card

Use only when the gym has a real ban/restriction record.

Show:
- member photo
- ACCESS BANNED
- restriction timestamp
- reason
- actor
- whether access is currently blocked
- lifted timestamp if resolved

CTA:
- View reason/history
- Lift ban only where caller has permission

This is a true access restriction and may use P0/P1 according to gym policy. Never fabricate or infer a ban from an unrelated complaint.

## 5. Full event-variant inventory

### Access / member journey — 10
CHECK_IN, CHECK_OUT, WALK_IN, TRIAL_STARTED, TRIAL_CONVERTED, TRIAL_EXPIRED, FREEZE_STARTED, FREEZE_ENDED, BANNED, BAN_LIFTED

### Membership — 6
MEMBER_CREATED, MEMBER_UPDATED, MEMBERSHIP_STARTED, MEMBERSHIP_RENEWED, MEMBERSHIP_EXPIRED, MEMBERSHIP_CANCELLED

### Payment — 8
PAYMENT_SUCCESS, PAYMENT_FAILED, PAYMENT_DUE, PAYMENT_OVERDUE, PAYMENT_PARTIAL, REFUND, INVOICE_CREATED, PACKAGE_PURCHASED

### Trainer — 6
TRAINER_ASSIGNED, TRAINER_SESSION_SCHEDULED, TRAINER_SESSION_STARTED, TRAINER_SESSION_COMPLETED, TRAINER_SESSION_MISSED, TRAINER_SESSION_CANCELLED

### Workout — 6
WORKOUT_STARTED, WORKOUT_COMPLETED, WORKOUT_SKIPPED, PR_ACHIEVED, GOAL_UPDATED, BODY_MEASUREMENT_UPDATED

### Services — 6
SERVICE_ACTIVATED, SERVICE_DEACTIVATED, SERVICE_BOOKED, SERVICE_USED, SERVICE_EXPIRED, SERVICE_ISSUE

### Facility / operations — 8
MACHINE_FAULT, MACHINE_REPORTED, MACHINE_FIXED, MAINTENANCE_STARTED, MAINTENANCE_COMPLETED, CLEANING_STARTED, CLEANING_COMPLETED, STOCK_LOW

### Issues / communication — 4+
COMPLAINT, COMPLAINT_RESOLVED, INCIDENT_REPORTED, INCIDENT_RESOLVED, ANNOUNCEMENT, OFFER

The catalog is extensible; the renderer must not require a new Composable for every new event.

## 6. Menu routing

| Event / state | Default menu | Decision |
|---|---|---|
| Walk-in | Attendance | Start trial |
| Trial active | Plan | View/convert |
| Trial expiring | Plan | Convert/extend |
| Trial expired | Plan | Convert/follow up |
| Check-in | Attendance | Open attendance |
| Check-in + overdue | Payment | Collect payment |
| Check-in + expired plan | Plan | Renew |
| Freeze started | Plan | View/unfreeze |
| Ban | Plan | View restriction |
| Payment failed/overdue | Payment | Collect |
| PT started | Trainer | Open session |
| Workout | Workout | Open workout |
| Service issue | Services | Resolve |
| Machine fault | Services/Operations | Resolve |
| Complaint | History/Issue | Resolve |
| Incident | Services/Operations | Resolve |

Auto-routing is assistance only. Owner can navigate to any enabled menu.

## 7. NOW / PAST / FUTURE

Use a compact segmented control where it adds decision value.

- Walk-in: NOW current visit; PAST previous visits; FUTURE trial follow-up/conversion.
- Trial: NOW current state; PAST walk-in + visits; FUTURE expiry/conversion.
- Freeze: NOW frozen state; PAST reason/history; FUTURE reactivation.
- Ban: NOW blocked; PAST reason/audit; FUTURE lift/review.
- Payment: NOW outstanding; PAST payments; FUTURE due/renewal.
- Trainer: NOW live/next; PAST sessions; FUTURE schedule.
- Workout: NOW latest/current; PAST sessions/progress; FUTURE program.
- Services: NOW active/issues; PAST usage; FUTURE expiry/bookings.

## 8. Media / photos / video

Media is evidence, not decoration.

| Card | Preferred media | Rule |
|---|---|---|
| Walk-in | member/visitor photo | only if actually captured |
| Trial | member photo + optional status media | no fake avatar/photo |
| Freeze | member avatar | photo optional |
| Ban | member photo | restriction evidence/audit; never raw biometric data |
| Payment | no media by default | receipt thumbnail only if stored |
| Trainer | trainer avatar | session media only if stored |
| Workout | progress/photo timeline | only if actual media exists |
| Service | service image | owner-configurable |
| Machine fault | equipment image | real asset image or neutral icon |
| Complaint/incident | attachment thumbnail | only actual attachment |
| Offer | promotion image | separate from alerts |

Video/status-style interaction is allowed only when the backend has a real media reference. No fabricated media.

## 9. Card geometry

Every card remains compact.

Above-fold target:
- 1 event/state headline
- identity
- 3–5 decision-relevant facts
- 1 primary CTA

Expanded:
- supporting evidence
- story/timeline
- NOW/PAST/FUTURE
- media viewer if supported
- audit detail

Never show 20 metrics at once.

## 10. Semantic visual personalities

- Access/check-in: fresh mint / aqua
- Walk-in/trial: warm peach / aqua
- Freeze: soft lavender / amber accent
- Ban: rose/red semantic only for actual restriction
- Payment: sunlit gold / semantic payment state
- Trainer: aqua / lavender
- Workout: fresh mint / progress accent
- Services: coral/peach
- Facility fault: amber/red semantic
- Resolved: green success
- Promotion: isolated promotional treatment

The semantic state must remain independent from the material theme.

## 11. Acceptance

- Walk-in, Trial, Freeze and Ban are visibly distinct card variants.
- Walk-in preserves the full walk-in → trial → conversion/expiry story.
- 16 reusable archetypes support 54+ event variants.
- Default menu routing is deterministic.
- NOW/PAST/FUTURE is contextual, not forced everywhere.
- Media is evidence-backed only.
- Disabled gym capabilities never produce cards.
- Plan entitlements control service visibility.
- No fake prices, plans, people, photos or event records.
- Primary CTA is always visible.
- Long names wrap correctly.
- Build/tests/runtime screenshots are required before completion.
