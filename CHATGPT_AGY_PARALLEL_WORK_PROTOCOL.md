# BAD GYM — Permanent Parallel Work + Merge Protocol

Version: 1.0
Status: PERMANENT
Authority: MASTER_AI_EXECUTION_CONTRACT.md

## Purpose

BAD GYM uses a capability-based two-lane workflow so ChatGPT and Google Antigravity can progress in parallel without duplicating work.

The goal is not an artificial 50/50 split by lines of code. Work is divided by capability:

- ChatGPT owns product/design/architecture/reasoning artifacts and acceptance definition.
- Google Antigravity owns repository execution, implementation, testing, build, device/runtime verification, screenshots, commit and push.
- GitHub is the merge boundary and durable shared memory.
- The user remains the final acceptance authority.

## Permanent division of work

### Lane A — ChatGPT: PLAN / DESIGN / REVIEW

ChatGPT should handle:
1. Product requirement decomposition.
2. UX and information architecture.
3. Visual/material/motion design direction.
4. Architecture decisions and implementation constraints.
5. Design tokens and behavior specifications.
6. Acceptance criteria and state matrices.
7. Test scenarios and verification requirements.
8. Risk/edge-case analysis.
9. Review of Antigravity's pushed implementation against the task.
10. Corrective task definition when evidence shows a defect.
11. Durable protocol/task documentation in GitHub.

ChatGPT must not claim to have run Android builds, tests, emulator/device sessions or GUI execution unless independently evidenced.

### Lane B — Google Antigravity: EXECUTE / VERIFY

Antigravity must handle:
1. Pull and synchronize the authorized branch.
2. Inspect the real repository and current implementation.
3. Translate ChatGPT's task/design artifacts into production code.
4. Select the appropriate available Gemini/Antigravity model and record it.
5. Implement visibly in the active GUI session.
6. Run unit/instrumentation/UI tests as applicable.
7. Run Gradle builds.
8. Install/run on the connected device when available.
9. Capture runtime/screenshots/performance evidence.
10. Fix implementation/test failures discovered during execution.
11. Update handoff/status/checkpoint/log files.
12. Commit and push the authorized branch.
13. Report exact evidence and SHA.

## Parallelism rule

Parallel means preparation can happen concurrently with execution, but product code must not be edited by two agents at the same time.

Allowed:
- ChatGPT prepares specification/design/test/acceptance artifacts while Antigravity later implements them.
- ChatGPT reviews a pushed implementation while Antigravity is idle or on a new checkpoint.
- Independent documentation can be prepared before implementation.

Not allowed:
- ChatGPT and Antigravity simultaneously editing the same production source file.
- Two agents creating competing implementations of the same feature.
- Hidden/background agents.
- Untracked side branches used as silent merge sources.
- Force-push or destructive reconciliation.

## Merge protocol

Every task follows:

CHATGPT PLAN
→ GitHub TASK PACKET
→ USER: "Pull and Run"
→ AGY SYNC
→ AGY IMPLEMENT
→ AGY VERIFY
→ AGY COMMIT/PUSH
→ CHATGPT REMOTE REVIEW
→ ACCEPT / CORRECTIVE TASK
→ NEXT STAGE

The "merge" is the evidence-backed reconciliation of ChatGPT's specification with Antigravity's implementation on the authorized Git branch.

If ChatGPT finds a defect after the push:
- do not silently edit production code;
- write a corrective task;
- Antigravity Pull and Run executes the correction;
- repeat verification.

## Same-branch rule

The authorized branch in CURRENT_TASK.md is the single delivery branch for the active task.

Do not create a parallel implementation branch unless CURRENT_TASK explicitly authorizes it.

## Fast-path rule

To save time:
- ChatGPT prepares the complete task packet before asking the user to run Pull and Run.
- Antigravity should read the task packet once, inspect only relevant source, implement, verify and push.
- ChatGPT should review the resulting SHA rather than asking Antigravity to repeat already verified work.
- Reuse SESSION_CONTEXT.md and file SHAs; do not reread unchanged large documents.

## Evidence rule

The implementation is accepted only from evidence:
1. Git/code;
2. tests/build;
3. runtime/device;
4. screenshots/measurements;
5. documented reasoning.

A model statement such as "done" is not sufficient.

## Communication sequence

Use:
- CHATGPT→AGY #NNN
- AGY→CHATGPT #NNN

Each message reports only the delta from the previous checkpoint.

## Permanent requirement

This protocol is part of the BAD GYM operating contract and must remain in GitHub for all future stages.

Every future CURRENT_TASK.md should contain a "Work split" section identifying:
- ChatGPT lane;
- Antigravity lane;
- merge/review gate.

