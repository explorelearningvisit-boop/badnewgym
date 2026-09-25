# Google Antigravity — Stage 7.6 Production Visual QA & Finalization

## EXECUTE THIS TASK NOW

You are the visible production executor for BAD GYM Member Intelligence on branch `member-intelligence-v3`.

**Do not wait for another prompt. Pull this branch, inspect the current implementation, run the app, capture screenshots, fix the UI/UX issues you actually observe, and push the verified result back to the same branch.**

### 1. Primary user feedback

The current Member Intelligence card is visually improved, but it still has production-level problems:

- Bottom CTA/button is getting clipped or visually cut off.
- Important menu-specific information is not consistently visible near the top.
- Several menus repeat the same member information even when the selected menu has its own useful context.
- Some screens contain irrelevant or low-value information while important information is buried below the fold.
- Different menus sometimes show different names/content while communicating the same underlying context, which makes the UI feel like copied templates rather than one coherent member intelligence system.
- The card is a bounded mobile canvas. It must not become a full-screen data dump.
- Horizontal scrolling must only be used where it improves a real collection of peer items; vertical scrolling must be used for content that naturally continues downward.
- CTA placement must be intentional and never clipped.
- Text/color contrast must be semantically correct: never put low-contrast text on a similarly colored button/background.
- Existing visual direction is liked: light themes, strong color combinations, circular/ring charts, bar charts, meaningful motion, polished card depth.

### 2. Do NOT blindly redesign

First inspect the actual current code and run the current app.

Do not invent a new architecture.
Do not replace working backend/data contracts just for visual reasons.
Do not create fake member data merely to make a screenshot look richer.
Do not add decorative UI that has no operational meaning.

Use actual available `MemberSnapshot`, event, attendance, payment, trainer, workout, service, history and insight data.

If a value is unavailable, show an intentional empty/not-recorded state rather than fabricating it.

### 3. Required live visual workflow

Use Google Antigravity's visible GUI and actual running app.

For the selected member:

1. Pull latest `member-intelligence-v3`.
2. Build/run the app.
3. Capture a baseline screenshot.
4. Open **every Member Intelligence menu** one at a time.
5. For every menu:
   - capture initial viewport;
   - inspect what is visible above the fold;
   - scroll vertically;
   - if horizontal content exists, inspect the horizontal interaction;
   - capture another screenshot after meaningful scroll;
   - verify bottom CTA/action area;
   - verify no clipped content;
   - verify no duplicate/redundant information;
   - verify the menu title and its content actually belong together.
6. Fix the implementation while continuously re-running the affected screen.
7. Repeat the screenshot pass after fixes.
8. Do not stop after one successful build. Visual/runtime evidence is mandatory.

### 4. Menu information hierarchy

Every menu must answer the menu's own question first.

**HOME**
- Current member/event state
- One or two highest-priority signals
- attendance ring/bar or compact trend
- immediate next action
- only the most useful supporting metrics
- CTA must remain fully visible

**ATTENDANCE**
Top: attendance rate, visits/target, current streak/pattern.
Then: calendar/pattern/timing/live/forecast.
Then: detailed events only when requested.

**PLAN**
Top: plan name, active/expired state, remaining/expiry.
Then: benefits/timeline/renewal information.
Do not repeat payment details unless directly relevant.

**PAYMENT**
Top: outstanding amount, due/overdue state, latest payment.
Then: audit/timeline/recurring/dues/forecast.
Payment CTA must be visible and correctly contrasted.

**TRAINER**
Top: assigned coach, next session, sessions remaining/status.
Then: schedule, session history, performance/notes/media.

**WORKOUT**
Top: current routine, latest workout, weekly frequency/progress.
Then: program/sessions/progress/muscles/PRs/media.

**SUPPLEMENTS**
Top: active stack or last relevant purchase/usage only if actually recorded.
Then: inventory/usage/orders/expiry/recommendations.

**NUTRITION**
Top: current plan/status and today's useful nutrition state if recorded.
Then: macros/meals/hydration/trends/plan/media.

**SERVICES**
Top: active services, next booking/expiry, current issue if any.
Then: bookings/usage/issues/media.

**HISTORY**
Top: recent event summary and useful filters.
Then: chronological audit/event timeline.
Do not repeat the entire member header for every event.

**INSIGHT**
Top: highest-priority actionable signals.
Then: pattern/risk/opportunity/forecast/explain.
Each insight must have evidence or a linked underlying record when supported.

