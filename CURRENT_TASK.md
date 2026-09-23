# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-V5-MASTER-VISUAL-RECONSTRUCTION-01
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission

Execute the combined BAD GYM Master UI/UX Reconstruction specification now.

Authoritative documents:
- docs/reference/MASTER_UI_UX_RECONSTRUCTION_PROMPT.md
- docs/reference/MEMBER_INTELLIGENCE_8_THEME_SPEC.md
- docs/reference/NATURAL_FRESH_VISUAL_SPEC.md
- docs/reference/current-device-output.png

The user-supplied reference board contains EIGHT Member Intelligence visual states:
1 Natural Fresh
2 Futuristic Neon
3 Minimal Dark
4 Glassmorphism
5 Premium 3D
6 Vibrant Gradient
7 Gym Beast Mode
8 Purple Royal

Treat the board as one shared component architecture + eight theme definitions, NOT eight unrelated screens.

## Non-negotiable execution order

1. Inspect repo, architecture and current MI implementation.
2. Read AGENTS.md, .agents/rules/00-badgym-github-loop.md and the two master reference documents.
3. Analyze current implementation against the reference specifications before coding.
4. Build/rebuild shared Member Intelligence component geometry first.
5. Implement the eight theme definitions using semantic design tokens.
6. Implement asset pipeline requirements: extract/reuse available assets, upscale when required, transparent PNG/vector where appropriate, manifest all assets.
7. Implement tier x event x member-state composition without duplicating screens.
8. Implement responsive geometry for 360dp, 375dp, 390dp and 412dp.
9. Add DEBUG-only runtime identity showing MI-V5 + short Git SHA; it MUST disappear from release builds.
10. Build locally, install and run on available device/emulator.
11. Capture real screenshots.
12. Compare screenshots against the available repository references/specification. Do not call compilation or navigation success visual completion.
13. Fix CRITICAL discrepancies first, then MAJOR, then MINOR.
14. Repeat screenshot -> compare -> fix until visually close.
15. Verify all eight themes and the Natural Fresh home hierarchy.
16. Update STATUS.md with exact work, commands/results, runtime build identity, screenshot verification and remaining discrepancies.
17. Commit and push to member-intelligence-v3.
18. Only set CURRENT_TASK to COMPLETED after genuine verification. If exact reference comparison is blocked because a required reference asset is unavailable locally, set BLOCKED and state that exact blocker instead of claiming success.

## Critical visual rules

- Do not replace custom visual elements with generic Material UI.
- Do not shrink everything to solve responsiveness.
- Do not allow intelligence alerts to overpower the reference Home composition.
- Natural Fresh must retain large rectangular portrait, strong CHECK-IN pill, prominent rail, Gold Plan band, ACTIVE band, three equal metrics, six-bar workout progression, full-width green CTA and botanical framing.
- Preserve semantic state visibility even when tier/theme colors are decorative.
- Use one shared Member Intelligence system; do not create eight disconnected screens.
- Do not invent unavailable production data.
- Preserve unrelated BAD GYM functionality.

## Version truth

The device must visibly prove the running build:
MI-V5
BUILD <short-git-sha>
DEBUG

Use BuildConfig.DEBUG (or equivalent). Production/release must hide this marker.

## Completion gate

A build passing is NOT enough. Completion requires real device/emulator verification and screenshot comparison. No "pixel perfect" claim without actual comparison evidence.

