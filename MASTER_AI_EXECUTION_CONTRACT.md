# BAD GYM — MASTER AI EXECUTION CONTRACT

Version: 1.0

Purpose: make Pull and Run a deterministic, all-in-one, visible ChatGPT ↔ Google Antigravity workflow.

## Permanent operating model

GitHub is the durable source of truth between ChatGPT and Antigravity.

Roles:
- ChatGPT: product reasoning, UX/design direction, engineering architecture, task definition, acceptance criteria, and user-feedback interpretation.
- Google Antigravity: visible implementation, repository inspection, code changes, tests, builds, device/runtime verification, documentation, commit and push.
- Gemini: implementation/reasoning model used inside Antigravity; actual model/configuration must be recorded.
- User: final product acceptance/rejection.

Never treat an invisible model conversation as durable state. If a fact matters, write it to GitHub.

## One command means one complete workflow

When the user says Pull and Run, Antigravity must execute the entire authorized workflow in the current visible GUI session:

1. Inspect local branch, HEAD, worktree and remotes.
2. Fetch and safely fast-forward the authorized branch.
3. Confirm the local branch is actually the authorized branch.
4. Validate Tier-0 synchronization state.
5. Reconcile stale protocol/handoff files when Git evidence proves they are stale.
6. Load required task context, using incremental context when valid.
7. Inspect actual implementation before coding.
8. Determine the task's current state.
9. If the task is already implemented, do not duplicate it; verify, document and reconcile instead.
10. If the task is authorized and incomplete, implement it visibly.
11. Run relevant tests, builds, runtime/device and visual checks.
12. Compare results against acceptance criteria.
13. Update durable state, handoff and logs.
14. Review the complete diff.
15. Commit only intended changes.
16. Push the authorized branch.
17. Verify remote HEAD.
18. Leave a concise visible report with exact SHA, evidence, failures and next state.

IMPORTANT: “Already up to date” is only a Git synchronization result. It is NOT a reason to stop. After Git is up to date, continue with the task protocol.

## Branch authority

The branch named in CURRENT_TASK.md is authoritative.

Before work, inspect branch, HEAD, upstream and worktree. Compare local HEAD with origin/<authorized-branch>. Never silently switch to another product branch.

If the branch differs, safely switch only when the worktree permits. Otherwise stop and report the exact conflict. Never reset or discard user work. Never force-push.

## Task authority and idempotency

CURRENT_TASK.md is the executable task authorization.

Lifecycle: READY_FOR_EXECUTION → RUNNING → VERIFYING → HANDOFF_READY → COMPLETED.
Pause: RUNNING/VERIFYING → PAUSED. Review: HANDOFF_READY → REVIEWING.

If CURRENT_TASK is COMPLETED: do not blindly rerun implementation. Inspect the latest commit and evidence, reconcile stale documentation, run only missing or explicitly requested verification, and wait for a new authorized task.

If CURRENT_TASK is READY_FOR_EXECUTION: Pull and Run must execute it.

Repeating Pull and Run must be idempotent. Never duplicate migrations, assets, features or commits.

## Context loading

Tier 0 must always be validated: branch/HEAD/worktree, CURRENT_TASK.md, AI_SYNC_STATE.md, SESSION_CONTEXT.md.

First execution or invalidated context: read the governing protocol, required handoff/log files, relevant source and tests.

Later execution: reuse SESSION_CONTEXT.md; compare task version, HEAD and relevant file SHAs; read only changed/relevant files.

Invalidate cached context when CURRENT_TASK, user feedback, relevant source/test/design files, protocol rules or unexpected GitHub commits change.

Do not repeatedly reread large unchanged documents.

## Visible-only execution

All product implementation must happen in the active Google Antigravity GUI.

Do NOT launch hidden/background AGY supervisors, detached agents, autonomous bridge scripts, duplicate model runners or silent shell supervisors.

The existing autonomous/headless bridge is disabled and must remain disabled unless the user explicitly re-authorizes it.

## Reason before editing

Before meaningful implementation, visibly establish: user goal, current behavior, observed defect, root cause, intended solution, affected files, acceptance criteria and verification plan.

Do not blindly implement another model's suggestion.

