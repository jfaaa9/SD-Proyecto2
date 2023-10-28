package src.component;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Cliente iniciado...");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String message = "";
            while (!message.equalsIgnoreCase("salir")) {
                System.out.print("Escribe un mensaje para el servidor (o 'salir' para terminar): ");
                message = scanner.nextLine();

                // Enviar el mensaje al servidor
                out.println(message);

                // Si el usuario escribe "salir", no esperamos una respuesta y salimos del bucle
                if (!message.equalsIgnoreCase("salir")) {
                    // Recibir la respuesta del servidor
                    String response = in.readLine();
                    System.out.println("Respuesta del servidor: " + response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
