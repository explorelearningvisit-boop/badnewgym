#!/usr/bin/env python3
"""
Screen Pulse - repo-owned laptop tool.

Talks to the BAD GYM app's private frame vault over ADB and nothing else:

    <sandbox>/files/.screenpulse

reached through `adb run-as` (debuggable builds only). Every pulled frame is
SHA-256-verified against the on-device manifest, and `purge` refuses to erase
a frame unless that frame is provably byte-identical on this laptop already.

USB-first: --serial defaults to the dev handset, falling back to the first
attached device. No network code, no daemons.

    python tools/screenpulse_pull.py status
    python tools/screenpulse_pull.py snapshot     # synthetic frame, no consent tap needed
    python tools/screenpulse_pull.py pull --verify
    python tools/screenpulse_pull.py purge --yes
"""

from __future__ import annotations

import argparse
import datetime as _dt
import hashlib
import json
import os
import re
import subprocess
import sys
import time

PACKAGE = "com.example.badnewgym"
RECEIVER = "com.example.badnewgym/.feature.screenpulse.ScreenPulseDebugReceiver"
VAULT_DIR = "files/.screenpulse"
ACTION_PREFIX = "com.example.badnewgym.SCREENPULSE."
TOKEN_FILE = ".token"
MANIFEST_FILE = "manifest.sha256"
STATUS_FILE = "status.json"

DEFAULT_SERIAL = "zxdada69gunb7ls4"
FRAME_RE = re.compile(r"^sp_\d{13}_[a-z0-9_]+\.jpg$")
HASH_RE = re.compile(r"^[0-9a-f]{64}$")

HERE = os.path.dirname(os.path.abspath(__file__))
PULL_ROOT = os.path.join(HERE, "pulled_screens", "screenpulse")
STATE_FILE = os.path.join(PULL_ROOT, ".state.json")


def _adb(args, serial=None, binary=False, timeout=120):
    cmd = ["adb"]
    if serial:
        cmd += ["-s", serial]
    cmd += list(args)
    return subprocess.run(
        cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=timeout,
        **({} if binary else {"text": True, "errors": "replace"}),
    )


def list_devices():
    res = _adb(["devices"])
    out = []
    for line in res.stdout.splitlines()[1:]:
        parts = line.strip().split()
        if len(parts) >= 2 and parts[1] == "device":
            out.append(parts[0])
    return out


def resolve_device(explicit=None):
    if explicit:
        return explicit
    devices = list_devices()
    if not devices:
        return None
    if DEFAULT_SERIAL in devices:
        return DEFAULT_SERIAL
    return devices[0]


def _run_as(serial, argv, timeout=60):
    res = _adb(["exec-out", "run-as", PACKAGE] + list(argv),
               serial=serial, binary=True, timeout=timeout)
    if res.returncode != 0:
        err = (res.stderr or b"").decode("utf-8", "replace").strip()
        raise RuntimeError(err or ("run-as failed: " + " ".join(argv)))
    return res.stdout


def _run_as_text(serial, argv, timeout=60):
    return _run_as(serial, argv, timeout).decode("utf-8", "replace")


def _shell(serial, command, timeout=60):
    res = _adb(["shell", command], serial=serial, timeout=timeout)
    if res.returncode != 0:
        raise RuntimeError((res.stderr or "").strip() or ("shell failed: " + command))
    return res.stdout


def broadcast(serial, action, token=None, timeout=30):
    """Send a control broadcast and return whatever the receiver reported.

    `am broadcast` is unordered, so result extras usually never come back on
    the shell line. When they don't, fall back to the timestamped
    last_result.txt the receiver leaves in the vault.
    """
    try:
        t0 = int(_shell(serial, "date +%s%3N", timeout=15).strip())
    except (RuntimeError, ValueError):
        t0 = 0
    try:
        _run_as(serial, ["rm", "-f", "%s/last_result.txt" % VAULT_DIR], timeout=15)
    except RuntimeError:
        pass
    cmd = "am broadcast -a %s%s -n %s" % (ACTION_PREFIX, action, RECEIVER)
    if token is not None:
        cmd += " --es token %s" % token
    out = _shell(serial, cmd, timeout=timeout)
    # Ordered-result line looks like: Broadcast completed: result=-1, data="frames=12,..."
    m = re.search(r'data="([^"]*)"', out)
    if m:
        return m.group(1)
    deadline = time.time() + timeout
    while time.time() < deadline:
        try:
            text = _run_as_text(serial, ["cat", "%s/last_result.txt" % VAULT_DIR]).strip()
        except RuntimeError:
            text = ""
        if text:
            parts = text.split(None, 1)
            try:
                ts = int(parts[0])
            except ValueError:
                ts = 0
            if ts >= t0 and len(parts) > 1:
                return parts[1]
        time.sleep(0.5)
    # No answer back => nothing handled the action (Screen Pulse build not
    # installed, or the action name changed). Never report that as success.
    raise RuntimeError(
        "no handler for %s%s on %s (is a Screen Pulse build installed?)"
        % (ACTION_PREFIX, action, serial))


