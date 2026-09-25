# BAD GYM — Stage 7.7 Event Card + Gym Capability Engine

## Mission

Turn Member Intelligence from a collection of static menu cards into a configurable, event-driven gym operations intelligence system.

Core model:

EVENT → PRIORITY → DEFAULT MENU → GLANCE → NOW / PAST / FUTURE → DETAIL → ACTION

## 1. Event taxonomy

Use a typed, extensible event catalog. Events are feature-gated and must represent real records only.

### Member / access
CHECK_IN, CHECK_OUT, WALK_IN, TRIAL_STARTED, TRIAL_CONVERTED, TRIAL_EXPIRED, FREEZE_STARTED, FREEZE_ENDED, MEMBER_CREATED, MEMBER_UPDATED, MEMBERSHIP_STARTED, MEMBERSHIP_RENEWED, MEMBERSHIP_EXPIRED, MEMBERSHIP_CANCELLED

### Payment / finance
PAYMENT_SUCCESS, PAYMENT_FAILED, PAYMENT_DUE, PAYMENT_OVERDUE, PAYMENT_PARTIAL, REFUND, INVOICE_CREATED, INVOICE_VOIDED, PACKAGE_PURCHASED

### Trainer / coaching
TRAINER_ASSIGNED, TRAINER_SESSION_SCHEDULED, TRAINER_SESSION_STARTED, TRAINER_SESSION_COMPLETED, TRAINER_SESSION_MISSED, TRAINER_SESSION_CANCELLED, TRAINER_NOTE_ADDED

### Workout / progress
WORKOUT_STARTED, WORKOUT_COMPLETED, WORKOUT_SKIPPED, PR_ACHIEVED, GOAL_UPDATED, BODY_MEASUREMENT_UPDATED

### Services / facilities
SERVICE_ACTIVATED, SERVICE_DEACTIVATED, SERVICE_BOOKED, SERVICE_USED, SERVICE_EXPIRED, SERVICE_ISSUE, SERVICE_RESOLVED

Configurable examples:
Steam, Sauna, Shower, Locker, Towel, Parking, Coffee, Water, Nutrition consultation, Recovery, Massage, Pool, Group class, Cardio zone, Premium equipment access, Guest pass, and custom services.

### Facility / operations
MACHINE_FAULT, MACHINE_REPORTED, MACHINE_FIXED, MAINTENANCE_STARTED, MAINTENANCE_COMPLETED, CLEANING_STARTED, CLEANING_COMPLETED, INSPECTION_STARTED, INSPECTION_COMPLETED, STOCK_LOW, STOCK_REPLENISHED, AREA_CLOSED, AREA_REOPENED, INCIDENT_REPORTED, INCIDENT_RESOLVED

### Communication / engagement
ANNOUNCEMENT, OFFER, MESSAGE_SENT, MESSAGE_DELIVERED, MESSAGE_FAILED, MEMBER_FEEDBACK, COMPLAINT, COMPLAINT_RESOLVED

Do not create fake records just to populate these types.

## 2. Reusable event card archetypes

Do NOT create dozens of unrelated layouts.

Use a small reusable visual system:

### Access Card
Check-in, check-out, walk-in, trial.
Show event, time, source when recorded, current member state, one relevant action.

### Finance Card
Payment success/failed/due/overdue/refund.
Show amount, state, due/paid date, concise history cue, primary finance action.

### Membership Card
Plan start/renewal/expiry/freeze.
Show plan, state, remaining/expiry, trial/freeze state when relevant, action.

### Trainer Card
Assignment/session scheduled/started/completed/missed.
Show trainer, session state, time, sessions remaining, action.

### Workout Card
Workout start/completion/PR/goal.
Show routine, duration, progress signal, action.

### Service Card
Service active/inactive/used/expiring/issue.
Show service icon/image, name, state, entitlement/usage/expiry, action.

### Facility/Ops Card
Machine fault, maintenance, cleaning, inspection.
Show asset/area, state, time, assignee when recorded, action.

### Issue/Resolution Card
Complaint, incident, service issue.
Show issue, severity, state, evidence, owner/action, resolution.

### Insight Card
Derived intelligence only.
Show signal, evidence, priority, action, source events.

All archetypes remain visually BAD GYM, but each has a recognizable semantic personality.

## 3. Event-first default routing

When an event opens Member Intelligence, derive the default menu.

Examples:

CHECK_IN + no urgent issue → Attendance
CHECK_IN + overdue payment → Payment
CHECK_IN + expired membership → Plan
CHECK_IN + scheduled PT session → Trainer
WALK_IN → Attendance or Trial/Lead context according to gym configuration
PAYMENT_FAILED → Payment
TRAINER_SESSION_STARTED → Trainer
SERVICE_ISSUE → Services
MACHINE_FAULT → Services / Operations
CLEANING_STARTED → Operations / Services
No actionable event → Home

Priority must be deterministic and explainable.

The selected menu must remain user-controllable; auto-routing is assistance, not a lock.

## 4. Attention system

Use four semantic levels:

P0 — immediate action
P1 — action required
P2 — important
P3 — informational

Do not make everything red.

Examples:
- payment failed → P0/P1 according to configured policy
- overdue payment → P1
- payment successful → P3
- machine fault → P0/P1
- cleaning completed → P3
- service expired → P1
- no service → informational, never an error
- active service → success/neutral
- resolved issue → active alert disappears

Attention must clear/decay when the underlying state is resolved.

## 5. Gym capability engine

Every gym has a capability profile.

