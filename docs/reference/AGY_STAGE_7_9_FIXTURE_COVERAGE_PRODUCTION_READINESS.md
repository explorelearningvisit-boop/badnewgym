# BAD GYM — Stage 7.9 Production-Ready Member Intelligence Fixture & Coverage Pack

## Mission
Make Member Intelligence fully testable across the complete event-card taxonomy before the feature is considered production-ready.

The current branch already contains the Stage 7.8 event taxonomy/registry and AdvancedEventMemberCard. The latest Antigravity fix commit is `d7d65cff3aff7e45f216f55c9e12aefb167b8b2c` and reports local `testDebugUnitTest assembleDebug` success. This task is the next execution packet.

## Non-negotiable principle
Generate a complete deterministic **test fixture universe** for every supported event/card variant. These fixtures are test/demo data only and MUST NEVER be mistaken for live production records.

Do NOT fabricate production member records, real biometric data, real payments, real plans, or real operational incidents.

## Deliverables

### 1. Complete event fixture dataset
Create a typed, deterministic fixture provider covering EVERY EventType in `MemberEvent.kt` and every `EventCardKind` in `EventCardCatalog.kt`.

For each event variant provide, where applicable:
- member identity
- event id
- event timestamp
- recorded timestamp
- actor/source
- state/status
- title/description
- supporting facts
- CTA/action
- menu route
- NOW/PAST/FUTURE context
- related-event/story links
- media reference metadata
- issue/service/payment/trainer/workout/plan fields required by that card

Use realistic but clearly synthetic fixture names/values. Add a visible Fixture/Demo indicator in the fixture-only surface.

### 2. Full card coverage
The fixture gallery must expose and render all card variants, not merely the 9 currently visible examples.

Minimum coverage:
- Check-in
- Check-out
- Walk-in
- Trial: Active / Expiring / Expired / Converted
- Freeze
- Ban / Access Restriction
- Member Created / Updated
- Membership Started / Renewed / Expired / Cancelled
- Payment Success / Failed / Due / Overdue / Partial / Refund / Invoice / Package
- Trainer Assigned / Scheduled / Started / Completed / Missed / Cancelled / Note
- Workout Started / Completed / Skipped / PR / Goal / Body Measurement
- Service Activated / Deactivated / Booked / Used / Expired / Issue / Resolved
- Machine Fault / Reported / Fixed
- Maintenance Started / Completed
- Cleaning Started / Completed
- Inspection Started / Completed
- Stock Low / Replenished
- Area Closed / Reopened
- Incident Reported / Resolved
- Announcement / Offer / Message Sent / Delivered / Failed
- Feedback / Complaint / Complaint Resolved
- Insight/action/pattern/risk/opportunity/forecast/explain variants where represented by the current model

If the current taxonomy contains additional event values, those MUST also receive fixtures.

### 3. Synthetic raw-data layer
Create a single deterministic fixture source, not scattered hard-coded card literals.

Provide:
- raw event list
- member snapshots
- payment records
- attendance history
- trainer sessions
- workout sessions
- service entitlements
- plan timeline
- issue/operations records
- media metadata
- temporal datasets for NOW/PAST/FUTURE
- relationships between events

The UI must consume these fixtures through the same mapping/renderer path used by production data.

### 4. Synthetic raw-media/assets
Create local fixture assets only where the UI actually supports media.

Required coverage:
- multiple synthetic member portrait assets with explicit fixture/demo semantics
- equipment/machine/service images for relevant cards
- generic evidence placeholders for cards with no real media
- thumbnail + full-media metadata path where supported

Never represent generated fixture media as a real member photograph or real gym evidence.

### 5. Fixture gallery / QA surface
Add a developer/QA-only Member Intelligence fixture gallery that can:
- browse every event/card variant
- filter by card kind
- filter by event type
- filter by NOW / PAST / FUTURE
- open card detail
- trigger primary CTA
- inspect routing
- inspect expanded story
- inspect media metadata/viewer when supported
- verify long member names
- verify missing optional data
- verify no-service state
- verify no-media state
- verify long descriptions
- verify disabled capability state
- verify plan entitlement state

Do not expose the QA gallery as a normal production customer menu.

### 6. Edge-case fixtures
Include deterministic fixtures for:
- long member name
- missing photo
- missing payment
- zero outstanding
- overdue payment
- expired membership
- no trainer
- no active service
- disabled gym capability
- plan excludes service
- missing media
- long issue description
- multiple simultaneous alerts
- event with no CTA
- future-only schedule
- historical-only event
- empty weekly attendance pattern
- incomplete attendance pattern (<7 days)
- exactly 7 days
- 30/90/365-day attendance history
- multiple events at same timestamp
- unknown/legacy event fallback

### 7. Testing
Add unit tests proving:
- every EventType has a fixture
- every EventCardKind is reachable
- every fixture resolves to the expected card kind
- every fixture has deterministic routing
- every card keeps one primary CTA where applicable
- no card displays unsupported/fake data
- walk-in story transitions correctly
- trial lifecycle transitions correctly
- freeze/ban states are distinct
- check-in override routing remains deterministic
- incomplete attendance data never renders a misleading 7-day graph
- disabled capabilities do not create visible cards
- long names do not overflow
- missing media never creates broken image UI

Add screenshot/Compose UI coverage for the complete fixture gallery where practical.

### 8. Production-readiness checks
Run:
- ./gradlew testDebugUnitTest
- ./gradlew assembleDebug
- lint/static checks already used by this project
- physical-device verification on the configured Xiaomi Redmi Note 11
- capture representative screenshots plus a fixture-gallery coverage screenshot
- verify scroll, CTA, text wrapping, menu routing, temporal state and media fallback

### 9. Evidence
Commit:
- fixture provider/source
- fixture assets
- QA gallery
- tests
- documentation
- screenshots/evidence

Update `CURRENT_TASK.md` and status docs with exact commit SHA and verification results.

## Acceptance criteria
This task is NOT complete if only 9 cards are visible.

Completion means the project can deterministically render and inspect the entire supported event taxonomy through one reusable production renderer with complete synthetic fixture coverage.

Do not claim "production ready" until build, tests, and device evidence are actually present.

Execution owner: Google Antigravity.
ChatGPT: review pushed SHA/evidence after execution.
