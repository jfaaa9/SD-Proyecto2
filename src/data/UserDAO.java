package src.data;

import src.server.UserManager.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean insertUser(User user) {
        String sql = "INSERT INTO Usuarios(Nombre, Clave) VALUES(?, ?)";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Suponiendo que la contraseña ya viene hasheada
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Usuarios WHERE Nombre = ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("Nombre"), rs.getString("Clave"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Puedes añadir más métodos aquí para actualizar o eliminar usuarios, si lo necesitas
}
