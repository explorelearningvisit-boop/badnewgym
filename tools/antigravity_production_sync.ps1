param(
  [string]$ExpectedHead = "0fbf60ce1970b19e7607d4323746e803dc75fcf4"
)

$ErrorActionPreference = "Stop"

Write-Host "=== BAD GYM / ANTIGRAVITY PRODUCTION SYNC ===" -ForegroundColor Cyan
Write-Host "Expected HEAD: $ExpectedHead"

git fetch origin
git checkout member-intelligence-v3
git pull --ff-only origin member-intelligence-v3

$head = (git rev-parse HEAD).Trim()
Write-Host "Pulled HEAD: $head"

if ($head -ne $ExpectedHead) {
  throw "HEAD mismatch. Expected $ExpectedHead but got $head. STOP."
}

Write-Host "=== FILE STATE ===" -ForegroundColor Cyan
git status --short
git diff --name-only

Write-Host "=== TEST ===" -ForegroundColor Cyan
./gradlew testDebugUnitTest

Write-Host "=== DEBUG BUILD ===" -ForegroundColor Cyan
./gradlew assembleDebug

if (Get-Command adb -ErrorAction SilentlyContinue) {
  Write-Host "=== ADB ===" -ForegroundColor Cyan
  adb devices
  adb install -r app/build/outputs/apk/debug/app-debug.apk
  adb shell am start -n com.example.badnewgym/.MainActivity
} else {
  Write-Warning "adb is not on PATH; device verification was not executed."
}

Write-Host "=== FINAL STATE ===" -ForegroundColor Cyan
git status --short
git diff --stat
git diff --name-only
Write-Host "FINAL HEAD: $((git rev-parse HEAD).Trim())"
Write-Host "APK: app/build/outputs/apk/debug/app-debug.apk"
Write-Host "Next: capture screenshot matrix and update docs/reference/ANTIGRAVITY_SYNC_ACK.md"
