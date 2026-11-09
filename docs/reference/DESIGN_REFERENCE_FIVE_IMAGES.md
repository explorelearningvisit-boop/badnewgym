# BAD GYM — Five-Image Design Reference Extraction

Source: five member-dashboard mockups supplied 2026-11-09.
Purpose: single source of truth for the pixel-parity migration. Every token below is
read directly off the references, not invented.

---

## 1. Global visual language

| Property | Value from references |
|---|---|
| Canvas | Light lavender wash, `#EDE4FB` → `#F7F2FE`, soft vertical gradient |
| Card surface | Near-white `#FCFAFF` with 1dp hairline `#E6DAF7` and soft violet shadow (0 6dp 18dp, `#6D28D9` @ 8%) |
| Card radius | 22dp–26dp outer, 14dp–16dp inner tiles, 999dp pills |
| Primary violet | `#7C2AE8` (buttons, active fills, icons) |
| Gradient violet | `#8B2BEF` → `#C026D3` (left→right) for CHECK-IN, active tab, streak card |
| Success green | `#16A34A` solid, `#22C55E` bar fill |
| Gradient green→lime | `#16A34A` → `#84CC16` (attendance ring, reliability ring) |
| Gradient green→violet | `#22C55E` → `#7C2AE8` (workout ring) |
| Warn amber | `#F59E0B` (late days, 8-days-left chip, late bars) |
| Danger red | `#EF4444` (absent, gap, lowest) |
| Info blue | `#3B82F6` (early payments, plan change) |
| Ink | `#1E1233` headings, `#5B4A73` body, `#8B7BA8` muted |
| Typography | ExtraBold display 22–26sp, Bold titles 15–17sp, SemiBold labels 10–11sp |

Decorative props (3D, right-aligned in header): violet dumbbell (attendance, workout,
body, payment-B), violet flexing arm (workout). Drawn as vector/asset, never emoji.

---

## 2. Header (identical across all five)

```
[☰]  [tile] BAD GYM              [ ✓ CHECK-IN ]        [🔔•]  [🏆 ₹1,200]
         Member Intelligence                                [16 Oct 2026]
```

- ☰ 22dp ink, 34dp hit box, left edge.
- Wordmark tile: 30dp rounded-square, violet gradient, dumbbell glyph (payment refs use
  green tile + green dumbbell). Wordmark `BAD GYM` 19sp ExtraBold ink, **left-aligned**
  against the tile; `Member Intelligence` 10sp SemiBold muted beneath.
- Centre CTA: pill 190×44dp, violet gradient, white `CHECK-IN` 17sp ExtraBold + white
  check disc icon.
  - **Checked-in state (refs 4,5):** green gradient, label `CHECKED IN`, second line
    `Today, 07:18 AM` 10sp, replaces the amount block weight.
- Bell: 26dp, red `#EF4444` badge 12dp top-right with white count `1`.
- Due card: white pill 96×40dp, 🏆 16dp, `₹1,200` 13sp ExtraBold, `16 Oct 2026` 9sp muted.

## 3. Identity row (identical)

- Avatar 58dp, 3dp violet ring + 2dp white gap, circular crop.
- Name 22sp ExtraBold ink; meta row `👤 BG215  |  📅 28 Yrs  |  ♂ Male` 11sp.
- Chips row: `● Active` green solid; `💎 Gold Plan` violet solid; `₹1,200 / 30 Days`
  outlined; `Auto (ON)` outlined (absent on both payment refs).
- 3D dumbbell prop, ~90×70dp, bleeding off the right edge.

## 4. Membership Progress — three variants

**A. Full card (refs 1,2,3)**
```
[📚] Membership Progress                                 22 / 30 Days
[======== green bar with white "73.3% Used" ======================]
[📅 09 Oct 2026 – 07 Nov 2026 (30 Days Plan)]  [⏳ 8 Days Left]
```
Green bar `#16A34A`, track `#EDE4FB`, 18dp tall, full radius, white 10sp label centred.

**B. Single-row inline (refs 4,5)** — no card chrome:
```
[👑] Membership            [=== green "73.3% (22 / 30 Days)" ===][amber 🕐 8 Days Left]
     Progress
```
Green segment + amber `#FDE68A` tail segment inside one 20dp track.

**C. Distance card** used when a gap exists: remaining days rendered as amber pill.

## 5. Tab row — two presentations

