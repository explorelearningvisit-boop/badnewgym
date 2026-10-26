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

## VERIFIED MILESTONE — ALL 7 GEMINI MASTER 3D ASSETS WITH TRUE ALPHA INTEGRATED
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Verification Evidence**:
  - Top Section & Hero: [master_avatar_top_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/master_avatar_top_live.png)
  - Mid Section (Attendance & Workout Trend): [master_mid_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/master_mid_live.png)
  - Bottom Section (Payment & Body Progress & Footer): [master_bottom_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/master_bottom_live.png)
- **Asset Ledger**:
  1. `avatar_ashwini.png`: 512×512 HD circular portrait cut-out from `Gemini_Generated_Image_cdlbtvcdlbtvcdlb.png`.
  2. `illustration_character_flex.png`: 450×512 true transparent 3D flexing character with gold stars from `Gemini_Generated_Image_68upxp68upxp68up.png`.
  3. `illustration_3d_attendance_calendar.png`: 396×512 true transparent 3D calendar with checkmark and smiling sun from `Gemini_Generated_Image_3nsn783nsn783nsn.png`.
  4. `illustration_3d_workout_dumbbells.png`: 446×512 true transparent 3D dumbbells with blue smiling cloud from `Gemini_Generated_Image_5wpfdj5wpfdj5wpf.png`.
  5. `illustration_3d_payment_wallet.png`: 473×512 true transparent 3D leather wallet with floating gold coins from `Gemini_Generated_Image_f5upvcf5upvcf5up.png`.
  6. `illustration_3d_body_scale.png`: 418×512 true transparent 3D weight scale and smiling lavender cloud from `Gemini_Generated_Image_nienb9nienb9nien.png`.
  7. `illustration_3d_footer_dumbbell.png`: 512×490 true transparent 3D purple dumbbell banner graphic from `Gemini_Generated_Image_i8m9p9i8m9p9i8m9.png`.

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
## VERIFIED MILESTONE  TARGET MOCKUP IMPLEMENTATION (ASHWINI SINGH 5-STAGE WIDGET ARCHITECTURE)
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
     - Open horseshoe speedometer: 270 circular sweep arc, 75% bold green, 25 / 100, Days Left.
     - 3 Quick-Stat tiles: Joined 01 Sep 2026 (1 Month 10 Days), Goal Muscle Gain, Branch Varanasi.
  3. **Stage 2 (Body Stats Bento)**:
     - Header: Dumbbell icon + Body Stats  Latest vs Previous, View Details > (blue text).
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
     - Item 1: ?2,800 (single line!), Paid  On Time,  1 Sep 2026.
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

## MILESTONE VERIFIED: MASTER 2x2 BENTO DASHBOARD (100% FIDELITY)
- **Target Specification**: Master Reference Mockup (Hero Profile Card + 2x2 Bento Intelligence Grid)
- **Components Implemented & Validated**:
  1. HeroProfileCard: Avatar with cyan/mint conic gradient halo & crown badge at 1 o'clock; Active Member & Prime Plan glass status pills; 25 Days Left expiry bar (~75%); Quote Bubble 'Better Than Yesterday' with bicep emoji; 4 quick stats (Height, Weight, BMI, Goal).
  2. AttendanceBentoCard: 85% hero metric, +12%, 30 Days dropdown, 30 attendance bars, Y/X axes, 3-stat footer with custom Canvas pie chart.
  3. WorkoutTrendBentoCard: 47 min hero metric, +8%, 30 Days dropdown, 30 cyan-blue workout bars, Y/X axes, 3-stat footer.
  4. PaymentBentoCard: Rs 2,800 + Due badge, unclipt 'View Receipt' button, 12-month capsule bars in hollow tracks, Y/X axes, 3-stat footer.
  5. BodyProgressBentoCard: 4 three-line metric chips (Weight, Muscle Mass, Body Fat, Waist), 12-month multi-line chart with 4 smooth solid Bezier splines & circular dots, warm gradient area, Dual Y-axes (kg / %), 12-month evenly spaced X-axis, 3-stat footer.
- **Verification Evidence**:
  - Screenshot: docs/reference/images/final_target_mockup_verified.png
  - Device: Xiaomi 11i (zxdada69gunb7ls4)
  - Unit Tests: ./gradlew.bat testDebugUnitTest PASSED
  - Build: ./gradlew.bat assembleDebug PASSED

## MILESTONE VERIFIED: ATTENDANCE ENHANCEMENT (REMOVED BAR GRAPH -> CIRCULAR GAUGE & CALENDAR)
- **User Request**: 'integrate best of best and remove bar graph' with reference designs.
- **Key Deliverables**:
  1. Removed generic 30-day vertical bar graph from AttendanceBentoCard.
  2. Integrated Radiant Circular Donut Gauge (85% Attendance Rate with emerald sweep arc & golden energy highlight tip).
  3. Integrated Frosted Present Stat Card (18 Present with green checkmark).
  4. Integrated Monthly Calendar Grid (Su Mo Tu We Th Fr Sa, 31 circular day chips in glowing mint green for Present, coral red for Absent, slate grey for Gym Off, and clean white for Rest).
  5. Month selector pill: < Oct 2026 >.
  6. Created full interactive AttendanceDetailsDialog with larger touch targets and interactive day inspection info card.
