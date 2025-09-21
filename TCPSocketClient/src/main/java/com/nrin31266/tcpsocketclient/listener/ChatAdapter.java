package com.nrin31266.tcpsocketclient.listener;


import com.nrin31266.tcpsocketclient.dto.MessageDto;
import java.util.List;

public abstract class ChatAdapter implements ChatListener {
    @Override
    public void selectUser(String key) {}

    @Override
    public void onMessage(String key, MessageDto message) {}

    @Override
    public void selectUser(String key, List<MessageDto> messages) {}
}
