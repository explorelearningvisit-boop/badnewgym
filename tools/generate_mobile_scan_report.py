import os
import sys
import json
import re
from collections import defaultdict
from datetime import datetime

base_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym"
stat_file = os.path.join(base_dir, "scratch_all_files_stat.txt")
diskstats_file = os.path.join(base_dir, "scratch_diskstats.txt")
pkg_list_file = os.path.join(base_dir, "scratch_packages_list.txt")
pkg_times_file = os.path.join(base_dir, "scratch_pkg_times.txt")
launcher_file = os.path.join(base_dir, "scratch_launcher_apps.txt")

out_json = os.path.join(base_dir, "OPPO", "OPPO_FULL_MOBILE_SCAN.json")
out_md = os.path.join(base_dir, "OPPO", "OPPO_FULL_MOBILE_SCAN_REPORT.md")

# 1. PROCESS ALL STORAGE FILES (9,517 files)
photo_exts = {'.jpg', '.jpeg', '.png', '.gif', '.webp', '.heic', '.bmp', '.dng', '.raw', '.tif', '.tiff', '.svg'}
video_exts = {'.mp4', '.mkv', '.avi', '.3gp', '.mov', '.webm', '.flv', '.ts', '.m4v', '.wmv'}
audio_exts = {'.mp3', '.m4a', '.aac', '.wav', '.ogg', '.opus', '.flac', '.amr'}
cache_img_names = {'imgcache.0', 'imgcache.1', 'screennailcache.0', 'screennailcache.1', 'tilecache.0', 'tilecache.1', 'facecache.0', 'facecache.1'}

photos = []
videos = []
cache_imgs = []
other_files = []

with open(stat_file, 'r', encoding='utf-8', errors='ignore') as f:
    for line in f:
        line = line.strip()
        if not line:
            continue
        parts = line.split(' ', 2)
        if len(parts) < 3:
            continue
        try:
            size = int(parts[0])
            mtime = int(parts[1])
            path = parts[2]
        except Exception:
            continue
        
        fname = os.path.basename(path)
        fname_lower = fname.lower()
        _, ext = os.path.splitext(fname_lower)
        
        is_hidden = ('/.' in path) or fname.startswith('.') or ('/private' in path.lower()) or ('/statuses' in path.lower())
        
        try:
            dt = datetime.fromtimestamp(mtime)
            date_str = dt.strftime('%Y-%m-%d %H:%M')
            year = dt.year
        except Exception:
            date_str = 'Unknown'
            year = 0

        item = {
            'name': fname,
            'size': size,
            'size_mb': round(size / (1024 * 1024), 2),
            'mtime': mtime,
            'date': date_str,
            'year': year,
            'path': path,
            'folder': os.path.dirname(path),
            'hidden': is_hidden
        }
        
        if fname_lower in cache_img_names:
            cache_imgs.append(item)
        elif ext in photo_exts:
            photos.append(item)
        elif ext in video_exts:
            videos.append(item)
        else:
            other_files.append(item)

# Group photos and videos by folder
def group_by_folder(items):
    folders = defaultdict(lambda: {'count': 0, 'size': 0, 'years': set(), 'dates': [], 'files': [], 'hidden_count': 0})
    for it in items:
        f = folders[it['folder']]
        f['count'] += 1
        f['size'] += it['size']
        if it['year'] > 0:
            f['years'].add(it['year'])
        f['dates'].append(it['date'])
        if it['hidden']:
            f['hidden_count'] += 1
        f['files'].append(it)
    
    result = []
    for fpath, data in folders.items():
        sorted_years = sorted(list(data['years']))
        year_str = f"{sorted_years[0]}-{sorted_years[-1]}" if len(sorted_years) > 1 else (str(sorted_years[0]) if sorted_years else "Unknown")
        is_folder_hidden = ('/.' in fpath) or os.path.basename(fpath).startswith('.') or ('/private' in fpath.lower()) or ('/statuses' in fpath.lower())
        result.append({
            'folder': fpath,
            'count': data['count'],
            'size_bytes': data['size'],
            'size_mb': round(data['size'] / (1024 * 1024), 2),
            'years': year_str,
            'hidden': is_folder_hidden or (data['hidden_count'] == data['count']),
            'hidden_files_count': data['hidden_count'],
            'dates_range': f"{min(data['dates'])} to {max(data['dates'])}" if data['dates'] else "Unknown"
        })
    result.sort(key=lambda x: x['size_bytes'], reverse=True)
    return result

