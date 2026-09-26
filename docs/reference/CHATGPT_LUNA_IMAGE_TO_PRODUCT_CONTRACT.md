# BAD GYM — ChatGPT Luna Image-to-Product Contract

## Purpose

This document converts the approved high-density BAD GYM concept image into an implementation contract for the existing Member Intelligence card.

The image is a product reference, not a bitmap to place behind the UI. The existing architecture, real repository data, capability rules, temporal records and canonical card remain the source of truth.

## Locked visual direction

- Light-first, premium, soft glass/neumorphism.
- Emerald brand signal with semantic red/amber/green/cyan.
- Dense but readable information hierarchy.
- Card may grow vertically when necessary; do not widen the card merely to fit content.
- One canonical member card.
- One left vertical rail.
- Persistent member context on every non-HOME menu.
- HOME is the executive decision cockpit.
- Non-HOME menus are specialized evidence/work surfaces.
- No fake values, people, payments, attendance, health metrics, ads, rankings or media.

## Image concepts that must become real product capabilities

### 1. Member identity

Show, when actually available:
- photo;
- full name;
- member code;
- tier;
- verification state;
- membership state;
- current event;
- attention state.

Long names must wrap/ellipsize without destroying readability.

### 2. Attendance intelligence

Use actual access events to expose:
- current check-in/out;
- visit count;
- target;
- streak;
- weekly consistency;
- preferred time;
- recent access records;
- late/early status;
- audit timestamp;
- NOW/PAST/FUTURE where meaningful.

Highlight attendance recognition only when gym-wide ranking/recognition data exists.

### 3. Membership intelligence

Expose:
- active/expiring/expired/frozen state;
- start/end;
- days remaining;
- freeze allowance/usage;
- renewal count;
- previous plans;
- entitlement/benefit records when available;
- renewal action.

### 4. Payment intelligence

Expose:
- outstanding balance;
- overdue days;
- due date;
- last payment;
- lifetime paid;
- payment status;
- transaction history;
- invoice/ref/method when recorded;
- partial/failed/refund state;
- collection CTA.

Never replace missing transaction records with decorative charts.

### 5. Trainer intelligence

Expose:
- assigned trainer;
- trainer photo when actually available;
- package/session counts;
- next session;
- completed/missed/cancelled history when available;
- focus/goal;
- progress/rating only when real;
- schedule CTA.

### 6. Workout intelligence

Expose:
- active routine;
- current/latest workout;
- duration;
- exercise/session records;
- progression/PR/goal/body data only when supported;
- real trend visualizations only when sufficient records exist.

### 7. Nutrition

Expose:
- active plan;
- meal/macro/hydration records only when actually logged;
- historical trend only when derivable;
- future consultation/plan only when scheduled;
- explicit NO_DATA when records are absent.

### 8. Supplements

Expose:
- active stack;
- purchases;
- usage;
- expiry/reorder;
- product media only when real;
- relevant offer only when actual inventory/campaign data exists.

### 9. Services

Expose:
- active entitlements;
- bookings;
- usage;
- expiry;
- service issues;
- resolution history;
- capability/entitlement state.

### 10. History

History is a true audit timeline:
- event;
- occurredAt;
- recordedAt when different;
- source;
- actor;
- status;
- linked entity;
- drill-down.

Do not use a fake event count.

### 11. Insight

Every insight must be:
signal -> evidence -> interpretation -> next action.

Supported categories:
- Action;
- Pattern;
- Risk;
- Opportunity;
- Forecast;
- Explain.

No invented health score or forecast.

### 12. Social-style member status

The image includes an Instagram-like status/story concept.

Implement as a data-gated feature:
- status author;
- text;
- timestamp/expiry;
- optional real media reference;
- reactions;
- comments;
- visibility.

Do not generate fake media. Status media is evidence/content supplied by the member or gym.

### 13. Approval inbox / Gmail-style workflow

Implement a compact approval surface for real requests:
- channel;
- subject;
- requester;
- preview;
- requested time;
- optional amount;
- PENDING / APPROVED / REJECTED / EXPIRED;
- approve/reject actions.

Email-style UI is a workflow metaphor; it must connect to the real approval repository.

### 14. Recognition / positive reinforcement

The image intentionally makes healthy business/member behavior visible.

