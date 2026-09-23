# BAD GYM — MEMBER INTELLIGENCE 8-THEME VISUAL RECONSTRUCTION SPEC

The supplied reference board is not one screen. It is eight visual states of one shared Member Intelligence system.

## Shared anatomy
- Premium outer shell
- Header: back, BAD GYM brand/logo/tagline, notifications, avatar
- Integrated left navigation rail: Home, Attend, Plan, Pay, Trainer, Workout, More/contextual
- Explicit event pill
- Event time + relative time
- Large portrait
- Member name + verification badge + member ID
- Decorative/motivational message
- Membership tier/plan band
- Membership status band
- Three decision metrics: Attendance, Payment, Workouts
- Contextual full-width CTA
- Decorative footer/brand message

## Eight visual identities

### 1 Natural Fresh
Pale mint/white, soft green, botanical leaves, wellness, glass depth, champagne/gold membership, green active status, attendance ring, six-bar workout progression, green CTA, botanical footer.

### 2 Futuristic Neon
Deep navy, cyan/electric green/violet, controlled glow, luminous borders, angular geometry, neon hero, futuristic metrics and luminous CTA.

### 3 Minimal Dark
Charcoal, gray surfaces, white type, restrained green status, minimal borders, no excessive glow, executive spacing.

### 4 Glassmorphism
Translucent/frosted layers, blue/white/lavender atmospheric background, blurred depth or programmatic fallback, luminous edges.

### 5 Premium 3D
Deep charcoal, champagne/gold metallic treatment, crown/luxury language, strong depth/reflection, premium CTA.

### 6 Vibrant Gradient
Pink/purple/cyan/blue/green gradients, colorful translucent surfaces, energetic but controlled visual hierarchy.

### 7 Gym Beast Mode
Dark performance aesthetic, red/electric green, sharp/angular accents, athletic typography, strong action CTA.

### 8 Purple Royal
Purple/violet/lavender/magenta/silver, royal premium surfaces, elegant typography and luminous edges.

## Shared data, different visual treatment
The data model is common. Theme, membership tier, event and member state determine the final appearance. Do not clone eight independent screens.

## Tier layer
Normal, Silver, Gold, Premium, VIP, Elite, Corporate, Trial, Walk-in.
Tier can affect badge, accent, border, surface, icon and subtle glow. Semantic status always wins.

## Event layer
Check-in, Check-out, Payment, Payment Failed, New Member, Walk-in, Renewal, Expired, Freeze, Reactivation, Trainer Session, Workout, Supplement Purchase, Nutrition, Service Purchase, Complaint, Maintenance.

## Home hierarchy
Event > time > member identity/portrait > membership > active/payment state > decision metrics > contextual CTA > decoration.

## Metric rules
Attendance uses a real progress ring when progress is meaningful.
Payment explicitly communicates due/clear/overdue and amount/date.
Workouts uses a six-bar progression visualization in the reference style, not an arbitrary generic chart.

## Visual rules
- No tiny circular portrait when reference calls for large portrait.
- No huge dead blank area.
- No generic Material card replacing custom surfaces.
- No duplicated back controls.
- No intelligence alerts allowed to visually overpower the reference Home composition.
- No semantic warning hidden by tier colors.
- No decorative asset should become a full-screen screenshot background.
- Use programmatic shapes for simple geometry and real assets for complex imagery.
