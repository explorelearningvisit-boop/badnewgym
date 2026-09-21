# ANTIGRAVITY_HANDOFF.md

You are receiving a complete BAD GYM Member Intelligence visual/asset starter pack.

ATTACHED/IMPORTED REFERENCE:
- preview/ALL_8_THEMES_CONTACT_SHEET.png
- preview/BAD_GYM_Member_Intelligence_8_Themes.gif
- preview/theme_01...theme_08 PNGs

SOURCE ASSETS:
- assets/members
- assets/products
- assets/icons
- assets/backgrounds

ANDROID SOURCE:
- app/src/main/java/com/badgym/memberintelligence

MISSION:
Integrate this Member Intelligence feature into the existing BAD GYM Android project.

DO NOT delete unrelated features.
DO NOT replace the entire app theme.
DO NOT turn the GIF into a UI.
The GIF is a visual review artifact only.

IMPLEMENT:
1. Inspect existing project.
2. Build imported standalone source.
3. Fix any version/dependency mismatch against the existing project's versions.
4. Replace demo models with the existing project's real member data through adapters.
5. Preserve the 8-theme visual system.
6. Replace placeholder YS photo with real member photo via Supabase Storage/Coil.
7. Connect member events to Supabase.
8. Add Room/offline cache if the existing app already uses it; otherwise add it at the repository layer.
9. Add Supabase Realtime for live events.
10. Add Firebase FCM/Crashlytics only through infrastructure.
11. Add feature flag `memberIntelligenceV2`.
12. Route the existing Live Activity entry point to this implementation.
13. Keep old implementation available behind the flag until validation is complete.
14. Add screenshot tests at 360dp, 375dp, 390dp, 412dp.
15. Validate every theme and every adaptive menu.
16. Do not hard-code production data.

CRITICAL:
The 8 themes are skins of one Member Intelligence engine, not 8 separate products.

SAME DATA.
SAME BUSINESS LOGIC.
SAME MENUS.
DIFFERENT VISUAL PERSONALITY.

Compile and run after each major migration step.
