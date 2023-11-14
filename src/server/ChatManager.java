package src.server;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import src.data.MessageDAO;

public class ChatManager {

    private static final String GLOBAL_CHAT_ROOM_NAME = "global";
    private static Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();

    static {
        chatRooms.put(GLOBAL_CHAT_ROOM_NAME, new ChatRoom(GLOBAL_CHAT_ROOM_NAME));
    }

    public static void handleAndSaveMessage(Message message) {
        MessageDAO messageDAO = new MessageDAO();
        messageDAO.insertMessage(message);
    }

    public static void getAndShowMessagesByRoom(String roomName, PrintWriter targetUser) {
        MessageDAO messageDAO = new MessageDAO();
        List<Message> messages = messageDAO.getMessagesByRoom(roomName);
        Collections.reverse(messages);
        for (Message message : messages) {
            String formattedMsg = message.formatForDisplay();
            getRoom(roomName).sendMessageToRoomClients(formattedMsg, targetUser);
        }
    }

    public static ChatRoom createOrGetRoom(String roomName) {
        return chatRooms.computeIfAbsent(roomName, ChatRoom::new);
    }

    public static ChatRoom getGlobalRoom() {
        return chatRooms.get(GLOBAL_CHAT_ROOM_NAME);
    }

    public static ChatRoom getRoom(String roomName) {
        return chatRooms.get(roomName);
    }

    public static void removeRoom(String roomName) {
        if (!GLOBAL_CHAT_ROOM_NAME.equals(roomName)) {
            chatRooms.remove(roomName);
        }
    }

    public static class ChatRoom {
        private String name;
        private Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());
        private Set<String> users = new HashSet<>();

        private ChatRoom(String name) {
            this.name = name;
        }

        public void addClient(PrintWriter writer, String username) {
            this.clientWriters.add(writer);
            this.users.add(username);
        }

        public void removeClient(PrintWriter writer) {
            this.clientWriters.remove(writer);
            this.users.removeIf(u -> u.equals(writer.toString())); // Asume que el username se puede obtener del PrintWriter
        }

        public void sendMessageToRoomClients(String message, PrintWriter targetUser) {
            synchronized (this.clientWriters) {
                for (PrintWriter writer : this.clientWriters) {
                    if (writer != targetUser) {
                        writer.println(message);
                    }
                }
            }
        }

        public static void sendMessageToUser(String message, PrintWriter targetUser) {
            targetUser.println(message);
        }

        public void broadcastMessage(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }

        public void broadcastMessageExcept(String message, PrintWriter excludedClient) {
            synchronized (clientWriters) {
                for (PrintWriter client : clientWriters) {
                    if (client != excludedClient) {
                        client.println(message);
                    }
                }
            }
        }
    }
}



