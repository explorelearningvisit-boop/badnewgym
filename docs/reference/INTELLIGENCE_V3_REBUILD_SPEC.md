# INTELLIGENCE V3 — Member Intelligence Dashboard Rebuild Spec

Source of truth = 5 approved reference images (asset pack + 10 themed phones + 3 large screens).
Goal: rebuild the dashboard as a fresh, dense, theme-driven system matching those images 1:1.
Old dashboard code will be MOVED to `unused/` afterwards (a separate agent does that — DO NOT move/delete anything).

## Ground rules (all builders)
- Package: `com.example.badnewgym.feature.membercard.presentation.intelligence`
- One agent = ONE new file. No edits to any existing file. No new Gradle deps, no new resources.
- Density: designed for 360–392dp width. Text: titles 12–13sp Bold, big stats 28–34sp Black, chips 9–10sp,
  body 10–11sp. Headings use negative letterSpacing (-0.2 to -0.5). Cards corner 16–18dp, padding 12dp,
  gaps 8–10dp. Icons: `androidx.compose.material.icons.filled.*` or `Icons.Rounded.*` only.
- Colors ONLY via `intelligenceColors()` tokens (below) + explicit reference hexes quoted in specs.
- KDoc one-liner on every public declaration. No TODOs. Must compile against the contracts.
- Data comes from `MemberCardData` / `BadGymMemberPass` (feature.membercard.domain.model) and
  `IntelligenceSeries` (feature.membercard.domain.intelligence). Never invent numbers not derivable
  from those or explicitly listed as template constants here.

## Contracts already on disk (read them first)
- `app/src/main/java/com/example/badnewgym/feature/membercard/presentation/intelligence/IntelligenceTheme.kt`
  → `IntelligenceColors`, `intelligenceColors()`, `IntelligenceSectionCard(title, icon, modifier, subtitle, trailing, content)`,
  `IntelligenceChipRow(chips, selected, onSelect, modifier)`, `IntelligenceLegendDot(color, label, modifier)`, `IntelligenceNote(text, modifier)`
- `app/src/main/java/com/example/badnewgym/feature/membercard/domain/intelligence/IntelligenceSeries.kt`
  → `workoutDailyNormalized(bars)`, `workoutMonthlyPattern()`, `paymentTrend()`, `ATTENDANCE_TARGET_PERCENT`, point models
- Data models: `app/src/main/java/com/example/badnewgym/feature/membercard/domain/model/MemberCardModels.kt`
  (sampleAshwiniSingh: attendance 81% 25/31, present 23 / late 2 / absent 2 / holiday 4, Oct 2026;
  workout bars 31 days, peak 88m day 8, low 32m day 16, 74m day 26, 19 active days;
  paymentPlan: Gold Plan (6 Months), ₹1,200 due 16 Oct 2026, 45 days left, ₹6,000 total paid, last ₹1,200 on 01 Oct 2026;
  bodyStats: Weight 69.5 (May 67.4→Oct 69.5), Muscle 57.2, Body Fat 17.6%, BMI 22.8; measurements: Chest 98 ↑2.0, Waist 82 ↓1.0, Arm 34 ↑1.0)
- Pass: `BadGymMemberPassModels.kt` → sampleAshwaniKumarSingh(): BG215, 28, Male, Gold,
  09 Oct 2026 → 07 Nov 2026, daysLeft 8, totalDays 30, planPrice "180 Days Plan".

## Shared computations
- usedDays = pass.totalDays - pass.daysLeft (= 22); usedPercent = usedDays*100/pass.totalDays (= 73.3%)
- workoutAvgSession = totalMinutes / activeDays (= 61 min); workoutTotalHr = totalMinutes/60 (1 decimal, = 19.4)
- workoutConsistency = activeDays * 100 / 31 (= 61%)
- payment display: `data.paymentPlan.currentDueAmount` (fallback `dueAlertAmount`)
- body start/current/target: start = 72.8 kg (TEMPLATE const), current = weight bodyStat value, target = 65.0 kg (TEMPLATE)
- body ring = 56 (TEMPLATE "Transformation Goal"), achieved = 42.3 (TEMPLATE), muscle gain = +4.5 kg (TEMPLATE)
  → mark template-derived numbers with a small "template" muted suffix label where shown standalone.

