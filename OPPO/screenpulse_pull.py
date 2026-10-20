#!/usr/bin/env python3
"""
Screen Pulse - laptop side.

Talks to the BAD GYM Android app's private frame vault over ADB and nothing
else. The vault lives at

    /data/data/com.example.badnewgym/files/.screenpulse

inside the app sandbox, which no other installed app can list, read or delete.
ADB reaches it through `run-as`, which only works on a *debuggable* build of
the app - exactly the build that ships to this handset during development.

Commands
--------
    connect     find the handset over Tailscale/USB and attach adb
    status      show whether capture is running and how big the vault is
    pull        copy frames to OPPO/pulled_screens/screenpulse/<stamp>/ and
                verify every SHA-256 against the on-device manifest
    snapshot    make the app synthesise one frame (self test, no screen
                consent needed - consent needs a human tap, this does not)
    purge       hard-erase every frame on the phone (needs --yes)
    watch       pull on an interval, optionally purging after each verified pull
    consent     open the on-device consent screen (the one manual tap)

Typical end-to-end run:

    python OPPO/screenpulse_pull.py pull --verify
    python OPPO/screenpulse_pull.py purge --yes

Frames are never deleted from the phone unless they are already present and
byte-identical on the laptop. `purge` re-checks that condition itself, so it is
safe to run even if you are not sure the last pull succeeded.
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
import tarfile
import time

PACKAGE = "com.example.badnewgym"
VAULT_DIR = "files/.screenpulse"
ACTION = "com.example.badnewgym.SCREENPULSE."
TOKEN_FILE = ".token"
MANIFEST_FILE = "manifest.sha256"
STATUS_FILE = "status.json"
NOMEDIA_FILE = ".nomedia"

WHATSAPP_TAGS = ("wa", "wab")

# Devices this workstation has historically paired with, ordered by preference:
# Tailscale endpoints first (they survive the cable coming out), then local LAN.
# A `connect` falls back to any online tailnet peer if none of these answer.
PRESET_DEVICES = [
    "100.84.228.109:5555",          # Xiaomi 11i [BAD GYM Embedded Tailscale]
    "100.80.18.55:5555",           # OPPO A53  [Tailscale]
    "100.123.18.54:5555",          # Xiaomi 11i [Tailscale Legacy]
    "192.168.1.37:5555",           # Xiaomi 11i [LAN]
    "zxdada69gunb7ls4",            # Xiaomi USB
]

HERE = os.path.dirname(os.path.abspath(__file__))
PULL_ROOT = os.path.join(HERE, "pulled_screens", "screenpulse")
STATE_FILE = os.path.join(PULL_ROOT, ".state.json")

ADB_PORT = 5555
TAILSCALE_BIN = r"C:\Program Files\Tailscale\tailscale.exe"


# --------------------------------------------------------------------------
# adb plumbing
# --------------------------------------------------------------------------

def _adb(args, serial=None, binary=False, timeout=120):
    """Run adb, optionally pinned to a serial. Returns CompletedProcess."""
    cmd = ["adb"]
    if serial:
        cmd += ["-s", serial]
    cmd += list(args)
    return subprocess.run(
        cmd,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        timeout=timeout,
        **({} if binary else {"text": True, "errors": "replace"}),
    )


def list_devices():
    res = _adb(["devices"])
    out = []
    for line in res.stdout.splitlines()[1:]:
        line = line.strip()
        if not line or line.startswith("*"):
            continue
        parts = line.split()
        if len(parts) >= 2 and parts[1] == "device":
            out.append(parts[0])
    return out


def tailscale_peers():
    """[(hostname, dns_name, ips, online)] for every peer in the tailnet."""
    if not os.path.exists(TAILSCALE_BIN):
        return []
    try:
        res = subprocess.run(
            [TAILSCALE_BIN, "status", "--json"],
            stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True,
            errors="replace", timeout=20,
        )
        data = json.loads(res.stdout)
    except (OSError, ValueError, subprocess.SubprocessError):
        return []
    peers = []
    for value in (data.get("Peer") or {}).values():
        peers.append((
            value.get("HostName") or "",
            value.get("DNSName") or "",
            value.get("TailscaleIPs") or [],
            bool(value.get("Online")),
        ))
    return peers


def _candidate_endpoints():
    """Endpoints worth trying, best first: already-known presets, then every
    online tailnet peer, then MagicDNS names (which keep working if DHCP moves
    the phone)."""
    out = []
    for preset in PRESET_DEVICES:
        if ":" not in preset:          # USB serial, not a dialable endpoint
            continue
        if preset not in out:
            out.append(preset)
    for host, dns, ips, online in tailscale_peers():
        if not online:
            continue
        for ip in ips:
            if ":" in ip:          # skip IPv6; adb wants the v4 form
                continue
            endpoint = "%s:%d" % (ip, ADB_PORT)
            if endpoint not in out:
                out.append(endpoint)
        short = dns.split(".")[0]
        if short:
            endpoint = "%s:%d" % (short, ADB_PORT)
            if endpoint not in out:
                out.append(endpoint)
    return out


def _adb_connect(endpoint, timeout=8):
    try:
        res = _adb(["connect", endpoint], timeout=timeout)
    except subprocess.SubprocessError:
        return False
    out = ((res.stdout or "") + (res.stderr or "")).lower()
    return "connected to" in out and "cannot" not in out and "failed" not in out


def _tailscale_ip_for(serial):
    """Which tailnet address belongs to a handset that is currently on a cable?

    The phone reports its own addresses; we intersect them with the tailnet peer
    list rather than guessing, so we never pin adbd to a port we cannot then
    reach (which would leave the phone connected to nothing at all).
    """
    peer_ips = {
        ip
        for _, _, ips, online in tailscale_peers()
        if online
        for ip in ips
        if ":" not in ip
    }
    if not peer_ips:
        return None
    try:
        res = _adb(["-s", serial, "shell", "ip -o addr show"], timeout=15)
    except (subprocess.SubprocessError, OSError):
        return None
    for ip in re.findall(r"inet\s+(100\.\d+\.\d+\.\d+)/", res.stdout or ""):
        if ip in peer_ips:
            return ip
    return None


def _pin_cable_devices():
    """`adb tcpip 5555` on anything plugged in, so it stays reachable over
    Tailscale after the cable comes out. Only done when we can confirm the
    handset's tailnet IP first - pinning blind would drop the only link we have.
    """
    for serial in [d for d in list_devices() if ":" not in d]:
        ip = _tailscale_ip_for(serial)
        if not ip:
            print("[screenpulse] %s: no tailnet address found, leaving USB alone" % serial)
            continue
        endpoint = "%s:%d" % (ip, ADB_PORT)
        if endpoint in list_devices() or _probe_port(endpoint):
            print("[screenpulse] %s already serving %s" % (serial, endpoint))
            continue
        print("[screenpulse] cable device %s -> tcp %d (so %s survives unplug)"
              % (serial, ADB_PORT, endpoint))
        try:
            _adb(["-s", serial, "tcpip", str(ADB_PORT)], timeout=20)
        except (subprocess.SubprocessError, OSError) as exc:
            print("[screenpulse]   could not switch: %s" % exc)
            continue
        time.sleep(3)
        _adb_connect(endpoint, timeout=8)


def ensure_connected(timeout=8):
    """Make sure at least one device is attached, dialling out over Tailscale if
    needed. Returns the serial we are talking to, or None.

    Order of preference:
      1. an already-attached wireless endpoint (always :5555 over Tailscale)
      2. pin anything on a cable to :5555 and redial - this is what keeps a
         cable-connected phone reachable over Tailscale after the cable comes out
      3. dial every online tailnet peer at :5555
      4. the USB serial itself (still perfectly usable for run-as pulls)
    """
    found = list_devices()
    _pin_cable_devices()          # keep cable phones reachable after the unplug

    picked = _pick_wireless(list_devices())
    if picked:
        return picked

    # Nothing wireless yet - dial every online tailnet peer at :5555.
    for endpoint in _candidate_endpoints():
        if _adb_connect(endpoint, timeout=timeout):
            picked = _pick_wireless(list_devices())
            if picked:
                return picked

    usb = [d for d in list_devices() if ":" not in d]
    if usb:
        return usb[0]

    return None


def _pick_wireless(devices):
    wireless = [d for d in devices if ":" in d]
    if not wireless:
        return None
    for preset in PRESET_DEVICES:
        if preset in wireless:
            return preset
    return wireless[0]


def resolve_device(explicit=None):
    if explicit and ":" in explicit:
        _adb_connect(explicit)
        return explicit
    return ensure_connected()


# --------------------------------------------------------------------------
# `connect` diagnostic
# --------------------------------------------------------------------------

def cmd_connect(args):
    attached = list_devices()
    print("Attached now : %s" % (", ".join(attached) if attached else "(none)"))

    peers = tailscale_peers()
    if peers:
        print("")
        print("Tailscale peers:")
        for host, dns, ips, online in peers:
            v4 = ", ".join(i for i in ips if ":" not in i) or "-"
            print("  %-16s %-22s %s" % (host, v4, "online" if online else "offline"))
    else:
        print("")
        print("Tailscale    : CLI not found at %s" % TAILSCALE_BIN)

    print("")
    print("Probing adb endpoints...")
    for endpoint in _candidate_endpoints():
        reachable = _probe_port(endpoint)
        state = "OPEN " if reachable else "closed"
        connected = _adb_connect(endpoint, timeout=4)
        print("  %-42s %-6s %s" % (endpoint, state, "connected" if connected else ""))

    serial = ensure_connected(timeout=6)
    print("")
    if serial:
        print("READY: using %s" % serial)
        if ":" in serial:
            print("Wireless path is up; cable can come out.")
        else:
            print("Cable path only - adbd was switched to tcp %d above." % ADB_PORT)
        return 1 if ":" not in serial else 0

    print("NOT READY: no adb endpoint is accepting connections.")
    print("")
    print("The phone is alive on Tailscale but adbd is in USB-only mode.")
    print("Do ONE of these on the handset, then re-run `connect`:")
    print("  1. Plug USB once  ->  adb tcpip 5555  ->  unplug (permanent fix)")
    print("  2. Settings > Additional settings > Developer options >")
    print("     Wireless debugging > ON  (then pair this laptop if asked)")
    return 1


def _probe_port(endpoint, timeout=1.5):
    try:
        host, port = endpoint.rsplit(":", 1)
        port = int(port)
    except ValueError:
        return False
    import socket
    sock = socket.socket()
    sock.settimeout(timeout)
    try:
        return sock.connect_ex((host, port)) == 0
    except OSError:
        return False
    finally:
        sock.close()


def _run_as(serial, argv, timeout=60):
    """`adb exec-out run-as <pkg> ...` -> raw bytes (binary safe, no CR mangling)."""
    res = _adb(["exec-out", "run-as", PACKAGE] + list(argv), serial=serial, binary=True, timeout=timeout)
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
    """Send a control broadcast and return whatever the receiver reported."""
    cmd = "am broadcast -a %s%s -p %s" % (ACTION, action, PACKAGE)
    if token is not None:
        cmd += " --es token %s" % token
    out = _shell(serial, cmd, timeout=timeout)
    # Result line looks like:  Broadcast completed: result=-1, data="frames=12,..."
    m = re.search(r'data="([^"]*)"', out)
    if m:
        return m.group(1)
    # No data back => nothing handled the action (Screen Pulse build not
    # installed, or the action name changed). Never report that as success.
    raise RuntimeError(
        "no handler for %s%s on %s (is a Screen Pulse build installed?)"
        % (ACTION, action, serial)
    )


def vault_token(serial):
    text = _run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, TOKEN_FILE)]).strip()
    if not text:
        # adb exec-out swallows the remote exit code, so an empty body is the
        # only reliable signal that the file (or the vault) does not exist.
        raise RuntimeError(
            "no Screen Pulse vault on this handset yet - run `consent` then "
            "`snapshot` once to initialise it"
        )
    return text


def vault_exists(serial):
    """True once the app has initialised its frame directory."""
    try:
        out = _run_as_text(serial, ["ls", "-1", VAULT_DIR])
    except RuntimeError:
        return False
    return bool(out.strip()) and "No such file" not in out


# --------------------------------------------------------------------------
# vault readers
# --------------------------------------------------------------------------

def remote_frames(serial):
    """Frame names on the device, oldest first (names sort chronologically)."""
    listing = _run_as_text(serial, ["ls", "-1", VAULT_DIR])
    names = []
    for line in listing.splitlines():
        name = line.strip()
        if re.match(r"^sp_\d{13}_[a-z0-9_]+\.jpg$", name):
            names.append(name)
    return sorted(names)


def remote_manifest(serial):
    """{name: sha256} from the on-device manifest file (may be stale)."""
    try:
        text = _run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, MANIFEST_FILE)])
    except RuntimeError:
        return {}
    out = {}
    for line in text.splitlines():
        parts = line.split()
        if len(parts) >= 2 and re.match(r"^[0-9a-f]{64}$", parts[0]):
            out[parts[-1]] = parts[0]
    return out


def remote_status(serial):
    """Refresh + read the app's status blob."""
    try:
        broadcast(serial, "STATUS")
    except RuntimeError:
        pass
    try:
        raw = _run_as_text(serial, ["cat", "%s/%s" % (VAULT_DIR, STATUS_FILE)])
        return json.loads(raw)
    except (RuntimeError, ValueError):
        return {}


