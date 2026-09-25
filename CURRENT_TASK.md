# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: MI-STAGE-7.3-MEMBER-INTELLIGENCE-REBUILD
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity GUI only — visible Pull and Run
BRANCH: member-intelligence-v3
BASE: 85e3e8ceef449d8725b01cad51f3676a38b46727
PACKET: docs/reference/STAGE_7_3_MEMBER_INTELLIGENCE_REBUILD.md

## Mission
Rebuild Member Intelligence as a real owner-facing intelligence workspace, not a compact poster. The current runtime screenshots show clipping, weak information hierarchy, insufficient past/present/future analysis, shallow menu content, hard-coded/demo-like values, weak sub-navigation, and theme treatment that does not match the latest light-only direction.

## Non-negotiables
- Light operational UI only. No black/dark-black user-facing backgrounds.
- Do not merely recolor the current implementation.
- Replace fixed-height clipping with content-driven/adaptive scrolling.
- Large readable event headings.
- Long member names must wrap safely.
- Every primary menu must expose meaningful past/present/future information.
- Add meaningful sub-menus where they materially change the information view.
- Event-driven default menu: Payment event opens Payment; late check-in opens Attendance; PT event opens Trainer; workout opens Workout; generic member opens Home.
- Payment remains highlighted while in Payment context; do not randomly auto-switch during active reading.
- Use a typed event model and temporal/analytics presentation model.
- Every visual must have explicit metric/state/period semantics.
- AI insights must be evidence-backed; no fabricated predictions.
- No fake demo numbers/dates/transactions in production path.
- Promotions are isolated and clearly labeled.
- One contextual primary CTA per menu.
- Real media only when repository/storage supports it; otherwise omit/unavailable state.
- Preserve existing domain/repository/navigation boundaries.
- Preserve Stage 7 motion/depth behavior, but subordinate motion to readability.
- Do not start Stage 8.

## Required menu coverage
Home, Attendance, Plan, Payment, Trainer, Workout, Supplements, Nutrition, Services, History, Insight, More.

## Required verification
- unit tests for menu mapping, badges, CTA state, temporal grouping, insight evidence;
- testDebugUnitTest;
- assembleDebug;
- Xiaomi Redmi Note 11 runtime when available;
- screenshots proving all menus, Payment default-open, late check-in default-open, long name, media, promotion, and light theme matrix;
- exact SHA, branch and evidence in final handoff.

## Execution sequence
INSPECT → REBUILD DATA/IA CONTRACT → IMPLEMENT → TEST → BUILD → RUNTIME → SCREENSHOTS → DOCUMENT → COMMIT → PUSH → REPORT EXACT SHA.

FINAL COMMAND:
Pull and Run MI-STAGE-7.3-MEMBER-INTELLIGENCE-REBUILD. Do not start Stage 8.
