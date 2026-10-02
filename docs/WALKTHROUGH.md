# BAD GYM — Member Intelligence Card v2

## Current implementation

The Member Intelligence Card is now a **canonical visit-driven mobile card**, not a dashboard made from a grid of equal white boxes.

### Visual hierarchy

1. **Member identity header**
   - photo / fallback avatar
   - member name and code
   - current plan
   - current event pill
   - subscription state
2. **56dp-class vertical rail**
   - only relevant/entitled menus are shown
   - active menu uses the event accent
   - alert badges remain visible
3. **One active menu canvas**
   - Home is the primary intelligence view
   - other menus render their own dedicated evidence/detail layout
   - the card does not dump every widget into the same viewport
4. **Primary intelligence**
   - the highest-priority existing IntelligenceSignal is surfaced first
   - no business-critical state is fabricated when the engine has no signal
5. **Event-aware status**
   - CHECK-IN presents live visit context
   - CHECK-OUT presents completed-visit context
   - outstanding payment remains visible after checkout
   - payment, trainer, workout, services and insight content remain entitlement/relevance driven

## Geometry

- Maximum card width: 360dp
- Shell ratio: 3:4
- Safe content target: 16dp
- Vertical rail: 56dp target
- Light premium BAD GYM palette
- Soft glass/neumorphic surfaces without a black background
- One practical mobile viewport; no full-card vertical dashboard scroll

## Important behavior

- The active menu is now actually used by the renderer.
- Optional menus such as Trainer, Supplements and Nutrition are hidden when the member does not have the capability **or** the gym does not have the corresponding entitlement.
- Server entitlement remains authoritative.
- Local widget controls cannot re-enable a server-revoked feature.
- Legacy theme selector/skin rendering is not part of the canonical card.

## Verification status

The source has been updated, but this chat environment does **not** have the user's local Android SDK/ADB execution path, so a live Xiaomi 11i build/install/screenshot has not been claimed here.

Required local verification:

```
./gradlew clean testDebugUnitTest assembleDebug
```

Then install/run on the 360dp-class target and verify at minimum:

- CHECK_IN
- CHECK_OUT
- PAYMENT / PAYMENT_OVERDUE
- Trainer hidden when unentitled
- Optional menus hidden when unavailable
- Home → rail → dedicated menu transitions
