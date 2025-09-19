package org.rin;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagement {
    //username@ip:port
    private final Map<String, UserDto> userMap = new HashMap<>();
    private final Map<String, PrintWriter> userWriters = new HashMap<>();
    public static UserManagement instance;

    private UserChangeListener listener;

    public void setListener(UserChangeListener listener) {
        this.listener = listener;
    }

    public static synchronized UserManagement getInstance() {
        if (instance == null) {
            instance = new UserManagement();
        }
        return instance;
    }

    public UserManagement() {

    }

    public void addUser(UserDto user, PrintWriter writer) {
        String key = generateKey(user);
        userMap.put(key, user);
        if (listener != null) {
            userWriters.put(key, writer);
            listener.onUserAdded(key ,user);
        }
    }
    public void removeUser(UserDto user) {
        String key = generateKey(user);
        userMap.remove(key);
        if (listener != null) {
            String userKey = generateKey(user);
            listener.onUserRemoved(userKey);
            userWriters.remove(userKey);
        }
    }
    public void removeUser(String key) {
        userMap.remove(key);
        if (listener != null) {
            listener.onUserRemoved(key);
            userWriters.remove(key);
        }
    }
    public UserDto getUser(String key) {
        return userMap.get(key);
    }
    public Map<String, UserDto> getAllUsers() {
        return userMap;
    }
    public Map<String, UserDto> getAllUsersNotIncluding(UserDto userDto) {
        Map<String, UserDto> result = new HashMap<>(userMap);
        result.remove(generateKey(userDto));
        return result;
    }
    public Map<String, PrintWriter> getUserWriters() {
        return userWriters;
    }
    public Map<String, PrintWriter> getUserWritersNotIncluding(UserDto userDto) {
        Map<String, PrintWriter> result = new HashMap<>(userWriters);
        result.remove(generateKey(userDto));
        return result;
    }
    public List<UserDto> getAllUsersByUsername(String username) {
        return userMap.values().stream().filter(user -> user.getUsername().equals(username)).toList();
    }


    public String generateKey(UserDto user) {
        return user.getUsername() + "@" + user.getIPAddress() + ":" + user.getPort();
    }
    public UserDto parseKey(String key) {
        String[] parts = key.split("@");
        String username = parts[0];
        String[] addressParts = parts[1].split(":");
        String ipAddress = addressParts[0];
        int port = Integer.parseInt(addressParts[1]);
        return new UserDto(username, ipAddress, port);
    }
}
