# BAD GYM — Visit Based Member Card System

BAD GYM is an Android Jetpack Compose gym-management application centered on a **Visit Based Member Card**.

The previous eight-theme card presentation is retired from the active renderer. The current product surface is a single responsive light UI whose widgets change with the member's current visit/event.

## Active card geometry

- Maximum card width: **360dp**
- Card ratio: **3:4**
- Safe content target: **16dp**
- Vertical rail: **56dp**
- Content target: **288dp**
- Four-column widget grid
- Widget sizes:
  - XS = 1 × 1
  - S = 2 × 1
  - M = 2 × 2
  - L = 3 × 2
  - XL = 4 × 2

## Visit Based Widget System

Every information block is a widget.

The widget catalog includes:
- Member Identity
- Current Visit
- Payment Alert
- Attendance
- Plan
- Gym Time
- Workout
- Trainer / PT
- Body Progress
- Services
- Offers
- History
- Insight

`VisitWidgetConditionResolver` decides whether a widget appears from:
1. current event/visit
2. member snapshot data
3. server entitlement
4. gym-local widget layout

A widget is not rendered just because it exists in the catalog.

## Example: CHECK-OUT

A CHECK-OUT visit can keep:
- member identity
- checkout event
- payment alert when outstanding money exists
- attendance
- plan

and can additionally expose:
- gym-time / visit duration
- workout evidence
- body progress
- visit history
- insight

This keeps the card operational rather than showing the same static dashboard for every member state.

## Commercial entitlement

The software owner/admin controls paid feature access on the server.

The app receives `VisitWidgetEntitlements`. A gym can:
- reorder widgets
- locally disable an included widget

but cannot locally re-enable a server-revoked paid feature.

When the server entitlement expires/revokes a paid feature, the corresponding widgets become unavailable after the entitlement refresh and the editor shows **Upgrade required**.

Production data source: Supabase tables documented in `docs/DATABASE_SCHEMA.md`, including `gym_subscriptions` and `gym_entitlements`.

## Implementation

- Kotlin
- Jetpack Compose
- Material 3
- Coil
- Room
- Gson
- KSP

Primary implementation:
`app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/PixelPerfectMemberCard.kt`

Domain:
`app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/VisitWidgetSystem.kt`

Server access contract:
`app/src/main/java/com/example/badnewgym/feature/memberintelligence/domain/model/GymWidgetAccess.kt`

Tests:
`app/src/test/java/com/example/badnewgym/feature/memberintelligence/domain/engine/VisitWidgetConditionResolverTest.kt`

## Google Antigravity

Pull and verify:

```bash
git pull origin main
./gradlew clean testDebugUnitTest assembleDebug
```

Then install/run on a 360dp-class device and exercise CHECK_IN, CHECK_OUT and PAYMENT scenarios.

See `ANTIGRAVITY_RUN.md` and `CURRENT_TASK.md` for the execution contract.

## Scope boundary

BAD GYM changes must remain inside BAD GYM application/test/documentation paths. Do not modify unrelated OPPO/mobile-control tooling or Banaras Tour Gold / Manchester-company work.
