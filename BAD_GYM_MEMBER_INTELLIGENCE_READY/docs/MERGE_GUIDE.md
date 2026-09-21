# Merge Guide

1. Copy feature/memberintelligence into the existing BAD GYM project.
2. Keep MemberIntelligenceScreen as the feature entry point.
3. Replace DemoMember with MemberSnapshot.
4. Add repository interface and Supabase implementation.
5. Map legacy member models using LegacyMemberAdapter.
6. Add feature flag `memberIntelligenceV2`.
7. Route existing LiveActivityCheckInCard to the new MemberIntelligenceScreen.
8. Keep database migrations additive; never delete existing tables during first integration.
9. Keep theme engine independent from the existing global app theme.
10. Run screenshot tests at 360dp before removing the legacy card.
