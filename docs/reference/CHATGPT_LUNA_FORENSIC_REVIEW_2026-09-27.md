# CHATGPT Luna — Member Intelligence Forensic Review
Date: 2026-09-27
Branch: member-intelligence-v3
Reviewed HEAD: 5824979b5d1f1169c72ae3a183d4f74f3a7a0ef1

## Executive finding

The current repository is materially better than the earlier split-rail/bottom-navigation experiments, but it is not yet a production-ready Member Intelligence system.

The active canonical card now has one left-side vertical context rail. The obsolete right-side rail concept has been removed from the domain model and resolver, and the global bottom navigation bar has been removed from the production screen.

The important remaining distinction is:

- visual/navigation architecture: substantially corrected;
- data model and truthful-state work: improved;
- real backend integration: not complete;
- physical-device verification after the latest Luna changes: not yet independently verified;
- production build after the latest Luna changes: not yet independently verified.

## What was verified directly in GitHub

### Navigation

Current code:
- `MemberIntelligenceScreen.kt` no longer renders `BadGymBottomBar`.
- `BadGymChrome.kt` now contains only the global BAD GYM/member-intelligence chrome.
- `MemberMenu.kt` no longer exposes `MenuRailSide`.
- `MenuAvailabilityResolver.kt` no longer assigns LEFT/RIGHT rail sides.
- `IntelligenceRail.kt` is icon-first and single-sided.
- `CompactMemberCard.kt` renders the member navigation only on the left.

Therefore the final direction for this cycle is:

**ONE CANONICAL CARD → ONE LEFT CONTEXT RAIL → ONE CONTENT VIEWPORT**

There is no right rail and no global bottom navigation competing with the card.

Android's current guidance also supports rail-style navigation for a small set of high-level destinations on larger windows, while bottom navigation is intended for a small set of 3–5 equal-priority destinations. The Member Intelligence card is not using bottom navigation as a second competing IA. See the Android navigation guidance linked from the project review.

## What was fixed by Luna

### Truthful data

Removed fabricated defaults from:
- Home KPI attendance;
- PT session count;
- workout count;
- membership plan/status;
- event time;
- member context header;
- synthetic fallback CHECK-IN event.

The intelligence engine can now evaluate a member without inventing a current event.

### Release safety

Preview repositories are now debug-only in `MainActivity.kt`.

Release builds use explicit unavailable repositories instead of silently displaying demo members.

This is intentional: shipping synthetic Yash/Arjun/etc. data as if it were production data is unacceptable.

### Engagement layer

The existing engagement contract remains:
- status stories;
- approvals;
- recognition;
- gym-wide recognition;
- rewards;
- communication/notification state.

Approval actions now route explicitly through the Member Intelligence navigator rather than incorrectly falling through to the member's generic CTA.

### Persistence

The optional engagement contract is persisted through the Room entity/converter path, with an additive database migration.

## Critical production blocker

The repository currently contains:

- `StubMemberRepositoryImpl`
- `StubTemporalIntelligenceRepositoryImpl`
- `StubSupabaseClientProviderImpl`

and the existing Supabase boundary exposes only a small map-based fetch contract.

There is no verified real Supabase SDK-backed Member Intelligence repository in the current source.

Therefore the UI/architecture can be production-oriented, but the full feature cannot honestly be called production-ready until the authenticated remote repository is implemented and wired.

Required real data path:

AUTH → RLS-SCOPED GYM → MEMBER SNAPSHOT → MEMBER EVENTS → TEMPORAL QUERIES → ENGAGEMENT COMMANDS → REALTIME UPDATES

No demo fallback may be used in release.

## Antigravity evidence reconciliation

Antigravity's latest recorded ACK reports:

- model: Gemini 3.7 Flash (High) / Google Antigravity Agent;
- provider/configuration: Google DeepMind Antigravity IDE;
- a successful debug build;
- successful unit tests;
- physical Xiaomi Redmi Note 11 verification;
- screenshot at `docs/reference/current-device-output.png`.

However, that evidence was recorded against the earlier Luna handoff HEAD `b6aeb1888c4f48eb593b189e10125bf4e26c03c8`.

The current reviewed HEAD is:

`5824979b5d1f1169c72ae3a183d4f74f3a7a0ef1`

There are 26 unique changed files between those two SHAs, including code and test changes.

Therefore the older build/device evidence must not be reused as evidence for the current HEAD.

## Model reporting

The project history contains inconsistent Antigravity model reports.

Older communication recorded Gemini 3.8 Flash (High), while the latest ACK records Gemini 3.7 Flash (High).

This must be resolved by the next Antigravity cycle from the actual running session. Do not infer or guess the model.

## Next mandatory execution cycle

Antigravity must pull exactly:

`5824979b5d1f1169c72ae3a183d4f74f3a7a0ef1`

Then verify:

1. compile;
2. unit tests;
3. lint;
4. physical Redmi Note 11;
5. Home;
6. every retained menu;
7. long member names;
8. overdue/all-clear payment;
9. no trainer/active trainer;
10. no nutrition/active nutrition;
11. no services/active services;
12. sparse/rich attendance;
13. empty/populated engagement;
14. approval action routing;
15. no right rail;
16. no global bottom navigation;
17. no fake production data;
18. release/debug data-source separation.

Required report:

MODEL → PROVIDER/CONFIGURATION → PULLED_HEAD → FILES_FOUND → FILES_CHANGED → IMPLEMENTATION_REASONING → TESTS → LINT → DEVICE → SCREENSHOTS → FINAL_SHA → BLOCKERS → OPEN_QUESTIONS

## Definition of done for Member Intelligence

The feature is done only when:

- one canonical card is visually coherent;
- one left context rail is enough to reach all capability-enabled menus;
- Home answers WHO / WHAT / WHY / WHAT NEXT;
- each menu has its own information personality;
- NOW / PAST / FUTURE is explicit where useful;
- evidence is drillable;
- charts are derived from real data;
- empty states are truthful;
- critical issues cannot be hidden under promotion;
- approval/recognition actions have explicit command routing;
- release builds cannot display synthetic fixtures;
- remote data is authenticated and RLS-scoped;
- realtime updates are idempotent;
- build/test/device evidence matches the exact final SHA.

## Ownership

ChatGPT Luna:
- product architecture;
- UX hierarchy;
- design system;
- data-state contracts;
- code implementation;
- review of exact Git SHA/diff.

Antigravity:
- repository pull;
- compile/test/lint;
- Android/device execution;
- screenshot evidence;
- final integration and runtime debugging.

Neither side may claim verification from the other side's old evidence.


## Final identity hardening in this cycle

- `MemberIntelligenceViewModel.loadMemberData` now requires explicit `memberId` and `gymId`.
- `MemberIntelligenceScreen` no longer embeds `BG204/gym1`.
- The debug `MainActivity` is the only current caller supplying the preview identity.
- This prevents a production screen from silently targeting a demo member.
