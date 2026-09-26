# BAD GYM — Member Intelligence Menu Concept Board

## STATUS
**ACTIVE DESIGN REVIEW — MENU IA IS NOT YET LOCKED**

This document records the menu concepts discussed with the user. It is a selection board, not permission to hard-code every menu.

The user explicitly wants to review menu varieties/concepts first and retain only the menus that are useful in daily gym operations. Additional menus may be introduced when a distinct operational workflow requires them.

## NON-NEGOTIABLE NAVIGATION DIRECTION

1. Do not assume a fixed count of 3, 10, 12, or 15 menus.
2. The Member Intelligence system must support a growing menu universe.
3. Do not keep the current right-side vertical rail as the final UX.
4. Move contextual/right-side navigation to a bottom placement or another space-efficient contextual pattern after prototype review.
5. Preserve a persistent compact member identity/context header on every non-HOME menu.
6. HOME may use a larger identity treatment.
7. Menu content must be decision-oriented, not a generic repeated card.
8. Minimize both horizontal and vertical scrolling.
9. Do not fabricate metrics, history, photos, services, promotions, or analytics.
10. Menus may be hidden when the underlying capability/data is genuinely unavailable.
11. A menu can contain internal submenus when that reduces navigation clutter.
12. Auto-open should surface the menu associated with the member's most important actionable problem.
13. HOME and the auto-opened problem menu must communicate the same underlying priority.
14. One canonical Member Intelligence card/detail surface; no duplicate secondary dashboard shell.

---

## CANDIDATE MENU UNIVERSE

### CORE

| Candidate | Purpose | Typical evidence |
|---|---|---|
| Home / Command | Cross-menu operational summary | current event, primary signal, CTA |
| Attendance | Visit behavior and attendance recovery | check-ins, timing, frequency |
| Plan / Membership | Entitlement and lifecycle | plan, dates, freeze, expiry |
| Payment | Collection and financial state | due, overdue, failed, partial, receipts |

### TRAINING / FITNESS

| Candidate | Purpose | Typical evidence |
|---|---|---|
| Trainer | Coaching operations | assigned coach, sessions, next/missed |
| Workout | Workout execution | routine, recent workout, milestones |
| Progress | Physical/fitness progress | measurements, strength, PR |
| Goals | Goal tracking | target, progress, deadline |

### LIFESTYLE / ADD-ONS

| Candidate | Purpose | Typical evidence |
|---|---|---|
| Nutrition | Diet/macros/adherence when connected | diet plan, macros, adherence |
| Supplements | Stack/purchases/renewal | actual supplement history |
| Services | Value-added entitlements | active services, usage, expiry |

### INTELLIGENCE / OPERATIONS

| Candidate | Purpose | Typical evidence |
|---|---|---|
| Insight | Prioritized signals and decision support | P0/P1/P2 signals, evidence, action |
| Issues / Resolution | Member-facing problems and resolution | issue, owner, state, next action |
| History | Evidence timeline | event history, filters |
| Communication | Follow-up and message state | messages, feedback, reminders |
| Offers | Real promotions and eligibility | promotion, expiry, eligibility |
| More | Secondary operational actions | grouped low-frequency actions |

### CONTEXTUAL / CONDITIONAL

These should not be forced onto every member.

| Candidate | When useful |
|---|---|
| Walk-in / Lead | lead/visitor workflow exists |
| Trial Journey | member is/was in a trial lifecycle |
| Access | freeze/ban/restriction state exists |
| Facility / Operations | member-related facility/incident evidence exists |
| Documents | invoices, receipts, agreements, documents exist |

---

## MENU VARIETY CONCEPTS

### Concept A — Classic Vertical Menu
A compact icon + label vertical menu.

Use when the available width is sufficient and the menu count is small.

Risk: a large menu universe becomes visually tall and competes with member content.

### Concept B — Intelligence-First Vertical Menu
Only the most relevant operational menus are prominent; the rest are grouped.

Example:
- Payment
- Plan
- Trainer
- Home
- Attendance
- Insight
- More

