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
- **Commit**: `ee1281f179693f0304f12997e08139bcf2add30a`
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/device_screen_option1_top_verified.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_option1_top_verified.png)
  - [docs/reference/images/device_screen_option1_bottom_verified.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_option1_bottom_verified.png)

## VERIFIED MILESTONE — HIGH-DENSITY SINGLE-VIEWPORT SMART REDESIGN (ALL 5 WIDGETS AT A GLANCE)
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/smart_single_viewport_verified.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/smart_single_viewport_verified.png)
- **Design & Technical Achievements**:
  1. **Single-Viewport Zero-Scroll Architecture**: All 5 widgets (Hero Profile, Body Stats Bento, Attendance Matrix, Workout Trend Chart, Payment & Plan) fit cleanly inside the ~740dp mobile viewport at once without scrolling, leaving comfortable safety margins for system insets.
  2. **Widget 1 (Hero Profile, ~112dp)**: 56dp avatar with gold crown & glowing emerald online dot; `Good Morning 👋` + `Ashwini Singh` (`ID: BG4187 | 25 yrs | Male`); `👑 Spark Member` + `✓ Active` pills; 62dp circular progress gauge (`18 Days Left of 30`); 4 two-column glass info chips (`Joined On: 14 Apr 24 6M`, `Workout Avg: 48 min ↑12%`, `Goal: Muscle Gain`, `Attendance: 77% 24/31`) with dedicated colored icon square badges.
  3. **Widget 2 (Body Stats Bento, ~90dp)**: Header `💪 Body Stats • Last 6 Months (5 Records) >`; 4 bento cards (Weight 69.5kg with emerald spline sparkline, Muscle 57.2kg with 9-bar purple audio/muscle frequency equalizer, Body Fat 17.6% with red wavy sparkline, BMI 22.8 with `Normal` pill badge and 3-segment range line with position indicator dot).
  4. **Widget 3 (Attendance Matrix, ~120dp)**: Header `📅 Attendance • Oct 2026 | 24/31 Days | 77% >`; 10-column header (`Mon`..`Wed`); 3 rows of 10 glossy circular buttons (1..10, 11..20, 21..31) in a perfect 10×3 grid; vertical legend on right (`Present 24`, `Absent 4`, `Holiday 1`, `Closed 2`).
  5. **Widget 4 (Workout Trend, ~128dp)**: Header `🏋️ Workout Trend • Oct 2026 | 30 Days >`; 65dp 30-day cyan/emerald bar chart with floating Peak badge (`👑 52m 10 Oct`) and Lowest badge (`↓ 12m 16 Oct`); 4 KPI summary cards below (`48m Avg/Day ↑+12%`, `52m Peak 10 Oct`, `12m Lowest 16 Oct`, `22 Days With Workout 8 Days Rest`).
  6. **Widget 5 (Payment & Plan, ~128dp)**: Header `👛 Payment & Plan • Last 12 Months | View All >`; 12-month horizontal scroll strip (Jan–Dec with colored badges & pending state); bottom action row featuring Due Alert (`₹2,800 Due in 3d ⚠️ 16 Oct`), Last Paid (`₹800 01 Oct`), Current Plan (`Spark Plan Nov 30`), and full primary action button `₹ Collect Money >`.