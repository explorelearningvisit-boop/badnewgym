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

## VERIFIED MILESTONE — CANONICAL OPTION 1 (EMERALD PREMIUM) DEPLOYED ON XIAOMI 11i
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/device_screen_option1_top_verified.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_option1_top_verified.png) (Hero profile, Body stats with sparklines, Attendance 10-col matrix, Workout 30-day bars)
  - [docs/reference/images/device_screen_option1_bottom_verified.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_option1_bottom_verified.png) (Workout KPI cards, Payment 12-month timeline, Due alert, Current Plan & Collect Money button)
- **Visual Match & Constraints Fulfilled**:
  1. **Canonical Option 1 Design (Emerald Premium)**: Faithful, pixel-perfect match to user-uploaded canonical mockups (`media_1792684643498.img`).
  2. **Luminous Hero Header**: Radiant emerald-to-mint gradient card with Ashwini Singh (`ID: BG4187 | 25 yrs | Male`), `Spark Member` & `Active` pills, avatar crown badge, and circular "18 Days Left of 30" progress ring gauge.
  3. **4 Glass Info Chips**: Joined On (14 Apr 2026, 6 Months), Workout Avg. (48 min, ↑ +12%), Goal (Muscle Gain), and Attendance (77%, 24/31 Days).
  4. **Body Stats Card**: Purple header (`💪 Body Stats • Last 6 Months (5 Records) >`) with 4 distinct stat cards (Weight 69.5kg with green curve, Muscle 57.2kg with purple histogram, Body Fat 17.6% with red curve, BMI 22.8 with "Normal" badge & blue curve).
  5. **Attendance Matrix**: 10 columns (`Mon` .. `Wed`), 3 rows (`1`..`31`), 3D glossy radial status circles with bold white date numbers, plus a vertical legend on the right (`● Present 24`, `● Absent 4`, `● Holiday 1`, `● Closed 2`).
  6. **Workout Trend**: 30 vertical cyan-to-blue gradient bars with Peak `👑 52m 10 Oct` and Lowest `↓ 12m 16 Oct` callout badges, dashed 60m/30m guidelines, and 4 KPI summary cards below.
  7. **Payment & Plan**: Red wallet header, 12-month pill strip (Jan–Dec with plan badges & discounts), Due Alert (`₹2,800 Due in 3 days ⚠️`), Last Paid (`₹800`), Current Plan (`Spark Plan`), and glowing Emerald Green `₹ Collect Money >` button.