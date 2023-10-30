package src.component;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;


public class UserInterface extends Application {
    
    private SocketClient client;
    private TextArea chatArea;
    private TextArea inputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // Iniciar el cliente
        client = new SocketClient() {
            @Override
            protected void startListening() {
                Thread listeningThread = new Thread(() -> {
                    try {
                        String response;
                        while ((response = in.readLine()) != null) {
                            final String finalResponse = response; // Declaración de una nueva variable
                            Platform.runLater(() -> {
                                chatArea.appendText(finalResponse + "\n");
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                listeningThread.start();
            }
        };
        client.startClient();
        
        // Configuración inicial de JavaFX
        setUpGUI(primaryStage);
    }

    private void setUpGUI(Stage primaryStage) {
        primaryStage.setTitle("Usuario");
        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputArea = new TextArea();
        inputArea.setPromptText("Escribe tu mensaje aqui...");
        inputArea.setPrefHeight(40);  // Define la altura a 40 pixels, pero puedes ajustarlo según tus necesidades
        inputArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = inputArea.getText();
                client.sendMessage(message);
                inputArea.clear();
                event.consume();  // para evitar un salto de línea no deseado después de presionar Enter
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(chatArea, inputArea);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
