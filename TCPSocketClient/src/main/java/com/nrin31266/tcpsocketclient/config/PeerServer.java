/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nrin31266.tcpsocketclient.config;

import com.google.gson.Gson;
import com.nrin31266.tcpsocketclient.PeerServerListener;
import com.nrin31266.tcpsocketclient.dto.PeerConnectDto;
import com.nrin31266.tcpsocketclient.dto.UserDto;
import com.nrin31266.tcpsocketclient.service.ConnectionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author nrin31266
 */
public class PeerServer {

    private ServerSocket listener;
    private String iPAddress;
    private int port;
    private static PeerServer instance;
    private volatile boolean running = false;
    private PeerServerListener peerServerListener;
    private Gson  gson = new Gson();
    public void setListener(PeerServerListener listener) {
        this.peerServerListener = listener;
    }
    public final ConnectionManagement connectionManagement = ConnectionManagement.getInstance();
    public PeerServer() {

    }

    public  static  synchronized PeerServer getInstance() {
        if (instance == null) {
            instance = new PeerServer();
        }
        return instance;
    }

    public void startServer() {
        if (running) {
            System.out.println("Peer server is already running!");
            return;
        }
        try {
            listener = new ServerSocket(0);
            int myPort = listener.getLocalPort();
            this.port = myPort;
            String myIP = InetAddress.getLocalHost().getHostAddress();
            this.iPAddress = myIP;
            running = true;
            // báo sẵn sàng qua callback
            if (peerServerListener != null) {
                peerServerListener.onReady(myIP, myPort);
            }
            while (true) {
                Socket clientSocket = listener.accept();
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
                    case "CHAT":
                        // Xử lý tin nhắn chat
                        System.out.println("Chat message: " + json);
                        break;
                    case "PEER_CONNECTION":
                        // Xử lý kết nối peer
                        PeerConnectDto peerConnectDto = gson.fromJson(json, PeerConnectDto.class);
                        String username = peerConnectDto.getUsername();
                        String host = clientSocket.getInetAddress().getHostAddress();
                        int port = clientSocket.getPort();
                        userDto = new UserDto(username, host, port);
                        connectionManagement.connectUser(userDto, clientSocket, out, in, "answer");
                        break;
                    default:
                        System.out.println("Unknown type: " + type);
                }
            }
            // nếu readLine trả về null -> client đã disconnect
            System.out.println("Client peer disconnected: " + clientSocket.getInetAddress());
            if (userDto != null) {
                connectionManagement.disconnectedUser(connectionManagement.generateKey(userDto));
            }
        } catch (IOException e) {
            System.out.println("Connection error with " + clientSocket.getInetAddress());
        }
    }

    public int getPort() {
        return port;
    }

    public String getIPAddress() {
        return iPAddress;
    }
}
