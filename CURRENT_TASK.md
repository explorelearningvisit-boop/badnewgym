# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-STAGE-04-EIGHT-THEME-ENGINE-REFERENCE-FIDELITY
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Authorization

Stage 3 Hardening/Reconciliation has now been completed by Antigravity and verified on the physical Xiaomi device. The repository source has also been reconciled: the previously reported placeholder/monolithic implementation is gone; the current MemberIntelligenceScreen.kt contains the centralized 8-theme engine and expanded implementation, and fresh reconciled screenshots are committed.

Proceed to Stage 4.

## Stage 4 objective

Build the production-grade Eight Theme Engine and reference-fidelity layer on top of the existing bounded Member Intelligence architecture.

The 8 canonical visual themes are:
1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

The uploaded/reference contact sheet in:
BAD_GYM_MEMBER_INTELLIGENCE_READY/preview/ALL_8_THEMES_CONTACT_SHEET.png
is the canonical visual source for theme relationships.

## Preserve these non-negotiables

- Compact browse card; never full-screen.
- Bounded detail; preserve browse context and side peek.
- Persistent photo/name/member ID/event/time/tier/state in detail.
- Vertical rail in detail only.
- All 11 menus remain available.
- No revenue dashboard inside the member card.
- No backend integration in Stage 4.
- No heavy continuous 3D.
- No fake business metrics.
- No generic Material-3 skin replacing the reference language.
- Important state must never be represented by color alone.
- Global dashboard/background must not unexpectedly recolor when a member/theme/menu changes.

## Stage 4 work

### A. Theme architecture
Create/strengthen a clean centralized theme model:
- ThemeDefinition
- Surface/material tokens
- typography emphasis
- gradients
- borders/highlights
- glow/shadow
- CTA
- portrait treatment
- decorative assets
- semantic state overlay
- tier decoration
- motion parameters

Theme selection must be data-driven and composable.

### B. Reference fidelity
For each theme, explicitly reconstruct the visual language from the reference board:
- Natural Fresh: mint/white glass + botanical leaves
- Futuristic Neon: midnight sapphire + cyan/neon glow
- Minimal Dark: restrained executive slate/charcoal language
- Glassmorphism: icy translucent glass + cool gradients
- Premium 3D: metallic gold + premium depth
- Vibrant Gradient: periwinkle/lilac/pink/blue gradients
- Gym Beast Mode: aggressive red/black gym energy
- Purple Royal: deep violet/lilac premium treatment

Do not make the eight themes differ only by accent color.

### C. Assets
Inventory current repository assets before creating anything.
Use:
- SVG/vector for crisp UI geometry
- transparent PNG/WebP for decorative artwork
- Compose Canvas for rings, bars, gradients and simple glows
- isolated 3D only if it materially improves Premium 3D / Beast / Royal treatment

Create or refine lightweight production assets where the reference board requires them:
- botanical clusters
- glass highlights
- premium metallic ornaments
- theme-specific glows/sparks
- tier icons/badges
- verification/event accents
- CTA ornaments
- portrait framing overlays

Avoid oversized raster files. Document dimensions, format and intended use.

### D. Semantic × theme matrix
Verify all critical semantic states inside every theme:
- ACTIVE
- PAYMENT_DUE
- PAYMENT_OVERDUE
- EXPIRED
- FROZEN
- NEW/WALK-IN
- TRAINER/PT_ACTIVE
- CRITICAL

Urgent states override decorative tier styling without destroying theme identity.

### E. Tier × theme matrix
Verify:
- NORMAL
- SILVER
- GOLD
- PREMIUM
- VIP/ELITE

Tier styling must remain recognizable but subordinate to critical operational states.

### F. Motion
Theme-aware but restrained:
- 150–250ms theme transition
- 180–220ms menu transition
- local ring/bar entrance
- subtle rail indicator
- badge pulse only for new/critical
- reduced-motion support

No infinite decorative animation.

### G. Visual QA
Use the same physical device:
- build
- install
- launch
- verify no crash/layout exception
- verify browse + bounded detail
- verify all 11 menus
- verify all 8 themes
- verify at least 5 semantic states
- verify side peek
- verify Back
- verify actual portraits
- verify global dashboard remains stable

### H. Required screenshot evidence

Create fresh physical-device screenshots:
- docs/screenshots/stage4_natural_fresh.png
- docs/screenshots/stage4_futuristic_neon.png
- docs/screenshots/stage4_minimal_dark.png
- docs/screenshots/stage4_glassmorphism.png
- docs/screenshots/stage4_premium_3d.png
- docs/screenshots/stage4_vibrant_gradient.png
- docs/screenshots/stage4_gym_beast_mode.png
- docs/screenshots/stage4_purple_royal.png
- docs/screenshots/stage4_semantic_matrix.png
- docs/screenshots/stage4_menu_matrix.png

### I. Documentation

Update STATUS.md with:
- exact implementation commit SHA
- device/build/test
- measured geometry
- all 8 themes verified
- semantic states verified
- menu coverage
- screenshot paths
- runtime marker
- remaining deviations

Append a Stage 4 implementation report to:
docs/reference/MI_V5_DESIGN_COMMUNICATION.md

Do not mark COMPLETE from Gradle success alone.

## Acceptance gate

Stage 4 is complete only when:
1. all 8 themes are visibly distinct and reference-faithful
2. theme engine is centralized/data-driven
3. assets are production-ready and optimized
4. semantic state overlays work in all themes
5. tier decorations work in all themes
6. all 11 menus remain bounded and readable
7. physical-device build/install/runtime passes
8. all required screenshots are committed
9. STATUS and communication log contain exact evidence

Do not start Stage 5 until ChatGPT reviews Stage 4 screenshot evidence.

## Live progress protocol

At real milestones update STATUS.md:
IN_PROGRESS → DESIGNING → IMPLEMENTING → BUILDING → INSTALLING → QA → COMPLETED
or BLOCKED with exact blocker/action.

CURRENT_TASK.md remains READY_FOR_EXECUTION until completion.
