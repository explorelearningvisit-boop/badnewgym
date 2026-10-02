# BAD GYM — ChatGPT → Laptop → Tailscale ADB → Xiaomi 11i

This directory contains the laptop-side MCP bridge for controlling the Xiaomi
11i that runs BAD GYM.

## Architecture

~~~text
ChatGPT
  │ custom MCP app
  ▼
OpenAI Secure MCP Tunnel
  │ outbound-only
  ▼
Windows laptop
  │
  ├─ BAD GYM Xiaomi MCP server
  └─ ADB client
       │ Tailscale
       ▼
Xiaomi 11i — 100.123.18.54:5555
       │
       └─ com.example.badnewgym
~~~

The bridge deliberately does not expose arbitrary ADB shell access.

## Exposed operations

- device/model/display/process status
- APK install
- BAD GYM launch/stop
- destructive app-data clear only with explicit confirmation
- live screenshots returned as images
- saved screenshots
- UIAutomator XML
- tap/swipe
- Back/Home and selected key events
- ordinary test text input
- wait for UI text/resource-id
- filtered logcat
- bounded UI smoke flows

## Laptop prerequisites

Install Android SDK Platform-Tools, Python 3.11+, Tailscale and Git.

The laptop must be able to run:

~~~powershell
adb connect 100.123.18.54:5555
adb -s 100.123.18.54:5555 get-state
~~~

The expected state is: device.

Do not expose ADB TCP/5555 to the public internet. Keep the phone on the
existing Tailscale path.

## Start the MCP server

Install the dependency:

~~~powershell
py -3 -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r requirements.txt
~~~

Then run:

~~~powershell
.\.venv\Scripts\python.exe .\xiaomi_mcp_server.py
~~~

Environment overrides:

~~~powershell
$env:BADGYM_XIAOMI_SERIAL = "100.123.18.54:5555"
$env:BADGYM_PACKAGE = "com.example.badnewgym"
$env:ADB_PATH = "C:\Android\Sdk\platform-tools\adb.exe"
$env:BADGYM_ARTIFACT_DIR = "C:\badgym\device-artifacts"
~~~

## MCP inspection

Use MCP Inspector to validate the server before connecting ChatGPT:

~~~powershell
npx @modelcontextprotocol/inspector@latest
~~~

Verify discovery and exercise read-only operations first. Then test the device
write operations.

## Connect to ChatGPT

For a private laptop-side MCP server, use OpenAI Secure MCP Tunnel rather than
opening the MCP server to the public internet.

The current OpenAI setup requires:

1. Create/manage a tunnel in OpenAI Platform tunnel settings.
2. Run tunnel-client on the laptop.
3. Configure the tunnel to launch this MCP server over stdio.
4. Run tunnel-client doctor and tunnel-client run.
5. In a ChatGPT workspace with current developer-mode/custom-MCP access, create
   a developer-mode app using the Tunnel connection.
6. Select the tunnel and add the app to the BAD GYM development chat.

Example tunnel profile command:

~~~powershell
$env:CONTROL_PLANE_API_KEY = "sk-REPLACE_WITH_RUNTIME_KEY"
tunnel-client init --sample sample_mcp_stdio_local --profile badgym-xiaomi --tunnel-id tunnel_REPLACE_ME --mcp-command "python C:\badgym\repo\docs\device-bridge\xiaomi_mcp_server.py"
tunnel-client doctor --profile badgym-xiaomi --explain
tunnel-client run --profile badgym-xiaomi
~~~

Never commit the runtime API key or other tunnel credentials.

## Autonomous BAD GYM loop

After the MCP app is connected, the intended development loop is:

1. Read the BAD GYM source of truth.
2. Edit Android source.
3. Build the APK on the laptop.
4. Install the APK through the device bridge.
5. Launch BAD GYM.
6. Capture the live screen.
7. Inspect UIAutomator state.
8. Tap/swipe through the target scenario.
9. Capture evidence after every meaningful transition.
10. Inspect logcat on failures.
11. Fix source and repeat.
12. Only mark the change production-ready after the real device flow passes.

GitHub remains source control. Tailscale + ADB remains the runtime path.

## Production security

The bridge:

- pins the default Xiaomi Tailscale target
- pins the BAD GYM package
- exposes no arbitrary shell tool
- limits key events
- bounds smoke-flow length
- requires explicit confirmation before app-data deletion
- stores screenshots locally as artifacts
- uses Secure MCP Tunnel rather than inbound public access

For a multi-user deployment, add MCP-layer workspace/user authorization
before enabling destructive operations.

## Acceptance checklist

- [ ] Laptop reaches Xiaomi through Tailscale
- [ ] ADB state is device
- [ ] MCP Inspector discovers the bridge
- [ ] Secure MCP Tunnel is healthy/ready
- [ ] ChatGPT discovers the custom app
- [ ] device_status works from ChatGPT
- [ ] ChatGPT receives a live screenshot
- [ ] ChatGPT can tap/swipe and observe changes
- [ ] BAD GYM APK install/launch works
- [ ] CHECK-IN passes
- [ ] CHECK-OUT passes
- [ ] PAYMENT/PAYMENT_OVERDUE passes
- [ ] Failed flows produce useful logcat evidence

## Official references

- OpenAI Secure MCP Tunnel:
  https://developers.openai.com/api/docs/guides/secure-mcp-tunnels
- OpenAI MCP servers:
  https://developers.openai.com/api/docs/guides/tools-connectors-mcp
- ChatGPT developer mode and MCP apps:
  https://help.openai.com/en/articles/12584461-developer-mode-and-full-mcp-connectors-in-chatgpt
- MCP Python SDK:
  https://github.com/modelcontextprotocol/python-sdk
