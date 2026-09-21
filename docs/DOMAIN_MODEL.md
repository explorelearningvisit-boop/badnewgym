# Domain Model

`MemberSnapshot` is the UI/domain contract. Database entities and remote DTOs must map into it.

Core types: MemberEvent, MembershipStatus, AttendanceSummary, PaymentSummary, TrainerSummary, WorkoutSummary, SupplementSummary, NutritionSummary, ServiceSummary, IntelligenceSignal, MemberMenu.

Events include `idempotencyKey`, `source`, and `displayLabel()` so raw enums never appear in production UI.
