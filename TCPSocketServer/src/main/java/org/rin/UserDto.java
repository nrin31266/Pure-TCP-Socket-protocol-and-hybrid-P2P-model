package org.rin;

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
