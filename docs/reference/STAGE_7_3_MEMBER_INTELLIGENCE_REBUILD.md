# BAD GYM — MI-STAGE-7.3-MEMBER-INTELLIGENCE-REBUILD

STATUS: READY_FOR_EXECUTION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible Pull and Run
BRANCH: member-intelligence-v3
BASE: 85e3e8ceef449d8725b01cad51f3676a38b46727
PURPOSE: Replace the current Member Intelligence prototype hierarchy with a real owner-facing intelligence cockpit.

## 0. WHY 7.3 EXISTS

The Redmi runtime screenshots show that Stage 7.2 is visually coherent but conceptually wrong for the requested product.

Observed problems:
- the card is still a compact poster rather than an intelligence workspace;
- the dark outer canvas and dark theme personalities contradict the latest light-only direction;
- the member header consumes space while important information is clipped;
- the Silver Plan / PT Active bands are visually prominent but are not behaving as a contextual sub-navigation layer;
- menu panels contain isolated summaries instead of past/present/future intelligence;
- several values are hard-coded/demo-like and are not a trustworthy domain representation;
- Payment, Attendance, Trainer, Workout, History, Insight and More do not expose enough drill-down structure;
- there is no unified activity/event model for the many kinds of gym events;
- there is no persistent default-menu policy;
- there is no clear distinction between operational state, historical evidence, forecast, AI interpretation and owner action;
- there is no member media/history wall;
- charts are too small and some visual encodings have no explicit business meaning;
- long member names are not handled robustly;
- content is forced into a fixed-height card instead of using adaptive scrolling;
- the current file contains prototype SampleMembers and hard-coded business values. Production UI must not manufacture facts.

7.3 is therefore a REBUILD OF INFORMATION ARCHITECTURE + VISUAL SYSTEM + DATA PRESENTATION, not a recolor.

## 1. PRODUCT LAW

Member Intelligence means:

EVENT -> MEMBER -> TIME -> STATE -> EVIDENCE -> PATTERN -> FORECAST -> DECISION -> ACTION

For every visible intelligence block, the owner must be able to answer:
1. What happened?
2. When?
3. What is the current state?
4. What evidence supports it?
5. Is it a past pattern, current condition, or future prediction?
6. What should the owner do next?
7. Can the owner open the underlying records?

No chart, badge, color, percentage or AI statement may exist without a metric/period/state definition.

## 2. SHELL REBUILD

Do not keep the current fixed-height 320dp-ish poster shell.

Use an adaptive bounded detail workspace:
- phone content width: fill available width with 12-16dp outer margin;
- preferred card width: 360-412dp;
- minimum usable content width: 320dp;
- height: WRAP_CONTENT inside a vertical LazyColumn; never clip content to a fixed maximum height;
- rail remains persistent while detail is open;
- content pane scrolls independently;
- header remains sticky within the detail shell;
- top event title is LARGE and explicit: e.g. "PAYMENT AUDIT", "CHECK-IN / CHECK-OUT", "PT SESSIONS";
- secondary member identity remains visible but compact;
- long names use 2-line layout with no ellipsis unless there is genuinely no room;
- member name can wrap to two lines;
- event title never competes with the member name;
- no bottom content is hidden behind a fixed footer;
- CTAs are inside the relevant content section and/or one contextual sticky action slot.

Compose implementation direction:
- use LazyColumn/LazyRow for long histories and media;
- use stable keys/contentType for repeated event rows;
- use adaptive width reasoning rather than fixed poster dimensions;
- preserve the existing navigation boundary and domain/repository boundary;
- do not add a new navigation framework merely for this redesign.

## 3. NEW DETAIL HEADER

Header order:

A. EVENT STRIP
- large event title;
- event subtype;
- event timestamp;
- live state;
- source: Gate 1 / QR / Staff / App / Trainer / System when available.

