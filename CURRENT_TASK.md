# BAD GYM — Current AI Handoff

STATUS: PAUSED_MANUAL
TASK_ID: MI-V6-MEMBER-INTELLIGENCE-UX-THEME-REBUILD
AUTHOR: ChatGPT
EXECUTOR: Manual Google Antigravity GUI only
BRANCH: member-intelligence-v3

## Mission

Rework the existing Member Intelligence card visual system based on the user's latest design review.

The previous distance-readability implementation is technically complete, but the user is NOT satisfied with the visual result. The problem is not primarily card size anymore. The current 8-theme system feels like eight unrelated color skins, with weak material/color relationships and several ugly opposing-color combinations.

Treat this as a UX/design-system correction, not a cosmetic recolor.

Do not redesign the whole application. Focus on Member Intelligence browse/detail cards and their 8-theme visual system.

## Product problem to solve

Member Intelligence is a gym owner's live operational observation surface.

The owner may glance at a phone on a desk without touching it. In one glance the card must communicate:

EVENT → MEMBER → TIME → STATE → DECISION SIGNAL → ACTION

The card is NOT a poster, mini-dashboard, or theme showcase.

It should feel like one premium BAD GYM product with eight coherent material personalities.

## Core design decision

Keep the 8 themes, but stop treating them as independent palettes.

All eight themes must share:
1. the same information architecture;
2. the same semantic hierarchy;
3. the same neutral/text system logic;
4. the same component geometry;
5. the same state semantics;
6. the same contrast rules.

Themes may change material, atmosphere, accent, texture, glow and decorative treatment — NOT the meaning or readability of information.

A user should immediately recognize every card as BAD GYM even when the theme changes.

## Theme design direction

Build each theme around a coherent 4-layer system:
A. Canvas/background
B. Card/surface material
C. Brand accent
D. Semantic state colors

Do NOT use accent colors as arbitrary body text.

Do NOT create red-on-red, cyan-on-cyan, gold-on-gold, pink-on-pink or purple-on-purple informational text.

Semantic colors must remain independent from decorative theme accents.

Natural Fresh:
- pale mint/ivory surface
- deep botanical green text
- restrained emerald accent
- soft organic depth
- wellness, clean, trustworthy

Futuristic Neon:
- deep blue/navy glass surface
- near-white primary text
- cyan as accent/glow only
- dark navy secondary text where used on bright cyan surfaces
- high-tech, energetic

Minimal Dark:
- charcoal/slate surface
- near-white primary text
- muted silver accent
- very restrained decoration
- calm, focused

Glassmorphism:
- light frosted blue/lavender surface
- deep navy text
- cool blue accent
- translucent layers with visible separation
- elegant, airy

Premium 3D:
- ivory/champagne surface
- deep espresso/charcoal text
- restrained metallic gold accent
- gold should never become low-contrast body copy
- luxury, premium, mature

Vibrant Gradient:
- controlled soft indigo/rose/ice gradient
- deep indigo primary text
- one dominant accent at a time
- avoid rainbow/noisy UI
- youthful, dynamic

Gym Beast Mode:
- deep graphite/crimson material
- white/near-white primary text
- crimson/red reserved for state/accent/border/glow
- no red body copy on red surfaces
- powerful, athletic

Purple Royal:
- deep amethyst/plum surface
- white/lavender primary text
- purple accent/glow
- restrained luxury
- no purple-on-purple informational text

## Semantic state vs theme

Critical/payment/expired/safety states must remain visually obvious in every theme.

Use semantic tokens:
- critical = red family
- warning = amber
- success = green
- info = blue/cyan

Resolve their foreground against the actual state surface.

Example red critical banner:
- red/pale-red surface
- deep red/charcoal or white foreground based on actual contrast
- never red text on red surface

The semantic state must not destroy the theme.

## Contrast implementation

Replace the current simplistic luminance threshold approach with actual WCAG-style contrast reasoning.

Implement a centralized resolver with:
- relative luminance
- contrast ratio
- minimum target for normal body text: 4.5:1
- large/bold text target: 3:1 minimum
- candidate foreground selection
- safe fallback to neutral foreground
- semantic foreground helpers for accent/state surfaces

Do not rely on a single luminance threshold as the only rule.

