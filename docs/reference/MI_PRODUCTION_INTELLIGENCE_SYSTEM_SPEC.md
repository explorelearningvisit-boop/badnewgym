# BAD GYM — Member Intelligence Production System
## MI-PRODUCTION-INTELLIGENCE-SYSTEM-v1.0

Status: AUTHORIZED FOR IMPLEMENTATION
Branch: member-intelligence-v3
Execution: Google Antigravity GUI only
Scope: Stage 7.1 visual/data refinement + production intelligence semantics
Stage 8: NOT AUTHORIZED

## 1. Product objective
Member Intelligence is an operational decision surface, not a decorative profile card.
The owner should understand, in this order:
EVENT → MEMBER → TIME → STATE → MEASURE → PROBLEM/OPPORTUNITY → ACTION
Every color, icon, chart, badge and number must have a documented meaning.

## 2. Three-level information hierarchy
Level A — Glance (always visible): event + time; identity; membership state; one dominant signal; 3–5 core KPIs; one contextual CTA.
Level B — Context (current menu): selected menu explains the signal using charts, timelines, status blocks and compact labels.
Level C — Detail (drill-down): history, transactions, schedules and secondary data may scroll inside the bounded content viewport without displacing the identity/event header or primary CTA.
Maximum useful information means maximum meaningful information per pixel, not maximum raw text.

## 3. Universal visual grammar
Every visual data component must answer: WHAT is measured? VALUE/AMOUNT? PERIOD/DATE? STATE? WHAT NEXT?
Mandatory labeling examples:
- Attendance — 80% — This Month
- Payments — ₹4,500 Paid / ₹500 Due
- Workout — 4 sessions — This Week
- Supplements — Protein — 15 days left
- Trainer — 2/4 sessions used — This Month
A chart/bar/ring is invalid if its meaning cannot be understood from the component itself.

## 4. Semantic color system
GREEN = positive/completed/active/paid/available.
RED = critical/overdue/failed/out-of-stock.
AMBER = due/warning/low/attention required.
BLUE/CYAN = informational/neutral/activity/reference data.
PURPLE = insight/AI/advanced analysis only when explicitly labeled.
THEME ACCENT = brand/material decoration only; never substitute for semantic state.
Every semantic color is paired with icon/shape + explicit label + value/state.
Payment example: show PAID ₹4,500, DUE ₹500, OVERDUE ₹0, next due date, method when available, latest payment date and transaction timeline. Never show unexplained green/red bars.

## 5. Home — operational cockpit
Hero: photo + name + member ID; current event + exact time; membership state + days remaining ring.
Core KPI row: Attendance 24/30 • 80% • This Month; Sessions 4/5 • This Month; Workouts 12 • This Month.
Secondary visual row: Weight, Body Fat and Progress only when real domain data exists.
Trend chart must state metric and period, e.g. Attendance — Last 7 Days.
Signal: one dominant issue/opportunity with severity icon + label + supporting metric.
CTA is dynamic: Collect Payment / Renew Plan / Book Trainer / Start Workout / View Insights.

## 6. Attendance — time and consistency
Primary: attendance rate ring; present/late/absent counts.
Visual timeline: last 7–14 attendance days; each day has icon + status + time when available.
Metrics: current streak; average check-in time; total hours only if real.
Action: Mark Attendance / View Full Attendance / Contact Member according to permissions/state.

## 7. Plan / Membership
Primary: plan name; active/expired/frozen; days remaining ring.
Progress: elapsed vs remaining; start/end dates.
Benefits: icon tiles with used/remaining counts when available.
Warnings: renewal due; expiry; freeze days.
Action: Renew / Extend / Upgrade / View Plan.

## 8. Payment / Finance
This menu is a mini ledger, not a decorative dashboard.
Top: Paid total; Due total; Overdue total.
Status matrix: PAID / DUE / OVERDUE / REFUNDED when supported.
Transaction timeline: date; amount; method; receipt/reference; status.
Visual chart: monthly payments with explicit month labels and currency.
Action: Pay Now / Collect Payment / Send Receipt / View Ledger based on role and state.

## 9. Trainer / Sessions
Trainer hero: trainer photo/name; specialization; rating only when actual domain data exists.
Session ring: Used / Purchased sessions.
Schedule: next and previous session with date/time/type/state.
Action: Book Session / Reschedule / Contact Trainer / View Schedule.

## 10. Workout
Primary: current routine; week progress.
Visuals: weekly session bars; muscle-group coverage; last workout; duration/calories only when real.
Action: Start Workout / View Routine / Book Trainer.

## 11. Supplements
Every supplement visual must identify the product.
Example: Protein Whey — 2.5 kg — 60% remaining; BCAA — 45% — 10 days; Creatine — 80% — 25 days; Pre-workout — 20% — 5 days.
Color semantics: green = sufficient/active; amber = low/reorder; red = out/expired; blue = informational/product metadata.
Show product image/icon + name + quantity/remaining + expiry/reorder status.
Actions: Buy Again / Reorder / View Product / View Purchases.
Never use a colored bar without product label and unit.

