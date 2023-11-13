@echo off
set CLASSPATH=.\lib\postgresql-42.6.0.jar;.\src
set SRC_DIR=src
set BIN_DIR=bin

REM Crear directorio BIN_DIR si no existe
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

REM Compilar todo en src
dir /b /s "%SRC_DIR%\*.java" > sources.txt
javac -cp "%CLASSPATH%" -d "%BIN_DIR%" @sources.txt
if %errorlevel% neq 0 (
  echo La compilación falló.
  exit /b 1
)

REM Descomentar la siguiente línea si desea eliminar sources.txt después de la compilación
REM del sources.txt

echo Compilación exitosa. Los archivos .class se encuentran en %BIN_DIR%.