Examples:
- CHECK-IN
- LATE CHECK-IN
- CHECK-OUT
- PAYMENT AUDIT
- PT SESSION
- WORKOUT
- PLAN RENEWAL
- SERVICE BOOKING
- MACHINE FAULT

B. MEMBER IDENTITY
- real member photo;
- full name, 1-2 lines;
- member ID;
- membership state;
- current tier/plan;
- optional verification badge.

C. CONTEXT CHIPS
The current "Silver Plan" and "PT Active" blocks become tappable context chips/cards:
- Membership
- PT
- Payment
- Current Service
Only show chips whose data actually exists.
Each opens the corresponding menu or sub-menu.
No decorative duplicate status.

D. INTELLIGENCE PRIORITY
A single compact priority strip:
- CRITICAL
- ACTION DUE
- WATCH
- STABLE
or "No action required".
This is derived from actual state and evidence.

## 4. RAIL REBUILD

Rail is an index, not a decorative icon column.

Each item:
ICON + LABEL + MEANINGFUL BADGE

Primary menus:
1 Home
2 Attendance
3 Plan
4 Payment
5 Trainer
6 Workout
7 Supplements
8 Nutrition
9 Services
10 History
11 Insight
12 More

Badge examples:
- Attendance: "12/18", "2 late", "3d streak"
- Plan: "28d", "Renew 7d"
- Payment: "₹0", "₹500 due", "3d overdue"
- Trainer: "2 left", "Next 4:16"
- Workout: "3/5", "Due"
- Supplements: "2 low"
- Nutrition: "77%"
- Services: "1 expiring"
- History: "24 events"
- Insight: "2 actions"
- More: no badge unless a real notification count exists.

Zero-state:
- no random dot;
- no meaningless badge;
- never hide a label behind a number.

Default active menu:
- When detail is opened from an event type, open the matching menu.
- Payment event -> Payment active.
- Late check-in -> Attendance active.
- PT event -> Trainer active.
- Workout event -> Workout active.
- Machine/service issue -> Services or relevant operational record.
- General member open -> Home active.
- After a menu interaction, preserve selected menu for the current session.
- On a fresh member detail open, use event-driven default.
- If user explicitly chooses another menu, keep it highlighted.
- If a configurable auto-return timeout is implemented, default back to the triggering event menu only after leaving/reopening detail, never during active reading.

## 5. SUB-MENU MODEL

Every primary menu may contain a secondary tab row when useful.

Rules:
- sub-menu labels are short;
- each tab must reveal materially different information;
- selected tab is obvious;
- sub-tabs can be horizontally scrollable;
- do not create tabs just to distribute content.

### HOME
Sub-menus:
- Overview
- Today
- Risk
- Opportunities

Overview:
- current event;
- membership state;
- 3 unique owner KPIs only;
- current trend;
- active services/PT;
- top intelligence signal.

Today:
- check-in/out;
- PT/workout/session;
- payments;
- service events;
- staff notes;
- live status.

Risk:
- overdue;
- attendance decline;
- plan expiry;
- missed PT;
- supplement low stock;
- unresolved issue.

Opportunities:
- renewal;
- upgrade;
- PT upsell;
- service upsell;
- referral/reward;
- seasonal/repeat-pattern opportunity.

### ATTENDANCE
Sub-menus:
- Live
- Pattern
- Calendar
- Timing
- Forecast

Live:
- current check-in/check-out;
- late/early flag;
- gate/source;
- session duration when available.

Pattern:
- 7d / 30d / 90d / 12m;
- visits/week;
- active weeks;
- streaks;
- missed weeks;
- attendance rate;
- weekday distribution.

Calendar:
- month calendar with present/late/absent/holiday/unknown;
- tap day -> event list.

Timing:
- typical arrival time;
- latest/earliest;
- peak weekday;
- variance;
- late-entry pattern.

Forecast:
- upcoming likely visit window;
- risk of attendance drop;
- confidence;
- evidence period;
- never state unsupported predictions as facts.