photos_by_folder = group_by_folder(photos)
videos_by_folder = group_by_folder(videos)

# 2. PROCESS APPS
# A. Parse launcher apps (UTF-16)
launcher_pkgs = set()
if os.path.exists(launcher_file):
    with open(launcher_file, 'r', encoding='utf-16', errors='ignore') as f:
        for line in f:
            m = re.search(r'([a-zA-Z0-9_.]+)/', line)
            if m:
                launcher_pkgs.add(m.group(1).strip())

# B. Parse package APK paths (UTF-16)
pkg_paths = {}
pkg_is_third_party = {}
if os.path.exists(pkg_list_file):
    with open(pkg_list_file, 'r', encoding='utf-16', errors='ignore') as f:
        for line in f:
            line = line.strip()
            if line.startswith("package:"):
                line = line[len("package:"):]
            if "=" in line:
                path, pkg = line.rsplit("=", 1)
                path = path.strip()
                pkg = pkg.strip()
                pkg_paths[pkg] = path
                # Check third-party vs system
                is_3rd = path.startswith("/data/app/") and not ("/data/app/BackupAndRestore" in path)
                pkg_is_third_party[pkg] = is_3rd

# C. Parse package install/update timestamps (UTF-16)
pkg_times = {}
if os.path.exists(pkg_times_file):
    with open(pkg_times_file, 'r', encoding='utf-16', errors='ignore') as f:
        content = f.read()
    blocks = re.split(r'Package \[', content)
    for b in blocks[1:]:
        lines = b.split('\n')
        pkg_name = lines[0].split(']')[0].strip()
        first_install = "Unknown"
        last_update = "Unknown"
        for l in lines:
            if "firstInstallTime=" in l:
                first_install = l.split("firstInstallTime=")[1].strip()
            elif "lastUpdateTime=" in l:
                last_update = l.split("lastUpdateTime=")[1].strip()
        pkg_times[pkg_name] = {
            'firstInstallTime': first_install,
            'lastUpdateTime': last_update
        }

# D. Parse app sizes from diskstats (UTF-16)
diskstats_data = {}
if os.path.exists(diskstats_file):
    with open(diskstats_file, 'r', encoding='utf-16', errors='ignore') as f:
        lines = [l.strip() for l in f if l.strip()]
    raw = {}
    for l in lines:
        if ":" in l:
            k, v = l.split(":", 1)
            try:
                raw[k.strip()] = json.loads(v.strip())
            except Exception:
                pass
    
    names = raw.get("Package Names", [])
    app_szs = raw.get("App Sizes", [])
    data_szs = raw.get("App Data Sizes", [])
    cache_szs = raw.get("Cache Sizes", [])
    
    for i, p in enumerate(names):
        diskstats_data[p] = {
            'app_size': app_szs[i] if i < len(app_szs) else 0,
            'data_size': data_szs[i] if i < len(data_szs) else 0,
            'cache_size': cache_szs[i] if i < len(cache_szs) else 0
        }

