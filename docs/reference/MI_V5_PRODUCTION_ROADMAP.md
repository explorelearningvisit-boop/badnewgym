# BAD GYM — MI-V5 Production Reconstruction Roadmap

## Goal

Turn the current Member Intelligence implementation into a production-ready, responsive, highly visual Android/Jetpack Compose system based on the supplied eight-theme reference board.

This is deliberately staged. Do not attempt to solve all visual, interaction, asset and 3D problems in one edit.

## Architecture principle

ONE Member Intelligence engine
×
ONE reusable component system
×
EIGHT theme definitions
×
Membership Tier
×
Current Event
×
Member State
×
Context

The eight reference cards are visual states, not eight independent screens.

## Stage sequence

### Stage 1 — Visual foundation / forensic geometry / asset inventory
- Freeze the visual truth into measurable geometry.
- Inventory existing assets and identify missing assets.
- Build the shared shell geometry: outer shell, header, rail, hero, identity, membership, status, metrics, CTA, decorative layer slots.
- Establish semantic design tokens.
- Establish asset manifest and asset-slot contracts.
- Establish DEBUG runtime identity.
- Add screenshot/visual QA harness scaffolding if practical.
- Do NOT add complex 3D dependencies yet unless required by a minimal validated prototype.
- Output: a structurally correct shell ready for theme skins.

### Stage 2 — Natural Fresh reference reconstruction
- Treat Natural Fresh as the primary fidelity baseline.
- Rebuild geometry before decoration.
- Large rectangular portrait.
- Strong CHECK-IN event pill and time hierarchy.
- Prominent integrated rail.
- Gold Plan band.
- ACTIVE band.
- Three equal metric blocks.
- Attendance ring.
- Six-bar workout progression.
- Full-width contextual CTA.
- Botanical framing.
- Remove dead whitespace.
- Real screenshot compare/fix loop.
- Output: Natural Fresh visual baseline passes primary geometry QA.

### Stage 3 — Asset/material pack
- Extract/reuse available project assets.
- Generate missing simple assets programmatically.
- Use transparent PNG/vector for complex decorative assets.
- Upscale low-resolution assets only when source fidelity permits.
- Add asset manifest with dimensions/placement.
- Add material recipes for glass, metallic gold, neon glow, gradient and premium surfaces.
- Avoid baking backgrounds into overlays.
- Output: reusable production asset/material pack.

### Stage 4 — Theme engine / all 8 themes
- Natural Fresh
- Futuristic Neon
- Minimal Dark
- Glassmorphism
- Premium 3D
- Vibrant Gradient
- Gym Beast Mode
- Purple Royal
All must reuse the same component architecture.
- Semantic state colors must override decorative tier colors.
- Output: theme engine with screenshot verification for each theme.

### Stage 5 — Adaptive member intelligence / tier / event composition
- NORMAL, SILVER, GOLD, PREMIUM, VIP, ELITE, CORPORATE, TRIAL, WALK_IN.
- CHECK_IN, CHECK_OUT, PAYMENT, PAYMENT_FAILED, NEW_MEMBER, WALK_IN, RENEWAL, EXPIRED, FREEZE, REACTIVATION, TRAINER_SESSION, WORKOUT, SUPPLEMENT_PURCHASE, NUTRITION, SERVICE_PURCHASE, COMPLAINT, MAINTENANCE.
- Event label, time and state are explicit.
- One primary CTA per context.
- Intelligence signals stay subordinate to the reference Home hierarchy.
- Output: data-driven adaptive visual composition.

### Stage 6 — Interaction and deep menus
Home always exists. Detail menus replace the main content area, not the entire shell:
- Attendance
- Plan/Membership
- Payment
- Trainer
- Workout
- Supplements
- Nutrition
- Services
- History
- Insight
Conditional menus appear only when relevant.
Every menu has meaningful content, context, states, empty/loading/error states and contextual actions.
Transitions: horizontal slide + fade, approximately 180–250ms, with preserved scroll state where practical.
Output: complete interactive Member Intelligence navigation.

### Stage 7 — 3D / depth / motion layer
Use a progressive enhancement strategy:
1. First-class 2.5D depth using Compose: layered parallax, highlights, perspective-like offsets, soft shadows, ambient glow, glass depth and controlled card lift.
2. Only where it materially improves the reference, add real glTF/3D rendering.
3. Prefer a small, optional 3D viewport rather than making the whole UI a 3D scene.
4. If SceneView/Filament is compatible with the existing project, use it for real-time glTF/PBR elements; otherwise keep the visual effect in Compose.
5. Heavy 3D must be isolated behind a component boundary and have a graceful non-3D fallback.
6. Do not make 3D animation continuous or distracting.
7. Support reduced motion/accessibility.
Output: web-like depth without sacrificing Android performance or maintainability.

### Stage 8 — Multi-device / production hardening
Verify 360dp, 375dp, 390dp, 412dp and available real device.
- typography scaling
- no clipping/overlap
- touch targets
- scroll behavior
- state restoration
- animation performance
- memory/image size
- release/debug separation
- production build has no MI-Vx marker
- build and install verification
- screenshot evidence

## Asset sourcing policy

Do not log into paid asset websites or bypass access controls.

Prefer:
- existing user/project assets
- generated/original assets
- open-license/CC0 assets
- official sample assets with compatible licenses

Record source/license in the asset manifest for every external asset.

Never use identifiable real-person photos from random websites for production member imagery.

## 3D policy

The goal is a premium 3D-feeling app, not a 3D demo.

Use real 3D only when it improves:
- premium depth
- equipment/service visualizations
- membership badge/object
- subtle hero object
- product/service visualization

Do not put a heavyweight 3D scene behind every screen.

The current official Filament ecosystem supports physically based rendering and glTF on Android; SceneView provides a Compose-oriented 3D scene abstraction. Verify dependency compatibility with the project's current Kotlin/Compose/AGP before adding it.

## Acceptance model

Every stage must produce:
- code
- build/test evidence
- real device evidence when applicable
- screenshot evidence for visual work
- STATUS.md update
- Git commit/push

A stage is not complete because Gradle succeeds.

## Handoff rule

After a stage is completed, CURRENT_TASK must identify the next stage only after the previous stage's evidence is inspected. Never blindly stack unverified stages.