### PLAN
Sub-menus:
- Current
- Timeline
- Benefits
- Renewals
- History
- Forecast

Current:
- plan;
- tier;
- start/end;
- days elapsed/remaining;
- status.

Timeline:
- all plan changes;
- upgrades/downgrades;
- freezes;
- extensions;
- cancellations;
- reactivations.

Benefits:
- actual entitlements;
- usage where supported.

Renewals:
- previous renewal dates;
- time-to-renew;
- historical renewal interval;
- current renewal action.

History:
- every historical plan state with dates.

Forecast:
- seasonal renewal pattern;
- expected renewal window only if enough historical evidence;
- explain evidence.

Example:
"Historically renewed in Oct-Dec in 3 of 4 recorded years" is acceptable if actual records support it.
"Will renew in December" is not acceptable without qualification.

### PAYMENT
Sub-menus:
- Audit
- Timeline
- Recurring
- Dues
- Forecast
- Offers

Audit:
- Paid total;
- Due total;
- Overdue total;
- credits/refunds;
- current balance;
- payment status.

Timeline:
- date;
- amount;
- method;
- reference;
- invoice/receipt;
- status.

Recurring:
- recurring plan/payment schedule;
- average interval;
- missed payment pattern.

Dues:
- overdue age;
- due date;
- split by invoice/charge;
- collection state;
- promised payment note if recorded.

Forecast:
- historical payment timing;
- likely upcoming due window;
- confidence/evidence.

Offers:
- only actual promotions applicable to member;
- clearly separated from dues;
- no false urgency.

Default Payment CTA:
- overdue -> "Collect ₹X"
- due -> "Collect ₹X"
- clean -> "View Receipt / Payment History"
Never show "Collect" when balance is zero.

### TRAINER
Sub-menus:
- Coach
- Sessions
- Schedule
- Performance
- Notes
- Media

Coach:
- trainer identity;
- specialties;
- assignment dates.

Sessions:
- purchased;
- used;
- remaining;
- completed/missed/cancelled/no-show;
- next/recent.

Schedule:
- future sessions;
- times;
- location;
- status.

Performance:
- session completion;
- attendance;
- training focus history.

Notes:
- actual coach notes only;
- dated;
- author.

Media:
- member/trainer workout media only if stored and supported.

CTA:
- "View next session" or "Book/Reschedule" only when supported.

### WORKOUT
Sub-menus:
- Today
- Program
- Sessions
- Progress
- Muscles
- PRs
- Media

Today:
- current workout;
- completion;
- exercises;
- duration.

Program:
- program name;
- current week;
- phase;
- next session.

Sessions:
- chronological workout history.

Progress:
- duration;
- volume;
- reps/sets;
- load;
- consistency;
- actual metrics only.

Muscles:
- coverage over 7/14/30d;
- undertrained/overrepresented areas;
- explain colors.

PRs:
- personal records if available.

Media:
- workout photos/videos if actually stored.

### SUPPLEMENTS
Sub-menus:
- Stack
- Inventory
- Usage
- Orders
- Expiry
- Recommendations

Stack:
- exact product name;
- image;
- pack size;
- unit;
- dosage if stored;
- active/inactive.

Inventory:
- remaining quantity;
- percentage;
- estimated days left only when supported by actual usage.

Usage:
- intake log;
- adherence;
- missed doses if tracked.

Orders:
- order history;
- supplier;
- cost;
- delivery status.

Expiry:
- expiry date;
- lot/batch if stored.

Recommendations:
- reorder only;
- no medical claims;
- recommendation must be tied to actual inventory/usage.

### NUTRITION
Sub-menus:
- Today
- Macros
- Meals
- Hydration
- Trends
- Plan
- Media

Today:
- consumed vs target.

Macros:
- calories;
- protein;
- carbs;
- fat;
- target and variance.

Meals:
- breakfast/lunch/dinner/snacks;
- timestamps;
- logged/not logged.

Hydration:
- consumed/target;
- trend.

