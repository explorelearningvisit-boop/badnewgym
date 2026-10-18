# Aman Member Intelligence Dashboard — Component Map

This map records how the user-supplied dashboard artwork is divided into native Compose components. The image is a visual target, not a background asset; the screen remains scrollable at the phone's native viewport.

## Screen order

| Region | Compose component | Data and behavior |
| --- | --- | --- |
| BAD GYM wordmark + Search, Alerts, More | `AppBrandStrip` in `ReferenceMemberIntelligenceDashboard.kt` | Alert badge derives from `snapshot.issues.size`. Search opens the roster picker, Alerts shows the selected member's issues, More opens scope/about copy. |
| Portrait, crown/status, name/meta, plan, elapsed ring, joined/goal/branch/validity | `MemberSummaryCard.kt` → `SummaryAvatar`, `PlanChip`, `ActiveChip`/`InactiveChip`, `StatusRing`, `FooterItem` | Maps `MemberIdentity` and `MembershipStatus`; the avatar ring is green while the membership is active (gold otherwise) and the crown badge marks verification. Optional age, gender, goal, branch, and photo stay omitted/fallback when missing. `DashboardMath` derives elapsed percent and ceil-rounded days remaining. |
| Overview, Payment, Attendance, Workout, Progress, Nutrition, Machine, More | `HorizontalMemberMenu` | Eight equally weighted destinations fit the row with wallet, calendar, dumbbell, bar-chart, fork-knife, wrench and grid glyphs. Section destinations scroll the list; modules not implemented here show honest notices. More opens About. |
| Payment & Plan title, plan capsule, paid/due summary, next due, receipt action | `PaymentPlanPanel` + `PlanRangeChip` | Paid/due state comes from outstanding balance and is prefixed by a state tile (green check / red error). Next Due has a blue calendar tile and a separate relative-days chip. Collect posts through the repository/VM; paid state opens the receipt dialog. Capsule is descriptive, not a fake plan-switch button. |
| 12-month payment bars and timing/upgrade legend | `PaymentHistoryChart` | Aggregates dated `PaymentTransaction`s by calendar month; a plan-upgrade month keeps its timing color and adds a gold premium marker; unknown timing stays neutral; no transaction is not called a missed payment. |
| Weekday calendar + status counts + visit ring | `AttendancePanel`, `AttendanceDayCell`, `CompactAttendanceCount`, `CircularKpi` | Sunday-first calendar comes from dated attendance records. Present/absent/late/holiday/unknown are distinct. Ring is visits/target; absent target renders an em dash. |
| Daily workout bars + average ring + peak/low/MoM cards | `WorkoutPanel`, `WorkoutMonthChart`, `WorkoutAverageRing`, `CompactWorkoutStat` | All values derive from dated workouts. Recorded durations are bars; confirmed no-workout is red; unknown is gray. Average/peak/low exclude missing data; comparison appears only with a comparable prior month. |
| Weight, muscle, body fat, BMI tiles and sparklines | `BodyProgressPanel`, `BodyMetricTile`, `MiniSparkline` | Only recorded metrics render. Aman deltas are since-joined baseline (reference says “Since Joined”); goal-aware colors use the declared goal, BMI retains its band label. |
| Search, notification, receipt, About dialogs + snackbar | Dashboard dialog composables and `MemberIntelligenceScreen` host | Search changes member data, receipt displays actual fixture transactions, notices disclose destinations not connected in this build. |

## Reference values and fixture boundary

`MemberScenarios.compactSummaryAman()` is a **stub-only visual fixture**, reached by BG305 and selected as the initial demo member for this reference. Its workout ledger derives 48m average, 52m peak (Oct 10), 12m low (Oct 16), and +12% vs September. Body measurements derive 69.5kg, 57.2kg, 17.6%, and BMI 22.8, with deltas since the joined baseline. `AmanReferenceScenarioTest` locks those derivations. It does not populate or change production repository records.

The source art has a tall-device vs reference-canvas aspect mismatch. The app uses native dp/sp sizes and scrolls; screenshots are not stretched to claim pixel equality. Device proof and current limits are recorded in `docs/PIXEL_PERFECT_ACCEPTANCE.md`.
