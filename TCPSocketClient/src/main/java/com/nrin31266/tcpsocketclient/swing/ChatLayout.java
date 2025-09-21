package com.nrin31266.tcpsocketclient.swing;

import javax.swing.*;
import java.awt.*;

public class ChatLayout extends JPanel {
    public ChatLayout() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(true);
        ChatHeader chatHeader = new ChatHeader();
        ChatBody chatBody = new ChatBody();
        ChatBottom chatBottom = new ChatBottom();

        add(chatHeader, BorderLayout.NORTH);
        add(chatBody, BorderLayout.CENTER);
        add(chatBottom, BorderLayout.SOUTH);
    }
}
