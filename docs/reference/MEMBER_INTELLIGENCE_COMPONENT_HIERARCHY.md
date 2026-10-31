# BAD GYM — Member Intelligence Dashboard
## Component Hierarchy & Composable Architecture

---

### 1. Composable Tree Architecture

The UI is built purely in **Jetpack Compose**, maintaining strict separation of concerns, stateless atomic components, and hoisted state handling.

```
MemberDashboardScreen (Root Scaffold / Box)
│
├── CanvasBackground (Fluid Aurora Gradient + Soft Radial Orbs)
│
├── DashboardScrollContent (VerticalScroll Column)
│   │
│   ├── DashboardTopBar (Edge-to-edge header)
│   │   ├── NavigationMenuButton (Squircle hamburger)
│   │   ├── BrandTitleBlock ("BAD GYM" + "Member Intelligence")
│   │   ├── DateCapsulePill ("02 Oct 2026")
│   │   └── NotificationBellButton (Circular frosted + Badge "2")
│   │
│   ├── MemberCenterpieceHero (Anchor Profile)
│   │   ├── AvatarHaloRing (Dual glowing neon cyan/mint stroke)
│   │   │   ├── CircularProfileImage (Ashwini Singh)
│   │   │   └── CrownPinBadge (Gold 3D crown at 45°)
│   │   ├── ActiveStatusPill ("Active" checkmark pill)
│   │   ├── MemberIdentityText ("Ashwini Singh 👑" + "BG305 | Prime Plan")
│   │   └── MembershipExpiryPill ("👑 25 Days Left" + "Prime Plan")
│   │
│   ├── BentoIntelligenceGrid (2x2 Balanced Cards Flanking Center)
│   │   ├── Row 1 (Top Cards)
│   │   │   ├── AttendanceBentoCard (Emerald Green, 85%, mini chart, chevron)
│   │   │   └── PaymentBentoCard (Amber Orange, ₹2,800, alert pill, chevron)
│   │   │
│   │   └── Row 2 (Bottom Cards)
│   │       ├── WorkoutBentoCard (Electric Cyan, 47 min, +8% pill, equalizer)
│   │       └── BodyProgressBentoCard (Magenta Purple, 69.5 kg, -0.7kg, sparkline)
│   │
│   ├── DetailedOverviewCard (Large Glassmorphic Container)
│   │   ├── OverviewHeader ("Detailed Overview" + "View All >")
│   │   ├── TimeframeSelectorPills ("7D", "30D" [Active], "3M", "6M")
│   │   │
│   │   ├── SplitAnalyticsSection (Row)
│   │   │   ├── DualBarLineCanvasChart (Left: Gradient Bars + Spline + Tooltip)
│   │   │   └── MetricSummaryStack (Right: 4 Stacked Rounded Pills)
│   │   │       ├── MetricSummaryPill (Attendance 85% ↑ +12%)
│   │   │       ├── MetricSummaryPill (Workout 47 min ↑ +8%)
│   │   │       ├── MetricSummaryPill (Payment ₹2,800 ↓ -5%)
│   │   │       └── MetricSummaryPill (Body Weight 69.5 kg ↓ -0.7 kg)
│   │   │
│   │   └── FitnessMilestoneRow (Bottom: 3 Capsule Targets)
│   │       ├── MilestonePill (🏆 Goal: Fit & Strong)
│   │       ├── MilestonePill (🎯 Target Weight: 68 kg)
│   │       └── MilestonePill (🔥 Burn Calories: 500 kcal/day)
│   │
│   └── BottomSpacer (Space for floating nav dock)
│
└── FloatingGlassBottomDock (Pill-shaped capsule navigation)
    ├── DockNavigationItem (Home - Active Glow Pill)
    ├── DockNavigationItem (Progress - Dumbbell)
    ├── DockNavigationItem (Goals - Trophy)
    └── DockNavigationItem (More - Grid)
```

---

### 2. Composable Component Specifications

#### 2.1 `DashboardTopBar`
- **Location**: Top of screen with `.statusBarsPadding()`.
- **Layout**: `Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically)`.
- **Inputs**:
  - `onMenuClick: () -> Unit`
  - `onDateClick: () -> Unit`
  - `onNotificationClick: () -> Unit`
  - `notificationCount: Int = 2`

