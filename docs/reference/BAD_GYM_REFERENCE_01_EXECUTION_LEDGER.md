# BAD GYM — Reference 01 Execution Ledger

Reference: Image 1/24
Source image: user-supplied design-system board, 1536×1024.
Required repository asset path: docs/reference/images/reference-01-member-card-design-system.png

## Golden rule
Anti-Gravity must pull the repository before work. The committed reference image is the visual source of truth. Do not ask the user to manually attach the reference image again.

## Execution loop
For every stage:
1. Read the full repository and current task/spec.
2. Inspect the exact existing files before editing.
3. Implement only the current atomic stage.
4. Build.
5. Run the relevant screen/state.
6. Capture a screenshot.
7. Compare against the committed reference image.
8. Record geometry, spacing, typography, color, icon, content, state, interaction and responsive discrepancies.
9. Fix discrepancies.
10. Rebuild and recapture.
11. Mark the stage VERIFIED only after the second comparison passes.
12. Record changed files, commit SHA, build result and screenshot/evidence location.
13. Move to the next stage.

Compilation alone is never verification.

## Atomic stage map

### Phase A — shell
A01: outer card shell, viewport constraints, safe padding, radius, surface, border, shadow.
A02: status/system-area treatment.
A03: BAD GYM app header.
A04: responsive shell constraints for 320–360dp, 360–390dp, 400–430dp, 600–800dp and 800dp+.

### Phase B — vertical rail
B01: rail container and fixed width.
B02: Home active state.
B03: Check-In / Check-Out navigation.
B04: Payment / Plan / Attendance.
B05: Gym Time / Workout / Trainer-PT.
B06: Body-Progress / History / Insight.
B07: Offers / More / utility controls.
B08: alert dots, locked states, selected states and touch targets.
B09: rail overflow/viewport verification.

### Phase C — member identity
C01: member portrait and verification badge.
C02: member name, code, age, gender.
C03: plan name and days remaining.
C04: progress indicator and chevron.
C05: real MemberSnapshot binding; no hard-coded reference values.

### Phase D — current visit/event
D01: CHECK-IN hero.
D02: CHECK-OUT state.
D03: event time and relative labels.
D04: On Time / Active / duration treatment.
D05: event-specific accent/state.
D06: event-driven domain binding.

### Phase E — widgets
E01: Attendance widget.
E02: Payment Due widget.
E03: Plan widget.
E04: Trainer/PT widget.
E05: Gym Time widget.
E06: Body Progress widget.
E07: bottom action widgets.
E08: widget-specific visual states; no generic duplicated cards.

### Phase F — widget system
F01: XS 1×1.
F02: S 2×1.
F03: M 2×2.
F04: L 3×2.
F05: XL/Hero 4×2.
F06: grid placement and spacing.
F07: responsive reflow without changing domain semantics.
F08: resize handles/editor behavior where supported.
F09: drag/reorder where supported; never accidentally draggable in normal member view.

### Phase G — nested widgets
G01: Payment Due parent container.
G02: status/title sub-widget.
G03: amount sub-widget.
G04: due-date sub-widget.
G05: Collect Payment action sub-widget.
G06: nested layout/responsive verification.

### Phase H — state matrix
H01: CHECK_IN.
H02: CHECK_OUT.
H03: PAYMENT.
H04: WALK_IN.
H05: TRIAL.
H06: current payment.
H07: overdue payment.
H08: attendance present/no attendance.
H09: trainer present/absent.
H10: optional service present/absent.
H11: loading/empty/error.
H12: server entitlement granted/revoked.
H13: checkout with outstanding money.
H14: locked/hidden widgets.

### Phase I — integration
I01: Home default.
I02: rail navigation.
I03: real domain data.
I04: server-authoritative entitlements.
I05: persistence and interaction.
I06: full-screen visual comparison.
I07: regression against existing menus.

### Phase J — production verification
J01: clean build.
J02: unit tests.
J03: UI/instrumentation tests where available.
J04: target Xiaomi 11i verification when device is reachable.
J05: screenshot evidence.
J06: final visual discrepancy report.
J07: final commit SHA.

## Status
Use exactly one status per stage:
PENDING | IN_PROGRESS | BLOCKED | VERIFIED

At the end of every Antigravity run, update this file with:
- current stage
- status
- files changed
- build/test result
- screenshot/evidence path
- comparison findings
- fixes made
- next stage

## Completion condition
Do not call the reference complete until all applicable stages are VERIFIED and the final integrated screen has been compared against the reference at the supported target sizes.
