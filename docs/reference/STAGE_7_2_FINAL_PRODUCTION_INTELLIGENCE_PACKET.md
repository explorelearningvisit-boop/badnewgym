# BAD GYM — Stage 7.2 Final Production Intelligence Refinement
## MI-STAGE-7.2-FINAL-PRODUCTION-INTELLIGENCE

STATUS: READY_FOR_EXECUTION
EXECUTOR: Google Antigravity GUI only — visible Pull and Run
BRANCH: member-intelligence-v3
PREVIOUS VERIFIED IMPLEMENTATION: 0c6c1d2e74a2d05cbfcd9b59695c24204bed1ded
NO STAGE 8 SCOPE

## 0. User-approved visual direction
Use the latest generated concept board as visual direction, not as a literal copy.
User preference: light UI only; no black/dark-black backgrounds; white/ivory base surfaces; pink/coral, yellow/gold, mint/green, aqua/sky and soft purple may be theme accents; large readable content; generous spacing; practical Android implementation; smarter vertical rail; every visual must have explicit meaning.

## 1. Current verified baseline
Stage 7.1 is already implemented and pushed.
Geometry: Compact 296x410dp browse / 320x440dp detail; Default 312x426dp / 340x463dp; Expanded 328x442dp / 360x480dp. Rail widths 50/56/60dp.
Existing visual primitives include attendance ring, KPI chips, weekly consistency, workout/load visuals, nutrition macro bars, service status, history timeline, insight priority stack and semantics.
Do not throw away the existing implementation. Refine it.

## 2. Core design law — NO DUPLICATED INFORMATION
A metric has one primary visual home.
Bad: attendance percentage in a ring, a second bar and another KPI.
Good: Home gives one current attendance signal; Attendance gives detailed timeline/breakdown. A Home delta is allowed only when it adds a different dimension.
Every visual must earn its space by answering a different decision question.

## 3. Home — interactive member cockpit
Always visible: member photo/name/ID/state; current event/time; membership signal; only 3 core KPIs; one trend; one intelligence signal; one contextual CTA.
Core KPIs: Attendance, Sessions, Workouts.
Home interactions: identity opens profile; attendance opens Attendance; sessions opens Trainer/Sessions; workouts opens Workout; membership opens Plan; intelligence opens Insight; CTA executes the real contextual action; current event opens relevant history/event context when available.
Home shows only ONE trend at a time. The trend must state metric + period + unit. Examples: Attendance — Last 7 Days; Workout Frequency — Last 4 Weeks.
Do not add a graph merely to fill space.

## 4. Menu-specific information architecture
Attendance: rate, present/late/absent, streak, 7/14-day timeline, check-in timing, action. Do not repeat Home plan/payment blocks.
Plan: plan name, active/expired/frozen, days remaining, start/end, elapsed/remaining, benefits, renewal/freeze state, action.
Payment: Paid total, Due total, Overdue total; transaction timeline with date/amount/method/reference/status; monthly payment trend only when useful; contextual action. No unexplained bars.
Trainer/Sessions: trainer identity, purchased/used/remaining sessions, next/previous session, schedule, type/status, actions.
Workout: current program, weekly completion, session history, muscle-group coverage, last workout, action. Do not duplicate Attendance.
Supplements: every item has product image/icon, exact product name, quantity/unit, remaining %, days left only when supported, stock/reorder/expiry state, reorder/buy/view action. No unlabeled colored bars.
Nutrition: calories consumed/target, protein/carbs/fat consumed/target, hydration, meals, plan state, action. Every visual has label + unit + target.
Services: service icon/photo, name, active/expired, usage/remaining, next booking, price only when available, service-specific CTA.
History: pictorial timeline with event icon, date/time, type, one compact value and optional detail. Filters may include All, Check-in, Workout, Payment, Trainer, Service.
Insight: P0/P1/P2, evidence metric, period, reason and action. Do not repeat raw Home KPIs.
More: action directory for Profile, Documents, Health Report, Achievements, Rewards, Announcements, Offers, Settings, Support only when supported.

## 5. Intelligent vertical rail
Rail = icon + short label + optional semantic badge.
Examples: Attendance 2 late; Plan 12d; Payment ₹500; Trainer 1 upcoming; Workout 3/5; Supplements 2 low; Nutrition 77%; Services 1 upcoming; History 3 new; Insight 1 P0; More no meaningless badge.
Zero means no badge. Badge meaning must be obvious. Badge never replaces label. No random colored dots. Accessibility description must expose badge meaning.

