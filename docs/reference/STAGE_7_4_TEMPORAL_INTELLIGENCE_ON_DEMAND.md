# BAD GYM — MI-STAGE-7.4-TEMPORAL-INTELLIGENCE-ON-DEMAND

STATUS: READY_FOR_EXECUTION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible Pull and Run
BRANCH: member-intelligence-v3
BASE_TASK: MI-STAGE-7.3-MEMBER-INTELLIGENCE-REBUILD
PURPOSE: Add professional temporal navigation, on-demand detail retrieval, bandwidth-aware data loading, event granularity, caching, aggregation and retention rules to Member Intelligence.

## 0. PRODUCT PRINCIPLE

Member Intelligence must answer:
"Show me the right information at the right time, at the right level of detail."

It must NOT mean:
"Load every record and display everything."

The system follows:

SUMMARY FIRST -> USER INTENT -> TARGETED QUERY -> DETAIL -> OPTIONAL DEEP DRILL -> CACHE -> AGGREGATE

The card should feel intelligent because it selects relevant evidence, not because it displays maximum data.

## 1. TEMPORAL NAVIGATOR

Every major menu must support a common temporal navigator when its underlying data supports it.

Levels:
- Live / Now
- Second
- Minute
- Hour
- Day
- Week
- Month
- Quarter
- 6 Months
- Year
- Custom Range

Important:
- The UI does not manufacture second-level events.
- Second-level resolution exists only when the source event actually has timestamp precision.
- Use the smallest useful resolution for the event type.
- Example: payment usually needs date/time, not every second; camera access events may legitimately use second precision.

Interaction:
- horizontal/segmented time-scale selector;
- date/range picker;
- previous/next period;
- jump to Today;
- jump to Latest Event;
- jump to Earliest Available;
- custom range;
- pinch/zoom is optional only if it remains understandable;
- scrolling through time must be virtualized/paginated.

Example:
Payment -> Month -> select September -> days with transactions -> tap day -> transactions -> tap transaction -> receipt/reference.
Workout -> Week -> days -> sessions -> exercises -> set-level details if recorded.
Attendance -> Day -> check-in/check-out events -> source -> gate/camera/QR/face.
History -> Year -> month density -> day -> event stream -> event detail.

## 2. COMMON TEMPORAL MODEL

Create a reusable temporal model:

TemporalRange:
- start
- end
- granularity
- timezone
- cursor/page
- hasMore

TemporalGranularity:
LIVE
SECOND
MINUTE
HOUR
DAY
WEEK
MONTH
QUARTER
HALF_YEAR
YEAR
CUSTOM

Every event should use:
- eventId
- memberId
- eventType
- occurredAt
- recordedAt when different
- source
- actor when known
- status
- payload/reference
- mediaReference when present

Use Instant/OffsetDateTime or the project's existing time type consistently. Never store formatted display strings as the canonical event timestamp.

## 3. RAW EVENT VS SUMMARY

Two levels:

A. Event record
The source-of-truth individual event.

B. Aggregated summary
A derived representation used for low-bandwidth overview.

Examples:

Attendance monthly summary:
- visitCount
- lateCount
- absentCount when explicitly known
- averageDuration
- peakWeekday
- peakArrivalHour

Payment monthly summary:
- paidAmount
- dueAmount
- overdueAmount
- transactionCount
- lastPaymentAt

Workout weekly summary:
- sessions
- completed
- duration
- volume when supported
- consistency

The card loads summaries first.

Raw events are loaded only when:
- user opens the relevant menu;
- user selects a period;
- user expands a day/week/month;
- user requests event detail;
- a live operational event requires it.

Never transfer a year's raw event list just to render a 30-day summary.

## 4. ON-DEMAND SERVER RETRIEVAL

Default policy:

INITIAL MEMBER OPEN:
Fetch only:
- member identity;
- current state;
- current triggering event;
- compact menu badges;
- minimal summary metrics;
- top actionable insight;
- applicable promotion metadata.

DO NOT fetch:
- full history;
- full media;
- every payment;
- every workout;
- every attendance event;
- every trainer note.

ON MENU TAP:
Fetch that menu's summary for the selected/default period.

ON SUB-MENU TAP:
Fetch the specific dataset required by that sub-menu.

ON TIME RANGE CHANGE:
Fetch only the selected range and granularity.

ON EVENT TAP:
Fetch only that event's full detail.

ON MEDIA OPEN:
Fetch thumbnail metadata first; full media only when viewer opens it.

ON SCROLL:
Use cursor-based pagination/infinite loading; fetch the next page only near the viewport threshold.

