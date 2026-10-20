@echo off
title Android Multi-Device Dev Studio
cd /d "%~dp0"
set "PYTHON_EXE=C:\Users\User\AppData\Roaming\uv\python\cpython-3.12-windows-x86_64-none\python.exe"
set "PYTHONW_EXE=C:\Users\User\AppData\Roaming\uv\python\cpython-3.12-windows-x86_64-none\pythonw.exe"

if exist "%PYTHONW_EXE%" (
    start "" "%PYTHONW_EXE%" oppo_dev_studio.py
) else if exist "%PYTHON_EXE%" (
    start "" "%PYTHON_EXE%" oppo_dev_studio.py
) else (
    start "" python oppo_dev_studio.py
)
exit