## 12. Nutrition
Primary: today's intake if real; target; completion percentage.
Visual: protein/carbs/fats progress; hydration; meals.
Every macro shows name + consumed + target + unit.
Action: View Diet Plan / Log Meal / Book Nutritionist.

## 13. Services
Use a visual service grid: service icon/photo; name; active/expired; remaining/usage; next appointment; price only when available.
Examples: Personal Training, Diet Consultation, Body Analysis, Locker, Physiotherapy, Group Class, Spa/Recovery.
Actions are service-specific: Book / Renew / Use / View / Contact.

## 14. History
Use a pictorial timeline.
Every event: event icon; date; time; event type; one compact value; optional detail.
Examples: Check-in, Workout, Payment, Trainer Session, Plan Update, Service Purchase, Supplement Purchase.
Avoid repeated large InfoCards.

## 15. Insight / Analytics
Priority: P0 Critical; P1 Attention; P2 Opportunity; Informational.
Every insight shows what happened; evidence metric; period; confidence/source when supported; recommended action.
Example: P1 — Attendance down 20% / Evidence: 4 visits vs 5 average • last 30 days / Action: Contact member.
Do not claim AI certainty where data is incomplete.

## 16. Vertical rail intelligence
The rail is not merely navigation.
Attendance may show late/absent count; Plan days remaining/renewal warning; Payment due amount/overdue marker; Trainer upcoming session count; Workout weekly progress; Supplements low-stock marker; Nutrition today's completion; Services upcoming booking count; History unread/new events; Insight P0/P1 count; Home overall attention marker.
Rules: badge has explicit semantics; no decorative numbers; zero-state hides badge; badge never replaces label; accessibility description exposes the same meaning.

## 17. Dynamic bottom action system
The bottom region is a contextual action slot, not a fixed View button.
Priority: critical resolution → financial/renewal → today's scheduled action → engagement → secondary navigation.
Examples: overdue payment → Collect ₹500; plan expiring → Renew Plan; upcoming trainer → View Session; low supplement stock → Reorder Protein; no workout today → Start Workout; P0 insight → Review Alert; no action → View Full Profile.
CTA must derive from real state/action availability.

## 18. Advertisement / promotion system
Advertising must never impersonate operational data.
Use a clearly labeled PROMOTION/OFFER surface.
Allowed examples: membership upgrade; PT package; supplement offer; nutrition consultation; seasonal campaign; approved partner offer.
Rules: visually separated; never use semantic red/green to advertise; never cover or push away critical member information; frequency capped; dismissible when appropriate; role/eligibility targeted only from available business data; explicit offer CTA; track impressions, opens, clicks and conversions.
Gym-owned promotions come first; approved partner promotions are optional.

## 19. Business / monetization model
A. Gym SaaS subscription: recurring core product revenue.
B. Gym-owned commerce: supplements, PT sessions, nutrition, services, upgrades and renewals; track impression → view → action → purchase.
C. Promotions/sponsored offers: optional inventory for gym-owned products/services first and approved partners second.
Ads must be clearly separated from operational intelligence.

## 20. Data architecture contract
UI consumes domain/repository abstractions. No UI hardcoded fake values.
Conceptual model: MemberSnapshot → MemberEvent → AttendanceSummary → MembershipSummary → PaymentSummary + PaymentTransaction[] → TrainerSummary + Session[] → WorkoutSummary + WorkoutEvent[] → SupplementSummary + Purchase[] → NutritionSummary → ServiceSummary + Booking[] → HistoryEvent[] → IntelligenceSignal[] → Promotion[].
If data is missing: show No data / Not recorded / Not available with an appropriate icon, not a fake number.

## 21. Information-density rule
Information-rich is not information-crowded.
Use large primary number, medium label, tiny secondary metadata only when necessary.
Never shrink typography to fit more data. Move additional data to the selected menu or bounded scroll while keeping the primary decision layer visible.

## 22. Production acceptance
Verify: 11 menus; 8 themes; 360/375/390/412dp; Xiaomi Redmi Note 11 when connected; no clipping; no unexplained color; no unlabeled chart; no fake data; no hidden CTA; rail semantics; dynamic bottom action; promotion isolation; accessibility semantics; Stage 6 navigation regression; Stage 7 depth/motion regression; reduced-motion behavior; unit tests; testDebugUnitTest; assembleDebug.
Evidence: full menu matrix; payment detail; supplement detail; home trend explanation; rail badges; dynamic CTA states; promotion state; 8-theme matrix.

## 23. Design principle
BAD GYM should feel like: I can understand this member without reading the card.
When a menu opens: I can understand the reason behind that signal without guessing what any color, chart or number means.