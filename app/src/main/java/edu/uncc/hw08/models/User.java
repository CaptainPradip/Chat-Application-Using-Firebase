package edu.uncc.hw08.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String userId;
    boolean onlineStatus;
    String userName;
    ArrayList<String> conversations;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<String> conversations) {
        this.conversations = conversations;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", userName='" + userName + '\'' +
                ", conversations=" + conversations +
                '}';
    }
}
