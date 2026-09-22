GOOGLE ANTIGRAVITY RUNBOOK

1. Read AGENTS.md and all docs/*_V3 or V3 requirements files.
2. Inspect current app entry point, theme, navigation, repository layer and member feature.
3. Implement the reusable theme/tier/event shell before polishing individual cards.
4. Implement menu registry + ViewModel state + real content switching.
5. Implement Home intelligence and detail menus.
6. Add graph dependency only after compatibility check; use decision-useful charts.
7. Add demo fixtures for all eight themes and member states.
8. Add repository interfaces and adapters; keep demo repository clearly isolated.
9. Add asset directories and real layered assets where permitted.
10. Compile/test locally only if tooling is available; report actual results rather than inferred success.

Acceptance: every menu changes content; theme switches preserve business data; CTAs emit typed actions; loading/error/offline/syncing states exist; metrics have explicit periods; expired/reactivated scenarios are temporally correct; no secrets are committed.