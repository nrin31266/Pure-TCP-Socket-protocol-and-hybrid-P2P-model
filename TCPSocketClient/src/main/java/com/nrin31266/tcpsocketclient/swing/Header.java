package com.nrin31266.tcpsocketclient.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Header extends JPanel {
    private JLabel label;
    private String title;
    private JButton btnLogout;
    public Header( String title) {
        this.title = title;
        initComponent();
    }
    public  void initComponent() {
        setBackground(java.awt.Color.DARK_GRAY);
        setLayout(new BorderLayout());
        // Left align
        label = new JLabel(title);
        label.setForeground(java.awt.Color.WHITE);
        label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // padding trái
        add(label, BorderLayout.WEST);

        // Button Logout bên phải
        btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        // Hiệu ứng hover
        Color normalBg = new Color(200, 50, 50);
        Color hoverBg = new Color(255, 68, 68);
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(normalBg);
            }
        });


        add(btnLogout, BorderLayout.EAST);

        // Height fix, width max
        setPreferredSize(new java.awt.Dimension(Integer.MAX_VALUE, 24));


    }
    public JButton getLogoutButton() {
        return btnLogout;
    }
}
