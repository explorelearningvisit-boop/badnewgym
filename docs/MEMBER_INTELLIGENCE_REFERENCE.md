# BAD GYM Member Intelligence — Reference Breakdown

## Approved visual direction
The uploaded references establish a bright, premium purple + neon-lime fitness game aesthetic. The 360dp mobile dashboard screenshot is the target composition; the transparent sprite sheets are the source material for the illustrated characters, equipment, road sections, props, badges and UI ornaments. Do not replace these with generic stock images or default Material cards.

## Visual tokens extracted from the reference
- Primary purple: #8A28E2 (reference palette label)
- Deep purple: #6A00DB
- Blue: #3B82F6
- Cyan: #06B6DA
- Emerald/lime success: #10B981 (use lime accents for high-energy emphasis)
- Warning: amber/orange; danger: red.
- Surfaces: white and very light lavender with subtle purple edge, layered soft shadows and selective glass effects.
- Typography direction: Poppins for display/brand and Inter for dense data labels; verify font licensing and package assets locally.
- Rounded pill statuses, icon buttons, compact cards, outlined/filled icon families and purple/lime gradient CTAs.

## Mobile overview composition (portrait)
1. Compact app bar: menu, BAD GYM logo/brand, Member Intelligence subtitle, scan action, notification, member's balance/date context only when data is real.
2. Member identity card: portrait, full name, member ID, age/gender only if permitted, plan and status chips, plan price/duration and membership progress with remaining days. No automatic renewal label/toggle unless a real consented auto-renew capability exists.
3. Fitness journey: layered winding road graphic with separate transparent road/environment assets; four compact journey cards in the same visual order: Attendance, Workout, Payment, Body. Each card contains only a few high-value facts and one clear navigation affordance.
4. Smart insight banner: deterministic and explainable insight, only when the source data supports it; otherwise hide it or show a neutral prompt. Never fabricate AI insight.
5. Bottom navigation: Overview, Attendance, Workout, Payment, Body; selected state uses purple/lime emphasis and readable labels.

## Detail navigation and information density
- Keep the shared member identity context stable while switching sections.
- Horizontal navigation is for the four intelligence domains; vertical scrolling is for records/history inside the selected domain.
- Use charts only where they add information; do not repeat the same KPI in both summary and detail unless the detail explains its breakdown.
- At 360 logical px, prevent horizontal overflow, tiny labels, overlapping art, and touch targets below accessible minimums.
- Support larger screens by expanding the content container, not by stretching the 360dp composition.

## Asset sheet handling
- Treat each uploaded sheet as a source atlas, not a final screen background.
- Extract individual characters, equipment, road/environment sections, badges, props, buttons and charts into separately named transparent PNG/SVG assets.
- Crop each element to its alpha bounds, retain safe padding, preserve source proportions, generate @2x/@3x variants only where appropriate, and optimize PNG size.
- Create an asset manifest with filename, source sheet, crop bounds, semantic ID, intended placement, aspect ratio, scale behavior and license/source note.
- Do not ship the full atlas as one giant image in the dashboard. Do not assume alpha is valid until each crop is inspected against a contrasting background.

## UI approval gate
- [ ] Match the uploaded mobile dashboard reference at 360dp.
- [ ] Replace placeholder avatar/illustrations with extracted source assets.
- [ ] Validate purple/lime palette, typography, gradients, shadows, road layering and card geometry.
- [ ] Verify horizontal section switching and vertical detail scrolling.
- [ ] Verify no overflow and readable typography at 360dp and large text scale.
- [ ] Capture Android screenshots and compare with the supplied reference.
- [ ] Obtain user approval before starting backend/API work or expanding to the remaining 20–25 screens.
