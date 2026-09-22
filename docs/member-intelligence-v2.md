# BAD GYM Member Intelligence v2

## Product model

Member Intelligence is a member + gym context cockpit, not seven disconnected screens.

The UI hierarchy is:
1. Identity — member, membership tier, live status.
2. Now — the newest event and the highest-priority action.
3. KPIs — attendance, payment, plan, trainer/workout.
4. Context — gym-wide operational pulse relevant to the member.
5. Menu data — every selected menu owns its facts and actions.
6. History/evidence — recent events and source metadata.

On phones below about 600dp the side rail is replaced by a compact horizontal module navigator. On larger screens the same modules use a narrow rail. Content uses a lazy scrolling column, so long menus scroll instead of creating a giant empty card.

## Event model

Two event streams are intentionally separate:
- MemberEvent: an event directly belonging to a member.
- GymOperationalEvent: a gym-wide event such as power loss/restoration, equipment fault/repair, trainer absence/return, cleaning completion, utility payment, tax/ledger entry, or facility work.

The UI may relate the streams through relatedMemberId, relatedAssetId, location, and metadata without misrepresenting ownership.

## Production event categories

- Access: check-in, check-out, gate/face/GPS verification, walk-in.
- Membership: new member, renewal, expiry, freeze, reactivation.
- Payment: invoice, successful payment, failed payment, refund, outstanding.
- Trainer: absent, arrived, session scheduled, started, completed, cancelled.
- Equipment: fault detected, technician assigned, repair started, repair completed.
- Maintenance: cleaning task, sanitation checklist, consumable replacement, inspection.
- Power: outage, backup started, power restored, meter anomaly.
- Facility: mirror installation, repair, procurement, budget approval.
- Finance: electricity bill, vendor payment, tax/GST entry, cash reconciliation.
- Security/System: alerts, device offline, sync failures, audit events.

## Supabase/Postgres mapping

Use append-only event tables plus current-state projections.
- gym_operational_events is the immutable operational event log.
- member_events is the immutable member event log.
- gym_live_snapshot is a projection for fast cockpit reads.
- maintenance_work_orders, trainer_sessions, payments, utility_bills, and assets keep domain-specific state.
- RLS must scope every row to a gym/branch membership.
- Realtime should publish inserts/updates for operational events and snapshot changes.
- Idempotency keys are required for device/gate/payment integrations.

The Android repository should consume the interfaces in domain/repository; Supabase is an adapter, not a UI dependency.