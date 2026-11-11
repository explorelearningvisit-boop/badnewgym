# BAD GYM Flutter Cross-Platform Migration

## Goal
Build BAD GYM Member Intelligence for Android and iOS from one Flutter/Dart codebase, with adaptive layouts for tablets and foldables. The supplied 3D road/fitness-world image is the visual target. Keep text, values, dates, charts and statuses native/dynamic; never bake data into raster images.

## Repository safety
- Base branch: main
- Isolated branch: migration/flutter-cross-platform
- Preserve the existing Kotlin app, phone-tracking backend and OPPO tools.
- Make small, reviewable, buildable changes. Never report tests, builds, installs or screenshots as passed unless actually run.

## Proposed stack
Flutter stable + Dart stable; Material 3 with custom design tokens; Riverpod; go_router; freezed/json_serializable; fl_chart; flutter_svg; cached_network_image only where remote images are needed; flutter_test, integration_test and golden tests. Pin compatible stable versions after inspecting the installed SDK. Avoid unnecessary dependencies.

## App structure
```
flutter_app/
  pubspec.yaml
  lib/
    main.dart
    app/{app.dart,router.dart,theme/}
    core/{accessibility/,responsive/,widgets/,formatting/}
    features/{member,attendance,workout,payment,body,overview}/
    shared/{models/,widgets/}
  assets/{characters/,environment/,equipment/,icons/,backgrounds/,effects/}
  test/
  integration_test/
  docs/
```

## Visual composition
1. Header/actions.
2. Member identity and membership summary.
3. Modular 3D road/platform environment.
4. Attendance, Workout, Payment and Body intelligence modules.
5. Data-grounded AI insight/action strip.
6. Bottom navigation.

Use phone portrait, phone landscape, foldable and tablet adaptive layouts. Respect iOS safe areas, Android insets, text scaling, accessibility and touch targets. Do not stretch a phone screenshot to tablet dimensions.

## Data preservation
Preserve member identity, membership period/status, attendance day records and history, workout sessions and aggregates, payment records/receipts/methods/partial payments/refunds/discounts/outstanding/renewals/plan history, and dated body measurements. Do not invent metrics or infer unmeasured body composition. Overview values must derive from source records. Never show Paid before a payment is persisted. Do not use automatic deductions/Auto Renew unless a real backend feature and explicit consent support it.

## Asset rules
Export characters, machines, scanner, road pieces, platforms and environment as independent transparent PNG/WebP files; icons/simple diagrams as SVG. Keep all text and values native Flutter. Maintain an asset manifest with file, dimensions, purpose, safe crop and semantic meaning. Never use a composite dashboard image as the production UI.

## Stages
0. Audit repository, contracts, domain models, API/auth, CI and build environment.
1. Scaffold Flutter, dependencies, theme tokens, router, linting and launch smoke test.
2. Produce/optimize transparent assets and SVG icon library; validate manifest.
3. Implement shared shell, safe areas, profile, membership, navigation and states.
4. Implement Attendance, Workout, Payment and Body with typed models/repositories.
5. Assemble Overview and meaningful visual journey.
6. Integrate audited backend and offline/sync behavior only where required.
7. Run format, analyzer, unit/widget/golden/integration tests; test 360dp, iPhone logical sizes, large text, landscape, tablets and foldables.
8. Build Android APK/AAB where Android SDK exists. Build iOS only on macOS with Xcode and signing. Capture real emulator/device screenshots, compare, fix, repeat.
9. Handoff exact setup/build/test commands, SDK versions, environment requirements, limitations and artifacts. Keep work on migration branch until reviewed.

## Execution ledger
- [x] Located repository and read README.
- [x] Read existing Member Intelligence V2 contract.
- [x] Created isolated branch migration/flutter-cross-platform from main.
- [ ] Inspect full repository tree and domain/backend contracts.
- [ ] Confirm Flutter/Android/iOS environment.
- [ ] Scaffold Flutter app.
- [ ] Produce individual assets.
- [ ] Implement screens and data integration.
- [ ] Run tests and screenshot verification.
- [ ] Build and hand off artifacts.