## 5. NETWORK/BANDWIDTH RULES

No continuous polling for historical menus.

Use:
- request/response;
- cursor pagination;
- conditional requests/ETag where backend supports it;
- compressed JSON;
- field selection/projection;
- date/range filtering server-side;
- server-side aggregation for summaries;
- thumbnail URLs/low-resolution previews for media;
- delta/realtime subscriptions only for genuinely live operational data.

Do not send large unused payloads.

The UI must not request:
"all member data."

It must request:
"member 204, Payment, September 2026, daily summary."

## 6. LIVE DATA

Only data that can change while the owner is looking at it should use live/realtime updates.

Potential live streams:
- current check-in/out;
- active workout session;
- current PT session;
- machine/service issue status;
- payment completion when a transaction is being processed;
- current gate/camera/QR attendance event.

Historical charts and old timelines do NOT need a permanent realtime connection.

When leaving a live menu:
- unsubscribe/stop the live stream;
- retain only the latest useful cached state.

## 7. CAMERA / FACE / QR / ATTENDANCE SOURCE INTELLIGENCE

Attendance events must preserve the actual source when available:

CHECK_IN:
- source = FACE
- source = CAMERA
- source = QR
- source = NFC/RFID
- source = STAFF
- source = MEMBER_APP
- source = UNKNOWN

Optional source metadata:
- gate/device identifier;
- verification method;
- confidence/verification result only if the existing system actually records it;
- occurredAt;
- recordedAt;
- check-in/check-out relationship;
- session duration.

UI:
"CHECK-IN • FACE • Gate 1 • 07:42:18"

or:

"CHECK-IN • QR • Reception • 07:42"

Never claim FACE/QR/CAMERA if the backend does not know the source.

For privacy/security:
- store only the minimum source metadata required for the product;
- do not expose biometric templates in Member Intelligence;
- do not place raw camera frames into general event payloads;
- media/biometric retention must follow the project's privacy/security policy.

## 8. SECOND-LEVEL EVENT VIEW

Second-level detail is an audit/drill-down capability, not the default UI.

Example:

Day:
12 Sep 2026 — 7 attendance events

Tap:
07:42–08:58

Tap:
07:42:18 CHECK_IN
Source: FACE
Gate: 1
Verification: successful

Tap:
08:58:41 CHECK_OUT
Source: FACE
Gate: 1

The user can scroll event-by-event when needed.

Do not show a second-by-second graph if there are no meaningful second-level events.

## 9. TEMPORAL VISUALIZATION

Use different visualizations based on density:

Few events:
- timeline.

Moderate events:
- day/week strips.

Many events:
- heatmap/density grid.

Financial:
- monthly bars/line + transaction list.

Attendance:
- calendar + weekday/time distribution.

Workout:
- session timeline + weekly trend.

Plan:
- lifecycle timeline.

Services:
- booking/usage timeline.

Insights:
- evidence timeline.

Never use a graph merely because space is available.

Every visualization must expose:
- metric;
- period;
- unit;
- selected value;
- accessibility summary.

## 10. TEMPORAL SCROLL UX

The owner should be able to:

1. Open Payment.
2. See current month summary.
3. Swipe/scroll month-by-month.
4. Tap September.
5. See daily transaction density.
6. Tap 18 Sep.
7. See that day's transactions.
8. Tap one transaction.
9. Open receipt/reference.
10. Back to day.
11. Back to month.
12. Back to member.

Same interaction pattern across menus.

Preserve navigation state:
- menu;
- sub-menu;
- temporal range;
- selected date;
- selected event.

Back should unwind that state naturally.

## 11. DATA DENSITY RULE

Use three presentation tiers:

TIER 1 — Decision
What the owner needs immediately.

TIER 2 — Explanation
Evidence supporting the decision.

TIER 3 — Audit
Raw records and exact timestamps.

Default card:
TIER 1.

Menu:
TIER 1 + TIER 2.

Drill-down:
TIER 3.

This prevents "information dumping."

## 12. MEMBER INTELLIGENCE SERVER QUERY CONTRACT

Do not implement a single giant member endpoint.

Prefer capability-scoped queries such as:

GET member summary
GET member attendance summary
GET member attendance events
GET member payment summary
GET member payment events
GET member workout summary
GET member workout events
GET member trainer summary
GET member trainer events
GET member service summary
GET member service events
GET member history summary
GET member history events
GET member insight summary
GET member insight evidence
GET member media thumbnails
GET member media detail

Actual API names must follow the existing backend architecture.

