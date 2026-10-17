param(
    [switch]$SkipPull
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path -Parent $PSScriptRoot
Set-Location $repoRoot
$expectedBranch = "member-intelligence-reference-dashboard"

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " BAD GYM — ONE-COMMAND PULL + AI HANDOFF" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "Root: $repoRoot"

if (-not $SkipPull) {
    $dirty = git status --porcelain
    if ($dirty) {
        $dirty | ForEach-Object { Write-Host $_ -ForegroundColor Yellow }
        throw "Working tree has changes. Commit/stash intentionally, then rerun. No changes were discarded."
    }

    git fetch origin
    if ($LASTEXITCODE -ne 0) { throw "git fetch failed." }

    $branchNow = (git branch --show-current).Trim()
    if ($branchNow -ne $expectedBranch) {
        git switch $expectedBranch
        if ($LASTEXITCODE -ne 0) { throw "Could not switch to $expectedBranch. Check local changes." }
    }

    git pull --ff-only origin $expectedBranch
    if ($LASTEXITCODE -ne 0) { throw "Fast-forward pull failed. Resolve branch divergence manually; never force-pull." }
} else {
    Write-Host "SkipPull specified: using current local working tree." -ForegroundColor Yellow
}

$branchNow = (git branch --show-current).Trim()
if ($branchNow -ne $expectedBranch) {
    throw "Wrong branch '$branchNow'. Expected '$expectedBranch'."
}

Write-Host ""
Write-Host "Branch: $branchNow" -ForegroundColor Green
Write-Host "Commit: $((git rev-parse HEAD).Trim())"

$requiredFiles = @(
    "AGENTS.md",
    "AI_COLLABORATION_PROTOCOL.md",
    "CURRENT_TASK.md",
    "BAD_GYM_AGENT_START_HERE.md",
    "docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md",
    "docs/OPEN_CODE_PULL_AND_RUN.md",
    "docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md",
    "docs/PIXEL_PERFECT_ACCEPTANCE.md",
    "docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md",
    "docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md",
    "docs/reference/assets/README.md"
)

Write-Host ""
Write-Host "== Checking the AI handoff files ==" -ForegroundColor Cyan
$missing = @()
foreach ($relativePath in $requiredFiles) {
    if (Test-Path (Join-Path $repoRoot $relativePath)) {
        Write-Host "[OK]      $relativePath" -ForegroundColor Green
    } else {
        Write-Host "[MISSING] $relativePath" -ForegroundColor Red
        $missing += $relativePath
    }
}
if ($missing.Count -gt 0) {
    throw "Required handoff files are missing. Fix the repository before asking the agent to implement."
}

$referencePath = Join-Path $repoRoot "docs/reference/assets/member-intelligence-full-screen-reference.jpg"
if (Test-Path $referencePath) {
    Write-Host "[OK]      exact reference image found: $referencePath" -ForegroundColor Green
} else {
    Write-Warning "Exact approved reference JPEG is NOT committed/present at $referencePath. Agent may inspect code and prepare work, but must not claim pixel-diff verification or invent a substitute. Add the original asset to this path."
}

Write-Host ""
Write-Host "== Agent handoff ==" -ForegroundColor Cyan
Write-Host "The agent is instructed to read the full documentation set, inspect the real source/assets, report an inventory, and only then edit."
Write-Host "No local changes are reset or deleted by this script."

$opencode = Get-Command opencode -ErrorAction SilentlyContinue
if (-not $opencode) {
    Write-Warning "OpenCode CLI is not installed or not on PATH."
    Write-Host "The repository is pulled and checked. Open this folder in OpenCode/Antigravity and paste the prompt from PULL_AND_RUN.md."
    exit 0
}

$prompt = @'
Read PULL_AND_RUN.md and BAD_GYM_AGENT_START_HERE.md first. Then read every file in the Required AI reading order in PULL_AND_RUN.md completely, including AGENTS.md, AI_COLLABORATION_PROTOCOL.md, CURRENT_TASK.md, product context, pull/run instructions, the full hyper-detailed blueprint, acceptance checklist, V4 master spec, end-to-end contract, and asset README.

Before editing, inspect actual Kotlin/Compose source, Gradle/version-catalog files, resources, domain models, ViewModels, repositories, event/payment/entitlement logic, tests and all tracked assets. Produce a concise inventory of files read, assets present/missing, architecture found, existing versus necessary dependencies, current build/test status, and the planned change list. Do not start implementation until this inventory is complete.

Then implement the active Member Intelligence task on this branch, preserving existing V4 domain/event/entitlement behavior and unrelated projects. Use the exact reference image if present. If missing, explicitly report that blocker and do not fabricate a replacement or claim pixel-perfect verification. Avoid dark-heavy UI, tablet-sized layouts, duplicate metrics, fake production data, false dues, dead actions and false success. Run unit tests/build, launch on an authorized emulator/device if available, capture screenshots, compare against the exact reference, fix discrepancies and document evidence. Report actual results and blockers; never claim tests or visual checks passed unless they did.
'@

Write-Host "Starting OpenCode in repository root..." -ForegroundColor Cyan
& opencode run $prompt
if ($LASTEXITCODE -ne 0) {
    throw "OpenCode agent run returned exit code $LASTEXITCODE. Review the agent output."
}
