# BAD GYM — MASTER UI/UX RECONSTRUCTION + PRODUCTION IMPLEMENTATION PROMPT

You are the Lead UI/UX Reconstruction Engineer, Visual Design Engineer, Android UI Engineer, Asset Pipeline Engineer, and Visual QA Engineer.

The reference image is a VISUAL SPECIFICATION, not inspiration. Reconstruct the design as accurately as technically possible as a real responsive Android UI. Do not simplify difficult visual elements into generic Material UI. Do not redesign according to taste. Preserve composition, hierarchy, proportions, spacing, typography, colors, gradients, shadows, surfaces, materials, imagery, graphics, decorative elements, iconography, density, alignment, corner radii, rhythm, layering and interaction hierarchy.

## 0. Project-first
Inspect the existing BAD GYM repository before coding. Identify Kotlin/Compose/Gradle versions, architecture, navigation, theme/tokens, components, assets, image loading, build variants and device configuration. Preserve unrelated functionality and existing domain/repository contracts. Do not rewrite architecture unnecessarily.

## 1. Reference forensic analysis — BEFORE CODE
Analyze the supplied reference and create a measurable reconstruction specification:
- canvas dimensions/aspect ratio
- viewport and safe areas
- outer shell and content bounds
- header, rail, hero, image, membership, status, metrics, CTA and footer bounds
- X/Y/width/height/aspect ratio
- margins/padding/gaps
- corner radii/borders/shadows/elevation/opacity
- z-order and layer order
Use proportional geometry when exact pixels cannot be known. Do not code until the visual specification is understood.

## 2. Hierarchy
Classify P0 primary, P1 secondary, P2 supporting and P3 decorative. Preserve what the eye sees first. A large hero remains large; a large CTA remains large; a prominent rail remains prominent. Secondary intelligence must not drown the primary composition.

## 3. Typography
Analyze family/fallback, size, weight, line height, letter spacing, case, alignment, opacity, color, max width and wrapping. Create centralized typography tokens. Do not blindly use default Material typography when it visibly differs.

## 4. Color
Extract approximate actual colors for background, surfaces, primary/secondary/accent, text, borders, success/warning/error, gradients and glow. Centralize semantic tokens. Never scatter arbitrary raw colors through business components.

## 5. Materials/surfaces
Classify flat/glass/frosted/translucent/gradient/metallic/glossy/matte/neumorphic/elevated/inset/blurred/textured/photographic/transparent. Reproduce base color, opacity, gradient, blur/fallback, border, inner highlight, shadow, elevation, reflection, texture and radius. Do not replace custom surfaces with generic Card.

## 6. Asset forensics
Identify photographic assets, transparent graphics, vectors and programmatic graphics. Use the correct representation. If an identifiable reference asset can be extracted, extract it rather than inventing a different replacement. Preserve shape, proportion, color, orientation and transparency.

## 7. Asset extraction/upscale/transparency
For raster assets:
1. extract/crop
2. remove unwanted background only if required
3. upscale when source resolution is insufficient
4. clean edges/artifacts
5. preserve transparency
6. export at production resolution
7. document source, dimensions, aspect ratio, transparency, intended display size and placement
Decorative overlays should normally be transparent PNGs or vectors. Do not bake screen backgrounds into transparent assets. Do not bake shadows that can be generated programmatically.

## 8. Asset manifest
Create a manifest listing every generated/extracted asset, type, source, dimensions, aspect ratio, transparency, intended usage, display size, placement and scaling mode.

## 9. Layer composition
Reconstruct as layers:
0 background
1 ambient gradients
2 material surfaces
3 decorative graphics
4 content
5 imagery
6 text
7 interactive elements
8 foreground decorations
9 overlays
Preserve z-order. Do not flatten the entire screen into one screenshot.

## 10. Responsive geometry
Reference may represent one device. Do not simply shrink everything. Test at 360dp, 375dp, 390dp and 412dp. Decide which dimensions are fixed/min/max/proportional/weighted/constrained/adaptive. Maintain 48dp minimum touch targets, readable text, correct aspect ratios and usable rail/navigation. Scroll only where needed; eliminate dead blank regions.

## 11. Data separation
Visual design is independent of business data. Use preview/debug data -> domain model -> ViewModel -> repository -> backend. Never invent unavailable member data. Never hardcode production business data into UI components.

## 12. Design system
Centralize colors, typography, spacing, dimensions, shapes, elevation, motion, icons, assets and responsive breakpoints. Business components consume semantic tokens.

## 13. Component architecture
Build reusable components: Screen, Background, Header, NavigationRail, EventHeader, Hero, Identity, Membership, Status, Metrics, CTA, DecorativeLayers and menu panels. Do not create eight unrelated screens for eight themes.

## 14. Member Intelligence architecture
One shared Member Intelligence engine:
Global Theme x Membership Tier x Current Event x Member State x Gym Brand x Context.
Use one information architecture and theme definitions. Home is intelligence-first; detail menus are evidence. Event, identity, membership, status, decision metrics and one contextual CTA are the primary home hierarchy.

## 15. Theme system
Support these eight visual themes as data-driven definitions:
1 Natural Fresh
2 Futuristic Neon
3 Minimal Dark
4 Glassmorphism
5 Premium 3D
6 Vibrant Gradient
7 Gym Beast Mode
8 Purple Royal

