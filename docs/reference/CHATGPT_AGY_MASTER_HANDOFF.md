# BAD GYM — ChatGPT → Antigravity Master Handoff

**Purpose:** This is the durable engineering/product handoff for any AI agent (especially Google Antigravity) continuing Member Intelligence work. It records the current product law, architecture, UX decisions, event-card system, data-truth rules, execution protocol, and verification gate.

**Branch:** member-intelligence-v3
**Source of truth:** GitHub branch HEAD + repository source
**Execution owner:** Antigravity for visible repo implementation, build, tests, device QA, commit and push
**Product/design/architecture owner:** ChatGPT
**No hidden/background execution. No force-push/reset/discard.**

## 1. READ ORDER — BEFORE TOUCHING CODE

Read in this order:
1. CURRENT_TASK.md
2. AI_SYNC_STATE.md
3. this file
4. active reference specs under docs/reference/
5. latest remote HEAD and changed-file inventory
6. relevant Member Intelligence source
7. current verification evidence

Never assume that an old task section is still active if a newer authoritative section conflicts with it. The newest explicit user-approved direction and latest remote implementation take precedence.

## 2. CURRENT REMOTE STATE

Latest known remote HEAD at handoff:
`ac3545396b6d4fb53fb1ad525f2004ad03f370d3`

Latest three commits in the current visual packet:
- `e5e653cbd519a4957a51a24bddb0ad904ea5e063` — added AdvancedEventMemberCard.kt
- `902cd522b4fed770a6e612dffe7e2d11890eb97e` — browse carousel uses AdvancedEventMemberCard
- `ac3545396b6d4fb53fb1ad525f2004ad03f370d3` — simplified MemberIntelligenceScreen workspace background

Those three commits represent **3 unique changed files**: one added + two modified. Commit count is not file count.

## 3. CURRENT VISUAL IMPLEMENTATION

### AdvancedEventMemberCard.kt
Reusable event-first light card renderer. It must:
- lead with the actual event/state;
- show member identity from real snapshot data;
- expose 3–5 decision facts above the fold;
- show one primary CTA when an action exists;
- use event-specific semantic layouts rather than a generic repeated panel;
- preserve truthful data only;
- keep light surfaces and restrained depth;
- never fabricate photos, prices, plans, dates, coaches, metrics, forecasts or media.

Current semantic variants include:
- Walk-in / Trial journey
- Freeze
- Ban/access restriction
- Payment
- Trainer
- Workout
- Service
- Facility/operations
- Issue/resolution
- generic event/overview
- contextual late/failed/partial/active/resolved/converted/expired/blocked variants where the underlying event state supports them.

Attendance visualization must render only when sufficient real weekly pattern data exists (7+ values). Do not render a misleading 7-day chart from incomplete data.

### CompactMemberCarousel.kt
Browse mode uses AdvancedEventMemberCard. Detail mode retains the established canonical detail card and interaction flow.

### MemberIntelligenceScreen.kt
Workspace background was simplified to the active light theme background to remove noisy competing gradients/decorative layers.

## 4. PRODUCT LAW

The Member Intelligence product follows:

EVENT → MEMBER → TIME → STATE → EVIDENCE → PATTERN → FORECAST → DECISION → ACTION

Every intelligence block should answer:
1. what happened;
2. when;
3. current state;
4. evidence;
5. past/current/future where meaningful;
6. owner/member next action;
7. underlying records on demand.

Never display data merely because it exists. Display it because it answers a decision question.

## 5. CANONICAL CARD LAW

- One canonical Member Intelligence card.
- Do not create a second member-detail/dashboard shell.
- Event intelligence lives inside the canonical card.
- No fake data.
- Large readable hierarchy.
- Long names wrap naturally.
- Light UI only; never use black/dark-black backgrounds.
- Avoid giant decorative gradients.
- Use subtle depth, glass/neumorphic cues and motion only where they improve hierarchy.
- Promotions are isolated from critical alerts/financial/access state.
- Critical overdue/access/safety/operations states can never be displaced by advertising.
- One clear primary CTA when an actionable state exists.

## 6. EVENT CARD VARIETY

Do not create dozens of unrelated Composables. Use reusable archetypes configured by event state, evidence, media references and actions.

Current reusable archetypes:
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

The taxonomy is broad (68 typed EventType values plus UNKNOWN). All supported typed events should map through EventCardCatalog/EventCardVariantResolver. UNKNOWN must remain safely handled.

