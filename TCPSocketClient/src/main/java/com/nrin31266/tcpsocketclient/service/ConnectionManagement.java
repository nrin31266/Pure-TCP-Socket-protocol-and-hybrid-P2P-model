package com.nrin31266.tcpsocketclient.service;


import com.nrin31266.tcpsocketclient.dto.PeerDto;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import com.nrin31266.tcpsocketclient.listener.UserChangeListener;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManagement {

    public static ConnectionManagement instance;
    private final Map<String, PeerDto> mapPeer = new HashMap<>();
    private UserChangeListener listener;
    public void setListener(UserChangeListener listener) {
        this.listener = listener;
    }
//    public void setListener(UserChangeListener listener) {
//        this.listener = listener;
//    }

    public static synchronized ConnectionManagement getInstance() {
        if (instance == null) {
            instance = new ConnectionManagement();
        }
        return instance;
    }

    public ConnectionManagement() {

    }

    public void connectUser(UserDto user, Socket socket, PrintWriter writer, BufferedReader reader, String type) {
        String key = generateKey(user);
        PeerDto peerDto = new PeerDto(socket, reader, writer, type);
        mapPeer.put(key, peerDto);
        if (listener != null) {
            listener.onUserConnected(key, user, type);
        }
    }

    public void disconnectedUser(String key) {
        PeerDto peerDto = mapPeer.get(key);
        if (peerDto != null) {
            // Khong can close o day, vi khi dong socket se tu dong close
            mapPeer.remove(key);
            if (listener != null) {
                listener.onUserDisconnected(key);
            }
        }
    }

    public Map<String, PeerDto> getMapPeer() {
        return mapPeer;
    }



    public PeerDto getPeer(String key) {
        return mapPeer.get(key);
    }

    public void clearAllConnections() {
        mapPeer.clear();
    }

    public String generateKey(UserDto user) {
        return user.getUsername() + "@" + user.getIPAddress() + ":" + user.getPort();
    }

    public void closeAllConnections() {
        for (PeerDto peer : mapPeer.values()) {
            peer.disconnect();
        }
        mapPeer.clear();
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
