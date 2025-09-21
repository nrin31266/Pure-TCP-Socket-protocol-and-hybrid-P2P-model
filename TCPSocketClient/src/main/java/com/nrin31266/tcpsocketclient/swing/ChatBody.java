package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.listener.ChatAdapter;
import com.nrin31266.tcpsocketclient.service.ChatManagement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatBody extends JPanel {
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private final ChatManagement chatManagement = ChatManagement.getInstance();

    public ChatBody() {
        setLayout(new BorderLayout());
        contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setOpaque(true);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        scrollPane.setBackground(Color.WHITE);
//        scrollPane.setOpaque(true);
        add(scrollPane, BorderLayout.CENTER);
        for (MessageDto message : chatManagement.getCurrentChat()) {
            addMessage(message);
        }
        // Thêm vài tin nhắn test SAU KHI đã add scrollPane

        chatManagement.addChatListener(new ChatAdapter() {
            @Override
            public void selectUser(String key, List<MessageDto> messages) {
                contentPanel.removeAll();
                for (MessageDto message : messages) {
                    addMessage(message);
                }
                contentPanel.revalidate();
                contentPanel.repaint();
                scrollToBottom();
            }

            @Override
            public void onMessage(String key, MessageDto message) {
                if (key.equals(chatManagement.getCurrentKey())) {
                    addMessage(message);
                }
            }
        });
    }



    private void addMessage(MessageDto message) {
        ChatItem item = new ChatItem(message);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm khoảng cách giữa các tin nhắn
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        contentPanel.add(item);

        // Scroll đến cuối - chỉ khi scrollPane đã được khởi tạo
        contentPanel.revalidate();
        contentPanel.repaint();

        // Scroll đến cuối một cách an toàn
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    // Thêm phương thức để scroll đến cuối từ bên ngoài
    public void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
}