friendly_names = {
    'com.whatsapp': 'WhatsApp',
    'com.example.badnewgym': 'BAD GYM (Member Intelligence)',
    'com.google.android.youtube': 'YouTube',
    'com.coloros.gallery3d': 'Photos / Gallery (ColorOS)',
    'com.google.android.apps.photos': 'Google Photos',
    'com.android.chrome': 'Google Chrome',
    'com.google.android.gm': 'Gmail',
    'com.google.android.googlequicksearchbox': 'Google Search',
    'com.google.android.apps.maps': 'Google Maps',
    'com.google.android.apps.messaging': 'Google Messages',
    'com.google.android.dialer': 'Phone by Google',
    'com.google.android.contacts': 'Google Contacts',
    'com.google.android.calendar': 'Google Calendar',
    'com.google.android.keep': 'Google Keep Notes',
    'com.google.android.apps.docs': 'Google Drive',
    'com.google.android.apps.tachyon': 'Google Meet',
    'com.google.android.tts': 'Speech Services by Google',
    'com.android.vending': 'Google Play Store',
    'com.heytap.market': 'App Market (OPPO)',
    'com.heytap.browser': 'HeyTap Browser',
    'com.heytap.themestore': 'Theme Store (OPPO)',
    'com.coloros.compass2': 'Compass',
    'com.coloros.calculator': 'Calculator',
    'com.coloros.soundrecorder': 'Sound Recorder',
    'com.coloros.weather2': 'Weather',
    'com.coloros.alarm': 'Clock',
    'com.coloros.alarmclock': 'Clock / Alarm',
    'com.coloros.filemanager': 'My Files (File Manager)',
    'com.coloros.video': 'Video Player (OPPO)',
    'com.coloros.gamespace': 'Game Space',
    'com.coloros.childrenspace': 'Kid Space',
    'com.coloros.securepay': 'Payment Protection',
    'com.coloros.phonemanager': 'Phone Manager (Security)',
    'com.coloros.backuprestore': 'Backup & Restore',
    'com.oppo.camera': 'Camera (OPPO)',
    'com.snapchat.android': 'Snapchat',
    'com.instagram.android': 'Instagram',
    'com.instagram.lite': 'Instagram Lite',
    'com.meesho.supply': 'Meesho (Online Shopping)',
    'in.amazon.mShop.android.shopping': 'Amazon Shopping',
    'com.manash.purplle': 'Purplle (Beauty Shopping)',
    'com.openai.chatgpt': 'ChatGPT',
    'com.google.android.apps.bard': 'Gemini (AI Assistant)',
    'com.linkedin.android': 'LinkedIn',
    'com.kotak811mobilebankingapp.instantsavingsupiscanandpayrecharge': 'Kotak811 Mobile Banking',
    'com.bankofbaroda.mconnect': 'bob World (Bank of Baroda)',
    'in.org.npci.upiapp': 'BHIM UPI',
    'com.google.android.apps.nbu.paisa.user': 'Google Pay (GPay)',
    'com.google.android.apps.nbu.files': 'Files by Google',
    'ph.spacedesk.beta': 'spacedesk (Display Extension)',
    'com.noisefit': 'NoiseFit (Smartwatch Sync)',
    'com.view.ppcs': 'LookCam (WiFi Security Cam)',
    'com.fotoable.makeup': 'Makeup Photo Editor',
    'com.video.fun.app': 'Video Fun Player',
    'cn.wps.moffice_eng': 'WPS Office',
    'com.tailscale.ipn': 'Tailscale VPN',
    'com.oplus.customize.coreapp': 'OPPO Custom Core (Disabled)',
    'com.google.android.ims': 'Google Carrier IMS (Disabled)',
    'com.example.banaraspuregold': 'Banaras Pure Gold'
}

disabled_pkgs = {'com.oplus.customize.coreapp', 'com.google.android.ims'}

all_apps = []
all_pkg_names = sorted(list(set(list(pkg_paths.keys()) + list(diskstats_data.keys()))))

