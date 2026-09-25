# BAD GYM — AI Command Rules

These commands are permanent shorthand for MASTER_AI_EXECUTION_CONTRACT.md.

## Pull and Run
Run: SYNC → CONTEXT → STATE → EXECUTE → VERIFY → DOCUMENT → COMMIT → PUSH → REMOTE VERIFY.

A successful git pull or “Already up to date” is only the SYNC phase. Continue with the remaining phases.

## Stop
Pause safely, save the exact continuation point in SESSION_CONTEXT.md, preserve work, and do not discard changes.

## Resume
Validate branch/HEAD/worktree/task, load SESSION_CONTEXT.md, and continue from the saved checkpoint.

## Status
Report current task, state, branch, HEAD, checkpoint, blockers, verification and next action. Do not modify product code.

## Review
Review implementation against task and evidence. Do not fix findings unless explicitly authorized.

## Recheck
Run only the missing or suspect verification.

## Safety
No short command authorizes force-push, reset/discard, deletion of unrelated work, hidden/background agents, detached runners, invented product scope, or bypassing verification.

## Idempotency
Inspect Git state and checkpoint before doing work. Repeating a command must not duplicate implementation or create duplicate commits.

## Parallel Work

- ChatGPT = plan/design/architecture/acceptance/review.
- Antigravity = visible implementation/test/build/device/evidence/commit/push.
- GitHub = durable merge boundary.
- Production source has one active writer at a time.
- Every new task must record the work split and merge gate.
- "Parallel" means independent preparation can overlap; implementation itself remains single-writer.
