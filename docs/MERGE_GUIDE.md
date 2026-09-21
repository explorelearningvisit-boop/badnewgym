# Merge Guide

1. Copy `feature/memberintelligence` and `docs/`.
2. Resolve Gradle (Compose BOM, Room, Coil already in catalog).
3. Map old theme via `LegacyThemeAdapter` → `ThemeId` tokens.
4. Map old member/attendance/payment/membership/trainer models via `LegacyMemberAdapter` → `MemberSnapshot`.
5. Wire `MemberIntelligenceNavigator` to old payment/renewal screens.
6. Enable `memberIntelligenceV2`.
7. Run old and new cards side by side.
8. Run unit tests.
9. Roll out gradually.
10. Remove the old card only after validation.

Never destructive-migrate without backup.
