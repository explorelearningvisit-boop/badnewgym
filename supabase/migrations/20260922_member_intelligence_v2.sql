-- BAD GYM Member Intelligence v2
-- Append-only event log + current-state projection.
-- Apply RLS policies to your existing gym membership/branch authorization model.

create table if not exists public.gym_operational_events (
    id uuid primary key default gen_random_uuid(),
    gym_id uuid not null,
    branch_id uuid,
    category text not null,
    status text not null,
    severity smallint not null default 3,
    title text not null,
    detail text not null,
    location text,
    actor_type text,
    actor_id uuid,
    occurred_at timestamptz not null default now(),
    related_member_id uuid,
    related_asset_id uuid,
    amount numeric(14,2),
    metadata jsonb not null default '{}'::jsonb,
    idempotency_key text,
    created_at timestamptz not null default now()
);

create unique index if not exists uq_gym_operational_event_idempotency
    on public.gym_operational_events(gym_id, idempotency_key)
    where idempotency_key is not null;

create index if not exists idx_gym_operational_events_live
    on public.gym_operational_events(gym_id, branch_id, occurred_at desc);
create index if not exists idx_gym_operational_events_member
    on public.gym_operational_events(related_member_id, occurred_at desc);
create index if not exists idx_gym_operational_events_asset
    on public.gym_operational_events(related_asset_id, occurred_at desc);

create table if not exists public.gym_live_snapshot (
    gym_id uuid not null,
    branch_id uuid not null,
    active_members integer not null default 0,
    check_ins_today integer not null default 0,
    check_outs_today integer not null default 0,
    open_maintenance integer not null default 0,
    active_trainers integer not null default 0,
    power_status text not null default 'ONLINE',
    today_revenue numeric(14,2) not null default 0,
    today_payments integer not null default 0,
    unresolved_alerts integer not null default 0,
    updated_at timestamptz not null default now(),
    primary key (gym_id, branch_id)
);

alter table public.gym_operational_events enable row level security;
alter table public.gym_live_snapshot enable row level security;

-- Replace the authorization expression below with the project's existing
-- gym_memberships/branch_memberships function before production deployment.
-- Example policy shape:
-- create policy gym_operational_events_select on public.gym_operational_events
-- for select using (public.user_can_access_gym(gym_id, branch_id));

-- Realtime: add public.gym_operational_events and public.gym_live_snapshot
-- to the Supabase realtime publication used by the application.