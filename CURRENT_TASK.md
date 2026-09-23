# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-STAGE-03-ADAPTIVE-INTELLIGENCE-VISUAL-UX
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


## REFERENCE-DRIVEN EXTENSION — MANDATORY

Before implementation, inspect and use:
- docs/reference/MI_V5_DESIGN_COMMUNICATION.md
- BAD_GYM_MEMBER_INTELLIGENCE_READY/preview/ALL_8_THEMES_CONTACT_SHEET.png

The 8-theme board is the visual source for the compact card's color relationships, material language, gradients, borders, glows, icon treatment, portrait framing, badges, CTA treatment and decorative assets.

Do not invent a new palette when a corresponding theme treatment exists in the board.

For every visual element decide whether it should be:
- Material/vector icon
- SVG/vector drawable
- transparent PNG/WebP
- Compose Canvas/programmatic
- optional isolated 3D/glTF

If an asset from the board is reusable, extract/reconstruct it at production resolution rather than replacing it with a generic approximation.

### Communication requirement

During Stage 2:
1. Read docs/reference/MI_V5_DESIGN_COMMUNICATION.md before coding.
2. Record implementation decisions, asset choices, measured geometry, and deviations in that file.
3. At completion append an Antigravity report containing exact commit SHA, files, device, build result, measured card width/height, screenshot paths and remaining visual deviations.
4. Commit and push the communication log together with the implementation.
5. Do not delete or overwrite prior communication history.

ChatGPT will inspect the report and screenshot evidence before Stage 3 is issued.


# STAGE 2 CORRECTIVE PASS — USER REVIEW 2026-09-23

STATUS: COMPLETED
TASK_ID: MI-V5-STAGE-02-CORRECTION-COMPACT-CARD-DETAIL-UX

The previous Stage 2 implementation satisfies the measured compact browse geometry, but the interaction composition is rejected by the user and MUST be corrected before Stage 3.

## User-approved browse geometry
- Current compact card width/height are acceptable as the baseline: approximately 232dp x 356dp.
- It is acceptable to increase width/height slightly if needed for readability and visual quality, but do NOT expand to viewport size.
- Preserve horizontal side-peek carousel behavior.

## Critical interaction correction
Current behavior: tapping the compact card opens the rich full-screen member detail, consuming the whole phone width/height.
Required behavior:
1. Tapping the compact card MUST NOT expand it into a full-screen card.
2. Keep the compact card as the persistent shell/bounds during the interaction.
3. On tap, reveal/transition to the member's detailed content INSIDE the compact card bounds (or a clearly bounded larger card/dialog that still preserves the browse context); never replace it with a full-screen member card.
4. The compact card's persistent identity/header MUST remain visible in the detail state:
   - member photo
   - member name
   - event type (CHECK-IN/CHECK-OUT/etc.)
   - event time / recency
   - current membership/status
5. The existing side vertical rail/menu MUST be present in the detailed card state. Do not remove it.
6. Menu tap changes the content panel inside the same bounded card. The rail is the persistent navigation mechanism.
7. Menu panels should show only the most important data for that menu, with the primary identity/event information remaining visible.
8. Do NOT replace the persistent identity/event area with a menu-specific header.
9. Do NOT put the entire existing full-screen PixelPerfectMemberCard into a compact card by scaling it down. Recompose the information hierarchy for bounded detail.
10. Android Back should return from bounded detail to compact browse state, not exit the screen.

## Required bounded-detail information behavior
Persistent across menu changes:
- photo
- member name + member ID
- event type
- event time/recency
- membership tier/state
- compact status indicator

Menu-specific high-value content examples:
- Home: current session, status, days left, attendance, primary insight
- Attendance: period, visits, target, streak/consistency, last visit
- Plan: plan, start, expiry, days left, freeze/renewal state
- Payment: due/overdue, last payment, next due, payment status/history summary
- Trainer: assigned trainer, next session, PT remaining, follow-up
- Workout: goal, routine, last workout, key progress
- Supplements/Nutrition/Services: active item/subscription, expiry/remaining, next action
- History: compact timeline / key recent events
- Insight: engagement, renewal/recovery signal, next action

## Visual requirements
- Preserve the selected theme's visual language from the 8-theme board.
- Keep typography readable; no miniature full-screen UI.
- Keep rail icons at usable touch size (minimum 48dp target).
- Use bounded animation around 180–250ms; avoid a full-screen zoom expansion.
- Maintain carousel context and member side-peek where the browse state is visible.

## Required QA
- Build + install on the same real device.
- Verify compact browse state.
- Verify tap -> bounded detail state.
- Verify vertical rail remains visible in detail.
- Verify photo/name/event/time remain visible after opening and while switching at least 3 menu items.
- Verify Back returns to compact browse.
- Capture and commit:
  - docs/screenshots/stage2_correction_browse.png
  - docs/screenshots/stage2_correction_detail_home.png
  - docs/screenshots/stage2_correction_detail_menu.png
- Update STATUS.md and this communication log with exact commit SHA, device, build/test result, measured compact/detail bounds, screenshots, runtime marker, and remaining deviations.
- Keep CURRENT_TASK READY_FOR_EXECUTION until every requirement is verified; only then mark COMPLETED.