Audit all direct text colors in Member Intelligence components and remove/replace hardcoded same-hue informational colors where they bypass the resolver.

## Card hierarchy

Default browse remains approximately 312dp × 406dp.
Expanded bounded detail remains approximately 340dp × 443dp.

Do not increase density just because the card is large.

Visual priority:
1. EVENT
2. MEMBER NAME
3. TIME
4. MEMBERSHIP / STATE
5. PRIMARY SIGNAL
6. KEY METRICS
7. CTA

Remove or visually demote decorative copy that competes with these.

## Browse card

Browse state:
- no menu rail;
- large portrait;
- event and time immediately visible;
- member name dominant;
- membership/state readable;
- one primary intelligence signal;
- compact key metrics;
- one clear CTA;
- adjacent card peek remains visible;
- automatic carousel remains.

Do not make every region colorful.

Prefer 1 dominant accent + neutral structure.

## Detail card

Expanded bounded detail:
- approximately 340 × 443;
- same theme language;
- menu rail appears;
- persistent identity/event/state header;
- menu content changes only below header;
- all 11 menus remain:
  Home, Attendance, Plan, Payment, Trainer, Workout, Supplements, Nutrition, Services, History, Insight.

Menu panels must look like part of the same theme, not separately colored mini-apps.

## Menu visual correction

Current implementation contains per-theme hardcoded InfoCard surfaces/borders and multiple raw status colors.

Refactor these toward semantic/theme tokens.

A menu panel should use:
- theme surface/elevated surface;
- theme border;
- semantic status accent only where necessary;
- resolved text foreground.

Do not create a new random color for each menu.

## Material behavior

Use restrained material behavior:
- Natural: soft matte/frosted
- Neon: dark glass + cyan edge light
- Minimal: matte slate
- Glass: translucent frost
- Premium: ivory + subtle metallic edge
- Vibrant: soft gradient atmosphere
- Beast: graphite + crimson energy edge
- Purple: amethyst glass

No excessive glow.
No rainbow gradients.
No decorative elements behind important text.
No black text on dark themes.
No white text on light themes unless contrast is proven.

## UX psychology

The owner should answer these questions in under one second:
1. Who is this?
2. What just happened?
3. Is there a problem?
4. What needs attention?
5. What should I do?

If decoration competes with these answers, reduce decoration.

A payment-due member should visually feel urgent without turning the entire card into a red poster.

A normal active member should feel calm and healthy.

An expired member should be unmistakable but still belong to the selected theme.

## Testing requirements

Add/extend unit tests for:
- contrast ratio calculation;
- white/black candidate selection;
- light/dark theme surfaces;
- red critical surface;
- gold premium surface;
- cyan neon surface;
- purple surface;
- glass light surface.

Run:
- testDebugUnitTest
- assembleDebug

If Xiaomi Redmi Note 11 is connected:
- install debug APK
- launch
- inspect default browse
- inspect bounded detail
- inspect all 8 themes
- inspect all 11 menus
- specifically inspect Beast Mode red/white
- inspect Premium gold/charcoal
- inspect Neon cyan/white/navy
- inspect Purple white/lavender
- inspect Glass deep navy
- inspect Natural green/deep botanical

Capture:
- docs/screenshots/stage5_theme_ux_browse.png
- docs/screenshots/stage5_theme_ux_detail.png
- docs/screenshots/stage5_theme_ux_matrix.png

## Acceptance gate

Do not mark complete until:
- the 8 themes visually read as one BAD GYM design system;
- each theme has a coherent material/color relationship;
- no obvious same-hue text/surface failures remain;
- semantic states remain clear;
- browse/detail hierarchy is clear from a distance;
- all 11 menus remain intact;
- tests pass;
- build passes;
- physical runtime is verified when device is available;
- fresh screenshots are committed;
- STATUS.md is updated with measured results;
- HANDOFF_STATUS.md records exact implementation and deviations;
- design communication log is appended;
- commit is pushed to member-intelligence-v3.

## Git rules

Never force-push.
Never reset/discard unrelated work.
Commit only intended changes.
Push to origin member-intelligence-v3.

## Model

Use Gemini 3.1 Pro High with high effort.
Execution is intentionally paused for autonomous/headless bridge execution. Open the project in Google Antigravity and run this task visibly in the GUI.
