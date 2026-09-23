# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-V4-VISUAL-RECONSTRUCTION-01
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission

The previous implementation is functionally working but visually fails the supplied BAD GYM Natural Fresh reference. This task is a VISUAL RECONSTRUCTION, not a minor polish pass.

Reference assets are stored in:
- docs/reference/member-intelligence-natural-fresh-reference.png
- docs/reference/current-device-output.png

The reference image is the visual source of truth for the Natural Fresh Home state.

## Critical rule

Do NOT mark this task COMPLETED merely because the app compiles or navigation works.

Completion requires a real emulator/device screenshot and visual comparison against the reference asset. Iterate until the composition, hierarchy, proportions, spacing, typography, surfaces, imagery, rail, and decorative layers are convincingly aligned.

## Stage 1 — Analyze before coding

Compare reference vs current implementation and document concrete mismatches in STATUS.md.

The current device screenshot is known to have these major problems:
- card composition is too small/short vertically and leaves a large empty area below;
- hero portrait is too small and circular instead of the large rounded rectangular portrait in the reference;
- member identity is compressed beside/around a tiny avatar instead of a strong two-column hero;
- CHECK-IN event pill is too small and visually weak;
- event time hierarchy is weaker than reference;
- Home rail active state is too small; reference uses a prominent green rounded active tile;
- rail proportions/icons/labels do not match reference hierarchy;
- Gold Plan surface is too small and not the strong gold/champagne band;
- ACTIVE surface is too compact;
- metric cards need the reference proportions and visual treatment;
- workout visualization must be a six-bar progression style, not an arbitrary count;
- the primary green Collect Payment CTA must be a prominent full-width action;
- intelligence cards currently appear too early/large and push the intended reference composition down;
- leaf/botanical composition is sparse and incorrectly placed;
- typography scale and weight are too small;
- back/header chrome differs from reference and currently has duplicated/competing back affordances;
- the card should visually occupy the usable mobile viewport like the reference, without a huge blank region.

## Stage 2 — Rebuild Natural Fresh Home composition

Reconstruct the Home state around this hierarchy:

1. Outer premium rounded card/shell with pale mint/white background and subtle green glass depth.
2. Header:
   - back button
   - BAD GYM leaf logo
   - BAD GYM
   - "Healthy People • Happier Lives"
   - notification icon
   - member/avatar control
3. Main content panel:
   - large CHECK-IN pill with icon
   - 4:03 PM / Just now hierarchy
   - large rounded rectangular member portrait
   - Yash Singh + verification badge
   - BG204
   - "Good Fitness Brighter You" decorative/brand message
   - botanical leaves integrated around hero
4. Gold Plan band:
   - crown/gold icon
   - Gold Plan
   - 12 Months
5. ACTIVE band:
   - green circular status icon
   - ACTIVE
   - 48 Days Left
6. Three equal metric cards:
   - 16/26 Attendance + 61% ring
   - ₹4,500 Payment Due + 3 days
   - 12 Workouts + six-bar progression
7. Full-width green "Collect Payment →" CTA.
8. Bottom botanical framing and "Small Steps Big Results" decorative message.
9. Rail remains vertically integrated at the left with Home prominently active.

Gold Plan + ACTIVE must remain common/visible in the Home state.

## Stage 3 — Responsive geometry

Target real device sizes:
- 360dp
- 375dp
- 390dp
- 412dp

Do not solve responsiveness by simply shrinking everything.

Use adaptive layout:
- portrait hero becomes smaller only when required;
- rail remains usable;
- text remains legible;
- CTA remains >=48dp touch target;
- content remains scrollable when required;
- no large dead blank region.

## Stage 4 — Interaction

Verify every rail item opens its actual data panel:
Home, Attendance, Plan, Payment, Trainer, Workout, Supplements, Nutrition, Services, History, Insight.

Menu transitions should be horizontal and approximately 180–250ms.

Preserve scroll state where practical.

## Stage 5 — Visual QA gate

After implementation:
1. build locally;
2. install/run on available device;
3. capture a screenshot;
4. compare it against docs/reference/member-intelligence-natural-fresh-reference.png;
5. fix visible discrepancies;
6. repeat until the reference hierarchy is closely reproduced.

Do NOT use the phrase "pixel perfect" unless screenshot comparison was actually performed.

## Stage 6 — Code quality

Keep:
- existing domain models;
- existing repository interfaces;
- theme token system;
- semantic state colors;
- adaptive menu architecture.

Do not delete unrelated BAD GYM functionality.

Avoid hardcoded business data outside preview/debug data.

## Completion

Only after local screenshot QA:
- update STATUS.md with exact comparison result;
- commit all changes;
- push to member-intelligence-v3;
- set this task STATUS to COMPLETED.

If blocked, set STATUS to BLOCKED and document the exact blocker.
