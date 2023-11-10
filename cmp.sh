#!/bin/bash

CLASSPATH="./lib/postgresql-42.6.0.jar:./src"
SRC_DIR="src"
BIN_DIR="bin"

# Crear directorio BIN_DIR si no existe
if [ ! -d "$BIN_DIR" ]; then
  mkdir "$BIN_DIR"
fi

# Compilar todo en src
find "$SRC_DIR" -name "*.java" > sources.txt
javac -cp "$CLASSPATH" -d "$BIN_DIR" @sources.txt
if [ $? -ne 0 ]; then
  echo "La compilación falló."
  exit 1
fi

#rm sources.txt

echo "Compilación exitosa. Los archivos .class se encuentran en $BIN_DIR."
