package com.nrin31266.tcpsocketclient.listener;

import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.dto.UserDto;

import java.util.List;

public interface ChatListener {
    void selectUser(String key);
    void onMessage(String key, MessageDto message);
    void selectUser(String key, List<MessageDto> messages);
}
