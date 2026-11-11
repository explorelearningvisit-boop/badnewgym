# BAD GYM Flutter Cross-Platform Architecture Contract

Status: User-approved direction on 2026-11-11. This supersedes the earlier Kotlin-only implementation recommendation. It does not delete or rewrite the existing Android project.

## Product target
One Flutter/Dart application targeting:
- Android phones
- iPhone / iOS

The same Flutter feature modules, domain rules, API contracts and design system should serve both platforms. Use platform adapters only where operating-system capabilities or UI conventions differ.

## Repository strategy
Current repository root is an existing Kotlin Android/Gradle application. It contains no `pubspec.yaml`, `lib/`, `ios/` or Flutter tests at the audited baseline.

Create a new isolated Flutter application at:
`mobile_flutter/`

Keep the existing Kotlin project (`:app`) intact as a legacy/reference implementation until Flutter reaches feature parity and passes Android+iOS verification. Do not put Flutter `lib/` or iOS project files into the existing Android module. Do not delete or rename the existing project during initial migration.

The existing `backend/` FastAPI code is a low-GPS telemetry/location resolver, not a BAD GYM membership/attendance/payment backend. Existing `docs/DATABASE_SCHEMA.md` and `docs/SUPABASE_ARCHITECTURE.md` are architecture proposals; an implemented Supabase business client/repository/schema was not found in the audited source. Do not treat those documents as proof of a live backend.

## Approved default Flutter stack
- Flutter stable channel; pin the exact SDK version in project tooling/docs after checking the installed SDK.
- Dart with sound null safety.
- Material 3 with the approved BAD GYM lavender/pearl theme.
- Riverpod for application state and dependency injection.
- GoRouter for navigation/deep links.
- Dio for HTTP transport, interceptors and typed error handling.
- Freezed + json_serializable for immutable domain/DTO models where useful.
- Drift (SQLite) for cross-platform local persistence/offline cache when the actual data schema is established.
- flutter_secure_storage for supported secure token storage.
- Flutter's native animation APIs for motion.
- Use existing Flutter widgets/CustomPainter for compact charts first; select a maintained chart package only after a chart interaction requirement is demonstrated.
- Add Supabase Flutter SDK only after confirming the actual project URL, public anon key handling, SQL migrations, RLS policies, auth model and business tables. Never embed service-role keys.
- Add platform plugins only after checking Android and iOS support, maintenance, privacy declarations and required permissions.

Do not add all packages blindly at scaffold time. Start with the minimal app and add dependencies when the first owning feature needs them.

## Suggested source layout
```
mobile_flutter/
  pubspec.yaml
  analysis_options.yaml
  lib/
    main.dart
    app/
      app.dart
      router/
      theme/
      bootstrap/
    core/
      network/
      storage/
      errors/
      permissions/
      platform/
      widgets/
    features/
      auth/
      dashboard/
      members/
      member_intelligence/
        data/
        domain/
        presentation/
          overview/
          attendance/
          workout/
          payment/
          body/
      check_in/
      payments/
      settings/
  test/
  integration_test/
  android/
  ios/
```

Keep data, domain and presentation layers separated. Widgets must not query databases or call APIs directly. Repositories own IO; use cases/domain services own calculations; Riverpod providers expose state.

## Five Member Intelligence destinations
1. Overview
2. Attendance
3. Workout
4. Payment
5. Body

Reuse the latest supplied light lavender/pearl, violet, emerald, amber, rose and grey design contract. The four supplied references and existing Body Intelligence reference remain visual sources. Preserve business semantics, privacy, entitlements, role checks and payment transaction behavior.

## Cross-platform acceptance
- Same user-visible features and data semantics on Android and iOS.
- Responsive validation at 360, 375, 393 and 412 logical pixels.
- Respect safe areas, status/navigation bars, keyboard, dynamic type/font scale, accessibility labels and platform back navigation.
- Android build and test on Android Studio/SDK.
- iOS build/test on macOS with Xcode, signing and iOS simulator/device. A Windows development machine cannot run the iOS Simulator or produce a signed iOS archive locally; use a Mac or supported macOS CI for those steps.
- Verify each plugin on both platforms. Android-only capabilities must be abstracted and have an iOS-compatible alternative or an explicitly documented iOS limitation.

## Platform capability warning
Do not promise feature parity for hardware APIs without verification. In particular, iOS restricts third-party Wi-Fi/BSSID scanning and background execution compared with Android. GPS, camera, BLE, notifications, biometric authentication and background sync each need platform-specific permission and lifecycle tests. Use a platform interface / MethodChannel only when a supported plugin does not cover the requirement.

## Migration and release policy
- Build Flutter beside the existing Kotlin app; do not big-bang replace.
- Reuse backend contracts and domain rules, not Kotlin UI code.
- No production data source may be a fixture.
- No fake analytics, fake AI insights or hard-coded production chart series.
- Do not invent APIs or database columns.
- Establish real auth/member/business data source before wiring dashboard production values.
- Keep the existing Kotlin app buildable during migration.
- Release Android and iOS only after the same acceptance suite passes on both platforms.
