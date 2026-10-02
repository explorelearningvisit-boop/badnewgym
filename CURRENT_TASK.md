# BAD GYM — Current Task

## Active task: implement supplied Member Card Design System reference

The user supplied the reference image in the current conversation.
Reference: BAD GYM Member Card Design System — Image 1/24
Canvas: 1536 × 1024 px.

Treat that supplied image as the visual source of truth for the Member Card shell and its responsive/widget behavior.

## Immediate objective

Implement the reference as the actual BAD GYM Android UI. Do not create an approximate redesign.

The reference defines:
- always-visible vertical rail navigation
- Home enabled by default
- complete member-card shell
- member identity + current visit
- conditional widgets
- action visit section
- fixed widget ratios
- nested widget structure
- resize / drag concept
- responsive mobile/tablet/foldable behavior
- light premium visual system
- semantic status colors
- plan-based/widget entitlement behavior

## Reference anatomy to reproduce

### 1. Vertical rail
Always visible on the left side of the card.
Menu sequence shown: Home, Check-In, Check-Out, Payment, Plan, Attendance, Gym Time, Workout, Trainer / PT, Body / Progress, History, Insight, Offers, More.
Home is the default active state.
The rail uses icon-first compact navigation with active green/mint treatment, alert dots, locked/disabled states, and compact spacing that fits a mobile viewport.
Do not replace the vertical rail with a horizontal tab bar.

### 2. Card shell
Reference mobile target: total card width 360dp, safe padding 16dp, rail width 56dp, content width approximately 288dp, rounded outer shell, light glass/material surface, soft mint/emerald gradients, subtle borders and shadows, no black/dark workspace.
Responsive targets shown: small phone 320–360dp, normal phone 360–390dp, large phone 400–430dp, tablet 600–800dp, foldable open 800dp+.

### 3. Member identity visit
Fixed-height identity block near the top. Reference information includes member photo, Aman Tripathi, BG305, 23 yrs, Male, Gold Plan (Monthly), 18 Days Left, verification indicator, plan progress. These are visual examples only; production values must come from MemberSnapshot.

### 4. Current event visit
Fixed-height live event block. Reference CHECK-IN state includes CHECK-IN, Today, 9:25 AM, On Time, Active, 1h 50m, and a chevron/action affordance. Event state must come from BAD GYM domain data.

### 5. Conditional widgets
Reference Home examples: Attendance, Payment Due, Plan, Trainer/PT, Gym Time, Body Progress, and bottom actions. Widgets must not be duplicated generic cards. Visibility remains driven by event/visit + member data + server entitlement.

### 6. Widget size system
XS = 1 × 1; S = 2 × 1; M = 2 × 2; L = 3 × 2; XL / Hero = 4 × 2. Do not flatten every widget into the same card dimensions.

### 7. Nested widgets
Payment Due is shown as a container with status/title, amount, due date, and action. Support a parent widget containing structured sub-content rather than one flat text block.

### 8. Resize / drag concept
The reference shows resize handles and drag affordances. Implement only where supported by the existing widget editor architecture. Preserve touch targets and avoid making normal member-view surfaces accidentally draggable.

### 9. Responsive behavior
Adapt the same semantic widget system across small phone, normal phone, large phone, tablet and foldable. Responsive changes alter layout/size/flow, not business logic.

### 10. Visual language
Premium light background; white/frosted surfaces; emerald/mint primary; blue secondary; red urgent/payment; amber plan/due; purple analytics/insight; rounded components; soft shadows; subtle glow; clear hierarchy; compact information density. Do not introduce a black/dark background.

## Atomic implementation protocol

Follow docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md.
Required sequence: inspect repository/current renderer; implement shell; verify; implement rail; verify; implement member identity; verify; implement current event; verify; implement each widget independently; verify each; implement nested widget behavior; verify; implement responsive layout; verify; connect real domain/event/entitlement logic; verify state matrix; integrate complete Home screen; build/test; device screenshot verification; fix remaining visual mismatches; commit verified result.

Never skip screenshot comparison when a target device is reachable.

## Acceptance

Do not report completion merely because Gradle compiles. Completion requires reference decomposition, shell/rail/identity/event/widgets, Home default behavior, event/entitlement logic, responsive layout, passing build/tests, target-device verification when reachable, and major mismatches fixed or explicitly recorded.

## Scope
Only BAD GYM application, tests, assets and documentation. Do not modify OPPO/mobile-control tooling or unrelated projects.
