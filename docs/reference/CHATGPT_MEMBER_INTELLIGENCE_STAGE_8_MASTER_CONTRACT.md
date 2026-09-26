# BAD GYM — Member Intelligence Stage 8 Master Implementation Contract

Status: AUTHORITATIVE NEXT IMPLEMENTATION
Baseline: 9a523402eecc6c3f8d049c397f3aa25a256af5e3
Owner: Antigravity for repository implementation/runtime; ChatGPT for product/UX/architecture review

## 0. Why this task exists

Antigravity has completed Concept F hybrid navigation and verified it on the Xiaomi Redmi Note 11. The current implementation is a stable baseline, not the final information architecture.

The next goal is to integrate the generated BAD GYM concept direction into the existing canonical member card without replacing working architecture.

The product requirement is stronger than "show menus":
- one canonical member card;
- vertical menu navigation inside the bounded card;
- each menu is a real member intelligence surface;
- every menu exposes decision data first and complete historical/detail data on drill-down;
- NOW / PAST / FUTURE are used where meaningful;
- no fake or assumed data;
- no duplicate information;
- adaptive states for ACTIVE / EXPIRING / EXPIRED / NOT_ENROLLED / NO_DATA / UNAVAILABLE;
- useful chart variety;
- controlled motion;
- monetization only in irrelevant/available attention space, never above critical member data;
- preserve existing working event/card/temporal architecture.

## 1. Current baseline that must be preserved

Current branch HEAD:
9a523402eecc6c3f8d049c397f3aa25a256af5e3

Latest Antigravity change:
feat(member-intelligence): implement concept F hybrid navigation and persistent member context

Current verified baseline includes:
- left core vertical rail;
- bottom contextual capability pills;
- persistent compact member context on non-HOME;
- HOME large identity treatment;
- deterministic auto-open of important problem menus;
- bounded canonical card;
- real-data-only panels;
- event-driven architecture;
- temporal/on-demand data model;
- QA fixture universe and gallery;
- build + unit tests + physical-device verification.

Do not delete these capabilities. Refactor/merge only when necessary.

## 2. Navigation direction — vertical menu is required

The user explicitly wants the menu concept to remain vertical.

Final direction:
- ONE primary vertical menu rail inside the card.
- No right-side rail.
- Do not create a second full-screen dashboard.
- The vertical rail may scroll independently if needed.
- Prefer icon + short label; labels must never become vertically stacked letters.
- Active item has clear state and optional alert badge.
- Capability-driven visibility remains mandatory.
- Use grouping/dividers sparingly:
  CORE: Home, Attendance, Plan, Payment
  TRAINING: Trainer, Workout
  WELLNESS: Supplements, Nutrition
  SERVICES: Services
  INTELLIGENCE: History, Insight
  UTILITY: More
- Offers/Advertisement should be contextual, not forced as a core navigation destination unless real promotion data exists.
- On narrow card widths, rail uses icon-first compact items with accessible semantics/content descriptions.

## 3. Card geometry

Current repository geometry:
- Compact 296x410dp
- Default 312x426dp
- Expanded 328x442dp
- Detail Compact 320x440dp
- Detail Default 340x463dp
- Detail Expanded 360x480dp

Proposed safe height budget:
- Compact 296x430dp
- Default 312x446dp
- Expanded 328x462dp
- Detail Compact 320x460dp
- Detail Default 340x483dp
- Detail Expanded 360x500dp

Do NOT increase width merely to fit content.
The height increase is optional if the implementation can fit within current bounds. If increased, keep it bounded and test carousel/device layout.

## 4. Universal information model

Every menu surface follows:

MEMBER CONTEXT
→ CURRENT STATE / EVENT
→ DECISION FACTS
→ EVIDENCE
→ NOW / PAST / FUTURE
→ INSIGHT / OPPORTUNITY
→ ONE PRIMARY ACTION

Never turn Tier 1 into a database dump.

Tier 1 = decision card.
Tier 2 = menu explanation.
Tier 3 = full records/audit/detail.

"One card" means one canonical interaction surface, not literally every historical row visible simultaneously.

## 5. Universal data states

Use a shared state resolver. Minimum states:

ACTIVE
EXPIRING
EXPIRED
NOT_ENROLLED
NO_DATA
UNAVAILABLE
CRITICAL
WARNING
RESOLVED

Rules:
- UNAVAILABLE: gym capability is disabled/not supported -> hide menu/section.
- NOT_ENROLLED: capability exists but member has not purchased/activated it -> show truthful opportunity state.
- NO_DATA: capability exists but no record for requested period -> show truthful no-data state.
- EXPIRED: historical record exists and is no longer active.
- ACTIVE/EXPIRING: show current facts.
- CRITICAL/WARNING: semantic emphasis only when supported by real signals.
- Never use '-' or blank placeholders to manufacture density.
- Never show fake names, prices, counts, dates, photos, coaches, services, health metrics, charts or forecast values.

## 6. Menu-by-menu information contract

### HOME
Primary:
- current event/state;
- member identity;
- access/membership state;
- attendance snapshot;
- payment state;
- one highest-priority signal;
- one CTA.

