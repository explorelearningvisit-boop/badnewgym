# BAD GYM — Current Task

## ACTIVE TASK — MEMBER INTELLIGENCE V2

The previous Member Intelligence presentation is obsolete and must not remain the active visual source of truth.
The new production contract is:
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md

## TARGET
Implement the new BAD GYM Member Intelligence concept as a production Android UI.
Primary compact card target: 300dp width × 550dp height.
Allow modest height expansion when real widget content requires it; prefer approximately 600dp maximum before deeper detail navigation.
Keep the global BAD GYM mobile reference composition at 691×1536 / 20:9 for screenshot comparison unless the user explicitly changes it.

## DESIGN
Professional light premium gym-management UI.
Emerald/green brand + mint surfaces.
Blue information/attendance.
Amber due/attention.
Red urgent/payment.
Purple trainer/insight where useful.
No black/dark-heavy background.
No fidget toys, toy-like 3D decoration, fake AI ornaments or meaningless visual filler.

## MEMBER CARD HIERARCHY
Current event → member identity → critical action → attendance → workout → body progress → conditional widgets → secondary detail.
Use real data only.
Do not show empty widgets or unsupported health metrics.
Do not duplicate information.

## WIDGET SYSTEM
Every widget is independently configurable.
Capabilities: enable/disable, resize, reorder/move, visibility condition, entitlement and detail action.
Logical sizes: XS 1×1, S 2×1, M 2×2, L 3×2, XL 4×2.
Normal member/staff view is not editable.

## ADMIN COMMAND CENTER
The main BAD GYM software admin owns widget configuration.
Admin can enable/disable, resize, reorder, preview, reset, configure defaults, publish configuration and manage plan/entitlement availability.
Server entitlement is authoritative and local UI state must never bypass it.
Keep admin configuration separate from member runtime state.

## IMPLEMENTATION
Active production project: repository root Gradle project, module :app.
Production package: com.example.badnewgym.
Do not implement current work in BAD_GYM_MEMBER_INTELLIGENCE_READY/.
Do not modify OPPO/ or Launch_Oppo_Dev_Studio.bat.

Build:
./gradlew clean testDebugUnitTest assembleDebug

Required workflow:
inspect → implement atomic component → build → run → screenshot → compare → fix → rebuild → screenshot → verify → ledger update.

## COMPLETION
Do not report completion from compilation alone.
Completion requires the new UI, widget editor architecture, admin command center integration point, real data/entitlement behavior, responsive 300×550dp target, screenshot verification and a verified commit on main.

## ACTIVE DESIGN OVERRIDE — MEMBER INTELLIGENCE V4

The current approved design/implementation source of truth is:

- `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md`
- `docs/reference/ANTIGRAVITY_MEMBER_INTELLIGENCE_V4_IMPLEMENTATION_PROMPT.md`

V4 supersedes earlier Member Intelligence visual-layout assumptions.

Hard runtime geometry:
- target width: 350dp
- responsive minimum: approximately 320dp
- preferred height: 500–540dp
- maximum normal adaptive height: 600dp
- screenshot/reference canvas: 691×1536 / 20:9

V4 priorities:
- event-first hero (CHECK-IN, CHECK-OUT, PAYMENT, PAYMENT DUE, TRAINER, ISSUE, etc.)
- dynamic event priority and widget reflow
- membership remaining days + plan duration + last payment + next due
- 30-day attendance calendar
- late = green center + amber border
- holiday = X
- monthly workout data with 7-day horizontal viewport
- monthly peak/lowest/trend/best-week summary
- goal-aware multi-metric Body Goals
- progress photos linked to measurements where available
- privacy hide/show for sensitive body metrics
- conditional Trainer/Payment/Issue widgets
- no permanent payment-completed widget
- no Recent Activity default widget
- compact premium light neumorphism
- admin-only drag/reorder/resize/enable/disable/priority/publish controls
- server entitlement remains authoritative

Do not implement the older oversized/tablet-like Member Intelligence composition.
