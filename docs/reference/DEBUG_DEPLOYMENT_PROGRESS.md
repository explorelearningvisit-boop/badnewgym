# BAD GYM Debug Deployment Progress

## Purpose

Expose the laptop → device debug deployment lifecycle inside BAD GYM with truthful telemetry.

Flow:

CONNECT → GRADLE BUILD → APK GENERATED → STREAM TRANSFER → INTEGRITY/SESSION VERIFY → INSTALL COMMIT → LAUNCH → RUNTIME VERIFY → COMPLETE

Broadcast:

com.example.badnewgym.DEBUG_DEPLOYMENT_PROGRESS

Helper:

tools/deploy_debug.ps1

## Telemetry law

Every visible percentage must have a declared measurement source.

### Gradle build

The 10→70 build range is a lifecycle indicator unless a trustworthy task/download denominator is available. It must never be presented as dependency bytes downloaded.

Gradle's Tooling API supports progress listeners and richer task/download events. If a Tooling API client is introduced later, task and file-download events can be surfaced directly.

### APK transfer

Once the APK exists, its exact byte size is known.

The deployment helper now uses Android PackageInstaller's streaming session:

1. create an install session with the exact APK size
2. stream the local APK through `pm install-write ... -`
3. count bytes written by the local streaming process
4. emit measured cumulative bytes after each 1 MiB read
5. commit the install session only after the stream finishes

Therefore a transfer message such as:

Receiving APK • 18.0 MB / 42.6 MB • 42%

is based on actual bytes read from the exact APK and written into the device install stream.

This is materially different from the previous opaque single `adb push`, which could only report transfer completion reliably.

### Integrity and installation

The streaming session uses the exact local APK size. PackageInstaller performs the package validation during commit.

If an additional independent SHA-256 device-file verification is required, it must only be added when the APK is materialized as a device-side file; the streaming-session path intentionally avoids creating a duplicate APK file.

### Process replacement

The install commit may replace the running debug app process. Deployment state must therefore be persisted before the install boundary and restored on relaunch.

## UX

The overlay should show:

- current phase
- current activity/task
- measured percentage when available
- bytes/total when available
- elapsed time
- build SHA
- error state

Preferred transfer text:

Receiving APK • 18.0 / 42.6 MB • 42% • Elapsed 00:24

Never show fabricated download MB.

## Future Gradle telemetry

Gradle's documented Tooling API can listen to build progress, task execution and file-download progress events. This is the correct future route for richer build-time telemetry instead of parsing unstable console text.

## Security

This is debug/developer workflow infrastructure only. It is not a production OTA/update mechanism.

## Verification

The implementation must be verified by Antigravity on the physical Xiaomi Redmi Note 11. A code commit alone does not establish that the stream works on the actual device.
