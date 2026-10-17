# BAD GYM — Product, Business and User Context

## Product

BAD GYM is a gym-management Android/SaaS product. The active Android repository is `explorelearningvisit-boop/badnewgym`, implemented in Kotlin and Jetpack Compose. The main product is used by gym owners/staff to manage member identity, check-in/check-out, memberships, payments, attendance, workouts, body-progress measurements, trainers, issues, services and configurable operational widgets.

This is not the user's mustard-oil business application. Do not mix BAD GYM data or documentation with Banaras Pure Gold / Shashi Arjun Gold Oil Company, OPPO/mobile-control, or Banaras Tour Gold projects.

## User's problem to solve

The user wants a premium Member Intelligence screen that lets a gym owner understand a member's current situation quickly, without opening many disconnected screens. The owner should be able to identify the member, membership status, payment action, attendance behavior, workout consistency and body-goal progress from one coherent mobile-first experience.

The supplied reference image shows these visual regions:
1. BAD GYM brand/header and utility actions.
2. Member portrait, name, member ID, age/gender where available, plan/status and membership progress.
3. Horizontal section navigation.
4. Payment & Plan with paid/outstanding state, due/last payment context and 12-month history.
5. Monthly attendance calendar and percentage.
6. Daily workout duration trend with average/peak/lowest/month-over-month summary.
7. Body Progress tiles for weight, muscle, body fat and BMI with trends when data exists.

The user is dissatisfied with approximate, generic, decorative UI that looks superficially similar but has wrong geometry, irrelevant metrics, duplicate content, fake values, clipped text, dead controls or no actual data flow. The goal is not just to generate a mockup: it is to implement, build, run, compare against the reference, correct discrepancies and make the screen usable.

## Design intent

- Premium, light, soft neumorphism; white/mint/pale-blue surfaces.
- Emerald/mint is the primary healthy/success language.
- Coral/red signals overdue/urgent/absent states.
- Blue identifies information/workout.
- Purple identifies body progress/insight.
- Gold identifies plan tier and upgrade markers.
- Navy is text color only, not a dark-heavy background.
- Clean rounded cards, restrained shadows, readable charts, consistent icons.
- No toy-like 3D ornament, fake AI decoration, random stock/gym photos, or visual filler.
- Mobile-first, not a tablet layout squeezed into a phone.

## Truth and data rules

- Member profile image/name/ID/plan must come from the selected member record.
- A paid member must not be shown a false “Due” alert or collection CTA.
- Payment amounts and monthly bars must be based on actual transactions.
- No payment transaction for a month is not automatically proof of a missed payment.
- Payment timing (on time/late/very late) is shown only when known.
- Attendance must distinguish present, absent, late, holiday/closed and unknown.
- Sunday is not automatically a holiday; use the actual gym calendar.
- Missing workout logs are not zero-minute workouts.
- Body metrics only appear when measurements exist and access is authorized.
- Weight gain/loss or muscle changes are not inherently positive/negative without a member goal.
- No fabricated production chart series or metrics.
- Stub/preview fixtures must be visibly and technically separated from production data adapters.
- Server subscription/entitlement remains authoritative; UI state cannot bypass it.
- All visible primary actions must navigate to a real workflow.

## Product architecture to preserve

The repository already has a native Android app, domain models, ViewModel/repository patterns and a V4 event-first/visit-card/widget architecture. The new full-screen reference must be integrated deliberately with this architecture. Do not delete or bypass event priority, canonical visit-card behavior, entitlement logic, privacy rules or existing working flows simply to reproduce the image.

The new expanded dashboard and the compact event-first visit card may be separate surfaces. Inspect existing navigation and determine the correct entry point. If the user reference requires an explicit architecture decision, record it in this file and in `CURRENT_TASK.md` before making a destructive change.

## User workflow preference

The user expects GitHub to be the shared source of truth. They want to pull the branch, open it in their coding environment, run the app, and have an AI agent read the repository instructions to continue work without requiring the user to relay prior chat context. Keep implementation intent and decisions in repository documentation rather than relying on conversation memory.

## Scope boundaries

Only work in BAD GYM app code, tests, assets and documentation. Do not change:
- `OPPO/` or `Launch_Oppo_Dev_Studio.bat`
- unrelated mobile-control tooling
- Banaras Tour Gold / Manchester-company work
- Banaras Pure Gold mustard-oil business assets or data

## Success means

The correct screen is wired into the real app, uses real repository data, builds and tests, matches the reference geometry and visual hierarchy on phone-sized viewports, handles real/empty/loading/error/permission states, has no dead controls, passes screenshot comparison and has an evidence-based handoff. Do not call it production-ready until those checks are complete.
