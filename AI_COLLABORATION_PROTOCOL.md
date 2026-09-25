# BAD GYM — AI Collaboration Protocol
## ChatGPT ↔ Google Antigravity ↔ Gemini models ↔ GitHub

GitHub is the durable intermediary between ChatGPT and Antigravity. The repository is the shared memory and evidence layer. ChatGPT does product/design/engineering reasoning and task authorization; Antigravity executes visibly in its GUI session and writes back evidence, decisions, implementation details, and questions.

## 1. The operating loop

Every cycle follows:

1. ChatGPT reads the latest repository state and prior handoff.
2. ChatGPT analyzes the user's goal, current product state, prior implementation, failures, constraints, and acceptance criteria.
3. ChatGPT writes an explicit authorized task to CURRENT_TASK.md.
4. User opens Google Antigravity and types exactly: **Pull and Run**.
5. Antigravity pulls the authorized branch and reads all required protocol/context files.
6. Antigravity inspects the actual code and current behavior before changing anything.
7. Antigravity chooses the appropriate Gemini/Antigravity model and uses the model's strongest applicable skills.
8. Antigravity implements visibly in the current GUI session. No hidden/background autonomous agent is allowed unless the user explicitly asks for it.
9. Antigravity verifies the work with appropriate tests, builds, runtime/device/browser checks, screenshots, measurements, and code review.
10. Antigravity writes the full result to CHATGPT_HANDOFF.md, HANDOFF_STATUS.md, STATUS.md, and the relevant communication/decision logs.
11. Antigravity commits and pushes the exact work to the authorized branch.
12. ChatGPT reads the new GitHub state and handoff before proposing the next task.
13. ChatGPT compares requested behavior against actual evidence, records what was accepted/rejected, and writes the next task.
14. Repeat.

## 2. No invisible conversation is authoritative

Do not rely on an agent's private chat/context as the only record of an important decision.

If a decision can affect future work, write it to the repository:
- requirement → CURRENT_TASK.md
- current state → STATUS.md
- detailed implementation → CHATGPT_HANDOFF.md
- concise execution state → HANDOFF_STATUS.md
- design rationale → docs/reference/MI_V5_DESIGN_COMMUNICATION.md or a relevant design log
- cross-agent reasoning → AI_COLLABORATION_LOG.md
- user acceptance/rejection → USER_FEEDBACK.md
- model/tool observations → MODEL_PERFORMANCE_LOG.md

## 3. ChatGPT's role

ChatGPT is the product/engineering reasoning and task-authoring side of the loop.

Before authorizing work, ChatGPT should determine:
- what the user actually wants;
- what is already implemented;
- what previous implementation got wrong;
- why it was wrong;
- what should remain unchanged;
- what should change;
- acceptance criteria;
- risks and edge cases;
- verification evidence required;
- the smallest production-grade implementation that satisfies the goal.

ChatGPT must not merely say "make it better". It must provide observable acceptance criteria.

ChatGPT may review Antigravity's implementation and disagree with it. When it does, record:
- observed evidence;
- expected behavior;
- technical/design reasoning;
- exact corrective action.

ChatGPT must not pretend to have run code or visually inspected a device when it has not. GitHub evidence and Antigravity reports must be attributed as such.

## 4. Antigravity's role

Antigravity is the visible execution side.

It must:
- pull first;
- read protocol/context files;
- inspect the real repository;
- reason about the authorized task;
- implement;
- test;
- build;
- run/inspect where possible;
- capture evidence;
- document decisions;
- commit;
- push.

Antigravity must not invent unrelated work because a model thinks it would be useful.

If requirements conflict or authorization is missing, stop and document the conflict instead of silently changing scope.

## 5. Model selection and model-to-model communication

The repository does not assume that one Gemini model is best for every task.

For each task, Antigravity should choose the appropriate available model/configuration based on:
- UI/UX reasoning;
- visual design;
- Android/Compose implementation;
- architecture;
- backend/API;
- debugging;
- testing;
- refactoring;
- documentation;
- performance.

The selected model and reason should be recorded in CHATGPT_HANDOFF.md.

If a second model is used for review, record:
- model/configuration;
- what it reviewed;
- disagreements;
- evidence supporting the final decision.

The protocol is evidence-first, not model-brand-first. A model is not considered correct because it is faster, newer, or more confident. Correctness is established by requirements + code + tests + runtime evidence + visual evidence where relevant.

## 6. Speed vs correctness

Every completed task should report, when reasonably available:
- implementation time or approximate duration;
- model/configuration used;
- files changed;
- tests/build duration if available;
- verification scope;
- known failures;
- unresolved risks.

Do not optimize for speed by skipping verification.

A faster implementation that fails acceptance criteria is not considered successful merely because it finished quickly.

## 7. User satisfaction is explicit, not inferred

Never infer that the user is happy from a successful build or a green test.

USER_FEEDBACK.md records explicit user feedback:
- ACCEPTED
- ACCEPTED_WITH_CHANGES
- REJECTED
- BLOCKED
- NEEDS_REVIEW

Each entry should include:
- task/commit;
- what the user liked;
- what the user disliked;
- requested change;
- exact date if known.

ChatGPT should use this history to avoid repeating rejected design decisions.

## 8. Evidence hierarchy

