# BAD GYM — Repository Execution Rules

## ACTIVE PROJECT
Production Android project is repository root Gradle project, module /app, package com.example.badnewgym.
Primary cross-platform Flutter project: `c:\Users\User\AndroidStudioProjects\productionreadymembercard` (and synchronized with `flutter_app/`).
Run Gradle commands from repository root.

## LEGACY / OUT OF SCOPE
BAD_GYM_MEMBER_INTELLIGENCE_READY/ is legacy/reference only and must not receive current implementation work.

## CURRENT MEMBER INTELLIGENCE SOURCE OF TRUTH
1. AGENTS.md
2. AI_COLLABORATION_PROTOCOL.md
3. CURRENT_TASK.md
4. docs/reference/BAD_GYM_MEMBER_INTELLIGENCE_V2_CONTRACT.md
5. docs/reference/ANTIGRAVITY_END_TO_END_CONTRACT.md
6. docs/reference/BAD_GYM_MOBILE_PIXEL_PERFECT_GUIDE.md
7. actual production source under app/src/main/
8. tests

The old Member Intelligence presentation is obsolete. Do not restore it.

## DEVICE CONNECTION & DEPLOYMENT PROTOCOL (LIFETIME MEMORY)
- **Device Model**: Xiaomi 11i 5G (Xiaomi 21091116I / MIUI / Android 13, 1080×2400)
- **Wired ADB Serial**: `zxdada69gunb7ls4`
- **Tailscale IP**: `100.123.18.54`
- **Wireless Debugging Port**: `5555` (Target: `100.123.18.54:5555`)
- **Application Package**: `com.badgym.bad_gym`
- **Main Launcher Activity**: `com.badgym.bad_gym/.MainActivity`

### DUAL-CHANNEL AUTO-FALLBACK STRATEGY
Always check connection in this exact order:
1. **Channel A — Wired USB**:
   - Check `adb devices`. If `zxdada69gunb7ls4` is connected, keep TCP port open: `adb -s zxdada69gunb7ls4 tcpip 5555`.
   - Target commands directly with `-s zxdada69gunb7ls4`.
2. **Channel B — Tailscale Wireless Network**:
   - If wired is not attached, immediately connect over Tailscale:
     `adb connect 100.123.18.54:5555`
   - Target commands directly with `-s 100.123.18.54:5555`.
3. **Screen Wake & Capture**:
   - Ensure screen is awake and unlocked on Xiaomi/MIUI:
     `adb -s <device> shell input keyevent 224`
     `adb -s <device> shell wm dismiss-keyguard`
     `adb -s <device> shell am start -n com.badgym.bad_gym/.MainActivity`
     `adb -s <device> shell screencap -p /sdcard/bad_gym_live.png`
     `adb -s <device> pull /sdcard/bad_gym_live.png <target_path>`

## EXECUTION MODEL
Pull latest main first.
Inspect complete active project.
Implement the current V2 contract atomically.
Build → run → screenshot → compare → fix → rebuild → verify.
Never declare completion from compilation alone.

## GIT
Current implementation work is intended for main.
Record exact SHA, changed files, build/test/device/screenshot evidence at verified milestones.

## BUILD
./gradlew clean testDebugUnitTest assembleDebug
