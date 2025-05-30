package com.ultimate.mindsupport.chat;

public class UserChat {
    private int id;
    private String name;
    private String lastMessage;
    private String time;
    private String profileImageUrl;

    public UserChat(int id, String name, String lastMessage, String time, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileImageUrl = profileImageUrl;
    }

    public int getId() { return id; }

    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
    public String getProfileImageUrl() { return profileImageUrl; }
}
