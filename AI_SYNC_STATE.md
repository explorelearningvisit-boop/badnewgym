# BAD GYM — AI Sync State

## Current
- Task: PROTOCOL-ALL-IN-ONE-PULL-RUN-RECONCILIATION
- Branch: member-intelligence-v3
- State: COMPLETED
- Protocol: MASTER_AI_EXECUTION_CONTRACT.md v1.0
- Execution: Google Antigravity GUI only
- Autonomous/headless bridge: DISABLED

## State machine
IDLE → AUTHORIZED → RUNNING → VERIFYING → HANDOFF_READY → COMPLETED
Pause: RUNNING/VERIFYING → PAUSED → Resume
Review: HANDOFF_READY → REVIEWING

## Durable authority
1. CURRENT_TASK.md = executable authorization.
2. Git HEAD/worktree = code truth.
3. SESSION_CONTEXT.md = compact checkpoint.
4. CHATGPT_HANDOFF.md = detailed evidence packet.
5. STATUS.md = current state.
6. HANDOFF_STATUS.md = concise state.
7. AI_COLLABORATION_LOG.md = append-only reasoning/evidence.
8. MODEL_PERFORMANCE_LOG.md = observed model/configuration.
9. USER_FEEDBACK.md = explicit acceptance/rejection.

## Rules
- Never infer completion from prose alone.
- “Already up to date” never ends Pull and Run.
- Never run two execution sessions silently against the same task.
- Unexpected GitHub changes require inspection and reconciliation.
- SHA changes invalidate only affected assumptions unless task/protocol scope changed.
- Visible execution only.