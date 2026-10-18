$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
Set-Location $repoRoot
$expectedBranch = "member-intelligence-reference-dashboard"

Write-Host "== BAD GYM Member Intelligence: Pull and Run ==" -ForegroundColor Cyan
Write-Host "Repository root: $repoRoot"

$branchNow = (git branch --show-current).Trim()
if ($branchNow -ne $expectedBranch) {
    Write-Host "Switching to $expectedBranch ..."
    git switch $expectedBranch
    if ($LASTEXITCODE -ne 0) { throw "Could not switch branch. Check local changes first." }
}

$dirty = git status --porcelain
if ($dirty) {
    Write-Host $dirty
    throw "Working tree has local changes. Commit or stash them before pull; this script will not discard work."
}

git fetch origin
if ($LASTEXITCODE -ne 0) { throw "git fetch failed." }

git pull --ff-only origin $expectedBranch
if ($LASTEXITCODE -ne 0) { throw "git pull failed. Resolve branch divergence manually; do not force-pull." }

Write-Host "== Current branch / commit ==" -ForegroundColor Cyan
git branch --show-current
git rev-parse HEAD

Write-Host "== Build + unit tests ==" -ForegroundColor Cyan
& .\gradlew.bat clean testDebugUnitTest assembleDebug
if ($LASTEXITCODE -ne 0) { throw "Gradle build/tests failed. Read the first compiler/test error and fix it on this feature branch." }

$apk = Join-Path $repoRoot "app\build\outputs\apk\debug\app-debug.apk"
if (-not (Test-Path $apk)) { throw "Build succeeded but APK was not found at $apk" }

Write-Host "APK created: $apk" -ForegroundColor Green
Write-Host "== Android device check ==" -ForegroundColor Cyan
$adbCommand = Get-Command adb -ErrorAction SilentlyContinue
if (-not $adbCommand) {
    Write-Warning "adb is not on PATH. Open Android Studio to run on an emulator/device, or add Android SDK platform-tools to PATH."
    exit 0
}

$devices = adb devices
$devices | ForEach-Object { Write-Host $_ }
$authorized = $devices | Where-Object { $_ -match "^\S+\s+device$" }
if (-not $authorized) {
    Write-Warning "No authorized Android device/emulator found. Build passed, but install/device verification is BLOCKED."
    exit 0
}

adb install -r $apk
if ($LASTEXITCODE -ne 0) { throw "APK install failed." }

adb shell monkey -p com.example.badnewgym 1
if ($LASTEXITCODE -ne 0) {
    Write-Warning "APK installed, but automatic launch failed. Open BAD GYM manually from the device launcher."
    exit 0
}

Write-Host "APK installed and launch request sent. Capture screenshots and complete docs/PIXEL_PERFECT_ACCEPTANCE.md before calling this verified." -ForegroundColor Green
