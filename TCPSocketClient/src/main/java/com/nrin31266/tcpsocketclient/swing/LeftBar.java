/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nrin31266.tcpsocketclient.swing;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author nrin31266
 */
public class LeftBar extends JPanel{
    
    
    public LeftBar(){
        initComponent();
    }
    
    private void initComponent(){
        setBackground(Color.BLACK);
        add(new JLabel("Left Bar"));
        setMinimumSize(new Dimension(200,0));
    }
}
