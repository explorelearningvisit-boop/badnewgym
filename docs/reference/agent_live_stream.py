import time
import json
import os
import sys
import io
import threading
from http.server import HTTPServer, BaseHTTPRequestHandler

# Reconfigure stdout/stderr for UTF-8 on Windows
if sys.platform == "win32":
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')
    sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8', errors='replace')

TRANSCRIPT_PATH = r"C:\Users\User\.gemini\antigravity-ide\brain\fdf3f7e1-3ffc-4f7d-b427-9316763f7533\.system_generated\logs\transcript.jsonl"
CURRENT_TASK_PATH = r"C:\Users\User\AndroidStudioProjects\badnewgym\CURRENT_TASK.md"

CYAN = "\033[96m"
YELLOW = "\033[93m"
GREEN = "\033[92m"
MAGENTA = "\033[95m"
BLUE = "\033[94m"
BOLD = "\033[1m"
RESET = "\033[0m"
RED = "\033[91m"

def print_header():
    os.system('cls' if os.name == 'nt' else 'clear')
    print(f"{CYAN}{'='*78}{RESET}", flush=True)
    print(f"{YELLOW}{BOLD}  ANTIGRAVITY AI AGENT -- LIVE ACTION & MOBILE REMOTE HUB  {RESET}", flush=True)
    print(f"{CYAN}{'='*78}{RESET}\n", flush=True)


def format_text(text):
    if not text:
        return ""
    lines = text.strip().split('\n')
    return "\n".join(["   " + l for l in lines])

class MobileCommandHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path in ('/api/stream', '/stream', '/'):
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            
            recent_logs = []
            def add_item(t_type, text_str):
                if text_str and text_str.strip():
                    recent_logs.append({"type": t_type, "text": text_str.rstrip()})

            if os.path.exists(TRANSCRIPT_PATH):
                try:
                    with open(TRANSCRIPT_PATH, 'r', encoding='utf-8', errors='ignore') as f:
                        lines = f.readlines()
                    for line in lines[-60:]:
                        try:
                            data = json.loads(line)
                            stype = data.get("type", "")
                            if stype == "USER_INPUT":
                                c = data.get("content", "")
                                if "<USER_REQUEST>" in c:
                                    c = c.split("<USER_REQUEST>")[1].split("</USER_REQUEST>")[0].strip()
                                for sub in c.split('\n'):
                                    add_item("USER", f"👤 USER: {sub}")
                                    
                            elif stype == "PLANNER_RESPONSE":
                                tcalls = data.get("tool_calls", [])
                                for tc in tcalls:
                                    name = tc.get("name", "tool")
                                    args = tc.get("args", {})
                                    cmd = args.get("CommandLine")
                                    target_file = args.get("TargetFile") or args.get("AbsolutePath") or args.get("SearchPath")
                                    desc = args.get("Description") or args.get("Instruction") or args.get("toolAction") or args.get("toolSummary") or ""
                                    
                                    add_item("ACTION", f"⚡ AGENT ACTION [{name}]")
                                    if cmd:
                                        add_item("ACTION", f"   $ {cmd}")
                                    if target_file:
                                        add_item("ACTION", f"   File: {target_file}")
                                    if desc:
                                        add_item("ACTION", f"   Info: {desc}")
                                
                                resp_text = data.get("content", "")
                                if resp_text and resp_text.strip():
                                    for sub in resp_text.strip().split('\n'):
                                        add_item("AI", f"💬 {sub}")
                                        
                            elif stype in ("SYSTEM", "SYSTEM_MESSAGE"):
                                content = data.get("content", "")
                                if content and content.strip():
                                    for sub in content.strip().split('\n'):
                                        # Skip verbose repetitive internal tags if needed
                                        if "<ADDITIONAL_METADATA>" in sub or "</ADDITIONAL_METADATA>" in sub:
                                            continue
                                        add_item("SYSTEM", f"🔔 {sub}")
                        except Exception:
                            pass
                except Exception:
                    pass
            # Keep last 150 lines for high performance scrolling
            self.wfile.write(json.dumps({"status": "ok", "logs": recent_logs[-150:]}).encode('utf-8'))
            return
        self.send_response(404)
        self.end_headers()


    def do_POST(self):
        content_length = int(self.headers.get('Content-Length', 0))
        post_data = self.rfile.read(content_length)
        try:
            payload = json.loads(post_data.decode('utf-8'))
            command = payload.get("command", "")
            image_info = payload.get("image", "")
            
            if command or image_info:
                prompt_msg = f"📱 MOBILE COMMAND RECEIVED: {command}"
                if image_info:
                    prompt_msg += f" [Attached Image: {image_info}]"
                print(f"\n{GREEN}{BOLD}{'='*70}{RESET}", flush=True)
                print(f"{GREEN}{BOLD}📱 MOBILE USER COMMAND:{RESET} {command}", flush=True)
                if image_info:
                    print(f"{GREEN}🖼️ IMAGE ATTACHED:{RESET} {image_info}", flush=True)
                print(f"{GREEN}{BOLD}{'='*70}{RESET}\n", flush=True)
                
                # Append command to CURRENT_TASK.md
                try:
                    with open(CURRENT_TASK_PATH, "a", encoding="utf-8") as f:
                        f.write(f"\n\n## Mobile Command ({time.strftime('%Y-%m-%d %H:%M:%S')})\n{command}\n")
                except Exception as ex:
                    pass
                
                self.send_response(200)
                self.send_header('Content-type', 'application/json')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                self.wfile.write(json.dumps({"status": "success", "message": "Command queued"}).encode('utf-8'))
                return
        except Exception as e:
            pass
        self.send_response(400)
        self.end_headers()

        
    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        self.end_headers()

