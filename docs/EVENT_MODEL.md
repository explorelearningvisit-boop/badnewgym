# Event Model

`MemberEvent` is the atomic business fact: who, which gym, what happened, when, from which source, optional actor, metadata, createdAt, idempotencyKey.

Duplicate gate scans with the same idempotency key must not create duplicate attendance. Repository `recordEvent` is the write boundary.
