package src.server;

import java.io.*;
import java.net.*;

public class SocketServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Servidor iniciado...");

        // Obtener la instancia del UserManager y agregar algunos usuarios
        UserManager userManager = UserManager.getInstance();
        userManager.addUser("usr1", "pwd1");
        userManager.addUser("usr2", "pwd2");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Crea un nuevo hilo para manejar la conexión del cliente
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
