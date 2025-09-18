package com.nrin31266.tcpsocketclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

public class LoginPanel extends JPanel {

    private final ClientApplication app;

    private JLabel jLabel1;
    private JTextField textUN;
    private JButton btnLogin;
    private JLabel lbError;

    public LoginPanel(ClientApplication app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        //Bg
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // set GridLayout: 3 hàng, 1 cột, khoảng cách 10px
        setLayout(new GridLayout(4, 1, 10, 10));
       
        jLabel1 = new JLabel("Tai khoan");
        textUN = new JTextField();

        btnLogin = new JButton("Dang nhap");
        textUN.setPreferredSize(new Dimension(300, 40));
        btnLogin.setPreferredSize(new Dimension(200, 40));

        btnLogin.addActionListener(evt -> btnLoginActionPerformed());

        // add theo thứ tự (trên xuống dưới)
        add(jLabel1);
        add(textUN);
        
        lbError = new JLabel();
        lbError.setForeground(Color.RED);
        add(lbError);
        
        add(btnLogin);
        
        
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(400, d.height); // width fix = 400, height auto
    }

    private void btnLoginActionPerformed() {
        lbError.setText("");

        
        String userName = textUN.getText();
        if (!userName.isBlank()) {
            try {
                ConnectServer connectServer = ConnectServer.getInstance(userName);
                connectServer.connect();
            } catch (Exception e) {
                lbError.setText("Khong the ket noi toi server");
                return;
            }
            
            
            app.showHomePanel(userName);
        }else{
            lbError.setText("Vui long nhap ten tai khoan");
        }
    }
}
