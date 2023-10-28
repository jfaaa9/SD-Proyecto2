package src.server;

import java.io.*;
import java.net.*;

// La clase ClientHandler implementa Runnable, lo que significa que puede ser ejecutada como un hilo.
public class ClientHandler implements Runnable {

    // Atributos para la conexión del cliente y para enviar datos al cliente.
    private Socket clientSocket;
    private PrintWriter out;

    // Constructor que acepta un objeto Socket, representando la conexión del cliente.
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    // Método run que se ejecuta cuando el hilo se inicia.
    @Override
    public void run() {
        try {
            // Inicializando el flujo de entrada para leer datos del cliente.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Inicializando el flujo de salida para enviar datos al cliente.
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Registrar el flujo de salida del cliente en ChatManager.
            // Esto permite enviar mensajes a este cliente desde otras partes del programa.
            ChatManager.addClient(out);

            String request;
            // Mientras haya mensajes del cliente, léelos y procesa.
            while ((request = in.readLine()) != null) {
                System.out.println("Recibido: " + request);
                
                // Formatear el mensaje para incluir el ID del hilo que lo procesa.
                String response = "Mensaje de [" + Thread.currentThread().getId() + "]: " + request;
                
                // Usar ChatManager para enviar el mensaje a todos los clientes conectados.
                ChatManager.broadcastMessage(response);
            }

        } catch (IOException e) {
            // Manejar excepciones relacionadas con la entrada/salida.
            e.printStackTrace();
        } finally {
            // Esta sección se ejecuta independientemente de si ocurrió una excepción o no.

            // Eliminar el flujo de salida del cliente de ChatManager.
            // Esto asegura que no intentamos enviar mensajes a un cliente desconectado.
            ChatManager.removeClient(out);

            // Intentar cerrar la conexión del cliente.
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

