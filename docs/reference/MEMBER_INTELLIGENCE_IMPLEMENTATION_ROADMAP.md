# BAD GYM — Member Intelligence Dashboard
## Implementation & Verification Roadmap

---

### 1. Phased Implementation Plan

This roadmap outlines the atomic engineering phases to deliver the pixel-perfect Member Intelligence Dashboard into the active production module (`:app`, package `com.example.badnewgym.feature.membercard`).

```
+-----------------------------------------------------------------------+
| PHASE 1: TOKEN & GRAPHICS FOUNDATION                                  |
| - Update AquaGlassTokens.kt with exact colors, brushes, and radii     |
| - Add Bento & Specular glass modifiers and custom shapes              |
+-----------------------------------┬-----------------------------------+
                                    │
                                    ▼
+-----------------------------------------------------------------------+
| PHASE 2: ATOMIC COMPONENT IMPLEMENTATION                              |
| - Build DashboardTopBar composable                                    |
| - Build MemberCenterpieceHero with dual halo ring & crown pin         |
| - Build 4 Bento Cards (Attendance, Payment, Workout, Body Progress)   |
| - Build DualBarLineCanvasChart with bezier spline & floating tooltip  |
| - Build DetailedOverviewCard with timeframe selector & target badges  |
| - Build FloatingGlassBottomDock with active glowing pill             |
+-----------------------------------┬-----------------------------------+
                                    │
                                    ▼
+-----------------------------------------------------------------------+
| PHASE 3: SCREEN ASSEMBLY & DATA BINDING                               |
| - Assemble MemberDashboardV2Screen integrating all atomic components  |
| - Connect to MemberCardData and state models                          |
| - Wire interactive sheets (Attendance, Receipt, Analytics, etc.)      |
+-----------------------------------┬-----------------------------------+
                                    │
                                    ▼
+-----------------------------------------------------------------------+
| PHASE 4: BUILD, TEST & VERIFICATION                                   |
| - Execute Gradle unit tests and debug assembly:                       |
|   ./gradlew clean testDebugUnitTest assembleDebug                     |
| - Deploy & capture screenshot on connected test device / emulator     |
| - Run pixel-perfect visual comparison against reference image         |
| - Record execution ledger update and verified commit on main          |
+-----------------------------------------------------------------------+
```

---

### 2. Target File Modifications & Creations

| File Path | Nature | Description |
| :--- | :--- | :--- |
| `feature/membercard/design/AquaGlassTokens.kt` | **Modify** | Add exact jewel-tone bento gradients, specular glares, and typography tokens. |
| `feature/membercard/presentation/components/DashboardTopBar.kt` | **Create** | Edge-to-edge top bar with squircle hamburger, BAD GYM title, date pill & bell. |
| `feature/membercard/presentation/components/MemberCenterpieceHero.kt` | **Create** | Centered circular profile with neon cyan/mint halo ring, crown badge, and status pills. |
| `feature/membercard/presentation/components/BentoIntelligenceGrid.kt` | **Create** | 2x2 bento cluster (Attendance, Payment, Workout, Body Progress). |
| `feature/membercard/presentation/components/DetailedOverviewCard.kt` | **Create** | Comprehensive analytics card with interactive canvas chart and target badges. |
| `feature/membercard/presentation/components/FloatingGlassBottomDock.kt` | **Create** | Capsule bottom navigation bar with active glow effect. |
| `feature/membercard/presentation/MemberCheckInScreen.kt` | **Update** | Wire the new dashboard as the default primary view. |

---

### 3. Verification Protocol & Quality Checklist

In accordance with `AGENTS.md` and repository guidelines:
1. **Compilation & Unit Tests**:
   ```powershell
   ./gradlew clean testDebugUnitTest assembleDebug
   ```
   *Must complete with exit code 0.*
2. **Visual Checklist against Reference Image**:
   - [ ] Background displays fluid luminous mint aurora gradient.
   - [ ] Center avatar has dual glowing cyan/mint halo ring with gold crown pin at top-right.
   - [ ] "Active" emerald pill is centered beneath the circular avatar.
   - [ ] 4 Bento cards match exact radiant gradients:
     - Attendance: Radiant Spring Green to Emerald `#00E676` -> `#059669`.
     - Payment: Amber to Flame Orange `#FFA000` -> `#EA580C`.
     - Workout: Electric Cyan to Royal Cobalt `#00E5FF` -> `#1D4ED8`.
     - Body Progress: Magenta to Vivid Violet `#F43F5E` -> `#8B5CF6`.
   - [ ] Detailed Overview card shows 16 gradient bars, smooth curved line, and floating tooltip (`85% (18/21)`).
   - [ ] Bottom targets show 3 pills (Fit & Strong, 68 kg, 500 kcal/day).
   - [ ] Floating navigation dock renders smoothly at bottom with glowing Home pill.
3. **Responsive Reflow**:
   - Card fits cleanly within a standard 20:9 viewport (691x1536 / 360-412dp width) without awkward cutoffs or overflow distortions.
