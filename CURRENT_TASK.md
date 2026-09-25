# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-DISTANCE-READABILITY-FINAL-PRODUCTION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission

Make Member Intelligence production-ready for a gym owner who places the phone on a desk at arm's length or farther away and observes the rotating live member cards without touching the screen.

This is NOT a small cosmetic tweak. Treat this as the final distance-readability design pass for the existing Stage 4/MI-V5 card architecture.

Do not start unrelated features or backend work.

## Product behavior

The Member Intelligence browse carousel is the primary live observation surface.

Cards should automatically slide/rotate one member/event at a time as already supported by the existing carousel. The owner should be able to understand, from a distance:

1. who the member is;
2. what just happened;
3. when it happened;
4. membership/tier/state;
5. the most important decision signal;
6. the primary action/status.

The owner should NOT need to touch the phone just to read basic activity.

## Geometry — IMPORTANT

The user explicitly prefers the CURRENT BOUNDED DETAIL DESIGN as the new default browse design.

Current verified detail:
- 312dp × 406dp

Make that approximately the DEFAULT browse card size:
- DEFAULT browse target: 312dp × 406dp

Then make the tapped/expanded bounded detail state approximately 8–10% larger while preserving the same width/height ratio:
- EXPANDED detail target: approximately 340dp × 443dp

Keep both bounded. Never use full-screen card presentation.

Preserve a visible adjacent-card side peek on the target Xiaomi Redmi Note 11 / ~392.7dp effective width. If exact 340dp creates insufficient peek in a specific layout, tune carousel padding/gap/rail treatment rather than shrinking the readability target below the requested default.

Remove the old visual distinction where the compact browse card feels materially smaller than the preferred detail card.

Use centralized geometry tokens. Do not hardcode scattered dimensions.

## Distance readability — PRIMARY REQUIREMENT

The current text is still too small for the user's observation distance.

Increase typography substantially and deliberately.

Target minimums, subject to actual layout fit:
- Member name: 18sp or larger
- Member code: 12–13sp
- Event label: 13–14sp
- Event time: 13–14sp
- Tier/status: 12–13sp
- Metric values: 15–17sp
- Metric labels: 11–12sp
- Primary signal: 13–14sp
- Signal supporting text: 11–12sp
- CTA: 13–14sp
- Detail menu labels: 11–13sp
- Detail menu micro-data: 10–11sp minimum
- Important menu headings/values: 13–16sp

Do NOT blindly scale every text element. Preserve hierarchy, line limits, and spacing. Remove decorative micro-copy if necessary rather than shrinking important information.

Portraits must also remain visibly large:
- browse portrait approximately 96–104dp wide where layout permits;
- expanded/detail portrait approximately 104–112dp wide where layout permits.

All touch targets remain >=48dp.

## DEFAULT vs EXPANDED UX

Default:
- large, immediately readable member card;
- same visual language as the currently preferred bounded detail card;
- no menu rail required in the browse state;
- event + member identity + membership/status + key metrics + signal + CTA remain visible.

Tap:
- bounded expansion only;
- card grows approximately 8–10% in the same aspect ratio;
- menu rail appears;
- menu text is also larger and distance-readable;
- no global screen zoom;
- no full-screen takeover.

Back:
- returns to the large default browse card.

## COLOR / CONTRAST — ALL 8 THEMES

The user reports that several themes currently become difficult to read because accent/card colors and text colors collapse into the same hue, especially red-on-red in Gym Beast Mode.

Fix this systematically, not with one-off overrides.

Create/use a centralized contrast-aware semantic foreground resolver.

