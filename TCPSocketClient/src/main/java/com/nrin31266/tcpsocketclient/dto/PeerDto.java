package com.nrin31266.tcpsocketclient.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PeerDto {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    // offer or answer
    private final String type;

    public PeerDto(Socket socket, BufferedReader in, PrintWriter out, String type) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.type = type;
    }

    public BufferedReader getIn() { return in; }
    public PrintWriter getOut() { return out; }
    public Socket getSocket() { return socket; }
    public String getType() { return type; }

    // Hàm tiện ích để ngắt kết nối
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.close(); // PrintWriter có close() không throw exception
        }
        System.out.println("Peer disconnected (" + type + ")");
    }
}