- **Verification Evidence**:
  - Main Dashboard Screenshot: docs/reference/images/attendance_calendar_verified.png
  - Interactive Dialog Screenshot: docs/reference/images/attendance_dialog_verified.png
  - Unit Tests: ./gradlew.bat testDebugUnitTest PASSED
  - Build: ./gradlew.bat assembleDebug PASSED

## MILESTONE VERIFIED: 100% COMPLETE MASTER 9:19 VERTICAL STORYBOARD WITH 3D ASSETS
- **User Request**: User provided master mobile reference design (tall 9:19 storyboard with 7 complete vertical sections, 3D character/illustrations, and rich intelligence cards).
- **Architecture**:
  - Full-width, single-column vertical scroll flow (`MasterStoryboardDashboard.kt`) giving every card ample horizontal space for uncompromised visualization.
  - Implemented 6 custom high-resolution 3D visual assets extracted directly from the design:
    1. `illustration_character_flex.png`: Anime boy flexing bicep with dumbbells.
    2. `illustration_3d_attendance_calendar.png`: 3D calendar with green checkmark & smiling sun on `#EFFEF2`.
    3. `illustration_3d_workout_dumbbells.png`: 3D dumbbells with smiling blue cloud on `#E5F1FB`.
    4. `illustration_3d_payment_wallet.png`: 3D wallet with golden coins on `#FEEDED`.
    5. `illustration_3d_body_scale.png`: 3D scale with smiling purple cloud on `#E7D6F5`.
    6. `illustration_3d_footer_dumbbell.png`: 3D purple dumbbell with star badge on `#5D22B6`.
- **Key Sections Implemented**:
  1. **MasterHeroHeader**:
     - Deep purple gradient container (`#2D0F62` to `#3B1266`).
     - BAD GYM branding with Member Intelligence subtitle and date pill (`📅 02 Oct 2026`).
     - "Stronger / Happier / You! 😃" typography with lime green accent and yellow flourish arrow (`➰⤴`).
     - Ashwini Singh profile avatar with green outer ring and golden crown badge.
     - Status pills: `● Active Member` and `👑 Prime Plan`.
     - Speech bubble: `"Better Than Yesterday" 💪`.
     - 3D flexing anime character.
     - Expiry bar: `25 Days Left (4 Aug 2026 – 12 Nov 2026)` with 75% progress bar.
     - Floating 4-Metric Dock: Height (178 cm), Weight (69.5 kg ↓ 2.5 kg), BMI (21.9 Normal), Goal (Lean Muscle).
  2. **MasterAttendanceCard** (Mint Green `#EFFEF2`):
     - 3D Calendar illustration.
     - 85% hero stat (18 of 21 days) with `↑ 12%` pill.
     - 3-column legend: Present 18 (86%), Absent 3 (14%), Gym Off 2 (-).
     - 30-day vertical bar chart with Y/X axes.
     - 4 bottom stat cards: Current Streak (6 Days), Best Streak (12 Days), Attendance Rate (🟢 85%), Total Visits (24 Days).
  3. **MasterWorkoutTrendCard** (Sky Blue `#E5F1FB`):
     - 3D Dumbbells illustration.
     - 47 min hero stat (Avg per session) with `↑ 8%` pill.
     - Legend column: Workout (24 days), No Workout (6 days).
     - 30-day vertical workout bars with Y-axis (0m, 45m, 90m) and X-axis (1..30).
     - 4 bottom stat cards: Total Workouts (24), Best Session (92 min), Current Streak (8 Days), Weekly Avg (3.4 days).
  4. **MasterPaymentCard** (Coral Pink `#FEEDED`):
     - 3D Wallet illustration.
     - ₹2,800 hero stat with `! Due` badge.
     - Next Due Date (12 Nov 2026, 25 days left) and `View Receipt` pill button.
     - 12-month capsule bar chart (Jan..Dec) with hollow track backgrounds and colored status capsules.
     - 4-item legend: Paid 8 months, Partial 2 months, Due 2 months, No Data -.
     - 4 bottom stat cards: Total Paid (₹12,600), Avg / Month (₹1,050), Next Due (12 Nov 2026), On Time (🟢 80%).
  5. **MasterBodyProgressCard** (Soft Lavender `#E7D6F5`):
     - 3D Scale illustration.
     - 4-item legend: Weight (kg), Muscle (kg), Body Fat (%), Waist (cm).
     - 12-month multi-line spline chart with 4 smooth Bezier curves and circular data dots.
     - 8 metric cards (2 rows of 4): Weight, Muscle Mass, Body Fat, Waist, Strength, Endurance, Overall Progress, Goal.
  6. **MasterFooterBanner** (Deep Purple `#5D22B6`):
     - 3D purple dumbbell.
     - "Consistent Steps, Brighter Days 彡" with yellow highlights.
     - "Keep Going! ➔" neon yellow pill button.
- **Verification Evidence**:
  - Live Top Screenshot: `docs/reference/images/master_top_refined.png`
  - Live Bottom Screenshot: `docs/reference/images/master_bottom_refined.png`
  - Target Physical Device: Xiaomi 11i (`zxdada69gunb7ls4`)
  - Unit Tests: `./gradlew.bat testDebugUnitTest` PASSED (code 0)
  - Compilation & Packaging: `./gradlew.bat assembleDebug` PASSED (code 0)