for pkg in all_pkg_names:
    apk_path = pkg_paths.get(pkg, 'System Partition')
    folder = os.path.dirname(apk_path) if apk_path != 'System Partition' else 'System Partition'
    
    is_3rd = pkg_is_third_party.get(pkg, False)
    is_launcher = pkg in launcher_pkgs
    is_disabled = pkg in disabled_pkgs
    
    if is_disabled:
        state = "DISABLED"
    elif is_launcher:
        state = "LAUNCHER_VISIBLE"
    elif is_3rd:
        state = "HIDDEN_3RD_PARTY"  # Installed 3rd party app with no launcher icon (helper, service, hidden)
    else:
        state = "SYSTEM_COMPONENT"  # System framework, overlay, or system service
    
    # Times
    times = pkg_times.get(pkg, {})
    first_install = times.get('firstInstallTime', 'Unknown')
    last_update = times.get('lastUpdateTime', 'Unknown')
    year = first_install[:4] if first_install != 'Unknown' and len(first_install) >= 4 else 'ROM'
    update_year = last_update[:4] if last_update != 'Unknown' and len(last_update) >= 4 else 'ROM'
    
    # Sizes
    sizes = diskstats_data.get(pkg, {'app_size': 0, 'data_size': 0, 'cache_size': 0})
    app_sz = sizes['app_size']
    data_sz = sizes['data_size']
    cache_sz = sizes['cache_size']
    total_sz = app_sz + data_sz + cache_sz
    
    # Friendly Name
    if pkg in friendly_names:
        label = friendly_names[pkg]
    else:
        # Generate readable label
        suffix = pkg.split('.')[-1].replace('_', ' ').capitalize()
        label = f"{suffix} ({pkg})"
    
    all_apps.append({
        'package': pkg,
        'name': label,
        'state': state,
        'is_launcher': is_launcher,
        'is_disabled': is_disabled,
        'is_3rd_party': is_3rd,
        'apk_path': apk_path,
        'folder': folder,
        'first_install': first_install,
        'last_update': last_update,
        'year': year,
        'update_year': update_year,
        'app_size_mb': round(app_sz / (1024 * 1024), 2),
        'data_size_mb': round(data_sz / (1024 * 1024), 2),
        'cache_size_mb': round(cache_sz / (1024 * 1024), 2),
        'total_size_mb': round(total_sz / (1024 * 1024), 2),
        'total_size_bytes': total_sz
    })

all_apps.sort(key=lambda x: x['total_size_bytes'], reverse=True)

# 3. BUILD JSON AND MARKDOWN REPORT
scan_summary = {
    'device': 'OPPO A53 (CPH2127)',
    'ip': '100.80.18.55:5555',
    'scan_timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
    'storage': {
        'total_gb': 48.0,
        'used_gb': 42.1,
        'avail_gb': 5.9,
        'use_percent': '88%'
    },
    'photos': {
        'total_count': len(photos),
        'total_size_mb': round(sum(p['size'] for p in photos) / (1024 * 1024), 2),
        'total_size_gb': round(sum(p['size'] for p in photos) / (1024 * 1024 * 1024), 3),
        'visible_count': len([p for p in photos if not p['hidden']]),
        'visible_size_mb': round(sum(p['size'] for p in photos if not p['hidden']) / (1024 * 1024), 2),
        'hidden_count': len([p for p in photos if p['hidden']]),
        'hidden_size_mb': round(sum(p['size'] for p in photos if p['hidden']) / (1024 * 1024), 2),
        'folders': photos_by_folder
    },
    'videos': {
        'total_count': len(videos),
        'total_size_mb': round(sum(v['size'] for v in videos) / (1024 * 1024), 2),
        'total_size_gb': round(sum(v['size'] for v in videos) / (1024 * 1024 * 1024), 3),
        'visible_count': len([v for v in videos if not v['hidden']]),
        'visible_size_mb': round(sum(v['size'] for v in videos if not v['hidden']) / (1024 * 1024), 2),
        'hidden_count': len([v for v in videos if v['hidden']]),
        'hidden_size_mb': round(sum(v['size'] for v in videos if v['hidden']) / (1024 * 1024), 2),
        'folders': videos_by_folder
    },
    'gallery_cache': {
        'total_count': len(cache_imgs),
        'total_size_mb': round(sum(c['size'] for c in cache_imgs) / (1024 * 1024), 2),
        'total_size_gb': round(sum(c['size'] for c in cache_imgs) / (1024 * 1024 * 1024), 3),
        'files': [{ 'name': c['name'], 'size_mb': c['size_mb'], 'folder': c['folder'], 'date': c['date'] } for c in cache_imgs]
    },
    'apps': {
        'total_packages': len(all_apps),
        'launcher_visible_count': len([a for a in all_apps if a['is_launcher']]),
        'hidden_or_background_count': len([a for a in all_apps if not a['is_launcher']]),
        'disabled_count': len([a for a in all_apps if a['is_disabled']]),
        'third_party_count': len([a for a in all_apps if a['is_3rd_party']]),
        'system_count': len([a for a in all_apps if not a['is_3rd_party']]),
        'total_apps_storage_mb': round(sum(a['total_size_bytes'] for a in all_apps) / (1024 * 1024), 2),
        'total_apps_storage_gb': round(sum(a['total_size_bytes'] for a in all_apps) / (1024 * 1024 * 1024), 2),
        'list': all_apps
    }
}

