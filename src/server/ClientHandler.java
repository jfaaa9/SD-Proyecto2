package src.server;

import java.io.*;
import java.net.*;

import src.server.UserManager.User;

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
                    currentRoom = request.split(" ")[1];
                    out.println("Has unido a la sala: " + currentRoom);
                } else if (request.startsWith("leave")) {
                    currentRoom = "global"; 
                    out.println("Has dejado la sala. Ahora estás en el chat global.");
                } else if (request.startsWith("msg ")) {
                    String message = request.split(" ", 2)[1];
                    // NO ES CORRECTO MANEJAR MENSAJES ACA, LA TARERA DE ESTOS IF'S ES REDIRECCIONAR
                    //String formattedMsg = InOut.horaActual() + "  " + "[" + currentRoom + "] Mensaje de [" + Thread.currentThread().getId() + "]: " + message;
                    String formattedMsg = InOut.horaActual() + "  " + "[" + currentRoom + "] Mensaje de [" + this.username + "]: " + message;
                    ChatManager.ChatRoom.broadcastMessage(formattedMsg);
                } else if (request.startsWith("login ")) {
                    // Aquí deberías verificar las credenciales del usuario.
                    handleLogin(request);
                    // Por ahora, solo imprime un mensaje de bienvenida.
                    //ChatManager.ChatRoom.sendMessageToUser((InOut.horaActual() + "  " + "Bienvenido al chat! Conectado como " + "[" + Thread.currentThread().getId()) + "]", out);
                    //ChatManager.ChatRoom.sendMessageToUser((InOut.horaActual() + "  " + "Bienvenido al chat! Conectado como " + "[" + this.username) + "]", out);
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
}
