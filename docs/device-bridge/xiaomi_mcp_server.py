"""
BAD GYM — Xiaomi 11i ChatGPT Device Bridge.

High-level, allow-listed Android operations for the laptop that can reach the
Xiaomi over Tailscale ADB. Designed for OpenAI Secure MCP Tunnel.

Environment:
  BADGYM_XIAOMI_SERIAL=100.123.18.54:5555
  BADGYM_PACKAGE=com.example.badnewgym
  ADB_PATH=adb
  BADGYM_ARTIFACT_DIR=./artifacts

No raw ADB shell is exposed as an MCP tool.
"""
from __future__ import annotations

import os
import re
import subprocess
import time
from pathlib import Path
from typing import Any

from mcp.server.fastmcp import FastMCP, Image

mcp = FastMCP("BAD GYM Xiaomi Device Bridge")

SERIAL = os.environ.get("BADGYM_XIAOMI_SERIAL", "100.123.18.54:5555")
PACKAGE = os.environ.get("BADGYM_PACKAGE", "com.example.badnewgym")
ADB = os.environ.get("ADB_PATH", "adb")
ARTIFACT_DIR = Path(os.environ.get("BADGYM_ARTIFACT_DIR", str(Path.cwd() / "artifacts"))).resolve()
ARTIFACT_DIR.mkdir(parents=True, exist_ok=True)

_KEYEVENTS = {
    "BACK": "4", "HOME": "3", "RECENTS": "187", "ENTER": "66", "ESC": "111",
    "DPAD_UP": "19", "DPAD_DOWN": "20", "DPAD_LEFT": "21",
    "DPAD_RIGHT": "22", "DPAD_CENTER": "23",
}

def _run(args: list[str], *, timeout: float = 30.0, binary: bool = False):
    proc = subprocess.run(
        [ADB, "-s", SERIAL, *args],
        capture_output=True, timeout=timeout, check=False,
    )
    if binary:
        return proc.returncode, proc.stdout, proc.stderr
    return (
        proc.returncode,
        proc.stdout.decode("utf-8", "replace"),
        proc.stderr.decode("utf-8", "replace"),
    )

def _ensure_connected() -> None:
    if ":" in SERIAL:
        proc = subprocess.run(
            [ADB, "connect", SERIAL],
            capture_output=True, timeout=10, check=False, text=True,
        )
        if proc.returncode != 0 and "already connected" not in proc.stdout.lower():
            raise RuntimeError(f"ADB connect failed: {proc.stderr.strip() or proc.stdout.strip()}")
    code, out, err = _run(["get-state"], timeout=10)
    if code != 0 or str(out).strip() != "device":
        raise RuntimeError(f"Xiaomi is not ready: {str(err).strip() or str(out).strip()}")

def _shell(command: list[str], *, timeout: float = 30.0, binary: bool = False):
    _ensure_connected()
    return _run(["shell", *command], timeout=timeout, binary=binary)

def _safe_name(value: str) -> str:
    return re.sub(r"[^A-Za-z0-9._-]+", "_", value)[:100] or "artifact"

def _screenshot_bytes() -> bytes:
    _ensure_connected()
    code, out, err = _run(["exec-out", "screencap", "-p"], timeout=20, binary=True)
    if code != 0 or not out.startswith(b"\x89PNG"):
        raise RuntimeError(f"Screenshot failed: {err.decode('utf-8', 'replace')}")
    return out

def _ui_xml() -> str:
    code, _, err = _shell(["uiautomator", "dump", "/sdcard/window.xml"], timeout=20)
    if code != 0:
        raise RuntimeError(f"UI dump failed: {str(err)}")
    code, out, err = _shell(["cat", "/sdcard/window.xml"], timeout=20)
    if code != 0:
        raise RuntimeError(f"UI read failed: {str(err)}")
    return str(out)

