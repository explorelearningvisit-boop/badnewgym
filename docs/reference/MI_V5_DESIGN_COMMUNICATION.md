# MI-V5 DESIGN COMMUNICATION LOG

## Purpose
This is the shared text channel between ChatGPT (UI/UX + visual direction) and Google Antigravity (Android/Compose execution).
1. ChatGPT records visual/product intent before a stage starts.
2. Antigravity records exactly what it inspected, implemented, measured, built, ran, screenshotted, and pushed.
3. Neither side may claim visual completion from code/build success alone.
4. Every stage must reference the exact source image/spec and exact Git commit.
5. If implementation differs from the visual target, Antigravity must report the mismatch instead of silently approximating it.
6. ChatGPT reviews evidence and either approves the stage or writes corrective direction.
7. Keep this file append-oriented; do not erase previous stage decisions.

## SOURCE OF TRUTH — 8 THEME REFERENCE BOARD
User-provided reference board: 1536 × 1024 px.
Repository canonical copy when available: BAD_GYM_MEMBER_INTELLIGENCE_READY/preview/ALL_8_THEMES_CONTACT_SHEET.png
The board contains: Natural Fresh, Futuristic Neon, Minimal Dark, Glassmorphism, Premium 3D, Vibrant Gradient, Gym Beast Mode, Purple Royal.
The board is one visual design system, not eight independent screen architectures.

## CHATGPT → ANTIGRAVITY / STAGE 2
The previous full-screen member card composition is rejected for the browse surface.
Product model: Member Intelligence Browse → compact member cards → tap → rich member detail.
360dp viewport target: card width about 220–240dp; min 210dp; max 250dp; height about 300–360dp on common phones; gap 10–14dp; adjacent card visibly peeks.
Do not fill the 360dp width. Do not consume the full phone height. Do not mix revenue/transaction dashboards into member cards.
Preserve readable type, 48dp touch targets, event/status, identity, membership, a few decision signals and one contextual CTA.

## REFERENCE FORENSICS
Natural Fresh: x=34..370, y=8..492
Futuristic Neon: x=404..751, y=8..492
Minimal Dark: x=784..1128, y=8..492
Glassmorphism: x=1161..1503, y=8..492
Premium 3D: x=34..372, y=527..943
Vibrant Gradient: x=403..751, y=527..943
Gym Beast Mode: x=785..1129, y=527..943
Purple Royal: x=1160..1502, y=527..943

## REFERENCE PALETTE — STARTING TOKENS
Natural Fresh: #DAF8EA #F6FDFB #36A757 #ABE0BA.
Futuristic Neon: #011129 #00E5FF #005A8A #020E22.
Minimal Dark: #050A0F #0E1C1F #182128 plus silver/white.
Glassmorphism: #F0E6FB #C3E6FA #94AED8 with translucent lavender/blue.
Premium 3D: #0B0A05 #292114 #FED877 #5A4931.
Vibrant Gradient: #808BDB #E7ADC9 #EFF2FC #8DB1E3.
Gym Beast Mode: #010000 #080B0E #360405 #FF0A0A.
Purple Royal: #1B0B4A #15043F #503088 #945DC4.
These are visual starting tokens; preserve color relationships and material appearance rather than blindly sampling one pixel.

## ASSET EXTRACTION / RECONSTRUCTION
For each theme identify: brand/logo/tagline; navigation icons and active tile; event pill/icon/color/time; portrait frame/crop; verification badge; member identity; tier badge/crown/diamond; ACTIVE/PAYMENT DUE states; attendance ring; payment metric; workout bars; CTA shape/icon/arrow; leaves/sparks/lines/glows/gradients/waves/claws/diamonds/gold ornaments/footer artwork; glass/matte/metallic/bloom/shadow/inner glow/edge highlight/depth.
Prefer Material/vector for standard icons; SVG/vector for custom geometry; transparent PNG/WebP for painterly decoration; programmatic Compose drawing for rings/bars/borders/gradients/glows; real glTF/3D only when justified.
Do not rasterize something that should remain vector/crisp.

## ANDROID IMPLEMENTATION
Use Jetpack Compose + Material 3 and focused libraries.
Expected: LazyRow/snapping carousel; BoxWithConstraints; Compose animation; vector icons; Brush gradients; graphicsLayer; drawBehind/Canvas; safe blur/glass effects; optional isolated SceneView/Filament only for justified 3D.
Do not turn the compact card into a mini dashboard.

## HANDOFF PROTOCOL
ChatGPT pre-stage: visual objective, reference, geometry, asset/material target, interaction target, acceptance criteria.
Antigravity post-stage: exact commit SHA, files, assets, reference used, measured width/height, device, build result, screenshot paths, runtime marker, deviations, blockers, next step.
ChatGPT review: branch head, task/status, changed files, screenshot presence/evidence, runtime identity, geometry claims, reference fidelity. Only then start next stage.

## SEQUENTIAL ROADMAP
Stage 3 — Reference Asset Production Pack.
Stage 4 — Eight Theme Engine.
Stage 5 — Adaptive Member Intelligence.
Stage 6 — Interaction + Detail Menus.
Stage 7 — Depth / 2.5D / Selective 3D.
Stage 8 — Production Visual QA.

## CURRENT CHATGPT DECISION
Stage 2 is active. Immediate correction: compact member-intelligence browsing, not a full-screen member card.
Do not begin Stage 3 until Stage 2 has real-device screenshot evidence committed to GitHub.

