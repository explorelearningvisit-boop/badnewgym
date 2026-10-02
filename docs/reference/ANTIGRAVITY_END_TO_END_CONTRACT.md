# BAD GYM — Google Antigravity End-to-End Execution Contract

## Mission

Treat this repository as the source of truth for the BAD GYM implementation. When a reference image is supplied by the user, do not approximate the whole screen in one pass. Convert the reference into a sequence of small, verifiable implementation stages and execute them in order.

The goal is not to write a design report. The goal is to modify the repository, build the app, verify behavior and leave working code ready to pull and run.

## Non-negotiable rules

1. The supplied reference image is the visual source of truth for that screen.
2. Do not invent a different visual language, layout, card style, spacing system, color system, or navigation pattern unless the repository requirements explicitly require it.
3. Never implement the entire reference screen in one giant pass.
4. Decompose the image into atomic components and finish one component before moving to the next.
5. After every meaningful component change: build -> run -> capture -> compare -> fix -> re-verify.
6. Do not declare success from compilation alone.
7. Do not declare success from a screenshot that has not been visually compared with the reference.
8. Do not leave placeholder UI, fake interactions, dead buttons, duplicated cards, or unexplained hard-coded mock behavior.
9. Preserve BAD GYM domain semantics and entitlement rules.
10. Do not modify OPPO/mobile-control tooling or unrelated project/company assets.
11. Keep changes inside BAD GYM application, test, asset, and documentation paths.
12. Never silently rewrite unrelated existing work.
13. If a requirement conflicts with existing architecture, inspect the architecture first and implement the smallest correct integration.
14. Record exact commit SHA, changed files, build/test status, runtime/device status, screenshot evidence, and unresolved issues at each completed stage.

## Model selection

Before substantial implementation, inspect the Gemini models available in the current Antigravity environment.

Select the highest-capability Gemini model available for software engineering, visual reasoning, repository modification, and debugging. Prefer a Pro/reasoning-capable model over a speed-optimized model when both are available. Do not assume a model name that is not actually available in the current environment.

Use the selected model consistently for the active implementation stage unless the environment requires a different model.

## Stage 0 — Repository and reference lock

Before editing:

- Pull the target branch.
- Read:
  - AI_COLLABORATION_PROTOCOL.md
  - CURRENT_TASK.md if present
  - ANTIGRAVITY_RUN.md
  - relevant feature/domain handoff documents
- Inspect the existing implementation instead of replacing it blindly.
- Identify the exact screen/component represented by the supplied image.
- Record the reference image dimensions and visible content.
- Identify whether the reference represents one state or one of several states.
- Create a reference checklist containing every visible region.

Do not proceed until the repository structure and target implementation path are understood.

## Stage 1 — Reference decomposition

Break the supplied image into atomic regions.

For each region record:

- component name
- approximate bounding box
- width/height relationship
- outer margins
- internal padding
- alignment
- typography hierarchy
- font weight
- text size
- line height
- color
- opacity
- border
- corner radius
- shadow/elevation
- icon geometry
- image/asset requirements
- interaction
- state
- data source
- animation if visible

Typical decomposition:

1. screen/container/background
2. top/header
3. identity/member area
4. status/badge area
5. primary action
6. metric blocks
7. event/visit block
8. payment block
9. attendance block
10. rail/navigation
11. secondary content
12. footer/overflow/action controls
13. overlays
14. loading/empty/error states

Do not assume these exact components exist; adapt to the actual reference.

## Stage 2 — Asset extraction and micro-design

For every visual asset:

- reuse an existing repository asset when it matches;
- otherwise create the required asset using the appropriate connected asset/image workflow;
- store it in the correct BAD GYM asset path;
- verify its density, crop, transparency, and rendering;
- never substitute a random icon/image just to make the screen compile.

If an image contains multiple useful visual elements, split the work into separate assets/components where that improves implementation accuracy.

## Stage 3 — Atomic implementation loop

Implement one atomic component at a time.

For EACH component execute this loop:

