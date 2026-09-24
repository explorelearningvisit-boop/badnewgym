# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-V5-STAGE-04-REVIEW-FIX-AND-EVIDENCE-GATE
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission
Do not start Stage 5. Re-open Stage 4 as a verification/fix pass and make the repository satisfy the Stage 4 acceptance gate exactly.

The previous Stage 4 commit is:
64adafde8c39a451eeb579fcbc5e8703189c0d2a
"feat: implement stage 4 eight theme engine and reference fidelity"

I have verified from GitHub that this commit is the branch HEAD and that it changed the actual production files:
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/design/colors/ColorTokens.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCard.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/ThemeOrnamentLayer.kt
and added physical screenshots.

## Required first reads
Read, in this order:
1. AGENTS.md
2. .agents/rules/00-badgym-github-loop.md
3. CURRENT_TASK.md
4. STATUS.md
5. HANDOFF_STRATEGY.md
6. HANDOFF_STATUS.md
7. docs/reference/MI_V5_DESIGN_COMMUNICATION.md

Then inspect the current branch source and screenshot evidence before editing.

## Known gate discrepancy to resolve
The Stage 4 task specified these exact screenshot paths:
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

The current commit instead contains files named stage4_theme_* plus individual semantic/menu files. Fix this evidence mismatch. Prefer renaming/copying the already captured physical screenshots to the exact required filenames when they correspond to the same evidence. If any required matrix screenshot is not actually a valid physical-device capture, capture it on the physical device now. Do not fabricate evidence.

## Visual/source review
Verify and fix, if needed:
- centralized ThemeId/ThemeResolver/ColorTokens architecture is actually used by the production card;
- all 8 themes are materially distinct, not only accent-color swaps;
- Minimal Dark is true charcoal/slate, not light slate;
- Glassmorphism has translucent/atmospheric depth;
- Premium 3D has metallic/depth treatment without heavy continuous 3D;
- Natural Fresh has botanical language;
- Neon has sapphire/cyan glow;
- Vibrant Gradient has multi-hue gradient language;
- Beast Mode has aggressive red/athletic language;
- Purple Royal has violet/lilac depth;
- semantic states override decoration where urgent;
- tier styling remains subordinate to critical states;
- all 11 menus remain implemented and bounded;
- browse card/detail dimensions and side peek remain intact;
- actual portraits remain used;
- global dashboard does not unexpectedly recolor;
- no fake metrics or backend claims;
- no infinite decorative animation.

Do not rewrite working architecture merely for cosmetic reasons. Make the smallest production-quality changes necessary.

## Physical QA
Use the same Xiaomi physical device if available. Build, test, install, launch, and verify:
- browse
- bounded detail
- Back
- all 8 themes
- at least 5 semantic states
- all 11 menus
- side peek
- actual portraits
- no crash/layout exception

Run the relevant unit tests and assembleDebug. Fix any failures.

## Evidence and documentation
After verification/fixes:
1. Commit exact required Stage 4 screenshot filenames.
2. Update STATUS.md with exact final implementation commit SHA, device/build/test results, geometry, 8-theme coverage, semantic coverage, 11-menu coverage, exact screenshot paths, runtime marker, and remaining deviations.
3. Append a Stage 4 review/fix report to docs/reference/MI_V5_DESIGN_COMMUNICATION.md.
4. Update HANDOFF_STATUS.md so it no longer contradicts the final state.
5. Commit and push to member-intelligence-v3.
6. Only then set CURRENT_TASK.md to STATUS: COMPLETED.

## Completion rule
Do NOT claim completion from Gradle success alone. The task is complete only when the evidence paths, source architecture, visual coverage, physical QA, and documentation all agree.

Use Gemini 3.1 Pro High with high effort for this pass. Execute immediately; do not wait for another turn.
