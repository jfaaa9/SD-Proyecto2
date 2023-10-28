package src.component;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    // Dirección del servidor al que se conectará el cliente
    private static final String SERVER_ADDRESS = "localhost";
    // Puerto del servidor al que se conectará el cliente
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Cliente iniciado...");

        // Intentamos establecer una conexión con el servidor
        try (
            // Socket para conectarse al servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            // Escritor para enviar mensajes al servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Lector para recibir mensajes del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Scanner para leer la entrada del usuario desde la consola
            Scanner scanner = new Scanner(System.in)
        ) {

            // Hilo dedicado a escuchar los mensajes entrantes del servidor
            Thread listeningThread = new Thread(() -> {
                try {
                    String response;
                    // Mientras haya mensajes entrantes del servidor (PERMITE MENSAJES VER LOS MENSAJES DE OTROS USUARIOS EN TIEMPO REAL)
                    while ((response = in.readLine()) != null) {
                        // Imprimir la respuesta del servidor en la consola
                        System.out.println("Respuesta del servidor: " + response);
                    }
                } catch (IOException e) {
                    // Manejar cualquier excepción que pueda surgir al escuchar los mensajes del servidor
                    e.printStackTrace();
                }
            });
            // Iniciar el hilo de escucha
            listeningThread.start();

            String message = "";
            // Mientras el usuario no escriba 'salir'
            while (!message.equalsIgnoreCase("salir")) {
                System.out.print("Escribe un mensaje para el servidor (o 'salir' para terminar): ");
                // Leer el mensaje del usuario
                message = scanner.nextLine();

                // Enviar el mensaje escrito por el usuario al servidor
                out.println(message);
            }

        } catch (IOException e) {
            // Manejar cualquier excepción que pueda surgir al intentar conectarse o comunicarse con el servidor
            e.printStackTrace();
        }
    }
}
