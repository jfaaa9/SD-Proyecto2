package src.server;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import src.data.MessageDAO;

public class ChatManager {

    private static final String GLOBAL_CHAT_ROOM_NAME = "Chat Global"; // Nombre para la sala de chat global
    private static Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();
    private static Map<String, Set<PrintWriter>> roomClients = new ConcurrentHashMap<>();


    static {
        // Inicializar la sala de chat global al cargar la clase
        chatRooms.put(GLOBAL_CHAT_ROOM_NAME, new ChatRoom(GLOBAL_CHAT_ROOM_NAME));
    }

    // Método para manejar y guardar un mensaje
    public static void handleAndSaveMessage(Message message) {
        // Llama a ChatRoom para difundir el mensaje
        MessageDAO messageDAO = new MessageDAO();
        // Llama a MessageDAO para guardar el mensaje en la base de datos
        messageDAO.insertMessage(message);
    }

    // Método para obtener y mostrar los mensajes de una sala de chat
    public static void getAndShowMessagesByRoom(String roomName, PrintWriter targetUser) {
        MessageDAO messageDAO = new MessageDAO();
        List<Message> messages = messageDAO.getMessagesByRoom(roomName);
    
        // Invierte el orden de los mensajes
        Collections.reverse(messages);
    
        // Formatea y muestra los mensajes en la sala de chat
        for (Message message : messages) {
            String formattedMsg = message.formatForDisplay();
            ChatRoom.sendMessageToUser(formattedMsg, targetUser);
        }
    }

    // Método para crear y agregar una nueva sala de chat. Si la sala ya existe, devuelve esa.
    public static ChatRoom createOrGetRoom(String roomName) {
        return chatRooms.computeIfAbsent(roomName, ChatRoom::new);
    }

    // Método para obtener la sala de chat global.
    public static ChatRoom getGlobalRoom() {
        return chatRooms.get(GLOBAL_CHAT_ROOM_NAME);
    }

    // Método para obtener una sala de chat por su nombre.
    public static ChatRoom getRoom(String roomName) {
        return chatRooms.get(roomName);
    }

    // Método para eliminar una sala de chat. No permite eliminar la sala global.
    public static void removeRoom(String roomName) {
        if(!GLOBAL_CHAT_ROOM_NAME.equals(roomName)) {
            chatRooms.remove(roomName);
        }
    }

    public static class ChatRoom {
        private String name;
        private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());
        private static Set<String> users = new HashSet<>();

        private ChatRoom(String name) {
            this.name = name;
        }

        public static void addClient(PrintWriter writer, String username, String roomName) {
            clientWriters.add(writer);
            users.add(username);
    
            roomClients.computeIfAbsent(roomName, k -> Collections.synchronizedSet(new HashSet<>())).add(writer);
        }
    
        public static void removeClient(PrintWriter writer, String roomName) {
            clientWriters.remove(writer);
    
            Set<PrintWriter> roomWriters = roomClients.get(roomName);
            if (roomWriters != null) {
                roomWriters.remove(writer);
            }
        }

        // Nuevo método para enviar mensajes solo a una sala específica
        public static void broadcastMessageInRoom(String message, String roomName) {
            Set<PrintWriter> roomWriters = roomClients.get(roomName);
            if (roomWriters != null) {
                synchronized (roomWriters) {
                    for (PrintWriter writer : roomWriters) {
                        writer.println(message);
                    }
                }
            }
        }

        public static void broadcastMessage(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }

        // Una variante de broadcastMessage que no envía el mensaje al PrintWriter especificado.
        public static void broadcastMessageExcept(String message, PrintWriter excludedClient) {
            synchronized (clientWriters) {
                for (PrintWriter client : clientWriters) {
                    if (client != excludedClient) {
                        client.println(message);
                    }
                }
            }
        }

        public static void sendMessageToUser(String message, PrintWriter targetUser) {
            synchronized (clientWriters) {
                if (clientWriters.contains(targetUser)) {
                    targetUser.println(message);
                }
            }
        }

    }
}


