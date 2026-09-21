# Database Schema (Supabase / PostgreSQL)

Business source of truth is Supabase. Android never embeds the service-role key.

Suggested tables: gyms, gym_users, members, member_photos, memberships, membership_history, attendance_events, payment_transactions, payment_invoices, trainers, trainer_sessions, workouts, supplements, supplement_purchases, nutrition_plans, nutrition_subscriptions, services, service_purchases, member_events, member_notes, member_issues, intelligence_signals, gym_subscriptions, gym_entitlements, audit_logs.

Use UUID PKs, `created_at`/`updated_at` timestamptz, indexes on gym_id, member_id, event_type, occurred_at, status.

Local Room caches `members` snapshots plus pending writes (QUEUED/SYNCING/SYNCED/FAILED).
