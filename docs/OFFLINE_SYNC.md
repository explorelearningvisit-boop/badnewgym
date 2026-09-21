# Offline Sync

Room holds the last member snapshot. Pending events queue with idempotency keys.

States: QUEUED → SYNCING → SYNCED | FAILED. Retry must not duplicate events.

UI may show a subtle OFFLINE/SYNCING chip; it must not blank the card.