os.makedirs(os.path.dirname(out_json), exist_ok=True)
with open(out_json, 'w', encoding='utf-8') as f:
    json.dump(scan_summary, f, indent=2)

print(f"JSON report written to: {out_json}")

# BUILD MARKDOWN REPORT
md_lines = []
md_lines.append("# Full Device Storage & Application Scan Report: OPPO A53 (CPH2127)")
md_lines.append(f"**Target Device:** OPPO A53 (`CPH2127` / Tailscale IP: `100.80.18.55:5555`)  ")
md_lines.append(f"**Scan Date:** {scan_summary['scan_timestamp']}  ")
md_lines.append(f"**Storage Capacity:** 48.0 GB Total | ~42.1 GB Used (88%) | ~5.9 GB Available  ")
md_lines.append("\n---\n")

# Executive Summary
md_lines.append("## 1. Executive Summary")
md_lines.append("| Category | Total Count | Total Size | Visible Size | Hidden / System Size |")
md_lines.append("| :--- | :--- | :--- | :--- | :--- |")
p_s = scan_summary['photos']
v_s = scan_summary['videos']
c_s = scan_summary['gallery_cache']
a_s = scan_summary['apps']
md_lines.append(f"| **Photos / Images** | **{p_s['total_count']:,}** files | **{p_s['total_size_mb']:.2f} MB** ({p_s['total_size_gb']:.2f} GB) | {p_s['visible_size_mb']:.2f} MB ({p_s['visible_count']:,} files) | **{p_s['hidden_size_mb']:.2f} MB** ({p_s['hidden_count']:,} files) |")
md_lines.append(f"| **Videos** | **{v_s['total_count']:,}** files | **{v_s['total_size_mb']:.2f} MB** ({v_s['total_size_gb']:.2f} GB) | {v_s['visible_size_mb']:.2f} MB ({v_s['visible_count']:,} files) | **{v_s['hidden_size_mb']:.2f} MB** ({v_s['hidden_count']:,} files) |")
md_lines.append(f"| **Gallery Thumbnail & Screen Caches** | **{c_s['total_count']:,}** files | **{c_s['total_size_mb']:.2f} MB** ({c_s['total_size_gb']:.2f} GB) | - | 1,068.63 MB (ColorOS Binary Cache) |")
md_lines.append(f"| **Applications & App Data** | **{a_s['total_packages']}** packages | **{a_s['total_apps_storage_mb']:.2f} MB** ({a_s['total_apps_storage_gb']:.2f} GB) | {a_s['launcher_visible_count']} Launcher Apps | {a_s['hidden_or_background_count']} Hidden/System/Services |")
md_lines.append(f"| **Total Media Combined** | **{p_s['total_count'] + v_s['total_count'] + c_s['total_count']:,}** items | **{(p_s['total_size_mb'] + v_s['total_size_mb'] + c_s['total_size_mb'])/1024:.2f} GB** | - | - |")
md_lines.append("\n---\n")

# Photos Breakdown
md_lines.append("## 2. Photos & Images Breakdown (by Folder)")
md_lines.append("All photo files found across `/storage/emulated/0`, sorted by folder size:\n")
md_lines.append("| Status | Folder Path | Count | Total Size | Date / Year Range |")
md_lines.append("| :---: | :--- | :---: | :---: | :---: |")
for f in photos_by_folder:
    status_badge = "🔒 **HIDDEN**" if f['hidden'] else "👁️ Visible"
    md_lines.append(f"| {status_badge} | `{f['folder']}` | {f['count']} | **{f['size_mb']:.2f} MB** | {f['years']} |")

md_lines.append("\n---\n")

