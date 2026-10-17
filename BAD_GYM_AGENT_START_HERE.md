# BAD GYM — AI Agent Start Here

**Repository entrypoint:** start with `PULL_AND_RUN.md` for the one-command setup and full handoff. Then read this file for the product-specific source-of-truth rules. If the repository is opened manually without the launcher, read this file before editing.

## Active task

Implement and verify the latest user-supplied BAD GYM Member Intelligence reference in this branch:

- Branch: `member-intelligence-reference-dashboard`
- Draft PR: https://github.com/explorelearningvisit-boop/badnewgym/pull/6
- Main implementation: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/ReferenceMemberIntelligenceDashboard.kt`
- Screen integration: `app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/MemberIntelligenceScreen.kt`
- Complete blueprint: `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
- Run instructions: `docs/OPEN_CODE_PULL_AND_RUN.md`
- Product/business context: `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`
- Pixel-perfect checklist: `docs/PIXEL_PERFECT_ACCEPTANCE.md`

## Read in this order

1. `AGENTS.md`
2. `AI_COLLABORATION_PROTOCOL.md`
3. `CURRENT_TASK.md`
4. This file
5. `docs/BAD_GYM_PRODUCT_AND_USER_CONTEXT.md`
6. `docs/OPEN_CODE_PULL_AND_RUN.md`
7. `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_HYPERDETAILED_IMPLEMENTATION_BLUEPRINT.md`
8. `docs/PIXEL_PERFECT_ACCEPTANCE.md`
9. Existing V4 architecture and execution contracts:
   - `docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V4_MASTER_SPEC.md`
   - `docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md`
10. Inspect actual Kotlin sources, domain models, ViewModel, repository implementations and tests before editing.

## Important reference-precedence rule

The user has now explicitly supplied a newer full-screen Member Intelligence reference image and asked to implement it. For this task, use that image and the new hyper-detailed blueprint as the visual target for the **expanded scrollable Member Intelligence screen**.

Preserve the established V4 engineering/business rules unless the new reference explicitly requires a change: real data, event/payment semantics, accessibility, privacy, server-authoritative entitlements, no dead buttons, no unrelated project edits, and build/run/screenshot/compare/fix verification.

Do not silently confuse two different surfaces:
- **Expanded dashboard/reference page:** the latest full-screen image, with identity, payment history, attendance calendar, workout trend and body progress.
- **Compact event-first visit/member card:** the existing V4 runtime surface and its event/entitlement architecture.

Current branch status: `MemberIntelligenceScreen.kt` is wired to `ReferenceMemberIntelligenceDashboard` as the active screen, while `PixelPerfectMemberCard.kt` and the existing domain/event system remain in the repository. This was the initial visual integration for the new reference, not proof that navigation/regression is complete. During verification, confirm the intended app entry flow. If the compact canonical card must remain directly reachable, add a deliberate navigation route rather than deleting it or bypassing its domain/entitlement logic.

The exact latest reference available in the current task environment is 921×1536 pixels (portrait ratio 0.600; JPEG bytes despite an earlier `.png` filename). It is not yet committed to this branch. Add it as `docs/reference/assets/member-intelligence-full-screen-reference.jpg` for reproducible pixel comparison. The older V4 spec's 691×1536 canvas applies to the previous compact-card reference and is not the pixel canvas for this newer screenshot. Runtime dp geometry must still be measured independently from screenshot pixels.

## User's goal

The user wants to pull the repository and run it without manually re-explaining prior decisions. The repository must explain the product goal, what changed, why it changed, the design rules, implementation plan, build commands and acceptance criteria. Keep the docs self-contained and update them when decisions change.

## Hard constraints

- Native Android, Kotlin, Jetpack Compose, Material 3.
- Repository root Gradle project; module `:app`; package `com.example.badnewgym`.
- Light, premium, mint/white/emerald-led visual language; user dislikes dark/black backgrounds.
- Mobile-first; inspect at 360dp and approximately 320dp.
- Use actual member data. Sample values may live only in stub/preview fixtures and must be labelled as demo.
- Never confuse unknown data with zero, absent, missed payment or holiday.
- Do not render fake body metrics, fake payments, generic athlete photos as real member photos, or made-up chart series.
- Keep actions real; no decorative no-op buttons.
- Respect member privacy and backend entitlements.
- Do not modify OPPO/mobile-control tools, Banaras Tour Gold work, or unrelated projects.
- Do not merge this branch or claim production-ready status until build, tests, screenshot comparison and integration review are complete.

## Required execution loop

Inspect → implement one component → compile → test → run → capture screenshot → compare to reference → record discrepancies → fix → rebuild → capture again → verify interactions/states → record evidence → commit.

A successful compile is not proof of pixel-perfect quality.

## Final report must include

- branch and exact HEAD SHA
- changed files
- build/test result
- screenshot/device verification result (or explicit BLOCKED status)
- production data integration gaps
- remaining defects
- exact command the user should run next

Never claim a device or visual test happened unless evidence exists.