## STAGE 3 — ADAPTIVE INTELLIGENCE + VISUAL UX PASS — USER REVIEW 2026-09-23

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-V5-STAGE-03-ADAPTIVE-INTELLIGENCE-VISUAL-UX

Stage 2 corrective pass is complete and verified. Implement this next pass on the existing bounded-card architecture.

### 1) Layout and accessibility
- Keep browse around 232dp x 356dp and bounded detail around 276dp x 372dp; responsive slight increase is allowed only for readability.
- Never expand to full-screen.
- Every menu must use the available bounded content area; eliminate large blank lower regions.
- Increase portrait prominence: target roughly 72–92dp wide where geometry permits, with moderate height, clear face/shoulders and no collision.
- Keep global dashboard/background colors stable. Tapping member/menu must NOT recolor the whole dashboard; theme/tier/state styling is local to the member card and its surfaces.

### 2) Tier + state visual semantics
Implement data-driven local visual semantics so users can recognize a member before reading:
- NORMAL, SILVER, GOLD, PREMIUM, VIP/ELITE use distinct but theme-compatible accents/materials.
- ACTIVE, PAYMENT_DUE/OVERDUE, EXPIRED, FROZEN, NEW/WALK-IN, TRAINER/PT ACTIVE, COMPLAINT/SAFETY/CRITICAL have distinct semantic treatments.
- Urgent semantic state overrides decorative tier styling.
- Never use color alone: pair accent with text/icon/badge/shape.
- Preserve all 8 themes; semantic overlays resolve inside the selected theme.

### 3) Rail behavior + micro-data
- Compact browse: hide vertical rail.
- Bounded detail: rail visible by default, with a compact/collapse affordance.
- Rail items may show truthful micro-data from available member data: Attendance 15/30; Payment ₹4.5k due or PAID ₹500; Trainer 2 PT; Workout 3/wk; Supplements ₹1.2k; Nutrition 18 d; Services 2 active; Plan 48 d; History 3 new; Insight Action.
- Critical/action-needed menu gets strongest local highlight even if Home remains selected. Active menu also has clear selection. Quiet menus stay quiet.
- Home remains default panel.

### 4) Menu-specific information density
Recompose each menu to fill the bounded panel intelligently; do not use generic text lists:
- HOME: KPI mini-grid + primary intelligence + next action + context row.
- ATTENDANCE: period progress + streak/consistency + recent visit timeline/heat strip.
- PLAN: plan hero + expiry countdown + freeze/renewal status + timeline.
- PAYMENT: due/paid state + amount + due date + last transaction + compact history + CTA.
- TRAINER: trainer identity + next session + PT remaining + progress/follow-up.
- WORKOUT: goal + weekly frequency + last workout + compact progression visualization.
- SUPPLEMENTS: product image/name + quantity/amount + purchase/expiry/reorder state.
- NUTRITION: subscription + plan + expiry + meal/log status + next action.
- SERVICES: active services + remaining/expiry + purchase state.
- HISTORY: compact chronological event timeline + category chips.
- INSIGHT: primary insight + evidence/source + next operational action.
Use charts only when they answer a decision. No fake metrics.

### 5) Motion
Replace identical slide-only transitions with a restrained mix:
- menu change: 180–220ms crossfade + directional shift
- number/count morph for key changing values
- local ring/bar entrance animation
- subtle rail indicator movement
- micro-badge pulse only for new/critical events
- no continuous auto-animation; respect reduced motion.

### 6) Required verification states
Verify visually on the same real device:
- Normal + Active
- Gold + Payment Due
- Premium + Active
- Expired
- Trainer/PT Active

### 7) QA evidence
Build, install, test, and capture:
- docs/screenshots/stage3_browse_semantic.png
- docs/screenshots/stage3_payment_due.png
- docs/screenshots/stage3_premium_active.png
- docs/screenshots/stage3_expired.png
- docs/screenshots/stage3_menu_density.png
Update STATUS.md + communication log with exact commit SHA, measured geometry, device/build/test results, menu coverage, runtime marker, and remaining deviations.
Do NOT start Stage 4 until ChatGPT reviews Stage 3 evidence.
Do not alter backend architecture and do not add heavy 3D.



## LIVE PROGRESS PROTOCOL — MANDATORY
At the beginning and at each major milestone, update STATUS.md with one checkpoint and push it so the owner can observe progress from GitHub without waiting for the final commit. Use these exact checkpoint values where applicable:
- IN_PROGRESS — task picked up, inspecting code/reference
- DESIGNING — architecture/UI plan finalized
- IMPLEMENTING — Compose/code/assets being changed
- BUILDING — Gradle build/tests running
- INSTALLING — APK being installed/launched on physical device
- QA — screenshots and interaction verification in progress
- COMPLETED — all acceptance evidence committed
- BLOCKED — include blocker and exact next action
Keep CURRENT_TASK.md at READY_FOR_EXECUTION until implementation is actually complete; after pickup, STATUS.md is the live heartbeat. Do not create fake checkpoint commits: each checkpoint must correspond to real work.
