---
description: "Always-on BAD GYM collaboration protocol for ChatGPT, GitHub, and Antigravity."
always_on: true
---

# BAD GYM GitHub Collaboration Protocol

This rule is ALWAYS ON.

## Shared source of truth
GitHub branch `member-intelligence-v3` is the shared handoff channel.
ChatGPT writes requirements/implementation to GitHub. Antigravity reads, executes, verifies, and pushes results back.

## Every agent session
Before implementation:
- `git fetch origin`
- `git pull --ff-only origin member-intelligence-v3`
- read root `AGENTS.md`
- read `CURRENT_TASK.md`
- read `STATUS.md` when present
- inspect relevant existing source files

If CURRENT_TASK.md is READY_FOR_EXECUTION, proceed without asking the user to repeat repository instructions.

## Pull means execute
When the human says "pull", interpret it as:
1. synchronize member-intelligence-v3,
2. read CURRENT_TASK.md,
3. understand the latest ChatGPT handoff,
4. execute the task,
5. verify locally,
6. commit and push,
7. write STATUS.md.

Do not stop after merely pulling if a READY_FOR_EXECUTION task exists.

## Push means report
After successful execution, push to member-intelligence-v3 and make STATUS.md the handoff report. Include status, commit, changed files, verification commands/results, remaining issues, and next task.

## Safety
Do not use destructive commands, history rewrites, force-pushes, or destructive DB operations unless explicitly required and justified. Never expose or commit secrets.

## Android verification
Prefer Gradle build/compile, unit tests, relevant UI/instrumentation tests, and emulator/device verification when available. Fix failures before completion whenever practical.

## No fake completion
Do not mark COMPLETED merely because files were edited. COMPLETED means verified to the extent possible in the local environment.
