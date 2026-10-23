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

## VERIFIED MILESTONE — 5-STAGE EXACT REFERENCE IMAGE ALIGNMENT (ZERO-SCROLL VIEWPORT)
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `260a5101fa6259609ac861a73e9c3e3e4cfa46a2`
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/stage_verified_single_viewport.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/stage_verified_single_viewport.png)
- **5-Stage Execution Breakdown**:
  1. **Stage 1 (Hero Profile Card Overhaul)**:
     - Member identity: `Ashwini Kumar Sharma` (16.5sp black bold), `Good Morning ☀️`, `ID: BG4187 | 25 yrs | Male`.
     - Status Pills: `👑 Spark Member` + `✓ Active`.
     - Gauge: Circular progress ring with emerald arc displaying `18 Days Left of 30`.
     - 5 Quick-Stat Glass Chips: `📅 Joined 14 Apr 2026 (6M)`, `🏋️ Avg. Workout 48 min (↑+12%)`, `🔥 Streak 12 Days`, `🎯 Goal Muscle Gain`, `📊 Attendance 77% (24/31 Days)`.
  2. **Stage 2 (Body Stats Bento)**:
     - Header: `💪 Body Stats • Last 6 Months (5 Records) >`.
     - 4 Bento tiles: Weight (`69.5 kg` with emerald curved sparkline), Muscle (`57.2 kg` with 9-bar purple equalizer histogram), Body Fat (`17.6%` with red wavy sparkline & dots), BMI (`22.8` with `Normal` green badge and 3-segment range line with indicator dot).
  3. **Stage 3 (Attendance 7-Column Mon–Sun Standard Calendar Grid)**:
     - 7 columns (`Mon Tue Wed Thu Fri Sat Sun`).
     - 5 rows (Row 1: `1..7`, Row 2: `8..14`, Row 3: `15..21`, Row 4: `22..28`, Row 5: `29, 30, 31` + 4 trailing placeholder dots `•`).
     - Right legend: `🟢 Present 24`, `🔴 Absent 4`, `🟡 Holiday 1`, `⚪ Closed 2`.
  4. **Stage 4 (Workout Trend Chart & KPI Cards)**:
     - Header: `🏋️ Workout Trend • October 2026`, `30 Days >`.
     - 30-day vertical bars with cyan/blue gradient and day 13/16 color highlights.
     - Floating Peak badge (`👑 52m 10 Oct`) and Lowest badge (`↓ 12m 16 Oct`).
     - 4 KPI cards: `⏱️ 48m Avg/Day ↑+12%`, `👑 52m Peak 10 Oct`, `↓ 12m Lowest 16 Oct`, `📊 22 Days With Workout 8 Days Rest`.
  5. **Stage 5 (Payment & Plan Inverted Architecture)**:
     - Header: `👛 Payment & Plan`, `View Details >` (orange text).
     - Top Metrics Row: Due Alert Card (`₹2,800 Due in 3 days ⚠️ 16 Oct 2026`), Current Plan Card (`Spark Plan 01 Nov – 30 Nov 2026`), Last Paid Card (`₹800 01 Oct 2026`), Vertical Action Button (Emerald gradient `₹ Collect Money >`).
     - Bottom Row: 12-Month Timeline Strip (`Jan` to `Dec`) with plan amount badges and status indicators.
  6. **Stage 6 (Single-Viewport Zero-Scroll Physical Device Verification)**:
     - Full screen fits completely within the single mobile viewport on Xiaomi 11i with zero scrolling needed and ~40dp bottom breathing space above system navigation bar.
     - All 78 unit tests pass (`./gradlew testDebugUnitTest assembleDebug`).
## VERIFIED MILESTONE � TARGET MOCKUP IMPLEMENTATION (ASHWINI SINGH 5-STAGE WIDGET ARCHITECTURE)
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Device**: Xiaomi 11i / 21091116I (zxdada69gunb7ls4) via USB cable ADB
- **Screenshot Evidence**: 
  - [docs/reference/images/device_screen_v2_refined.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_v2_refined.png)
  - [docs/reference/images/device_screen_v2_scrolled_refined.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_v2_scrolled_refined.png)
