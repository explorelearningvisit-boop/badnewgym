# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-STAGE-03-HARDENING-RECONCILIATION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Why this task exists

Stage 3 was marked COMPLETED by the previous Antigravity run, but ChatGPT performed a repository-level reconciliation and found a material mismatch between the Stage 3 verification report and the actual Kotlin source currently present on `member-intelligence-v3`.

The current `MemberIntelligenceScreen.kt` still contains the older compact implementation with:
- inline `ThemeSkin` tokens
- simple Material icons
- text-only member portrait placeholder (`YS`)
- generic menu panels
- no verified asset-driven portrait rendering
- no clearly separated production theme engine
- no evidence in the source that all claimed Stage 3 visual overhaul changes are actually present

Therefore Stage 3 is NOT accepted as production-complete yet.

Do not start Stage 4 until this reconciliation is implemented and verified.

## Primary objective

Reconcile the actual code/assets with the Stage 3 acceptance report and the user-provided 8-theme reference board.

The result must be production-ready Jetpack Compose code, not a documentation-only correction.

## Required work

### 1. Inspect before editing

Read:
- AGENTS.md
- .agents/rules/00-badgym-github-loop.md
- docs/reference/MI_V5_DESIGN_COMMUNICATION.md
- docs/reference/MI_V5_PRODUCTION_ROADMAP.md
- docs/reference/MEMBER_INTELLIGENCE_8_THEME_SPEC.md
- BAD_GYM_MEMBER_INTELLIGENCE_READY/preview/ALL_8_THEMES_CONTACT_SHEET.png
- current MemberIntelligenceScreen.kt and all referenced assets

Do not trust the previous STATUS.md claims without reconciling them against source.

### 2. Preserve the approved product architecture

Keep:
- compact browse carousel
- bounded detail state
- persistent identity/event header
- vertical rail only in bounded detail
- side-peek browse context
- Android Back -> browse
- no full-screen member card
- no revenue/transaction dashboard inside member card

Do NOT replace the architecture with a new screen system.

### 3. Implement a real centralized theme system

Move theme semantics out of one monolithic screen where appropriate.

Create/strengthen centralized theme tokens for all 8:
1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

Each theme must define:
- canvas/background
- surface / elevated surface
- glass alpha
- border/highlight
- primary/secondary accent
- text/muted text
- glow/shadow
- CTA treatment
- portrait treatment
- decorative treatment

Preserve the reference-board color relationships. Do not flatten all themes into the same generic Material surface.

### 4. Fix the actual asset pipeline

Use the repository assets instead of placeholder text or generic substitutes.

Portraits:
- original/member source
- card crop
- thumbnail
- face-aware crop where possible

Use:
- vector/SVG for crisp UI geometry
- transparent PNG/WebP for painterly/decorative elements
- Compose Canvas for rings, bars, glows and gradients
- 3D only where isolated and justified

Do not invent fake product/portrait assets when an existing repository asset is available.

### 5. Stage 3 semantic intelligence

Preserve/strengthen:
- NORMAL / SILVER / GOLD / PREMIUM / VIP_ELITE
- ACTIVE
- PAYMENT_DUE
- PAYMENT_OVERDUE
- EXPIRED
- FROZEN
- NEW/WALK-IN
- TRAINER/PT_ACTIVE
- COMPLAINT/SAFETY/CRITICAL

Urgent state must override decorative tier treatment.

Never use color alone: icon + label + shape/badge are mandatory.

### 6. Bounded geometry

Use measured hardware geometry as the baseline, but tune only when visually justified:
- browse approximately 247dp default / up to 261dp expanded
- detail approximately 291dp default / up to 303dp expanded
- browse height approximately 356dp
- detail height approximately 372dp
- preserve visible adjacent-card peek
- preserve >=48dp interactive targets

Never expand to full screen.

### 7. Portrait hierarchy

The current source's `YS` placeholder is not acceptable.

Render the actual repository member portrait asset.

Browse:
- approximately 72–92dp width where geometry allows
- clear face + shoulders
- verification badge
- tier/status treatment

Detail:
- persistent portrait + name + member ID + event + time + membership/state

### 8. Menu quality

All 11 menus must remain useful and bounded:
HOME, ATTENDANCE, PLAN, PAYMENT, TRAINER, WORKOUT, SUPPLEMENTS, NUTRITION, SERVICES, HISTORY, INSIGHT.

No generic repeated "Signal 1/2/3/4" placeholders.

Use only available truthful snapshot data. If data is unavailable, use an explicit meaningful empty state.

### 9. Motion

Keep:
- 180–220ms menu crossfade + directional shift
- local ring/bar entrance
- restrained rail indicator motion
- count morph where meaningful
- pulse only for new/critical state
- reduced-motion support

No continuous auto-animation.

### 10. Reference fidelity

Reconstruct visual concepts from the 8-theme board:
- glass
- matte
- metallic
- glow
- inner highlight
- soft shadow
- gradients
- leaves
- sparks
- gold ornaments
- claws/diamonds where appropriate
- CTA shape
- event pill
- verification badge
- tier badge

Do not make every theme look like Natural Fresh or generic Material 3.

### 11. Black-background clarification

The previous report claims both:
- a stable `#0C1017` dashboard background, and
- complete eradication of `#0C1017` / black backgrounds.

Resolve this contradiction in code and documentation.

Important:
- Dark themes may legitimately use dark surfaces.
- Do NOT interpret "no black background" as "remove all dark-theme styling".
- Preserve the reference board's intended dark themes.
- Global dashboard color must remain stable while local member theme styling remains local.

### 12. Asset production pack

Add/reconstruct any missing lightweight production assets needed for the 8 themes.

Prefer small, optimized assets.
Avoid huge raster packs.
Document each asset's intended use and source/reference.

### 13. Tests / QA

On the same physical device:
- testDebugUnitTest
- assembleDebug
- install
- launch
- verify no crashes/layout exceptions

Verify:
- browse
- bounded detail
- Back
- side peek
- actual portrait
- all 11 menus
- at least 5 required semantic states
- at least 8 theme skins
- global dashboard does not recolor unexpectedly
- urgent rail highlight
- readable typography
- 48dp targets

### 14. Required screenshots

Replace/update with fresh physical-device evidence after reconciliation:
- docs/screenshots/stage3_reconciled_browse.png
- docs/screenshots/stage3_reconciled_natural.png
- docs/screenshots/stage3_reconciled_neon.png
- docs/screenshots/stage3_reconciled_premium.png
- docs/screenshots/stage3_reconciled_expired.png
- docs/screenshots/stage3_reconciled_menu_density.png

### 15. Documentation truthfulness

Update STATUS.md only from actual observed results.

Append to docs/reference/MI_V5_DESIGN_COMMUNICATION.md:
- exact implementation commit SHA
- changed files
- assets added/changed
- measured geometry
- device/build/test result
- screenshot paths
- runtime marker
- remaining deviations

Do not claim visual fidelity that was not verified.

### Acceptance gate

Stage 3 becomes ACCEPTED only after:
1. actual source matches the claimed architecture/features
2. actual portraits/assets are rendered
3. all 8 themes are demonstrably data-driven
4. all 11 menus are non-placeholder and bounded
5. physical build/install/runtime passes
6. required screenshots are committed
7. STATUS and communication log contain exact evidence

Only after this gate may a future handoff authorize Stage 4.

## Important prohibitions

- No full-screen member card.
- No architecture rewrite.
- No backend integration in this hardening pass.
- No fake business metrics.
- No generic repeated menu placeholders.
- No heavy 3D.
- No giant unoptimized assets.
- No "completed" status without physical evidence.
