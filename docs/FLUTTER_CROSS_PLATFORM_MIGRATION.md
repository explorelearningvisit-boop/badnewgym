# BAD GYM — Fresh Flutter Product (Android + iOS)

## Non-negotiable product direction
This is a NEW product implementation from a clean Flutter/Dart application foundation. Do not migrate, port, copy, or depend on the former Kotlin/Android UI implementation. Old implementation decisions are not constraints. The approved product requirements, data rules and supplied BAD GYM visual reference are the source of truth.

Android and iOS are first-class targets from the first commit. Maintain one shared Flutter codebase and shared product behavior. Add platform adapters only where platform APIs genuinely differ. Do not create separate Android and iOS application implementations.

## Targets
- Android phones and iPhones: primary.
- iPad, Android tablets and foldables: adaptive secondary targets.
- Compact reference viewport: 360 logical px wide.
- Handle safe areas, notches, dynamic islands, gesture navigation, keyboard, large text, orientation and platform accessibility.
- Visual reference: BAD GYM fitness journey/road dashboard. Rebuild as native Flutter composition with independent image assets; never ship a flattened screenshot as the app UI.

## Technology selection
Use stable compatible releases and pin versions in pubspec.lock after SDK verification.
- Flutter stable / Dart stable: shared cross-platform UI and application logic.
- Material 3 + custom ThemeExtension/design tokens: consistent theme and controlled component variants.
- Riverpod: dependency injection, state management and testable feature boundaries.
- go_router: declarative navigation and deep links.
- freezed + json_serializable: immutable domain/DTO models and serialization.
- dio: typed HTTP client, interceptors, timeouts and error mapping (only if REST API is selected).
- flutter_secure_storage: secure storage for refresh/session secrets; never store secrets in preferences.
- drift (SQLite): typed local database/offline cache when offline workflows require it.
- connectivity_plus: network-state hints only; never treat connectivity as proof that the server is reachable.
- fl_chart: charts with accessible labels/tooltips; use custom painters only for genuinely bespoke visualizations.
- flutter_svg: vector icons/illustrations.
- cached_network_image: remote image caching where needed.
- image: image resizing/optimization in build tooling, not runtime UI.
- permission_handler: permissions only for supported platform features.
- camera / mobile_scanner / local_auth / firebase_messaging: add only after the relevant feature and platform requirements are confirmed.
- intl: locale-aware dates, numbers and currency.
- flutter_test + integration_test + golden tests: automated quality gates.
- Patrol may be evaluated for native permission/system-flow integration tests if the test matrix needs it.

Avoid dependency sprawl. Every package must have an explicit use case, maintained upstream, compatible licenses and current Flutter/Dart support. Do not add a package solely because it is popular.

## Backend strategy
Keep backend selection independent of UI. First define domain contracts and API boundaries. If the existing Supabase project is still the intended system of record, verify its schema, RLS, auth, storage and edge functions before connecting; do not assume old credentials or schema remain valid. If backend is not ready, use repository interfaces plus deterministic local fixtures for UI development. Never fake production data or ship fixture credentials.

## Architecture
Feature-first, layered, testable:
- presentation: screens, widgets, controllers/providers
- domain: entities, value objects, use cases, repository interfaces
- data: DTOs, API clients, local persistence, repository implementations
- platform: camera, biometrics, notifications, scanner, share and deep-link adapters

Suggested project:
```
bad_gym/
  pubspec.yaml
  analysis_options.yaml
  lib/
    main.dart
    app/
      app.dart
      router.dart
      bootstrap.dart
      theme/
        colors.dart
        typography.dart
        spacing.dart
        theme.dart
    core/
      errors/
      networking/
      persistence/
      responsive/
      accessibility/
      platform/
      widgets/
      utils/
    features/
      auth/
      gym/
      member/
      attendance/
      workout/
      payment/
      body/
      overview/
      notifications/
      admin/
    shared/
      domain/
      data/
      presentation/
  assets/
    characters/
    environment/
    road/
    equipment/
    icons/
    backgrounds/
    effects/
    manifest.json
  test/
  integration_test/
  docs/
```