def start_mobile_server():
    try:
        server = HTTPServer(('0.0.0.0', 7682), MobileCommandHandler)
        server.serve_forever()
    except Exception:
        pass

def parse_and_print_entry(line):
    try:
        data = json.loads(line)
        stype = data.get("type", "")
        
        if stype == "USER_INPUT":
            c = data.get("content", "")
            if "<USER_REQUEST>" in c:
                c = c.split("<USER_REQUEST>")[1].split("</USER_REQUEST>")[0].strip()
            if c:
                print(f"\n{MAGENTA}{BOLD}👤 USER PROMPT:{RESET} {c}", flush=True)
        
        elif stype == "PLANNER_RESPONSE":
            tcalls = data.get("tool_calls", [])
            for tc in tcalls:
                name = tc.get("name", "tool")
                args = tc.get("args", {})
                tool_action = args.get("toolAction") or args.get("toolSummary") or ""
                cmd = args.get("CommandLine")
                target_file = args.get("TargetFile") or args.get("AbsolutePath") or args.get("SearchPath")
                
                details = ""
                if cmd:
                    details = f" -> {cmd}"
                elif target_file:
                    details = f" -> {target_file}"
                elif tool_action:
                    details = f" -> {tool_action}"
                    
                print(f"{YELLOW}⚡ AGENT ACTION [{name}]:{RESET}{details}", flush=True)
            
            resp_text = data.get("content", "")
            if resp_text and resp_text.strip():
                print(f"{CYAN}{BOLD}💬 ANTIGRAVITY AI:{RESET}\n{format_text(resp_text)}\n", flush=True)
                
        elif stype in ("SYSTEM", "SYSTEM_MESSAGE"):
            content = data.get("content", "")
            if "finished with result" in content or "BUILD" in content or "Task id" in content:
                first_line = content.strip().split("\n")[0]
                print(f"{BLUE}🔔 SYSTEM NOTIFICATION:{RESET} {first_line}", flush=True)
    except Exception:
        pass

def stream_logs():
    print_header()
    print(f"{GREEN}[LIVE STREAM ACTIVE] Listening for All Actions, Tools, System Events & Mobile Commands...{RESET}\n", flush=True)
    
    # Start background command HTTP server
    t = threading.Thread(target=start_mobile_server, daemon=True)
    t.start()
    
    last_pos = 0
    
    if os.path.exists(TRANSCRIPT_PATH):
        try:
            with open(TRANSCRIPT_PATH, 'r', encoding='utf-8', errors='ignore') as f:
                lines = f.readlines()
                last_pos = len(lines)
                for line in lines[-20:]:
                    parse_and_print_entry(line)
        except Exception:
            pass

    print(f"\n{BLUE}--- REAL-TIME AGENT ACTION FEED ---{RESET}\n", flush=True)

    while True:
        try:
            if os.path.exists(TRANSCRIPT_PATH):
                with open(TRANSCRIPT_PATH, 'r', encoding='utf-8', errors='ignore') as f:
                    lines = f.readlines()
                if len(lines) > last_pos:
                    new_lines = lines[last_pos:]
                    last_pos = len(lines)
                    for line in new_lines:
                        parse_and_print_entry(line)
        except Exception:
            pass
        sys.stdout.flush()
        time.sleep(0.5)

if __name__ == "__main__":
    stream_logs()

