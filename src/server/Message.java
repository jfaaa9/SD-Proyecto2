package src.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String username; // Nombre del usuario que envía el mensaje
    private String roomName; // Nombre de la sala de chat donde se envía el mensaje
    private String content; // Contenido del mensaje
    private LocalDateTime timestamp; // Fecha y hora en que se envió el mensaje

    public Message(String username, String roomName, String content, LocalDateTime timestamp) {
        this.username = username;
        this.roomName = roomName;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters y setters para cada campo
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String formatForDisplay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s (%s): %s", formatter.format(this.timestamp), this.username, this.roomName, this.content);
    }

    // Otros métodos que puedas necesitar, como un método para formatear el mensaje para almacenarlo en la base de datos, etc.
}


