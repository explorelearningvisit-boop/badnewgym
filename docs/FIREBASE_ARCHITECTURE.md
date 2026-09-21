# Firebase Architecture

Firebase is operational, not the business database.

- FCM: authorized staff alerts (overdue, expiry).
- Crashlytics: crashes, sync failures.
- Analytics: event types and gym IDs only. No member names.

`FirebaseEventStream` is the Android boundary.