#### 2.2 `MemberCenterpieceHero`
- **Layout**: Centered vertical stack (`Column(horizontalAlignment = Alignment.CenterHorizontally)`).
- **Sub-components**:
  - `AvatarWithHalo`: `Box` with `Modifier.size(96.dp)` with canvas-drawn gradient ring and shadow, containing the clipped circular image.
  - Crown icon positioned via `Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp)`.
  - `ActiveStatusPill` positioned via `Modifier.align(Alignment.BottomCenter).offset(y = 10.dp)`.
- **Inputs**:
  - `name: String`
  - `memberCode: String`
  - `planName: String`
  - `daysLeft: Int`
  - `isActive: Boolean`
  - `avatarUrl: String?`

#### 2.3 `BentoIntelligenceGrid`
- **Layout**: `Column(verticalArrangement = Arrangement.spacedBy(10.dp))` containing 2 `Row`s with equal weights `Modifier.weight(1f)` and `spacedBy(10.dp)`.
- **Individual Cards**:
  1. `AttendanceBentoCard`:
     - Gradient: `AttendanceCardGradient`
     - Displays `85%`, `18/21 Days`, `+12%` trend, 7-bar chart.
     - Click handler: `onAttendanceClick: () -> Unit`
  2. `PaymentBentoCard`:
     - Gradient: `PaymentCardGradient`
     - Displays `₹2,800`, `Due Amount`, alert badge `! 25 Days Left`.
     - Click handler: `onPaymentClick: () -> Unit`
  3. `WorkoutBentoCard`:
     - Gradient: `WorkoutCardGradient`
     - Displays `47 min`, `Avg/Day`, trend `+8%`, audio-bar graph.
     - Click handler: `onWorkoutClick: () -> Unit`
  4. `BodyProgressBentoCard`:
     - Gradient: `BodyProgressCardGradient`
     - Displays `69.5 kg`, `-0.7 kg` difference, wave line graph.
     - Click handler: `onBodyProgressClick: () -> Unit`

#### 2.4 `DetailedOverviewCard`
- **Container**: `Box` with `Modifier.fillMaxWidth().clip(RoundedCornerRadius(28.dp)).background(GlassSurfaceUltra).border(1.5.dp, GlassBorderHighlight, RoundedCornerRadius(28.dp))`.
- **Child Elements**:
  - `TimeframeSelector`: Row of pills with animated background transition between `7D`, `30D`, `3M`, `6M`.
  - `SplitAnalyticsSection`:
    - `DualBarLineCanvasChart`:
      - Drawn using Jetpack Compose `Canvas(modifier = Modifier.weight(1.3f).height(160.dp))`.
      - Renders gradient rounded bars (`drawRoundRect`).
      - Renders cubic bezier spline connecting peak points (`drawPath`).
      - Renders glowing circular dots at each vertex.
      - Renders floating bubble tooltip with triangle pointer for the peak point (`85% (18/21)`).
    - `MetricSummaryStack`:
      - `Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp))`.
      - 4 micro-pills with leading icons and formatted text.
  - `FitnessMilestoneRow`:
    - `Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp))`.
    - 3 pills sharing equal width `Modifier.weight(1f)`.

#### 2.5 `FloatingGlassBottomDock`
- **Geometry**: `Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp).height(64.dp).clip(CircleShape).background(GlassSurfaceUltra).border(1.dp, GlassBorderGlow, CircleShape))`
- **Items**:
  - `Home`: Enclosed in an active emerald pill container with glow.
  - `Progress`, `Goals`, `More`: Framed with slate icon and subtitle.

---

### 3. State Management & Data Binding

The screen is driven by a unified UI state model:

```kotlin
data class MemberDashboardUiState(
    val memberProfile: MemberProfileData,
    val attendanceSummary: AttendanceMetricData,
    val paymentSummary: PaymentMetricData,
    val workoutSummary: WorkoutMetricData,
    val bodyProgressSummary: BodyProgressMetricData,
    val overviewAnalytics: OverviewAnalyticsData,
    val selectedTimeframe: TimeframeOption = TimeframeOption.THIRTY_DAYS,
    val activeBottomNav: NavigationTab = NavigationTab.HOME
)
```

Events are surfaced via a clean intent/callback interface:
```kotlin
sealed interface DashboardUiEvent {
    object OnMenuClicked : DashboardUiEvent
    object OnNotificationClicked : DashboardUiEvent
    object OnViewAllOverviewClicked : DashboardUiEvent
    data class OnTimeframeSelected(val option: TimeframeOption) : DashboardUiEvent
    data class OnCardClicked(val cardType: BentoCardType) : DashboardUiEvent
    data class OnNavigationTabSelected(val tab: NavigationTab) : DashboardUiEvent
}
```
