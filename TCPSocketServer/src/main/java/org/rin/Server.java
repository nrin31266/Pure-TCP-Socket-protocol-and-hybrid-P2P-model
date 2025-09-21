package org.rin;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private final int port;
    private boolean running = false;
    private static final UserManagement userManagement = UserManagement.getInstance();
    private ServerListener listener;

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    private Server(int port) {
        this.port = port;
    }

    public static synchronized Server getInstance(int port) {
        if (instance == null) {
            instance = new Server(port);
        }
        return instance;
    }

    // start server
    public void startServer() {
        if (running) {
            System.out.println("Server is already running!");
            return;
        }
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server started on port " + port);

            if (listener != null) {
                listener.onReady(port);
            }
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // xử lý client trong thread riêng
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket) {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String line;
            UserDto userDto = null;
            while ((line = in.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getInetAddress() + ": " + line);
                // Xử lý dữ liệu từ client ở đây
                String[] parts = line.split("\\|", 2);
                String type = parts[0];
                String json = parts[1];
                switch (type){
                    case "SYSTEM_CONNECTION" :{
                        UserDto newUser = new Gson().fromJson(json, UserDto.class);
                        userManagement.addUser(newUser, out);
                        userDto = newUser;

                        // --- Gửi lại danh sách user hiện có ---
                        String usersJson = new Gson().toJson(userManagement.getAllUsersNotIncluding(userDto.getUsername()).values());
                        out.println("USER_LIST|" + usersJson);
                        // --- Thông báo user mới đến các user khác ---
                        String newUserJson = new Gson().toJson(newUser);
                        for (PrintWriter writer : userManagement.getUserWritersNotIncluding(userDto.getUsername()).values()) {
                            writer.println("USER_CONNECTED|" + newUserJson);
                        }

                        break;
                    }
                    default:{
                        System.out.println("Unknown message type: " + type);
                    }
                }
            }

            // nếu readLine trả về null -> client đã disconnect
            if (userDto != null) {
                userManagement.removeUser(userDto);
                String userJson = new Gson().toJson(userDto);
                for (PrintWriter writer : userManagement.getUserWriters().values()) {
                    writer.println("USER_DISCONNECTED|" + userJson);
                }
                System.out.println("Client disconnected: " + userDto.getUsername());
            }else{
                System.out.println("Client disconnected before sending user info: " + clientSocket.getInetAddress());
            }
//            in.close();
//            out.close();

        } catch (IOException e) {
            System.out.println("Connection error with " + clientSocket.getInetAddress());
        }
    }


    // stop server
    public void stopServer() {
        try {
            running = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
