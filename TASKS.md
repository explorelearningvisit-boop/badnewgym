# BAD GYM — Task Board

Legend: `[x]` done · `[~]` in progress · `[ ]` pending · `[!]` blocked

## Phase 7 — Appearance (done)

- [x] `NeuStyle` persisted in `SharedPreferences("badgym.neu.style")`
- [x] 7 skins: Lavender (default), Snow, Mint, Sand, Steel, Rose, Midnight
- [x] Text size, icons & controls, corner radius, shadow depth sliders
- [x] Reset to guide defaults (now returns to Lavender)
- [x] Skin grid 4 + 3 so no label clips
- [x] Dashboard re-skinned live on theme change (`key(NeuStyle.themeId)`)

## Phase 8 — Overview Intelligence (next)

- [ ] Fitness score card (attendance × consistency × payment × progress composite)
- [ ] Cross-module snapshot: attendance %, workout hours, payment status, weight delta
- [ ] "Next 3 actions" list derived from the insight engines
- [ ] Add as a fifth tab or as the landing strip above the tabs (decide with the user)
- [ ] Unit tests for the composite score

## Phase 9 — Real data

- [!] Data source decision required (local Room vs remote API)
- [ ] Replace `MemberCardData.sampleAshwiniSingh()` at the call site
- [ ] Map payment ledger → reliability score instead of the literal `86`
- [ ] Map measurement history → Body Composition / Measurements cards
- [ ] Replace hardcoded `₹14,400`, `₹1,200`, `12` cycles with domain values
- [ ] Database tests for the new queries

## Phase 10 — Chart polish

- [ ] Tap a data point → value tooltip
- [ ] Empty state and loading skeleton per chart
- [ ] Unit tests for chart data ranges (no fabricated points)

## Phase 11 — Accessibility & responsive sweep

- [ ] TalkBack labels for every icon-only control
- [ ] Contrast audit at the active skin (AA for body text)
- [ ] 360 / 375 / 393 / 412 dp screenshot sweep
- [ ] Font-scale 0.85–1.35 sanity check on all four tabs

## Phase 12 — Release

- [ ] `assembleRelease`
- [ ] Final screenshot set committed under `docs/reference/images/`
- [ ] `CURRENT_TASK.md` ledger closed with the SHA
