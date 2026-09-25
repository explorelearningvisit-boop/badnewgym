# BAD GYM — AI Command Rules

## Short commands

The following user commands have defined meanings:

### Pull and Run
Synchronize safely, load incremental context, execute CURRENT_TASK visibly, verify, document, commit, and push.

### Stop
Pause the current execution safely. Save SESSION_CONTEXT.md with the exact continuation point. Do not discard work.

### Resume
Validate Git state and SESSION_CONTEXT.md, then continue from the checkpoint. Do not restart the entire analysis unless context is stale.

### Status
Report the current task, checkpoint, Git state, current action, blockers, verification state, and next action. Do not modify product code.

### Review
Perform a review of the current implementation against the task and evidence. Do not make changes unless the user explicitly asks to fix findings.

### Recheck
Repeat only the relevant verification that is currently missing or suspect. Do not rerun the entire pipeline without reason.

## Command safety

If a short command is ambiguous in the current state, use the current task/checkpoint to resolve it. If it would change scope, ask for explicit authorization.

Never interpret a short command as permission to:
- force-push;
- reset/discard work;
- delete unrelated changes;
- create hidden/background agents;
- invent a new product feature;
- bypass required verification.

## Idempotency

Repeating the same command after a successful checkpoint should not duplicate commits, duplicate migrations, duplicate assets, or duplicate work. First inspect the checkpoint and Git state.
