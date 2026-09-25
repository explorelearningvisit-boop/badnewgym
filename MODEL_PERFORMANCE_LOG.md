# BAD GYM — Model Performance Log

Track model/tool performance only from observed work.

## Per-task template

### Task
- TASK_ID:
- Date:
- Model/configuration:
- Role: implementation / review / debugging / design / architecture / testing
- Approximate duration:

### Quality observations
- Requirements understood:
- Code quality:
- UI/UX quality:
- Architecture quality:
- Debugging quality:
- Test quality:
- Visual verification quality:
- Documentation quality:

### Evidence
- What was verified:
- What was missed:
- Rework required:
- User acceptance status:

### Speed / correctness
- Fast aspects:
- Slow aspects:
- Correct on first pass:
- Corrections required:
- Important lesson:

## Rules

1. Never rank models globally without evidence.
2. Do not treat speed as quality.
3. Do not treat model confidence as evidence.
4. Record concrete observations tied to a task/commit.
5. Preserve both successful and failed observations.

### Task
- TASK_ID: PROTOCOL-ALL-IN-ONE-PULL-RUN-RECONCILIATION
- Date: 2026-09-25
- Model/configuration: Gemini (Antigravity IDE)
- Role: documentation
- Approximate duration: 5 min

### Quality observations
- Requirements understood: Yes
- Code quality: N/A
- UI/UX quality: N/A
- Architecture quality: N/A
- Debugging quality: N/A
- Test quality: N/A
- Visual verification quality: N/A
- Documentation quality: Good

### Evidence
- What was verified: Git logs for MI-V6 commit e5ee32c.
- What was missed: None.
- Rework required: None.
- User acceptance status: Pending.

### Speed / correctness
- Fast aspects: File updates
- Slow aspects: None
- Correct on first pass: Yes
- Corrections required: None
- Important lesson: Git evidence must always be checked when documentation conflicts.
