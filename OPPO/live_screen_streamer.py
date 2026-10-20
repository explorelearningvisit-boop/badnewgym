import os
import sys
import time
import json
import threading
import subprocess
import webbrowser
from http.server import HTTPServer, SimpleHTTPRequestHandler

DEVICE = "100.80.18.55:5555"
TOTAL_DURATION = 600  # 10 minutes
INTERVAL = 5          # 5 seconds
PORT = 8999

BASE_DIR = r"c:\Users\User\AndroidStudioProjects\badnewgym"
OUTPUT_IMAGE = os.path.join(BASE_DIR, "oppo_live_screen.png")
TEMP_IMAGE = os.path.join(BASE_DIR, "oppo_live_screen_tmp.png")

state = {
    "status": "Starting...",
    "app": "Detecting...",
    "last_update": "",
    "pull_count": 0,
    "elapsed": 0,
    "remaining": TOTAL_DURATION,
    "active": True
}

HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OPPO Live Screen Monitor (Tailscale)</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background: #0f172a;
      color: #f8fafc;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
    }
    header {
      text-align: center;
      margin-bottom: 20px;
    }
    h1 {
      font-size: 1.6rem;
      font-weight: 700;
      letter-spacing: -0.5px;
      color: #38bdf8;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
    }
    .pulse-dot {
      width: 12px;
      height: 12px;
      background: #22c55e;
      border-radius: 50%;
      box-shadow: 0 0 10px #22c55e;
      animation: pulse 1.5s infinite;
    }
    @keyframes pulse {
      0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(34, 197, 94, 0.7); }
      70% { transform: scale(1.1); box-shadow: 0 0 0 8px rgba(34, 197, 94, 0); }
      100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(34, 197, 94, 0); }
    }
    .stats-card {
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 12px;
      padding: 12px 24px;
      display: flex;
      gap: 24px;
      margin-bottom: 20px;
      font-size: 0.9rem;
      box-shadow: 0 4px 15px rgba(0,0,0,0.3);
      flex-wrap: wrap;
      justify-content: center;
    }
    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .stat-label {
      color: #94a3b8;
      font-size: 0.75rem;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .stat-value {
      font-weight: 600;
      color: #f1f5f9;
      margin-top: 3px;
    }
    .screen-container {
      position: relative;
      background: #000;
      border: 4px solid #334155;
      border-radius: 28px;
      padding: 8px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.6);
      max-width: 420px;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
    }
    #screen-img {
      width: 100%;
      height: auto;
      border-radius: 20px;
      display: block;
      object-fit: contain;
      background: #111;
      min-height: 500px;
    }
    .badge {
      display: inline-block;
      padding: 2px 8px;
      border-radius: 6px;
      font-size: 0.8rem;
      font-weight: 600;
      background: #0369a1;
      color: #e0f2fe;
    }
  </style>
</head>
<body>
  <header>
    <h1><span class="pulse-dot" id="live-dot"></span> OPPO A53 Live Monitor</h1>
    <p style="color:#64748b; font-size: 0.85rem; margin-top: 4px;">Tailscale: 100.80.18.55:5555 &bull; Pulling every 5 seconds</p>
  </header>

  <div class="stats-card">
    <div class="stat-item">
      <span class="stat-label">Active App</span>
      <span class="stat-value badge" id="app-name">Checking...</span>
    </div>
    <div class="stat-item">
      <span class="stat-label">Last Updated</span>
      <span class="stat-value" id="last-update">--:--:--</span>
    </div>
    <div class="stat-item">
      <span class="stat-label">Pulls Completed</span>
      <span class="stat-value" id="pull-count">0</span>
    </div>
    <div class="stat-item">
      <span class="stat-label">Time Remaining</span>
      <span class="stat-value" id="time-left">10:00</span>
    </div>
  </div>

  <div class="screen-container">
    <img id="screen-img" src="/screen.png" alt="OPPO Live Screen">
  </div>

  <script>
    async function updateStatus() {
      try {
        const res = await fetch('/status?t=' + Date.now());
        if (res.ok) {
          const data = await res.json();
          document.getElementById('app-name').innerText = data.app || 'N/A';
          document.getElementById('last-update').innerText = data.last_update || '--';
          document.getElementById('pull-count').innerText = data.pull_count;
          
          const rem = Math.max(0, data.remaining);
          const mins = Math.floor(rem / 60);
          const secs = rem % 60;
          document.getElementById('time-left').innerText = 
            String(mins).padStart(2, '0') + ':' + String(secs).padStart(2, '0');
            
          if (!data.active) {
            document.getElementById('live-dot').style.background = '#ef4444';
            document.getElementById('live-dot').style.animation = 'none';
          }
        }
      } catch (e) {
        console.error("Status fetch error", e);
      }
    }

    function refreshImage() {
      const img = document.getElementById('screen-img');
      img.src = '/screen.png?t=' + Date.now();
    }

    // Refresh image every 2 seconds to catch new pulls
    setInterval(refreshImage, 2500);
    // Update status every 1.5 seconds
    setInterval(updateStatus, 1500);
    updateStatus();
  </script>
