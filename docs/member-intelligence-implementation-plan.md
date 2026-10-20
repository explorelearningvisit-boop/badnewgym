# BAD GYM Member Intelligence — Implementation Plan & Strategy

## Reference source (visually inspected)
Attachment `image.png` — the final approved dashboard (360dp wide). Composition:

| Section | Content | Data source |
|---|---|---|
| Top bar | BAD GYM wordmark, search, bell, settings | VM + resources |
| Left rail | Home, Members 142, Check-In 8, Payment ₹2,800, Attendance 92%, Workout 68, Body 45%, Supplements 1, History 1, Insights 11d, Messages, More | real records |
| Event header | LIVE CHECK-IN 00:02:36, Started 10:27 AM, Exercises 5/6, Calories 420 kcal, End Session | check-in events |
| Member info card | Aman Tripathi, BG305 | 23 yrs | Male, Gold Plan · Active, 135/180 days · 45 days left | snapshot identity + membership |
| Payment & Plan | ₹2,800 Due in 3 days, Gold Plan (6 Months), Last/Current/Next ₹2,800 dates, Collect Payment | PaymentSummary |
| Attendance | Oct 2026 · 24/31 · 77%, 7-col calendar, Present 24 Late 3 Absent 1 Holiday 1 Closed 1 | AttendanceMonth |
| Workout Trend | Last 30 Days bar chart, Peak 58 min (5 Oct), Lowest 12 min (9 Oct), Average 32 min ↑18% | WorkoutAnalytics |
| Body Progress | Weight 69.5 ↓2.5kg, Muscle Mass 57.2 ↑1.4kg, Body Fat 17.6% ↓1.2%, BMI 22.8 Normal, sparklines | BodyProgressSummary |
| Color states panel | Healthy/Active, Inactive/At Risk, Payment Due, Payment Upcoming, Payment Received | semantic tokens |

## Security
- No API keys, passwords, or secrets in source code.
- All member/payment/attendance data stays within existing repository + Room/OfflineFirst repository.
- No new network endpoints; Supabase client is not introduced.
- Payment collection goes through the existing ViewModel handler — success only on real result.

## Performance / memory
- Compose `remember` + `derivedStateOf` for scroll position; no full-list recomposition on every frame.
- AsyncImage with contentScale Crop for member photo; photo is a bundled asset (file:///android_asset/media/member_yash.jpg).
- Sparkline charts use lightweight Canvas/line drawing, not image assets.
- Row/Column with weights instead of nested weights where possible.

## Responsiveness
- 360dp base width (reference Mobile Size: 360dp × 600-700dp).
- No fixed pixel sizes; dp/sp throughout.
- Vertical scroll allowed by existing screen structure — do not force single-screen fit.
- Test widths 360/375/390/412dp; height 600-750dp short viewport.

## Data integrity (non-negotiable)
- No invented plan names: repository uses "Gold Plan".
- No fabricated history: 12-month timeline marks only months with real transactions.
- Attendance: present/late/absent/holiday/closed/unknown only from records; future days are not absences.
- Body progress: neutral direction when goal direction unavailable.
- All percentages reconciled from open/eligible days.

## Files to change (canonical production screen only)
- `MemberIntelligenceHome.kt` — shell, rail, event header, layout order
- `HomeWidgetsTop.kt` — member card + event hero
- `HomeWidgetsMid.kt` — payment & plan + attendance
- `HomeWidgetsBottom.kt` — workout trend + body progress
- `HomeModels.kt` — new data carriers (payment timeline, body tile, workout peak/lowest)
- `MiDesignTokens.kt` — light green/semantic palette
- Tests added under `presentation/home/`

## Phases
1. Audit + plan (done here)
2. Light theme + tokens (no dark chrome)
3. Left rail + event header (light card, live timer)
4. Member info card
5. Payment & plan card
6. Attendance card (true calendar)
7. Workout trend card
8. Body progress card
9. Build, test, screenshot, compare, fix → commit
