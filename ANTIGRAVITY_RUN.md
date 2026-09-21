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