## Theme engine (existing, reuse, do not modify)
`design/BadGymThemeEngine.kt`: `BadGymThemeManager.activeTheme` (mutableStateOf), `BadGymThemeSelectorStrip(selectedTheme, onThemeSelect, modifier)`,
`theme.getCanvasGradient()` (dark hero band top ~22%, light body below), `getActiveTabBrush()`, `getGlowColor()`.

## FILES TO CREATE (exact public signatures)

### 1. IntelligenceHeader.kt — agent "header"
```kotlin
@Composable
fun IntelligenceHeader(
    data: MemberCardData,
    pass: BadGymMemberPass,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onDueClick: () -> Unit,
    modifier: Modifier = Modifier
)
```
Vertical stack, content on top of the dark hero band (transparent bg; parent draws gradient):
1. Top bar row: menu icon (white) → onMenuClick; center: `Icons.Filled.EmojiEvents` crown tinted gold
   + "BAD GYM" 18sp Black white with accent-tinted "GYM" + "Member Intelligence" 8.5sp white70;
   right: trophy pill (gold gradient bg `Brush.linearGradient(0xFFFBBF24,0xFFF59E0C)`, dark ink text):
   "₹1,200" 11sp Black + "16 Oct 2026" 8sp (due amount/date from paymentPlan), click → onDueClick.
2. Identity row: avatar 50dp circle with 2dp accent ring (pass.avatarRes) → onAvatarClick;
   right column: name 16sp Bold white (pass.name); meta row 9sp white80: "👤 BG215   📅 28 Yrs   ♂ Male"
   (pass.memberId/age/gender; use small icons not emoji: Person/Calendar/Male);
   chips row (9sp pills): "● Active" (good bg 20% + good text), "💎 Gold Plan" (accentWash + onAccent-ish white),
   "₹1,200 / 30 Days" (white 14% bg white text), "Auto ON" (white 14% bg).
3. Membership progress card (white card 16dp corner): row "// Membership Progress" 11sp Bold ink
   + right "22/30 Days" 11sp Black; progress track 14dp rounded (ink 8%), fill = accent gradient 73.3%,
   centered overlay text "73.3% Used" 8.5sp white Black-ish; below: 2 chips 8.5sp:
   "📅 09 Oct 2026 – 07 Nov 2026 (30 Days Plan)" and "⏳ 8 Days Left" (muted bg, inkSoft).
4. KPI tab row: 4 equal tiles (Row weight(1f)): [CalendarIcon/KPI "81%"/"Attendance"],
   [FitnessCenter/"61 min"/"Workout"], [Wallet/"₹1,200"/"Payment"], [Accessibility/"69.5 kg"/"Body"].
   Selected tile: `activeTabBrush` bg, onActiveTab text, 3dp accent glow shadow, bottom mini progress bar
   (onActiveTab 40%): attendance 0.81 / workout consistency 0.61 / payment dues ratio (1200/1800 plan? use
   planDaysUsed/planTotalDays 0.75) / body 0.423. Unselected: white card, ink values, muted label, faint bar.

### 2. AttendanceIntelligenceScreen.kt — agent "attendance"
```kotlin
@Composable
fun AttendanceIntelligenceScreen(
    data: MemberCardData,
    monthRecords: List<AttendanceMonthRecord> = attendanceMonthHistory(data.attendance.percent),
    paidDay: Int? = null,
    dueDay: Int? = null,
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
)
```
(`AttendanceMonthRecord` + `attendanceMonthHistory` are public in `presentation/components/InfographicMasterComponents.kt` — import & reuse.)
1. `IntelligenceSectionCard("Attendance", Icons.Filled.EventAvailable)` trailing: month pill
   "Oct 2026 ▾" (accentWash pill, static label `data.attendance.monthLabel`).