Trends:
- 7/30/90d adherence.

Plan:
- active nutrition plan;
- start/end;
- coach/dietitian if stored.

Media:
- meal photos when supported.

### SERVICES
Sub-menus:
- Active
- Bookings
- Usage
- Expiry
- Issues
- Media

Services:
- locker;
- sauna;
- steam;
- recovery;
- classes;
- equipment/access;
- any real gym service.

Bookings:
- upcoming/past;
- status.

Usage:
- used/remaining.

Expiry:
- service end date.

Issues:
- member-reported or staff-recorded service issues;
- opened/fixed;
- severity;
- timestamps;
- responsible staff.

Media:
- service evidence/photos if supported.

### HISTORY
Sub-menus:
- All
- Check-in/out
- Workout
- Payment
- Trainer
- Plan
- Service
- Issues
- Media

This is the universal event ledger.

Every event:
- event icon;
- type;
- timestamp;
- status;
- value;
- actor/source;
- optional photo/video;
- tap -> full event detail.

### INSIGHT
Sub-menus:
- Action
- Pattern
- Risk
- Opportunity
- Forecast
- Explain

Action:
- P0/P1/P2 only when actionable.

Pattern:
- evidence-backed recurring patterns.

Risk:
- payment;
- attendance;
- renewal;
- service;
- trainer;
- workout;
- nutrition.

Opportunity:
- renewal;
- upgrade;
- PT;
- services;
- loyalty;
- seasonal outreach.

Forecast:
- prediction + confidence + evidence period + limitations.

Explain:
- "Why am I seeing this?"
- list source metrics and records.

No AI statement may be presented as raw truth.
Use labels:
- Observed
- Calculated
- Pattern
- Forecast
- Suggested action

### MORE
Sub-menus:
- Profile
- Documents
- Health/Body Comp
- Achievements
- Rewards
- Announcements
- Offers
- Settings
- Support

Only show destinations that exist in the domain.
Unsupported screens must not be fake clickable placeholders.

## 6. EVENT TAXONOMY — RAW ACTIVITY SYSTEM

Create a typed event model rather than scattering hard-coded strings.

Minimum event types:

Attendance:
- CHECK_IN
- LATE_CHECK_IN
- EARLY_CHECK_IN
- CHECK_OUT
- LONG_SESSION
- ABSENT
- STREAK_STARTED
- STREAK_BROKEN

Membership:
- PLAN_STARTED
- PLAN_RENEWED
- PLAN_UPGRADED
- PLAN_DOWNGRADED
- PLAN_FROZEN
- PLAN_UNFROZEN
- PLAN_EXPIRED
- PLAN_REACTIVATED
- PLAN_CANCELLED

Payment:
- PAYMENT_RECEIVED
- PAYMENT_DUE
- PAYMENT_OVERDUE
- PAYMENT_FAILED
- PAYMENT_REFUND
- CREDIT_APPLIED
- INVOICE_CREATED

Trainer:
- PT_PURCHASED
- PT_SESSION_BOOKED
- PT_SESSION_COMPLETED
- PT_SESSION_MISSED
- PT_SESSION_CANCELLED
- TRAINER_ASSIGNED
- TRAINER_CHANGED
- TRAINER_NOTE

Workout:
- WORKOUT_STARTED
- WORKOUT_COMPLETED
- WORKOUT_SKIPPED
- PR_RECORDED
- PROGRAM_STARTED
- PROGRAM_CHANGED

Nutrition:
- MEAL_LOGGED
- MEAL_MISSED
- WATER_LOGGED
- NUTRITION_PLAN_CHANGED
- BODY_COMP_RECORDED

Supplement:
- SUPPLEMENT_ADDED
- SUPPLEMENT_USED
- SUPPLEMENT_LOW
- SUPPLEMENT_REORDERED
- SUPPLEMENT_EXPIRED

Services:
- SERVICE_ACTIVATED
- SERVICE_BOOKED
- SERVICE_USED
- SERVICE_EXPIRED
- SERVICE_ISSUE_OPENED
- SERVICE_ISSUE_FIXED

