/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.nrin31266.tcpsocketclient;


import com.nrin31266.tcpsocketclient.config.PeerServer;
import com.nrin31266.tcpsocketclient.swing.HomePanel;
import com.nrin31266.tcpsocketclient.swing.LoginPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nrin31266
 */
public class ClientApplication extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClientApplication.class.getName());
    private JPanel currentPanel;


    /**
     * Creates new form ClientApplication
     */
    public ClientApplication() {
        initComponents();
        init();
    }

    private void init() {
        PeerServer peerServer = PeerServer.getInstance();
//        peerServer.startServer();
        peerServer.setListener((ip, port) -> {
            
            System.out.println("Peer server ready on " + ip + ":" + port);
            // có thể update GUI hoặc connect signaling server ở đây
            setTitle("Chat app P2P lai");
            showLoginPanel();
        });

        new Thread(() -> peerServer.startServer()).start();

    }

    private void setPanel(JPanel panel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = panel;
        this.add(panel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        this.pack();

    }

    // Hiển thị màn hình login
    public void showLoginPanel() {
        LoginPanel loginPanel = new LoginPanel(this);
        JPanel wrapper = new JPanel(new java.awt.GridBagLayout());
        wrapper.add(loginPanel, new java.awt.GridBagConstraints());
        wrapper.setPreferredSize(new Dimension(600, 600));
        setPanel(wrapper);
        this.setLocationRelativeTo(null);
    }

    public void showHomePanel(String username) {
        HomePanel homePanel = new HomePanel(username, this);
        setPanel(homePanel);
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // center
        getContentPane().setLayout(new BorderLayout());
        
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new ClientApplication().setVisible(true));

    }              
}