Supporting:
- compact PT/workout/service state only when real;
- one useful trend/visual;
- relevant opportunity or sponsored surface only after member-critical content.

### ATTENDANCE
NOW:
- current check-in/out;
- current visit duration if real;
- today's visit state.

PAST:
- visits by day/week/month;
- visit count;
- attendance consistency;
- missed/no-show if supported;
- typical visit timing;
- streak if real.

FUTURE:
- scheduled visit/session if real;
- forecast only if a real forecast exists.

Charts:
- 7/30-day bars only with enough real points;
- timing distribution;
- streak/progress ring;
- compact calendar/heatmap only if data supports it.

Drilldown:
- every check-in/out record;
- timestamp;
- source/device if recorded;
- duration;
- actor if relevant.

### PLAN / MEMBERSHIP
NOW:
- plan name;
- active/frozen/expired/cancelled;
- start/end;
- remaining days.

PAST:
- previous plans;
- renewals;
- freezes;
- cancellations;
- plan changes.

FUTURE:
- expiry;
- renewal;
- scheduled plan change.

Benefits:
- included services;
- excluded/not-enrolled capabilities when meaningful;
- limits/usage only when actual.

### PAYMENT
NOW:
- outstanding;
- due state;
- overdue days;
- payment status;
- next due if real.

PAST:
- full payment timeline;
- amount;
- date/time;
- method;
- invoice/reference;
- partial/failed/refund state.

FUTURE:
- scheduled/recurring payment;
- upcoming due.

Charts:
- payment timeline;
- due vs paid;
- monthly payment trend when enough data exists.

Drilldown must let operator inspect individual payment/invoice records.

### TRAINER / PT
ACTIVE:
- assigned trainer;
- trainer contact/media only if recorded;
- next session;
- completed/remaining sessions;
- package validity.

PAST:
- all sessions;
- completed/missed/cancelled;
- session duration;
- trainer notes only if permissioned/available;
- attendance/session trend.

FUTURE:
- schedule;
- upcoming sessions.

NOT_ENROLLED:
- "No active PT package/trainer assigned";
- optional relevant house offer only if PT capability exists.

UNAVAILABLE:
- hide PT surface if gym does not support it.

Charts:
- sessions completed vs remaining;
- attendance/consistency;
- trainer trend only if actual data exists.

### WORKOUT
NOW:
- active routine;
- current session;
- completion;
- duration;
- exercises.

PAST:
- sessions;
- volume/reps/sets if recorded;
- PRs;
- goals;
- body measurements if actually supported.

FUTURE:
- assigned program;
- scheduled workout;
- next goal.

Charts:
- session frequency;
- volume progression;
- PR progression;
- muscle distribution only if real.

### SUPPLEMENTS
NOW:
- active stack;
- product;
- status;
- usage if recorded.

PAST:
- purchases;
- usage;
- expired items;
- orders.

FUTURE:
- expiry/reorder only when real.

NOT_ENROLLED:
- no active supplements;
- relevant product opportunity only when capability/inventory/offer is real.

Charts:
- usage/servings;
- expiry timeline;
- purchase trend if sufficient.

### NUTRITION
NOW:
- today's meals;
- calories/macros only if logged;
- hydration only if logged.

PAST:
- meal log;
- macro trend;
- hydration trend;
- adherence only if derivable from real data.

FUTURE:
- active plan;
- upcoming meal plan;
- scheduled nutrition consultation if real.

NO_DATA:
- "No nutrition records for this period" rather than fabricated values.

### SERVICES
NOW:
- active services;
- usage;
- booking state;
- issues.

PAST:
- service usage;
- expired/deactivated services;
- service issue/resolution history.

FUTURE:
- bookings;
- expiry;
- scheduled service.

Capability/entitlement rules:
- disabled capability -> hide;
- plan-excluded service -> show only in Plan benefits/opportunity when relevant;
- not purchased -> NOT_ENROLLED, not empty placeholder.

Facility/operations issues may appear here only when relevant to the member or permissioned workflow.

### HISTORY
Purpose: audit/timeline, not a dashboard.

Filters:
- All
- Check-in/out
- Workout
- Payment
- Trainer
- Plan
- Service
- Issues
- Communication
- Media

Every record should expose:
- event type;
- occurredAt;
- recordedAt if different;
- actor/source;
- status;
- linked entity;
- drill-down.

Use cursor pagination/on-demand loading.

### INSIGHT
Only real intelligence.

Sections:
- Action
- Pattern
- Risk
- Opportunity
- Forecast
- Explain

Every insight must expose evidence:
signal -> evidence -> interpretation -> next action.

No invented "health score" unless the actual product model provides it.

### MORE
Utility directory:
- Profile
- Documents
- Body/health composition if actually supported
- Achievements
- Rewards
- Announcements
- Offers
- Settings
- Support only if actually implemented

Do not turn More into a data dump.

### OFFERS / ADVERTISEMENT
Commercial layer is separate from member facts.

Priority:
1. relevant member data;
2. relevant operational action;
3. legitimate cross-sell opportunity;
4. house offer;
5. partner/sponsored offer;
6. nothing.

