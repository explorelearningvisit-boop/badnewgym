# BAD GYM Reference Deconstruction V3

The supplied 1536x1024 board is treated as the visual source of truth.

## Measured card geometry
Approximate crops from the supplied board:
- Top row cards are approximately 339–352px wide x 484–486px high.
- The dominant portrait-card ratio is therefore approximately 0.70 width/height.
- Bottom row cards are approximately 339–353px wide x 419–421px high in the supplied composition because the board crop includes less vertical content; the common shell still uses the same portrait-card language.

For Android, the production target is:
- card width = available width minus 16–24dp outer gutter
- target aspect ratio ≈ 0.70
- shell corner radius ≈ 20–24dp
- rail width ≈ 52–58dp
- internal horizontal padding ≈ 10–14dp
- event chip height ≈ 24–28dp
- portrait ≈ 72–82dp
- metric row = 3 equal columns
- CTA height ≈ 40–46dp
- border = 1dp base, theme-specific glow layered underneath

## Reconstruction order

### Phase 1 — background
1. Base gradient.
2. Theme-specific radial lighting.
3. Decorative ornaments.
4. Edge glow.
5. Card shell.
6. Rail surface.

### Phase 2 — structure
7. Header/logo.
8. Notification/admin avatar.
9. Event chip and timestamp.
10. Member portrait and identity.
11. Membership tier/status.
12. Three metric tiles.
13. Intelligence signal.
14. CTA.
15. Footer/microcopy.

### Phase 3 — visual material
- Natural: translucent botanical leaves + soft mint bloom.
- Neon: deep navy + cyan/electric-green/violet glow lines.
- Minimal: charcoal + restrained silver/emerald edge.
- Glass: translucent white/cool blue/lilac surfaces + soft blobs.
- Premium: charcoal + champagne/gold metallic highlights.
- Vibrant: white/lilac base + purple/blue/pink/orange gradient light.
- Beast: charcoal/red + cyan contrast + claw/angular accents.
- Royal: deep violet + magenta/lavender + jewel highlights.

### Phase 4 — typography
Typography is functional and must not be baked into images:
- BAD GYM: 14–16sp heavy.
- Event: 9–10sp extra-bold.
- Member: 16–18sp heavy.
- ID: 10–12sp.
- Tier: 10–12sp.
- Metric value: 11–14sp bold.
- Metric label: 8–9sp.
- CTA: 12–13sp bold.
- Footer: 8–10sp.

### Phase 5 — behavior
- rail click changes content.
- theme click changes visual tokens only.
- CTA emits typed action.
- no screenshot is used as a background.
- preserve member/business state across theme changes.

### Phase 6 — fidelity gate
Do not call the card pixel-accurate until:
- shell ratio matches target.
- rail width and content proportions match.
- event row matches.
- portrait size matches.
- tier/status/metric/CTA spacing matches.
- each theme has its own background material recipe.
- semantic states remain visible across all themes.
