# BAD GYM – Member Intelligence UI
## Ready for Google Antigravity

### How to run in Antigravity / AI Studio

1. Pull the latest code:
```bash
git pull origin main
```

2. In Google Antigravity / AI Studio:
   - Open this repository
   - Or paste:  
     “Build and run the Member Intelligence feature. Use the 8 themes already defined in ThemeId. The main entry is MainActivity → MemberIntelligenceScreen.”

3. The app launches directly into the themed Member Intelligence screen with a theme switcher at the top.

### What you get
- All 8 themes (Natural Fresh, Futuristic Neon, Minimal Dark, Glassmorphism, Premium 3D, Vibrant Gradient, Gym Beast Mode, Purple Royal)
- Side navigation rail
- Hero member card with photo, name, motto
- Attendance / Payment / Workouts metrics
- Theme-aware CTA button
- Fully data-driven (MemberSnapshot + ViewModel)

### Architecture reminder
```
feature/memberintelligence/
├── design/          ← ThemeId, colors, shapes, motion
├── domain/          ← models + intelligence engine
├── data/            ← Room + repository
└── presentation/    ← Screens + components
```

Just pull and run. The UI matches the design sheet.

## 2026-09-29 Mobile Home Intelligence V2

The Home experience was updated to combine the practical phone geometry of the existing Member Intelligence card with the richer Home information architecture.

### New Home layer
- `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/MobileHomeIntelligence.kt`
- Responsive target: 320dp minimum, 360–430dp primary.
- Home is still the default menu.
- Tapping Home expands the Home workspace to the full available mobile surface and hides the rail.
- Expanded Home has a collapse/back affordance.
- Includes member hero, today status, five KPIs, recent activity, weekly overview, body progress, focus area, upcoming, new-at-gym, AI insights, More data and the primary CTA.
- Uses existing MemberSnapshot, current event and intelligence signals.
- No new external dependency was added.

### Design specification
Read `docs/HOME_MOBILE_REDESIGN_PROMPT.md` before making further UI changes. It contains the exact mobile constraints, information architecture and Antigravity acceptance criteria.

### Validation
The GitHub workspace could not be built from the assistant runtime because outbound GitHub network access is unavailable in that runtime. Run:
```
./gradlew assembleDebug
```
then validate first on a narrow phone emulator and then a 360–430dp device.
