# BAD GYM Member Intelligence V4 — Visual Redesign Handoff

## Objective

Replace the previous cramped/flat member-card presentation with the approved light, premium, information-dense BAD GYM visual language.

The implementation is one canonical member intelligence card. It is not a second member-detail dashboard.

## Locked visual direction

- Light-first only: ivory/white/mint/emerald plus semantic accent colors.
- No black dashboard and no dark sci-fi surface.
- Two short rails are visible inside the same card:
  - LEFT: Home, Attendance, Plan, Payment, More.
  - RIGHT: PT, Workout/Lift, Supplements, Nutrition/Diet, Services, History/Log, Insight/AI, Offers.
- Right-rail entries are capability/data driven.
- Rails never become long scrolling navigation.
- Identity remains persistent while menus change.
- The center surface is the only changing content area.
- Menu changes use restrained horizontal transition motion.
- Critical semantic colors remain truthful: red/danger, amber/warning, green/success, blue/info, purple/PT.
- Existing member-photo resolver remains the media source; no generated fake media is introduced by the V4 shell.

## Information hierarchy

EVENT → MEMBER → TIME → STATE → EVIDENCE → PATTERN → ACTION

Above the fold:

1. Current event + relative time.
2. Member portrait, name, member code, tier and verification.
3. Attendance, PT balance and workout KPI tiles.
4. Menu context grammar: NOW/PAST/FUTURE or the menu's specific evidence model.
5. One primary action.

## Menu integration

The V4 shell routes all existing typed menu panels through the same canonical card:

- Home — cockpit and decision signals.
- Attendance — real attendance history/patterns.
- Plan — membership lifecycle.
- Payment — dues, timeline and payment evidence.
- Trainer/PT — coach and session data.
- Workout/Lift — workout history/progress.
- Supplements — recorded supplement activity.
- Nutrition/Diet — recorded nutrition subscription/data.
- Services — services, Flex and facility evidence.
- History/Log — timestamped evidence timeline.
- Insight/AI — existing intelligence signals/evidence.
- More — utility directory.
- Offers — only real promotion data.

No panel may fabricate data to fill empty space.

## Geometry

The card geometry was rebalanced specifically for the two-rail composition. The 360dp-class phone layout now gives the center content enough width while keeping both rails visible.

The carousel now correctly honors its expanded-card state instead of hard-coding the card as non-detail.

## Code

Primary V4 shell:
app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/RedesignedMemberIntelligenceCard.kt

Carousel integration:
app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/CompactMemberCarousel.kt

Menu surface polish:
app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/MenuContentPanels.kt

Geometry:
app/src/main/java/com/example/badnewgym/feature/memberintelligence/design/dimensions/CompactCardDimensions.kt

## Verification required before release claim

Antigravity must pull the exact branch HEAD and run:

1. git fetch origin
2. git pull --ff-only origin member-intelligence-v3
3. git rev-parse HEAD
4. Kotlin/unit tests.
5. Debug APK build.
6. Lint if configured.
7. Physical Xiaomi Redmi Note 11 verification.

Capture screenshots for at least:

- Home / active check-in
- Attendance
- Payment overdue
- PT
- Workout
- Services/Flex when capability exists
- History
- Insight
- expired/frozen/payment-critical fixtures
- machine/facility operational fixture

Do not reuse screenshots from an older SHA.

## Current implementation SHA

342f4eadc1c515db7ea5c08317b01995f26607e8

Build/device status is intentionally not claimed by this document until Antigravity verifies the exact SHA.