2. Row: LEFT column: "81%" 34sp Black accent (data.attendance.percent), "25 / 31 Days" 9.5sp muted;
   RIGHT: month calendar grid, Mon..Sun header (7 columns, weight 1f), 1..31 day chips 26dp rounded(8dp):
   PRESENT=good bg/white text, LATE=warn, ABSENT=bad, HOLIDAY=faint bg/white; dueDay = red ring +
   tiny "₹" dot, paidDay = good ring + tiny "✓"; today = accent ring. Day click → onDayClick.
   Column offset: first day offset = data.attendance.calendarDays.first().dayOfWeekIndex mapped to Mon-first.
3. Legend row (4 IntelligenceLegendDot): Present 23 / Late 2 / Absent 2 / Holiday 4 (counts from calendarDays,
   holiday = status HOLIDAY).
4. `IntelligenceSectionCard("Attendance Trend (12 Months)", Icons.Filled.Insights)` trailing
   "Nov 2025 – Oct 2026" 8sp muted. Custom Canvas line/area chart, height 110dp:
   - x = monthRecords (Nov 2025..Oct 2026), y 0..100%, gridlines 0/25/50/75/100 (faint, 8sp labels).
   - Area fill accentWash, line accent 2.5dp, dots on recorded months.
   - Dashed warn horizontal line at `IntelligenceSeries.ATTENDANCE_TARGET_PERCENT` (75%).
   - Labels: peak month "97% (30d)" + gold crown dot (Jan), drop month "58% (18d)" red (Mar),
     current "81% (25d)" (Oct). monthRecords gives percent/attended/total; peak = max, drop = min.
   - Untracked months (percent null): faint dashed connector.
5. `IntelligenceNote("Mar 2026 drop (58%): Holi holidays & annual exam break")`.
6. Footer 4 stat tiles (equal row, white cards): 🏆 "Best" 97% (Jan) good / 📊 "Average" 81% ink /
   🔻 "Min" 58% (Mar) bad / 🗓 "Tracked" 8/12 mo accent — compute from monthRecords (round average).

### 3. WorkoutIntelligenceScreen.kt — agent "workout"
```kotlin
@Composable
fun WorkoutIntelligenceScreen(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
)
```
1. `IntelligenceSectionCard("Workout Analytics", Icons.Filled.FitnessCenter, subtitle="Daily Workout Trend • Oct 2026")`
   trailing month pill "Oct 2026 ▾".
2. Custom Canvas line/area chart height 130dp of `IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)`:
   x 1..31, y 0/23/45/68/90m gridlines (8sp muted labels); accent line + area fill accentWash;
   point labels (8.5sp): first point "47m" ink; peak day 88 → gold crown (draw 👑 via crown path or
   Icons draw: use a small triangle-free label with warn-colored "88m" + gold dot) + label "88m" gold;
   low 32 → bad red label; alert 41 → bad red; 74 → info cyan-blue label. Zero-min days: no dot.
3. Stat strip row (3 tiles): "Avg/day" 38 min / "Best" 88 min / "Lowest" 32 min (values computed from
   series: total/31, max, min). Second strip: "Total" 19.4 hr / "Active" 19/31 d / "Consistency" 61%
   (active*100/31).
4. `IntelligenceSectionCard("Monthly Pattern (This Year)", Icons.Filled.BarChart)` trailing
   "⏱ Total: 306.0 hr" 8.5sp accent (computed sum). Column/bars chart height 120dp:
   10 bars Jan..Oct from `IntelligenceSeries.workoutMonthlyPattern()`, bar = hours (accent gradient),
   each bar labeled above "36h" 8.5sp ink bold and below "18d" 8sp muted; Jul peak bar gets gold crown
   dot + taller emphasis; Mar drop bar bad-tinted. X labels 8sp.

