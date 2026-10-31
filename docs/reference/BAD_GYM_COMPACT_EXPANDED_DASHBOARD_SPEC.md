# BAD GYM — Compact (~300dp) & Expanded (~400dp) Member Intelligence Dashboard
## Dual-Mode Architectural Specification & Widgets Integration

---

### 1. Architectural Concept

This document defines the dual-mode visual and interaction contract illustrated in the reference mockup (`input_file_0.png`):
1. **COMPACT VIEW (~300 dp height, 360 dp mobile width)**:
   - Ultra-compact, high-density, zero-scroll friendly dashboard.
   - 4 primary bento cards flanking the central member identity showcase.
   - Pinned demographic data (`BG305 | 23 yrs | Male`) + `🟢 Online` status indicator.
   - **Horizontal Quick Widgets Carousel**: Integrated micro-metric chips (`Activity 48 min`, `Goals 4/5`, `Nutrition 3,250 kcal`, `Plan 25 Days`, `Trainer Rahul Sir`, `Next Session Today 6 PM`).
2. **EXPANDED VIEW (On Tap, ~400 dp height)**:
   - Deep-dive analytics surface with 4 rich modular columns (Attendance, Workout, Payment, Body Progress).
   - Timeframe filter bar: `7D`, `30D` (Active), `3M`, `6M`, `1Y`.
   - Day-level inspection: This Week day badges (`M T W T F S S`), weekly workout volume bars, payment receipt stamps, and body composition sub-tabs (`Weight`, `BMI`, `Body Fat`, `Muscle`).
   - Coach / AI Insight lightbulb callouts on every card.

---

### 2. Detailed Component Breakdown

#### 2.1 Compact View Layout
```
+-------------------------------------------------------------------------+
| [Menu] BAD GYM / Member Intelligence          [02 Oct 2026] [Bell] [...]|
|                                                                         |
|  +--------------------+   (• Online)    +--------------------+          |
|  | [📅] Attendance    |     (Avatar)    | [💳] Payment   (!) |          |
|  | 85%  18/21 Days    |     ( Halo )    | ₹2,800             |          |
|  | [..::'']       [>] |    ( Ashwini )  | 25 Days Left   [>] |          |
|  +--------------------+  Ashwini Singh  +--------------------+          |
|  | [🏋️] Workout       | BG305|23m|Male  | [👤] Body Progress |          |
|  | 47 min  Avg/Day    | [Active Member] | 69.5 kg   -0.7kg   |          |
|  | [||||||]       [>] |                 | [/\/\/\/\/\/\\][>] |          |
|  +--------------------+                 +--------------------+          |
|                                                                         |
|  (🔥 Activity 48m) (🎯 Goals 4/5) (🍏 Nutrition 3,250) (👑 Plan 25D)...[>]
+-------------------------------------------------------------------------+
```

#### 2.2 Expanded View Layout
```
+-------------------------------------------------------------------------+
| [←] BAD GYM / Member Details   [7D] [30D] [3M] [6M] [1Y]  [Date] [Bell] |
|                                                                         |
| +-------------+ +-------------+ +-------------+ +---------------------+ |
| | Attendance  | | Workout     | | Payment     | | Body Progress   ↑5% | |
| | 85%    ↑12% | | 47m     ↑8% | | ₹2,800  ↓20%| | 69.5 kg     ↓0.7kg  | |
| | 18/21 Days  | | Avg/Day     | | 25 Days Left| | [Wgt][BMI][Fat][Msc]| |
| | [30D Bars]  | | [Equalizer] | | [Timeline]  | | Start Curr Target   | |
| | Pres:18 Abs3| | Tot:18 Strk | | Paid: ₹12.6k| | 72.3  69.5  68 kg   | |
| | Week: ✔✔✔❌✔| | Week: 45..40| | History: 7₹ | | Progress 38% [====] | |
| | 💡 Good!    | | 💡 Great!   | | 💡 Stable   | | 💡 Good progress!   | |
| +-------------+ +-------------+ +-------------+ +---------------------+ |
+-------------------------------------------------------------------------+
```

---

### 3. Quick Widgets Carousel Specification

The carousel renders horizontally scrollable frosted capsules:
1. **Activity**: Leading flame icon (`#EF4444`), Label: `"Activity"`, Value: `"48 min"`.
2. **Goals**: Leading target icon (`#0284C7`), Label: `"Goals"`, Value: `"4/5"`.
3. **Nutrition**: Leading apple icon (`#10B981`), Label: `"Nutrition"`, Value: `"3,250 kcal"`.
4. **Plan**: Leading crown icon (`#F59E0B`), Label: `"Plan"`, Value: `"25 Days"`.
5. **Trainer**: Leading badge icon (`#6366F1`), Label: `"Trainer"`, Value: `"Rahul Sir"`.
6. **Next Session**: Leading clock icon (`#0EA5E9`), Label: `"Next Session"`, Value: `"Today 6 PM"`.
7. **Trailing Chevron**: Circular frosted button `>` allowing expansion or continuous scrolling.
