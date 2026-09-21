# Supabase + Firebase Boundary

Supabase:
- PostgreSQL business data
- Auth
- RLS
- Realtime
- Storage
- server-side business operations

Firebase:
- FCM push notifications
- Crashlytics
- optional product analytics

Never put a Supabase service-role key in Android.
Never send unnecessary member PII to analytics.
Use gym-scoped RLS and authenticated identities.
