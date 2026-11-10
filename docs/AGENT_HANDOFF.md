# AGENT_HANDOFF

Read this before changing anything in the Member Intelligence dashboard.

## Ground rules

1. Work only on `feature/member-intelligence-v3`. The user's `main` stays untouched.
2. Never rewrite a file from an assumed state — read it first. Previous sessions found
   on-disk revisions that differed from the previous summary.
3. All dashboard typography goes through `IntelligenceTypography`; all spacing through
   `IntelligenceDensity`; all colours through `NeuPalette`. No raw `sp`, `dp` gaps or hex
   literals in the intelligence package.
4. `NeuPalette` is theme-backed at draw time. Anything cached across a theme switch must be
   rebuilt under `key(NeuStyle.themeId)` (already done at the dashboard root).
5. Verify on the device, not by compilation alone.

## Where things live

```
intelligence/MemberIntelligenceDashboard.kt   shell + LocalDensity scaling + font injection
intelligence/IntelligenceAppearance.kt        NeuTheme / NeuThemes (7 skins) / NeuStyle
intelligence/IntelligenceTheme.kt             NeuPalette, neuRaised/Sunken/Accent, cards/pills
intelligence/IntelligenceTypography.kt        bold type scale
intelligence/IntelligenceDensity.kt           spacing / radius / icon tokens
intelligence/IntelligenceHeader.kt            top bar, identity, progress, KPI tabs
intelligence/AttendanceIntelligenceScreen.kt  tab 0
intelligence/WorkoutIntelligenceScreen.kt     tab 1
intelligence/PaymentIntelligenceScreen.kt     tab 2 (+4 sub-views)
intelligence/BodyIntelligenceLiteScreen.kt    tab 3
bodyprogress/.../BodyModularWorkspace.kt      body primary/secondary nav + modules
components/MemberQuickActionsSheet.kt         ☰ sheet incl. appearance panel
```

## Gotchas learned the hard way

- `TextUnit` has no `coerceAtMost`; cap type with a concrete token instead.
- `Dp` is not comparable to `Float`; write `> 0.dp`.
- Neo shadows need `android.graphics.Paint.setShadowLayer`; Compose `shadow()` cannot offset.
- Files are LF on disk. PowerShell here-strings containing `\n` silently fail to match;
  use `[Regex]` with `\r?\n`, or edit by line index.
- `[Regex]::Replace(str, pat, repl, 1)` does **not** limit to one match.
- `adb` screenshots come back 900×2000 while the device is 1080×2400 — multiply image
  coordinates by 1.2 before `input tap`.
- The app restores the last tab *and* the body workspace (`Body → More` is common).
  Always read a fresh screenshot before tapping a tab tile.
- CIUI/MIUI re-enables auto-rotate; re-apply `accelerometer_rotation 0` + `user_rotation 0`
  before every capture.
- `pm clear` resets the appearance sliders (good for baseline screenshots, but it also
  resets the selected tab).

## Build & deploy

```powershell
cmd /c "set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr&& gradlew assembleDebug --console=plain --no-daemon"
adb -s zxdada69gunb7ls4 install -r app\build\outputs\apk\debug\app-debug.apk
```

## Definition of done for a UI checkpoint

- [ ] `assembleDebug` green
- [ ] `testDebugUnitTest` green
- [ ] all four tabs captured from the device
- [ ] no clipped label, no overlap, no hard-cut nav row
- [ ] `docs/IMPLEMENTATION_PROGRESS.md` updated
- [ ] commit with an evidence list