Never place an ad above a critical issue, overdue payment action, access restriction, or safety/operations alert.

Clearly label sponsored/promotional content.

## 7. Adaptive monetization slot

Introduce a presentation-level concept only if backend/inventory supports it:

AdaptiveMonetizationSlot:
- eligibility;
- placement;
- campaign;
- sponsor label;
- impression/click/conversion tracking;
- frequency cap;
- member relevance reason.

Examples:
- PT not enrolled -> PT house offer;
- nutrition not enrolled -> nutrition offer;
- active workout + no supplement stack -> supplement offer;
- no relevant house offer -> partner/platform promotion.

Never infer sensitive attributes or invent member preferences.
Never use advertising to conceal missing data.

## 8. Chart and motion system

Chart selection must be semantic:
- Attendance -> bars / heatmap / timing pulse
- Payment -> timeline / amount trend
- Plan -> lifecycle rail
- Trainer -> session completion bars / schedule strip
- Workout -> progression bars / PR trend
- Nutrition -> macro rings / meal timeline
- Supplements -> usage/expiry bars
- Services -> usage meters
- History -> timeline
- Insight -> evidence bars / signal graph

Do not reuse one generic chart everywhere.

Animation:
- 120–180ms micro;
- 180–260ms focus/menu;
- 220–360ms carousel settle;
- data bars/rings animate only on actual value changes or first reveal;
- no infinite decorative animation;
- reduced-motion respected.

## 9. Visual system

Keep light-first:
- ivory/white/soft mint foundations;
- emerald brand accent;
- semantic red/amber/green/cyan;
- controlled theme personalities;
- subtle glass/neumorphism;
- no black dashboard;
- no noisy sci-fi effects;
- no giant gradients;
- no tiny unreadable typography.

Use visual variety through information architecture, not random colors.

## 10. Duplicate-data elimination

Before adding any field ask:
- Is this already shown in member context?
- Is this already shown in the primary state?
- Does the same metric repeat in another section?
- Can it become a drill-down instead?

Examples:
- member name/photo appears once in persistent context;
- payment total is primary in Payment and only a compact status chip elsewhere;
- attendance count can be a Home KPI but detailed records belong to Attendance/History.

Preserve evidence; remove redundant presentation.

## 11. Responsive card rules

Target content viewport after vertical rail must never be narrower than needed for readable labels.

Do:
- icon-first rail;
- short labels;
- ellipsis only for non-critical text;
- 2-column micro grids when width permits;
- single-column collapse on narrow constraints;
- bounded internal scroll for Tier 2/3 content;
- keep critical state above fold.

Do not:
- squeeze text into vertical letters;
- create nested unbounded LazyColumns;
- create giant empty whitespace;
- use full-screen proportions inside 296–328dp card.

## 12. Architecture direction

Preserve:
EVENT -> MEMBER -> TIME -> STATE -> EVIDENCE -> PATTERN -> FORECAST -> DECISION -> ACTION

Suggested reusable layers:
- MemberMenuDataResolver
- MemberDataStateResolver
- MenuInformationModel
- MenuChartSelector
- AdaptiveMonetizationResolver
- MenuContentRegistry
- existing EventCardCatalog/EventCardRouter
- existing temporal/on-demand repository contracts

Do not create 50 unrelated composables.

Use reusable visual archetypes:
- DecisionHero
- MetricGrid
- Timeline
- ProgressRail
- TrendBars
- UsageMeter
- StateEmpty
- OpportunityCard
- EvidenceRow
- SponsoredSlot

## 13. Testing contract

Add/extend tests for:
- every menu;
- every universal data state;
- capability disabled;
- capability enabled but not enrolled;
- no data;
- expired;
- critical;
- chart suppression with insufficient data;
- temporal NOW/PAST/FUTURE;
- duplicate suppression;
- contextual ad eligibility;
- deterministic initial menu;
- CTA selection;
- all existing 69 event fixtures.

Physical-device QA on Xiaomi Redmi Note 11:
- all menus;
- long member names;
- payment overdue;
- payment all clear;
- no PT;
- active PT;
- no services;
- active services;
- no nutrition data;
- sparse attendance;
- rich attendance;
- expired plan;
- frozen plan;
- issue/incident;
- offers/ad slot;
- reduced motion if testable.

## 14. Verification gate

Do not claim completion until:
- git pull --ff-only succeeded;
- exact HEAD recorded;
- ./gradlew testDebugUnitTest passes;
- ./gradlew assembleDebug passes;
- lint if configured;
- physical device runtime checked;
- screenshots updated;
- exact changed-file inventory reported;
- final SHA reported.

Current GitHub Actions run 249 for 9a523402eecc6c3f8d049c397f3aa25a256af5e3 was still IN_PROGRESS when this contract was authored. Re-check before claiming CI success.

## 15. Required Antigravity report

MODEL -> PROVIDER/CONFIGURATION -> PULLED_HEAD -> FILES_FOUND -> FILES_CHANGED -> WORK -> TESTS -> DEVICE -> SCREENSHOTS -> FINAL_SHA -> BLOCKERS

Do not claim work that was not actually executed.
