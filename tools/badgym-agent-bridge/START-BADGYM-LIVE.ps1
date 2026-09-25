$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
Set-Location $repoRoot

Write-Host ""
Write-Host "BAD GYM LIVE START"
Write-Host "Repository: $repoRoot"
Write-Host "Branch: member-intelligence-v3"
Write-Host ""

$branch = (git branch --show-current).Trim()
if ($branch -ne "member-intelligence-v3") {
  Write-Host "Switching to member-intelligence-v3 ..."
  git switch member-intelligence-v3
}

Write-Host "Fetching latest GitHub state ..."
git fetch origin member-intelligence-v3

if ((git status --porcelain).Count -eq 0) {
  git pull --ff-only origin member-intelligence-v3
} else {
  Write-Host "Local working tree is dirty. The main bridge will wait rather than overwrite it."
}

Write-Host ""
Write-Host "Starting visible BAD GYM Agent Bridge..."
Write-Host ""

& powershell.exe -ExecutionPolicy Bypass -NoProfile -NoExit -File (Join-Path $PSScriptRoot "Start-BADGYMAgentBridge.ps1")
