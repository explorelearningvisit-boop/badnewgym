# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-V5-STAGE-01-VISUAL-FOUNDATION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Stage 1 — Visual Foundation / Forensic Geometry / Asset Inventory

Read and obey:
- AGENTS.md
- .agents/rules/00-badgym-github-loop.md
- docs/reference/MASTER_UI_UX_RECONSTRUCTION_PROMPT.md
- docs/reference/MEMBER_INTELLIGENCE_8_THEME_SPEC.md
- docs/reference/MI_V5_PRODUCTION_ROADMAP.md
- docs/reference/NATURAL_FRESH_VISUAL_SPEC.md

### Objective

Do NOT attempt to finish the entire product in this stage.

Build the production foundation that prevents the previous failure mode of making a few decorative changes while the composition remains wrong.

### Execute

1. Inspect current Member Intelligence code and identify the current screen/component entry points.
2. Create a concise forensic geometry spec for the reference board, with Natural Fresh as the primary measurable baseline.
3. Identify current visual mismatches against the existing Natural Fresh specification and current device output.
4. Inventory all existing MI assets and classify:
   - raster
   - transparent overlay
   - vector
   - programmatic
   - portrait
   - logo/icon
5. Create/complete an asset manifest with source, dimensions, intended display bounds, scaling mode, transparency and placement.
6. Establish centralized semantic design tokens for:
   - dimensions
   - spacing
   - typography
   - colors
   - surfaces/materials
   - elevation
   - motion
   - responsive breakpoints
7. Refactor the shared MI shell so that these slots are structurally explicit:
   - outer shell
   - header
   - integrated navigation rail
   - event/time header
   - large hero portrait
   - identity
   - membership
   - status
   - three decision metrics
   - primary CTA
   - decorative layers
   - menu content viewport
8. Do not redesign business data or invent data.
9. Add DEBUG-only runtime identity:
   MI-V5
   BUILD <short Git SHA>
   DEBUG
   It must not render in release builds.
10. Add the architecture hooks for theme definitions and adaptive tier/event state, but do not duplicate eight screens.
11. Prepare visual QA scaffolding so later stages can capture the same screen deterministically.
12. Build and run on the available device/emulator.
13. Capture at least one real screenshot of the new foundation.
14. Verify that the runtime marker matches the source commit.
15. Update STATUS.md with:
   - exact files changed
   - build command/result
   - device/emulator
   - runtime marker
   - screenshot location if available
   - remaining Stage 1 limitations
16. Commit and push to member-intelligence-v3.
17. Mark CURRENT_TASK COMPLETED only if the above was genuinely executed. Otherwise mark BLOCKED and document the exact blocker.

### Important

Do NOT add a heavy 3D engine in Stage 1 unless a minimal compatibility check shows it is safe. Stage 7 handles real 3D. Stage 1 should establish the geometry/material architecture first.

Do NOT mark the whole MI-V5 roadmap complete. This is only Stage 1.
