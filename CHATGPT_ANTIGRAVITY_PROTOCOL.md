# ChatGPT ↔ Antigravity Communication Protocol

Repository artifacts are the durable communication channel between ChatGPT and Antigravity.

CURRENT_TASK.md = executable authorization.
STATUS.md = current implementation state.
CHATGPT_HANDOFF.md = detailed implementation report and next-context message.
HANDOFF_STATUS.md = concise handoff state.
docs/reference/MI_V5_DESIGN_COMMUNICATION.md = long-lived design decisions when applicable.

After every completed task, Antigravity must document: request, implementation, rationale, important technical/design decisions, files changed, tests/build/runtime checks, screenshots/evidence, deviations, unresolved risks, exact commit SHA, and recommended next step.

On the next interaction ChatGPT should inspect the latest GitHub state and these handoff files before creating the next task. The user should not need to repeat technical context already documented.

Important decisions must be written to the repository; do not rely on an invisible agent conversation as the only source of truth.

If no task is authorized, stop and ask for a task rather than inventing work.
