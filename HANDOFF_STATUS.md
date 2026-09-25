# BAD GYM — HANDOFF STATUS

STATUS: READY_FOR_EXECUTION
BRANCH: member-intelligence-v3
CURRENT AUTHORIZATION: MI-V6-MEMBER-INTELLIGENCE-UX-THEME-REBUILD
EXECUTOR: Google Antigravity

## Design review decision

The previous MI-V5 distance-readability pass is technically complete but visually rejected by the user.

Primary defect:
The eight themes currently feel like unrelated color skins rather than one coherent BAD GYM design system. Some component/menu colors also fight the selected theme and create same-hue or opposing combinations.

This stage is a design-system correction.

## Non-negotiables

- Keep all 8 themes.
- Keep the shared information architecture.
- Keep default browse approximately 312 × 406dp.
- Keep bounded detail approximately 340 × 443dp.
- Keep adjacent card peek.
- Keep automatic carousel.
- Keep all 11 menus.
- Do not introduce backend/fake data.
- Do not turn the card into a mini-dashboard.
- Do not use full-screen detail.
- Do not solve theme differences by simply changing hue.

## Desired visual principle

One BAD GYM system, eight material personalities.

Theme changes:
material + atmosphere + accent + decorative treatment.

Theme does NOT change:
information hierarchy + semantics + readability + component geometry.

## Priority

P0: contrast/readability defects
P0: theme coherence
P1: card hierarchy and glanceability
P1: menu consistency
P2: decorative refinement

## Required evidence

Fresh device screenshots:
- stage5_theme_ux_browse.png
- stage5_theme_ux_detail.png
- stage5_theme_ux_matrix.png

Required report:
- exact commit SHA
- changed files
- tests/build results
- device and resolution
- measured geometry
- theme-by-theme visual notes
- any deviations/blockers

Do not claim visual completion from build success alone.
