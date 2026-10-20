#!/usr/bin/env python3
"""
BAD GYM - Tailscale Screen Pull & Remote Control Utility.

Pulls live Android screens or connects wireless ADB over Tailscale VPN
without requiring the standalone Tailscale app on mobile.

Commands:
    python tools/pull_tailscale_screen.py pull
    python tools/pull_tailscale_screen.py stream
    python tools/pull_tailscale_screen.py adb
    python tools/pull_tailscale_screen.py status
"""

import argparse
import json
import os
import subprocess
import sys
import time
import urllib.request
import webbrowser

TAILSCALE_BIN = r"C:\Program Files\Tailscale\tailscale.exe"
DEFAULT_PORT = 8787
DEFAULT_ADB_PORT = 5555

def get_tailscale_peers():
    if not os.path.exists(TAILSCALE_BIN):
        return []
    try:
        res = subprocess.run([TAILSCALE_BIN, "status", "--json"],
                             capture_output=True, text=True, errors="replace", timeout=10)
        data = json.loads(res.stdout)
    except Exception as e:
        print(f"[!] Warning fetching Tailscale peers: {e}")
        return []
    
    peers = []
    for peer in (data.get("Peer") or {}).values():
        hostname = peer.get("HostName", "")
        ips = [ip for ip in peer.get("TailscaleIPs", []) if ":" not in ip]
        online = peer.get("Online", False)
        peers.append({"hostname": hostname, "ips": ips, "online": online})
    return peers

def find_target_ip(target_arg=None):
    if target_arg:
        return target_arg

    peers = get_tailscale_peers()
    # Find online badgym peers first
    for p in peers:
        if p["online"] and "badgym" in p["hostname"].lower() and p["ips"]:
            print(f"[*] Found online BAD GYM node: {p['hostname']} ({p['ips'][0]})")
            return p["ips"][0]

    # Find any online android peer
    for p in peers:
        if p["online"] and p["ips"]:
            print(f"[*] Found online peer: {p['hostname']} ({p['ips'][0]})")
            return p["ips"][0]

    print("[!] No active BAD GYM Tailscale nodes found automatically.")
    print("    Provide the target IP explicitly: --ip <100.x.y.z>")
    return None

def pull_screen(ip, port=DEFAULT_PORT, out_path="pulled_tailscale_screen.png"):
    url = f"http://{ip}:{port}/screen.png"
    print(f"[*] Pulling screen from {url}...")
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "BadGym-Pull/1.0"})
        with urllib.request.urlopen(req, timeout=10) as resp:
            data = resp.read()
            with open(out_path, "wb") as f:
                f.write(data)
        size_kb = len(data) / 1024
        print(f"[+] Screen successfully captured and saved to: {os.path.abspath(out_path)} ({size_kb:.1f} KB)")
        return True
    except Exception as e:
        print(f"[-] Failed to pull screen: {e}")
        return False

def open_stream(ip, port=DEFAULT_PORT):
    url = f"http://{ip}:{port}/screen/stream"
    print(f"[*] Opening live screen stream in browser: {url}")
    webbrowser.open(url)

def connect_adb(ip, adb_port=DEFAULT_ADB_PORT, launch_scrcpy=False):
    endpoint = f"{ip}:{adb_port}"
    print(f"[*] Connecting ADB to Tailscale endpoint: {endpoint}...")
    res = subprocess.run(["adb", "connect", endpoint], capture_output=True, text=True)
    print(res.stdout.strip())
    if "connected" in res.stdout.lower() and launch_scrcpy:
        print("[*] Launching scrcpy...")
        subprocess.Popen(["scrcpy", "-s", endpoint])

def check_status(ip, port=DEFAULT_PORT):
    url = f"http://{ip}:{port}/health"
    print(f"[*] Checking status of {url}...")
    try:
        with urllib.request.urlopen(url, timeout=5) as resp:
            data = resp.read().decode("utf-8")
            print(f"[+] Service OK: {data}")
            return True
    except Exception as e:
        print(f"[-] Service unreachable: {e}")
        return False

def main():
    parser = argparse.ArgumentParser(description="BAD GYM Tailscale Screen Pull")
    subparsers = parser.add_subparsers(dest="command", required=True)

    pull_p = subparsers.add_parser("pull", help="Pull a single screenshot over Tailscale")
    pull_p.add_argument("--ip", help="Target Tailscale IP")
    pull_p.add_argument("--port", type=int, default=DEFAULT_PORT, help="Port (default 8787)")
    pull_p.add_argument("--out", default="pulled_tailscale_screen.png", help="Output file path")

    stream_p = subparsers.add_parser("stream", help="Stream screen live over Tailscale in browser")
    stream_p.add_argument("--ip", help="Target Tailscale IP")
    stream_p.add_argument("--port", type=int, default=DEFAULT_PORT, help="Port (default 8787)")

    adb_p = subparsers.add_parser("adb", help="Connect ADB over Tailscale")
    adb_p.add_argument("--ip", help="Target Tailscale IP")
    adb_p.add_argument("--port", type=int, default=DEFAULT_ADB_PORT, help="ADB Port (default 5555)")
    adb_p.add_argument("--scrcpy", action="store_true", help="Launch scrcpy after connecting")

    status_p = subparsers.add_parser("status", help="Check remote agent health")
    status_p.add_argument("--ip", help="Target Tailscale IP")
    status_p.add_argument("--port", type=int, default=DEFAULT_PORT, help="Port (default 8787)")

    args = parser.parse_args()

    ip = find_target_ip(getattr(args, "ip", None))
    if not ip and args.command != "list":
        sys.exit(1)

    if args.command == "pull":
        pull_screen(ip, args.port, args.out)
    elif args.command == "stream":
        open_stream(ip, args.port)
    elif args.command == "adb":
        connect_adb(ip, args.port, getattr(args, "scrcpy", False))
    elif args.command == "status":
        check_status(ip, args.port)

if __name__ == "__main__":
    main()
