# Antigravity Pull & Run Protocol

When the user says **Pull and Run**, execute this protocol in the CURRENT repository.

1. Sync the current authorized branch from origin with fetch + fast-forward pull. Never reset, force-push, discard unrelated work, or overwrite dirty user work.

2. Read AGENTS.md, .agents/rules/00-badgym-github-loop.md if present, CURRENT_TASK.md, STATUS.md, HANDOFF_STRATEGY.md if present, HANDOFF_STATUS.md, docs/reference/MI_V5_DESIGN_COMMUNICATION.md if present, CHATGPT_ANTIGRAVITY_PROTOCOL.md, and ENGINEERING_SKILLS.md.

3. Treat CURRENT_TASK.md as the executable authorization. Do not invent unrelated work.

4. Work visibly in this Antigravity session. Do NOT launch hidden/background AGY supervisors, duplicate agents, or bridge scripts unless the user explicitly asks.

5. Before coding, briefly establish current behavior, user problem, constraints, intended solution, affected files, and acceptance criteria. Then implement; do not stop at a proposal.

6. Apply relevant skills from ENGINEERING_SKILLS.md: product analysis, UI/UX, accessibility, frontend/mobile, backend/API, database, security, testing/QA, performance, DevOps/Git, documentation, and prompt/context engineering. Use only what the task needs.

7. Verify with appropriate tests, lint/static analysis, build, and runtime/device/browser checks. For UI work capture required screenshots/evidence.

8. Before finishing, update STATUS.md, HANDOFF_STATUS.md, and CHATGPT_HANDOFF.md. Record what changed, why, decisions, files, tests/build/runtime evidence, limitations, screenshots, exact commit SHA, and recommended next task.

9. Review git diff, commit only intended changes, and push to the authorized branch. Never claim completion without verification.

Short trigger: Pull and Run
