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

## VERIFIED MILESTONE — THREE-SECTION REFERENCE-DRIVEN MEMBER INTELLIGENCE (`media_1793376151174_5cd06942.jpg`)
- **Status**: VERIFIED & DEPLOYED TO PHYSICAL DEVICE
- **Architecture**: Exactly Three Sections:
  1. **Section 01: Member Intelligence Pass**:
     - 135° Linear Gradient Header (`#7428E8` $\to$ `#D92BAF` $\to$ `#F34B9A` $\to$ `#FF963C`)
     - Left: 3D Gold Crown 👑, "BAD" (White) + "GYM" (Yellow `#FFD45C`), "MEMBER INTELLIGENCE", "DISCIPLINE BUILDS FREEDOM"
     - Middle: Translucent glowing stadium capsule (`CHECK-IN Today • 08:49 AM`) with fingerprint icon
     - Right: White stadium capsule with green leather wallet (`₹2,800` in bold red, `03 Nov 2026`, blue chevron `›`)
     - Member Profile: Ashwani Kumar Singh Srivastav, BG215 • 28 yrs • Male, "Discipline Builds Freedom", `[• Active]`, 09 Oct 2026 – 07 Nov 2026 (30 Days Plan), 3-dots action menu
     - 4 Metric Pods: Height (5'9" with cyan ruler), Weight (69.5 kg $\downarrow 2.5\text{ kg}$ with purple scale), BMI (22.8 Normal with amber chart), Goal (Fat Loss with orange flame)
     - Membership Progress: Ultra-thin 5dp gradient progress bar (`#FF9D32` $\to$ `#EF36A8`), 22/30 days used, 8 days left, 73% completion
     - Outer Card Border: 1.5dp multi-color gradient border (`#E84BAF`, `#8B54F5`, `#45C9F5`, `#FFC45B`)
  2. **Section 02: Quick Navigation**:
     - 4 equal-width 50dp stadium capsules with spherical icons, chevron indicators, and mini 3.5dp progress bars:
       - Attendance (81%) — Emerald theme
       - Workout (67m) — Purple theme
       - Payment (₹2,800 with red `!` alert circle) — Orange theme
       - Body (69.5 kg) — Blue theme
  3. **Section 03: Attendance Intelligence**:
     - **Module A (Calendar Grid)**:
       - Header: Calendar icon, "Attendance", "Daily check-ins • Mon – Sun", compact sponsored slot (`Stay Fit with MYPROTEIN [AD]`), month selector (`‹ Oct 2026 ›`)
       - Left: 81% Attendance Rate circular donut gauge, 25/31 days, 4 holidays, status breakdown (Present 23, Late 2, Absent 2, Holiday 4)
       - Right: 7-column calendar grid with round status circles (Present `#078A69`, Late `#EAA12E`, Absent `#E53945`, Holiday `#E9EEF0`)
     - **Module B (12-Month Attendance Trend)**:
       - Header: Trend icon, "Attendance trend", "Attendance rate by month", compact sponsored slot (`blinkit Healthy Snacks [AD]`), `12 months ⌵`
       - 12-month vertical bars with values on top
       - 3 Dynamic Dashed Reference Lines drawn across canvas:
         - **Max 97%** (Green dashed line)
         - **Avg 81%** (Amber dashed line)
         - **Min 58%** (Red dashed line)
       - Compact legend below chart: `--- Max 97%   --- Avg 81%   --- Min 58%`
- **Verification Evidence**:
  - Full screenshot: `docs/reference/images/reference_v3_live_verified.png`
  - Section 1 crop: `docs/reference/images/live_sec1_member_pass.png`
  - Section 2 crop: `docs/reference/images/live_sec2_quick_nav.png`
  - Section 3A crop: `docs/reference/images/live_sec3a_calendar.png`
  - Section 3B crop: `docs/reference/images/live_sec3b_trend.png`
  - Build & Test: `BUILD SUCCESSFUL in 8m 34s (unit tests)` & `BUILD SUCCESSFUL in 7m 50s (assembleDebug)`
  - Physical Device: Xiaomi 11i 5G (`zxdada69gunb7ls4`) live installation successful.

## VERIFIED MILESTONE — 1:1 DITTO CLONE OF TOP HEADER COMPONENT (`media_1793346029417_a6bb6f74.png`)
- **Status**: VERIFIED & DEPLOYED TO PHYSICAL DEVICE
- **Commit**: `c6e9dea`
- **Device Tested**: Physical Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400)
- **Live Device Screenshot Evidence**:
  - Full Live Screen: [docs/reference/images/device_screen_header_ditto_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_header_ditto_live.png)
  - Crop Top Header: [docs/reference/images/crop_header_ditto_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/crop_header_ditto_live.png)
- **Key Deliverables Implemented & Verified**:
  1. **Left Branding Section**:
     - 3D Gold Crown 👑 with royal blue gems.
     - Line 1: `BAD ` in black/navy `#0F172A` + `GYM` in emerald green `#059669`.
     - Line 2: `MEMBER INTELLIGENCE` in vibrant royal blue `#2563EB`.
     - Line 3: `DISCIPLINE BUILDS FREEDOM` in slate/grey `#64748B`.
  2. **Middle Capsule (CHECK-IN Active)**:
     - Stadium pill shape (`RoundedCornerShape(50.dp)`) with rich emerald green gradient (`#059669` $\to$ `#047857` $\to$ `#065F46`).
     - Circular mint fingerprint badge with `Icons.Default.Fingerprint`.
     - `CHECK-IN` in bold white typography (`11sp Black`).
     - `Today • 08:49 AM` + neon green `[Active]` pill badge right next to it.
  3. **Right Capsule (Payment Due ₹800)**:
     - Pure white stadium capsule (`RoundedCornerShape(50.dp)`) with soft sky-blue border `#BAE6FD`.
     - Green leather wallet rounded badge with embossed white Rupee symbol `₹`.
     - 3-line column: `Payment Due` (slate), **`₹800` in BOLD RED `#DC2626`**, and `12 Oct 2026` (slate).
     - Royal blue chevron `›` in `#2563EB`.
  4. **Build & Test**:
     - Unit tests pass with `BUILD SUCCESSFUL`.
     - Verified visually on physical device.