</body>
</html>
"""

class LiveHandler(SimpleHTTPRequestHandler):
    def log_message(self, format, *args):
        pass  # Suppress HTTP access logs to keep terminal clean

    def do_GET(self):
        if self.path == '/' or self.path.startswith('/?'):
            self.send_response(200)
            self.send_header('Content-Type', 'text/html; charset=utf-8')
            self.end_headers()
            self.wfile.write(HTML_CONTENT.encode('utf-8'))
        elif self.path.startswith('/screen.png'):
            if os.path.exists(OUTPUT_IMAGE):
                try:
                    with open(OUTPUT_IMAGE, 'rb') as f:
                        data = f.read()
                    self.send_response(200)
                    self.send_header('Content-Type', 'image/png')
                    self.send_header('Cache-Control', 'no-cache, no-store, must-revalidate')
                    self.send_header('Pragma', 'no-cache')
                    self.send_header('Expires', '0')
                    self.end_headers()
                    self.wfile.write(data)
                except Exception as e:
                    self.send_error(500, str(e))
            else:
                self.send_error(404, "Screen image not found")
        elif self.path.startswith('/status'):
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.send_header('Cache-Control', 'no-cache')
            self.end_headers()
            self.wfile.write(json.dumps(state).encode('utf-8'))
        else:
            self.send_error(404)

def run_server():
    server = HTTPServer(('127.0.0.1', PORT), LiveHandler)
    server.serve_forever()

def get_focused_app():
    try:
        res = subprocess.run(
            ["adb", "-s", DEVICE, "shell", "dumpsys window | grep -E 'mCurrentFocus|mFocusedApp'"],
            capture_output=True, text=True, timeout=5
        )
        out = res.stdout.strip()
        lines = out.splitlines()
        for line in lines:
            if "mFocusedApp" in line or "mCurrentFocus" in line:
                # Extract package name
                parts = line.split()
                for p in parts:
                    if "/" in p:
                        clean = p.replace("u0", "").replace("}", "").replace("{", "").strip()
                        return clean
        return "Unknown"
    except Exception:
        return "Checking..."

def monitor_loop():
    start_time = time.time()
    print(f"[*] Starting OPPO live monitor for {TOTAL_DURATION}s (10 mins), interval ~{INTERVAL}s...")
    
    # Ensure connected
    subprocess.run(["adb", "connect", DEVICE], capture_output=True, timeout=10)
    
    pull_idx = 0
    while True:
        elapsed = int(time.time() - start_time)
        remaining = max(0, TOTAL_DURATION - elapsed)
        
        state["elapsed"] = elapsed
        state["remaining"] = remaining
        
        if remaining <= 0:
            print("[*] 10 minutes completed. Stopping monitor.")
            state["active"] = False
            state["status"] = "Completed"
            break

        pull_start = time.time()
        
        # Capture screen on device
        cap_res = subprocess.run(
            ["adb", "-s", DEVICE, "shell", "screencap -p /sdcard/live_screen.png"],
            capture_output=True, timeout=12
        )
        
        if cap_res.returncode == 0:
            # Pull to temp file
            pull_res = subprocess.run(
                ["adb", "-s", DEVICE, "pull", "/sdcard/live_screen.png", TEMP_IMAGE],
                capture_output=True, timeout=15
            )
            if pull_res.returncode == 0 and os.path.exists(TEMP_IMAGE):
                # Atomic replace
                try:
                    if os.path.exists(OUTPUT_IMAGE):
                        os.remove(OUTPUT_IMAGE)
                    os.rename(TEMP_IMAGE, OUTPUT_IMAGE)
                    pull_idx += 1
                    state["pull_count"] = pull_idx
                    state["last_update"] = time.strftime("%H:%M:%S")
                    
                    app_info = get_focused_app()
                    state["app"] = app_info
                    print(f"[{time.strftime('%H:%M:%S')}] Pull #{pull_idx} OK | App: {app_info} | Remaining: {remaining}s")
                except Exception as e:
                    print(f"[!] Error updating image file: {e}")
        else:
            # Try reconnect if failed
            print("[!] Screencap failed, reconnecting ADB...")
            subprocess.run(["adb", "connect", DEVICE], capture_output=True, timeout=10)

        # Sleep remaining time of the 5s interval
        duration_of_pull = time.time() - pull_start
        sleep_time = max(1.0, INTERVAL - duration_of_pull)
        time.sleep(sleep_time)

if __name__ == "__main__":
    # Start web server thread
    srv_thread = threading.Thread(target=run_server, daemon=True)
    srv_thread.start()
    print(f"[*] Live Web Viewer running at http://localhost:{PORT}")
    
    # Open browser
    try:
        webbrowser.open(f"http://localhost:{PORT}")
    except Exception:
        pass
        
    # Run the monitor loop in main thread
    monitor_loop()
