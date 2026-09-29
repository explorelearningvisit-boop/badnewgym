# BAD GYM — Cellular Multi-Device Sync & Verification Contract

**Branch:** `member-intelligence-v3`  
**Purpose:** Define the production handoff for running BAD GYM on multiple Android phones using a cellular/mobile-data path rather than USB data transport.

## 1. Important separation

GitHub is the **source-of-truth for code, schemas, contracts, setup documentation and verification evidence**. It is **not the runtime database** and must not contain live member records, authentication tokens, SIM identifiers, passwords, API keys, or private network credentials.

Runtime/member/device data must be stored in the configured backend (Supabase/Postgres where that backend is selected), with local Room/cache only as an offline working layer.

## 2. Required transport

The requested test path is:

`Android phone → Teliskol/mobile-data network → Internet → HTTPS API → backend → database/realtime → all authorized BAD GYM devices`

Do **not** use USB as the application data transport for this test.

USB may still be used only for installing/debugging the APK if necessary; it must not be used as the sync/data path.

## 3. Network/port contract

Use outbound HTTPS from the Android app.

- Application transport: **HTTPS/TLS**
- Standard destination port: **TCP 443**
- Backend host: configured through the app's environment/configuration; never hard-code credentials.
- Android must use normal mobile-data connectivity and reconnect automatically after temporary loss.
- Do not design an inbound listener on the phone. Cellular carriers commonly use NAT/CGNAT, so a phone should not be expected to accept arbitrary inbound connections.
- The exact carrier/APN/network name used during a physical test must be recorded in the verification report, but secrets and SIM/private identifiers must not be committed.

If "Teliskol" refers to a specific carrier/service, the exact spelling/provider and APN must be confirmed during physical testing rather than guessed.

## 4. Device registration

Each phone must have a stable application-generated installation/device ID, for example:

- installationId
- appVersion
- platform
- deviceModel
- OS version
- lastSeenAt
- connectionState
- lastSuccessfulSyncAt
- syncCursor/version
- backend/environment

Do not use IMEI, phone number, SIM serial, MAC address or other unnecessary hardware identifiers as the application identity.

Registration flow:

`INSTALL → AUTHENTICATE → REGISTER DEVICE → INITIAL SYNC → REALTIME/DELTA SYNC → HEARTBEAT/LAST-SEEN`

A device that is already registered must reuse its registration and continue syncing. A new device must create a new registration and perform an initial authorized sync.

## 5. Multi-device data flow

When any authorized phone records a member event:

1. Write the event through the normal application/backend API.
2. Give the event a globally unique event ID and idempotency key.
3. Persist it in the backend.
4. Update required derived/member summaries server-side.
5. Deliver the delta to other authorized devices through realtime/subscription or the next sync.
6. Persist the received state locally.
7. Update Member Intelligence only from persisted/validated data.

Never use GitHub commits as a runtime synchronization mechanism.

## 6. Offline/reconnect behavior

If mobile data disappears:

- keep user-entered changes in the local Room/outbox layer;
- show an explicit offline/sync state;
- do not silently discard data;
- when connectivity returns, upload pending events with idempotency keys;
- pull the server cursor/delta;
- resolve conflicts using the documented event/version rules;
- update `lastSuccessfulSyncAt`.

## 7. Connection/progress UI

The app may show a compact connection/sync progress surface:

- Connecting
- Authenticating
- Registering device
- Initial sync 0–100%
- Syncing changes
- Up to date
- Offline / retrying
- Sync failed

The percentage must represent actual measured work (for example, known page/event counts or bytes), not a decorative timer.

After initial sync, do not continuously reload the entire dataset. Use cursor-based deltas/realtime updates and fetch detailed records on demand.

## 8. What must be saved in GitHub

Commit durable engineering context such as:

- this contract;
- backend schema/migrations;
- API/repository interfaces;
- device registration schema;
- sync protocol;
- conflict/idempotency rules;
- environment-variable names (never their secret values);
- test plan;
- physical-device verification reports;
- screenshots/log summaries that contain no secrets or personal data;
- exact commit SHA and changed-file inventory.

Do **not** commit:

- access tokens;
- service-role keys;
- passwords;
- private API keys;
- SIM/IMEI/phone-number lists;
- production member PII;
- raw authentication/session logs.

## 9. Physical verification gate

After pulling the latest branch:

1. Connect the Xiaomi test phone for installation/debugging only.
2. Confirm BAD GYM opens.
3. Enable mobile/cellular data and ensure the app is not using USB/network-over-USB for application traffic.
4. Register the device.
5. Show initial sync progress from real sync state.
6. Insert controlled test data from Device A over cellular data.
7. Confirm the backend persists it.
8. Connect Device B over its own cellular/mobile-data path.
9. Confirm Device B receives the same authorized data.
10. Insert a different controlled event from Device B.
11. Confirm Device A receives the delta.
12. Toggle mobile data off/on and verify outbox/reconnect behavior.
13. Verify duplicate delivery does not create duplicate events.
14. Record exact device model, app version, environment, network/provider label, timestamp, test IDs, result and final Git SHA.

## 10. Verification evidence format

Antigravity must report:

`MODEL → PROVIDER/CONFIG → BRANCH → PULLED_HEAD → DEVICES → NETWORK_PATH → BACKEND_ENV → TEST_CASES → BUILD → TESTS → RUNTIME → SYNC_RESULT → SCREENSHOTS → FINAL_SHA → BLOCKERS`

No runtime claim is considered verified until the physical cellular-data test produces evidence.

## 11. Acceptance criteria

- App launches normally after Pull & Run.
- Initial sync progress is truthful and visible when work is measurable.
- Every authorized device gets a unique installation registration.
- Cellular/mobile-data is the application transport for this test.
- Application traffic uses HTTPS/TLS on TCP 443 unless the configured backend explicitly documents another secure endpoint.
- Data survives reconnect.
- Cross-device deltas are persisted and visible.
- Duplicate events are prevented by idempotency.
- Local offline changes are not silently lost.
- GitHub contains the durable contract and implementation context.
- No runtime secrets or private member data are committed to GitHub.
- Final verification includes the exact SHA.

## 12. Current status

This document is the **implementation/verification contract**. It does not itself prove that the cellular sync path has already been physically tested. The physical test must be performed after Pull & Run on the target devices.
