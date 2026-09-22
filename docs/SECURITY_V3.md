# Security V3

- Android app never contains service-role secrets.
- Use Supabase Auth + RLS for business data.
- Keep privileged calculations/mutations server-side where necessary.
- Minimize PII in analytics.
- Audit payment/admin actions.
- Use idempotency keys for events/payment operations.
- Treat local cache as sensitive.
- Avoid logging full member PII in release builds.
- Never use cleartext HTTP in production.
- Move secrets/config into local.properties/secure CI configuration.