Gym operations:
- MACHINE_FAULT
- MACHINE_REPORTED
- MACHINE_FIXED
- CLEANING_COMPLETED
- MAINTENANCE_COMPLETED
- ACCESS_ISSUE
- MEMBER_FEEDBACK
- STAFF_NOTE
- ANNOUNCEMENT_SENT
- OFFER_SHOWN
- OFFER_ACCEPTED
- OFFER_DISMISSED

Media:
- MEMBER_PHOTO
- MEMBER_VIDEO
- WORKOUT_PHOTO
- WORKOUT_VIDEO
- PROGRESS_PHOTO
- DOCUMENT_ADDED

The UI must only render event types for which the backend/domain has records.

## 7. PAST / PRESENT / FUTURE MODEL

Every major menu gets three temporal lanes:

PAST:
historical evidence and trends.

PRESENT:
current state and today's events.

FUTURE:
scheduled events, due dates, expected windows, forecasts.

Example Attendance:
PAST: 12/18 this month, 30/90d trend, weekday pattern.
PRESENT: checked in 4:03 PM, late by 23 min.
FUTURE: next likely attendance window, only if calculated from history.

Example Plan:
PAST: previous plans and renewals.
PRESENT: Silver 6-month plan, 28 days left.
FUTURE: expiry/renewal window and historical renewal season.

Example Payment:
PAST: transactions.
PRESENT: balance/due/overdue.
FUTURE: next due date and recurring schedule.

## 8. ANALYTICS LAYER

Use a single analytics vocabulary:

Observed:
directly stored event/record.

Calculated:
derived from records, e.g. attendance rate.

Pattern:
repeated behavior over a defined period.

Forecast:
future estimate based on historical evidence.

Recommendation:
action proposed to owner.

Each chart must display:
- metric;
- period;
- unit;
- legend when needed;
- selected point details;
- accessible content description.

Charts to use only when they answer a real question:
- attendance weekday heat strip;
- 7/30/90d trend;
- arrival-time distribution;
- payment timeline;
- plan lifecycle timeline;
- PT session completion;
- workout volume trend;
- muscle coverage;
- supplement depletion;
- nutrition macro bars;
- service usage;
- universal activity timeline;
- insight evidence chain.

Do not repeat the same chart in Home and its detail menu.

## 9. AI INTELLIGENCE

AI is a layer on top of evidence, not a replacement for data.

Every insight object should carry:
- priority;
- title;
- statement;
- evidence metrics;
- evidence period;
- source event IDs when available;
- confidence;
- action;
- explanation.

Example:
P1 — Renewal opportunity
Evidence: member renewed in Oct 2024, Nov 2025; current plan expires 23 Oct 2026.
Pattern: renewal usually occurs within 14 days of expiry.
Action: prepare renewal outreach.
Confidence: derived from 2 prior renewals.
This must be generated from actual history, not hard-coded.

No fake "AI" prose such as "excellent renewal candidate" unless there is an explainable rule behind it.

## 10. MEMBER MEDIA WALL

Add a real media concept to the data/UI contract:
- profile photo;
- progress photos;
- workout photos/videos;
- meal photos;
- trainer media;
- documents remain separate from social media.

UI:
- compact horizontal media rail;
- tap -> full viewer;
- date/type/author;
- optional caption;
- add media only through a real repository/storage flow;
- do not invent upload functionality if storage is not connected.

"Instagram-like" means visual timeline interaction, not copying Instagram branding.

## 11. PROMOTION / ADVERTISEMENT

Promotions are isolated from operational data.

Promotion card:
- PROMOTION / MEMBER OFFER label;
- image/product/service;
- offer title;
- actual price/discount if stored;
- expiry;
- CTA;
- dismiss.

Placement:
- after primary intelligence/action section;
- never between Paid/Due/Overdue values;
- never use alert colors to make ads look operational;
- never hide critical member data.

