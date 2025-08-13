package com.chat_app.chat_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Start embedded server in a new thread so it doesn't block UI
        new Thread(ChatServer::startServer).start();

        Parent root = FXMLLoader.load(ChatClient.class.getResource("chat_view.fxml"));
        primaryStage.setTitle("Chat App");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
