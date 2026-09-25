# BAD GYM — Stage 7 Parallel Work Packet
## MI-STAGE-7-DEPTH-MOTION-LAYER

Status: READY_FOR_EXECUTION
Branch: member-intelligence-v3
Execution: Google Antigravity GUI only
Protocol: MASTER_AI_EXECUTION_CONTRACT.md v1.0
Parallel protocol: CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md v1.0

## Objective

Add a production-grade 2.5D depth and motion layer to Member Intelligence without damaging the MI-V6 semantic/material system or Stage 6 deep-menu behavior.

This is progressive enhancement, not a decorative 3D rewrite.

## Design principle

Depth must communicate:
- hierarchy;
- focus;
- selection;
- continuity;
- physical material;
- interaction feedback.

Do not add motion/depth merely because it looks impressive.

Use Compose-native graphicsLayer/drawing/transforms where sufficient. A real 3D/glTF/SceneView dependency is NOT authorized by default. Introduce real 3D only if a concrete product requirement cannot be met with Compose-native 2.5D and after documenting the reason, dependency impact and fallback.

## ChatGPT lane — completed in this packet

ChatGPT defines:
- depth hierarchy;
- motion hierarchy;
- token ranges;
- interaction behavior;
- reduced-motion behavior;
- performance guardrails;
- theme mapping;
- acceptance matrix;
- verification plan.

## Antigravity lane — execute

Antigravity must:
- inspect current MI-V6 and Stage 6 implementation;
- identify actual carousel/card/menu animation points;
- implement the authorized 2.5D layer visibly;
- preserve all existing menu/navigation semantics;
- run tests/build/device verification;
- capture evidence;
- document actual model/configuration;
- commit and push.

## Depth system

### Layer 0 — Canvas
Existing theme background remains authoritative.

### Layer 1 — Card surface
Use existing theme material/surface tokens.
Add only subtle elevation/shadow/material response.

### Layer 2 — Identity/photo
Portrait may receive controlled parallax or scale response.
Never distort identity or reduce readability.

### Layer 3 — Focus/selection
Selected card can use:
- scale approximately 1.00–1.03;
- small elevation increase;
- subtle translation/parallax;
- accent edge/glow only where the theme supports it.

### Layer 4 — Interactive content
CTA/pressed/selected states may use small scale/elevation response.
No bounce-heavy or attention-stealing animation.

## Motion tokens

Default target ranges:
- press feedback: 80–140ms;
- micro state transition: 120–180ms;
- card focus transition: 180–260ms;
- menu transition: preserve Stage 6 target 180–250ms;
- carousel settle: 220–360ms;
- depth/parallax interpolation: smooth, interruptible;
- no infinite decorative animation by default.

Use existing MotionTokens if available. Extend tokens rather than scattering literal durations.

## 2.5D behavior

### Browse carousel
- Center/focused card: scale 1.00–1.03.
- Adjacent cards: slightly reduced scale/alpha/elevation.
- Horizontal position may drive a very small rotationY/parallax effect.
- No card may clip important text.
- Side peek remains intact.
- Gesture interruption must be safe.

### Detail card
- Persistent header remains visually stable.
- Menu content transitions preserve Stage 6 slide+fade behavior.
- Content panel may have subtle depth separation from the persistent shell.
- Menu switching must not cause identity/header jump.

### Press
- Pressed card/CTA may compress slightly (target ~0.98–0.99 scale).
- Return must be smooth and cancellable.
- Do not use long spring tails.

### Selection
- Selected state should be distinguishable without relying only on color.
- Use elevation/shape/scale/indicator together with semantic color.

### Theme mapping
Depth/material must respect all 8 MI-V6 themes:
1. Natural soft matte/frosted
2. Neon dark glass + cyan edge
3. Minimal matte slate
4. Glass translucent frost
5. Premium ivory + subtle metallic edge
6. Vibrant soft gradient
7. Beast graphite + crimson energy edge
8. Purple amethyst glass

Semantic state colors remain independent from theme accent colors.

## Accessibility

- Reduced-motion preference must suppress or minimize non-essential transforms.
- No meaning may depend solely on animation.
- Selected/pressed/disabled states remain understandable statically.
- Touch targets remain production-appropriate.
- Do not reduce contrast through translucent overlays.

## Performance

Prefer:
- Modifier.graphicsLayer for independent transforms;
- drawing modifiers where appropriate;
- tokenized animation specs;
- state-driven layer updates;
- no per-frame object allocation;
- no unnecessary offscreen compositing;
- no large bitmap duplication;
- no continuously running animations unless required.

Check recomposition/frame behavior where practical.

## Device target

Primary runtime regression device:
- Xiaomi Redmi Note 11, when connected.

Also reason about:
- 360dp;
- 375dp;
- 390dp;
- 412dp widths.

## Required verification

1. Relevant unit tests.
2. testDebugUnitTest.
3. assembleDebug.
4. Connected-device runtime when available.
5. Browse carousel depth.
6. Detail card depth.
7. Stage 6 menu transition regression.
8. Back navigation regression.
9. Pressed/selected/disabled states.
10. Reduced-motion behavior where testable.
11. All 8 themes.
12. No clipping/overlap.
13. No obvious frame/jank regression.
14. No new unnecessary dependency unless justified.

## Evidence

Capture:
- docs/screenshots/stage7_depth_browse.png
- docs/screenshots/stage7_depth_detail.png
- docs/screenshots/stage7_motion_states.png
- docs/screenshots/stage7_theme_matrix.png

If runtime screenshots differ because of device constraints, record the exact limitation rather than fabricating the requested evidence.

## Acceptance gate

Stage 7 is complete only when:
- 2.5D depth is visibly implemented;
- motion communicates hierarchy/interaction;
- MI-V6 material semantics remain intact;
- Stage 6 menus remain functional;
- all 8 themes regress cleanly;
- tests/build pass;
- device verification is performed when available;
- screenshots/evidence are committed;
- documentation contains exact SHA/model/tests/runtime;
- authorized branch is pushed and remote HEAD verified.

No Stage 8 work in this task.
