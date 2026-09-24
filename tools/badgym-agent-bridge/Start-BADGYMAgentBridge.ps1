param([switch]$Once)
$ErrorActionPreference = "Stop"
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$config = Get-Content (Join-Path $PSScriptRoot "config.json") -Raw | ConvertFrom-Json
$statePath = Join-Path $PSScriptRoot ".bridge-state.json"
$logPath = Join-Path $PSScriptRoot "bridge.log"
$branch = [string]$config.branch
function Log([string]$m) { Add-Content -LiteralPath $logPath -Value "$(Get-Date -Format o) $m" }
function SaveState($sha,$task,$result) { @{remoteSha=$sha;taskId=$task;result=$result;updatedAt=(Get-Date).ToString("o")} | ConvertTo-Json | Set-Content -LiteralPath $statePath -Encoding UTF8 }
Set-Location $repoRoot
$agyPath = Join-Path $env:LOCALAPPDATA "agy\bin"
if ((Test-Path $agyPath) -and ($env:PATH -notlike "*$agyPath*")) {
  $env:PATH = "$agyPath;$env:PATH"
}
Log "Bridge started: $repoRoot / $branch"
while ($true) {
  try {
    $dirty = @(git status --porcelain)
    if ($dirty.Count -gt 0) { Log "Dirty checkout; waiting."; if ($Once) { break }; Start-Sleep -Seconds ([int]$config.pollSeconds); continue }
    
    $ErrorActionPreference = "Continue"
    $fetchOut = git fetch origin $branch 2>&1
    $ErrorActionPreference = "Stop"
    foreach ($line in $fetchOut) { Log "$line" }
    if ($LASTEXITCODE -ne 0) { if ($Once) { break }; Start-Sleep -Seconds ([int]$config.pollSeconds); continue }
    
    $remoteSha = (git rev-parse "origin/$branch").Trim()
    $localSha = (git rev-parse "HEAD").Trim()
    if ($remoteSha -ne $localSha) {
      $ErrorActionPreference = "Continue"
      $pullOut = git pull --ff-only origin $branch 2>&1
      $ErrorActionPreference = "Stop"
      foreach ($line in $pullOut) { Log "$line" }
      if ($LASTEXITCODE -ne 0) { Log "Fast-forward pull failed."; if ($Once) { break }; Start-Sleep -Seconds ([int]$config.pollSeconds); continue }
    }
    $taskText = Get-Content (Join-Path $repoRoot "CURRENT_TASK.md") -Raw
    $sm = [regex]::Match($taskText,"(?im)^STATUS:\s*([A-Z_]+)")
    $tm = [regex]::Match($taskText,"(?im)^TASK_ID:\s*(.+)$")
    $taskStatus = if ($sm.Success) {$sm.Groups[1].Value.Trim()} else {""}
    $taskId = if ($tm.Success) {$tm.Groups[1].Value.Trim()} else {"UNKNOWN"}
    if ($taskStatus -ne "READY_FOR_EXECUTION") { if ($Once) { break }; Start-Sleep -Seconds ([int]$config.pollSeconds); continue }
    $state = if (Test-Path $statePath) { Get-Content $statePath -Raw | ConvertFrom-Json } else {$null}
    if ($state -and $state.remoteSha -eq $remoteSha -and $state.taskId -eq $taskId) { if ($Once) { break }; Start-Sleep -Seconds ([int]$config.pollSeconds); continue }
    SaveState $remoteSha $taskId "RUNNING"
    $prompt = @"
Read AGENTS.md, .agents/rules/00-badgym-github-loop.md, CURRENT_TASK.md, STATUS.md, HANDOFF_STRATEGY.md, HANDOFF_STATUS.md, and docs/reference/MI_V5_DESIGN_COMMUNICATION.md before implementation. Treat CURRENT_TASK.md as the executable authorization, HANDOFF_STRATEGY.md as execution strategy, HANDOFF_STATUS.md as the current handoff state, and the design communication log as the visual/product source of truth.
Execute the current READY_FOR_EXECUTION BAD GYM task immediately and autonomously. Inspect architecture and existing assets first.
Run Android builds/tests and fix failures where practical. Do not wait for GitHub Actions.
Preserve unrelated work. Never invent data.
Update STATUS.md with exact changes, verification, remaining issues and next task.
On verified completion, set CURRENT_TASK.md STATUS to COMPLETED. If genuinely blocked, set BLOCKED and document the blocker.
Commit intended changes and push to origin member-intelligence-v3.
Never force-push or erase local work. Do the implementation, not merely an explanation.
"@
    Log "Launching Antigravity for $taskId"
    $args = @("-p",$prompt,"--output-format","json","--print-timeout","$([int]$config.maxAgentMinutes)m","--effort",[string]$config.effort,"--dangerously-skip-permissions")
    if (-not [string]::IsNullOrWhiteSpace([string]$config.model)) { $args += @("--model",[string]$config.model) }
    $ErrorActionPreference = "Continue"
    $agyOut = & ([string]$config.antigravityCommand) @args 2>&1
    $ErrorActionPreference = "Stop"
    foreach ($line in $agyOut) { Log "$line" }
    $exitCode = $LASTEXITCODE
    SaveState $remoteSha $taskId "AGENT_EXIT_$exitCode"
    Log "Antigravity exited $exitCode."
    if ($Once) { break }
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  } catch {
    Log "Bridge exception: $($_.Exception.Message)"
    if ($Once) { break }
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  }
}
