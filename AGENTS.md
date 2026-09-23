# BAD GYM — Agent Operating Contract

This repository is operated by a coordinated AI development loop:

ChatGPT = architecture/product/UI reasoning and task author.
Google Antigravity = primary local Android implementation, build, run, test, and repair agent.
GitHub = shared source of truth and handoff channel.

## Mandatory startup behavior

Before doing implementation work:
1. Inspect the repository state.
2. Run `git fetch origin`.
3. On `member-intelligence-v3`, run `git pull --ff-only origin member-intelligence-v3`.
4. Read `.agents/rules/00-badgym-github-loop.md`, `CURRENT_TASK.md`, and `STATUS.md`.
5. Treat `CURRENT_TASK.md` as the authoritative current task from ChatGPT.
6. Do not begin unrelated work while it contains a READY_FOR_EXECUTION task.

## Execution contract

When CURRENT_TASK.md is READY_FOR_EXECUTION:
- Execute autonomously.
- Inspect existing architecture before changing it.
- Preserve unrelated BAD GYM functionality.
- Run appropriate Android builds/tests.
- Fix compile/runtime/test failures encountered during the task.
- Update STATUS.md with changes, verification, remaining issues, and next task.
- Commit intended changes with a clear conventional commit.
- Push to `origin member-intelligence-v3`.
- Mark CURRENT_TASK.md COMPLETED only when actually completed; if blocked, mark BLOCKED and document the exact blocker.
- Never claim verification passed unless it was actually run.

## Conflict resolution
If local work conflicts with a remote ChatGPT commit, inspect and preserve intentional local changes; do not blindly discard them. Resolve deliberately and document the resolution in STATUS.md.

## Product rules
- Existing BAD GYM architecture is the baseline.
- Do not replace working screens with mockups.
- Do not delete unrelated features.
- Member Intelligence is data-driven and production-oriented.
- Never invent unavailable member data.
- Semantic states such as overdue, expired, payment failed, or complaint remain recognizable regardless of tier/theme.
- Keep domain/business logic independent from Compose UI.
- Never commit secrets or Supabase service-role keys.

## Completion signal
A successful implementation ends with code committed, pushed to member-intelligence-v3, STATUS.md updated, CURRENT_TASK.md marked COMPLETED, and remaining limitations explicitly recorded.
