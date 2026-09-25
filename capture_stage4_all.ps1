$device = "zxdada69gunb7ls4"
$package = "com.example.badnewgym"

Write-Host "Ensuring screenshot directory..."
New-Item -ItemType Directory -Force -Path "docs\screenshots"

function Capture-Screen($remotePath, $localPath) {
    adb -s $device shell screencap -p $remotePath
    adb -s $device pull $remotePath $localPath
}

# 1. NATURAL_FRESH
Write-Host "1. Capturing NATURAL_FRESH..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "NATURAL_FRESH"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_natural_fresh.png" "docs\screenshots\stage4_natural_fresh.png"

# 2. FUTURISTIC_NEON
Write-Host "2. Capturing FUTURISTIC_NEON..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "FUTURISTIC_NEON"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_futuristic_neon.png" "docs\screenshots\stage4_futuristic_neon.png"

# 3. MINIMAL_DARK
Write-Host "3. Capturing MINIMAL_DARK..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "MINIMAL_DARK"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_minimal_dark.png" "docs\screenshots\stage4_minimal_dark.png"

# 4. GLASSMORPHISM
Write-Host "4. Capturing GLASSMORPHISM..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "GLASSMORPHISM"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_glassmorphism.png" "docs\screenshots\stage4_glassmorphism.png"

# 5. PREMIUM_3D
Write-Host "5. Capturing PREMIUM_3D..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "PREMIUM_3D"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_premium_3d.png" "docs\screenshots\stage4_premium_3d.png"

# 6. VIBRANT_GRADIENT
Write-Host "6. Capturing VIBRANT_GRADIENT..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "VIBRANT_GRADIENT"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_vibrant_gradient.png" "docs\screenshots\stage4_vibrant_gradient.png"

# 7. GYM_BEAST_MODE
Write-Host "7. Capturing GYM_BEAST_MODE..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "BEAST_MODE"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_gym_beast_mode.png" "docs\screenshots\stage4_gym_beast_mode.png"

# 8. PURPLE_ROYAL
Write-Host "8. Capturing PURPLE_ROYAL..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 -e variant "PURPLE_ROYAL"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_purple_royal.png" "docs\screenshots\stage4_purple_royal.png"

# 9. SEMANTIC MATRIX
Write-Host "9. Capturing SEMANTIC MATRIX (Browse multi-card side peek with overdue, premium, expired)..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_semantic_matrix.png" "docs\screenshots\stage4_semantic_matrix.png"

# 10. MENU MATRIX
Write-Host "10. Capturing MENU MATRIX (Bounded detail mode with rail & payment menu)..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 --ez openDetail `$true
Start-Sleep -Seconds 2
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" -e menu "PAYMENT"
Start-Sleep -Seconds 2
Capture-Screen "/sdcard/stage4_menu_matrix.png" "docs\screenshots\stage4_menu_matrix.png"

Write-Host "All 10 required physical screenshots captured successfully!"
