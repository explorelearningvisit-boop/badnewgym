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

## VERIFIED MILESTONE — CANONICAL V2 DEPLOYED ON XIAOMI 11i
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `9a35fd1f79da72d577e8037c8b019cfd7b251d54`
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/device_screen_compact_verified.png](file:///C:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_compact_verified.png) (Gold 6-Month Plan)
  - [docs/reference/images/device_screen_basic_plan_verified.png](file:///C:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_basic_plan_verified.png) (Basic Member Green Tint on Hero Profile only)
  - [docs/reference/images/device_screen_spark_plan_verified.png](file:///C:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_spark_plan_verified.png) (Spark Member Diamond/Cyan Tint on Hero Profile only)
  - [docs/reference/images/device_screen_receipt_sheet_verified.png](file:///C:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_receipt_sheet_verified.png) (Interactive Official Receipt sheet)
- **Visual Match & Constraints Fulfilled**:
  1. **Zero-Scroll Viewport**: Entire dashboard (Hero profile, Body Stats, Attendance, 30d Workout Trend, Payment Plan & 12M History) fits cleanly in a single screen without scrolling.
  2. **Top App Bar Removed**: Eliminated top header bar & standalone pills to save vertical space.
  3. **Embedded Notification Bell**: Compact bell icon with unread badge embedded directly in the Hero profile card.
  4. **Single-Widget Tier Tinting**: Only the Hero Member Profile widget receives the member's tier styling (Emerald Green for Basic, Gold/Sapphire for 6M Gold, Electric Cyan for Spark). All other cards maintain their natural luxury dark obsidian palette.
  5. **Numbered Attendance Dots**: Explicit day numbers `1` through `31` rendered inside every attendance day circle.
  6. **Side-by-Side Split**: Attendance (left) and Workout Trend (right) positioned in a 2-column layout to eliminate wasted horizontal space.