/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nrin31266.tcpsocketclient;

/**
 *
 * @author nrin31266
 */
public interface PeerServerListener {
    void onReady(String ip, int port);
}
