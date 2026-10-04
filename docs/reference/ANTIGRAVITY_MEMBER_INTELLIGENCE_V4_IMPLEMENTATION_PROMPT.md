# BAD GYM — UNIVERSAL AI IMPLEMENTATION PROMPT
## Member Intelligence V4

You are the implementation engineer for BAD GYM.

Do not invent a new design. Implement the approved Member Intelligence V4 specification exactly.

SOURCE OF TRUTH:
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md

Visual reference:
the latest approved compact Member Intelligence V4 design board supplied in the task.

START:

git checkout main
git pull --ff-only origin main
git status

Read first:
AGENTS.md
CURRENT_TASK.md
docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md
docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md
docs/reference/BAD_GYM_MOBILE_PIXEL_PERFECT_GUIDE.md

Then inspect all current Member Intelligence code under:
app/src/main/java/com/example/badnewgym/feature/memberintelligence/

IMPORTANT PRODUCT RULES:

- Target card width: 350dp; responsive down to approximately 320dp.
- Preferred runtime height: 500–540dp.
- Maximum normal adaptive height: 600dp.
- Global screenshot canvas: 691×1536, 20:9.
- Mobile-first; never build a tablet UI and squeeze it into a phone.
- Hero event must identify the card: CHECK-IN, CHECK-OUT, PAYMENT, PAYMENT DUE, TRAINER, MACHINE ISSUE, etc.
- Never use "Member Overview" as the hero.
- Event priority determines hero and supporting widget order.
- Membership must show days remaining, plan duration, validity and last payment; next due where applicable.
- Attendance uses 30-day data with a calendar.
- Late = green center + amber border.
- Absent = red.
- Holiday/gym closed = clear X.
- Workout uses up to 30 days of data, with a horizontally paged 7-day viewport.
- Monthly workout summary must expose total, average, peak, lowest, trend and best week only when real data exists.
- Body Progress is goal-aware, not weight-only.
- Support up to 3 active member-specific body/fitness metrics.
- Never assume positive weight change is good.
- Progress photos may be linked to measurements.
- Sensitive body metrics must support hide/show authorization.
- Trainer is conditional; never show "PT: No" as a permanent widget.
- Payment completed is not a permanent widget.
- Recent Activity is not a default widget.
- No fake production data.
- No meaningless metrics.
- No decorative filler.
- No black-heavy UI.
- Use restrained premium neumorphism with emerald/mint semantic colors.
- Every widget must have its own visual grammar.
- One primary action per actionable widget.

IMPLEMENTATION:

1. Inspect current architecture and reuse valid domain/data code.
2. Create centralized V4 design tokens.
3. Implement compact card shell.
4. Implement event priority resolver.
5. Implement HeroEventWidget.
6. Implement MemberIdentityWidget + MembershipSummary.
7. Implement 30-day AttendanceWidget.
8. Implement 30-day WorkoutDurationWidget with 7-day horizontal paging.
9. Implement BodyGoalsWidget with multi-metric goal-aware trends.
10. Implement ProgressPhotoWidget and measurement linkage.
11. Implement conditional Payment/Trainer/Issue widgets.
12. Implement action sheets for payment and communication.
13. Implement WidgetLayoutEngine.
14. Implement admin-only widget editor with:
    - enable/disable
    - priority
    - resize
    - drag/reorder
    - visibility
    - entitlement
    - preview
    - save draft
    - publish
15. Implement privacy/accessibility.
16. Add/repair tests.
17. Perform visual regression.
18. Perform device verification when an actual device is reachable.
19. Update the V4 execution ledger.
20. Commit and push verified work to main.

CHART IMPLEMENTATION:

Prefer Compose-native Canvas/custom drawing for compact charts unless an existing compatible chart library materially improves the implementation. Do not add multiple chart libraries.

All charts must communicate WHAT, WHEN, VALUE and TREND.

Never create anonymous bars or meaningless decorative lines.

TESTING LOOP:

For each meaningful component:

inspect
implement
compile
unit test
run
screenshot
compare against V4 reference
fix
rebuild
rescreenshot
verify

A passing Gradle build is not visual verification.

DEVICE:

If the Xiaomi 11i or another supported Android device is actually reachable:
- install
- launch
- exercise states
- test touch interactions
- test horizontal chart paging
- test sheets
- test privacy
- test admin drag/reorder/resize
- capture screenshots.

If not reachable:
record device verification as BLOCKED.
Never claim it was tested.

BUILD:

./gradlew clean testDebugUnitTest assembleDebug

FINAL REPORT MUST INCLUDE:

- implementation stages completed
- changed files
- tests
- build result
- visual verification
- device verification
- final commit SHA
- unresolved issues

Do not stop at a mockup.
Do not leave placeholder buttons.
Do not leave dead interactions.
Do not replace real domain logic with fake fixture logic.
