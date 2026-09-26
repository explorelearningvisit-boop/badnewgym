# BAD GYM — Permanent ChatGPT ↔ Google Antigravity Communication Protocol

## STATUS
This document is a permanent project protocol. It must be preserved on GitHub and followed for every future ChatGPT ↔ Antigravity implementation cycle.

## 1. GitHub is the shared source of truth
- Branch: `member-intelligence-v3`
- GitHub is the authoritative communication and handoff channel.
- ChatGPT must communicate implementation intent through Git.
- Antigravity must communicate pull confirmation, implementation work, QA evidence, and final results through Git.
- Never claim a handoff was consumed merely because a commit exists remotely.

## 2. Every ChatGPT push MUST report file count + exact filenames
For every implementation push:
- Report the exact number of unique changed files.
- Report the exact path/name of every changed file.
- Report each file's purpose.
- Report commit SHA.
- Report what was added/modified/deleted where known.
- Report build/test/device verification status; never imply verification that was not performed.

### Required user-facing format
```
FILES PUSHED: <N>

1. <exact path>
   Purpose: <what this file does>

2. <exact path>
   Purpose: <what this file does>

Commit SHA: <exact SHA>
Purpose of push: <task>
Verification: <actual status>
```

**Commit count is NEVER a substitute for file count.**

## 3. Mandatory Antigravity Pull & Inspect protocol
Before Antigravity changes any code:
1. `git fetch origin`
2. `git pull --ff-only origin member-intelligence-v3`
3. `git rev-parse HEAD`
4. Record the exact pulled HEAD SHA.
5. Inspect the task/handoff documentation.
6. Verify every expected file/path exists.
7. Show the user/record the exact files found.
8. For every file, state what work is expected in that file and why.
9. Only after this inspection may implementation begin.

### Required pre-execution report
```
PULL CONFIRMATION

Branch: member-intelligence-v3
Pulled HEAD: <SHA>

FILES FOUND: <N>

1. <exact path>
   Expected work: <work>
   Reason: <why>

2. <exact path>
   Expected work: <work>
   Reason: <why>

Execution may begin: YES/NO
Blockers: <none or exact blocker>
```

If an expected file is missing, STOP and reconcile. Do not silently continue with a partial handoff.

## 4. Antigravity completion protocol
After implementation, Antigravity must record in Git:
- pulled HEAD
- exact files inspected
- exact files added
- exact files modified
- exact files deleted
- purpose of each changed file
- build result
- unit-test result
- lint result if configured
- physical-device/runtime result if run
- screenshots/evidence if produced
- final push SHA
- blockers
- any deviations from ChatGPT's requested scope

Preferred file:
`docs/reference/ANTIGRAVITY_SYNC_ACK.md`

## 5. Two-way communication
### ChatGPT → Git → Antigravity
ChatGPT provides:
- task
- exact intended files when known
- implementation purpose
- acceptance criteria
- verification requirements

### Antigravity → Git → ChatGPT
Antigravity provides:
- files actually found
- actual files changed
- implementation summary
- build/test/device evidence
- final SHA
- blockers/deviations

The next ChatGPT review must inspect the Git state and Antigravity ACK before declaring the cycle complete.

## 6. Permanent audit trail
For every meaningful implementation cycle, the repository must preserve enough information to answer:
- What was generated?
- Which exact file?
- When/which commit?
- Why was it generated?
- What changed?
- Who/which agent performed the work?
- Was it built?
- Was it tested?
- Was it run on a device?
- What was the final SHA?
- Were there blockers?

Do not erase this audit trail merely to make the repository cleaner.

## 7. File-count integrity
The authoritative file count is the unique changed-file inventory for the relevant commit/range.
- 1 commit containing 5 files = 5 files.
- 5 commits containing 1 file each = 5 files.
- Documentation-only changes must be identified as documentation-only.
- Generated images/assets count as files when they are actually committed.
- Never invent a file count.

## 8. Scope discipline
- Do not silently change unrelated files.
- If additional files become necessary, document them before/while executing and include them in the final inventory.
- No fake data, fake assets, or fabricated QA evidence.
- No claim of build/device success without actual evidence.

## 9. User visibility
The user must be able to understand the collaboration without reading raw Git history:
```
WHAT WAS FOUND → WHAT WILL CHANGE → WHY → WHAT CHANGED → VERIFICATION → FINAL SHA
```

This protocol is permanent unless the user explicitly instructs the project to replace or amend it.