def refresh_manifest(serial):
    """Ask the app to rewrite its manifest over exactly the frames it can see."""
    return broadcast(serial, "VERIFY")


# --------------------------------------------------------------------------
# local state (what the laptop already has, and what was verified)
# --------------------------------------------------------------------------

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


def _proof_on_laptop(state, name, manifest):
    """Is this frame demonstrably sitting on the laptop, byte-identical?

    Returns (ok, reason). Deliberately conservative: it re-hashes the local file
    rather than trusting an earlier note, so a frame whose laptop copy was since
    edited or deleted blocks the purge.
    """
    entry = state.get("verified", {}).get(name)
    if not isinstance(entry, dict):
        return False, "never pulled"
    path = entry.get("path")
    if not path or not os.path.isfile(path):
        return False, "missing on laptop"
    try:
        digest = sha256_file(path)
    except OSError:
        return False, "unreadable on laptop"
    if digest != entry.get("sha256"):
        return False, "laptop copy changed"
    expected = manifest.get(name)
    if expected and expected != digest:
        return False, "manifest mismatch"
    return True, "ok"


def _record_verified(state, name, path, digest):
    state.setdefault("verified", {})[name] = {
        "sha256": digest,
        "path": path,
        "bytes": os.path.getsize(path),
    }


# --------------------------------------------------------------------------
# pull
# --------------------------------------------------------------------------

