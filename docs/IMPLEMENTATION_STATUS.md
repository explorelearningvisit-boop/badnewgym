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
- [ ] Run `flutter pub get`, `dart format`, `flutter analyze`, `flutter test` and Android/iOS CI builds in a Flutter SDK environment.
- [ ] Verify Android emulator and physical Android device.
- [ ] Verify iOS simulator and physical iPhone using macOS/Xcode.

## Current limitations (must not be mistaken for completed production features)
- All visible member values are sample/demo fixtures.
- No backend, authentication, role authorization or live sync is connected.
- No real payment collection, receipt generation, check-in persistence or notification delivery is implemented.
- Profile portrait and 3D environment are placeholders; generated sprite sheets still need individual asset extraction, alpha/transparency validation, optimization and licensing/source manifest.
- iOS/Android builds have not been run in this environment because Flutter/Dart SDK and Android SDK/Xcode are not available here.
- No APK, AAB or IPA has been produced.

## Next milestones
1. Run the local verification commands in `flutter_app/README.md`; fix formatter, analyzer and test failures from the actual output.
2. Extract overview, profile and membership UI into feature-first presentation/domain/data layers.
3. Confirm backend provider, schema, auth, RLS and storage; then implement the concrete auth/data adapters.
4. Build the real owner/member login, gym context and member workflows.
5. Implement operational CRUD: check-in/out, workout logging, payments/receipts and measured body updates.
6. Build individual image/SVG assets with source and license manifest.
7. Add offline cache/sync only after conflict rules are defined.
8. Add accessibility, responsive, golden and integration coverage.
9. Verify Android artifacts on emulator/device and iOS simulator/device on macOS/Xcode.
