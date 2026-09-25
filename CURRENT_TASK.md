# BAD GYM — Current AI Handoff

STATUS: COMPLETED
TASK_ID: MI-STAGE-6-DEEP-MENUS-PRODUCTION
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible execution
BRANCH: member-intelligence-v3
PROTOCOL_VERSION: 1.0

## Mission
Move BAD GYM Member Intelligence from the verified MI-V6 theme system into Stage 6: complete interactive deep-menu navigation and production-quality menu states.

This is a production implementation task, not a mockup.

## Product goal
When a gym owner opens a member's Member Intelligence detail card, Home is the default operational summary. Tapping a menu must replace only the content area below the persistent member/event/state header. The shell, identity, event context and navigation remain stable.

The owner must be able to understand the selected area and take the relevant action without leaving Member Intelligence.

## Required menus
Keep all existing 11 destinations intact:
1. Home
2. Attendance
3. Plan / Membership
4. Payment
5. Trainer
6. Workout
7. Supplements
8. Nutrition
9. Services
10. History
11. Insight

Do not remove, rename, or replace existing destinations.

## Required behavior
- Detail shell remains bounded and preserves current MI-V6 geometry/theme system.
- Persistent header/context stays visible while menu content changes.
- Menu selection must have clear selected state.
- Horizontal slide + fade transition target: 180–250ms.
- Preserve menu/content state where practical when switching and returning.
- Back navigation returns to the previous menu/detail state correctly.
- Avoid full-screen navigation unless existing app architecture requires it.
- Do not create fake backend data.
- Use the existing MemberSnapshot/domain models and repository abstractions.
- Loading, empty, error and populated states must be explicit where a menu can reach those states.
- Actions must have contextual labels and safe affordances.
- Disabled/unavailable menu states must explain why when entitlement/context makes them unavailable.

## Menu content minimums
Attendance:
- current/period attendance summary;
- recent check-ins/check-outs;
- attendance trend;
- meaningful empty/loading/error states.

Plan/Membership:
- current plan/tier;
- start/end/remaining information;
- entitlement summary;
- renewal/action state.

Payment:
- outstanding/paid state;
- recent transactions;
- due/overdue information;
- contextual payment action;
- failed payment state where applicable.

Trainer:
- assigned trainer;
- next session;
- recent session history;
- trainer/session action.

Workout:
- current workout/program;
- recent workout activity;
- progress/status;
- contextual action.

Supplements:
- active/recent supplements or purchases;
- status/expiry where applicable;
- relevant action.

Nutrition:
- current nutrition context/plan;
- recent adherence/context;
- relevant action or empty state.

Services:
- active/expired services;
- service history where available;
- contextual action.

History:
- chronological member events;
- readable event type/time/state;
- loading/empty/error states.

Insight:
- prioritized intelligence signals;
- urgency/state semantics;
- explain why a signal matters;
- one contextual action per actionable signal where possible.

Home:
- preserve existing MI-V6 operational hierarchy. Do not turn Home into a mini-dashboard.

## UX rules
The persistent glance hierarchy remains:
EVENT → MEMBER → TIME → STATE → DECISION SIGNAL → ACTION

Menu content must be subordinate to the shell hierarchy.

Do not introduce decorative color noise. Reuse BADGymTheme semantic/material tokens from MI-V6. Never reintroduce hardcoded same-hue text/surface combinations.

Use real interaction states:
- pressed
- selected
- disabled
- loading
- empty
- error
- success/confirmed where applicable

Touch targets should be production-appropriate and accessible.

## Technical rules
- Reuse existing Member Intelligence architecture.
- Do not create a parallel navigation architecture.
- Keep business logic out of composables where a domain/use-case layer already exists.
- Use ViewModel/StateFlow state appropriately.
- Preserve offline-first repository boundaries.
- Do not introduce new backend providers or fake APIs.
- Avoid unnecessary dependencies.
- Keep existing theme/contrast resolver intact unless a concrete defect is found.
- Keep autonomous/headless bridge disabled.

## Verification
Run:
- relevant unit tests;
- testDebugUnitTest;
- assembleDebug;
- UI/runtime verification on connected device when available;
- verify all 11 menus;
- verify selected/pressed/disabled states;
- verify transitions;
- verify back navigation;
- verify loading/empty/error states;
- verify at least one populated scenario for each menu;
- verify all 8 themes still render menu content coherently;
- verify no regression in browse/detail cards.

Capture fresh evidence:
- docs/screenshots/stage6_menu_home.png
- docs/screenshots/stage6_menu_attendance.png
- docs/screenshots/stage6_menu_payment.png
- docs/screenshots/stage6_menu_insight.png
- docs/screenshots/stage6_menu_matrix.png

If a connected Xiaomi Redmi Note 11 is available, use it for runtime verification.

## Acceptance gate
Do not mark complete until:
- all 11 menus are functional in the detail shell;
- each menu has meaningful content and appropriate state handling;
- transitions/navigation work;
- no fake data/backend shortcuts were introduced;
- MI-V6 visual system remains intact;
- tests pass;
- debug build passes;
- runtime/device verification is performed when available;
- fresh screenshots are committed;
- handoff/status/logs contain exact evidence and SHA;
- changes are committed and pushed to member-intelligence-v3.

## Execution
Use Pull and Run protocol. Visible Antigravity GUI execution only.
Do not start unrelated Stage 7/8 work in this task.
