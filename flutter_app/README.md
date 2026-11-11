# BAD GYM — Flutter app

Fresh cross-platform Flutter application foundation for Android and iOS.

## Requirements
- Flutter stable (Dart SDK constraint is in pubspec.yaml)
- Android Studio / Android SDK for Android builds
- macOS + Xcode for iOS simulator/device builds and signed release archives

## Run
```bash
cd flutter_app
flutter pub get
flutter analyze
flutter test
flutter run
```

## Current milestone
This is the first runnable UI foundation: responsive member overview, member identity/membership summary, Attendance/Workout/Payment/Body overview modules, and section detail views. All displayed values are labelled as demo/sample and are not connected to a production backend.

## Next milestones
1. Replace placeholder profile/illustration visuals with separately exported, transparent and verified assets.
2. Extract feature models and repositories from the prototype screen.
3. Connect verified authentication, gym/member APIs and authorization.
4. Implement operational flows: check-in/out, workout logging, payment collection/receipts, dated body measurements.
5. Add local persistence, sync conflict rules and push notifications where required.
6. Add widget/golden/integration tests and Android/iOS device verification.

Never commit signing keys, API secrets, real member personal data or production credentials.
