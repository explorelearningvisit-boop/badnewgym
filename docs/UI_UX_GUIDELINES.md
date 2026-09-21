# BAD GYM UI / UX Guidelines

## Member Intelligence — 2026 visual system

The Member Intelligence experience is a single data-driven operating surface. Themes change visual personality only; they never replace or reload member data.

### Visual hierarchy

1. App identity + live event
2. Member identity + verification
3. Membership state
4. Contextual navigation
5. Primary intelligence signal
6. Supporting metrics
7. Secondary signals
8. One clear next action

The UI must answer "what happened, what matters, and what should I do next?" without requiring the owner to open multiple screens.

### Layout

- Mobile-first target: 360–430dp.
- Outer gutter: 12dp.
- Primary surface: 26dp radius.
- Internal cards: 17–20dp radius.
- Avoid fixed-height dashboard shells that create large empty areas.
- Content panels may scroll vertically when evidence exceeds the viewport.
- Navigation uses compact horizontal controls rather than a tall desktop-style rail.
- Theme selector is horizontally scrollable and always remains discoverable.

### Intelligence

- P0 critical and P1 action-required signals must remain visually dominant.
- Payment overdue must not be visually buried by membership/promotional content.
- Use icon + label + color for status; color is never the only status cue.
- Hide empty data categories instead of showing fake placeholders.
- Home is the intelligence layer; Attendance, Plan, Payment, Trainer and Workout are evidence/detail layers.

### Motion

- Menu changes: directional slide + fade, 150–260ms.
- Theme changes: preserve the same member snapshot and animate the visual shell; never reload the repository just to change theme.
- Buttons and selected navigation use subtle scale/width feedback.
- Avoid continuous decorative animation that competes with operational information.
- Prefer Compose AnimatedContent for stateful content transitions.

### Themes

Eight skins share the same information architecture:

1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

Each theme owns background, surface, border, accent, CTA gradient, rail state, typography contrast and motivational tone.

### Accessibility / quality

- Maintain readable contrast for primary text and status labels.
- Keep touch targets comfortable for mobile use.
- Never make an icon the sole affordance for a critical action.
- Preserve domain/business truth from MemberSnapshot and MemberIntelligenceEngine.
- Presentation code must not infer membership state from payment state.

### Architecture

MemberIntelligenceViewModel owns data/intelligence state.

MemberDashboard owns the visual composition.

MemberIntelligenceEngine owns signal prioritization.

MenuAvailabilityResolver owns contextual navigation availability.

Changing a theme must not trigger a member repository reload.
