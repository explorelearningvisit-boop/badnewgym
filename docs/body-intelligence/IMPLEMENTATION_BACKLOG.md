# BAD GYM — Implementation Backlog

Branch: `feature/body-workout-intelligence`

## Workstream 0 — Repository preservation
- [ ] Keep all commits on this feature branch.
- [ ] Capture baseline build/test results.
- [ ] Review existing Member Intelligence entry points and feature flags.
- [ ] Preserve existing member header, four tabs and current repositories.
- [ ] Record existing fixtures vs production data boundaries.

## Workstream 1 — Requirements and visual audit
- [ ] Inventory existing screenshots and design references.
- [ ] Reconcile older zero-scroll docs with current scrollable detail requirements.
- [ ] Define 360dp baseline and viewport matrix.
- [ ] Approve light pearl / mint / emerald token system.
- [ ] Define typography, spacing, radii, elevation and motion tokens.
- [ ] Create per-widget empty/loading/error states.

## Workstream 2 — Anatomy catalog
- [ ] Replace enum-only expansion path with stable catalog identifiers where needed.
- [ ] Define body region hierarchy.
- [ ] Define muscle/subdivision hierarchy and aliases.
- [ ] Define left/right/bilateral semantics.
- [ ] Define which structures support direct measurement.
- [ ] Define functional performance tests separately from anatomy.
- [ ] Define mesh-to-anatomy stable ID contract.

## Workstream 3 — Exercise/equipment registry
- [ ] Audit current ExerciseCatalog coverage.
- [ ] Define exercise, variant and progression-family identity.
- [ ] Define equipment catalog and machine-specific load context.
- [ ] Define primary/secondary/stabilizer mapping.
- [ ] Define logging profile by modality.
- [ ] Add gym custom exercise workflow.
- [ ] Define global catalog moderation and localization.

## Workstream 4 — Workout logger
- [ ] Workout plan entry.
- [ ] Exercise search and recent/favorite.
- [ ] Body-map exercise entry.
- [ ] QR/NFC machine entry contract.
- [ ] Load selector with unit and load-basis.
- [ ] Target reps vs actual reps.
- [ ] Set start/stop/save timestamps.
- [ ] Countdown and optional sound/vibration.
- [ ] Rest timer and recovery after background/process recreation.
- [ ] Offline queue and idempotent sync.
- [ ] Warm-up/working/drop/rest-pause/cluster/superset support.
- [ ] Cardio, cycling, intervals, isometric, grip and mobility profiles.
- [ ] Previous-session recall.
- [ ] Daily/weekly/monthly/yearly/all-time history.

## Workstream 5 — Body Intelligence
- [ ] Transformation journey.
- [ ] Body composition with method/source/date.
- [ ] Measurements with member/staff source distinction.
- [ ] Interactive 3D anatomy prototype and asset licensing review.
- [ ] Muscle/subdivision detail.
- [ ] Strength/PR history.
- [ ] Training exposure and volume.
- [ ] Activity/endurance.
- [ ] Fitness assessment.
- [ ] Progress gallery with privacy controls.
- [ ] Training/break/retraining phases.
- [ ] AI insights with evidence and confidence explanations.

## Workstream 6 — Trust and verification
- [ ] Self-reported state.
- [ ] Context-linked state.
- [ ] Trainer attestation with scope.
- [ ] Video/device evidence metadata.
- [ ] Consent and retention controls.
- [ ] Independent review workflow.
- [ ] Dispute and appeal.
- [ ] Reversible audit events.
- [ ] Reciprocal-approval risk signals.
- [ ] Never label a record 100% genuine without an appropriate validated protocol.

## Workstream 7 — Rewards
- [ ] Gym-configurable reward policy.
- [ ] Attendance and safe-consistency inputs.
- [ ] Optional logging participation.
- [ ] Separate engagement and evidence quality.
- [ ] Discount caps and effective policy versions.
- [ ] Rest/injury/recovery fairness review.
- [ ] Reward history and explanation.

## Workstream 8 — Roles and operations
- [ ] Member own-data access.
- [ ] Trainer assigned-member access.
- [ ] Owner aggregate analytics and permissioned individual access.
- [ ] Staff equipment/maintenance access without body-data access.
- [ ] Machine QR/NFC inventory.
- [ ] Maintenance/cleaning tasks and audit.

## Workstream 9 — Quality gates
- [ ] Baseline and incremental Gradle build.
- [ ] Unit tests for analytics and load comparability.
- [ ] Timer state-machine tests.
- [ ] Offline/idempotency tests.
- [ ] Verification and audit tests.
- [ ] Rewards policy tests.
- [ ] Accessibility and reduced motion.
- [ ] Screenshot tests at 320/360/375/390/412dp.
- [ ] No fake data in production composables.
- [ ] Feature branch diff reviewed; no main merge without user approval.

## Immediate next tasks
1. Inspect current BodyProgress models, aggregation, ViewModel, Activity and entry point.
2. Inspect full project build/test baseline.
3. Add taxonomy and workout specification tests.
4. Implement a first native quick-set logger slice behind a feature flag.
5. Add body taxonomy without breaking current analytics.
6. Evaluate 3D asset licensing and native rendering before adding a binary asset.