A. Inspect current code.
B. Implement the component.
C. Compile/build the affected module.
D. Run the app on the target device/emulator when available.
E. Navigate to the exact target state.
F. Capture a screenshot.
G. Compare screenshot with the supplied reference.
H. List discrepancies by:
   - geometry
   - spacing
   - typography
   - color
   - iconography
   - content
   - state
   - interaction
   - animation
I. Fix the discrepancies.
J. Rebuild.
K. Capture again.
L. Re-compare.
M. Only then mark that component verified.

Never move to the next component while a major discrepancy remains in the current component.

## Stage 4 — State matrix

For every interactive/event-driven component, build and verify the required state matrix.

At minimum inspect relevant BAD GYM states such as:

- CHECK_IN
- CHECK_OUT
- PAYMENT
- WALK_IN
- TRIAL
- overdue payment
- paid/current payment
- attendance present
- no attendance
- trainer present
- trainer absent
- optional service present
- optional service absent
- loading
- empty
- error
- entitlement granted
- entitlement revoked/locked

Only implement states relevant to the actual feature, but do not assume one screenshot is the only valid state.

## Stage 5 — Interaction and logic

After visual components pass individually:

- connect real domain state;
- connect navigation;
- connect tap actions;
- connect swipe/scroll behavior;
- connect event/visit resolution;
- connect entitlement checks;
- connect loading/error/empty states;
- connect persistence where required;
- remove temporary mock-only logic.

Every visible control must either work or be intentionally non-interactive and visually communicate that fact.

## Stage 6 — Full-screen integration

Assemble all verified atomic components.

Then perform a complete-screen verification against the supplied reference.

Check:

- overall geometry
- safe areas
- viewport usage
- rail width
- card width
- vertical rhythm
- content density
- clipping
- overflow
- scrolling
- keyboard/input behavior
- animation
- touch targets
- accessibility labels where appropriate

Do not use a fixed design assumption from an old handoff if the current implementation or supplied reference has superseded it. The current verified implementation is authoritative.

## Stage 7 — Device verification

Target device: Xiaomi 11i when available.

Use the repository's phone-native device-agent workflow when available.

Verification sequence:

1. build APK
2. install/run
3. open BAD GYM
4. navigate to target screen
5. capture screenshot
6. compare against reference
7. perform representative taps
8. perform representative swipes
9. verify state transitions
10. repeat after fixes

If a physical device is not reachable from the current Antigravity environment, do not fake device verification. Record the exact limitation and complete all available emulator/build/static verification.

## Stage 8 — Regression verification

Run:

- clean build
- unit tests
- relevant instrumentation/UI tests if available
- lint/static checks available in the project
- target-screen smoke flow
- affected navigation paths

Check for:

- compile errors
- crashes
- broken imports
- broken resources
- duplicate UI
- dead controls
- entitlement bypass
- incorrect event semantics
- accidental changes outside scope

## Stage 9 — Evidence and handoff

Before claiming completion, produce:

- exact HEAD SHA
- branch
- changed files
- build result
- unit-test result
- UI/instrumentation result
- device result
- screenshot evidence
- reference-vs-implementation discrepancies remaining
- unresolved issues
- exact next command for the user if any action remains

Completion means verified implementation, not merely generated code.

## Git discipline

Use small logical commits where practical.

Commit messages should describe the completed stage, for example:

- `ui: implement reference header atomic component`
- `ui: verify payment state against reference`
- `ui: integrate member card reference components`
- `test: verify visit state matrix`

Do not rewrite history destructively unless explicitly requested.

## User interaction contract

The user should only need to provide the reference image and high-level goal.

Antigravity should infer the implementation workflow from this contract, execute the stages, and report concise progress.

Do not repeatedly ask the user to manually translate the image into technical specifications when the information can be extracted from the image and repository.

## Final acceptance condition

The task is complete only when:

- the reference has been decomposed;
- atomic components have been implemented;
- each component has been verified;
- the full screen has been verified;
- interactions and logic have been connected;
- relevant states have been checked;
- build/tests pass;
- device verification has been performed when the device is reachable;
- changes are committed;
- no known major visual mismatch remains unexplained.

Never report "done" merely because the code compiles.
