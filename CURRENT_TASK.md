# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-STAGE-7.4-TEMPORAL-INTELLIGENCE-ON-DEMAND
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible Pull and Run
BRANCH: member-intelligence-v3
BASE: 07e022336f7fe560143e3703527e7ab11a673d6f
PACKETS:
- docs/reference/STAGE_7_3_MEMBER_INTELLIGENCE_REBUILD.md
- docs/reference/STAGE_7_4_TEMPORAL_INTELLIGENCE_ON_DEMAND.md

## Mission
Add professional temporal navigation and bandwidth-aware data retrieval to Member Intelligence. Owners must be able to drill from year/month/week/day down to exact event timestamps where source data supports that precision, without eagerly downloading the member's entire history.

## Non-negotiables
- Summary first; raw detail on demand.
- Do not load full member history on card open.
- Menu tap fetches only that menu's required period/data.
- Sub-menu/time-range changes fetch only the selected dataset/range.
- Long histories use cursor pagination/lazy loading.
- Repeated data uses cache/revalidation where existing architecture supports it.
- Realtime subscriptions only for genuinely live operational contexts.
- Payment, Attendance, Workout, Plan, Trainer, Services and History get temporal drill-down where their data supports it.
- Second/minute/hour resolution is a drill-down capability, not a requirement to record useless events every second.
- Attendance source must preserve FACE/CAMERA/QR/NFC/RFID/STAFF/APP/UNKNOWN when actually recorded.
- No fabricated timestamps, events, source types, analytics or predictions.
- Charts/graphs must have explicit metric, period and unit.
- Event detail must show only relevant fields.
- Do not expose biometric templates/raw camera frames in general Member Intelligence.
- Do not casually delete database columns when a UI field is removed; use deliberate deprecation/retention/migration policy.
- Optimize query count, payload bytes, duplicate requests, media bandwidth and realtime connections.
- Do not start Stage 8.

## Temporal scales
LIVE, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, QUARTER, HALF_YEAR, YEAR, CUSTOM.

Use the smallest meaningful resolution for each event type. A payment normally needs transaction timestamp; an attendance verification may legitimately retain seconds.

## Retrieval model
Initial member open:
identity + current state + triggering event + compact badges + minimal summary + top actionable insight + applicable promotion metadata.

Menu open:
fetch that menu's summary/current period.

Sub-menu or date-range change:
fetch only the requested dataset/range.

Event tap:
fetch full event detail.

Media open:
thumbnail metadata first; full media only when viewer opens it.

History scroll:
cursor-paginate next page near viewport.

## Temporal UX
Every supported menu should allow:
- current period;
- previous/next period;
- Today/Latest/Earliest;
- custom range;
- drill from aggregate -> day -> event -> detail.

Preserve menu, sub-menu, range and selected event on back navigation.

## Required menu examples
Payment: Audit / Timeline / Recurring / Dues / Forecast / Offers
Attendance: Live / Pattern / Calendar / Timing / Forecast
Workout: Today / Program / Sessions / Progress / Muscles / PRs
History: All / Attendance / Payment / Workout / Trainer / Plan / Service / Issues / Media

## Server/cache architecture
Prefer scoped repository/API contracts:
member summary, menu summary, filtered events, event detail, media thumbnails/detail, insight/evidence.
Use server-side filtering/aggregation, field projection, compression and cursor pagination where supported.
Use L0 UI state, L1 memory and optional compact local persistence; network remains source of truth.
Instrument request count, response bytes, cache hit rate, latency, duplicate queries, media bytes and realtime connection duration.

## Data lifecycle
Separate UI deprecation, API deprecation, retention and physical schema migration.
Use explicit hot/warm/cold/archive/deletion policies by data category and applicable privacy/business requirements.
Never replace audit records with summaries.

## Verification
- tests for temporal granularity, date ranges, pagination, default periods, source mapping, summary-vs-raw selection, cache/revalidation, stale state, event-to-menu/CTA mapping and temporal back navigation;
- testDebugUnitTest;
- assembleDebug;
- Xiaomi Redmi Note 11 runtime when available;
- runtime evidence for Payment month/day/detail, Attendance day/source detail, Workout week/day, History month→day→event, temporal navigation, lazy loading, no-data period, live source when real data exists.

## Execution sequence
INSPECT → DEFINE EVENT/TEMPORAL CONTRACTS → IMPLEMENT ON-DEMAND RETRIEVAL → PAGINATE/CACHE → INTEGRATE MENUS → TEST → BUILD → RUNTIME → SCREENSHOTS → DOCUMENT → COMMIT → PUSH → REPORT EXACT SHA.

FINAL COMMAND:
Pull and Run MI-STAGE-7.4-TEMPORAL-INTELLIGENCE-ON-DEMAND. Do not start Stage 8.
