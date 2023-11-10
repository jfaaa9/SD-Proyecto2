package src.component;

// Importaciones necesarias para la interfaz gráfica y el manejo de excepciones
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

// Clase UserInterface que extiende de Application, que es parte del framework de JavaFX para crear interfaces gráficas
public class UserInterface extends Application {

    // Campos de la clase que incluyen la conexión con el servidor, áreas de texto para la interfaz y el escenario principal
    private SocketClient client;
    private TextArea chatArea;
    private TextField inputField;
    private Stage primaryStage;

    // Método principal que lanza la aplicación JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    // Método inicial que configura y muestra la pantalla de inicio
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    // Método para mostrar la pantalla de inicio de sesión
    private void showLoginScreen() {
        // Configuración del panel de inicio de sesión con texto y campos de contraseña
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(10));
        loginPane.setHgap(8);
        loginPane.setVgap(10);

        // Campos para ingresar el usuario y contraseña
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Botón de inicio de sesión y su acción al hacer clic
        Button loginButton = new Button("Iniciar sesión");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            handleLogin(username, password);
        });
        /*
        loginButton.setOnAction(e -> {
            // Inicia el cliente si no está iniciado y se conecta al servidor
            if (client == null || client.socket.isClosed()) {
                try {
                    client = new SocketClient();
                    client.startClient();
                    setUpChatClient();  // Configura el cliente para la escucha de mensajes
                } catch (IOException ioException) {
                    // En caso de error, muestra una alerta
                    ioException.printStackTrace();
                    showAlert("Error de conexión", "No se pudo conectar al servidor.");
                    return;
                }
            }

            // Envío de las credenciales del usuario al servidor
            String username = usernameField.getText();
            String password = passwordField.getText();
            client.sendMessage("login " + username + " " + password);
            
            // Cambio a la escena del chat después de enviar las credenciales
            changeToChatScene();
        });
         */
        // Añade los componentes al panel y muestra la escena
        loginPane.add(new Label("Usuario:"), 0, 0);
        loginPane.add(usernameField, 1, 0);
        loginPane.add(new Label("Contraseña:"), 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(loginButton, 1, 2);
        GridPane.setMargin(loginButton, new Insets(20, 0, 0, 0));

        Scene scene = new Scene(loginPane, 300, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para cambiar a la escena de chat
    private void changeToChatScene() {
        setUpGUI();  // Configuración de la interfaz gráfica de usuario
    }

    // Método para configurar el cliente de chat
    private void setUpChatClient() {
        // Configura el listener de mensajes y comienza a escuchar mensajes del servidor
        client.setMessageListener(message -> Platform.runLater(() -> chatArea.appendText(message + "\n")));
        client.startListening();
    }

    // Método para configurar la interfaz gráfica de usuario del chat
    private void setUpGUI() {
        // Configuración de la ventana de chat y los campos para mostrar y enviar mensajes
        primaryStage.setTitle("Chat");

        chatArea = new TextArea();
        chatArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Escribe tu mensaje aquí...");
        inputField.setOnAction(event -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                client.sendMessage(message);
                inputField.clear();
            }
        });

        VBox layout = new VBox(10, chatArea, inputField);
        layout.setPadding(new Insets(10));

        Scene chatScene = new Scene(layout, 400, 300);
        primaryStage.setScene(chatScene);
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para manejar el inicio de sesión
    private void handleLogin(String username, String password) {
        new Thread(() -> {
            try {
                if (client == null || client.socket.isClosed()) {
                    client = new SocketClient();
                    client.startClient();
                }
                client.sendMessage("login " + username + " " + password);
                String serverResponse = client.readMessage();
                if (serverResponse != null && serverResponse.equals("Login successful")) {
                    Platform.runLater(() -> {
                        setUpChatClient(); // Establece el listener de mensajes y comienza a escuchar
                        changeToChatScene(); // Cambia a la escena del chat
                        //chatArea.appendText("Bienvenido al chat, " + username + "!\n"); // Muestra mensaje de bienvenida
                    });
                }
                 else {
                    Platform.runLater(() -> showAlert("Login Failed", "Invalid username or password. Please try again."));
                }
            } catch (IOException e) {
                Platform.runLater(() -> showAlert("Connection Error", "Could not connect to the server."));
            }
        }).start();
    }

}
