# Intelligence Rules

Priority order: P0 critical → P1 action required → P2 important → P3 background.

Home shows one primary (first P0/P1, else first signal) and up to three secondary signals.

Payment overdue > 7 days is P0. Payment overdue ≤ 7 days is P1. Expired membership is P0. Expiring ≤ 7 days is P1. Consistency < 70% on an active period is P1.

Membership lifecycle and payment lifecycle are separate. Never infer membership only from payment.

Expired members do not receive fake current-period attendance (`AttendancePeriodCalculator`).
