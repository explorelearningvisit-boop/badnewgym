import os
import subprocess
import sys

dest_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym\OPPO\WhatsApp_Private_Documents_50MB"
os.makedirs(dest_dir, exist_ok=True)

adb_device = "100.80.18.55:5555"

print("Querying image files in WhatsApp Documents/Private...")
cmd = [
    "adb", "-s", adb_device, "shell",
    "find '/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents/Private' -type f \\( -name '*.jpg' -o -name '*.jpeg' -o -name '*.png' \\) -exec stat -c '%s %n' {} +"
]

res = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, errors="ignore")
lines = res.stdout.strip().split("\n")

files = []
for line in lines:
    line = line.strip()
    if not line:
        continue
    parts = line.split(" ", 1)
    if len(parts) == 2:
        try:
            sz = int(parts[0])
            p = parts[1].strip()
            files.append((sz, p))
        except Exception:
            pass

print(f"Total image files found: {len(files)}")

# Select files up to ~50 MB
target_bytes = 50 * 1024 * 1024
selected = []
accumulated = 0

for sz, p in files:
    selected.append((sz, p))
    accumulated += sz
    if accumulated >= target_bytes:
        break

print(f"Selected {len(selected)} files totaling {accumulated / (1024*1024):.2f} MB")

# Create staging directory on phone
subprocess.run(["adb", "-s", adb_device, "shell", "mkdir -p /sdcard/Download/wa_doc_50mb"])

# Copy selected files to staging directory in batches of 15
for i in range(0, len(selected), 15):
    batch = selected[i:i+15]
    cp_cmds = [f"cp '{p}' /sdcard/Download/wa_doc_50mb/" for _, p in batch]
    batch_cmd = " ; ".join(cp_cmds)
    subprocess.run(["adb", "-s", adb_device, "shell", batch_cmd])

print("Staged files on phone. Now pulling to local folder...")
# Pull the folder
subprocess.run(["adb", "-s", adb_device, "pull", "/sdcard/Download/wa_doc_50mb/.", dest_dir])

# Cleanup staging directory on phone
subprocess.run(["adb", "-s", adb_device, "shell", "rm -rf /sdcard/Download/wa_doc_50mb"])

print(f"Done! All files pulled to: {dest_dir}")
