# BAD GYM — ChatGPT ↔ Google Antigravity GitHub Sync Contract

## Purpose
GitHub is the shared communication channel between ChatGPT and Google Antigravity.
Neither side should assume that a commit was pulled merely because it exists remotely.

## Current handoff
- Branch: `member-intelligence-v3`
- Remote HEAD required before execution: `ac3545396b6d4fb53fb1ad525f2004ad03f370d3`
- Expected unique file delta in the latest ChatGPT handoff: **3 files**
- Authoritative rule: verify the exact HEAD SHA and the exact changed-file paths. Never use commit count as a substitute for file count.

## Files in this handoff
1. `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/AdvancedEventMemberCard.kt`
   - ADDED
   - Event-specific light card renderer.
   - Covers Walk-in/Trial, Freeze, Ban, Payment, Trainer, Workout, Service, and operations/issue variants.
   - Keeps one primary CTA and only shows truthful attendance pulse data when sufficient recorded values exist.

2. `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCarousel.kt`
   - MODIFIED
   - Browse mode now uses `AdvancedEventMemberCard`.
   - Expanded/detail mode keeps the existing detail-card interaction flow.

3. `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceScreen.kt`
   - MODIFIED
   - Removes the noisy workspace vertical gradient.
   - Uses the active light theme/background directly.

## Mandatory Antigravity pull protocol
Before build/run:
1. `git fetch origin`
2. `git pull --ff-only origin member-intelligence-v3`
3. Run `git rev-parse HEAD` and record the resulting SHA.
4. If HEAD is not `ac3545396b6d4fb53fb1ad525f2004ad03f370d3` or a later explicitly documented SHA, STOP and reconcile before running.
5. Verify all 3 paths above exist at the pulled HEAD.
6. Only then build/test/install/run.

## Mandatory Antigravity ACK
After pulling, Antigravity must communicate back through GitHub by updating/adding:
`docs/reference/ANTIGRAVITY_SYNC_ACK.md`

The ACK must contain:
- pulled HEAD SHA
- exact list of ChatGPT handoff files found
- any additional local/remote files changed by Antigravity
- build/test result
- device/runtime result, if run
- exact push SHA after any correction
- blockers, if any

## No partial-pull rule
If even one expected file is missing:
- do not report the handoff as complete;
- do not silently continue on a partial state;
- document the mismatch in the ACK;
- pull/reconcile until the exact expected state is present.

## Two-way communication rule
ChatGPT communicates implementation intent and file-level notes through this repository.
Antigravity communicates pull confirmation, implementation changes, QA evidence, and correction SHAs through this repository.
The next ChatGPT review must read the ACK and the pushed commit before declaring the cycle complete.

## Important
Commit count != changed-file count.
The exact SHA + exact changed-file inventory is the source of truth.
