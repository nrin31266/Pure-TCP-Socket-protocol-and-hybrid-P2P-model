/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nrin31266.tcpsocketclient.swing;

import com.nrin31266.tcpsocketclient.ClientApplication;
import com.nrin31266.tcpsocketclient.config.ConnectServer;
import com.nrin31266.tcpsocketclient.service.ConnectionManagement;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends javax.swing.JPanel {
    private String username;
    private ConnectServer client;
    private ClientApplication app;
    private final ConnectionManagement connectionManagement = ConnectionManagement.getInstance();
    
    public HomePanel(String username, ClientApplication app) {
        this.username = username;
        this.app = app;
        initComponents();
        
        client = ConnectServer.getInstance();
        client.startListening();

    }


    private void initComponents() {


        setPreferredSize(new java.awt.Dimension(800,600));
        setLayout(new BorderLayout());
        Header header = new Header("Welcome " + username);
        add(header, BorderLayout.NORTH);
        header.getLogoutButton().addActionListener(this::btnLogoutActionPerformed);

        // Left bar
        LeftBar leftBar = new LeftBar();

        // Main content
        HomeContent homeContent = new HomeContent();
        // SplitPane để chia left - main
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftBar, homeContent);
        //leftBar ban đầu rộng 250 px, phần còn lại là homeContent
        splitPane.setDividerLocation(300); // vị trí mặc định
//        0.0 → chỉ panel bên phải (homeContent) co giãn.
//        1.0 → chỉ panel bên trái (leftBar) co giãn.
//        0.5 → cả hai co giãn đều nhau.
        splitPane.setResizeWeight(0.0);    // khi resize, ưu tiên main panel
        //Cho phép hiện nút mũi tên nhỏ trên divider → bấm để thu gọn/mở rộng panel bên trái/phải
        splitPane.setOneTouchExpandable(true); // thêm nút đóng/mở
        //Khi kéo divider, hai panel sẽ vẽ lại ngay lập tức
        splitPane.setContinuousLayout(false);

        add(splitPane, BorderLayout.CENTER);

    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        System.out.println("Logout clicked");
        this.username = null;
        client.disconnect();
        app.showLoginPanel(false);
        connectionManagement.closeAllConnections();

    }




}
