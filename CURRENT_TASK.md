# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-V5-CARD-READABILITY-GEOMETRY-FIX
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission
Do not start Stage 5. Perform a focused production fix to the Member Intelligence card geometry/readability and verify it on the physical Xiaomi device.

## Required first reads
1. AGENTS.md
2. .agents/rules/00-badgym-github-loop.md
3. CURRENT_TASK.md
4. STATUS.md
5. HANDOFF_STRATEGY.md
6. HANDOFF_STATUS.md
7. docs/reference/MI_V5_DESIGN_COMMUNICATION.md

Then inspect the current Member Intelligence card, dimension tokens, and the latest Stage 4 evidence before editing.

## User-observed defect
When the card is tapped, its outer width increases but the internal typography and portrait become visually smaller. This is unacceptable. The bounded detail state must become MORE readable as it gets larger, not smaller.

## Required geometry direction
Increase the compact browse card by approximately 10% while preserving the existing aspect ratio and carousel/side-peek behavior.
Current default baseline is approximately 247dp × 356dp. Target approximately 270dp × 389dp.
Current expanded baseline is approximately 261dp × 366dp. Target approximately 286dp × 401dp.
Keep the card bounded; never make it full-screen. Preserve a visible adjacent-card side peek.

Update min/max geometry tokens so the new target is not artificially clamped. Keep the detail card bounded and proportionally larger as well; do not allow its internal content to shrink.

## Readability requirements
- The tapped/expanded card must NOT reduce font sizes, portrait size, or important spacing merely because width increases.
- Internal content should use the larger geometry tokens: portrait, member name, event/time text, membership bands, metrics, signal, CTA and menu/detail content should become at least as readable as browse mode.
- Prefer explicit dimension tokens and responsive typography over global scale-down transforms.
- Portrait should visibly increase rather than shrink.
- Preserve 48dp touch targets.
- Maintain event, identity, membership/status, decision metrics, signal and CTA hierarchy.
- Do not cram extra business data just because space is available.
- Do not change backend behavior or invent metrics.

## Interaction requirements
- Browse remains compact horizontal carousel.
- Tap opens bounded detail only.
- Back returns to browse.
- Side peek remains visible.
- Existing 11 menus remain intact.
- Menu transitions remain restrained.
- Global dashboard must not unexpectedly recolor.

## Visual direction
Use the existing Stage 4 theme engine and semantic system. Do not rewrite the theme architecture.
All 8 themes must continue to work.
Minimal Dark must remain true charcoal/slate.
Urgent semantic states must retain their semantic override.

## Physical QA
On the same Xiaomi Redmi Note 11 if available:
- testDebugUnitTest
- assembleDebug
- install and launch
- browse screenshot
- tapped/detail screenshot
- verify text and portrait readability at both states
- verify side peek
- verify Back
- smoke-test all 8 themes and the 11 menus
- no crash/layout exception

## Evidence
Add/update physical screenshots:
- docs/screenshots/stage4_card_readability_browse.png
- docs/screenshots/stage4_card_readability_detail.png
Update STATUS.md with exact final commit SHA, measured geometry, build/test/install/runtime results and any deviations.
Append a dated entry to docs/reference/MI_V5_DESIGN_COMMUNICATION.md describing the user-observed readability defect and the fix.
Update HANDOFF_STATUS.md to reflect execution state.
Commit and push to member-intelligence-v3.
Only mark CURRENT_TASK.md COMPLETED after the physical acceptance gate passes.

## Model
Use Gemini 3.1 Pro High with high effort. Execute immediately.