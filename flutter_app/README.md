# BAD GYM — Flutter app

Fresh cross-platform Flutter application foundation for Android and iOS. The legacy Kotlin application is not used as the Flutter UI implementation.

## Requirements
- Flutter stable with Dart SDK satisfying `pubspec.yaml`
- Android Studio + Android SDK + an Android emulator or physical device for Android
- macOS + Xcode for iOS simulator/device builds and signed release archives
- Git for branch-based development

## First local verification (Windows PowerShell)
Open PowerShell at the repository root and run:

```powershell
cd flutter_app
flutter --version
flutter doctor -v
flutter pub get
dart format lib test
flutter analyze
flutter test
flutter run
```

If Flutter is not on PATH, install Flutter stable using the official Flutter Windows installation guide, add its `bin` directory to PATH, close/reopen the terminal, and verify `flutter --version` before continuing. Android Studio should have Android SDK Platform Tools, Command-line Tools and an emulator image installed. Accept SDK licenses with `flutter doctor --android-licenses` when prompted.

For a connected Android phone, enable Developer options and USB debugging, connect it, then run `flutter devices` followed by `flutter run -d <device-id>`. For an emulator, start it from Android Studio Device Manager first.

## Verification and build commands
```powershell
flutter analyze
flutter test
flutter build apk --debug
flutter build appbundle --release
```

Run the iOS simulator build on macOS only:
```bash
flutter build ios --simulator --no-codesign
```
A signed iOS release additionally requires Xcode signing configuration and Apple Developer credentials. Do not put signing keys, API secrets or production credentials in Git.

## Current milestone
The prototype includes a responsive member overview and dedicated Attendance, Workout, Payment and Body feature screens with typed domain models and repository interfaces. Provider-neutral authentication contracts and Owner/Member presentation permission policy are in place. All visible values remain demo fixtures; no production backend or real authentication is connected.

## Send back after the first run
Please share the complete terminal output for `flutter doctor -v`, `flutter analyze` and `flutter test`, including the first error and the final exit status. If a command fails, do not try random fixes; send the output so the next change can be targeted.
