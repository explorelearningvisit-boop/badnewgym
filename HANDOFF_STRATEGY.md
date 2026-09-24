# BAD GYM — HANDOFF STRATEGY

## Execution order
1. Sync `member-intelligence-v3` with origin; never discard local work.
2. Read `AGENTS.md`, `.agents/rules/00-badgym-github-loop.md`, `CURRENT_TASK.md`, `STATUS.md`, this file, `HANDOFF_STATUS.md`, and `docs/reference/MI_V5_DESIGN_COMMUNICATION.md`.
3. Inspect the current Android/Compose implementation and repository assets before editing.
4. Execute the READY_FOR_EXECUTION task; do not stop at explanation or planning.
5. Build, test, install and run on the available physical device when applicable. Fix failures encountered.
6. Capture the exact evidence required by CURRENT_TASK.md.
7. Update STATUS.md and the design communication log with exact evidence and deviations.
8. Commit and push to `member-intelligence-v3`.
9. Mark CURRENT_TASK COMPLETED only after the acceptance gate is actually satisfied; otherwise mark BLOCKED with the exact blocker.

## Stage 4 visual strategy
Reference fidelity is more important than superficial theme recoloring. Preserve compact browse, bounded detail, side peek, rail, all 11 menus, semantic state clarity and stable global dashboard chrome. Reconstruct each of the eight visual languages as distinct material systems. Do not flatten Minimal Dark into a light slate theme: its reference tokens are charcoal/slate with restrained silver/white. Do not invent business metrics. Do not use continuous decorative animation or heavy 3D.

## Model strategy
Use the highest-reasoning available coding model for the long-horizon Stage 4 implementation: Gemini 3.1 Pro (High). Keep reasoning effort high. Flash High is useful for fast iteration, but the primary Stage 4 executor is pinned to Pro High because this task requires architecture inspection, visual reconstruction, Compose implementation, asset decisions, and multi-step physical QA.