@mcp.tool()
def device_status() -> str:
    """Return Xiaomi connection, Android version, display size and BAD GYM process state."""
    _ensure_connected()
    _, model, _ = _shell(["getprop", "ro.product.model"])
    _, version, _ = _shell(["getprop", "ro.build.version.release"])
    _, size, _ = _shell(["wm", "size"])
    _, pid, _ = _shell(["pidof", PACKAGE])
    return (
        f"device=online\nserial={SERIAL}\nmodel={str(model).strip()}\n"
        f"android={str(version).strip()}\ndisplay={str(size).strip()}\n"
        f"package={PACKAGE}\nprocess={'running' if str(pid).strip() else 'stopped'}"
    )

@mcp.tool()
def screen_size() -> str:
    """Return the Android display size."""
    _, size, err = _shell(["wm", "size"])
    return str(size).strip() or str(err).strip()

@mcp.tool()
def install_apk(apk_path: str) -> str:
    """Install/replace BAD GYM from a local APK path."""
    path = Path(apk_path).expanduser().resolve()
    if path.suffix.lower() != ".apk" or not path.is_file():
        raise ValueError("apk_path must be an existing .apk file")
    _ensure_connected()
    proc = subprocess.run(
        [ADB, "-s", SERIAL, "install", "-r", str(path)],
        capture_output=True, timeout=120, check=False, text=True,
    )
    if proc.returncode != 0 or "success" not in proc.stdout.lower():
        raise RuntimeError(proc.stderr.strip() or proc.stdout.strip() or "APK install failed")
    return f"installed={path}\npackage={PACKAGE}"

@mcp.tool()
def launch_app() -> str:
    """Launch BAD GYM."""
    code, out, err = _shell(["monkey", "-p", PACKAGE, "1"], timeout=20)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"launched={PACKAGE}"

@mcp.tool()
def stop_app() -> str:
    """Stop BAD GYM without clearing its data."""
    code, out, err = _shell(["am", "force-stop", PACKAGE], timeout=15)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"stopped={PACKAGE}"

@mcp.tool()
def clear_app_data(confirm: bool = False) -> str:
    """Clear BAD GYM data; destructive and requires confirm=true."""
    if not confirm:
        return "Not executed. Re-call with confirm=true if this is intentional."
    code, out, err = _shell(["pm", "clear", PACKAGE], timeout=30)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"cleared_data={PACKAGE}"

@mcp.tool()
def screenshot(label: str = "screen") -> Image:
    """Capture the live Xiaomi screen and return the image to ChatGPT."""
    data = _screenshot_bytes()
    path = ARTIFACT_DIR / f"{time.strftime('%Y%m%d-%H%M%S')}_{_safe_name(label)}.png"
    path.write_bytes(data)
    return Image(data=data, format="png")

@mcp.tool()
def save_screenshot(label: str = "screen") -> str:
    """Capture and save a timestamped PNG in the bridge artifact folder."""
    data = _screenshot_bytes()
    path = ARTIFACT_DIR / f"{time.strftime('%Y%m%d-%H%M%S')}_{_safe_name(label)}.png"
    path.write_bytes(data)
    return str(path)

@mcp.tool()
def dump_ui() -> str:
    """Return Android UIAutomator XML for the current screen."""
    return _ui_xml()

@mcp.tool()
def tap(x: int, y: int) -> str:
    """Tap an exact screen coordinate."""
    if x < 0 or y < 0:
        raise ValueError("x and y must be non-negative")
    code, out, err = _shell(["input", "tap", str(x), str(y)], timeout=10)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"tapped=({x},{y})"

@mcp.tool()
def swipe(x1: int, y1: int, x2: int, y2: int, duration_ms: int = 350) -> str:
    """Swipe between two screen coordinates."""
    if min(x1, y1, x2, y2) < 0:
        raise ValueError("coordinates must be non-negative")
    duration_ms = max(50, min(duration_ms, 5000))
    code, out, err = _shell(
        ["input", "swipe", str(x1), str(y1), str(x2), str(y2), str(duration_ms)],
        timeout=15,
    )
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"swiped=({x1},{y1})->({x2},{y2}) duration={duration_ms}ms"

