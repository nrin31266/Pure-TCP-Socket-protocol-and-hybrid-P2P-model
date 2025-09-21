package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.config.ConnectServer;
import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.service.ChatManagement;

import javax.swing.*;
import java.awt.*;

public class ChatItem extends JPanel {
    MessageDto message;

    public ChatItem(MessageDto message) {
        this.message = message;

        initComponents();
    }
    private final ChatManagement chatManagement = ChatManagement.getInstance();
    private final ConnectServer connectServer = ConnectServer.getInstance();
    private JLabel messageLabel;

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        messageLabel = new JLabel("<html>" + message.getMessage().replace("\n", "<br>") + "</html>");
        messageLabel.setOpaque(true);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        if(message.getType().equals("SYSTEM")){
            messageLabel.setBackground(new Color(240, 240, 240));
            messageLabel.setForeground(Color.DARK_GRAY);
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            messageLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerWrapper.setOpaque(false);
            centerWrapper.add(messageLabel);
            add(centerWrapper, BorderLayout.CENTER);

        } else {
            boolean isSender = connectServer.getUsername().equals(message.getSenderKey());

            if(isSender){
                // Tin nhắn của mình -> căn phải
                messageLabel.setBackground(new Color(30, 144, 255));
                messageLabel.setForeground(Color.WHITE);
                messageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(20, 120, 220), 1, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                
                JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                rightWrapper.setOpaque(false);
                rightWrapper.add(messageLabel);
                add(rightWrapper, BorderLayout.EAST);

            } else {
                // Tin nhắn của người khác -> căn trái
                messageLabel.setBackground(new Color(240, 240, 240));
                messageLabel.setForeground(Color.BLACK);
                messageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));

                // Thêm tên người gửi
                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setOpaque(false);
                
                JLabel senderLabel = new JLabel(message.getSenderKey());
                senderLabel.setFont(new Font("Arial", Font.BOLD, 11));
                senderLabel.setForeground(Color.DARK_GRAY);
                senderLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                
                leftPanel.add(senderLabel);
                leftPanel.add(messageLabel);
                
                JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
                leftWrapper.setOpaque(false);
                leftWrapper.add(leftPanel);
                add(leftWrapper, BorderLayout.WEST);
            }
        }
    }
}