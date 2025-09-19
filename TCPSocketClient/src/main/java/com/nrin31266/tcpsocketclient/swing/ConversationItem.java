package com.nrin31266.tcpsocketclient.swing;

import javax.swing.*;

public class ConversationItem extends JPanel {
    private JLabel nameLabel;
    private JLabel messageLabel;

    public ConversationItem(String name, String message) {
        nameLabel = new JLabel(name);
        messageLabel = new JLabel(message);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(nameLabel);
        add(messageLabel);
    }

}