## VERIFIED MILESTONE — PINNED MEMBER CARD ARCHITECTURE, STADIUM PILL 4-TABS (IMAGE 4) & ZERO DRIFT VERIFICATION
- **Status**: VERIFIED & DEPLOYED TO PHYSICAL DEVICE
- **Commit**: `8426e54`
- **Device Tested**: Physical Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400)
- **Live Device Screenshot Evidence**:
  - Pinned Top Member Card & Menu Row: [docs/reference/images/device_screen_pinned_perfect.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_pinned_perfect.png)
  - Scrolled Lower Content: [docs/reference/images/device_screen_pinned_scrolled.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_pinned_scrolled.png)
  - Tab 1 Workout: [docs/reference/images/device_screen_verified_tab1_workout.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_verified_tab1_workout.png)
  - Tab 2 Payment: [docs/reference/images/device_screen_verified_tab2_payment.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_verified_tab2_payment.png)
  - Tab 3 Body: [docs/reference/images/device_screen_verified_tab3_body.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_verified_tab3_body.png)
- **Key Deliverables Implemented & Verified**:
  1. **Pinned Member Card & 4-Tab Dock Architecture**:
     - `UnifiedBadGymMemberCard` and `MenuRow4Tabs` are locked stationary at the top of the dashboard inside a non-scrolling parent container.
     - Only the expanded active widget data inside `LazyColumn(modifier = Modifier.weight(1f))` scrolls.
     - Verified with automated pixel-by-pixel differential test: **Exactly 0 pixels drift** on the member card and tabs during scrolling, while 10,417 sample pixels actively scrolled in the lower viewport.
  2. **Menu Row 4-Tab Stadium Pills (`media_1793300075801_5ac48325.jpg` Clone)**:
     - 52dp stadium capsule geometry (`RoundedCornerShape(50.dp)`).
     - Compact 3-element stack per tab: 26dp 3D spherical icon, Title (`10.5sp Bold`) with alert badge `!`, Value (`12.5sp Black`) + `›` chevron, and 3.5dp mini horizontal progress bar.
     - No clipped text or awkward wrapping across all tabs.
     - Signature active theme glows: Orange glow with exclamation badge `!` for Payment, Emerald for Attendance, Purple for Workout, and Sky Blue for Body.
  3. **Sponsored Advertisement Section**:
     - `BadGymSponsoredPromoCard` for Bad Nutrition Labs 100% Whey Gold (27g Isolate, code: `BADPUMP`) with 3D dumbbell badge, 25% OFF pill, and `Claim ➔` CTA button placed in scrollable area before motivation banner.
  4. **Payment & Body Progress Polish**:
     - Paid badge `Paid ₹500` formatted on a single line with `maxLines = 1, softWrap = false`.
     - Unit tests pass with `BUILD SUCCESSFUL`.

## VERIFIED MILESTONE — 1:1 DITTO CLONE FINAL MASTER DASHBOARD SPECIFICATION
- **Status**: VERIFIED & DEPLOYED TO PHYSICAL DEVICE
- **Commit**: `4089160`
- **Device Tested**: Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400)
- **Live Device Screenshot Evidence**: [docs/reference/images/final_perfect_live.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/final_perfect_live.png)
- **Key Deliverables Matching Final Master Specification (`media_1793297706074_a3a169e8.jpg`)**:
  1. **Component 1 (Top Header)**: Sparkles ✨ + Crown 👑 + BAD GYM + MEMBER INTELLIGENCE; emerald-cyan neon gradient capsule with Fingerprint, CHECK-IN, Today • 08:49 AM, Active green pill; Paid Quick Badge on right (wallet icon, Paid ✔, ₹500, 12 Oct 2026, ›).
  2. **Component 2 (Profile Section)**: 72dp avatar with multi-color radiant halo ring + star pin badge, Ashwani Kumar Singh Srivastav ✏️, BG215 | 28 yrs | Male, “Discipline Builds Freedom” with [✔ Active] green pill right next to quote, 01 Aug – 31 Aug 2026, crown watermark silhouette with purple script "Stronger 
 Healthier 
 Happier".
  3. **Component 3 (Stats Row - 4 Pods)**: Moved to Component 3 position matching master layout: 5'9" Height, 74.1 kg Weight, 23.5 [Normal] BMI, Fat Loss Goal >.
  4. **Component 4 (Full-Width Days Progress Bar)**: Full-width electric cyan card with calendar icon, 22 / 30 Days Left, glowing neon mint-to-cyan progress bar, and 73% capsule pill on right.
  5. **Component 5 (Menu Row - 4 Horizontal Capsule Tabs)**: Horizontal pill layout (circular icon + title & bold value + › chevron) for Attendance 90%, Workout 70m, Payment ₹800, and Body 74.1 kg.
  6. **Component 6 (Attendance Section with Top Legend)**: 40dp Aurora Wave Header Bar `< Oct 2026 >`, 5-chip Top Legend row right below header (● Present 27 (90%), ● Absent 1 (3%), ● Holiday 1 (3%), ● Sunday 1 (3%), ○ Not Member 0 (0%)), 3D Calendar with 90% 27/30 Days, and 7-column calendar grid with 31 circular day tokens.
  7. **Component 7 (Monthly Attendance Trend with Reference Lines & KPI Pods)**: 38dp Trend Wave Header Bar `12 Months ˇ`, 3 Top KPI Pods (🏆 Best Day 29d (Jul) 95%, 📈 Average 24.6d 82%, ⬇️ Min Day 18d (Mar) 60%), Y-axis percentage labels (100%, 75%, 50%, 25%, 0%), 3 horizontal dashed reference lines (Red Max 95%, Yellow-dotted Average 82%, Blue Min 60%), 12 Candle Bars with proportional data heights and exact baseline alignment, and Bottom Reference Legend.
  8. **Component 8 (Bottom Motivation Banner)**: 50dp royal purple-to-blue gradient banner with 3D metallic dumbbell, DISCIPLINE TODAY Consistent Steps, Brighter Days ☀️, and golden CTA Keep Going! ➔.
  9. **Zero-Scroll Single-Viewport Fit**: Compact proportional heights ensuring all 8 master components are 100% visible in a single viewport on physical device with zero scrolling and generous breathing room.