Owner can enable/disable capabilities such as:
PT, steam, sauna, coffee, locker, pool, nutrition, group classes, parking, recovery, machine maintenance, cleaning workflow, trial membership, freeze, walk-ins, and custom services.

Disabled capability:
- does not appear in menus
- does not create empty cards
- does not create empty metrics
- does not generate alerts

## 6. Plan entitlement engine

Gym owners define plans and entitlements.

Example only:

₹500 Basic:
Gym access ✓
Coffee ✕
Steam ✕
Premium cardio ✕
PT ✕

₹1000 Premium:
Gym access ✓
Coffee ✓
Steam ✓
Premium cardio ✓
PT 4 sessions ✓

Do not hard-code these prices or names.

Plan editor must support:
- create/edit plans
- add/remove services
- quantity/usage limits
- validity
- included/excluded state
- service availability override
- activation/deactivation

Member view:
- show included active services when useful
- do not list every excluded service on the main card
- detailed exclusions belong under Plan → Benefits

## 7. Custom service engine

Owner can create/edit:
- service name
- icon/image
- category
- active/inactive
- description
- price
- included plans
- usage limit
- duration/expiry
- booking required
- responsible staff/role

Images must be replaceable by owner using storage/image references.

Compact service tile:
icon/image + name + state + one useful datum.

## 8. Expand / collapse / hide / unhide

Default view shows only important information.

Expand reveals supporting details.

Open reveals full history/detail.

Owner may hide optional low-value sections and restore them later.

Never hide:
- critical active issue
- actionable overdue payment
- membership expiry affecting access
- active safety/operational incident

Hiding presentation does not delete data.

## 9. NOW / PAST / FUTURE

Menus with temporal meaning support:

NOW | PAST | FUTURE

NOW = current state/action.
PAST = history/trend/audit.
FUTURE = schedule/expiry/renewal/forecast/planned action.

Use a compact segmented control, not three giant cards.

Preserve temporal selection when navigating into detail and back.

## 10. Menu minimum information

HOME:
current event, current state, top 1–2 attention signals, useful attendance ring/trend, one primary action.

ATTENDANCE:
visits/target, streak/pattern, compact trend, access history on demand.

PLAN:
plan, active/expired/frozen/trial state, expiry/remaining, benefits on demand.

PAYMENT:
outstanding, due/overdue state, latest payment, compact history/trend, collect/record action.

TRAINER:
coach, next session, sessions remaining, session state, schedule/open action.

WORKOUT:
current routine, latest workout, frequency/progress, meaningful evidence.

SUPPLEMENTS:
active/recent relevant item, usage/expiry when recorded.

NUTRITION:
current plan/status, today's tracked state if available, trend only when data exists.

SERVICES:
active services, state, expiry/usage, issue/action when present, clear no-service state.

HISTORY:
recent events, filters, audit detail.

INSIGHT:
maximum 3–4 high-value signals, evidence, action.

MORE:
settings/actions/documents only.

## 11. Information-density rule

Every visible field must answer at least one:
1. Does it change a decision?
2. Does it trigger an action?
3. Does it explain a meaningful trend?
4. Does it expose an operational problem?

If none apply, keep it behind detail or remove it from the default view.

## 12. Large-gym scalability

The architecture must support small and large gyms without permanent UI clutter.

Possible capabilities:
multiple branches, zones, equipment inventory, maintenance teams, cleaning teams, pool/sauna/steam, café, recovery center, PT, group classes, guest passes, parking, lockers, retail, nutrition, physiotherapy, events.

These are capabilities/modules, not permanent dashboard sections.

## 13. Production rules

- Preserve compact card geometry unless runtime evidence proves a small adjustment is needed.
- Vertical scroll for long sequential content.
- Horizontal scroll only for peer collections/tabs/timelines.
- Never clip CTA.
- Never fabricate data.
- Never expose disabled capabilities.
- Keep promotions separate from operational alerts.
- Preserve accessibility and contrast.
- Support reduced motion.
- Use data-driven purposeful animation.
- Do not store raw biometric templates/camera frames in UI.
- Do not start unrelated Stage 8 work.

## 14. Acceptance criteria

- typed event catalog exists;
- event-to-default-menu routing is deterministic;
- reusable event card archetypes exist;
- gym capabilities can be enabled/disabled;
- custom services can be created/edited with replaceable images;
- plans can include/exclude services;
- member view reflects actual entitlements;
- no-service is clear and non-alarming;
- active/expired/problem states are distinct;
- NOW/PAST/FUTURE works where applicable;
- repeated information is removed;
- primary CTAs are visible;
- scrolling is intentional;
- runtime screenshots exist for representative events;
- tests/build pass;
- Antigravity pushes exact verified SHA.

## 15. Antigravity execution packet

Read this specification and execute visibly on member-intelligence-v3.

Before editing, inspect the current code and run the app.

Verify representative event states:
1. check-in
2. check-out
3. overdue payment
4. payment success
5. payment failed
6. frozen member
7. trial member
8. walk-in
9. trainer session started
10. service active
11. service inactive/no service
12. machine fault
13. maintenance
14. cleaning started/completed

Also verify one custom-service + plan-entitlement scenario.

For each representative state:
- capture initial screenshot;
- inspect above-fold information;
- test vertical/horizontal scrolling;
- verify CTA;
- verify priority highlight;
- verify menu routing;
- verify NOW/PAST/FUTURE where applicable.

Update collaboration/status files and push the verified implementation to member-intelligence-v3.

Do not claim completion without runtime evidence.
