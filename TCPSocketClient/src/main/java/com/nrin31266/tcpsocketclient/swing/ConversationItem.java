package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.dto.UserDto;

import javax.swing.*;
import java.awt.*;

public class ConversationItem extends JPanel {
    private final String key;
    private final UserDto userDto;
    private boolean isOnline;
    private String typeConnect;

    private JLabel nameLabel;
    private JLabel statusLabel;

    public ConversationItem(String key, UserDto userDto, boolean isOnline, String typeConnect) {
        this.key = key;
        this.userDto = userDto;
        this.isOnline = isOnline;
        this.typeConnect = typeConnect;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), // true = bo tròn
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));


        // ép kích thước panel này
        setPreferredSize(new Dimension(200, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        nameLabel = new JLabel(userDto.getUsername() + " (" + key + ") "+ typeConnect);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel = new JLabel();

        add(nameLabel);
        add(statusLabel);

        updateStatusLabel();
    }

    private void updateStatusLabel() {
        String statusText = isOnline ? "Online" : "Offline";
        statusLabel.setText("Status: " + statusText);
        statusLabel.setForeground(isOnline ? Color.GREEN.darker() : Color.RED);
    }

    public String getKey() {
        return key;
    }

    public void setOnline(boolean online) {
        isOnline = online;
        updateStatusLabel();
    }
}
