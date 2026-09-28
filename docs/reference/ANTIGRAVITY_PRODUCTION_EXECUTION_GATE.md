# BAD GYM — Antigravity Production Execution Gate

Date: 2026-09-28
Repository: explorelearningvisit-boop/badnewgym
Branch: member-intelligence-v3
Expected HEAD before execution: cb56cef26ff7bc0d46e99bacc78ccef05c497e86
Application ID: com.example.badnewgym
Main Activity: com.example.badnewgym/.MainActivity

## Mission

Make the current BAD GYM Member Intelligence implementation production-ready. Do not create a second dashboard, mock screen, fake repository, or temporary parallel architecture.

GitHub branch `member-intelligence-v3` is the source of truth.

## Mandatory first step

Run:

git fetch origin
git checkout member-intelligence-v3
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD

STOP if HEAD is not the expected SHA above. Report the actual SHA and blocker.

## Read before editing

Read these files first:

- CURRENT_TASK.md
- STATUS.md
- docs/reference/CHATGPT_ANTIGRAVITY_SYNC.md
- docs/reference/CHATGPT_MEMBER_INTELLIGENCE_V5_VISUAL_MASTER_PROMPT.md
- docs/reference/MEMBER_INTELLIGENCE_PRODUCTION_BLUEPRINT.md
- docs/reference/CHATGPT_ANTIGRAVITY_STAGE_8_EXCHANGE_LOG.md
- docs/reference/generated/member-intelligence/ (all 36 reference visuals)

Also inspect the actual Member Intelligence Kotlin implementation and repository/data wiring before making changes.

## Product contract

One canonical Member Intelligence card.

Left rail:
Home, Attendance, Plan, Payment, More

Right rail:
PT, Workout/Lift, Supplements, Nutrition/Diet, Services, History/Log, Insight/AI, Offers

No global bottom navigation.
No right-side scrolling rail.
No second member dashboard shell.
Rails must remain compact and readable on the existing mobile card geometry.

Every menu must have its own real information model and visual personality:
- current/NOW state
- historical/PAST evidence
- future/NEXT forecast or scheduled information
- analytical charts only when sufficient real records exist
- drill-down to underlying records
- one clear primary action
- truthful ACTIVE / EXPIRING / EXPIRED / NOT_ENROLLED / NO_DATA / UNAVAILABLE / WARNING / CRITICAL / RESOLVED states

Never fabricate values to make a chart or card look complete.

## Production data rules

Remove or isolate any demo/stub repository from production execution paths.
Release must use authenticated, RLS-scoped real repositories.
Do not silently fall back to synthetic members, fake events, fake dates, fake payments, fake trainer sessions, fake services, or fake charts.
Do not delete backend data.
Preserve idempotency for event persistence and Flex settlement.

Flex state machine:
REQUESTED -> ACCEPTED -> CHECKED_IN -> CHECKED_OUT -> CREDITED
Terminal alternatives: DECLINED / CANCELLED
A request alone never creates a credit.
Settlement must be auditable and idempotent.

## Visual implementation

Use the existing fixed mobile card dimensions from the repository. Do not invent arbitrary device dimensions.
Integrate the 36 reference concepts as design/reference guidance, not runtime fake content.
Keep the light-first premium system: ivory/white/soft mint, emerald semantic accents, glass/neumorphism, readable typography.
No black/dark dashboard.
No infinite decorative animation.
Respect reduced motion.
Keep critical states above the fold.

## Verification commands

At minimum run:

./gradlew testDebugUnitTest
./gradlew assembleDebug

If configured:
./gradlew lint

Then install and launch the exact debug artifact on the connected physical Android device:

adb devices
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.example.badnewgym/.MainActivity

Verify:
1. app launches without crash
2. canonical card renders
3. both rails are visible
4. menu selection works
5. Home/event state works
6. real-data empty/unavailable states do not crash
7. payment history/analytics does not fabricate records
8. plan history/renewal evidence does not fabricate records
9. PT/workout/nutrition/supplement/service surfaces respect capability and entitlement
10. machine/operations surfaces do not leak gym-level fake data into a member snapshot
11. Flex states are deterministic and auditable
12. no critical content is clipped or hidden
13. reduced-motion behavior works

Capture a screenshot matrix from the physical device:
- Home
- Attendance
- Plan
- Payment
- PT
- Workout
- Nutrition
- Supplements
- Services
- History
- Insight
- Offers
- at least one critical event state
- at least one empty/unavailable state

## Git discipline

Do not use force push.
Do not overwrite unrelated user work.
Commit only intentional production changes.

Before final push:
git status --short
git diff --stat
git diff --name-only
git rev-parse HEAD

Push:
git push origin member-intelligence-v3

Then verify the remote SHA again.

## Mandatory final report

Update docs/reference/ANTIGRAVITY_SYNC_ACK.md with exactly:

MODEL ->
PROVIDER / CONFIGURATION ->
PULLED_HEAD ->
FILES_FOUND ->
FILES_CHANGED ->
IMPLEMENTATION ->
UNIT_TEST ->
BUILD ->
LINT ->
DEVICE ->
SCREENSHOTS ->
FINAL_SHA ->
BLOCKERS ->
OPEN QUESTIONS FOR CHATGPT ->

Do not claim device/build success without actual command output or screenshot evidence.
Do not claim production-ready if any critical gate is red.
