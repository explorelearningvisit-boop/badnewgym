# BAD GYM — Pull and Run

This file is governed by MASTER_AI_EXECUTION_CONTRACT.md.

When the user says Pull and Run, execute the complete all-in-one workflow in the current visible Google Antigravity GUI.

## Mandatory sequence
1. SYNC — inspect branch, HEAD, upstream and worktree; fetch and safely fast-forward; never reset, force-push or discard unrelated work.
2. LOAD — validate CURRENT_TASK.md, AI_SYNC_STATE.md and SESSION_CONTEXT.md; load full protocol on first/invalidated context, otherwise load deltas.
3. STATE — determine READY, RUNNING, VERIFYING, HANDOFF_READY, COMPLETED or PAUSED. If Git is already up to date, continue.
4. EXECUTE — READY means execute visibly; RUNNING/PAUSED means resume checkpoint; COMPLETED means do not duplicate implementation, but reconcile/verify/document as needed.
5. VERIFY — run task-appropriate tests/build/runtime/device/visual/accessibility checks; never claim unavailable checks passed.
6. DOCUMENT — update STATUS.md, HANDOFF_STATUS.md, CHATGPT_HANDOFF.md, SESSION_CONTEXT.md and AI_SYNC_STATE.md; append collaboration/model logs.
7. COMMIT — inspect diff, commit only intended changes, push authorized branch, verify remote HEAD.
8. REPORT — visibly report task, evidence, exact SHA, branch, deviations and next state.

## Permanent safety
- Visible GUI only.
- Autonomous/headless bridge remains disabled.
- No hidden agents or detached runners.
- No force-push/reset/discard.
- No duplicate work.
- No “up to date” early exit.
- Git evidence outranks stale documentation.
- User feedback outranks model preference.
- Verification outranks confidence.

## Permanent parallel work rule

Before execution, read CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md.

ChatGPT may prepare task/design/architecture/test artifacts while Antigravity is idle or preparing to execute. Antigravity is the single writer for production implementation during the execution phase.

The merge boundary is the authorized Git branch after Antigravity verification and push. ChatGPT then reviews the remote SHA/evidence and either accepts the state or writes a corrective task.

Never interpret "parallel" as permission for two agents to edit the same production source simultaneously.