@mcp.tool()
def keyevent(key: str) -> str:
    """Press an allow-listed Android key event."""
    normalized = key.strip().upper()
    code_value = _KEYEVENTS.get(normalized)
    if not code_value:
        raise ValueError(f"Unsupported key: {normalized}. Allowed: {', '.join(sorted(_KEYEVENTS))}")
    code, out, err = _shell(["input", "keyevent", code_value], timeout=10)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"keyevent={normalized}"

@mcp.tool()
def back() -> str:
    """Press Android Back."""
    return keyevent("BACK")

@mcp.tool()
def home() -> str:
    """Press Android Home."""
    return keyevent("HOME")

@mcp.tool()
def input_text(text: str) -> str:
    """Type ordinary non-sensitive test text into the focused Android field."""
    if not text:
        return "No text entered."
    encoded = text.replace("%", "%25").replace(" ", "%s")
    code, out, err = _shell(["input", "text", encoded], timeout=15)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    return f"input_length={len(text)}"

@mcp.tool()
def wait_for_element(text: str = "", resource_id: str = "", timeout_seconds: int = 15) -> str:
    """Wait until UIAutomator XML contains the requested text or resource-id."""
    if not text and not resource_id:
        raise ValueError("Provide text or resource_id")
    deadline = time.monotonic() + max(1, min(timeout_seconds, 60))
    needle_text = text.lower()
    needle_id = resource_id.lower()
    while time.monotonic() < deadline:
        xml = _ui_xml()
        if needle_text and needle_text in xml.lower():
            return f"found_text={text}"
        if needle_id and needle_id in xml.lower():
            return f"found_resource_id={resource_id}"
        time.sleep(0.5)
    raise TimeoutError(
        f"Element not found within {timeout_seconds}s: text={text!r}, resource_id={resource_id!r}"
    )

@mcp.tool()
def logcat_tail(lines: int = 250, package_filter: bool = True) -> str:
    """Return recent logcat lines, optionally filtered to BAD GYM/crash evidence."""
    lines = max(1, min(lines, 2000))
    _ensure_connected()
    code, out, err = _run(["logcat", "-d", "-t", str(lines)], timeout=30)
    if code != 0:
        raise RuntimeError(str(err).strip() or str(out).strip())
    text = str(out)
    if package_filter:
        rows = [
            row for row in text.splitlines()
            if PACKAGE.lower() in row.lower()
            or "androidruntime" in row.lower()
            or "fatal exception" in row.lower()
        ]
        text = "\n".join(rows)
    return text[-30000:] if text else "No matching logcat lines."

@mcp.tool()
def run_smoke_flow(steps: list[dict[str, Any]], settle_ms: int = 500) -> str:
    """
    Execute a bounded allow-listed UI smoke flow.
    Actions: launch, screenshot, tap, swipe, back, home, wait.
    Maximum 40 steps. No arbitrary shell commands.
    """
    if len(steps) > 40:
        raise ValueError("A smoke flow is limited to 40 steps.")
    results: list[str] = []
    for index, step in enumerate(steps, start=1):
        action = str(step.get("action", "")).lower()
        if action == "launch":
            result = launch_app()
        elif action == "screenshot":
            result = save_screenshot(str(step.get("label", f"step-{index}")))
        elif action == "tap":
            result = tap(int(step["x"]), int(step["y"]))
        elif action == "swipe":
            result = swipe(
                int(step["x1"]), int(step["y1"]), int(step["x2"]), int(step["y2"]),
                int(step.get("duration_ms", 350)),
            )
        elif action == "back":
            result = back()
        elif action == "home":
            result = home()
        elif action == "wait":
            result = wait_for_element(
                str(step.get("text", "")),
                str(step.get("resource_id", "")),
                int(step.get("timeout_seconds", 15)),
            )
        else:
            raise ValueError(f"Unsupported smoke-flow action at step {index}: {action!r}")
        results.append(f"{index}: {result}")
        if settle_ms:
            time.sleep(max(0, min(settle_ms, 3000)) / 1000)
    return "\n".join(results)

if __name__ == "__main__":
    mcp.run()
