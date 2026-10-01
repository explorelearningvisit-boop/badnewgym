import os
import sys
import time
import re
import csv
from datetime import datetime
import base64
import subprocess
import threading
import socket
import struct
import io
import hashlib
import tkinter as tk
from tkinter import ttk, messagebox, filedialog

try:
    from PIL import Image, ImageTk
    HAS_PIL = True
except ImportError:
    HAS_PIL = False

NO_WINDOW = subprocess.CREATE_NO_WINDOW if os.name == 'nt' else 0

PRESET_DEVICES = [
    ("OPPO A53 [Tailscale]", "100.80.18.55:5555"),
    ("Xiaomi 11i [USB Fast]", "zxdada69gunb7ls4"),
    ("Xiaomi 11i [Tailscale]", "100.123.18.54:5555"),
]

DEFAULT_PKG = "com.example.badnewgym"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_DIR = os.path.dirname(BASE_DIR)

# Dedicated date-wise laptop storage folder
PULLED_SCREENS_DIR = os.path.join(PROJECT_DIR, "pulled_screens")
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
        self.geometry("1280x880")
        self.minsize(1100, 740)
        self.configure(bg="#0f172a")

        self.target_device = PRESET_DEVICES[0][1] # Default: OPPO A53 Tailscale
        self.connected = False
        self.pulling = False
        self.pull_thread = None
        self.screen_img = None
        self.gallery_preview_img = None
        self.last_raw_bytes = None
        self.current_scale_factor = 3
        self.pull_counter = 0

        self.last_saved_path = None
        self.last_saved_folder = None
        self.current_remote_items = []

        # Gallery media state
        self.all_gallery_items = []      # list of dicts: {path, name, folder, size, size_bytes, date, category, selected}
        self.filtered_gallery_items = []
        self.current_gallery_filter = "ALL"
        self.previewing_item = None
        self.gallery_view_mode = "GRID"  # "GRID" or "TABLE"
        self.gallery_thumb_size = 140    # 100 (Small), 140 (Medium), 185 (Large)
        self.card_image_cache = {}       # path_hash -> PhotoImage
        self.card_widgets = {}           # global_idx -> dict of card widgets
        self.gallery_page = 0
        self.gallery_page_size = 36
        self.gallery_total_pages = 1
        self.thumb_queue = []
        self.thumb_queue_lock = threading.Lock()
        self.thumb_worker_active = False

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
        top_bar = tk.Frame(self, bg="#1e293b", padx=16, pady=10, relief="solid", bd=1)
        top_bar.pack(fill="x", side="top")

        tk.Label(top_bar, text="📱 Android Dev Studio", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 12, "bold")).pack(side="left", padx=(0, 16))

        tk.Label(top_bar, text="Device:", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 9, "bold")).pack(side="left", padx=4)
        
        self.device_combo = ttk.Combobox(
            top_bar, 
            values=[f"{label} - {addr}" for label, addr in PRESET_DEVICES],
            width=36,
            state="readonly"
        )
        self.device_combo.current(0)
        self.device_combo.pack(side="left", padx=4)
        self.device_combo.bind("<<ComboboxSelected>>", self._on_device_combo_select)

        self.btn_refresh_devs = tk.Button(top_bar, text="🔄 Scan", bg="#334155", fg="white", activebackground="#475569", activeforeground="white", font=("Segoe UI", 8), padx=6, relief="flat", command=self.refresh_devices_list)
        self.btn_refresh_devs.pack(side="left", padx=4)

        self.btn_connect = tk.Button(top_bar, text="Connect ADB", bg="#0284c7", fg="white", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=12, relief="flat", command=self.toggle_connection)
        self.btn_connect.pack(side="left", padx=10)

        self.status_lbl = tk.Label(top_bar, text="● Disconnected", bg="#1e293b", fg="#ef4444", font=("Segoe UI", 10, "bold"))
        self.status_lbl.pack(side="left", padx=10)

        tk.Button(top_bar, text="💤 Sleep Mobile", bg="#475569", fg="#f8fafc", activebackground="#334155", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.put_device_to_sleep).pack(side="right", padx=6)
        tk.Button(top_bar, text="⚡ Wake Mobile", bg="#0284c7", fg="#f8fafc", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.wake_device).pack(side="right", padx=6)

        main_paned = tk.PanedWindow(self, orient="horizontal", bg="#0f172a", sashwidth=4, bd=0)
        main_paned.pack(fill="both", expand=True, padx=12, pady=12)

        left_col = tk.Frame(main_paned, bg="#0f172a")
        main_paned.add(left_col, minsize=540)

        tabs = ttk.Notebook(left_col)
        tabs.pack(fill="both", expand=True)

        tab_gallery = ttk.Frame(tabs, padding=8)
        tab_calls = ttk.Frame(tabs, padding=8)
        tab_apps = ttk.Frame(tabs, padding=8)
        tab_files = ttk.Frame(tabs, padding=8)
        tab_power = ttk.Frame(tabs, padding=8)
        tab_log = ttk.Frame(tabs, padding=8)

        tabs.add(tab_gallery, text=" 🖼️ Gallery & Recovery ")
        tabs.add(tab_calls, text=" 📞 Calls & Contacts ")
        tabs.add(tab_apps, text=" ⚡ Apps & Hidden ")
        tabs.add(tab_files, text=" 📁 File Explorer ")
        tabs.add(tab_power, text=" 🔋 Power & Battery ")
        tabs.add(tab_log, text=" 📋 ADB Log ")

        self._build_gallery_tab(tab_gallery)
        self._build_calls_tab(tab_calls)
        self._build_apps_tab(tab_apps)
        self._build_files_tab(tab_files)
        self._build_power_tab(tab_power)
        self._build_log_tab(tab_log)

        right_col = tk.Frame(main_paned, bg="#1e293b", padx=14, pady=14, relief="solid", bd=1)
        main_paned.add(right_col, minsize=460)
        self._build_screen_panel(right_col)

    def _on_device_combo_select(self, event=None):
        sel = self.device_combo.get()
        if " - " in sel:
            addr = sel.split(" - ")[-1].strip()
            if addr != self.target_device or not self.connected:
                self.target_device = addr
                self.log(f"Switched target device to: {sel}")
                self._clear_device_data()
                self.toggle_connection(force_connect=True)

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
                if hasattr(self, 'file_listbox'):
                    self.file_listbox.delete(0, "end")

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
        """Clean mobile name for organizing folder structure without illegal path chars"""
        for label, addr in PRESET_DEVICES:
            if addr == self.target_device:
                clean = label.split("[")[0].strip().replace(" ", "_")
                return clean
        curr = self.device_combo.get()
        if " - " in curr:
            lbl = curr.split(" - ")[0].strip()
            clean = lbl.split("(")[0].strip().replace(" ", "_")
            if clean:
                return clean
        code, out, _ = self.run_adb(["shell", "getprop ro.product.model"], timeout=3)
        if code == 0 and out.strip():
            clean = out.strip().replace(" ", "_")
            return "".join(c for c in clean if c.isalnum() or c in ('_', '-'))
        return self.target_device.replace(":", "_").replace(".", "_")

    def _build_apps_tab(self, parent):
        top_bar = tk.Frame(parent, bg="#0f172a")
        top_bar.pack(fill="x", pady=(0, 4))
        tk.Label(top_bar, text="⚡ Installed & Hidden Applications", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")
        self.apps_summary_lbl = tk.Label(top_bar, text="Click 'Scan Apps' to list", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.apps_summary_lbl.pack(side="right")

        filter_bar = tk.Frame(parent, bg="#0f172a")
        filter_bar.pack(fill="x", pady=2)
        
        self.btn_app_user = tk.Button(filter_bar, text="👤 User Apps (3rd-Party)", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_apps_filter("USER"))
        self.btn_app_user.pack(side="left", padx=2)

        self.btn_app_disabled = tk.Button(filter_bar, text="🚫 Disabled / Hidden", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_apps_filter("DISABLED"))
        self.btn_app_disabled.pack(side="left", padx=2)

        self.btn_app_system = tk.Button(filter_bar, text="⚙️ System Apps", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_apps_filter("SYSTEM"))
        self.btn_app_system.pack(side="left", padx=2)

        self.btn_app_all = tk.Button(filter_bar, text="📦 All Packages", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_apps_filter("ALL"))
        self.btn_app_all.pack(side="left", padx=2)

        tk.Button(filter_bar, text="🔄 Scan Apps", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.scan_installed_apps).pack(side="right", padx=2)

        search_bar = tk.Frame(parent, bg="#1e293b", padx=6, pady=4, relief="solid", bd=1)
        search_bar.pack(fill="x", pady=4)
        tk.Label(search_bar, text="🔍 Search:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")
        self.apps_search_entry = tk.Entry(search_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=24)
        self.apps_search_entry.pack(side="left", padx=4)
        self.apps_search_entry.bind("<KeyRelease>", lambda e: self._apply_apps_filter_and_render())

        self.selected_app_lbl = tk.Label(search_bar, text="Select an app below", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"))
        self.selected_app_lbl.pack(side="right", padx=4)

        # Action Toolbar (packed at bottom of container before table for guaranteed visibility)
        app_act_bar = tk.Frame(parent, bg="#0f172a")
        app_act_bar.pack(side="bottom", fill="x", pady=(4, 0))

        tk.Button(app_act_bar, text="▶ Launch App", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=4, relief="flat", command=self.launch_selected_app).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="⏹ Force Stop", bg="#dc2626", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=4, relief="flat", command=self.stop_selected_app).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="🚫 Freeze / Hide", bg="#7f1d1d", fg="white", font=("Segoe UI", 8, "bold"), padx=6, pady=4, relief="flat", command=self.freeze_selected_app).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="🟢 Unfreeze / Enable", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.unfreeze_selected_app).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="📥 Pull APK to PC", bg="#0891b2", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.pull_selected_app_apk).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="🧹 Clear Data", bg="#475569", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=self.clear_selected_app_data).pack(side="left", padx=2)
        tk.Button(app_act_bar, text="🗑️ Uninstall", bg="#b91c1c", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.uninstall_selected_app).pack(side="right", padx=2)

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
        """Dedicated Gallery & Screenshot Manager with Interactive Thumbnail Grid & Table Views"""
        top_row = tk.Frame(parent, bg="#0f172a")
        top_row.pack(fill="x", pady=(0, 4))
        tk.Label(top_row, text="Mobile Gallery, Hidden & Trashed Recovery", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")
        
        self.gallery_summary_lbl = tk.Label(top_row, text="Scanning...", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8))
        self.gallery_summary_lbl.pack(side="right")

        # Category filters & View switcher bar
        filter_bar = tk.Frame(parent, bg="#0f172a")
        filter_bar.pack(fill="x", pady=2)
        
        self.btn_f_all = tk.Button(filter_bar, text="🖼️ All Images", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_gallery_filter("ALL"))
        self.btn_f_all.pack(side="left", padx=2)

        self.btn_f_screens = tk.Button(filter_bar, text="📱 Screenshots", bg="#334155", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_gallery_filter("SCREENSHOTS"))
        self.btn_f_screens.pack(side="left", padx=2)

        self.btn_f_camera = tk.Button(filter_bar, text="📸 Camera", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_gallery_filter("CAMERA"))
        self.btn_f_camera.pack(side="left", padx=2)

        self.btn_f_wa = tk.Button(filter_bar, text="💬 WhatsApp", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_gallery_filter("WHATSAPP"))
        self.btn_f_wa.pack(side="left", padx=2)

        self.btn_f_dl = tk.Button(filter_bar, text="📥 Downloads & Purchases", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_gallery_filter("DOWNLOADS"))
        self.btn_f_dl.pack(side="left", padx=2)

        self.btn_f_root = tk.Button(filter_bar, text="📁 Root Storage", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_gallery_filter("ROOT"))
        self.btn_f_root.pack(side="left", padx=2)

        self.btn_f_trash = tk.Button(filter_bar, text="🗑️ Trashed & Hidden", bg="#7f1d1d", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_gallery_filter("TRASHED"))
        self.btn_f_trash.pack(side="left", padx=2)

        # View Mode Switcher
        v_sep = tk.Label(filter_bar, text="|", bg="#0f172a", fg="#475569", font=("Segoe UI", 9))
        v_sep.pack(side="left", padx=4)

        self.btn_v_grid = tk.Button(filter_bar, text="🖼️ Grid View", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=lambda: self._set_gallery_view_mode("GRID"))
        self.btn_v_grid.pack(side="left", padx=2)

        self.btn_v_table = tk.Button(filter_bar, text="📋 Table View", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._set_gallery_view_mode("TABLE"))
        self.btn_v_table.pack(side="left", padx=2)

        # Tile size selector
        tk.Label(filter_bar, text="Tile:", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left", padx=(6, 2))
        self.btn_sz_s = tk.Button(filter_bar, text="S", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._set_thumb_size(100))
        self.btn_sz_s.pack(side="left", padx=1)
        self.btn_sz_m = tk.Button(filter_bar, text="M", bg="#0284c7", fg="white", font=("Segoe UI", 7, "bold"), padx=4, relief="flat", command=lambda: self._set_thumb_size(140))
        self.btn_sz_m.pack(side="left", padx=1)
        self.btn_sz_l = tk.Button(filter_bar, text="L", bg="#334155", fg="white", font=("Segoe UI", 7), padx=4, relief="flat", command=lambda: self._set_thumb_size(185))
        self.btn_sz_l.pack(side="left", padx=1)

        tk.Button(filter_bar, text="🔄 Scan Mobile", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.scan_mobile_gallery).pack(side="right", padx=2)

        # Search & Selection Bar WITH DIRECT VISIBLE DELETE & BACKUP BUTTONS
        sel_bar = tk.Frame(parent, bg="#1e293b", padx=6, pady=4, relief="solid", bd=1)
        sel_bar.pack(fill="x", pady=4)

        tk.Label(sel_bar, text="🔍 Filter:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")
        self.gallery_search_entry = tk.Entry(sel_bar, bg="#334155", fg="white", insertbackground="white", font=("Segoe UI", 8), width=14)
        self.gallery_search_entry.pack(side="left", padx=4)
        self.gallery_search_entry.bind("<KeyRelease>", lambda e: self._apply_gallery_search())

        tk.Button(sel_bar, text="☑ All", bg="#334155", fg="white", font=("Segoe UI", 8), padx=4, relief="flat", command=self._select_all_gallery).pack(side="left", padx=1)
        tk.Button(sel_bar, text="☐ None", bg="#334155", fg="white", font=("Segoe UI", 8), padx=4, relief="flat", command=self._deselect_all_gallery).pack(side="left", padx=1)
        tk.Button(sel_bar, text="📱 Screen", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=4, relief="flat", command=self._select_screenshots_only).pack(side="left", padx=1)
        tk.Button(sel_bar, text="🗑️ Trash", bg="#7f1d1d", fg="white", font=("Segoe UI", 8, "bold"), padx=4, relief="flat", command=self._select_trashed_only).pack(side="left", padx=1)

        # Top Quick Delete & Backup buttons
        self.top_btn_del = tk.Button(sel_bar, text="🗑️ Delete (0)", bg="#dc2626", fg="white", activebackground="#b91c1c", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.delete_selected_gallery_images)
        self.top_btn_del.pack(side="left", padx=4)

        self.top_btn_bak = tk.Button(sel_bar, text="📥 Backup (0)", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.backup_selected_gallery_images)
        self.top_btn_bak.pack(side="left", padx=2)

        self.gallery_sel_count_lbl = tk.Label(sel_bar, text="Selected: 0 files", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"))
        self.gallery_sel_count_lbl.pack(side="right", padx=4)

        # BOTTOM ACTION BAR PACKED FIRST (GUARANTEES 100% VISIBILITY)
        act_bar = tk.Frame(parent, bg="#0f172a")
        act_bar.pack(side="bottom", fill="x", pady=(4, 0))

        tk.Button(act_bar, text="💥 Delete ALL Screenshots", bg="#dc2626", fg="white", activebackground="#b91c1c", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=8, pady=4, relief="flat", command=self.delete_all_screenshots_from_mobile).pack(side="left", padx=2)
        tk.Button(act_bar, text="🗑️ Delete Selected (+Trash)", bg="#b45309", fg="white", activebackground="#92400e", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=8, pady=4, relief="flat", command=self.delete_selected_gallery_images).pack(side="left", padx=2)
        tk.Button(act_bar, text="🗑️ Empty Mobile Trash / Bin", bg="#7f1d1d", fg="white", activebackground="#991b1b", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=8, pady=4, relief="flat", command=self.empty_mobile_trash_bin).pack(side="left", padx=2)
        tk.Button(act_bar, text="📥 Backup Selected to PC...", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=8, pady=4, relief="flat", command=self.backup_selected_gallery_images).pack(side="left", padx=2)
        tk.Button(act_bar, text="🧹 Purge Temp Cache", bg="#475569", fg="white", font=("Segoe UI", 8), padx=6, pady=4, relief="flat", command=self.purge_mobile_temp_cache).pack(side="right", padx=2)

        # Main Split Content: Content Container (Grid or Table) on Left, Preview Panel on Right
        content_split = tk.PanedWindow(parent, orient="horizontal", bg="#0f172a", sashwidth=3, bd=0)
        content_split.pack(fill="both", expand=True, pady=4)

        self.gallery_content_container = tk.Frame(content_split, bg="#0f172a")
        content_split.add(self.gallery_content_container, minsize=340)

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
        self.grid_page_bar = tk.Frame(self.gallery_grid_frame, bg="#1e293b", padx=6, pady=4, relief="solid", bd=1)
        self.grid_page_bar.pack(side="bottom", fill="x")

        self.btn_page_first = tk.Button(self.grid_page_bar, text="⏮ First", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._go_gallery_page(0))
        self.btn_page_first.pack(side="left", padx=2)

        self.btn_page_prev = tk.Button(self.grid_page_bar, text="◀ Prev", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._go_gallery_page(self.gallery_page - 1))
        self.btn_page_prev.pack(side="left", padx=2)

        self.page_info_lbl = tk.Label(self.grid_page_bar, text="Page 1 of 1 (0 items)", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 8, "bold"), padx=6)
        self.page_info_lbl.pack(side="left", padx=4)

        self.btn_page_next = tk.Button(self.grid_page_bar, text="Next ▶", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._go_gallery_page(self.gallery_page + 1))
        self.btn_page_next.pack(side="left", padx=2)

        self.btn_page_last = tk.Button(self.grid_page_bar, text="Last ⏭", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=lambda: self._go_gallery_page(self.gallery_total_pages - 1))
        self.btn_page_last.pack(side="left", padx=2)

        tk.Label(self.grid_page_bar, text="Per Page:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="right", padx=(6, 2))
        self.page_size_combo = ttk.Combobox(self.grid_page_bar, values=["24", "36", "60", "120"], width=4, state="readonly")
        self.page_size_combo.set("36")
        self.page_size_combo.pack(side="right", padx=2)
        self.page_size_combo.bind("<<ComboboxSelected>>", self._on_page_size_change)
        
        self.grid_canvas = tk.Canvas(self.gallery_grid_frame, bg="#0f172a", highlightthickness=0, bd=0)
        self.grid_scroll = tk.Scrollbar(self.gallery_grid_frame, orient="vertical", command=self.grid_canvas.yview)
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
        preview_panel = tk.Frame(content_split, bg="#1e293b", padx=8, pady=8, relief="solid", bd=1)
        content_split.add(preview_panel, minsize=190)

        tk.Label(preview_panel, text="Selected Photo Preview", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 9, "bold")).pack(anchor="w")

        self.preview_canvas_frame = tk.Frame(preview_panel, bg="#000000", height=200, relief="sunken", bd=1)
        self.preview_canvas_frame.pack(fill="x", pady=6)
        self.preview_canvas_frame.pack_propagate(False)

        self.gallery_preview_lbl = tk.Label(self.preview_canvas_frame, text="Click any photo card\nor list row to preview", bg="#000000", fg="#64748b", font=("Segoe UI", 8), justify="center")
        self.gallery_preview_lbl.pack(expand=True)

        self.preview_info_lbl = tk.Label(preview_panel, text="No image selected", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8), wraplength=180, justify="left")
        self.preview_info_lbl.pack(anchor="w", pady=2)

        tk.Button(preview_panel, text="👁 Open Full Viewer", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=4, pady=3, relief="flat", command=self.open_previewed_image_on_pc).pack(fill="x", pady=2)
        tk.Button(preview_panel, text="📥 Pull Image to PC", bg="#334155", fg="white", font=("Segoe UI", 8), padx=4, pady=3, relief="flat", command=self.pull_previewed_image).pack(fill="x", pady=2)
        tk.Button(preview_panel, text="🗑️ Delete Photo (+Trash)", bg="#dc2626", fg="white", activebackground="#b91c1c", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=4, pady=3, relief="flat", command=self.delete_previewed_image).pack(fill="x", pady=2)
        self.btn_preview_restore = tk.Button(preview_panel, text="♻️ Restore to Gallery", bg="#16a34a", fg="white", activebackground="#15803d", activeforeground="white", font=("Segoe UI", 8, "bold"), padx=4, pady=3, relief="flat", command=self.restore_previewed_image)
        self.btn_preview_restore.pack(fill="x", pady=2)

    def _build_files_tab(self, parent):
        top_lbl_frame = tk.Frame(parent, bg="#0f172a")
        top_lbl_frame.pack(fill="x", pady=(0, 4))
        tk.Label(top_lbl_frame, text="Device Storage File Explorer", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")
        tk.Label(top_lbl_frame, text="(One-click shortcuts to mobile directories)", bg="#0f172a", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="right")

        sc_row1 = tk.Frame(parent, bg="#0f172a")
        sc_row1.pack(fill="x", pady=1)
        shortcuts_1 = [
            ("📸 Camera", "/sdcard/DCIM/Camera"),
            ("📱 Screenshots", "/sdcard/DCIM/Screenshots"),
            ("🖼 Pictures", "/sdcard/Pictures"),
            ("📥 Downloads", "/sdcard/Download"),
            ("📄 Documents", "/sdcard/Documents"),
        ]
        for name, pth in shortcuts_1:
            tk.Button(sc_row1, text=name, bg="#334155", fg="white", activebackground="#0284c7", activeforeground="white", font=("Segoe UI", 8), command=lambda p=pth: self.load_path(p)).pack(side="left", padx=2)

        sc_row2 = tk.Frame(parent, bg="#0f172a")
        sc_row2.pack(fill="x", pady=1)
        shortcuts_2 = [
            ("💬 WA Images", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images"),
            ("💬 WA Docs", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents"),
            ("💬 WA Video", "/sdcard/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video"),
            ("🎥 Movies", "/sdcard/Movies"),
            ("🎵 Music", "/sdcard/Music"),
        ]
        for name, pth in shortcuts_2:
            tk.Button(sc_row2, text=name, bg="#1e293b", fg="#cbd5e1", activebackground="#0284c7", activeforeground="white", font=("Segoe UI", 8), command=lambda p=pth: self.load_path(p)).pack(side="left", padx=2)

        sc_row3 = tk.Frame(parent, bg="#0f172a")
        sc_row3.pack(fill="x", pady=1)
        shortcuts_3 = [
            ("📁 Root /sdcard", "/sdcard"),
            ("💾 /storage/emulated/0", "/storage/emulated/0"),
            ("🟢 ColorOS / OPPO", "/sdcard/ColorOS"),
            ("🗂 DCIM Root", "/sdcard/DCIM"),
            ("📦 Android Media", "/sdcard/Android/media"),
            ("✈️ Telegram", "/sdcard/Telegram"),
        ]
        for name, pth in shortcuts_3:
            tk.Button(sc_row3, text=name, bg="#0f172a", fg="#94a3b8", activebackground="#0284c7", activeforeground="white", font=("Segoe UI", 7, "bold"), bd=1, relief="solid", command=lambda p=pth: self.load_path(p)).pack(side="left", padx=2)

        nav_frame = tk.Frame(parent, bg="#0f172a")
        nav_frame.pack(fill="x", pady=6)
        tk.Button(nav_frame, text="⬆ Up", bg="#475569", fg="white", font=("Segoe UI", 8, "bold"), padx=6, command=self.navigate_up).pack(side="left", padx=(0, 4))
        self.path_entry = tk.Entry(nav_frame, bg="#1e293b", fg="#38bdf8", insertbackground="white", font=("Consolas", 9))
        self.path_entry.insert(0, "/sdcard/DCIM/Camera")
        self.path_entry.pack(side="left", fill="x", expand=True, padx=4)
        tk.Button(nav_frame, text="Go", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, command=self.list_device_files).pack(side="left", padx=2)
        tk.Button(nav_frame, text="🔄 Refresh", bg="#334155", fg="white", font=("Segoe UI", 8), command=self.list_device_files).pack(side="left", padx=2)

        list_frame = tk.Frame(parent, bg="#1e293b")
        list_frame.pack(fill="both", expand=True, pady=4)

        self.file_listbox = tk.Listbox(
            list_frame, 
            bg="#1e293b", 
            fg="#f8fafc", 
            selectbackground="#0284c7", 
            selectforeground="white",
            font=("Consolas", 9),
            bd=0,
            highlightthickness=0
        )
        scroll_y = tk.Scrollbar(list_frame, orient="vertical", command=self.file_listbox.yview)
        self.file_listbox.configure(yscrollcommand=scroll_y.set)

        self.file_listbox.pack(side="left", fill="both", expand=True)
        scroll_y.pack(side="right", fill="y")
        self.file_listbox.bind("<Double-Button-1>", self._on_item_double_click)

        file_act = tk.Frame(parent, bg="#0f172a")
        file_act.pack(fill="x", pady=(6, 0))
        tk.Button(file_act, text="👁 Open/Preview Selected", bg="#0284c7", fg="white", font=("Segoe UI", 9, "bold"), padx=8, pady=4, relief="flat", command=self.open_selected_item).pack(side="left", padx=2)
        tk.Button(file_act, text="📥 Pull Selected to PC...", bg="#334155", fg="white", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.pull_selected_file).pack(side="left", padx=2)
        tk.Button(file_act, text="📤 Push File to Current Folder...", bg="#334155", fg="white", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.push_file_to_device).pack(side="left", padx=2)

    def _build_power_tab(self, parent):
        tk.Label(parent, text="Battery Health & Power Save Controls", bg="#0f172a", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(anchor="w", pady=(0, 8))

        p_card = tk.Frame(parent, bg="#1e293b", padx=14, pady=12, relief="solid", bd=1)
        p_card.pack(fill="x", pady=4)

        self.batt_level_lbl = tk.Label(p_card, text="Battery Level: Checking...", bg="#1e293b", fg="#f8fafc", font=("Segoe UI", 10, "bold"))
        self.batt_level_lbl.pack(anchor="w", pady=2)

        self.batt_status_lbl = tk.Label(p_card, text="Charging State: Unknown", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 9))
        self.batt_status_lbl.pack(anchor="w", pady=2)

        tk.Button(p_card, text="🔄 Check Battery Info", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=8, pady=3, relief="flat", command=self.check_battery).pack(anchor="w", pady=6)

        p_acts = tk.Frame(parent, bg="#0f172a")
        p_acts.pack(fill="x", pady=10)

        tk.Button(p_acts, text="💤 Put Mobile To Sleep", bg="#334155", fg="#f8fafc", activebackground="#475569", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=10, pady=6, relief="flat", command=self.put_device_to_sleep).grid(row=0, column=0, padx=4, pady=4, sticky="ew")
        tk.Button(p_acts, text="⚡ Wake Mobile Screen", bg="#0284c7", fg="#f8fafc", activebackground="#0369a1", activeforeground="white", font=("Segoe UI", 9, "bold"), padx=10, pady=6, relief="flat", command=self.wake_device).grid(row=0, column=1, padx=4, pady=4, sticky="ew")

        tk.Button(p_acts, text="🔆 Dim Screen to 1", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.dim_screen).grid(row=1, column=0, padx=4, pady=4, sticky="ew")
        tk.Button(p_acts, text="☀️ Restore Brightness (150)", bg="#1e293b", fg="#cbd5e1", font=("Segoe UI", 8), padx=8, pady=4, relief="flat", command=self.restore_screen).grid(row=1, column=1, padx=4, pady=4, sticky="ew")

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

    def _build_screen_panel(self, parent):
        top_ctrl = tk.Frame(parent, bg="#1e293b")
        top_ctrl.pack(fill="x", pady=(0, 8))

        tk.Label(top_ctrl, text="Live Screen", bg="#1e293b", fg="#38bdf8", font=("Segoe UI", 11, "bold")).pack(side="left")

        tk.Label(top_ctrl, text="Interval:", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left", padx=(10, 2))
        self.interval_spin = tk.Spinbox(top_ctrl, from_=1, to=60, width=3, bg="#334155", fg="#ffffff", font=("Segoe UI", 9))
        self.interval_spin.delete(0, "end")
        self.interval_spin.insert(0, "1")
        self.interval_spin.pack(side="left", padx=2)
        tk.Label(top_ctrl, text="s", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8)).pack(side="left")

        self.btn_pull_live = tk.Button(top_ctrl, text="▶ Start 1s Stream", bg="#16a34a", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.toggle_live_pull)
        self.btn_pull_live.pack(side="left", padx=6)

        tk.Button(top_ctrl, text="📸 Snapshot", bg="#475569", fg="white", font=("Segoe UI", 8, "bold"), padx=8, relief="flat", command=self.pull_single_screen).pack(side="left", padx=2)

        # Right buttons for opening laptop saved folder and photo viewer
        tk.Button(top_ctrl, text="📁 Saved Folder", bg="#0284c7", fg="white", font=("Segoe UI", 8, "bold"), padx=6, relief="flat", command=self.open_saved_folder).pack(side="right", padx=2)
        tk.Button(top_ctrl, text="🖼 Open Photo", bg="#334155", fg="white", font=("Segoe UI", 8), padx=6, relief="flat", command=self.open_image_viewer).pack(side="right", padx=2)

        self.screen_frame = tk.Frame(parent, bg="#000000", bd=2, relief="groove")
        self.screen_frame.pack(fill="both", expand=True)

        self.screen_canvas = tk.Canvas(self.screen_frame, bg="#000000", highlightthickness=0)
        self.screen_scroll = tk.Scrollbar(self.screen_frame, orient="vertical", command=self.screen_canvas.yview)
        self.screen_canvas.configure(yscrollcommand=self.screen_scroll.set)

        self.screen_scroll.pack(side="right", fill="y")
        self.screen_canvas.pack(side="left", fill="both", expand=True)

        self.screen_inner_frame = tk.Frame(self.screen_canvas, bg="#000000")
        self.screen_canvas_win = self.screen_canvas.create_window((0, 0), window=self.screen_inner_frame, anchor="nw")

        self.screen_lbl = tk.Label(self.screen_inner_frame, text="No Screen Captured\nClick 'Snapshot' or 'Start 1s Stream'", bg="#000000", fg="#64748b", font=("Segoe UI", 10), justify="center")
        self.screen_lbl.pack(pady=40, padx=20)

        self.screen_inner_frame.bind("<Configure>", lambda e: self.screen_canvas.configure(scrollregion=self.screen_canvas.bbox("all")))
        self.screen_canvas.bind("<Configure>", self._center_canvas)

        self.screen_status_lbl = tk.Label(parent, text="Screen status: Ready | Saved to laptop date-wise | Phone gallery excluded", bg="#1e293b", fg="#94a3b8", font=("Segoe UI", 8))
        self.screen_status_lbl.pack(fill="x", pady=(4, 0))

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
        # If network device, ensure it is connected
        if ":" in addr:
            try:
                subprocess.run(["adb", "connect", addr], capture_output=True, timeout=4, creationflags=NO_WINDOW)
            except Exception:
                pass

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
            return res.returncode, out, err
        except Exception as e:
            return -1, "", str(e)

    def refresh_devices_list(self):
        def task():
            self.log("Scanning ADB devices...")
            try:
                res = subprocess.run(["adb", "devices"], capture_output=True, timeout=8, creationflags=NO_WINDOW)
                lines = res.stdout.decode('utf-8', errors='replace').strip().splitlines()
            except Exception:
                lines = []
            detected = []
            for line in lines[1:]:
                parts = line.strip().split()
                if len(parts) >= 2 and parts[1] == "device":
                    dev_id = parts[0]
                    try:
                        m_res = subprocess.run(["adb", "-s", dev_id, "shell", "getprop ro.product.model"], capture_output=True, timeout=5, creationflags=NO_WINDOW)
                        model = m_res.stdout.decode('utf-8', errors='replace').strip() or dev_id
                    except Exception:
                        model = dev_id
                    detected.append((f"{model} ({dev_id})", dev_id))
            
            items = []
            seen = set()
            for label, addr in PRESET_DEVICES:
                seen.add(addr)
                items.append(f"{label} - {addr}")
            for label, addr in detected:
                if addr not in seen:
                    items.append(f"{label} - {addr}")
            
            def update_ui():
                self.device_combo['values'] = items
                self.log(f"Found {len(detected)} active ADB device(s).")
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
                self.log(f"Found {len(pkgs)} package(s): {', '.join(pkgs)}")
            else:
                self.log("No specific gym packages found in filter.")
        threading.Thread(target=task, daemon=True).start()

    def auto_detect_and_connect(self):
        def task():
            # Strict default: OPPO A53 [Tailscale] (PRESET_DEVICES[0])
            self.target_device = PRESET_DEVICES[0][1]
            self.after(0, lambda: self.device_combo.current(0))
            if ":" in self.target_device:
                try:
                    subprocess.run(["adb", "connect", self.target_device], capture_output=True, timeout=6, creationflags=NO_WINDOW)
                except Exception:
                    pass
            self.toggle_connection(force_connect=True)
        threading.Thread(target=task, daemon=True).start()

    def toggle_connection(self, force_connect=False):
        addr = self.target_device
        if not force_connect and self.connected:
            def dc_task():
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
                    res = subprocess.run(["adb", "connect", addr], capture_output=True, timeout=8, creationflags=NO_WINDOW)
                    out = res.stdout.decode('utf-8', errors='replace').strip().lower()
                except Exception as e:
                    out = f"connect timeout: {e}"

            # Verify actual device connection
            c_test, m_test, _ = self.run_adb(["shell", "getprop ro.product.model"], timeout=5)
            if c_test == 0 and m_test:
                self.connected = True
                dev_model = m_test.strip()
                self.after(0, lambda: self.status_lbl.config(text=f"● Connected ({dev_model} @ {addr})", fg="#22c55e"))
                self.after(0, lambda: self.btn_connect.config(text="Disconnect", bg="#b91c1c"))
                self.log(f"Successfully connected to {dev_model} ({addr})")
                
                # Cleanly staggered initial loading so Tailscale connection is not overwhelmed
                self.check_foreground()
                self.check_battery()
                self.after(400, self.pull_single_screen)
                self.after(1000, self.fetch_call_logs_and_contacts)
                self.after(2200, self.scan_installed_apps)
                self.after(3500, self.scan_mobile_gallery)
                self.after(4800, self.list_device_files)
            else:
                self.connected = False
                self.after(0, lambda: self.status_lbl.config(text="● Disconnected", fg="#ef4444"))
                self.after(0, lambda: self.btn_connect.config(text="Connect ADB", bg="#0284c7"))
                self.log(f"Connection notice for {addr}: {out or 'Device offline'}")

        threading.Thread(target=task, daemon=True).start()

    def check_foreground(self):
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
                self.after(0, lambda a=app_info: self.active_win_lbl.config(text=a))
                self.log(f"Current foreground: {app_info}")
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
        port = self._get_device_port()
        self._ensure_fastcap_installed()
        
        target_w = 380
        quality = 35

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
            if c_pull == 0 and os.path.exists(local_tmp_jpg):
                with open(local_tmp_jpg, "rb") as f:
                    data = f.read()
                if len(data) > 500:
                    saved_path = self.save_pulled_frame(data)
                    return data, None, saved_path
        except Exception as e:
            return None, str(e), None

        return None, "All screen capture channels failed", None

        return None, "All screen capture channels failed", None

    def pull_single_screen(self):
        def task():
            self.after(0, lambda: self.screen_status_lbl.config(text="Capturing low-KB screen...", fg="#38bdf8"))
            raw, err, saved_path = self._capture_screen_bytes()
            if raw:
                kb = len(raw) / 1024.0
                self.pull_counter += 1
                cnt = self.pull_counter
                self.after(0, lambda r=raw, c=cnt, k=kb, sp=saved_path: self._update_screen_ui(r, c, k, sp))
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
            self.btn_pull_live.config(text="⏹ Stop Live Stream", bg="#dc2626")
            try:
                interval = max(1, int(self.interval_spin.get()))
            except ValueError:
                interval = 1

            def loop():
                dev_name = self.get_device_clean_name()
                self.log(f"Started continuous screen stream ({interval}s interval, low-KB) for {dev_name}...")
                pull_idx = 0
                while self.pulling:
                    start_t = time.time()
                    raw, err, saved_path = self._capture_screen_bytes()
                    if raw:
                        pull_idx += 1
                        kb = len(raw) / 1024.0
                        self.after(0, lambda r=raw, p=pull_idx, k=kb, sp=saved_path: self._update_screen_ui(r, p, k, sp))
                    else:
                        self.log(f"Stream frame notice: {err}")

                    elapsed = time.time() - start_t
                    sleep_t = max(0.05, interval - elapsed)
                    time.sleep(sleep_t)

                # Purge any dangling temp capture files when stopped
                self.purge_mobile_temp_cache(silent=True)
                self.log("Live screen monitor stopped. Cleaned temporary mobile memory.")
                self.after(0, lambda: self.screen_status_lbl.config(text="Live stream stopped. Mobile cache clean.", fg="#94a3b8"))
                if self.var_auto_sleep.get():
                    self.put_device_to_sleep()

            self.pull_thread = threading.Thread(target=loop, daemon=True)
            self.pull_thread.start()
        else:
            self.pulling = False
            self.btn_pull_live.config(text="▶ Start 1s Stream", bg="#16a34a")

    def purge_mobile_temp_cache(self, silent=False):
        """Deletes any temporary adb/fastcap capture files from /data/local/tmp/"""
        def task():
            if not silent:
                self.log("Purging all temporary screenshot caches from mobile /data/local/tmp/...")
            self.run_adb(["shell", "rm -f /data/local/tmp/_sc* /data/local/tmp/screen.* /data/local/tmp/_fastcap_* /sdcard/gui_cap_screen.png /sdcard/tmp_screen.png"], timeout=6)
            if not silent:
                self.log("Mobile temporary capture cache is completely purged.")
                self.after(0, lambda: messagebox.showinfo("Cleaned", "Temporary mobile cache purged! 0 bytes used for captures."))
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
        if self.last_saved_path and os.path.exists(self.last_saved_path):
            os.startfile(self.last_saved_path)
        elif os.path.exists(os.path.join(PULLED_SCREENS_DIR, "latest_screen.jpg")):
            os.startfile(os.path.join(PULLED_SCREENS_DIR, "latest_screen.jpg"))
        else:
            messagebox.showinfo("Notice", "No screen pulled yet.")

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

            def _classify_item(full_path, fname):
                p_lower = full_path.lower()
                f_lower = fname.lower()
                folder_lower = os.path.dirname(p_lower)

                is_trashed = (
                    ".trashed" in p_lower or 
                    ".trash" in p_lower or 
                    "globaltrash" in p_lower or 
                    ".nomedia" in p_lower or 
                    "/.hidden" in p_lower or
                    "/.secret" in p_lower or
                    "/.thumbnails" in p_lower or
                    "/.aceself" in p_lower or
                    "recycle" in p_lower
                )

                if is_trashed:
                    return "Trashed/Hidden", True
                elif any(k in f_lower or k in folder_lower for k in ("screenshot", "screen_cap", "screencap", "gui_cap", "snap_")):
                    return "Screenshots", False
                elif "/camera" in p_lower or "dcim/camera" in p_lower or f_lower.startswith("img_20"):
                    return "Camera", False
                elif "whatsapp" in p_lower or "com.whatsapp" in p_lower:
                    return "WhatsApp", False
                elif any(k in p_lower for k in ("/download", "/downloads", "purchase", "bill", "invoice", "receipt", "order", "cart", "payment")):
                    return "Downloads & Purchases", False
                elif folder_lower in ("/sdcard", "/storage/emulated/0", "/storage/self/primary"):
                    return "Root Storage", False
                elif "/pictures" in p_lower:
                    return "Pictures", False
                else:
                    return "Gallery", False

            # 1. Query MediaStore Files Table (discovers all registered storage files across the system)
            f_code, f_out, _ = self.run_adb([
                "shell",
                "content query --uri content://media/external/file --projection _data:_size:date_modified --sort 'date_modified DESC'"
            ], timeout=20)

            if f_code == 0 and f_out:
                for line in f_out.splitlines():
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
                    cat, is_trashed = _classify_item(full_path, fname)

                    if ext not in valid_exts and not is_trashed:
                        continue

                    seen_paths.add(full_path)
                    seen_paths.add(norm_path)

                    if mtime <= 0:
                        m_date = re.search(r'(20\d{2})[-_]?(\d{2})[-_]?(\d{2})[-_]?(\d{2})?[-_]?(\d{2})?', fname)
                        if m_date:
                            try:
                                g = m_date.groups()
                                yr, mo, dy = int(g[0]), int(g[1]), int(g[2])
                                hr = int(g[3]) if g[3] else 12
                                mn = int(g[4]) if g[4] else 0
                                sc = int(g[5]) if g[5] else 0
                                dt = datetime(yr, mo, dy, hr, mn, sc)
                                mtime = int(dt.timestamp())
                            except Exception: pass

                    date_str = datetime.fromtimestamp(mtime).strftime('%Y-%m-%d %H:%M') if mtime > 0 else ""
                    items.append({
                        "path": full_path,
                        "name": fname,
                        "folder": os.path.dirname(full_path),
                        "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                        "size_bytes": size_bytes,
                        "mtime": mtime,
                        "date": date_str,
                        "category": cat,
                        "is_trashed": is_trashed,
                        "selected": False
                    })

            # 2. Query MediaStore Images & Video tables
            for m_uri in ["content://media/external/images/media", "content://media/external/video/media"]:
                q_code, q_out, _ = self.run_adb([
                    "shell",
                    f"content query --uri {m_uri} --projection _data:_size:date_modified --sort 'date_modified DESC'"
                ], timeout=15)
                if q_code == 0 and q_out:
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
                        cat, is_trashed = _classify_item(full_path, fname)
                        if ext not in valid_exts and not is_trashed:
                            continue

                        seen_paths.add(full_path)
                        seen_paths.add(norm_path)

                        if mtime <= 0:
                            m_date = re.search(r'(20\d{2})[-_]?(\d{2})[-_]?(\d{2})[-_]?(\d{2})?[-_]?(\d{2})?', fname)
                            if m_date:
                                try:
                                    g = m_date.groups()
                                    yr, mo, dy = int(g[0]), int(g[1]), int(g[2])
                                    hr = int(g[3]) if g[3] else 12
                                    mn = int(g[4]) if g[4] else 0
                                    sc = int(g[5]) if g[5] else 0
                                    dt = datetime(yr, mo, dy, hr, mn, sc)
                                    mtime = int(dt.timestamp())
                                except Exception: pass

                        date_str = datetime.fromtimestamp(mtime).strftime('%Y-%m-%d %H:%M') if mtime > 0 else ""
                        items.append({
                            "path": full_path,
                            "name": fname,
                            "folder": os.path.dirname(full_path),
                            "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                            "size_bytes": size_bytes,
                            "mtime": mtime,
                            "date": date_str,
                            "category": cat,
                            "is_trashed": is_trashed,
                            "selected": False
                        })

            # 3. Query MediaStore Trashed & Recycle Bin items
            for t_uri in ["content://media/external/images/media", "content://media/external/file"]:
                t_code, t_out, _ = self.run_adb([
                    "shell",
                    f"content query --uri {t_uri} --where 'is_trashed=1' --projection _data:_size:date_modified"
                ], timeout=10)
                if t_code == 0 and t_out:
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
                        date_str = datetime.fromtimestamp(mtime).strftime('%Y-%m-%d %H:%M') if mtime > 0 else ""

                        items.append({
                            "path": full_path,
                            "name": fname,
                            "folder": os.path.dirname(full_path),
                            "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                            "size_bytes": size_bytes,
                            "mtime": mtime,
                            "date": date_str,
                            "category": "Trashed/Hidden",
                            "is_trashed": True,
                            "selected": False
                        })

            # 4. Deep Filesystem Scan on key directories, root internal storage, hidden & trash paths
            find_cmd = (
                "find /sdcard/DCIM /sdcard/Pictures /sdcard/Download /sdcard/Downloads "
                "/sdcard/Android/media /sdcard/Documents /sdcard/Movies /sdcard/Music "
                "/sdcard/Bluetooth /sdcard/Browser /sdcard/Telegram /sdcard/ColorOS "
                "/sdcard/Snapchat /sdcard/WhatsApp /sdcard/MIUI /sdcard/tencent "
                "/sdcard/Recordings /sdcard/Audiobooks /sdcard/Podcasts /sdcard/Ringtones "
                "/sdcard/Alarms /sdcard/Notifications -type f -exec stat -c '%s %Y %n' {} + 2>/dev/null; "
                "stat -c '%s %Y %n' /sdcard/* /sdcard/.* /storage/emulated/0/* 2>/dev/null"
            )
            code, out, err = self.run_adb(["shell", find_cmd], timeout=35)

            if code == 0 and out:
                for line in out.splitlines():
                    line = line.strip()
                    if not line:
                        continue
                    parts = line.split(' ', 2)
                    if len(parts) < 3:
                        continue
                    try:
                        size_bytes = int(parts[0])
                        mtime = int(parts[1])
                        full_path = parts[2].strip()
                    except Exception:
                        continue

                    if not full_path:
                        continue

                    norm_path = full_path.replace("/storage/emulated/0", "/sdcard")
                    if full_path in seen_paths or norm_path in seen_paths:
                        continue
                    ext = os.path.splitext(full_path)[1].lower()
                    fname = os.path.basename(full_path)
                    cat, is_trashed = _classify_item(full_path, fname)

                    if ext not in valid_exts and not is_trashed:
                        continue

                    seen_paths.add(full_path)
                    seen_paths.add(norm_path)

                    if mtime <= 0:
                        m_date = re.search(r'(20\d{2})[-_]?(\d{2})[-_]?(\d{2})[-_]?(\d{2})?[-_]?(\d{2})?', fname)
                        if m_date:
                            try:
                                g = m_date.groups()
                                yr, mo, dy = int(g[0]), int(g[1]), int(g[2])
                                hr = int(g[3]) if g[3] else 12
                                mn = int(g[4]) if g[4] else 0
                                sc = int(g[5]) if g[5] else 0
                                dt = datetime(yr, mo, dy, hr, mn, sc)
                                mtime = int(dt.timestamp())
                            except Exception: pass

                    date_str = datetime.fromtimestamp(mtime).strftime('%Y-%m-%d %H:%M') if mtime > 0 else ""

                    items.append({
                        "path": full_path,
                        "name": fname,
                        "folder": os.path.dirname(full_path),
                        "size": self._format_size(size_bytes) if size_bytes > 0 else "",
                        "size_bytes": size_bytes,
                        "mtime": mtime,
                        "date": date_str,
                        "category": cat,
                        "is_trashed": is_trashed,
                        "selected": False
                    })

            # Always sort strictly by recent to older (newest first)
            items.sort(key=lambda x: (-x["mtime"], x["name"].lower()), reverse=False)

            def update_ui():
                self.all_gallery_items = items
                self._apply_gallery_filter_and_render()
                sc_count = sum(1 for it in items if it["category"] == "Screenshots")
                tr_count = sum(1 for it in items if it["category"] == "Trashed/Hidden")
                rt_count = sum(1 for it in items if it["category"] == "Root Storage")
                self.gallery_summary_lbl.config(
                    text=f"Total: {len(items)} media | 📱 Screenshots: {sc_count} | 📁 Root Storage: {rt_count} | 🗑️ Trashed/Hidden: {tr_count}"
                )
                self.log(f"Gallery scan complete: Found {len(items)} items (Screenshots: {sc_count}, Root: {rt_count}, Trashed/Hidden: {tr_count}) sorted Recent ➔ Older.")

            try:
                self.after(0, update_ui)
            except Exception:
                pass
        threading.Thread(target=task, daemon=True).start()

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

    def _bind_mousewheel_to_grid(self, widget):
        def _on_mousewheel(event):
            try:
                self.grid_canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")
            except Exception:
                pass
        widget.bind("<MouseWheel>", _on_mousewheel)

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
            sz = int(self.page_size_combo.get())
            if sz > 0:
                self.gallery_page_size = sz
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
        self.btn_f_all.config(bg="#0284c7" if filter_type == "ALL" else "#334155")
        self.btn_f_screens.config(bg="#0284c7" if filter_type == "SCREENSHOTS" else "#334155")
        self.btn_f_camera.config(bg="#0284c7" if filter_type == "CAMERA" else "#334155")
        self.btn_f_wa.config(bg="#0284c7" if filter_type == "WHATSAPP" else "#334155")
        self.btn_f_dl.config(bg="#0284c7" if filter_type == "DOWNLOADS" else "#334155")
        if hasattr(self, 'btn_f_root'):
            self.btn_f_root.config(bg="#0284c7" if filter_type == "ROOT" else "#334155")
        self.btn_f_trash.config(bg="#dc2626" if filter_type == "TRASHED" else "#7f1d1d")

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
            elif ft == "TRASHED" and (cat != "TRASHED/HIDDEN" and not item.get("is_trashed")):
                continue

            if query:
                if query not in item["name"].lower() and query not in item["folder"].lower():
                    continue

            filtered.append(item)

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
        self.gallery_total_pages = max(1, (total_items + self.gallery_page_size - 1) // self.gallery_page_size)
        if self.gallery_page >= self.gallery_total_pages:
            self.gallery_page = max(0, self.gallery_total_pages - 1)

        start_idx = self.gallery_page * self.gallery_page_size
        end_idx = min(start_idx + self.gallery_page_size, total_items)
        page_items = items[start_idx:end_idx]

        if total_items > 0:
            self.page_info_lbl.config(
                text=f"Page {self.gallery_page + 1} of {self.gallery_total_pages} ({start_idx + 1}-{end_idx} of {total_items})"
            )
        else:
            self.page_info_lbl.config(text="Page 0 of 0 (0 items)")

        card_w = self.gallery_thumb_size
        img_h = int(card_w * 0.85)

        try:
            canv_w = max(340, self.grid_canvas.winfo_width() - 24)
        except Exception:
            canv_w = 400
        cols = max(2, canv_w // (card_w + 14))

        new_queue = []

        for p_idx, item in enumerate(page_items):
            global_idx = start_idx + p_idx
            r = p_idx // cols
            c = p_idx % cols

            is_sel = item["selected"]
            is_trash = item.get("is_trashed") or item["category"] == "Trashed/Hidden"
            
            card_bg = "#1e3a8a" if is_sel else ("#3b1818" if is_trash else "#1e293b")
            border_c = "#38bdf8" if is_sel else ("#ef4444" if is_trash else "#334155")

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

            cat_short = "🗑️" if is_trash else ("📸" if item["category"] == "Camera" else ("📱" if item["category"] == "Screenshots" else ("💬" if item["category"] == "WhatsApp" else "🖼")))
            cat_lbl = tk.Label(hdr_frame, text=f"{cat_short} {item['category'][:6]}", bg=card_bg, fg="#fca5a5" if is_trash else "#cbd5e1", font=("Segoe UI", 7))
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
                "page_idx": p_idx,
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
            self.thumb_queue = new_queue

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
        
        def worker_loop():
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
                    def update_card(ci=card_info):
                        gi = ci.get("global_idx", ci.get("idx"))
                        if gi in self.card_widgets:
                            self._apply_cached_thumb_to_card(self.card_widgets[gi])
                    self.after(0, update_card)

        for _ in range(2):
            threading.Thread(target=worker_loop, daemon=True).start()

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

    # =========================================================================
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
        """Scans 3rd party user apps, disabled/hidden apps, and system apps from device"""
        def task():
            self.log("Scanning installed and hidden applications on mobile...")
            try:
                self.after(0, lambda: self.apps_summary_lbl.config(text="Scanning packages on mobile..."))
            except Exception:
                pass

            # 1. User packages (3rd party)
            code3, out3, _ = self.run_adb(["shell", "pm list packages -3 -f"], timeout=12)
            # 2. Disabled/Hidden packages
            coded, outd, _ = self.run_adb(["shell", "pm list packages -d -f"], timeout=12)
            # 3. System packages
            codes, outs, _ = self.run_adb(["shell", "pm list packages -s -f"], timeout=12)

            disabled_set = set()
            if coded == 0 and outd:
                for line in outd.splitlines():
                    if "=" in line:
                        pkg = line.split("=")[-1].strip()
                        disabled_set.add(pkg)

            apps = []
            seen_pkg = set()

            def parse_block(out, type_label):
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
                        if pkg_name in seen_pkg:
                            continue
                        seen_pkg.add(pkg_name)
                        is_dis = pkg_name in disabled_set
                        status_str = "🚫 Hidden/Frozen" if is_dis else "🟢 Active"
                        cat = "Disabled/Hidden" if is_dis else type_label
                        apps.append({
                            "pkg": pkg_name,
                            "apk": apk_path,
                            "type": cat,
                            "is_disabled": is_dis,
                            "status": status_str
                        })

            parse_block(out3, "User (3rd Party)")
            parse_block(outd, "Disabled/Hidden")
            parse_block(outs, "System")

            apps.sort(key=lambda x: (x["type"] != "Disabled/Hidden", x["type"] != "User (3rd Party)", x["pkg"].lower()))

            def update_ui():
                self.all_installed_apps = apps
                self._apply_apps_filter_and_render()
                u_cnt = sum(1 for a in apps if "User" in a["type"])
                d_cnt = sum(1 for a in apps if a["is_disabled"])
                self.apps_summary_lbl.config(
                    text=f"Total: {len(apps)} apps | 👤 User: {u_cnt} | 🚫 Hidden/Disabled: {d_cnt}"
                )
                self.log(f"Found {len(apps)} installed packages (User: {u_cnt}, Hidden/Disabled: {d_cnt}).")

            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def _set_apps_filter(self, filter_type):
        self.current_apps_filter = filter_type
        self.btn_app_user.config(bg="#0284c7" if filter_type == "USER" else "#334155")
        self.btn_app_disabled.config(bg="#0284c7" if filter_type == "DISABLED" else "#334155")
        self.btn_app_system.config(bg="#0284c7" if filter_type == "SYSTEM" else "#334155")
        self.btn_app_all.config(bg="#0284c7" if filter_type == "ALL" else "#334155")
        self._apply_apps_filter_and_render()

    def _apply_apps_filter_and_render(self):
        query = self.apps_search_entry.get().strip().lower() if hasattr(self, 'apps_search_entry') else ""
        ft = getattr(self, 'current_apps_filter', 'USER')

        filtered = []
        for app in self.all_installed_apps:
            t = app["type"].upper()
            if ft == "USER" and "USER" not in t:
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
            self.path_entry.insert(0, p)
            
        def task():
            self.log(f"Listing directory: {p}...")
            code, out, err = self.run_adb(["shell", f"ls -laF '{p}'"], timeout=12)
            
            items = []
            if code == 0 and out:
                lines = out.splitlines()
                for line in lines:
                    line = line.strip()
                    if not line or line.startswith("total"):
                        continue
                    parts = line.split(None, 8)
                    if len(parts) >= 9:
                        perm = parts[0]
                        size = parts[4]
                        name = parts[8]
                        if name in [".", "..", "./", "../"]:
                            continue
                        is_dir = perm.startswith("d") or name.endswith("/")
                        clean_name = name.rstrip("/") if is_dir else name
                        
                        items.append((is_dir, clean_name, f"{p.rstrip('/')}/{clean_name}", size))
                
                items.sort(key=lambda x: (not x[0], x[1].lower()))
            
            def update_ui():
                self.current_remote_items = items
                self.file_listbox.delete(0, "end")
                if items:
                    for is_dir, name, full_pth, size in items:
                        if is_dir:
                            display = f"📁 [DIR] {name}/"
                        else:
                            sz_str = self._format_size(size)
                            ext = os.path.splitext(name)[1].lower()
                            if ext in [".jpg", ".jpeg", ".png", ".webp", ".gif"]:
                                icon = "🖼"
                            elif ext in [".mp4", ".mkv", ".mov", ".3gp"]:
                                icon = "🎥"
                            elif ext in [".pdf", ".docx", ".doc", ".txt", ".xlsx"]:
                                icon = "📄"
                            elif ext in [".apk"]:
                                icon = "📦"
                            elif ext in [".mp3", ".m4a", ".opus", ".wav"]:
                                icon = "🎵"
                            else:
                                icon = "📎"
                            display = f"{icon} {name}  ({sz_str})"
                        self.file_listbox.insert("end", display)
                    self.log(f"Found {len(items)} items in {p}")
                else:
                    self.file_listbox.insert("end", "[Directory is empty or restricted]")
                    if err:
                        self.log(f"ls notice: {err}")
            self.after(0, update_ui)
        threading.Thread(target=task, daemon=True).start()

    def _on_item_double_click(self, event):
        sel = self.file_listbox.curselection()
        if not sel or sel[0] >= len(self.current_remote_items):
            return
        is_dir, name, full_path, size = self.current_remote_items[sel[0]]
        if is_dir:
            self.load_path(full_path)
        else:
            self.open_selected_item()

    def open_selected_item(self):
        sel = self.file_listbox.curselection()
        if not sel or sel[0] >= len(self.current_remote_items):
            messagebox.showinfo("Select Item", "Please select a file to open.")
            return
        is_dir, name, full_path, size = self.current_remote_items[sel[0]]
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
        sel = self.file_listbox.curselection()
        if not sel or sel[0] >= len(self.current_remote_items):
            messagebox.showinfo("Select Item", "Please select an item from the list.")
            return
        is_dir, name, full_path, size = self.current_remote_items[sel[0]]
        
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
