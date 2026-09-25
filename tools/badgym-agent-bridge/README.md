# BAD GYM Agent Bridge

GitHub -> local Git -> Antigravity CLI -> GitHub, with visible live execution.

## Normal operation

1. GitHub is the source of truth.
2. The bridge polls `origin/member-intelligence-v3` every 5 seconds.
3. If the local checkout is dirty, it does **not** pull or overwrite anything; it waits.
4. If GitHub is ahead, it fast-forward pulls automatically.
5. When `CURRENT_TASK.md` is `READY_FOR_EXECUTION`, it launches Antigravity automatically.
6. Antigravity runs with Gemini 3.1 Pro High / high effort.
7. Antigravity output is streamed live to the PowerShell window and written to `bridge.log`.
8. The agent builds/tests/implements, updates handoff files, commits and pushes.
9. The bridge detects the new GitHub SHA and continues polling for the next authorized task.

## One-paste startup

From Antigravity, paste the startup instruction provided in the repository handoff. It should:

- verify the repository and branch;
- pull the latest bridge files;
- start `Start-BADGYMAgentBridge.ps1` in a visible PowerShell window;
- leave that bridge window running.

The bridge itself owns task execution. Do not manually duplicate the same task in another Antigravity process.

## Manual visible start

From the repository root:

```powershell
powershell -ExecutionPolicy Bypass -NoExit -File .\tools\badgym-agent-bridge\Start-BADGYMAgentBridge.ps1
```

For a one-cycle diagnostic:

```powershell
powershell -ExecutionPolicy Bypass -File .\tools\badgym-agent-bridge\Start-BADGYMAgentBridge.ps1 -Once
```

## Live output

The bridge uses Antigravity CLI `--output-format stream-json`, so NDJSON events appear incrementally instead of waiting for the run to finish.

## Permission mode

`skipPermissions: true` intentionally enables `--dangerously-skip-permissions` for this trusted local automation. This is required for unattended build/test/edit/commit execution. Do not use this setup on an untrusted repository.

## Safety invariants

- never force-push;
- never reset/discard dirty local work;
- never pull over dirty work;
- never invent a task;
- only execute `READY_FOR_EXECUTION`;
- only mark a task complete after its acceptance gate;
- commit and push only intended repository changes.
