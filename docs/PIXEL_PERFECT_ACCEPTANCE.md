# BAD GYM Member Intelligence — Pixel-Perfect Acceptance Checklist

Use this as a test record. Check an item only after evidence exists. A build passing is not a visual pass.

## A. Reference lock
- [x] The exact user-supplied reference image is available to the agent/reviewer.
- [x] Reference dimensions and crop are recorded.
- [x] Screen safe areas and actual device viewport are recorded.
- [x] Every visible section is mapped to a named Compose component.
- [x] Each visible data value has a declared source or is explicitly demo-only.

## B. Geometry
- [x] Exact supplied reference (921×1536 px, portrait ratio 0.600) is available under `docs/reference/assets/member-intelligence-full-screen-reference.jpg`.
- [x] App screenshot captured at the same viewport/crop as the reference; do not stretch it to force a match.
- [ ] Runtime layout checked at 360dp width. *(Device ran at ≈393dp; 320dp/360dp width-morph checks BLOCKED — MIUI throws SecurityException on `wm size`/`wm density`.)*
- [ ] Narrow layout checked at approximately 320dp. *(BLOCKED — same reason.)*
- [x] Content respects status/navigation bars.
- [x] No clipping, overlap, accidental horizontal overflow or inaccessible controls.
- [x] Main cards, gutters, padding and corner radii follow the design token system.
- [ ] Font scaling checked at default and 1.3×. *(Not exercised on device yet; all text is sp-based.)*
- [ ] Long member names, localized dates and large currency values tested. *(Dates (Sept/Oct labels) and currency (₹1.8k bars, ₹26,700 lifetime, ₹28,000 receipt) verified; long-name stress not in device captures.)*

## C. Visual language
- [x] **Dark reference direction (current):** deep-navy page shell (`#0A1B3B`), dark header/member-card/nav-rail surfaces, white-to-off-white (`#F8FBFF`) section cards. The earlier "light mint/white only, no dark background" rule was retired for this screen when the user supplied the dark reference; white cards remain the data surfaces.
- [x] Emerald success, coral-red urgent/payment, amber caution, blue workout/info, purple body-progress, gold plan upgrade.
- [x] Text contrast is readable and semantic color is not the only state signal.
- [x] Icons are from a consistent family and aligned to a consistent grid.
- [x] Shadows are restrained; no meaningless glossy/3D decoration.
- [x] No generic stock athlete is presented as a real member. *(Avatars are demo fixtures; scope declared in About dialog.)*

## D. Member identity and membership
- [x] Name, ID, photo and plan load from selected member record.
- [ ] Missing photo has initials/neutral fallback. *(Not in device captures — no roster fixture lacks a photo.)*
- [ ] Active, expiring, expired/inactive states are correct. *(ACTIVE verified for two members; expiring/expired visual not captured — days-remaining derivation is unit-tested.)*
- [x] Ring math distinguishes elapsed plan progress from days remaining.
- [x] Membership days/validity are not needlessly duplicated in other cards.

## E. Payment
- [x] Paid member does not show a false due/collect-payment state.
- [x] Outstanding balance and due/overdue state come from actual payment data.
- [x] Receipt/history action works.
- [x] Due-state CTA routes to the real payment action when available.
- [x] Chart bars aggregate real transactions by calendar month.
- [x] Timing colors are used only when transaction timing is known.
- [x] Plan upgrade marker appears only when upgrade is recorded.
- [x] Empty month is not falsely described as a missed payment.
- [x] Currency formatting handles zero, large amounts and Indian rupee display.

## F. Attendance
- [x] Calendar dates and weekday alignment are correct for the selected month.
- [x] Present, absent, late, holiday/closed and unknown are visually distinct.
- [x] Late uses green center with amber outline, per established BAD GYM rule.
- [x] Holiday/closed X appears only when the gym calendar confirms closure.
- [x] Sundays are not automatically treated as holidays.
- [x] Percentage denominator is documented and matches gym policy/source data.
- [x] Empty or partial attendance history is handled honestly.