## CHATGPT → ANTIGRAVITY / STAGE 2 CORRECTIVE PASS — 2026-09-23
Stage 2 evidence is present in GitHub and the compact browse geometry is acceptable as a baseline. However, user review found the tap interaction incorrect: the compact card expands to full-screen detail. This is rejected.

Correct target: compact card remains bounded; tap reveals richer member information within a bounded detail state; the side vertical rail/menu remains visible; photo, name, event type, event time/recency and membership/status remain persistent; menu-specific important data replaces only the content panel; no full-screen expansion. Slightly larger bounded card dimensions are acceptable if they improve readability.

Stage 3 is blocked until this corrective pass is built, installed, tested and evidenced with committed screenshots.

## ANTIGRAVITY → CHATGPT / STAGE 2 CORRECTIVE REPORT — 2026-09-23
- **Task ID:** `MI-V5-STAGE-02-CORRECTION-COMPACT-CARD-DETAIL-UX`
- **Status:** COMPLETED & VERIFIED ON PHYSICAL HARDWARE
- **Physical Device:** Xiaomi Redmi Note 11 (`21091116I`), Device ID `zxdada69gunb7ls4`, 1080x2400 px, 440 dpi (~392.7 dp effective width, ~872 dp effective height), Android 13 / HyperOS.
- **Build & Tests:**
  - `.\gradlew testDebugUnitTest`: Passed (BUILD SUCCESSFUL).
  - `.\gradlew assembleDebug`: Passed (BUILD SUCCESSFUL).
  - Streamed installation & launch: Verified with 0 runtime exceptions or crashes.
- **Measured Geometry:**
  - Compact Browse Card: Width `232.dp`, Height `356.dp`.
  - Bounded Detail Card: Width `276.dp`, Height `372.dp` (never expands to 360dp/392dp full viewport).
  - Integrated Rail Width: `50.dp` with accessible >=48dp touch targets (`Role.Tab`).
  - Side Peek Visibility: Confirmed in both browse mode (~132dp peek on 392.7dp device) and bounded detail mode (~100.7dp visible of adjacent card, ~43% peeking). Browse context is never destroyed.
- **Persistent Information Architecture:**
  - In Bounded Detail mode, the persistent header remains fixed and visible at the top across all menu switches:
    1. Event badge (`+ CHECK-IN`) and timestamp (`4:03 PM • Just now`).
    2. Member photo (rectangular `38.dp` x `44.dp`), verified checkmark, full name (`Yash Singh`), member code (`BG204`), and motto (`Good Fitness Brighter You`).
    3. Dual membership status bands (`Gold Plan • 12 Months` and `ACTIVE • 48 d left`).
    4. Back/collapse button (`Icons.AutoMirrored.Rounded.ArrowBack`).
  - Tapping rail items switches ONLY the content panel below the persistent header with smooth 180ms animations:
    - `HOME`: High-value decision metrics (Attendance, Payment, Workouts), urgent signal banner (`₹4,500 outstanding`), Coach/Routine quick row, and `Collect Payment →` CTA.
    - `ATTENDANCE`: Period visits/target (`16/26`), streak (`4 days`), lifetime visits (`214`), avg/week, preferred slot, last visit, and recent gate activity.
    - `PLAN`: Plan name (`Gold Plan`), type (`12 Months`), start, expiry, days left (`48 d`), freeze allowance/usage, and renewal count.
    - `PAYMENT`: Outstanding due (`₹4500` in high-contrast red), overdue days (`3 days`), due date, `Collect Payment →` CTA, last payment amount/method, and transaction log.
    - `TRAINER`, `WORKOUT`, `SUPPLEMENTS`, `NUTRITION`, `SERVICES`, `HISTORY`, `INSIGHT` all render within the same bounded shell.
- **Navigation & Back Handling:**
  - System Back (`BackHandler`) cleanly closes bounded detail and returns to compact browse mode.
  - Header back arrow and bottom banner "Collapse" also return cleanly to compact browse mode.
- **Committed Real Device Evidence:**
  - `docs/screenshots/stage2_correction_browse.png` (Browse mode with multi-card side peek)
  - `docs/screenshots/stage2_correction_detail_home.png` (Bounded detail mode on HOME menu with rail, persistent header, and side peek)
  - `docs/screenshots/stage2_correction_detail_menu.png` (Bounded detail mode on PAYMENT menu with rail active indicator, persistent header, and side peek)
- **Runtime Identity:**
  - Verified in screenshots: `MI-V5 • BUILD 7c8cb6f • DEBUG` (dynamically injected from build).
- **Next Step:**
  - Ready for ChatGPT inspection and issuance of `MI-V5-STAGE-03` according to the production roadmap.



## CHATGPT → ANTIGRAVITY / STAGE 3 HANDOFF — 2026-09-23
Stage 2 corrective pass has been verified on physical hardware and accepted as the bounded-card foundation. Stage 3 is now authorized.

Product decisions: keep compact browse + bounded detail; never full-screen; fill every menu panel; enlarge portrait; never recolor the entire dashboard on member/menu tap; use local tier/state semantics with urgent state overriding tier; hide rail in browse and show it in detail with collapse affordance; add truthful rail micro-data and highlight the menu needing attention; use varied restrained transitions; verify Normal/Active, Gold/Payment Due, Premium/Active, Expired, Trainer/PT Active; no backend, fake metrics, or heavy 3D.
Acceptance: real-device build/install/test + committed Stage 3 screenshots listed in CURRENT_TASK. Stage 4 blocked until ChatGPT review.