## 6. Dynamic bottom action
One primary action slot, derived from real state.
Examples: payment due/overdue → Collect ₹500; renewal → Renew Plan; trainer session → View Session; low supplement → Reorder Protein; nutrition incomplete → Log Meal; workout due → Start Workout; P0 insight → Review Alert; no action → View Member.
Do not create three competing primary buttons.

## 7. Advertisement / promotion
Use one compact clearly labeled PROMOTION/OFFER surface.
Show actual offer image/icon, title, price/discount only when actual, expiry only when actual, one CTA and dismiss when appropriate.
Never place ads inside payment totals; never use semantic red/green to advertise; never push critical intelligence below the fold; never make an ad look like an operational alert.
Business examples: supplements, PT package, nutrition consultation, membership upgrade, gym event, approved partner offer.

## 8. Typography and spacing
Do not keep increasing height to compensate for bad information architecture.
Remove redundant content first; enlarge important text; give charts real space; keep secondary metadata compact; use consistent 8/12/16dp rhythm; never shrink text just to fit.

## 9. Light theme system
Eight light material personalities:
1 Natural Fresh — ivory + emerald/mint + botanical vector
2 Coral Pink — warm white + coral/pink
3 Gold Light — ivory + champagne/gold
4 Mint Minimal — white + mint
5 Aqua Glass — white + aqua/sky
6 Sunset Peach — warm white + peach/coral
7 Royal Purple Light — white + soft purple
8 Premium Ivory — ivory + restrained warm metallic
Semantic states remain independent: success green, warning amber, critical red, informational blue/cyan, insight purple.

## 10. Sequential execution
L0 geometry/safe viewport
L1 background/material
L2 border/shape
L3 identity/photo
L4 event/state
L5 unique visual analytics
L6 contextual CTA
L7 typography
L8 restrained decorative assets
L9 vertical rail
L10 menu-specific content
L11 motion/depth
L12 all eight light themes
L13 interaction/accessibility audit
L14 final runtime verification
Verify each layer before advancing. If clipping or semantic ambiguity exists, stop and fix it.

## 11. Assets
Do not bake UI text into raster backgrounds.
Prefer Compose vectors, Canvas, small transparent PNG/WebP assets where genuinely useful, real member photos and product/service imagery from existing assets.

## 12. Clickability
Every interactive-looking element must perform a real action, navigate to a real flow, open an existing editor/detail flow, or be visibly non-interactive. No fake buttons.
Respect current repository/domain/navigation boundaries. If an edit action is unsupported, omit it or navigate to a real supported flow.

## 13. Acceptance
Verify all 11 menus + More; all visible buttons; rail navigation; contextual CTA; menu transitions/back; all 8 light themes; 360/375/390/412dp; Xiaomi Redmi Note 11 when available; no clipping/overlap; no repeated primary metrics; no unexplained chart/color; no fake data; readable text; accessibility semantics; Stage 7 depth/motion; reduced-motion contract; unit tests; testDebugUnitTest; assembleDebug.
Evidence: Home interactive state; Payment semantics; Supplements semantics; rail badges; dynamic CTA; More menu; promotion; all-menu matrix; all-theme matrix.

## 14. Final implementation prompt
Pull and Run this task as a production refinement of the existing BAD GYM Member Intelligence implementation. Inspect the current branch and Stage 7.1 code first; do not rewrite working architecture. Use this packet as the authoritative source. Build one coherent light visual system with white/ivory/pastel materials, large readable information and generous spacing. Make Home interactive and decision-focused with only unique high-value KPIs. Move detailed analytics to the correct menu. Remove duplicate metrics across Home and menus. Every chart, ring, bar, color and badge must have explicit metric/state/period semantics. Payment must clearly separate Paid/Due/Overdue. Supplements must identify every product and quantity/remaining/reorder state. Make the vertical rail semantically intelligent with meaningful badges only. Make the bottom CTA state-derived. Keep promotions isolated and clearly labeled. Use real MemberSnapshot/domain/repository data; never invent numbers. Use Compose-native primitives and lightweight assets, not giant bitmap UI. Execute L0→L14 sequentially and verify each layer. Preserve Stage 6 navigation and Stage 7 depth/motion. Do not start Stage 8. Finish with tests, build, Redmi runtime when available, screenshots, docs, commit and push, then report exact SHA and evidence.