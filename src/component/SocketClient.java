package src.component;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    protected static final String SERVER_ADDRESS = "localhost";
    protected static final int SERVER_PORT = 12345;
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    
    public void startClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Escucha los mensajes del servidor en un hilo separado
        startListening();
        
        // Lógica de envío de mensajes - esto puede ser sobrescrito
        
    }

    protected void startListening() {
        Thread listeningThread = new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println("Sv:" + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listeningThread.start();
    }

    
    protected void sendMessageLogic() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String message = "";
        while (!message.equalsIgnoreCase("salir")) {
            System.out.print("Escribe un mensaje para el servidor (o 'salir' para terminar): ");
            message = scanner.nextLine();
            out.println(message);
        }
        socket.close();
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static void main(String[] args) {
        try {
            new SocketClient().startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
