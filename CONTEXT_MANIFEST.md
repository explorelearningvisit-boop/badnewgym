# BAD GYM — Context Manifest

Purpose: minimize repeated reads while keeping context trustworthy.

## Read priority

### Tier 0 — always validate
- current Git HEAD
- current branch
- git working-tree state
- CURRENT_TASK.md status/task/version
- AI_SYNC_STATE.md
- SESSION_CONTEXT.md

### Tier 1 — read on first task/session or when invalidated
- ANTIGRAVITY_PULL_AND_RUN.md
- AI_COLLABORATION_PROTOCOL.md
- CHATGPT_ANTIGRAVITY_PROTOCOL.md
- ENGINEERING_SKILLS.md
- STATUS.md
- HANDOFF_STATUS.md
- CHATGPT_HANDOFF.md
- USER_FEEDBACK.md
- MODEL_PERFORMANCE_LOG.md
- AI_COLLABORATION_LOG.md

### Tier 2 — read only when relevant
- AGENTS.md
- .agents/rules/*
- task-specific design documents
- relevant source files
- relevant tests
- build configuration
- runtime/device evidence

## Invalidation

Invalidate Tier 1 broadly when:
- collaboration protocol changes;
- task ID changes;
- task version changes;
- major scope/architecture decision changes.

Invalidate Tier 2 selectively when:
- relevant file SHA changes;
- dependency/API changes;
- tests expose a new failure;
- runtime evidence contradicts the checkpoint.

## Cache principle

Do not duplicate large source text into checkpoint files. Store references, hashes, decisions, and next actions.

## Freshness

Before using cached context, validate:
- HEAD SHA;
- task ID/status;
- relevant file SHA;
- checkpoint version.

If all remain valid, reuse the cached understanding and inspect only deltas.

## Security

Do not store secrets, access tokens, passwords, private keys, or sensitive personal data in any context or handoff file.
