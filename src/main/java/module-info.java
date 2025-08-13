module com.chat_app.chat_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.websocket.client;
    requires jakarta.websocket;
    requires org.glassfish.tyrus.server;


    opens com.chat_app.chat_app to javafx.fxml;
    exports com.chat_app.chat_app;
}