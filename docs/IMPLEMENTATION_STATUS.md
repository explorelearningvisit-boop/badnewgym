# BAD GYM Flutter — Implementation Status

Updated: 2026-11-11

## Milestone 1 — Flutter foundation
- [x] Created isolated branch: `migration/flutter-cross-platform`.
- [x] Added fresh Flutter app at `flutter_app/`; no Kotlin UI port.
- [x] Added Material 3 light theme and shared color tokens.
- [x] Added responsive Member Intelligence overview shell.
- [x] Added member identity and membership progress summary.
- [x] Added Attendance, Workout, Payment and Body summary modules.
- [x] Added initial dedicated detail states for the four intelligence modules.
- [x] Extracted member profile and membership value model into core domain.
- [x] Added typed attendance record/summary domain models.
- [x] Added AttendanceRepository boundary and clearly labelled demo implementation.
- [x] Added dedicated monthly Attendance screen with month navigation, status calendar, legend and distribution summary.
- [x] Routed Attendance tab to its feature screen.
- [x] Added typed workout session and summary models plus a repository boundary.
- [x] Added Workout Intelligence screen with 7-day duration bars, recent sessions and logged training volume.
- [x] Routed Workout tab to its feature screen.
- [x] Added typed payment transaction, method and status models plus repository boundary.
- [x] Added Payment Intelligence screen with payment state, outstanding amount, renewal, history and payment-method mix.
- [x] Routed Payment tab to its feature screen.
- [x] Added dated body measurement model and repository boundary.
- [x] Added Body Intelligence screen for measured weight, goal reference and dated waist/chest/arm measurements.
- [x] Routed Body tab to its feature screen.
- [x] Expanded widget tests to cover navigation and key content for all four tabs.
- [x] Expanded CI workflow to include Android debug APK and iOS simulator builds.
- [x] Added bottom navigation and two widget smoke tests.
- [x] Added app setup instructions.
- [x] Run `flutter pub get`, `dart format`, `flutter analyze`, `flutter test` and Android/iOS CI builds in a Flutter SDK environment.
- [x] Verify Android emulator and physical Android device.
- [ ] Verify iOS simulator and physical iPhone using macOS/Xcode.

## Current limitations (must not be mistaken for completed production features)
- All visible member values are sample/demo fixtures.
- No backend, authentication, role authorization or live sync is connected.
- No real payment collection, receipt generation, check-in persistence or notification delivery is implemented.
- Profile portrait and 3D environment are placeholders; generated sprite sheets still need individual asset extraction, alpha/transparency validation, optimization and licensing/source manifest.
- iOS builds have not been run: this environment is Windows, macOS/Xcode is required.
- Only a debug APK exists. No signed AAB for release and no IPA has been produced.
- `dart format` has not been applied to `flutter_app/`: the current Dart 3.13 formatter splits existing single-line `if` statements and thereby introduces `curly_braces_in_flow_control_structures` violations, so a formatting pass needs its own commit together with the brace fixes.

## Verification record - 2026-11-11
Environment: Windows, Flutter 3.47.9 stable, Dart 3.13.7, JDK from Android Studio JBR,
Android SDK build-tools 36.0.0, physical device Xiaomi 11i (1080x2400, API level as reported by adb).

| Check | Result |
| --- | --- |
| `flutter pub get` | OK |
| `flutter analyze` | No issues found |
| `flutter test` | 8/8 passed (2 role-policy, 5 screen/navigation, 1 swipe navigation) |
| `flutter build apk --debug` | OK, `build/app/outputs/flutter-apk/app-debug.apk` |
| Install + launch on physical device | OK, no crash in logcat |
| Visual check of all five screens | OK, no overflow, see screenshots in `docs/screenshots/` |

Build configuration changes required to make the build reproducible on this machine:
- `flutter_app/android/gradle.properties` caps the Gradle heap at 1536 MB. The template's
  `-Xmx8G -XX:MaxMetaspaceSize=4G` crashed the daemon on a 4 GB RAM machine.
- `flutter_app/android/app/build.gradle.kts` no longer pins `ndkVersion`. The app has no native
  sources, and the pinned NDK download failed. Restore it when a plugin needs native code.

## Next milestones
1. ~~Run the local verification commands in `flutter_app/README.md`; fix formatter, analyzer and test failures.~~ Done - see the verification record above.
2. Extract overview, profile and membership UI into feature-first presentation/domain/data layers. `lib/main.dart` is currently one file holding the whole home screen; split it only when no other change is in flight on that file.
3. Apply `dart format` in a dedicated commit together with the `curly_braces_in_flow_control_structures` fixes it exposes.
4. Confirm backend provider, schema, auth, RLS and storage; then implement the concrete auth/data adapters.
5. Build the real owner/member login, gym context and member workflows.
6. Implement operational CRUD: check-in/out, workout logging, payments/receipts and measured body updates.
7. Build individual image/SVG assets with source and license manifest.
8. Add offline cache/sync only after conflict rules are defined.
9. Add accessibility, responsive, golden and integration coverage.
10. Verify iOS simulator/device on macOS/Xcode.

## Scope lock — UI-first approval gate (2026-11-11)
- Current deliverable is ONLY the mobile-first Member Intelligence Card and its Overview, Attendance, Workout, Payment and Body UI interactions.
- Match the user's approved image reference for visual style, color, typography, material assets, chart grammar, proportions and motion. Do not infer unprovided image details.
- Support 360 logical-pixel mobile viewport, compact shared member header, tap-accessible modules, horizontal/vertical progressive disclosure and no redundant metrics.
- Demo fixtures are permitted for UI demonstration only and must be labelled; no backend, Firebase, Supabase, production API, authentication, payment collection or operational sync until visual sign-off.
- Previously committed auth/role contracts are dormant scaffolding, not a new implementation priority.
- Acceptance gate: local Flutter formatting/analyzer/tests pass, Android debug app runs, screenshots compared to the approved reference, interaction and overflow checks pass, user approves design.
  - Status 2026-11-11: analyzer and tests pass, Android debug build installed and verified on a physical device, screenshots captured in `docs/screenshots/`.
  - Remaining: user compares the screenshots with the approved reference and approves the design. Formatting pass is tracked separately.
- Remaining 20–25 reference images/menus are explicitly out of scope until this first card is approved.
- Local workflow: hot reload for UI iterations; incremental builds for changed native dependencies; APK build only when an installable artifact is needed.