Risk: useful secondary capabilities can become hidden.

### Concept C — Adaptive Priority Menu
The menu universe is stable, but the order/visibility of surfaced items responds to the member's actual state.

Example member A:
- Payment
- Plan
- Trainer
- Home
- Attendance
- Insight
- More

Example member B:
- Issue
- Trainer
- Workout
- Attendance
- Home
- Payment
- More

**Important:** adaptive ordering must not alter the underlying business meaning of a menu or invent a problem.

### Concept D — Bottom Contextual Menu
Primary navigation moves to the bottom of the canonical card; contextual menus appear as compact chips/items.

This is the current required direction for replacing the right-side vertical rail.

Potential structure:
- top: persistent member context
- center: selected menu intelligence
- bottom: primary/contextual menu strip

Risk: too many bottom items can become crowded; internal grouping or horizontal paging may be needed.

### Concept E — Bottom Primary + Expandable Context
Bottom area contains a small primary set plus an expandable menu tray.

Useful when the complete menu universe is larger than the card width.

### Concept F — Hybrid Bottom Navigation
Bottom navigation has:
- 4–6 primary menus
- one "More" entry
- contextual menus surfaced only when evidence/capability exists

This is likely the most scalable direction if the menu universe continues to grow, but it must be validated visually on the actual device.

---

## MENU CONTENT PERSONALITY

A menu is not complete merely because its label exists.

### Home
Large identity + current state + primary decision + CTA.

### Attendance
Consistency, recent pattern, timing, missed/recovery opportunity.

### Plan
Entitlement, expiry, access, freeze, renewal.

### Payment
Amount/state, due age, last payment, collection timeline, action.

### Trainer
Coach, session state, next/missed session, remaining sessions, action.

### Workout
Routine, recent evidence, frequency/gap, milestone, action.

### Progress
Measurements/strength/PR/goal progress only when actual evidence exists.

### Goals
Goal, current progress, target, deadline, next action.

### Nutrition
Plan/macros/adherence only when connected; otherwise clearly unavailable state.

### Supplements
Actual stack/purchase/renewal information only.

### Services
Active services, usage, expiry, entitlement/action.

### Insight
Signals + evidence + priority + decision + CTA.

### Issues
Problem, state, owner, reported time, resolution, next action.

### History
Evidence timeline and filters; not a giant audit dump inside the front card.

### Communication
Last contact, delivery/failure/feedback, next follow-up when real data exists.

### Offers
Only actual promotion/eligibility/expiry information.

### More
Grouped low-frequency operations.

---

## USER SELECTION GATE

**Do not permanently lock the final menu IA until the user reviews the menu concepts.**

The implementation agent should:
1. Build/prepare a lightweight concept/fixture view that makes the alternative menu structures visually comparable.
2. Show the user the available concepts/variants.
3. Record the selected concept(s) in this file.
4. Only then harden the final navigation structure.

Until selection:
- existing code may be refactored safely,
- right-side rail may be removed/repositioned,
- menu content architecture may be prepared,
- but do not unnecessarily delete supported capabilities.

## SELECTION RECORD

User-selected concept: **PENDING**

User-selected menus to keep: **PENDING**

User-selected menus to move under More: **PENDING**

User-requested additional menus: **PENDING**

User-requested menus to remove: **PENDING**

Final navigation lock commit: **PENDING**

---

## QA REQUIREMENTS FOR ANY SELECTED MENU

For every retained menu:
- member identity remains obvious;
- menu-specific information is useful within the available viewport;
- primary decision metric/state is above the fold;
- CTA is visible when an actionable state exists;
- no giant empty canvas;
- no vertical letter stacking;
- no clipped text/transactions;
- no unnecessary scrolling;
- no fake analytics;
- no duplicate identity/header;
- actual data/capability gates visibility;
- responsive on narrow physical device;
- accessible labels;
- reduced-motion safe;
- deterministic routing.

The menu concept is accepted only after physical-device visual review.
