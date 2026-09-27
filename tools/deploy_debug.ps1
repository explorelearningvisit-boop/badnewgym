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
Send-Progress "TRANSFER" "APK ready" (("APK size " + [math]::Round($total / 1MB, 1) + " MB • measured transfer begins • " + (ElapsedText))) 70 0 $total $sha
$createOutput = & adb -d shell pm install-create -r -S $total 2>&1
if ($LASTEXITCODE -ne 0) { Send-Progress "ERROR" "Install session creation failed" ("PackageInstaller session could not be created • " + (ElapsedText)) 70 0 $total $sha -ErrorState; throw "Install session creation failed" }
$createText = ($createOutput -join " ")
if ($createText -notmatch 'Success: created install session \[(\d+)\]') { Send-Progress "ERROR" "Install session creation failed" ("Unexpected PackageInstaller response • " + (ElapsedText)) 70 0 $total $sha -ErrorState; throw "Unable to parse install session id" }
$sessionId = $Matches[1]
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "adb"
$psi.Arguments = "-d shell pm install-write -S $total $sessionId base.apk -"
$psi.UseShellExecute = $false
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.CreateNoWindow = $true
$proc = New-Object System.Diagnostics.Process
$proc.StartInfo = $psi
[void]$proc.Start()
$reader = [System.IO.File]::OpenRead((Resolve-Path $apk))
$buffer = New-Object byte[] (1MB)
$sent = 0L
try {
    Send-Progress "TRANSFER" "Streaming APK to device" ("0 / " + [math]::Round($total / 1MB, 1) + " MB • 0% • " + (ElapsedText)) 70 0 $total $sha
    while ($sent -lt $total) {
        $remaining = $total - $sent
        $want = [int][math]::Min($buffer.Length, $remaining)
        $read = $reader.Read($buffer, 0, $want)
        if ($read -le 0) { throw "Unexpected end of APK stream" }
        $proc.StandardInput.BaseStream.Write($buffer, 0, $read)
        $proc.StandardInput.BaseStream.Flush()
        $sent += $read
        $transferPercent = [int][math]::Floor(70 + (($sent / $total) * 20))
        Send-Progress "TRANSFER" "Streaming APK to device" (([math]::Round($sent / 1MB, 1)) + " MB / " + [math]::Round($total / 1MB, 1) + " MB • " + $transferPercent + "% • " + (ElapsedText)) $transferPercent $sent $total $sha
    }
    $proc.StandardInput.Close()
    $proc.WaitForExit()
    if ($proc.ExitCode -ne 0) { throw "PackageInstaller stream failed: " + $proc.StandardError.ReadToEnd() }
}
catch {
    if (-not $proc.HasExited) { $proc.Kill() }
    Send-Progress "ERROR" "APK transfer failed" ($_.Exception.Message + " • " + (ElapsedText)) 70 $sent $total $sha -ErrorState
    throw
}
finally {
    $reader.Dispose()
    if (-not $proc.HasExited) { $proc.WaitForExit() }
}
Send-Progress "VERIFY" "APK bytes received" (($sent / 1MB).ToString("0.0") + " MB / " + [math]::Round($total / 1MB, 1) + " MB • exact byte count reached • " + (ElapsedText)) 91 $sent $total $sha
$remoteInstallResult = & adb -d shell pm install-commit $sessionId 2>&1
if ($LASTEXITCODE -ne 0) { Send-Progress "ERROR" "Install commit failed" (($remoteInstallResult -join " ") + " • " + (ElapsedText)) 94 $sent $total $sha -ErrorState; throw "Install commit failed" }
Send-Progress "LAUNCH" "Launching BAD GYM" ("Starting fresh APK • " + (ElapsedText)) 97 $total $total $sha
& adb -d shell am force-stop $package
& adb -d shell monkey -p $package 1 | Out-Null
Start-Sleep -Milliseconds 1200
Send-Progress "VERIFY" "Verifying runtime" ("App launched • waiting for UI • " + (ElapsedText)) 99 $total $total $sha
Start-Sleep -Milliseconds 500
Send-Progress "COMPLETE" "BAD GYM is ready" (("Update complete • " + (ElapsedText))) 100 $total $total $sha

Write-Host "BAD GYM deploy complete: $sha" -ForegroundColor Green