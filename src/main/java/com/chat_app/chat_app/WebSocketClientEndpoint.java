package com.chat_app.chat_app;

import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class WebSocketClientEndpoint {

    private Session session;
    private MessageHandler messageHandler;

    public WebSocketClientEndpoint(String uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        if (messageHandler != null) {
            messageHandler.handleMessage(message);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Connection closed: " + reason);
    }

    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public interface MessageHandler {
        void handleMessage(String message);
    }
}

