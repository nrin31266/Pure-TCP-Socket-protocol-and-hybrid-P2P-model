package com.nrin31266.tcpsocketclient.swing;

import javax.swing.*;
import java.awt.*;

public class HomeContent extends JPanel {
    public HomeContent() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLUE);

        add(new JLabel("Home Content Area"));
    }
}
