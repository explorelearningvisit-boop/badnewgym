# ANTIGRAVITY — BAD GYM MEMBER INTELLIGENCE V2 EXECUTION PROMPT

Pull main and execute this task without asking the user to restate the design.

START:

git checkout main
git pull --ff-only origin main
git status

Read:
AGENTS.md
CURRENT_TASK.md
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_EXECUTION_LEDGER.md
docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md
docs/reference/BAD_GYM_MOBILE_PIXEL_PERFECT_GUIDE.md

Then inspect the complete active production implementation under app/src/main/.

IMPORTANT:
The old Member Intelligence presentation is obsolete. Do not restore it.
BAD_GYM_MEMBER_INTELLIGENCE_READY/ is legacy/reference only.
OPPO/ and Launch_Oppo_Dev_Studio.bat are out of scope.

IMPLEMENT:

1. Replace the old Member Intelligence presentation with the V2 concept.
2. Use 300dp × 550dp as the compact Member Intelligence card target.
3. Allow modest height expansion only when real content requires it.
4. Keep the global screenshot composition at 691×1536 / 20:9.
5. Use the new light premium emerald/mint visual language with semantic blue/amber/red/purple.
6. Keep the card compact and operational.
7. Do not add fidget toys, toy-like 3D UI, decorative objects, fake AI ornaments or meaningless metrics.
8. Use real member/domain data only.
9. Current event must be the first priority.
10. Member identity must contain useful identity and membership validity without duplicate plan cards.
11. Payment appears only when relevant and must use a real collection/reminder flow.
12. Attendance must use real history.
13. Workout must use real duration history; compact view is 7-day vertical bars.
14. Body progress must be metric-aware and neutral when goal direction is unknown.
15. Conditional widgets appear only when real data and entitlement allow them.

WIDGET SYSTEM:

Every widget must support:
- enable/disable
- size
- position/order
- conditional visibility
- entitlement
- detail action

Logical sizes:
XS 1×1
S 2×1
M 2×2
L 3×2
XL 4×2

ADMIN COMMAND CENTER:

Build the product-admin configuration architecture and UI.
Admin can:
- enable/disable widgets
- resize widgets
- reorder/move widgets
- configure defaults
- control plan/entitlement availability
- preview the member card
- reset
- save draft
- publish

Keep admin configuration separate from member runtime state.
Server entitlement is authoritative.
Normal member/staff view must not expose editor controls.

EXECUTION LOOP:

For each meaningful component:
inspect → implement → build → run → screenshot → compare → fix → rebuild → screenshot → verify.

Do not declare success from compilation alone.

VERIFY:
- 300×550dp card geometry
- responsive height expansion
- widget enable/disable
- widget resize
- widget reorder/move
- entitlement gating
- real data
- payment states
- attendance
- workout
- body progress
- conditional modules
- admin command center
- no obsolete UI
- no toy/fidget UI
- no duplicate or fake data

BUILD:

./gradlew clean testDebugUnitTest assembleDebug

Update:
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_EXECUTION_LEDGER.md

Record:
- stage
- status
- changed files
- build result
- test result
- screenshot evidence
- discrepancies
- fixes
- device result

When verified, commit and push to main.

FINAL REPORT:
- old presentation removed/replaced
- V2 implemented
- admin command center status
- widget system status
- changed files
- build/test status
- screenshot/device verification
- final SHA
- remaining issues