**MORE**
Top: real actions/settings directory.
No decorative metrics that duplicate Home.

### 5. Repetition rule

The persistent member header is allowed to remain persistent.

Everything below it must earn its space.

Remove repeated:
- member name
- plan name
- coach name
- attendance number
- payment number
- generic status chips
- identical summary metrics

when those values are already visible in the persistent header or are not needed for the selected menu.

A menu should add information, not repeat the header.

### 6. Layout and scrolling rules

Use the actual available card width/height; do not assume a desktop canvas.

- Preserve the compact member-intelligence card footprint.
- Increase/decrease internal component sizes where needed.
- Use vertical scrolling for long menu content.
- Use horizontal scrolling only for peer collections, tabs, compact timelines, chart points, or cards where horizontal browsing is genuinely useful.
- Never hide the primary CTA below an accidental clipped viewport.
- The primary CTA must be inside the content's safe bottom region.
- Respect system navigation/status-bar insets.
- Keep meaningful bottom padding.
- Avoid excessive empty space.
- Avoid giant decorative gradients.
- Keep the UI light; no black/dark-black dashboard foundation.
- Long member names must wrap safely instead of ugly truncation.
- No content should overlap another component.

### 7. Visual design direction

Merge the strongest parts of the previously generated concepts into the existing compact card:

- light premium surface;
- tasteful glass/frosted depth;
- strong but controlled accent colors;
- circular progress/ring charts;
- bar charts;
- compact trend visualization;
- semantic red/amber/green/blue states;
- subtle 2.5D depth;
- meaningful 120–360ms transitions;
- data-driven animation only;
- no infinite decorative animation.

Do not make every menu visually identical.

Each menu should have a recognizable visual personality while remaining one BAD GYM product.

### 8. CTA rules

A CTA must:
- have sufficient contrast;
- have readable text;
- communicate exactly what will happen;
- be fully visible;
- stay associated with the relevant menu;
- not duplicate another CTA unnecessarily.

Examples:
- Payment → Collect/Record Payment
- Trainer → Schedule Session
- Attendance → View Pattern / Attendance action when supported
- Workout → Open Program / Log Session when supported
- Services → Book/Resolve when supported

Only use actions that actually exist in the application.

### 9. Media / realistic imagery

Do not add random stock photos.

If the current architecture supports member media:
- use real repository/storage-backed media metadata;
- show thumbnails;
- open full media only on interaction.

If there is no real media source yet:
- create the UI state/placeholders without pretending the media exists;
- document the limitation.

Do not store raw biometric templates or camera frames in the UI layer.

### 10. Production quality checklist

Before declaring complete, verify:

- [ ] Debug build passes.
- [ ] Existing tests pass.
- [ ] Member Intelligence opens.
- [ ] All menus open.
- [ ] No menu crashes.
- [ ] No CTA clipping.
- [ ] No text overlap.
- [ ] No accidental horizontal overflow.
- [ ] Vertical scrolling works where needed.
- [ ] Horizontal scrolling works only where intentional.
- [ ] Back navigation preserves menu/context.
- [ ] Menu-specific data is prioritized.
- [ ] Repeated data is removed.
- [ ] No invented member facts.
- [ ] Button/text contrast is correct.
- [ ] Light themes remain readable.
- [ ] Reduced-motion behavior remains respected.
- [ ] Screenshots captured for every menu.
- [ ] Before/after visual evidence exists for important fixes.
- [ ] Final Git diff reviewed.
- [ ] Commit pushed to `member-intelligence-v3`.
- [ ] Exact final SHA reported.

### 11. Definition of DONE

Do not report "done" merely because Gradle succeeds.

DONE means:

**Build + runtime + every menu + scroll behavior + CTA visibility + information hierarchy + visual consistency + screenshot evidence + Git push.**

When finished, update:
- `STATUS.md`
- `CURRENT_TASK.md`
- `HANDOFF_STATUS.md`
- `CHATGPT_HANDOFF.md`
- `SESSION_CONTEXT.md`
- `AI_SYNC_STATE.md`
- `AI_COLLABORATION_LOG.md`

Include:
- exact commit SHA;
- build/test result;
- device/emulator used;
- menus visually checked;
- screenshots/evidence locations;
- remaining limitations, if any.

**Do not modify unrelated product areas.**
**Do not start Stage 8.**
