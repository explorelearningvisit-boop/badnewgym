# BAD GYM — Full Engineering + Product + Design Skill Matrix

Use this as a capability checklist. Apply the relevant skills to each task; do not over-engineer.

## A. Product intelligence
- requirements discovery and decomposition
- acceptance criteria
- product strategy
- user journeys and task flows
- gym-owner operational psychology
- information architecture
- domain modeling
- edge cases
- empty/loading/error/offline states
- prioritization and scope control
- usability and cognitive-load reduction
- decision-support design
- feature instrumentation and observability

## B. UI/UX design
- mobile-first responsive design
- Android Material 3 / Jetpack Compose
- interaction design
- visual hierarchy
- typography systems
- spacing and geometry systems
- design tokens
- color theory
- semantic color systems
- WCAG contrast
- accessibility and touch targets
- affordances
- progressive disclosure
- glanceability
- information density
- onboarding
- forms and validation
- empty/loading/skeleton states
- error/recovery states
- confirmation/undo patterns
- micro-interactions
- haptics where appropriate
- gesture design
- swipe/drag behavior
- bottom sheets/dialogs
- navigation
- responsive breakpoints
- adaptive layouts
- localization readiness

## C. Visual design / art direction
- premium SaaS visual language
- 2D composition
- gradients
- glass/frosted glass
- neumorphism when appropriate
- matte materials
- metallic/material cues
- lighting hierarchy
- shadows
- elevation
- depth
- blur
- translucency
- texture
- visual rhythm
- iconography
- illustration direction
- photography treatment
- asset direction
- visual consistency across themes
- light/dark theme systems

## D. 3D UI / spatial UI
- 3D visual hierarchy
- depth and parallax
- perspective
- lighting/shading
- material response
- glossy/plastic/metal/glass materials
- 3D cards and objects
- spatial composition
- camera framing
- controlled extrusion
- realistic shadow behavior
- 3D asset integration
- performance-aware 3D
- fallback design for low-end devices

3D must support comprehension or brand value; do not add 3D merely for decoration.

## E. Motion / animation / transitions
- motion hierarchy
- enter/exit transitions
- shared-element concepts
- state transitions
- spring/tween animation
- gesture-driven motion
- carousel motion
- shimmer/skeleton
- loading transitions
- number/value transitions
- progress animation
- micro-interactions
- press feedback
- elevation/depth transitions
- staggered content reveal
- parallax
- animation interruption/cancellation
- reduced-motion accessibility
- frame-rate/performance awareness

Motion must communicate state, hierarchy, continuity, or feedback.

## F. Android / Kotlin / Compose
- Kotlin
- Jetpack Compose
- Material 3
- state hoisting
- ViewModel
- Flow/StateFlow
- lifecycle-aware collection
- navigation
- adaptive UI
- Room
- WorkManager
- offline-first
- background sync
- permissions
- camera
- biometric/face recognition integration where appropriate
- notifications
- Firebase/FCM
- Supabase integration
- image loading/caching
- performance profiling
- Compose recomposition control
- accessibility semantics
- UI testing

## G. Web / frontend
- React
- Next.js
- TypeScript
- Tailwind CSS
- component architecture
- design systems
- responsive web
- browser compatibility
- accessibility
- performance
- SEO when relevant
- animation libraries
- charts/data visualization
- state management
- API integration

## H. Backend / API / distributed systems
- REST/JSON
- API contracts
- DTOs
- validation
- authentication
- authorization/RBAC
- pagination
- filtering/sorting
- idempotency
- transactions
- concurrency
- optimistic/pessimistic strategies
- caching
- queues
- background jobs
- event-driven architecture
- realtime subscriptions
- webhook design
- retry/backoff
- rate limiting
- observability

## I. Database / data engineering
- PostgreSQL
- Supabase
- schema design
- normalization/denormalization
- migrations
- indexes
- constraints
- transactions
- query planning
- data lifecycle
- audit history
- soft deletion
- conflict resolution
- synchronization
- analytics/event data

## J. Security / privacy
- auth/session security
- RBAC
- least privilege
- input validation
- output encoding
- secret management
- secure local storage
- API security
- OWASP-oriented review
- privacy boundaries
- PII handling
- logging hygiene
- dependency risk

## K. Testing / quality
- unit tests
- integration tests
- UI tests
- snapshot/golden tests where useful
- accessibility tests
- regression tests
- contract tests
- static analysis
- lint
- build verification
- device/browser verification
- performance tests
- visual QA
- screenshot comparison
- failure reproduction
- test data design

## L. Performance / reliability
- startup performance
- rendering performance
- recomposition
- memory
- image loading
- network efficiency
- battery impact
- offline behavior
- retry behavior
- crash prevention
- logging/telemetry
- graceful degradation

## M. DevOps / Git / delivery
- branch hygiene
- safe pull/push
- focused commits
- PR/review workflow
- CI/CD
- reproducible builds
- versioning
- release notes
- rollback strategy
- migration safety
- artifact verification

## N. Documentation / communication
- architecture decision records
- implementation notes
- design rationale
- changelogs
- screenshots/evidence
- handoffs
- risk registers
- test reports
- model-to-model communication
- user feedback capture

## O. Engineering judgment
- understand before editing
- measure before optimizing
- use the simplest production-grade solution
- avoid unnecessary dependencies
- preserve existing good work
- never hide failures
- prefer evidence over confidence
- prefer reversible changes
- verify visually when the requirement is visual
- verify behavior when the requirement is behavioral
- keep product intent above model preference

## Definition of skill application
For every task, explicitly identify the relevant skill groups before implementation and apply them during implementation and verification.
