package com.ultimate.mindsupport.chat;

public class Message {
    public int senderId;
    public String content;
    public String timestamp; // you can add if your API returns it

    public Message(int senderId, String content, String timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
