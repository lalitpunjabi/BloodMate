@echo off
echo Compiling JavaFX application...
call mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
) 