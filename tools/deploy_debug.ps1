$ErrorActionPreference = "Stop"
$package = "com.example.badnewgym"
$apk = Join-Path $PSScriptRoot "..\app\build\outputs\apk\debug\app-debug.apk"
$action = "com.example.badnewgym.DEBUG_DEPLOYMENT_PROGRESS"
$started = Get-Date

function Send-Progress {
    param([string]$Phase,[string]$Title,[string]$Detail,[int]$Percent,[long]$Bytes=-1,[long]$Total=-1,[string]$Sha="",[switch]$ErrorState)
    $args = @("shell","am","broadcast","-a",$action,"--es","phase",$Phase,"--es","title",$Title,"--es","detail",$Detail,"--ei","percent",$Percent)
    if ($Bytes -ge 0) { $args += @("--el","bytes",$Bytes) }
    if ($Total -ge 0) { $args += @("--el","total_bytes",$Total) }
    if ($Sha) { $args += @("--es","sha",$Sha) }
    if ($ErrorState) { $args += @("--ez","error","true") }
    & adb @args | Out-Null
}

function ElapsedText {
    $span = (Get-Date) - $started
    return ("Elapsed {0:mm\:ss}" -f $span)
}

& adb devices
if ($LASTEXITCODE -ne 0) { throw "adb is not available" }
$deviceLines = & adb devices | Select-String "\sdevice$"
if (-not $deviceLines) { throw "No ADB device connected" }

Send-Progress "CONNECT" "BAD GYM update" ("Device connected • " + (ElapsedText)) 5

$sha = (& git rev-parse --short=7 HEAD).Trim()
Send-Progress "BUILD" "Building BAD GYM" ("Gradle assembleDebug started • " + (ElapsedText)) 10 -Sha $sha

& .\gradlew.bat assembleDebug --console=plain
if ($LASTEXITCODE -ne 0) {
    Send-Progress "ERROR" "Build failed" ("Gradle assembleDebug failed • " + (ElapsedText)) 0 -Sha $sha -ErrorState
    throw "Gradle build failed"
}
Send-Progress "BUILD" "Build complete" ("APK generated • " + (ElapsedText)) 70 -Sha $sha

if (-not (Test-Path $apk)) { throw "APK not found: $apk" }
$apkInfo = Get-Item $apk
$total = [long]$apkInfo.Length
Send-Progress "TRANSFER" "APK ready" (("APK size " + [math]::Round($total / 1MB, 1) + " MB • transfer/install starting • " + (ElapsedText))) 82 0 $total $sha

Send-Progress "TRANSFER" "Sending APK to device" (("0 / " + [math]::Round($total / 1MB, 1) + " MB • " + (ElapsedText))) 84 0 $total $sha
& adb -d push $apk /data/local/tmp/badnewgym-debug.apk | Out-Null
if ($LASTEXITCODE -ne 0) {
    Send-Progress "ERROR" "APK transfer failed" ("adb push failed • " + (ElapsedText)) 84 -Sha $sha -ErrorState
    throw "APK transfer failed"
}
Send-Progress "TRANSFER" "APK transferred" (([math]::Round($total / 1MB, 1).ToString() + " MB / " + [math]::Round($total / 1MB, 1) + " MB • " + (ElapsedText))) 90 $total $total $sha

Send-Progress "INSTALL" "Installing update" ("Replacing BAD GYM package • " + (ElapsedText)) 94 $total $total $sha
& adb -d shell pm install -r /data/local/tmp/badnewgym-debug.apk | Out-Null
if ($LASTEXITCODE -ne 0) {
    Send-Progress "ERROR" "Install failed" ("Package install failed • " + (ElapsedText)) 94 -Sha $sha -ErrorState
    throw "Package install failed"
}

Send-Progress "LAUNCH" "Launching BAD GYM" ("Starting fresh APK • " + (ElapsedText)) 97 $total $total $sha
& adb -d shell am force-stop $package
& adb -d shell monkey -p $package 1 | Out-Null
Start-Sleep -Milliseconds 1200
Send-Progress "VERIFY" "Verifying runtime" ("App launched • waiting for UI • " + (ElapsedText)) 99 $total $total $sha
Start-Sleep -Milliseconds 500
Send-Progress "COMPLETE" "BAD GYM is ready" (("Update complete • " + (ElapsedText))) 100 $total $total $sha

Write-Host "BAD GYM deploy complete: $sha" -ForegroundColor Green