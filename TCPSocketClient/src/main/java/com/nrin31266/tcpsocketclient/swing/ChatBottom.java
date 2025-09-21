package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.config.ConnectServer;
import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.dto.PeerDto;
import com.nrin31266.tcpsocketclient.service.ChatManagement;
import com.nrin31266.tcpsocketclient.service.ConnectionManagement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ChatBottom extends JPanel {
    private JTextField messageField;
    private JButton sendButton;
    private final ChatManagement chatManagement = ChatManagement.getInstance();
    private final ConnectionManagement connectionManagement = ConnectionManagement.getInstance();
    private final ConnectServer connectServer = ConnectServer.getInstance();
    public ChatBottom() {
        setLayout(new BorderLayout(5, 5)); // khoảng cách 5px giữa các thành phần
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        messageField = new JTextField();
        sendButton = new JButton("Send");

        add(messageField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);

        // Ví dụ thêm sự kiện gửi
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage()); // nhấn Enter cũng gửi
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            System.out.println("Send message: " + text); // thay bằng logic gửi message thật
            messageField.setText(""); // xóa sau khi gửi
        }
        String key = chatManagement.getCurrentKey();
        if(key != null && !key.isEmpty()){
            PeerDto peer = connectionManagement.getPeer(key);
            if(peer != null){
                MessageDto messageDto= new MessageDto(connectServer.getUsername(), key, text, "CHAT", LocalDateTime.now().toString());
                connectionManagement.sendMessageToPeer(key,messageDto);
                chatManagement.onMessage(key, messageDto);
            }
        }
    }


}
