package com.nrin31266.tcpsocketclient.dto;

import java.time.LocalDateTime;

public class MessageDto {
    private String senderKey;
    private String receiverKey;
    private String message;
    private String timestamp;
    // "CHAT" or "SYSTEM"
    private String type;
    public MessageDto(String senderKey, String receiverKey, String message, String type, String timestamp) {
        this.senderKey = senderKey;
        this.receiverKey = receiverKey;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
