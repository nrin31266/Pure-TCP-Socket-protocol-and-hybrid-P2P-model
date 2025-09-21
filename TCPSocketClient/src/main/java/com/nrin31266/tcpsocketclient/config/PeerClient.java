package com.nrin31266.tcpsocketclient.config;

import com.google.gson.Gson;
import com.nrin31266.tcpsocketclient.dto.MessageDto;
import com.nrin31266.tcpsocketclient.dto.PeerConnectDto;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import com.nrin31266.tcpsocketclient.service.ChatManagement;
import com.nrin31266.tcpsocketclient.service.ConnectionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class PeerClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final int peerServerPort;
    private final String peerServerHost;
    private final String username;
    private final ConnectionManagement connectionManagement = ConnectionManagement.getInstance();
    private final String myUsername;
    private  final ChatManagement chatManagement = ChatManagement.getInstance();

    private Gson gson = new Gson();

    public PeerClient(String host, int port , String username, String myUsername) {
        this.username = username;
        this.peerServerHost = host;
        this.peerServerPort = port;
        this.myUsername = myUsername;
    }

    public void connect() throws Exception {
        try {
            socket = new Socket(peerServerHost, peerServerPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            connectionManagement.connectUser(
                    new UserDto(username, peerServerHost, peerServerPort)
                    ,socket, out, in, "offer");
            // Gửi username lần đầu khi kết nối
            sendMessage("PEER_CONNECTION", new PeerConnectDto(myUsername));
            System.out.println("Connected to peer server " + peerServerHost + ":" + peerServerPort);
            new Thread(this::startListening).start();
        } catch (Exception e) {
            throw e;
        }
    }
    public void startListening() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    // Parse type|json
                    System.out.println(line);
                    String[] parts = line.split("\\|", 2);
                    String type = parts[0];
                    String json = parts.length > 1 ? parts[1] : "";

                    switch (type) {
                        case "CHAT":
                            System.out.println("Chat message: " + json);
                            MessageDto messageDto = gson.fromJson(json, MessageDto.class);

                            chatManagement.onMessage(username, messageDto);
                            break;


                        default:
                            System.out.println("Unknown message from peer server: " + line);
                    }
                }
                System.err.println("Dung lang nghe server");
                connectionManagement.disconnectedUser(username);
            } catch (IOException e) {
                System.out.println("Listener stopped (disconnected).");
            }
        }).start();
    }

    // Gửi message kèm username
    public void sendMessage(String type, Object content) {
        String json = gson.toJson(content);
        out.println(type + "|" + json);
    }



}
