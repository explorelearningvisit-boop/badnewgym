# unused/member-intelligence-v1

Legacy Member Intelligence dashboard implementations, archived from the live
source set on 05 Nov 2026 and replaced by the Intelligence V3 rebuild:

- Live replacement: `app/src/main/java/com/example/badnewgym/feature/membercard/presentation/intelligence/`
  (`MemberIntelligenceDashboard.kt` shell + `IntelligenceHeader.kt` +
  Attendance/Workout/Payment/Body intelligence screens)
- Spec: `docs/reference/INTELLIGENCE_V3_REBUILD_SPEC.md`

These files are NOT compiled (this folder is outside `app/src`). They are kept
only as historical reference. Do not import from them; do not restore them.

Archived files:
- MasterStoryboardDashboard.kt (old 4-tab storyboard shell)
- ThemedMemberCardView.kt (wrapper)
- AquaGlassMemberView.kt, AquaGlassComponents.kt, AquaGlassTokens.kt (liquid-glass v1)
- MemberDashboardComponents.kt (bento hero variant)
- CompactViewportMemberCard.kt, CompactAndExpandedDashboard.kt (compact variants)
- BadGymPassShowcase.kt (pass showcase used only by the old storyboard)
- MemberCheckInCard.kt (legacy check-in card + fallback branch)

Note: `MemberIntelligenceV2Components.kt` remains live — the new
`IntelligenceHeader.kt` reuses `IntelligenceTopBar` from it.
