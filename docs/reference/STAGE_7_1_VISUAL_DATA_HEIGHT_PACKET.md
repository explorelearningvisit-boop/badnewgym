# BAD GYM — Stage 7.1 Visual Data + Card Height Packet
## MI-STAGE-7.1-VISUAL-DATA-REDESIGN

Status: READY_FOR_EXECUTION
Branch: member-intelligence-v3
Execution: Google Antigravity GUI only
Protocol: MASTER_AI_EXECUTION_CONTRACT.md v1.0
Parallel protocol: CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md v1.0

## Objective

Refine the completed Stage 7 Member Intelligence card so the detail card has more usable vertical space and every menu communicates primarily through visual, pictorial, glanceable data rather than dense text.

This is a focused UX/data-visualization refinement. Do not start Stage 8.

## User requirements

1. Increase card height by approximately 20dp.
2. Ensure bottom content is not accidentally hidden or clipped in any menu.
3. Redesign menu content so important information remains visible at a glance.
4. Prefer charts, rings, progress bars, timelines, icon-led status blocks, pictorial indicators and compact visual summaries over paragraphs.
5. A child or older user should be able to understand the main state from the visual hierarchy with minimal reading.
6. Preserve the existing Member Intelligence data/domain/repository boundaries and use real available snapshot data. No fake backend data.
7. Preserve all 8 MI-V6 themes, semantic colors, contrast resolver, Stage 6 navigation and Stage 7 depth/motion.

## ChatGPT lane

- Define information hierarchy and visual-data grammar.
- Define the ~20dp geometry adjustment.
- Define menu-by-menu visual presentation requirements.
- Define no-clipping/no-hidden-content acceptance criteria.
- Define accessibility, semantic meaning, contrast and reduced-motion constraints.
- Define verification matrix and screenshot requirements.
- Review Antigravity's pushed implementation and create corrective tasks if needed.

## Antigravity lane

- Inspect current card geometry and all 11 menu panels.
- Implement the height adjustment in the centralized dimension tokens; do not scatter literals.
- Refactor menu presentation using existing real data models.
- Build reusable visual primitives where practical instead of duplicating chart code.
- Run unit tests, build and connected-device runtime.
- Capture evidence for all 11 menus and all 8 themes.
- Document model/configuration, implementation decisions, limitations, tests and runtime evidence.
- Commit and push to member-intelligence-v3.

## Geometry

Current Default geometry is approximately:
- card: 312 x 406dp
- detail: 340 x 443dp

Current responsive variants:
- Compact: 296 x 390dp; detail 320 x 420dp
- Default: 312 x 406dp; detail 340 x 443dp
- Expanded: 328 x 422dp; detail 360 x 460dp

Target adjustment:
- add approximately +20dp height to browse and detail heights in each centralized responsive token:
  - Compact: 410dp / 440dp
  - Default: 426dp / 463dp
  - Expanded: 442dp / 480dp
- Verify the resulting card still fits the intended mobile viewport and carousel side-peek behavior.
- Do not increase width unless runtime evidence shows a separate clipping problem.
- If exact +20dp causes a device-specific collision, preserve the intent (more usable vertical content) and document the smallest safe adjustment.

## No-hidden-content contract

For every menu:
- persistent identity/event/state header remains visible;
- content viewport must be bounded correctly;
- no text, chart, CTA, legend or status indicator may be clipped by the card;
- verticalScroll may remain as a safety fallback, but primary content should fit the first viewport whenever practical;
- do not place essential information below an invisible or unreachable region;
- long values must wrap/ellipsize intentionally;
- charts must have visible labels or icon semantics;
- CTA must remain reachable;
- bottom spacing must account for the debug/runtime badge or remove that row from production content if it is not required;
- verify at 360/375/390/412dp reasoning and on the Xiaomi Redmi Note 11 when connected.

## Visual-data grammar

Prefer these primitives:
- KPI number + icon + semantic state
- circular progress/ring for completion/attendance
- horizontal progress for plan/payment/session usage
- mini bar chart for time-series/load
- donut/proportion chart when composition matters
- timeline/event rail for history
- status matrix/grid for service/payment/attendance state
- icon-led schedule blocks for trainer/workout
- macro/proportion visualization for nutrition
- compact action tile for CTA
- trend arrow/delta where real data supports it