def _read_tar(serial):
    """One round trip for the whole vault. Falls back to per-file reads."""
    try:
        blob = _run_as(serial, ["sh", "-c", "cd files && tar -c -f - .screenpulse"])
        if not blob:
            raise RuntimeError("empty tar")
        tar = tarfile.open(fileobj=__import__("io").BytesIO(blob), mode="r:")
        out = {}
        for member in tar.getmembers():
            if not member.isfile():
                continue
            name = os.path.basename(member.name)
            if not re.match(r"^sp_\d{13}_[a-z0-9_]+\.jpg$", name):
                continue
            handle = tar.extractfile(member)
            if handle is not None:
                out[name] = handle.read()
        if out:
            return out
        raise RuntimeError("tar contained no frames")
    except Exception:
        return None


def _read_each(serial, names):
    out = {}
    for name in names:
        try:
            out[name] = _run_as(serial, ["cat", "%s/%s" % (VAULT_DIR, name)])
        except RuntimeError as exc:
            print("  ! could not read %s: %s" % (name, exc))
    return out


def cmd_pull(args):
    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached. Turn the phone on, enable USB debugging, then retry.")

    dest = os.path.join(
        PULL_ROOT,
        args.dest or _dt.datetime.now().strftime("%Y%m%d_%H%M%S"),
    )

    print("Device   : %s" % serial)
    print("Vault    : %s" % VAULT_DIR)

    if not vault_exists(serial):
        fail(
            "no vault at %s on %s - install a Screen Pulse build, then run "
            "`consent` once and `snapshot` to initialise it" % (VAULT_DIR, serial)
        )

    refresh = refresh_manifest(serial)
    names = remote_frames(serial)
    manifest = remote_manifest(serial)
    print("Manifest : %s" % (refresh or "unchanged"))
    print("Frames on device: %d (manifest covers %d)" % (len(names), len(manifest)))

    if args.wa_only:
        names = [n for n in names if n.endswith(("_wa.jpg", "_wab.jpg"))]
        print("WhatsApp tagged: %d" % len(names))

    if args.new_only:
        state = load_state()
        known = set(state.get("verified", {}))
        fresh = [n for n in names if n not in known]
        if fresh:
            names = fresh
            print("New (not yet verified locally): %d" % len(names))
        else:
            print("New (not yet verified locally): 0 - nothing to do")

    if not names:
        print("Nothing to pull.")
        return 0

    print("Reading frames from device...")
    payload = _read_tar(serial)
    if payload is None:
        payload = _read_each(serial, names)
    missing = [n for n in names if n not in payload]
    payload = {n: b for n, b in payload.items()}
    pulled = len(payload)
    print("Transferred: %d frames (%.1f KB)"
          % (pulled, sum(len(b) for b in payload.values()) / 1024.0))

    os.makedirs(dest, exist_ok=True)
    saved = {}
    for name, data in sorted(payload.items()):
        path = os.path.join(dest, name)
        with open(path, "wb") as fh:
            fh.write(data)
        saved[name] = sha256_file(path)

    # Verify against the device manifest.
    verified = 0
    mismatched = []
    unverified = []
    state = load_state()
    for name, digest in saved.items():
        path = os.path.join(dest, name)
        expected = manifest.get(name)
        if expected is None:
            # Frame written after the manifest snapshot; the app will cover it
            # next time. Keep the local digest so purge can still account for it.
            prev = state["verified"].get(name)
            prev_digest = prev.get("sha256") if isinstance(prev, dict) else prev
            if prev_digest == digest:
                verified += 1
                _record_verified(state, name, path, digest)
            else:
                unverified.append(name)
            continue
        if expected == digest:
            verified += 1
            _record_verified(state, name, path, digest)
        else:
            mismatched.append(name)

    gone = [n for n in manifest if n not in saved and (not args.wa_only)]
    if args.wa_only:
        gone = []

    save_state(state)

    sizes = [os.path.getsize(os.path.join(dest, n)) for n in saved]
    report = {
        "device": serial,
        "timestamp": _dt.datetime.now().isoformat(timespec="seconds"),
        "destination": dest,
        "frames_on_device": len(names),
        "pulled": pulled,
        "missing_from_device": len(missing),
        "verified": verified,
        "mismatched": mismatched,
        "unverified": unverified,
        "in_manifest_not_pulled": gone,
        "bytes": sum(sizes),
        "min_frame_bytes": min(sizes) if sizes else 0,
        "max_frame_bytes": max(sizes) if sizes else 0,
    }
    with open(os.path.join(dest, "pull_report.json"), "w", encoding="utf-8") as fh:
        json.dump(report, fh, indent=2)

    print("")
    print("Destination : %s" % dest)
    print("Pulled      : %d" % pulled)
    print("Verified    : %d" % verified)
    if sizes:
        print("Frame size  : %d - %d bytes" % (min(sizes), max(sizes)))
    if missing:
        print("Unreadable  : %d (%s)" % (len(missing), ", ".join(missing[:5])))
    if mismatched:
        print("SHA MISMATCH: %d -> %s" % (len(mismatched), ", ".join(mismatched[:5])))
    if unverified:
        print("Not yet in manifest: %d (verify again on the next pull)" % len(unverified))
    if gone:
        print("Pruned on device before pull: %d" % len(gone))
    print("Report      : %s" % os.path.join(dest, "pull_report.json"))

    if mismatched:
        print("")
        print("FAILED: local bytes differ from the device manifest.")
        return 2
    if args.verify and verified < len(saved) - len(unverified):
        print("")
        print("FAILED: not every pulled frame was verified.")
        return 2
    print("")
    print("OK: every pulled frame matches the device manifest byte for byte.")
    return 0


