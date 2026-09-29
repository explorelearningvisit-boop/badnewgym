# BAD GYM — Mobile Home Intelligence Redesign / Antigravity Execution Prompt

## Objective

Rebuild the BAD GYM Member Intelligence Home experience inside the existing Android Jetpack Compose project.

Reference A — practical existing phone UI: 691x1536 px. This is the geometry/source of truth for the actual phone canvas, card shell, rail proportions, spacing and mobile constraints.

Reference B — desired Home information design: 1024x1536 px. This supplies the Home data hierarchy, information types, charts, cards, statuses, member intelligence and visual language. Do NOT copy its tablet/desktop proportions.

Combine:
1. Reference A practical mobile geometry and existing BAD GYM card/rail language.
2. Reference B Home data and information architecture.
3. Existing MemberSnapshot -> ViewModel -> PixelPerfectMemberCard data flow.
4. Existing 8-theme BAD GYM design system.

## Repository

Repository: explorelearningvisit-boop/badnewgym
Default branch: main

Important architecture:
- feature/memberintelligence/domain/model/MemberSnapshot.kt
- feature/memberintelligence/presentation/MemberIntelligenceScreen.kt
- feature/memberintelligence/presentation/MemberIntelligenceViewModel.kt
- feature/memberintelligence/presentation/components/PixelPerfectMemberCard.kt
- feature/memberintelligence/presentation/components/IntelligenceRail.kt
- feature/memberintelligence/presentation/components/CardHeader.kt
- feature/memberintelligence/presentation/components/HeroMemberSection.kt
- feature/memberintelligence/presentation/components/CardMetricsGrid.kt
- feature/memberintelligence/presentation/components/home/HomeMenu.kt
- feature/memberintelligence/presentation/components/home/HomeShared.kt
- feature/memberintelligence/design/ThemeId.kt
- feature/memberintelligence/design/colors/ColorTokens.kt

Do not replace the architecture with a new navigation/data stack. Reuse the current state flow and domain model.

## Core mobile requirement

The design must work from approximately 320dp minimum width, with primary design target 360–430dp.

Rules:
- No tablet-width assumptions.
- No desktop dashboard shell.
- No fixed giant card dimensions.
- No mandatory horizontal scrolling for operational information.
- Text must ellipsize gracefully.
- Critical actions remain tappable.
- Use responsive Compose measurements and BoxWithConstraints/adaptive sizing where useful.
- Preserve safe-area handling already provided by MemberIntelligenceScreen.
- Initial Home state must fit useful intelligence into one practical mobile viewport.
- Additional related data must be exposed through a clear More interaction instead of squeezing tiny unreadable text into the card.

## Home interaction model

Home is the default menu.

When the owner taps Home in the vertical rail:
- Home becomes the active intelligence workspace.
- The Home card expands to the full available mobile width and height inside the safe app area.
- Hide the navigation rail while expanded so content gets maximum practical width.
- Provide a clear collapse/back affordance.
- Preserve the loaded MemberSnapshot; do not reload data.
- Animate the transition using existing Compose motion.
- Other rail menus continue using the existing menu architecture.

## Information architecture from Reference B

The Home workspace must contain these concepts, adapted to the small phone:

1. BAD GYM / Member Intelligence header
- BAD GYM brand
- Member Intelligence context
- Live state/count
- Compact operational status

2. Member identity hero
- member photo
- name
- verification
- member code
- membership tier
- plan
- days remaining
- active/inactive state

3. Today Status
- checked-in/current status
- time
- floor/location when available
- current workout/activity context
- duration where available

4. Five compact KPIs
- Attendance
- Payment
- Plan
- Workout
- PT
Each KPI needs value, status/subtitle and small progress/trend treatment.

5. Recent Activity
Use recentEvents and currentEvent for check-in, workout, PT session, payment, supplement, renewal and supported event types.

6. This Week Overview
Show a 7-day bar visualization plus attendance percentage, workout metric and streak. Use real attendance data where available and demo fallback only when needed.

7. Body Progress
Show the visual language from Reference B: previous/current member visual, weight change, body-fat change and muscle change. Until real body-composition fields exist in MemberSnapshot, isolate the presentation values so real fields can replace them later.