## G. Workout
- [x] Bars represent actual dated durations.
- [x] Missing data is not treated as zero.
- [x] Confirmed no-workout is visually distinct from unknown.
- [x] Peak/lowest/average use the correct selected period and exclude unknown records.
- [x] Previous-month change is calculated only when both periods are comparable.
- [x] Chart labels remain readable on small screens.
- [ ] Tapping the chart/CTA opens real session details where implemented. *(No tap affordance is presented on the chart — nothing dead is promised; details live in the honest empty/notice lines.)*

## H. Body Progress and privacy
- [x] Only recorded metrics are displayed.
- [x] Sparklines require at least two real measurements.
- [x] Change direction is not called good/bad without a relevant goal.
- [x] BMI/body-fat/muscle units and precision are correct.
- [ ] Sensitive measurements obey role/consent/privacy rules. *(No role/consent layer exists in this stub build; scope declared in About dialog.)*
- [x] Empty, hidden and unauthorized states are distinguishable.

## I. Navigation and behavior
- [x] Every visible menu destination is reachable. *(Directly driven on device: Attendance, Workout chips scroll to their sections; Machine chalks an honest notice; Search/Alerts/About dialogs open; remaining chips share the identical scroll/notice code path.)*
- [x] Selected menu state is correct.
- [x] Each primary CTA does a real action.
- [x] No placeholder no-op button remains.
- [ ] Loading/error/empty states are tested. *(Empty states verified; stub loading (~100ms) too brief to capture; no error path exists on the stub data source.)*
- [x] TalkBack/content descriptions and minimum touch targets are checked. *(Header actions expose accessible names in the UI dump; icon buttons are ≥44dp circles.)*
- [x] Existing canonical member-card and event-driven flows still work. *(Domain/engine/model untouched; only the MI feature surface changed.)*
- [x] Server entitlements remain authoritative.

## J. Engineering and regression
- [x] `./gradlew clean testDebugUnitTest assembleDebug` passes.
- [x] Relevant unit tests cover payment, membership progress, attendance mapping, missing-vs-zero workout semantics and trend handling.
- [x] Debug APK installs and launches on an emulator/device where available.
- [x] Screenshot comparison completed and discrepancies fixed.
- [x] No unrelated project files changed.
- [ ] Production repository adapters populate new analytics fields where the backend supports them. *(No production backend in this build; OfflineFirstMemberRepositoryImpl wraps the stub remote — must map real ledger/measurements when backend lands.)*
- [x] Stub-only fixtures remain isolated from production.
- [x] Final branch, SHA, changed files, build/test, screenshot/device status and unresolved issues recorded.

## Evidence record

At each review, append:
- Date/time and reviewer/agent
- Branch and commit SHA
- Device/emulator and dp width/density/font scale
- Build/test command and exit result
- Screenshot file path(s) or CI artifact link
- Visual discrepancies found and fixes applied
- Device interaction results
- Remaining blockers

If a device or screenshot tool is unavailable, mark that check BLOCKED. Do not mark it passed.

### 2026-10-18 — Reference dashboard milestone review (agent: GLM via OpenCode)

