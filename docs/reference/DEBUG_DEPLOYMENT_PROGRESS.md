# BAD GYM Debug Deployment Progress

## Purpose

The debug build/run workflow now exposes the laptop → device deployment lifecycle inside BAD GYM itself.

Flow:

`CONNECT → GRADLE BUILD → APK READY → APK TRANSFER → INSTALL → LAUNCH → VERIFY → COMPLETE`

The Android app listens only to the debug deployment broadcast:

`com.example.badnewgym.DEBUG_DEPLOYMENT_PROGRESS`

The Windows helper is:

`tools/deploy_debug.ps1`

Run it from the repository root in PowerShell:

`powershell -ExecutionPolicy Bypass -File .\tools\deploy_debug.ps1`

## What the user sees

- live percentage
- current phase
- elapsed time
- APK size when available
- transferred/total bytes at the transfer/install boundary
- build SHA
- completion or failure state

## Accuracy rule

Gradle's standard console does not expose a reliable total-byte percentage for all dependency downloads. Therefore the UI must **not invent download bytes**.

The helper reports deterministic lifecycle progress for the build and exact APK size/transfer completion. The progress bar is a deployment lifecycle indicator, not a fabricated measurement of Gradle dependency-download bytes.

Gradle's normal lifecycle logging is intended for build progress, while the Tooling API can expose richer build progress events if we later want task-level telemetry. See the official Gradle logging and Tooling API documentation.

## Install boundary

Android's `adb install` can replace the running debug package, so the app process may disappear during installation. Progress is persisted in SharedPreferences before that boundary. On the next launch BAD GYM restores the last deployment state and receives the final `COMPLETE` event from the helper.

## Security

This receiver is debug/developer workflow infrastructure. It must not be treated as a production update channel. Do not expose deployment broadcasts or debug deployment scripts in a production OTA/update mechanism.