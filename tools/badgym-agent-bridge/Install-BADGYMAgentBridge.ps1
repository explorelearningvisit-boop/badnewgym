$ErrorActionPreference = "Stop"
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$bridge = Join-Path $repoRoot "tools\badgym-agent-bridge\Start-BADGYMAgentBridge.ps1"
$taskName = "BAD GYM Agent Bridge"

if (-not (Get-Command git -ErrorAction SilentlyContinue)) { throw "Git is not installed or not on PATH." }
if (-not (Get-Command agy -ErrorAction SilentlyContinue)) {
  Write-Host "Install Antigravity CLI first: irm https://antigravity.google/cli/install.ps1 | iex"
  throw "agy not found."
}

& agy -p "Reply only: BAD_GYM_BRIDGE_AUTH_OK" --output-format text --print-timeout 2m
if ($LASTEXITCODE -ne 0) { throw "Antigravity authentication failed. Run agy once interactively, authenticate, then rerun." }

$actionArgs = '-NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File "' + $bridge + '"'
$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument $actionArgs
$trigger = New-ScheduledTaskTrigger -AtLogOn -User "$env:USERDOMAIN\$env:USERNAME"
$settings = New-ScheduledTaskSettingsSet -ExecutionTimeLimit (New-TimeSpan -Days 3650) -MultipleInstances IgnoreNew -StartWhenAvailable
Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings -Description "BAD GYM GitHub to Antigravity autonomous development bridge" -Force | Out-Null
Write-Host "BAD GYM Agent Bridge installed and starting."
Start-Process powershell.exe -ArgumentList @("-NoProfile","-ExecutionPolicy","Bypass","-File",$bridge) -WindowStyle Hidden