# --------------------------------------------------------------------------
# status / snapshot / purge / watch / consent
# --------------------------------------------------------------------------

def cmd_status(args):
    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached.")
    status = remote_status(serial) or {}
    print("Device     : %s" % serial)
    if status:
        print("Running    : %s" % status.get("running", "unknown"))
    else:
        print("Running    : (no status back from the handset)")
    print("Interval   : %s ms" % status.get("intervalMs", "-"))
    print("Frames     : %s" % status.get("frames", "-"))
    print("Vault size : %s bytes" % status.get("bytes", "-"))
    print("Oldest     : %s" % _fmt_ts(status.get("oldest", 0)))
    print("Newest     : %s" % _fmt_ts(status.get("newest", 0)))
    if not vault_exists(serial):
        print("")
        print("Vault      : NOT INITIALISED")
        print("           Install a Screen Pulse build, run `consent` once, then")
        print("           `snapshot` to create it.")
        return 1
    try:
        vault_token(serial)
        token_note = "present"
    except RuntimeError as exc:
        token_note = "unreadable (%s)" % exc
    print("Token      : %s" % token_note)
    return 0


def _fmt_ts(ms):
    if not ms:
        return "-"
    return _dt.datetime.fromtimestamp(ms / 1000.0).strftime("%Y-%m-%d %H:%M:%S")


