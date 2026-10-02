# BAD GYM — Phone-Native ChatGPT Device Agent

This branch replaces the laptop/ADB runtime with a **phone-native control agent**.

## Runtime target

```
ChatGPT
   │
   │ Streamable HTTP MCP
   ▼
Xiaomi 11i
   ├─ BAD GYM app
   ├─ RemoteDeviceMcpServer :8787
   ├─ AccessibilityService
   └─ foreground Device Agent
```

There is no laptop, desktop or console in the runtime path.

## Implemented in the Android app

The phone-native agent exposes MCP tools for:

- `device_status`
- `screenshot`
- `ui_tree`
- `tap`
- `swipe`
- `back`
- `home`
- `set_text`
- `launch_badgym`
- `open_accessibility_settings`

The server is stateless Streamable-HTTP-compatible JSON MCP at:

```
http://<xiaomi-address>:8787/mcp
```

A bearer token is generated once and stored in Android private preferences.

## First-time phone setup

1. Install/run BAD GYM on the Xiaomi 11i.
2. BAD GYM starts the foreground Device Agent automatically.
3. Enable **BAD GYM Device Control** in Android Accessibility settings.
4. Keep the phone reachable through the chosen private network/tunnel.
5. Connect ChatGPT to the phone's `/mcp` endpoint using the generated bearer token.

The accessibility service is required for cross-screen UI inspection and gesture dispatch. Android requires explicit user enablement for this capability.

## Security boundaries

- No arbitrary shell/ADB command is exposed.
- Coordinates, gestures and text are high-level allow-listed MCP tools.
- App-data deletion is not exposed by the phone agent.
- Authentication is bearer-token based.
- The MCP server is intended to be reachable only through a private network or authenticated HTTPS tunnel.
- Do not expose port 8787 directly to the public internet without TLS and an authenticated access boundary.

## Screenshot scope

`screenshot` captures the visible BAD GYM application window. This is deliberately narrower than unrestricted system-screen capture and avoids requesting MediaProjection permission just to test BAD GYM.

For full-device screenshots outside the BAD GYM window, a later MediaProjection module can be added after explicit user authorization.

## ChatGPT connection

OpenAI supports remote MCP over Streamable HTTP and also supports private MCP servers through Secure MCP Tunnel. The phone agent is designed around the same `/mcp` transport so the network boundary can be swapped without changing the device-control API.

Official references:

- OpenAI MCP servers: https://developers.openai.com/plugins/build/mcp-server
- OpenAI MCP connections: https://developers.openai.com/api/docs/guides/agents-api/tools/mcp
- OpenAI MCP quickstart: https://developers.openai.com/plugins/build/app-quickstart
- Android AccessibilityService: https://developer.android.com/reference/android/accessibilityservice/AccessibilityService