For visual/UX tasks reason about information hierarchy, user decision flow, visual semantics, accessibility/contrast, material/color relationships, responsive geometry, motion and visual QA.

## Evidence hierarchy

Truth order:
1. current repository code and Git state;
2. executable test/build/runtime results;
3. screenshots/device evidence;
4. explicit user feedback;
5. documented model reasoning.

Model confidence is never evidence. A status word such as COMPLETED is never sufficient by itself.

If documentation conflicts with Git/code/evidence: preserve the code, identify the inconsistency, reconcile the documentation, and record provenance and uncertainty.

## Verification gate

Implementation is not complete merely because it compiles.

Use task-appropriate static inspection, tests, builds, runtime, connected device, screenshots, visual comparison, accessibility/contrast, performance, security and data verification.

Do not claim a check that was not actually run. If a required environment is unavailable, record unavailable rather than pretending it passed.

## Durable communication

Use monotonically increasing labels:
- CHATGPT→AGY #NNN
- AGY→CHATGPT #NNN

Each message contains deltas rather than repeated history.

Durable files:
- CURRENT_TASK.md — executable authorization
- STATUS.md — current state
- HANDOFF_STATUS.md — concise handoff
- CHATGPT_HANDOFF.md — detailed implementation/evidence packet
- SESSION_CONTEXT.md — compact checkpoint
- AI_SYNC_STATE.md — synchronization state machine
- AI_COLLABORATION_PROTOCOL.md — governing workflow
- AI_COLLABORATION_LOG.md — append-only reasoning/evidence history
- MODEL_PERFORMANCE_LOG.md — actual model/configuration and observed performance
- USER_FEEDBACK.md — explicit acceptance/rejection
- ENGINEERING_SKILLS.md — capability matrix

## Completion packet

Every implementation task must record:
- task ID and status;
- starting and ending SHA;
- branch;
- actual root cause;
- implementation summary;
- important design/technical decisions;
- actual model/configuration;
- changed files;
- exact test/build/runtime results;
- screenshots/evidence paths;
- failures/deviations;
- unresolved risks;
- exact commit SHA;
- recommended next task.

## Git safety

Never force-push, reset/discard user work, overwrite unrelated files, or silently rebase another person's work.

Always inspect diff, commit only intended work, push the authorized branch, and verify remote HEAD.

## Conflict handling

If another change appears while executing: stop editing, save checkpoint, inspect history/diff, identify relation, preserve unrelated work and reconcile explicitly.

Never silently merge contradictory task requirements.

## Stop / Resume

Stop: safely pause, save the exact continuation point to SESSION_CONTEXT.md, preserve work.

Resume: validate branch/HEAD/worktree/task, load SESSION_CONTEXT and continue; do not restart from zero unless context is invalid.

## Short commands

- Pull and Run: complete workflow from synchronization through implementation/verification/documentation/commit/push.
- Status: report only.
- Review: inspect only unless fixes are authorized.
- Recheck: repeat only missing/suspect verification.
- Stop: safe checkpoint.
- Resume: continue checkpoint.

## Final visible response

Antigravity must finish with what happened, what was verified, exact commit SHA, remote branch, limitations/deviations and next state.

Never finish only with “up to date”, “done”, or “looks good”.


## Permanent capability-based parallel work

BAD GYM uses a capability-based two-lane workflow defined in CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md.

ChatGPT lane:
- product reasoning;
- UX/design;
- architecture;
- design/motion/depth specifications;
- acceptance criteria;
- verification planning;
- post-push review and corrective-task authoring.

Antigravity lane:
- visible repository execution;
- production implementation;
- tests/build;
- device/runtime verification;
- screenshots;
- documentation;
- commit/push;
- exact evidence reporting.

This is not an artificial 50/50 code split. Work is divided by capability to minimize duplicate effort and maximize correctness.

GitHub is the merge boundary. Production source must not be edited simultaneously by both agents. ChatGPT prepares the durable task packet; Antigravity implements and verifies it; ChatGPT reviews the pushed SHA/evidence; defects become explicit corrective tasks.

Every future CURRENT_TASK.md must contain a Work split section naming both lanes and the merge/review gate.

## Permanent fast-path

Preparation and execution may overlap when their artifacts are independent, but implementation remains single-writer. The user should only need to trigger Pull and Run after the task packet is ready.