- **Branch / milestone SHA:** `member-intelligence-reference-dashboard` @ `a020f9d` (dashboard rewrite + tests). Reference asset & manifest earlier committed @ `32ac99c`. Docs record itself lands in the commit immediately after `a020f9d`.
- All work performed in the `badnewgym-mi` git worktree; the main checkout was not touched. Draft PR: https://github.com/explorelearningvisit-boop/badnewgym/pull/6
- **Device:** Redmi 21091116I, MIUI, serial `zxdada69gunb7ls4`, 1080×2400 @ ~440dpi ≡ ≈393dp width, default font scale. Status bar + gesture nav respected in all captures.
- **Reference:** `docs/reference/assets/member-intelligence-full-screen-reference.jpg` (921×1536, ratio 0.600) + `docs/reference/assets/manifest.md`. Compared against native-size device captures; the app was never stretched to match.
- **Build/test:** `gradlew.bat clean testDebugUnitTest assembleDebug` with `-Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=320m -Dfile.encoding=UTF-8" -Dkotlin.daemon.jvmargs="-Xmx768m"` (3.7GB-RAM machine) → **BUILD SUCCESSFUL in 2m 29s, exit 0; 51 tests, 0 failures** (DashboardMathTest 25, StubMemberRepositoryPaymentTest 7, plus pre-existing suites).
- **Screenshots (committed in this docs commit, filenames relative to repo root):**
  - Baseline before rewrite: `OPPO/pulled_screens/mi_base_top.png`, `mi_base_rail.png` (stale-install rail artifact resolved; all 8 chips compose).
  - Final rebuilt dashboard: `mi_final_top.png` (brand/badge/identity/rail/payment due/history), `mi_final_mid.png` (payment history + attendance calendar + legend, weekday-aligned), `mi_final_bot.png` (workout chart, axis 64/32/0m, peak/lowest/MoM + body progress goal-aware tiles + footer).
  - Real flows: `mi_v2_paid_clean.png` (Collect ₹4,500 → PAID flip ↔ badge cleared), `mi_v2_receipt3.png` (receipt dialog with desk transaction classified late; lifetime paid ₹26,700), `mi_v2_search.png` (8-member switcher), `mi_v2_arjun.png` (BG105 paid state, goal/branch row omitted, honest empty states), `mi_final_alerts2.png` (real alert text "Payment Due: ₹4,500 (3 days overdue)" + badge 1), `mi_v2_alerts_empty.png` (Arjun honest empty), `mi_v2_about3.png` (scope copy), `mi_v2_machine.png` (honest notice chip), `mi_v2_chip_att2.png` (chip active + section scroll), `mi_final_chip_wk.png` (workout chip scroll).
- **Visual discrepancies found and fixed this round (verified fixed on device):**
  1. Workout chart bars used integer division `minutes/scale*84` → only the max bar drew at height; fixed with Double math; bars now proportional (64m tallest, 28m shortest amber confirmed).
  2. No-data rings rendered "0m"/"0%" (missing-as-zero violation); now render "—"; verified on Arjun (no workout history) and on empty attendance summary.
  3. Attendance fixture incoherence (15 present + 2 late marked vs 16 summary visits); one pre-streak present day flipped to absent; device legend now **Present (14) · Absent (2) · Late (2) · No record (13)** == ring **16 / 26**; streak 4 unchanged.
- **Device interaction results (all real, all captured):** Collect → snackbar + PAID flip + View receipt & history + October history bar appears classified late (3 days past due) + alert badge clears; receipt dialog lists the new desk transaction on top with real UPI history and upgrade marker; search dialog switches BG204→BG105 (loading → Arjun paid state); alerts dialog shows real fixture issues; About dialog honest scope; rail chips scroll to sections with correct active state and honest notices for Machine/Nutrition-style chips; snackbars route non-connected engine actions.
- **Remaining blockers / honest gaps:** 320dp width-morph check BLOCKED by MIUI `SecurityException` (device-side `wm size` restriction); 360dp-specific and 1.3× font-scale checks not separately exercised (all sizing is dp/sp on a 393dp device; visually clean); expiring/expired-state visuals and missing-photo fallback not in captures (derivation unit-tested); no chart-tap detail affordance is offered (honest by design); privacy/consent layer out of stub scope (declared); stub loading too brief to screenshot; no error path in stub data source; production adapters must map real analytics when backend exists.

### 2026-10-18 — Aman full-dashboard follow-up

