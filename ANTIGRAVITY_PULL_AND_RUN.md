# BAD GYM — "Pull and Run" Execution Contract

When the user types **Pull and Run** in Google Antigravity, execute this protocol visibly in the current Antigravity GUI session.

## 1. Synchronize
- Identify the currently authorized branch from CURRENT_TASK.md.
- Fetch and fast-forward pull the branch.
- Do not reset, force-push, discard unrelated work, or overwrite dirty user changes.

## 2. Read the complete context
Before implementation, read:
- AGENTS.md
- .agents/rules/00-badgym-github-loop.md, if present
- CURRENT_TASK.md
- STATUS.md
- HANDOFF_STATUS.md
- CHATGPT_HANDOFF.md
- CHATGPT_ANTIGRAVITY_PROTOCOL.md
- AI_COLLABORATION_PROTOCOL.md
- AI_COLLABORATION_LOG.md
- MODEL_PERFORMANCE_LOG.md
- USER_FEEDBACK.md
- ENGINEERING_SKILLS.md
- HANDOFF_STRATEGY.md, if present
- docs/reference/MI_V5_DESIGN_COMMUNICATION.md, if present
- all task-specific files named by CURRENT_TASK.md

Also inspect recent Git history and the relevant source code before editing.

## 3. Authorization
CURRENT_TASK.md is the executable authorization.

If STATUS is PAUSED_MANUAL or otherwise not authorized for execution, do not invent work. Tell the user in the visible Antigravity session that authorization is paused and stop.

If a new task is authorized, execute only that task.

## 4. Reason before coding
Write or visibly establish:
- goal;
- current behavior;
- actual defect;
- root cause;
- intended solution;
- affected files;
- acceptance criteria;
- verification plan.

Do not blindly implement a previous model's suggestion.

## 5. Full engineering skill usage
Apply every skill relevant to the task from ENGINEERING_SKILLS.md, including product reasoning, UI/UX, 2D/3D visual design, motion/animation, Android/Compose, frontend, backend/API, database, security, accessibility, testing, performance, DevOps, Git, and visual QA.

Use only the skills relevant to the actual task; do not add unnecessary technology.

## 6. Visible execution
All implementation must occur in the active Antigravity GUI session.

Do not launch:
- hidden/background AGY supervisors;
- duplicate autonomous agents;
- detached model runners;
- repository bridge scripts;
unless the user explicitly asks for such execution.

## 7. Verification
Verify at the appropriate level:
- static/code inspection;
- unit/integration/UI tests;
- build;
- runtime;
- device/browser;
- screenshots/video;
- visual measurements;
- accessibility/contrast;
- performance where relevant.

Do not call a visual task complete from build success alone.

## 8. Write back to GitHub
Before finishing, update:
- STATUS.md
- HANDOFF_STATUS.md
- CHATGPT_HANDOFF.md
- AI_COLLABORATION_LOG.md
- MODEL_PERFORMANCE_LOG.md
- USER_FEEDBACK.md when explicit feedback is available

Record:
- what was requested;
- what was found;
- what was changed;
- why;
- model/configuration;
- files;
- tests/build/runtime;
- screenshots/evidence;
- failures/deviations;
- risks;
- exact commit SHA;
- next task.

## 9. Commit and push
- Review git diff.
- Commit only intended work.
- Push the authorized branch.
- Report the exact commit SHA in the visible session and in CHATGPT_HANDOFF.md.

## 10. Finish with a ChatGPT-readable handoff
The final visible message should summarize:
- task;
- result;
- evidence;
- known limitations;
- exact SHA;
- recommended next task.

The repository is the communication channel. ChatGPT will read the pushed artifacts before creating the next task.


## Incremental context rule

Do not start every short follow-up command from zero. On first execution, build/read the full context. Then maintain SESSION_CONTEXT.md as a compact checkpoint. For subsequent commands or Stop/Resume cycles, validate HEAD/task/worktree and read only the checkpoint plus changed/relevant files. Do not reread unchanged protocol files or large source files. A changed task, material decision, relevant file, or stale checkpoint invalidates only the affected context. Before stopping, save the exact continuation point. A Stop is a pause, not a reset.
\n\n## Context loading optimization\n\nBefore reading the full protocol set, validate Tier 0 from CONTEXT_MANIFEST.md. If SESSION_CONTEXT.md is fresh and task/version/HEAD/relevant file hashes are unchanged, reuse it and load only the delta. On a first session or invalidated context, load the required Tier 1 documents once. Use Tier 2 only when relevant.\n\nRead AI_COMMAND_RULES.md for the defined short-command behavior. Read AI_SYNC_STATE.md for collaboration state and conflict rules.\n