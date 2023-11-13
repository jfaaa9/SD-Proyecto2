package src.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Tus datos de conexión
    private static final String URL = "jdbc:postgresql://localhost:5432/prueba99";
    private static final String USER = "postgres";
    private static final String PASSWORD = "jonafalcon1";
    /* 
    private static final String URL = "jdbc:postgresql://localhost:5432/prueba99";
    private static final String USER = "postgres";
    private static final String PASSWORD = "jonafalcon1";
    */
    
    // Método para establecer la conexión
    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("No se pudo establecer la conexión a la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    // Método para cerrar la conexión
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
    /*
    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");
        Connection conexion = conectar();
        if (conexion != null) {
            cerrarConexion(conexion);
        }
    } 
     */

}
