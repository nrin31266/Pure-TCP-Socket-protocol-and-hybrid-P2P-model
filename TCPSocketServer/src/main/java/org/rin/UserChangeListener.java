package org.rin;

public interface UserChangeListener {
    void onUserAdded(String key, UserDto user);
    void onUserRemoved(String key);
}