### Mandatory distinct stories
- WALK-IN is its own card, not a generic member card.
- Walk-in story: WALK-IN → TRIAL → CONVERTED or EXPIRED.
- TRIAL is its own card/lifecycle.
- FREEZE is its own member-state card.
- BAN is its own access-restriction card and appears only for a real restriction record.
- Check-in, payment, PT, workout, service, operations and issue events must feel semantically different.
- Media is evidence, never decoration; only render stored media references.
- Facility-wide operations must not be incorrectly represented as member facts; use gym-level repositories for owner-level machine maintenance, cleaning, installation, utilities, etc.

## 7. DETERMINISTIC ROUTING

Default event/menu routing:
- check-in → Attendance
- check-in + overdue payment → Payment
- check-in + expired membership → Plan
- check-in + scheduled PT → Trainer
- walk-in → Attendance / Trial context
- payment failure/overdue → Payment
- trainer session → Trainer
- workout → Workout
- service issue → Services
- machine/operations issue → Services/Operations
- no actionable event → Home

P0/P1 signals have priority. Home and the automatically opened problem menu must agree on the same priority.

## 8. MENU INFORMATION ARCHITECTURE

Core:
- Home: Overview, Today, Risk, Opportunities
- Attendance: Live, Pattern, Calendar, Timing, Forecast
- Plan: Current, Timeline, Benefits, Renewals, History, Forecast
- Payment: Audit, Timeline, Recurring, Dues, Forecast, Offers

Contextual/intelligence:
- Trainer: Coach, Sessions, Schedule, Performance, Notes, Media
- Workout: Today, Program, Sessions, Progress, Muscles, PRs, Media
- Supplements: Stack, Inventory, Usage, Orders, Expiry, Recommendations
- Nutrition: Today, Macros, Meals, Hydration, Trends, Plan, Media
- Services: Active, Bookings, Usage, Expiry, Issues, Media
- History: All, Check-in/out, Workout, Payment, Trainer, Plan, Service, Issues, Media
- Insight: Action, Pattern, Risk, Opportunity, Forecast, Explain
- More: Profile, Documents, Body/Health records only when actually supported, Achievements, Rewards, Announcements, Settings, Support
- Offers: real promotions/eligibility/expiry only.

Every menu should be a real information surface, not a placeholder. Use NOW/PAST/FUTURE only where it helps.

## 9. TEMPORAL + DATA LOADING LAW

Temporal granularity:
LIVE, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, QUARTER, HALF_YEAR, YEAR, CUSTOM.

Every event carries occurrence/recording context and source/actor/status/payload/reference when available.

Loading strategy:
- initial member open: identity + current state + trigger event + compact badges + minimal summary + top actionable insight + promotion metadata;
- menu tap: fetch menu summary;
- range/submenu: fetch only requested dataset;
- event tap: fetch detail;
- media: thumbnail metadata first, full media only on viewer open;
- cursor pagination;
- conditional requests/ETag, compression, server-side filtering/projection/aggregation;
- no continuous polling for historical menus;
- heavy aggregation server-side;
- network is source of truth; local cache/outbox supports offline operation.

Three density tiers:
1. Decision/front card
2. Explanation/menu
3. Audit/drilldown

Do not dump raw data into the front card.

## 10. TRUTH / CAPABILITY / ENTITLEMENT RULES

Gym capabilities are owner-configurable: PT, steam, sauna, coffee, locker, pool, nutrition, group classes, parking, recovery, maintenance, cleaning, trial, freeze, walk-ins and custom services.

If a capability is disabled:
- no menu;
- no empty card;
- no empty metric;
- no alert.

Plan entitlements control member-visible services. Custom services require real configuration/records.

Unknown data = unavailable/no-data. Never guess.

Attendance source must be one actually recorded source (FACE, CAMERA, QR, NFC/RFID, STAFF, MEMBER_APP, UNKNOWN). Never store/display raw biometric templates or camera frames.

## 11. WALK-IN / TRIAL / FREEZE / BAN CONTRACT

Walk-in above fold:
- visitor/member photo only if actually recorded;
- WALK-IN badge;
- visit time;
- source if recorded;
- trial status;
- trial duration if recorded;
- one CTA.

Expanded story:
walk-in timestamp → trial start → trial expiry → conversion timestamp → plan → actor/staff → trial visits → last interaction.

Trial states:
ACTIVE, EXPIRING, EXPIRED, CONVERTED.

Freeze:
plan, state, start/end, remaining days, reason if recorded, reactivation state.

Ban:
member identity, ACCESS BANNED, timestamp, reason, actor, current restriction, lifted timestamp when available. Action is permission-dependent.

## 12. UX / VISUAL DIRECTION

The desired visual language combines:
- futuristic/high-tech polish;
- simple, elegant comprehension;
- light pastel/neumorphic/glass material;
- restrained 2.5D depth;
- clear semantic state accents;
- strong typography and spacing;
- useful charts/graphs only;
- subtle microinteraction.