Only render a promotion when a real offer exists.

## 12. CTA ENGINE

Exactly one primary contextual CTA per menu.

Rules:
Payment:
- overdue/due -> Collect
- clean -> View receipt/history

Attendance:
- currently checked in -> View session / Check-out only if real action exists
- late -> View late history / notify workflow if supported

Plan:
- expiring -> Renew
- active -> View benefits

Trainer:
- upcoming -> View session
- remaining zero -> Buy/Assign only if real flow exists

Workout:
- scheduled -> Start workout
- completed -> View session

Supplements:
- low -> Reorder
- adequate -> View stack

Nutrition:
- incomplete today -> Log meal
- complete -> View day

Services:
- upcoming booking -> View booking
- issue -> Resolve/Track issue

History:
- event selected -> Open event

Insight:
- P0/P1 -> Review action

Home:
- highest priority action from the above, otherwise View member.

## 13. VISUAL SYSTEM — REPLACE THE CURRENT 8 THEME PERSONALITIES

The current screenshots show theme treatment competing with intelligence. 7.3 uses one LIGHT operational base with restrained personalities.

Base:
- page: warm white / very light ivory;
- card: white;
- primary text: deep neutral;
- secondary text: muted neutral;
- borders: low-contrast neutral;
- semantic colors independent of theme.

Eight personalities:
1 Fresh Mint — mint/emerald
2 Coral Bloom — coral/pink
3 Sunlit Gold — champagne/yellow
4 Aqua Air — sky/aqua
5 Soft Lavender — lavender/purple
6 Peach Cream — peach
7 Rose Quartz — rose
8 Premium Ivory — warm ivory/bronze

No black/dark-black background.
No full-card neon gradients.
No dark theme variants.
No accent-colored body paragraphs.
No repeated colored blocks merely for decoration.

Semantic:
- critical = red;
- warning = amber;
- success = green;
- info = blue/cyan;
- insight = purple;
- neutral = slate/graphite.

## 14. CARD GEOMETRY

The current screenshots clip meaningful content because the card is too short.

New geometry:
- outer detail card: min height based on content, not fixed max;
- internal content: LazyColumn;
- section spacing: 12/16dp;
- primary numbers: 20-28sp;
- section title: 15-18sp;
- body: 12-14sp;
- metadata: 10-12sp;
- rail label: minimum readable size, never 7sp;
- touch targets: at least 44dp where practical;
- long names: 2 lines;
- long values: wrap or horizontal scroll, never silent ellipsis;
- important data may move below fold rather than be clipped;
- user can scroll to see complete record.

## 15. HOME INFORMATION BUDGET

Home must not become a 40-metric dashboard.

Home only:
- identity;
- current event;
- membership state;
- 3 unique KPIs;
- one trend;
- one priority insight;
- one CTA;
- one compact promotion if applicable.

Everything else belongs in detail menus.

This is the opposite of "everything everywhere": the whole member record is accessible through the card, while Home remains a decision cockpit.

## 16. MENU-SPECIFIC COLOR LANGUAGE

Each menu has a restrained accent, but the data semantics win:

Attendance = teal/green
Plan = violet/indigo
Payment = emerald for clear, amber/red for due/overdue
Trainer = purple
Workout = aqua/teal
Supplements = lime/green
Nutrition = orange/peach
Services = sky
History = slate
Insight = purple
More = neutral mint

These are accents, not backgrounds for all content.

## 17. NO FAKE DATA

Delete or isolate:
- SampleMembers as production source;
- hard-coded payment amounts;
- hard-coded dates;
- hard-coded trainer/session counts;
- hard-coded workout calories/volume;
- hard-coded supplement stock;
- hard-coded nutrition plans;
- hard-coded service inventory;
- hard-coded AI claims.

If a domain field does not exist:
- omit it;
- show "Not recorded";
- or add a clearly typed unavailable state.

Never substitute a believable fake number.

