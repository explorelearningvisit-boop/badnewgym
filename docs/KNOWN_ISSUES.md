# KNOWN_ISSUES

| # | Issue | Severity | Status |
|---|---|---|---|
| 1 | Payment tab scrolls ~40 dp at default scale (AI Insight card sits partly below the fold) | Low | Accepted — content is denser than one viewport after the type increase |
| 2 | Body tab scrolls; AI Insights card is partly below the fold | Low | Accepted |
| 3 | Attendance/Workout leave ~110–120 dp free at the bottom on this device | Cosmetic | Accepted — cards are content-sized, not stretched |
| 4 | Body secondary nav is horizontally scrollable with 8 pills; edge fades added, but two pills are still off-screen | Low | Mitigated (fade + auto-scroll on tab change) |
| 5 | Changing a skin resets the selected tab to Attendance (`key(themeId)` rebuild) | Low | Known trade-off; revisit if it annoys |
| 6 | All member values are fixture data (`MemberCardData.sampleAshwiniSingh`) | High for production | Deferred to Phase 9 — needs a data source decision |
| 7 | Overview module from the brief does not exist yet | Medium | Deferred to Phase 8 |
| 8 | Appearance panel not re-verified after the typography pass | Medium | Next verification item |
| 9 | No unit tests for the new tokens/scales | Low | Deferred |
| 10 | Hardcoded amounts (`₹1,200`, `₹14,400`, `86/100`) still exist in some payment sub-views | Medium | Deferred to Phase 9 |

## Fixed in this checkpoint

- Header wrapping / clipping at raised text scale.
- Payment five-pill sub-nav hard-cut → 3+2 grid.
- Payment trend badge drawn on every point (mis-ordered `if/else`).
- Chart Y labels drawn at `x = 0` (clipped) and callout badges on the plot edge.
- Body trend start badge colliding with axis labels.
- Body workspace nav stuck at a scrolled offset when switching primary tabs.
