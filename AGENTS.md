# BAD GYM Member Intelligence V3 — Agent Contract

This repository is the source of truth for the new Member Intelligence feature on branch member-intelligence-v3.

## Objective
Build a production-grade Jetpack Compose/Kotlin Member Intelligence experience matching the supplied eight-theme reference image as closely as practical while keeping business logic independent from visual themes.

## Non-negotiables
- Do not build a static mockup.
- Every member-menu item must change the content.
- Every primary CTA emits a typed intent/action.
- No fabricated business, health, payment, attendance, or engagement data.
- Every metric has an explicit period/source.
- Semantic states override decorative membership-tier colors.
- Theme is data-driven; do not create eight independent screens.
- No Supabase service-role key or secrets in source control.
- Preserve a clean domain/repository boundary.
- Use fake/demo data only behind a clearly named demo repository.
- Real repository adapters must be injectable.
- Offline/loading/error/syncing states are first-class UI states.
- Accessibility: minimum 48dp touch targets, content descriptions, non-color state communication.

## Visual direction
Reference: supplied 8-card BAD GYM Member Intelligence board:
Natural Fresh, Futuristic Neon, Minimal Dark, Glassmorphism, Premium 3D, Vibrant Gradient, Gym Beast Mode, Purple Royal.

Use one reusable shell:
Global Theme × Membership Tier × Current Event × Member State.

Visual ingredients:
- rounded 18–24dp surfaces
- layered surfaces
- subtle inner highlights
- selective glow for neon/premium modes
- theme-specific ornamental backgrounds
- tasteful depth/shadow
- compact information hierarchy
- strong event chip
- member identity/photo
- membership badge
- 3 decision metrics
- intelligence signal
- contextual CTA
- persistent adaptive member rail

Avoid decorative graphs without a decision question.

## Data contract
Domain models include:
MemberSnapshot, MembershipStatus, AttendanceSummary, PaymentSummary, TrainerSummary, WorkoutSummary, SupplementSummary, NutritionSummary, ServiceSummary, MemberEvent, IntelligenceSignal, MenuAvailability, SubscriptionEntitlement.

## Event contract
CHECK_IN, CHECK_OUT, PAYMENT, PAYMENT_FAILED, NEW_MEMBER, WALK_IN, RENEWAL, EXPIRED, FREEZE, REACTIVATION, TRAINER_SESSION, WORKOUT, SUPPLEMENT_PURCHASE, NUTRITION, SERVICE_PURCHASE, COMPLAINT, MAINTENANCE.

## Menu contract
HOME, ATTENDANCE, PLAN, PAYMENT, TRAINER, WORKOUT, SUPPLEMENTS, NUTRITION, SERVICES, HISTORY, INSIGHT.

Use MemberMenuRegistry for conditional availability and entitlement/permission gating.

## Backend direction
Supabase/Postgres is business source of truth; Room is local cache; Realtime drives live updates; Firebase is for FCM/Crashlytics/optional analytics. Follow current Supabase Kotlin guidance in docs/architecture.

## Build discipline
Before changing architecture, inspect existing project files and keep unrelated application screens intact. This branch is a replacement Member Intelligence implementation, not a destructive rewrite of the whole product.
