$device = "zxdada69gunb7ls4"
$apk = "app\build\outputs\apk\debug\app-debug.apk"
$package = "com.example.badnewgym"

Write-Host "Installing APK..."
adb -s $device install -r $apk
Write-Host "Starting App..."
adb -s $device shell am start -n "$package/.MainActivity"
Start-Sleep -Seconds 5

$themes = @("NATURAL_FRESH", "FUTURISTIC_NEON", "MINIMAL_DARK", "GLASSMORPHISM", "PREMIUM_3D", "VIBRANT_GRADIENT", "BEAST_MODE", "PURPLE_ROYAL")
mkdir -Force docs\screenshots

foreach ($theme in $themes) {
    Write-Host "Capturing $theme"
    adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" -e variant $theme
    Start-Sleep -Seconds 2
    adb -s $device exec-out screencap -p > "docs\screenshots\stage4_theme_$theme.png"
}

Write-Host "Capturing Semantic Matrix..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0
Start-Sleep -Seconds 2
adb -s $device exec-out screencap -p > "docs\screenshots\stage4_semantic_yash_overdue.png"

adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 1
Start-Sleep -Seconds 2
adb -s $device exec-out screencap -p > "docs\screenshots\stage4_semantic_arjun_premium.png"

adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 8
Start-Sleep -Seconds 2
adb -s $device exec-out screencap -p > "docs\screenshots\stage4_semantic_vikram_expired.png"

Write-Host "Capturing Menu Matrix..."
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" --ei memberIndex 0 --ez openDetail `$true
Start-Sleep -Seconds 2
adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" -e menu PAYMENT
Start-Sleep -Seconds 2
adb -s $device exec-out screencap -p > "docs\screenshots\stage4_menu_payment.png"

adb -s $device shell am broadcast -a "$package.DEBUG_VARIANT" -e menu PLAN
Start-Sleep -Seconds 2
adb -s $device exec-out screencap -p > "docs\screenshots\stage4_menu_plan.png"

Write-Host "Done"