Avoid:
- black backgrounds;
- giant decorative gradients;
- visual hodgepodge;
- repetitive identical cards;
- data-dump dashboards;
- fake analytics;
- fake images;
- meaningless animation;
- excessive scrolling;
- vertical letter stacking;
- clipped content behind rails.

Motion tokens:
- press: 80–140ms
- micro: 120–180ms
- focus: 180–260ms
- menu: 180–250ms
- carousel settle: 220–360ms
Respect reduced motion and avoid infinite decorative animation.

## 13. RAIL / NAVIGATION PRINCIPLE

Navigation must remain vertical and readable within the canonical card. Capability-driven visibility is mandatory.

The exact rail arrangement must follow the latest user-approved implementation in CURRENT_TASK and source, not an obsolete historical section. Do not resurrect a superseded navigation architecture merely because an old document describes it.

No global bottom navigation. No second detail shell.

## 14. MULTI-DEVICE / CELLULAR SYNC

GitHub is durable engineering context, NOT the runtime database.

Runtime path:
Android → HTTPS/TLS → backend → database/realtime → authorized devices.

USB/ADB may install/debug but must not be assumed to be the application sync path.

Use app-generated installationId, not IMEI/SIM/phone/MAC, as device identity.

Flow:
INSTALL → AUTHENTICATE → REGISTER DEVICE → INITIAL SYNC → DELTA/REALTIME SYNC → LAST-SEEN

Progress must represent real work:
Connecting → Authenticating → Registering → Initial Sync 0–100% → Syncing → Up to date
or Offline / Retrying / Sync failed.

Never invent progress percentages.

## 15. GYM FLEX WORKFLOW

Flex cross-gym workflow:
REQUESTED → ACCEPTED → CHECKED_IN → CHECKED_OUT → CREDITED
with DECLINED/CANCELLED terminal states.

Host-gym credit is created only after the configured completed visit. All request/acceptance/attendance/settlement records must be auditable and idempotent.

## 16. ANTIGRAVITY EXECUTION CONTRACT

After receiving a request to Pull and Run:
1. git fetch origin
2. git pull --ff-only origin member-intelligence-v3
3. git rev-parse HEAD
4. inspect CURRENT_TASK.md, AI_SYNC_STATE.md, this master handoff, active specs
5. inspect exact changed-file inventory from the latest remote state
6. inspect relevant source before editing
7. report MODEL → FILES FOUND → EXPECTED WORK → EXECUTION PLAN → BLOCKERS
8. implement only the requested/authorized work
9. run build
10. run unit tests
11. run lint if configured
12. install/run on physical device
13. capture representative screenshots
14. verify behavior, not merely compilation
15. update ANTIGRAVITY_SYNC_ACK.md
16. commit/push only necessary changes
17. report FOUND → PLAN → CHANGED → VERIFIED → SHA → BLOCKERS

Never claim build/device verification without actual evidence.

## 17. VERIFICATION MATRIX

At minimum exercise:
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
- Offers when real data/capability exists

Critical event fixtures:
Walk-in, Trial Active, Trial Expired, Trial Converted, Freeze Active, Ban Active, Check-in, Check-out, Payment Success, Payment Overdue/Failed/Partial, PT Started/Scheduled/Missed, Workout, Service Active/Expired/Issue, Machine Fault, Cleaning/Maintenance, Issue Resolved, Communication/Promotion.

Verify:
- no fake data;
- no vertical text stacking;
- no clipping;
- no giant empty panels;
- decision metric above fold;
- CTA visible when action exists;
- minimal scrolling;
- charts only with actual data;
- member context preserved;
- routing deterministic;
- capability visibility correct;
- temporal state correct;
- promotions never displace critical state.

## 18. CURRENT KNOWN BUILD/RUNTIME STATUS

The last known GitHub Actions build associated with HEAD `ac3545396b6d4fb53fb1ad525f2004ad03f370d3` was still in progress at the time this handoff was authored. Therefore Antigravity must re-run/verify build and tests rather than assuming success.

Do not inherit historical “verified” claims as proof for the current HEAD.

## 19. EVIDENCE / DOCUMENTATION

Antigravity must update:
`docs/reference/ANTIGRAVITY_SYNC_ACK.md`

Required fields:
- exact model;
- provider/configuration;
- branch;
- pulled HEAD;
- files found/inspected;
- files added/modified/deleted;
- purpose;
- build result;
- test result;
- lint result if configured;
- physical device/model;
- runtime result;
- screenshots;
- deviations;
- blockers;
- final SHA.

## 20. ABSOLUTE RULE

When uncertain, inspect the repository and real data model. Do not guess.

The repository is the implementation truth. The active task/spec is the execution contract. User approval is the product-direction authority. Build/test/device evidence is the verification authority.
