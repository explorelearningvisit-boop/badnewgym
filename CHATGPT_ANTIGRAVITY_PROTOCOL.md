# ChatGPT ↔ Antigravity Communication Protocol

GitHub is the durable communication channel between ChatGPT and Google Antigravity.

## Authority

- CURRENT_TASK.md = executable authorization and acceptance criteria.
- STATUS.md = current implementation state.
- CHATGPT_HANDOFF.md = detailed implementation report and next-context message.
- HANDOFF_STATUS.md = concise current handoff.
- AI_COLLABORATION_PROTOCOL.md = governing bidirectional workflow.
- AI_COLLABORATION_LOG.md = reasoning exchange and reconciliation history.
- MODEL_PERFORMANCE_LOG.md = observed model speed/quality/rework evidence.
- USER_FEEDBACK.md = explicit user acceptance/rejection only.
- ENGINEERING_SKILLS.md = broad product/design/engineering capability matrix.

## ChatGPT → Antigravity

Before creating or updating a task, ChatGPT should inspect the latest GitHub state and prior handoff.

The task must communicate:
- user goal;
- current problem;
- evidence;
- why the previous approach failed, if applicable;
- intended behavior;
- design/engineering reasoning;
- non-negotiables;
- affected scope;
- acceptance criteria;
- required verification;
- known risks.

ChatGPT may explicitly disagree with a previous implementation. That disagreement must be grounded in code, screenshots, test results, documented user feedback, or another concrete source.

## Antigravity → ChatGPT

After execution, Antigravity must write:
- what it found;
- what it implemented;
- why;
- important technical/design decisions;
- actual model/configuration used;
- files changed;
- tests;
- build;
- runtime/device/browser verification;
- screenshots/measurements;
- failures;
- deviations;
- unresolved risks;
- exact commit SHA;
- recommended next task.

## Model-to-model reasoning

If Antigravity uses multiple Gemini models/configurations, document each model's role and any disagreement.

Do not treat model confidence, speed, or branding as evidence of correctness. Correctness comes from requirements plus implementation plus verification evidence.

## User satisfaction

Do not infer user satisfaction. Record explicit feedback in USER_FEEDBACK.md.

If the user rejects a result, the next task must preserve the rejection as a constraint so the same defect is not repeated.

## Pull and Run

The user's short command **Pull and Run** means Antigravity should:
1. pull the authorized branch safely;
2. read all required protocol/context files;
3. understand the current authorized task;
4. inspect the actual code;
5. apply all relevant skills from ENGINEERING_SKILLS.md;
6. implement visibly in the current GUI session;
7. verify;
8. document;
9. commit and push;
10. leave a complete handoff for ChatGPT.

Do not launch hidden/background execution unless the user explicitly requests it.

## Important

If no task is authorized, stop and ask for authorization rather than inventing work.

Important decisions must live in the repository, not only in an invisible model conversation.