**Pills (refs 1,2,3):** 4 equal pills, 40dp, white + 1dp violet hairline, icon 16dp violet
+ label 12sp SemiBold ink. Active = violet gradient fill, white icon + white text,
4dp violet glow.

**KPI tiles (refs 4,5):** 4 equal tiles, 84dp, white, radius 18dp:
```
[icon 18dp violet]
81%            <- value 15sp ExtraBold, green when positive
Attendance     <- label 10sp SemiBold muted
```
Active tile (Payment) = violet gradient fill, all content white, value stays white.
Body tile value is inline: `69.5 kg ▲+2.1 kg`.

**Payment sub-nav (ref 4):** pills `Overview | History | Plan & Changes | Behaviour |
Analytics`, active = violet gradient.

## 6. Attendance Intelligence

Card header: 40dp violet gradient tile w/ calendar-check glyph, `Attendance Intelligence`
19sp ExtraBold, right white dropdown pill `Oct 2026 ▾`.

Layout: 2 columns.

Left column (≈38% width):
- **Ring**: 130dp, 14dp stroke, gradient green→lime, `81%` 34sp ExtraBold ink,
  `Attendance` 12sp, `25 / 31 Days` 10sp muted.
- **Current Streak** card: violet gradient, radius 16dp, 🔥 16dp, `Current Streak` 9sp
  white 80%, `6 Days` 16sp ExtraBold white.
- **Avg. Check-in Time** pill card: 🕐 violet, `Avg. Check-in Time` 9sp muted,
  `07:18 AM` 12sp Bold + `↓ (-12m)` 9sp green.

Right column:
- **4 stat tiles** (2×2 look but actually one row of 4 in refs): ✅23 Present,
  🕐2 Late, ✖️2 Absent, 📅4 Holiday. Each 72dp, white, icon 20dp coloured
  (green/amber/red/grey), value 17sp ExtraBold, label 9.5sp.
- **Calendar**: weekday header row Mon–Sun (Sun in red), 6 rows of day pills 30dp
  rounded-square:
  - present `#16A34A` fill + white ✓-prefixed number
  - late `#F59E0B` fill
  - absent `#DC2626` fill
  - holiday/future `#E9E2F5` fill, muted ink
  - today = 2dp green ring, white fill, green ink
- **Trend card**: header `Attendance Trend` + sub `Nov 2025 – Oct 2026`, right
  `12 Months ▾`. Area chart: violet line 2.5dp, dots 8dp white-filled with violet
  stroke, violet→transparent area fill. Callout pills: 🏆`97% Jan` green, `58% Mar` red,
  `81% Oct` violet. Y `0/25/50/75/100%`, X `Nov…Oct`.
- **Trend footer strip**: 5 items separated by hairlines — 🔥6 Days Current Streak,
  📊81% Avg. Attendance, 🏆97% Best (Jan), 📉58% Lowest (Mar), 📅8/12 Tracked Months.

**Smart Insight card**: ✨ violet tile + `Smart Insight` 15sp ExtraBold + `AI` lavender
pill badge; 💡 + body 12sp; right green card `Next Goal ≥ 90%` (🎯 26dp).

## 7. Workout Intelligence

- Header tile dumbbell, title `Workout Intelligence`, right `Oct 2026 ▾`.
- **Ring**: `19.4 hr` 30sp, `Total Workout` 11sp, gradient green→violet, 14dp stroke.
- **4 stat tiles**: ⏱37 min Avg/Day, 👑88 min Best, 📉32 min Lowest, 🎯61% Consistency.
- **Daily Workout Trend**: violet line + violet dots + **green vertical gradient bars**
  behind each point; callouts `47m` violet, `88m` green + crown, `32m` red, `74m` violet.
  Y `0m/23m/45m/68m/90m`, X `1…31`.
- **4-tile footer**: 🏃19/31 Active Days, 🔥5 Days Current Streak, 📅18/30 Goal Days,
  🎯61% Consistency.
- **Monthly Workout Pattern (This Year)**: right `⏱ Total: 256.0 hr`; rounded-top bars
  10dp radius, violet gradients, `36h` above, month inside; Mar red `12h`, Jun dashed
  grey placeholder `–`, Jul green `44h` + crown.
- **Smart Insight**: same shell + right `View Plan ›` pill; 3D flexing-arm prop.

## 8. Body Intelligence

- Title block: violet person tile, `Body Intelligence`, sub
  `Composition • Measurements • Progress`, right `Oct 2026 ▾`.
