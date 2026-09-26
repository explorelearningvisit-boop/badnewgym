# BAD GYM — Member Intelligence UX V2 / Dense Decision Surface Blueprint

## Objective
Redesign the canonical Member Intelligence card so every menu is a useful, dense, decision-oriented workspace while preserving the single-card architecture and dual-side rails.

## Core UX law
Every menu must preserve a compact member identity context, then use the remaining viewport for menu-specific intelligence.

### Persistent identity context
HOME:
- Large portrait
- Large member name
- member code
- membership/tier
- current event + time
- primary signal
- primary CTA

NON-HOME:
- Compact persistent member strip at the top
- Small portrait
- member name + code
- tier/status chip
- current event/time
- one compact attention badge when relevant
- never hide the identity context when switching menus

The identity strip is persistent; menu content changes below it.

## Space law
- No giant empty areas.
- No unnecessary vertical scrolling.
- Prefer a single viewport composition.
- Use vertical scrolling only when content genuinely exceeds the available viewport.
- Do not force every menu to use the same card layout.
- Each menu gets its own information architecture.
- Use horizontal segmented controls only when they materially improve navigation.
- Charts must answer an operational question; decorative charts are prohibited.

## Default menu intelligence
When opening a member, choose the most operationally important available menu:
1. critical access/problem/payment issue
2. overdue/due payment
3. expired/expiring membership
4. scheduled/active trainer session
5. active workout event / workout attention
6. unresolved service/issue
7. meaningful promotion
8. otherwise HOME

HOME must still summarize the selected menu's reason for attention. Selecting another menu must not lose the identity context.

## Menu UX matrix

### HOME — Member cockpit
Question: "What matters about this member right now?"
- large identity
- 3–4 KPI tiles
- attendance ring + 7-day trend
- current membership/payment state
- active trainer/service state
- top intelligence signal
- one contextual CTA
- small evidence shortcuts

### ATTENDANCE
Question: "Is this member coming consistently?"
- 30-day attendance rate
- visits / target
- 7-day consistency strip
- weekly trend bars
- peak visit timing
- streak
- last check-in / last check-out
- late/early pattern if recorded
- sub-tabs: Pattern / Calendar / Timing / Streak
- CTA only when recovery/action is justified

### PLAN
Question: "What access does this membership currently provide?"
- plan name
- start/end date
- days remaining
- renewal/expiry state
- entitlement chips
- plan utilization
- freeze history summary
- renewal window
- upgrade/value opportunity only when supported
- sub-tabs: Overview / Access / Timeline

### PAYMENT
Question: "What money is due, paid, or at risk?"
- outstanding amount
- due/overdue state
- lifetime paid
- last transaction
- next due date
- payment timeline
- monthly/periodic paid-vs-due bars when data supports it
- transaction count
- failed/partial/refund markers
- sub-tabs: Timeline / Recurring / Audit
- overdue should visually dominate without hiding member context

### TRAINER / PT
Question: "What coaching activity is scheduled or at risk?"
- trainer name
- sessions completed / total / remaining
- next session
- attendance to PT sessions
- completion trend
- missed/cancelled sessions
- upcoming session timeline
- sub-tabs: Sessions / Coach / Progress
- CTA: schedule/reschedule/follow-up based on actual state

### WORKOUT
Question: "What is the member actually training?"
- current routine
- latest workout
- sessions/week
- workout duration
- workout consistency trend
- muscle/focus distribution when recorded
- PR/milestone evidence when recorded
- missed workout gap
- sub-tabs: Overview / Progress / Sessions
- never invent workout metrics

### SUPPLEMENTS
Question: "What is the member consuming/purchasing and what needs attention?"
- active stack
- product name
- purchase date
- spend
- renewal/replenishment
- usage/adherence only if recorded
- product count
- spend trend when multiple records exist
- sub-tabs: Stack / Purchases / Renewal
- no medical recommendations unless backed by product/domain data

### NUTRITION
Question: "Is the nutrition plan active and being followed?"
- nutrition plan
- active state
- renewal date
- macro targets if connected
- target-vs-recorded macro chart only with actual tracking data
- adherence trend
- meal/log count if available
- sub-tabs: Plan / Macros / Adherence
- if tracking is unavailable, show a truthful connection/availability state rather than empty fake metrics

### SERVICES
Question: "Which paid/value-added benefits are active and being used?"
- active services
- entitlement/remaining usage
- expiry dates
- utilization
- upcoming booking
- unused benefit opportunities
- service timeline
- sub-tabs: Active / Usage / Expiry
- premium/VIP should feel materially different through real entitlements, not decorative styling

### HISTORY
Question: "What actually happened?"
- chronological event timeline
- event type filters
- time range
- grouped day/date
- important-event emphasis
- event detail drill-down
- sub-tabs: All / Attendance / Payment / Training / Other
- avoid huge empty timeline canvas

### INSIGHT
Question: "What should the operator notice or decide?"
- priority-ranked signals
- evidence under every signal
- severity/state
- trend/forecast only when derivable from actual records
- action owner
- recommended next action
- signal history/resolution
- sub-tabs: Attention / Trends / Evidence
- never label arbitrary data as AI insight

### MORE
Question: "What operational action is not a primary intelligence surface?"
- compact action directory
- edit profile
- biometric management
- invoices/receipts
- support/complaint
- settings/actions relevant to this member
- group actions by purpose
- do not turn More into a dumping ground

### ADVERTISEMENT / OFFERS
Question: "What relevant offer is active for this member?"
- actual promotion
- eligibility
- expiry
- benefit/value
- redemption state if recorded
- CTA
- no generic fake promotions

## Visual system
- Light-first, premium, modern, calm.
- Keep the existing BAD GYM identity and dual rails.
- Avoid black/dark default.
- Use semantic accent colors for state, not random decoration.
- Use varied content modules: KPI tile, timeline, progress ring, bar chart, sparkline, status capsule, evidence row, action row.
- Typography hierarchy should be clear at a glance.
- Long names must wrap safely.
- No vertical-letter artifacts.
- No clipping.
- No content hidden behind rails.
- No duplicate identity header.
- No repeated generic white rectangle pattern for every menu.

## Responsive behavior
- Header compacts automatically for non-HOME.
- Rail width remains bounded.
- Menu content gets maximum available width/height.
- Prefer 2-column micro-layouts inside the content viewport where useful.
- Collapse secondary facts before forcing scrolling.
- Keep primary KPI + action visible above the fold.

## Data integrity
- Production path must use real repository/domain data.
- Fixture/QA data is allowed only in explicit QA mode.
- Never invent photos, payments, services, trainer sessions, nutrition values, supplement adherence, or workout metrics.
- Empty/unsupported state must explain what is unavailable and provide the relevant connection/action.

## Acceptance criteria
1. Every visible menu retains compact member identity context.
2. HOME has the richest identity treatment.
3. Non-HOME menus use compact identity and maximize content area.
4. Opening a member auto-selects the most important available menu.
5. HOME and the selected problem menu communicate the same priority signal.
6. Every menu has distinct useful information architecture.
7. Charts/graphs are evidence-based and decision-useful.
8. Minimal scrolling on the target device.
9. No clipping/overflow/vertical text.
10. No fake data.
11. Dual-side rail behavior remains intact.
12. The same canonical card remains the single interaction surface.
13. Build, unit tests, lint if configured, and physical-device verification are required before completion.
