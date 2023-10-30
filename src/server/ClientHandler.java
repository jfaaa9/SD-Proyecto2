package src.server;

import java.io.*;
import java.net.*;

// Esta clase representa un manejador para cada cliente conectado al servidor.
public class ClientHandler implements Runnable {

    // El socket que representa la conexión con el cliente.
    private Socket clientSocket;
    // El flujo de salida para enviar mensajes al cliente.
    private PrintWriter out;
    // La sala de chat actual a la que está unido el cliente. Por defecto, es el chat global.
    private String currentRoom = "global"; 

    // Constructor que toma el socket del cliente como argumento.
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    // Método que se ejecuta cuando se inicia el hilo del cliente.
    @Override
    public void run() {
        try {
            // Inicializa el flujo de entrada para leer mensajes del cliente.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Inicializa el flujo de salida para enviar mensajes al cliente.
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Cuando un cliente se conecta, automáticamente se une al chat global.
            ChatManager.ChatRoom.addClient(out);

            // Enviar un mensaje de bienvenida al cliente e indica su ID.
            ChatManager.ChatRoom.sendMessageToUser(("Bienvenido al chat! Conectado como " + "[" + Thread.currentThread().getId()) + "]", out);

            String request;
            // Escucha los mensajes entrantes del cliente.
            while ((request = in.readLine()) != null) {
                System.out.println("Recibido: " + request);

                // Si el cliente envía un mensaje que comienza con "JOIN", cambia su sala de chat actual.
                if (request.startsWith("join ")) {
                    currentRoom = request.split(" ")[1];
                    out.println("Has unido a la sala: " + currentRoom);
                } 
                // Si el cliente envía un mensaje que comienza con "LEAVE", lo regresa al chat global.
                else if (request.startsWith("leave")) {
                    currentRoom = "global"; 
                    out.println("Has dejado la sala. Ahora estas en el chat global.");
                } 
                // Si el cliente envía un mensaje que comienza con "MSG", envía ese mensaje a la sala actual.
                else if (request.startsWith("msg ")) {
                    String message = request.split(" ", 2)[1];
                    String formattedMsg = "[" + currentRoom + "] Mensaje de [" + Thread.currentThread().getId() + "]: " + message;
                    // Enviamos el mensaje a todos los clientes EXCEPTO al cliente actual.
                    //ChatManager.ChatRoom.broadcastMessageExcept(formattedMsg, out);
                    ChatManager.ChatRoom.broadcastMessage(formattedMsg);
                } 
                // Si el mensaje no sigue ningún formato reconocido, informa al cliente.
                else {
                    out.println("Servidor: Comando no reconocido.");
                }
            }
        } catch (IOException e) {
            // Manejo de excepciones relacionadas con operaciones de I/O.
            e.printStackTrace();
        } finally {
            // Bloque que se ejecuta después de salir del bucle, independientemente de si se produjo una excepción.
            
            // Quita al cliente de la sala de chat cuando deja el chat o se desconecta.
            ChatManager.ChatRoom.removeClient(out);
            // Intenta cerrar la conexión con el cliente.
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

