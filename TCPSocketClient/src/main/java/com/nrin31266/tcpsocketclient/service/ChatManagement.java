package com.nrin31266.tcpsocketclient.service;

import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.listener.ChatListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManagement {
    private final Map<String, List<MessageDto>> chatMap = new HashMap<>();
    private static ChatManagement instance;
    private List<ChatListener> listeners = new ArrayList<>();
    private String currentKey = "GLOBAL_CHAT";
    private List<MessageDto> currentChat = new ArrayList<>();




    public void addChatListener(ChatListener listener) {
        listeners.add(listener);
    }
    public void removeChatListener(ChatListener listener) {
        listeners.remove(listener);
        System.out.println("Removed listener: " + listener);
    }
    public static synchronized ChatManagement getInstance() {
        if (instance == null) {
            instance = new ChatManagement();
        }
        return instance;
    }
    public void selectUser(String currentKey) {
        this.currentKey = currentKey;
        this.currentChat = chatMap.getOrDefault(currentKey, new ArrayList<>());
//        if (listener != null) {
//            listener.selectUser(currentKey);
//            listener.selectUser(currentKey, currentChat);
//        }
        for (ChatListener listener : listeners) {
            listener.selectUser(currentKey);
            listener.selectUser(currentKey, currentChat);
        }
        System.out.println("Selected User: " + currentKey);
    }
    public synchronized void onMessage(String key, MessageDto message) {
        chatMap.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
//        if (key.equals(currentKey)) {
//            currentChat.add(message);
//        }
        for (ChatListener listener : listeners) {
            listener.onMessage(key, message);
        }
    }

    public String getCurrentKey() {
        return currentKey;
    }
    public List<MessageDto> getCurrentChat() {
        return currentChat;
    }
    public void init(){
        this.chatMap.clear();
        this.currentChat = new ArrayList<>();
        this.currentKey = "GLOBAL_CHAT";
    }

}
