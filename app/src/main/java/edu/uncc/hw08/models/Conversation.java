package edu.uncc.hw08.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable {
    public String id;
    public String senderId;
    public String receiverId;
    public String latestMessage;
    public String latestMessageAt;
    public String latestMessageBy;
    public ArrayList<Message> messages;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getLatestMessageAt() {
        return latestMessageAt;
    }

    public void setLatestMessageAt(String latestMessageAt) {
        this.latestMessageAt = latestMessageAt;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getLatestMessageBy() {
        return latestMessageBy;
    }

    public void setLatestMessageBy(String latestMessageBy) {
        this.latestMessageBy = latestMessageBy;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", latestMessage='" + latestMessage + '\'' +
                ", latestMessageAt='" + latestMessageAt + '\'' +
                ", latestMessageBy='" + latestMessageBy + '\'' +
                ", messages=" + messages +
                '}';
    }
}
