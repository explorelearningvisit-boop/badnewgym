# Supabase Architecture

`SupabaseClientProvider` is the SDK boundary. Repositories call it; ViewModels do not.

RLS: gym users only see rows for gyms they belong to. Never trust client-supplied gym_id.

Realtime: subscribe to `member_events` and payment updates for the active gym/member, then push Flows into the repository.

Roles: OWNER, ADMIN, MANAGER, TRAINER, STAFF, READ_ONLY with capability gates for view/edit/collect.
