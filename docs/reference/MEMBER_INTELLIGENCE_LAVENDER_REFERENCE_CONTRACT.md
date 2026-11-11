# BAD GYM — Lavender Full-Screen Member Intelligence Reference Contract

Status: New task-specific visual contract. It supersedes the older compact 300×550dp / emerald-only visual target for this full-screen redesign. Existing domain, security, privacy, entitlement and data-integrity rules remain binding.

## Product target
Implement five real, native Android Compose destinations:
1. Overview Intelligence
2. Attendance Intelligence
3. Workout Intelligence
4. Payment Intelligence
5. Body Intelligence

This is not a static image, web page or single giant poster. Every screen must be responsive, scrollable where required, interactive and data-driven.

## Shared shell
- BAD GYM brand/header, hamburger, check-in state/action, notification badge and compact payment/date capsule.
- Member identity: avatar, name, member ID, age/gender only when available, membership state, plan, price/validity and days remaining.
- Four compact summary chips: Attendance, Workout, Payment, Body.
- Secondary navigation is contextual to the selected module.
- Bottom navigation: Overview, Members, Check-in, Workout, More (preserve the existing app navigation contract where different).
- Preserve Android system-bar insets and touch targets.
- Use a consistent shared header and member context; do not repeat the same detailed data in every card.

## Overview
- Fitness score only if its calculation is defined and supported.
- Attendance, workout, payment and body summaries.
- Performance trend with a selectable date range.
- Smart Insight based on real rules/data, with a clear explanation and actionable next step.
- No fabricated scores or AI predictions.

## Attendance
- Month selector and correctly aligned calendar.
- Present, Late, Absent, Holiday/Off, Sunday and Unknown states remain distinct.
- Show attendance percentage with its denominator, tracked days and meaningful streak.
- Attendance trend with range selector, best/lowest/average labels derived from records.
- Unknown/future/unrecorded days must not become absences.

## Workout
- Total duration, average/day, best, lowest and consistency only when derivable.
- Daily duration trend and monthly workout pattern from actual session records.
- Active days, streak and goal progress with explicit denominators.
- Do not draw missing months as zero or use static arrays as production data.

## Payment
- Paid / Due / Partial / Overdue / No record states.
- Active plan, actual amount, validity and next renewal.
- Lifetime paid, cycles, discount and outstanding only from ledger/transactions.
- Payment behaviour and plan history from actual records.
- Monthly payment trend must reconcile with transactions.
- Collection and reminder actions must use existing real flows; a tap must not imply payment success.

## Body
- Transformation goal and progress only where a goal exists.
- Weight, muscle, body-fat, BMI and measurements only when recorded.
- Metric-aware trend direction; do not assume weight gain/loss is always good.
- Progress gallery and privacy controls remain intact.
- No fabricated health metrics or interpolated readings.

## Visual system
Use the uploaded reference as the visual source:
- Canvas: pale pearl/lavender.
- Cards: near-white with subtle lilac borders and soft, restrained neumorphic elevation.
- Primary: violet/purple for selected tabs and primary actions.
- Positive: emerald.
- Warning: amber.
- Urgent/error: rose/red.
- Neutral/off: cool grey.
- Typography: use the existing bundled font if it matches; otherwise use a consistent readable sans-serif such as Lato. Establish a real type scale, tabular numeric metrics and high-contrast labels.
- Match the reference's compact spacing, rounded cards, icon scale, chart density and labels.
- No black/dark-heavy shell, uncontrolled neon, rainbow theme switcher, toy-like 3D, decorative characters or visual filler.
- Use dp/sp, not fixed screen pixels.

## Geometry and responsive rules
- Primary target width: 360dp.
- Validate 360, 375, 393 and 412dp.
- Reference is a full-screen mobile dashboard with module-specific scroll, not a 300×550dp compact card.
- Maintain readable font sizes and 48dp touch targets; do not force all content into one viewport.
- Avoid horizontal overflow, clipped labels, chart overlap and duplicate metrics.

## Data and architecture
- Existing Kotlin/Compose architecture is retained.
- Trace the production repository/ViewModel path before UI edits.
- UI renders resolved domain state; business calculations stay in domain/use-case layers.
- Sample data is permitted only for previews and tests.
- Server entitlement remains authoritative.
- Keep offline/cache behavior and existing API contracts intact.
- No new network endpoints or backend migrations without evidence and approval.

## Dependencies
- Reuse Compose Material 3 and Material Icons Extended already present.
- Reuse Compose Canvas charts initially; add a maintained chart library only if a specific chart interaction cannot be implemented cleanly with existing primitives.
- Use existing Compose animation APIs for restrained transitions.
- Use Coil for image loading.
- Do not add Three.js/React Three Fiber or 3D engines to a native Compose app. The supplied dashboard reference does not require 3D assets.
- Review license, maintenance and compatibility before adding any third-party library or GitHub skill.

## Acceptance
- All five destinations are real Compose UI.
- Shared shell and navigation are consistent.
- No sample fixture is the production data source.
- All charts and financial totals derive from real records.
- Existing interactions, receipts, payment flows, privacy and entitlement checks work.
- Light lavender reference is visually compared on device at 360dp and additional widths.
- Unit tests, build, UI tests and regression tests pass.
- Screenshots and exact commit SHA are recorded in the execution ledger.
