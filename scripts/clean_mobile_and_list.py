import subprocess
import os
import glob

def main():
    print("==================================================")
    print("      MOBILE TEMPORARY & SCREENSHOT PURGE         ")
    print("==================================================")
    
    # Check connected devices
    proc = subprocess.run(["adb", "devices"], capture_output=True, text=True)
    devices = [line.split()[0] for line in proc.stdout.splitlines() if "\tdevice" in line]
    print(f"Active connected ADB devices: {devices}")
    
    for dev in devices:
        print(f"\n[Device {dev}] Auditing storage...")
        # Inspect temp files
        check = subprocess.run(["adb", "-s", dev, "shell", "ls -la /sdcard/screen* /sdcard/screencap* /sdcard/badgym* /data/local/tmp/ 2>/dev/null"], capture_output=True, text=True)
        print("Existing temporary dumps/files found:")
        print(check.stdout.strip() if check.stdout.strip() else "(None found)")
        
        # Clean temporary dumps, screenshots, screencaps, and cache
        clean_cmd = "rm -f /sdcard/screen* /sdcard/screencap* /sdcard/screenshot* /sdcard/badgym* /data/local/tmp/*.png /data/local/tmp/*.jpg /data/local/tmp/*.raw /data/local/tmp/*.apk /data/local/tmp/*.tmp 2>/dev/null"
        subprocess.run(["adb", "-s", dev, "shell", clean_cmd])
        
        post_ls = subprocess.run(["adb", "-s", dev, "shell", "ls /data/local/tmp/ 2>/dev/null"], capture_output=True, text=True).stdout.strip()
        print(f"Status: /data/local/tmp/ is now: {post_ls if post_ls else 'EMPTY (0 bytes)'}")
        print(f"-> Device {dev} temporary mobile storage cleared.")

    print("\n==================================================")
    print("      LAPTOP PULLED SCREENSHOTS SUMMARY           ")
    print("==================================================")
    
    base_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym\pulled_screens"
    if not os.path.exists(base_dir):
        print("No pulled_screens directory found.")
        return
        
    total_files = 0
    total_bytes = 0
    
    for root, dirs, files in os.walk(base_dir):
        img_files = [f for f in files if f.lower().endswith(('.png', '.jpg', '.jpeg', '.webp'))]
        if img_files:
            dir_size = sum(os.path.getsize(os.path.join(root, f)) for f in img_files)
            total_files += len(img_files)
            total_bytes += dir_size
            rel_path = os.path.relpath(root, base_dir)
            print(f"  Folder: pulled_screens\\{rel_path if rel_path != '.' else ''} | {len(img_files)} images | {dir_size / (1024*1024):.2f} MB")
            # show newest 3 files
            for f in sorted(img_files, key=lambda x: os.path.getmtime(os.path.join(root, x)), reverse=True)[:3]:
                mtime = os.path.getmtime(os.path.join(root, f))
                fsize = os.path.getsize(os.path.join(root, f))
                print(f"    - {f} ({fsize/1024:.1f} KB)")
                
    print("--------------------------------------------------")
    print(f"TOTAL PULLED SCREENS ON LAPTOP: {total_files} files ({total_bytes / (1024*1024):.2f} MB)")
    print("==================================================")

if __name__ == "__main__":
    main()
