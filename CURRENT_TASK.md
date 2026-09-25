# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-STAGE-7-DEPTH-MOTION-LAYER
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible execution
BRANCH: member-intelligence-v3
PROTOCOL_VERSION: 1.0
WORK_SPLIT_PROTOCOL: CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md v1.0

## Mission

Move Member Intelligence from the completed Stage 6 deep-menu system into Stage 7: production 2.5D depth and motion.

This is progressive enhancement, not a full 3D rewrite.

## Work split

### ChatGPT lane
- product/UX reasoning;
- depth and motion specification;
- design token ranges;
- theme mapping;
- accessibility/reduced-motion rules;
- performance guardrails;
- acceptance criteria;
- verification matrix;
- post-push review.

### Antigravity lane
- repository inspection;
- production Kotlin/Compose implementation;
- tests;
- build;
- device/runtime verification;
- screenshots;
- documentation;
- commit/push;
- exact evidence report.

### Merge/review gate
ChatGPT's specification is durable in GitHub. Antigravity implements it on the authorized branch. After push, ChatGPT reviews the remote SHA/evidence. Any defect becomes a new corrective task; no silent production-code edits by ChatGPT.

## Scope

1. Progressive 2.5D card depth.
2. Carousel focus/parallax.
3. Detail-shell depth separation.
4. Press/selection micro-interactions.
5. Tokenized motion.
6. Reduced-motion handling.
7. All 8 MI-V6 theme/material personalities.
8. Stage 6 menu/navigation regression.
9. Performance-aware Compose implementation.

## Technical constraints

- Prefer Compose-native graphicsLayer/drawing/animation.
- No real glTF/SceneView/Filament dependency by default.
- No fake backend data.
- No new navigation architecture.
- Preserve Member Intelligence state/domain/repository boundaries.
- Preserve contrast resolver and semantic colors.
- Preserve existing autonomous/headless bridge disabled state.
- Do not start Stage 8.
- No force-push/reset/discard.
- No unrelated refactors.

## Full specification

See docs/reference/STAGE_7_PARALLEL_WORK_PACKET.md.

## Acceptance

Antigravity must not mark COMPLETED until implementation, tests/build/runtime/evidence/documentation/commit/push/remote verification are actually complete.