For implementation truth:
1. actual repository/code;
2. automated tests/build output;
3. runtime/device/browser evidence;
4. screenshots/videos/measurements;
5. model explanation.

For product intent:
1. explicit current user requirement;
2. explicit prior accepted decision;
3. documented design decision;
4. model suggestion.

A model's explanation cannot override direct user requirements or observed runtime evidence.

## 9. Communication packet

Every completed task should produce a machine-readable-enough human-readable packet in CHATGPT_HANDOFF.md:

- TASK_ID
- REQUEST
- USER_INTENT
- START_STATE
- IMPLEMENTATION
- WHY
- IMPORTANT_DECISIONS
- MODELS_USED
- FILES_CHANGED
- TESTS
- BUILD
- RUNTIME_DEVICE
- SCREENSHOTS
- MEASUREMENTS
- FAILURES
- DEVIATIONS
- RISKS
- USER_FEEDBACK_STATUS
- COMMIT_SHA
- NEXT_RECOMMENDED_TASK

## 10. Pull and Run contract

The user should not need to explain the architecture every time.

The phrase **Pull and Run** means:
- synchronize the current authorized branch safely;
- read all repository instructions and collaboration context;
- find CURRENT_TASK.md;
- execute only the authorized task;
- use the full relevant engineering/design skill profile;
- verify;
- document;
- commit;
- push;
- leave a complete handoff for ChatGPT.

It does NOT mean:
- launch hidden agents;
- create background supervisors;
- ignore user scope;
- reset/discard work;
- force-push;
- claim success without evidence.

## 11. Safety around Git state

Never:
- force-push;
- reset unrelated work;
- overwrite dirty user changes;
- delete another agent's work without explicit authorization;
- rewrite history to hide a failure.

If the working tree contains unrelated changes, preserve them and report them.

## 12. Definition of done

A task is complete only when:
- implementation exists;
- acceptance criteria are checked;
- appropriate tests pass or failures are documented;
- build/runtime verification is performed when applicable;
- visual work has visual evidence;
- docs/handoff are updated;
- commit SHA is known;
- branch is pushed;
- next context is recorded.

---
This file is the governing collaboration protocol for future Pull and Run cycles.


## 12. Incremental context / no-repeat reading protocol

The system must optimize for long-running work sessions. Do not reread the entire repository or every protocol file after every short follow-up command.

Maintain a compact SESSION_CONTEXT.md as the active working-memory checkpoint.

SESSION_CONTEXT.md should contain only current state: session/task ID; branch and HEAD SHA; clean/dirty state; files currently being edited; files already inspected and whether their relevant content is unchanged; last completed action; current action; exact stopping point; next authorized action; settled decisions; blockers; tests/build known; latest evidence; last communication sequence; context version/update marker.

### Context reuse rules
1. On the first Pull and Run of a task/session, perform the full required context read.
2. After that, prefer SESSION_CONTEXT.md plus git status/diff/log and only files affected by the new command.
3. Do not reread unchanged large files merely because a new command arrived.
4. If HEAD SHA, task ID, or relevant file SHA has not changed, treat previously inspected content as cached context.
5. If a file changed, reread only the changed/relevant range unless the change invalidates broader assumptions.
6. If the user stops and resumes shortly afterward, continue from the checkpoint rather than restarting analysis.
7. If ChatGPT changes CURRENT_TASK.md, task ID/version, or a material design decision, invalidate only affected context and reread relevant documents.
8. If a new model/session has no trusted SESSION_CONTEXT.md, reconstruct context once and then write a checkpoint.
9. Validate HEAD, task ID, working-tree state, and relevant file hashes before reusing context.
10. Keep the checkpoint compact. It is a cache/index, not a duplicate of the repository.

### Incremental communication
ChatGPT and Antigravity should communicate using deltas, not repeated full copies. Each update should identify: checkpoint/version; what changed since the previous checkpoint; what did not change and therefore was not reread; new decision; new evidence; next action.

Use sequence numbers such as CHATGPT→AGY #001, AGY→CHATGPT #001, CHATGPT→AGY #002. Do not paste the entire previous reasoning into every update.

### Stop/resume behavior
A Stop action is not a reset. Before stopping, Antigravity should update SESSION_CONTEXT.md with the exact safe continuation point.

When resumed: verify HEAD and working tree; read SESSION_CONTEXT.md; inspect only files affected since the checkpoint; continue from the recorded next action.

If uncertainty exists about stale context, perform targeted validation instead of rereading the whole repository.

### ChatGPT-side efficiency
ChatGPT should maintain a compact checkpoint when authoring consecutive tasks: last GitHub SHA reviewed; last handoff sequence; current task ID; explicit user acceptance/rejection; settled decisions; areas already reviewed; only new evidence requiring attention.

The next task should describe only the delta from the previous state whenever possible.

### Token/time principle
Full read once → compact checkpoint → targeted delta reads → update checkpoint.

Context caching must never override fresh user requirements or actual repository changes.
\n\n## 13. Context manifest and command semantics\n\nUse CONTEXT_MANIFEST.md to decide what must be read and what can be reused. Use AI_SYNC_STATE.md as the compact synchronization ledger and AI_COMMAND_RULES.md for short command semantics. These files exist to prevent repeated full-context reads during long sessions.\n\nA short command is not permission to expand scope. Repeated commands are idempotent: inspect the checkpoint and Git state before doing work again.\n