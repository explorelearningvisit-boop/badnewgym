# BAD GYM — Design System

The dashboard uses **one** token set. Pack themes no longer recolour the dashboard; they only
skin the legacy member-card strips. Every number below is the value that is actually compiled.

---

## 1. Colour — `NeuThemes.Lavender` (default skin)

Source: the supplied reference screenshot + the palette in the brief, expressed through the
existing neumorphic engine so light/shadow stay physically consistent.

| Token | Value | Use |
|---|---|---|
| `canvas` | `#F5F3F8` | screen background |
| `canvasDeep` | `#EFEBF6` | gradient stop |
| `surface` | `#FFFFFF` | card fill |
| `surfaceSunken` | `#EDE8F6` | progress tracks |
| `shadow` | `#C9C0DC` | bottom-right shadow |
| `shadowSoft` | `#E4DEF0` | hairlines, grid lines |
| `ink` | `#2B2340` | primary text / KPI values |
| `inkSoft` | `#5F5674` | secondary text |
| `muted` | `#8B82A0` | labels |
| `violet` | `#8239E8` | primary accent, active tab |
| `violetDeep` | `#6424C8` | gradient end, emphasis |
| `violetWash` | `#F0E8FA` | accent tint backgrounds |
| `green` | `#079B70` | success, present, on-time |
| `greenBright` | `#2FC79A` | gauge sweep highlight |
| `greenWash` | `#DDF6EC` | success tint |
| `coral` | `#E34E58` | danger, absent, late payment |
| `amber` | `#E9AA35` | warning, late arrival, streak |
| `blue` / `sky` | `#5B8DEF` / `#489CC1` | informational series |
| `pink` | `#F88AB0` | tertiary series |

### Semantic rules (applied consistently)

| Meaning | Colour |
|---|---|
| Present / paid / on track / positive delta | green |
| Late / pending / near-limit / streak | amber |
| Absent / overdue / negative delta / cancellation | coral |
| Selected / primary action / attendance tab | violet |
| Neutral / disabled / holiday | muted on `surface` |

Six alternate skins reuse the same token contract: Snow, Mint, Sand, Steel, Rose and Midnight
(dark neumorphism with an inverted shadow pair). Switching a skin cannot leave stale colours
because the dashboard subtree is rebuilt under `key(NeuStyle.themeId)`.

## 2. Typography — `IntelligenceTypography`

Family: **Plus Jakarta Sans** (variable, SIL OFL), injected once via `LocalTextStyle` at the
dashboard root with `fontWeight = Bold`. Nothing renders below SemiBold.

| Token | Size | Weight | Use |
|---|---|---|---|
| `sizeMicro` | 9 sp | SemiBold | axis ticks, badge text, legal-ish meta |
| `sizeSmall` | 11 sp | Bold | chip labels, pods, secondary data |
| `sizeBody` | 14 sp | Bold | insight copy |
| `sizeCardTitle` | 16 sp | ExtraBold | card titles, member name |
| `sizeMetric` | 22 sp | Black | KPI values, gauge headlines |
| `sizeHero` | 26 sp | Black | reserved for hero numbers |

Weight tokens: `weightLabel` (SemiBold), `weightBody` (Bold), `weightTitle` (ExtraBold),
`weightValue` (Black). No `FontWeight.Normal` exists anywhere in the dashboard.

## 3. Density — `IntelligenceDensity`

| Token | Value |
|---|---|
| `cardPaddingH` / `cardPaddingV` | 10 dp / 8 dp |
| `widgetGap` / `widgetGapSm` / `widgetGapLg` | 4 / 3 / 6 dp |
| `sectionGap` | 5 dp |
| `iconMicro…iconLarge` | 11 / 13 / 15 / 17 dp |
| `kpiTileHeight` | 44 dp |
| `radiusCard` / `radiusPill` / `radiusChip` / `radiusSmall` | 18 / 50 / 10 / 6 dp |

Touch targets stay ≥ 44 dp even at the smallest component scale.

## 4. Elevation & shape

- Raised: dark shadow bottom-right + white highlight top-left, depth 2–6 dp, blur 4–16 dp.
- Sunken: inner-shadow pair, used for progress tracks.
- Accent: coloured glow, used for the selected KPI tab and the CHECK-IN pill only.
- Radius and depth are multiplied globally by `NeuStyle.radiusScale` / `depthScale`.

## 5. Layout rules

1. Everything is vertical: header → KPI tab row → module cards, 4–6 dp apart.
2. Inside a card, data is laid out **horizontally** (donut left / calendar right,
   value left / trend right) so vertical height stays low.
3. Cards never pad to fill space; height is content-driven.
4. Any horizontally scrollable nav row provides an edge fade and never hard-cuts a label;
   where a fixed set fits, it is laid out as an equal-width grid instead (payment 3+2).
5. Charts reserve a left gutter for Y labels and a right gutter for callout badges.

## 6. Appearance controls (`NeuStyle`)

| Control | Range | Effect |
|---|---|---|
| Text size | 0.85× – 1.35× | multiplies `fontScale` only |
| Icons & controls | 0.85× – 1.25× | multiplies `density` (icons, paddings, boxes) |
| Corner radius | 0.6× – 1.6× | multiplies every radius |
| Shadow depth | 0× – 1.6× | multiplies offset + blur; 0 = flat |

Persisted in `SharedPreferences("badgym.neu.style")`.
