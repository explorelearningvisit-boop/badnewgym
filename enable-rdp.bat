@echo off
echo ========================================================
echo [BAD GYM] Enable Windows Remote Desktop (RDP)
echo ========================================================

net session >nul 2>&1
if %errorlevel% == 0 (
    echo [1/4] Enabling RDP Registry...
    reg add "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Terminal Server" /v fDenyTSConnections /t REG_DWORD /d 0 /f
    reg add "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Terminal Server\WinStations\RDP-Tcp" /v UserAuthentication /t REG_DWORD /d 0 /f

    echo [2/4] Enabling Firewall Rules...
    netsh advfirewall firewall set rule group="remote desktop" new enable=Yes

    echo [3/4] Starting Services...
    sc config TermService start=auto
    sc config UmRdpService start=auto
    net start UmRdpService
    net stop TermService /y
    net start TermService

    echo [4/4] Verifying Port 3389...
    netstat -ano | findstr :3389

    echo ========================================================
    echo [SUCCESS] Windows Remote Desktop is now fully ACTIVE!
    echo ========================================================
    timeout /t 3
) else (
    powershell -Command "Start-Process '%~f0' -Verb RunAs"
)
