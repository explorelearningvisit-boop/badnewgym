import os
import subprocess
import sys

go_exe = r"C:\Users\User\go_sdk\go\bin\go.exe"
tsnet_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym\tools\badgym-tsnet"
out_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym\app\src\main\jniLibs\arm64-v8a"
out_file = os.path.join(out_dir, "libbadgym_tsnet.so")

os.makedirs(out_dir, exist_ok=True)

env = os.environ.copy()
env["GOOS"] = "android"
env["GOARCH"] = "arm64"
env["CGO_ENABLED"] = "0"

cmd = [
    go_exe,
    "build",
    "-ldflags=-s -w",
    "-o",
    out_file,
    "."
]

print(f"Building {out_file} for android/arm64...")
res = subprocess.run(cmd, cwd=tsnet_dir, env=env)
if res.returncode != 0:
    print(f"Build failed with code {res.returncode}")
    sys.exit(res.returncode)

size_mb = os.path.getsize(out_file) / (1024 * 1024)
print(f"Build succeeded! Output binary size: {size_mb:.2f} MB")
