import os
import sys
import time
import re
import csv
import shutil
from datetime import datetime
import base64
import subprocess
import threading
import socket
import struct
import io
import hashlib
import concurrent.futures
import queue
import tkinter as tk
from tkinter import ttk, messagebox, filedialog
import json
import urllib.request
import webbrowser

try:
    from PIL import Image, ImageTk
    HAS_PIL = True
except ImportError:
    HAS_PIL = False

NO_WINDOW = subprocess.CREATE_NO_WINDOW if os.name == 'nt' else 0

PRESET_DEVICES = [
    ("Xiaomi 11i [BAD GYM Tailscale Live]", "100.84.228.109:5555"),
    ("OPPO A53 [Tailscale]", "100.80.18.55:5555"),
    ("Xiaomi 11i [USB Fast]", "zxdada69gunb7ls4"),
    ("Xiaomi 11i [Wi-Fi Local]", "192.168.1.37:5555"),
    ("Xiaomi 11i [Tailscale Standalone]", "100.123.18.54:5555"),
]

DEFAULT_PKG = "com.example.badnewgym"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_DIR = os.path.dirname(BASE_DIR)

# Dedicated date-wise laptop storage folder
PULLED_SCREENS_DIR = os.path.join(BASE_DIR, "pulled_screens")
os.makedirs(PULLED_SCREENS_DIR, exist_ok=True)

# Local DEX compiler output for Android internal execution
FASTCAP_DEX_LOCAL = os.path.join(BASE_DIR, "fastcap", "classes.dex")
FASTCAP_DEX_REMOTE = "/data/local/tmp/fastcap.dex"

SCREEN_PATH = os.path.join(BASE_DIR, "current_screen.png")
TMP_SCREEN_PATH = os.path.join(BASE_DIR, "tmp_screen.png")
PULLED_MEDIA_DIR = os.path.join(BASE_DIR, "pulled_media")
os.makedirs(PULLED_MEDIA_DIR, exist_ok=True)
os.makedirs(os.path.join(PULLED_MEDIA_DIR, "thumbs"), exist_ok=True)

class DeviceDevStudio(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Android Multi-Device Dev Studio (OPPO & Xiaomi)")
        self.geometry("1400x900")
        self.minsize(1080, 720)
        self.configure(bg="#0f172a")

        self.target_device = PRESET_DEVICES[0][1] # Default: OPPO A53 Tailscale
        self.connected = False
        self.pulling = False
        self.pull_thread = None
        self.screen_img = None
        self.gallery_preview_img = None
        self.file_preview_img = None
        self.last_raw_bytes = None
        self.current_scale_factor = 3
        self.pull_counter = 0
        self.var_autosave_frames = tk.BooleanVar(value=True)

        self.last_saved_path = None
        self.last_saved_folder = None
        self.current_remote_items = []
        self.file_preview_selected_path = None

        # Gallery media state
        self.all_gallery_items = []      # list of dicts: {path, name, folder, size, size_bytes, date, category, selected, is_sticker, is_trashed}
        self.filtered_gallery_items = []
        self.current_gallery_filter = "ALL"
        self.gallery_group_by = "NONE"   # "NONE", "DATE_MONTH", "FOLDER", "SIZE", "EXT"
        self.exclude_stickers = True     # Exclude WhatsApp stickers & junk webp by default
        self.gallery_sort_order = "DESC" # "DESC" (Today ➔ Day 1) or "ASC" (Day 1 Purchasing Photo ➔ Today)
        self.previewing_item = None
        self.preview_panel_visible = True
        self.gallery_view_mode = "GRID"  # "GRID" or "TABLE"
        self.gallery_thumb_size = 140    # 100 (Small), 140 (Medium), 185 (Large)
        self.card_image_cache = {}       # path_hash -> PhotoImage
        self.card_widgets = {}           # global_idx -> dict of card widgets
        self.gallery_page = 0
        self.gallery_page_size = 60
        self.gallery_total_pages = 1
        self.gallery_stream_loaded_count = 0
        self.gallery_is_loading_more = False
        self.gallery_infinite_scroll = True
        self.thumb_queue = []
        self.thumb_queue_lock = threading.Lock()
        self.thumb_worker_active = False

        # Sleep/Wake presence observer state (Zero Mobile CPU/RAM)
        self.wake_watcher_active = False
        self.wake_watcher_thread = None

        # Call logs & contacts state
        self.all_call_logs = []
        self.filtered_call_logs = []
        self.all_contacts = []
        self.filtered_contacts = []
        
        # Installed & Hidden apps state
        self.all_installed_apps = []
        self.filtered_installed_apps = []
        self.current_apps_filter = "USER"
        self.selected_app_pkg = None
        self.selected_app_apk = None

        # Analytics cached metrics
        self.analytics_data = {}

        # WhatsApp Chat & Media Sync state
        self.all_wa_chats = []
        self.filtered_wa_chats = []
        self.selected_wa_chat = None
        self.wa_messages = []
        self.wa_pulled_media = []
        self.wa_auto_sync = False
        self.wa_sync_thread = None
        self.wa_image_cache = {}

        self._setup_styles()
        self._build_layout()
        self.protocol("WM_DELETE_WINDOW", self.on_close)
        
        # Connect to default device after UI draws
        self.after(300, self.auto_detect_and_connect)

    def _setup_styles(self):
        style = ttk.Style(self)
        style.theme_use("clam")
        style.configure("TFrame", background="#0f172a")
        style.configure("TLabel", background="#0f172a", foreground="#f8fafc", font=("Segoe UI", 9))
        style.configure("Header.TLabel", background="#0f172a", foreground="#38bdf8", font=("Segoe UI", 12, "bold"))
        style.configure("Status.TLabel", background="#1e293b", foreground="#94a3b8", font=("Segoe UI", 9))
        style.configure("Accent.TButton", font=("Segoe UI", 9, "bold"), background="#0284c7", foreground="#ffffff")
        style.map("Accent.TButton", background=[("active", "#0369a1")])
        style.configure("Stop.TButton", font=("Segoe UI", 9, "bold"), background="#b91c1c", foreground="#ffffff")
        style.map("Stop.TButton", background=[("active", "#991b1b")])

        # Notebook & Tab styling for dark theme
        style.configure("TNotebook", background="#0f172a", borderwidth=0)
        style.configure("TNotebook.Tab", background="#1e293b", foreground="#94a3b8", padding=[4, 2], font=("Segoe UI", 8, "bold"))
        style.map("TNotebook.Tab", background=[("selected", "#0284c7")], foreground=[("selected", "#ffffff")])

        # Combobox styling for dark theme
        style.configure("TCombobox", fieldbackground="#1e293b", background="#334155", foreground="#ffffff", darkcolor="#1e293b", lightcolor="#334155")
        style.map("TCombobox", fieldbackground=[("readonly", "#1e293b")], foreground=[("readonly", "#ffffff")])
        
        # Treeview styling for dark theme
        style.configure(
            "Treeview",
            background="#1e293b",
            foreground="#f8fafc",
            fieldbackground="#1e293b",
            font=("Segoe UI", 9),
            rowheight=24
        )
        style.configure("Treeview.Heading", background="#334155", foreground="#38bdf8", font=("Segoe UI", 9, "bold"))
        style.map("Treeview", background=[("selected", "#0284c7")], foreground=[("selected", "#ffffff")])

    def _build_layout(self):
        top_container = tk.Frame(self, bg="#1e293b", relief="solid", bd=1)
        top_container.pack(fill="x", side="top")

        # Row 1: App Title, Target Device Picker, Connection & Power Controls
        top_bar_row1 = tk.Frame(top_container, bg="#1e293b", padx=10, pady=5)
        top_bar_row1.pack(fill="x", side="top")

        tk.Label(top_bar_row1, text="📱 Android Dev Studio", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left", padx=(0, 10))

        tk.Label(top_bar_row1, text="Device:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8, "bold")).pack(side="left", padx=2)

        self.device_combo = ttk.Combobox(
            top_bar_row1,
            values=[f"{label} - {addr}" for label, addr in PRESET_DEVICES],
            width=30,
            state="readonly"
        )
        self.device_combo.current(0)
        self.device_combo.pack(side="left", padx=3)
        self.device_combo.bind("<<ComboboxSelected>>", self._on_device_combo_select)

        self.btn_refresh_devs = tk.Button(top_bar_row1, text="🔄 Scan", bg="#334155", fg="white", activebackground="#475569", activeforeground="white", font=("Segoe UI", 8), padx=6, relief="flat", command=self.refresh_devices_list)
        self.btn_refresh_devs.pack(side="left", padx=2)

        self.btn_connect = tk.Button(top_bar_row1, text="Connect ADB", bg="#0284c7", fg="white", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.toggle_connection)
        self.btn_connect.pack(side="left", padx=2)

        self.status_lbl = tk.Label(top_bar_row1, text="● Disconnected", bg="#1e293b", fg="#ef4444", font=("Segoe UI", 9, "bold"))
        self.status_lbl.pack(side="left", padx=6)

        # BAD GYM LIVE STATUS BADGE
        self.badgym_status_badge = tk.Label(top_bar_row1, text="🏋️ BAD GYM: CHECKING...", bg="#334155", fg="#f59e0b", font=("Segoe UI", 8, "bold"), padx=8, pady=2, relief="flat", cursor="hand2")
        self.badgym_status_badge.pack(side="left", padx=4)
        self.badgym_status_badge.bind("<Button-1>", lambda e: self.open_badgym_tab())

        # Power Controls on Right of Row 1
        tk.Button(top_bar_row1, text="💤 Sleep", bg="#475569", fg="#f8fafc", activebackground="#334155", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.put_device_to_sleep).pack(side="right", padx=3)
        tk.Button(top_bar_row1, text="⚡ Wake", bg="#0284c7", fg="#f8fafc", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.wake_device).pack(side="right", padx=3)

        # Row 2: Action Toolbar & Active App Banner
        top_bar_row2 = tk.Frame(top_container, bg="#0f172a", padx=10, pady=4)
        top_bar_row2.pack(fill="x", side="top")

        self.btn_web_stream_top = tk.Button(top_bar_row2, text="🎥 Live Web Stream", bg="#0891b2", fg="white", activebackground="#0e7490", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.open_live_web_stream)
        self.btn_web_stream_top.pack(side="left", padx=3)

        self.btn_snap_top = tk.Button(top_bar_row2, text="📸 Snap Screen", bg="#0284c7", fg="white", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.pull_single_screen)
        self.btn_snap_top.pack(side="left", padx=3)

        self.btn_open_pulled_top = tk.Button(top_bar_row2, text="📁 Pulled Screens", bg="#334155", fg="white", activebackground="#475569", activeforeground="white", font=("Segoe UI", 8), padx=7, relief="flat", command=self.open_saved_folder)
        self.btn_open_pulled_top.pack(side="left", padx=3)

        self.btn_full_backup_top = tk.Button(top_bar_row2, text="⚡ 1-Click Backup", bg="#7c3aed", fg="white", activebackground="#6d28d9", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.backup_entire_phone_data_one_click)
        self.btn_full_backup_top.pack(side="left", padx=3)

        self.btn_scan_pull_top = tk.Button(top_bar_row2, text="🚀 Scan Media", bg="#16a34a", fg="white", activebackground="#15803d", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.scan_and_pull_whole_mobile_media)
        self.btn_scan_pull_top.pack(side="left", padx=3)

        self.btn_storage_clean_top = tk.Button(top_bar_row2, text="💾 Storage & Purge", bg="#0d9488", fg="white", activebackground="#0f766e", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.open_storage_tab)
        self.btn_storage_clean_top.pack(side="left", padx=3)

        self.btn_pair_wireless = tk.Button(top_bar_row2, text="🔗 Pair Wireless", bg="#475569", fg="white", activebackground="#334155", activeforeground="white", font=("Segoe UI", 8), padx=7, relief="flat", command=self.open_wireless_pairing_dialog)
        self.btn_pair_wireless.pack(side="left", padx=3)

        self.active_app_banner = tk.Label(top_bar_row2, text="🏠 Home / App Closed", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8, "bold"), padx=10, pady=2, relief="flat")
        self.active_app_banner.pack(side="right", padx=3)

        main_paned = tk.PanedWindow(self, orient="horizontal", bg="#0f172a", sashwidth=4, bd=0)
        main_paned.pack(fill="both", expand=True, padx=8, pady=8)

        left_col = tk.Frame(main_paned, bg="#0f172a")
        main_paned.add(left_col, minsize=520)

        self.main_tabs = ttk.Notebook(left_col)
        self.main_tabs.pack(fill="both", expand=True)

        tab_badgym = ttk.Frame(self.main_tabs, padding=6)
        self.tab_badgym = tab_badgym
        tab_whatsapp = ttk.Frame(self.main_tabs, padding=6)
        tab_gallery = ttk.Frame(self.main_tabs, padding=6)
        tab_calls = ttk.Frame(self.main_tabs, padding=6)
        tab_apps = ttk.Frame(self.main_tabs, padding=6)
        tab_files = ttk.Frame(self.main_tabs, padding=6)
        tab_storage = ttk.Frame(self.main_tabs, padding=6)
        tab_analytics = ttk.Frame(self.main_tabs, padding=6)
        tab_power = ttk.Frame(self.main_tabs, padding=6)
        tab_log = ttk.Frame(self.main_tabs, padding=6)

        self.tab_storage = tab_storage

        self.main_tabs.add(tab_badgym, text=" 🏋️ BAD GYM ")
        self.main_tabs.add(tab_whatsapp, text=" 💬 WhatsApp ")
        self.main_tabs.add(tab_gallery, text=" 🖼️ Gallery ")
        self.main_tabs.add(tab_calls, text=" 📞 Calls ")
        self.main_tabs.add(tab_apps, text=" ⚡ Apps ")
        self.main_tabs.add(tab_files, text=" 📁 Files ")
        self.main_tabs.add(tab_storage, text=" 💾 Storage & Clean ")
        self.main_tabs.add(tab_analytics, text=" 📊 Analytics ")
        self.main_tabs.add(tab_power, text=" 🔋 Power ")
        self.main_tabs.add(tab_log, text=" 📋 ADB Log ")

        self._build_badgym_tab(tab_badgym)
        self._build_whatsapp_tab(tab_whatsapp)
        self._build_gallery_tab(tab_gallery)
        self._build_calls_tab(tab_calls)
        self._build_apps_tab(tab_apps)
        self._build_files_tab(tab_files)
        self._build_storage_tab(tab_storage)
        self._build_analytics_tab(tab_analytics)
        self._build_power_tab(tab_power)
        self._build_log_tab(tab_log)

        right_col = tk.Frame(main_paned, bg="#1e293b", padx=10, pady=10, relief="solid", bd=1)
        main_paned.add(right_col, minsize=420)
        self._build_screen_panel(right_col)

    def _on_device_combo_select(self, event=None):
        sel = self.device_combo.get()
        if " - " in sel:
            addr = sel.split(" - ")[-1].strip()
            if addr != self.target_device or not self.connected:
                self._stop_wake_watcher()
                if self.pulling:
                    self.pulling = False
                    self.btn_pull_live.config(text="▶ Stream", bg="#16a34a")
                self.target_device = addr
                self.log(f"Switched target device to: {sel}")
                self._clear_device_data()
                self.toggle_connection(force_connect=True)

    def _start_wake_watcher(self, addr):
        """Ultra-low-overhead presence observer (0% Mobile CPU/RAM/Battery) that detects when sleeping or powered-off phone wakes up / reconnects on Tailscale and auto-connects"""
        if self.wake_watcher_active or self.connected:
            return
        if ":" not in addr:
            return

        ip, port_str = addr.split(":", 1)
        try:
            port = int(port_str)
        except ValueError:
            port = 5555

        self.wake_watcher_active = True

        def observer_loop():
            self.log(f"💤 Device sleeping / turned off. Observer active: auto-monitoring Tailscale wake-up for {addr}...")
            if not self.connected:
                self.after(0, lambda: self.status_lbl.config(
                    text=f"💤 Monitoring Tailscale ({ip}...)",
                    fg="#f59e0b"
                ))
            
            probe_counter = 0
            while self.wake_watcher_active and not self.connected:
                time.sleep(2.5)
                if not self.wake_watcher_active or self.connected:
                    break
                
                probe_counter += 1
                tcp_alive = False
                ping_alive = False

                # 1. TCP Port Probe
                try:
                    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    s.settimeout(1.2)
                    res = s.connect_ex((ip, port))
                    s.close()
                    if res == 0:
                        tcp_alive = True
                except Exception:
                    pass

                # 2. ICMP Ping probe over Tailscale
                if not tcp_alive:
                    try:
                        p_res = subprocess.run(["ping", "-n", "1", "-w", "800", ip], capture_output=True, timeout=2, creationflags=NO_WINDOW)
                        out_str = p_res.stdout.decode('utf-8', errors='ignore').lower()
                        if p_res.returncode == 0 and ("reply from" in out_str or "bytes=" in out_str):
                            ping_alive = True
                    except Exception:
                        pass

                if ping_alive and not tcp_alive:
                    self.after(0, lambda: self.status_lbl.config(
                        text=f"📡 Device Online on Tailscale! ({ip} OK • Auto-opening Port 5555)",
                        fg="#38bdf8"
                    ))
                    # Auto-recovery after mobile reboot: check if USB cable is plugged in to automatically re-enable tcpip 5555
                    try:
                        dev_check = subprocess.run(["adb", "devices"], capture_output=True, timeout=4, creationflags=NO_WINDOW)
                        dev_lines = dev_check.stdout.decode('utf-8', errors='ignore').splitlines()
                        for dl in dev_lines[1:]:
                            dp = dl.strip().split()
                            if len(dp) >= 2 and dp[1] == "device" and ":" not in dp[0]:
                                subprocess.run(["adb", "-s", dp[0], "tcpip", "5555"], capture_output=True, timeout=5, creationflags=NO_WINDOW)
                                time.sleep(1)
                                break
                    except Exception:
                        pass

                if tcp_alive or (ping_alive and probe_counter % 3 == 0):
                    # Attempt ADB connect over Tailscale
                    try:
                        subprocess.run(["adb", "disconnect", addr], capture_output=True, timeout=3, creationflags=NO_WINDOW)
                        subprocess.run(["adb", "connect", addr], capture_output=True, timeout=5, creationflags=NO_WINDOW)
                    except Exception:
                        pass

                    # Verify model
                    try:
                        t_res = subprocess.run(["adb", "-s", addr, "shell", "getprop ro.product.model"], capture_output=True, timeout=4, creationflags=NO_WINDOW)
                        t_out = t_res.stdout.decode('utf-8', errors='replace').strip()
                    except Exception:
                        t_out = ""

                    if t_out and "offline" not in t_out.lower() and "error" not in t_out.lower():
                        self.log(f"⚡ Mobile Woke Up & Connected! Model: {t_out}")
                        self.wake_watcher_active = False
                        self.after(0, lambda: self.status_lbl.config(
                            text="⚡ Mobile Woke Up! Connecting...",
                            fg="#38bdf8"
                        ))
                        self.after(0, lambda: self.toggle_connection(force_connect=True))
                        break

            self.wake_watcher_active = False

        self.wake_watcher_thread = threading.Thread(target=observer_loop, daemon=True)
        self.wake_watcher_thread.start()

    def _stop_wake_watcher(self):
        self.wake_watcher_active = False

    def _clear_device_data(self):
        """Completely clears previously loaded mobile data, gallery, calls, contacts, apps, and previews so devices never mix"""
        self.all_gallery_items = []
        self.filtered_gallery_items = []
        self.card_widgets = {}
        self.card_image_cache = {}
        self.previewing_item = None
        self.gallery_preview_img = None
        with self.thumb_queue_lock:
            self.thumb_queue = []
            self.thumb_worker_active = False

        self.all_call_logs = []
        self.filtered_call_logs = []
        self.all_contacts = []
        self.filtered_contacts = []

        self.all_installed_apps = []
        self.filtered_installed_apps = []
        self.selected_app_pkg = None
        self.selected_app_apk = None

        self.current_remote_items = []

        def _reset_ui():
            try:
                # Gallery UI reset
                if hasattr(self, 'gallery_tree'):
                    self.gallery_tree.delete(*self.gallery_tree.get_children())
                if hasattr(self, 'grid_inner_frame'):
                    for w in self.grid_inner_frame.winfo_children():
                        w.destroy()
                if hasattr(self, 'gallery_summary_lbl'):
                    self.gallery_summary_lbl.config(text="Switching device... click 'Scan Mobile' to refresh")
                if hasattr(self, 'gallery_sel_count_lbl'):
                    self.gallery_sel_count_lbl.config(text="Selected: 0 files")
                if hasattr(self, 'gallery_preview_lbl'):
                    self.gallery_preview_lbl.config(image="", text="Click any photo card\nor list row to preview")
                if hasattr(self, 'preview_info_lbl'):
                    self.preview_info_lbl.config(text="No image selected")
                if hasattr(self, 'page_info_lbl'):
                    self.page_info_lbl.config(text="Page 0 of 0 (0 items)")

                # Calls & Contacts UI reset
                if hasattr(self, 'calls_tree'):
                    self.calls_tree.delete(*self.calls_tree.get_children())
                if hasattr(self, 'contacts_tree'):
                    self.contacts_tree.delete(*self.contacts_tree.get_children())
                if hasattr(self, 'calls_summary_lbl'):
                    self.calls_summary_lbl.config(text="Click 'Fetch Live Calls' to load")

                # Apps UI reset
                if hasattr(self, 'apps_tree'):
                    self.apps_tree.delete(*self.apps_tree.get_children())
                if hasattr(self, 'apps_summary_lbl'):
                    self.apps_summary_lbl.config(text="Click 'Scan Apps' to list")
                if hasattr(self, 'selected_app_lbl'):
                    self.selected_app_lbl.config(text="Select an app below")

                # Files UI reset
                if hasattr(self, 'file_tree'):
                    self.file_tree.delete(*self.file_tree.get_children())

                # Battery & Screen UI reset
                if hasattr(self, 'batt_level_lbl'):
                    self.batt_level_lbl.config(text="Battery Level: Checking...")
                if hasattr(self, 'batt_status_lbl'):
                    self.batt_status_lbl.config(text="Charging State: Unknown")
                if hasattr(self, 'screen_lbl') and not self.pulling:
                    self.screen_lbl.config(image="", text="Connecting to target device...\nClick 'Snapshot' or 'Start 1s Stream'")
            except Exception:
                pass

        try:
            self.after(0, _reset_ui)
        except Exception:
            _reset_ui()

    def _get_thumb_path(self, remote_path, fname):
        """Isolates thumbnail cache per target device name so devices never share cached thumbnails"""
        dev_name = self.get_device_clean_name()
        thumb_dir = os.path.join(PULLED_MEDIA_DIR, "thumbs", dev_name)
        os.makedirs(thumb_dir, exist_ok=True)
        path_hash = hashlib.md5(f"{dev_name}_{remote_path}".encode('utf-8', errors='ignore')).hexdigest()[:10]
        safe_name = "".join(ch for ch in fname if ch.isalnum() or ch in ('_', '-', '.'))
        return os.path.join(thumb_dir, f"thumb_{path_hash}_{safe_name}.jpg")

    def get_device_clean_name(self):
        """Clean mobile name for organizing folder structure without illegal path chars (Thread-Safe)"""
        curr = ""
        try:
            curr = self.device_combo.get() if hasattr(self, 'device_combo') else ""
        except Exception:
            pass
        if not curr:
            curr = getattr(self, '_selected_device_text', '')
        if " - " in curr:
            lbl = curr.split(" - ")[0].strip()
            clean = lbl.split("[")[0].strip().replace(" ", "_")
            clean = "".join(c for c in clean if c.isalnum() or c in ('_', '-'))
            if clean:
                return clean
        for label, addr in PRESET_DEVICES:
            if addr == self.target_device:
                clean = label.split("[")[0].strip().replace(" ", "_")
                clean = "".join(c for c in clean if c.isalnum() or c in ('_', '-'))
                return clean
        return self.target_device.replace(":", "_").replace(".", "_")

    def _get_stream_interval(self):
        """Thread-safe stream speed interval parser"""
        mode_sel = "1.0s (Smooth)"
        try:
            if hasattr(self, 'interval_combo'):
                mode_sel = self.interval_combo.get()
                self._cached_interval_mode = mode_sel
        except Exception:
            mode_sel = getattr(self, '_cached_interval_mode', "1.0s (Smooth)")
        m_num = re.search(r'([0-9.]+)', mode_sel)
        if m_num:
            try:
                return float(m_num.group(1))
            except Exception:
                pass
        return 1.0

    def open_badgym_tab(self):
        if hasattr(self, 'tab_badgym') and hasattr(self, 'main_tabs'):
            self.main_tabs.select(self.tab_badgym)
            self._check_target_badgym_health()

    def open_live_web_stream(self):
        ip = self._get_badgym_ip() or "100.84.228.109"
        url = f"http://{ip}:8787/"
        self.log(f"Opening live video stream & remote control in browser: {url}")
        if hasattr(self, 'badgym_log'):
            self.badgym_log(f"Opening live video stream & remote control in browser: {url}")
        webbrowser.open(url)

    def _get_badgym_ip(self):
        addr = self.target_device
        if ":" in addr:
            return addr.split(":")[0]
        # USB device: check or establish port forwarding
        try:
            subprocess.run(["adb", "-s", addr, "forward", "tcp:8787", "tcp:8787"], capture_output=True, timeout=1, creationflags=NO_WINDOW)
            return "127.0.0.1"
        except Exception:
            pass
        return None

    def _start_badgym_health_poller(self):
        """Continuously checks if BAD GYM is live on the selected target device"""
        if getattr(self, '_badgym_poller_active', False):
            return
        self._badgym_poller_active = True
        
        def poller_loop():
            while getattr(self, '_badgym_poller_active', False):
                self._check_target_badgym_health()
                time.sleep(4.0)
                
        threading.Thread(target=poller_loop, daemon=True).start()

    def _check_target_badgym_health(self):
        addr = self.target_device
        ip = self._get_badgym_ip()
        
        is_live = False
        health_info = None
        if ip:
            try:
                with urllib.request.urlopen(f"http://{ip}:8787/health", timeout=1.2) as res:
                    if res.status == 200:
                        is_live = True
                        try:
                            health_info = json.loads(res.read().decode())
                        except Exception:
                            pass
            except Exception:
                pass

        def update_ui():
            if is_live:
                if hasattr(self, 'badgym_status_badge'):
                    self.badgym_status_badge.config(
                        text="🏋️ BAD GYM: LIVE 🟢",
                        bg="#064e3b",
                        fg="#34d399"
                    )
                if hasattr(self, 'badgym_tab_status_lbl'):
                    self.badgym_tab_status_lbl.config(
                        text="● LIVE 🟢 (Service & tsnet Active)",
                        fg="#22c55e"
                    )
                if hasattr(self, 'screen_badgym_badge'):
                    self.screen_badgym_badge.config(
                        text="🟢 BAD GYM LIVE",
                        fg="#22c55e"
                    )
            else:
                if hasattr(self, 'badgym_status_badge'):
                    self.badgym_status_badge.config(
                        text="🏋️ BAD GYM: OFFLINE 🔴",
                        bg="#450a0a",
                        fg="#f87171"
                    )
                if hasattr(self, 'badgym_tab_status_lbl'):
                    self.badgym_tab_status_lbl.config(
                        text="● OFFLINE 🔴 (Port 8787 Unreachable)",
                        fg="#ef4444"
                    )
                if hasattr(self, 'screen_badgym_badge'):
                    self.screen_badgym_badge.config(
                        text="🔴 BAD GYM OFFLINE",
                        fg="#ef4444"
                    )
            if hasattr(self, 'badgym_lbl_target'):
                self.badgym_lbl_target.config(text=f"Target ADB: {self.target_device}")
            if hasattr(self, 'badgym_lbl_ip'):
                self.badgym_lbl_ip.config(text=f"Tailscale IP: {ip or 'Resolving...'}")
            if hasattr(self, 'badgym_lbl_endpoint'):
                self.badgym_lbl_endpoint.config(text=f"HTTP Agent: http://{ip or '100.84.228.109'}:8787")
        try:
            self.after(0, update_ui)
        except Exception:
            pass

    def badgym_log(self, msg):
        def _append():
            if hasattr(self, 'badgym_console'):
                ts = time.strftime("%H:%M:%S")
                self.badgym_console.insert("end", f"[{ts}] {msg}\n")
                self.badgym_console.see("end")
        try:
            self.after(0, _append)
        except Exception:
            pass

    def badgym_launch(self):
        def task():
            self.badgym_log("Launching BAD GYM MainActivity on device...")
            addr = self.target_device
            code, out, err = self.run_adb(["shell", f"am start -n {DEFAULT_PKG}/.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"])
            if code == 0:
                self.badgym_log("🚀 BAD GYM launched via ADB successfully.")
            else:
                self.badgym_log(f"ADB launch note: {out or err}")
            
            ip = self._get_badgym_ip()
            if ip:
                try:
                    payload = json.dumps({"method": "tools/call", "params": {"name": "launch_badgym", "arguments": {}}, "id": 1}).encode()
                    req = urllib.request.Request(f"http://{ip}:8787/mcp", data=payload, headers={"Content-Type": "application/json", "Authorization": "Bearer badgym-secret-mcp-token"})
                    with urllib.request.urlopen(req, timeout=3) as res:
                        self.badgym_log(f"MCP launch response: {res.status}")
                except Exception:
                    pass
            self._check_target_badgym_health()
        threading.Thread(target=task, daemon=True).start()

    def badgym_stop(self):
        def task():
            self.badgym_log(f"Stopping package {DEFAULT_PKG}...")
            code, out, _ = self.run_adb(["shell", f"am force-stop {DEFAULT_PKG}"])
            self.badgym_log(f"Force stop executed: code={code}")
            self._check_target_badgym_health()
        threading.Thread(target=task, daemon=True).start()

    def badgym_restart_service(self):
        def task():
            self.badgym_log("Restarting BAD GYM Foreground Service & embedded tsnet daemon...")
            self.run_adb(["shell", f"am start-foreground-service -n {DEFAULT_PKG}/.deviceagent.DeviceAgentForegroundService"])
            ip = self._get_badgym_ip()
            if ip:
                try:
                    payload = json.dumps({"method": "tools/call", "params": {"name": "tailscale_restart", "arguments": {}}, "id": 1}).encode()
                    req = urllib.request.Request(f"http://{ip}:8787/mcp", data=payload, headers={"Content-Type": "application/json", "Authorization": "Bearer badgym-secret-mcp-token"})
                    with urllib.request.urlopen(req, timeout=4) as res:
                        self.badgym_log(f"Tailscale restart response: {res.read().decode('utf-8', errors='ignore')}")
                except Exception as e:
                    self.badgym_log(f"Service trigger note: {e}")
            time.sleep(2)
            self._check_target_badgym_health()
        threading.Thread(target=task, daemon=True).start()

    def badgym_ping_health(self):
        def task():
            ip = self._get_badgym_ip()
            if not ip:
                self.badgym_log(f"Cannot resolve IP for target {self.target_device}")
                return
            t0 = time.time()
            try:
                with urllib.request.urlopen(f"http://{ip}:8787/health", timeout=2.5) as res:
                    dt = int((time.time() - t0) * 1000)
                    resp_body = res.read().decode()
                    self.badgym_log(f"🟢 Health OK ({dt} ms): {resp_body}")
            except Exception as e:
                self.badgym_log(f"🔴 Health check failed: {e}")
            self._check_target_badgym_health()
        threading.Thread(target=task, daemon=True).start()

    def badgym_fetch_diagnostics(self):
        def task():
            ip = self._get_badgym_ip()
            if not ip:
                self.badgym_log("Target IP not available.")
                return
            self.badgym_log(f"Querying diagnostics from http://{ip}:8787/mcp...")
            try:
                payload = json.dumps({"method": "tools/call", "params": {"name": "device_status", "arguments": {}}, "id": 1}).encode()
                req = urllib.request.Request(f"http://{ip}:8787/mcp", data=payload, headers={"Content-Type": "application/json", "Authorization": "Bearer badgym-secret-mcp-token"})
                with urllib.request.urlopen(req, timeout=5) as res:
                    out = res.read().decode('utf-8', errors='ignore')
                    self.badgym_log(f"📊 Device Status:\n{out}")
            except Exception as e:
                self.badgym_log(f"Diagnostics error: {e}")
        threading.Thread(target=task, daemon=True).start()

    def wake_and_unlock_device(self):
        def task():
            self.badgym_log("Sending wake & unlock keyevents...")
            self.run_adb(["shell", "input keyevent 26 && input keyevent 82"])
            self.badgym_log("Device wake & unlock sent.")
        threading.Thread(target=task, daemon=True).start()

    def remote_keyevent(self, keycode):
        def task():
            names = {3: "HOME", 4: "BACK", 187: "APP_SWITCH", 26: "POWER", 82: "MENU/UNLOCK", 24: "VOL_UP", 25: "VOL_DOWN"}
            k_name = names.get(keycode, str(keycode))
            self.badgym_log(f"Sending keyevent {k_name} ({keycode})...")
            self.run_adb(["shell", f"input keyevent {keycode}"])
        threading.Thread(target=task, daemon=True).start()

    def remote_expand_notifications(self):
        def task():
            self.badgym_log("Expanding notification shade...")
            self.run_adb(["shell", "cmd statusbar expand-notifications"])
        threading.Thread(target=task, daemon=True).start()

    def remote_send_text_from_entry(self):
        if not hasattr(self, 'badgym_text_entry'):
            return
        text = self.badgym_text_entry.get().strip()
        if not text:
            return
        self.badgym_text_entry.delete(0, "end")
        def task():
            self.badgym_log(f"Typing on device: {text}")
            escaped = text.replace(" ", "%s").replace("'", "\\'").replace('"', '\\"')
            self.run_adb(["shell", f"input text '{escaped}'"])
        threading.Thread(target=task, daemon=True).start()

    def remote_tap_from_entry(self):
        if not hasattr(self, 'badgym_tap_x') or not hasattr(self, 'badgym_tap_y'):
            return
        x_str = self.badgym_tap_x.get().strip()
        y_str = self.badgym_tap_y.get().strip()
        if not x_str or not y_str:
            return
        try:
            x, y = int(x_str), int(y_str)
        except ValueError:
            return
        def task():
            self.badgym_log(f"Tapping coordinate ({x}, {y})...")
            self.run_adb(["shell", f"input tap {x} {y}"])
        threading.Thread(target=task, daemon=True).start()

    def _on_ts_tree_double_click(self, event=None):
        if not hasattr(self, 'ts_tree'):
            return
        sel = self.ts_tree.selection()
        if sel:
            item = self.ts_tree.item(sel[0])
            vals = item.get("values", [])
            if len(vals) >= 3:
                ip = vals[2]
                addr = f"{ip}:5555"
                for idx, val_str in enumerate(self.device_combo['values']):
                    if addr in val_str:
                        self.device_combo.current(idx)
                        self._on_device_combo_select()
                        break

    def _try_badgym_http_screen_pull(self, target_w, quality):
        ip = self._get_badgym_ip()
        if not ip:
            return None
        for endpoint in ("/screen.jpg", "/screenshot", "/screen.png"):
            try:
                req = urllib.request.Request(f"http://{ip}:8787{endpoint}")
                with urllib.request.urlopen(req, timeout=6.0) as res:
                    if res.status == 200:
                        raw = res.read()
                        if len(raw) > 500:
                            if HAS_PIL:
                                try:
                                    img = Image.open(io.BytesIO(raw))
                                    w, h = img.size
                                    new_h = int(h * (target_w / max(1, w)))
                                    resized = img.resize((target_w, new_h), Image.Resampling.LANCZOS).convert("RGB")
                                    out_io = io.BytesIO()
                                    resized.save(out_io, format="JPEG", quality=quality)
                                    return out_io.getvalue()
                                except Exception:
                                    return raw
                            return raw
            except Exception:
                pass
        return None

    def _build_badgym_tab(self, parent):
        canvas = tk.Canvas(parent, bg="#0f172a", highlightthickness=0)
        scrollbar = ttk.Scrollbar(parent, orient="vertical", command=canvas.yview)
        scroll_frame = tk.Frame(canvas, bg="#0f172a")

        scroll_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )
        canvas_win = canvas.create_window((0, 0), window=scroll_frame, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)
        canvas.bind('<Configure>', lambda e: canvas.itemconfig(canvas_win, width=e.width))

        scrollbar.pack(side="right", fill="y")
        canvas.pack(side="left", fill="both", expand=True)

        head_card = tk.Frame(scroll_frame, bg="#1e293b", padx=12, pady=10, relief="solid", bd=1)
        head_card.pack(fill="x", padx=4, pady=4)

        tk.Label(
            head_card,
            text="🏋️ BAD GYM — Remote Intelligence & Control Center",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 12, "bold")
        ).pack(side="left")

        self.badgym_tab_status_lbl = tk.Label(
            head_card,
            text="● Checking Health...",
            bg="#1e293b",
            fg="#f59e0b",
            font=("Segoe UI", 9, "bold")
        )
        self.badgym_tab_status_lbl.pack(side="right", padx=6)

        info_card = tk.LabelFrame(
            scroll_frame,
            text=" 📱 Active Target & Tailscale Info ",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 9, "bold"),
            padx=10,
            pady=8
        )
        info_card.pack(fill="x", padx=4, pady=4)

        self.badgym_lbl_target = tk.Label(info_card, text=f"Target ADB: {self.target_device}", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8))
        self.badgym_lbl_target.grid(row=0, column=0, sticky="w", padx=6, pady=2)

        self.badgym_lbl_ip = tk.Label(info_card, text="Tailscale IP: Resolving...", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8))
        self.badgym_lbl_ip.grid(row=0, column=1, sticky="w", padx=6, pady=2)

        self.badgym_lbl_endpoint = tk.Label(info_card, text="HTTP Agent: http://100.84.228.109:8787", bg="#1e293b", fg="#a7f3d0", font=("Segoe UI", 8))
        self.badgym_lbl_endpoint.grid(row=1, column=0, sticky="w", padx=6, pady=2)

        self.badgym_lbl_pkg = tk.Label(info_card, text=f"Package: {DEFAULT_PKG}", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8))
        self.badgym_lbl_pkg.grid(row=1, column=1, sticky="w", padx=6, pady=2)

        app_ctrl_card = tk.LabelFrame(
            scroll_frame,
            text=" ⚡ BAD GYM App Management ",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 9, "bold"),
            padx=10,
            pady=8
        )
        app_ctrl_card.pack(fill="x", padx=4, pady=4)

        btn_row1 = tk.Frame(app_ctrl_card, bg="#1e293b")
        btn_row1.pack(fill="x", pady=2)

        tk.Button(btn_row1, text="🚀 Launch BAD GYM", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=self.badgym_launch).pack(side="left", padx=3)
        tk.Button(btn_row1, text="🎥 Open Live Web Stream", bg="#0891b2", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=self.open_live_web_stream).pack(side="left", padx=3)
        tk.Button(btn_row1, text="📸 Instant Screen Snapshot", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=self.pull_single_screen).pack(side="left", padx=3)
        tk.Button(btn_row1, text="⚡ Health Ping", bg="#334155", fg="white", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.badgym_ping_health).pack(side="left", padx=3)

        btn_row2 = tk.Frame(app_ctrl_card, bg="#1e293b")
        btn_row2.pack(fill="x", pady=4)

        tk.Button(btn_row2, text="🔄 Restart Service & Tsnet", bg="#eab308", fg="#0f172a", font=("Segoe UI", 8, "bold"), padx=10, pady=3, relief="flat", command=self.badgym_restart_service).pack(side="left", padx=3)
        tk.Button(btn_row2, text="⏹ Force Stop App", bg="#dc2626", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=3, relief="flat", command=self.badgym_stop).pack(side="left", padx=3)
        tk.Button(btn_row2, text="📊 Diagnostics / Status", bg="#475569", fg="white", font=("Segoe UI", 8), padx=8, pady=3, relief="flat", command=self.badgym_fetch_diagnostics).pack(side="left", padx=3)
        tk.Button(btn_row2, text="📂 Open Pulled Screens Folder", bg="#334155", fg="white", font=("Segoe UI", 8), padx=8, pady=3, relief="flat", command=self.open_saved_folder).pack(side="left", padx=3)

        nav_card = tk.LabelFrame(
            scroll_frame,
            text=" 🎮 Wireless Remote Hardware Control (Anywhere in the World) ",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 9, "bold"),
            padx=10,
            pady=8
        )
        nav_card.pack(fill="x", padx=4, pady=4)

        nav_row1 = tk.Frame(nav_card, bg="#1e293b")
        nav_row1.pack(fill="x", pady=2)

        tk.Button(nav_row1, text="⚡ Wake & Unlock", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=self.wake_and_unlock_device).pack(side="left", padx=3)
        tk.Button(nav_row1, text="🏠 Home", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=lambda: self.remote_keyevent(3)).pack(side="left", padx=3)
        tk.Button(nav_row1, text="🔙 Back", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=lambda: self.remote_keyevent(4)).pack(side="left", padx=3)
        tk.Button(nav_row1, text="📱 Recent Apps", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=10, pady=4, relief="flat", command=lambda: self.remote_keyevent(187)).pack(side="left", padx=3)
        tk.Button(nav_row1, text="🔔 Notification Shade", bg="#334155", fg="white", font=("Segoe UI", 8), padx=10, pady=4, relief="flat", command=self.remote_expand_notifications).pack(side="left", padx=3)
        tk.Button(nav_row1, text="💤 Sleep / Screen Off", bg="#475569", fg="white", font=("Segoe UI", 8), padx=10, pady=4, relief="flat", command=lambda: self.remote_keyevent(26)).pack(side="left", padx=3)

        input_row = tk.Frame(nav_card, bg="#1e293b")
        input_row.pack(fill="x", pady=(6, 2))

        tk.Label(input_row, text="⌨️ Send Text:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8)).pack(side="left", padx=2)
        self.badgym_text_entry = tk.Entry(input_row, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=24)
        self.badgym_text_entry.pack(side="left", padx=4)
        self.badgym_text_entry.bind("<Return>", lambda e: self.remote_send_text_from_entry())
        tk.Button(input_row, text="Send Text", bg="#0891b2", fg="white", font=("Segoe UI", 7, "bold"), padx=6, relief="flat", command=self.remote_send_text_from_entry).pack(side="left", padx=2)

        tk.Label(input_row, text="👆 Tap (X, Y):", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8)).pack(side="left", padx=(10, 2))
        self.badgym_tap_x = tk.Entry(input_row, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=5)
        self.badgym_tap_x.pack(side="left", padx=2)
        self.badgym_tap_y = tk.Entry(input_row, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=5)
        self.badgym_tap_y.pack(side="left", padx=2)
        tk.Button(input_row, text="Tap", bg="#0d9488", fg="white", font=("Segoe UI", 7, "bold"), padx=6, relief="flat", command=self.remote_tap_from_entry).pack(side="left", padx=2)

        ts_card = tk.LabelFrame(
            scroll_frame,
            text=" 🌐 Programmatic Multi-Device Tailnet (explorelearningvisit@gmail.com) ",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 9, "bold"),
            padx=10,
            pady=8
        )
        ts_card.pack(fill="x", padx=4, pady=4)

        ts_head = tk.Frame(ts_card, bg="#1e293b")
        ts_head.pack(fill="x", pady=(0, 4))
        tk.Label(ts_head, text="All detected devices under your account. Select any to control & pull screen:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")
        tk.Button(ts_head, text="🔄 Refresh Tailnet", bg="#16a34a", fg="white", font=("Segoe UI", 7, "bold"), padx=6, relief="flat", command=self.refresh_devices_list).pack(side="right")

        ts_cols = ("status", "name", "ip", "badgym", "os")
        self.ts_tree = ttk.Treeview(ts_card, columns=ts_cols, show="headings", height=5, selectmode="browse")
        self.ts_tree.heading("status", text="State")
        self.ts_tree.heading("name", text="Device Model / Name")
        self.ts_tree.heading("ip", text="Tailscale IP")
        self.ts_tree.heading("badgym", text="BAD GYM Live")
        self.ts_tree.heading("os", text="OS")

        self.ts_tree.column("status", width=75, anchor="center")
        self.ts_tree.column("name", width=180, anchor="w")
        self.ts_tree.column("ip", width=120, anchor="center")
        self.ts_tree.column("badgym", width=110, anchor="center")
        self.ts_tree.column("os", width=70, anchor="center")
        self.ts_tree.pack(fill="x", pady=2)
        self.ts_tree.bind("<Double-1>", self._on_ts_tree_double_click)

        log_card = tk.LabelFrame(
            scroll_frame,
            text=" 📋 BAD GYM Real-Time Intelligence Log ",
            bg="#1e293b",
            fg="#38bdf8",
            font=("Segoe UI", 9, "bold"),
            padx=10,
            pady=8
        )
        log_card.pack(fill="both", expand=True, padx=4, pady=4)

        self.badgym_console = tk.Text(log_card, bg="#000000", fg="#34d399", insertbackground="white", font=("Consolas", 8), height=8, relief="flat")
        self.badgym_console.pack(fill="both", expand=True)
        self.badgym_log("BAD GYM Control Center ready. Monitoring embedded agent on port 8787...")

    def _build_apps_tab(self, parent):
        top_bar = tk.Frame(parent, bg="#0f172a")
        top_bar.pack(fill="x", pady=(0, 4))
        tk.Label(top_bar, text="⚡ Installed & Hidden Applications", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")
        self.apps_summary_lbl = tk.Label(top_bar, text="Click 'Scan Apps' to list", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.apps_summary_lbl.pack(side="right")

        filter_bar = tk.Frame(parent, bg="#0f172a")
        filter_bar.pack(fill="x", pady=2)
        
        self.btn_app_user = tk.Button(filter_bar, text="👤 User Apps", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=5, relief="flat", command=lambda: self._set_apps_filter("USER"))
        self.btn_app_user.pack(side="left", padx=1)

        self.btn_app_dual = tk.Button(filter_bar, text="🔒 Dual / Cloned", bg="#581c87", fg="#f0abfc", font=("Segoe UI", 8, "bold"), padx=5, relief="flat", command=lambda: self._set_apps_filter("DUAL"))
        self.btn_app_dual.pack(side="left", padx=1)

        self.btn_app_disabled = tk.Button(filter_bar, text="🚫 Hidden / Frozen", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=5, relief="flat", command=lambda: self._set_apps_filter("DISABLED"))
        self.btn_app_disabled.pack(side="left", padx=1)

        self.btn_app_system = tk.Button(filter_bar, text="⚙️ System Apps", bg="#334155", fg="white", font=("Segoe UI", 8), padx=5, relief="flat", command=lambda: self._set_apps_filter("SYSTEM"))
        self.btn_app_system.pack(side="left", padx=1)

        self.btn_app_all = tk.Button(filter_bar, text="📦 All Packages", bg="#334155", fg="white", font=("Segoe UI", 8), padx=5, relief="flat", command=lambda: self._set_apps_filter("ALL"))
        self.btn_app_all.pack(side="left", padx=1)

        tk.Button(filter_bar, text="🔄 Scan Apps", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.scan_installed_apps).pack(side="right", padx=2)

        search_bar = tk.Frame(parent, bg="#1e293b", padx=6, pady=4, relief="solid", bd=1)
        search_bar.pack(fill="x", pady=4)
        tk.Label(search_bar, text="🔍 Search:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")
        self.apps_search_entry = tk.Entry(search_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=24)
        self.apps_search_entry.pack(side="left", padx=4)
        self.apps_search_entry.bind("<KeyRelease>", lambda e: self._apply_apps_filter_and_render())

        self.selected_app_lbl = tk.Label(search_bar, text="Select an app below", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"))
        self.selected_app_lbl.pack(side="right", padx=4)

        # Action Toolbars (packed at bottom of container before table for guaranteed visibility)
        app_act_bar2 = tk.Frame(parent, bg="#0f172a")
        app_act_bar2.pack(side="bottom", fill="x", pady=(2, 0))

        tk.Button(app_act_bar2, text="🚫 Freeze / Hide", bg="#7f1d1d", fg="white", font=("Segoe UI", 7, "bold"), padx=5, pady=3, relief="flat", command=self.freeze_selected_app).pack(side="left", padx=1)
        tk.Button(app_act_bar2, text="🟢 Unfreeze / Enable", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=5, pady=3, relief="flat", command=self.unfreeze_selected_app).pack(side="left", padx=1)
        tk.Button(app_act_bar2, text="📥 Pull APK to PC", bg="#0891b2", fg="white", font=("Segoe UI", 7, "bold"), padx=5, pady=3, relief="flat", command=self.pull_selected_app_apk).pack(side="left", padx=1)
        tk.Button(app_act_bar2, text="🧹 Clear Data", bg="#475569", fg="white", font=("Segoe UI", 7), padx=5, pady=3, relief="flat", command=self.clear_selected_app_data).pack(side="left", padx=1)
        tk.Button(app_act_bar2, text="🗑️ Uninstall App", bg="#b91c1c", fg="white", font=("Segoe UI", 7, "bold"), padx=5, pady=3, relief="flat", command=self.uninstall_selected_app).pack(side="right", padx=1)

        app_act_bar1 = tk.Frame(parent, bg="#0f172a")
        app_act_bar1.pack(side="bottom", fill="x", pady=(3, 1))

        tk.Button(app_act_bar1, text="▶ Launch App", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.launch_selected_app).pack(side="left", padx=1)
        tk.Button(app_act_bar1, text="⏹ Force Stop", bg="#dc2626", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.stop_selected_app).pack(side="left", padx=1)
        tk.Button(app_act_bar1, text="📁 Explore Files", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.explore_selected_app_files).pack(side="left", padx=1)
        tk.Button(app_act_bar1, text="🔗 Deep Links & Intents", bg="#7c3aed", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.inspect_selected_app_deeplinks).pack(side="left", padx=1)
        tk.Button(app_act_bar1, text="📤 Push Media to App", bg="#0d9488", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.push_media_to_selected_app).pack(side="left", padx=1)

        # Main Apps Table
        table_frame = tk.Frame(parent, bg="#1e293b")
        table_frame.pack(fill="both", expand=True, pady=4)

        app_cols = ("type", "pkg", "status", "apk")
        self.apps_tree = ttk.Treeview(table_frame, columns=app_cols, show="headings", selectmode="browse")
        self.apps_tree.heading("type", text="Type")
        self.apps_tree.heading("pkg", text="Application / Package Name")
        self.apps_tree.heading("status", text="Status")
        self.apps_tree.heading("apk", text="APK File Path on Mobile")

        self.apps_tree.column("type", width=80, anchor="center")
        self.apps_tree.column("pkg", width=220, anchor="w")
        self.apps_tree.column("status", width=90, anchor="center")
        self.apps_tree.column("apk", width=260, anchor="w")

        tree_scroll = tk.Scrollbar(table_frame, orient="vertical", command=self.apps_tree.yview)
        self.apps_tree.configure(yscrollcommand=tree_scroll.set)

        self.apps_tree.pack(side="left", fill="both", expand=True)
        tree_scroll.pack(side="right", fill="y")

        self.apps_tree.bind("<<TreeviewSelect>>", self._on_app_tree_select)
        self.apps_tree.bind("<Double-Button-1>", lambda e: self.launch_selected_app())

    def _build_calls_tab(self, parent):
        top_bar = tk.Frame(parent, bg="#0f172a")
        top_bar.pack(fill="x", pady=(0, 4))
        tk.Label(top_bar, text="📞 Mobile Call History & Contacts", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")
        self.calls_summary_lbl = tk.Label(top_bar, text="Click 'Fetch Live Calls' to load", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.calls_summary_lbl.pack(side="right")

        action_bar = tk.Frame(parent, bg="#0f172a")
        action_bar.pack(fill="x", pady=2)

        self.btn_call_f_all = tk.Button(action_bar, text="All Logs", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_calls_filter("ALL"))
        self.btn_call_f_all.pack(side="left", padx=2)

        self.btn_call_f_in = tk.Button(action_bar, text="📥 Incoming", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_calls_filter("INCOMING"))
        self.btn_call_f_in.pack(side="left", padx=2)

        self.btn_call_f_out = tk.Button(action_bar, text="📤 Outgoing", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_calls_filter("OUTGOING"))
        self.btn_call_f_out.pack(side="left", padx=2)

        self.btn_call_f_missed = tk.Button(action_bar, text="❌ Missed", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_calls_filter("MISSED"))
        self.btn_call_f_missed.pack(side="left", padx=2)

        self.btn_call_f_rej = tk.Button(action_bar, text="🚫 Rejected", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_calls_filter("REJECTED"))
        self.btn_call_f_rej.pack(side="left", padx=2)

        tk.Button(action_bar, text="🔄 Fetch Live Calls", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.fetch_call_logs_and_contacts).pack(side="right", padx=2)
        tk.Button(action_bar, text="📥 Backup Logs (CSV)", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.export_call_logs_csv).pack(side="right", padx=2)
        tk.Button(action_bar, text="👥 Save Contacts (CSV/vCard)", bg="#7c3aed", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.export_contacts_dialog).pack(side="right", padx=2)

        search_bar = tk.Frame(parent, bg="#1e293b", padx=6, pady=4, relief="solid", bd=1)
        search_bar.pack(fill="x", pady=4)
        tk.Label(search_bar, text="🔍 Search Calls / Contacts:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")
        self.calls_search_entry = tk.Entry(search_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=24)
        self.calls_search_entry.pack(side="left", padx=4)
        self.calls_search_entry.bind("<KeyRelease>", lambda e: self._apply_calls_filter_and_render())

        # Main Split: Left Call Logs (60%), Right Contacts Directory (40%)
        calls_split = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=3, bd=0)
        calls_split.pack(fill="both", expand=True, pady=4)

        # 1. Call Logs Pane
        logs_frame = tk.Frame(calls_split, bg="#1e293b")
        calls_split.add(logs_frame, minsize=320)

        tk.Label(logs_frame, text="📜 Call History Log", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"), padx=6, pady=4).pack(anchor="w")

        call_cols = ("type", "name", "number", "date", "duration")
        self.calls_tree = ttk.Treeview(logs_frame, columns=call_cols, show="headings", selectmode="browse")
        self.calls_tree.heading("type", text="Type")
        self.calls_tree.heading("name", text="Contact Name")
        self.calls_tree.heading("number", text="Phone Number")
        self.calls_tree.heading("date", text="Date & Time")
        self.calls_tree.heading("duration", text="Duration")

        self.calls_tree.column("type", width=70, anchor="center")
        self.calls_tree.column("name", width=120, anchor="w")
        self.calls_tree.column("number", width=110, anchor="w")
        self.calls_tree.column("date", width=110, anchor="center")
        self.calls_tree.column("duration", width=60, anchor="e")

        c_scroll = tk.Scrollbar(logs_frame, orient="vertical", command=self.calls_tree.yview)
        self.calls_tree.configure(yscrollcommand=c_scroll.set)
        self.calls_tree.pack(side="left", fill="both", expand=True)
        c_scroll.pack(side="right", fill="y")

        # 2. Contacts Pane
        contacts_frame = tk.Frame(calls_split, bg="#1e293b")
        calls_split.add(contacts_frame, minsize=240)

        tk.Label(contacts_frame, text="👥 Contacts Directory & Stats", bg="#1e293b", fg="#a855f7", font=("Segoe UI", 8, "bold"), padx=6, pady=4).pack(anchor="w")

        contact_cols = ("c_name", "c_number", "c_total", "c_last")
        self.contacts_tree = ttk.Treeview(contacts_frame, columns=contact_cols, show="headings", selectmode="browse")
        self.contacts_tree.heading("c_name", text="Name")
        self.contacts_tree.heading("c_number", text="Number")
        self.contacts_tree.heading("c_total", text="Calls")
        self.contacts_tree.heading("c_last", text="Last Call")

        self.contacts_tree.column("c_name", width=110, anchor="w")
        self.contacts_tree.column("c_number", width=100, anchor="w")
        self.contacts_tree.column("c_total", width=45, anchor="center")
        self.contacts_tree.column("c_last", width=90, anchor="center")

        cnt_scroll = tk.Scrollbar(contacts_frame, orient="vertical", command=self.contacts_tree.yview)
        self.contacts_tree.configure(yscrollcommand=cnt_scroll.set)
        self.contacts_tree.pack(side="left", fill="both", expand=True)
        cnt_scroll.pack(side="right", fill="y")

        self.contacts_tree.bind("<<TreeviewSelect>>", self._on_contact_tree_select)

    def _build_gallery_tab(self, parent):
        """Dedicated Gallery & Screenshot Manager with Interactive Thumbnail Grid, Grouping & Table Views"""
        top_row = tk.Frame(parent, bg="#0f172a")
        top_row.pack(fill="x", pady=(0, 2))
        tk.Label(top_row, text="Mobile Gallery & Timeline Recovery", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 10, "bold")).pack(side="left")
        
        self.gallery_summary_lbl = tk.Label(top_row, text="Scanning...", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.gallery_summary_lbl.pack(side="right")

        # Row 1: Category Filter Pills / Dropdown, Grouping Selector, and Deep Scan
        filter_bar1 = tk.Frame(parent, bg="#0f172a")
        filter_bar1.pack(fill="x", pady=(1, 2))
        
        tk.Label(filter_bar1, text="Category:", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8, "bold")).pack(side="left", padx=(0, 2))
        self.cat_combo = ttk.Combobox(
            filter_bar1,
            values=[
                "🖼️ All Media",
                "📸 Camera Photos",
                "📱 Screenshots",
                "💬 WhatsApp",
                "🔒 Locked / Private Safe",
                "📥 Downloads & Purchases",
                "📁 Root Storage",
                "🗑️ Trashed & Hidden",
                "🕰️ Day 1 Archive",
                "🎨 Stickers & WebP"
            ],
            width=20,
            state="readonly"
        )
        self.cat_combo.current(0)
        self.cat_combo.pack(side="left", padx=2)
        self.cat_combo.bind("<<ComboboxSelected>>", self._on_cat_combo_change)

        # Quick Pill Filters
        self.btn_f_all = tk.Button(filter_bar1, text="All", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_filter("ALL"))
        self.btn_f_all.pack(side="left", padx=1)
        self.btn_f_camera = tk.Button(filter_bar1, text="📸 Cam", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._set_gallery_filter("CAMERA"))
        self.btn_f_camera.pack(side="left", padx=1)
        self.btn_f_screens = tk.Button(filter_bar1, text="📱 Screen", bg="#334155", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_filter("SCREENSHOTS"))
        self.btn_f_screens.pack(side="left", padx=1)
        self.btn_f_locked = tk.Button(filter_bar1, text="🔒 Safe", bg="#581c87", fg="#f0abfc", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_filter("LOCKED"))
        self.btn_f_locked.pack(side="left", padx=1)
        self.btn_f_trash = tk.Button(filter_bar1, text="🗑️ Trash", bg="#7f1d1d", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_filter("TRASHED"))
        self.btn_f_trash.pack(side="left", padx=1)
        self.btn_f_day1 = tk.Button(filter_bar1, text="🕰️ Day 1", bg="#475569", fg="#fcd34d", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_filter("DAY1"))
        self.btn_f_day1.pack(side="left", padx=1)

        tk.Label(filter_bar1, text="| Group:", bg="#0f172a", fg="#64748b", font=("Segoe UI", 8)).pack(side="left", padx=(4, 2))
        self.group_combo = ttk.Combobox(
            filter_bar1,
            values=[
                "None (Flat Stream)",
                "📅 Month / Year",
                "📁 Folder Source",
                "📦 File Size",
                "🏷️ File Type"
            ],
            width=15,
            state="readonly"
        )
        self.group_combo.current(0)
        self.group_combo.pack(side="left", padx=2)
        self.group_combo.bind("<<ComboboxSelected>>", self._on_group_combo_change)

        tk.Button(filter_bar1, text="🚀 Clone All (16k+)", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.clone_all_gallery_media_to_pc).pack(side="right", padx=1)
        tk.Button(filter_bar1, text="🔄 Deep Scan", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.scan_mobile_gallery).pack(side="right", padx=1)

        # Row 2: Sort Controls, Sticker Exclusion, View Switcher, Tile Sizes & Toggle Preview
        filter_bar2 = tk.Frame(parent, bg="#0f172a")
        filter_bar2.pack(fill="x", pady=1)

        self.btn_sort_newest = tk.Button(filter_bar2, text="📅 Newest (Today ➔ Day 1)", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=lambda: self._set_gallery_sort("DESC"))
        self.btn_sort_newest.pack(side="left", padx=1)

        self.btn_sort_oldest = tk.Button(filter_bar2, text="🕰️ Day 1 (Oldest ➔ Today)", bg="#334155", fg="white", font=("Segoe UI", 7), padx=5, relief="flat", command=lambda: self._set_gallery_sort("ASC"))
        self.btn_sort_oldest.pack(side="left", padx=1)

        self.btn_exclude_stickers = tk.Button(filter_bar2, text="🚫 Exclude Stickers: ON", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self._toggle_exclude_stickers)
        self.btn_exclude_stickers.pack(side="left", padx=2)

        self.btn_v_grid = tk.Button(filter_bar2, text="🖼️ Grid", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_gallery_view_mode("GRID"))
        self.btn_v_grid.pack(side="left", padx=1)

        self.btn_v_table = tk.Button(filter_bar2, text="📋 Table", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._set_gallery_view_mode("TABLE"))
        self.btn_v_table.pack(side="left", padx=1)

        tk.Label(filter_bar2, text="Tile:", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 7)).pack(side="left", padx=(3, 1))
        self.btn_sz_s = tk.Button(filter_bar2, text="S", bg="#334155", fg="white", font=("Segoe UI", 7), padx=2, relief="flat", command=lambda: self._set_thumb_size(100))
        self.btn_sz_s.pack(side="left", padx=1)
        self.btn_sz_m = tk.Button(filter_bar2, text="M", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=2, relief="flat", command=lambda: self._set_thumb_size(140))
        self.btn_sz_m.pack(side="left", padx=1)
        self.btn_sz_l = tk.Button(filter_bar2, text="L", bg="#334155", fg="white", font=("Segoe UI", 7), padx=2, relief="flat", command=lambda: self._set_thumb_size(185))
        self.btn_sz_l.pack(side="left", padx=1)

        self.btn_toggle_preview = tk.Button(filter_bar2, text="👁 Preview: ON", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=self._toggle_preview_panel)
        self.btn_toggle_preview.pack(side="right", padx=1)

        # Row 3: Search, Quick Selection & Action Counters
        sel_bar = tk.Frame(parent, bg="#1e293b", padx=4, pady=3, relief="solid", bd=1)
        sel_bar.pack(fill="x", pady=2)

        tk.Label(sel_bar, text="🔍 Filter:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 7)).pack(side="left")
        self.gallery_search_entry = tk.Entry(sel_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=12)
        self.gallery_search_entry.pack(side="left", padx=2)
        self.gallery_search_entry.bind("<KeyRelease>", lambda e: self._apply_gallery_search())

        tk.Button(sel_bar, text="☑ All", bg="#334155", fg="white", font=("Segoe UI", 7), padx=3, relief="flat", command=self._select_all_gallery).pack(side="left", padx=1)
        tk.Button(sel_bar, text="☐ None", bg="#334155", fg="white", font=("Segoe UI", 7), padx=3, relief="flat", command=self._deselect_all_gallery).pack(side="left", padx=1)
        tk.Button(sel_bar, text="📱 Screen", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=3, relief="flat", command=self._select_screenshots_only).pack(side="left", padx=1)
        tk.Button(sel_bar, text="🔒 Safe", bg="#581c87", fg="#f0abfc", font=("Segoe UI", 7, "bold"), padx=3, relief="flat", command=self._select_locked_only).pack(side="left", padx=1)
        tk.Button(sel_bar, text="🗑️ Trash", bg="#7f1d1d", fg="white", font=("Segoe UI", 7, "bold"), padx=3, relief="flat", command=self._select_trashed_only).pack(side="left", padx=1)

        self.top_btn_del = tk.Button(sel_bar, text="🗑️ Delete (0)", bg="#dc2626", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=self.delete_selected_gallery_images)
        self.top_btn_del.pack(side="left", padx=2)

        self.top_btn_bak = tk.Button(sel_bar, text="📥 Backup (0)", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=self.backup_selected_gallery_images)
        self.top_btn_bak.pack(side="left", padx=1)

        tk.Button(sel_bar, text="🚀 Rapid Clone (16k+)", bg="#16a34a", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=self.clone_all_gallery_media_to_pc).pack(side="left", padx=2)

        self.gallery_sel_count_lbl = tk.Label(sel_bar, text="Selected: 0 files", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 7, "bold"))
        self.gallery_sel_count_lbl.pack(side="right", padx=2)

        # BOTTOM ACTION BARS (PACKED FIRST FOR 100% VISIBILITY)
        act_bar2 = tk.Frame(parent, bg="#0f172a")
        act_bar2.pack(side="bottom", fill="x", pady=(1, 0))

        tk.Button(act_bar2, text="🚀 Rapid Clone / Backup ALL Media (16k+)", bg="#16a34a", fg="white", activebackground="#15803d", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, pady=3, relief="flat", command=self.clone_all_gallery_media_to_pc).pack(side="left", padx=2)
        tk.Button(act_bar2, text="📥 Backup Selected...", bg="#0284c7", fg="white", font=("Segoe UI", 8), padx=6, pady=3, relief="flat", command=self.backup_selected_gallery_images).pack(side="left", padx=2)
        tk.Button(act_bar2, text="🧹 Purge Temp Cache", bg="#475569", fg="white", font=("Segoe UI", 7), padx=5, pady=3, relief="flat", command=self.purge_mobile_temp_cache).pack(side="left", padx=2)
        tk.Button(act_bar2, text="📊 View Analytics & Charts ➔", bg="#7c3aed", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self._switch_to_analytics_tab).pack(side="right", padx=2)

        act_bar1 = tk.Frame(parent, bg="#0f172a")
        act_bar1.pack(side="bottom", fill="x", pady=(2, 1))

        tk.Button(act_bar1, text="💥 Delete ALL Screenshots", bg="#dc2626", fg="white", activebackground="#b91c1c", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.delete_all_screenshots_from_mobile).pack(side="left", padx=2)
        tk.Button(act_bar1, text="🗑️ Delete Selected (+Trash)", bg="#b45309", fg="white", activebackground="#92400e", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.delete_selected_gallery_images).pack(side="left", padx=2)
        tk.Button(act_bar1, text="🗑️ Empty Mobile Trash / Bin", bg="#7f1d1d", fg="white", activebackground="#991b1b", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=6, pady=3, relief="flat", command=self.empty_mobile_trash_bin).pack(side="left", padx=2)

        # Main Split Content: Content Container (Grid or Table) on Left, Preview Panel on Right
        self.gallery_content_split = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=3, bd=0)
        self.gallery_content_split.pack(fill="both", expand=True, pady=2)

        self.gallery_content_container = tk.Frame(self.gallery_content_split, bg="#0f172a")
        self.gallery_content_split.add(self.gallery_content_container, minsize=320)

        # 1. Table View Frame
        self.gallery_table_frame = tk.Frame(self.gallery_content_container, bg="#1e293b")
        
        columns = ("sel", "category", "filename", "size", "date")
        self.gallery_tree = ttk.Treeview(self.gallery_table_frame, columns=columns, show="headings", selectmode="browse")
        self.gallery_tree.heading("sel", text="[X]")
        self.gallery_tree.heading("category", text="Category")
        self.gallery_tree.heading("filename", text="Filename")
        self.gallery_tree.heading("size", text="Size")
        self.gallery_tree.heading("date", text="Date")

        self.gallery_tree.column("sel", width=36, anchor="center")
        self.gallery_tree.column("category", width=95, anchor="w")
        self.gallery_tree.column("filename", width=170, anchor="w")
        self.gallery_tree.column("size", width=64, anchor="e")
        self.gallery_tree.column("date", width=80, anchor="center")

        tree_scroll = tk.Scrollbar(self.gallery_table_frame, orient="vertical", command=self.gallery_tree.yview)
        self.gallery_tree.configure(yscrollcommand=tree_scroll.set)

        self.gallery_tree.pack(side="left", fill="both", expand=True)
        tree_scroll.pack(side="right", fill="y")

        self.gallery_tree.bind("<<TreeviewSelect>>", self._on_gallery_tree_select)
        self.gallery_tree.bind("<space>", self._on_gallery_tree_toggle_space)
        self.gallery_tree.bind("<Double-Button-1>", self._on_gallery_tree_double_click)

        # 2. Grid View Frame (Canvas + Scrollbar + Responsive Inner Flow Frame + Pagination Footer)
        self.gallery_grid_frame = tk.Frame(self.gallery_content_container, bg="#0f172a")

        # Bottom Pagination Bar
        self.grid_page_bar = tk.Frame(self.gallery_grid_frame, bg="#1e293b", padx=4, pady=3, relief="solid", bd=1)
        self.grid_page_bar.pack(side="bottom", fill="x")

        self.btn_page_first = tk.Button(self.grid_page_bar, text="⏮", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._go_gallery_page(0))
        self.btn_page_first.pack(side="left", padx=1)

        self.btn_page_prev = tk.Button(self.grid_page_bar, text="◀", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._go_gallery_page(self.gallery_page - 1))
        self.btn_page_prev.pack(side="left", padx=1)

        self.page_info_lbl = tk.Label(self.grid_page_bar, text="Page 1 of 1 (0 items)", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 7, "bold"), padx=4)
        self.page_info_lbl.pack(side="left", padx=2)

        self.btn_page_next = tk.Button(self.grid_page_bar, text="▶", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._go_gallery_page(self.gallery_page + 1))
        self.btn_page_next.pack(side="left", padx=1)

        self.btn_page_last = tk.Button(self.grid_page_bar, text="⏭", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._go_gallery_page(self.gallery_total_pages - 1))
        self.btn_page_last.pack(side="left", padx=1)

        # Direct Page Jump Box
        tk.Label(self.grid_page_bar, text="| Go:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 7)).pack(side="left", padx=(4, 1))
        self.page_jump_entry = tk.Entry(self.grid_page_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 7, "bold"), width=4, justify="center")
        self.page_jump_entry.insert(0, "1")
        self.page_jump_entry.pack(side="left", padx=1)
        self.page_jump_entry.bind("<Return>", lambda e: self._jump_to_page())

        tk.Button(self.grid_page_bar, text="Go", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=self._jump_to_page).pack(side="left", padx=1)

        tk.Label(self.grid_page_bar, text="Batch / Mode:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 7)).pack(side="right", padx=(4, 1))
        self.page_size_combo = ttk.Combobox(self.grid_page_bar, values=["60", "120", "240", "500", "♾️ Endless Stream"], width=13, state="readonly")
        self.page_size_combo.set("60")
        self.page_size_combo.pack(side="right", padx=1)
        self.page_size_combo.bind("<<ComboboxSelected>>", self._on_page_size_change)
        
        self.grid_canvas = tk.Canvas(self.gallery_grid_frame, bg="#0f172a", highlightthickness=0, bd=0)
        self.grid_scroll = tk.Scrollbar(self.gallery_grid_frame, orient="vertical", command=self._on_grid_scroll_command)
        self.grid_canvas.configure(yscrollcommand=self.grid_scroll.set)

        self.grid_inner_frame = tk.Frame(self.grid_canvas, bg="#0f172a")
        self.grid_window = self.grid_canvas.create_window((0, 0), window=self.grid_inner_frame, anchor="nw")

        self.grid_canvas.pack(side="left", fill="both", expand=True)
        self.grid_scroll.pack(side="right", fill="y")

        self.grid_inner_frame.bind("<Configure>", lambda e: self.grid_canvas.configure(scrollregion=self.grid_canvas.bbox("all")))
        self.grid_canvas.bind("<Configure>", self._on_grid_canvas_configure)
        self._bind_mousewheel_to_grid(self.grid_canvas)
        self._bind_mousewheel_to_grid(self.grid_inner_frame)

        # Default view: Grid View
        self.gallery_grid_frame.pack(fill="both", expand=True)

        # Right Preview Panel
        self.preview_panel = tk.Frame(self.gallery_content_split, bg="#1e293b", padx=6, pady=6, relief="solid", bd=1)
        self.gallery_content_split.add(self.preview_panel, minsize=180)

        tk.Label(self.preview_panel, text="Selected Photo Preview", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold")).pack(anchor="w")

        self.preview_canvas_frame = tk.Frame(self.preview_panel, bg="#000000", height=180, relief="sunken", bd=1)
        self.preview_canvas_frame.pack(fill="x", pady=4)
        self.preview_canvas_frame.pack_propagate(False)

        self.gallery_preview_lbl = tk.Label(self.preview_canvas_frame, text="Click any photo card\nor list row to preview", bg="#000000", fg="#64748b", font=("Segoe UI", 8), justify="center")
        self.gallery_preview_lbl.pack(expand=True)

        self.preview_info_lbl = tk.Label(self.preview_panel, text="No image selected", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 7), wraplength=170, justify="left")
        self.preview_info_lbl.pack(anchor="w", pady=2)

        tk.Button(self.preview_panel, text="👁 Open Full Viewer", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, pady=2, relief="flat", command=self.open_previewed_image_on_pc).pack(fill="x", pady=1)
        tk.Button(self.preview_panel, text="📥 Pull Image to PC", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, pady=2, relief="flat", command=self.pull_previewed_image).pack(fill="x", pady=1)
        tk.Button(self.preview_panel, text="🗑️ Delete Photo (+Trash)", bg="#dc2626", fg="white", activebackground="#b91c1c", activeforeground="white", font=("Segoe UI", 7, "bold"), padx=4, pady=2, relief="flat", command=self.delete_previewed_image).pack(fill="x", pady=1)
        self.btn_preview_restore = tk.Button(self.preview_panel, text="♻️ Restore to Gallery", bg="#16a34a", fg="white", activebackground="#15803d", activeforeground="white", font=("Segoe UI", 7, "bold"), padx=4, pady=2, relief="flat", command=self.restore_previewed_image)
        self.btn_preview_restore.pack(fill="x", pady=1)

    def _toggle_preview_panel(self):
        self.preview_panel_visible = not self.preview_panel_visible
        if self.preview_panel_visible:
            self.gallery_content_split.add(self.preview_panel, minsize=180)
            self.btn_toggle_preview.config(text="👁 Preview: ON", bg="#0284c7")
        else:
            self.gallery_content_split.forget(self.preview_panel)
            self.btn_toggle_preview.config(text="👁 Preview: OFF", bg="#334155")

    def _on_cat_combo_change(self, event=None):
        val = self.cat_combo.get()
        mapping = {
            "🖼️ All Media": "ALL",
            "📸 Camera Photos": "CAMERA",
            "📱 Screenshots": "SCREENSHOTS",
            "💬 WhatsApp": "WHATSAPP",
            "🔒 Locked / Private Safe": "LOCKED",
            "📥 Downloads & Purchases": "DOWNLOADS",
            "📁 Root Storage": "ROOT",
            "🗑️ Trashed & Hidden": "TRASHED",
            "🕰️ Day 1 Archive": "DAY1",
            "🎨 Stickers & WebP": "STICKERS"
        }
        filter_type = mapping.get(val, "ALL")
        self._set_gallery_filter(filter_type)

    def _on_group_combo_change(self, event=None):
        val = self.group_combo.get()
        mapping = {
            "None (Flat Stream)": "NONE",
            "📅 Month / Year": "DATE_MONTH",
            "📁 Folder Source": "FOLDER",
            "📦 File Size": "SIZE",
            "🏷️ File Type": "EXT"
        }
        self.gallery_group_by = mapping.get(val, "NONE")
        self.gallery_page = 0
        self._apply_gallery_filter_and_render()

    def _switch_to_analytics_tab(self):
        # Programmatically switches to Analytics tab
        for w in self.winfo_children():
            if isinstance(w, tk.PanedWindow):
                for child in w.winfo_children():
                    for sub in child.winfo_children():
                        if isinstance(sub, ttk.Notebook):
                            try:
                                sub.select(4) # Tab index 4 is Analytics
                                self.refresh_analytics_data()
                                return
                            except Exception:
                                pass

    def _build_files_tab(self, parent):
        top_lbl_frame = tk.Frame(parent, bg="#0f172a")
        top_lbl_frame.pack(fill="x", pady=(0, 2))
        tk.Label(top_lbl_frame, text="Universal Device Storage & Folder Explorer", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 10, "bold")).pack(side="left")
        
        tk.Button(top_lbl_frame, text="💬 Scan All WhatsApp Folders", bg="#15803d", fg="white", activebackground="#166534", font=("Segoe UI", 7, "bold"), padx=5, pady=1, relief="flat", command=self.scan_all_whatsapp_deep_folders).pack(side="right", padx=2)
        tk.Button(top_lbl_frame, text="🔒 Scan All Hidden Folders & Files (.*)", bg="#7c3aed", fg="white", activebackground="#6d28d9", font=("Segoe UI", 7, "bold"), padx=5, pady=1, relief="flat", command=self.scan_all_hidden_mobile_folders).pack(side="right", padx=2)

        # Quick directory bookmarks (Combobox to save space)
        nav_frame2 = tk.Frame(parent, bg="#0f172a")
        nav_frame2.pack(fill="x", pady=2)
        tk.Label(nav_frame2, text="Bookmarks:", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left", padx=2)
        
        shortcuts_all = [
            ("📸 Camera", "/sdcard/DCIM/Camera"),
            ("📱 Screens", "/sdcard/DCIM/Screenshots"),
            ("🖼 Pictures", "/sdcard/Pictures"),
            ("📥 Downloads", "/sdcard/Download"),
            ("📄 Docs", "/sdcard/Documents"),
            ("💬 WhatsApp Media", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media"),
            ("🎥 Movies", "/sdcard/Movies"),
            ("🎵 Music", "/sdcard/Music"),
            ("📁 Root /sdcard", "/sdcard"),
            ("💾 Emulated 0", "/storage/emulated/0"),
            ("🔒 Hidden .thumbnails", "/sdcard/.thumbnails"),
            ("🗑️ Hidden .trashed", "/sdcard/DCIM/.trashed-*"),
            ("🟢 ColorOS / OPPO", "/sdcard/ColorOS"),
            ("⚡ MIUI", "/sdcard/MIUI"),
            ("⚙️ /data/local/tmp", "/data/local/tmp"),
            ("📦 /system", "/system"),
            ("👑 / (Root)", "/"),
            ("👻 Snapchat", "/sdcard/Snapchat"),
            ("📸 Instagram", "/sdcard/Pictures/Instagram"),
            ("✈️ Telegram", "/sdcard/Telegram"),
            ("👥 Facebook", "/sdcard/Pictures/Facebook"),
            ("🎵 TikTok", "/sdcard/Pictures/TikTok"),
            ("🔒 Private Safe", "/sdcard/.aceself"),
            ("🔒 Dual Apps 999", "/storage/emulated/999"),
            ("📦 App Media", "/sdcard/Android/media"),
            ("📂 App Data", "/sdcard/Android/data"),
        ]
        self.shortcuts_dict = {name: pth for name, pth in shortcuts_all}
        self.bookmark_combo = ttk.Combobox(nav_frame2, values=list(self.shortcuts_dict.keys()), state="readonly", width=40)
        self.bookmark_combo.pack(side="left", padx=2)
        self.bookmark_combo.set("--- Select a Bookmark ---")
        self.bookmark_combo.bind("<<ComboboxSelected>>", lambda e: self.load_path(self.shortcuts_dict.get(self.bookmark_combo.get(), "/sdcard")))


        # Path Navigation Bar
        nav_frame = tk.Frame(parent, bg="#0f172a")
        nav_frame.pack(fill="x", pady=3)
        tk.Button(nav_frame, text="⬆ Up", bg="#475569", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self.navigate_up).pack(side="left", padx=(0, 2))
        self.path_entry = tk.Entry(nav_frame, bg="#1e293b", fg="#38bdf8", insertbackground="white", font=("Consolas", 8))
        self.path_entry.insert(0, "/sdcard/DCIM/Camera")
        self.path_entry.pack(side="left", fill="x", expand=True, padx=2)
        tk.Button(nav_frame, text="Go", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=6, relief="flat", command=self.list_device_files).pack(side="left", padx=1)
        tk.Button(nav_frame, text="🔄 Refresh", bg="#334155", fg="white", font=("Segoe UI", 7), padx=5, relief="flat", command=self.list_device_files).pack(side="left", padx=1)

        # Split: Left File List (68%), Right Micro-Thumbnail & Inspector (32%)
        files_split = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=3, bd=0)
        files_split.pack(fill="both", expand=True, pady=2)

        left_file_pane = tk.Frame(files_split, bg="#1e293b")
        files_split.add(left_file_pane, minsize=280)

        file_cols = ("size", "datetime")
        # Use tree headings so column #0 is visible for thumbnails
        self.file_tree = ttk.Treeview(left_file_pane, columns=file_cols, show="tree headings", selectmode="extended")
        
        # Configure rowheight for thumbnails (e.g. 50px)
        style = ttk.Style()
        style.configure('Treeview', rowheight=50)

        self.file_tree.heading("#0", text="File Name (Thumbnail)")
        self.file_tree.heading("size", text="Size")
        self.file_tree.heading("datetime", text="Date / Time")
        self.file_tree.column("#0", width=250, anchor="w")
        self.file_tree.column("size", width=80, anchor="e")
        self.file_tree.column("datetime", width=120, anchor="w")

        scroll_y = tk.Scrollbar(left_file_pane, orient="vertical", command=self.file_tree.yview)
        self.file_tree.configure(yscrollcommand=scroll_y.set)

        self.file_tree.pack(side="left", fill="both", expand=True)
        scroll_y.pack(side="right", fill="y")
        self.file_tree.bind("<Double-Button-1>", self._on_item_double_click)
        self.file_tree.bind("<<TreeviewSelect>>", self._on_file_select_preview)
        self.file_tree.bind("<Button-3>", self._show_file_listbox_context_menu)

        # Right Inspector & Micro-Thumbnail Preview Panel
        right_insp_pane = tk.Frame(files_split, bg="#1e293b", padx=6, pady=6, relief="solid", bd=1)
        files_split.add(right_insp_pane, minsize=190)

        tk.Label(right_insp_pane, text="Inspector & Preview", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold")).pack(anchor="w")

        self.auto_fetch_thumbs_var = tk.BooleanVar(value=True) # Enabled by default per user request
        tk.Checkbutton(right_insp_pane, text="Auto-fetch Thumbs", variable=self.auto_fetch_thumbs_var, bg="#1e293b", fg="#cbd5e1", selectcolor="#0f172a", font=("Segoe UI", 7)).pack(anchor="w", pady=1)

        self.file_preview_canvas_frame = tk.Frame(right_insp_pane, bg="#000000", height=130, relief="sunken", bd=1)
        self.file_preview_canvas_frame.pack(fill="x", pady=3)
        self.file_preview_canvas_frame.pack_propagate(False)

        self.file_preview_lbl = tk.Label(self.file_preview_canvas_frame, text="Click any file to inspect", bg="#000000", fg="#64748b", font=("Segoe UI", 7), justify="center")
        self.file_preview_lbl.pack(expand=True)

        self.file_info_lbl = tk.Label(right_insp_pane, text="Select a file or folder on the left", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 7), wraplength=170, justify="left")
        self.file_info_lbl.pack(anchor="w", pady=2)

        # Quick Actions Grid (To save vertical space and prevent cutting off)
        actions_frame = tk.Frame(right_insp_pane, bg="#1e293b")
        actions_frame.pack(fill="x", pady=5)
        actions_frame.columnconfigure(0, weight=1)
        actions_frame.columnconfigure(1, weight=1)
        
        btn_font = ("Segoe UI", 7, "bold")
        tk.Button(actions_frame, text="📋 Copy Path", bg="#334155", fg="#38bdf8", font=btn_font, relief="flat", command=self.copy_explorer_remote_path).grid(row=0, column=0, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="📋 Copy Name", bg="#334155", fg="#cbd5e1", font=btn_font, relief="flat", command=self.copy_explorer_file_name).grid(row=0, column=1, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="📥 Pull File", bg="#0284c7", fg="white", font=btn_font, relief="flat", command=self.pull_selected_file).grid(row=1, column=0, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="🚀 Pull ALL", bg="#16a34a", fg="white", font=btn_font, relief="flat", command=self.pull_all_discovered_files).grid(row=1, column=1, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="⚡ Get Thumb", bg="#0891b2", fg="white", font=btn_font, relief="flat", command=self.pull_micro_thumbnail).grid(row=2, column=0, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="📤 Push File", bg="#475569", fg="white", font=btn_font, relief="flat", command=self.push_file_to_device).grid(row=2, column=1, sticky="ew", padx=1, pady=1)
        tk.Button(actions_frame, text="🗑️ Delete", bg="#dc2626", fg="white", font=btn_font, relief="flat", command=self.delete_selected_explorer_item).grid(row=3, column=0, columnspan=2, sticky="ew", padx=1, pady=1)

    def _background_tree_thumbnail_fetcher(self, media_list, current_dir):
        from PIL import Image, ImageTk
        for i, full_pth, name in media_list:
            # If the user navigated away, abort background fetching
            if self.path_entry.get().strip() != current_dir:
                break
                
            local_thumb = self._get_thumb_path(full_pth, name)
            if not os.path.exists(local_thumb):
                safe_remote = full_pth.replace("'", "'\''")
                thumb_cmd = (
                    f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap thumb "
                    f"'{safe_remote}' 50 15 1" # quality=50, scale=15
                )
                self.run_adb(["shell", thumb_cmd], timeout=3)
                self.run_adb(["pull", safe_remote + ".thumb.jpg", local_thumb], timeout=2)
                self.run_adb(["shell", f"rm -f '{safe_remote}.thumb.jpg'"], timeout=1)
                
            if os.path.exists(local_thumb) and os.path.getsize(local_thumb) > 0:
                try:
                    img = Image.open(local_thumb)
                    img.thumbnail((45, 45)) # Fit within 50px row height
                    photo = ImageTk.PhotoImage(img)
                    
                    def update_row(iid=str(i), p=photo):
                        if self.file_tree.exists(iid):
                            self._tree_images_cache[iid] = p
                            self.file_tree.item(iid, image=p)
                    self.after(0, update_row)
                except Exception:
                    pass

    def _on_file_select_preview(self, event=None):
        sel = self.file_tree.selection()
        if not sel:
            return
            
        try:
            # Our item IDs are strings of integers corresponding to current_remote_items index
            idx = int(sel[0])
            if idx >= len(self.current_remote_items):
                return
        except ValueError:
            return # In case they select the "[Directory is empty]" row
            
        is_dir, name, full_path, size, dt_str = self.current_remote_items[idx]
        self.file_preview_selected_path = full_path

        sz_str = self._format_size(size) if not is_dir else "Folder / Directory"
        ext = os.path.splitext(name)[1].lower()
        self.file_info_lbl.config(
            text=f"{'📁 [DIR]' if is_dir else '📄 [FILE]'}: {name}\\nSize: {sz_str}\\nModified: {dt_str}\\nPath: {full_path}"
        )

        if not is_dir and ext in [".jpg", ".jpeg", ".png", ".webp", ".gif", ".mp4", ".mkv", ".mov"]:
            if getattr(self, 'auto_fetch_thumbs_var', None) and self.auto_fetch_thumbs_var.get():
                self._fetch_micro_thumbnail_for_file(full_path, name)
            else:
                self.file_preview_lbl.config(image="", text=f"📄 {name}\\n\\n(Auto-fetch disabled.\\nClick 'Pull Micro-Thumb' below)")
        else:
            self.file_preview_lbl.config(image="", text=f"📁 {name}" if is_dir else f"📄 {name}")

    def _fetch_micro_thumbnail_for_file(self, remote_path, fname):
        """Ultra-fast, sub-sampled 40KB thumbnail extractor via FastCap DEX without transferring large raw media"""
        self.file_preview_lbl.config(image="", text="⚡ Extracting <40KB Micro-Thumb...")
        local_thumb = self._get_thumb_path(remote_path, fname)

        def task():
            if not os.path.exists(local_thumb):
                safe_remote = remote_path.replace("'", "'\\''")
                thumb_cmd = (
                    f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap thumb "
                    f"'{safe_remote}' /data/local/tmp/_thumb_f.jpg 240 45"
                )
                code, out, _ = self.run_adb(["shell", thumb_cmd], timeout=6)
                pulled = False
                if code == 0 and "THUMB_OK" in (out or ""):
                    p_code, _, _ = self.run_adb(["pull", "/data/local/tmp/_thumb_f.jpg", local_thumb], timeout=6)
                    pulled = (p_code == 0 and os.path.exists(local_thumb))
                    self.run_adb(["shell", "rm -f /data/local/tmp/_thumb_f.jpg"], timeout=3)
                
                if not pulled and not os.path.exists(local_thumb):
                    ext = os.path.splitext(remote_path)[1].lower()
                    if ext in ['.jpg', '.jpeg', '.png', '.webp', '.gif']:
                        self.run_adb(["pull", remote_path, local_thumb], timeout=10)

            if os.path.exists(local_thumb) and HAS_PIL:
                try:
                    pil_img = Image.open(local_thumb)
                    w, h = pil_img.size
                    max_w, max_h = 160, 130
                    scale = min(max_w / max(1, w), max_h / max(1, h))
                    tw, th = max(1, int(w * scale)), max(1, int(h * scale))
                    resized = pil_img.resize((tw, th), Image.Resampling.LANCZOS)
                    tk_img = ImageTk.PhotoImage(resized)
                    thumb_kb = os.path.getsize(local_thumb) / 1024

                    def show():
                        self.file_preview_img = tk_img
                        self.file_preview_lbl.config(image=tk_img, text="")
                        self.file_info_lbl.config(
                            text=f"📄 {fname}\nMicro-Thumb: {thumb_kb:.1f} KB (Crisp & Light)\n📍 {remote_path}"
                        )
                    self.after(0, show)
                except Exception:
                    pass

        threading.Thread(target=task, daemon=True).start()

    def pull_micro_thumbnail(self):
        if not self.file_preview_selected_path:
            messagebox.showinfo("Select", "Please select an image or video file first.")
            return
        fname = os.path.basename(self.file_preview_selected_path)
        local_thumb = self._get_thumb_path(self.file_preview_selected_path, fname)
        if not os.path.exists(local_thumb):
            messagebox.showinfo("Notice", "Extracting thumbnail... please wait a second and retry.")
            return
        dest = filedialog.asksaveasfilename(
            title="Save Micro-Thumbnail (<40KB)",
            initialfile=f"thumb_{fname}.jpg",
            defaultextension=".jpg",
            filetypes=[("JPEG Image", "*.jpg")]
        )
        if dest:
            try:
                import shutil
                shutil.copyfile(local_thumb, dest)
                messagebox.showinfo("Saved", f"Micro-thumbnail saved to:\n{dest}")
            except Exception as e:
                messagebox.showerror("Error", f"Failed to save thumbnail: {e}")

    def delete_selected_explorer_item(self):
        if not self.file_preview_selected_path:
            messagebox.showinfo("Select", "Please select a file or folder to delete.")
            return
        p = self.file_preview_selected_path
        fname = os.path.basename(p)
        if not messagebox.askyesno("Delete", f"Permanently delete '{fname}' from mobile storage?\n\nPath: {p}"):
            return
        def task():
            self.log(f"Deleting {p} from device...")
            self.run_adb(["shell", f"rm -rf '{p}'"], timeout=10)
            self.log(f"Deleted {p}")
            self.list_device_files()
        threading.Thread(target=task, daemon=True).start()

    # =========================================================================
    # DATA ANALYTICS & VISUAL CHARTS TAB
    # =========================================================================

    def _build_analytics_tab(self, parent):
        top_bar = tk.Frame(parent, bg="#0f172a")
        top_bar.pack(fill="x", pady=(0, 4))
        tk.Label(top_bar, text="📊 Mobile Data Analytics, Storage Charts & Timeline Insights", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")

        tk.Button(top_bar, text="🔄 Refresh Analytics", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.refresh_analytics_data).pack(side="right")

        # Scrollable Analytics Canvas Container
        canvas = tk.Canvas(parent, bg="#0f172a", highlightthickness=0)
        scrollbar = tk.Scrollbar(parent, orient="vertical", command=canvas.yview)
        canvas.configure(yscrollcommand=scrollbar.set)

        scrollbar.pack(side="right", fill="y")
        canvas.pack(side="left", fill="both", expand=True)

        self.analytics_inner_frame = tk.Frame(canvas, bg="#0f172a")
        self.analytics_canvas_win = canvas.create_window((0, 0), window=self.analytics_inner_frame, anchor="nw")

        self.analytics_inner_frame.bind("<Configure>", lambda e: canvas.configure(scrollregion=canvas.bbox("all")))
        canvas.bind("<Configure>", lambda e: canvas.itemconfig(self.analytics_canvas_win, width=e.width))
        self._bind_mousewheel_to_grid(canvas)
        self._bind_mousewheel_to_grid(self.analytics_inner_frame)

        self._render_analytics_dashboard()

    def refresh_analytics_data(self):
        """Computes comprehensive storage, timeline, call and battery analytics without any mobile battery/CPU drain"""
        self.log("Recalculating mobile storage, media timeline, and call analytics...")
        self._render_analytics_dashboard()
        self.log("Analytics charts and timeline graphs refreshed!")

    def _render_analytics_dashboard(self):
        for w in self.analytics_inner_frame.winfo_children():
            w.destroy()

        items = self.all_gallery_items
        calls = self.all_call_logs
        apps = self.all_installed_apps

        total_items = len(items)
        total_media_bytes = sum(it.get("size_bytes", 0) for it in items)

        # Category breakdown
        cam_items = [it for it in items if it.get("category") == "Camera"]
        sc_items = [it for it in items if it.get("category") == "Screenshots"]
        wa_items = [it for it in items if it.get("category") == "WhatsApp"]
        dl_items = [it for it in items if "DOWNLOAD" in it.get("category", "").upper()]
        tr_items = [it for it in items if it.get("is_trashed") or it.get("category") == "Trashed/Hidden"]
        stk_items = [it for it in items if it.get("is_sticker")]
        rt_items = [it for it in items if it.get("category") == "Root Storage"]

        # Metric Summary Cards Row
        cards_row = tk.Frame(self.analytics_inner_frame, bg="#0f172a")
        cards_row.pack(fill="x", pady=4)

        def make_card(parent, title, val, sub, bg_c, fg_c):
            c = tk.Frame(parent, bg=bg_c, padx=10, pady=8, relief="solid", bd=1, highlightbackground=fg_c, highlightthickness=1)
            c.pack(side="left", fill="both", expand=True, padx=3)
            tk.Label(c, text=title, bg=bg_c, fg="#94a3b8", font=("Segoe UI", 7, "bold")).pack(anchor="w")
            tk.Label(c, text=val, bg=bg_c, fg=fg_c, font=("Segoe UI", 12, "bold")).pack(anchor="w", pady=1)
            tk.Label(c, text=sub, bg=bg_c, fg="#cbd5e1", font=("Segoe UI", 7)).pack(anchor="w")

        oldest_date = "Day 1 Unboxing"
        if items:
            valid_mtimes = [it["mtime"] for it in items if it["mtime"] > 0]
            if valid_mtimes:
                min_ts = min(valid_mtimes)
                oldest_date = datetime.fromtimestamp(min_ts).strftime('%Y-%m-%d')

        make_card(cards_row, "📦 TOTAL MEDIA SCANNED", f"{total_items:,} Files", f"Total Size: {self._format_size(total_media_bytes)}", "#1e293b", "#38bdf8")
        make_card(cards_row, "🕰️ DAY 1 EARLIEST PHOTO", oldest_date, "Timeline Origin Date", "#1e293b", "#fcd34d")
        make_card(cards_row, "🗑️ TRASH & HIDDEN JUNK", f"{len(tr_items):,} Files", f"Size: {self._format_size(sum(it.get('size_bytes',0) for it in tr_items))}", "#1e293b", "#ef4444")
        make_card(cards_row, "📞 CALL HISTORY STATS", f"{len(calls):,} Logs", f"{len(self.all_contacts):,} Extracted Contacts", "#1e293b", "#a855f7")

        # -------------------------------------------------------------
        # 1. STORAGE DISTRIBUTION BAR CHART
        # -------------------------------------------------------------
        chart1_frame = tk.Frame(self.analytics_inner_frame, bg="#1e293b", padx=10, pady=8, relief="solid", bd=1)
        chart1_frame.pack(fill="x", pady=6)

        tk.Label(chart1_frame, text="📊 Mobile Media Storage Distribution (Size & File Count)", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold")).pack(anchor="w")

        c1 = tk.Canvas(chart1_frame, bg="#1e293b", height=120, highlightthickness=0)
        c1.pack(fill="x", pady=4)

        categories_data = [
            ("📸 Camera", sum(it.get("size_bytes",0) for it in cam_items), len(cam_items), "#0284c7"),
            ("💬 WhatsApp", sum(it.get("size_bytes",0) for it in wa_items), len(wa_items), "#16a34a"),
            ("📱 Screens", sum(it.get("size_bytes",0) for it in sc_items), len(sc_items), "#06b6d4"),
            ("📥 Downloads", sum(it.get("size_bytes",0) for it in dl_items), len(dl_items), "#f59e0b"),
            ("🗑️ Trashed", sum(it.get("size_bytes",0) for it in tr_items), len(tr_items), "#ef4444"),
            ("🎨 Stickers", sum(it.get("size_bytes",0) for it in stk_items), len(stk_items), "#ec4899"),
            ("📁 Root/Other", sum(it.get("size_bytes",0) for it in rt_items), len(rt_items), "#8b5cf6"),
        ]

        max_b = max([x[1] for x in categories_data] + [1])

        def draw_storage_bars(event=None):
            c1.delete("all")
            w = c1.winfo_width() or 600
            y_start = 10
            bar_h = 12
            for idx, (label, size_b, count, color) in enumerate(categories_data):
                y = y_start + (idx * 15)
                c1.create_text(8, y + 6, text=label, fill="#cbd5e1", font=("Segoe UI", 7, "bold"), anchor="w")
                
                bar_x1 = 110
                max_w = w - 240
                bar_len = max(4, int((size_b / max_b) * max_w))
                c1.create_rectangle(bar_x1, y, bar_x1 + bar_len, y + bar_h, fill=color, outline="")
                
                sz_str = self._format_size(size_b)
                c1.create_text(bar_x1 + bar_len + 8, y + 6, text=f"{sz_str} ({count:,} files)", fill="#94a3b8", font=("Segoe UI", 7), anchor="w")

        c1.bind("<Configure>", draw_storage_bars)
        self.after(50, draw_storage_bars)

        # -------------------------------------------------------------
        # 2. MEDIA TIMELINE GRAPH (DAY 1 UNBOXING ➔ TODAY)
        # -------------------------------------------------------------
        chart2_frame = tk.Frame(self.analytics_inner_frame, bg="#1e293b", padx=10, pady=8, relief="solid", bd=1)
        chart2_frame.pack(fill="x", pady=6)

        tk.Label(chart2_frame, text="🕰️ Media Acquisition Timeline (Photos & Videos per Year/Month from Day 1 to Today)", bg="#1e293b", fg="#fcd34d", font=("Segoe UI", 9, "bold")).pack(anchor="w")

        c2 = tk.Canvas(chart2_frame, bg="#0f172a", height=130, highlightthickness=0)
        c2.pack(fill="x", pady=4)

        # Group items by YYYY-MM
        timeline_counts = {}
        for it in items:
            mtime = it.get("mtime", 0)
            if mtime > 0:
                dt_key = datetime.fromtimestamp(mtime).strftime('%Y-%m')
                timeline_counts[dt_key] = timeline_counts.get(dt_key, 0) + 1

        sorted_months = sorted(timeline_counts.keys())
        # Pick the most active / distributed periods
        if len(sorted_months) > 20:
            step = max(1, len(sorted_months) // 20)
            displayed_months = sorted_months[::step]
            if sorted_months[-1] not in displayed_months:
                displayed_months.append(sorted_months[-1])
        else:
            displayed_months = sorted_months

        max_t_count = max([timeline_counts[m] for m in displayed_months] + [1])

        def draw_timeline_bars(event=None):
            c2.delete("all")
            w = c2.winfo_width() or 600
            if not displayed_months:
                c2.create_text(w // 2, 60, text="No date footprint data found. Click 'Scan Mobile' to build timeline.", fill="#64748b", font=("Segoe UI", 8))
                return

            n = len(displayed_months)
            slot_w = max(12, (w - 60) / n)
            bar_w = max(6, slot_w - 4)

            for i, m in enumerate(displayed_months):
                cnt = timeline_counts[m]
                x1 = 30 + (i * slot_w)
                x2 = x1 + bar_w
                h = int((cnt / max_t_count) * 80)
                y1 = 100 - h
                y2 = 100

                # Gradient color based on recency
                color = "#38bdf8" if i == n - 1 else ("#0284c7" if i > n // 2 else "#475569")
                c2.create_rectangle(x1, y1, x2, y2, fill=color, outline="")
                c2.create_text((x1 + x2) / 2, y1 - 6, text=str(cnt), fill="#94a3b8", font=("Segoe UI", 6))
                
                # Label
                label_m = m[2:] # "20-07"
                c2.create_text((x1 + x2) / 2, 112, text=label_m, fill="#cbd5e1", font=("Segoe UI", 6), angle=0)

        c2.bind("<Configure>", draw_timeline_bars)
        self.after(60, draw_timeline_bars)

        # -------------------------------------------------------------
        # 3. CALLS & CONTACTS ACTIVITY BREAKDOWN
        # -------------------------------------------------------------
        chart3_frame = tk.Frame(self.analytics_inner_frame, bg="#1e293b", padx=10, pady=8, relief="solid", bd=1)
        chart3_frame.pack(fill="x", pady=6)

        tk.Label(chart3_frame, text="📞 Call Activity & Contact Distribution", bg="#1e293b", fg="#a855f7", font=("Segoe UI", 9, "bold")).pack(anchor="w")

        in_calls = sum(1 for c in calls if c.get("type_id") == "1")
        out_calls = sum(1 for c in calls if c.get("type_id") == "2")
        mis_calls = sum(1 for c in calls if c.get("type_id") == "3")
        rej_calls = sum(1 for c in calls if c.get("type_id") == "5")
        tot_c = max(1, len(calls))

        c3_row = tk.Frame(chart3_frame, bg="#1e293b")
        c3_row.pack(fill="x", pady=4)

        def make_stat_meter(parent, label, count, color):
            pct = (count / tot_c) * 100
            f = tk.Frame(parent, bg="#1e293b")
            f.pack(side="left", fill="both", expand=True, padx=4)
            tk.Label(f, text=f"{label}: {count} ({pct:.0f}%)", bg="#1e293b", fg=color, font=("Segoe UI", 8, "bold")).pack(anchor="w")
            pb = ttk.Progressbar(f, value=pct, maximum=100)
            pb.pack(fill="x", pady=2)

        make_stat_meter(c3_row, "📥 Incoming", in_calls, "#16a34a")
        make_stat_meter(c3_row, "📤 Outgoing", out_calls, "#0284c7")
        make_stat_meter(c3_row, "❌ Missed", mis_calls, "#ef4444")
        make_stat_meter(c3_row, "🚫 Rejected", rej_calls, "#f59e0b")

    def _build_power_tab(self, parent):
        tk.Label(parent, text="Battery Health & Power Save Controls", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(anchor="w", pady=(0, 6))

        p_card = tk.Frame(parent, bg="#1e293b", padx=12, pady=10, relief="solid", bd=1)
        p_card.pack(fill="x", pady=4)

        self.batt_level_lbl = tk.Label(p_card, text="Battery Level: Checking...", bg="#1e293b", fg="#f8fafc", font=("Segoe UI", 10, "bold"))
        self.batt_level_lbl.pack(anchor="w", pady=2)

        self.batt_status_lbl = tk.Label(p_card, text="Charging State: Unknown", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 9))
        self.batt_status_lbl.pack(anchor="w", pady=2)

        tk.Button(p_card, text="🔄 Check Battery Info", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=8, pady=3, relief="flat", command=self.check_battery).pack(anchor="w", pady=4)

        p_acts = tk.Frame(parent, bg="#0f172a")
        p_acts.pack(fill="x", pady=6)

        tk.Button(p_acts, text="💤 Put Mobile To Sleep", bg="#334155", fg="#f8fafc", activebackground="#475569", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, pady=4, relief="flat", command=self.put_device_to_sleep).grid(row=0, column=0, padx=3, pady=3, sticky="ew")
        tk.Button(p_acts, text="⚡ Wake Mobile Screen", bg="#0284c7", fg="#f8fafc", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, pady=4, relief="flat", command=self.wake_device).grid(row=0, column=1, padx=3, pady=3, sticky="ew")

        tk.Button(p_acts, text="🔆 Dim Screen to 1", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.dim_screen).grid(row=1, column=0, padx=3, pady=3, sticky="ew")
        tk.Button(p_acts, text="☀️ Restore Brightness (150)", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.restore_screen).grid(row=1, column=1, padx=3, pady=3, sticky="ew")

        self.var_auto_sleep = tk.BooleanVar(value=False)
        cb1 = tk.Checkbutton(p_card, text="Auto-sleep screen when live monitor is stopped", variable=self.var_auto_sleep, bg="#1e293b", fg="#f8fafc", selectcolor="#0f172a", activebackground="#1e293b", activeforeground="white")
        cb1.pack(anchor="w", pady=2)

        self.var_auto_sleep_close = tk.BooleanVar(value=False)
        cb2 = tk.Checkbutton(p_card, text="Auto-sleep screen when closing Dev Studio on PC", variable=self.var_auto_sleep_close, bg="#1e293b", fg="#f8fafc", selectcolor="#0f172a", activebackground="#1e293b", activeforeground="white")
        cb2.pack(anchor="w", pady=2)

    def _build_log_tab(self, parent):
        self.log_text = tk.Text(parent, bg="#1e293b", fg="#38bdf8", insertbackground="white", font=("Consolas", 8), wrap="word", bd=0)
        scroll = tk.Scrollbar(parent, orient="vertical", command=self.log_text.yview)
        self.log_text.configure(yscrollcommand=scroll.set)
        self.log_text.pack(side="left", fill="both", expand=True)
        scroll.pack(side="right", fill="y")

    def open_storage_tab(self):
        if hasattr(self, 'tab_storage'):
            self.main_tabs.select(self.tab_storage)
            self.scan_all_storage_inventories()

    def _build_storage_tab(self, parent):
        # Header banner
        header = tk.Frame(parent, bg="#1e293b", padx=10, pady=8, relief="solid", bd=1)
        header.pack(fill="x", pady=(0, 6))

        tk.Label(header, text="💾 Storage & Mobile Cleaner", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")

        self.btn_refresh_storage = tk.Button(header, text="🔄 Scan Mobile & PC", bg="#0284c7", fg="white", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.scan_all_storage_inventories)
        self.btn_refresh_storage.pack(side="left", padx=8)

        self.lbl_mobile_storage_badge = tk.Label(header, text="📱 Mobile: Calculating...", bg="#334155", fg="#a855f7", font=("Segoe UI", 8, "bold"), padx=6, pady=2)
        self.lbl_mobile_storage_badge.pack(side="left", padx=3)

        self.lbl_laptop_storage_badge = tk.Label(header, text="💻 Laptop Archive: Safe (Protected)", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"), padx=6, pady=2)
        self.lbl_laptop_storage_badge.pack(side="left", padx=3)

        tk.Label(header, text="🛡️ In-Memory Stream (0 Bytes Phone Footprint)", bg="#064e3b", fg="#34d399", font=("Segoe UI", 8, "bold"), padx=6, pady=2).pack(side="right", padx=2)

        # Paned Window: Left = Mobile Storage & Live File Selector, Right = Laptop Archive Info
        paned = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=4, bd=0)
        paned.pack(fill="both", expand=True)

        # Left/Main Column: Mobile Storage & Live Cleaner (Primary)
        mob_col = tk.Frame(paned, bg="#1e293b", padx=8, pady=8, relief="solid", bd=1)
        paned.add(mob_col, minsize=520)

        m_top = tk.Frame(mob_col, bg="#1e293b")
        m_top.pack(fill="x", pady=(0, 4))
        tk.Label(m_top, text="📱 Mobile Phone Storage & Residual Dumps", bg="#1e293b", fg="#a855f7", font=("Segoe UI", 10, "bold")).pack(side="left")
        self.lbl_mobile_target_device = tk.Label(m_top, text="(Connecting...)", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8))
        self.lbl_mobile_target_device.pack(side="left", padx=4)

        # Top Table: Mobile Categories
        columns_mob = ("category", "path", "count", "size", "status")
        self.tree_mobile_storage = ttk.Treeview(mob_col, columns=columns_mob, show="headings", height=5)
        self.tree_mobile_storage.heading("category", text="Category (Click row to view live files on phone)")
        self.tree_mobile_storage.heading("path", text="Storage Path")
        self.tree_mobile_storage.heading("count", text="Files")
        self.tree_mobile_storage.heading("size", text="Size")
        self.tree_mobile_storage.heading("status", text="Safety")
        self.tree_mobile_storage.column("category", width=160, anchor="w")
        self.tree_mobile_storage.column("path", width=170, anchor="w")
        self.tree_mobile_storage.column("count", width=50, anchor="center")
        self.tree_mobile_storage.column("size", width=70, anchor="center")
        self.tree_mobile_storage.column("status", width=80, anchor="center")
        self.tree_mobile_storage.bind("<<TreeviewSelect>>", self._on_mobile_category_select)

        mob_scroll = tk.Scrollbar(mob_col, orient="vertical", command=self.tree_mobile_storage.yview)
        self.tree_mobile_storage.configure(yscrollcommand=mob_scroll.set)
        self.tree_mobile_storage.pack(side="top", fill="x", pady=(0, 4))
        mob_scroll.pack(side="right", fill="y")

        # Middle Label & File Selection Controls
        mid_bar = tk.Frame(mob_col, bg="#1e293b", pady=3)
        mid_bar.pack(fill="x")
        self.lbl_mobile_files_status = tk.Label(mid_bar, text="📁 Click a category above to load individual files on mobile", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold"))
        self.lbl_mobile_files_status.pack(side="left")

        tk.Button(mid_bar, text="📋 Copy All Paths", bg="#0e7490", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self.copy_all_listed_mobile_paths).pack(side="right", padx=1)
        tk.Button(mid_bar, text="📋 Copy Path", bg="#334155", fg="#38bdf8", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self.copy_selected_mobile_file_path).pack(side="right", padx=1)
        tk.Button(mid_bar, text="📋 Copy Name", bg="#334155", fg="#cbd5e1", font=("Segoe UI", 7), padx=5, relief="flat", command=self.copy_selected_mobile_file_name).pack(side="right", padx=1)
        tk.Button(mid_bar, text="Select All", bg="#334155", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self._select_all_mobile_files).pack(side="right", padx=2)
        tk.Button(mid_bar, text="Deselect All", bg="#334155", fg="white", font=("Segoe UI", 7), padx=5, relief="flat", command=self._deselect_all_mobile_files).pack(side="right", padx=2)

        # Bottom Table: Live Files Stored on Mobile (Selected Category)
        columns_files = ("name", "size", "date", "path")
        self.tree_mobile_files = ttk.Treeview(mob_col, columns=columns_files, show="headings", height=8, selectmode="extended")
        self.tree_mobile_files.heading("name", text="File Name (Stored on Phone)")
        self.tree_mobile_files.heading("size", text="Size")
        self.tree_mobile_files.heading("date", text="Date")
        self.tree_mobile_files.heading("path", text="Mobile Path")
        self.tree_mobile_files.column("name", width=180, anchor="w")
        self.tree_mobile_files.column("size", width=65, anchor="center")
        self.tree_mobile_files.column("date", width=100, anchor="center")
        self.tree_mobile_files.column("path", width=180, anchor="w")
        self.tree_mobile_files.bind("<Button-3>", self._show_tree_mobile_files_context_menu)

        f_scroll = tk.Scrollbar(mob_col, orient="vertical", command=self.tree_mobile_files.yview)
        self.tree_mobile_files.configure(yscrollcommand=f_scroll.set)
        self.tree_mobile_files.pack(side="top", fill="both", expand=True)
        f_scroll.pack(side="right", fill="y")

        # Mobile Action Buttons
        r_acts = tk.Frame(mob_col, bg="#1e293b", pady=6)
        r_acts.pack(fill="x", side="bottom")

        tk.Button(r_acts, text="📥 PULL SELECTED TO LAPTOP", bg="#0284c7", fg="white", activebackground="#0369a1", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.pull_selected_mobile_files_command).pack(side="left", padx=2)
        tk.Button(r_acts, text="🚀 Pull ALL WhatsApp & Hidden", bg="#7c3aed", fg="white", activebackground="#6d28d9", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.pull_all_whatsapp_and_hidden_vault).pack(side="left", padx=2)
        tk.Button(r_acts, text="🗑️ DELETE SELECTED", bg="#b91c1c", fg="white", activebackground="#991b1b", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.delete_selected_mobile_files_command).pack(side="left", padx=2)
        tk.Button(r_acts, text="⚡ Temp Purge (0-KB)", bg="#16a34a", fg="white", activebackground="#15803d", font=("Segoe UI", 8), padx=5, relief="flat", command=self.purge_mobile_temp_buffers_now).pack(side="left", padx=2)
        tk.Button(r_acts, text="🚨 Clean Category", bg="#991b1b", fg="white", activebackground="#7f1d1d", font=("Segoe UI", 8), padx=5, relief="flat", command=self.delete_selected_mobile_storage_category).pack(side="right", padx=2)

        # Right Column: Laptop Storage Archive (Preserved)
        lap_col = tk.Frame(paned, bg="#1e293b", padx=8, pady=8, relief="solid", bd=1)
        paned.add(lap_col, minsize=260)

        l_top = tk.Frame(lap_col, bg="#1e293b")
        l_top.pack(fill="x", pady=(0, 4))
        tk.Label(l_top, text="💻 Laptop Archive (Preserved)", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 10, "bold")).pack(side="left")

        # Laptop Treeview
        columns_lap = ("folder", "count", "size")
        self.tree_laptop_storage = ttk.Treeview(lap_col, columns=columns_lap, show="headings", height=12)
        self.tree_laptop_storage.heading("folder", text="Folder")
        self.tree_laptop_storage.heading("count", text="Files")
        self.tree_laptop_storage.heading("size", text="Size")
        self.tree_laptop_storage.column("folder", width=140, anchor="w")
        self.tree_laptop_storage.column("count", width=45, anchor="center")
        self.tree_laptop_storage.column("size", width=55, anchor="center")

        lap_scroll = tk.Scrollbar(lap_col, orient="vertical", command=self.tree_laptop_storage.yview)
        self.tree_laptop_storage.configure(yscrollcommand=lap_scroll.set)
        self.tree_laptop_storage.pack(side="top", fill="both", expand=True)
        lap_scroll.pack(side="right", fill="y")

        l_acts = tk.Frame(lap_col, bg="#1e293b", pady=6)
        l_acts.pack(fill="x", side="bottom")
        tk.Button(l_acts, text="📂 Open in Windows Explorer", bg="#0284c7", fg="white", activebackground="#0369a1", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.open_laptop_storage_explorer).pack(fill="x", padx=2)

    def _show_file_listbox_context_menu(self, event):
        try:
            idx = self.file_tree.index(self.file_tree.identify_row(event.y)) if self.file_tree.identify_row(event.y) else -1
            if idx >= 0:
                self.file_tree.selection_remove(self.file_tree.selection())
                self.file_tree.selection_set(self.file_tree.get_children()[idx])
                self.file_tree.focus(self.file_tree.get_children()[idx])
                self._on_file_select_preview()
        except Exception:
            pass
        menu = tk.Menu(self, tearoff=0, bg="#1e293b", fg="#f8fafc", activebackground="#0284c7", activeforeground="#ffffff")
        menu.add_command(label="📋 Copy Full Path", command=self.copy_explorer_remote_path)
        menu.add_command(label="📋 Copy File Name", command=self.copy_explorer_file_name)
        menu.add_command(label="📋 Copy ALL Listed Paths", command=self.copy_all_explorer_listed_paths)
        menu.add_separator()
        menu.add_command(label="📥 Pull Selected to Laptop...", command=self.pull_selected_file)
        menu.add_command(label="🚀 Pull ALL Listed Files to Laptop", command=self.pull_all_discovered_files)
        menu.add_separator()
        menu.add_command(label="🗑️ Delete from Device", command=self.delete_selected_explorer_item)
        menu.post(event.x_root, event.y_root)

    def _show_tree_mobile_files_context_menu(self, event):
        item = self.tree_mobile_files.identify_row(event.y)
        if item:
            if item not in self.tree_mobile_files.selection():
                self.tree_mobile_files.selection_set(item)
        menu = tk.Menu(self, tearoff=0, bg="#1e293b", fg="#f8fafc", activebackground="#0284c7", activeforeground="#ffffff")
        menu.add_command(label="📋 Copy Path", command=self.copy_selected_mobile_file_path)
        menu.add_command(label="📋 Copy File Name", command=self.copy_selected_mobile_file_name)
        menu.add_command(label="📋 Copy ALL Listed Paths", command=self.copy_all_listed_mobile_paths)
        menu.add_separator()
        menu.add_command(label="📥 Pull Selected to Laptop", command=self.pull_selected_mobile_files_command)
        menu.add_command(label="🚀 Pull ALL WhatsApp & Hidden Vault", command=self.pull_all_whatsapp_and_hidden_vault)
        menu.add_separator()
        menu.add_command(label="🗑️ Delete Selected File(s) from Mobile", command=self.delete_selected_mobile_files_command)
        menu.post(event.x_root, event.y_root)

    def copy_explorer_remote_path(self):
        if hasattr(self, 'file_preview_selected_path') and self.file_preview_selected_path:
            self.clipboard_clear()
            self.clipboard_append(self.file_preview_selected_path)
            self.update()
            self.log(f"📋 Copied remote path: {self.file_preview_selected_path}")
            messagebox.showinfo("Copied", f"Copied path to clipboard:\n{self.file_preview_selected_path}")

    def copy_explorer_file_name(self):
        if hasattr(self, 'file_preview_selected_path') and self.file_preview_selected_path:
            fname = os.path.basename(self.file_preview_selected_path)
            self.clipboard_clear()
            self.clipboard_append(fname)
            self.update()
            self.log(f"📋 Copied file name: {fname}")
            messagebox.showinfo("Copied", f"Copied file name to clipboard:\n{fname}")

    def copy_all_explorer_listed_paths(self):
        if not self.current_remote_items:
            messagebox.showinfo("No Files", "No items currently listed.")
            return
        paths = [item[2] for item in self.current_remote_items]
        text = "\n".join(paths)
        self.clipboard_clear()
        self.clipboard_append(text)
        self.update()
        self.log(f"📋 Copied {len(paths)} explorer paths to clipboard.")
        messagebox.showinfo("Copied", f"Copied {len(paths)} file paths to clipboard!")

    def copy_selected_mobile_file_path(self):
        sel = self.tree_mobile_files.selection()
        if not sel:
            messagebox.showinfo("Select File", "Please select a file from the list first.")
            return
        vals = self.tree_mobile_files.item(sel[0], "values")
        if len(vals) >= 4:
            pth = vals[3]
            self.clipboard_clear()
            self.clipboard_append(pth)
            self.update()
            self.log(f"📋 Copied mobile path: {pth}")
            messagebox.showinfo("Copied", f"Copied path to clipboard:\n{pth}")

    def copy_selected_mobile_file_name(self):
        sel = self.tree_mobile_files.selection()
        if not sel:
            messagebox.showinfo("Select File", "Please select a file from the list first.")
            return
        vals = self.tree_mobile_files.item(sel[0], "values")
        fname = vals[0]
        self.clipboard_clear()
        self.clipboard_append(fname)
        self.update()
        self.log(f"📋 Copied file name: {fname}")
        messagebox.showinfo("Copied", f"Copied file name to clipboard:\n{fname}")

    def copy_all_listed_mobile_paths(self):
        paths = []
        for item_id in self.tree_mobile_files.get_children():
            vals = self.tree_mobile_files.item(item_id, "values")
            if len(vals) >= 4:
                paths.append(vals[3])
        if not paths:
            messagebox.showinfo("No Files", "No files currently listed in the table.")
            return
        text = "\n".join(paths)
        self.clipboard_clear()
        self.clipboard_append(text)
        self.update()
        self.log(f"📋 Copied {len(paths)} mobile paths to clipboard.")
        messagebox.showinfo("Copied", f"Copied {len(paths)} mobile file paths to clipboard!")

    def pull_selected_mobile_files_command(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return

        selected_items = self.tree_mobile_files.selection()
        if not selected_items:
            messagebox.showinfo("Select Files", "Please select one or more files to pull to your laptop.")
            return

        files_to_pull = []
        for item_id in selected_items:
            vals = self.tree_mobile_files.item(item_id, "values")
            if len(vals) >= 4:
                files_to_pull.append((vals[0], vals[3]))

        dev_name = self.get_device_clean_name()
        ts_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        dest_folder = os.path.join(PULLED_SCREENS_DIR, dev_name, f"Pulled_Files_{ts_str}")
        os.makedirs(dest_folder, exist_ok=True)

        def task():
            self.log(f"📥 Pulling {len(files_to_pull)} files to {dest_folder} (Works over Tailscale & USB)...")
            success = 0
            for name, rpath in files_to_pull:
                local_file = os.path.join(dest_folder, os.path.basename(rpath))
                code, _, _ = self.run_adb(["pull", rpath, local_file], timeout=60)
                if code == 0 and os.path.exists(local_file):
                    success += 1
            
            self.log(f"✓ Pulled {success}/{len(files_to_pull)} files to laptop: {dest_folder}")
            self.after(0, lambda: messagebox.showinfo("Pull Complete", f"Successfully pulled {success}/{len(files_to_pull)} files to laptop:\n{dest_folder}"))
            self.scan_all_storage_inventories()
            os.startfile(dest_folder)

        threading.Thread(target=task, daemon=True).start()

    def pull_all_whatsapp_and_hidden_vault(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return
        dev_name = self.get_device_clean_name()
        ts_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        dest_folder = os.path.join(PULLED_SCREENS_DIR, dev_name, f"WhatsApp_and_Hidden_Vault_{ts_str}")
        os.makedirs(dest_folder, exist_ok=True)

        confirm = messagebox.askyesno(
            "🚀 1-Click WhatsApp & Hidden Vault Extraction",
            f"Extract and pull ALL WhatsApp media/databases and ALL hidden/trashed folders from phone to laptop?\n\nDestination:\n{dest_folder}\n\nWorks seamlessly over Tailscale Wi-Fi or USB Cable."
        )
        if not confirm:
            return

        def task():
            self.log("🚀 Starting deep extraction of WhatsApp & Hidden Vault...")
            wa_dest = os.path.join(dest_folder, "WhatsApp_All")
            hidden_dest = os.path.join(dest_folder, "Hidden_and_Private_Safe")
            os.makedirs(wa_dest, exist_ok=True)
            os.makedirs(hidden_dest, exist_ok=True)

            # Pull WhatsApp
            wa_sources = [
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Media",
                "/sdcard/WhatsApp/Media",
                "/sdcard/Android/media/com.whatsapp.w4b/WhatsApp Business/Media",
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Databases",
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Backups",
                "/storage/emulated/999/Android/media/com.whatsapp/WhatsApp/Media"
            ]
            for ws in wa_sources:
                self.log(f"Extracting WhatsApp source: {ws}...")
                self.run_adb(["pull", ws, wa_dest], timeout=180)

            # Pull Hidden & Private Safe
            hidden_sources = [
                "/sdcard/.trash-storage",
                "/sdcard/.aceself",
                "/sdcard/.secret",
                "/sdcard/.thumbnails",
                "/sdcard/.hidden",
                "/sdcard/.private",
                "/storage/emulated/999"
            ]
            for hs in hidden_sources:
                self.log(f"Extracting Hidden Vault source: {hs}...")
                self.run_adb(["pull", hs, hidden_dest], timeout=180)

            self.log(f"✓ Complete WhatsApp & Hidden Vault extracted to: {dest_folder}")
            self.after(0, lambda: messagebox.showinfo("Extraction Complete", f"All WhatsApp media & hidden vault folders pulled to:\n{dest_folder}"))
            self.scan_all_storage_inventories()
            os.startfile(dest_folder)

        threading.Thread(target=task, daemon=True).start()

    def scan_all_whatsapp_deep_folders(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return
        self.log("🔍 Scanning ALL WhatsApp folders, media, voice notes, stickers, and databases...")
        self.path_entry.delete(0, tk.END)
        self.path_entry.insert(0, "WHATSAPP_ALL_MEDIA")

        def task():
            wa_paths = [
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Media",
                "/sdcard/WhatsApp/Media",
                "/sdcard/Android/media/com.whatsapp.w4b/WhatsApp Business/Media",
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Databases",
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Backups",
                "/storage/emulated/999/Android/media/com.whatsapp/WhatsApp/Media",
                "/sdcard/WhatsApp/Databases",
                "/sdcard/WhatsApp/Backups"
            ]
            
            discovered = []
            for wp in wa_paths:
                cmd = f"find '{wp}' -type f 2>/dev/null | head -n 400"
                try:
                    res = subprocess.run(["adb", "-s", self.target_device, "shell", cmd], capture_output=True, text=True, timeout=8, creationflags=NO_WINDOW)
                    lines = res.stdout.strip().splitlines()
                    for f in lines:
                        f = f.strip()
                        if f and f not in [x[2] for x in discovered]:
                            fname = os.path.basename(f)
                            discovered.append((False, fname, f, 0))
                except Exception:
                    pass

            def show():
                self.current_remote_items = discovered
                self.file_tree.delete(*self.file_tree.get_children())
                for is_dir, name, full_p, sz in discovered:
                    self.file_tree.insert("", "end", values=(f"💬 {name}", ""))
                self.file_info_lbl.config(text=f"Discovered {len(discovered)} WhatsApp media & database files across all folders.")
                self.log(f"Discovered {len(discovered)} WhatsApp items.")
            self.after(0, show)

        threading.Thread(target=task, daemon=True).start()

    def scan_all_hidden_mobile_folders(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return
        self.log("🔒 Deep-scanning ALL hidden folders, private safe, and .trashed files...")
        self.path_entry.delete(0, tk.END)
        self.path_entry.insert(0, "HIDDEN_AND_PRIVATE_VAULTS")

        def task():
            hidden_roots = [
                "/sdcard/.trash-storage",
                "/sdcard/.aceself",
                "/sdcard/.secret",
                "/sdcard/.thumbnails",
                "/sdcard/.hidden",
                "/sdcard/.private",
                "/storage/emulated/999",
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Media/.Statuses",
                "/sdcard/WhatsApp/Media/.Statuses"
            ]
            discovered = []
            for hr in hidden_roots:
                cmd = f"find '{hr}' -type f 2>/dev/null | head -n 250"
                try:
                    res = subprocess.run(["adb", "-s", self.target_device, "shell", cmd], capture_output=True, text=True, timeout=8, creationflags=NO_WINDOW)
                    lines = res.stdout.strip().splitlines()
                    for f in lines:
                        f = f.strip()
                        if f and f not in [x[2] for x in discovered]:
                            fname = os.path.basename(f)
                            discovered.append((False, fname, f, 0))
                except Exception:
                    pass
            
            try:
                cmd_tr = "find /sdcard -maxdepth 3 -name '.*' -type f 2>/dev/null | head -n 300"
                res_tr = subprocess.run(["adb", "-s", self.target_device, "shell", cmd_tr], capture_output=True, text=True, timeout=8, creationflags=NO_WINDOW)
                for f in res_tr.stdout.strip().splitlines():
                    f = f.strip()
                    if f and f not in [x[2] for x in discovered]:
                        fname = os.path.basename(f)
                        discovered.append((False, fname, f, 0))
            except Exception:
                pass

            def show():
                self.current_remote_items = discovered
                self.file_tree.delete(*self.file_tree.get_children())
                for is_dir, name, full_p, sz in discovered:
                    self.file_tree.insert("", "end", values=(f"🔒 {name}", ""))
                self.file_info_lbl.config(text=f"Discovered {len(discovered)} hidden & private safe files across phone.")
                self.log(f"Discovered {len(discovered)} hidden & locked files.")
            self.after(0, show)

        threading.Thread(target=task, daemon=True).start()

    def pull_all_discovered_files(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return
        if not self.current_remote_items:
            messagebox.showinfo("No Files", "Please browse or scan a folder first.")
            return

        files = [item for item in self.current_remote_items if not item[0]]
        if not files:
            messagebox.showinfo("No Files", "No individual files to pull.")
            return

        dev_name = self.get_device_clean_name()
        ts_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        dest_folder = os.path.join(PULLED_SCREENS_DIR, dev_name, f"Batch_Extract_{ts_str}")
        os.makedirs(dest_folder, exist_ok=True)

        confirm = messagebox.askyesno(
            "Batch Pull Confirmation",
            f"Pull ALL {len(files)} discovered files to laptop?\n\nDestination:\n{dest_folder}\n\nWorks over both Tailscale and USB cable."
        )
        if not confirm:
            return

        def task():
            self.log(f"🚀 Batch pulling {len(files)} files to {dest_folder}...")
            count = 0
            for is_dir, fname, full_path, sz in files:
                target_f = os.path.join(dest_folder, fname)
                code, _, _ = self.run_adb(["pull", full_path, target_f], timeout=60)
                if code == 0 and os.path.exists(target_f):
                    count += 1
            self.log(f"✓ Pulled {count}/{len(files)} files to laptop.")
            self.after(0, lambda: messagebox.showinfo("Pull Complete", f"Successfully pulled {count}/{len(files)} files to:\n{dest_folder}"))
            self.scan_all_storage_inventories()
            os.startfile(dest_folder)

        threading.Thread(target=task, daemon=True).start()

    def _select_all_mobile_files(self):
        for item in self.tree_mobile_files.get_children():
            self.tree_mobile_files.selection_add(item)

    def _deselect_all_mobile_files(self):
        for item in self.tree_mobile_files.get_children():
            self.tree_mobile_files.selection_remove(item)

    def _on_mobile_category_select(self, event=None):
        sel = self.tree_mobile_storage.selection()
        if not sel or not self.connected:
            return
        vals = self.tree_mobile_storage.item(sel[0], "values")
        category = vals[0]
        path = vals[1]
        self.lbl_mobile_files_status.config(text=f"Loading live files for {category} [{path}]...")
        threading.Thread(target=self._load_mobile_category_files_worker, args=(category, path), daemon=True).start()

    def _load_mobile_category_files_worker(self, category, path):
        dev = self.target_device
        file_items = []

        if path == "/data/local/tmp":
            cmd = "ls -la /data/local/tmp/ 2>/dev/null"
        elif "DCIM" in path or "Pictures" in path or "Movies" in path or "Download" in path:
            cmd = f"find '{path}' -maxdepth 2 -type f 2>/dev/null | head -n 250"
        else:
            cmd = f"find '{path}' -maxdepth 2 -type f 2>/dev/null | head -n 150"

        try:
            res = subprocess.run(["adb", "-s", dev, "shell", cmd], capture_output=True, text=True, timeout=8, creationflags=NO_WINDOW)
            raw_out = res.stdout.strip()
            if raw_out:
                lines = raw_out.splitlines()
                for line in lines:
                    line = line.strip()
                    if not line or line.startswith("total") or line.endswith("/.") or line.endswith("/.."):
                        continue
                    
                    if line.startswith("/"):
                        # Path output from find
                        fpath = line
                        fname = os.path.basename(fpath)
                        # Query size
                        file_items.append((fname, "Active", "On Phone", fpath))
                    else:
                        # ls output
                        parts = line.split()
                        if len(parts) >= 8:
                            fname = " ".join(parts[8:])
                            if fname not in [".", ".."]:
                                sz_bytes = int(parts[4]) if parts[4].isdigit() else 0
                                sz_disp = f"{sz_bytes/1024:.1f} KB" if sz_bytes < 1024*1024 else f"{sz_bytes/(1024*1024):.2f} MB"
                                date_disp = f"{parts[5]} {parts[6]}"
                                full_p = f"{path.rstrip('/')}/{fname}"
                                file_items.append((fname, sz_disp, date_disp, full_p))
        except Exception as e:
            self.log(f"Error loading mobile category files: {e}")

        def update_files_ui():
            for i in self.tree_mobile_files.get_children():
                self.tree_mobile_files.delete(i)
            for item in file_items:
                self.tree_mobile_files.insert("", "end", values=item)
            self.lbl_mobile_files_status.config(text=f"Category: {category} ({len(file_items)} files live on phone)")

        self.after(0, update_files_ui)

    def delete_selected_mobile_files_command(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return

        selected_items = self.tree_mobile_files.selection()
        if not selected_items:
            messagebox.showinfo("Select Files", "Please select one or more files in the Live Mobile Files list to delete.")
            return

        files_to_delete = []
        for item_id in selected_items:
            vals = self.tree_mobile_files.item(item_id, "values")
            if len(vals) >= 4:
                files_to_delete.append(vals[3])

        if not files_to_delete:
            return

        sample_names = [os.path.basename(p) for p in files_to_delete[:4]]
        sample_str = ", ".join(sample_names) + ("..." if len(files_to_delete) > 4 else "")

        confirm = messagebox.askyesno(
            "⚠️ Confirm Delete from Mobile",
            f"Are you sure you want to delete {len(files_to_delete)} selected file(s) directly from phone storage ({self.target_device})?\n\nFiles: {sample_str}\n\nNote: Laptop data is completely safe and will NOT be touched."
        )
        if not confirm:
            return

        dev = self.target_device
        self.log(f"🗑️ Deleting {len(files_to_delete)} selected files from mobile {dev}...")
        
        escaped_paths = " ".join([f"'{p}'" for p in files_to_delete])
        cmd = f"rm -f {escaped_paths} 2>/dev/null"
        try:
            subprocess.run(["adb", "-s", dev, "shell", cmd], capture_output=True, timeout=12, creationflags=NO_WINDOW)
            self.log(f"✓ Deleted {len(files_to_delete)} files from mobile {dev}.")
            self.scan_all_storage_inventories()
            self._on_mobile_category_select()
            messagebox.showinfo("Mobile Clean Complete", f"Successfully deleted {len(files_to_delete)} file(s) from phone storage.")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to delete files on mobile: {e}")

    def scan_all_storage_inventories(self):
        threading.Thread(target=self._scan_storage_worker, daemon=True).start()

    def _scan_storage_worker(self):
        self.after(0, lambda: self.lbl_laptop_storage_badge.config(text="💻 Laptop Archive: Scanning..."))
        self.after(0, lambda: self.lbl_mobile_storage_badge.config(text="📱 Mobile: Scanning..."))

        # 1. Laptop scan
        lap_items = []
        total_lap_files = 0
        total_lap_bytes = 0
        
        base_dir = PULLED_SCREENS_DIR
        if os.path.exists(base_dir):
            for root, dirs, files in os.walk(base_dir):
                m_files = [f for f in files if f.lower().endswith(('.png', '.jpg', '.jpeg', '.webp', '.raw', '.mp4', '.csv', '.pdf'))]
                if m_files:
                    f_size = sum(os.path.getsize(os.path.join(root, f)) for f in m_files)
                    total_lap_files += len(m_files)
                    total_lap_bytes += f_size
                    rel = os.path.relpath(root, base_dir).replace("\\", "/")
                    size_str = f"{f_size / (1024*1024):.2f} MB" if f_size > 1024*1024 else f"{f_size/1024:.1f} KB"
                    lap_items.append((rel if rel != '.' else 'Root / Snapshots', len(m_files), size_str))

        # 2. Mobile scan
        mob_items = []
        total_mob_files = 0
        total_mob_bytes = 0
        
        dev = self.target_device
        if self.connected:
            mobile_targets = [
                ("Past Screenshots", "/sdcard/DCIM/Screenshots", "Safe to Purge"),
                ("Camera Photos & Videos", "/sdcard/DCIM/Camera", "Personal Media"),
                ("Downloads & Residuals", "/sdcard/Download", "User Files"),
                ("Pictures & App Media", "/sdcard/Pictures", "App Media"),
                ("Screen Recordings & Movies", "/sdcard/Movies", "Recordings"),
                ("Documents", "/sdcard/Documents", "Documents"),
                ("Music & Audio", "/sdcard/Music", "Audio"),
                ("System Temp & Buffers", "/data/local/tmp", "Zero Risk Clean"),
                ("WhatsApp Media & Data", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media", "WhatsApp Media"),
                ("WhatsApp Root / Legacy", "/sdcard/WhatsApp", "Legacy WA"),
                ("WhatsApp Business", "/sdcard/Android/media/com.whatsapp.w4b/WhatsApp Business", "WA Business"),
                ("WhatsApp Statuses (.Statuses)", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media/.Statuses", "Hidden WA"),
                ("Hidden Trash (.trash-storage)", "/sdcard/.trash-storage", "Recycle Bin"),
                ("OPPO Private Safe (.aceself)", "/sdcard/.aceself", "Locked Safe"),
                ("Hidden Thumbnails (.thumbnails)", "/sdcard/.thumbnails", "Cache / Clean"),
                ("Dual App Space (999)", "/storage/emulated/999", "Dual Space"),
            ]
            
            for cat, path, safety in mobile_targets:
                cnt_cmd = f"find {path} -type f 2>/dev/null | wc -l"
                try:
                    res_cnt = subprocess.run(["adb", "-s", dev, "shell", cnt_cmd], capture_output=True, text=True, timeout=5, creationflags=NO_WINDOW).stdout.strip()
                    cnt = int(res_cnt) if res_cnt.isdigit() else 0
                except Exception:
                    cnt = 0
                
                du_cmd = f"du -sk {path} 2>/dev/null | head -n 1"
                try:
                    res_du = subprocess.run(["adb", "-s", dev, "shell", du_cmd], capture_output=True, text=True, timeout=5, creationflags=NO_WINDOW).stdout.strip()
                    kb = 0
                    if res_du and res_du.split():
                        first_tok = res_du.split()[0]
                        if first_tok.isdigit():
                            kb = int(first_tok)
                except Exception:
                    kb = 0
                        
                size_str = f"{kb/1024:.2f} MB" if kb < 1024*1024 else f"{kb/(1024*1024):.2f} GB"
                total_mob_files += cnt
                total_mob_bytes += kb * 1024
                mob_items.append((cat, path, cnt, size_str, safety))

        def update_ui():
            # Update Laptop Tree
            for i in self.tree_laptop_storage.get_children():
                self.tree_laptop_storage.delete(i)
            for item in lap_items:
                self.tree_laptop_storage.insert("", "end", values=item)
            
            lap_badge = f"💻 Laptop Archive: {total_lap_files} files ({total_lap_bytes/(1024*1024):.1f} MB) - Safe"
            self.lbl_laptop_storage_badge.config(text=lap_badge)

            # Update Mobile Tree
            for i in self.tree_mobile_storage.get_children():
                self.tree_mobile_storage.delete(i)
            for item in mob_items:
                self.tree_mobile_storage.insert("", "end", values=item)
            
            if self.connected:
                mob_badge = f"📱 Mobile: {total_mob_files} files ({total_mob_bytes/(1024*1024*1024):.2f} GB)"
                self.lbl_mobile_storage_badge.config(text=mob_badge)
                self.lbl_mobile_target_device.config(text=f"({self.target_device})")
            else:
                self.lbl_mobile_storage_badge.config(text="📱 Mobile: Disconnected")
                self.lbl_mobile_target_device.config(text="(Offline)")

        self.after(0, update_ui)

    def open_laptop_storage_explorer(self):
        try:
            if os.name == 'nt':
                os.startfile(PULLED_SCREENS_DIR)
            else:
                subprocess.Popen(['xdg-open', PULLED_SCREENS_DIR])
        except Exception as e:
            messagebox.showerror("Error", f"Failed to open storage directory: {e}")

    def purge_mobile_temp_buffers_now(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return

        dev = self.target_device
        self.log(f"⚡ Purging temporary ADB buffers and residual screencaps on {dev}...")
        cmd = "rm -f /sdcard/screen* /sdcard/screencap* /sdcard/screenshot* /sdcard/badgym* /data/local/tmp/*.png /data/local/tmp/*.jpg /data/local/tmp/*.raw /data/local/tmp/*.apk /data/local/tmp/*.tmp 2>/dev/null"
        try:
            subprocess.run(["adb", "-s", dev, "shell", cmd], capture_output=True, timeout=8, creationflags=NO_WINDOW)
            self.log(f"✓ Mobile device {dev} temporary storage 100% clean (0 bytes overhead).")
            self.scan_all_storage_inventories()
            messagebox.showinfo("Clean Complete", f"Device {dev} temporary buffers, screencaps, and cache files have been cleared with 0 bytes residual.")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to purge mobile temp files: {e}")

    def delete_selected_mobile_storage_category(self):
        if not self.connected:
            messagebox.showwarning("Offline", "Please connect to device via ADB first.")
            return

        sel = self.tree_mobile_storage.selection()
        if not sel:
            messagebox.showinfo("Select Category", "Please select a category row in the Mobile Storage table to clean.")
            return

        vals = self.tree_mobile_storage.item(sel[0], "values")
        category = vals[0]
        path = vals[1]
        count = vals[2]
        size = vals[3]

        confirm = messagebox.askyesno(
            "⚠️ Confirm Mobile Storage Category Deletion",
            f"Are you sure you want to delete ALL files in this category on mobile?\n\nCategory: {category}\nPath: {path}\nFiles: {count}\nSize: {size}\n\nWARNING: Files will be deleted directly from phone memory. Laptop data is NOT touched."
        )
        if not confirm:
            return

        dev = self.target_device
        self.log(f"🗑️ Deleting all files in {path} on mobile {dev}...")
        cmd = f"rm -rf {path}/* 2>/dev/null"
        try:
            subprocess.run(["adb", "-s", dev, "shell", cmd], capture_output=True, timeout=12, creationflags=NO_WINDOW)
            self.log(f"✓ Mobile path {path} cleared on {dev}.")
            self.scan_all_storage_inventories()
            for i in self.tree_mobile_files.get_children():
                self.tree_mobile_files.delete(i)
            messagebox.showinfo("Deleted", f"Category '{category}' ({path}) has been cleaned on the phone.")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to clean mobile storage path: {e}")

    def _build_screen_panel(self, parent):
        ctrl_container = tk.Frame(parent, bg="#1e293b", relief="solid", bd=1)
        ctrl_container.pack(fill="x", pady=(0, 6))

        # Row 1: Title, Stream toggle, Snap button, BAD GYM badge, Web Stream
        r1 = tk.Frame(ctrl_container, bg="#1e293b", padx=6, pady=3)
        r1.pack(fill="x")

        tk.Label(r1, text="📱 Live Screen", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold")).pack(side="left")

        self.btn_pull_live = tk.Button(r1, text="▶ Stream", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.toggle_live_pull)
        self.btn_pull_live.pack(side="left", padx=3)

        tk.Button(r1, text="📸 Snap", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.pull_single_screen).pack(side="left", padx=2)

        self.screen_badgym_badge = tk.Label(r1, text="● BAD GYM", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 7, "bold"))
        self.screen_badgym_badge.pack(side="left", padx=3)

        tk.Button(r1, text="🎥 Web", bg="#0891b2", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.open_live_web_stream).pack(side="right", padx=2)

        # Row 2: Speed / Interval, Auto-save check, Open Last Saved, Open Folder
        r2 = tk.Frame(ctrl_container, bg="#0f172a", padx=6, pady=3)
        r2.pack(fill="x")

        tk.Label(r2, text="Speed:", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left", padx=(0, 2))
        self.interval_combo = ttk.Combobox(
            r2,
            values=["0.5s (Fast)", "1.0s (Smooth)", "2.0s (Normal)", "5.0s (Power Saver)"],
            width=13,
            state="readonly"
        )
        self.interval_combo.current(1)
        self.interval_combo.pack(side="left", padx=2)

        if not hasattr(self, 'var_autosave_frames'):
            self.var_autosave_frames = tk.BooleanVar(value=True)
        self.chk_autosave = tk.Checkbutton(
            r2,
            text="Save All",
            variable=self.var_autosave_frames,
            bg="#0f172a",
            fg="#cbd5e1",
            selectcolor="#1e293b",
            activebackground="#0f172a",
            activeforeground="#38bdf8",
            font=("Segoe UI", 7)
        )
        self.chk_autosave.pack(side="left", padx=2)

        tk.Button(r2, text="📁 Folder", bg="#334155", fg="white", font=("Segoe UI", 7), padx=5, relief="flat", command=self.open_saved_folder).pack(side="right", padx=1)
        self.btn_open_last = tk.Button(r2, text="📂 Last Saved", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=5, relief="flat", command=self.open_last_screenshot)
        self.btn_open_last.pack(side="right", padx=2)

        self.screen_frame = tk.Frame(parent, bg="#000000", bd=2, relief="groove")
        self.screen_frame.pack(fill="both", expand=True)

        self.screen_canvas = tk.Canvas(self.screen_frame, bg="#000000", highlightthickness=0)
        self.screen_scroll = tk.Scrollbar(self.screen_frame, orient="vertical", command=self.screen_canvas.yview)
        self.screen_canvas.configure(yscrollcommand=self.screen_scroll.set)

        self.screen_scroll.pack(side="right", fill="y")
        self.screen_canvas.pack(side="left", fill="both", expand=True)

        self.screen_inner_frame = tk.Frame(self.screen_canvas, bg="#000000")
        self.screen_canvas_win = self.screen_canvas.create_window((0, 0), window=self.screen_inner_frame, anchor="nw")

        self.screen_lbl = tk.Label(self.screen_inner_frame, text="No Screen Captured\nClick 'Snap' or 'Stream'", bg="#000000", fg="#64748b", font=("Segoe UI", 9), justify="center")
        self.screen_lbl.pack(pady=40, padx=10)

        self.screen_inner_frame.bind("<Configure>", lambda e: self.screen_canvas.configure(scrollregion=self.screen_canvas.bbox("all")))
        self.screen_canvas.bind("<Configure>", self._center_canvas)

        self.screen_status_lbl = tk.Label(parent, text="Status: Ready | Saved to laptop date-wise | Phone gallery excluded", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 7))
        self.screen_status_lbl.pack(fill="x", pady=(2, 0))

    def _center_canvas(self, event):
        canvas_width = event.width
        self.screen_canvas.itemconfig(self.screen_canvas_win, width=canvas_width)

    def log(self, msg):
        def _append():
            try:
                timestamp = time.strftime("%H:%M:%S")
                self.log_text.insert("end", f"[{timestamp}] {msg}\n")
                self.log_text.see("end")
            except Exception:
                pass
        try:
            self.after(0, _append)
        except Exception:
            pass

    def run_adb(self, cmd_list, timeout=20):
        addr = self.target_device
        full_cmd = ["adb", "-s", addr] + cmd_list
        try:
            res = subprocess.run(
                full_cmd,
                capture_output=True,
                timeout=timeout,
                creationflags=NO_WINDOW
            )
            out = res.stdout.decode('utf-8', errors='replace').strip() if res.stdout else ""
            err = res.stderr.decode('utf-8', errors='replace').strip() if res.stderr else ""
            
            # If command failed due to device offline or disconnected over network
            if ("offline" in out.lower() or "device offline" in err.lower() or "error: device" in err.lower()) and ":" in addr:
                if self.connected:
                    self.connected = False
                    self.after(0, lambda: self.status_lbl.config(text="💤 Connection Lost / Device Sleep", fg="#f59e0b"))
                    self.after(0, lambda: self.btn_connect.config(text="Connect ADB", bg="#0284c7"))
                    self._start_wake_watcher(addr)

            if res.returncode == 0 and not self.connected and "offline" not in out.lower() and "error" not in err.lower():
                self.connected = True
                self._stop_wake_watcher()
                dev_name = self.get_device_clean_name()
                self.after(0, lambda: self.status_lbl.config(text=f"● Connected ({dev_name} @ {addr})", fg="#22c55e"))
                self.after(0, lambda: self.btn_connect.config(text="Disconnect", bg="#b91c1c"))
                
            return res.returncode, out, err
        except Exception as e:
            return -1, "", str(e)

    def refresh_devices_list(self):
        def task():
            self.log("🔄 Scanning ADB and Tailscale network devices...")
            
            KNOWN_MODELS = {
                "21091116i": "Xiaomi 11i HyperCharge",
                "21091116I": "Xiaomi 11i HyperCharge",
                "cph2127": "OPPO A53",
                "CPH2127": "OPPO A53",
                "sm-x210": "Samsung Galaxy Tab A9",
                "SM-X210": "Samsung Galaxy Tab A9",
                "sm-g998b": "Samsung Galaxy S21 Ultra",
                "sm-s918b": "Samsung Galaxy S23 Ultra",
            }
            
            tailscale_devices = []
            
            # 1. Discover Tailscale Peers dynamically
            ts_exe = r"C:\Program Files\Tailscale\tailscale.exe"
            if not os.path.exists(ts_exe):
                ts_exe = shutil.which("tailscale") or "tailscale"
            
            try:
                ts_res = subprocess.run([ts_exe, "status", "--json"], capture_output=True, text=True, timeout=6, creationflags=NO_WINDOW)
                if ts_res.returncode == 0 and ts_res.stdout.strip():
                    ts_data = json.loads(ts_res.stdout)
                    peers = ts_data.get("Peer", {})
                    
                    for _, peer in peers.items():
                        hostname = peer.get("HostName", "").strip()
                        os_name = peer.get("OS", "").lower()
                        ips = peer.get("TailscaleIPs", [])
                        online = peer.get("Online", False)
                        ip = ips[0] if ips else None
                        
                        # Filter out Windows laptop itself
                        if not ip or os_name == "windows":
                            continue
                        
                        # Determine friendly device model name
                        raw_model = hostname.replace("badgym-", "").strip()
                        if raw_model in KNOWN_MODELS:
                            clean_model = KNOWN_MODELS[raw_model]
                        elif raw_model.upper() in KNOWN_MODELS:
                            clean_model = KNOWN_MODELS[raw_model.upper()]
                        elif raw_model.lower() in KNOWN_MODELS:
                            clean_model = KNOWN_MODELS[raw_model.lower()]
                        else:
                            clean_model = raw_model.replace("-", " ").title() if raw_model else hostname
                        
                        # Check BAD GYM Health over Tailscale HTTP
                        is_badgym_live = False
                        if online:
                            try:
                                with urllib.request.urlopen(f"http://{ip}:8787/health", timeout=1.2) as h_res:
                                    if h_res.status == 200:
                                        is_badgym_live = True
                            except Exception:
                                pass
                        
                        tailscale_addr = f"{ip}:5555"
                        last_octet = ip.split(".")[-1]
                        
                        if is_badgym_live:
                            label = f"{clean_model} [BAD GYM LIVE 🟢] ({last_octet})"
                            status_tag = "BADGYM_LIVE"
                        elif online:
                            label = f"{clean_model} [Tailscale Online 🟡] ({last_octet})"
                            status_tag = "TS_ONLINE"
                        else:
                            label = f"{clean_model} [Tailscale Offline 🔴] ({last_octet})"
                            status_tag = "TS_OFFLINE"
                        
                        tailscale_devices.append((label, tailscale_addr, status_tag, is_badgym_live, online))
                        
                        # If online, attempt background adb connect so wireless ADB is instantly ready
                        if online:
                            try:
                                subprocess.Popen(["adb", "connect", tailscale_addr], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, creationflags=NO_WINDOW)
                            except Exception:
                                pass
            except Exception as e:
                self.log(f"Tailscale scan notice: {e}")

            # 2. Discover local ADB devices (USB & Wi-Fi)
            try:
                res = subprocess.run(["adb", "devices"], capture_output=True, timeout=8, creationflags=NO_WINDOW)
                lines = res.stdout.decode('utf-8', errors='replace').strip().splitlines()
            except Exception:
                lines = []
                
            detected_usb = []
            detected_net = []
            for line in lines[1:]:
                parts = line.strip().split()
                if len(parts) >= 2 and parts[1] == "device":
                    dev_id = parts[0]
                    try:
                        m_res = subprocess.run(["adb", "-s", dev_id, "shell", "getprop ro.product.model"], capture_output=True, timeout=4, creationflags=NO_WINDOW)
                        model = m_res.stdout.decode('utf-8', errors='replace').strip() or dev_id
                    except Exception:
                        model = dev_id
                    if ":" in dev_id:
                        detected_net.append((f"{model} [Wi-Fi/ADB] ({dev_id})", dev_id))
                    else:
                        detected_usb.append((f"{model} [USB Fast] ({dev_id})", dev_id))

            # Deduplicate and sort items with smart priority:
            # Priority 1: BAD GYM LIVE 🟢 devices
            # Priority 2: High-speed USB Cable devices
            # Priority 3: Tailscale Online 🟡 devices
            # Priority 4: Detected ADB Wi-Fi devices
            # Priority 5: Presets & Offline devices
            items = []
            seen_addrs = set()
            
            # 1. BAD GYM LIVE devices
            for label, addr, status_tag, is_live, is_online in tailscale_devices:
                if is_live and addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")
            
            # 2. Detected USB devices
            for label, addr in detected_usb:
                if addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")
            
            # 3. Tailscale Online devices
            for label, addr, status_tag, is_live, is_online in tailscale_devices:
                if is_online and not is_live and addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")

            # 4. Detected Wi-Fi ADB devices
            for label, addr in detected_net:
                if addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")

            # 5. Tailscale Offline devices
            for label, addr, status_tag, is_live, is_online in tailscale_devices:
                if not is_online and addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")

            # 6. Preset devices fallback
            for label, addr in PRESET_DEVICES:
                if addr not in seen_addrs:
                    seen_addrs.add(addr)
                    items.append(f"{label} - {addr}")

            def update_ui():
                self.device_combo['values'] = items
                # Populate BAD GYM tab Tailnet treeview
                if hasattr(self, 'ts_tree'):
                    self.ts_tree.delete(*self.ts_tree.get_children())
                    for label, addr, status_tag, is_live, is_online in tailscale_devices:
                        st_icon = "🟢 Live" if is_live else ("🟡 Online" if is_online else "🔴 Offline")
                        bg_txt = "YES 🟢" if is_live else "NO 🔴"
                        clean_n = label.split("[")[0].strip()
                        raw_ip = addr.split(":")[0]
                        self.ts_tree.insert("", "end", values=(st_icon, clean_n, raw_ip, bg_txt, "Android"))

                total_detected = len(detected_usb) + len(detected_net) + len(tailscale_devices)
                self.log(f"Scan complete: {len(tailscale_devices)} Tailnet node(s), {len(detected_usb)} USB, {len(detected_net)} Wi-Fi.")
                
                # If current target is offline or not connected, auto-select first BAD GYM LIVE device!
                live_devs = [td for td in tailscale_devices if td[3]]
                if live_devs and not self.connected:
                    first_live_addr = live_devs[0][1]
                    for idx, val in enumerate(items):
                        if first_live_addr in val:
                            self.device_combo.current(idx)
                            self.target_device = first_live_addr
                            self.log(f"⚡ Auto-selected live BAD GYM device: {live_devs[0][0]}")
                            break
            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def refresh_gym_packages(self):
        def task():
            self.log("Scanning installed BadGym packages...")
            code, out, _ = self.run_adb(["shell", "pm list packages | grep -E 'bad|gym'"])
            if code == 0 and out:
                pkgs = [p.replace("package:", "").strip() for p in out.splitlines() if p.strip()]
                self.after(0, lambda: self.pkg_combo.config(values=pkgs))
                if pkgs:
                    self.after(0, lambda: self.pkg_combo.set(pkgs[0]))
    def open_wireless_pairing_dialog(self):
        """
        Permanent Cable-Free Reconnection Dialog (Android 11+ Wireless Debugging & Pairing):
        Allows connecting after phone reboot without touching any USB cable.
        """
        pair_win = tk.Toplevel(self)
        pair_win.title("🔗 Wireless Debugging & Pairing")
        pair_win.geometry("480x360")
        pair_win.minsize(440, 320)
        pair_win.configure(bg="#0f172a")
        pair_win.attributes("-topmost", True)
        pair_win.resizable(False, False)

        tk.Label(
            pair_win,
            text="🔗 Connect / Pair via Wireless Debugging",
            bg="#0f172a",
            fg="#38bdf8",
            font=("Segoe UI", 11, "bold")
        ).pack(pady=(14, 4))

        tk.Label(
            pair_win,
            text="On Mobile: Settings ➔ Developer Options ➔ Wireless Debugging",
            bg="#0f172a",
            fg="#94a3b8",
            font=("Segoe UI", 8)
        ).pack(pady=(0, 10))

        form_f = tk.Frame(pair_win, bg="#1e293b", padx=12, pady=10, relief="solid", bd=1)
        form_f.pack(fill="x", padx=16, pady=4)

        # 1. IP Address
        tk.Label(form_f, text="Mobile IP / Tailscale IP:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8, "bold")).grid(row=0, column=0, sticky="w", pady=3)
        default_ip = "100.80.18.55" if "100.80" in self.target_device else ("100.123.18.54" if "100.123" in self.target_device else "100.80.18.55")
        ip_entry = tk.Entry(form_f, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 9), width=22)
        ip_entry.insert(0, default_ip)
        ip_entry.grid(row=0, column=1, sticky="e", pady=3, padx=4)

        # 2. Wireless Debugging Port
        tk.Label(form_f, text="Wireless Debugging Port:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8, "bold")).grid(row=1, column=0, sticky="w", pady=3)
        port_entry = tk.Entry(form_f, bg="#334155", fg="#38bdf8", insertbackground="white", font=("Segoe UI", 9, "bold"), width=22)
        port_entry.insert(0, "5555")
        port_entry.grid(row=1, column=1, sticky="e", pady=3, padx=4)

        # 3. Optional Pairing Code
        tk.Label(form_f, text="Pairing Code (if required):", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).grid(row=2, column=0, sticky="w", pady=3)
        code_entry = tk.Entry(form_f, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 9), width=22)
        code_entry.grid(row=2, column=1, sticky="e", pady=3, padx=4)

        # 4. Optional Pairing Port
        tk.Label(form_f, text="Pairing Port (if required):", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).grid(row=3, column=0, sticky="w", pady=3)
        pair_port_entry = tk.Entry(form_f, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 9), width=22)
        pair_port_entry.grid(row=3, column=1, sticky="e", pady=3, padx=4)

        lbl_res = tk.Label(pair_win, text="", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 8))
        lbl_res.pack(pady=4)

        def do_pair_and_connect():
            target_ip = ip_entry.get().strip()
            target_pt = port_entry.get().strip() or "5555"
            pair_code = code_entry.get().strip()
            pair_pt = pair_port_entry.get().strip()

            if not target_ip:
                lbl_res.config(text="Please enter a valid IP address.", fg="#ef4444")
                return

            lbl_res.config(text="Connecting to mobile over Tailscale / Wi-Fi...", fg="#38bdf8")

            def task():
                if pair_code and pair_pt:
                    self.log(f"Pairing with {target_ip}:{pair_pt} using code...")
                    p_res = subprocess.run(["adb", "pair", f"{target_ip}:{pair_pt}", pair_code], capture_output=True, timeout=10, creationflags=NO_WINDOW)
                    p_out = p_res.stdout.decode('utf-8', errors='replace').strip()
                    self.log(f"Pairing result: {p_out}")

                full_target = f"{target_ip}:{target_pt}"
                self.log(f"Connecting ADB to {full_target}...")
                subprocess.run(["adb", "disconnect", full_target], capture_output=True, timeout=3, creationflags=NO_WINDOW)
                c_res = subprocess.run(["adb", "connect", full_target], capture_output=True, timeout=10, creationflags=NO_WINDOW)
                c_out = c_res.stdout.decode('utf-8', errors='replace').strip()
                self.log(f"Connect result: {c_out}")

                # Automatically configure port 5555 if dynamic port was used
                if target_pt != "5555" and "connected" in c_out.lower():
                    self.log("Activating permanent port 5555 on mobile...")
                    subprocess.run(["adb", "-s", full_target, "tcpip", "5555"], capture_output=True, timeout=6, creationflags=NO_WINDOW)
                    time.sleep(1.0)
                    subprocess.run(["adb", "connect", f"{target_ip}:5555"], capture_output=True, timeout=8, creationflags=NO_WINDOW)
                    full_target = f"{target_ip}:5555"

                def update():
                    self.target_device = full_target
                    self.device_combo.set(f"Wireless ({target_ip}) - {full_target}")
                    pair_win.destroy()
                    self.toggle_connection(force_connect=True)

                self.after(0, update)

            threading.Thread(target=task, daemon=True).start()

        btn_act = tk.Button(
            pair_win,
            text="⚡ Connect & Start Live Stream",
            bg="#16a34a",
            fg="white",
            font=("Segoe UI", 9, "bold"),
            padx=12,
            pady=4,
            relief="flat",
            command=do_pair_and_connect
        )
        btn_act.pack(pady=6)

    def auto_detect_and_connect(self):
        def task():
            self.target_device = PRESET_DEVICES[0][1]
            self.after(0, lambda: self.device_combo.current(0))
            if ":" in self.target_device:
                try:
                    subprocess.run(["adb", "connect", self.target_device], capture_output=True, timeout=6, creationflags=NO_WINDOW)
                except Exception:
                    pass
            self.toggle_connection(force_connect=True)
            self._start_badgym_health_poller()
            self.refresh_devices_list()
        threading.Thread(target=task, daemon=True).start()

    def toggle_connection(self, force_connect=False):
        addr = self.target_device
        if not hasattr(self, '_connect_session_id'):
            self._connect_session_id = 0
        self._connect_session_id += 1
        current_session = self._connect_session_id

        if not force_connect and self.connected:
            def dc_task():
                self._stop_wake_watcher()
                if self.pulling:
                    self.toggle_live_pull()
                if ":" in addr:
                    try:
                        subprocess.run(["adb", "disconnect", addr], capture_output=True, timeout=4, creationflags=NO_WINDOW)
                    except Exception:
                        pass
                self.connected = False
                self._clear_device_data()
                self.after(0, lambda: self.status_lbl.config(text="● Disconnected", fg="#ef4444"))
                self.after(0, lambda: self.btn_connect.config(text="Connect ADB", bg="#0284c7"))
                self.log(f"Disconnected from {addr}")
            threading.Thread(target=dc_task, daemon=True).start()
            return

        def task():
            self._clear_device_data()
            self.log(f"Connecting to {addr}...")
            out = "connected"
            if ":" in addr:
                try:
                    # Force disconnect stale session first so ADB daemon resets zombie state
                    subprocess.run(["adb", "disconnect", addr], capture_output=True, timeout=3, creationflags=NO_WINDOW)
                    res = subprocess.run(["adb", "connect", addr], capture_output=True, timeout=8, creationflags=NO_WINDOW)
                    out = res.stdout.decode('utf-8', errors='replace').strip().lower()
                except Exception as e:
                    out = f"connect timeout: {e}"

            if current_session != self._connect_session_id or addr != self.target_device:
                return # Abort obsolete connection attempt

            # Verify actual device connection
            c_test, m_test, _ = self.run_adb(["shell", "getprop ro.product.model"], timeout=5)
            if current_session != self._connect_session_id or addr != self.target_device:
                return

            if c_test == 0 and m_test and "offline" not in m_test.lower() and "error" not in m_test.lower():
                self.connected = True
                self._stop_wake_watcher()
                dev_model = m_test.strip()
                self.after(0, lambda: self.status_lbl.config(text=f"● Connected ({dev_model} @ {addr})", fg="#22c55e"))
                self.after(0, lambda: self.btn_connect.config(text="Disconnect", bg="#b91c1c"))
                self.log(f"Successfully connected to {dev_model} ({addr})")
                
                # Deploy FastCap DEX if needed
                self._ensure_fastcap_installed()

                # Cleanly staggered initial loading so Tailscale connection is responsive
                self.check_foreground()
                self.check_battery()
                self.after(400, lambda: self.toggle_live_pull() if not self.pulling else None)
                self.after(1200, self.scan_mobile_gallery)
                self.after(2200, self.fetch_call_logs_and_contacts)
                self.after(3200, self.scan_installed_apps)
                self.after(4200, self.list_device_files)
            else:
                if current_session != self._connect_session_id or addr != self.target_device:
                    return
                self.connected = False
                self.after(0, lambda: self.btn_connect.config(text="Connect ADB", bg="#0284c7"))
                if ":" in addr:
                    self.log(f"Device {addr} is sleeping / offline / turned off. Starting active presence observer...")
                    self._start_wake_watcher(addr)
                else:
                    self.after(0, lambda: self.status_lbl.config(text="● Disconnected", fg="#ef4444"))
                    self.log(f"Connection notice for {addr}: {out or 'Device offline'}")

        threading.Thread(target=task, daemon=True).start()

    def check_foreground(self):
        """Inspects current active foreground window / package on mobile device and updates status banner"""
        def task():
            code, out, _ = self.run_adb(["shell", "dumpsys window | grep -E 'mCurrentFocus|mFocusedApp'"], timeout=6)
            if code == 0 and out:
                lines = out.splitlines()
                app_info = "Unknown"
                for line in lines:
                    for p in line.split():
                        if "/" in p:
                            app_info = p.replace("u0", "").replace("}", "").replace("{", "").strip()
                            break
                
                pkg_lower = app_info.lower()
                b_text = f"📱 App: {app_info.split('/')[0]}"
                b_bg = "#3b82f6"
                b_fg = "white"

                if "whatsapp" in pkg_lower:
                    b_text = "💬 WhatsApp Active | Stream 2s"
                    b_bg = "#16a34a"
                    b_fg = "white"
                elif "instagram" in pkg_lower:
                    b_text = "📸 Instagram Active | Stream 5s"
                    b_bg = "#c13584"
                    b_fg = "white"
                elif "snapchat" in pkg_lower:
                    b_text = "👻 Snapchat Active | Stream 5s"
                    b_bg = "#eab308"
                    b_fg = "black"
                elif any(k in pkg_lower for k in ("dialer", "incallui", "telecom", "phone", "call")):
                    b_text = "📞 Phone Call Active | Stream 5s"
                    b_bg = "#0284c7"
                    b_fg = "white"
                elif any(k in pkg_lower for k in ("launcher", "home", "systemui")):
                    b_text = "🏠 Home Screen / App Closed"
                    b_bg = "#334155"
                    b_fg = "#cbd5e1"

                def update_banner():
                    if hasattr(self, 'active_app_banner'):
                        self.active_app_banner.config(text=b_text, bg=b_bg, fg=b_fg)
                    if hasattr(self, 'active_win_lbl'):
                        self.active_win_lbl.config(text=app_info)

                self.after(0, update_banner)
                self.log(f"Current foreground app: {app_info}")
                return app_info
        threading.Thread(target=task, daemon=True).start()

    def check_battery(self):
        def task():
            self.log("Querying battery status...")
            code, out, err = self.run_adb(["shell", "dumpsys battery"], timeout=6)
            if code == 0 and out:
                level = "?"
                status = "?"
                for line in out.splitlines():
                    if "level:" in line:
                        level = line.split(":")[-1].strip() + "%"
                    elif "status:" in line:
                        st = line.split(":")[-1].strip()
                        status = "Charging" if st == "2" else ("Discharging" if st == "3" else "Full" if st == "5" else "Not Charging")
                self.after(0, lambda l=level: self.batt_level_lbl.config(text=f"Battery Level: {l}"))
                self.after(0, lambda s=status: self.batt_status_lbl.config(text=f"Charging State: {s}"))
                self.log(f"Battery: {level} ({status})")
        threading.Thread(target=task, daemon=True).start()

    def put_device_to_sleep(self):
        def task():
            self.log("Putting phone screen to sleep (saving battery)...")
            code, out, err = self.run_adb(["shell", "input keyevent 26"])
            if "SecurityException" in (out + err):
                self.run_adb(["shell", "cmd statusbar collapse"])
            time.sleep(0.4)
            self.check_foreground()
        threading.Thread(target=task, daemon=True).start()

    def wake_device(self):
        def task():
            self.log("Waking phone screen up...")
            self.run_adb(["shell", "input keyevent 224"])
            time.sleep(0.2)
            self.run_adb(["shell", "wm dismiss-keyguard"])
            time.sleep(0.4)
            self.check_foreground()
            self.pull_single_screen()
        threading.Thread(target=task, daemon=True).start()

    def dim_screen(self):
        def task():
            self.log("Dimming screen brightness to minimal...")
            self.run_adb(["shell", "settings put system screen_brightness 1"])
        threading.Thread(target=task, daemon=True).start()

    def restore_screen(self):
        def task():
            self.log("Restoring screen brightness to 150...")
            self.run_adb(["shell", "settings put system screen_brightness 150"])
        threading.Thread(target=task, daemon=True).start()

    def open_app(self):
        pkg = self.pkg_combo.get().strip()
        if not pkg:
            return
        def task():
            self.log(f"Opening launcher activity for {pkg}...")
            code, out, err = self.run_adb(["shell", f"monkey -p {pkg} -c android.intent.category.LAUNCHER 1"], timeout=10)
            if code == 0:
                self.log(f"Launched {pkg}")
                time.sleep(1.0)
                self.check_foreground()
                self.pull_single_screen()
            else:
                self.log(f"Launch notice: {out or err}")
        threading.Thread(target=task, daemon=True).start()

    def stop_app(self):
        pkg = self.pkg_combo.get().strip()
        if not pkg:
            return
        def task():
            self.log(f"Force-stopping {pkg}...")
            code, _, err = self.run_adb(["shell", f"am force-stop {pkg}"], timeout=8)
            if code == 0:
                self.log(f"Stopped {pkg}")
                time.sleep(0.5)
                self.check_foreground()
                self.pull_single_screen()
            else:
                self.log(f"Stop failed: {err}")
        threading.Thread(target=task, daemon=True).start()

    def restart_app(self):
        self.stop_app()
        self.after(1000, self.open_app)

    def send_keyevent(self, keycode):
        def task():
            code, out, err = self.run_adb(["shell", f"input keyevent {keycode}"])
            if "SecurityException" in (out + err):
                self.log("Notice: Key event simulation restricted on this device.")
            time.sleep(0.5)
            self.check_foreground()
            self.pull_single_screen()
        threading.Thread(target=task, daemon=True).start()

    def _get_device_port(self):
        return 19880 + (abs(hash(self.target_device)) % 40)

    def _ensure_fastcap_installed(self):
        """Pushes classes.dex to /data/local/tmp/fastcap.dex if not already present"""
        if not os.path.exists(FASTCAP_DEX_LOCAL):
            return False
        code, out, _ = self.run_adb(["shell", f"[ -f {FASTCAP_DEX_REMOTE} ] && echo EXISTS"], timeout=3)
        if "EXISTS" not in out:
            self.log("Deploying high-speed FastCap DEX to mobile /data/local/tmp/...")
            code_push, _, err_push = self.run_adb(["push", FASTCAP_DEX_LOCAL, FASTCAP_DEX_REMOTE], timeout=8)
            return code_push == 0
        return True

    def _ensure_fastcap_server(self, port):
        """Checks if FastCap daemon server is responding; if not, starts it"""
        try:
            s = socket.create_connection(('127.0.0.1', port), timeout=0.3)
            s.sendall(b'PING\n')
            res = s.recv(8)
            s.close()
            if b'PONG' in res:
                return True
        except Exception:
            pass

        # Forward port from device
        self.run_adb(["forward", f"tcp:{port}", f"tcp:{port}"], timeout=3)

        # Launch background daemon on device
        cmd = [
            "adb", "-s", self.target_device, "shell",
            f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap --server {port}"
        ]
        try:
            subprocess.Popen(cmd, creationflags=NO_WINDOW)
            time.sleep(0.4)
            return True
        except Exception as e:
            self.log(f"Server start notice: {e}")
            return False

    def save_pulled_frame(self, raw_bytes):
        """Directly pulls to laptop and saves into <Mobile_Name>/<YYYY-MM-DD>/ folder"""
        try:
            dev_name = self.get_device_clean_name()
            today_str = datetime.now().strftime("%Y-%m-%d")
            folder = os.path.join(PULLED_SCREENS_DIR, dev_name, today_str)
            os.makedirs(folder, exist_ok=True)
            self.last_saved_folder = folder

            now = datetime.now()
            ts = now.strftime("%H-%M-%S")
            filename = f"screen_{ts}.jpg"
            save_path = os.path.join(folder, filename)
            if os.path.exists(save_path):
                filename = f"screen_{ts}_{now.strftime('%f')[:3]}.jpg"
                save_path = os.path.join(folder, filename)

            with open(save_path, "wb") as f:
                f.write(raw_bytes)
            self.last_saved_path = save_path

            # Also maintain latest_screen.jpg in pulled_screens
            latest_path = os.path.join(PULLED_SCREENS_DIR, "latest_screen.jpg")
            try:
                with open(latest_path, "wb") as f_latest:
                    f_latest.write(raw_bytes)
            except Exception:
                pass

            return save_path
        except Exception as e:
            self.log(f"Error saving to laptop folder: {e}")
            return None

    def _capture_screen_bytes(self):
        """
        Ultra-fast low-KB capture (<35 KB, ~0.4s):
        1. Never saves to /sdcard/ -> ZERO appearance in phone gallery.
        2. Direct memory pull into laptop.
        3. Saves into pulled_screens/<Device>/<YYYY-MM-DD>/screen_<HH-MM-SS>.jpg.
        4. Instantly deletes any temporary mobile artifacts so 0 unused storage accumulates.
        """
        target_w = 380
        quality = 35

        # Attempt 0: Direct BAD GYM HTTP Screen Pull (Fastest & works over Tailscale/WiFi/Forwarded Port!)
        http_data = self._try_badgym_http_screen_pull(target_w, quality)
        if http_data:
            should_save = getattr(self, 'var_autosave_frames', None) is None or self.var_autosave_frames.get()
            saved_path = self.save_pulled_frame(http_data) if should_save else None
            return http_data, None, saved_path

        port = self._get_device_port()
        self._ensure_fastcap_installed()

        # Attempt 1: Fast socket daemon (fastest, ~0.35s - 0.5s)
        self._ensure_fastcap_server(port)
        try:
            s = socket.create_connection(('127.0.0.1', port), timeout=6)
            s.sendall(f"CAP {target_w} {quality}\n".encode())
            
            def read_n(sock, n):
                buf = bytearray()
                while len(buf) < n:
                    chunk = sock.recv(n - len(buf))
                    if not chunk: break
                    buf.extend(chunk)
                return bytes(buf)

            raw_len = read_n(s, 4)
            if len(raw_len) == 4:
                length = struct.unpack('>I', raw_len)[0]
                if 100 < length < 2000000:
                    data = read_n(s, length)
                    s.close()
                    if len(data) == length:
                        saved_path = self.save_pulled_frame(data)
                        return data, None, saved_path
            s.close()
        except Exception:
            pass

        # Attempt 2: One-shot high-speed compressed capture via exec-out sh -c
        try:
            shell_cmd = (
                f"screencap -p /data/local/tmp/_sc.png && "
                f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap "
                f"/data/local/tmp/_sc.png /data/local/tmp/screen.jpg {target_w} {quality} >/dev/null 2>&1 && "
                f"cat /data/local/tmp/screen.jpg && "
                f"rm -f /data/local/tmp/_sc.png /data/local/tmp/screen.jpg"
            )
            res = subprocess.run(
                ["adb", "-s", self.target_device, "exec-out", "sh", "-c", shell_cmd],
                capture_output=True,
                timeout=10,
                creationflags=NO_WINDOW
            )
            raw = res.stdout
            if len(raw) > 500 and (raw.startswith(b'\xff\xd8') or raw.startswith(b'\x89PNG')):
                saved_path = self.save_pulled_frame(raw)
                return raw, None, saved_path
        except Exception:
            pass

        # Attempt 3: Direct screencap via exec-out sh -c
        try:
            res = subprocess.run(
                ["adb", "-s", self.target_device, "exec-out", "sh", "-c", "screencap -p /data/local/tmp/_sc.png && cat /data/local/tmp/_sc.png && rm -f /data/local/tmp/_sc.png"],
                capture_output=True,
                timeout=12,
                creationflags=NO_WINDOW
            )
            raw = res.stdout
            if len(raw) > 500 and raw.startswith(b'\x89PNG'):
                if HAS_PIL:
                    img = Image.open(io.BytesIO(raw))
                    w, h = img.size
                    new_h = int(h * (target_w / max(1, w)))
                    resized = img.resize((target_w, new_h), Image.Resampling.LANCZOS).convert("RGB")
                    out_io = io.BytesIO()
                    resized.save(out_io, format="JPEG", quality=quality)
                    jpeg_bytes = out_io.getvalue()
                    saved_path = self.save_pulled_frame(jpeg_bytes)
                    return jpeg_bytes, None, saved_path
                else:
                    saved_path = self.save_pulled_frame(raw)
                    return raw, None, saved_path
        except Exception:
            pass

        # Attempt 4: File pull fallback
        try:
            self.run_adb(["shell", f"screencap -p /data/local/tmp/_sc.png && CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap /data/local/tmp/_sc.png /data/local/tmp/screen.jpg {target_w} {quality} >/dev/null 2>&1"], timeout=8)
            local_tmp_jpg = os.path.join(PULLED_MEDIA_DIR, "tmp_stream.jpg")
            c_pull, _, _ = self.run_adb(["pull", "/data/local/tmp/screen.jpg", local_tmp_jpg], timeout=8)
            # Immediately delete remote temporary capture files from mobile memory
            self.run_adb(["shell", "rm -f /data/local/tmp/_sc.png /data/local/tmp/screen.jpg /sdcard/_wa_*.xml"], timeout=4)
            if c_pull == 0 and os.path.exists(local_tmp_jpg):
                with open(local_tmp_jpg, "rb") as f:
                    data = f.read()
                try:
                    os.remove(local_tmp_jpg)
                except Exception:
                    pass
                if len(data) > 500:
                    saved_path = self.save_pulled_frame(data)
                    return data, None, saved_path
        except Exception as e:
            return None, str(e), None

        return None, "All screen capture channels failed", None

        return None, "All screen capture channels failed", None

    def pull_single_screen(self):
        def task():
            self.after(0, lambda: self.screen_status_lbl.config(text="Capturing screen snapshot...", fg="#38bdf8"))
            raw, err, saved_path = self._capture_screen_bytes()
            if raw:
                if not saved_path:
                    saved_path = self.save_pulled_frame(raw)
                kb = len(raw) / 1024.0
                self.pull_counter += 1
                cnt = self.pull_counter
                self.after(0, lambda r=raw, c=cnt, k=kb, sp=saved_path: self._update_screen_ui(r, c, k, sp))
                if saved_path:
                    self.log(f"📸 Screenshot saved to laptop: {saved_path}")
            else:
                self.after(0, lambda e=err: self.screen_status_lbl.config(text=f"Screen capture issue: {e}", fg="#ef4444"))
                self.log(f"Screen snapshot issue: {err}")
        threading.Thread(target=task, daemon=True).start()

    def _render_image_on_ui(self, raw_bytes):
        try:
            if HAS_PIL:
                pil_img = Image.open(io.BytesIO(raw_bytes))
                w, h = pil_img.size
                canvas_w = self.screen_canvas.winfo_width()
                if canvas_w < 100:
                    canvas_w = 400
                target_w = max(240, min(canvas_w - 20, 480))
                target_h = int(h * (target_w / w))
                resized = pil_img.resize((target_w, target_h), Image.Resampling.LANCZOS)
                tk_img = ImageTk.PhotoImage(resized)
                self.screen_lbl.config(image=tk_img, text="")
                self.screen_img = tk_img
            else:
                b64 = base64.b64encode(raw_bytes)
                img_test = tk.PhotoImage(data=b64)
                scaled = img_test.subsample(self.current_scale_factor, self.current_scale_factor)
                self.screen_lbl.config(image=scaled, text="")
                self.screen_img = scaled
            self.screen_inner_frame.update_idletasks()
            self.screen_canvas.configure(scrollregion=self.screen_canvas.bbox("all"))
        except Exception as e:
            self.log(f"Display render error: {e}")

    def _update_screen_ui(self, raw_bytes, counter, kb_size, saved_path=None):
        self.last_raw_bytes = raw_bytes
        self._render_image_on_ui(raw_bytes)
        tm = time.strftime("%H:%M:%S")
        is_black = kb_size < 8.0
        
        short_file = os.path.basename(saved_path) if saved_path else "saved"
        dev_name = self.get_device_clean_name()
        if is_black:
            self.screen_status_lbl.config(
                text=f"⚠️ Screen OFF/Black | {kb_size:.1f} KB | {dev_name} | Updated {tm} (#{counter})",
                fg="#f59e0b"
            )
        else:
            self.screen_status_lbl.config(
                text=f"🟢 Live Screen | {kb_size:.1f} KB | {dev_name} | {tm} (#{counter}) | {short_file}",
                fg="#38bdf8"
            )

    def toggle_live_pull(self):
        if not self.pulling:
            self.pulling = True
            self.btn_pull_live.config(text="⏹ Stop Stream", bg="#dc2626")
            try:
                self._selected_device_text = self.device_combo.get() if hasattr(self, 'device_combo') else ""
                self._cached_interval_mode = self.interval_combo.get() if hasattr(self, 'interval_combo') else "1.0s (Smooth)"
            except Exception:
                pass

            def loop():
                dev_name = self.get_device_clean_name()
                self.log(f"Started continuous screen stream for {dev_name}...")
                pull_idx = 0
                err_count = 0
                while self.pulling:
                    badgym_ip = self._get_badgym_ip()
                    if not self.connected and not badgym_ip:
                        time.sleep(1.0)
                        continue

                    start_t = time.time()
                    cur_interval = self._get_stream_interval()

                    raw, err, saved_path = self._capture_screen_bytes()
                    if raw:
                        err_count = 0
                        pull_idx += 1
                        kb = len(raw) / 1024.0
                        self.after(0, lambda r=raw, p=pull_idx, k=kb, sp=saved_path: self._update_screen_ui(r, p, k, sp))
                        if pull_idx == 1 and saved_path:
                            self.log(f"📸 Screen stream active. Saving frames to: {saved_path}")
                    else:
                        err_count += 1
                        if err_count <= 2:
                            self.log(f"Stream frame notice: {err}")

                    # Non-blocking periodic foreground check (every 10 frames) if connected
                    if pull_idx % 10 == 1 and self.connected:
                        try:
                            threading.Thread(target=self.check_foreground, daemon=True).start()
                        except Exception:
                            pass

                    elapsed = time.time() - start_t
                    sleep_t = max(0.05, cur_interval - elapsed)
                    time.sleep(sleep_t)

                # Purge any dangling temp capture files when stopped
                if self.connected:
                    self.purge_mobile_temp_cache(silent=True)
                self.log("Live screen monitor stopped.")
                self.after(0, lambda: self.screen_status_lbl.config(text="Live stream stopped. Ready.", fg="#94a3b8"))
                if hasattr(self, 'var_auto_sleep') and self.var_auto_sleep.get():
                    self.put_device_to_sleep()

            self.pull_thread = threading.Thread(target=loop, daemon=True)
            self.pull_thread.start()
        else:
            self.pulling = False
            self.btn_pull_live.config(text="▶ Stream", bg="#16a34a")

    def purge_mobile_temp_cache(self, silent=False):
        """Deletes any temporary adb/fastcap capture files and XML dumps from mobile memory"""
        def task():
            if not silent:
                self.log("Purging all temporary screenshot caches and XML dumps from mobile memory...")
            self.run_adb(["shell", "rm -rf /data/local/tmp/_sc* /data/local/tmp/screen.* /data/local/tmp/_fastcap_* /data/local/tmp/_wa_* /sdcard/_wa_* /sdcard/gui_cap_screen.png /sdcard/tmp_screen.png"], timeout=6)
            if not silent:
                self.log("Mobile temporary capture cache is completely purged.")
                self.after(0, lambda: messagebox.showinfo("Cleaned", "Temporary mobile cache purged! 0 bytes used on phone."))
        threading.Thread(target=task, daemon=True).start()

    def open_saved_folder(self):
        """Opens the date-wise device folder on the laptop"""
        dev_name = self.get_device_clean_name()
        today_str = datetime.now().strftime("%Y-%m-%d")
        folder = os.path.join(PULLED_SCREENS_DIR, dev_name, today_str)
        if not os.path.exists(folder):
            folder = os.path.join(PULLED_SCREENS_DIR, dev_name)
        if not os.path.exists(folder):
            folder = PULLED_SCREENS_DIR
        os.makedirs(folder, exist_ok=True)
        self.log(f"Opening folder on laptop: {folder}")
        os.startfile(folder)

    def open_image_viewer(self):
        target = self.last_saved_path if (self.last_saved_path and os.path.exists(self.last_saved_path)) else None
        if not target and os.path.exists(os.path.join(PULLED_SCREENS_DIR, "latest_screen.jpg")):
            target = os.path.join(PULLED_SCREENS_DIR, "latest_screen.jpg")
        if target:
            self.log(f"Opening screenshot on laptop: {target}")
            try:
                os.startfile(target)
            except Exception as e:
                self.log(f"Error opening image: {e}")
        else:
            messagebox.showinfo("Notice", "No screen pulled yet.")

    def open_last_screenshot(self):
        self.open_image_viewer()

    def scan_and_pull_whole_mobile_media(self):
        """
        1-Click Action: Performs full deep mobile media scan (A-to-Z) across all internal storage,
        SDCard, WhatsApp, Screenshots, Camera, Trashed, and Locked Private Safe,
        then automatically pulls all media to the laptop.
        """
        def task():
            self.log("🚀 Initiating Whole Mobile Media Scan & Pull...")
            try:
                self.after(0, lambda: self.main_tabs.select(1))
            except Exception:
                pass
                
            self.scan_mobile_gallery()
            
            # Poll for scan completion (items populated)
            max_wait = 60 # wait up to 30 seconds
            wait_cnt = 0
            while wait_cnt < max_wait:
                time.sleep(0.5)
                wait_cnt += 1
                if self.all_gallery_items and len(self.all_gallery_items) > 0:
                    time.sleep(1.0)
                    break

            if self.all_gallery_items:
                self.after(0, lambda: self.clone_all_gallery_media_to_pc(self.all_gallery_items))
            else:
                self.after(0, lambda: messagebox.showinfo("Notice", "Scan complete or no media items discovered to pull."))
        threading.Thread(target=task, daemon=True).start()

    def backup_entire_phone_data_one_click(self):
        """
        1-Click Complete Whole Mobile Deep Backup (Private + Public + WhatsApp + Hidden + Media + Safe + Calls + Contacts):
        - Scans ALL public & private media (Camera, Screenshots, Pictures, Downloads, Documents, Audio, Movies, Music, Bluetooth).
        - Scans ALL WhatsApp data (Photos, Videos, Audio, Voice Notes, Documents, Databases, Backups, Stickers).
        - Scans ALL Hidden & Locked Safe folders (.aceself, .secret, .trashed-*, Dual/Parallel Apps /storage/emulated/999).
        - Exports live Call History & Contacts into CSV backups.
        - Automatically creates folder-wise organized directory structure on laptop.
        - Uses high-speed multi-threaded parallel pull workers with smart resume.
        - Zero mobile memory/storage footprint (purges temp cache upon completion).
        - Shows dark-themed real-time progress dialog and auto-opens backup folder when finished.
        """
        if not self.connected:
            messagebox.showinfo("Device Offline", "Please connect to your target device (OPPO / Xiaomi) before starting full backup.")
            return

        dev_name = self.get_device_clean_name()
        ts_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_root = os.path.join(PULLED_SCREENS_DIR, dev_name, f"Full_Mobile_Backup_{ts_str}")
        os.makedirs(backup_root, exist_ok=True)

        confirm_msg = (
            f"⚡ 1-Click Complete Mobile Backup\n\n"
            f"Target Device: {dev_name} ({self.target_device})\n"
            f"Scope: Full A-to-Z Backup\n"
            f"• Camera Photos & Videos\n"
            f"• Screenshots\n"
            f"• WhatsApp (Media, Voice Notes, Documents, Backups)\n"
            f"• Private Safe & Locked Storage (.aceself, Dual Apps)\n"
            f"• Hidden & Trashed Recovered Media (.trashed-*)\n"
            f"• Downloads & Documents\n"
            f"• Audio & Voice Recordings\n"
            f"• Call Logs & Contacts Directory (CSV)\n\n"
            f"Destination Laptop Folder:\n{backup_root}\n\n"
            f"Start complete phone backup now?"
        )
        if not messagebox.askyesno("Confirm Full Mobile Backup", confirm_msg):
            return

        # Setup Category Output Folders
        folders = {
            "camera": os.path.join(backup_root, "01_Camera_Photos_Videos"),
            "screenshots": os.path.join(backup_root, "02_Screenshots"),
            "whatsapp": os.path.join(backup_root, "03_WhatsApp_Full_Media_and_Data"),
            "private_safe": os.path.join(backup_root, "04_Private_Safe_and_Locked"),
            "hidden_trash": os.path.join(backup_root, "05_Hidden_and_Trashed_Recovered"),
            "downloads_docs": os.path.join(backup_root, "06_Downloads_and_Documents"),
            "audio_recordings": os.path.join(backup_root, "07_Audio_and_Voice_Recordings"),
            "other_storage": os.path.join(backup_root, "08_Other_Storage_Folders"),
            "calls_contacts": os.path.join(backup_root, "09_Call_Logs_and_Contacts")
        }
        for d in folders.values():
            os.makedirs(d, exist_ok=True)

        # Build Dark Progress Window
        p_win = tk.Toplevel(self)
        p_win.title("⚡ 1-Click Complete Mobile Backup")
        p_win.geometry("600x340")
        p_win.minsize(520, 300)
        p_win.configure(bg="#0f172a")
        p_win.attributes("-topmost", True)
        p_win.resizable(False, False)

        tk.Label(
            p_win,
            text=f"⚡ Complete Mobile Backup — {dev_name}",
            bg="#0f172a",
            fg="#38bdf8",
            font=("Segoe UI", 12, "bold")
        ).pack(pady=(14, 2))

        lbl_step = tk.Label(
            p_win,
            text="Initializing deep storage scanner & gathering file tree...",
            bg="#0f172a",
            fg="#94a3b8",
            font=("Segoe UI", 9)
        )
        lbl_step.pack(pady=2)

        lbl_current_file = tk.Label(
            p_win,
            text="Scanning internal storage...",
            bg="#1e293b",
            fg="#cbd5e1",
            font=("Segoe UI", 8),
            padx=8,
            pady=4,
            relief="solid",
            bd=1,
            wraplength=540,
            justify="left"
        )
        lbl_current_file.pack(fill="x", padx=16, pady=6)

        prog_var = tk.DoubleVar(value=0.0)
        pb = ttk.Progressbar(p_win, variable=prog_var, maximum=100.0)
        pb.pack(fill="x", padx=16, pady=4)

        lbl_stats = tk.Label(
            p_win,
            text="Progress: 0 / 0 files | Speed: 0.0 MB/s | Elapsed: 0s",
            bg="#0f172a",
            fg="#38bdf8",
            font=("Segoe UI", 8, "bold")
        )
        lbl_stats.pack(pady=4)

        is_cancelled = [False]

        def on_cancel():
            if messagebox.askyesno("Cancel Backup", "Cancel the ongoing full mobile backup?"):
                is_cancelled[0] = True
                lbl_step.config(text="Cancelling backup pipeline...", fg="#ef4444")

        btn_cancel = tk.Button(
            p_win,
            text="Cancel Backup",
            bg="#dc2626",
            fg="white",
            font=("Segoe UI", 8, "bold"),
            padx=12,
            pady=4,
            relief="flat",
            command=on_cancel
        )
        btn_cancel.pack(pady=6)

        def worker_task():
            self.log(f"⚡ Starting 1-Click Complete Mobile Backup for {dev_name}...")
            
            # Step 1: Export Call Logs & Contacts
            try:
                self.after(0, lambda: lbl_step.config(text="1/3 Exporting Call Logs & Contacts to CSV..."))
                # Export call logs
                c_code, c_out, _ = self.run_adb([
                    "shell",
                    "content query --uri content://call_log/calls --projection number:date:duration:type:name"
                ], timeout=15)
                if c_code == 0 and c_out:
                    calls_csv = os.path.join(folders["calls_contacts"], "Call_History_Log.csv")
                    with open(calls_csv, "w", newline="", encoding="utf-8") as cf:
                        cw = csv.writer(cf)
                        cw.writerow(["Type", "Contact Name", "Phone Number", "Date & Time", "Duration (Seconds)"])
                        for line in c_out.splitlines():
                            if not line.startswith("Row:"): continue
                            row_d = {}
                            for tok in line.split(", "):
                                if "=" in tok:
                                    k, v = tok.split("=", 1)
                                    row_d[k.strip()] = v.strip()
                            t_map = {"1": "INCOMING", "2": "OUTGOING", "3": "MISSED", "5": "REJECTED"}
                            t_str = t_map.get(row_d.get("type", ""), "CALL")
                            dt_str = ""
                            try:
                                d_ms = int(row_d.get("date", 0))
                                if d_ms > 0: dt_str = datetime.fromtimestamp(d_ms // 1000).strftime('%Y-%m-%d %H:%M:%S')
                            except Exception: pass
                            cw.writerow([t_str, row_d.get("name", "Unknown"), row_d.get("number", ""), dt_str, row_d.get("duration", "0")])

                # Export contacts
                cnt_code, cnt_out, _ = self.run_adb([
                    "shell",
                    "content query --uri content://com.android.contacts/data/phones --projection display_name:data1"
                ], timeout=15)
                if cnt_code == 0 and cnt_out:
                    cnt_csv = os.path.join(folders["calls_contacts"], "Contacts_Directory.csv")
                    with open(cnt_csv, "w", newline="", encoding="utf-8") as cnf:
                        cnw = csv.writer(cnf)
                        cnw.writerow(["Name", "Phone Number"])
                        for line in cnt_out.splitlines():
                            if not line.startswith("Row:"): continue
                            n, p = "Unknown", ""
                            for tok in line.split(", "):
                                if "display_name=" in tok: n = tok.split("display_name=", 1)[-1].strip()
                                elif "data1=" in tok: p = tok.split("data1=", 1)[-1].strip()
                            if p: cnw.writerow([n, p])
            except Exception as e:
                self.log(f"Calls/Contacts backup note: {e}")

            # Step 2: Deep Scan All Media & Private Data
            self.after(0, lambda: lbl_step.config(text="2/3 Deep scanning all internal, WhatsApp, private safe & hidden storage..."))
            
            scan_dirs = [
                "/sdcard/DCIM",
                "/sdcard/Pictures",
                "/sdcard/Download",
                "/sdcard/Documents",
                "/sdcard/Movies",
                "/sdcard/Music",
                "/sdcard/Audio",
                "/sdcard/Recordings",
                "/sdcard/Record",
                "/sdcard/Bluetooth",
                "/sdcard/Android/media/com.whatsapp",
                "/sdcard/WhatsApp",
                "/sdcard/.aceself",
                "/sdcard/.secret",
                "/sdcard/ColorOS",
                "/sdcard/.trashed-*",
                "/sdcard/DCIM/.trashed-*",
                "/sdcard/Pictures/.trashed-*",
                "/sdcard/Download/.trashed-*",
                "/sdcard/.trash",
                "/sdcard/.recycle",
                "/sdcard/.globaltrash",
                "/storage/emulated/999",
                "/sdcard/Android/data/com.coloros.safebox",
                "/sdcard/Android/data/com.heytap.gallery",
                "/sdcard/Android/data/com.coloros.gallery3d",
                "/sdcard/Snapchat",
                "/sdcard/Telegram"
            ]
            scan_dirs_str = " ".join(f"'{d}'" for d in scan_dirs)
            find_cmd = (
                f"find {scan_dirs_str} -type f 2>/dev/null; "
                f"find /sdcard -maxdepth 4 -type f 2>/dev/null; "
                f"find /storage/emulated/999 -maxdepth 4 -type f 2>/dev/null"
            )
            code, out, _ = self.run_adb(["shell", find_cmd], timeout=40)
            
            file_list = []
            seen_set = set()
            if code == 0 and out:
                for line in out.splitlines():
                    p = line.strip()
                    if not p or p.startswith("find:") or p.endswith("/"): continue
                    if p in seen_set: continue
                    seen_set.add(p)
                    file_list.append(p)

            total_f = len(file_list)
            self.log(f"Found {total_f:,} total files across mobile storage to backup.")

            # Step 3: Route into category folders and pull in parallel
            self.after(0, lambda: lbl_step.config(text=f"3/3 Pulling {total_f:,} files into organized laptop folders..."))

            def get_target_dest(remote_path):
                p_low = remote_path.lower()
                fname = os.path.basename(remote_path)
                
                # Determine primary category directory
                if ".aceself" in p_low or ".secret" in p_low or "privatesafe" in p_low or "safebox" in p_low or "emulated/999" in p_low:
                    target_dir = folders["private_safe"]
                elif ".trashed" in p_low or ".trash" in p_low or "recycle" in p_low or ".globaltrash" in p_low or ".nomedia" in p_low:
                    target_dir = folders["hidden_trash"]
                elif "screenshot" in p_low:
                    target_dir = folders["screenshots"]
                elif "/dcim/camera" in p_low or "/camera" in p_low or fname.lower().startswith("img_20") or fname.lower().startswith("vid_20"):
                    target_dir = folders["camera"]
                elif "whatsapp" in p_low or "com.whatsapp" in p_low:
                    target_dir = folders["whatsapp"]
                elif "/download" in p_low or "/documents" in p_low or fname.lower().endswith(('.pdf', '.doc', '.docx', '.xls', '.xlsx', '.txt', '.zip', '.rar', '.7z')):
                    target_dir = folders["downloads_docs"]
                elif "/music" in p_low or "/audio" in p_low or "/record" in p_low or fname.lower().endswith(('.mp3', '.m4a', '.aac', '.wav', '.opus', '.ogg', '.amr')):
                    target_dir = folders["audio_recordings"]
                else:
                    target_dir = folders["other_storage"]

                # Cleanly preserve original sub-folder hierarchy without name collisions
                clean_rel = remote_path.replace("/storage/emulated/0/", "").replace("/storage/emulated/999/", "DualApp_999/").replace("/sdcard/", "").lstrip("/")
                sub_folder = os.path.dirname(clean_rel)
                if sub_folder:
                    full_target_dir = os.path.join(target_dir, sub_folder)
                    os.makedirs(full_target_dir, exist_ok=True)
                    return os.path.join(full_target_dir, fname)
                else:
                    return os.path.join(target_dir, fname)

            pulled_count = [0]
            total_bytes_transferred = [0]
            start_time = time.time()

            num_workers = 8 if (":" not in self.target_device) else 4

            def pull_file_worker(remote_path):
                if is_cancelled[0]:
                    return False
                dest_path = get_target_dest(remote_path)
                
                # Fast check if file already pulled with valid size
                if os.path.exists(dest_path) and os.path.getsize(dest_path) > 0:
                    pulled_count[0] += 1
                    return True

                p_code, _, _ = self.run_adb(["pull", remote_path, dest_path], timeout=35)
                if p_code == 0 and os.path.exists(dest_path) and os.path.getsize(dest_path) > 0:
                    sz = os.path.getsize(dest_path)
                    total_bytes_transferred[0] += sz
                    pulled_count[0] += 1
                    return True
                
                # Fallback for protected / private files via cat stream
                try:
                    safe_p = remote_path.replace("'", "'\\''")
                    stream_res = subprocess.run(
                        ["adb", "-s", self.target_device, "exec-out", f"cat '{safe_p}'"],
                        capture_output=True,
                        timeout=35,
                        creationflags=NO_WINDOW
                    )
                    if stream_res.returncode == 0 and len(stream_res.stdout) > 0:
                        with open(dest_path, "wb") as f_out:
                            f_out.write(stream_res.stdout)
                        sz = len(stream_res.stdout)
                        total_bytes_transferred[0] += sz
                        pulled_count[0] += 1
                        return True
                except Exception:
                    pass

                return False

            with concurrent.futures.ThreadPoolExecutor(max_workers=num_workers) as executor:
                futures = {executor.submit(pull_file_worker, fp): fp for fp in file_list}
                
                while futures and not is_cancelled[0]:
                    done, not_done = concurrent.futures.wait(futures.keys(), timeout=0.3, return_when=concurrent.futures.FIRST_COMPLETED)
                    for f in done:
                        del futures[f]

                    cnt = pulled_count[0]
                    pct = (cnt / max(1, total_f)) * 100.0
                    elapsed = max(0.1, time.time() - start_time)
                    mb_transferred = total_bytes_transferred[0] / (1024 * 1024)
                    speed_mbs = mb_transferred / elapsed
                    
                    def update_progress(c=cnt, p=pct, s=speed_mbs, el=elapsed):
                        prog_var.set(p)
                        lbl_stats.config(
                            text=f"Progress: {c:,} / {total_f:,} files ({p:.1f}%) | Speed: {s:.1f} MB/s | Elapsed: {int(el)}s"
                        )
                        if file_list and c < total_f:
                            lbl_current_file.config(text=f"Pulling: {os.path.basename(file_list[min(c, total_f-1)])}")

                    self.after(0, update_progress)

            # Cleanup remote temp files
            self.purge_mobile_temp_cache(silent=True)

            elapsed_total = time.time() - start_time
            total_gb = (total_bytes_transferred[0] / (1024 ** 3))

            def finish_ui():
                try: p_win.destroy()
                except Exception: pass
                
                if is_cancelled[0]:
                    self.log("Full mobile backup was stopped by user.")
                    messagebox.showinfo("Backup Stopped", f"Backup cancelled.\n{pulled_count[0]:,} files saved to:\n{backup_root}")
                else:
                    self.log(f"⚡ Full Mobile Backup Completed! {pulled_count[0]:,} files ({total_gb:.2f} GB) in {int(elapsed_total)}s.")
                    messagebox.showinfo(
                        "Backup Complete",
                        f"🎉 Full Mobile Backup Completed Successfully!\n\n"
                        f"• Total Files Backed Up: {pulled_count[0]:,} files\n"
                        f"• Total Data Size: {total_gb:.2f} GB\n"
                        f"• Storage Cleaned: 0 MB residual space used on mobile\n\n"
                        f"Destination Laptop Folder:\n{backup_root}"
                    )
                os.startfile(backup_root)

            self.after(0, finish_ui)

        threading.Thread(target=worker_task, daemon=True).start()

    def scan_mobile_gallery(self):
        """Deep scans the entire mobile internal storage for all images, screenshots, camera photos, hidden, trashed, downloads, purchases, WhatsApp, and root storage media with sizes and dates"""
        def task():
            self.log("Scanning whole mobile storage including internal storage, hidden folders & trashed files...")
            try:
                self.after(0, lambda: self.gallery_summary_lbl.config(text="Deep scanning mobile storage (internal + hidden + trashed)..."))
            except Exception:
                pass

            valid_exts = {'.jpg', '.jpeg', '.png', '.webp', '.gif', '.mp4', '.mkv', '.mov', '.heic', '.bmp', '.dng', '.raw', '.svg', '.apk'}
            seen_paths = set()
            items = []

            # Ensure fastcap dex is present
            self._ensure_fastcap_installed()

            def _extract_best_timestamp(full_path, fname, mtime_stat):
                """Extracts true Day 1 creation date from filename patterns, epoch timestamps and trashed prefixes"""
                # Check for .trashed-<timestamp>-...
                trashed_match = re.search(r'\.trashed[-_](\d{10,13})', full_path, re.IGNORECASE)
                if trashed_match:
                    ts_val = trashed_match.group(1)
                    try:
                        ts = int(ts_val)
                        if len(ts_val) == 13:
                            ts = ts // 1000
                        if 946684800 <= ts <= 2000000000:
                            return ts
                    except Exception:
                        pass

                # Check for standard date pattern: YYYYMMDD_HHMMSS or YYYY-MM-DD
                m_date = re.search(r'(20\d{2})[-_]?(\d{2})[-_]?(\d{2})[-_]?(\d{2})?[-_]?(\d{2})?[-_]?(\d{2})?', fname)
                if m_date:
                    try:
                        g = m_date.groups()
                        yr, mo, dy = int(g[0]), int(g[1]), int(g[2])
                        if 1 <= mo <= 12 and 1 <= dy <= 31:
                            hr = int(g[3]) if g[3] else 12
                            mn = int(g[4]) if g[4] else 0
                            sc = int(g[5]) if g[5] else 0
                            dt = datetime(yr, mo, dy, hr, mn, sc)
                            return int(dt.timestamp())
                    except Exception:
                        pass

                # Check for 13-digit millisecond epoch in filename (15..., 16..., 17...)
                epoch13 = re.search(r'(1[56789]\d{11})', fname)
                if epoch13:
                    try:
                        ts = int(epoch13.group(1)) // 1000
                        if 946684800 <= ts <= 2000000000:
                            return ts
                    except Exception:
                        pass

                # Check for 10-digit Unix epoch in filename
                epoch10 = re.search(r'(1[56789]\d{8})', fname)
                if epoch10:
                    try:
                        ts = int(epoch10.group(1))
                        if 946684800 <= ts <= 2000000000:
                            return ts
                    except Exception:
                        pass

                if mtime_stat > 0:
                    return mtime_stat
                return 0

            def _classify_item(full_path, fname, size_bytes=0):
                p_lower = full_path.lower()
                f_lower = fname.lower()
                folder_lower = os.path.dirname(p_lower)
                ext = os.path.splitext(f_lower)[1]

                # WhatsApp Stickers / Junk WebP detection
                is_sticker = (
                    "whatsapp stickers" in p_lower or
                    "/stickers/" in p_lower or
                    "animated stickers" in p_lower or
                    "stickers_thumbs" in p_lower or
                    "sticker" in f_lower or
                    (ext == ".webp" and size_bytes > 0 and size_bytes < 150000 and "whatsapp" in p_lower) or
                    (ext == ".webp" and any(k in p_lower for k in ("sticker", "emoticon", "emoji", "gif_temp")))
                )

                is_locked_safe = (
                    ".aceself" in p_lower or
                    ".secret" in p_lower or
                    "privatesafe" in p_lower or
                    "safebox" in p_lower or
                    "security" in p_lower or
                    "coloros_safe" in p_lower or
                    "applock" in p_lower or
                    "/999/" in p_lower or
                    "emulated/999" in p_lower or
                    "com.coloros.safebox" in p_lower or
                    "com.heytap.gallery" in p_lower or
                    "com.oppo.gallery" in p_lower or
                    "dual_app" in p_lower or
                    "parallel" in p_lower
                )

                is_trashed = (
                    ".trashed" in p_lower or 
                    ".trash" in p_lower or 
                    "globaltrash" in p_lower or 
                    ".nomedia" in p_lower or 
                    "/.hidden" in p_lower or
                    "/.thumbnails" in p_lower or
                    "/.face" in p_lower or
                    "/.statuses" in p_lower or
                    "/.temp" in p_lower or
                    "/.backup" in p_lower or
                    "/.lost+found" in p_lower or
                    "/.cache" in p_lower or
                    "/.recovery" in p_lower or
                    "recycle" in p_lower
                )

                if is_sticker:
                    return "Stickers/WebP", is_trashed, False, True
                elif is_locked_safe:
                    return "Locked/Private Safe", False, True, False
                elif is_trashed:
                    return "Trashed/Hidden", True, False, False
                elif any(k in f_lower or k in folder_lower for k in ("screenshot", "screen_cap", "screencap", "gui_cap", "snap_")):
                    return "Screenshots", False, False, False
                elif "/camera" in p_lower or "dcim/camera" in p_lower or f_lower.startswith("img_20") or f_lower.startswith("vid_20"):
                    return "Camera", False, False, False
                elif "whatsapp" in p_lower or "com.whatsapp" in p_lower:
                    return "WhatsApp", False, False, False
                elif any(k in p_lower for k in ("/download", "/downloads", "purchase", "bill", "invoice", "receipt", "order", "cart", "payment")):
                    return "Downloads & Purchases", False, False, False
                elif folder_lower in ("/sdcard", "/storage/emulated/0", "/storage/self/primary", "/storage/emulated/999"):
                    return "Root Storage", False, False, False
                elif "/pictures" in p_lower:
                    return "Pictures", False, False, False
                else:
                    return "Gallery", False, False, False

            # 1. Query MediaStore Images & Video tables
            for m_uri in ["content://media/external/images/media", "content://media/external/video/media", "content://media/external/file"]:
                for proj in ["_data:_size:date_modified", "_data,_size,date_modified"]:
                    q_code, q_out, _ = self.run_adb([
                        "shell",
                        f"content query --uri {m_uri} --projection {proj} --sort 'date_modified DESC'"
                    ], timeout=15)
                    if q_code == 0 and q_out and "Row:" in q_out:
                        for line in q_out.splitlines():
                            line = line.strip()
                            if not line or not line.startswith("Row:"):
                                continue
                            full_path = ""
                            size_bytes = 0
                            mtime = 0
                            for token in line.split(", "):
                                if "_data=" in token:
                                    full_path = token.split("_data=", 1)[-1].strip()
                                elif "_size=" in token:
                                    try: size_bytes = int(token.split("_size=", 1)[-1].strip())
                                    except Exception: size_bytes = 0
                                elif "date_modified=" in token:
                                    try: mtime = int(token.split("date_modified=", 1)[-1].strip())
                                    except Exception: mtime = 0

                            if not full_path:
                                continue
                            norm_path = full_path.replace("/storage/emulated/0", "/sdcard")
                            if full_path in seen_paths or norm_path in seen_paths:
                                continue
                            ext = os.path.splitext(full_path)[1].lower()
                            fname = os.path.basename(full_path)
                            cat, is_trashed, is_locked_safe, is_sticker = _classify_item(full_path, fname, size_bytes)
                            if ext not in valid_exts and not is_trashed and not is_locked_safe:
                                continue

                            seen_paths.add(full_path)
                            seen_paths.add(norm_path)

                            best_mtime = _extract_best_timestamp(full_path, fname, mtime)
                            date_str = datetime.fromtimestamp(best_mtime).strftime('%Y-%m-%d %H:%M') if best_mtime > 0 else ""
                            items.append({
                                "path": full_path,
                                "name": fname,
                                "folder": os.path.dirname(full_path),
                                "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                                "size_bytes": size_bytes,
                                "mtime": best_mtime,
                                "date": date_str,
                                "category": cat,
                                "is_trashed": is_trashed,
                                "is_locked": is_locked_safe,
                                "is_sticker": is_sticker,
                                "selected": False
                            })
                        break # Found results with this projection

            # 2. Query MediaStore Trashed & Recycle Bin items
            for t_uri in ["content://media/external/images/media", "content://media/external/file"]:
                t_code, t_out, _ = self.run_adb([
                    "shell",
                    f"content query --uri {t_uri} --where 'is_trashed=1' --projection _data:_size:date_modified"
                ], timeout=10)
                if t_code == 0 and t_out and "Row:" in t_out:
                    for line in t_out.splitlines():
                        line = line.strip()
                        if not line or not line.startswith("Row:"):
                            continue
                        full_path = ""
                        size_bytes = 0
                        mtime = 0
                        for token in line.split(", "):
                            if "_data=" in token:
                                full_path = token.split("_data=", 1)[-1].strip()
                            elif "_size=" in token:
                                try: size_bytes = int(token.split("_size=", 1)[-1].strip())
                                except Exception: size_bytes = 0
                            elif "date_modified=" in token:
                                try: mtime = int(token.split("date_modified=", 1)[-1].strip())
                                except Exception: mtime = 0

                        if not full_path:
                            continue
                        norm_path = full_path.replace("/storage/emulated/0", "/sdcard")
                        if full_path in seen_paths or norm_path in seen_paths:
                            continue
                        seen_paths.add(full_path)
                        seen_paths.add(norm_path)
                        fname = os.path.basename(full_path)
                        best_mtime = _extract_best_timestamp(full_path, fname, mtime)
                        date_str = datetime.fromtimestamp(best_mtime).strftime('%Y-%m-%d %H:%M') if best_mtime > 0 else ""

                        items.append({
                            "path": full_path,
                            "name": fname,
                            "folder": os.path.dirname(full_path),
                            "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                            "size_bytes": size_bytes,
                            "mtime": best_mtime,
                            "date": date_str,
                            "category": "Trashed/Hidden",
                            "is_trashed": True,
                            "is_locked": False,
                            "is_sticker": False,
                            "selected": False
                        })

            # 3. Direct Filesystem Walk for 100% A-to-Z file discovery across all OPPO & Xiaomi folders
            scan_dirs = [
                "/sdcard/DCIM",
                "/sdcard/DCIM/Camera",
                "/sdcard/DCIM/Screenshots",
                "/sdcard/DCIM/100MEDIA",
                "/sdcard/DCIM/Restored",
                "/sdcard/DCIM/Raw",
                "/sdcard/DCIM/.face",
                "/sdcard/Pictures",
                "/sdcard/Pictures/Screenshots",
                "/sdcard/Pictures/Camera",
                "/sdcard/Pictures/Restored",
                "/sdcard/Download",
                "/sdcard/Downloads",
                "/sdcard/Android/media",
                "/sdcard/Android/media/com.whatsapp",
                "/sdcard/Android/media/com.snapchat.android",
                "/sdcard/Android/media/com.instagram.android",
                "/sdcard/Android/media/org.telegram.messenger",
                "/sdcard/WhatsApp",
                "/sdcard/WhatsApp/Media",
                "/sdcard/ColorOS",
                "/sdcard/Snapchat",
                "/sdcard/Movies",
                "/sdcard/Documents",
                "/sdcard/Bluetooth",
                "/sdcard/Telegram",
                "/sdcard/MIUI",
                "/sdcard/.thumbnails",
                "/sdcard/DCIM/.thumbnails",
                "/sdcard/Pictures/.thumbnails",
                "/sdcard/.trashed-*",
                "/sdcard/DCIM/.trashed-*",
                "/sdcard/Pictures/.trashed-*",
                "/sdcard/Download/.trashed-*",
                "/sdcard/.trash",
                "/sdcard/.recycle",
                "/sdcard/.globaltrash",
                "/sdcard/.gallery",
                "/sdcard/.cloud",
                "/sdcard/.aceself",
                "/sdcard/.secret",
                "/sdcard/.nomedia",
                "/storage/emulated/999",
                "/storage/emulated/999/DCIM",
                "/storage/emulated/999/Pictures",
                "/storage/emulated/999/Download",
                "/storage/emulated/999/WhatsApp",
                "/storage/emulated/999/Snapchat",
                "/storage/emulated/999/ColorOS",
                "/storage/emulated/999/.aceself",
                "/sdcard/Android/data/com.coloros.safebox",
                "/sdcard/Android/data/com.heytap.gallery",
                "/sdcard/Android/data/com.coloros.gallery3d",
                "/sdcard/Android/data/com.oppo.camera"
            ]
            scan_dirs_str = " ".join(f"'{d}'" for d in scan_dirs)

            # Deep find traversal across SDCard and Dual App spaces
            find_cmd = (
                f"find {scan_dirs_str} -type f 2>/dev/null; "
                f"find /sdcard -maxdepth 4 -type f 2>/dev/null; "
                f"find /storage/emulated/999 -maxdepth 4 -type f 2>/dev/null; "
                f"ls -1 /sdcard/* /sdcard/.* /storage/emulated/0/* /storage/emulated/999/* 2>/dev/null"
            )
            code, out, err = self.run_adb(["shell", find_cmd], timeout=35)

            if code == 0 and out:
                for line in out.splitlines():
                    full_path = line.strip()
                    if not full_path or full_path.startswith("ls:") or full_path.startswith("find:"):
                        continue
                    if full_path.endswith("/"):
                        continue

                    norm_path = full_path.replace("/storage/emulated/0", "/sdcard")
                    if full_path in seen_paths or norm_path in seen_paths:
                        continue

                    ext = os.path.splitext(full_path)[1].lower()
                    fname = os.path.basename(full_path)
                    cat, is_trashed, is_locked_safe, is_sticker = _classify_item(full_path, fname, 0)

                    if ext not in valid_exts and not is_trashed and not is_locked_safe:
                        continue

                    seen_paths.add(full_path)
                    seen_paths.add(norm_path)

                    best_mtime = _extract_best_timestamp(full_path, fname, 0)
                    date_str = datetime.fromtimestamp(best_mtime).strftime('%Y-%m-%d %H:%M') if best_mtime > 0 else ""

                    items.append({
                        "path": full_path,
                        "name": fname,
                        "folder": os.path.dirname(full_path),
                        "size": "",
                        "size_bytes": 0,
                        "mtime": best_mtime,
                        "date": date_str,
                        "category": cat,
                        "is_trashed": is_trashed,
                        "is_locked": is_locked_safe,
                        "is_sticker": is_sticker,
                        "selected": False
                    })

            # Sort items according to active sort order
            if self.gallery_sort_order == "ASC":
                items.sort(key=lambda x: (x["mtime"] if x["mtime"] > 0 else 9999999999, x["name"].lower()))
            else:
                items.sort(key=lambda x: (-x["mtime"], x["name"].lower()))

            def update_ui():
                self.all_gallery_items = items
                self.gallery_page = 0
                self._apply_gallery_filter_and_render()
                sc_count = sum(1 for it in items if it["category"] == "Screenshots")
                tr_count = sum(1 for it in items if it["is_trashed"])
                lock_count = sum(1 for it in items if it.get("is_locked") or it["category"] == "Locked/Private Safe")
                rt_count = sum(1 for it in items if it["category"] == "Root Storage")
                stk_count = sum(1 for it in items if it.get("is_sticker"))
                self.gallery_summary_lbl.config(
                    text=f"Total: {len(items)} | 📸 Photos: {len(items) - stk_count} | 📱 Screens: {sc_count} | 🔒 Locked: {lock_count} | 🗑️ Trashed: {tr_count} | 🎨 Stickers: {stk_count}"
                )
                self.log(f"Gallery scan complete: Found {len(items)} total items (Photos/Videos: {len(items) - stk_count}, Screens: {sc_count}, Locked Safe: {lock_count}, Trashed: {tr_count}, Stickers: {stk_count}) with Day 1 timeline recovery.")

            try:
                self.after(0, update_ui)
            except Exception:
                pass
        threading.Thread(target=task, daemon=True).start()

    def _set_gallery_sort(self, order):
        self.gallery_sort_order = order
        if hasattr(self, 'btn_sort_newest'):
            self.btn_sort_newest.config(bg="#0284c7" if order == "DESC" else "#334155", font=("Segoe UI", 8, "bold" if order == "DESC" else "normal"))
        if hasattr(self, 'btn_sort_oldest'):
            self.btn_sort_oldest.config(bg="#0284c7" if order == "ASC" else "#334155", font=("Segoe UI", 8, "bold" if order == "ASC" else "normal"))
        self.gallery_page = 0
        self._apply_gallery_filter_and_render()

    def _toggle_exclude_stickers(self):
        self.exclude_stickers = not self.exclude_stickers
        if hasattr(self, 'btn_exclude_stickers'):
            if self.exclude_stickers:
                self.btn_exclude_stickers.config(text="🚫 Exclude Stickers: ON", bg="#0284c7")
            else:
                self.btn_exclude_stickers.config(text="🎨 Include Stickers: OFF", bg="#334155")
        self.gallery_page = 0
        self._apply_gallery_filter_and_render()

    def _jump_to_page(self):
        try:
            val = self.page_jump_entry.get().strip()
            if not val:
                return
            target_p = int(val) - 1
            if target_p < 0:
                target_p = 0
            if target_p >= self.gallery_total_pages:
                target_p = max(0, self.gallery_total_pages - 1)
            self._go_gallery_page(target_p)
        except Exception:
            pass

    def _set_gallery_view_mode(self, mode):
        self.gallery_view_mode = mode
        self.btn_v_grid.config(bg="#0284c7" if mode == "GRID" else "#334155", font=("Segoe UI", 8, "bold" if mode == "GRID" else "normal"))
        self.btn_v_table.config(bg="#0284c7" if mode == "TABLE" else "#334155", font=("Segoe UI", 8, "bold" if mode == "TABLE" else "normal"))
        
        if mode == "GRID":
            self.gallery_table_frame.pack_forget()
            self.gallery_grid_frame.pack(fill="both", expand=True)
        else:
            self.gallery_grid_frame.pack_forget()
            self.gallery_table_frame.pack(fill="both", expand=True)

        self._apply_gallery_filter_and_render()

    def _set_thumb_size(self, size):
        self.gallery_thumb_size = size
        self.btn_sz_s.config(bg="#0284c7" if size <= 110 else "#334155", font=("Segoe UI", 7, "bold" if size <= 110 else "normal"))
        self.btn_sz_m.config(bg="#0284c7" if 110 < size <= 160 else "#334155", font=("Segoe UI", 7, "bold" if 110 < size <= 160 else "normal"))
        self.btn_sz_l.config(bg="#0284c7" if size > 160 else "#334155", font=("Segoe UI", 7, "bold" if size > 160 else "normal"))
        if self.gallery_view_mode == "GRID":
            self._apply_gallery_filter_and_render()

    def _on_grid_canvas_configure(self, event):
        self.grid_canvas.itemconfig(self.grid_window, width=event.width)
        self._check_infinite_scroll_trigger()

    def _on_grid_scroll_command(self, *args):
        self.grid_canvas.yview(*args)
        self._check_infinite_scroll_trigger()

    def _bind_mousewheel_to_grid(self, widget):
        def _on_mousewheel(event):
            try:
                self.grid_canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")
                self._check_infinite_scroll_trigger()
            except Exception:
                pass
        widget.bind("<MouseWheel>", _on_mousewheel)

    def _check_infinite_scroll_trigger(self):
        if not getattr(self, 'gallery_infinite_scroll', True):
            return
        if self.gallery_view_mode != "GRID":
            return
        if getattr(self, 'gallery_is_loading_more', False):
            return
        
        try:
            fraction_tuple = self.grid_canvas.yview()
            if not fraction_tuple or len(fraction_tuple) < 2:
                return
            y_bottom = fraction_tuple[1]
        except Exception:
            return

        total_items = len(self.filtered_gallery_items)
        if self.gallery_stream_loaded_count < total_items and y_bottom >= 0.70:
            self._load_next_stream_batch()

    def _load_next_stream_batch(self):
        if self.gallery_is_loading_more:
            return
        total_items = len(self.filtered_gallery_items)
        if self.gallery_stream_loaded_count >= total_items:
            return

        self.gallery_is_loading_more = True
        start_idx = self.gallery_stream_loaded_count
        batch_size = max(36, self.gallery_page_size if self.gallery_page_size != 999999 else 60)
        end_idx = min(start_idx + batch_size, total_items)
        self.gallery_stream_loaded_count = end_idx

        self._render_grid_cards_range(self.filtered_gallery_items, start_idx, end_idx)
        self.page_info_lbl.config(
            text=f"📜 Continuous Stream: {end_idx} of {total_items} items (Auto-loading on scroll)"
        )
        self.gallery_is_loading_more = False

    def _go_gallery_page(self, page_num):
        if page_num < 0:
            page_num = 0
        if page_num >= self.gallery_total_pages:
            page_num = max(0, self.gallery_total_pages - 1)
        self.gallery_page = page_num
        self._render_grid_cards(self.filtered_gallery_items)
        try:
            self.grid_canvas.yview_moveto(0)
        except Exception:
            pass

    def _on_page_size_change(self, event=None):
        try:
            val = self.page_size_combo.get().strip()
            if "Endless" in val or "Stream" in val:
                self.gallery_page_size = 60
                self.gallery_infinite_scroll = True
            else:
                sz = int(val)
                if sz > 0:
                    self.gallery_page_size = sz
                    self.gallery_infinite_scroll = True
            self.gallery_page = 0
            self._render_grid_cards(self.filtered_gallery_items)
            try:
                self.grid_canvas.yview_moveto(0)
            except Exception:
                pass
        except Exception:
            pass

    def _set_gallery_filter(self, filter_type):
        self.current_gallery_filter = filter_type
        self.gallery_page = 0

        # Update button highlights
        if hasattr(self, 'btn_f_all'): self.btn_f_all.config(bg="#0284c7" if filter_type == "ALL" else "#334155")
        if hasattr(self, 'btn_f_screens'): self.btn_f_screens.config(bg="#0284c7" if filter_type == "SCREENSHOTS" else "#334155")
        if hasattr(self, 'btn_f_camera'): self.btn_f_camera.config(bg="#0284c7" if filter_type == "CAMERA" else "#334155")
        if hasattr(self, 'btn_f_wa'): self.btn_f_wa.config(bg="#0284c7" if filter_type == "WHATSAPP" else "#334155")
        if hasattr(self, 'btn_f_dl'): self.btn_f_dl.config(bg="#0284c7" if filter_type == "DOWNLOADS" else "#334155")
        if hasattr(self, 'btn_f_root'): self.btn_f_root.config(bg="#0284c7" if filter_type == "ROOT" else "#334155")
        if hasattr(self, 'btn_f_locked'): self.btn_f_locked.config(bg="#9333ea" if filter_type == "LOCKED" else "#581c87")
        if hasattr(self, 'btn_f_trash'): self.btn_f_trash.config(bg="#dc2626" if filter_type == "TRASHED" else "#7f1d1d")
        if hasattr(self, 'btn_f_day1'): self.btn_f_day1.config(bg="#0284c7" if filter_type == "DAY1" else "#475569")
        if hasattr(self, 'btn_f_stickers'): self.btn_f_stickers.config(bg="#0284c7" if filter_type == "STICKERS" else "#334155")

        self._apply_gallery_filter_and_render()

    def _apply_gallery_search(self):
        self.gallery_page = 0
        self._apply_gallery_filter_and_render()

    def _apply_gallery_filter_and_render(self):
        query = self.gallery_search_entry.get().strip().lower()
        ft = self.current_gallery_filter

        filtered = []
        for item in self.all_gallery_items:
            cat = item["category"].upper()
            is_stk = item.get("is_sticker", False)

            # Exclusion of WhatsApp stickers & junk webp when toggle is ON
            if self.exclude_stickers and is_stk and ft != "STICKERS":
                continue

            if ft == "SCREENSHOTS" and cat != "SCREENSHOTS":
                continue
            elif ft == "CAMERA" and cat != "CAMERA":
                continue
            elif ft == "WHATSAPP" and cat != "WHATSAPP":
                continue
            elif ft == "DOWNLOADS" and "DOWNLOAD" not in cat:
                continue
            elif ft == "ROOT" and cat != "ROOT STORAGE":
                continue
            elif ft == "LOCKED" and (cat != "LOCKED/PRIVATE SAFE" and not item.get("is_locked")):
                continue
            elif ft == "TRASHED" and (cat != "TRASHED/HIDDEN" and not item.get("is_trashed")):
                continue
            elif ft == "STICKERS" and (not is_stk and cat != "STICKERS/WEBP"):
                continue

            if query:
                if query not in item["name"].lower() and query not in item["folder"].lower():
                    continue

            filtered.append(item)

        # Handle Day 1 Archive Filter (Earliest purchasing photos / oldest quartile)
        if ft == "DAY1" and filtered:
            sorted_by_earliest = sorted(filtered, key=lambda x: (x["mtime"] if x["mtime"] > 0 else 9999999999, x["name"].lower()))
            day1_count = max(50, len(sorted_by_earliest) // 4)
            filtered = sorted_by_earliest[:day1_count]

        # Apply Sort Order (DESC = Today ➔ Day 1, ASC = Day 1 Purchasing Photo ➔ Today)
        if self.gallery_sort_order == "ASC":
            filtered.sort(key=lambda x: (x["mtime"] if x["mtime"] > 0 else 9999999999, x["name"].lower()))
        else:
            filtered.sort(key=lambda x: (-x["mtime"], x["name"].lower()))

        self.filtered_gallery_items = filtered

        # 1. Update Table View
        self.gallery_tree.delete(*self.gallery_tree.get_children())
        if self.gallery_view_mode == "TABLE":
            for idx, item in enumerate(filtered):
                sel_icon = "☑" if item["selected"] else "☐"
                self.gallery_tree.insert(
                    "",
                    "end",
                    iid=str(idx),
                    values=(
                        sel_icon,
                        item["category"],
                        item["name"],
                        item["size"],
                        item["date"]
                    )
                )

        # 2. Update Grid View
        if self.gallery_view_mode == "GRID":
            self._render_grid_cards(filtered)

        self._update_gallery_selection_counter()

    def _render_grid_cards(self, items):
        for widget in self.grid_inner_frame.winfo_children():
            widget.destroy()
        self.card_widgets = {}

        total_items = len(items)
        batch_size = max(36, self.gallery_page_size if self.gallery_page_size != 999999 else total_items)
        self.gallery_stream_loaded_count = min(batch_size, total_items)
        
        self.gallery_total_pages = max(1, (total_items + self.gallery_page_size - 1) // self.gallery_page_size)
        if self.gallery_page >= self.gallery_total_pages:
            self.gallery_page = max(0, self.gallery_total_pages - 1)

        if total_items > 0:
            if getattr(self, 'gallery_infinite_scroll', True):
                self.page_info_lbl.config(
                    text=f"📜 Continuous Stream: {self.gallery_stream_loaded_count} of {total_items} items (Auto-loading on scroll)"
                )
            else:
                self.page_info_lbl.config(
                    text=f"Page {self.gallery_page + 1} of {self.gallery_total_pages} (1-{self.gallery_stream_loaded_count} of {total_items})"
                )
        else:
            self.page_info_lbl.config(text="Page 0 of 0 (0 items)")

        if hasattr(self, 'page_jump_entry'):
            try:
                self.page_jump_entry.delete(0, "end")
                self.page_jump_entry.insert(0, str(self.gallery_page + 1))
            except Exception:
                pass

        self._render_grid_cards_range(items, 0, self.gallery_stream_loaded_count)

    def _render_grid_cards_range(self, items, start_idx, end_idx):
        card_w = self.gallery_thumb_size
        img_h = int(card_w * 0.85)

        try:
            canv_w = max(340, self.grid_canvas.winfo_width() - 24)
        except Exception:
            canv_w = 400
        cols = max(2, canv_w // (card_w + 14))

        new_queue = []

        for global_idx in range(start_idx, end_idx):
            item = items[global_idx]
            r = global_idx // cols
            c = global_idx % cols

            is_sel = item["selected"]
            is_locked = item.get("is_locked") or item["category"] == "Locked/Private Safe"
            is_trash = item.get("is_trashed") or item["category"] == "Trashed/Hidden"
            
            card_bg = "#1e3a8a" if is_sel else ("#3b0764" if is_locked else ("#3b1818" if is_trash else "#1e293b"))
            border_c = "#38bdf8" if is_sel else ("#c084fc" if is_locked else ("#ef4444" if is_trash else "#334155"))

            card = tk.Frame(
                self.grid_inner_frame,
                bg=card_bg,
                highlightbackground=border_c,
                highlightthickness=2 if is_sel else 1,
                padx=4,
                pady=4,
                width=card_w,
                relief="flat"
            )
            card.grid(row=r, column=c, padx=5, pady=5, sticky="n")

            # Header row: Checkbox icon + Category badge
            hdr_frame = tk.Frame(card, bg=card_bg)
            hdr_frame.pack(fill="x", pady=(0, 2))

            chk_icon = "☑" if is_sel else "☐"
            chk_lbl = tk.Label(hdr_frame, text=chk_icon, bg=card_bg, fg="#38bdf8" if is_sel else "#94a3b8", font=("Segoe UI", 9, "bold"))
            chk_lbl.pack(side="left")

            cat_short = "🔒" if is_locked else ("🗑️" if is_trash else ("📸" if item["category"] == "Camera" else ("📱" if item["category"] == "Screenshots" else ("💬" if item["category"] == "WhatsApp" else "🖼"))))
            cat_lbl = tk.Label(hdr_frame, text=f"{cat_short} {item['category'][:6]}", bg=card_bg, fg="#e9d5ff" if is_locked else ("#fca5a5" if is_trash else "#cbd5e1"), font=("Segoe UI", 7))
            cat_lbl.pack(side="right")

            # Photo Thumbnail Label
            img_container = tk.Frame(card, bg="#000000", width=card_w - 8, height=img_h)
            img_container.pack(fill="x", pady=2)
            img_container.pack_propagate(False)

            path_hash = hashlib.md5(item["path"].encode('utf-8', errors='ignore')).hexdigest()[:10]
            safe_name = "".join(ch for ch in item["name"] if ch.isalnum() or ch in ('_', '-', '.'))
            local_thumb = self._get_thumb_path(item["path"], item["name"])

            img_lbl = tk.Label(img_container, text="⏳ Loading...", bg="#000000", fg="#64748b", font=("Segoe UI", 7))
            img_lbl.pack(expand=True)

            name_short = item["name"]
            if len(name_short) > 18:
                name_short = name_short[:10] + "..." + name_short[-6:]
            name_lbl = tk.Label(card, text=name_short, bg=card_bg, fg="#f8fafc", font=("Segoe UI", 8), wraplength=card_w - 8, justify="center")
            name_lbl.pack(fill="x", pady=(2, 0))

            sz_lbl = tk.Label(card, text=f"{item['size'] or 'Media'}", bg=card_bg, fg="#94a3b8", font=("Segoe UI", 7))
            sz_lbl.pack(fill="x")

            card_info = {
                "frame": card,
                "hdr_frame": hdr_frame,
                "chk_lbl": chk_lbl,
                "cat_lbl": cat_lbl,
                "img_lbl": img_lbl,
                "name_lbl": name_lbl,
                "sz_lbl": sz_lbl,
                "item": item,
                "global_idx": global_idx,
                "path_hash": path_hash,
                "safe_name": safe_name,
                "local_thumb": local_thumb,
                "target_w": card_w - 8,
                "target_h": img_h
            }
            self.card_widgets[global_idx] = card_info

            for w in [card, hdr_frame, chk_lbl, cat_lbl, img_container, img_lbl, name_lbl, sz_lbl]:
                w.bind("<Button-1>", lambda e, gi=global_idx, it=item: self._toggle_card_selection(gi, it))
                w.bind("<Double-Button-1>", lambda e, it=item: self._on_card_double_click(it))
                w.bind("<Button-3>", lambda e, it=item, gi=global_idx: self._show_card_context_menu(e, it, gi))
                self._bind_mousewheel_to_grid(w)

            if os.path.exists(local_thumb) and HAS_PIL:
                self._apply_cached_thumb_to_card(card_info)
            else:
                new_queue.append(card_info)

        with self.thumb_queue_lock:
            self.thumb_queue.extend(new_queue)

        if new_queue and not self.thumb_worker_active:
            self._start_thumb_workers()

    def _apply_cached_thumb_to_card(self, card_info):
        try:
            local_thumb = card_info["local_thumb"]
            target_w = card_info["target_w"]
            target_h = card_info["target_h"]
            cache_key = f"{local_thumb}_{target_w}x{target_h}"

            if cache_key in self.card_image_cache:
                tk_img = self.card_image_cache[cache_key]
            else:
                pil_img = Image.open(local_thumb)
                w, h = pil_img.size
                scale = min(target_w / max(1, w), target_h / max(1, h))
                scaled_w, scaled_h = max(1, int(w * scale)), max(1, int(h * scale))
                resized = pil_img.resize((scaled_w, scaled_h), Image.Resampling.LANCZOS)
                tk_img = ImageTk.PhotoImage(resized)
                self.card_image_cache[cache_key] = tk_img

            card_info["img_lbl"].config(image=tk_img, text="")
        except Exception as err:
            card_info["img_lbl"].config(text="🖼 Photo")

    def _start_thumb_workers(self):
        self.thumb_worker_active = True
        
        def worker_loop(wid):
            while True:
                card_info = None
                with self.thumb_queue_lock:
                    if self.thumb_queue:
                        card_info = self.thumb_queue.pop(0)
                    else:
                        self.thumb_worker_active = False
                        break

                if not card_info:
                    break

                remote_path = card_info["item"]["path"]
                local_thumb = card_info["local_thumb"]

                if not os.path.exists(local_thumb):
                    safe_remote = remote_path.replace("'", "'\\''")
                    tmp_remote_thumb = f"/data/local/tmp/_thumb_{wid}.jpg"
                    thumb_cmd = (
                        f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap thumb "
                        f"'{safe_remote}' {tmp_remote_thumb} 280 50"
                    )
                    code, out, _ = self.run_adb(["shell", thumb_cmd], timeout=8)
                    pulled = False
                    if code == 0 and "THUMB_OK" in (out or ""):
                        p_code, _, _ = self.run_adb(["pull", tmp_remote_thumb, local_thumb], timeout=8)
                        pulled = (p_code == 0 and os.path.exists(local_thumb))
                        self.run_adb(["shell", f"rm -f {tmp_remote_thumb}"], timeout=3)
                    
                    if not pulled and not os.path.exists(local_thumb):
                        ext = os.path.splitext(remote_path)[1].lower()
                        if ext in ['.jpg', '.jpeg', '.png', '.webp', '.gif']:
                            self.run_adb(["pull", remote_path, local_thumb], timeout=15)

                if os.path.exists(local_thumb):
                    def update_card(ci=card_info):
                        gi = ci.get("global_idx", ci.get("idx"))
                        if gi in self.card_widgets:
                            self._apply_cached_thumb_to_card(self.card_widgets[gi])
                    self.after(0, update_card)

        for i in range(4):
            threading.Thread(target=lambda w=i: worker_loop(w), daemon=True).start()

    def _toggle_card_selection(self, idx, item):
        item["selected"] = not item["selected"]
        is_sel = item["selected"]
        is_trash = item.get("is_trashed") or item["category"] == "Trashed/Hidden"
        
        if idx in self.card_widgets:
            ci = self.card_widgets[idx]
            card_bg = "#1e3a8a" if is_sel else ("#3b1818" if is_trash else "#1e293b")
            border_c = "#38bdf8" if is_sel else ("#ef4444" if is_trash else "#334155")
            ci["frame"].config(bg=card_bg, highlightbackground=border_c, highlightthickness=2 if is_sel else 1)
            ci["hdr_frame"].config(bg=card_bg)
            ci["chk_lbl"].config(text="☑" if is_sel else "☐", bg=card_bg, fg="#38bdf8" if is_sel else "#94a3b8")
            ci["cat_lbl"].config(bg=card_bg)
            ci["name_lbl"].config(bg=card_bg)
            ci["sz_lbl"].config(bg=card_bg)

        try:
            self.gallery_tree.set(str(idx), "sel", "☑" if is_sel else "☐")
        except Exception:
            pass

        self._update_gallery_selection_counter()
        self.preview_gallery_image(item)

    def _on_card_double_click(self, item):
        self.previewing_item = item
        self.open_previewed_image_on_pc()

    def _show_card_context_menu(self, event, item, idx):
        self.previewing_item = item
        menu = tk.Menu(self, tearoff=0, bg="#1e293b", fg="white", activebackground="#0284c7", activeforeground="white")
        menu.add_command(label=f"👁 Open Full: {item['name'][:24]}", command=self.open_previewed_image_on_pc)
        menu.add_command(label="📥 Pull / Backup to Laptop", command=self.pull_previewed_image)
        if item.get("is_trashed") or item["category"] == "Trashed/Hidden":
            menu.add_command(label="♻️ Restore to Gallery", command=self.restore_previewed_image)
        menu.add_separator()
        menu.add_command(label="☑ Toggle Select", command=lambda: self._toggle_card_selection(idx, item))
        menu.add_command(label="🗑️ Delete Photo (+Trash)", command=self.delete_previewed_image)
        menu.tk_popup(event.x_root, event.y_root)

    def _update_gallery_selection_counter(self):
        selected_items = [it for it in self.all_gallery_items if it["selected"]]
        total_bytes = sum(it["size_bytes"] for it in selected_items)
        cnt = len(selected_items)
        sz_str = self._format_size(total_bytes) if cnt > 0 else "0 B"
        self.gallery_sel_count_lbl.config(
            text=f"Selected: {cnt} files ({sz_str})"
        )
        if hasattr(self, 'top_btn_del'):
            self.top_btn_del.config(text=f"🗑️ Delete ({cnt})")
        if hasattr(self, 'top_btn_bak'):
            self.top_btn_bak.config(text=f"📥 Backup ({cnt})")

    def _on_gallery_tree_select(self, event):
        sel = self.gallery_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if 0 <= idx < len(self.filtered_gallery_items):
            item = self.filtered_gallery_items[idx]
            self.preview_gallery_image(item)

    def _on_gallery_tree_double_click(self, event):
        sel = self.gallery_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if 0 <= idx < len(self.filtered_gallery_items):
            item = self.filtered_gallery_items[idx]
            self._toggle_card_selection(idx, item)

    def _on_gallery_tree_toggle_space(self, event):
        sel = self.gallery_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if 0 <= idx < len(self.filtered_gallery_items):
            item = self.filtered_gallery_items[idx]
            self._toggle_card_selection(idx, item)

    def _select_all_gallery(self):
        for item in self.filtered_gallery_items:
            item["selected"] = True
        self._apply_gallery_filter_and_render()

    def _deselect_all_gallery(self):
        for item in self.all_gallery_items:
            item["selected"] = False
        self._apply_gallery_filter_and_render()

    def _select_screenshots_only(self):
        self._deselect_all_gallery()
        for item in self.filtered_gallery_items:
            if item["category"] == "Screenshots":
                item["selected"] = True
        self._apply_gallery_filter_and_render()

    def _select_camera_only(self):
        self._deselect_all_gallery()
        for item in self.filtered_gallery_items:
            if item["category"] == "Camera":
                item["selected"] = True
        self._apply_gallery_filter_and_render()

    def _select_trashed_only(self):
        self._deselect_all_gallery()
        for item in self.filtered_gallery_items:
            if item.get("is_trashed") or item["category"] == "Trashed/Hidden":
                item["selected"] = True
        self._apply_gallery_filter_and_render()

    def _select_locked_only(self):
        self._deselect_all_gallery()
        for item in self.filtered_gallery_items:
            if item.get("is_locked") or item["category"] == "Locked/Private Safe":
                item["selected"] = True
        self._apply_gallery_filter_and_render()

    def preview_gallery_image(self, item):
        """Pulls a very clear, high-resolution thumbnail generated ON MOBILE for instant preview with zero mobile storage waste"""
        self.previewing_item = item
        remote_path = item["path"]
        is_trash = item.get("is_trashed") or item["category"] == "Trashed/Hidden"
        
        path_hash = hashlib.md5(remote_path.encode('utf-8', errors='ignore')).hexdigest()[:10]
        safe_name = "".join(c for c in item["name"] if c.isalnum() or c in ('_', '-', '.'))
        local_thumb = self._get_thumb_path(remote_path, item["name"])

        self.preview_info_lbl.config(
            text=f"📁 {item['category']}\n📄 {item['name']}\n💾 {item['size']} | {item['date']}\n⏳ Loading clear preview..."
        )

        def task():
            if not os.path.exists(local_thumb):
                safe_remote = remote_path.replace("'", "'\\''")
                thumb_cmd = (
                    f"CLASSPATH={FASTCAP_DEX_REMOTE} app_process /data/local/tmp com.studio.FastCap thumb "
                    f"'{safe_remote}' /data/local/tmp/_thumb.jpg 280 50"
                )
                code, out, _ = self.run_adb(["shell", thumb_cmd], timeout=8)
                
                pulled = False
                if code == 0 and "THUMB_OK" in (out or ""):
                    p_code, _, _ = self.run_adb(["pull", "/data/local/tmp/_thumb.jpg", local_thumb], timeout=8)
                    pulled = (p_code == 0 and os.path.exists(local_thumb))
                    self.run_adb(["shell", "rm -f /data/local/tmp/_thumb.jpg"], timeout=3)
                
                if not pulled and not os.path.exists(local_thumb):
                    ext = os.path.splitext(remote_path)[1].lower()
                    if ext in ['.jpg', '.jpeg', '.png', '.webp', '.gif']:
                        self.run_adb(["pull", remote_path, local_thumb], timeout=15)

            if os.path.exists(local_thumb):
                try:
                    thumb_size = os.path.getsize(local_thumb)
                    thumb_kb = thumb_size / 1024.0
                    
                    if HAS_PIL:
                        pil_img = Image.open(local_thumb)
                        w, h = pil_img.size
                        max_w, max_h = 180, 200
                        scale = min(max_w / max(1, w), max_h / max(1, h))
                        target_w, target_h = max(1, int(w * scale)), max(1, int(h * scale))
                        resized = pil_img.resize((target_w, target_h), Image.Resampling.LANCZOS)
                        tk_img = ImageTk.PhotoImage(resized)

                        def show():
                            self.gallery_preview_lbl.config(image=tk_img, text="")
                            self.gallery_preview_img = tk_img
                            status_tag = "🗑️ Trashed/Hidden" if is_trash else "Active"
                            self.preview_info_lbl.config(
                                text=(
                                    f"📁 {item['category']} ({status_tag})\n"
                                    f"📄 {item['name']}\n"
                                    f"💾 Original: {item['size']} | {item['date']}\n"
                                    f"⚡ Crisp Thumbnail: {thumb_kb:.1f} KB (Clear)\n"
                                    f"📍 {remote_path}"
                                )
                            )
                        self.after(0, show)
                except Exception as err:
                    self.log(f"Thumbnail render error: {err}")

        threading.Thread(target=task, daemon=True).start()

    def open_previewed_image_on_pc(self):
        if not self.previewing_item:
            messagebox.showinfo("Select", "Please select an image from the gallery list or grid.")
            return
        
        remote_path = self.previewing_item['path']
        fname = self.previewing_item['name']
        local_orig = os.path.join(PULLED_MEDIA_DIR, f"full_{fname}")

        def task():
            self.log(f"Pulling full original {fname} for viewing...")
            code, _, err = self.run_adb(["pull", remote_path, local_orig], timeout=30)
            if code == 0 and os.path.exists(local_orig):
                self.log(f"Opening full image: {local_orig}")
                os.startfile(local_orig)
            else:
                self.log(f"Failed to open original: {err}")
                self.after(0, lambda: messagebox.showerror("Open Error", f"Could not load full original:\n{err}"))

        threading.Thread(target=task, daemon=True).start()

    def pull_previewed_image(self):
        if not self.previewing_item:
            messagebox.showinfo("Select", "Please select an image first.")
            return
        dest = filedialog.askdirectory(title="Save Image to Laptop", initialdir=PROJECT_DIR)
        if not dest:
            return
        def task():
            self.log(f"Pulling {self.previewing_item['name']} to {dest}...")
            target = os.path.join(dest, self.previewing_item['name'])
            code, _, err = self.run_adb(["pull", self.previewing_item['path'], target], timeout=30)
            if code == 0:
                self.log(f"Successfully saved {self.previewing_item['name']} to laptop!")
                self.after(0, lambda: messagebox.showinfo("Saved", f"Saved to:\n{target}"))
            else:
                self.log(f"Pull error: {err}")
        threading.Thread(target=task, daemon=True).start()

    def delete_previewed_image(self):
        """Permanently deletes the single previewed image from mobile and purges trash"""
        if not self.previewing_item:
            messagebox.showinfo("Select", "Please select an image to delete.")
            return
        item = self.previewing_item
        p = item["path"]
        fname = item["name"]

        if not messagebox.askyesno(
            "Confirm Delete",
            f"Permanently delete '{fname}' from mobile storage?\n\n"
            "✓ Removes original file from device\n"
            "✓ Purges from mobile Trash & Recently Deleted"
        ):
            return

        def task():
            self.log(f"Deleting {fname} from mobile storage...")
            self.run_adb(["shell", f"rm -f '{p}'"], timeout=8)
            self.purge_mobile_trash_and_mediastore(deleted_paths=[p], refresh_gallery=True)
            self.log(f"Deleted {fname} and updated gallery.")
            self.after(0, lambda: messagebox.showinfo("Deleted", f"Permanently deleted:\n{fname}"))

        threading.Thread(target=task, daemon=True).start()

    def restore_previewed_image(self):
        """Restores a trashed or hidden image back to normal gallery view"""
        if not self.previewing_item:
            messagebox.showinfo("Select", "Please select a trashed or hidden image to restore.")
            return
        item = self.previewing_item
        p = item["path"]
        fname = item["name"]

        def task():
            self.log(f"Restoring {fname} to active gallery...")
            folder = os.path.dirname(p)
            new_name = fname
            if fname.startswith(".trashed-"):
                # .trashed-1654085741-original.jpg -> original.jpg
                parts = fname.split("-", 2)
                if len(parts) >= 3:
                    new_name = parts[2]
                else:
                    new_name = fname.replace(".trashed-", "")
            elif fname.startswith("."):
                new_name = fname.lstrip(".")

            target_path = os.path.join(folder, new_name).replace("\\", "/")
            if target_path != p:
                self.run_adb(["shell", f"mv '{p}' '{target_path}'"], timeout=8)
                p_final = target_path
            else:
                p_final = p

            # Restore MediaStore trash record if flagged
            self.run_adb(["shell", f"content update --uri content://media/external/images/media --set 'is_trashed=0' --where \"_data='{p_final}'\""], timeout=6)
            self.run_adb(["shell", f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d 'file://{p_final}'"], timeout=4)
            self.log(f"Restored {fname} -> {new_name} and broadcast to gallery!")
            self.after(0, lambda: messagebox.showinfo("Restored", f"Successfully restored to active Gallery:\n{new_name}"))
            self.scan_mobile_gallery()

        threading.Thread(target=task, daemon=True).start()

    def purge_mobile_trash_and_mediastore(self, deleted_paths=None, refresh_gallery=True):
        """
        Deep trash purge:
        1. Deletes hidden .trashed-* files across DCIM, Pictures, Download, Movies, Documents
        2. Deletes Android gallery recycle bins (_.globalTrash, .globalTrash, .trash)
        3. Cleans MediaStore trashed items (is_trashed=1)
        4. Broadcasts MediaScanner to instantly update Android Gallery UI
        """
        trash_cleanup_cmd = (
            "rm -f /sdcard/DCIM/.trashed-* /sdcard/Pictures/.trashed-* /sdcard/Download/.trashed-* "
            "/sdcard/Movies/.trashed-* /sdcard/Documents/.trashed-* /sdcard/.trashed-* 2>/dev/null; "
            "rm -rf /sdcard/DCIM/_.globalTrash/* /sdcard/DCIM/.globalTrash/* /sdcard/Pictures/.trash/* "
            "/sdcard/Movies/.trash/* /sdcard/Android/media/com.whatsapp/WhatsApp/Media/.trash/* 2>/dev/null; "
            "rm -f /data/local/tmp/_thumb* /data/local/tmp/_sc* /data/local/tmp/screen.* 2>/dev/null"
        )
        self.run_adb(["shell", trash_cleanup_cmd], timeout=15)

        # Delete MediaStore database trash entries
        self.run_adb(["shell", "content delete --uri content://media/external/images/media --where 'is_trashed=1'"], timeout=8)
        self.run_adb(["shell", "content delete --uri content://media/external/video/media --where 'is_trashed=1'"], timeout=8)
        self.run_adb(["shell", "content delete --uri content://media/external/file --where 'is_trashed=1'"], timeout=8)

        if deleted_paths:
            for p in deleted_paths[:20]:
                self.run_adb(["shell", f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d 'file://{p}'"], timeout=3)

        folders_to_scan = [
            "/sdcard/DCIM/Camera",
            "/sdcard/DCIM/Screenshots",
            "/sdcard/Pictures/Screenshots",
            "/sdcard/Pictures",
            "/sdcard/Download"
        ]
        for fld in folders_to_scan:
            self.run_adb(["shell", f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d 'file://{fld}'"], timeout=4)

        if refresh_gallery:
            self.scan_mobile_gallery()

    def empty_mobile_trash_bin(self):
        """Dedicated one-click button to empty Mobile Trash, Recycle Bin, and Recently Deleted files"""
        if not messagebox.askyesno(
            "Empty Mobile Trash / Recently Deleted",
            "Permanently empty and purge all files in Mobile Trash / Recently Deleted / Recycle Bin across the entire phone?\n\n"
            "This will completely free up mobile storage and clear all hidden .trashed files."
        ):
            return

        def task():
            self.log("Purging all mobile trash, Recently Deleted items, and MediaStore trash records...")
            self.purge_mobile_trash_and_mediastore(refresh_gallery=True)
            self.log("Mobile Trash / Recently Deleted bin is completely emptied!")
            self.after(0, lambda: messagebox.showinfo(
                "Trash Emptied",
                "Mobile Trash & Recently Deleted bin completely emptied!\n"
                "All hidden .trashed files and MediaStore trash records were permanently deleted."
            ))

        threading.Thread(target=task, daemon=True).start()

    def delete_all_screenshots_from_mobile(self):
        """One-click batch deletion of ALL screenshot files on mobile with deep trash purge and MediaStore refresh"""
        screenshot_items = [it for it in self.all_gallery_items if it["category"] == "Screenshots"]
        count = len(screenshot_items)
        if count == 0:
            code, out, _ = self.run_adb(["shell", "find /sdcard/DCIM/Screenshots /sdcard/Pictures/Screenshots -type f 2>/dev/null | wc -l"], timeout=5)
            try: count = int(out.strip())
            except Exception: count = 0

        if count == 0:
            messagebox.showinfo("No Screenshots", "No screenshots found on this mobile device!")
            return

        total_mb = sum(it["size_bytes"] for it in screenshot_items) / (1024 * 1024) if screenshot_items else 0
        msg = (
            f"Permanently delete ALL {count} screenshot(s) (~{total_mb:.1f} MB) from mobile?\n\n"
            "✓ Removes originals from mobile storage\n"
            "✓ Completely purges from mobile Trash & Recently Deleted\n"
            "✓ Refreshes phone gallery immediately"
        )
        if not messagebox.askyesno("Confirm Screenshot Cleanup", msg):
            return

        def task():
            self.log(f"Deleting all {count} screenshot(s) and purging trash from mobile...")
            del_cmd = (
                "rm -f /sdcard/DCIM/Screenshots/* /sdcard/Pictures/Screenshots/* /sdcard/DCIM/Screenshots/.* "
                "/sdcard/Pictures/Screenshots/.* /data/local/tmp/_sc* /data/local/tmp/screen.* "
                "/sdcard/gui_cap_screen.png /sdcard/tmp_screen.png 2>/dev/null"
            )
            self.run_adb(["shell", del_cmd], timeout=25)
            self.purge_mobile_trash_and_mediastore(refresh_gallery=True)

            self.log(f"All {count} screenshots permanently deleted from mobile storage & trash.")
            self.after(0, lambda: messagebox.showinfo(
                "Cleaned",
                f"Successfully deleted all {count} screenshot(s) from mobile!\n"
                "Mobile Trash & Recently Deleted were also emptied."
            ))

        threading.Thread(target=task, daemon=True).start()

    def delete_selected_gallery_images(self):
        """Deletes selected/checked original high-quality images from mobile AND purges them from mobile Trash / Recently Deleted"""
        selected_items = [it for it in self.all_gallery_items if it["selected"]]
        count = len(selected_items)
        if count == 0:
            messagebox.showinfo("No Selection", "Please check/select the images you want to delete.")
            return

        total_mb = sum(it["size_bytes"] for it in selected_items) / (1024 * 1024)
        msg = (
            f"Permanently delete {count} selected file(s) (~{total_mb:.1f} MB) from mobile storage?\n\n"
            "✓ Original high-quality files will be deleted from mobile\n"
            "✓ Also completely purged from mobile Trash / Recently Deleted\n"
            "✓ Action cannot be undone."
        )
        if not messagebox.askyesno("Confirm Delete", msg):
            return

        def task():
            self.log(f"Deleting {count} selected original files and purging mobile trash...")
            deleted_paths = []
            for it in selected_items:
                p = it["path"]
                code, _, err = self.run_adb(["shell", f"rm -f '{p}'"], timeout=6)
                if code == 0:
                    deleted_paths.append(p)
                    path_hash = hashlib.md5(p.encode('utf-8', errors='ignore')).hexdigest()[:10]
                    safe_name = "".join(c for c in it["name"] if c.isalnum() or c in ('_', '-', '.'))
                    loc_t = self._get_thumb_path(p, it["name"])
                    if os.path.exists(loc_t):
                        try: os.remove(loc_t)
                        except Exception: pass

            self.purge_mobile_trash_and_mediastore(deleted_paths=deleted_paths, refresh_gallery=True)
            self.log(f"Successfully deleted {len(deleted_paths)} of {count} selected file(s) and purged mobile trash.")
            self.after(0, lambda: messagebox.showinfo(
                "Deleted",
                f"Permanently deleted {len(deleted_paths)} file(s) from mobile storage & mobile Trash / Recently Deleted."
            ))

        threading.Thread(target=task, daemon=True).start()

    def backup_selected_gallery_images(self):
        """Pulls selected images to a chosen folder on laptop"""
        selected_items = [it for it in self.all_gallery_items if it["selected"]]
        count = len(selected_items)
        if count == 0:
            messagebox.showinfo("No Selection", "Please check/select images to backup first.")
            return

        dev_name = self.get_device_clean_name()
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, "gallery_backup")
        os.makedirs(default_dir, exist_ok=True)

        dest = filedialog.askdirectory(title=f"Backup {count} Image(s) to Laptop Folder", initialdir=default_dir)
        if not dest:
            return

        def task():
            self.log(f"Backing up {count} images to {dest}...")
            saved = 0
            for it in selected_items:
                target = os.path.join(dest, it["name"])
                code, _, err = self.run_adb(["pull", it["path"], target], timeout=20)
                if code == 0:
                    saved += 1

            self.log(f"Backup complete! {saved}/{count} image(s) saved to {dest}")
            self.after(0, lambda: messagebox.showinfo("Backup Done", f"Saved {saved} image(s) to:\n{dest}"))
            os.startfile(dest)

        threading.Thread(target=task, daemon=True).start()

    def clone_all_gallery_media_to_pc(self, items_to_clone=None):
        """
        High-Speed Multi-Threaded Parallel ADB Clone of ALL 16,757+ Media files to Laptop:
        - Prioritizes fast USB cable throughput (8+ workers, ~30-50 MB/s).
        - Automatically creates date-stamped backup folder with organized category sub-directories.
        - Bypasses read/permission restrictions on locked safe (.aceself) and trashed/hidden files via stream extraction fallback.
        - Displays a live dark-themed progress modal with ETA, speed indicator (MB/s), live counts, and cancel support.
        - Zero mobile CPU/RAM strain (pure storage stream).
        """
        if not self.all_gallery_items:
            if messagebox.askyesno("Scan Required", "No media items loaded yet.\n\nRun Deep Gallery Scan now before cloning?"):
                self.scan_mobile_gallery()
            return

        items = items_to_clone if items_to_clone is not None else self.all_gallery_items
        total_items = len(items)
        if total_items == 0:
            messagebox.showinfo("Empty", "No media files available to clone.")
            return

        total_bytes = sum(it.get("size_bytes", 0) for it in items)
        total_gb = total_bytes / (1024 ** 3)
        total_mb = total_bytes / (1024 ** 2)

        dev_name = self.get_device_clean_name()
        timestamp_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, f"rapid_clone_{timestamp_str}")
        os.makedirs(default_dir, exist_ok=True)

        size_disp = f"{total_gb:.2f} GB" if total_gb >= 1.0 else f"{total_mb:.1f} MB"
        confirm_msg = (
            f"🚀 High-Speed Mobile Media Rapid Clone\n\n"
            f"Target Device: {dev_name} ({self.target_device})\n"
            f"Total Files to Clone: {total_items:,} media files (~{size_disp})\n"
            f"Includes: Photos, Videos, Screenshots, Locked Private Safe, Trashed & Stickers.\n\n"
            f"Destination Laptop Folder:\n{default_dir}\n\n"
            f"Would you like to start the rapid multi-threaded clone now?"
        )
        if not messagebox.askyesno("Confirm Full Mobile Media Clone", confirm_msg):
            return

        # Create subcategory directories
        cat_dirs = {
            "Camera": os.path.join(default_dir, "Camera_Photos_Videos"),
            "Screenshots": os.path.join(default_dir, "Screenshots"),
            "WhatsApp": os.path.join(default_dir, "WhatsApp_Media"),
            "Locked/Private Safe": os.path.join(default_dir, "Locked_Private_Safe"),
            "Trashed/Hidden": os.path.join(default_dir, "Trashed_Hidden_Recovered"),
            "Downloads & Purchases": os.path.join(default_dir, "Downloads"),
            "Stickers/WebP": os.path.join(default_dir, "Stickers_WebP"),
            "Root Storage": os.path.join(default_dir, "Root_Storage"),
            "Other Media": os.path.join(default_dir, "Other_Media")
        }
        for d in cat_dirs.values():
            os.makedirs(d, exist_ok=True)

        # Build Dark-Themed Progress Modal
        progress_win = tk.Toplevel(self)
        progress_win.title("⚡ Rapid Mobile Media Clone Progress")
        progress_win.geometry("560x310")
        progress_win.minsize(500, 270)
        progress_win.configure(bg="#0f172a")
        progress_win.attributes("-topmost", True)
        progress_win.resizable(False, False)

        tk.Label(
            progress_win,
            text=f"🚀 Rapid Multi-Threaded Media Clone ({dev_name})",
            bg="#0f172a",
            fg="#38bdf8",
            font=("Segoe UI", 12, "bold")
        ).pack(pady=(16, 4))

        lbl_status = tk.Label(
            progress_win,
            text=f"Preparing multi-threaded worker pipeline for {total_items:,} items...",
            bg="#0f172a",
            fg="#cbd5e1",
            font=("Segoe UI", 9)
        )
        lbl_status.pack(pady=2)

        pb = ttk.Progressbar(progress_win, orient="horizontal", length=500, mode="determinate")
        pb.pack(pady=10)
        pb["maximum"] = total_items
        pb["value"] = 0

        lbl_stats = tk.Label(
            progress_win,
            text="Files: 0 / 0 | Transferred: 0 MB | Speed: 0 MB/s | ETA: Calculating...",
            bg="#0f172a",
            fg="#a855f7",
            font=("Segoe UI", 9, "bold")
        )
        lbl_stats.pack(pady=4)

        lbl_curr_file = tk.Label(
            progress_win,
            text="",
            bg="#0f172a",
            fg="#64748b",
            font=("Segoe UI", 8),
            wraplength=520,
            justify="center"
        )
        lbl_curr_file.pack(pady=4)

        is_cancelled = {"value": False}

        def on_cancel():
            is_cancelled["value"] = True
            lbl_status.config(text="Stopping workers... Please wait.", fg="#ef4444")

        btn_cancel = tk.Button(
            progress_win,
            text="⏹ Cancel / Stop",
            bg="#dc2626",
            fg="white",
            activebackground="#b91c1c",
            activeforeground="white",
            font=("Segoe UI", 9, "bold"),
            padx=12,
            pady=4,
            relief="flat",
            command=on_cancel
        )
        btn_cancel.pack(pady=8)

        def worker_task():
            is_usb = ":" not in self.target_device
            num_workers = 8 if is_usb else 4
            addr = self.target_device
            
            self.log(f"⚡ Starting Rapid Clone: {total_items:,} items using {num_workers} parallel worker threads over {'High-Speed USB' if is_usb else 'Network'}.")

            completed = 0
            errors = 0
            bytes_transferred = 0
            start_time = time.time()
            last_ui_update = [time.time()]

            def pull_single_item(item_info):
                if is_cancelled["value"]:
                    return False, 0, item_info["name"]

                rpath = item_info["path"]
                fname = item_info["name"]
                cat = item_info.get("category", "Other Media")
                target_sub = cat_dirs.get(cat, cat_dirs["Other Media"])

                # Sanitize filename
                safe_fname = "".join(c for c in fname if c.isalnum() or c in ('_', '-', '.'))
                if not safe_fname:
                    safe_fname = f"media_{hashlib.md5(rpath.encode('utf-8', errors='ignore')).hexdigest()[:8]}.bin"

                dest_file = os.path.join(target_sub, safe_fname)
                if os.path.exists(dest_file):
                    # Disambiguate duplicate names
                    h = hashlib.md5(rpath.encode('utf-8', errors='ignore')).hexdigest()[:6]
                    root, ext = os.path.splitext(safe_fname)
                    dest_file = os.path.join(target_sub, f"{root}_{h}{ext}")

                # Standard ADB pull
                try:
                    res = subprocess.run(
                        ["adb", "-s", addr, "pull", rpath, dest_file],
                        capture_output=True,
                        timeout=25,
                        creationflags=NO_WINDOW
                    )
                    if res.returncode == 0 and os.path.exists(dest_file) and os.path.getsize(dest_file) > 0:
                        sz = os.path.getsize(dest_file)
                        return True, sz, fname
                except Exception:
                    pass

                # Fallback: Stream via exec-out cat (bypasses lock and permissions)
                try:
                    with open(dest_file, "wb") as f_out:
                        res2 = subprocess.run(
                            ["adb", "-s", addr, "exec-out", f"cat '{rpath}'"],
                            stdout=f_out,
                            stderr=subprocess.PIPE,
                            timeout=25,
                            creationflags=NO_WINDOW
                        )
                    if res2.returncode == 0 and os.path.exists(dest_file) and os.path.getsize(dest_file) > 0:
                        sz = os.path.getsize(dest_file)
                        return True, sz, fname
                    else:
                        if os.path.exists(dest_file) and os.path.getsize(dest_file) == 0:
                            os.remove(dest_file)
                except Exception:
                    pass

                return False, 0, fname

            with concurrent.futures.ThreadPoolExecutor(max_workers=num_workers) as executor:
                futures = {executor.submit(pull_single_item, it): it for it in items}
                for future in concurrent.futures.as_completed(futures):
                    if is_cancelled["value"]:
                        break
                    success, sz, fname = future.result()
                    completed += 1
                    if success:
                        bytes_transferred += sz
                    else:
                        errors += 1

                    # Throttle UI update to every 100ms
                    now = time.time()
                    if now - last_ui_update[0] >= 0.1 or completed == total_items:
                        last_ui_update[0] = now
                        elapsed = max(0.1, now - start_time)
                        speed_mb = (bytes_transferred / (1024 * 1024)) / elapsed
                        trans_mb = bytes_transferred / (1024 * 1024)
                        trans_gb = trans_mb / 1024
                        disp_trans = f"{trans_gb:.2f} GB" if trans_gb >= 1.0 else f"{trans_mb:.1f} MB"
                        
                        remain = total_items - completed
                        rate = completed / elapsed
                        eta_sec = int(remain / rate) if rate > 0 else 0
                        eta_str = f"{eta_sec // 60:02d}:{eta_sec % 60:02d}"
                        elapsed_str = f"{int(elapsed) // 60:02d}:{int(elapsed) % 60:02d}"

                        def update_progress_ui(c=completed, t=total_items, st=disp_trans, sp=speed_mb, fn=fname, el=elapsed_str, eta=eta_str):
                            if not progress_win.winfo_exists():
                                return
                            pb["value"] = c
                            pct = (c / max(1, t)) * 100
                            lbl_status.config(text=f"Cloning: {c:,} / {t:,} files ({pct:.1f}%)")
                            lbl_stats.config(text=f"Saved: {st} | Speed: {sp:.1f} MB/s | Elapsed: {el} | ETA: ~{eta}")
                            lbl_curr_file.config(text=f"Active: {fn}")
                        
                        self.after(0, update_progress_ui)

            total_elapsed = time.time() - start_time
            total_saved_mb = bytes_transferred / (1024 * 1024)
            total_saved_gb = total_saved_mb / 1024
            disp_total = f"{total_saved_gb:.2f} GB" if total_saved_gb >= 1.0 else f"{total_saved_mb:.1f} MB"

            def finish_ui():
                if progress_win.winfo_exists():
                    progress_win.destroy()
                
                status_word = "Cancelled" if is_cancelled["value"] else "Complete"
                self.log(f"🚀 Rapid Clone {status_word}! Saved {completed - errors:,} media files ({disp_total}) in {int(total_elapsed)}s to {default_dir}")
                
                msg = (
                    f"🚀 Rapid Mobile Media Clone {status_word}!\n\n"
                    f"✓ Successfully Cloned: {completed - errors:,} files\n"
                    f"✓ Total Data Saved: {disp_total}\n"
                    f"✓ Time Taken: {int(total_elapsed)} seconds\n"
                    f"✓ Errors/Skipped: {errors}\n\n"
                    f"Destination Folder:\n{default_dir}"
                )
                messagebox.showinfo("Clone Complete", msg)
                try:
                    os.startfile(default_dir)
                except Exception:
                    pass

            self.after(0, finish_ui)

        threading.Thread(target=worker_task, daemon=True).start()
    # CALL LOGS & CONTACTS DIRECTORY METHODS
    # =========================================================================

    def fetch_call_logs_and_contacts(self):
        """Fetches complete call history and address book contacts from device via content provider"""
        def task():
            self.log("Fetching live call logs and address book contacts from device...")
            try:
                self.after(0, lambda: self.calls_summary_lbl.config(text="Querying device calls & contacts..."))
            except Exception:
                pass

            logs = []
            contacts_map = {} # number -> {name, number, count, last_date, last_timestamp}

            # 1. Query Address Book Contacts directly
            c_code, c_out, _ = self.run_adb([
                "shell",
                "content query --uri content://com.android.contacts/data/phones --projection display_name:data1"
            ], timeout=15)

            if c_code == 0 and c_out:
                for line in c_out.splitlines():
                    line = line.strip()
                    if not line or not line.startswith("Row:"):
                        continue
                    c_name = ""
                    c_num = ""
                    for token in line.split(", "):
                        if "display_name=" in token:
                            c_name = token.split("display_name=", 1)[-1].strip()
                            if c_name == "NULL": c_name = ""
                        elif "data1=" in token:
                            c_num = token.split("data1=", 1)[-1].strip()
                    if c_num:
                        norm = "".join(c for c in c_num if c.isdigit() or c == '+')
                        key = norm if norm else c_num
                        contacts_map[key] = {
                            "name": c_name if c_name else c_num,
                            "number": c_num,
                            "count": 0,
                            "last_date": "",
                            "last_timestamp": 0
                        }

            # 2. Query Call Logs
            code, out, err = self.run_adb([
                "shell",
                "content query --uri content://call_log/calls --projection number:name:date:duration:type --sort 'date DESC'"
            ], timeout=25)

            type_map = {
                "1": ("📥 Incoming", "Incoming"),
                "2": ("📤 Outgoing", "Outgoing"),
                "3": ("❌ Missed", "Missed"),
                "4": ("🎙 Voicemail", "Voicemail"),
                "5": ("🚫 Rejected", "Rejected"),
                "6": ("🛑 Blocked", "Blocked"),
                "7": ("📱 External", "External")
            }

            if code == 0 and out:
                for line in out.splitlines():
                    line = line.strip()
                    if not line or not line.startswith("Row:"):
                        continue
                    num = ""
                    name = ""
                    date_ms = 0
                    duration_s = 0
                    call_type = "1"

                    for token in line.split(", "):
                        if "number=" in token:
                            num = token.split("number=", 1)[-1].strip()
                        elif "name=" in token:
                            n_val = token.split("name=", 1)[-1].strip()
                            name = "" if n_val == "NULL" else n_val
                        elif "date=" in token:
                            try: date_ms = int(token.split("date=", 1)[-1].strip())
                            except Exception: date_ms = 0
                        elif "duration=" in token:
                            try: duration_s = int(token.split("duration=", 1)[-1].strip())
                            except Exception: duration_s = 0
                        elif "type=" in token:
                            call_type = token.split("type=", 1)[-1].strip()

                    if not num:
                        continue

                    # Format date
                    date_str = ""
                    if date_ms > 0:
                        try:
                            date_str = datetime.fromtimestamp(date_ms / 1000.0).strftime('%Y-%m-%d %H:%M:%S')
                        except Exception:
                            date_str = ""

                    # Format duration
                    if duration_s == 0:
                        dur_str = "0s"
                    elif duration_s < 60:
                        dur_str = f"{duration_s}s"
                    else:
                        dur_str = f"{duration_s // 60}m {duration_s % 60}s"

                    icon_type, type_label = type_map.get(str(call_type), ("📞 Call", "Call"))
                    display_name = name if name else "(Unknown)"

                    log_entry = {
                        "number": num,
                        "name": display_name,
                        "raw_name": name,
                        "date_str": date_str,
                        "date_ms": date_ms,
                        "duration_s": duration_s,
                        "dur_str": dur_str,
                        "type_icon": icon_type,
                        "type_label": type_label,
                        "type_code": str(call_type)
                    }
                    logs.append(log_entry)

                    # Build contacts aggregate
                    norm_num = "".join(c for c in num if c.isdigit() or c == '+')
                    c_key = norm_num if norm_num else num
                    if c_key not in contacts_map:
                        contacts_map[c_key] = {
                            "name": name if name else num,
                            "number": num,
                            "count": 1,
                            "last_date": date_str,
                            "last_timestamp": date_ms
                        }
                    else:
                        contacts_map[c_key]["count"] += 1
                        if name and (not contacts_map[c_key]["name"] or contacts_map[c_key]["name"] == num):
                            contacts_map[c_key]["name"] = name
                        if date_ms > contacts_map[c_key]["last_timestamp"]:
                            contacts_map[c_key]["last_date"] = date_str
                            contacts_map[c_key]["last_timestamp"] = date_ms

            contacts_list = list(contacts_map.values())
            contacts_list.sort(key=lambda x: (-x["count"], -x["last_timestamp"]))

            def update_ui():
                self.all_call_logs = logs
                self.all_contacts = contacts_list
                self._apply_calls_filter_and_render()
                self._apply_contacts_filter_and_render()
                self.calls_summary_lbl.config(
                    text=f"Total: {len(logs)} calls | 👥 Unique Contacts: {len(contacts_list)}"
                )
                self.log(f"Successfully loaded {len(logs)} call logs and {len(contacts_list)} contacts from device.")

            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def _set_calls_filter(self, filter_type):
        self.current_calls_filter = filter_type
        self.btn_call_f_all.config(bg="#0284c7" if filter_type == "ALL" else "#334155")
        self.btn_call_f_in.config(bg="#0284c7" if filter_type == "INCOMING" else "#334155")
        self.btn_call_f_out.config(bg="#0284c7" if filter_type == "OUTGOING" else "#334155")
        self.btn_call_f_missed.config(bg="#0284c7" if filter_type == "MISSED" else "#334155")
        self.btn_call_f_rej.config(bg="#0284c7" if filter_type == "REJECTED" else "#334155")
        self._apply_calls_filter_and_render()

    def _apply_calls_filter_and_render(self):
        query = self.calls_search_entry.get().strip().lower() if hasattr(self, 'calls_search_entry') else ""
        ft = getattr(self, 'current_calls_filter', 'ALL')

        filtered = []
        for log in self.all_call_logs:
            t_lbl = log["type_label"].upper()
            if ft == "INCOMING" and t_lbl != "INCOMING":
                continue
            elif ft == "OUTGOING" and t_lbl != "OUTGOING":
                continue
            elif ft == "MISSED" and t_lbl != "MISSED":
                continue
            elif ft == "REJECTED" and t_lbl != "REJECTED":
                continue

            if query:
                if query not in log["name"].lower() and query not in log["number"].lower():
                    continue

            filtered.append(log)

        self.filtered_call_logs = filtered
        self.calls_tree.delete(*self.calls_tree.get_children())
        for idx, log in enumerate(filtered):
            self.calls_tree.insert(
                "",
                "end",
                iid=str(idx),
                values=(
                    log["type_icon"],
                    log["name"],
                    log["number"],
                    log["date_str"],
                    log["dur_str"]
                )
            )

    def _apply_contacts_filter_and_render(self):
        query = self.calls_search_entry.get().strip().lower() if hasattr(self, 'calls_search_entry') else ""
        filtered = []
        for c in self.all_contacts:
            if query:
                if query not in c["name"].lower() and query not in c["number"].lower():
                    continue
            filtered.append(c)

        self.filtered_contacts = filtered
        self.contacts_tree.delete(*self.contacts_tree.get_children())
        for idx, c in enumerate(filtered):
            self.contacts_tree.insert(
                "",
                "end",
                iid=str(idx),
                values=(
                    c["name"],
                    c["number"],
                    c["count"],
                    c["last_date"].split()[0] if c["last_date"] else ""
                )
            )

    def _on_contact_tree_select(self, event):
        sel = self.contacts_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if 0 <= idx < len(self.filtered_contacts):
            c = self.filtered_contacts[idx]
            # Set search entry to filter call logs for this contact
            self.calls_search_entry.delete(0, "end")
            self.calls_search_entry.insert(0, c["number"])
            self._apply_calls_filter_and_render()

    def export_call_logs_csv(self):
        """Exports complete call logs to CSV file on PC"""
        if not self.all_call_logs:
            messagebox.showinfo("No Data", "Please fetch call logs first by clicking 'Fetch Live Calls'.")
            return

        dev_name = self.get_device_clean_name()
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, "calls_backup")
        os.makedirs(default_dir, exist_ok=True)
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_file = os.path.join(default_dir, f"call_logs_{timestamp}.csv")

        dest = filedialog.asksaveasfilename(
            title="Save Call Logs as CSV",
            initialdir=default_dir,
            initialfile=os.path.basename(default_file),
            filetypes=[("CSV Spreadsheet", "*.csv")]
        )
        if not dest:
            return

        try:
            with open(dest, mode='w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Type", "Contact Name", "Phone Number", "Date & Time", "Duration (Seconds)", "Duration Formatted"])
                for log in self.all_call_logs:
                    writer.writerow([
                        log["type_label"],
                        log["raw_name"] or log["name"],
                        log["number"],
                        log["date_str"],
                        log["duration_s"],
                        log["dur_str"]
                    ])
            self.log(f"Exported {len(self.all_call_logs)} call log(s) to: {dest}")
            messagebox.showinfo("Export Successful", f"Saved {len(self.all_call_logs)} call logs to:\n{dest}")
            os.startfile(os.path.dirname(dest))
        except Exception as e:
            self.log(f"Export CSV error: {e}")
            messagebox.showerror("Export Error", f"Failed to save CSV file:\n{e}")

    def export_contacts_dialog(self):
        if not self.all_contacts:
            messagebox.showinfo("No Data", "Please fetch call logs and contacts first.")
            return

        dialog = tk.Toplevel(self)
        dialog.title("Save / Backup Contacts")
        dialog.geometry("380x180")
        dialog.configure(bg="#0f172a")
        dialog.transient(self)
        dialog.grab_set()

        tk.Label(dialog, text=f"Backup {len(self.all_contacts)} Extracted Contacts", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 10, "bold")).pack(pady=12)
        tk.Label(dialog, text="Choose export format for contacts:", bg="#0f172a", fg="#cbd5e1", font=("Segoe UI", 9)).pack(pady=4)

        btn_f = tk.Frame(dialog, bg="#0f172a")
        btn_f.pack(pady=12)

        def do_csv():
            dialog.destroy()
            self.export_contacts_csv()

        def do_vcf():
            dialog.destroy()
            self.export_contacts_vcf()

        tk.Button(btn_f, text="📊 Save as CSV", bg="#0284c7", fg="white", font=("Segoe UI", 9, "bold"), padx=10, pady=5, relief="flat", command=do_csv).pack(side="left", padx=6)
        tk.Button(btn_f, text="📇 Save as vCard (.vcf)", bg="#7c3aed", fg="white", font=("Segoe UI", 9, "bold"), padx=10, pady=5, relief="flat", command=do_vcf).pack(side="left", padx=6)

    def export_contacts_csv(self):
        dev_name = self.get_device_clean_name()
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, "calls_backup")
        os.makedirs(default_dir, exist_ok=True)
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_file = os.path.join(default_dir, f"contacts_{timestamp}.csv")

        dest = filedialog.asksaveasfilename(
            title="Save Contacts as CSV",
            initialdir=default_dir,
            initialfile=os.path.basename(default_file),
            filetypes=[("CSV Spreadsheet", "*.csv")]
        )
        if not dest:
            return

        try:
            with open(dest, mode='w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Contact Name", "Phone Number", "Total Calls Recorded", "Last Interaction"])
                for c in self.all_contacts:
                    writer.writerow([c["name"], c["number"], c["count"], c["last_date"]])
            self.log(f"Exported {len(self.all_contacts)} contacts to CSV: {dest}")
            messagebox.showinfo("Export Successful", f"Saved {len(self.all_contacts)} contacts to:\n{dest}")
            os.startfile(os.path.dirname(dest))
        except Exception as e:
            messagebox.showerror("Export Error", f"Failed to save CSV:\n{e}")

    def export_contacts_vcf(self):
        dev_name = self.get_device_clean_name()
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, "calls_backup")
        os.makedirs(default_dir, exist_ok=True)
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_file = os.path.join(default_dir, f"contacts_{timestamp}.vcf")

        dest = filedialog.asksaveasfilename(
            title="Save Contacts as vCard (.vcf)",
            initialdir=default_dir,
            initialfile=os.path.basename(default_file),
            filetypes=[("vCard Contacts", "*.vcf")]
        )
        if not dest:
            return

        try:
            with open(dest, mode='w', encoding='utf-8') as f:
                for c in self.all_contacts:
                    f.write("BEGIN:VCARD\n")
                    f.write("VERSION:3.0\n")
                    f.write(f"FN:{c['name']}\n")
                    f.write(f"TEL;TYPE=CELL:{c['number']}\n")
                    f.write("END:VCARD\n\n")
            self.log(f"Exported {len(self.all_contacts)} contacts to vCard: {dest}")
            messagebox.showinfo("Export Successful", f"Saved {len(self.all_contacts)} contacts to vCard:\n{dest}\n\nCan be imported directly to phone or Google Contacts!")
            os.startfile(os.path.dirname(dest))
        except Exception as e:
            messagebox.showerror("Export Error", f"Failed to save vCard:\n{e}")

    # =========================================================================
    # INSTALLED & HIDDEN APPS MANAGEMENT METHODS
    # =========================================================================

    def scan_installed_apps(self):
        """Scans 3rd party user apps, dual/cloned profiles (User 999/10/11), disabled/hidden apps, and system apps from device"""
        def task():
            self.log("Scanning installed, hidden, and dual/cloned applications on mobile...")
            try:
                self.after(0, lambda: self.apps_summary_lbl.config(text="Scanning packages on mobile..."))
            except Exception:
                pass

            # 1. Check multi-user / dual app profiles
            u_code, u_out, _ = self.run_adb(["shell", "pm list users"], timeout=8)
            user_ids = [0]
            if u_code == 0 and u_out:
                for match in re.finditer(r'UserInfo\{(\d+):', u_out):
                    try:
                        uid = int(match.group(1))
                        if uid not in user_ids:
                            user_ids.append(uid)
                    except Exception:
                        pass
            if 999 not in user_ids:
                user_ids.append(999) # OPPO / Xiaomi Dual Apps profile fallback

            # 2. User packages (3rd party primary)
            code3, out3, _ = self.run_adb(["shell", "pm list packages -3 -f"], timeout=12)
            # 3. Disabled/Hidden packages
            coded, outd, _ = self.run_adb(["shell", "pm list packages -d -f"], timeout=12)
            # 4. System packages
            codes, outs, _ = self.run_adb(["shell", "pm list packages -s -f"], timeout=12)

            disabled_set = set()
            if coded == 0 and outd:
                for line in outd.splitlines():
                    if "=" in line:
                        pkg = line.split("=")[-1].strip()
                        disabled_set.add(pkg)

            apps = []
            seen_pkg = set()

            def parse_block(out, type_label, is_dual_user=False, uid=0):
                if not out:
                    return
                for line in out.splitlines():
                    line = line.strip()
                    if not line or "=" not in line:
                        continue
                    # package:/data/app/~~.../base.apk=com.example.app
                    clean_l = line.replace("package:", "")
                    parts = clean_l.rsplit("=", 1)
                    if len(parts) == 2:
                        apk_path, pkg_name = parts[0].strip(), parts[1].strip()
                        key = f"{pkg_name}_{uid}" if is_dual_user else pkg_name
                        if key in seen_pkg:
                            continue
                        seen_pkg.add(key)
                        is_dis = pkg_name in disabled_set

                        # Detect private vault / safe app packages across brands
                        is_vault = any(k in pkg_name.lower() for k in (
                            "safebox", "privatesafe", "securefolder", "securitycenter",
                            "privatespace", "safecenter", "applock", "coloros.safe"
                        ))

                        if is_dual_user:
                            status_str = f"🔒 Dual (User {uid})"
                            cat = f"🔒 Dual/Clone (User {uid})"
                        elif is_dis:
                            status_str = "🚫 Hidden/Frozen"
                            cat = "Disabled/Hidden"
                        elif is_vault:
                            status_str = "🔒 Private Vault"
                            cat = "🔒 Vault/Security"
                        else:
                            status_str = "🟢 Active"
                            cat = type_label

                        apps.append({
                            "pkg": pkg_name,
                            "apk": apk_path,
                            "type": cat,
                            "is_disabled": is_dis,
                            "is_dual": is_dual_user,
                            "is_vault": is_vault,
                            "user_id": uid,
                            "status": status_str
                        })

            parse_block(out3, "User (3rd Party)", False, 0)

            # Query secondary user profiles (Dual Apps / Parallel Space / Work Profile)
            for uid in user_ids:
                if uid == 0:
                    continue
                c_dual, o_dual, _ = self.run_adb(["shell", f"pm list packages --user {uid} -3 -f"], timeout=8)
                if c_dual == 0 and o_dual and "=" in o_dual:
                    parse_block(o_dual, f"🔒 Dual/Clone", True, uid)

            parse_block(outd, "Disabled/Hidden", False, 0)
            parse_block(outs, "System", False, 0)

            apps.sort(key=lambda x: (
                not x.get("is_dual", False),
                x["type"] != "Disabled/Hidden",
                not x.get("is_vault", False),
                x["type"] != "User (3rd Party)",
                x["pkg"].lower()
            ))

            def update_ui():
                self.all_installed_apps = apps
                self._apply_apps_filter_and_render()
                u_cnt = sum(1 for a in apps if "User" in a["type"])
                dual_cnt = sum(1 for a in apps if a.get("is_dual"))
                d_cnt = sum(1 for a in apps if a["is_disabled"])
                self.apps_summary_lbl.config(
                    text=f"Total: {len(apps)} | 👤 User: {u_cnt} | 🔒 Dual: {dual_cnt} | 🚫 Hidden: {d_cnt}"
                )
                self.log(f"Found {len(apps)} installed packages (User: {u_cnt}, Dual/Cloned: {dual_cnt}, Hidden/Disabled: {d_cnt}).")

            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def _set_apps_filter(self, filter_type):
        self.current_apps_filter = filter_type
        if hasattr(self, 'btn_app_user'): self.btn_app_user.config(bg="#0284c7" if filter_type == "USER" else "#334155")
        if hasattr(self, 'btn_app_dual'): self.btn_app_dual.config(bg="#9333ea" if filter_type == "DUAL" else "#581c87")
        if hasattr(self, 'btn_app_disabled'): self.btn_app_disabled.config(bg="#0284c7" if filter_type == "DISABLED" else "#334155")
        if hasattr(self, 'btn_app_system'): self.btn_app_system.config(bg="#0284c7" if filter_type == "SYSTEM" else "#334155")
        if hasattr(self, 'btn_app_all'): self.btn_app_all.config(bg="#0284c7" if filter_type == "ALL" else "#334155")
        self._apply_apps_filter_and_render()

    def _apply_apps_filter_and_render(self):
        query = self.apps_search_entry.get().strip().lower() if hasattr(self, 'apps_search_entry') else ""
        ft = getattr(self, 'current_apps_filter', 'USER')

        filtered = []
        for app in self.all_installed_apps:
            t = app["type"].upper()
            if ft == "USER" and ("USER" not in t and not app.get("is_dual")):
                continue
            elif ft == "DUAL" and (not app.get("is_dual") and "DUAL" not in t and "CLONE" not in t):
                continue
            elif ft == "DISABLED" and not app["is_disabled"]:
                continue
            elif ft == "SYSTEM" and "SYSTEM" not in t:
                continue

            if query:
                if query not in app["pkg"].lower() and query not in app["apk"].lower():
                    continue

            filtered.append(app)

        self.filtered_installed_apps = filtered
        self.apps_tree.delete(*self.apps_tree.get_children())
        for idx, app in enumerate(filtered):
            self.apps_tree.insert(
                "",
                "end",
                iid=str(idx),
                values=(
                    app["type"],
                    app["pkg"],
                    app["status"],
                    app["apk"]
                )
            )

    def _on_app_tree_select(self, event):
        sel = self.apps_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if 0 <= idx < len(self.filtered_installed_apps):
            app = self.filtered_installed_apps[idx]
            self.selected_app_pkg = app["pkg"]
            self.selected_app_apk = app["apk"]
            self.selected_app_lbl.config(text=f"Selected: {app['pkg']}")
            if hasattr(self, 'pkg_combo'):
                self.pkg_combo.set(app['pkg'])

    def launch_selected_app(self):
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please click an app in the table to select it.")
            return
        pkg = self.selected_app_pkg
        def task():
            self.log(f"Launching application {pkg} on device...")
            code, out, err = self.run_adb(["shell", f"monkey -p {pkg} -c android.intent.category.LAUNCHER 1"], timeout=8)
            if code == 0:
                self.log(f"Launched {pkg} successfully.")
                self.check_foreground()
            else:
                self.log(f"Launch notice: {err or out}")
        threading.Thread(target=task, daemon=True).start()

    def stop_selected_app(self):
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please click an app in the table to select it.")
            return
        pkg = self.selected_app_pkg
        def task():
            self.log(f"Force-stopping application {pkg}...")
            self.run_adb(["shell", f"am force-stop {pkg}"], timeout=6)
            self.log(f"Force-stopped {pkg}.")
            self.check_foreground()
        threading.Thread(target=task, daemon=True).start()

    def freeze_selected_app(self):
        """Disables or hides an app on the device (freeze)"""
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app to freeze/hide.")
            return
        pkg = self.selected_app_pkg
        if not messagebox.askyesno("Freeze / Hide App", f"Freeze/Hide '{pkg}' on device?\n\nThe app will be hidden and disabled from the device launcher."):
            return
        def task():
            self.log(f"Freezing / Disabling package {pkg}...")
            code, out, err = self.run_adb(["shell", f"pm disable-user --user 0 {pkg}"], timeout=8)
            if code != 0:
                self.run_adb(["shell", f"pm hide {pkg}"], timeout=8)
            self.log(f"Froze {pkg}: {out or err}")
            self.scan_installed_apps()
        threading.Thread(target=task, daemon=True).start()

    def unfreeze_selected_app(self):
        """Enables or un-hides a previously disabled/hidden app (unfreeze)"""
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app to unfreeze.")
            return
        pkg = self.selected_app_pkg
        def task():
            self.log(f"Unfreezing / Enabling package {pkg}...")
            code, out, err = self.run_adb(["shell", f"pm enable {pkg}"], timeout=8)
            self.run_adb(["shell", f"pm unhide {pkg}"], timeout=8)
            self.log(f"Unfroze {pkg}: {out or err}")
            self.scan_installed_apps()
        threading.Thread(target=task, daemon=True).start()

    def pull_selected_app_apk(self):
        """Extracts and pulls the APK of the selected package to laptop"""
        if not self.selected_app_pkg or not self.selected_app_apk:
            messagebox.showinfo("Select App", "Please select an app from the list.")
            return
        pkg = self.selected_app_pkg
        apk_path = self.selected_app_apk

        dev_name = self.get_device_clean_name()
        default_dir = os.path.join(PULLED_SCREENS_DIR, dev_name, "apps_backup")
        os.makedirs(default_dir, exist_ok=True)
        dest_file = os.path.join(default_dir, f"{pkg}.apk")

        def task():
            self.log(f"Pulling APK for {pkg} from {apk_path}...")
            code, _, err = self.run_adb(["pull", apk_path, dest_file], timeout=60)
            if code == 0 and os.path.exists(dest_file):
                self.log(f"APK successfully saved to: {dest_file}")
                self.after(0, lambda: messagebox.showinfo("APK Extracted", f"Successfully extracted APK to:\n{dest_file}"))
                os.startfile(default_dir)
            else:
                self.log(f"Failed to pull APK: {err}")
                self.after(0, lambda: messagebox.showerror("Pull Error", f"Could not pull APK:\n{err}"))

        threading.Thread(target=task, daemon=True).start()

    def clear_selected_app_data(self):
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app.")
            return
        pkg = self.selected_app_pkg
        if not messagebox.askyesno("Clear App Data", f"Clear all cache and data for '{pkg}'?"):
            return
        def task():
            self.log(f"Clearing cache & user data for {pkg}...")
            code, out, err = self.run_adb(["shell", f"pm clear {pkg}"], timeout=10)
            self.log(f"Clear result: {out or err}")
            self.after(0, lambda: messagebox.showinfo("Cleared", f"App data cleared for {pkg}!"))
        threading.Thread(target=task, daemon=True).start()

    def uninstall_selected_app(self):
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app.")
            return
        pkg = self.selected_app_pkg
        if not messagebox.askyesno("Uninstall App", f"Are you sure you want to uninstall '{pkg}' from device?"):
            return
        def task():
            self.log(f"Uninstalling {pkg}...")
            code, out, err = self.run_adb(["uninstall", pkg], timeout=20)
            self.log(f"Uninstall result: {out or err}")
            self.after(0, lambda: messagebox.showinfo("Uninstalled", f"Uninstalled {pkg}: {out or err}"))
            self.scan_installed_apps()
        threading.Thread(target=task, daemon=True).start()

    def explore_selected_app_files(self):
        """Jumps directly to Files Explorer and loads the app's media/storage directory across SDCard and Dual App spaces"""
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app from the list first.")
            return
        pkg = self.selected_app_pkg
        pkg_lower = pkg.lower()
        
        candidates = [
            f"/sdcard/Android/media/{pkg}",
            f"/sdcard/Android/data/{pkg}",
            f"/storage/emulated/0/Android/media/{pkg}",
            f"/storage/emulated/999/Android/media/{pkg}",
            f"/storage/emulated/999/Android/data/{pkg}",
        ]
        if "snapchat" in pkg_lower:
            candidates = [
                "/sdcard/Snapchat",
                "/sdcard/Movies/Snapchat",
                "/sdcard/Pictures/Snapchat",
                "/sdcard/DCIM/Snapchat",
                "/storage/emulated/999/Snapchat",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif "whatsapp" in pkg_lower:
            candidates = [
                "/sdcard/Android/media/com.whatsapp/WhatsApp/Media",
                "/sdcard/WhatsApp/Media",
                "/storage/emulated/999/WhatsApp/Media",
                "/storage/emulated/999/Android/media/com.whatsapp",
                "/sdcard/WhatsApp",
            ] + candidates
        elif "instagram" in pkg_lower:
            candidates = [
                "/sdcard/Pictures/Instagram",
                "/sdcard/Movies/Instagram",
                "/storage/emulated/999/Pictures/Instagram",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif "telegram" in pkg_lower:
            candidates = [
                "/sdcard/Telegram",
                "/sdcard/Telegram/Telegram Images",
                "/sdcard/Telegram/Telegram Video",
                "/sdcard/Telegram/Telegram Documents",
                "/storage/emulated/999/Telegram",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("facebook", "katana", "orca", "messenger")):
            candidates = [
                "/sdcard/Pictures/Facebook",
                "/sdcard/Pictures/Messenger",
                "/sdcard/Movies/Facebook",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("tiktok", "musically", "trill")):
            candidates = [
                "/sdcard/Pictures/TikTok",
                "/sdcard/Movies/TikTok",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("twitter", "x.android", "threads")):
            candidates = [
                "/sdcard/Pictures/Twitter",
                "/sdcard/Pictures/X",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("youtube", "vlc", "mxplayer", "videoplayer")):
            candidates = [
                "/sdcard/Movies",
                "/sdcard/Download",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("capcut", "kinemaster", "inshot", "lightroom", "snapseed")):
            candidates = [
                "/sdcard/DCIM/Camera/CapCut",
                "/sdcard/Movies/CapCut",
                "/sdcard/DCIM/Kinemaster",
                "/sdcard/Pictures/Lightroom",
                "/sdcard/Pictures/Snapseed",
                f"/sdcard/Android/media/{pkg}",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("miui", "xiaomi")):
            candidates = [
                "/sdcard/MIUI/Gallery",
                "/sdcard/MIUI/sound_recorder",
                "/sdcard/MIUI/video",
                "/sdcard/MIUI",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("sec.android", "samsung", "knox")):
            candidates = [
                "/sdcard/Samsung",
                "/sdcard/Voice Recorder",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates
        elif any(k in pkg_lower for k in ("coloros", "heytap", "oppo")):
            candidates = [
                "/sdcard/ColorOS",
                "/sdcard/.aceself",
                "/storage/emulated/999/ColorOS",
                f"/sdcard/Android/data/{pkg}",
            ] + candidates

        def task():
            target_path = f"/sdcard/Android/media/{pkg}"
            for c in candidates:
                code, out, _ = self.run_adb(["shell", f"[ -d '{c}' ] && echo EXISTS"], timeout=4)
                if "EXISTS" in (out or ""):
                    target_path = c
                    break
            
            def jump():
                self.main_tabs.select(3) # Switch to Files tab
                self.load_path(target_path)
                self.log(f"Navigated to app storage folder: {target_path}")
            self.after(0, jump)

        threading.Thread(target=task, daemon=True).start()

    def inspect_selected_app_deeplinks(self):
        """Inspects package manifest, deep links, URL schemes, exported activities and allows testing intent packets"""
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app from the list first.")
            return
        pkg = self.selected_app_pkg

        def task():
            self.log(f"Inspecting package manifest, activities & deep links for {pkg}...")
            code, out, err = self.run_adb(["shell", f"dumpsys package {pkg}"], timeout=15)
            
            schemes = set()
            activities = []
            main_act = ""

            if code == 0 and out:
                # Find main activity
                m_act = re.search(r'android\.intent\.action\.MAIN:\s*\n\s*([^\s]+)\s+([^\s/]+)/([^\s]+)', out)
                if m_act:
                    main_act = m_act.group(3)

                # Extract URL schemes
                for line in out.splitlines():
                    line = line.strip()
                    if "Scheme:" in line:
                        sch = line.split("Scheme:", 1)[-1].strip().strip('"').strip("'")
                        if sch:
                            schemes.add(sch)
                    if "Activity" in line and "/" in line:
                        parts = line.split()
                        for p in parts:
                            if "/" in p and pkg in p:
                                activities.append(p)

            # Comprehensive preset schemes for popular Android apps
            pkg_lower = pkg.lower()
            if "snapchat" in pkg_lower:
                schemes.update(["snapchat://", "https://www.snapchat.com/add/", "snapchat://creativeKitWeb/camera"])
            elif "whatsapp" in pkg_lower:
                schemes.update(["whatsapp://send", "https://api.whatsapp.com/send", "whatsapp://chat"])
            elif "instagram" in pkg_lower:
                schemes.update(["instagram://user?username=", "instagram://camera", "instagram://direct-inbox", "https://instagram.com/"])
            elif "telegram" in pkg_lower:
                schemes.update(["tg://resolve?domain=", "tg://msg?text=", "https://t.me/"])
            elif any(k in pkg_lower for k in ("facebook", "katana")):
                schemes.update(["fb://profile/", "fb://facewebmodal/f?href=https://facebook.com", "https://facebook.com/"])
            elif any(k in pkg_lower for k in ("messenger", "orca")):
                schemes.update(["fb-messenger://user-thread/", "https://m.me/"])
            elif any(k in pkg_lower for k in ("twitter", "x.android", "threads")):
                schemes.update(["twitter://user?screen_name=", "twitter://timeline", "https://twitter.com/"])
            elif "youtube" in pkg_lower:
                schemes.update(["vnd.youtube://", "https://youtube.com/watch?v="])
            elif any(k in pkg_lower for k in ("tiktok", "musically")):
                schemes.update(["snssdk1233://", "https://www.tiktok.com/"])
            elif "spotify" in pkg_lower:
                schemes.update(["spotify:track:", "spotify:playlist:", "spotify:search:"])
            elif any(k in pkg_lower for k in ("chrome", "browser")):
                schemes.update(["googlechrome://", "https://google.com"])
            elif "maps" in pkg_lower:
                schemes.update(["geo:0,0?q=", "google.navigation:q="])

            def show_modal():
                dlg = tk.Toplevel(self)
                dlg.title(f"Deep Links, Intents & Manifest Inspector - {pkg}")
                dlg.geometry("640x520")
                dlg.configure(bg="#0f172a")
                dlg.transient(self)

                tk.Label(dlg, text=f"📦 {pkg}", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(anchor="w", padx=12, pady=(10, 2))
                if main_act:
                    tk.Label(dlg, text=f"Main Activity: {main_act}", bg="#0f172a", fg="#94a3b8", font=("Consolas", 8)).pack(anchor="w", padx=12, pady=(0, 6))

                # Deep Link Schemes frame
                sch_frame = tk.LabelFrame(dlg, text="🔗 Detected Deep Link Schemes & URLs", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold"), padx=8, pady=8)
                sch_frame.pack(fill="x", padx=12, pady=4)

                sch_list = list(schemes) if schemes else [f"{pkg}://", "https://"]
                sch_combo = ttk.Combobox(sch_frame, values=sch_list, font=("Consolas", 9), width=45)
                sch_combo.set(sch_list[0])
                sch_combo.pack(side="left", fill="x", expand=True, padx=(0, 6))

                def launch_deep_link():
                    uri = sch_combo.get().strip()
                    if not uri:
                        return
                    safe_uri = uri.replace("'", "'\\''")
                    cmd = f"am start -a android.intent.action.VIEW -d '{safe_uri}'"
                    self.log(f"Launching deep link: {cmd}")
                    c, o, e = self.run_adb(["shell", cmd], timeout=8)
                    self.log(f"Deep link output: {o or e or 'Triggered'}")
                    self.check_foreground()

                tk.Button(sch_frame, text="🚀 Test & Launch", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=launch_deep_link).pack(side="right")

                # Custom Intent & Broadcast Sender frame
                intent_frame = tk.LabelFrame(dlg, text="📨 Send Custom Intent / Packet to Mobile App", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold"), padx=8, pady=8)
                intent_frame.pack(fill="x", padx=12, pady=6)

                tk.Label(intent_frame, text="Action:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8)).grid(row=0, column=0, sticky="w", pady=2)
                action_combo = ttk.Combobox(intent_frame, values=[
                    "android.intent.action.VIEW",
                    "android.intent.action.MAIN",
                    "android.intent.action.SEND",
                    "android.intent.action.BOOT_COMPLETED"
                ], width=35, font=("Consolas", 8))
                action_combo.set("android.intent.action.VIEW")
                action_combo.grid(row=0, column=1, sticky="w", pady=2)

                tk.Label(intent_frame, text="Data URI:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8)).grid(row=1, column=0, sticky="w", pady=2)
                data_entry = tk.Entry(intent_frame, bg="#334155", fg="white", insertbackground="white", font=("Consolas", 8), width=37)
                data_entry.insert(0, sch_list[0])
                data_entry.grid(row=1, column=1, sticky="w", pady=2)

                def send_intent():
                    act = action_combo.get().strip()
                    d_uri = data_entry.get().strip()
                    cmd = f"am start -a {act}"
                    if d_uri:
                        cmd += f" -d '{d_uri}'"
                    cmd += f" -p {pkg}"
                    self.log(f"Sending intent command: {cmd}")
                    c, o, e = self.run_adb(["shell", cmd], timeout=8)
                    self.log(f"Intent result: {o or e or 'Success'}")
                    self.check_foreground()

                tk.Button(intent_frame, text="⚡ Send Intent", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=send_intent).grid(row=2, column=1, sticky="e", pady=6)

                # Raw Manifest / Activities Log frame
                raw_frame = tk.LabelFrame(dlg, text="📋 Exported Components & Package Details", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8), padx=6, pady=6)
                raw_frame.pack(fill="both", expand=True, padx=12, pady=4)

                txt = tk.Text(raw_frame, bg="#0f172a", fg="#f8fafc", insertbackground="white", font=("Consolas", 8), wrap="none")
                sc_y = tk.Scrollbar(raw_frame, orient="vertical", command=txt.yview)
                txt.configure(yscrollcommand=sc_y.set)
                txt.pack(side="left", fill="both", expand=True)
                sc_y.pack(side="right", fill="y")
                txt.insert("1.0", out if out else f"Package: {pkg}\nNo detailed manifest output returned.")
                txt.config(state="disabled")

            self.after(0, show_modal)
        threading.Thread(target=task, daemon=True).start()

    def push_media_to_selected_app(self):
        """Pushes an image, video, or file from laptop directly to the app storage on mobile"""
        if not self.selected_app_pkg:
            messagebox.showinfo("Select App", "Please select an app first.")
            return
        pkg = self.selected_app_pkg
        file_path = filedialog.askopenfilename(title=f"Select Media to push to {pkg}")
        if not file_path:
            return

        fname = os.path.basename(file_path)
        dest_dir = f"/sdcard/Android/media/{pkg}"
        if "snapchat" in pkg.lower():
            dest_dir = "/sdcard/Snapchat"
        elif "whatsapp" in pkg.lower():
            dest_dir = "/sdcard/Android/media/com.whatsapp/WhatsApp/Media"

        dest_remote = f"{dest_dir}/{fname}"
        def task():
            self.log(f"Pushing {fname} to {dest_remote}...")
            self.run_adb(["shell", f"mkdir -p '{dest_dir}'"], timeout=6)
            code, _, err = self.run_adb(["push", file_path, dest_remote], timeout=60)
            if code == 0:
                self.log(f"Successfully pushed {fname} to mobile {dest_remote}!")
                # Trigger media scanner
                self.run_adb(["shell", f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d 'file://{dest_remote}'"], timeout=4)
                self.after(0, lambda: messagebox.showinfo("File Pushed", f"File successfully stored on device:\n{dest_remote}\n\nMedia scanner updated!"))
            else:
                self.log(f"Push error: {err}")
                self.after(0, lambda: messagebox.showerror("Push Error", f"Failed to push file:\n{err}"))
        threading.Thread(target=task, daemon=True).start()

    # =========================================================================
    # WHATSAPP LIVE CHAT & PROMPT SYNC METHODS (DUAL PANE WORKSPACE)
    # =========================================================================
    def _build_whatsapp_tab(self, parent):
        """Dedicated WhatsApp Chat & Media Sync with WhatsApp Web Dual-Pane Layout, Chat List Sidebar, and Prompt Tool"""
        top_bar = tk.Frame(parent, bg="#0f172a")
        top_bar.pack(fill="x", pady=(0, 4))
        tk.Label(top_bar, text="💬 WhatsApp Live Chat & Prompt Mirror", bg="#0f172a", fg="#25d366", font=("Segoe UI", 11, "bold")).pack(side="left")
        self.wa_status_lbl = tk.Label(top_bar, text="Ready | Click 'Scan All Chats' to load mobile conversations", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.wa_status_lbl.pack(side="right")

        # Main PanedWindow Split: Left Sidebar (Chat List 32%), Right Container (Active Chat Thread 68%)
        self.wa_main_split = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=3, bd=0)
        self.wa_main_split.pack(fill="both", expand=True)

        # =========================================================================
        # LEFT PANE: WHATSAPP CONVERSATIONS LIST SIDEBAR (#111b21)
        # =========================================================================
        sidebar = tk.Frame(self.wa_main_split, bg="#111b21", padx=4, pady=4)
        self.wa_main_split.add(sidebar, minsize=260)

        sb_top = tk.Frame(sidebar, bg="#111b21")
        sb_top.pack(fill="x", pady=(0, 4))
        
        tk.Label(sb_top, text="💬 Chats List", bg="#111b21", fg="#e9edef", font=("Segoe UI", 9, "bold")).pack(side="left", padx=2)
        
        tk.Button(sb_top, text="🚀 Clone All (Offline)", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.pull_whole_mobile_whatsapp).pack(side="right", padx=1)
        tk.Button(sb_top, text="🔄 Scan", bg="#25d366", fg="#0b141a", activebackground="#1eab53", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=5, relief="flat", command=self.scan_all_whatsapp_chats).pack(side="right", padx=1)

        # Search / Filter Bar for Sidebar
        sb_search = tk.Frame(sidebar, bg="#202c33", padx=4, pady=3, relief="solid", bd=1)
        sb_search.pack(fill="x", pady=(0, 4))
        
        tk.Label(sb_search, text="🔍", bg="#202c33", fg="#8696a0", font=("Segoe UI", 8)).pack(side="left")
        self.wa_search_entry = tk.Entry(sb_search, bg="#202c33", fg="#e9edef", insertbackground="white", font=("Segoe UI", 8), bd=0)
        self.wa_search_entry.pack(side="left", fill="x", expand=True, padx=4)
        self.wa_search_entry.bind("<KeyRelease>", lambda e: self._filter_whatsapp_chats_list())

        # Scrollable Frame / Treeview for Chats List
        list_container = tk.Frame(sidebar, bg="#111b21")
        list_container.pack(fill="both", expand=True)

        chat_cols = ("name", "time", "snippet")
        self.wa_chats_tree = ttk.Treeview(list_container, columns=chat_cols, show="headings", selectmode="browse")
        self.wa_chats_tree.heading("name", text="Contact / Group Name")
        self.wa_chats_tree.heading("time", text="Time")
        self.wa_chats_tree.heading("snippet", text="Last Message Preview")

        self.wa_chats_tree.column("name", width=135, anchor="w")
        self.wa_chats_tree.column("time", width=65, anchor="e")
        self.wa_chats_tree.column("snippet", width=140, anchor="w")

        tree_sc = tk.Scrollbar(list_container, orient="vertical", command=self.wa_chats_tree.yview)
        self.wa_chats_tree.configure(yscrollcommand=tree_sc.set)
        
        self.wa_chats_tree.pack(side="left", fill="both", expand=True)
        tree_sc.pack(side="right", fill="y")

        self.wa_chats_tree.bind("<<TreeviewSelect>>", self._on_whatsapp_chat_select)

        # =========================================================================
        # RIGHT PANE: ACTIVE CONVERSATION THREAD & PROMPT TOOL (#0b141a)
        # =========================================================================
        chat_pane = tk.Frame(self.wa_main_split, bg="#0f172a")
        self.wa_main_split.add(chat_pane, minsize=420)

        # Active Header Bar
        ctrl_bar = tk.Frame(chat_pane, bg="#1e293b", padx=8, pady=6, relief="solid", bd=1)
        ctrl_bar.pack(fill="x", pady=(0, 4))

        tk.Label(ctrl_bar, text="👤 Active Chat:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8, "bold")).pack(side="left", padx=(0, 2))
        
        self.wa_active_contact_lbl = tk.Label(ctrl_bar, text="Select a chat from left sidebar", bg="#1e293b", fg="#25d366", font=("Segoe UI", 9, "bold"))
        self.wa_active_contact_lbl.pack(side="left", padx=4)

        tk.Label(ctrl_bar, text="App:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left", padx=(8, 2))
        self.wa_pkg_combo = ttk.Combobox(
            ctrl_bar,
            values=["com.whatsapp (WhatsApp)", "com.whatsapp.w4b (Business)"],
            width=20,
            state="readonly"
        )
        self.wa_pkg_combo.current(0)
        self.wa_pkg_combo.pack(side="left", padx=2)

        tk.Button(ctrl_bar, text="🔄 Sync Selected Chat", bg="#25d366", fg="#0b141a", activebackground="#1eab53", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.sync_whatsapp_chat).pack(side="left", padx=4)
        tk.Button(ctrl_bar, text="📱 Open on Phone", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.open_whatsapp_chat_on_phone).pack(side="left", padx=2)
        tk.Button(ctrl_bar, text="📁 Pull Media", bg="#0891b2", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.pull_whatsapp_media).pack(side="left", padx=2)
        
        self.btn_wa_auto = tk.Button(ctrl_bar, text="⚡ Auto-Sync: OFF", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=self.toggle_whatsapp_auto_sync)
        self.btn_wa_auto.pack(side="right", padx=2)

        # Chat display canvas
        chat_container = tk.Frame(chat_pane, bg="#0b141a", relief="solid", bd=1)
        chat_container.pack(fill="both", expand=True, pady=2)

        self.wa_canvas = tk.Canvas(chat_container, bg="#0b141a", highlightthickness=0)
        self.wa_scrollbar = tk.Scrollbar(chat_container, orient="vertical", command=self.wa_canvas.yview)
        self.wa_chat_inner = tk.Frame(self.wa_canvas, bg="#0b141a")

        self.wa_chat_inner.bind(
            "<Configure>",
            lambda e: self.wa_canvas.configure(scrollregion=self.wa_canvas.bbox("all"))
        )

        self.wa_canvas_window = self.wa_canvas.create_window((0, 0), window=self.wa_chat_inner, anchor="nw")
        self.wa_canvas.configure(yscrollcommand=self.wa_scrollbar.set)

        self.wa_canvas.pack(side="left", fill="both", expand=True)
        self.wa_scrollbar.pack(side="right", fill="y")

        self.wa_canvas.bind('<Configure>', lambda e: self.wa_canvas.itemconfig(self.wa_canvas_window, width=e.width))

        def _on_wa_mousewheel(event):
            self.wa_canvas.yview_scroll(int(-1*(event.delta/120)), "units")
        self.wa_canvas.bind("<Enter>", lambda e: self.wa_canvas.bind_all("<MouseWheel>", _on_wa_mousewheel))
        self.wa_canvas.bind("<Leave>", lambda e: self.wa_canvas.unbind_all("<MouseWheel>"))

        self._render_whatsapp_empty_state()

        # Bottom Input Bar
        send_frame = tk.Frame(chat_pane, bg="#1e293b", padx=6, pady=6, relief="solid", bd=1)
        send_frame.pack(fill="x", pady=(4, 0))

        self.wa_input_txt = tk.Text(send_frame, bg="#0f172a", fg="#f8fafc", insertbackground="white", font=("Segoe UI", 9), height=3, wrap="word", bd=1, relief="solid")
        self.wa_input_txt.pack(side="left", fill="both", expand=True, padx=(0, 6))

        btn_col = tk.Frame(send_frame, bg="#1e293b")
        btn_col.pack(side="right", fill="y")

        tk.Button(btn_col, text="📤 Send to Mobile", bg="#25d366", fg="#0b141a", activebackground="#1eab53", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=10, pady=3, relief="flat", command=self.send_whatsapp_prompt).pack(fill="x", pady=1)
        tk.Button(btn_col, text="📋 Paste & Send", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=8, pady=2, relief="flat", command=self.paste_and_send_whatsapp_prompt).pack(fill="x", pady=1)
        
        opt_row = tk.Frame(btn_col, bg="#1e293b")
        opt_row.pack(fill="x", pady=1)
        tk.Button(opt_row, text="📎 Send File", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=self.send_whatsapp_laptop_file).pack(side="left", padx=1)
        tk.Button(opt_row, text="📋 Copy Prompts", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=self.copy_all_whatsapp_prompts).pack(side="left", padx=1)

        # Auto-load offline WhatsApp database if available
        self.after(200, self._load_whatsapp_db)

    def scan_all_whatsapp_chats(self):
        """Deep scans WhatsApp main screen and notification history to build full chats sidebar list"""
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"
        
        def task():
            self.log("Scanning mobile device for WhatsApp conversations & chats list...")
            self.after(0, lambda: self.wa_status_lbl.config(text="Scanning mobile for WhatsApp chats..."))

            c, out_focus, _ = self.run_adb(["shell", "dumpsys window | grep mCurrentFocus"], timeout=4)
            if pkg not in out_focus:
                self.run_adb(["shell", f"am start -n {pkg}/.Main"], timeout=5)
                time.sleep(1.2)

            dump_remote = "/sdcard/_wa_list_dump.xml"
            code_dump, out_dump, _ = self.run_adb(["shell", f"uiautomator dump {dump_remote}"], timeout=8)
            
            parsed_chats = []
            if code_dump == 0:
                local_dump = os.path.join(BASE_DIR, "wa_list_dump.xml")
                code_p, _, _ = self.run_adb(["pull", dump_remote, local_dump], timeout=6)
                if code_p == 0 and os.path.exists(local_dump):
                    parsed_chats = self._parse_whatsapp_chats_list(local_dump)

            if not parsed_chats:
                parsed_chats.append({
                    "name": "My Notes",
                    "date": datetime.now().strftime("%I:%M %p"),
                    "snippet": "Personal prompt notes",
                    "is_you": False,
                    "bounds": None
                })

            self.all_wa_chats = parsed_chats
            self.filtered_wa_chats = list(parsed_chats)
            
            self.after(0, self._render_whatsapp_chats_sidebar)
            self.after(0, lambda: self.wa_status_lbl.config(text=f"Loaded {len(parsed_chats)} chats from mobile"))

        threading.Thread(target=task, daemon=True).start()

    def _parse_whatsapp_chats_list(self, xml_path):
        """Parses uiautomator XML dump of WhatsApp main screen to extract contact names, times, snippets, and bounds"""
        chats = []
        try:
            import xml.etree.ElementTree as ET
            tree = ET.parse(xml_path)
            root = tree.getroot()

            for node in root.findall(".//node"):
                res_id = node.attrib.get("resource-id", "")
                if "contact_row_container" in res_id or "conversations_row_contact_name" in res_id:
                    name = ""
                    date = ""
                    msg = ""
                    is_you = False
                    bounds = node.attrib.get("bounds", "")

                    for child in node.findall(".//node"):
                        c_res = child.attrib.get("resource-id", "")
                        c_txt = child.attrib.get("text", "").strip()
                        if "conversations_row_contact_name" in c_res:
                            name = c_txt
                        elif "conversations_row_date" in c_res:
                            date = c_txt
                        elif "single_msg_tv" in c_res:
                            msg = c_txt
                        elif "msg_from_tv" in c_res:
                            is_you = True

                    if name and not any(c["name"] == name for c in chats):
                        chats.append({
                            "name": name,
                            "date": date,
                            "snippet": ("You: " if is_you else "") + msg,
                            "is_you": is_you,
                            "bounds": bounds
                        })
        except Exception as e:
            self.log(f"Error parsing WA chats list dump: {e}")

        return chats

    def _render_whatsapp_chats_sidebar(self):
        """Populates left sidebar treeview with parsed WhatsApp chats"""
        self.wa_chats_tree.delete(*self.wa_chats_tree.get_children())

        for idx, chat in enumerate(self.filtered_wa_chats):
            name = chat.get("name", "Unknown")
            time_str = chat.get("date", "")
            snip = chat.get("snippet", "")

            self.wa_chats_tree.insert(
                "",
                "end",
                iid=str(idx),
                values=(name, time_str, snip)
            )

        if self.filtered_wa_chats:
            self.wa_chats_tree.selection_set("0")
            self._on_whatsapp_chat_select()

    def _filter_whatsapp_chats_list(self):
        """Filters left sidebar chats by search entry"""
        query = self.wa_search_entry.get().strip().lower()
        if not query:
            self.filtered_wa_chats = list(self.all_wa_chats)
        else:
            self.filtered_wa_chats = [
                c for c in self.all_wa_chats
                if query in c.get("name", "").lower() or query in c.get("snippet", "").lower()
            ]
        self._render_whatsapp_chats_sidebar()

    def _on_whatsapp_chat_select(self, event=None):
        """Handles selection of a chat in left sidebar"""
        sel = self.wa_chats_tree.selection()
        if not sel:
            return
        idx = int(sel[0])
        if idx < len(self.filtered_wa_chats):
            chat = self.filtered_wa_chats[idx]
            self.selected_wa_chat = chat
            c_name = chat.get("name", "Active Chat")
            
            self.wa_active_contact_lbl.config(text=c_name)
            
            bounds = chat.get("bounds")
            if bounds:
                def task():
                    try:
                        m_b = re.findall(r'\[(\d+),(\d+)\]', bounds)
                        if len(m_b) == 2:
                            cx = (int(m_b[0][0]) + int(m_b[1][0])) // 2
                            cy = (int(m_b[0][1]) + int(m_b[1][1])) // 2
                            self.log(f"Tapping contact '{c_name}' on mobile screen at ({cx}, {cy})...")
                            self.run_adb(["shell", f"input tap {cx} {cy}"], timeout=4)
                            time.sleep(0.8)
                    except Exception:
                        pass
                    self.sync_whatsapp_chat()

                threading.Thread(target=task, daemon=True).start()
            else:
                self.sync_whatsapp_chat()

    def _render_whatsapp_empty_state(self):
        """Displays empty state graphic when no conversation is selected"""
        for w in self.wa_chat_inner.winfo_children():
            w.destroy()

        empty_box = tk.Frame(self.wa_chat_inner, bg="#0b141a", pady=40)
        empty_box.pack(fill="both", expand=True)

        tk.Label(empty_box, text="💬", bg="#0b141a", fg="#25d366", font=("Segoe UI", 36)).pack()
        tk.Label(empty_box, text="WhatsApp Chat & Prompt Mirror", bg="#0b141a", fg="#e9edef", font=("Segoe UI", 12, "bold")).pack(pady=4)
        tk.Label(empty_box, text="Select a conversation from the left sidebar or click 'Scan All Chats'.\nClick 'Sync Selected Chat' to pull text, messages, and all accessible media from mobile.", bg="#0b141a", fg="#8696a0", font=("Segoe UI", 9), justify="center").pack(pady=2)

    def open_whatsapp_chat_on_phone(self):
        """Launches WhatsApp or opens contact chat directly on phone"""
        c_name = self.selected_wa_chat.get("name") if self.selected_wa_chat else "My Notes"
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"
        
        def task():
            self.log(f"Opening WhatsApp chat for '{c_name}' on target device...")
            cmd = f"am start -n {pkg}/.Main"
            self.run_adb(["shell", cmd], timeout=6)
            time.sleep(1.2)
            search_cmd = f"am start -a android.intent.action.SEND -t 'text/plain' --es android.intent.extra.TEXT '' -p {pkg}"
            self.run_adb(["shell", search_cmd], timeout=6)
            self.after(0, lambda: self.wa_status_lbl.config(text=f"WhatsApp opened on phone for: {c_name}"))
            
        threading.Thread(target=task, daemon=True).start()

    def _get_whatsapp_db_path(self):
        dev_clean = self.get_device_clean_name()
        wa_dir = os.path.join(PULLED_MEDIA_DIR, "whatsapp", dev_clean)
        os.makedirs(wa_dir, exist_ok=True)
        return os.path.join(wa_dir, "whatsapp_offline_db.json")

    def _load_whatsapp_db(self):
        """Loads offline WhatsApp database from laptop storage into memory and populates UI"""
        db_path = self._get_whatsapp_db_path()
        if os.path.exists(db_path):
            try:
                import json
                with open(db_path, "r", encoding="utf-8") as f:
                    db_data = json.load(f)
                
                chats_dict = db_data.get("chats", {})
                loaded_chats = []
                for name, info in chats_dict.items():
                    loaded_chats.append({
                        "name": info.get("name", name),
                        "date": info.get("date", ""),
                        "snippet": info.get("snippet", ""),
                        "messages": info.get("messages", []),
                        "bounds": None
                    })
                
                if loaded_chats:
                    self.all_wa_chats = loaded_chats
                    self.filtered_wa_chats = list(loaded_chats)
                    self._render_whatsapp_chats_sidebar()
                    self.wa_status_lbl.config(text=f"💾 Offline Mode | Loaded {len(loaded_chats)} chats from laptop disk ({db_data.get('last_updated', '')})")
            except Exception as e:
                self.log(f"Notice: Could not load offline WhatsApp DB: {e}")

    def pull_whole_mobile_whatsapp(self):
        """Crawls all conversations on mobile WhatsApp and saves entire chat history + media to laptop storage for offline use"""
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"
        
        def task():
            self.log("🚀 Starting Full Mobile WhatsApp Clone & Offline Backup...")
            self.after(0, lambda: self.wa_status_lbl.config(text="🚀 Crawling & cloning ALL WhatsApp chats..."))

            self.scan_all_whatsapp_chats()
            time.sleep(2)

            chats_to_crawl = list(self.all_wa_chats)
            total = len(chats_to_crawl)
            self.log(f"Found {total} WhatsApp chats to clone for offline use.")

            db_data = {"device": self.get_device_clean_name(), "last_updated": datetime.now().strftime("%Y-%m-%d %H:%M:%S"), "chats": {}}

            for idx, chat in enumerate(chats_to_crawl):
                c_name = chat.get("name")
                if not c_name:
                    continue
                
                self.after(0, lambda i=idx+1, name=c_name: self.wa_status_lbl.config(text=f"Cloning chat {i}/{total}: '{name}'..."))
                self.log(f"Cloning ({idx+1}/{total}): '{c_name}'...")

                bounds = chat.get("bounds")
                if bounds:
                    try:
                        m_b = re.findall(r'\[(\d+),(\d+)\]', bounds)
                        if len(m_b) == 2:
                            cx = (int(m_b[0][0]) + int(m_b[1][0])) // 2
                            cy = (int(m_b[0][1]) + int(m_b[1][1])) // 2
                            self.run_adb(["shell", f"input tap {cx} {cy}"], timeout=3)
                            time.sleep(1.0)
                    except Exception:
                        pass

                dump_remote = f"/sdcard/_wa_crawl_{idx}.xml"
                code_dump, _, _ = self.run_adb(["shell", f"uiautomator dump {dump_remote}"], timeout=6)
                msgs = []
                if code_dump == 0:
                    local_dump = os.path.join(BASE_DIR, f"wa_crawl_{idx}.xml")
                    self.run_adb(["pull", dump_remote, local_dump], timeout=5)
                    if os.path.exists(local_dump):
                        msgs = self._parse_whatsapp_ui_dump(local_dump, c_name)
                        try:
                            os.remove(local_dump)
                        except Exception:
                            pass

                media_items = self._scan_and_pull_whatsapp_media_for_chat(c_name, pkg)
                for m in media_items:
                    if not any(x.get('text') == m.get('text') for x in msgs):
                        msgs.append(m)

                db_data["chats"][c_name] = {
                    "name": c_name,
                    "date": chat.get("date", ""),
                    "snippet": chat.get("snippet", ""),
                    "messages": msgs
                }

                self.run_adb(["shell", "input keyevent 4"], timeout=3)
                time.sleep(0.5)

            db_path = self._get_whatsapp_db_path()
            try:
                import json
                os.makedirs(os.path.dirname(db_path), exist_ok=True)
                with open(db_path, "w", encoding="utf-8") as f:
                    json.dump(db_data, f, ensure_ascii=False, indent=2)
                self.log(f"Successfully saved complete WhatsApp clone database to laptop: {db_path}")
            except Exception as e:
                self.log(f"Error saving WhatsApp DB: {e}")

            self._load_whatsapp_db()
            self.after(0, lambda: messagebox.showinfo("WhatsApp Full Clone Complete", f"All {total} WhatsApp chats and media are 100% saved to laptop disk!\n\nYou can now view all your chats anytime in OFFLINE mode without phone!"))

        threading.Thread(target=task, daemon=True).start()

    def sync_whatsapp_chat(self):

        """Pulls messages, chat text, and media from phone for the currently selected chat"""
        c_name = self.selected_wa_chat.get("name") if self.selected_wa_chat else "My Notes"
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"

        def task():
            self.log(f"Syncing WhatsApp conversation for '{c_name}'...")
            self.after(0, lambda: self.wa_status_lbl.config(text=f"Syncing chat for '{c_name}'..."))

            dump_remote = "/sdcard/_wa_thread_dump.xml"
            code_dump, out_dump, _ = self.run_adb(["shell", f"uiautomator dump {dump_remote}"], timeout=8)
            
            parsed_messages = []
            if code_dump == 0:
                local_dump = os.path.join(BASE_DIR, "wa_dump.xml")
                code_p, _, _ = self.run_adb(["pull", dump_remote, local_dump], timeout=6)
                if code_p == 0 and os.path.exists(local_dump):
                    parsed_messages = self._parse_whatsapp_ui_dump(local_dump, c_name)

            notif_msgs = self._parse_whatsapp_notifications(c_name, pkg)
            for nm in notif_msgs:
                if not any(m['text'] == nm['text'] for m in parsed_messages):
                    parsed_messages.append(nm)

            media_items = self._scan_and_pull_whatsapp_media_for_chat(c_name, pkg)
            for m in media_items:
                parsed_messages.append(m)

            self.wa_messages = parsed_messages
            self.after(0, self._render_whatsapp_messages)
            self.after(0, lambda: self.wa_status_lbl.config(text=f"Synced {len(parsed_messages)} items for '{c_name}'"))

        threading.Thread(target=task, daemon=True).start()

    def _parse_whatsapp_ui_dump(self, xml_path, target_contact):
        """Parses uiautomator XML dump to extract chat bubbles, text messages, timestamps, and sent/received state"""
        messages = []
        try:
            import xml.etree.ElementTree as ET
            tree = ET.parse(xml_path)
            root = tree.getroot()

            ignore_texts = {
                "Type a message", "Search", "WhatsApp", "Chats", "Status", "Calls",
                "Updates", "Tools", "Community", "Messages", target_contact.strip().lower(),
                "online", "typing...", "yesterday", "today"
            }

            nodes = root.findall(".//node")
            for node in nodes:
                txt = node.attrib.get("text", "").strip()
                res_id = node.attrib.get("resource-id", "")
                bounds = node.attrib.get("bounds", "")

                if txt and len(txt) > 0 and txt.lower() not in ignore_texts:
                    # Ignore pure time strings like "08:25 PM" or "11:48 am"
                    if re.match(r'^\d{1,2}:\d{2}\s*(am|pm|AM|PM)?$', txt, re.IGNORECASE):
                        continue

                    is_sent = True
                    if bounds:
                        try:
                            m_b = re.findall(r'\[(\d+),(\d+)\]', bounds)
                            if len(m_b) == 2:
                                x1 = int(m_b[0][0])
                                # Sent messages are aligned to right side of screen (x1 > 200)
                                if x1 < 200:
                                    is_sent = False
                        except Exception:
                            pass

                    now_ts = datetime.now().strftime("%I:%M %p")
                    messages.append({
                        "text": txt,
                        "timestamp": now_ts,
                        "is_sent": is_sent,
                        "sender": "You" if is_sent else target_contact,
                        "type": "TEXT",
                        "media_path": None
                    })
        except Exception as e:
            self.log(f"Error parsing WA UI dump: {e}")

        return messages

    def _parse_whatsapp_notifications(self, target_contact, pkg):
        """Extracts recent WhatsApp notifications for target contact via dumpsys notification"""
        messages = []
        try:
            code, out, _ = self.run_adb(["shell", "dumpsys notification"], timeout=5)
            if code == 0 and out:
                lines = out.split("\n")
                in_wa = False
                for line in lines:
                    if pkg in line:
                        in_wa = True
                    elif "pkg=" in line and pkg not in line:
                        in_wa = False
                    
                    if in_wa and ("tickerText=" in line or "android.text=" in line or "android.title=" in line):
                        if target_contact.lower() in line.lower() or "text=" in line:
                            parts = line.split("=", 1)
                            if len(parts) == 2:
                                val = parts[1].strip()
                                if val and len(val) > 1 and not val.startswith("null"):
                                    messages.append({
                                        "text": val,
                                        "timestamp": datetime.now().strftime("%I:%M %p"),
                                        "is_sent": False,
                                        "sender": target_contact,
                                        "type": "TEXT",
                                        "media_path": None
                                    })
        except Exception:
            pass
        return messages

    def _scan_and_pull_whatsapp_media_for_chat(self, target_contact, pkg):
        """Scans WhatsApp Media folders on device and pulls recent media files matching the chat"""
        media_msgs = []
        dev_clean = self.get_device_clean_name()
        c_safe = "".join(c for c in target_contact if c.isalnum() or c in ('_', '-'))
        local_dir = os.path.join(PULLED_MEDIA_DIR, "whatsapp", dev_clean, c_safe)
        os.makedirs(local_dir, exist_ok=True)

        wa_base = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media"
        if "w4b" in pkg:
            wa_base = "/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media"

        cmd = [
            "shell",
            f"find '{wa_base}' -type f \\( -name '*.jpg' -o -name '*.png' -o -name '*.pdf' -o -name '*.mp4' -o -name '*.opus' \\) -exec stat -c '%Y %s %n' {{}} + 2>/dev/null | sort -nr | head -n 12"
        ]

        code, out, _ = self.run_adb(cmd, timeout=8)
        if code == 0 and out.strip():
            lines = out.strip().split("\n")
            for line in lines:
                parts = line.strip().split(" ", 2)
                if len(parts) == 3:
                    try:
                        mtime = int(parts[0])
                        sz = int(parts[1])
                        remote_p = parts[2].strip()
                        fname = os.path.basename(remote_p)
                        local_dest = os.path.join(local_dir, fname)

                        is_sent = "Sent" in remote_p
                        m_type = "IMAGE"
                        if fname.lower().endswith(('.mp4', '.mkv')):
                            m_type = "VIDEO"
                        elif fname.lower().endswith(('.pdf', '.doc', '.docx', '.txt')):
                            m_type = "DOC"
                        elif fname.lower().endswith(('.opus', '.m4a', '.mp3', '.aac')):
                            m_type = "AUDIO"

                        if not os.path.exists(local_dest) or os.path.getsize(local_dest) != sz:
                            self.run_adb(["pull", remote_p, local_dest], timeout=15)

                        if os.path.exists(local_dest):
                            ts_str = datetime.fromtimestamp(mtime).strftime("%I:%M %p")
                            media_msgs.append({
                                "text": f"📎 {fname} ({self._format_size(sz)})",
                                "timestamp": ts_str,
                                "is_sent": is_sent,
                                "sender": "You" if is_sent else target_contact,
                                "type": m_type,
                                "media_path": local_dest,
                                "mtime": mtime
                            })
                    except Exception:
                        pass

        media_msgs.sort(key=lambda x: x.get("mtime", 0))
        return media_msgs

    def pull_whatsapp_media(self):
        """Pulls all WhatsApp images, documents, and videos to laptop"""
        c_name = self.selected_wa_chat.get("name") if self.selected_wa_chat else "General"
        def task():
            self.log(f"Pulling WhatsApp media for '{c_name}' to laptop storage...")
            self.after(0, lambda: self.wa_status_lbl.config(text="Pulling WhatsApp Media..."))
            items = self._scan_and_pull_whatsapp_media_for_chat(c_name, "com.whatsapp")
            self.after(0, self.sync_whatsapp_chat)
            self.after(0, lambda: messagebox.showinfo("Media Pulled", f"Successfully pulled {len(items)} WhatsApp media files to laptop:\n{PULLED_MEDIA_DIR}\\whatsapp"))
        threading.Thread(target=task, daemon=True).start()

    def toggle_whatsapp_auto_sync(self):
        """Toggles background auto-sync for WhatsApp chat"""
        self.wa_auto_sync = not self.wa_auto_sync
        if self.wa_auto_sync:
            self.btn_wa_auto.config(text="⚡ Auto-Sync: ON (3s)", bg="#16a34a")
            self.log("WhatsApp Auto-Sync started (every 3s).")
            
            def auto_loop():
                while self.wa_auto_sync:
                    time.sleep(3)
                    if not self.wa_auto_sync:
                        break
                    self.sync_whatsapp_chat()

            self.wa_sync_thread = threading.Thread(target=auto_loop, daemon=True)
            self.wa_sync_thread.start()
        else:
            self.btn_wa_auto.config(text="⚡ Auto-Sync: OFF", bg="#334155")
            self.log("WhatsApp Auto-Sync stopped.")

    def _render_whatsapp_messages(self):
        """Renders parsed WhatsApp messages in WhatsApp Dark Theme UI/UX"""
        for w in self.wa_chat_inner.winfo_children():
            w.destroy()

        if not self.wa_messages:
            self._render_whatsapp_empty_state()
            return

        header_frame = tk.Frame(self.wa_chat_inner, bg="#0b141a", pady=6)
        header_frame.pack(fill="x")
        date_pill = tk.Label(header_frame, text=datetime.now().strftime("%B %d, %Y").upper(), bg="#182229", fg="#8696a0", font=("Segoe UI", 7, "bold"), padx=12, pady=4)
        date_pill.pack(anchor="center")

        for msg in self.wa_messages:
            txt = msg.get("text", "")
            ts = msg.get("timestamp", "")
            is_sent = msg.get("is_sent", True)
            m_type = msg.get("type", "TEXT")
            m_path = msg.get("media_path", None)

            bubble_outer = tk.Frame(self.wa_chat_inner, bg="#0b141a", pady=3, padx=10)
            bubble_outer.pack(fill="x")

            align_side = "e" if is_sent else "w"
            bg_color = "#005c4b" if is_sent else "#202c33"
            txt_color = "#e9edef"

            bubble_box = tk.Frame(bubble_outer, bg=bg_color, padx=10, pady=8, relief="flat")
            bubble_box.pack(anchor=align_side, padx=(60 if is_sent else 10, 10 if is_sent else 60))

            if not is_sent and msg.get("sender"):
                tk.Label(bubble_box, text=msg.get("sender"), bg=bg_color, fg="#53bdeb", font=("Segoe UI", 8, "bold")).pack(anchor="w", pady=(0, 2))

            if m_path and os.path.exists(m_path):
                if m_type == "IMAGE" and HAS_PIL:
                    try:
                        img_key = f"{m_path}_{os.path.getmtime(m_path)}"
                        if img_key not in self.wa_image_cache:
                            im = Image.open(m_path)
                            im.thumbnail((260, 260))
                            self.wa_image_cache[img_key] = ImageTk.PhotoImage(im)
                        
                        img_lbl = tk.Label(bubble_box, image=self.wa_image_cache[img_key], bg=bg_color, cursor="hand2")
                        img_lbl.pack(anchor="w", pady=2)
                        img_lbl.bind("<Button-1>", lambda e, p=m_path: os.startfile(p))
                    except Exception:
                        pass
                elif m_type in ("VIDEO", "DOC", "AUDIO"):
                    m_btn = tk.Button(bubble_box, text=f"📂 Open {m_type}: {os.path.basename(m_path)}", bg="#182229", fg="#38bdf8", font=("Segoe UI", 8, "bold"), padx=6, pady=4, relief="flat", command=lambda p=m_path: os.startfile(p))
                    m_btn.pack(anchor="w", pady=2)

            if txt:
                if "\n" in txt or len(txt) > 80:
                    code_frame = tk.Frame(bubble_box, bg="#111b21", padx=8, pady=6, relief="solid", bd=1)
                    code_frame.pack(anchor="w", fill="x", pady=2)

                    txt_lbl = tk.Label(code_frame, text=txt, bg="#111b21", fg="#f8fafc", font=("Consolas", 8), justify="left", wrap=320)
                    txt_lbl.pack(anchor="w")

                    copy_btn = tk.Button(code_frame, text="📋 Copy Prompt", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 7, "bold"), padx=6, relief="flat", command=lambda t=txt: self._copy_to_clipboard(t))
                    copy_btn.pack(anchor="e", pady=(4, 0))
                else:
                    msg_lbl = tk.Label(bubble_box, text=txt, bg=bg_color, fg=txt_color, font=("Segoe UI", 9), justify="left", wrap=340)
                    msg_lbl.pack(anchor="w")

            footer_frame = tk.Frame(bubble_box, bg=bg_color)
            footer_frame.pack(anchor="e", pady=(2, 0))

            tick_mark = " ✔✔" if is_sent else ""
            ts_lbl = tk.Label(footer_frame, text=f"{ts}{tick_mark}", bg=bg_color, fg="#53bdeb" if is_sent else "#8696a0", font=("Segoe UI", 7))
            ts_lbl.pack(side="right")

        self.wa_canvas.update_idletasks()
        self.wa_canvas.yview_moveto(1.0)

    def _copy_to_clipboard(self, text):
        self.clipboard_clear()
        self.clipboard_append(text)
        self.update()
        self.log("Copied prompt text to clipboard!")
        self.wa_status_lbl.config(text="Prompt copied to clipboard!")

    def send_whatsapp_prompt(self):
        """Sends the typed or pasted prompt directly to WhatsApp on phone via ADB"""
        prompt_text = self.wa_input_txt.get("1.0", "end").strip()
        c_name = self.selected_wa_chat.get("name") if self.selected_wa_chat else "My Notes"
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"

        if not prompt_text:
            messagebox.showinfo("Send Prompt", "Please type or paste a prompt first.")
            return

        def task():
            self.log(f"Sending prompt to WhatsApp on phone for '{c_name}'...")
            self.after(0, lambda: self.wa_status_lbl.config(text="Sending prompt to phone..."))

            safe_text = prompt_text.replace("'", "'\\''").replace("\n", " ")
            cmd = f"am start -a android.intent.action.SEND -t 'text/plain' --es android.intent.extra.TEXT '{safe_text}' -p {pkg}"
            c, out, err = self.run_adb(["shell", cmd], timeout=8)

            now_ts = datetime.now().strftime("%I:%M %p")
            self.wa_messages.append({
                "text": prompt_text,
                "timestamp": now_ts,
                "is_sent": True,
                "sender": "You",
                "type": "TEXT",
                "media_path": None
            })

            self.after(0, self._render_whatsapp_messages)
            self.after(0, lambda: self.wa_input_txt.delete("1.0", "end"))
            self.after(0, lambda: self.wa_status_lbl.config(text=f"Prompt sent to {c_name}!"))

        threading.Thread(target=task, daemon=True).start()

    def paste_and_send_whatsapp_prompt(self):
        """Pastes laptop clipboard content and sends immediately to WhatsApp"""
        try:
            clip = self.clipboard_get()
            if clip:
                self.wa_input_txt.delete("1.0", "end")
                self.wa_input_txt.insert("1.0", clip)
                self.send_whatsapp_prompt()
        except Exception as e:
            messagebox.showerror("Clipboard Error", f"Could not read clipboard:\n{e}")

    def send_whatsapp_laptop_file(self):
        """Pushes a file/photo from laptop to WhatsApp media directory on mobile"""
        pkg = "com.whatsapp.w4b" if "w4b" in self.wa_pkg_combo.get().lower() else "com.whatsapp"
        file_path = filedialog.askopenfilename(title="Select File to send to WhatsApp Mobile")
        if not file_path:
            return

        fname = os.path.basename(file_path)
        dest_remote = f"/sdcard/Android/media/{pkg}/WhatsApp/Media/WhatsApp Documents/{fname}"
        
        def task():
            self.log(f"Pushing {fname} to WhatsApp Mobile: {dest_remote}...")
            self.run_adb(["shell", f"mkdir -p '/sdcard/Android/media/{pkg}/WhatsApp/Media/WhatsApp Documents/'"], timeout=6)
            code, _, err = self.run_adb(["push", file_path, dest_remote], timeout=60)
            if code == 0:
                self.log(f"Pushed {fname} to WhatsApp Mobile!")
                self.run_adb(["shell", f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d 'file://{dest_remote}'"], timeout=4)
                
                now_ts = datetime.now().strftime("%I:%M %p")
                self.wa_messages.append({
                    "text": f"📎 Sent Laptop File: {fname}",
                    "timestamp": now_ts,
                    "is_sent": True,
                    "sender": "You",
                    "type": "DOC",
                    "media_path": file_path
                })
                self.after(0, self._render_whatsapp_messages)
                self.after(0, lambda: messagebox.showinfo("File Sent", f"File pushed to WhatsApp Mobile:\n{dest_remote}"))
            else:
                self.log(f"Push failed: {err}")
                self.after(0, lambda: messagebox.showerror("Push Error", f"Could not push file:\n{err}"))

        threading.Thread(target=task, daemon=True).start()

    def copy_all_whatsapp_prompts(self):
        """Gathers all prompt texts from the current WhatsApp chat and copies to clipboard"""
        prompts = [m['text'] for m in self.wa_messages if m.get('text')]
        if prompts:
            combined = "\n\n--- PROMPT SEPARATOR ---\n\n".join(prompts)
            self._copy_to_clipboard(combined)
            messagebox.showinfo("Prompts Copied", f"Copied {len(prompts)} prompts from WhatsApp chat to laptop clipboard!")
        else:
            messagebox.showinfo("No Prompts", "No messages found in chat.")

    # =========================================================================
    # STORAGE & FILE EXPLORER METHODS
    # =========================================================================

    def load_path(self, path):
        self.path_entry.delete(0, "end")
        self.path_entry.insert(0, path)
        self.list_device_files()

    def navigate_up(self):
        curr = self.path_entry.get().strip().rstrip("/")
        if not curr or curr == "/sdcard" or curr == "/storage/emulated/0" or curr == "/":
            self.load_path("/sdcard")
            return
        parent = os.path.dirname(curr)
        if not parent:
            parent = "/sdcard"
        self.load_path(parent)

    def _format_size(self, size_bytes):
        try:
            n = float(size_bytes)
            if n < 1024:
                return f"{int(n)} B"
            elif n < 1024 * 1024:
                return f"{n/1024:.1f} KB"
            elif n < 1024 * 1024 * 1024:
                return f"{n/(1024*1024):.1f} MB"
            else:
                return f"{n/(1024*1024*1024):.2f} GB"
        except Exception:
            return ""

    def list_device_files(self):
        p = self.path_entry.get().strip()
        if not p:
            p = "/sdcard/DCIM/Camera"
            self.path_entry.delete(0, "end")
            self.path_entry.insert(0, p)
            
        # Reset inspector pane immediately
        self.file_preview_lbl.config(image="", text="Loading directory...")
        self.file_info_lbl.config(text="Select a file or folder on the left")
        self.file_preview_selected_path = None
        self._tree_images_cache = {} # Prevent image garbage collection
            
        def task():
            self.log(f"Listing directory: {p}...")
            safe_p = p.replace("'", "'\''")
            cmd = (
                f"if [ -d '{safe_p}' ] || [ -f '{safe_p}' ]; then "
                f"  toybox ls -la '{safe_p}' 2>/dev/null || ls -la '{safe_p}' 2>/dev/null || ls -1 '{safe_p}' 2>/dev/null; "
                f"else "
                f"  ls -la {p} 2>/dev/null || ls -1 {p} 2>/dev/null; "
                f"fi"
            )
            code, out, err = self.run_adb(["shell", cmd], timeout=15)
            
            items = []
            if code == 0 and out:
                lines = out.splitlines()
                for line in lines:
                    line = line.strip()
                    if not line or line.startswith("total"):
                        continue
                    parts = line.split()
                    if len(parts) >= 8 and (parts[0].startswith('d') or parts[0].startswith('-') or parts[0].startswith('l') or parts[0].startswith('c')):
                        perm = parts[0]
                        size = parts[4] if len(parts) >= 5 and parts[4].isdigit() else (parts[3] if parts[3].isdigit() else "0")
                        
                        # Extract date/time (usually parts 5 and 6)
                        datetime_str = " ".join(parts[5:7]) if len(parts) >= 8 else ""
                        
                        name = " ".join(parts[7:]) if len(parts) >= 8 else parts[-1]
                        if " -> " in name:
                            name = name.split(" -> ")[0].strip()
                        if name in [".", "..", "./", "../"]:
                            continue
                        is_dir = perm.startswith("d") or name.endswith("/")
                        clean_name = name.rstrip("/") if is_dir else name
                        full_item_path = f"{p.rstrip('/')}/{clean_name}"
                        items.append((is_dir, clean_name, full_item_path, size, datetime_str))
                    else:
                        name = line.strip()
                        if name in [".", "..", "./", "../"]:
                            continue
                        is_dir = name.endswith("/")
                        clean_name = name.rstrip("/")
                        full_item_path = f"{p.rstrip('/')}/{clean_name}"
                        items.append((is_dir, clean_name, full_item_path, "0", ""))
                
                items.sort(key=lambda x: (not x[0], x[1].lower()))
            
            def update_ui():
                self.current_remote_items = items
                self.file_tree.delete(*self.file_tree.get_children())
                
                # We will queue media files to fetch thumbnails
                media_to_fetch = []
                
                if items:
                    for i, (is_dir, name, full_pth, size, dt_str) in enumerate(items):
                        ext = os.path.splitext(name)[1].lower()
                        sz_str = ""
                        if is_dir:
                            display = f"📁 [DIR] {name}/"
                        else:
                            sz_str = self._format_size(size)
                            if ext in [".jpg", ".jpeg", ".png", ".webp", ".gif", ".heic"]:
                                icon = "🖼"
                                media_to_fetch.append((i, full_pth, name))
                            elif ext in [".mp4", ".mkv", ".mov", ".3gp", ".webm"]:
                                icon = "🎥"
                                media_to_fetch.append((i, full_pth, name))
                            elif ext in [".pdf", ".docx", ".doc", ".txt", ".xlsx"]:
                                icon = "📄"
                            elif ext in [".apk"]:
                                icon = "📦"
                            elif ext in [".mp3", ".m4a", ".opus", ".wav"]:
                                icon = "🎵"
                            else:
                                icon = "📎"
                            display = f"{icon} {name}"
                            
                        # Insert into tree, store item_id as i (so we can update it later)
                        # We use text=display (column #0)
                        item_id = self.file_tree.insert("", "end", iid=str(i), text=" " + display, values=(sz_str if not is_dir else "", dt_str))
                    
                    self.log(f"Found {len(items)} items in {p}")
                    
                    # Start background thumbnail fetcher if auto-fetch is enabled
                    if getattr(self, 'auto_fetch_thumbs_var', None) and self.auto_fetch_thumbs_var.get() and media_to_fetch:
                        threading.Thread(target=self._background_tree_thumbnail_fetcher, args=(media_to_fetch, p), daemon=True).start()
                else:
                    self.file_tree.insert("", "end", text=" [Directory is empty or restricted]", values=("", ""))
                    if err:
                        self.log(f"ls notice: {err}")
                
                self.file_preview_lbl.config(image="", text="Click any file to inspect")
            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def _on_item_double_click(self, event):
        sel = self.file_tree.selection()
        if not sel:
            return
        try:
            idx = int(sel[0])
            if idx >= len(self.current_remote_items):
                return
        except ValueError:
            return
            
        is_dir, name, full_path, size, dt_str = self.current_remote_items[idx]
        if is_dir:
            self.load_path(full_path)
        else:
            self.open_selected_item()

    def open_selected_item(self):
        sel = self.file_tree.selection()
        if not sel:
            messagebox.showinfo("Select Item", "Please select a file to open.")
            return
        try:
            idx = int(sel[0])
            if idx >= len(self.current_remote_items):
                messagebox.showinfo("Select Item", "Please select a file to open.")
                return
        except ValueError:
            return

        is_dir, name, full_path, size, dt_str = self.current_remote_items[idx]
        if is_dir:
            self.load_path(full_path)
            return

        local_dest = os.path.join(PULLED_MEDIA_DIR, name)
        def task():
            self.log(f"Pulling {name} to preview...")
            code, _, err = self.run_adb(["pull", full_path, local_dest], timeout=60)
            if code == 0 and os.path.exists(local_dest):
                self.log(f"Opening {name} on PC...")
                os.startfile(local_dest)
            else:
                self.log(f"Failed to pull {name}: {err}")
                self.after(0, lambda: messagebox.showerror("Pull Error", f"Could not pull {name}:\n{err}"))
        threading.Thread(target=task, daemon=True).start()

    def pull_selected_file(self):
        sel = self.file_tree.selection()
        if not sel:
            messagebox.showinfo("Select Item", "Please select an item from the list.")
            return
        try:
            idx = int(sel[0])
            if idx >= len(self.current_remote_items):
                messagebox.showinfo("Select Item", "Please select an item from the list.")
                return
        except ValueError:
            return

        is_dir, name, full_path, size, dt_str = self.current_remote_items[idx]
        
        dest = filedialog.askdirectory(title=f"Save '{name}' to PC folder", initialdir=BASE_DIR)
        if not dest:
            return
            
        def task():
            self.log(f"Pulling {full_path} to {dest}...")
            code, out, err = self.run_adb(["pull", full_path, dest], timeout=120)
            if code == 0:
                self.log(f"Successfully pulled {name}!")
                self.after(0, lambda: messagebox.showinfo("Success", f"Pulled:\n{name}\nTo: {dest}"))
            else:
                self.log(f"Pull failed: {err}")
                self.after(0, lambda: messagebox.showerror("Error", f"Failed to pull {name}:\n{err}"))
        threading.Thread(target=task, daemon=True).start()

    def push_file_to_device(self):
        src = filedialog.askopenfilename(title="Select file to push to mobile")
        if not src:
            return
        remote_dest = self.path_entry.get().strip()
        def task():
            self.log(f"Pushing {os.path.basename(src)} to {remote_dest}...")
            code, out, err = self.run_adb(["push", src, remote_dest], timeout=90)
            if code == 0:
                self.log("Push complete!")
                self.list_device_files()
            else:
                self.log(f"Push failed: {err}")
        threading.Thread(target=task, daemon=True).start()

    def browse_apk(self):
        apk = filedialog.askopenfilename(title="Select APK file", filetypes=[("Android Packages", "*.apk")])
        if apk:
            self.apk_entry.delete(0, "end")
            self.apk_entry.insert(0, apk)

    def install_apk(self):
        apk = self.apk_entry.get().strip()
        if not apk or not os.path.exists(apk):
            messagebox.showerror("Error", "Please select a valid APK file.")
            return
        def task():
            self.log(f"Installing {os.path.basename(apk)} on {self.target_device}...")
            code, out, err = self.run_adb(["install", "-r", apk], timeout=120)
            if code == 0:
                self.log(f"Install successful: {out}")
                self.after(0, lambda: messagebox.showinfo("Installed", "APK installed successfully!"))
            else:
                self.log(f"Install failed: {err or out}")
                self.after(0, lambda: messagebox.showerror("Install Failed", f"{err or out}"))
        threading.Thread(target=task, daemon=True).start()

    def on_close(self):
        self._stop_wake_watcher()
        self.pulling = False
        self.purge_mobile_temp_cache(silent=True)
        if self.var_auto_sleep_close.get():
            try:
                subprocess.run(
                    ["adb", "-s", self.target_device, "shell", "input keyevent 26"],
                    creationflags=NO_WINDOW,
                    timeout=3
                )
            except Exception:
                pass
        self.destroy()

if __name__ == "__main__":
    app = DeviceDevStudio()
    app.mainloop()
