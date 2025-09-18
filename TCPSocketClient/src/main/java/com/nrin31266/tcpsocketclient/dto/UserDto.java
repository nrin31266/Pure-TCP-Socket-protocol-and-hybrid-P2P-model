/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nrin31266.tcpsocketclient.dto;
public class UserDto {
    private String username;
    private String iPAddress;
    private int port;

    public UserDto(String username, String iPAddress, int port) {
        this.username = username;
        this.iPAddress = iPAddress;
        this.port = port;
    }
    public UserDto(){

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }
}