Support real recognition records for:
- attendance;
- on-time payment;
- streak;
- workout;
- goal;
- referral;
- milestone.

Recognition must be factual and policy-controlled.

### 15. Gym-wide recognition board

If the backend provides a real gym-wide dataset, show:
- rank;
- member;
- metric;
- value;
- reason;
- current-member highlight.

Examples:
- highest attendance;
- strongest consistency;
- on-time payment streak;
- workout milestone.

Do not derive a leaderboard from a single-member snapshot.

### 16. Rewards

If the real system supports rewards:
- points;
- tier;
- earned milestone;
- redemption state.

No fake points.

### 17. Communication pulse

If available:
- email;
- WhatsApp;
- SMS;
- push;
- in-app communication;
- sent/delivered/read/failed;
- subject/preview;
- timestamp.

### 18. Promotions / paid placements

Commercial content must remain secondary:
1. critical member issue;
2. operational action;
3. relevant house offer;
4. partner/sponsored offer;
5. nothing.

Every promotional surface must have:
- campaign;
- eligibility;
- placement;
- sponsor label;
- frequency cap;
- tracking;
- expiry;
- real inventory/offer source.

Never allow ads to cover overdue payment, access restriction, safety or operational issues.

## Business-owner operating principle

The card should make good member behavior visible without creating unfair or misleading pressure.

Recognition should be configurable by gym policy and visibility. Owners/staff can see operational recognition; members can see their own achievements and any gym-wide board that the gym has explicitly enabled.

## Real-time / second-level record direction

The existing event architecture remains authoritative.

Every important event should preserve:
- event id;
- member id;
- gym id;
- event type;
- occurredAt;
- createdAt;
- source;
- actor;
- metadata;
- idempotency key.

Second-level timestamps should be shown only when the underlying record has that precision.

Use temporal/on-demand loading for large history/media sets.

## Data-state law

Every new surface must support truthful states:
- ACTIVE
- EXPIRING
- EXPIRED
- NOT_ENROLLED
- NO_DATA
- UNAVAILABLE
- CRITICAL
- WARNING
- RESOLVED

Never fill an empty surface with made-up values.

## Reusable UI architecture

Do not create one giant composable or dozens of one-off panels.

Use reusable primitives:
- DecisionHero
- MetricGrid
- EvidenceRow
- Timeline
- ProgressRail
- TrendBars
- UsageMeter
- StatusStories
- ApprovalInbox
- RecognitionPulse
- Leaderboard
- CommunicationPulse
- OpportunityCard
- SponsoredSlot
- StateEmpty

## Current ChatGPT Luna implementation

The following source changes have already been pushed through GitHub:

1. `MemberSnapshot.kt`
   - Added optional `engagement` data contract.

2. `MemberIntelligenceEngagementModels.kt`
   - Added real-data models for status stories, approvals, recognitions, gym leaderboard entries, communications, rewards and notification count.

3. `MemberEngagementHub.kt`
   - Added data-gated social-style status, approval inbox, recognition/rewards and gym recognition surfaces.

4. `CompactMemberCard.kt`
   - Integrated the engagement hub into HOME.
   - Removed decorative/fabricated workout trend bars.

5. `CompactCardDimensions.kt`
   - Increased vertical card height to accommodate the richer information surface without widening.

6. `MenuContentPanels.kt`
   - Removed hardcoded payment/workout/history event counts.
   - Replaced them with real snapshot-derived counts.
   - Replaced nutrition placeholder copy with an explicit truthful data state.

## Verification requirement

These changes are implementation work, not a claim of build success.

Antigravity must now:
1. pull the exact branch HEAD;
2. inspect all changed files;
3. compile;
4. run unit tests;
5. run lint if configured;
6. install on Xiaomi Redmi Note 11;
7. capture HOME and every retained menu;
8. verify no clipping/overflow/empty canvases;
9. verify no fake data;
10. verify the new engagement surfaces with populated and empty fixtures;
11. report exact changed-file inventory and final SHA.

## Acceptance gate

The concept is accepted only when the physical device demonstrates that the card feels like an operational member intelligence workspace rather than a collection of small dashboard tiles.

The implementation must remain useful for:
- gym owner;
- manager;
- front desk/reception;
- trainer;
- sales staff;
- operations/facility staff;
- member.