Each theme defines background, surface, elevated surface, primary/secondary accent, text, border/divider, semantic colors, glow, gradients, shadows, radii, typography treatment, rail treatment, metric treatment, CTA treatment and decorative assets/placement.

### Natural Fresh
Pale mint/white, botanical leaves, soft glass depth, fresh green identity, large rectangular portrait, strong CHECK-IN pill, champagne/gold plan band, green ACTIVE band, attendance ring, payment metric, six-bar workout progression, full-width green CTA, botanical footer and wellness typography.

### Futuristic Neon
Deep navy/near-black, cyan/electric green/violet glow, luminous borders, neon hero, high-tech progress graphics, controlled bloom, luminous CTA and angular geometry.

### Minimal Dark
Charcoal/black, restrained gray surfaces, white typography, subtle green status, minimal borders, no unnecessary glow, elegant spacing and executive density.

### Glassmorphism
Translucent/frosted surfaces, blurred or simulated blur background, pale blue/white/lavender atmosphere, layered depth and luminous edges. If blur is unavailable, use gradients/translucent layers as fallback.

### Premium 3D
Deep charcoal, champagne/gold, metallic highlights/reflections, crown/luxury language, deep elevation and premium CTA. Avoid cheap yellow gradients.

### Vibrant Gradient
Pink/purple/cyan/blue/green gradient atmosphere, colorful translucent surfaces, youthful energy and controlled glow without destroying hierarchy.

### Gym Beast Mode
Dark charcoal, red/electric green, athletic/performance language, sharp accents, strong CTA, high contrast and controlled angular graphics.

### Purple Royal
Deep purple/violet/lavender/magenta/silver, premium gradients, royal membership treatment, elegant typography and luminous edges.

## 16. Membership tiers
Support NORMAL, SILVER, GOLD, PREMIUM, VIP, ELITE, CORPORATE, TRIAL and WALK_IN. Tier can influence badge, accent, border, membership surface, icon, subtle glow and selected rail state. Semantic states always override decorative tier styling: overdue/expired/payment failed/complaint must remain unmistakable.

## 17. Events
Support CHECK_IN, CHECK_OUT, PAYMENT, PAYMENT_FAILED, NEW_MEMBER, WALK_IN, RENEWAL, EXPIRED, FREEZE, REACTIVATION, TRAINER_SESSION, WORKOUT, SUPPLEMENT_PURCHASE, NUTRITION, SERVICE_PURCHASE, COMPLAINT and MAINTENANCE. Event label must be explicit; never communicate event only through color.

## 18. Interaction
Identify tap/selected/pressed/disabled/loading/error/navigation/scroll states. Use approximately 180–250ms horizontal transitions where appropriate. Preserve scroll state where practical. Avoid constant animation, excessive glow or decorative motion.

## 19. Accessibility
48dp touch targets, content descriptions, readable contrast, scalable text, semantic labels and accessible navigation without destroying visual intent.

## 20. Implementation
Use existing Android stack, especially Jetpack Compose/Kotlin if already present. Reuse existing navigation, theme, repository and architecture. Do not build fake architecture only for screenshots.

## 21. Development build identity
Every visual milestone must expose a DEBUG-only runtime identity such as:
MI-V4
BUILD <short-git-sha>
DEBUG
Use BuildConfig.DEBUG or equivalent. It must automatically disappear from release/production builds. The identity is mandatory for device verification.

## 22. Build and device verification
Run compilation, relevant tests/lint, build APK, install on an available real device/emulator, launch and verify screen, navigation, assets, data, runtime marker, dimensions, scrolling and interactions.

## 23. Screenshot visual QA — MANDATORY
Capture a real device screenshot. Compare to the supplied reference. Do not accept compilation, Compose Preview or navigation as visual proof. Compare overall composition, proportions, positions, spacing, typography, image crop/size, colors, surfaces, shadows, gradients, decorative assets, navigation, CTA, density, whitespace and hierarchy.

Classify discrepancies CRITICAL/MAJOR/MINOR. Fix CRITICAL first, then MAJOR, then MINOR.

Repeat BUILD -> INSTALL -> RUN -> SCREENSHOT -> COMPARE -> DIFFERENCE ANALYSIS -> FIX until visually close. Do not claim pixel-perfect without actual screenshot comparison.

## 24. Multi-device QA
After primary device match, verify 360dp/375dp/390dp/412dp without making everything tiny or distorting the composition.

## 25. No-placeholder rule
No generic Material cards, random icons, fake gradients, arbitrary stock imagery, placeholder decorative graphics, temporary text, fake charts or TODO UI when a reference element exists. If exact reproduction is technically impossible, document the limitation rather than silently substituting a different design.

## 26. Git
Work only on member-intelligence-v3. Never force-push or erase unrelated work. Commit logically by stage and push. Update STATUS.md with exact changes, verification and remaining discrepancies. Mark CURRENT_TASK.md COMPLETED only after genuine verification; otherwise BLOCKED with exact blocker.

## 27. Completion checklist
Reference analyzed; geometry spec; typography; colors; materials; assets; upscale/transparent assets; asset manifest; shared architecture; eight themes; events/tiers; responsive rules; debug identity; build; device install; screenshot; visual comparison; discrepancies fixed; 360/375/390/412 verified; existing functionality preserved; commit pushed; status updated.

## Final truth sources
Reference image = visual truth.
Real device screenshot = implementation truth.
Git commit + runtime debug marker = version truth.

Never stop merely because the application compiles.
