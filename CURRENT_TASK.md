# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-STAGE-02-COMPACT-MEMBER-CARD
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Stage 2 — Compact Member Intelligence Card Reconstruction

Read and obey:
- AGENTS.md
- .agents/rules/00-badgym-github-loop.md
- docs/reference/MASTER_UI_UX_RECONSTRUCTION_PROMPT.md
- docs/reference/MEMBER_INTELLIGENCE_8_THEME_SPEC.md
- docs/reference/MI_V5_PRODUCTION_ROADMAP.md
- docs/reference/FORENSIC_GEOMETRY_SPEC.md
- docs/reference/ASSET_MANIFEST.md
- STATUS.md

### Product correction — IMPORTANT

The Stage 1 foundation screenshot is present in the repository, but its current composition is NOT the target product composition.

Do NOT treat the Natural Fresh full-screen reference as a requirement that every member card occupy the full 360dp device width or most/all of the viewport height.

BAD GYM Member Intelligence is a member-list intelligence surface. The user must be able to see multiple members at once.

Primary browse requirement:
- Member card width target: approximately 220–240dp on a 360dp-wide phone.
- Never force the card to 360dp/full-screen width in the member-list/browse surface.
- Show the next/previous member card partially at the sides so the horizontal carousel visibly communicates more members.
- Horizontal spacing/gap should be intentional, approximately 10–14dp.
- Card height should normally be clearly below half of a typical phone viewport; target approximately 300–360dp on common 720–800dp-height devices, with responsive scaling.
- Do not stretch the card merely to fill available height.
- Preserve comfortable information density: identity + event + membership/status + a few decision signals + one contextual CTA.
- Do NOT put finance/revenue/transaction dashboards into this member card. Those are separate product surfaces.
- The card is an intelligence summary, not a complete member detail page.

### Interaction model

Implement the member intelligence surface as:
1. Horizontal member-card carousel/list.
2. Compact intelligence card.
3. Tap a card -> member detail/intelligence view can use the richer/full composition and the existing menu architecture.
4. Swipe horizontally -> next member.
5. Card width must remain stable and not expand to fill the viewport.
6. Preserve existing vertical scrolling only where detail content actually requires it.

### Visual direction

Keep the useful Natural Fresh visual language:
- pale mint/white premium surface
- green BAD GYM identity
- glass/soft depth
- portrait
- event/time
- member identity
- membership tier
- active/status state
- 2–3 high-value metrics
- contextual CTA
- restrained botanical decoration

But redesign the composition for compact member intelligence browsing:
- prioritize portrait + identity + live event at the top
- compact membership/status row
- concise intelligence signal
- compact decision metrics
- small but obvious CTA
- decorative leaves only where they improve hierarchy
- no giant empty regions
- no oversized rail consuming most of the card
- no full-screen shell inside each list card
- no tiny unreadable text caused by over-compression

### Geometry requirements

Create a dedicated compact-card geometry token set. Do not hack existing full-screen values.

Recommended starting geometry (must be responsive and measured):
- card width: 220–240dp at 360dp viewport
- card min width: 210dp
- card max width: 250dp
- card height: approximately 300–360dp at common phone heights
- outer horizontal padding: 12–16dp
- carousel gap: 10–14dp
- corner radius: 20–24dp
- portrait region: approximately 30–38% of card height
- event/header region: approximately 10–14%
- identity + membership/status: approximately 20–24%
- intelligence/metrics: approximately 18–24%
- CTA: approximately 8–11%
These are starting constraints, not permission to distort content. Tune from real screenshots.

### Information hierarchy

P0:
- current event/status
- member name/photo
- membership state
- urgent/actionable intelligence

P1:
- attendance/payment/workout compact decision metrics
- trainer/session or other relevant context

P2:
- secondary decorative/background details

Do not display every available data point on the compact card.

### Architecture

- Keep the existing shared Member Intelligence engine and menu registry.
- Do not create eight separate card implementations.
- Separate compact browse presentation from rich member-detail presentation using shared data/state.
- Keep tier/event/state/theme as data-driven inputs.
- Keep semantic tokens centralized.
- Keep all business data unchanged.
- Do not invent new fake business metrics.

### Accessibility

- Minimum interactive target 48dp.
- Maintain readable typography at compact size.
- Never encode important state using color alone.
- Content descriptions for member image and action icons.
- Horizontal carousel must be keyboard/accessibility navigable where supported.

### Animation

Add restrained production motion:
- horizontal card snapping/settling
- subtle scale/elevation change for selected card
- 180–250ms transitions
- no distracting auto-animation
- respect reduced-motion/accessibility settings

### Required visual QA

1. Build debug.
2. Install/run on the real available device.
3. Capture the actual compact member-list screen.
4. Capture a second screenshot with at least two cards visible/partially visible.
5. Save the latest screenshots to:
   - docs/screenshots/stage2_compact_member_card_latest.png
   - docs/screenshots/stage2_compact_member_card_peek.png
6. Commit and push BOTH screenshots.
7. Update STATUS.md with:
   - exact commit SHA
   - device
   - build result
   - screenshot paths
   - actual measured card width/height
   - whether two-card side peek is visible
   - any remaining visual limitations.
8. CURRENT_TASK.md must remain READY until all required work is actually complete; then mark COMPLETED.
9. Do not claim completion from Gradle success alone.

### Acceptance criteria

Stage 2 passes only when:
- card is visibly compact rather than full-screen
- approximately 220–240dp wide on 360dp viewport
- substantially less than full viewport height
- another member card is visibly peeking beside it
- content remains legible and useful
- no giant empty areas
- no finance/revenue dashboard content is mixed into the member card
- screenshot evidence is committed to GitHub
- STATUS contains real device/build/screenshot evidence

If any criterion fails, mark BLOCKED or continue fixing before completion.

## Important

Do not start Stage 3 or other roadmap stages.
Do not add heavy 3D.
Do not replace the architecture.
Focus only on the compact member intelligence browse/card experience.
