@echo off
title Hybrid Tracking Backend Server
cd /d "%~dp0"
set "PYTHON_EXE=C:\Users\User\AppData\Roaming\uv\python\cpython-3.12-windows-x86_64-none\python.exe"

echo Starting Hybrid Tracking FastAPI Backend on http://0.0.0.0:8000...
if exist "%PYTHON_EXE%" (
    "%PYTHON_EXE%" -m uvicorn main:app --reload --host 0.0.0.0 --port 8000
) else (
    python -m uvicorn main:app --reload --host 0.0.0.0 --port 8000
)
if errorlevel 1 (
    echo.
    echo Backend server exited with an error.
    pause
)