- **Hero**: 150dp wide member photo, radius 22dp, violet gradient backdrop + glow.
- **Transformation Goal** card: 🎯 `Transformation Goal`, sub
  `Muscle Gain • Target 72.0 kg`, right green pill `✓ On Track`.
  - Ring 46%, 92dp, gradient green→violet, 🏋️ glyph inside, `46%` 26sp.
  - Right rows: 🚩72.0 kg Goal, 🏋️69.5 kg Current, ⚖️67.4 kg Start.
  - Bottom 3 delta tiles: `+2.1 kg Weight` (green up), `+2.6 kg Muscle` (violet),
    `-1.8% Body Fat` (red down).
- **Body Composition** card: multi-colour donut (green/violet/magenta) with body
  silhouette, rows `BMI 22.8 [Normal]`, `Body Fat 17.6% [Athletic]`,
  `Muscle 57.2 kg [Good]` — status pills green tinted.
- **Measurements** card: right pill `vs. Last`; rows 💪Chest 98.0 cm `↑+2.0 cm`,
  📏Waist 82.0 cm `↓-1.0 cm`, 💪Arm 34.0 cm `↑+1.0 cm` (positive green, negative red).
- **Weight & Composition Trend**: legend ●Weight purple ●Muscle green ●Body Fat orange;
  3 lines with end labels `69.5 kg` purple, `57.2 kg` green, `17.6%` orange;
  Y `60/64/68/72/76`, X `May…Oct`.
- **Bottom row**: `Gallery` card (📷 + 3 thumbs + dashed `+ Add`), `AI Insights` card
  (✨ AI badge + 3 ✓ lines), `Next Target` green card (🎯 `Next Target 72.0 kg →`).

## 9. Payment Intelligence

Two variants must both be supported.

**Variant A (ref 4) — status-first**
- `Payment Status` card: 40dp green ✓ disc, `Paid` 24sp green, `Valid till 07 Nov 2026`;
  divider; 👑Gold Plan / 30 Days / ₹1,200; right `Next Renewal 07 Nov 2026` + green
  `8 Days Left` pill.
- 4 tiles: 💳₹14,400 Lifetime Paid, 📚12 Total Cycles, 🎁₹1,200 Total Discount,
  🔄0 Outstanding.
- `Payment Behaviour`: ring `86/100 Reliability`; bars On Time 8(67%) green,
  Early 2(17%) blue, Late 2(17%) amber; footer 📅3d Avg. Late, ⚡5d Avg. Early, ⚠️0 Overdue.
- `Plan History`: 4-dot vertical timeline with coloured dots.
- `Monthly Payment Trend (₹)`: pure line+area, violet; `Plan↑` violet callout above
  Apr→May; Apr `0` red dot; Y `0/0.5k/1.0k/1.2k/1.5k`, X `Jan…Oct`.
- `AI Insight` + `Suggestion` split card with `›` chevron.

**Variant B (ref 5) — summary-first**
- `Plan Status` green gradient card + `Lifetime Paid` ₹11,700 with right stat column.
- `Payment Behaviour` with progress bars + `Reliability Score` ring `86%`.
- `Membership & Payment Trend`: legend ●Payment ●Plan Change ●Gap/Inactive; dashed red
  gap segment to zero, blue `Plan Upgrade` callout, orange `Late (+3d)` callout,
  red `Gap (No Plan)` callout.
- `Membership History` timeline: ✓Oct Renewed ₹1,200, ↑May Upgrade ₹1,200,
  ✕Apr Gap —, 🕐Mar Late ₹1,000, ✓Jan Joined ₹1,000.
- `Key Insights` (AI) 4 ✓ lines + green `Next Renewal Reminder 03 Nov 2026`.

---

## 10. Interaction patterns to honour

1. Check-in CTA is stateful: `CHECK-IN` → `CHECKED IN · Today, 07:18 AM`, green gradient.
2. Active tab, active sub-nav and active chips all use the violet gradient fill.
3. Every trend chart has 1–4 labelled callouts max, never colliding; crown marks the peak.
4. Every intelligence card ends with a Smart/AI Insight strip — never an empty footer.
5. Rings are always two-stop gradients, never flat strokes.
6. Numbers are tabular and always paired with a unit or denominator.
7. Empty/gap states are explicit (`–`, dashed bar), never fabricated values.
