# BAD GYM Member Intelligence — Pixel-Perfect Acceptance Checklist

Use this as a test record. Check an item only after evidence exists. A build passing is not a visual pass.

## A. Reference lock
- [ ] The exact user-supplied reference image is available to the agent/reviewer.
- [ ] Reference dimensions and crop are recorded.
- [ ] Screen safe areas and actual device viewport are recorded.
- [ ] Every visible section is mapped to a named Compose component.
- [ ] Each visible data value has a declared source or is explicitly demo-only.

## B. Geometry
- [ ] Target screenshot captured at the agreed 20:9 canvas, approximately 691×1536 px.
- [ ] Runtime layout checked at 360dp width.
- [ ] Narrow layout checked at approximately 320dp.
- [ ] Content respects status/navigation bars.
- [ ] No clipping, overlap, accidental horizontal overflow or inaccessible controls.
- [ ] Main cards, gutters, padding and corner radii follow the design token system.
- [ ] Font scaling checked at default and 1.3×.
- [ ] Long member names, localized dates and large currency values tested.

## C. Visual language
- [ ] Light mint/white/pale-blue surfaces; no dark-heavy background.
- [ ] Emerald success, coral-red urgent/payment, amber caution, blue workout/info, purple body-progress, gold plan upgrade.
- [ ] Text contrast is readable and semantic color is not the only state signal.
- [ ] Icons are from a consistent family and aligned to a consistent grid.
- [ ] Shadows are restrained; no meaningless glossy/3D decoration.
- [ ] No generic stock athlete is presented as a real member.

## D. Member identity and membership
- [ ] Name, ID, photo and plan load from selected member record.
- [ ] Missing photo has initials/neutral fallback.
- [ ] Active, expiring, expired/inactive states are correct.
- [ ] Ring math distinguishes elapsed plan progress from days remaining.
- [ ] Membership days/validity are not needlessly duplicated in other cards.

## E. Payment
- [ ] Paid member does not show a false due/collect-payment state.
- [ ] Outstanding balance and due/overdue state come from actual payment data.
- [ ] Receipt/history action works.
- [ ] Due-state CTA routes to the real payment action when available.
- [ ] Chart bars aggregate real transactions by calendar month.
- [ ] Timing colors are used only when transaction timing is known.
- [ ] Plan upgrade marker appears only when upgrade is recorded.
- [ ] Empty month is not falsely described as a missed payment.
- [ ] Currency formatting handles zero, large amounts and Indian rupee display.

## F. Attendance
- [ ] Calendar dates and weekday alignment are correct for the selected month.
- [ ] Present, absent, late, holiday/closed and unknown are visually distinct.
- [ ] Late uses green center with amber outline, per established BAD GYM rule.
- [ ] Holiday/closed X appears only when the gym calendar confirms closure.
- [ ] Sundays are not automatically treated as holidays.
- [ ] Percentage denominator is documented and matches gym policy/source data.
- [ ] Empty or partial attendance history is handled honestly.

## G. Workout
- [ ] Bars represent actual dated durations.
- [ ] Missing data is not treated as zero.
- [ ] Confirmed no-workout is visually distinct from unknown.
- [ ] Peak/lowest/average use the correct selected period and exclude unknown records.
- [ ] Previous-month change is calculated only when both periods are comparable.
- [ ] Chart labels remain readable on small screens.
- [ ] Tapping the chart/CTA opens real session details where implemented.

## H. Body Progress and privacy
- [ ] Only recorded metrics are displayed.
- [ ] Sparklines require at least two real measurements.
- [ ] Change direction is not called good/bad without a relevant goal.
- [ ] BMI/body-fat/muscle units and precision are correct.
- [ ] Sensitive measurements obey role/consent/privacy rules.
- [ ] Empty, hidden and unauthorized states are distinguishable.

## I. Navigation and behavior
- [ ] Every visible menu destination is reachable.
- [ ] Selected menu state is correct.
- [ ] Each primary CTA does a real action.
- [ ] No placeholder no-op button remains.
- [ ] Loading/error/empty states are tested.
- [ ] TalkBack/content descriptions and minimum touch targets are checked.
- [ ] Existing canonical member-card and event-driven flows still work.
- [ ] Server entitlements remain authoritative.

## J. Engineering and regression
- [ ] `./gradlew clean testDebugUnitTest assembleDebug` passes.
- [ ] Relevant unit tests cover payment, membership progress, attendance mapping, missing-vs-zero workout semantics and trend handling.
- [ ] Debug APK installs and launches on an emulator/device where available.
- [ ] Screenshot comparison completed and discrepancies fixed.
- [ ] No unrelated project files changed.
- [ ] Production repository adapters populate new analytics fields where the backend supports them.
- [ ] Stub-only fixtures remain isolated from production.
- [ ] Final branch, SHA, changed files, build/test, screenshot/device status and unresolved issues recorded.

## Evidence record

At each review, append:
- Date/time and reviewer/agent
- Branch and commit SHA
- Device/emulator and dp width/density/font scale
- Build/test command and exit result
- Screenshot file path(s) or CI artifact link
- Visual discrepancies found and fixes applied
- Device interaction results
- Remaining blockers

If a device or screenshot tool is unavailable, mark that check BLOCKED. Do not mark it passed.
