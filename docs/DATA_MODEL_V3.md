# Data Model V3

The UI must consume a MemberSnapshot aggregate built by repositories/use cases.

## Core aggregate

MemberSnapshot
- identity
- membership
- attendance
- payment
- trainer
- workout
- supplements
- nutrition
- services
- recentEvents
- issues
- intelligenceSignals
- menuAvailability
- entitlements

## Persistence mapping
members
member_photos
memberships
membership_history
attendance_events
payment_invoices
payment_transactions
trainers
trainer_sessions
workouts
supplements
supplement_purchases
nutrition_plans
nutrition_subscriptions
services
service_purchases
member_events
member_issues
intelligence_signals
audit_logs

Use UUID identifiers and timezone-aware timestamps.

## Example event
CHECK_IN:
- id
- gym_id
- member_id
- occurred_at
- source
- actor_id
- metadata
- idempotency_key

## Example payment
payment_invoices:
- id
- gym_id
- member_id
- invoice_period_start
- invoice_period_end
- issue_date
- due_date
- amount
- status

payment_transactions:
- id
- invoice_id
- gym_id
- member_id
- transaction_at
- amount
- method
- reference
- status
- idempotency_key