# Videos Breakdown
md_lines.append("## 3. Videos Breakdown (by Folder)")
md_lines.append("All video files found across `/storage/emulated/0`, sorted by folder size:\n")
md_lines.append("| Status | Folder Path | Count | Total Size | Date / Year Range |")
md_lines.append("| :---: | :--- | :---: | :---: | :---: |")
for f in videos_by_folder:
    status_badge = "🔒 **HIDDEN**" if f['hidden'] else "👁️ Visible"
    md_lines.append(f"| {status_badge} | `{f['folder']}` | {f['count']} | **{f['size_mb']:.2f} MB** | {f['years']} |")

md_lines.append("\n---\n")

# Hidden Gallery Cache Images
md_lines.append("## 4. ColorOS Gallery Internal Binary Cache (`com.coloros.gallery3d`)")
md_lines.append("These are database-backed thumbnail, tile, and full-resolution screen caches rendered by the system gallery app:\n")
md_lines.append("| Cache File Name | Size (MB) | Folder Path | Last Modified Date |")
md_lines.append("| :--- | :---: | :--- | :---: |")
for c in scan_summary['gallery_cache']['files']:
    md_lines.append(f"| `{c['name']}` | **{c['size_mb']:.2f} MB** | `{c['folder']}` | {c['date']} |")

md_lines.append("\n---\n")

# Applications Breakdown
md_lines.append("## 5. All Applications Scan (Including Hidden, Disabled, and System)")
md_lines.append(f"Total Packages: **{a_s['total_packages']}** | Launcher Visible: **{a_s['launcher_visible_count']}** | Hidden/System/Services: **{a_s['hidden_or_background_count']}** | Disabled: **{a_s['disabled_count']}**\n")

# Section 5A: Top Apps by Storage Size
md_lines.append("### 5A. Top 35 Applications by Storage Footprint (App + Data + Cache)")
md_lines.append("| App Name | Package Name | State | App Size | Data Size | Cache Size | **Total Size** | Install / Update Year |")
md_lines.append("| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |")
for a in all_apps[:35]:
    st_label = "👁️ Launcher" if a['state'] == 'LAUNCHER_VISIBLE' else ("🚫 Disabled" if a['state'] == 'DISABLED' else ("🔒 Hidden 3rd Party" if a['state'] == 'HIDDEN_3RD_PARTY' else "⚙️ System/BG"))
    md_lines.append(f"| **{a['name']}** | `{a['package']}` | {st_label} | {a['app_size_mb']:.1f} MB | {a['data_size_mb']:.1f} MB | {a['cache_size_mb']:.1f} MB | **{a['total_size_mb']:.1f} MB** | {a['year']} / {a['update_year']} |")

# Section 5B: Third-Party Applications (Installed by user)
md_lines.append("\n### 5B. Third-Party Installed Applications")
md_lines.append("| App Name | Package Name | Visibility | Total Size | Folder / APK Path | Install Date |")
md_lines.append("| :--- | :--- | :---: | :---: | :--- | :---: |")
third_party_apps = [a for a in all_apps if a['is_3rd_party']]
for a in third_party_apps:
    vis = "👁️ App Drawer" if a['is_launcher'] else "🔒 Hidden / Helper"
    md_lines.append(f"| **{a['name']}** | `{a['package']}` | {vis} | **{a['total_size_mb']:.1f} MB** | `{a['folder']}` | {a['first_install'][:10] if a['first_install'] != 'Unknown' else 'Unknown'} |")

# Section 5C: Disabled Applications
md_lines.append("\n### 5C. Disabled Applications")
md_lines.append("| App Name | Package Name | Total Size | Folder / Path |")
md_lines.append("| :--- | :--- | :---: | :--- |")
disabled_apps_list = [a for a in all_apps if a['is_disabled']]
for a in disabled_apps_list:
    md_lines.append(f"| **{a['name']}** | `{a['package']}` | **{a['total_size_mb']:.1f} MB** | `{a['folder']}` |")

with open(out_md, 'w', encoding='utf-8') as f:
    f.write("\n".join(md_lines))

print(f"Markdown report updated at: {out_md}")
