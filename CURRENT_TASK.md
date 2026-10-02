# BAD GYM — Current Task

## Active product direction
Replace the legacy theme-based Member Intelligence card presentation with the **Visit Based Member Card v1**.

## Scope
Only files under the BAD GYM Android application and its BAD GYM documentation/tests are in scope.

## Product rules
- Card shell: 360dp maximum width, 3:4 ratio.
- Safe content target: 16dp.
- Vertical rail: 56dp.
- Content target: 288dp.
- Four-column widget grid.
- Widget sizes: XS 1x1, S 2x1, M 2x2, L 3x2, XL 4x2.
- Every information block is a widget.
- Visibility is event/visit driven.
- Gym layout can enable/disable and reorder widgets.
- Server entitlement is authoritative. Local UI cannot re-enable a server-revoked feature.
- Checkout keeps payment alert visible when money is outstanding and enables visit-duration/progress widgets.
- No old theme selector or theme-dependent card rendering.

## Verification required
Run:
```
./gradlew clean testDebugUnitTest assembleDebug
```

Then install/run on a 360dp-class phone and verify CHECK_IN, CHECK_OUT and PAYMENT scenarios.