def cmd_snapshot(args):
    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached.")
    token = vault_token(serial)
    result = broadcast(serial, "SNAPSHOT", token=token)
    print("SNAPSHOT -> %s" % result)
    if "saved=" not in result or "saved=nothing" in result:
        return 1
    return cmd_pull(_pull_args(args))


def _pull_args(args):
    ns = argparse.Namespace()
    for key in ("serial", "dest", "wa_only", "new_only", "verify"):
        ns.__dict__[key] = getattr(args, key, None)
    ns.dest = None
    ns.new_only = False
    ns.verify = True
    return ns


def cmd_purge(args):
    if not args.yes:
        fail("Refusing to purge without --yes (this overwrites every frame on the phone).")

    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached.")

    if not vault_exists(serial):
        fail("no vault to purge on %s" % serial)

    names = remote_frames(serial)
    manifest = remote_manifest(serial)
    state = load_state()

    # Safety: never erase a frame unless it is provably on the laptop right now.
    unproven = []
    for name in names:
        ok, why = _proof_on_laptop(state, name, manifest)
        if not ok:
            unproven.append((name, why))
    if unproven and not args.force:
        reasons = {}
        for _, why in unproven:
            reasons[why] = reasons.get(why, 0) + 1
        detail = ", ".join("%d %s" % (c, w) for w, c in sorted(reasons.items()))
        fail(
            "%d/%d frame(s) on the phone are not proven to be on the laptop (%s). "
            "Run `pull --verify` first, or pass --force to override."
            % (len(unproven), len(names), detail)
        )
    if unproven:
        print("WARNING: --force, ignoring %d frame(s) without local proof." % len(unproven))

    token = vault_token(serial)
    print("Stopping capture...")
    broadcast(serial, "STOP", token=token)
    print("Overwriting and deleting %d frames..." % len(names))
    result = broadcast(serial, "PURGE", token=token)
    print("PURGE -> %s" % result)

    remaining = remote_frames(serial)
    status = remote_status(serial)
    print("Frames left on device: %d" % len(remaining))
    print("Status               : %s" % status.get("note", "?"))

    if remaining:
        print("")
        print("FAILED: vault is not empty.")
        return 1

    # Forget local digests so a later purge can never rely on stale proof.
    for name in names:
        state["verified"].pop(name, None)
    save_state(state)

    print("")
    print("OK: vault empty, %d frames hard-erased from the phone." % len(names))
    return 0


