# BAD GYM — Google Antigravity End-to-End Handoff

## First read

Before changing code, read:

1. `AI_COLLABORATION_PROTOCOL.md`
2. `docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`
3. `CURRENT_TASK.md` if present
4. this file
5. the relevant feature/domain handoff

The end-to-end contract is authoritative for the image-to-implementation workflow.

## Pull and build

Use the active working branch/PR. Do not assume `main` contains unmerged work.

```bash
git pull
./gradlew clean testDebugUnitTest assembleDebug
```

## Current renderer direction

The old 360dp / 3:4 / four-column presentation described by older documentation is stale.

The current member-card renderer is a full available mobile viewport surface with:

- light premium Material treatment
- no black/dark workspace
- maximum content width around 420dp
- compact header
- vertical rail navigation
- compact metrics and event-driven content
- vertically scrollable secondary menu content
- Home as the operational command-center
- visit/event semantics resolved by the domain layer
- server entitlement authoritative for paid features

Inspect the actual current implementation before changing geometry:

`app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/PixelPerfectMemberCard.kt`

Do not restore retired eight-theme card presentation.

## Reference-image execution

When the user supplies a reference image:

1. Lock the image as the visual source of truth.
2. Decompose it into atomic regions.
3. Inspect existing code before creating anything.
4. Implement one atomic region.
5. Build.
6. Run on Xiaomi 11i when reachable.
7. Capture the target screen.
8. Compare with the reference.
9. Fix geometry/typography/color/assets/interaction.
10. Verify again.
11. Only then move to the next region.
12. Integrate all verified regions.
13. Verify the complete screen.
14. Verify interaction and domain logic.
15. Run regression tests.
16. Commit the verified work.

Never implement the whole image in one speculative pass.

## Model

At the start of a substantial task, inspect the Gemini models actually available in Google Antigravity.

Select the highest-capability available model for visual reasoning + Android/Compose coding + debugging. Prefer the strongest Pro/reasoning-capable option available. Never invent an unavailable model name.

## Device verification

Target: Xiaomi 11i.

When the phone-native device agent is available, use it for:

- launch BAD GYM
- screenshot
- UI-tree inspection
- tap
- swipe
- back/home
- text entry where applicable

Do not claim physical-device verification if the device is not reachable.

Current phone-agent limitation: its screenshot path currently captures the BAD GYM Activity view rather than arbitrary Android/system UI. Do not claim full-device screenshot support until MediaProjection/accessibility screenshot capture has actually been implemented and verified.

## Required completion report

At every completed implementation stage record:

- exact HEAD SHA
- branch
- changed files
- build result
- test result
- device result
- screenshot evidence
- remaining visual discrepancies
- unresolved issues

Completion is not "code generated"; completion means the implementation has been built and verified to the extent the available device/tooling permits.
