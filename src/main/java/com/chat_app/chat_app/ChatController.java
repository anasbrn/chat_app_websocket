package com.chat_app.chat_app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class ChatController {

    @FXML
    private VBox chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    private Session session;

    @FXML
    public void initialize() {
        connectToServer();
    }

    private void connectToServer() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8025/chat"));
        } catch (Exception e) {
            e.printStackTrace();
            addMessage("Failed to connect to server.", false);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        Platform.runLater(() -> addMessage(message, false));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        addMessage("Disconnected from server.", false);
    }

    @FXML
    public void onSendClicked() {
        String message = inputField.getText();
        if (message != null && !message.trim().isEmpty()) {
            addMessage(message, true);
            sendMessageToServer(message);
            inputField.clear();
        }
    }

    private void sendMessageToServer(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to send message.");
            }
        } else {
            System.out.println("Not connected to server.");
        }
    }

    private void addMessage(String message, boolean isOwnMessage) {
        HBox messageBox = new HBox();
        Text text = new Text(message);
        text.setWrappingWidth(300); // optional: max width for message bubble

        Label label = new Label("", text);
        label.setWrapText(true);
        label.setPadding(new Insets(8));
        label.setStyle(isOwnMessage
                ? "-fx-background-color: #4caf50; -fx-background-radius: 10; -fx-text-fill: white;"
                : "-fx-background-color: #e0e0e0; -fx-background-radius: 10; -fx-text-fill: black;");

        messageBox.setAlignment(isOwnMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        messageBox.getChildren().add(label);

        chatArea.getChildren().add(messageBox);
    }
}