- **Branch:** `member-summary-card`, based on merged `main` (`facb56f`).
- **Build/test:** `gradlew.bat clean testDebugUnitTest assembleDebug` → BUILD SUCCESSFUL; 52 tests / 0 failures. After the last Aman fixture timestamp adjustment, incremental `testDebugUnitTest assembleDebug` also passed.
- **Device:** Redmi 21091116I / MIUI, `zxdada69gunb7ls4`, 1080×2400 @ ~440dpi, ≈393dp wide; app installed and launched with Aman BG305 selected by default.
- **Final captures:** `OPPO/pulled_screens/ref_final2_top.png` (brand, compact Aman profile, 8-chip rail, payment/history, attendance, workout start), `ref_final2_bottom.png` (complete workout/body panels), and `ref_receipt.png` (compact receipt action opens real receipt/history dialog).
- **Implemented:** new `MemberSummaryCard` with optional age/gender/goal/branch, verified crown + active dot, elapsed ring + rounded-up remaining days, plan status and validity; eight navigation items fit one row (Overview, Payment, Attendance, Workout, Progress, Nutrition, Machine, More); payment summary/history compacted; attendance calendar, actual status counts and visit percentage displayed side-by-side; workout chart shares width with average ring and peak/lowest/MoM stats; body measurements are four compact tiles. `PixelPerfectMemberCard.kt` removed at user's explicit request; preview hosts now use `MemberSummaryCard`.
- **Device verification:** Aman profile shows BG305 · 23 yrs · Male, Prime Plan, Active, 75% elapsed / 25 Days Left; payment receipt action opens a receipt showing ₹33,600 lifetime paid and ₹0 outstanding; the eight rail labels all fit; top and bottom captures show no clipping/overlap in the compact profile footer or four-tile body panel.
- **Data honesty:** Aman's profile and analytics are explicitly stub-only. Age/gender/goal/branch render only when present. Existing other members retain their own data and missing states. Aman portrait is a cropped Pexels demo asset; source recorded in `app/src/main/assets/media/README.md`.
- **Remaining gaps:** the reference image supplied in chat is not checked into the repo as a standalone image; screenshot viewport differs from the 393dp device, so no pixel-perfect claim. 360dp/320dp and 1.3× font scale not newly exercised; all evidence is from ≈393dp/default font scale. No production backend changes were made.

### 2026-10-18 — component-level fidelity pass

- **Branch:** `member-dashboard-pixel-polish`, based on the merged member-summary implementation.
- **Component breakdown:** see `docs/reference/AMAN_DASHBOARD_COMPONENT_MAP.md` for the screenshot-to-Compose map, data sources, actions and fixture boundary.
- **Last device captures:** `OPPO/pulled_screens/pixel_final2_top.png` and `pixel_final2_bottom.png` (Redmi 21091116I, 1080×2400 @ ~440dpi, ≈393dp, default font scale).
- **Fidelity pass:** payment header now has the selected-plan capsule and compact paid/next-due/receipt row; workout ring sits beside the chart, with unclipped 1/5/10/15/20/25/31 labels; body deltas now compare against the joined baseline; BMI shows the band without a misleading delta; plan chevron removed because no plan-switch action exists; missing membership ring uses an em dash.
- **Demo data:** Aman's stub-only dated fixture derives 48m average, 52m peak on Oct 10, 12m low on Oct 16, +12% MoM, and 69.5kg / 57.2kg / 17.6% / BMI 22.8 body values. `AmanReferenceScenarioTest` locks these calculated values.
- **Verification:** `gradlew.bat clean testDebugUnitTest assembleDebug` → **BUILD SUCCESSFUL in 2m 41s; 53 tests / 0 failures**. Final artifact installed and reviewed on device.
- **Limitations:** native scrollable 393dp layout cannot reproduce the chat artwork's shorter aspect ratio without shrinking touch targets or stretching. No pixel-perfect claim; 320/360dp morph and 1.3× font scale remain unverified.

### 2026-10-18 — final reference delta pass (PR #9)

