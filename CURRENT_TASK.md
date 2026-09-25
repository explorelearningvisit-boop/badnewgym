# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-STAGE-7.1-VISUAL-DATA-REDESIGN
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible execution
BRANCH: member-intelligence-v3
PROTOCOL_VERSION: 1.0
WORK_SPLIT_PROTOCOL: CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md v1.0

## Mission

Refine the completed Stage 7 Member Intelligence card with approximately +20dp vertical space and a visual-first redesign of all 11 menu panels so essential information is glanceable and not hidden/clipped.

## Authorization

Full specification:
docs/reference/STAGE_7_1_VISUAL_DATA_HEIGHT_PACKET.md

Previous Stage 7 implementation:
03d60a01d5ca1983fb5d2d83d8184dd30f6821eb

Known Stage 7 limitation remains:
rememberIsReducedMotion() is still a stub; full Settings.Global.ANIMATOR_DURATION_SCALE wiring is not part of this task unless required to preserve the reduced-motion contract.

## ChatGPT lane

Product/UX reasoning, visual-data grammar, geometry targets, accessibility, acceptance criteria, verification matrix and post-push review.

## Antigravity lane

Visible repository inspection, production Kotlin/Compose implementation, tests, build, device/runtime, screenshots, documentation, commit/push and exact evidence.

## Non-negotiables

- No Stage 8.
- No fake backend data.
- Preserve domain/repository/state boundaries.
- Preserve all 8 MI-V6 themes and semantic colors.
- Preserve Stage 6 menu/navigation behavior.
- Preserve Stage 7 depth/motion behavior.
- Centralize geometry changes in dimension tokens.
- No hidden essential content or clipped CTA/chart/legend.
- Visual-first; minimize prose and reading load.
- No heavy chart dependency without justification.
- No force-push/reset/discard.


## Additional user-directed design requirement

Reference direction: the generated 8-theme Member Intelligence concept image supplied in the current task.

Implementation must be SEQUENTIAL LAYER-BY-LAYER, not a single giant redesign:
L0 geometry/canvas → L1 background/material → L2 border/shape → L3 photo/identity → L4 event/state → L5 visual KPIs/analytics → L6 CTA → L7 typography → L8 visual assets → L9 menu/rail → L10 menu-specific visual content → L11 motion/depth → L12 8-theme matrix.

Start with ONE card, Natural Fresh, and prove it on-device before generalizing.

The reference image is inspiration, not a bitmap UI background. Use practical Compose-native/vector/lightweight assets, real MemberSnapshot data, responsive tokens and accessible semantics.

User explicitly rejects black/dark-black user-facing card backgrounds. The redesigned themes must use premium light/soft alternatives while preserving the theme personality.

Acceptance emphasis:
- empty-shell geometry first;
- then background only;
- then each layer in sequence;
- no hidden/clipped essential content;
- visual-first analytics;
- no fake data;
- no giant raster UI;
- no tiny text;
- verify each layer before advancing.
