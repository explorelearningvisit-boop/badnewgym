# BAD GYM — Project Audit

Audit date: 2026-11-10
Branch: `feature/member-intelligence-v3`
Last verified commit before this audit: `6820770`
Device used for verification: Xiaomi 11i 5G `zxdada69gunb7ls4` (1080×2400 @ 440dpi)

---

## 1. Stack

| Item | Value |
|---|---|
| Language | Kotlin (99 files under `app/src/main/java`) |
| UI | Jetpack Compose + Material 3 (`androidx.compose.material3`) |
| Build | Gradle 9.6, AGP with Kotlin Compose plugin, KSP |
| compileSdk / targetSdk | 37 |
| minSdk | 24 |
| Application ID | `com.example.badnewgym` |
| Entry point | `MainActivity` → `feature/membercard/presentation/MemberCheckInScreen` |
| Persistence | `SharedPreferences` (appearance), Room (`hybridtracking/db`) |
| Networking | Retrofit + OkHttp + Gson (`hybridtracking/network`) |
| Background work | WorkManager (`KeepAliveWorker`, `TelemetrySyncWorker`) |
| Images | Coil, bundled `R.drawable` fixtures |

There is **no Supabase/Firebase client and no backend API** for Member Intelligence.
The dashboard renders from `MemberCardData` / `BadGymMemberPass` domain models, and the
current instance is `MemberCardData.sampleAshwiniSingh()`.

## 2. Module map

```
com.example.badnewgym
├── MainActivity.kt                     // whole-screen Compose host
├── BadGymApp.kt
├── deviceagent/                        // keep-alive, accessibility service, Tailscale, MCP server
├── hybridtracking/                     // Room + Retrofit telemetry (Ble/Wifi/Cell scanners)
├── feature/
│   ├── membercard/
│   │   ├── domain/model/               // MemberCardData, BadGymMemberPass, CalendarDay
│   │   ├── design/                     // pack theme engine, member-card tokens/accents
│   │   ├── presentation/
│   │   │   ├── MemberCheckInScreen.kt  // shell that mounts the dashboard
│   │   │   ├── intelligence/           // ← the Member Intelligence dashboard (this work)
│   │   │   └── components/             // sheets: quick actions, search, receipt, notifications
│   │   └── data/MemberBodyIntelligenceMapper.kt
│   ├── bodyprogress/                   // body analytics, modular workspace, charts
│   └── screenpulse/                    // screen recording/encoding service
└── ui/theme/                           // legacy Material theme (not used by the dashboard)
```

## 3. Member Intelligence surface (the part being replaced)

| Concern | File |
|---|---|
| Shell, tab state, density scaling, font injection | `intelligence/MemberIntelligenceDashboard.kt` |
| Header, identity, progress, KPI tabs | `intelligence/IntelligenceHeader.kt` |
| Palette, neumorphic modifiers, card/pill/chip primitives | `intelligence/IntelligenceTheme.kt` |
| Theme + appearance state (persisted) | `intelligence/IntelligenceAppearance.kt` |
| Typography scale | `intelligence/IntelligenceTypography.kt` |
| Spacing / radius / icon scale | `intelligence/IntelligenceDensity.kt` |
| Attendance module | `intelligence/AttendanceIntelligenceScreen.kt` |
| Workout module | `intelligence/WorkoutIntelligenceScreen.kt` |
| Payment module (+4 sub-views) | `intelligence/PaymentIntelligenceScreen.kt` |
| Body module (lite) | `intelligence/BodyIntelligenceLiteScreen.kt` |
| Body workspace nav + modules | `bodyprogress/presentation/intelligence/BodyModularWorkspace.kt` |
| ☰ sheet incl. appearance panel | `components/MemberQuickActionsSheet.kt` |

## 4. Known defects found during the audit

1. **Duplicate/divergent revisions.** Several files on disk were not the revision described
   by the previous session summary (e.g. `IntelligenceHeader.kt` still used raw `sp` values and
   `Color(0xFF7C3AED)` literals). Typography and colour tokens have now been re-applied.
2. **Header overflow at non-default text scale.** `BAD GYM` wrapped to two lines and the
   `CHECKED-IN` pill clipped. Fixed with single-line + ellipsis and a dedicated `CHECKED` label.
3. **Payment sub-nav clipped.** Five pills in a horizontal scroller were cut mid-word.
   Replaced with a 3+2 equal-width grid.
4. **Chart axis clipping.** Payment/body/workout charts drew Y labels at `x = 0` and callout
   badges on the plot edge. Paddings and label anchoring corrected.
5. **Payment trend badge bug.** A mis-ordered `if/else` drew the `Plan ↑ 1.5k` badge on every
   data point. Corrected to May only, sized to its text.
6. **`coerceAtMost` on `TextUnit`** does not compile; type caps must use concrete `sp` tokens.

## 5. Constraints that shape the redesign

- No backend for member data → all values remain fixture-driven; the design must not pretend
  to be live. Real-data wiring is a separate integration phase.
- Gilroy/SF Pro cannot be shipped; Plus Jakarta Sans (OFL) is bundled at `res/font/plus_jakarta.ttf`.
- Compose `shadow()` cannot offset or dual-colour, so neumorphism is drawn with
  `android.graphics.Paint.setShadowLayer` inside `neuRaised` / `neuSunken` / `neuAccent`.
- `LazyColumn` items cannot weight-stretch; a hard "fill the viewport" rule is not enforceable,
  so density is tuned per module instead.

## 6. Build verification

```
./gradlew assembleDebug            → BUILD SUCCESSFUL
./gradlew testDebugUnitTest        → BUILD SUCCESSFUL (last run at 6820770)
```
