# BAD GYM — Body & Workout Intelligence Master Blueprint

Status: Working specification on `feature/body-workout-intelligence`  
Base: `main` at `13fe7792c58a95d6bd8ea0b0048a88a72e262fee`  
Scope: Android-first, 360dp baseline, existing BAD GYM project.

## 1. Product intent

BAD GYM remains a gym-owner operating product while adding a member-facing fitness intelligence layer. The member experience is optional and low-friction; it must not make ordinary attendance, payment or gym use dependent on workout logging.

The system has four existing Member Intelligence entry tabs: Attendance, Workout, Payment, Body. Preserve the current member identity/header and existing data contracts. Body and Workout details are drill-down experiences, not replacements for the existing compact member card.

## 2. Non-negotiable principles

- Main branch is not modified by this feature work.
- Preserve existing project architecture and working flows.
- No fabricated member data, chart points, measurements, PRs or verification claims.
- Do not duplicate a metric across multiple cards without a clear contextual reason.
- Distinguish anatomical measurement, performance, training exposure and transformation.
- Target vs actual reps are separate fields.
- Self-reported data is useful but not automatically verified.
- Trainer attestation, machine context, video evidence and independent review are distinct evidence types.
- Corrections append audit events; never silently overwrite historical records.
- Rewards must not incentivize unsafe loads, excessive exercise or false claims.
- Member logging remains optional; no penalty for not logging.
- Keep the UI readable at 360dp; compactness must not be achieved by illegible text or undersized touch targets.
- 3D anatomy is a navigation/education visualization, not a diagnostic scan.

## 3. Existing codebase findings

The repository already contains:
- Kotlin + Jetpack Compose Android app.
- `feature/bodyprogress/domain/model/BodyProgressModels.kt` with body metrics, muscle groups, exercise and workout set/session models, measurement method, source and verification concepts.
- `feature/bodyprogress/domain/catalog/ExerciseCatalog.kt` with a starter exercise catalog.
- Body progress aggregation, analytics, insight engine and presentation screens.
- Member card components including compact viewport and Aqua Glass variants.
- Room, WorkManager, Retrofit/OkHttp dependencies.
- Existing docs for UI guidelines, member intelligence, Supabase architecture and testing.
- Existing design/reference images under `docs/reference/images`.

The current exercise model is a useful foundation, not a complete global exercise taxonomy. It currently uses enums for muscle/equipment/load types, so future global expansion should move toward stable catalog IDs and data-driven relationships rather than repeatedly extending enums.

The repository's most recent main commit observed during this audit is:
`13fe7792c58a95d6bd8ea0b0048a88a72e262fee` — "feat(ui): eradicate dark/black elements and enforce Light Luxury Pearl zero-scroll theme" (2026-10-20).

Important reconciliation: older docs mention zero-scroll viewport fit and some older theme variants. The current user requirements allow vertically scrolling detail workspaces; retain compact main card, but do not force dense Body Intelligence detail into a zero-scroll shell.

## 4. Product modules

1. Member Intelligence shell — existing header and four tabs.
2. Attendance Intelligence.
3. Workout Intelligence.
4. Body Intelligence.
5. Anatomy & Exercise Registry.
6. Performance & Longitudinal History.
7. Trust, Evidence & Verification.
8. Rewards & Engagement.
9. Trainer Intelligence.
10. Gym Owner & Operations.

## 5. Body Intelligence information architecture

Primary sections:
- Transformation Journey / Goal Progress.
- Interactive 3D Anatomy Map.
- Body Composition.
- Body Measurements.
- Muscle Intelligence.
- Strength Intelligence.
- Muscle Focus / Training Exposure.
- Activity & Endurance.
- Fitness Assessment.
- Progress Gallery.
- Training Phases (training, break, detraining, return).
- AI Insights.

Do not create a duplicate "Key Changes" card. Place each change next to its canonical metric.

### Four separate data domains

1. Anatomy/measurement: circumference, body composition and documented physical measurements.
2. Performance: force, strength, grip, endurance, mobility, stability and test outcomes.
3. Training exposure: exercise, sets, reps, load context, volume, frequency and intensity.
4. Transformation: longitudinal changes against an explicit goal.

