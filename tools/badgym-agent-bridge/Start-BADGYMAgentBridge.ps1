param([switch]$Once)

$ErrorActionPreference = "Stop"
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$config = Get-Content (Join-Path $PSScriptRoot "config.json") -Raw | ConvertFrom-Json
$statePath = Join-Path $PSScriptRoot ".bridge-state.json"
$logPath = Join-Path $PSScriptRoot "bridge.log"
$branch = [string]$config.branch

function Log([string]$m) {
  $line = "$(Get-Date -Format o) $m"
  Add-Content -LiteralPath $logPath -Value $line
  Write-Host $line
}

function SaveState($sha,$task,$result) {
  @{
    remoteSha=$sha
    taskId=$task
    result=$result
    updatedAt=(Get-Date).ToString("o")
  } | ConvertTo-Json | Set-Content -LiteralPath $statePath -Encoding UTF8
}

function WaitForNextCycle {
  if ($Once) { return $false }
  Start-Sleep -Seconds ([int]$config.pollSeconds)
  return $true
}

Set-Location $repoRoot

$agyPath = Join-Path $env:LOCALAPPDATA "agy\bin"
if ((Test-Path $agyPath) -and ($env:PATH -notlike "*$agyPath*")) {
  $env:PATH = "$agyPath;$env:PATH"
}

Write-Host ""
Write-Host "============================================================"
Write-Host " BAD GYM LIVE AGENT BRIDGE"
Write-Host " Branch : $branch"
Write-Host " Repo   : $repoRoot"
Write-Host " Mode   : GitHub -> Pull -> READY task -> AGY -> Push"
Write-Host "============================================================"
Write-Host ""

Log "Bridge started."

while ($true) {
  try {
    $dirty = @(git status --porcelain)
    if ($dirty.Count -gt 0) {
      Log "WAIT: local checkout is dirty. No pull or agent launch will occur."
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    Log "SYNC: fetching origin/$branch ..."
    $ErrorActionPreference = "Continue"
    $fetchOut = git fetch origin $branch 2>&1
    $fetchExit = $LASTEXITCODE
    $ErrorActionPreference = "Stop"
    foreach ($line in $fetchOut) { Log "git: $line" }

    if ($fetchExit -ne 0) {
      Log "ERROR: git fetch failed with exit code $fetchExit."
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    $remoteSha = (git rev-parse "origin/$branch").Trim()
    $localSha = (git rev-parse "HEAD").Trim()

    if ($remoteSha -ne $localSha) {
      Log "SYNC: GitHub changed ($localSha -> $remoteSha). Pulling fast-forward ..."
      $ErrorActionPreference = "Continue"
      $pullOut = git pull --ff-only origin $branch 2>&1
      $pullExit = $LASTEXITCODE
      $ErrorActionPreference = "Stop"
      foreach ($line in $pullOut) { Log "git: $line" }

      if ($pullExit -ne 0) {
        Log "ERROR: fast-forward pull failed with exit code $pullExit."
        if (-not (WaitForNextCycle)) { break }
        continue
      }

      $localSha = (git rev-parse "HEAD").Trim()
      Log "SYNC: local HEAD is now $localSha."
    } else {
      Log "SYNC: GitHub and laptop are aligned at $localSha."
    }

    $taskPath = Join-Path $repoRoot "CURRENT_TASK.md"
    if (-not (Test-Path $taskPath)) {
      Log "ERROR: CURRENT_TASK.md is missing."
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    $taskText = Get-Content $taskPath -Raw
    $sm = [regex]::Match($taskText,"(?im)^STATUS:\s*([A-Z_]+)")
    $tm = [regex]::Match($taskText,"(?im)^TASK_ID:\s*(.+)$")
    $taskStatus = if ($sm.Success) { $sm.Groups[1].Value.Trim() } else { "" }
    $taskId = if ($tm.Success) { $tm.Groups[1].Value.Trim() } else { "UNKNOWN" }

    Log "TASK: $taskId / STATUS=$taskStatus"

    if ($taskStatus -ne "READY_FOR_EXECUTION") {
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    $state = if (Test-Path $statePath) { Get-Content $statePath -Raw | ConvertFrom-Json } else { $null }
    if ($state -and $state.remoteSha -eq $remoteSha -and $state.taskId -eq $taskId -and $state.result -eq "SUCCESS") {
      Log "TASK: already completed for this remote SHA/task. Waiting for the next GitHub change."
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    SaveState $remoteSha $taskId "RUNNING"

    $prompt = @"
You are the autonomous Google Antigravity executor for the BAD GYM repository.

Before touching code, read:
AGENTS.md
.agents/rules/00-badgym-github-loop.md
CURRENT_TASK.md
STATUS.md
HANDOFF_STRATEGY.md
HANDOFF_STATUS.md
docs/reference/MI_V5_DESIGN_COMMUNICATION.md

CURRENT_TASK.md is the executable authorization.
Do not invent a different task.
Do not start Stage 5 while the current task says Stage 5 is blocked.
Inspect the existing implementation and evidence before editing.

Execute the current READY_FOR_EXECUTION task completely and autonomously.
Implement the actual production code; do not merely explain it.
Run the requested Android unit tests/build/install/runtime checks and fix failures where practical.
Use the physical device when available.
Update STATUS.md with exact implementation, verification, measured results, remaining issues and next task.
Update HANDOFF_STATUS.md and append the required dated design communication entry.
Only after the acceptance gate passes, set CURRENT_TASK.md STATUS to COMPLETED.
If genuinely blocked, set STATUS/BLOCKED with the exact blocker and stop without pretending completion.
Preserve unrelated work.
Never force-push.
Commit the intended changes and push to origin member-intelligence-v3.
"@

    Log "AGY: launching task $taskId with live stream output."
    Log "AGY: model=$([string]$config.model), effort=$([string]$config.effort), timeout=$([int]$config.maxAgentMinutes)m"

    $args = @(
      "-p", $prompt,
      "--output-format", "stream-json",
      "--print-timeout", "$([int]$config.maxAgentMinutes)m",
      "--effort", [string]$config.effort
    )

    if (-not [string]::IsNullOrWhiteSpace([string]$config.model)) {
      $args += @("--model", [string]$config.model)
    }

    if ([bool]$config.skipPermissions) {
      $args += "--dangerously-skip-permissions"
      Log "AGY: autonomous permission mode enabled by config."
    }

    $ErrorActionPreference = "Continue"
    & ([string]$config.antigravityCommand) @args 2>&1 | ForEach-Object {
      $line = [string]$_
      Add-Content -LiteralPath $logPath -Value "$(Get-Date -Format o) AGY $line"
      Write-Host "AGY> $line"
    }
    $exitCode = $LASTEXITCODE
    $ErrorActionPreference = "Stop"

    Log "AGY: process exited with code $exitCode."

    if ($exitCode -eq 0) {
      SaveState $remoteSha $taskId "SUCCESS"
    } else {
      SaveState $remoteSha $taskId "AGENT_EXIT_$exitCode"
    }

    if ($Once) { break }

    Log "SYNC: next poll in $([int]$config.pollSeconds)s."
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  }
  catch {
    Log "BRIDGE EXCEPTION: $($_.Exception.Message)"
    if ($Once) { break }
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  }
}
