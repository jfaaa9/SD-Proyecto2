package src.server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

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

            //ChatManager.ChatRoom.addClient(out);

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
                } else if (request.startsWith("bc ")) {
                    handleMessageBroadcast(request);
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
            ChatManager.ChatRoom.removeClient(out, currentRoom);
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
            ChatManager.getAndShowMessagesByRoom(currentRoom, out);
    
            // Agrega al usuario a la sala
            ChatManager.ChatRoom.addClient(out, username, currentRoom);
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
                // Agrega al usuario a la sala global
                ChatManager.ChatRoom.addClient(out, username, currentRoom);
                // Cambia el mensaje de bienvenida para usar el nombre de usuario
                ChatManager.ChatRoom.sendMessageToUser((InOut.horaActual() + "  " + "Bienvenido al chat! Conectado como " + "[" + this.username + "]"), out);
                // Método para obtener y mostrar los mensajes de la sala global
                ChatManager.getAndShowMessagesByRoom("global", out); 
            } else {
                out.println("Login failed");
            }
        } else {
            out.println("Login failed");
        }
    }
    

    private void handleCreateUser(String request) {
        // Verificar si el usuario actual es 'admin'
        if (!"admin".equals(this.username)) {
            out.println("Acceso denegado: solo 'admin' puede crear usuarios.");
            return;
        }
    
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
    
    // Método para manejar mensajes entrantes y difundir broadcast
    private void handleMessageBroadcast(String request) {
        if (!"admin".equals(this.username)) {
            out.println("Acceso denegado: solo 'admin' puede enviar mensajes broadcast.");
            return;
        }
        
        if (request.startsWith("bc ")) {
            String content = request.substring("bc ".length());
            Message message = new Message(this.username, this.currentRoom, content, LocalDateTime.now());
            ChatManager.handleAndSaveMessage(message);
            String formattedMsg = message.formatForDisplay();
            ChatManager.ChatRoom.broadcastMessageInRoom(formattedMsg, currentRoom);
        }
    }

    // Método para manejar mensajes entrantes y difundir broadcast
    private void handleMessage(String request) {
        if (request.startsWith("msg ")) {
            String content = request.substring("msg ".length());
            Message message = new Message(this.username, this.currentRoom, content, LocalDateTime.now());
            ChatManager.handleAndSaveMessage(message);
            String formattedMsg = message.formatForDisplay();
            ChatManager.ChatRoom.broadcastMessageInRoom(formattedMsg, currentRoom);
        }
    }
}