- **Implementation Breakdown**:
  1. **Top Branding Bar**:
     - Hamburger menu icon.
     - BAD GYM title in emerald green (#047857) + Member Intelligence subtitle in slate grey.
     - Date selector pill: Oct 2026 ? with blue calendar icon and down chevron.
     - 3-dots circular menu button with soft subtle border.
  2. **Stage 1 (Hero Profile Card)**:
     - Circular avatar inside glowing sweep gradient ring with gold crown badge at top-left and neon green active dot at bottom-right.
     - Identity: Ashwini Singh (18sp black bold), BG305 | 23 yrs | Male.
     - Status pills: ? Prime Plan (golden yellow) + ? Active (emerald green).
     - Open horseshoe speedometer: 270� circular sweep arc, 75% bold green, 25 / 100, Days Left.
     - 3 Quick-Stat tiles: Joined 01 Sep 2026 (1 Month 10 Days), Goal Muscle Gain, Branch Varanasi.
  3. **Stage 2 (Body Stats Bento)**:
     - Header: Dumbbell icon + Body Stats � Latest vs Previous, View Details > (blue text).
     - 4 Bento tiles:
       - Weight: 69.5 kg, Weight, ? 2.5 kg, smooth green Bezier curve, VectorSync + Good Progress.
       - Muscle: 57.2 kg, Muscle, ? 1.4 kg, smooth purple Bezier curve with peak dot, VectorTrendUp + Improving.
       - Body Fat: 17.6%, Body Fat, ? 1.2%, smooth red Bezier curve, VectorTrendDown + Leaner.
       - BMI: 22.8, BMI, Normal green badge, smooth blue Bezier curve, VectorShield + Healthy.
  4. **Stage 3 (Workout Trend & Attendance)**:
     - Header: Bar chart icon + Workout Trend & Attendance, Oct 2026 ?, Details >.
     - 4 Colored threshold background bands: High 45m+ (green), Good 30-45m (cyan), Normal 15-30m (amber), Low <15m (red).
     - Horizontal dashed reference line at 28m with dark blue Avg 28m pill.
     - Floating Peak callout (+ Peak / 52m (10 Oct)) with thin vertical line dropping down to Day 10 bar.
     - Floating Lowest callout (? Lowest / 12m (16 Oct)) with thin vertical line dropping down to Day 16 bar.
     - 31 Attendance circular day chips (1..31) in Green (Present), Red (Absent), and Grey (Holiday).
     - Bottom legend row with custom vector icons: Present 24 Days (77%), Absent 3 Days (10%), Holiday 2 Days (6%), Avg 28 min, Peak 52 min, Lowest 12 min.
  5. **Stage 4 (Payment & Plan)**:
     - Header: Red wallet icon + Payment & Plan, amber pill ? Prime Plan (1 Month) >.
     - Item 1: ?2,800 (single line!), Paid � On Time,  1 Sep 2026.
     - Item 2: Next Due, 30 Sep 2026 (single line!), 25 Days Left.
     - Item 3: View\nReceipt with blue receipt icon.
     - Item 4: Collect\nMoney emerald button with white Rupee circle.
  6. **Stage 5 (Today's Activity)**:
     - Header: Purple lightning bolt + Today's Activity, Details >.
     - Check-in:  6:10 AM with soft green radial ambient bloom and clock icon.
     - Workout: 62 min with soft purple radial ambient bloom and dumbbell icon.
     - Check-out:  7:42 AM with soft red radial ambient bloom and clock icon.
  7. **Device & Verification Integrity**:
     - Built and tested with ./gradlew testDebugUnitTest assembleDebug (ALL unit tests pass).
     - Deployed and verified on physical Xiaomi 11i device (zxdada69gunb7ls4).
