$ErrorActionPreference = "Stop"
Unregister-ScheduledTask -TaskName "BAD GYM Agent Bridge" -Confirm:$false -ErrorAction SilentlyContinue
Write-Host "BAD GYM Agent Bridge removed."
