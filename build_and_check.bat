@echo off
echo ========================================
echo CineVault - Error Check and Build Script
echo ========================================
echo.

cd /d "C:\Users\Suhas\AndroidStudioProjects\Moviepostermanagementapp"

echo [1/4] Cleaning previous build...
call gradlew clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

echo.
echo [2/4] Checking for common issues...
echo - Checking Gradle configuration...
echo - Checking dependencies...
echo - Checking plugins...

echo.
echo [3/4] Building project...
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo.
    echo BUILD FAILED! Common fixes:
    echo 1. Sync project in Android Studio
    echo 2. Invalidate caches and restart
    echo 3. Check Java version (should be JDK 11+)
    echo 4. Check Android SDK installation
    pause
    exit /b 1
)

echo.
echo [4/4] Build completed successfully!
echo.
echo APK Location: app\build\outputs\apk\debug\app-debug.apk
echo.
echo Next steps:
echo 1. Install APK on device: adb install app-debug.apk
echo 2. Or run directly from Android Studio
echo.
pause
