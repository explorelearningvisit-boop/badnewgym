# BAD GYM — AI Device Transport & Deployment Progress Protocol

Version: 1.0
Date: 2026-09-29
Status: AUTHORITATIVE USER DIRECTIVE

## 1. Purpose

When ChatGPT, Google Antigravity, BAD GYM, or deployment tooling needs to move/build/install/verify data or APKs across devices, prefer the lowest-cost available transport and make every material operation observable.

GitHub remains the durable engineering/source-of-truth boundary. Runtime application data remains in the configured backend; never put secrets or production PII in GitHub.

## 2. Transport priority

Use this priority order for device-to-device or development-device deployment when the requested operation permits it:

1. **Direct wired connection** — USB cable / ADB / directly attached device.
   - First detect which physical device is actually connected by wire.
   - Prefer this path before Tailscale or Internet.
   - Avoid consuming mobile-data/internet bandwidth when a wired path is available.
   - Record the target device identity using safe operational identifiers such as installationId/model; never use IMEI/SIM serial as application identity.

2. **Local Wi-Fi path** — device is reachable over the same local Wi-Fi/LAN.
   - Prefer this over cellular/Internet when the requested operation can use it.
   - Internet access is not required for the local transfer itself.
   - Verify actual reachability before using it.

3. **Tailscale / other private network path**.
   - Use when wired and suitable local-Wi-Fi paths are unavailable or when the requested workflow specifically requires the private network.
   - Tailscale is not automatically the first choice.
   - Prefer a local route that avoids unnecessary Internet/mobile-data consumption when available.

4. **Internet / cellular data**.
   - Last resort when no suitable local/wired/private path is available.
   - When used, make the network cost and transfer size visible.

Important: transport priority is a deployment/data-transfer preference, not a rule to bypass the application's normal HTTPS/TLS backend architecture. Production application sync remains governed by the backend/sync contract.

## 3. Target selection before transfer

Before pushing/installing to multiple devices, determine the user's intended target:

- Specific device named by the user → target only that device.
- "All mobiles" → identify all currently authorized/available mobile targets and show the list before a potentially large multi-device transfer.
- "All devices" → identify mobiles, tablets and laptops separately.
- Ambiguous target → ask before transferring.
- Do not silently broadcast an APK/data update to every reachable device.

Show:
TARGET → DEVICE TYPE → MODEL/SAFE ID → CONNECTION PATH → ONLINE/REACHABLE → ACTION

## 4. Antigravity verification contract

For every meaningful execution cycle:

FOUND → PLAN → CHANGED → VERIFIED → SHA → BLOCKERS

Antigravity must inspect the exact pulled HEAD, changed files and current runtime before claiming completion.

ChatGPT reviews the resulting SHA and changed-file inventory. GitHub is the durable handoff boundary.

## 5. Transfer/build/install progress

For APK or large artifact operations, surface measurable progress:

- Build started
- Build completed
- Artifact path
- Artifact size (MB)
- Hash/version when available
- Transfer started
- Bytes/MB transferred and total when measurable
- Transfer percentage when total size is known
- Transfer transport/path
- Install started
- Install completed/failed
- App launch started
- App launch completed
- App update/sync started
- Sync phase and measurable progress
- Final state

Use real measurements only. Never invent a percentage.

## 6. In-app update/sync UX

When the app is open and an update/sync is actually occurring, expose a compact, readable progress surface:

Connecting
→ Authenticating
→ Registering
→ Downloading 0–100% (only when measurable)
→ Installing
→ Verifying
→ Syncing
→ Up to date

Failure states:
Offline / Retrying / Sync failed / Install failed

For updates where Android controls installation and exact byte progress is unavailable, state that honestly rather than displaying a fake percentage.

## 7. Communication log

The user should be able to inspect a chronological operation log for meaningful deployment/sync actions, including:

timestamp
actor/tool
operation
source
target
transport
artifact/version
size when known
status
result
SHA/build identifier
error/retry information

Example:
[14:51] Antigravity — pulled HEAD ac3545…
[14:53] Gradle — assembleDebug started
[14:55] Gradle — APK 25.7 MB created
[14:56] USB/ADB — transfer 25.7/25.7 MB
[14:56] Device — install started
[14:57] Device — install completed
[14:57] BAD GYM — launch verified

Do not expose credentials, tokens, private keys, raw authentication logs, or production PII.

## 8. AI-to-AI communication

ChatGPT and Antigravity must share durable handoff information through GitHub artifacts/docs and exact SHAs.

Every execution packet should identify:
- model/provider/configuration when known
- branch
- pulled HEAD
- files inspected
- expected work
- changes
- verification evidence
- final SHA
- blockers/deviations

Unknown model/provider information must remain UNKNOWN until reported; never guess it.

## 9. Project context loading

When an AI is instructed to "read the project", "understand the project", "continue work", "pull and run", or similar, it should first read the durable project context before modifying source:

1. CURRENT_TASK.md
2. AI_SYNC_STATE.md
3. relevant docs/reference handoff/specification for the active task
4. latest Git history/HEAD and changed-file inventory
5. relevant source files
6. current verification/build evidence

Do not assume old task sections are still active when a newer authoritative section supersedes them.

## 10. Evidence and reporting

A successful operation is not inferred from a command being issued.

Required evidence depends on the operation:
- Pull → exact HEAD + worktree status
- Build → actual build result
- Test → actual test result
- Install → actual device install result
- Runtime → actual device evidence
- Transfer → actual measured bytes/size/path where available
- Multi-device sync → backend persistence + receiving-device evidence
- Push → exact final SHA + changed-file list

No claim of "done", "verified", or "production-ready" without the corresponding evidence.

## 11. Safety / data rules

Never commit:
- passwords
- access tokens
- service-role keys
- SIM/IMEI/phone-number inventories
- raw authentication/session logs
- production member PII
- raw biometric templates or camera frames

Application device identity uses installationId plus required operational metadata, not IMEI/SIM/MAC as identity.

## 12. Default operating principle

**Use the cheapest/localest reliable transport first, ask before ambiguous multi-device targeting, measure transfers, show progress, preserve an auditable operation log, and commit durable engineering context to GitHub.**
