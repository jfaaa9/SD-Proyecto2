package src.server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import src.server.UserManager.User;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private String currentRoom = "global"; 
    private UserManager userManager = UserManager.getInstance();
    private String username;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            ChatManager.ChatRoom.addClient(out);

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println(InOut.horaActual() + " " + "Recibido: " + request);

                if (request.startsWith("createuser ")) {
                    handleCreateUser(request);
                } else if (request.startsWith("join ")) {
                    handleJoin(request);
                } else if (request.startsWith("leave")) {
                    currentRoom = "global"; 
                    out.println("Has dejado la sala. Ahora estás en el chat global.");
                } else if (request.startsWith("msg ")) {
                    handleMessage(request);
                } else if (request.startsWith("login ")) {
                    // Aquí verificar las credenciales del usuario.
                    handleLogin(request);

                } else {
                    out.println("Servidor: Comando no reconocido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ChatManager.ChatRoom.removeClient(out);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleJoin(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 2) {
            String roomName = parts[1];
            currentRoom = roomName;
            out.println("Has unido a la sala: " + currentRoom);
    
            // Llama al método para obtener y mostrar los mensajes de la sala de chat
            ChatManager.getAndShowMessagesByRoom(currentRoom);
        } else {
            out.println("Uso correcto: join <nombre_sala>");
        }
    }
    

    private void handleLogin(String request){
        String[] parts = request.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userManager.verifyCredentials(username, password)) {
                this.username = username; // Guardar el nombre de usuario después de verificar las credenciales
                out.println("Login successful");
                // Cambia el mensaje de bienvenida para usar el nombre de usuario
                ChatManager.ChatRoom.sendMessageToUser((InOut.horaActual() + "  " + "Bienvenido al chat! Conectado como " + "[" + this.username + "]"), out);
                // Método para obtener y mostrar los mensajes de la sala global
                ChatManager.getAndShowMessagesByRoom("global"); 
            } else {
                out.println("Login failed");
            }
        } else {
            out.println("Login failed");
        }
    }

    private void handleCreateUser(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (createUser(username, password)) {
                out.println("Usuario creado exitosamente: " + username);
            } else {
                out.println("No se pudo crear el usuario.");
            }
        } else {
            out.println("Uso correcto: createuser <username> <password>");
        }
    }

    private boolean createUser(String username, String password) {
        if (userManager.getUser(username) == null) {
            userManager.addUser(username, password);
            return true;
        } else {
            out.println("Usuario ya registrado");
            return false;
        }
    }

    // Método para manejar mensajes entrantes
    private void handleMessage(String request) {
        if (request.startsWith("msg ")) {
            String content = request.substring("msg ".length());
            Message message = new Message(this.username, this.currentRoom, content, LocalDateTime.now());
            // Llama a la función de ChatManager para manejar y guardar el mensaje
            ChatManager.handleAndSaveMessage(message);
            String formattedMsg = message.formatForDisplay(); // Utiliza el método para formatear el mensaje
            ChatManager.ChatRoom.broadcastMessage(formattedMsg);

            // Aquí puedes también llamar al método para guardar el mensaje en la base de datos si lo necesitas
            // Por ejemplo: messageDAO.insertMessage(message);
        }
    }
}
