package src.server;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

// La clase ChatManager maneja la difusión de mensajes a todos los clientes conectados.
public class ChatManager {

    // Una estructura de datos (conjunto) para almacenar las conexiones activas de los clientes.
    // Las Collections.synchronizedSet garantizan que este conjunto es seguro para ser usado por múltiples hilos.
    private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());

    // Método para agregar una nueva conexión al conjunto.
    public static void addClient(PrintWriter writer) {
        clientWriters.add(writer);
    }

    // Método para eliminar una conexión del conjunto.
    public static void removeClient(PrintWriter writer) {
        clientWriters.remove(writer);
    }

    // Método para enviar un mensaje a todos los clientes conectados.
    public static void broadcastMessage(String message) {
        // Dado que el conjunto es un recurso compartido por múltiples hilos, necesitamos 
        // sincronizar el acceso para evitar problemas de concurrencia.
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message); // Enviar el mensaje a cada cliente.
            }
        }
    }
}