## VERIFIED MILESTONE — 8-COMPONENT MASTER INFOGRAPHIC BLUEPRINT, DYNAMIC CARD HEADERS & ATTENDANCE BAR BASELINE
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `b38f360`
- **Device Tested**: Xiaomi 11i 5G (`zxdada69gunb7ls4`)
- **Key Deliverables**:
  1. **8 Master Components Integrated**:
     - Component 1: Top Header Component (glass gradient pill, dynamic activity badge, plan badge).
     - Component 2: Profile Section (72dp avatar with concentric halo ring + star pin, Ashwani Kumar Singh Srivastav, 01 Aug – 31 Aug 2026, crown watermark silhouette).
     - Component 3: Progress & Payment Row (22/30 Days Left with 73% cyan/mint bar + 8 Days Left pill, and Paid ₹500 08:49 AM card).
     - Component 4: Stats Row Compact (4 pods: 5'9" Height, 74.1 kg Weight, 23.5 [Normal] BMI, Fat Loss Goal >).
     - Component 5: Menu Row 4 Tabs (Attendance 90% >, Workout 70m >, Payment ₹800 >, Body 74.1 kg > with non-clipped layout).
     - Component 6: Attendance Section (3D Calendar + 90% 27/30 Days, 7-column calendar grid Mon-Sun with 31 days circular tokens, horizontal legend chips).
     - Component 7: Monthly Trend Section (12-month bar chart with Jan 100% at 58dp max, Oct 90% at 42dp, locked baseline alignment, ✔ Min and Best badges).
     - Component 8: Bottom Motivation Banner (3D dumbbell, "Consistent Steps, Brighter Days ☀️", Keep Going! ➔ CTA).
  2. **Dynamic Header & Status Switching**:
     - Supports `CHECK-IN`, `CHECK-OUT`, `PAYMENT` (with Done, Pending, Failed statuses), `TRAINER CHECK-IN`, and `MACHINE FAULT`.
     - When payment is due, automatically defaults to `PAYMENT` with `PAYMENT DUE` and `Pending` badge.
     - Tapping activity badge cycles dynamically across states with matching highlight colors.
  3. **Visual Evidence**:
     - Verified Attendance Screen: `docs/reference/images/true_live.png`
     - Verified Payment Screen: `docs/reference/images/badgym_fullscreen.png`
     - Verified Check-Out Screen: `docs/reference/images/cycle_header1.png`
     - Verified Trainer Check-In Screen: `docs/reference/images/cycle_header2.png`
     - Verified Machine Alert Screen: `docs/reference/images/cycle_header3.png`

## VERIFIED MILESTONE — WIDGET THEMING, MONTHLY TRENDS, MUSCLE GRAPHICS & DATA-DRIVEN PROGRESS FILL
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `e38c592`
- **Device**: Xiaomi 11i 5G / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB (1080x2400)
- **Live Device Screenshot Evidence (All 4 Tabs 1:1 Pixel-Perfect in Single Viewport)**:
  - Attendance Tab: [docs/reference/images/device_screen_attendance_updated.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_attendance_updated.png)
  - Workout Tab: [docs/reference/images/device_screen_workout_now.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_workout_now.png)
  - Payment Tab: [docs/reference/images/device_screen_payment_updated.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_payment_updated.png)
  - Body Progress Tab: [docs/reference/images/device_screen_body_polished.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_body_polished.png)
- **Key Enhancements Completed**:
  1. **Monthly Attendance Trend Outer Border & Background**:
     - Bordered with `#A7F3D0` (1.2dp) and filled with soft mint background (`#F0FDF4`, 13dp corners).
     - Fixed "No Member" bar text clipping by formatting as `"No\nMem"` at `7.5.sp` with `8.sp` line height inside the 20dp box.
     - Added `"★ Best"` badge above Dec (100%) and `"▼ Min"` badge above Mar (60%).
     - Added present days numbers (`30`, `24`, `18`, `27`, `21`, `29`, etc.) centered in white inside each candle bar.
     - Scaled candle heights proportionally with data: 60% -> 17dp, 70% -> 20dp, 75% -> 21dp, 80% -> 23dp, 85% -> 25dp, 90% -> 27dp, 95% -> 29dp, 100% -> 32dp.
  2. **Comprehensive Category Theming Across All Widgets**:
     - Attendance Card: Mint Emerald theme (`#F8FEFA` surface, `#A7F3D0` border).
     - Workout Card: Ocean Sky Blue theme (`#F0F9FF` surface, `#BAE6FD` border).
     - Payment Card: Rose Coral theme (`#FFF1F2` surface, `#FECDD3` border).
     - Body Progress Card: Lavender Amethyst theme (`#FAF5FF` surface, `#DDD6FE` border).
  3. **Body Progress Trend Y-Axis Truncation Fix**:
     - Increased Y-axis label column and Canvas height from `29.dp` to `38.dp` inside `44.dp` container.
     - Formatted labels as `"76"`, `"72"`, `"68"`, `"64"`, eliminating number truncation on `"64"`.
  4. **Workout Distribution Transparent Muscle Graphics**:
     - Generated and integrated 6 high-res transparent anatomical PNGs (`ic_muscle_chest.png`, `ic_muscle_back.png`, `ic_muscle_shoulder.png`, `ic_muscle_biceps.png`, `ic_muscle_triceps.png`, `ic_muscle_legs.png`).
     - Displayed with athletic silhouettes, V-taper torsos, and targeted colored muscle group highlights.
     - Header polished to "Distribution" and "This Month ⌵" with generous spacing.
  5. **Spacious Activity Type Pod**:
     - Expanded donut rings to `28.dp` with `2.6.dp` stroke and larger `13.dp` icons.
     - Increased typography to `10.sp` / `11.sp` / `9.sp` and padding to `horizontal = 7.dp, vertical = 5.dp`.
  6. **Workout Monthly Pattern Top & Low Badges**:
     - Added `"★ Top"` callout over peak month (Jul 22d) and `"▼ Low"` callout over minimum month (Mar 6d).
  7. **Data-Driven Member Pass Progress Bar Fill**:
     - Updated Row 3 Days Left capsule to dynamically fill proportional to actual data: `92 / 180 Days Left (51%)`.
     - 51% filled with vibrant emerald gradient (`#10B981` to `#059669`) over a deep emerald green track (`#064E3B`).
  8. **Strict Zero-Scroll Single-Viewport Retention**:
     - All 4 tabs tested on physical Xiaomi 11i 5G (1080×2400) via ADB; 100% single viewport with zero vertical scrolling.

## VERIFIED MILESTONE — PRODUCTION ULTRA-CLEAR SCALED TYPOGRAPHY & ZERO-SCROLL SINGLE-VIEWPORT DASHBOARD
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Device**: Xiaomi 11i 5G / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB (1080x2400)
- **Live Device Screenshot Evidence (All 4 Tabs 1:1 Pixel-Perfect in Single Viewport)**:
  - Attendance Tab: [docs/reference/images/final_attendance.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/final_attendance.png)
  - Workout Tab: [docs/reference/images/final_workout.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/final_workout.png)
  - Payment Tab: [docs/reference/images/final_payment.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/final_payment.png)
  - Body Progress Tab: [docs/reference/images/final_body.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/final_body.png)
- **Key Enhancements Completed**:
  1. **Systematic Font Scaling Across Every Expanded Card**:
     - *Attendance Card*: 90% Hero metric scaled to `36.sp` Black, 27/30 Days to `14.5.sp` Black, 30-day circles to `20.dp` with `10.sp` Bold numbers, Monthly Trend to `15.sp` / `10.5.sp` Bold months / `9.5.sp` bar labels, Key Insights to `16.5.sp` Black / `10.5.sp` Bold.
     - *Workout Trend Card*: 70 min Avg Session scaled to `35.sp` Black / `13.5.sp` Bold, KPI Pods to `15.sp` Black / `10.5.sp` Bold, 30-day canvas to `9.5.sp` Y-axis / `10.sp` day numbers / `10.5.sp` legend items, Monthly Pattern to `10.sp` bar labels / `10.5.sp` Bold months.
     - *Payment Card*: ₹800 scaled to `35.sp` Black, Pending badge to `12.5.sp` Black, Due date to `13.sp` Bold, Action buttons to `13.sp` Black / `11.5.sp` Bold, Stacked bars to `10.sp` totals / `10.5.sp` months, Bottom summary metrics to `17.5.sp` Black.
     - *Body Progress Card*: 69.5 kg scaled to `33.sp` Black / `13.sp` Bold, Goal to `13.5.sp` Black, 4 Goal Grid cards to `15.5.sp` Black / `10.5.sp` Bold, Metric/Time chips to `10.sp` / `9.5.sp`, Bottom pods to `12.sp` Black / `10.5.sp` percentages.
     - *Footer Banner*: Consistent Steps Brighter Days to `14.5.sp` Black, Keep Going button to `12.5.sp` Black.
  2. **Zero-Scroll Single-Viewport Packaging**: Entire dashboard (Hero Member Pass + Sticky Tabs + Active Widget + Footer Banner) packed into ~710dp, fitting 100% inside the viewport without vertical scrolling.
  3. **Deep UX/UI Polish & Bug Fixes**:
     - Fixed "Peak Session" clipping in Workout KPI pod by setting adaptive font sizing and tight padding.
     - Separated 30-day workout volume bars from right guideline text by expanding label margin to `48.dp`.
     - Calibrated July 4-block and December 3-block bar heights in Payment Trend chart to eliminate compression and give clear visibility to the "Jul" label.
     - Prevented "12M" time chip line-wrapping in Progress Trend with `softWrap = false` and calibrated chip padding.

## VERIFIED MILESTONE — LUXURY EXECUTIVE MEMBER PASS & SCALED ULTRA-CLEAR DASHBOARD
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `d821566`
- **Target Images**: `media_1793276747772_0ed434f5.png` (Authoritative Executive Dashboard Design)
- **Device**: Xiaomi 11i 5G / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB (1080x2400)
- **Live Device Screenshot Evidence (All 4 Tabs 1:1 Pixel-Perfect in Single Viewport)**:
  - Attendance Tab: [docs/reference/images/verified_attendance_luxury.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/verified_attendance_luxury.png)
  - Workout Tab: [docs/reference/images/verified_workout_luxury.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/verified_workout_luxury.png)
  - Payment Tab: [docs/reference/images/verified_payment_luxury.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/verified_payment_luxury.png)
  - Body Progress Tab: [docs/reference/images/verified_body_luxury.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/verified_body_luxury.png)
- **Key Enhancements Completed**:
  1. **Luxury Executive Hero Pass (`BadGymMemberPassCard`)**:
     - Converted background from dark green to crisp, luxury frosted crystal white (`#FFFFFF`) with subtle border (`#E2E8F0`) and 22dp corners.
     - Header Row: Dark green circular badge with gold dumbbell, `BAD GYM` in dark text and emerald accent, `MEMBER PASS` subtitle, `DISCIPLINE BUILDS FREEDOM` motto and 3-dots menu button (`⋮`).
     - Member Profile: Rohit Kumar Singh avatar (`avatar_rohit.png`) with silver/ice halo ring and dark navy star badge (`★`).
     - Details: `Rohit Kumar Singh` (19.5sp Black), `BG215 | 28 yrs | Male` (12.5sp Bold), `“Discipline Builds Freedom”` (12sp Bold), green calendar icon + `01 Aug 2026 – 31 Jan 2027`.
     - Membership Badges: `⭐ Silver / 6 Months` container chip + `✓ Active` emerald green capsule pill.
     - Days Left Capsule: Full-width rich emerald gradient (`#05654B` to `#044E3B`) `92 / 180 Days Left` with white chevron (`>`).
     - 4 Biometric KPI Pods: Clean white container card with 4 pods separated by vertical dividers:
       - 📐 `5'9" Height`
       - ⚖️ `74.1 kg Weight`
       - 📊 `23.5 BMI`
       - 🔥 `Fat Loss Goal`
  2. **Zero Clutter**: Removed top horizontal tier chips row from default view so the executive card begins cleanly below the status bar.
  3. **Scaled Typography Across Whole Dashboard**: All titles, metrics, labels, and badges enlarged with high-contrast `#0F172A` / `#059669` / `#E11D48` / `#0284C7` for ultra-clear legibility.
  4. **Single-Viewport Zero-Scroll Architecture**: Verified across all 4 tabs that Hero Card + Sticky Menu Bar + Active Widget + Bottom Footer Banner fit 100% within a single screen without vertical scrolling.

## VERIFIED MILESTONE — COMPACT SINGLE-VIEWPORT DASHBOARD & ULTRA-CLEAR TYPOGRAPHY
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `1a4aef4`
- **Device**: Xiaomi 11i 5G / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB (1080x2400)
- **Live Device Screenshot Evidence (All 4 Tabs Fit in 1 Viewport With Zero Scrolling)**:
  - Attendance Tab (Zero-Scroll): [docs/reference/images/device_screen_attendance_compact.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_attendance_compact.png)
  - Workout Tab (Zero-Scroll): [docs/reference/images/device_screen_workout_compact.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_workout_compact.png)
  - Payment Tab (Zero-Scroll): [docs/reference/images/device_screen_payment_perfect_final.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_payment_perfect_final.png)
  - Body Progress Tab (Zero-Scroll): [docs/reference/images/device_screen_body_perfect_final.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_body_perfect_final.png)
- **Key Enhancements**:
  1. **Zero Dead Space**: Removed hardcoded `450.dp` heights and `SpaceBetween` stretching from Payment and Body Progress cards. Replaced with natural `spacedBy(5.dp)` to wrap cleanly.
  2. **Compacted Hero Pass (`BadGymMemberPassCard`)**: Outer padding tightened to `9.dp`, column spacing to `7.dp`, avatar halo `68.dp`, progress bar `7.dp`, bottom pods `7.dp x 5.dp`. Saved ~40dp of vertical space.
  3. **Enlarged Typography**: Name `19.sp` Black, Plan `12.5.sp` Bold, Validity `12.sp` Bold, Days Left pill `11.5.sp` Black, Tier chips `11.5.sp` Bold.
  4. **Compact Sticky Menubar**: Dock height `42.dp`, tab labels `11.5.sp` Bold/Black, badge numbers `11.sp` Black.
  5. **Compact Workout Trend**: 30-day daily volume canvas height `72.dp`, Y-axis & guidelines `8.5.sp` Bold, day numbers `9.sp` Black, 4 quick KPI pods values `14.sp` Black, monthly pattern `44.dp` with `9.5.sp` Bold months.
  6. **Compact Payment Card**: 3D wallet `50.dp`, amount `₹800` `32.sp` Black, 3 action buttons height `38.dp` with `11.sp` bold text, stacked chart `74.dp`, bottom metrics `16.sp` Black.
  7. **Compact Body Progress Card**: 3D scale `48.dp`, current weight `28.sp` Black, 4 goal cards values `14.sp` Black, progress spline `44.dp`, bottom muscle & activity pods compact with `11.sp` headers and `9.5.sp` bold metrics.
  8. **Compact Footer Banner**: Vertical padding `7.dp`, 3D dumbbell `36.dp`, text `13.5.sp` Black, action button `11.5.sp` Black.

## VERIFIED MILESTONE — 1:1 PIXEL-PERFECT MASTER ATTENDANCE CARD IMPLEMENTED & VERIFIED
- **Status**: VERIFIED & DEPLOYED TO DEVICE
- **Commit**: `7acf112`
- **Device**: Xiaomi 11i / 21091116I (`zxdada69gunb7ls4`) via USB cable ADB
- **Target Image**: `media_1793271128777_8bf1743c.jpg` (`docs/reference/images/attendance_trend_target.jpg`)
- **Screenshot Evidence**: 
  - Attendance Tab Verified: [docs/reference/images/device_screen_attendance_perfect.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_attendance_perfect.png)
  - Workout Tab Live: [docs/reference/images/device_screen_workout_live_now.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_workout_live_now.png)
  - Payment Tab Live: [docs/reference/images/device_screen_payment_live_now.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_payment_live_now.png)
  - Body Progress Tab Live: [docs/reference/images/device_screen_body_live_now.png](file:///c:/Users/User/AndroidStudioProjects/badnewgym/docs/reference/images/device_screen_body_live_now.png)
- **Key Deliverables Completed**:
  1. **Dual Floating Card Architecture**: Card 1 (30-Day Attendance Dashboard) + Card 2 (Key Insights) separated by 8dp gap.
  2. **Card 1 Hero & Legend Pod**: 3D Calendar illustration (`illustration_3d_attendance_calendar`), green badge with star + "Attendance" title, "Oct 2026" with circular `‹` and `›` chevrons, 35sp bold emerald **90%**, **27 / 30 Days**, and right container legend (Present 27, Absent 1, Holiday 1, Sunday 1, Not a Member 0).
  3. **30-Day Numbered Attendance Circles**: Two rows of 15 circles (1..15, 16..30) with exact status colors (Present emerald, Late amber, Absent red, Sunday soft gray, Upcoming white outline).
  4. **Monthly Attendance Trend**: Mini 3-bar icon + "Monthly Attendance Trend" + "12 Months ⌵" pill, 12 monthly bars with Oct highlighted in green border box, dashed outline for non-member months, continuous baseline connecting all bars.
  5. **Card 2 Key Insights Pods**: Purple lightbulb icon badge, 3 insight pods (Trophy Best Month Dec 2026 100%, Coral Trend Lowest Month Mar 2026 60%, Calendar Next Month Target 21 Days to maintain 90%+).
  6. **Zero-Scroll Viewport**: Entire attendance section fits comfortably in the viewport below the sticky tab bar.

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
     - Item 1: ?2,800 (single line!), Paid  On Time, 1 Sep 2026.
     - Item 2: Next Due, 30 Sep 2026 (single line!), 25 Days Left.
     - Item 3: View\nReceipt with blue receipt icon.
     - Item 4: Collect\nMoney emerald button with white Rupee circle.
  6. **Stage 5 (Today's Activity)**:
     - Header: Purple lightning bolt + Today's Activity, Details >.
     - Check-in: 6:10 AM with soft green radial ambient bloom and clock icon.
     - Workout: 62 min with soft purple radial ambient bloom and dumbbell icon.
     - Check-out: 7:42 AM with soft red radial ambient bloom and clock icon.
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


### Milestone [2026-10-30]: Unified Physical Member Card, Image 1 Menu Tabs & Image 2 Payment Studio
- **Unified Member Card Container**:
  - Combined Top Header (BAD GYM logo, CHECK-IN / CHECK-OUT / PAYMENT status badge, Silver Plan badge), Profile Section (Avatar, Name, Member ID, Age, Gender, Quote, Dates, Active badge), Stats pods (Height 5'9", Weight 74.1 kg, BMI 23.5 Normal, Goal Fat Loss), and Days Progress bar (22/30 Days Left, 73%) into a single unified widget (`UnifiedBadGymMemberCard`).
  - Styled with genuine physical membership card aesthetic: crisp rounded corners (22.dp), luxury border `BorderStroke(1.2.dp, Color(0xFFBAE6FD))`, frosted glass surface, and zero nested visual clutter.
- **Image 1 Menu Tabs (media_1793300075801_5ac48325.jpg)**:
  - Implemented 4-capsule Sticky Menu Row with 3D spherical circular icons on the left, titles, bold metrics, chevrons, mini progress bars inside each tab (Attendance 90%, Workout 70m, Payment ₹800, Body 74.1 kg), and glowing warm amber active tab with red `(!)` alert badge on Payment.
- **Image 2 Payment Studio (media_1793300207105_ea6c02fd.jpg)**:
  - Full-fidelity Payment Studio View with 3D leather wallet & coins illustration.
  - Due Amount Hero Card: Due Amount ₹800 with glowing red `[! Pending]` badge, Due on 12 Oct 2026 › (calendar icon), and 4 Days Left (hourglass icon).
  - 5 Action Buttons Row: Large red gradient `Collect Payment` CTA, WhatsApp Reminder with red `(1)` badge, Message, Send Receipt, and More (⋮).
  - 6 Quick Actions Capsules: `+ Add Payment`, `◔ Split Payment`, `🔗 Payment Link`, `% Mark Waived`, `⚙ Adjust Amount`, `📄 Add Note`.
  - 12-Month Payment Trend Chart: Y-axis (₹3k, ₹2k, ₹1k, 0), exact rupee amounts above each bar, and 6-color status legend (🟢 On Time, 🟡 Partial, 🔴 Late, ⚪ No Payment, 🟣 Extra, 🔵 Waived).
  - 3 Summary Cards Row: Total Paid ₹11,400 with circular progress gauge, Unpaid Balance ₹800 with 🎯 bullseye, and Waived by Owner ₹100 with % badge.
- **Cohesive Dashboard Color Palette**:
  - Light luxury aurora background gradient (`Color(0xFFF0FDF9)`, `Color(0xFFF8FAFC)`, `Color(0xFFF0F9FF)`, `Color(0xFFFAF5FF)`).
  - Clean frosted card surfaces with crisp themed borders, eliminating clashing colors.
- **Live Verification**:
  - Target Physical Device: Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400).
  - Screenshots:
    - `docs/reference/images/payment_studio_live_perfect.png` (Payment Studio View)
    - `docs/reference/images/attendance_live_perfect.png` (Attendance View)
  - Viewport Fit: Clean single-viewport zero-scroll fit (Y=0 to Y=2190 out of 2400).
  - Unit Tests: `./gradlew testDebugUnitTest --no-daemon` PASSED (code 0).
  - Compilation: `./gradlew assembleDebug --no-daemon` PASSED (code 0).

### Milestone [2026-10-30]: Pixel-Perfect Implementation of Body Progress Widget & Achievement Banner
- **Source of Truth**: \`media_1793338088611_f0e49ddf.jpg\`
- **Component Breakdown**:
  1. **Body Progress Header Row**:
     - 💪 Flexed arm circular badge in soft lavender container.
     - "Body Progress" (Bold title) + "Track Your Transformation" (Subtitle).
     - \`[ Last 6 Months ⌵ ]\` dropdown pill with purple text and down arrow.
  2. **Hero Transformation & Weight Card**:
     - 3D Torso Mannequin illustration (\`illustration_3d_mannequin.png\`).
     - Current Weight: **69.5 kg** (Bold purple), \`↓ 2.5 kg (3.5%)\` in bold emerald green, \`vs Aug (Start)\`.
     - Radial Semi-Circular Goal Gauge: ⛳ "Goal: 66.0 kg", 180° sweep gradient arc from green to purple, **67%** in center, \`↓ 3.5 kg to go\`.
     - Plan Progress: 🎯 "Plan Progress", horizontal gradient green capsule bar **73%**, "22 / 30 Days".
  3. **4 Key Metric Pods Row**:
     - Muscle Mass: 💪 **57.2 kg**, \`↑ 1.4 kg (2.5%)\`, purple sparkline wave, \`✓ Good\` status pill.
     - Body Fat: 🔥 **17.6 %**, \`↓ 1.2 %\`, orange sparkline wave, \`✓ Target Met\` status pill.
     - BMI: 📊 **22.8**, \`Normal (18.5–24.9)\`, sky blue sparkline wave, \`✓ Normal\` status pill.
     - BMR (Est.): ⚡ **1,620 kcal**, \`↑ 45 kcal\`, pink sparkline wave, \`✓ Good\` status pill.
  4. **Progress Trend Interactive Chart**:
     - Filter Chips: \`[Weight]\` (Active purple), \`[Muscle]\`, \`[Body Fat]\`, \`[BMI]\`, \`[Chest]\`.
     - Time Range Chips: \`[1M]\`, \`[3M]\`, \`[6M]\` (Active purple), \`[1Y]\`.
     - Spline Chart Canvas: Left Y-axis (76, 72, 68, 64), dashed grid lines, smooth cubic bezier line, lavender gradient area fill, circular data points, floating purple tooltip \`[ 69.5 kg ]\` on Oct, and X-axis (Jan..Oct).
  5. **Middle Row (2 Columns)**:
     - **Body Measurements**: 📏 "Body Measurements" + \`[ This Month ⌵ ]\` with 6 anatomical pods:
       - Chest: **40.0"** (\`↑ 0.5"\`)
       - Waist: **32.0"** (\`↓ 1.0"\`)
       - Shoulder: **48.0"** (\`↑ 0.5"\`)
       - Biceps: **14.5"** (\`↑ 0.5"\`)
       - Triceps: **12.0"** (\`↑ 0.3"\`)
       - Thigh: **22.0"** (\`↑ 0.5"\`)
     - **Muscle Distribution**: 👥 "Muscle Distribution" with Anatomical Body Mannequin diagram and 6-color muscle breakdown legend:
       - 🟦 Chest: **12%**
       - 🟩 Back: **18%**
       - 🟧 Shoulder: **14%**
       - 🟥 Triceps: **10%**
       - 🟪 Biceps: **16%**
       - 🟦 Legs: **30%**
  6. **Bottom-Mid Row (2 Columns)**:
     - **Activity Performance**: 🏃 "Activity Performance" + \`[ This Month ⌵ ]\` with 4 activity pods:
       - 🏋️ Gym: **18 Days** (65%)
       - 🏃 Cardio: **7 Days** (25%)
       - 🏃 Running: **2 Days** (5%)
       - 🤸 Mixed: **2 Days** (5%)
     - **Strength Progress**: 🏋️ "Strength Progress" + \`View All ›\` link with 3 key exercise rows:
       - Bench Press: **80 kg** (\`↑ +20 kg\`) ›
       - Squat: **100 kg** (\`↑ +30 kg\`) ›
       - Deadlift: **140 kg** (\`↑ +40 kg\`) ›
  7. **Bottom Motivation & Achievement Banner**:
     - When Body tab is selected, automatically shows:
       - 🏆 Golden trophy icon in left circle
       - "Latest Achievement"
       - "✨ New Personal Best! 🎉"
       - "Bench Press • 80 kg  [✓ Verified by Trainer]  20 Oct 2026"
       - \`[ Keep Going! ➔ ]\` golden yellow pill button.
- **Live Verification**:
  - Device: Physical Xiaomi 11i 5G (\`zxdada69gunb7ls4\`, 1080×2400)
  - Unit Tests: \`./gradlew testDebugUnitTest --no-daemon\` PASSED (56s)
  - Build: \`./gradlew assembleDebug --no-daemon\` PASSED (1m 57s)
  - Screenshots:
    - \`docs/reference/images/body_progress_live_verified.png\` (Full Body Progress View)
    - \`docs/reference/images/body_progress_bottom_live_verified.png\` (Scrolled Bottom with Achievement Banner)


## VERIFIED MILESTONE — COLLECTIVE MULTI-LINE PROGRESS TREND & PRIORITY-FIRST 2X REDESIGN
- **Status**: VERIFIED & DEPLOYED TO PHYSICAL DEVICE
- **Key Enhancements**:
  1. **Collective Multi-Line Progress Trend Chart**:
     - Added `[All]` chip option, active by default.
     - Collectively displays all 5 key metrics on a single high-contrast 95dp canvas:
       - 🟣 **Weight**: `#7C3AED` (curves from 74.5kg down to 69.5kg)
       - 🟡 **Muscle**: `#F59E0B` (curves up from 54.8kg to 57.2kg)
       - 🔴 **Body Fat**: `#EF4444` (curves down from 22.0% to 17.6%)
       - 🔵 **BMI**: `#0284C7` (curves down from 24.8 to 22.8)
       - 🟢 **Chest / Tape**: `#10B981` (curves up from 38.8" to 40.0")
     - Interactive Bottom Legend with 5 colored pods and live values:
       - 🟣 Weight 69.5kg | 🟡 Muscle 57.2kg | 🔴 Fat 17.6% | 🔵 BMI 22.8 | 🟢 Chest 40.0"
     - Interactive Individual Metric Focusing:
       - Tapping `[Weight]`, `[Muscle]`, `[Body Fat]`, `[BMI]`, or `[Chest]` boldly highlights that specific curve with gradient area underfill, switches the Y-axis to that metric's real scale, and updates the floating tooltip callout (e.g. `[ 57.2 kg ]` in amber).
       - Tapping `[All]` instantly restores all 5 curves simultaneously.
     - Dynamic Time Filtering: `[1M]`, `[3M]`, `[6M]`, `[1Y]` updates timeline density and X-axis labels.
  2. **Priority-First Information Architecture (Curiosity & Motivation Upfront)**:
     - **Priority 1 (Curious Milestone Spotlight)**: 🏆 "Latest Achievement — ✨ New Personal Best! 🎉 Bench Press • 80 kg (+20 kg)" with `✓ Verified by Trainer` badge and `20 Oct 2026` positioned right at the top of the Body view.
     - **Priority 2 (Hero Transformation)**: 3D Torso Mannequin silhouette + Bold 35sp Current Weight **69.5 kg** on one single clean line + Semicircular Goal Arc (67% to 66.0 kg goal) + Plan Progress bar (73%, 22/30 Days).
     - **Priority 3 (Collective Trend Chart)**: 95dp canvas with all 5 curves and real metrics legend.
     - **Priority 4 (4 Key Metric Pods)**: Muscle Mass (57.2 kg), Body Fat (17.6%), BMI (22.8), BMR (1,620 kcal) with sparklines and clear status badges.
     - **Priority 5 (Heavy Strength Lifts)**: Bench Press 80 kg (+20 kg), Squat 100 kg (+30 kg), Deadlift 140 kg (+40 kg).
     - **Priority 6 (Body Measurements & Muscle Distribution)**: 6 tape measurements + Torso muscle group breakdown.
     - **Priority 7 (Activity Performance)**: Gym (65%), Cardio (25%), Running (5%), Mixed (5%).
     - **Priority 8 (Motivation CTA)**: "Consistent Steps, Brighter Days ☀️ Keep Going! ➔".
  3. **2x Legibility Scale Across Entire Dashboard**:
     - Scaled up font sizes and touch paddings across all cards (Attendance day cells to 26dp with 12.5sp bold numbers, Monthly trend KPIs to 13–15sp, pod values to 16.5sp, labels to 11–13sp).
     - Resolved all text clipping and wrapped lines.
- **Physical Device & Verification Evidence**:
  - Target Device: Physical Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400)
  - Unit Tests: `./gradlew testDebugUnitTest --no-daemon` PASSED (1m 3s)
  - Build: `./gradlew assembleDebug --no-daemon` PASSED (2m 7s)
  - Live Screenshots:
    - `docs/reference/images/device_screen_body_perfect_live.png` (Default `[All]` collective 5-line view + Spotlight Win + Hero)
    - `docs/reference/images/device_screen_body_muscle_focused.png` (Interactive `[Muscle]` focused curve + Y-scale + Tooltip)
    - `docs/reference/images/device_screen_body_all_again.png` (Switched back to collective 5-line view)


### Milestone: 1:1 Pixel-Perfect Master Reference Implementation Verified Live on Device (2026-10-30)
- **Design Source of Truth**: User Master Reference Sheet `media_1793380723789_735d7ce6.jpg` (Section 8: Complete Mobile Preview 360px).
- **Key Enhancements**:
  1. **Status Bar Clearance**: Added `.statusBarsPadding()` to root Column in `MasterStoryboardDashboard.kt` so the Member Pass Card never clips into the phone's clock/battery/wifi icons.
  2. **Top Header**:
     - Added 3D bodybuilder flex character (`R.drawable.illustration_character_flex`) on the top right edge.
     - 135° Purple -> Magenta -> Orange brand gradient background with 👑 BAD GYM MEMBER INTELLIGENCE.
     - Translucent glowing magenta stadium capsule with fingerprint + CHECK-IN Today • 08:49 AM.
     - White capsule with green leather wallet ₹2,800 + 03 Nov 2026.
  3. **Days Used Progress Bar**:
     - Solid green circular calendar icon badge (`#078A69`) on left.
     - Ultra-thin 5dp gradient bar (Orange -> Magenta -> Purple).
     - Clean right side pod: "8 days left", "73%", circular chevron button.
  4. **All 4 Navigation Tabs Fully Colored**:
     - Attendance: Emerald Green gradient (`#065F46 -> #059669`) with 3D calendar sphere, white text, 81%, green mini bar.
     - Workout: Purple gradient (`#581C87 -> #7C3AED`) with 3D dumbbell sphere, white text, 67m, purple mini bar.
     - Payment: Orange gradient (`#9A3412 -> #EA580C`) with 3D wallet sphere, red "!" alert, ₹2,800, orange mini bar.
     - Body: Ocean Blue gradient (`#0369A1 -> #0284C7`) with 3D person sphere, white text, 69.5 kg, cyan mini bar.
     - All 4 tabs rendered with full glossy gradient surfaces simultaneously matching master reference.
  5. **Attendance Section (1:1 Reference Match)**:
     - Outer 1.5dp multi-color gradient border (`BorderGradient`).
     - Vibrant wave gradient header banner (`#D92BAF -> #F34B9A -> #FF963C`) with red calendar badge, "Attendance", "Daily check-ins • Mon – Sun", sponsored "Stay Fit with MYPROTEIN [AD]", and "‹ Oct 2026 ›" selector.
     - Left column: Donut gauge (green gradient with yellow tip, 81% Attendance Rate), "25 / 31 days", "4 holidays", and 4-row vertical legend (Present 23, Late 2, Absent 2, Holiday 4).
     - Right column: 7-column calendar grid with Thu 1..Sat 31, exact semantic color mapping (Absent red on 17 & 24, Late amber on 22 & 28, Sunday grey, today concentric ring on 30).
     - Removed redundant bottom text row to preserve compact mobile viewport.
  6. **Attendance Trend Section (1:1 Reference Match)**:
     - Soft pastel aurora header banner with 3-bar green icon, "Attendance trend", sponsored "blinkit Healthy Snacks [AD]", and "12 months ⌵" pill.
     - Removed bulky KPI cards above the chart to match reference design.
     - 12-month bar chart (0, 50, 100 Y-axis) with deep green Oct 81% active bar.
     - 3 Dynamic Dashed Reference Lines (Green Max 97%, Amber Avg 81%, Red Min 58%) with bottom dashed preview legend.
- **Physical Device & Verification Evidence**:
  - Target Device: Physical Xiaomi 11i 5G (`zxdada69gunb7ls4`, 1080×2400)
  - Unit Tests: `./gradlew testDebugUnitTest --no-daemon` PASSED (1m 6s, 27 tasks)
  - Build: `./gradlew assembleDebug --no-daemon` PASSED (2m 14s, 38 tasks)
  - Live Screenshots:
    - `docs/reference/images/live_perfect_match.png` (Default state: Attendance + Trend 1:1 match)
    - `docs/reference/images/tab_workout.png` (Interactive tap on Workout tab)
    - `docs/reference/images/tab_payment_test.png` (Interactive tap on Payment tab)
    - `docs/reference/images/tab_body_test.png` (Interactive tap on Body tab)
    - `docs/reference/images/tab_attendance_final.png` (Switched back to Attendance tab)
