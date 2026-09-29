# BAD GYM — AI Sync State

Task: MI-STAGE-7.2-FINAL-PRODUCTION-INTELLIGENCE
Branch: member-intelligence-v3
State: AUTHORIZED
Execution: Google Antigravity GUI only
Autonomous/headless bridge: DISABLED
Protocol: MASTER_AI_EXECUTION_CONTRACT.md v1.0
Parallel protocol: CHATGPT_AGY_PARALLEL_WORK_PROTOCOL.md v1.0

CURRENT_TASK.md is executable authorization.
Git HEAD/worktree is code truth.
ChatGPT owns product/design/architecture/acceptance/review artifacts.
Antigravity owns visible implementation/test/build/device verification/commit/push.
Production source is not edited simultaneously by both agents.
GitHub is the merge boundary and durable shared memory.
Pull and Run continues beyond sync through execution, verification, documentation, commit/push and remote verification.
No hidden/background execution. No force-push/reset/discard.

## EVENT CARD VARIETY LAB — 2026-09-26
- Remote branch inspected directly: `member-intelligence-v3`.
- Verified `MemberEvent.kt`: 68 typed event values (plus sentinel `UNKNOWN`).
- `EventCardCatalog.kt`: now 68 catalog entries covering all typed events except `UNKNOWN`.
- Reusable archetypes: 16.
- Added `EventCardVariantResolver.kt` for contextual variants such as Late Check-in and Late Payment without duplicating Compose components.
- Updated `AdvancedEventMemberCard.kt` to surface contextual variant headlines/emphasis and late-event evidence.
- Added `docs/reference/EVENT_CARD_VARIETY_LAB_HANDOFF.md` as the durable Antigravity execution contract.
- Latest ChatGPT source commits in this packet: `7112c48f090ab57d0abe05780c03742ad5feb2b2`, `e43fac3dd6e3b93d6effbdde2d2433884cd1c6e5`, `d5138a056c65d1995e10143491b523bfb142d2f3`, `41b4db9de51640a8fcfdab1e4ec162f1c4e08d22`.
- Antigravity must pull the complete branch, inspect the latest HEAD, build/test, exercise the event-card gallery, capture runtime evidence, and push only necessary corrections.
- Commit count is never treated as file count. Exact HEAD + changed-file list is authoritative.


## DEVICE TRANSPORT + DEPLOYMENT PROTOCOL — 2026-09-29

- Authoritative protocol: `docs/reference/AI_DEVICE_TRANSPORT_AND_PROGRESS_PROTOCOL.md`
- Transport priority: wired USB/ADB → local Wi-Fi/LAN → Tailscale/private network → Internet/cellular.
- Wired/local paths are preferred to reduce unnecessary Internet/mobile-data consumption.
- Before multi-device deployment, resolve the requested target scope: specific device, all mobiles, all devices, or ask if ambiguous. Never silently broadcast.
- For every meaningful cycle expose: FOUND → PLAN → CHANGED → VERIFIED → SHA → BLOCKERS.
- Measure and report APK/artifact size and transfer bytes/progress whenever the platform exposes them; never invent percentages.
- In-app update/sync UI should expose real phases such as Connecting → Authenticating → Downloading → Installing → Verifying → Syncing → Up to date, with honest unavailable-progress states.
- Maintain a chronological, user-visible operation log where technically supported; do not log secrets, tokens, raw auth/session logs, production PII or biometric data.
- When an AI is told to read/understand/continue/pull-and-run the project, first read `CURRENT_TASK.md`, `AI_SYNC_STATE.md`, the active reference handoff/spec, latest HEAD/changed-file inventory, relevant source, and current verification evidence.
- GitHub is the durable engineering context/hand-off boundary; runtime member/device state remains in the backend.
