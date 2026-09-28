param(
  [string]$ExpectedHead = "dc02e71f1766c77bc1521a77807d393a26b2590d"
)

$ErrorActionPreference = "Stop"

Write-Host "=== BAD GYM / ANTIGRAVITY PRODUCTION SYNC ===" -ForegroundColor Cyan
Write-Host "Expected HEAD: $ExpectedHead"

git fetch origin
git checkout member-intelligence-v3
git pull --ff-only origin member-intelligence-v3

$head = (git rev-parse HEAD).Trim()
Write-Host "Pulled HEAD: $head"

if ($ExpectedHead -and $head -ne $ExpectedHead) {
  Write-Warning "Current HEAD $head differs from ExpectedHead $ExpectedHead."
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
  $deviceLines = & adb devices | Select-String "\sdevice$"
  if ($deviceLines) {
    $targetDevice = (($deviceLines[0].Line.Trim()) -split '\s+')[0]
    Write-Host "Target Device: $targetDevice"
    adb -s $targetDevice install -r app/build/outputs/apk/debug/app-debug.apk
    adb -s $targetDevice shell am start -n com.example.badnewgym/.MainActivity
  } else {
    Write-Warning "No authorized device connected."
  }
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