### 4. PaymentIntelligenceScreen.kt — agent "payment"
```kotlin
@Composable
fun PaymentIntelligenceScreen(
    data: MemberCardData,
    onHistoryClick: () -> Unit = {},
    onViewDueClick: () -> Unit = {},
    onReceiptClick: () -> Unit = {},
    onRemindClick: () -> Unit = {},
    modifier: Modifier = Modifier
)
```
1. `IntelligenceSectionCard("Payment", Icons.Filled.AccountBalanceWallet, subtitle=data.paymentPlan.planDuration)`
   trailing chip "🕘 History ▸" (accentWash, click → onHistoryClick).
2. Row of 2 white tiles (equal): tile 1 icon Wallet: "Upcoming Renewal" 8.5sp muted,
   "₹1,200" 16sp Black ink, "⏱ Due in 45 Days" 8.5sp accent (due amount = displayDue logic
   currentDueAmount→dueAlertAmount; days = planDaysRemaining); tile 2 icon Event:
   "Renewal Date" muted, "16 Oct 2026" 13sp Black, "✔ Active • 45 Days Left" 8.5sp good.
3. Action row: "View Due ▸" filled accent button (onAccent text, onDueClick) + 3 soft tiles:
   🔔 "Remind" (onRemindClick), 🧾 "Receipt" (onReceiptClick), ⋯ "More".
4. `IntelligenceSectionCard("Payment Trend", Icons.Filled.ShowChart)` trailing "Jan – Oct 2026" 8sp muted.
   Canvas line/area chart height 120dp from `IntelligenceSeries.paymentTrend()`: y auto 0..2000;
   ₹ labels (8.5sp ink bold) on every non-null point: ₹600 ₹600 ₹1,000 ₹1,200 ₹1,800 ₹1,200 ₹500;
   Jul peak gets gold crown + "₹1,800" gold; gap months (Mar, Apr, Aug) = dashed faint connectors, no dot.
   Accent line + accentWash area.
5. Two `IntelligenceNote`s: "★ Jul Spike: Annual Plan Renewal" (use warn tone w/o icon swap) and
   "Mar, Apr, Aug: Pre-paid cycle months — no invoice raised".
6. Summary row 3 tiles: ✔ "Total Paid" ₹6,000 (good, from amountPaid) / ⟳ "Unpaid Balance" ₹1,200 (bad,
   due amount) / 🧾 "Last Payment" ₹1,200 + "01 Oct 2026" 8sp (lastPaymentAmount/lastPaymentDate).

### 5. BodyIntelligenceLiteScreen.kt — agent "body"
```kotlin
@Composable
fun BodyIntelligenceLiteScreen(
    data: MemberCardData,
    pass: BadGymMemberPass,
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
)
```
1. `IntelligenceSectionCard("Body Intelligence", Icons.Filled.Accessibility)` trailing chip "⚡ More ▸"
   (accentWash → onMoreClick).
2. `IntelligenceChipRow` 8 chips: Overview, Composition, Measurements, Progress, Gallery, Goals, Health, More
   — `rememberSaveable { mutableIntStateOf(0) }`. Selecting "More" ALSO fires onMoreClick.
3. Overview (selected 0): Transformation card: row "// Transformation Goal" 11sp Bold + right ring
   56% (52dp ring, accent, center "56%" 11sp Black); sub "Fat Loss → Target 65.0 kg" 9sp muted;
   Row of 3 weight pods: "Start" 72.8 kg / "Current" 69.5 kg / "Target" 65.0 kg (current accent + bold;
   current from weight bodyStat; start/target TEMPLATE consts + "template" 7.5sp muted under Start).
   Then "Body Composition" 3 tiles: BMI 22.8 "Normal" good / Body Fat 17.6% "Moderate" warn /
   Muscle 57.2 kg "Good" good (from data.bodyStats, status word + tone per stat).
   Then "Quick Insights" row 3 chips: "▼ 3.3 kg Weight" bad-tone, "▲ 4.5 kg Muscle" good-tone,
   "◎ 42.3% Achieved" accent (template).
