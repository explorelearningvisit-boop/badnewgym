@echo off
title Screen Pulse Pull
cd /d "%~dp0"
set "PYTHON_EXE=C:\Users\User\AppData\Roaming\uv\python\cpython-3.12-windows-x86_64-none\python.exe"

if not exist "%PYTHON_EXE%" set "PYTHON_EXE=python"

rem Any arguments are passed straight through, e.g.
rem   Launch_ScreenPulse_Pull.bat pull --verify
rem   Launch_ScreenPulse_Pull.bat purge --yes
start "Screen Pulse Pull" cmd /k ""%PYTHON_EXE%" screenpulse_pull.py %*"
exit
