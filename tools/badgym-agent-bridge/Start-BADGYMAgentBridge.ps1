param([switch]$Once)

$ErrorActionPreference = "Stop"
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$config = Get-Content (Join-Path $PSScriptRoot "config.json") -Raw | ConvertFrom-Json
$statePath = Join-Path $PSScriptRoot "agent-state.json"
$eventsPath = Join-Path $PSScriptRoot "agent-events.jsonl"
$logPath = Join-Path $PSScriptRoot "bridge.log"
$branch = [string]$config.branch

function Log([string]$m) {
  $line = "$(Get-Date -Format o) $m"
  Add-Content -LiteralPath $logPath -Value $line
  Write-Host $line
}

function Write-Event([string]$status, [string]$phase, [string]$msg) {
  $evt = @{
    timestamp = (Get-Date).ToString("o")
    status = $status
    task = $global:currentTaskId
    model = $config.model
    effort = $config.effort
    phase = $phase
    message = $msg
    pid = $global:agyPid
    heartbeat = $true
  }
  $evtJson = $evt | ConvertTo-Json -Compress
  
  try {
    Add-Content -LiteralPath $eventsPath -Value $evtJson -ErrorAction Stop
  } catch {
    Start-Sleep -Milliseconds 100
    Add-Content -LiteralPath $eventsPath -Value $evtJson -ErrorAction SilentlyContinue
  }
  
  # Console friendly output
  $time = (Get-Date).ToString("HH:mm:ss")
  Write-Host "[$time] $status | $phase | $msg"
}

function Save-AgentState([string]$status, [string]$phase, [string]$msg) {
  $elapsed = 0
  if ($global:taskStartTime) {
    $elapsed = [math]::Round(((Get-Date) - $global:taskStartTime).TotalSeconds)
  }
  
  $state = @{
    status = $status
    task = $global:currentTaskId
    branch = $branch
    model = $config.model
    effort = $config.effort
    pid = $global:agyPid
    startTime = if ($global:taskStartTime) { $global:taskStartTime.ToString("o") } else { $null }
    elapsedSeconds = $elapsed
    phase = $phase
    message = $msg
    diffStat = $global:currentDiff
    lastEventTime = (Get-Date).ToString("o")
  }
  
  try {
    $state | ConvertTo-Json | Set-Content -LiteralPath "$statePath.tmp" -Encoding UTF8 -ErrorAction Stop
    Move-Item -Path "$statePath.tmp" -Destination $statePath -Force -ErrorAction Stop
  } catch {
    Start-Sleep -Milliseconds 100
    $state | ConvertTo-Json | Set-Content -LiteralPath $statePath -Encoding UTF8 -ErrorAction SilentlyContinue
  }
  
  Write-Event $status $phase $msg
}

function WaitForNextCycle {
  if ($Once) { return $false }
  Start-Sleep -Seconds ([int]$config.pollSeconds)
  return $true
}

Set-Location $repoRoot

# Autonomous AGY execution is permanently disabled. Use the Antigravity GUI directly.
Write-Host "BAD GYM autonomous AGY bridge is DISABLED. Use Google Antigravity GUI directly."
exit 0

$agyPath = Join-Path $env:LOCALAPPDATA "agy\bin"
if ((Test-Path $agyPath) -and ($env:PATH -notlike "*$agyPath*")) {
  $env:PATH = "$agyPath;$env:PATH"
}

Write-Host ""
Write-Host "============================================================"
Write-Host " BAD GYM LIVE AGENT BRIDGE & SUPERVISOR"
Write-Host " Branch : $branch"
Write-Host " Repo   : $repoRoot"
Write-Host " Dashboard : Run 'python -m http.server 8080' in tools/badgym-agent-bridge"
Write-Host "============================================================"
Write-Host ""

Log "Bridge & Supervisor started."
Save-AgentState "WAITING" "INIT" "Starting supervisor"

$global:currentTaskId = "UNKNOWN"
$global:agyPid = $null
$global:taskStartTime = $null
$global:currentDiff = ""
$global:lastDiffTime = (Get-Date).AddDays(-1)