def cmd_watch(args):
    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached.")
    print("Watching %s every %ss - Ctrl+C to stop." % (serial, args.interval))
    try:
        while True:
            rc = cmd_pull(_pull_args(args))
            if args.purge and rc == 0:
                print("--- verified pull complete, purging device vault ---")
                cmd_purge(argparse.Namespace(serial=serial, yes=True, force=False))
            print("")
            print("[%s] sleeping %ss..."
                  % (_dt.datetime.now().strftime("%H:%M:%S"), args.interval))
            time.sleep(args.interval)
    except KeyboardInterrupt:
        print("")
        print("Stopped.")
        return 0


def cmd_consent(args):
    serial = resolve_device(args.serial)
    if not serial:
        fail("No device attached.")
    out = _shell(
        serial,
        "am start -n %s/.feature.screenpulse.ScreenPulseControlActivity" % PACKAGE,
    )
    print(out.strip())
    print("Tap 'Start capture' on the handset once. Android requires that tap;")
    print("everything after it is driven from this tool.")
    return 0


# --------------------------------------------------------------------------
# cli
# --------------------------------------------------------------------------

def fail(message):
    sys.stdout.flush()
    print("error: %s" % message, file=sys.stderr)
    sys.exit(1)


def main(argv=None):
    if hasattr(sys.stdout, "reconfigure"):
        try:
            sys.stdout.reconfigure(encoding="utf-8", errors="replace")
            sys.stderr.reconfigure(encoding="utf-8", errors="replace")
        except Exception:
            pass

    parser = argparse.ArgumentParser(
        prog="screenpulse_pull.py",
        description="Pull, verify and hard-erase Screen Pulse frames from a handset.",
    )
    parser.add_argument("--serial", help="adb serial (default: first attached preset device)")
    sub = parser.add_subparsers(dest="command")

    p = sub.add_parser("connect", help="find the phone over Tailscale/USB and attach adb")
    p.set_defaults(func=cmd_connect)

    p = sub.add_parser("status", help="show vault state on the device")
    p.set_defaults(func=cmd_status)

    p = sub.add_parser("pull", help="copy frames to the laptop and verify SHA-256")
    p.add_argument("--dest", help="sub-folder name under pulled_screens/screenpulse/")
    p.add_argument("--wa-only", action="store_true", help="only WhatsApp-tagged frames")
    p.add_argument("--new-only", action="store_true", help="skip frames already verified here")
    p.add_argument("--verify", action="store_true", help="fail unless everything verified")
    p.set_defaults(func=cmd_pull)

    p = sub.add_parser("snapshot", help="make the app emit one synthetic frame")
    p.set_defaults(func=cmd_snapshot)

    p = sub.add_parser("purge", help="hard-erase every frame on the phone")
    p.add_argument("--yes", action="store_true", help="required confirmation")
    p.add_argument("--force", action="store_true", help="skip the 'already on laptop' check")
    p.set_defaults(func=cmd_purge)

    p = sub.add_parser("watch", help="pull on an interval, purge after each verified pull")
    p.add_argument("--interval", type=int, default=30, help="seconds between pulls (default 30)")
    p.add_argument("--purge", action="store_true", help="erase the phone after each verified pull")
    p.add_argument("--wa-only", action="store_true")
    p.add_argument("--verify", action="store_true", default=True)
    p.set_defaults(func=cmd_watch)

    p = sub.add_parser("consent", help="open the on-device capture consent screen")
    p.set_defaults(func=cmd_consent)

    args = parser.parse_args(argv)
    if not getattr(args, "func", None):
        parser.print_help()
        return 1
    try:
        return args.func(args)
    except RuntimeError as exc:
        fail(str(exc))
        return 1


if __name__ == "__main__":
    sys.exit(main())