Rules:
- visual first, text second;
- one clear title per section;
- avoid paragraph-like explanatory copy;
- never invent precision that is not present in the domain data;
- if data is unavailable, show a clear visual empty state rather than fabricated numbers;
- do not encode meaning with color alone; pair color with icon/shape/label;
- preserve semantic state colors independently of theme accent;
- maintain WCAG-aware text contrast.

## Menu targets

### Home
Visual executive summary:
- attendance/progress ring;
- payment state/amount visual;
- plan status;
- workout/engagement mini metrics;
- primary intelligence signal as a compact visual alert/action;
- one dominant CTA.

### Attendance
- attendance rate ring;
- present/absent/late breakdown;
- recent attendance timeline or compact day strip;
- streak/consistency visual;
- avoid long event prose.

### Plan / Membership
- plan status hero;
- days remaining progress;
- start/end date visual;
- renewal/expiry warning indicator;
- benefits as icon tiles.

### Payment
- amount/status hero;
- paid/due/overdue visual split;
- payment history mini timeline/bar;
- next due/renewal visual;
- CTA remains visible.

### Trainer
- trainer identity block;
- session usage ring;
- next/last session timeline;
- focus as icon-led chips.

### Workout
- routine hero;
- session duration/calories KPI tiles only when actual data exists;
- recent workout load mini bar chart;
- weekly target progress;
- last workout visual.

### Supplements
- current product/status visual;
- purchase/price/date compact timeline;
- supply/reorder progress;
- empty state when no history.

### Nutrition
- plan/subscription status;
- macro visualization where actual macro data exists;
- hydration progress;
- renewal visual;
- avoid paragraph text.

### Services
- service status matrix/cards;
- active/expired visual state;
- expiry/progress indicators;
- price only where available.

### History
- icon-led chronological timeline;
- event-type pictograms;
- date/time compact;
- avoid repeated InfoCards with verbose rows.

### Insight
- priority visual stack;
- P0/P1/P2 represented by icon + semantic color + label;
- evidence as compact supporting metric;
- actionable signal and CTA;
- no wall of text.

## Accessibility / usability

- Important meaning must survive without animation.
- Reduced-motion path remains respected.
- Charts need content descriptions or equivalent semantic descriptions.
- Touch targets remain production appropriate.
- Do not use tiny text to force-fit content.
- Visual hierarchy must work in light and dark/material themes.
- Do not rely on color alone for status.

## Performance

- Reuse lightweight Compose primitives.
- Avoid heavy chart libraries unless clearly justified.
- Prefer Canvas/Compose drawing for simple charts.
- Avoid per-frame allocations and continuously running animations.
- Preserve Stage 7 graphicsLayer performance characteristics.

## Verification

Required:
1. Relevant unit tests.
2. testDebugUnitTest.
3. assembleDebug.
4. Connected-device runtime when Xiaomi Redmi Note 11 is available.
5. All 11 menus.
6. All 8 themes.
7. 360/375/390/412dp reasoning or previews.
8. No clipping/overlap/hidden CTA.
9. Back navigation and Stage 6 menu transitions.
10. Stage 7 depth/motion regression.
11. Accessibility semantics for visual summaries.
12. Verify actual content against MemberSnapshot/domain data; no fabricated backend values.

Required screenshots:
- docs/screenshots/stage7_1_home_visual.png
- docs/screenshots/stage7_1_attendance_visual.png
- docs/screenshots/stage7_1_payment_visual.png
- docs/screenshots/stage7_1_menu_matrix.png
- docs/screenshots/stage7_1_theme_matrix.png

Acceptance requires actual implementation, tests, build, runtime/evidence, documentation, commit, push and remote SHA verification.


## Layer-by-layer implementation contract — REQUIRED SEQUENCE

The reference image is a visual direction, not a literal pixel asset to blindly reproduce. Implementation must be practical on a real Android device and use real domain data.

### Master sequence

Implement and verify one layer before moving to the next:

**L0 — Geometry / canvas**
- Start from an empty card shell.
- Establish responsive width/height, safe insets, corner radius, internal grid and clipping.
- Target the +20dp height adjustment from this packet.
- Prove that the empty shell itself fits 360/375/390/412dp reasoning.

