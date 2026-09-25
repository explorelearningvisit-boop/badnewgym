# BAD GYM — Current AI Handoff

STATUS: READY_FOR_EXECUTION
TASK_ID: AGENT-SUPERVISOR-LIVE-CONTROL-CENTER
AUTHOR: ChatGPT
EXECUTOR: Google Antigravity
BRANCH: member-intelligence-v3

## Mission
AGENT SUPERVISOR / LIVE CONTROL CENTER — PRODUCTION IMPLEMENTATION

Upgrade the existing BAD GYM autonomous agent bridge into a fully observable production agent supervisor.

Do NOT replace the existing GitHub → bridge → AGY → GitHub workflow.
Do NOT create a duplicate agent.
Do NOT change the authorized task execution model.

## Requirements

1. Show current task ID.
2. Show Git branch.
3. Show AGY process state and PID.
4. Show exact model currently configured/running.
5. Show effort level.
6. Show task start time and elapsed time.
7. Show current execution phase.
8. Show live AGY stream output.
9. Persist structured machine-readable agent state (agent-state.json).
10. Persist append-only event history (agent-events.jsonl).
11. Add heartbeat/last-event tracking.
12. Detect stale output and show POSSIBLY STUCK.
13. Detect AGY process exit/crash.
14. Detect git/network failures.
15. Detect Gradle/build failures.
16. Detect ADB/device disconnect.
17. Never falsely report progress.
18. Only show percentage for defined execution milestones (Sync, Task pickup, Context analysis, Implementation, Unit tests, Build, Install, Runtime QA, Screenshot QA, Documentation, Commit, Push).
19. Keep a human-readable PowerShell live console.
20. Add a local browser-based Live Control Center (e.g., http://127.0.0.1:xxxx).
21. Dashboard must remain usable while AGY is executing.
22. Show exact states: RUNNING, WAITING, SYNCING, POSSIBLY STUCK, NETWORK ERROR, AGY EXITED, BUILD FAILED, DEVICE OFFLINE, COMPLETED, BLOCKED.
23. Show the latest meaningful event and event age.
24. Show current phase history.
25. Show task completion milestones.
26. Show final commit SHA after successful push.
27. Prevent duplicate agent launches.
28. Preserve current 5-second polling.
29. Preserve stream-json output.
30. Preserve Gemini 3.1 Pro High / high effort configuration.
31. Add graceful shutdown handling.
32. Add supervisor self-health information.
33. Add documentation explaining every visible status.
34. Add automated tests for state transitions and stale-agent detection.
35. Build and run the monitor locally.
36. Verify the existing autonomous bridge still executes normally.
37. Capture screenshots of the Live Control Center.
38. Commit and push everything to member-intelligence-v3.

## Acceptance gate
The laptop must visibly show what task is running, which model is running, which phase is running, what the latest activity was, whether the agent is healthy/stuck/error, and whether GitHub/network/ADB/build systems are healthy. 

No fake progress. No hidden background execution without observable state. No duplicate agents.

## Git rules
Never force-push.
Never erase unrelated local work.
Never reset/discard dirty work.
Commit only intended changes.
Push to origin member-intelligence-v3.

## Model
Use Gemini 3.1 Pro High with high effort. Execute immediately.