## 18. IMPLEMENTATION STRUCTURE

Prefer splitting the current monolithic MemberIntelligenceScreen.kt.

Suggested production modules:
- MemberIntelligenceScreen.kt
- MemberIntelligenceState.kt
- MemberIntelligenceEvent.kt
- MemberIntelligenceAnalytics.kt
- MemberIntelligenceTheme.kt
- MemberIntelligenceRail.kt
- MemberIntelligenceHeader.kt
- MemberIntelligenceHome.kt
- MemberIntelligenceAttendance.kt
- MemberIntelligencePlan.kt
- MemberIntelligencePayment.kt
- MemberIntelligenceTrainer.kt
- MemberIntelligenceWorkout.kt
- MemberIntelligenceSupplements.kt
- MemberIntelligenceNutrition.kt
- MemberIntelligenceServices.kt
- MemberIntelligenceHistory.kt
- MemberIntelligenceInsight.kt
- MemberIntelligenceMore.kt
- MemberIntelligenceMedia.kt
- MemberIntelligenceCharts.kt

Do not create these files if the existing architecture already has equivalent modules; reuse existing boundaries.

## 19. ASSETS

Use:
- actual member images from existing asset repository;
- product/service images already present;
- small transparent SVG/PNG assets;
- Compose icons for common semantic events;
- vector/Canvas for charts;
- no giant bitmap background UI.

Need new icons for event categories only if Material icons do not communicate meaning clearly.

## 20. INTERACTION

Required:
- rail tap -> menu;
- sub-menu tap -> subsection;
- context chip -> corresponding menu;
- event row -> event detail;
- chart point -> metric detail;
- insight -> evidence;
- CTA -> real action;
- media -> viewer;
- back -> previous menu/detail state;
- collapse -> browse;
- long name -> full readable name without truncation where practical.

No decorative click targets.

## 21. MOTION

Keep Stage 7 depth/motion but subordinate it to information.

- menu transition: 180-250ms;
- sub-menu transition: 150-220ms;
- chart reveal: 180-260ms;
- media rail: 180-260ms;
- CTA state change: 120-180ms;
- no infinite animation;
- no heavy parallax;
- reduced-motion must remove nonessential transforms;
- do not animate large lists on every recomposition.

## 22. PERFORMANCE

Because the record can become large:
- LazyColumn for event/history;
- LazyRow for submenus/media;
- stable keys;
- contentType for heterogeneous event rows;
- remember derived analytics;
- no per-frame allocation in graphicsLayer;
- load media thumbnails, not full-resolution images in lists;
- do not eagerly compose the entire history;
- use Paging only if repository scale requires it.

## 23. ACCESSIBILITY

Every semantic visual:
- chart has summary;
- badge has meaning;
- color is never the only signal;
- event type is spoken;
- priority is spoken;
- CTA is explicit;
- member name is fully exposed;
- payment state includes amount + status;
- attendance includes period + rate.

Example:
"Attendance, 30 days: 78 percent, 23 visits, 2 late check-ins."

## 24. RESPONSIVE TEST MATRIX

Verify at:
- 320dp
- 360dp
- 375dp
- 390dp
- 412dp

Also:
- long member names;
- long plan names;
- large rupee amounts;
- zero data;
- many events;
- no photo;
- multiple photos;
- missing trainer;
- no PT;
- overdue;
- expired plan;
- multiple services;
- low supplement;
- incomplete nutrition;
- P0/P1/P2 insights.

## 25. REQUIRED SCREENSHOTS

Produce runtime evidence for:
1 Home
2 Attendance / Pattern
3 Plan / Timeline
4 Payment / Audit
5 Trainer / Sessions
6 Workout / Progress
7 Supplements / Inventory
8 Nutrition / Macros
9 Services / Issues
10 History / All
11 Insight / Explain
12 More / Profile tools
13 Payment default-open from payment event
14 Late check-in default-open from attendance event
15 long member name
16 media wall
17 promotion isolated
18 all-menu matrix
19 all-8-light-personality matrix
20 320/360/390/412 width evidence where practical

