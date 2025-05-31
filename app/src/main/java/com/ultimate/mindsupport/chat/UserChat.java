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

    private boolean unread = false;

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChat)) return false;

        UserChat that = (UserChat) o;

        return id == that.id &&
                unread == that.unread &&
                name.equals(that.name) &&
                lastMessage.equals(that.lastMessage) &&
                time.equals(that.time) &&
                profileImageUrl.equals(that.profileImageUrl);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + lastMessage.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + profileImageUrl.hashCode();
        result = 31 * result + Boolean.hashCode(unread);
        return result;
    }


}
