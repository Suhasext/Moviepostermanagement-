@echo off
echo Building CineVault Android App...
echo.

cd /d "C:\Users\Suhas\AndroidStudioProjects\Moviepostermanagementapp"

echo Cleaning previous build...
call gradlew clean

echo.
echo Building debug APK...
call gradlew assembleDebug

echo.
echo Build completed!
echo APK location: app\build\outputs\apk\debug\app-debug.apk
echo.
pause
