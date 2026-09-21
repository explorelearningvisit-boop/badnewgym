# BAD GYM — Pixel Perfect Member Card

The supplied eight-theme reference board is the visual source of truth for the Member Intelligence card.

## Composition
- 3:4 card language.
- 8dp outer shell gutter.
- 53dp vertical navigation rail.
- Compact event/time row.
- 82dp member portrait.
- Verified member identity and membership state.
- Three metric cards: attendance, payment due, workouts.
- Three compact support cards: trainer, workout, services.
- One dominant CTA.
- Motivational footer.
- Horizontal theme selector with all eight themes.

## Themes
1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

## Motion
Menu and theme transitions use Compose fade + horizontal slide transitions. Theme changes do not reload the member snapshot.

## Assets
app/src/main/assets/materials/ is the source material pack for decorative backgrounds and token documentation. The member portrait is a deterministic Android vector fallback at res/drawable/member_yash.xml; production member photos can replace it without changing the card geometry.

## Product rule
Do not add large empty dashboard shells, oversized desktop rails, or disconnected theme controls. The reference is a compact operational card, not a generic dashboard.
