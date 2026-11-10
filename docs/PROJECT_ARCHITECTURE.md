# BAD GYM — Project Architecture

## 1. Runtime shape

```
Process
└── MainActivity (ComponentActivity, edge-to-edge, keep-screen-on)
    ├── DeviceAgentRuntime.attach(this)        // foreground service + watchdog
    └── setContent { MemberCheckInScreen() }
        └── MemberIntelligenceDashboard(data, pass)
            ├── LocalDensity  = baseDensity × NeuStyle.componentScale
            ├── LocalTextStyle = BadGymSans, Bold
            └── key(NeuStyle.themeId)
                ├── IntelligenceTopBar / MemberIdentityCard / MembershipProgress
                ├── KpiTabRow  →  selectedTab 0..3
                └── LazyColumn
                    ├── AttendanceIntelligenceScreen
                    ├── WorkoutIntelligenceScreen
                    ├── PaymentIntelligenceScreen
                    └── BodyIntelligenceLiteScreen → BodyModularWorkspace
```

## 2. Layers

| Layer | Package | Responsibility |
|---|---|---|
| Domain | `feature/membercard/domain` | `MemberCardData`, `BadGymMemberPass`, `IntelligenceSeries` — pure Kotlin, no Android |
| Body domain | `feature/bodyprogress/domain` | aggregators, analytics, insight engine, ledger — pure Kotlin |
| Data | `feature/membercard/data`, `feature/bodyprogress/data` | mappers + fixture source |
| Presentation | `feature/*/presentation` | Compose screens, view-state derived with `remember` |
| Design | `feature/membercard/design`, `presentation/intelligence/IntelligenceTheme.kt` | tokens, palette, neumorphic modifiers |
| Telemetry | `hybridtracking` | Room DB, Retrofit sync, scanners, workers |
| Device agent | `deviceagent` | keep-alive, accessibility, Tailscale, MCP server |
| Recovery | `screenpulse` | screen recording/encoding service |

## 3. State ownership

- `NeuStyle` — process-wide singleton `object`, `mutableStateOf` fields, persisted in
  `SharedPreferences("badgym.neu.style")`, attached to a context once from the dashboard.
- `selectedTab` — `rememberSaveable` inside the dashboard; survives rotation, resets on a
  theme change because the subtree is re-keyed.
- `BodyIntelligenceState` — `rememberBodyIntelligenceState(bodyInput)`; holds primary/secondary
  tab selection, sub-tab selection, gallery paging.
- Payment sub-tab — `rememberSaveable` inside `PaymentIntelligenceScreen`.

## 4. Data flow

`MemberCardData.sampleAshwiniSingh()` (fixture)
→ `MemberBodyIntelligenceMapper.from(data)` produces `BodyIntelligenceInput`
→ `rememberBodyIntelligenceState` → derived analytics
→ screens render. No repository, no network, no cache invalidation.

## 5. Theming pipeline

1. `NeuStyle.themeId` changes → `save()` → recomposition.
2. `key(NeuStyle.themeId)` recreates the subtree so no draw-time colour is stale.
3. `NeuPalette.*` are property getters that read `NeuStyle.theme` at call time.
4. `neuRaised` / `neuSunken` / `neuAccent` paint via `android.graphics.Paint.setShadowLayer`
   inside a `drawBehind`, because Compose `Modifier.shadow` cannot offset or dual-colour.

## 6. Scaling pipeline

- Text: `Density.fontScale × NeuStyle.textScale` — every `sp` scales.
- Components: `Density.density × NeuStyle.componentScale` — every `dp` scales.
- Radius/depth: multipliers applied inside the neumorphic modifiers.
- `IntelligenceTypography` / `IntelligenceDensity` hold the base values; no screen hardcodes.

## 7. Extension points

- New skin → add a `NeuTheme` to `NeuThemes.all`; nothing else changes.
- New module tab → add to `KpiTabRow` + a `when(selectedTab)` branch.
- Real data → replace the fixture at the `MemberCheckInScreen` call site; screens already
  accept domain models, not literals.