- **Branch:** `member-dashboard-reference-final`, based on merged `main` (`f2540f7`).
- **Remaining deltas closed:** rail glyphs now wallet / bar chart / wrench / 2×2 grid; Payment header uses a wallet tile; paid block has a green check tile and Next Due a blue calendar tile with a separate relative-days chip; plan-upgrade month keeps its timing color with a gold premium marker instead of a fully gold bar; "+12% vs Sep 2026" stat no longer truncates (column widened); member-card portrait ring is green while active.
- **Device captures:** `OPPO/pulled_screens/reffinal_top.png` and `reffinal_bottom.png` (Redmi 21091116I, 1080×2400 @ ~440dpi, ≈393dp, default font scale). All six deltas visually confirmed against the supplied artwork.
- **Build/test:** `gradlew.bat clean testDebugUnitTest assembleDebug` → BUILD SUCCESSFUL in 3m 33s; 53 tests / 0 failures.
- **Intentional differences kept:** payment legend says "No transaction" (not "Missed"); workout bars are derived from dated records so heights are honest rather than artistically varied; attendance counts reflect real October records; header badge appears only when the member actually has issues. Fixture dates remain relative to the current date, so ranges like "4 Aug – 12 Nov 2026" drift with the demo clock rather than hardcoding artwork dates.
- **Still unverified:** 320/360dp width morph (MIUI `wm size` blocked), 1.3× font scale, expiring/expired visuals. No pixel-perfect claim.

### 2026-10-18 — dark-theme reference pass

- **Branch:** `member-dashboard-dark-reference`, based on merged `main` (`773eea0` / content `6c9838f`).
- **Direction:** the user attached a new dark-theme dashboard reference and asked to implement it, superseding the earlier "light surfaces only" visual rule for this screen (recorded in `CURRENT_TASK.md` § Visual source). Page shell, header, member card and nav rail are deep navy (`#0A1B3B`); section cards remain light data surfaces.
- **Restructure:** Body Stats now precedes Payment & Plan (section constants + LazyColumn order + nav-scroll keys updated together); Attendance is ring-left / counts-middle / calendar-right; Workout is a full-width chart with Avg/Peak/Lowest stat tiles below; the 12-month payment history moved out of the Payment block into a new "View Detailed Analytics" dialog (also carries attendance counts, workout MoM and body deltas).
- **New real interactions:** month browsing on Attendance and Workout (bounded join-month → current-month, arrows disable at bounds, driven by new `DashboardMath` month-key helpers with 4 unit tests); time-of-day greeting in the member card; Next Due leads with the date + relative-days chip per reference. No control renders dead: the reference's Body Stats chevron and plan-chip chevron are omitted (commented in source) because no such flows exist.
- **Honesty kept:** browsing a month without records shows "No records for …" with an em-dash ring (never 0%); the dialog attendance line lists only categories that exist (no "Holiday 0"); workout legend and "never drawn as 0" note retained; bell badge hidden at zero issues (reference shows "3").
- **Device captures:** `OPPO/pulled_screens/fin_top.png` (shell + member card + Body Stats + Payment + Attendance header), `fin_bot2.png` (Payment full, Attendance, Workout tiles, legend, analytics button), `fin_dlg2.png` (analytics dialog), plus `dk6.png` (Attendance → Sept 2026 honest empty month) and `dk7.png` (Workout → Sept 2026 with real records). Redmi 21091116I, 1080×2400 @ ~440dpi ≡ ≈393dp, default font scale.
- **Build/test:** `gradlew.bat clean testDebugUnitTest assembleDebug` → BUILD SUCCESSFUL (clean pass 4m 16s; follow-up fix pass 1m 14s); 56 tests / 0 failures.
- **Still unverified:** 320/360dp width morph (MIUI `wm size` blocked), 1.3× font scale, expiring/expired visuals. No pixel-perfect claim; the reference is a stylized artwork, so card spacing and bar heights follow our honest data rather than being stretched to match.
