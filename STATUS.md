# BAD GYM — Agent Status

STATUS: READY_FOR_EXECUTION
CHECKPOINT: MI-V5-DISTANCE-READABILITY-FINAL-PRODUCTION
LAST_AGENT: ChatGPT
LAST_COMPLETED_TASK: MI-V5-CARD-READABILITY-GEOMETRY-FIX
CURRENT_TASK: MI-V5-DISTANCE-READABILITY-FINAL-PRODUCTION
BRANCH: member-intelligence-v3

## Authorization

The previous geometry correction is accepted, but user review found the card and menu typography still too small for desk-distance observation.

This task is authorized as the final production readability/contrast pass before Stage 5.

## Required outcome

Use the currently preferred bounded detail composition as the new default browse card:
- default approximately 312dp × 406dp;
- expanded approximately 340dp × 443dp with the same aspect ratio.

Increase important typography and portrait scale for distance readability. Increase menu typography. Introduce centralized contrast-aware foreground resolution across all 8 themes, with special attention to saturated red/accent surfaces.

## Acceptance

Physical Xiaomi verification when available, unit tests, debug build, APK install/runtime verification, fresh screenshots, 8-theme contrast audit, 11-menu readability audit, documentation, commit and push.

Stage 5 remains blocked until this task passes.