8. Focus Area
Compact donut/ring for Chest, Back, Legs, Shoulders and Arms. Use the supplied reference percentages as initial design data.

9. Upcoming
Show PT session, payment due and plan renewal. Use snapshot dates wherever available.

10. New at Gym
Use existing supplement/product asset when available: image, product name, price and New Stock state.

11. AI Insights
Use existing IntelligenceSignal engine output. Primary signal first, then secondary signals, with icon + label + severity/status. Never fabricate business-critical state when no signal exists.

12. More
A visible More control must reveal related secondary information on the same screen: nutrition, active services, signal count, member code/basic profile data and other supporting intelligence. More must not be a dead button.

13. Primary CTA
Use existing SignalAction where possible. If payment is outstanding, keep the CTA payment-oriented: Collect Payment ->. Otherwise use the intelligence engine action label.

## Visual system

Preserve all 8 existing themes:
1. Natural Fresh
2. Futuristic Neon
3. Minimal Dark
4. Glassmorphism
5. Premium 3D
6. Vibrant Gradient
7. Gym Beast Mode
8. Purple Royal

Same information architecture across themes. Theme changes only visual tokens. Preserve loaded snapshot. Reuse BADGymColors, theme brushes, radii, CTA gradients and rail tokens. Light themes must remain premium/light; do not force black backgrounds into them.

## Geometry

Reference A is the geometry source of truth.

Target:
- practical phone canvas
- approximately 320dp minimum supported width
- 360–430dp primary target
- rounded shell
- compact vertical rail when not expanded
- content gets remaining width
- no tablet-like two-column dashboard as default phone composition

Expanded:
- Home uses complete available width
- no left navigation rail
- one practical vertical Home scroll container only when necessary
- initial intelligence remains dense but readable
- More reveals secondary information rather than shrinking everything

## Accessibility and implementation quality

- Primary interactive controls should have approximately 44dp touch targets.
- Never use color as the only status indicator.
- Use ellipsis/maxLines for long names and labels.
- Keep typography legible on narrow screens.
- Avoid continuous decorative animation.
- Avoid nested competing scroll containers.
- Reuse Coil/AvatarImage/MemberPhoto assets already present.
- Do not add a dependency unless the repository lacks an equivalent capability.
- Do not break existing menus.

## Data integrity

Do not change business semantics:
- membership state comes from membership data
- payment state comes from payment data
- attendance state comes from attendance/event data
- intelligence priority comes from MemberIntelligenceEngine
- theme changes never reload repository data

Do not hard-code production member identity when a snapshot value exists.

## Acceptance criteria

1. Home is default on launch.
2. Existing vertical rail remains functional.
3. Tapping Home expands Home to full available mobile width/height.
4. Expanded Home has a collapse/back control.
5. Reference A mobile proportions are respected.
6. Reference B Home information hierarchy is represented.
7. No tablet-width dashboard geometry.
8. 320dp-class phones remain readable and usable.
9. 360–430dp phones show primary intelligence without forced horizontal scrolling.
10. More reveals secondary Home data.
11. Payment/attendance/plan/workout/PT data are driven by MemberSnapshot.
12. AI Insights are driven by existing intelligence signals.
13. Existing non-Home menus remain operational.
14. All 8 themes still work.
15. Theme switching does not reload member data.
16. Existing Gradle/dependency architecture remains stable.
17. Project compiles with ./gradlew assembleDebug.
18. No duplicate Home navigation implementation is introduced.
19. UI uses existing BAD GYM design tokens instead of arbitrary colors wherever possible.
20. Commit implementation to GitHub and leave a concise Antigravity implementation note.

## Antigravity instruction

Pull latest main. Build and run Member Intelligence. Treat MobileHomeIntelligence.kt as the Home composition layer and PixelPerfectMemberCard.kt as the existing shell/router. Fix only integration compile/runtime issues; do not redesign the information architecture. Validate on a narrow phone emulator first, then a 360–430dp phone. Preserve reference geometry and existing theme tokens.
