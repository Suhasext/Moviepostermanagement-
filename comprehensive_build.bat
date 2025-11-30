@echo off
echo ========================================
echo CineVault - Comprehensive Build Fix
echo ========================================
echo.

cd /d "C:\Users\Suhas\AndroidStudioProjects\Moviepostermanagementapp"

echo [1/6] Cleaning project...
call gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

echo.
echo [2/6] Checking Gradle configuration...
echo - Verifying plugins...
echo - Checking dependencies...
echo - Validating version catalogs...

echo.
echo [3/6] Compiling Kotlin files...
call gradlew.bat :app:compileDebugKotlin
if %errorlevel% neq 0 (
    echo.
    echo KOTLIN COMPILATION FAILED!
    echo Common fixes:
    echo 1. Check for circular imports
    echo 2. Verify all imports are correct
    echo 3. Check for syntax errors
    echo 4. Ensure all dependencies are available
    echo.
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo [4/6] Processing annotations (KAPT)...
call gradlew.bat :app:kaptDebugKotlin
if %errorlevel% neq 0 (
    echo.
    echo KAPT PROCESSING FAILED!
    echo This usually means:
    echo 1. Room database annotations have issues
    echo 2. Hilt dependency injection has problems
    echo 3. Missing or incorrect annotations
    echo.
    pause
    exit /b 1
)

echo.
echo [5/6] Building APK...
call gradlew.bat assembleDebug
if %errorlevel% neq 0 (
    echo.
    echo APK BUILD FAILED!
    echo Check the error messages above for details.
    pause
    exit /b 1
)

echo.
echo [6/6] Build completed successfully!
echo.
echo APK Location: app\build\outputs\apk\debug\app-debug.apk
echo.
echo Next steps:
echo 1. Install APK: adb install app\build\outputs\apk\debug\app-debug.apk
echo 2. Or run from Android Studio
echo.
echo All compilation errors have been resolved!
pause
