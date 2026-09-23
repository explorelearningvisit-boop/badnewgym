BAD GYM MEMBER INTELLIGENCE V3 REQUIREMENTS

Build the supplied eight-card visual reference as a real Jetpack Compose product surface. One reusable shell, eight data-driven themes: Natural Fresh, Futuristic Neon, Minimal Dark, Glassmorphism, Premium 3D, Vibrant Gradient, Gym Beast Mode, Purple Royal.

Visual ingredients: theme-specific background gradients/ornaments, layered rounded surfaces, inner highlight strokes, controlled glow, membership-tier accents, current-event pill, member portrait, membership badge, three decision metrics, intelligence signal, contextual CTA, persistent adaptive member rail.

The same data and menus power all themes. Theme changes must not reload business data.

Member menus: Home, Attendance, Plan, Payment, Trainer, Workout, Supplements, Nutrition, Services, History, Insight. Conditional menus are resolved from actual data, entitlements and permissions.

Menu clicks must change the main content. Use ViewModel state + intent/event flow. Use AnimatedContent with horizontal slide. Preserve per-menu scroll state.

Data: MemberSnapshot aggregates identity, membership, attendance, payments, trainer, workouts, supplements, nutrition, services, events, issues and signals. Metrics must include explicit period labels. Attendance is anchored to active membership period or explicitly labelled historical period. Payment exposes invoice period, due date, lifecycle and transaction history.

Primary actions: Collect Payment, Retry Payment, Renew Membership, Recovery Follow-up, Open Trainer Session, Reorder, Renew Nutrition, Resolve Issue, Complete Onboarding. Actions must emit typed intents and have real repository/navigation adapters.

Priority: P0 critical; P1 action-required; P2 important; P3 background. Semantic status colors override tier/decorative colors.

Data source target: Supabase/Postgres business source of truth, Room local cache, Supabase Realtime for live events. Firebase may provide FCM/Crashlytics/analytics only. Never commit secrets or service-role keys. Never use cleartext networking in production.

Graphs: use a native Compose chart library such as Vico when compatible with the project. Add only decision-useful charts: attendance trends, payment aging/payment trend, workout consistency, trainer sessions, evidence-backed insight trend. No decorative/fake graphs.

Assets: use layered Compose/vector assets. Keep original member portraits, card crop and thumbnail; optional transparent cutouts only when rights/consent permit. Product/trainer/equipment cutouts may use transparent PNG. Do not bake text into images.

Responsive target: 360, 375, 390, 412dp. Minimum 48dp touch targets. Support font scaling and non-color status communication.


## PLATFORM LOCK
This feature is Android-only. Do NOT create HTML, CSS, JavaScript, web UI, or a separate web implementation. The visual reference must be reconstructed as native Jetpack Compose/Kotlin UI. Use Android resources, Compose Canvas/Brush/graphicsLayer/drawWithCache, Material 3 where appropriate, Coil for images, and native Compose-compatible charting. Do not flatten the reference screenshot into a background image. Match the supplied eight-card reference through real components and theme tokens.
