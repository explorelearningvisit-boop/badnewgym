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
- [x] Expanded CI workflow to include Android debug APK and iOS simulator builds.
- [x] Added bottom navigation and two widget smoke tests.
- [x] Added app setup instructions.
- [ ] Run `flutter pub get`, `flutter analyze`, `flutter test` in a Flutter SDK environment.
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
1. Wait for CI and fix any analyzer/test/build failures.
2. Split the prototype into feature-first presentation/domain/data layers.
3. Build individual asset pack from the generated visual references.
4. Confirm backend schema/auth/RLS and create typed repository contracts.
5. Implement member, gym, attendance, workout, payment, body and admin workflows.
6. Add offline cache/sync only after domain conflict rules are defined.
7. Add accessibility, responsive, golden and integration coverage.
8. Build and verify Android artifacts; build iOS artifacts on macOS/Xcode CI.