## 26. TESTS

Add unit tests for:
- menu selection;
- event-to-default-menu mapping;
- badge calculation;
- CTA calculation;
- payment state;
- attendance state;
- plan expiry state;
- insight priority;
- long-name formatting;
- temporal grouping;
- forecast evidence requirement;
- "no fake data" / unavailable state where practical.

Run:
- testDebugUnitTest
- assembleDebug
- relevant lint/static checks
- device runtime on Xiaomi Redmi Note 11 when available.

## 27. EXECUTION ORDER

L0: inspect current architecture/domain/repository.
L1: remove fixed-height clipping and dark outer user-facing shell.
L2: establish light operational theme tokens.
L3: rebuild header and long-name behavior.
L4: rebuild rail and default-menu policy.
L5: create typed event/temporal/intelligence presentation model.
L6: implement Home.
L7: implement Payment + Attendance first because event-driven default behavior depends on them.
L8: implement Plan + Trainer + Workout.
L9: implement Supplements + Nutrition + Services.
L10: implement History + Insight + More.
L11: implement media.
L12: implement promotion isolation.
L13: implement contextual CTA engine.
L14: motion/accessibility/performance.
L15: all light personalities.
L16: test/build/runtime/screenshots.
Do not move to the next layer when the previous layer clips, lies, or has fake data.

## 28. IMPORTANT: WHAT NOT TO DO

Do NOT:
- simply recolor the existing screen;
- preserve the current 7-9sp typography;
- preserve fixed max-height card clipping;
- add more boxes to the same cramped card;
- add fake charts;
- add fake AI;
- add fake future predictions;
- invent 2026 dates/transactions;
- keep SampleMembers as production data;
- add dark theme variants;
- use huge gradients behind operational data;
- hide data because it does not fit;
- create 40 tabs with no semantic difference;
- create separate duplicate metrics in every menu;
- make every card colorful;
- use ads as alerts;
- add unsupported upload/edit actions.

## 29. ANTIGRAVITY LOAD-SHEDDING

ChatGPT owns:
- information architecture;
- UX;
- visual system;
- event taxonomy;
- analytics semantics;
- CTA rules;
- acceptance matrix;
- task packet;
- post-run review.

Antigravity owns:
- repository inspection;
- Kotlin/Compose implementation;
- tests;
- Gradle build;
- device installation;
- screenshots;
- commit/push.

The user should only need:
1. Pull and Run.
2. Review runtime screenshots.
3. Report visual defects.

Do not ask Antigravity to invent the product design. This packet is the design/behavior contract.

## 30. DEFINITION OF DONE

7.3 is complete only when:
- the member card behaves like an intelligence workspace;
- every primary menu has useful past/present/future information;
- every relevant menu has meaningful sub-menus;
- the rail badge tells the truth;
- opening from an event selects the correct menu;
- Payment remains Payment by default when opened from payment context;
- all critical information is readable and scrollable;
- long names are safe;
- charts have explicit meaning;
- AI is evidence-backed;
- media is real or clearly unavailable;
- promotions are isolated;
- CTAs are state-derived;
- no fake demo values remain in the production path;
- all eight visual personalities are light;
- tests/build/runtime pass;
- screenshots prove the result;
- exact commit SHA is documented.

## 31. REFERENCE BASIS

The adaptive layout direction follows Android's current Compose guidance: layouts should adapt to available width and use containment/panes rather than forcing components into fixed dimensions; LazyColumn/LazyRow are appropriate for large or unknown-length collections. See:
https://developer.android.com/develop/ui/compose/build-adaptive-apps
https://developer.android.com/develop/ui/compose/lists

FINAL COMMAND:
Pull and Run MI-STAGE-7.3-MEMBER-INTELLIGENCE-REBUILD.
Inspect first. Implement only against this packet. Do not start Stage 8.