## Design and layout system
- Native Flutter widgets for all text, numbers, charts, labels, buttons, navigation and dynamic states.
- Independent transparent high-resolution assets for character, gym equipment, scanner, road sections, platform, trees/plants, badges and meaningful illustrations.
- SVG for icons and simple vector art. Raster art only for complex illustrations/textures.
- Maintain asset manifest with ID, filename, source, dimensions, aspect ratio, safe crop, semantic meaning, intended location, scale rules and license.
- Responsive breakpoints based on available logical width, not device brand. Support text scaling and minimum touch targets.
- Build light premium theme variants as design tokens, not hard-coded colors scattered through widgets.
- Avoid oversized shadows, expensive blur, unnecessary 3D motion, uncontrolled gradients, and decorative assets that consume useful screen space.
- Respect the compact member intelligence footprint; progressive disclosure for detail rather than hiding all other app content below a giant card.

## Data integrity
Preserve product requirements for member identity, membership, attendance day records/history, workout sessions/aggregates, payment records/receipts/methods/partial payments/refunds/discounts/outstanding/renewals/plan history, and dated body measurements.
- No fabricated live metrics.
- No inferred body-fat, skeletal-muscle or health values.
- Overview KPIs must derive from source records and avoid duplicate presentation.
- Payment status changes only after a successful persisted payment.
- No automatic deductions or Auto Renew unless explicitly supported and consented.
- Role-based access is enforced by backend authorization, not just hidden UI.
- All date/time/currency formatting is locale-aware (India defaults where configured).

## Platform parity policy
Implement and test feature parity for Android and iOS. Use Flutter plugins first; use method channels/platform implementations only when a plugin cannot satisfy a verified requirement. Keep platform-specific code thin and documented. Do not promise identical OS-native permission prompts or identical background execution behavior; respect each OS's lifecycle and policies.

## Delivery pipeline
- Android: debug/release APK and Play-ready AAB; test on emulator and physical Android device.
- iOS: simulator build and signed archive/IPA via macOS + Xcode and Apple signing credentials. A Windows/Linux laptop cannot locally produce a signed iOS release; use a macOS machine or trusted macOS CI such as Codemagic/GitHub Actions macOS runner.
- CI: format, analyze, unit tests, widget tests, golden tests, integration tests, Android build; iOS simulator build/test on macOS runner.
- Keep signing certificates, API secrets and service credentials out of Git.

## Staged implementation
0. Fresh-project bootstrap and toolchain matrix; verify Flutter, Dart, Android SDK, Xcode/CI availability.
1. Create clean Flutter app root, package identity, linting, theme tokens, router, CI and smoke test.
2. Build responsive app shell and reusable design system.
3. Produce asset pack as separate transparent components and validate manifest.
4. Implement auth, gym context and member identity.
5. Implement Attendance, Workout, Payment and Body feature modules with typed repositories.
6. Implement Overview and visual fitness journey composition.
7. Integrate verified backend, secure auth, persistence, offline/sync only as specified.
8. Implement platform-specific adapters and parity checks.
9. Run automated tests, golden comparisons and device/emulator screenshots across Android/iOS form factors.
10. Build Android artifacts and iOS artifacts in their respective supported environments; document exact commands and limitations.
11. Handoff source, assets, test evidence, setup guide and release checklist.

## Git safety
Work on branch migration/flutter-cross-platform until the clean project foundation is reviewed. The old Android implementation is not a dependency and must not be ported. Do not delete unrelated repository content without an explicit repository-level cleanup plan. If the user requests a truly empty repository root, create a dedicated new repository or a clean root tree rather than pretending a subfolder is the repository root.

## Execution ledger
- [x] Confirmed user wants a fresh project, not Kotlin migration.
- [x] Created isolated branch migration/flutter-cross-platform.
- [x] Updated this specification to make Android and iOS first-class from day one.
- [x] Scaffold clean Flutter app under `flutter_app/` on isolated branch.
- [x] Establish package identity, Material 3 light theme and first responsive app shell.
- [x] Add feature-first Attendance, Workout, Payment and Body domain/repository/presentation modules.
- [x] Add initial navigation widget tests and Android/iOS CI build jobs.
- [ ] Verify toolchain and decide whether to move the Flutter app to a dedicated clean repository root.
- [ ] Produce individual image/SVG asset packs.
- [x] Define provider-neutral auth session/repository contracts and Owner/Member presentation permission policy.
- [ ] Complete overview extraction, gym context, backend integration and production CRUD workflows.
- [ ] Run parity tests and capture screenshots on Android/iOS.
- [ ] Produce Android and iOS release artifacts in supported environments.
