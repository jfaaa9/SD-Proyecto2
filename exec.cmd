@echo off

set CLASS_PATH=.\bin;.\lib\postgresql-42.6.0.jar
set SERVER_CLASS=src.server.SocketServer
set UI_CLASS=src.component.UserInterface

REM Iniciar Servidor
start cmd /k "java -cp %CLASS_PATH% %SERVER_CLASS%"
ping 127.0.0.1 -n 3 > nul

REM Iniciar interfaces de usuario
for /l %%i in (1,1,3) do (
    start cmd /k "java -cp %CLASS_PATH% %UI_CLASS%"
    ping 127.0.0.1 -n 2 > nul
)
