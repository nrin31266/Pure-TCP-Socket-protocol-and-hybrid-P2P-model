package com.nrin31266.tcpsocketclient.config;

import com.google.gson.Gson;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

public class ConnectServer {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final int serverPort = 8080;
    private final String serverHost = "127.0.0.1";
    private String username;   // thêm username
    private static ConnectServer instance;
    // volatile đảm bảo mọi thread đọc giá trị mới nhất của biến (không bị cache)
    private volatile boolean connected = false;
    private Gson gson = new Gson();
    private final PeerServer peerServer = PeerServer.getInstance();

    // private constructor (Singleton)
    private ConnectServer() {
    }

    // Lấy instance duy nhất
    public static synchronized ConnectServer getInstance() {
        if (instance == null) {
            instance = new ConnectServer();
        }
        return instance;
    }



    // Hàm connect tới server
    public void connect() throws IOException {
        if (connected) {
            System.out.println("Đã kết nối tới server rồi!");
            return;
        }
        try {
            socket = new Socket(serverHost, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
            UserDto userDto = new UserDto(this.username, peerServer.getIPAddress(), peerServer.getPort());

            // Gửi username lần đầu khi kết nối
            sendMessage("SYSTEM_CONNECTION", userDto);
            System.out.println("Connected to server " + serverHost + ":" + serverPort + " as " + username);
        } catch (IOException e) {
            throw e;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void startListening() {
        new Thread(() -> {
            try {
                String line;
                while (connected && (line = in.readLine()) != null) {
                    // Parse type|json
                    String[] parts = line.split("\\|", 2);
                    String type = parts[0];
                    String json = parts.length > 1 ? parts[1] : "";

                    switch (type) {
                        case "USER_LIST":
//                            Type listType = new com.google.gson.reflect.TypeToken<List<UserDto>>() {
//                            }.getType();
//                            List<UserDto> users = gson.fromJson(json, listType);
                            System.out.println("Current users: " + json);
                            // TODO: cập nhật UI, bảng user,...

                            Type listType = new com.google.gson.reflect.TypeToken<List<UserDto>>() {
                            }.getType();
                            List<UserDto> users = gson.fromJson(json, listType);
                            for (UserDto user : users) {
                                PeerClient peerClient = new PeerClient(user.getIPAddress(), user.getPort(), user.getUsername(), this.username);
                                new Thread(() -> {
                                    try {
                                        peerClient.connect();
                                    } catch (Exception e) {
                                        System.err.println("Failed to connect to peer " + user);
                                    }
                                }).start();
                            }
                            break;

                        case "USER_CONNECTED":
                            System.err.println("Joined: "+ json);
//                            UserDto newUser = gson.fromJson(json, UserDto.class);
//                            System.out.println("New user joined: " + newUser);
                            // TODO: thêm vào bảng UI
//                            UserDto newUser = gson.fromJson(json, UserDto.class);
//                            PeerClient peerClient = new PeerClient(newUser.getIPAddress(), newUser.getPort(), this.username);
//                            new Thread(() -> {
//                                try {
//                                    peerClient.connect();
//                                } catch (Exception e) {
//                                    System.err.println("Failed to connect to new peer " + newUser);
//                                }
//                            }).start();
                            break;
                         case "USER_DISCONNECTED":
                            System.err.println("Disconnected: "+ json);

                            break;


                        default:
                            System.out.println("Unknown message from server: " + line);
                    }
                }
                System.err.println("Dung lang nghe server");
            } catch (IOException e) {
                if (connected) { // chỉ in lỗi khi chưa logout
                    e.printStackTrace();
                } else {
                    System.out.println("Listener stopped (disconnected).");
                }
            }
        }).start();
    }

    // Gửi message kèm username
    public void sendMessage(String type, Object content) {
        String json = gson.toJson(content);
        out.println(type + "|" + json);
    }


    // Ngắt kết nối
    public void disconnect() {
        try {
//            if (connected && out != null) {
//                out.println("[SYSTEM] " + username + " has left the chat.");
//            }
            connected = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
