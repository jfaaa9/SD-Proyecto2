package src.server;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String request;
            while ((request = in.readLine()) != null) {
                // Mostrar el ID del hilo junto con el mensaje recibido
                System.out.println("Hilo ID: " + Thread.currentThread().getId() + " - Recibido: " + request);
                
                if ("salir".equalsIgnoreCase(request)) {
                    out.println("Hilo ID: " + Thread.currentThread().getId() + " - Hasta luego!");
                    break;
                }
                
                String response = "Hilo ID: " + Thread.currentThread().getId() + " - Mensaje recibido: " + request;
                out.println(response);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
