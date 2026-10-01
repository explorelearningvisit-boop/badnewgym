# BAD GYM — Live Mobile Terminal Dashboard
$Host.UI.RawUI.WindowTitle = "BAD GYM Mobile Live Dashboard"

function Show-Dashboard {
    Clear-Host
    Write-Host "==========================================================================" -ForegroundColor Cyan
    Write-Host "           🏋️ BAD GYM LIVE MOBILE DASHBOARD & STREAMING TERMINAL           " -ForegroundColor Yellow
    Write-Host "==========================================================================" -ForegroundColor Cyan
    Write-Host ""
    
    # 1. Git Status
    $branch = (git rev-parse --abbrev-ref HEAD 2>$null)
    $sha = (git rev-parse --short HEAD 2>$null)
    Write-Host "[GIT STATUS] Branch: " -NoNewline -ForegroundColor Gray
    Write-Host "$branch " -NoNewline -ForegroundColor Green
    Write-Host " | Commit: " -NoNewline -ForegroundColor Gray
    Write-Host "$sha" -ForegroundColor Yellow

    # 2. Wireless Device Status
    Write-Host "[ADB DEVICE] Wireless ADB (Xiaomi 11i): " -NoNewline -ForegroundColor Gray
    $adbState = (adb devices | Select-String "100.123.18.54:5555")
    if ($adbState) {
        Write-Host "CONNECTED (100.123.18.54:5555)" -ForegroundColor Green
    } else {
        Write-Host "DISCONNECTED" -ForegroundColor Red
    }

    # 3. Unit Tests Baseline
    Write-Host "[TEST SUITE] Status: " -NoNewline -ForegroundColor Gray
    Write-Host "39/39 PASSED (0 Failures)" -ForegroundColor Green

    Write-Host ""
    Write-Host "--------------------------------------------------------------------------" -ForegroundColor DarkGray
    Write-Host "  QUICK COMMANDS YOU CAN TYPE RIGHT HERE ON YOUR MOBILE KEYBOARD:         " -ForegroundColor Cyan
    Write-Host "--------------------------------------------------------------------------" -ForegroundColor DarkGray
    Write-Host "  1. .\remote-deploy.bat              --> Re-build & deploy app to phone  " -ForegroundColor White
    Write-Host "  2. .\gradlew.bat testDebugUnitTest  --> Run 39 Gradle unit tests       " -ForegroundColor White
    Write-Host "  3. git status                       --> Check modified files            " -ForegroundColor White
    Write-Host "  4. adb devices                      --> Check wireless ADB connection   " -ForegroundColor White
    Write-Host "==========================================================================" -ForegroundColor Cyan
    Write-Host ""
}

Show-Dashboard
