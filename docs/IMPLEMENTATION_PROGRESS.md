# BAD GYM Implementation Progress

Task: Lavender full-screen Member Intelligence rebuild
Branch: `feature/member-intelligence-lavender-rebuild`
Baseline: `backup/local-main-2026-11-11` @ `32fccc7a306d93f4c0216156e81e76b4d70f0f48`

## Status
- [x] Repository located and remote audit completed.
- [x] Remote main vs local-main backup compared (backup is 2 commits ahead, 0 behind).
- [x] Repository-specific audit committed.
- [x] Lavender full-screen reference contract committed.
- [x] Implementation plan committed.
- [x] Agent handoff and crash-recovery instructions committed.
- [ ] Local working tree and uncommitted changes inspected.
- [x] Active source-of-truth instructions updated.
- [x] Initial runtime/data-flow audit completed (production business repository not found; backend contract is a blocker).
- [x] Flutter platform absence confirmed at audited baseline; app target set to mobile_flutter/.
- [x] Cross-platform Flutter contract, execution plan and model prompts committed.
- [x] Existing Kotlin app designated legacy/reference and preserved.
- [ ] Real BAD GYM business backend/schema identified.
- [ ] Local working tree and uncommitted changes inspected.
- [ ] Flutter scaffold created and Android/iOS platform folders verified.
- [ ] Shared lavender design system implemented.
- [ ] Overview implemented.
- [ ] Attendance implemented.
- [ ] Workout implemented.
- [ ] Payment implemented.
- [ ] Body implemented.
- [ ] Android and iOS builds, tests and device screenshots verified.

## Current checkpoint
Flutter cross-platform direction is now user-approved and recorded. The Flutter contract, plan, handoff, model prompts, audit and source-of-truth overrides are committed. The existing Kotlin application remains untouched. No Flutter source scaffold or production UI code has been created yet.

## Known limitations
- The laptop's local working directory is not mounted in this session. Only user-uploaded reference images are present.
- No Flutter SDK execution was available during this remote repository audit; no Flutter scaffold/build/test is claimed.
- GitHub backup branch is the latest accessible committed baseline; it does not prove there are no uncommitted local changes.
- The backup branch has no Actions run returned by the current audit query. The latest inspected main build passed on 2026-11-02.
- No Gradle build, emulator or physical-device verification has been run in this session.

## Next action
Obtain the real BAD GYM business backend/schema or backend repository and inspect the local working tree. Then scaffold Flutter under mobile_flutter/ (never at repository root), configure Android+iOS targets, and build the shared lavender shell. Do not connect production metrics until each one maps to a verified backend field. iOS build validation requires macOS/Xcode.

## Recovery protocol
Read this file, `docs/implementation/AGENT_HANDOFF.md`, `AGENTS.md`, `CURRENT_TASK.md`, `docs/reference/FLUTTER_CROSS_PLATFORM_CONTRACT.md` and `docs/implementation/FLUTTER_CROSS_PLATFORM_PLAN.md`. Check `git status` and current SHA. Never discard or overwrite uncommitted changes. Resume at the first unchecked item.
