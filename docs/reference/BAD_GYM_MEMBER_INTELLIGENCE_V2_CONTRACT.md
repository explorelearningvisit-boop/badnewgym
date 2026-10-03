# BAD GYM — Member Intelligence V2 / Production Design Contract

## STATUS
This document supersedes the previous Member Intelligence presentation concept.
The previous visual presentation is OBSOLETE. Do not preserve it, merge it, or use it as a visual reference.
The new concept is the only active Member Intelligence presentation contract.

## PULL-AND-RUN RULE
An Antigravity agent must checkout main, pull with git pull --ff-only origin main, read AGENTS.md, CURRENT_TASK.md and this document, inspect app/src/main/, implement the contract, build, run, screenshot, compare, fix, re-verify, update the execution ledger, then commit and push the verified result to main.

## CANONICAL VISUAL DIRECTION
The new BAD GYM Member Intelligence concept is a professional gym-management product: light premium UI, compact high-information mobile card, green brand identity, semantic colors, soft depth, real data visualization and progressive disclosure.
Every visual element must help a gym owner or staff member understand a member or perform an operational action.

## BASE MEMBER-CARD GEOMETRY
Primary target: 300dp width × 550dp height.
Height may expand when the active widget set genuinely requires it; preferred expansion ceiling is approximately 600dp before switching to a deeper detail view.
Use approximately 12–16dp outer safe padding and 48dp minimum touch targets.
The card must never become a full-screen information dump merely because more data exists.

## INFORMATION HIERARCHY
1. Current event
2. Member identity
3. Critical operational alert/action
4. Attendance
5. Workout
6. Body progress
7. Conditional modules
8. History/secondary detail

Current event: compact CHECK-IN/CHECK-OUT or relevant event, time, status, duration when meaningful, event-specific action.
Member identity: photo, name, member code, real verification state, membership status, plan name, days remaining, tier/status.
Plan information belongs with identity rather than creating redundant plan cards.

## PAYMENT
Only show payment alert when payment state requires attention or is relevant.
Payment Due communicates amount, due/overdue state, due date, Collect Payment and Remind.
Collect Payment opens a payment collection sheet; it does not imply payment was received.
Sheet fields: due amount, amount received, Cash/UPI/Card/Bank Transfer, optional note, Record Payment, Remind Instead.
Partial payment shows received and remaining.
Reminder defaults to WhatsApp with SMS fallback; Email and Copy Message remain manual alternatives.
Show delivery state: Ready, Sending, Sent, Delivered, SMS Fallback or Failed.

## ATTENDANCE
Use actual historical attendance.
Preferred compact representation: percentage, visits/target and day-level markers for Present, Late, Absent and Holiday.
Do not invent data.

## WORKOUT
Use actual workout data.
Preferred compact representation: 7-day vertical duration bars, daily duration, Today, Peak and Lowest.
Expanded detail can show 30 days.
Do not duplicate information merely to fill the card.

## BODY PROGRESS
Body progress is metric-aware. Possible real metrics: Weight, Body Fat, Muscle Mass, Waist, Chest, BMI.
Never assume gaining weight is good or losing weight is good.
If goal direction is unavailable, use neutral labels such as Body Measurement and show the actual trend.

## CONDITIONAL MODULES
Only render modules when real data and entitlement support them: Trainer/PT, Services, Offers, Gym Time, History, Insight, machine/issue state and renewal state.
No empty placeholder cards. No fake metrics. No decorative filler.

## WIDGET SYSTEM
Every widget is a first-class configurable object.
Required capabilities: on/off, size, position/order, visibility condition, entitlement, state, priority and detail action.
Logical size vocabulary: XS 1×1, S 2×1, M 2×2, L 3×2, XL 4×2.
For the compact 300dp card, map logical units to available grid width rather than forcing every widget to full width.

## ADMIN COMMAND CENTER
Member/staff view is read-only with respect to layout. Normal users must not accidentally drag or resize widgets.
The main BAD GYM software admin gets a dedicated command-center configuration surface.
Admin capabilities: enable/disable widgets, reorder widgets, resize widgets, configure default positions and sizes, define allowed widgets by plan/entitlement, inspect state, preview the card, reset to system default, save/publish configuration, distinguish system defaults from gym/account overrides.
Admin configuration must be separated from member runtime state.
Server entitlement is authoritative.
Recommended precedence: SERVER ENTITLEMENT → ADMIN DEFAULT → GYM/ACCOUNT OVERRIDE → USER/PERSONAL PREFERENCE where explicitly supported → RENDERER.
Never allow local UI state to bypass entitlement.

## POSITION AND RESIZE
The layout model must support ordered widget IDs, per-widget size override, enabled/disabled state, position/order, default layout, published layout and draft layout.
Admin editing should provide drag/reorder where supported, accessible move controls as fallback, resize controls, enable/disable, reset and preview.
Normal member view must not expose editor affordances.

## COLOR SYSTEM
Primary: emerald/green brand and mint supporting surfaces.
Blue = information/attendance/analytics.
Amber = due/attention/plan timing.
Red = urgent/payment overdue/error.
Purple = trainer/insight where semantically useful.
Surfaces: warm/light white, very pale mint and subtle neutral separators.
Avoid black/dark-heavy backgrounds, uncontrolled neon, rainbow gradients, excessive glow, excessive glassmorphism and high-saturation decorative blocks.
Color must communicate state.

## TYPOGRAPHY
Event/status is compact and bold. Member name is the strongest identity text. Primary metric is large but not oversized. Supporting values are medium. Labels are compact and high-contrast. Secondary metadata is muted.
Do not make every number visually dominant.

## PSYCHOLOGY / USABILITY
The first visual pass should answer: Who? Why are they here? Is there an issue? Is action required? How is attendance? What is the recent workout pattern? Is there real body-progress data?
Use progressive disclosure. Do not force the user to inspect every widget.
Do not use gamification as a substitute for useful information.
Do not show predictions unless a real predictive model exists.
Do not show achievements or streaks by default unless they are an actual product requirement and provide distinct value.

## NO FIDGET / TOY UI
Strictly prohibited: fidget toys, toy-like 3D decorations, floating decorative objects, childish illustrations, decorative characters, meaningless animated objects, visual ornaments that consume card space, fake AI ornaments and decorative widgets with no business meaning.

## DATA INTEGRITY
Production UI must use real domain data. Reference/sample member values are for preview/screenshot fixtures only.
Do not permanently hard-code member names, IDs, payment values, attendance percentages, workout durations, body measurements or dates.
No fake health data. No unsupported fields.

## PERFORMANCE
Optimize for a Xiaomi 11i-class Android device. Avoid unnecessary recompositions, continuous animations, oversized bitmaps, duplicate network calls, heavy blur layers, excessive shadows, rendering hidden widgets and loading detail data before requested.

## RESPONSIVE BEHAVIOR
The 300×550dp card is the compact reference target.
If the active configuration cannot fit: reduce secondary content, collapse secondary widgets, move detail to a sheet, increase height modestly, then use deeper detail navigation.
Do not squeeze typography below usable sizes or create a vertically endless card.

## ACCEPTANCE
Obsolete presentation is removed from active code.
New visual contract is implemented.
300×550dp target is practical and modest height expansion works.
Widgets can be enabled/disabled, resized and reordered/moved.
Admin command center exists in the product architecture.
Server entitlement remains authoritative.
Normal member view is not accidentally editable.
Real data is used; no fake/filler metrics exist; no toy/fidget UI exists.
Build passes, unit tests pass, screenshot verification is performed, execution ledger is updated and final verified commit is pushed to main.