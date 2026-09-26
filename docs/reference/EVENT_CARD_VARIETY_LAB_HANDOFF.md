# BAD GYM — Event Card Variety Lab / Antigravity Handoff

## Current remote target
Branch: `member-intelligence-v3`

This packet defines the production contract for the Member Intelligence event-card system. It is intentionally separate from the old "3 commits = 3 files" pull note: the card system is a broader product surface and must be evaluated from the complete current branch.

## Verified taxonomy
- 68 typed `EventType` values currently exist in `MemberEvent.kt`.
- 68 event types now have `EventCardCatalog` entries (all except the sentinel `UNKNOWN`).
- 16 reusable visual archetypes exist in `EventCardCatalog.kt`.
- Contextual variants are layered on top through `EventCardVariantResolver`.

### Important distinction
Do NOT create one Compose function per event.

Use:
`EVENT TYPE + CONTEXTUAL VARIANT + EVIDENCE + STATE + PRIORITY + CTA`
→ one reusable archetype.

This produces 68+ user-visible semantic card variants without a 68/100-composable maintenance problem.

## Required contextual variants

### Attendance
1. Normal Check-in
2. Late Check-in
3. Early Check-in
4. Check-out
5. Walk-in
6. Walk-in → Trial Started
7. Trial Expiring
8. Trial Converted
9. Trial Expired

### Membership / access
10. Membership Started
11. Membership Renewed
12. Membership Expired
13. Membership Cancelled
14. Freeze Active
15. Freeze Ended / Reactivated
16. Access Banned
17. Ban Lifted
18. New Member
19. Member Updated

### Payment
20. Payment Received
21. Late Payment Received
22. Payment Failed
23. Payment Due
24. Payment Overdue
25. Partial Payment
26. Refund
27. Invoice Created
28. Package Purchased

### Trainer
29. Trainer Assigned
30. PT Scheduled
31. PT Started
32. PT Completed
33. PT Missed
34. PT Cancelled

### Workout / progress
35. Workout Started
36. Workout Completed
37. Workout Skipped
38. PR Achieved
39. Goal Updated
40. Body Measurement Updated

### Services
41. Service Activated
42. Service Deactivated
43. Service Booked
44. Service Used
45. Service Expired
46. Service Issue
47. Service Purchase
48. Supplement Purchase
49. Nutrition Update

### Facility / operations
50. Machine Fault
51. Machine Reported
52. Machine Fixed
53. Maintenance Started
54. Maintenance Completed
55. Cleaning Started
56. Cleaning Completed
57. Stock Low
58. Incident Reported
59. Incident Resolved

### Communication / issues
60. Complaint
61. Complaint Resolved
62. Announcement
63. Offer

### General legacy event variants
64. Generic Payment Event
65. Generic Renewal
66. Generic Expired Event
67. Generic PT Session
68. Generic Workout

The exact final visible set may expand further when event metadata contains a meaningful state, but duplicate cards must not be created just for cosmetic differences.

## Late event rule
Late check-in and late payment are **distinct presentation variants**, not duplicate event types:
- CHECK_IN + `latenessMinutes > 0` or `late=true` → LATE variant.
- PAYMENT_SUCCESS/PAYMENT + `wasOverdue=true`, `latePayment=true`, or `overdueDays > 0` → OVERDUE / Late Payment variant.

The event remains truthful and auditable. The renderer changes headline, emphasis, evidence and CTA.

## Walk-in story
Walk-in is never collapsed into a generic member card.

Required story:
`WALK-IN → TRIAL → CONVERTED / EXPIRED`

Show only fields actually present:
- visitor/member identity
- visit timestamp
- source
- trial start
- trial expiry
- conversion time
- plan
- staff/actor
- visits during trial
- last interaction

## Operational cards
Machine/facility events are first-class:
- Machine Fault
- Machine Reported
- Machine Fixed
- Maintenance Started
- Maintenance Completed
- Cleaning Started
- Cleaning Completed
- Stock Low
- Incident Reported
- Incident Resolved

Cleaning must support the operational story:
`STAFF CHECK-IN → CLEANING STARTED → CLEANING COMPLETED`
when those records exist. Do not invent attendance or completion data.

## Visual contract
Light-only BAD GYM visual system.
- No black/dark workspace.
- One coherent surface system.
- Semantic accents: critical/error, warning, success, info.
- Event headline is dominant.
- Long names wrap; do not ellipsize important identity.
- 3–5 decision facts above the fold.
- One primary CTA.
- Evidence/media only when a real media reference exists.
- NOW/PAST/FUTURE only when it improves the specific event story.
- No decorative infinite animation.
- Motion must communicate state/focus/transition.

Material 3 cards are intended to represent one coherent piece of content; use that principle while varying state, emphasis and evidence rather than making visually unrelated containers. Android's current Compose guidance also supports Material 3 card elevation/color roles and explicit image sizing/downsampling for performance. See:
- https://developer.android.com/develop/ui/compose/quick-guides/content/create-card-as-container
- https://developer.android.com/develop/ui/compose/designsystems/material3
- https://developer.android.com/develop/ui/compose/graphics/images/optimization

## Asset contract
Do not use fake production evidence.

Asset families:
- member identity thumbnails: only fixture/demo assets
- equipment thumbnails: synthetic/demo equipment assets
- service thumbnails: synthetic/demo service assets
- status/emphasis graphics: transparent assets or vector equivalents
- theme/background materials: restrained, light, transparent overlays
- chart graphics: generated from actual fixture values, not hand-painted fake data

Preferred implementation:
- vector for geometric UI decoration where possible
- PNG/WebP only where raster artwork materially improves the result
- explicit target dimensions
- thumbnails separate from full-size media
- no oversized bitmaps in cards

## Antigravity execution
1. Pull the exact latest `origin/member-intelligence-v3` HEAD.
2. Read this document and `CURRENT_TASK.md`.
3. Inspect all 68 EventType mappings.
4. Verify `EventCardVariantResolver` integration.
5. Build and test.
6. Exercise representative cards:
   - normal check-in
   - late check-in
   - check-out
   - walk-in
   - trial active/expired/converted
   - freeze active/ended
   - ban active/lifted
   - payment success
   - late payment
   - payment failed/overdue/partial
   - PT scheduled/started/completed/missed
   - workout completed/skipped/PR
   - service active/expired/issue
   - machine fault/fixed
   - maintenance
   - cleaning started/completed
   - complaint/resolved
   - incident/reported/resolved
7. Capture screenshots and record exact SHA.
8. Push only corrections that are actually required.
9. Report:
   - HEAD SHA
   - changed files
   - build result
   - test result
   - runtime device/result
   - screenshot paths
   - any unresolved issue

## Shared communication files
These are the durable coordination channel:
- `CURRENT_TASK.md`
- `AI_SYNC_STATE.md`
- `AI_COLLABORATION_PROTOCOL.md`
- this file

Every agent must write exact SHA + changed-file list + verification status back into the shared state. Do not communicate completion through commit count alone.
