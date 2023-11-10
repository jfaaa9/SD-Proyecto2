package src.server;

import src.data.UserDAO;

public class UserManager {
    private UserDAO userDAO = new UserDAO(); // Instancia de UserDAO para interactuar con la base de datos

    private static UserManager instance;

    private UserManager() {}

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean addUser(String username, String password) {
        User user = new User(username, password); // Aquí deberías hashear la contraseña antes de guardarla
        return userDAO.insertUser(user);
    }

    public boolean verifyCredentials(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            return user.checkPassword(password); // Aquí deberías verificar el hash de la contraseña
        }
        return false;
    }

    public User getUser(String username) {
        return userDAO.getUserByUsername(username);
    }

    public static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password; // En la realidad, aquí debería ir el hash de la contraseña
        }

        public String getUsername() {
            return username;
        }

        // Método adicional para obtener la contraseña si es necesario
        // Debe usarse con cuidado y preferiblemente no exponerlo
        public String getPassword() {
            return password;
        }

        public boolean checkPassword(String inputPassword) {
            // Simplemente compara la contraseña directa sin hashear
            return this.password.equals(inputPassword);
        }
        
    }
}

