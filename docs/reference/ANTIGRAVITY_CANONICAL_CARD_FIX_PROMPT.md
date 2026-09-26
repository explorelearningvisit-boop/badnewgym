# ANTIGRAVITY EXECUTION — CANONICAL MEMBER CARD FIX

Repository: explorelearningvisit-boop/badnewgym
Branch: member-intelligence-v3

## Objective

Finish and verify the Member Intelligence card consolidation immediately.

The required product behavior is:

**ONE canonical Member Intelligence card.**

Do NOT show an AdvancedEventMemberCard as the default card and then replace it with a second CompactMemberCard/detail interface after tapping. Event-specific intelligence must be integrated into the canonical card itself.

## Git sync — mandatory first

Run:

git fetch origin
git pull --ff-only origin member-intelligence-v3
git rev-parse HEAD

Then inspect the actual diff from the latest HEAD before changing anything.

Recent ChatGPT commits already on the branch:

- 59fd7b7f343743d8cf806b11a10f23dc0d4435d6
- acaef6b242c7200c12c377723c5cfe99a35fed4b
- 42fe2414938f7289e56a6665ee3e4642ab86c342
- 0be3ec4cc91596e83e1bb2f90889f5d26383c925
- 4050a912d2dd28d6e76ee162595cb00d156da14d

These are the starting point. Do not blindly overwrite them.

## Files to inspect first

- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceScreen.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceViewModel.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCarousel.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCard.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/AdvancedEventMemberCard.kt
- app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/EventCardCatalog.kt

## Required code behavior

### 1. Canonical surface

The normal carousel must render CompactMemberCard.

Do not use AdvancedEventMemberCard as the default browse surface.

AdvancedEventMemberCard must not appear as a second interface after tapping.

If it is now unused by the normal flow, do not delete it blindly; inspect references first and preserve it only where genuinely required by QA/legacy code.

### 2. Tap behavior

Tapping/selecting a member must NOT create a second expanded dashboard/detail shell.

The selected member stays on the same canonical card.

openMemberDetail() must not set isDetailExpanded=true for the normal member-card flow.

Remove or bypass any duplicate detail launcher row.

### 3. Event intelligence belongs INSIDE the canonical card

The card should expose compact, decision-useful event evidence without changing the card identity.

Minimum representative mappings:

- CHECK_IN → arrival + on-time/late
- CHECK_OUT → checkout + session duration
- PAYMENT_OVERDUE / PAYMENT_DUE / PAYMENT_FAILED / PAYMENT_PARTIAL → outstanding + payment status
- WALK_IN / TRIAL_* → journey + expiry/outcome
- FREEZE_* → freeze start + end
- BANNED / BAN_LIFTED → access + reason
- TRAINER_SESSION_* → coach + session state/time
- WORKOUT_* / PR_ACHIEVED → routine + activity
- SERVICE_PURCHASE / SUPPLEMENT_PURCHASE / NUTRITION → service + status
- MAINTENANCE → asset + operational status

No fake values. Missing evidence must render as an honest placeholder such as — / Not recorded.

### 4. One CTA

There must be one contextual primary CTA.

Representative event-aware labels:

- payment overdue/due/failed → Collect Payment
- partial payment → Collect Balance
- trial started/expired → Convert Trial
- trial converted → Activate Plan
- freeze started → View Freeze
- ban active → View Ban Reason
- trainer scheduled → View Trainer Session
- trainer started → View Live Session
- trainer missed → Reschedule Session
- workout → View Workout
- service → View Service
- maintenance → View Maintenance

Do not add multiple competing CTAs.

### 5. Remove duplicate UI

The old bottom member row:

member photo + name + plan/trainer + OPEN

must NOT exist on the Member Intelligence browse surface.

Do not recreate it in another composable.

### 6. Do not regress the design

Keep the existing BAD GYM light-first visual system.

No black/dark dashboard redesign.

Do not create a generic admin dashboard.

Do not add fake avatars, fake charts, fake event data, or random decorative state changes.

Do not create dozens of unrelated composables. Reuse the canonical card renderer and event taxonomy.

### 7. QA Lab

QA Lab is allowed as an explicit QA/debug action.

It must NOT become the normal/default Member Intelligence surface.

The runtime default must open directly into the canonical member-card carousel.

## Compile correctness

After editing, run:

./gradlew assembleDebug
./gradlew testDebugUnitTest

If either fails, fix the actual compile/test errors before stopping.

Also run lint if the repository has the configured lint task.

Pay special attention to:

- CompactMemberCarousel braces/control flow
- unused or stale isDetailExpanded logic
- CompactMemberCard imports
- EventType nullable/non-null smart casts
- duplicate click handlers
- MemberIntelligenceScreen BackHandler logic
- ViewModel state updates
- any references still expecting the old detail surface

Do not claim success without command output.

## Physical device verification

Install/run the debug build on the connected Android device.

Verify these cases:

1. CHECK_IN
2. PAYMENT_OVERDUE
3. FREEZE_STARTED
4. TRIAL_STARTED or TRIAL_EXPIRED
5. TRAINER_SESSION_SCHEDULED
6. WORKOUT_COMPLETED
7. SERVICE_PURCHASE
8. BANNED

For every case verify:

- canonical card is the first/default visible surface
- no second card appears after tap
- no duplicate OPEN row
- event facts are integrated inside the card
- one CTA only
- long names do not clip
- amounts/dates do not clip
- card remains usable in horizontal carousel
- no fake data was introduced
- QA Lab remains optional

Capture screenshots for at least CHECK_IN, PAYMENT_OVERDUE, FREEZE_STARTED, TRIAL, TRAINER and WORKOUT.

## Git discipline

Before commit:

git status --short
git diff --stat
git diff --name-only

Then commit only the required files with a focused message, e.g.:

fix: consolidate member intelligence card surface

Push to:

origin member-intelligence-v3

After push:

git rev-parse HEAD

Report:

1. pulled HEAD
2. exact changed files
3. build result
4. unit-test result
5. lint result if run
6. physical-device result
7. screenshot result
8. final pushed SHA
9. any remaining blocker

Important: commit count is NOT file count. The exact SHA and exact changed-file inventory are the source of truth.