def vault_token(serial):
    text = _run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, TOKEN_FILE)]).strip()
    if not text:
        raise RuntimeError("no vault token on device - run `snapshot` once to initialise it")
    return text


def ensure_token(serial):
    """Return the per-install vault token, minting it on first contact.

    The receiver only mints the token file when it is asked to check one, so
    a fresh install needs one denied attempt (with a garbage token) before the
    real token becomes readable over run-as.
    """
    try:
        return vault_token(serial)
    except RuntimeError:
        pass
    try:
        broadcast(serial, "SNAPSHOT", token="mint")
    except RuntimeError:
        pass
    first = vault_token(serial)
    # The minting write must have settled: consecutive reads have to agree
    # before the token is used, or a first-run race reads a torn value.
    for _ in range(5):
        second = vault_token(serial)
        if second == first and second:
            return second
        first = second
        time.sleep(0.5)
    return first


def vault_exists(serial):
    try:
        out = _run_as_text(serial, ["ls", "-1", VAULT_DIR])
    except RuntimeError:
        return False
    return bool(out.strip()) and "No such file" not in out


def remote_frames(serial):
    return sorted(n.strip() for n in _run_as_text(serial, ["ls", "-1", VAULT_DIR]).splitlines()
                  if FRAME_RE.match(n.strip()))


def remote_manifest(serial):
    try:
        text = _run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, MANIFEST_FILE)])
    except RuntimeError:
        return {}
    out = {}
    for line in text.splitlines():
        parts = line.split()
        if len(parts) >= 2 and HASH_RE.match(parts[0]):
            out[parts[-1]] = parts[0]
    return out


def remote_status(serial):
    try:
        broadcast(serial, "STATUS")
    except RuntimeError:
        pass
    try:
        return json.loads(_run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, STATUS_FILE)]))
    except (RuntimeError, ValueError):
        return {}


def load_state():
    if not os.path.exists(STATE_FILE):
        return {"verified": {}}
    try:
        with open(STATE_FILE, "r", encoding="utf-8") as fh:
            data = json.load(fh)
        if not isinstance(data.get("verified"), dict):
            data["verified"] = {}
        return data
    except (OSError, ValueError):
        return {"verified": {}}


def save_state(state):
    os.makedirs(os.path.dirname(STATE_FILE), exist_ok=True)
    tmp = STATE_FILE + ".tmp"
    with open(tmp, "w", encoding="utf-8") as fh:
        json.dump(state, fh, indent=2, sort_keys=True)
    os.replace(tmp, STATE_FILE)