Never infer muscle hypertrophy solely from training volume or a single exercise performance.

## 6. Anatomy hierarchy

Use stable IDs and parent-child relationships:
Body region → muscle group → anatomical structure/subdivision → optional functional substructure.

Initial coverage:
- Head/neck.
- Shoulder: anterior/lateral/posterior deltoid; rotator cuff structures.
- Chest: pectoralis major regions and pectoralis minor.
- Back: latissimus dorsi, trapezius regions, rhomboids, teres group, erector spinae.
- Arm: biceps long/short heads, brachialis, triceps long/lateral/medial heads.
- Forearm: flexor/extensor compartments, pronation/supination structures.
- Hand/wrist: grip, pinch, wrist flexion/extension, deviation and forearm rotation performance tests.
- Core: rectus abdominis, obliques, transverse abdominis, spinal extensors.
- Hip/gluteal structures.
- Thigh: quadriceps subdivisions, hamstrings, adductors, abductors.
- Lower leg: gastrocnemius, soleus, tibialis anterior and other relevant structures.
- Foot/ankle: functional mobility/strength tests.

The anatomy catalog must support aliases, source references, laterality, parent IDs, selectable 3D mesh IDs, measurement eligibility and exercise relationships. Do not assume every anatomical structure has a reliable direct circumference or isolated strength test.

## 7. Exercise and equipment registry

Exercise and equipment are separate entities. An exercise may be performed with multiple equipment types; one machine may support multiple exercises.

Exercise fields should include:
- Stable exercise ID, canonical name, aliases, language/localized labels.
- Movement pattern and modality.
- Primary, secondary and stabilizer anatomy targets.
- Equipment and equipment variant.
- Grip, stance, angle, laterality and range-of-motion metadata where relevant.
- Logging profile and required/optional metrics.
- Progression family and comparability rules.
- Provenance, review status and active/deprecated status.

Support free weights, machines, cables, plate-loaded/selectorized machines, bodyweight, calisthenics, bands, chains, sleds, flywheel, cardio, cycling, rowing, ski erg, swimming, mobility, isometrics, grip/wrist and gym-specific custom equipment.

Gym custom exercises must be loggable immediately. Global catalog publishing requires moderation and anatomy mapping review. Do not silently merge different machine variants into one comparable strength history.

## 8. Workout logging

Entry paths:
- Workout plan.
- 3D body map.
- Exercise search.
- QR/NFC machine tag.
- Recent/favorite exercises.

Set workflow:
1. Select exercise and equipment context.
2. Select load and load basis.
3. Optional target reps.
4. Start with configurable 3-2-1 or 5-4-3-2-1 countdown.
5. Local monotonic timer records start.
6. Stop records end; member enters actual reps.
7. Save set and start rest timer.
8. Carry forward previous load by default, editable before next set.
9. Preserve each set independently.

Record start, stop and save timestamps separately. Do not blindly subtract a fixed 3–6 second delay from set duration. Show recorded interval and save interaction delay separately; any active-duration estimate must be explicitly labelled.

Logging profiles must support:
- Load + reps.
- Per-hand dumbbell load.
- Bar + plates.
- Machine/stack load with machine identity.
- Assisted bodyweight (assistance is not lifted load).
- Hold duration and external load.
- Distance/time/pace/speed/incline/resistance/cadence.
- Force/grip tests, side and test protocol.
- Mobility/range-of-motion tests.
- Intervals and rounds.
- Drop sets, supersets, giant sets, rest-pause, cluster sets, warm-up and working sets.

Offline timer and set capture must survive backgrounding, temporary process recreation and network loss. Sync must be idempotent and conflict-aware.

## 9. Longitudinal history

Retain daily, weekly, monthly, 6-month, 12-month, 24-month and all-time views where records exist.

Support training, reduced training, break/detraining and return-to-training phases. A low later measurement is a recorded observation, not an automatic diagnosis or explanation.

Personal records must specify the exact exercise variant, equipment/load basis, protocol and date. Estimated 1RM and actual 1RM must remain distinct.

## 10. Trust and verification

