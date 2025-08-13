package com.chat_app.chat_app;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatServer {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session sender) {
        System.out.println("Message from " + sender.getId() + ": " + message);

        // Broadcast message to all clients except sender
        synchronized (sessions) {
            for (Session s : sessions) {
                if (!s.equals(sender)) {
                    try {
                        s.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    public static void startServer() {
        Server server = new Server("localhost", 8025, "/", null, ChatServer.class);
        try {
            server.start();
            System.out.println("Server started on ws://localhost:8025/chat");
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.startServer();
    }
}
