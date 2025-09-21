package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import com.nrin31266.tcpsocketclient.listener.ChatAdapter;
import com.nrin31266.tcpsocketclient.listener.ChatListener;
import com.nrin31266.tcpsocketclient.service.ChatManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ConversationItem extends JPanel {
    private final String key;
    private final UserDto userDto;
    private boolean isOnline;
    private String typeConnect;
    private JLabel nameLabel;
    private JLabel statusLabel;
    private final ChatManagement chatManagement = ChatManagement.getInstance();
    private final Color SELECT_BACKGROUND = new Color(30, 144, 255);
    private final Color UNSELECT_BACKGROUND = Color.WHITE;
    ChatListener chatListener;

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

        nameLabel = new JLabel(key+ " | " + typeConnect);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel = new JLabel();

        add(nameLabel);
        add(statusLabel);

        updateStatusLabel();

        if (key.equals(chatManagement.getCurrentKey())) {
            setSelected(true);
        } else {
            setSelected(false);
        }

        // Tạo listener và lưu reference
        chatListener = new ChatAdapter() {
            @Override
            public void selectUser(String selectedKey) {
                if (ConversationItem.this.key.equals(selectedKey)) {
                    setSelected(true);
                } else {
                    setSelected(false);
                }
            }
        };

        // Đăng ký listener
        chatManagement.addChatListener(chatListener);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick();
            }
        });

    }

    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(SELECT_BACKGROUND);
            nameLabel.setForeground(Color.WHITE);
            statusLabel.setForeground(Color.WHITE);
        } else {
            setBackground(UNSELECT_BACKGROUND);
            nameLabel.setForeground(Color.BLACK);
            updateStatusLabel();
        }
        revalidate();
        repaint();
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

    private void onClick() {
        if (chatManagement.getCurrentKey() != null && chatManagement.getCurrentKey().equals(key)) {
            // đã chọn rồi thì thôi
            return;
        }
        chatManagement.selectUser(key);
    }


    // Hàm cleanup khi component bị xóa
    @Override
    public void removeNotify() {
        super.removeNotify();
        unregisterListener();
    }
    public void unregisterListener() {
        // Hủy đăng ký listener khi không cần thiết
        if (chatListener != null) {
            chatManagement.removeChatListener(chatListener);
            chatListener = null; // tránh hủy nhiều lần
        }
    }

}
