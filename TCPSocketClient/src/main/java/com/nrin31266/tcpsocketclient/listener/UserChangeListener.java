package com.nrin31266.tcpsocketclient.listener;

import com.nrin31266.tcpsocketclient.dto.UserDto;

public interface UserChangeListener {
    void onUserConnected(String key, UserDto user, String type);
    void onUserDisconnected(String key);
}