4. Composition (1): single-series line chart (110dp) of weight history (bodyStats.first().history values),
   labels first/last "67.4"/"69.5", + same 3 composition tiles.
5. Measurements (2): rows (Chest 98.0 cm ↑ 2.0 good / Waist 82.0 cm ↓ 1.0 good / Arm 34.0 cm ↑ 1.0 good)
   from data.measurements (name, value+unit, delta, favourable) as list tiles.
6. Progress (3): line chart of weight history + milestone chips row ("First 5 kg ▸", "BF < 18% ▸" muted).
7. Gallery (4): honest empty state card "No progress photos uploaded yet" + dashed add tile.
8. Goals (5): rows "Fat Loss → 65.0 kg" progress bar 42.3% (template), "Muscle Gain → 60 kg" 56% (template),
   chips "Goal Achieved" gold when ≥100.
9. Health (6): tiles: Heart Rate "Not recorded" muted / Sleep "Not tracked" muted / Water "2.5 L" accent
   (template) — never invent vitals.

### 6. MemberIntelligenceDashboard.kt — agent "shell"
```kotlin
@Composable
fun MemberIntelligenceDashboard(
    data: MemberCardData,
    pass: BadGymMemberPass,
    onMenuClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
)
```
- Root Column `.fillMaxSize().background(BadGymThemeManager.activeTheme.getCanvasGradient())`.
- Top: `BadGymThemeSelectorStrip(selectedTheme = BadGymThemeManager.activeTheme, onThemeSelect = { BadGymThemeManager.setTheme(it) })`.
- `IntelligenceHeader(data, pass, selectedTab, onTabSelected = { selectedTab = it }, onMenuClick, onAvatarClick, onDueClick = onPaymentClick)`.
- Body (weight 1f): `val bodyState = rememberBodyIntelligenceState(bodyInput)` — build bodyInput exactly
  like MasterStoryboardDashboard did (read that file: MemberBodyIntelligenceMapper.from(data).copy(…pass identity…)).
  `var showFullBody by rememberSaveable { mutableStateOf(false) }`; `var selectedTab by rememberSaveable { mutableIntStateOf(0) }`.
  `LazyColumn(state)` when(selectedTab):
  - 0 → item("tab_attendance") { AttendanceIntelligenceScreen(data, paidDay = due/paid extraction same regex as old file, onDayClick = onAttendanceDayClick) }
  - 1 → item("tab_workout") { WorkoutIntelligenceScreen(data, onWorkoutClick) }
  - 2 → item("tab_payment") { PaymentIntelligenceScreen(data, onHistoryClick = onPaymentClick, onViewDueClick = onPaymentClick, onReceiptClick = onViewReceiptClick, onRemindClick = {}) }
  - 3 → if (showFullBody) { item("tab_body_back") { back chip "◂ Body overview" } ; bodyIntelligenceItems(state = bodyState, listState = listState) }
        else item("tab_body") { BodyIntelligenceLiteScreen(data, pass, onMoreClick = { showFullBody = true }) }
  - contentPadding 10.dp, verticalArrangement spacedBy(10.dp).
- Sheet hosts INSIDE: `BodyIntelligenceSheetHost(state = bodyState)` (already public).
- Import bodyIntelligenceItems from feature.bodyprogress.presentation.intelligence (existing public API).
- When Body "More" opens the full workspace the header/KPI row STAYS (tabs remain switchable).

## Verification (coordinator runs)
`cmd /c "set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr&& gradlew assembleDebug --console=plain --no-daemon"`
then `testDebugUnitTest`, install via adb (device zxdada69gunb7ls4), screenshot each tab + theme switch,
compare against reference images, fix, commit on feature/member-intelligence-v3.
