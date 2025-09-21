package com.nrin31266.tcpsocketclient.dto;

public class PeerConnectDto {
    private String username;

    public PeerConnectDto(String username){
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

}