Every query should accept where relevant:
- memberId
- start
- end
- granularity
- cursor
- limit
- filters
- projection/fields

Server performs filtering/aggregation before transmission whenever practical.

## 13. CACHE STRATEGY

Use a layered cache:

L0 UI state:
- selected menu;
- selected sub-menu;
- selected range;
- expanded item.

L1 memory:
- current member summary;
- current menu summary;
- recently viewed period.

L2 local persistence:
- only if existing architecture supports it;
- store compact summaries and recently viewed event pages;
- do not create a giant offline mirror by default.

Network:
- source of truth.

Rules:
- cache has freshness metadata;
- show stale state honestly when necessary;
- revalidate on revisit when data can change;
- invalidate after mutations;
- do not repeatedly download identical period data.

## 14. SERVER-SIDE AGGREGATION

Heavy aggregation belongs on the server/backend where appropriate.

Examples:
- attendance by month;
- payment by month;
- workout by week;
- event counts by type;
- late-arrival distribution;
- service usage;
- insight evidence windows.

The mobile app should not download 50,000 raw events just to count 12 months.

If backend does not yet support aggregation:
- create a typed repository contract;
- implement the UI against that contract;
- do not fake the aggregates.

## 15. RETENTION / DATA LIFECYCLE

Do NOT casually delete database columns because a menu no longer displays them.

Separate:
1. UI deprecation
2. API field deprecation
3. data retention
4. physical schema migration

When a field becomes unused:
- mark deprecated;
- measure actual usage;
- migrate consumers;
- define retention period;
- archive if needed;
- only then remove from schema in a deliberate migration.

For event data:
- hot: recent operational records;
- warm: historical records used for analytics;
- cold/archive: old records retained for compliance/business reasons;
- deletion: only according to an explicit retention policy.

Retention periods must be configurable by data category and legal/privacy requirements.

## 16. SERVER COST / BANDWIDTH OPTIMIZATION

The product should optimize:
- bytes transferred;
- query count;
- duplicate requests;
- server compute;
- database scans;
- media bandwidth;
- realtime connections.

Metrics to instrument:
- request count per member detail session;
- response bytes;
- cache hit rate;
- average query latency;
- menu-open fetch count;
- repeated identical query count;
- media bytes;
- realtime connection duration;
- aggregation latency.

Use these metrics to improve the next Member Intelligence versions.

Do not optimize by deleting useful data merely because it is not currently visible.

## 17. ADAPTIVE INTELLIGENCE / FRONT-OF-CARD EVOLUTION

Future optimization loop:

RAW EVENTS
-> AGGREGATED METRICS
-> OWNER USAGE TELEMETRY
-> IDENTIFY HIGH-VALUE SIGNALS
-> IMPROVE MENU ORDER / SUMMARY
-> KEEP LOW-VALUE DETAILS BEHIND DRILL-DOWN
-> REVIEW
-> REPEAT

The system may learn which information is useful, but:
- never infer sensitive personal traits;
- never silently remove audit-critical information;
- do not replace source records with summaries;
- keep explainable rules for why something is promoted.

Owner-facing "front" content is a presentation optimization, not data deletion.

## 18. EVENT DETAIL QUALITY

Every raw event detail should show only fields relevant to that event.

Payment event:
- amount;
- currency;
- timestamp;
- method;
- reference;
- invoice;
- status.

Attendance:
- check-in/out;
- timestamp;
- source;
- gate/device if available;
- session duration.

Workout:
- program;
- session;
- exercises;
- sets/reps/load if recorded;
- duration.

Machine fault:
- machine;
- issue;
- reportedAt;
- severity;
- status;
- resolvedAt;
- responsible staff.

Do not render universal empty fields like:
"Reference: N/A, Actor: N/A, Device: N/A, Media: N/A"
for every event. Show only meaningful fields.

## 19. PROFESSIONAL EMPTY / PARTIAL STATES

Use:
- Not recorded
- No events in this period
- Data available on demand
- More history available
- Last synced ...
- Offline — showing cached data

Do NOT use:
- fake zero;
- fake percentage;
- fake prediction;
- decorative empty charts;
- dummy timeline entries.

## 20. TEMPORAL MENU EXAMPLES

PAYMENT:
[Audit] [Timeline] [Recurring] [Dues] [Forecast]

Period:
[Live] [Month] [6M] [Year] [Custom]

ATTENDANCE:
[Live] [Pattern] [Calendar] [Timing] [Forecast]

Period:
[Today] [7D] [30D] [90D] [1Y] [Custom]

