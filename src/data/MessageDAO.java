package src.data;

import src.server.Message;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Método para insertar un mensaje en la base de datos
    public void insertMessage(Message message) {
        String sql = "INSERT INTO mensajes (usuario, sala, mensaje, fecha_hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getUsername());
            pstmt.setString(2, message.getRoomName());
            pstmt.setString(3, message.getContent());
            pstmt.setTimestamp(4, Timestamp.valueOf(message.getTimestamp()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para recuperar mensajes de una sala de chat específica
    public List<Message> getMessagesByRoom(String roomName) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM mensajes WHERE sala = ? ORDER BY fecha_hora DESC";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("usuario");
                String content = rs.getString("mensaje");
                LocalDateTime timestamp = rs.getTimestamp("fecha_hora").toLocalDateTime();
                Message message = new Message(username, roomName, content, timestamp);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Otros métodos relacionados con la base de datos que puedas necesitar
}
