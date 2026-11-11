# ACTIVE RUN OVERRIDE — LAVENDER FULL-SCREEN MEMBER INTELLIGENCE

Use the latest task-specific contract:
- docs/audit/REPOSITORY_AUDIT_2026-11-11.md
- docs/reference/MEMBER_INTELLIGENCE_LAVENDER_REFERENCE_CONTRACT.md
- docs/implementation/MEMBER_INTELLIGENCE_LAVENDER_IMPLEMENTATION_PLAN.md
- docs/implementation/DEEPSEEK_NOMOTRON_ANTIGRAVITY_PROMPTS.md
- docs/implementation/AGENT_HANDOFF.md
- docs/IMPLEMENTATION_PROGRESS.md

The new user reference supersedes the older compact 300×550dp / emerald-mint-only visual target for this task. Implement five full-screen native Compose destinations: Overview, Attendance, Workout, Payment and Body. Preserve old V2 domain, data-integrity, payment, privacy and entitlement rules.

Start by checking git status and the exact baseline. The intended starting baseline is branch backup/local-main-2026-11-11 at 32fccc7a306d93f4c0216156e81e76b4d70f0f48; the task branch is feature/member-intelligence-lavender-rebuild. Never overwrite uncommitted local work.

Follow the repository-specific plan and agent ownership prompts. Do not claim build/device verification unless it was actually run and recorded.

---

# BAD GYM — Antigravity Pull-and-Run Handoff

STOP USING THE OLD MEMBER INTELLIGENCE PRESENTATION.

## FIRST
git checkout main
git pull --ff-only origin main
git status

Read:
AGENTS.md
AI_COLLABORATION_PROTOCOL.md
CURRENT_TASK.md
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md
docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md
docs/reference/BAD_GYM_MOBILE_PIXEL_PERFECT_GUIDE.md
then inspect the complete active production source under app/src/main/.

## NEW TARGET
Member Intelligence V2.
Compact target: 300dp × 550dp.
Modest height expansion is allowed when real content requires it.
Global screenshot reference: 691×1536 / 20:9.

## REQUIRED PRODUCT BEHAVIOR
Current event first.
Member identity second.
Payment/critical action only when needed.
Attendance with real history.
Workout with real 7-day duration bars and expandable detail.
Body progress based on an actual selected metric.
Conditional widgets only when real data and entitlement allow them.

Every widget must support configuration: enabled/disabled, size, order/position, state, entitlement and detail action.
Admin command center must support widget enable/disable, resize, reorder/move, defaults, plan/entitlement availability, preview, reset and publish.
Normal member view must not expose editing controls.
Server entitlement is authoritative.

## VISUAL RULES
Light premium, emerald/mint brand, semantic blue/amber/red/purple.
Compact information density.
Soft depth.
No black-heavy background.
No fidget/toy/3D decorative UI.
No fake data.
No duplicate statistics.
No fake health metrics.
No empty filler cards.

## ATOMIC EXECUTION
Do not implement the entire screen blindly.
For each meaningful component: inspect → implement → build → run → screenshot → compare → fix → rebuild → screenshot → verify.
Update the execution ledger after each verified stage.

## PROJECT ROUTING
Active production module is :app at repository root.
BAD_GYM_MEMBER_INTELLIGENCE_READY/ is legacy/reference only.
OPPO/ and Launch_Oppo_Dev_Studio.bat are out of scope.

## BUILD
./gradlew clean testDebugUnitTest assembleDebug

## FINAL
Verify the 300×550dp card on the target device when reachable.
Verify widget resize/reorder/toggle behavior in admin mode.
Verify entitlement gating.
Verify no obsolete Member Intelligence presentation remains active.
Update the ledger.
Commit and push the verified implementation to main.
Final report must include SHA, changed files, build/test status and screenshot verification.