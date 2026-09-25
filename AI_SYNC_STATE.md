# BAD GYM — AI Sync State

This is the compact synchronization ledger for ChatGPT ↔ GitHub ↔ Antigravity.

## State machine

- IDLE: no active execution.
- AUTHORIZED: CURRENT_TASK contains an authorized task.
- RUNNING: Antigravity is actively executing visibly.
- PAUSED: execution stopped with a saved checkpoint.
- VERIFYING: implementation exists; verification is in progress.
- HANDOFF_READY: evidence and handoff are complete; awaiting ChatGPT review.
- REVIEWING: ChatGPT is reviewing the latest result.
- SUPERSEDED: a newer task replaces this state.

## Current
- Task: MI-V6-MEMBER-INTELLIGENCE-UX-THEME-REBUILD
- Branch: member-intelligence-v3
- State: AUTHORIZED
- Last known checkpoint: SESSION_CONTEXT.md
- Last known communication sequence: initialization
- Last known implementation evidence: none for MI-V6

## Synchronization rules

1. CURRENT_TASK.md is the authorization source.
2. SESSION_CONTEXT.md is the short-lived working-memory cache.
3. CHATGPT_HANDOFF.md is the detailed result packet.
4. AI_COLLABORATION_LOG.md is append-only reasoning history.
5. USER_FEEDBACK.md is the explicit acceptance/rejection history.
6. Git HEAD plus working-tree state are the source of truth for code state.
7. A SHA change invalidates assumptions only for affected files unless the task itself changed.
8. Never infer completion from a status word alone; require evidence.
9. Never let two execution sessions silently edit the same authorized task concurrently.
10. If a concurrent modification is detected, stop, preserve work, and reconcile through Git.

## Sequence protocol

Communication uses monotonically increasing human-readable sequence labels:
- CHATGPT→AGY #NNN
- AGY→CHATGPT #NNN

Each message records only deltas from the previous checkpoint.

## Conflict protocol

If ChatGPT updates the task while Antigravity is executing:
- do not silently merge incompatible requirements;
- save the current checkpoint;
- report the conflict;
- validate the new task version;
- resume only when the authorized scope is unambiguous.

If GitHub changed unexpectedly:
- inspect commit history and diff;
- preserve unrelated work;
- do not reset;
- identify the author/source of the change;
- reconcile before continuing.

## Evidence rule

A claim is not considered verified until its evidence is recorded. Model reasoning is explanatory context, not proof.
