# BAD GYM — Member Intelligence Card System

A production-grade, pixel-perfect Android application built using Jetpack Compose, implementing an 8-theme dynamic **Member Intelligence Card** for gym and fitness club management.

---

## 📸 Verified Device Running Preview

![BAD GYM Pixel-Perfect Member Intelligence Card](docs/screenshots/badgym_screenshot.png)

---

## 🚀 Key Highlights & Architecture

- **8 Distinct Visual Themes**:
  1. **Natural Fresh** — Wellness, Clean, Friendly (*Yash Singh - BG204*)
  2. **Futuristic Neon** — Bold, Energetic, High Tech (*Arjun Mehta - BG105*)
  3. **Minimal Dark** — Simple, Elegant, Focused (*Riya Sharma - BG310*)
  4. **Glassmorphism** — Translucent, Modern, Elegant (*Neha Kapoor - BG407*)
  5. **Premium 3D** — Luxury, Stylish, Premium (*Kabir Oberoi - BG001*)
  6. **Vibrant Gradient** — Youthful, Dynamic, Colorful (*Aarav Patel - BG220*)
  7. **Gym Beast Mode** — Powerful, Intense, Motivational (*Rohan Varma - BG330*)
  8. **Purple Royal** — Elegant, Royal, Exclusive (*Simran Kaur - BG502*)

- **Composition & Layout**:
  - **3:4 Aspect Ratio Card Shell** with 8dp outer gutter and 24dp rounded corners.
  - **Top Horizontal Theme Strip**: Fast, smooth switching across all 8 theme personalities with active badge highlighting.
  - **54dp Vertical Navigation Rail**: Accessible navigation rail for *Home, Attend, Plan, Pay, Trainer, Workout, and More*.
  - **Verified Member Identity**: Circular avatar with dual verification badges, member code, and signature theme motto.
  - **Interactive KPI Rings & Metrics**: Attendance progress ring, payment status tile, workout counter, and shortcut triple.
  - **Actionable Intelligence Signals**: Priority-ranked (P0 Critical, P1 Action Required, P2 Important) intelligence signals with contextual actions.
  - **Dominant CTA Button**: Gradient action button (e.g. *Collect Payment →*).
  - **Edge-to-Edge Safe Padding**: Built-in `statusBarsPadding()` and `navigationBarsPadding()` ensuring compatibility with any screen cutout, notch, or navigation bar.

---

## 🛠️ Build & Run Commands

### Prerequisites
- Android Studio Ladybug / Meerkat or newer
- JDK 17+ (JDK 21 / 24 JVM target)
- Android SDK (API 34/35)

### Command Line
```bash
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device or emulator
./gradlew installDebug

# Launch main activity
adb shell am start -n com.example.badnewgym/.MainActivity
```

---

## 📂 Project Structure

```
app/src/main/java/com/example/badnewgym/
├── MainActivity.kt                          # Main Activity entry point with Edge-to-Edge
└── feature/memberintelligence/
    ├── data/repository/                     # Member data repositories
    ├── design/                              # Centralized Design System
    │   ├── BADGymTheme.kt                   # CompositionLocal theme provider
    │   ├── ThemeId.kt                       # 8 Theme definitions & metadata
    │   └── colors/ColorTokens.kt            # BADGymColors palettes for all 8 skins
    ├── domain/                              # Business domain models & intelligence engine
    │   ├── engine/MemberIntelligenceEngine.kt
    │   └── model/                           # MemberSnapshot, Event, Signal, Menus
    └── presentation/
        ├── MemberIntelligenceScreen.kt      # State collector & safe insets container
        ├── MemberIntelligenceViewModel.kt   # Unidirectional state flow ViewModel
        └── components/
            ├── PixelPerfectMemberCard.kt    # Main 8-theme pixel-perfect card
            ├── CardHeader.kt                # Top BAD GYM branding & slogan badge
            ├── HeroMemberSection.kt         # Portrait, verified badge & motto
            ├── IntelligenceRail.kt          # 54dp vertical navigation rail
            ├── CardMetricsGrid.kt           # KPI ring progress & due status tiles
            ├── MenuContentPanels.kt         # Attendance, Plan, Pay, Trainer, Workout, Services
            ├── ThemedCtaButton.kt           # Dynamic gradient action CTA
            └── CardFooterSection.kt         # Theme signature motivational footer
```

---

## 📄 Documentation
- [Design Specification](docs/PIXEL_PERFECT_CARD_SPEC.md)
- [Walkthrough & Architecture Report](docs/WALKTHROUGH.md)
