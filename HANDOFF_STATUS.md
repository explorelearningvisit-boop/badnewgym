# BAD GYM — HANDOFF STATUS

STATUS: READY_FOR_EXECUTION
BRANCH: member-intelligence-v3
CURRENT AUTHORIZATION: MI-V5-DISTANCE-READABILITY-FINAL-PRODUCTION
EXECUTOR: Google Antigravity on the user's laptop

## User-approved direction

The currently preferred bounded detail card is now the desired DEFAULT browse card.

Target:
- Default browse: ~312dp × 406dp
- Expanded detail: ~340dp × 443dp
- Same aspect ratio
- Bounded, never full-screen
- Visible adjacent-card side peek

The owner will place the phone on a desk and observe member activity from a distance. The card therefore prioritizes glanceable typography and high contrast over dense information.

## Required visual hierarchy

EVENT → MEMBER NAME → TIME → MEMBERSHIP/STATE → PRIMARY SIGNAL → KEY METRICS → CTA

Important text must be substantially larger than the previous implementation.

## Contrast requirement

Audit all 8 themes with centralized semantic contrast resolution. Accent colors must not become unreadable text on same-hue surfaces. Critical red states may use red accents, but primary text must remain high contrast.

## Required reads

- AGENTS.md
- .agents/rules/00-badgym-github-loop.md
- CURRENT_TASK.md
- STATUS.md
- HANDOFF_STRATEGY.md
- HANDOFF_STATUS.md
- docs/reference/MI_V5_DESIGN_COMMUNICATION.md

## Execution

Use Gemini 3.1 Pro High with high effort.
Implement, test, build, install, physically verify where possible, capture evidence, commit and push.
Only then mark CURRENT_TASK.md COMPLETED.
