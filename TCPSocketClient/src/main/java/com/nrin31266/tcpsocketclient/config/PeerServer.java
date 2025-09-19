/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nrin31266.tcpsocketclient.config;

import com.nrin31266.tcpsocketclient.PeerServerListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public void setListener(PeerServerListener listener) {
        this.peerServerListener = listener;
    }

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
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getInetAddress() + ": " + line);
            }
            // nếu readLine trả về null -> client đã disconnect
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
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