WORKOUT:
[Today] [Program] [Sessions] [Progress] [Muscles] [PRs]

Period:
[Today] [Week] [Month] [3M] [1Y]

HISTORY:
[All] [Attendance] [Payment] [Workout] [Trainer] [Plan] [Service] [Issues] [Media]

Period:
[Today] [Week] [Month] [6M] [Year] [Custom]

## 21. ACCESSIBILITY

Temporal controls must announce:
- selected range;
- granularity;
- start/end date;
- number of events.

Example:
"Payment timeline. September 2026. 6 transactions. Swipe left for August."

Charts:
"Attendance, last 30 days: 23 visits, 2 late, 76 percent attendance."

## 22. PERFORMANCE ACCEPTANCE

The implementation must not:
- eagerly load all historical events;
- open realtime subscriptions for every menu;
- download full media thumbnails unnecessarily;
- recompute huge histories on the main thread;
- render thousands of Compose nodes at once.

Required:
- pagination;
- lazy lists;
- stable keys;
- server-side filtering;
- summaries before raw events;
- cache/revalidation;
- lifecycle-aware realtime;
- background parsing for heavy payloads where needed.

## 23. TESTS

Add tests for:
- temporal granularity mapping;
- date range generation;
- cursor pagination;
- default period selection;
- event source mapping;
- summary vs raw query selection;
- cache hit/revalidation;
- stale data state;
- event-to-menu mapping;
- event-to-CTA mapping;
- temporal back navigation;
- second-level event rendering;
- no fabricated values for empty periods;
- retention/deprecation policy objects if introduced.

Run:
- testDebugUnitTest
- assembleDebug
- runtime on Xiaomi Redmi Note 11 when available.

## 24. REQUIRED RUNTIME EVIDENCE

Screenshots/video where practical:

1. Payment Month
2. Payment Day
3. Payment Transaction Detail
4. Attendance Day
5. Attendance second-level source detail
6. Attendance 30-day pattern
7. Workout Week
8. Workout Day
9. History Month -> Day -> Event
10. Temporal navigator across multiple periods
11. On-demand loading state
12. Cached/stale state
13. Long history lazy scrolling
14. No-data period
15. Live attendance source (FACE/QR/CAMERA) when real data exists
16. All menus with temporal control where supported

## 25. IMPLEMENTATION ORDER

L0: inspect existing repository/backend contracts.
L1: define reusable temporal/event/query models.
L2: define summary vs raw repository APIs.
L3: implement temporal navigator.
L4: implement server-filtered/on-demand retrieval.
L5: implement cursor pagination and lazy lists.
L6: implement cache/revalidation using existing architecture.
L7: integrate Attendance source metadata.
L8: integrate Payment temporal navigation.
L9: integrate Workout/Trainer/Plan temporal navigation.
L10: integrate Supplements/Nutrition/Services temporal navigation.
L11: integrate universal History timeline.
L12: integrate Insight evidence windows.
L13: integrate media lazy loading.
L14: add performance instrumentation.
L15: add tests.
L16: build/runtime/screenshots.
Do not invent backend data if a required capability is not currently available.

## 26. CRITICAL PRODUCT RULE

"Every second" is an available drill-down resolution, NOT a mandate to record every second.

Do not create:
- one database row every second;
- continuous polling;
- continuous camera frames;
- useless heartbeat events.

Store meaningful events at the source's natural resolution.

A payment transaction is one event.
A check-in is one event.
A workout set is one event when recorded.
A live machine state may use state transitions.
A camera/face verification is an attendance event with a timestamp.

This is how BAD GYM remains professional, scalable and bandwidth-conscious.

## 27. DEFINITION OF DONE

7.4 is complete when:
- owner can navigate historical data by appropriate time scale;
- raw event detail is available on demand;
- summaries load before raw history;
- menu taps trigger targeted server queries;
- long histories paginate;
- repeated data is cached/revalidated;
- realtime is limited to live operational contexts;
- attendance source (FACE/QR/CAMERA/etc.) is preserved when available;
- second-level detail is possible where source precision exists;
- no fake timestamps or fake events exist;
- server bandwidth is not wasted by eager full-history loading;
- schema/data retention is treated separately from UI visibility;
- all temporal controls are accessible and understandable;
- tests/build/runtime evidence pass.

FINAL COMMAND:
Pull and Run MI-STAGE-7.4-TEMPORAL-INTELLIGENCE-ON-DEMAND.
Inspect existing backend/repository contracts first. Implement only supported capabilities; create typed contracts for future backend work without inventing data. Do not start Stage 8.
