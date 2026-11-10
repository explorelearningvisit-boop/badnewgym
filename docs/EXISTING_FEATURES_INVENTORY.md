# BAD GYM — Existing Features Inventory

Status legend: **KEEP** = behaviour preserved, **RESKIN** = logic preserved, visuals replaced,
**NEW** = added during this redesign, **DEFER** = needs real data/backend, out of scope for UI work.

## A. App shell

| Feature | Where | Status |
|---|---|---|
| Edge-to-edge window, transparent system bars | `MainActivity` | KEEP |
| Keep screen on / show when locked | `MainActivity` | KEEP |
| Device agent keep-alive, watchdog work, battery-optimisation prompt | `deviceagent/*` | KEEP |
| Deep-link sheet intent (`sheet`, `theme` extras) | `MainActivity.handleSheetIntent` | KEEP |
| Dashboard host | `MemberCheckInScreen` | RESKIN |

## B. Member header

| Feature | Status |
|---|---|
| BAD GYM wordmark + "Member Intelligence" lockup | RESKIN |
| Hamburger opens ☰ sheet | KEEP |
| CHECK-IN / CHECKED pill (green when checked) | RESKIN |
| Notification bell with unread badge | KEEP (badge static) |
| Membership due capsule (amount + date) | RESKIN |
| Member avatar (tap → body sheets) | KEEP |
| Name, member ID, age, gender | RESKIN |
| Status / plan / price / auto-renew chips | RESKIN |
| Membership progress bar with used % and days left | RESKIN |
| Four KPI tabs (Attendance, Workout, Payment, Body) | RESKIN |

## C. Attendance Intelligence

| Feature | Status |
|---|---|
| 81% donut gauge + `25/31` days | RESKIN |
| Current streak pill (6 days) | RESKIN |
| Average check-in time + delta | RESKIN |
| Present / Late / Absent / Holiday tiles | RESKIN |
| Mon–Sun calendar grid with state colours and today ring | RESKIN (day tap → bottom sheet) |
| 12-month area spline with Jan best / Mar worst / Oct current flags | RESKIN |
| 5 summary pods (streak, avg, best, lowest, tracked months) | RESKIN |
| Smart Insight + AI tag + next goal | RESKIN |

## D. Workout Intelligence

| Feature | Status |
|---|---|
| 19.4 h total-workout gauge | RESKIN |
| Avg / Best / Lowest / Consistency tiles | RESKIN |
| Daily workout trend with vertical stems, peak badges, callouts | RESKIN |
| 4 status chips (active days, streak, goal, consistency) | RESKIN |
| Monthly workout pattern with crown peak | RESKIN |
| Smart Insight + View Plan | RESKIN |
| Session history, category breakdown | DEFER (no data model yet) |

## E. Payment Intelligence

| Feature | Status |
|---|---|
| Sub-navigation (Overview / History / Plan / Behaviour / Analytics) | RESKIN (3+2 grid) |
| Payment status hero (Paid, valid till, plan, price, next renewal, days left) | RESKIN |
| 4 stat pods (lifetime paid, cycles, discounts, outstanding) | RESKIN |
| Behaviour card: 86/100 ring + on-time/early/late bars + averages | RESKIN |
| Plan history timeline | RESKIN |
| Monthly payment trend with April dip, May upgrade pill, Oct live point | RESKIN |
| AI Insight / Suggestion split card | RESKIN |
| History list + receipt sheet | KEEP |
| Reliability score / on-time % computed from transactions | DEFER |

## F. Body Intelligence

| Feature | Status |
|---|---|
| Transformation goal ring (46%) + Goal / Current / Start | RESKIN |
| Weight / Muscle / Body-fat delta pills | RESKIN |
| Body composition (BMI, fat %, muscle) + Normal/Athletic/Good chips | RESKIN |
| Measurements (chest, waist, arm) vs last | RESKIN |
| Weight & composition multi-spline trend | RESKIN |
| Progress gallery + Add | KEEP |
| AI insights list | RESKIN |
| Primary/secondary workspace nav (Overview…More) | RESKIN (auto-scroll + edge fades) |
| Measurement entry, photo upload | DEFER (no persistence) |

## G. Appearance (new)

| Feature | Status |
|---|---|
| 7 skins: Lavender (default), Snow, Mint, Sand, Steel, Rose, Midnight | NEW |
| Text size slider (0.85×–1.35×) | NEW |
| Icons & controls slider (0.85×–1.25×) | NEW |
| Corner radius slider | NEW |
| Shadow depth slider | NEW |
| Reset to guide defaults | NEW |
| Persistence across launches (`badgym.neu.style`) | NEW |

## H. Deliberately removed / not restored

- The retired eight-pack member-card presentation and its ambient blooms.
- Decorative 3D dumbbell, painted swooshes, athlete silhouettes used as pure decoration.
- Any dashboard element that only existed to fill vertical space.
