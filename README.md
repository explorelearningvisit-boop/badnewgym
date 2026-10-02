# BAD GYM — Visit Based Member Card System

BAD GYM is an Android Jetpack Compose gym-management application centered on a **Visit Based Member Card**.

## Current implementation contract

The production workflow is now reference-image driven:

**reference image → atomic decomposition → component implementation → build → device screenshot → visual comparison → correction → verification → next component → full integration → logic/state verification → regression**

The complete Google Antigravity execution contract is:

`docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`

Google Antigravity must not implement a supplied reference image as one approximate pass.

## Current member-card renderer

The old eight-theme card presentation and old 360dp/3:4/four-column documentation are retired/stale.

The current renderer uses:

- full available mobile viewport
- approximately 420dp maximum content width
- light premium Material surface
- emerald/blue/red/amber/purple semantic accents
- compact header
- vertical rail navigation
- Home command-center content
- compact visit/payment/attendance information
- event/visit-driven widget resolution
- server-authoritative commercial entitlements

Primary implementation:

`app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/PixelPerfectMemberCard.kt`

Domain:

`app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/VisitWidgetSystem.kt`

Server access contract:

`app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/GymWidgetAccess.kt`

Tests:

`app/src/test/java/com/example/badnewgym/feature/memberintelligence/domain/engine/VisitWidgetConditionResolverTest.kt`

## Build

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

## Scope

BAD GYM changes must remain inside BAD GYM application/test/documentation paths. Do not modify unrelated OPPO/mobile-control tooling or Banaras Tour Gold / Manchester-company work.
