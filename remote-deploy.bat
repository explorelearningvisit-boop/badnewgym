@echo off
echo ===================================================
echo [BAD GYM] Remote Wirelessly Deploy over Tailscale
echo Target: Xiaomi 11i (100.123.18.54:5555)
echo ===================================================

adb connect 100.123.18.54:5555
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to connect to phone over Tailscale. Ensure Tailscale VPN is ON on phone.
    exit /b %ERRORLEVEL%
)

echo [1/2] Streaming and installing APK to phone...
adb -s 100.123.18.54:5555 install -r app\build\outputs\apk\debug\app-debug.apk

echo [2/2] Launching BAD GYM application on phone...
adb -s 100.123.18.54:5555 shell am start -n com.example.badnewgym/.MainActivity

echo [SUCCESS] App updated and running live on your mobile screen!