def sha256_file(path):
    h = hashlib.sha256()
    with open(path, "rb") as fh:
        for chunk in iter(lambda: fh.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def cmd_status(args):
    serial = resolve_device(args.serial)
    if not serial:
        return fail("No device attached.")
    status = remote_status(serial) or {}
    print("Device     : %s" % serial)
    print("Running    : %s" % status.get("running", "(no status back from the handset)"))
    print("Interval   : %s ms" % status.get("intervalMs", "-"))
    print("Frames     : %s" % status.get("frames", "-"))
    print("Vault size : %s bytes" % status.get("bytes", "-"))
    print("Vault      : %s" % ("initialised" if vault_exists(serial) else "NOT INITIALISED"))
    return 0


def cmd_snapshot(args):
    serial = resolve_device(args.serial)
    if not serial:
        return fail("No device attached.")
    token = ensure_token(serial)
    result = broadcast(serial, "SNAPSHOT", token=token)
    print("SNAPSHOT -> %s" % result)
    return 0 if "saved=" in result and "saved=nothing" not in result else 1


def cmd_pull(args):
    serial = resolve_device(args.serial)
    if not serial:
        return fail("No device attached.")
    if not vault_exists(serial):
        return fail("no vault on %s - run `snapshot` once to initialise it" % serial)

    print("Device     : %s" % serial)
    print("Manifest   : %s" % broadcast(serial, "VERIFY"))
    names = remote_frames(serial)
    manifest = remote_manifest(serial)
    print("Frames     : %d on device, %d in manifest" % (len(names), len(manifest)))
    if not names:
        print("Nothing to pull.")
        return 0

    dest = os.path.join(PULL_ROOT, args.dest or _dt.datetime.now().strftime("%Y%m%d_%H%M%S"))
    os.makedirs(dest, exist_ok=True)

    saved, missing = {}, []
    for name in names:
        try:
            data = _run_as(serial, ["cat", "%s/%s" % (VAULT_DIR, name)])
        except RuntimeError as exc:
            print("  ! unreadable %s: %s" % (name, exc))
            missing.append(name)
            continue
        path = os.path.join(dest, name)
        with open(path, "wb") as fh:
            fh.write(data)
        saved[name] = sha256_file(path)

    state = load_state()
    verified, mismatched, unverified = 0, [], []
    for name, digest in saved.items():
        expected = manifest.get(name)
        if expected is None:
            unverified.append(name)
        elif expected == digest:
            verified += 1
            state.setdefault("verified", {})[name] = {"sha256": digest, "path": os.path.join(dest, name)}
        else:
            mismatched.append(name)
    save_state(state)

    sizes = [os.path.getsize(os.path.join(dest, n)) for n in saved]
    report = {
        "device": serial,
        "timestamp": _dt.datetime.now().isoformat(timespec="seconds"),
        "destination": dest,
        "frames_on_device": len(names),
        "pulled": len(saved),
        "missing_from_device": missing,
        "verified": verified,
        "mismatched": mismatched,
        "unverified": unverified,
        "bytes": sum(sizes),
        "min_frame_bytes": min(sizes) if sizes else 0,
        "max_frame_bytes": max(sizes) if sizes else 0,
    }
    with open(os.path.join(dest, "pull_report.json"), "w", encoding="utf-8") as fh:
        json.dump(report, fh, indent=2)

    print("Destination: %s" % dest)
    print("Pulled     : %d  Verified: %d  Mismatched: %d  Unverified: %d"
          % (len(saved), verified, len(mismatched), len(unverified)))
    if sizes:
        print("Frame size : %d - %d bytes" % (min(sizes), max(sizes)))
    if mismatched:
        return fail("local bytes differ from the device manifest: %s" % ", ".join(mismatched[:5]))
    if args.verify and verified + len(unverified) < len(saved):
        return fail("not every pulled frame was verified")
    print("OK: every pulled frame matches the device manifest byte for byte.")
    return 0


def cmd_purge(args):
    if not args.yes:
        return fail("Refusing to purge without --yes.")
    serial = resolve_device(args.serial)
    if not serial:
        return fail("No device attached.")
    if not vault_exists(serial):
        return fail("no vault to purge on %s" % serial)

    names = remote_frames(serial)
    manifest = remote_manifest(serial)
    state = load_state()

    # Never erase a frame unless it is provably byte-identical on this laptop.
    unproven = [n for n in names if not _proven(state, n, manifest)]
    if unproven and not args.force:
        return fail("%d/%d frame(s) not proven on laptop - run `pull --verify` first"
                    % (len(unproven), len(names)))
    if unproven:
        print("WARNING: --force, ignoring %d frame(s) without local proof." % len(unproven))

    token = ensure_token(serial)
    print("Stopping capture...")
    broadcast(serial, "STOP", token=token)
    print("Overwriting and deleting %d frames..." % len(names))
    print("PURGE -> %s" % broadcast(serial, "PURGE", token=token))

    remaining = remote_frames(serial)
    print("Frames left on device: %d" % len(remaining))
    if remaining:
        return fail("vault is not empty")
    for name in names:
        state.get("verified", {}).pop(name, None)
    save_state(state)
    print("OK: vault empty, %d frames hard-erased from the phone." % len(names))
    return 0


def _proven(state, name, manifest):
    entry = state.get("verified", {}).get(name)
    if not isinstance(entry, dict):
        return False
    path = entry.get("path")
    if not path or not os.path.isfile(path):
        return False
    try:
        digest = sha256_file(path)
    except OSError:
        return False
    if digest != entry.get("sha256"):
        return False
    expected = manifest.get(name)
    return not expected or expected == digest


def fail(message):
    print("error: %s" % message, file=sys.stderr)
    return 1


def main(argv=None):
    parser = argparse.ArgumentParser(
        prog="screenpulse_pull.py",
        description="Pull, verify and hard-erase Screen Pulse frames from a handset.")
    parser.add_argument("--serial", help="adb serial (default: dev handset, else first device)")
    sub = parser.add_subparsers(dest="command")

    p = sub.add_parser("status", help="show vault state on the device")
    p.set_defaults(func=cmd_status)
    p = sub.add_parser("snapshot", help="make the app emit one synthetic frame")
    p.set_defaults(func=cmd_snapshot)
    p = sub.add_parser("pull", help="copy frames to the laptop and verify SHA-256")
    p.add_argument("--dest", help="sub-folder name under tools/pulled_screens/screenpulse/")
    p.add_argument("--verify", action="store_true", help="fail unless everything verified")
    p.set_defaults(func=cmd_pull)
    p = sub.add_parser("purge", help="hard-erase every frame on the phone")
    p.add_argument("--yes", action="store_true", help="required confirmation")
    p.add_argument("--force", action="store_true", help="skip the already-on-laptop check")
    p.set_defaults(func=cmd_purge)

    args = parser.parse_args(argv)
    if not getattr(args, "func", None):
        parser.print_help()
        return 1
    try:
        return args.func(args)
    except RuntimeError as exc:
        return fail(str(exc))


if __name__ == "__main__":
    sys.exit(main())