while ($true) {
  try {
    $dirty = @(git status --porcelain)
    if ($dirty.Count -gt 0) {
      Save-AgentState "BLOCKED" "PRE-FLIGHT" "Local checkout is dirty"
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    Save-AgentState "SYNCING" "GIT" "Fetching origin/$branch"
    $ErrorActionPreference = "Continue"
    $fetchOut = git fetch origin $branch 2>&1
    $fetchExit = $LASTEXITCODE
    $ErrorActionPreference = "Stop"

    if ($fetchExit -ne 0) {
      Save-AgentState "NETWORK ERROR" "GIT" "Fetch failed: exit code $fetchExit"
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    $remoteSha = (git rev-parse "origin/$branch").Trim()
    $localSha = (git rev-parse "HEAD").Trim()

    if ($remoteSha -ne $localSha) {
      Save-AgentState "SYNCING" "GIT" "Pulling fast-forward"
      $ErrorActionPreference = "Continue"
      $pullOut = git pull --ff-only origin $branch 2>&1
      $pullExit = $LASTEXITCODE
      $ErrorActionPreference = "Stop"

      if ($pullExit -ne 0) {
        Save-AgentState "NETWORK ERROR" "GIT" "Fast-forward failed: exit code $pullExit"
        if (-not (WaitForNextCycle)) { break }
        continue
      }
      $localSha = (git rev-parse "HEAD").Trim()
    }

    $taskPath = Join-Path $repoRoot "CURRENT_TASK.md"
    if (-not (Test-Path $taskPath)) {
      Save-AgentState "BLOCKED" "PRE-FLIGHT" "CURRENT_TASK.md missing"
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    $taskText = Get-Content $taskPath -Raw
    $sm = [regex]::Match($taskText,"(?im)^STATUS:\s*([A-Z_]+)")
    $tm = [regex]::Match($taskText,"(?im)^TASK_ID:\s*(.+)$")
    $taskStatus = if ($sm.Success) { $sm.Groups[1].Value.Trim() } else { "" }
    $global:currentTaskId = if ($tm.Success) { $tm.Groups[1].Value.Trim() } else { "UNKNOWN" }

    if ($taskStatus -ne "READY_FOR_EXECUTION") {
      Save-AgentState "WAITING" "IDLE" "Task status is $taskStatus"
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    # Check if we already finished this SHA
    $bridgeState = Join-Path $PSScriptRoot ".bridge-state.json"
    $state = if (Test-Path $bridgeState) { Get-Content $bridgeState -Raw | ConvertFrom-Json } else { $null }
    if ($state -and $state.remoteSha -eq $remoteSha -and $state.taskId -eq $global:currentTaskId -and $state.result -eq "SUCCESS") {
      Save-AgentState "WAITING" "IDLE" "Task already completed for this SHA"
      if (-not (WaitForNextCycle)) { break }
      continue
    }

    # Mark as running
    @{
      remoteSha=$remoteSha
      taskId=$global:currentTaskId
      result="RUNNING"
      updatedAt=(Get-Date).ToString("o")
    } | ConvertTo-Json | Set-Content -LiteralPath $bridgeState -Encoding UTF8

    $global:taskStartTime = Get-Date

    $prompt = "You are the autonomous Google Antigravity executor for the BAD GYM repository. Before touching code, read: AGENTS.md, .agents/rules/00-badgym-github-loop.md, CURRENT_TASK.md, STATUS.md, HANDOFF_STRATEGY.md, HANDOFF_STATUS.md, docs/reference/MI_V5_DESIGN_COMMUNICATION.md. CURRENT_TASK.md is the executable authorization. Execute the current READY_FOR_EXECUTION task completely and autonomously. Run the requested Android unit tests/build/install/runtime checks. Update STATUS.md with exact implementation, verification. Update HANDOFF_STATUS.md. Only after the acceptance gate passes, set CURRENT_TASK.md STATUS to COMPLETED. If genuinely blocked, set STATUS/BLOCKED. Commit the intended changes and push to origin member-intelligence-v3."

    $args = @(
      "-p", "`"$prompt`"",
      "--output-format", "stream-json",
      "--print-timeout", "$([int]$config.maxAgentMinutes)m",
      "--effort", [string]$config.effort
    )
    if (-not [string]::IsNullOrWhiteSpace([string]$config.model)) {
      $args += @("--model", [string]$config.model)
    }
    if ([bool]$config.skipPermissions) {
      $args += "--dangerously-skip-permissions"
    }

    Save-AgentState "RUNNING" "STARTING" "Launching AGY process"

    $ErrorActionPreference = "Continue"
    
    # Launch AGY and capture stream-json
    $processInfo = New-Object System.Diagnostics.ProcessStartInfo
    $processInfo.FileName = [string]$config.antigravityCommand
    $processInfo.Arguments = $args -join " "
    $processInfo.RedirectStandardOutput = $true
    $processInfo.RedirectStandardError = $true
    $processInfo.UseShellExecute = $false
    $processInfo.CreateNoWindow = $true
    $processInfo.WorkingDirectory = $repoRoot
    
    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $processInfo
    $process.Start() | Out-Null
    
    $global:agyPid = $process.Id
    Save-AgentState "RUNNING" "EXECUTION" "AGY process started (PID: $($process.Id))"

    $lastHeartbeat = Get-Date
    
    while (-not $process.HasExited) {
      if (((Get-Date) - $global:lastDiffTime).TotalSeconds -gt 5) {
        $ErrorActionPreference = "Continue"
        $global:currentDiff = (git diff --stat 2>&1) -join "`n"
        $ErrorActionPreference = "Stop"
        $global:lastDiffTime = Get-Date
      }

      if ($process.StandardOutput.EndOfStream -and $process.StandardError.EndOfStream) {
        $staleTime = (Get-Date) - $lastHeartbeat
        if ($staleTime.TotalMinutes -gt 5) {
          Save-AgentState "POSSIBLY STUCK" "EXECUTION" "No output for over 5 minutes"
        }
        Start-Sleep -Milliseconds 500
        continue
      }
      
      $lastHeartbeat = Get-Date
      if (-not $process.StandardOutput.EndOfStream) {
        $line = $process.StandardOutput.ReadLine()
        # Basic parsing for phase updates
        $phase = "EXECUTION"
        if ($line -match "testDebugUnitTest") { $phase = "ANDROID_TEST" }
        elseif ($line -match "assembleDebug") { $phase = "BUILD" }
        elseif ($line -match "adb install") { $phase = "DEVICE_QA" }
        elseif ($line -match "git commit") { $phase = "COMMIT" }
        elseif ($line -match "git push") { $phase = "PUSH" }
        
        Save-AgentState "RUNNING" $phase "AGY> $($line.Substring(0, [math]::Min($line.Length, 150)))"
        Add-Content -LiteralPath $logPath -Value "$(Get-Date -Format o) AGY_OUT $line"
      }
      
      if (-not $process.StandardError.EndOfStream) {
        $line = $process.StandardError.ReadLine()
        Save-AgentState "RUNNING" "EXECUTION" "AGY_ERR> $($line.Substring(0, [math]::Min($line.Length, 150)))"
        Add-Content -LiteralPath $logPath -Value "$(Get-Date -Format o) AGY_ERR $line"
      }
    }
    
    $exitCode = $process.ExitCode
    $ErrorActionPreference = "Stop"

    if ($exitCode -eq 0) {
      Save-AgentState "COMPLETED" "FINISH" "Task successfully completed."
      @{
        remoteSha=$remoteSha
        taskId=$global:currentTaskId
        result="SUCCESS"
        updatedAt=(Get-Date).ToString("o")
      } | ConvertTo-Json | Set-Content -LiteralPath $bridgeState -Encoding UTF8
    } else {
      Save-AgentState "AGY EXITED" "CRASH" "AGY exited with code $exitCode"
      @{
        remoteSha=$remoteSha
        taskId=$global:currentTaskId
        result="AGENT_EXIT_$exitCode"
        updatedAt=(Get-Date).ToString("o")
      } | ConvertTo-Json | Set-Content -LiteralPath $bridgeState -Encoding UTF8
    }

    $global:agyPid = $null
    $global:taskStartTime = $null

    if ($Once) { break }

    Save-AgentState "WAITING" "IDLE" "Waiting for next cycle"
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  }
  catch {
    Save-AgentState "BLOCKED" "EXCEPTION" "Supervisor error: $($_.Exception.Message)"
    if ($Once) { break }
    Start-Sleep -Seconds ([int]$config.pollSeconds)
  }
}
