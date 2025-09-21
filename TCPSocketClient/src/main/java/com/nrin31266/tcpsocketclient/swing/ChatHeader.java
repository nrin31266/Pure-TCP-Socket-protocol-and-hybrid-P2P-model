package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.listener.ChatAdapter;
import com.nrin31266.tcpsocketclient.service.ChatManagement;

import javax.swing.*;
import java.awt.*;

public class ChatHeader  extends JPanel {
    ChatAdapter chatAdapter;
    JLabel chatHeader;
    private final ChatManagement chatManagement = ChatManagement.getInstance();
    public ChatHeader() {
        setLayout(new BorderLayout());
        // padding


        chatHeader = new JLabel();
        chatHeader.setFont(new Font("Arial", Font.BOLD, 16));
        chatHeader.setForeground(Color.DARK_GRAY);
        chatHeader.setText("Chat with: " + chatManagement.getCurrentKey());
        add(chatHeader, BorderLayout.WEST);

        chatAdapter = new ChatAdapter() {
            @Override
            public void selectUser(String key) {
                chatHeader.setText("Chat with: " + key);
            }
        };
        // Border bottom
        // combine

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),  // viền dưới màu xám
                BorderFactory.createEmptyBorder(10, 10, 10, 10)                // padding bên trong
        ));

        chatManagement.addChatListener(chatAdapter);
    }
}
