# BAD GYM — HANDOFF STATUS

STATUS: READY_FOR_EXECUTION
BRANCH: member-intelligence-v3
CURRENT AUTHORIZATION: MI-V6-MEMBER-INTELLIGENCE-UX-THEME-REBUILD
EXECUTOR: Google Antigravity GUI — visible execution

## Communication channel

GitHub is the durable intermediary between ChatGPT and Antigravity.

ChatGPT writes authorization, requirements, design reasoning, and acceptance criteria.
Antigravity reads those artifacts, implements visibly, verifies the result, and writes back implementation reasoning, model selection, evidence, failures, decisions, and next context.

## Required read order

1. CURRENT_TASK.md
2. ANTIGRAVITY_PULL_AND_RUN.md
3. AI_COLLABORATION_PROTOCOL.md
4. CHATGPT_ANTIGRAVITY_PROTOCOL.md
5. STATUS.md
6. CHATGPT_HANDOFF.md
7. AI_COLLABORATION_LOG.md
8. MODEL_PERFORMANCE_LOG.md
9. USER_FEEDBACK.md
10. ENGINEERING_SKILLS.md
11. task-specific design/code documents

## MI-V6 design review

The previous MI-V5 distance-readability pass is technically complete but visually rejected by the user.

Primary defect:
The eight themes currently feel like unrelated color skins rather than one coherent BAD GYM design system. Some component/menu colors also fight the selected theme and create same-hue or opposing combinations.

## Non-negotiables

- Keep all 8 themes.
- Keep shared information architecture and semantic hierarchy.
- Keep default browse approximately 312 × 406dp.
- Keep bounded detail approximately 340 × 443dp.
- Keep adjacent card peek and automatic carousel.
- Keep all 11 menus.
- Do not introduce backend/fake data.
- Do not turn the card into a mini-dashboard.
- Do not use full-screen detail.
- Do not solve theme differences by simply changing hue.
- Use actual contrast-ratio reasoning.
- Capture fresh visual evidence.

## Completion

Do not claim completion from build success alone. The final handoff must contain:
- implementation summary;
- why the changes were made;
- important design/technical decisions;
- model/configuration used;
- files changed;
- tests/build/runtime results;
- screenshots/evidence;
- deviations and unresolved risks;
- exact commit SHA;
- recommended next task.
