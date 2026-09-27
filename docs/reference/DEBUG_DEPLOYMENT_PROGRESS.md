# BAD GYM Debug Deployment Progress

## Purpose

Expose the laptop → device debug deployment lifecycle inside BAD GYM with truthful telemetry.

Flow:

CONNECT → GRADLE BUILD → APK GENERATED → TRANSFER → INTEGRITY VERIFY → INSTALL → LAUNCH → RUNTIME VERIFY → COMPLETE

Broadcast:

com.example.badnewgym.DEBUG_DEPLOYMENT_PROGRESS

Helper:

tools/deploy_debug.ps1

## Telemetry law

Every visible percentage must have a declared measurement source.

### Gradle build

Gradle task/build progress may be reported as task or lifecycle progress. Do not convert unknown dependency-download work into fake byte percentages.

Gradle's Tooling API supports progress listeners and richer task/download events, including file-download progress events. A future implementation may use those events for task/download telemetry when the build environment can host a Tooling API client.

### APK transfer

Once the APK exists, its exact byte size is known.

The deployment helper should expose measured cumulative device-received bytes. A deterministic chunked transfer is acceptable when the normal single adb push operation does not expose a usable byte-progress stream.

Example:

Receiving APK • 18.0 MB / 42.6 MB • 42%

The displayed percentage must be calculated from measured bytes / exact APK size.

### Integrity

Before install, verify:

1. device-side APK size == local APK size
2. device-side SHA-256 == local APK SHA-256

Only then report integrity verified.

### Process replacement

The install operation may replace the running debug app process. Deployment state must therefore be persisted before the install boundary and restored on relaunch.

## UX

The overlay should show:

- current phase
- current activity/task
- measured percentage when available
- bytes/total when available
- elapsed time
- build SHA
- error state
- integrity state

Prefer activity text such as:

Gradle • :app:compileDebugKotlin • task progress

or:

Receiving APK • 18.0 / 42.6 MB • 42%

Never show fabricated download MB.

## Sources

Gradle documents that its Tooling API can execute builds while listening to stdout/stderr and progress messages, and receive task/test/build events.

Gradle's public event API also exposes file-download progress events and task progress events.

These capabilities should be preferred over parsing unstable human-readable console output when a Tooling API integration is introduced.

## Security

This is debug/developer workflow infrastructure only. It is not a production OTA/update mechanism.
