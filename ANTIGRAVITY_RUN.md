# BAD GYM — Visit Based Member Card / Google Antigravity Handoff

## Pull
```bash
git pull origin main
```

## Build
```bash
./gradlew clean testDebugUnitTest assembleDebug
```

## Run
Install the generated debug APK on the connected phone/emulator and open BAD GYM.

## Active product architecture
The old eight-theme Member Intelligence presentation is retired from the rendered card.

The active UI is:
- 360dp maximum card width
- 3:4 card ratio
- 16dp safe content
- 56dp vertical rail
- approximately 288dp content width
- four-column responsive widget grid
- XS 1x1, S 2x1, M 2x2, L 3x2, XL 4x2 widgets
- light premium material surface with emerald/blue/red/amber/purple semantic accents
- no black/dark workspace
- every information block is a widget

## Visit-driven rendering
The current event decides which widgets are relevant.

Examples:
- CHECK_IN: identity, current visit, attendance, plan and relevant live/support widgets.
- CHECK_OUT: identity + checkout visit remain, payment alert remains visible when outstanding, and gym-time/body-progress/workout evidence becomes eligible.
- PAYMENT: payment/plan/history/insight evidence is prioritized.
- WALK_IN/TRIAL: identity, visit, plan, offers and trial history are used.

The resolver is:
`VisitWidgetConditionResolver`.

## Entitlement model
The software owner/admin controls commercial feature access on the server.

The Android client receives:
`VisitWidgetEntitlements`.

Rules:
1. Server entitlement is authoritative.
2. A gym owner can reorder or locally disable widgets only inside the features granted by the server.
3. If a paid feature expires or is revoked, its widgets automatically disappear/lock on the client after the entitlement refresh.
4. The client must not locally re-enable a server-revoked feature.
5. The existing database architecture already defines `gym_subscriptions` and `gym_entitlements`; a production Supabase repository should populate the entitlement contract.

## Widget editor
Tap the tuning icon in the card header.
- Toggle available widgets.
- Long-press widgets to reorder them.
- Locked features show `Upgrade required`.
- Condition rendering still applies after local configuration.

## Important scope boundary
Do not modify:
- OPPO/mobile-control tooling
- phone-operation/remote-control experiments
- Banaras Tour Gold / Manchester-company work
- unrelated repository folders

Only modify BAD GYM feature, test and documentation files for this task.

## Completion report
After verification, report:
- exact HEAD SHA
- changed files
- Gradle build result
- unit test result
- connected device result
- screenshot path
- unresolved issues
