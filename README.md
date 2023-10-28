# SD-Proyecto2

Proyecto de chat con sockets e hilos

--------------------------------------------------------------------------

## Description ID: 1

- SocketServer: servidor que escucha las conexiones entrantes en el puerto 12345. Cuando recibe una conexión, crea un nuevo hilo para manejar esa conexión y la delega a ClientHandler. Dentro de este servidor, también se ha agregado una instancia de UserManager para gestionar los usuarios. Sin embargo, no está haciendo uso activo de UserManager en la lógica actual del servidor.

- SocketClient: cliente que intenta conectarse al servidor en localhost en el puerto 12345. Una vez que se establece la conexión, solicita un mensaje del usuario, lo envía al servidor y luego espera una respuesta

- ClientHandler:  Este es el componente que maneja las conexiones individuales de los clientes en hilos separados. Una vez que un cliente se conecta, el servidor delega la gestión de esa conexión a este handler. El ClientHandler lee un mensaje del cliente, imprime ese mensaje junto con el ID del hilo que lo está manejando, y luego envía una respuesta al cliente.

- Flujo de la aplicacion:

    El servidor (SocketServer) se inicia y escucha conexiones.
    Un cliente (SocketClient) se conecta y envía un mensaje.
    El servidor acepta la conexión y crea un nuevo hilo, delegando la gestión de la conexión a ClientHandler.
    ClientHandler lee el mensaje, lo imprime, y envía una respuesta al cliente.
    El cliente recibe la respuesta y la imprime.

- Compilacion: En la carpeta raiz, "javac src/server/SocketServer.java" y "javac src/component/SocketClient.java"
- Ejecucion: java src.server.SocketServer y src.component.SocketClient

## ID: 2

- Para simular cada conexion de un cliente, se puede iniciar una cli (cmd) propia para cada usuario y que compile el SocketClient

- ChatManager: Por el momento solo agrega, elimina clientes y también permite enviar mensajes a todos los usuarios conectados en el canal principal. Se espera que esta clase maneje las salas de chat y la difusion de mensajes entre usuarios.

- CAMBIOS:
* ClientHandler: Usa metodos de ChatManager.
* SocketClient: Se añade un while para asegurar que los usuarios puedan ver los mensajes de otros usuarios en tiempo real.