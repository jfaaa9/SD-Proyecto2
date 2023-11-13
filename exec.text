@echo off
setlocal enabledelayedexpansion

set "CLASS_PATH=.\bin"

set "SERVER_CLASS=src.server.SocketServer"
set "CLIENT_CLASS=src.component.SocketClient"
set "UI_CLASS=src.component.UserInterface"

start "Servidor" cmd /k "java -cp !CLASS_PATH! !SERVER_CLASS!"
start "UI-CLI 1" cmd /k "java -cp !CLASS_PATH! !UI_CLASS!"
start "UI-CLI 2" cmd /k "java -cp !CLASS_PATH! !UI_CLASS!"
start "UI-CLI 3" cmd /k "java -cp !CLASS_PATH! !UI_CLASS!"

