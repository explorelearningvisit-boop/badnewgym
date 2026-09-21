# Member Intelligence Spec

## What
A compact operational card for one gym member: identity, current event, the single most important signal, a few supporting facts, and one next action.

## Why
Owners should not open Attendance + Payment + Plan + Trainer to decide what to do. Home is intelligence. Other menus are evidence.

## Flow
Event → persist (idempotent) → load snapshot → `MemberIntelligenceEngine` → menus via `MenuAvailabilityResolver` → CTA via `ContextualCtaEngine` → UI.

## Isolation
All code lives under `feature/memberintelligence/`. Public API: `MemberIntelligenceEntryPoint`, `MemberIntelligenceRepository`, `MemberIntelligenceNavigator`. Feature flag: `memberIntelligenceV2`.
