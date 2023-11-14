package src.server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import src.server.ChatManager.ChatRoom;

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
                } else if (request.startsWith("bc ")) {
                    // Cambio: Obtiene la sala actual y llama a broadcastMessage
                    ChatRoom room = ChatManager.getRoom(currentRoom);
                    if (room != null) {
                        room.broadcastMessage(request);
                    } else {
                        out.println("Error: Sala de chat no encontrada.");
                    }
                } else if (request.startsWith("login ")) {
                    handleLogin(request);
                } else {
                    out.println("Servidor: Comando no reconocido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cambio: Obtiene la sala actual y llama a removeClient
            ChatRoom room = ChatManager.getRoom(currentRoom);
            if (room != null) {
                room.removeClient(out);
            }
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

            ChatManager.getAndShowMessagesByRoom(currentRoom, out);

            ChatRoom room = ChatManager.getRoom(currentRoom);
            if (room != null) {
                room.addClient(out, username);
            } else {
                out.println("Error: No se pudo unir a la sala.");
            }
        } else {
            out.println("Uso correcto: join <nombre_sala>");
        }
    }
    
    private void handleLogin(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userManager.verifyCredentials(username, password)) {
                this.username = username;
                out.println("Login successful");

                ChatRoom globalRoom = ChatManager.getGlobalRoom();
                if (globalRoom != null) {
                    globalRoom.addClient(out, username);
                } else {
                    out.println("Error: No se pudo conectar a la sala global.");
                }

                ChatManager.getAndShowMessagesByRoom("global", out);
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

    private void handleMessage(String request) {
        if (request.startsWith("msg ")) {
            String content = request.substring("msg ".length());
            Message message = new Message(this.username, this.currentRoom, content, LocalDateTime.now());

            ChatManager.handleAndSaveMessage(message);

            String formattedMsg = message.formatForDisplay(); 

            ChatRoom room = ChatManager.getRoom(this.currentRoom);
            if (room != null) {
                room.sendMessageToRoomClients(formattedMsg, out);
            } else {
                out.println("Error: Sala de chat no encontrada.");
            }

            out.println(formattedMsg);
        }
    }
}
