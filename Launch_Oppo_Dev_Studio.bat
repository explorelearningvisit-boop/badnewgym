@echo off
title Android Multi-Device Dev Studio
cd /d "%~dp0"
set "PYTHONW_EXE=C:\Users\User\AppData\Roaming\uv\python\cpython-3.12-windows-x86_64-none\pythonw.exe"
if exist "%PYTHONW_EXE%" (
    start "" "%PYTHONW_EXE%" tools\oppo_dev_studio.py
) else (
    start "" pythonw tools\oppo_dev_studio.py
)
exit
