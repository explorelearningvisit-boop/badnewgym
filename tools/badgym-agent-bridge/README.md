# BAD GYM Agent Bridge

ChatGPT -> GitHub -> local Git -> Antigravity CLI -> GitHub.

## One-time Windows setup
1. Clone/switch to member-intelligence-v3.
2. Install/authenticate Antigravity CLI.
3. From repository root run:
   powershell -ExecutionPolicy Bypass -File .\tools\badgym-agent-bridge\Install-BADGYMAgentBridge.ps1
4. The bridge becomes a per-user Scheduled Task at logon.
5. It polls GitHub and executes READY_FOR_EXECUTION tasks automatically.

Manual test:
powershell -ExecutionPolicy Bypass -File .\tools\badgym-agent-bridge\Start-BADGYMAgentBridge.ps1 -Once

The bridge never force-pushes, never discards dirty local work, and never uses --dangerously-skip-permissions.
