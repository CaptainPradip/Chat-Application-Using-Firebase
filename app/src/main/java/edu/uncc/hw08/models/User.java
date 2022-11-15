package edu.uncc.hw08.models;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Homework 08
 * User.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class User implements Serializable {
    String userId;
    boolean isOnline;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
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
        if (conversations != null) {
            this.conversations = conversations;
        } else {
            this.conversations = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", isOnline=" + isOnline +
                ", userName='" + userName + '\'' +
                ", conversations=" + conversations +
                '}';
    }
}
