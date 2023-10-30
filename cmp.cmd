@echo off

set CLASS_NAME1=SocketClient
set CLASS_NAME2=SocketServer
set CLASS_NAME3=UserInterface
set SRC_DIR1=src\component
set SRC_DIR2=src\server
set SRC_DIR3=src\component

set BIN_DIR=bin

if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

javac -d "%BIN_DIR%" "%SRC_DIR1%\%CLASS_NAME1%.java"
if %errorlevel% neq 0 (
    echo La compilación de %CLASS_NAME1% falló.
    exit /b %errorlevel%
)

javac -d "%BIN_DIR%" "%SRC_DIR2%\%CLASS_NAME2%.java"
if %errorlevel% neq 0 (
    echo La compilación de %CLASS_NAME2% falló.
    exit /b %errorlevel%
)

javac -d "%BIN_DIR%" "%SRC_DIR3%\%CLASS_NAME3%.java"
if %errorlevel% neq 0 (
    echo La compilación de %CLASS_NAME3% falló.
    exit /b %errorlevel%
)

echo Compilacion exitosa. Los archivos .class se encuentran en %BIN_DIR%.