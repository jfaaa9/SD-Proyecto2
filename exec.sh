#!/bin/zsh

# Define the CLASS_PATH to include both the bin directory and the PostgreSQL driver JAR.
CLASS_PATH="./bin:./lib/postgresql-42.6.0.jar"
SERVER_CLASS="src.server.SocketServer"
UI_CLASS="src.component.UserInterface"

# Configura el tamaño inicial y máximo del montón de Java (por ejemplo, 512 MB inicial y 2 GB máximo)
JAVA_OPTS="-Xms512m -Xmx2048m"

# Iniciar Servidor
osascript -e 'tell application "Terminal" to do script "cd '"$PWD"'; java '"$JAVA_OPTS"' -cp '"$CLASS_PATH"' '"$SERVER_CLASS"'"' &
sleep 2

# Iniciar interfaces de usuario
for i in {1..3}
do
   osascript -e 'tell application "Terminal" to do script "cd '"$PWD"'; java '"$JAVA_OPTS"' -cp '"$CLASS_PATH"' '"$UI_CLASS"'"' &
   sleep 1
done