Rules:
- Never use an accent color as body text on a surface where contrast is insufficient.
- Never use red/crimson text on a red/crimson surface when a neutral high-contrast foreground is required.
- Critical states may retain red as border, icon, badge, glow or surface accent, but primary readable text should resolve to an appropriate high-contrast foreground.
- Light surfaces -> deep neutral/semantic text.
- Dark surfaces -> near-white/very-light text.
- Saturated accent surfaces -> white or very-dark text based on actual contrast.
- Gold/yellow surfaces -> dark charcoal/brown foreground.
- Cyan/blue surfaces -> very dark navy or white depending on luminance.
- Purple surfaces -> white/lavender with sufficient contrast.
- Pink/red surfaces -> white or very-dark neutral based on luminance.
- Semantic meaning (critical/success/warning/info) must remain visible without sacrificing readability.
- Preserve the identity of all 8 themes; do not flatten them into one generic palette.

Use WCAG-style contrast reasoning as a design constraint. Prefer a centralized resolver/token system so future themes cannot reintroduce the same defect.

Required theme audit:
1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

## VISUAL HIERARCHY

Distance hierarchy must be:

EVENT → MEMBER NAME → TIME → MEMBERSHIP/STATE → PRIMARY SIGNAL → KEY METRICS → CTA

The card should be understandable in approximately one glance.

Do not add more business data just because the card is larger.

Do not use decorative charts that compete with the member/event information.

## MENU READABILITY

All 11 existing menus remain intact:
Home, Attendance, Plan, Payment, Trainer, Workout, Supplements, Nutrition, Services, History, Insight.

Every menu must receive the larger typography treatment.

Avoid 7–8sp informational text except genuinely nonessential metadata. The current rail can retain compact labels only where unavoidable, but the actual menu content must be comfortably readable from the user's stated distance.

Preserve bounded scrolling inside menu content.

## CAROUSEL / LIVE OBSERVATION

Preserve:
- horizontal carousel;
- adjacent card visibility;
- automatic member/event rotation already present;
- restrained motion;
- no accidental vertical page takeover;
- no touch required for basic observation.

Do not introduce a new backend or fake live data.

## ACCESSIBILITY

Use:
- minimum 48dp touch targets;
- strong text/background contrast;
- stable text hierarchy;
- no information conveyed only by color;
- content descriptions for member/event semantics;
- avoid tiny text for important information.

## PRODUCTION QA — REQUIRED

On Xiaomi Redmi Note 11 if connected:

1. testDebugUnitTest
2. assembleDebug
3. install debug APK
4. launch
5. verify default browse geometry
6. verify expanded geometry
7. verify readable typography at arm's-length / desk observation distance
8. verify side peek
9. verify Back
10. verify automatic carousel/live observation behavior
11. smoke-test all 8 themes
12. smoke-test all 11 menus
13. specifically inspect Gym Beast Mode red/white contrast
14. inspect Premium 3D gold contrast
15. inspect Futuristic Neon cyan contrast
16. inspect Purple Royal contrast
17. inspect Glassmorphism light-surface contrast
18. verify no AndroidRuntime crash/layout exception

Capture fresh evidence:
- docs/screenshots/stage4_distance_readability_browse.png
- docs/screenshots/stage4_distance_readability_detail.png
- docs/screenshots/stage4_distance_theme_matrix.png

## Acceptance gate

Do NOT mark this task COMPLETED until:
- code is implemented;
- tests pass;
- build passes;
- APK installs;
- runtime is verified on the physical Xiaomi when available;
- default card is approximately 312 × 406dp;
- expanded card is approximately 340 × 443dp or a measured equivalent preserving the same ratio;
- typography is visibly larger;
- menu content typography is visibly larger;
- all 8 themes remain distinct;
- contrast defects are corrected;
- screenshots are committed;
- STATUS.md and HANDOFF_STATUS.md are updated with measured results;
- design communication log is updated;
- commit is pushed to member-intelligence-v3.

If physical device verification is genuinely unavailable, document that exact limitation and do not falsely claim physical acceptance.

## Git rules

Never force-push.
Never erase unrelated local work.
Never reset/discard dirty work.
Commit only intended changes.
Push to origin member-intelligence-v3.

## Model

Use Gemini 3.1 Pro High with high effort. Execute immediately.