Suggested evidence states:
- Self-reported.
- Context-linked (gym/machine/session context; not proof of completed reps).
- Trainer-attested (specify what was observed).
- Evidence-supported (video/device/test; specify what the evidence establishes).
- Independently reviewed.
- Challenged/disputed.
- Superseded/corrected.

Friend endorsements are social endorsements, not proof. Detect suspicious reciprocal approval patterns as review signals, not automatic fraud findings. Trainer decisions must be auditable, attributable and reversible through a new event.

Video capture requires explicit consent, privacy-safe framing, access control, retention/deletion policy and secure storage. Avoid capturing bystanders. Evidence verification must specify whether it confirms exercise identity, visible reps, technique, load or only attendance; do not overstate what a camera can prove.

## 11. Rewards

Reward consistency, attendance, safe participation and optional honest logging—not raw load, body size, weight loss or dangerous performance targets.

Keep separate:
- Engagement score/eligibility.
- Evidence quality/trust indicators.

Gym owner controls policy, caps, eligibility windows and discount rates. No discount is awarded solely because a member claims a large lift. Do not penalize legitimate rest, injury recovery or non-use of optional logging.

## 12. Visual direction

- Existing app is Kotlin/Jetpack Compose; retain native architecture.
- Light Luxury Pearl / near-white surfaces with restrained mint/emerald accent.
- Soft neumorphism/glass only where it improves hierarchy; avoid excessive shadows.
- Readable typography and comfortable touch targets.
- 360dp baseline, test 320/360/375/390/412dp and 600–750dp height.
- Existing compact Member Intelligence shell remains compact.
- Body detail screens may scroll vertically; do not force all advanced data into one viewport.
- Use consistent iconography, semantic colors and accessible labels.
- Motion should be purposeful and reduced-motion aware.

## 13. 3D asset strategy

Do not commit an unlicensed anatomical model. Evaluate:
- BodyParts3D / Anatomography sources and their exact license/attribution obligations.
- Z-Anatomy assets and license compatibility.
- Sketchfab only when each model's license explicitly allows the intended commercial app use and redistribution.
- Commercial anatomy vendors only after license and mobile runtime terms are reviewed.

Prototype options:
- Android-native rendering with Filament/GLB if supported by project/runtime requirements.
- Three.js only for a web prototype or embedded web surface if native feasibility is unsuitable.

Required asset properties:
- Separable/selectable structures or sub-meshes.
- Stable anatomy IDs.
- Front/back/side orientation.
- Mobile polygon/texture budgets and LOD.
- No medical diagnostic claims.
- Rotation pauses on touch, slow idle rotation, accessibility/reduced-motion mode and static fallback.

## 14. Data architecture direction

Prefer stable catalog IDs and relational tables over hard-coded enum-only taxonomy:
- anatomy_regions
- anatomy_structures
- anatomy_aliases
- anatomy_mesh_mappings
- exercise_catalog
- exercise_variants
- exercise_muscle_targets
- equipment_catalog
- gym_custom_exercises
- workout_sessions
- workout_exercises
- workout_sets
- set_segments
- set_timer_events
- body_measurements
- performance_tests
- training_phases
- workout_evidence
- verification_reviews
- verification_events
- reward_policies
- reward_events
- audit_events

Before schema changes, inspect current persistence and backend implementation; do not introduce a second competing source of truth.

## 15. Delivery stages

1. Repository audit and requirements freeze.
2. Anatomy taxonomy.
3. Exercise/equipment taxonomy.
4. Workout/performance specification.
5. Trust/rewards/privacy specification.
6. Data/API/security design.
7. Visual design and staged mockups.
8. Native implementation, tests and release candidate.

The larger 12-stage plan can split stages 7–8 into Member Intelligence, Body Intelligence, Workout Logger, Verification, Rewards, Operations and QA.

## 16. Acceptance gates

- Main branch unchanged.
- Feature branch starts from observed main SHA.
- Existing app compiles before feature work and after each implementation slice.
- No API secrets committed.
- No fabricated production data.
- Tests for load semantics, target-vs-actual reps, timers, idempotent sync, corrections and verification transitions.
- Screenshot review at 360dp and smaller/larger widths.
- User approval before any merge to main.
