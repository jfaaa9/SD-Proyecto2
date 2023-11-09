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

--------------------------------------------------------------------------

## ID: 2

- Para simular cada conexion de un cliente, se puede iniciar una cli (cmd) propia para cada usuario y que compile el SocketClient

- ChatManager: Por el momento solo agrega, elimina clientes y también permite enviar mensajes a todos los usuarios conectados en el canal principal. Se espera que esta clase maneje las salas de chat y la difusion de mensajes entre usuarios.

- CAMBIOS:
* ClientHandler: Usa metodos de ChatManager.
* SocketClient: Se añade un while para asegurar que los usuarios puedan ver los mensajes de otros usuarios en tiempo real.

--------------------------------------------------------------------------

## ID: 3

* Se Añade:
- UserInterface: Interfaz grafica? javafX


* Refactor
- ChatManager: nueva clase interna ChatRoom, metodos para la clase ChatRoom crear sala, eliminar sala, nuevas funciones de mensajes, a un solo usuario, a todos menos 1, inicio de creacion de salas.

- SocketClient: Ahora solo estan especificadas las funciones que realizan la conexion, para que sean conectadas a traves de la interfaz gráfica.

- ClientHandler: Adaptacion para interfaz gráfica tambien, nuevo mensajes de parte del servidor

- ChatManager, nuevas funciones de mensajes, a un solo usuario, a todos menos 1, inicio de creacion de salas.


* Misceláneo
- Para el chat global se pone msg antes del mensaje

- Ahora los usuarios no ven sus propios mensajes llegar desde el servidor (Esto era en por cli)

- Scripting (.cmd) para compilar las clases necesarias, tambien para ejecutar el servidor y los clientes y sus interfaces visuales

## ID: 4

* Misceláneo

- Scripting (.sh) , hay que dar permisos de ejecucion (chmod +x cmp.sh)

* Se Añade:

- InOut: Clase que proporcionará metodos de formato de entrada y salida de datos, para que la interfaz gráfica no tenga que preocuparse por eso, es decir será parte del servidor y una dependencia de clientHandler, no de SocketServer como se habia previsto en la arquitectura de componentes inicial.

- Interfaz para el login 

- Nueva capa data y conexiona  base de datos y crud básico.

- UserDAO: Clase que se encarga de la comunicación con la base de datos, para obtener, crear, actualizar y eliminar usuarios.

Para establecer la conexion a base de datos de usa jdbc (nativo de java), se agrego el driver de postgresql en la carpeta lib, para compilar se debe agregar al classpath el driver, compilar y ejecutar:

```bash
javac -cp "./lib/postgresql-42.6.0.jar" src/data/ConexionBD.java
java -cp "./lib/postgresql-42.6.0.jar:" src.data.ConexionBD    
```

- Conexion lograda en bdd de pruebas (prueba99), solo guarda nombre y contraseña (se actualizara dependiendo del enunciado), se hicieron cambios en UserManager, para que ahora use la base de datos, tambien ClientHandler, se agrego el comando para el admin de añadir a base de datos con 'createuser', 

SALE 2 VECES EL MENSAJE ConexionBD: "CONEXION EXITOSA CON LA BASE DE DATOS", SON 2 PORQUE CUANDO SE CREA UN USUARIO EN ClientHanlder SE LLAMA UN METODO handleCreateUser que llama createUser que hace 2 conexiones a bdd, una para ver si existe el usuario y otra para crearlo, al establecer una conexion ConexionBD avisa que se ha creado exitosamente,.