package edu.uncc.hw08.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String userId;
    boolean onlineStatus;
    String userName;
    ArrayList<MyChat> conversations;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public ArrayList<MyChat> getMyChats() {
        return conversations;
    }

    public void setMyChats(ArrayList<MyChat> conversations) {
        this.conversations = conversations;
    }

    public boolean getOnlineStatus() {
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