**L1 — Background / material only**
- Remove all content temporarily.
- Implement only the card background/material.
- Match the reference mood using Compose-native gradients, translucent surfaces, subtle texture/vector ornaments and elevation.
- No text, photos, buttons, charts or menu icons at this stage.
- Do NOT use a bitmap screenshot as the entire card background.
- Keep assets lightweight and scalable.
- Natural Fresh is the first reference theme to implement.
- Black/dark-black backgrounds are explicitly prohibited for the user-facing redesign. Replace dark reference variants with premium light/soft alternatives.
- Suggested palette direction: ivory/mist white + fresh emerald/mint + soft aqua + restrained warm lime; exact values must be theme-tokenized.

**L2 — Border / shape / edge treatment**
- Add corner radius, inner edge, subtle highlight, border and depth response.
- Border must remain readable without becoming a glowing outline everywhere.
- Theme material, not arbitrary decoration, controls the edge.

**L3 — Identity / photo layer**
- Add member photo, verification badge and identity block.
- Photo must remain undistorted, legible and stable under Stage 7 parallax.
- Use actual snapshot photo data; fallback avatar/vector when unavailable.
- Do not hardcode the example person's identity from the reference image.

**L4 — Event / state layer**
- Add event badge, timestamp, membership state and semantic status.
- Semantic state colors remain independent of the theme accent.
- Meaning must be understandable with icon + label + shape, not color alone.

**L5 — Visual KPI / analytics layer**
- Add only real-data visuals: attendance ring, payment status, plan progress, workout/session mini chart, etc.
- Use reusable Compose primitives.
- Prefer compact visual encodings over KeyValue text rows.
- No fabricated values to make the card look like the reference.

**L6 — CTA / action layer**
- Add exactly one dominant contextual CTA on the browse card.
- Use a production touch target and pressed state.
- CTA must remain visible at the bottom and never be clipped.
- Label must derive from actual state/action.

**L7 — Typography layer**
- Add brand/member/title typography after geometry and visuals are stable.
- Use hierarchy: event → member → time/state → decision signal → action.
- Keep secondary text short.
- No decorative handwritten text unless it is implemented as a scalable vector/typographic asset and does not reduce accessibility.
- Never use accent color as arbitrary body text.

**L8 — Decorative visual asset layer**
- Add restrained botanical/vector/gradient ornaments only where they improve recognition.
- Decorative assets must stay behind information and never compete with KPI/chart content.
- Prefer vector/Canvas or small local assets over huge raster images.
- All 8 themes receive their own material personality without changing information architecture.

**L9 — Menu / rail layer**
- Add the 11-menu navigation only after the card content is stable.
- Menu icons are icon-first, label-second.
- Selected state uses shape/elevation/indicator + semantic/accent color; never color alone.
- Menu rail must not consume so much width that content becomes cramped.

**L10 — Menu-specific visual content**
- Implement each menu one by one, in this order:
  Home → Attendance → Plan → Payment → Trainer → Workout → Supplements → Nutrition → Services → History → Insight.
- For each menu: empty state → visual summary → supporting detail → CTA (if applicable).
- Verify no clipping before proceeding to the next menu.
- Reuse visual primitives; do not create 11 unrelated mini design systems.

**L11 — Motion / depth integration**
- Re-enable Stage 7 depth/parallax/press behavior after static composition is correct.
- Reduced-motion behavior must preserve meaning.
- Do not use motion to hide or reveal essential information.

**L12 — Theme matrix**
- Once Natural Fresh is production-stable, adapt the same structure to all 8 themes:
  Natural Fresh, Futuristic Neon, Minimal Slate, Glass/Frost, Premium Ivory, Vibrant Gradient, Beast Mode (lightened; no black), Purple Royal.
- Do not literally reproduce the dark/black reference themes.
- All themes must remain readable and practical.

### Layer gate

Antigravity must record in its completion packet:
- which layer was implemented;
- files changed;
- test/build result;
- screenshot/evidence;
- any deviation;
- why the next layer is safe to start.

If a layer fails visual/runtime verification, stop at that layer and fix it before proceeding.

## Practicality rules

- Reference image is inspiration for composition/material, not a requirement to use every decorative element.
- Real Android layout constraints take precedence over image fidelity.
- No giant bitmap background containing UI text.
- No rasterized text where normal Compose text is appropriate.
- No fake charts or invented data.
- No tiny text to squeeze more content into the card.
- No dark/black background for the redesigned user-facing themes.
- No new chart/navigation framework unless a concrete limitation requires it.
