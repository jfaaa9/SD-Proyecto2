package src.component;

// Se incluyen las importaciones necesarias para la entrada/salida y redes en Java.
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

// Definición de la clase SocketClient.
public class SocketClient {
    // Constantes para la dirección y el puerto del servidor al que se conectará este cliente.
    protected static final String SERVER_ADDRESS = "localhost";
    protected static final int SERVER_PORT = 12345;
    
    // Variables de instancia para el socket, los flujos de entrada/salida y un listener de mensajes.
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    private Consumer<String> messageListener;

    // Método para iniciar el cliente, que intenta abrir una conexión al servidor y establecer flujos de entrada/salida.
    public void startClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // La lógica de inicio de sesión y otros detalles de la conexión se manejarán en otro lugar.
    }

    // Método para establecer un listener que manejará los mensajes entrantes.
    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }

    // Método para comenzar a escuchar mensajes del servidor.
    protected void startListening() {
        // Crea un hilo para escuchar mensajes, lo que permite que la interfaz de usuario permanezca receptiva.
        Thread listeningThread = new Thread(() -> {
            try {
                String response;
                // Continúa escuchando mientras haya mensajes que leer del servidor.
                while ((response = in.readLine()) != null) {
                    // Si hay un listener de mensajes configurado, acepta el mensaje para que sea manejado.
                    if (messageListener != null) {
                        messageListener.accept(response);
                    }
                }
            } catch (IOException e) {
                // Si ocurre un error al escuchar los mensajes, imprime el error.
                System.out.println("Error listening for messages: " + e.getMessage());
                // Puedes manejar reconexiones o notificar al usuario aquí.
            } finally {
                // Cierra la conexión cuando termina el ciclo de escucha.
                closeConnection();
            }
        });
        listeningThread.start(); // Inicia el hilo.
    }

    // Método para enviar mensajes al servidor.
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message); // Escribe el mensaje al flujo de salida, que lo envía al servidor.
        }
    }

    // Método para cerrar la conexión y limpiar los recursos.
    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            // Si hay un error al cerrar la conexión, imprime el error.
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }

    // Método principal que se podría usar para probar el cliente directamente.
    public static void main(String[] args) {
        try {
            SocketClient client = new SocketClient();
            client.startClient(); // Inicia el cliente.
            client.startListening(); // Comienza a escuchar mensajes.

            // Aquí se podría agregar lógica para manejar la entrada de la línea de comandos.
            // client.sendMessageLogic();
        } catch (IOException e) {
            // Si no se puede iniciar el cliente, imprime el error.
            System.out.println("Error starting the client: " + e.getMessage());
        }
    }
}
