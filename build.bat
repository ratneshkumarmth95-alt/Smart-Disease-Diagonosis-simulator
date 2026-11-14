@echo off
title Build Smart Disease Diagnosis Simulator
echo Compiling Smart Disease Diagnosis Simulator...

if not exist bin mkdir bin

dir /s /b src\main\java\*.java > sources.txt
javac -d bin -encoding UTF-8 @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Run run.bat to launch the application.
) else (
    echo Compilation failed.
)
pause
