package src.server;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();

    // Singleton pattern: para garantizar una única instancia de UserManager en toda la aplicación
    private static UserManager instance;

    private UserManager() {}

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(String username, String password) {
        User user = new User(username, password);
        users.put(username, user);
    }

    public boolean verifyCredentials(String username, String password) {
        User user = users.get(username);
        if (user != null) {
            return user.checkPassword(password);
        }
        return false;
    }

    public static class User {
        private String username;
        private String password;

        private User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public boolean checkPassword(String inputPassword) {
            return this.password.equals(inputPassword);
        }
    }
}
