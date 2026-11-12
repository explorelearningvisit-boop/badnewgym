# BAD GYM — Repository Execution Rules & Lifetime Device Connection Memory

## ACTIVE PROJECT
Primary Flutter repository: `productionreadymembercard`
Remote: `https://github.com/explorelearningvisit-boop/productionreadymembercard.git`
Branch: `main`

## DEVICE CONNECTION & DEPLOYMENT PROTOCOL (LIFETIME MEMORY)

### PHYSICAL DEVICE SPECIFICATIONS
- **Device Model**: Xiaomi 11i 5G (Xiaomi 21091116I / MIUI / Android 13, 1080×2400)
- **Wired ADB Serial**: `zxdada69gunb7ls4`
- **Tailscale IP**: `100.123.18.54`
- **Wireless Debugging Port**: `5555` (Target: `100.123.18.54:5555`)
- **Application Package**: `com.badgym.bad_gym`
- **Main Launcher Activity**: `com.badgym.bad_gym/.MainActivity`

### DUAL-CHANNEL AUTO-FALLBACK STRATEGY
Always check connection in this exact order:
1. **Channel A — Wired USB**:
   - Run `adb devices`.
   - If `zxdada69gunb7ls4` is listed as `device`:
     - Keep wireless port active: `adb -s zxdada69gunb7ls4 tcpip 5555`.
     - Target commands directly with `-s zxdada69gunb7ls4`.
2. **Channel B — Tailscale Wireless Network**:
   - If `zxdada69gunb7ls4` is not connected, immediately connect over Tailscale:
     `adb connect 100.123.18.54:5555`
   - Target commands directly with `-s 100.123.18.54:5555`.
3. **Screen Wake & Capture**:
   - To capture clean screenshots on Xiaomi/MIUI without keyguard blanking:
     `adb -s <device> shell input keyevent 224` (WAKEUP)
     `adb -s <device> shell wm dismiss-keyguard`
     `adb -s <device> shell am start -n com.badgym.bad_gym/.MainActivity`
     `adb -s <device> shell screencap -p /sdcard/bad_gym_live.png`
     `adb -s <device> pull /sdcard/bad_gym_live.png <target_path>`

## EXECUTION MODEL
- Build → Run on device (Wired or Tailscale) → Screenshot → Inspect → Verify.
- Never declare completion from compilation alone.
- Keep `flutter analyze` at 0 issues and all unit/widget tests passing.
