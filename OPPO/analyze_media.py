import os, sys
from collections import defaultdict
from datetime import datetime

photo_exts = {'.jpg', '.jpeg', '.png', '.gif', '.webp', '.heic', '.bmp', '.dng', '.raw', '.tif', '.tiff', '.svg'}
video_exts = {'.mp4', '.mkv', '.avi', '.3gp', '.mov', '.webm', '.flv', '.ts', '.m4v', '.wmv'}
cache_img_names = {'imgcache.0', 'imgcache.1', 'screennailcache.0', 'screennailcache.1', 'tilecache.0', 'tilecache.1', 'facecache.0', 'facecache.1'}

photos = []
videos = []
cache_imgs = []
other_files = []

stat_file = 'c:/Users/User/AndroidStudioProjects/badnewgym/scratch_all_files_stat.txt'

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
        
        fname = os.path.basename(path).lower()
        _, ext = os.path.splitext(fname)
        
        is_hidden = '/.' in path or fname.startswith('.')
        
        try:
            dt = datetime.fromtimestamp(mtime)
            date_str = dt.strftime('%Y-%m-%d %H:%M')
            year = dt.year
        except Exception:
            date_str = 'Unknown'
            year = 0

        item = {
            'size': size,
            'mtime': mtime,
            'date': date_str,
            'year': year,
            'path': path,
            'name': os.path.basename(path),
            'dir': os.path.dirname(path),
            'hidden': is_hidden
        }
        
        if fname in cache_img_names:
            cache_imgs.append(item)
        elif ext in photo_exts:
            photos.append(item)
        elif ext in video_exts:
            videos.append(item)
        else:
            other_files.append(item)

total_photos_mb = sum(p['size'] for p in photos) / (1024 * 1024)
total_videos_mb = sum(v['size'] for v in videos) / (1024 * 1024)
total_cache_mb = sum(c['size'] for c in cache_imgs) / (1024 * 1024)

print(f"=== OVERALL MEDIA TOTALS ===")
print(f"Photos (files): {len(photos)} files, {total_photos_mb:.2f} MB ({total_photos_mb/1024:.2f} GB)")
print(f"Videos (files): {len(videos)} files, {total_videos_mb:.2f} MB ({total_videos_mb/1024:.2f} GB)")
print(f"Gallery Binary Cache Images: {len(cache_imgs)} files, {total_cache_mb:.2f} MB ({total_cache_mb/1024:.2f} GB)")

hidden_photos = [p for p in photos if p['hidden']]
visible_photos = [p for p in photos if not p['hidden']]
hidden_videos = [v for v in videos if v['hidden']]
visible_videos = [v for v in videos if not v['hidden']]

print(f"\n=== PHOTOS: VISIBLE VS HIDDEN ===")
print(f"Visible Photos: {len(visible_photos)} files, {sum(p['size'] for p in visible_photos)/(1024*1024):.2f} MB")
print(f"Hidden Photos: {len(hidden_photos)} files, {sum(p['size'] for p in hidden_photos)/(1024*1024):.2f} MB")

print(f"\n=== VIDEOS: VISIBLE VS HIDDEN ===")
print(f"Visible Videos: {len(visible_videos)} files, {sum(v['size'] for v in visible_videos)/(1024*1024):.2f} MB")
print(f"Hidden Videos: {len(hidden_videos)} files, {sum(v['size'] for v in hidden_videos)/(1024*1024):.2f} MB")

# Folders breakdown
def folder_summary(items, title):
    by_folder = defaultdict(list)
    for it in items:
        by_folder[it['dir']].append(it)
    
    sorted_folders = sorted(by_folder.items(), key=lambda x: sum(f['size'] for f in x[1]), reverse=True)
    print(f"\n=== {title} BY FOLDER (Top 15) ===")
    for folder, flist in sorted_folders[:15]:
        sz_mb = sum(f['size'] for f in flist) / (1024 * 1024)
        years = sorted(list(set(f['year'] for f in flist if f['year'] > 0)))
        year_str = f"{years[0]}-{years[-1]}" if len(years) > 1 else (str(years[0]) if years else "N/A")
        is_hid = "HIDDEN" if ('/.' in folder or os.path.basename(folder).startswith('.')) else "VISIBLE"
        print(f"[{is_hid}] {folder} -> {len(flist)} files, {sz_mb:.2f} MB, Year(s): {year_str}")

folder_summary(photos, "PHOTOS")
folder_summary(videos, "VIDEOS")

# By year breakdown
def year_summary(items, title):
    by_yr = defaultdict(list)
    for it in items:
        by_yr[it['year']].append(it)
    print(f"\n=== {title} BY YEAR ===")
    for yr in sorted(by_yr.keys()):
        flist = by_yr[yr]
        sz_mb = sum(f['size'] for f in flist) / (1024 * 1024)
        print(f"Year {yr}: {len(flist)} files, {sz_mb:.2f} MB")

year_summary(photos, "PHOTOS")
year_summary(videos, "VIDEOS")
