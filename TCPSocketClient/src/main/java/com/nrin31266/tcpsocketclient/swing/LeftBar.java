package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.config.ConnectServer;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import com.nrin31266.tcpsocketclient.listener.ServerListener;
import com.nrin31266.tcpsocketclient.listener.UserChangeListener;
import com.nrin31266.tcpsocketclient.service.ChatManagement;
import com.nrin31266.tcpsocketclient.service.ConnectionManagement;

import javax.swing.*;
import java.awt.*;

public class LeftBar extends JPanel {
    ConnectionManagement connectionManagement = ConnectionManagement.getInstance();
    ConnectServer connectServer = ConnectServer.getInstance();
    ChatManagement chatManagement = ChatManagement.getInstance();
    public LeftBar() {
        initComponent();
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 0));

        // Panel chứa các item user
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //Vì bạn không setMaximumSize, nên LeftBar hoàn toàn có thể rộng hơn 200px nếu người dùng resize cửa sổ
        scrollPane.setPreferredSize(new Dimension(200, 0));

        add(scrollPane, BorderLayout.CENTER);
        ConversationItem globalChatItem = new ConversationItem("GLOBAL_CHAT", new UserDto("GLOBAL_CHAT", null, -1), true, "SERVER");
        userListPanel.add(globalChatItem);

        connectServer.setServerListener(new ServerListener() {

            @Override
            public void onDisconnected() {
                // Thay doi 1 minh trang thai server disconnected
                Component[] components = userListPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof ConversationItem) {
                        ConversationItem item = (ConversationItem) comp;
                        if (item.getKey().equals("GLOBAL_CHAT")) {
                            item.setOnline(false);
                        }}}
            }
        });

        connectionManagement.setListener(new UserChangeListener() {
            @Override
            public void onUserConnected(String key, UserDto user, String typeConnect) {
                boolean isOnline = connectionManagement.getPeer(key) != null;

                // duyệt các component có sẵn
                Component[] components = userListPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof ConversationItem) {
                        ConversationItem item = (ConversationItem) comp;
                        if (item.getKey().equals(key)) {
                            // đã có -> update thôi
                            item.setOnline(isOnline);
                            userListPanel.revalidate();
                            userListPanel.repaint();
                            return;
                        }
                    }
                }

                // nếu chưa có thì add mới -> add lên đầu luôn
                ConversationItem newItem = new ConversationItem(key, user, isOnline, typeConnect);
                userListPanel.add(newItem, 1);
                userListPanel.revalidate();
                userListPanel.repaint();
            }


            @Override
            public void onUserDisconnected(String key) {
                // Chỉ đổi giao diện, không xóa user khỏi userManagement
                Component[] components = userListPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof ConversationItem) {
                        ConversationItem item = (ConversationItem) comp;
                        if (item.getKey().equals(key)) {
                            item.setOnline(false);
                        }
                    }
                }
            }
        });
    }
}
