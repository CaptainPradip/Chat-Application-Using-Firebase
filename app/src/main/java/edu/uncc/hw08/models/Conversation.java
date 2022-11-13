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
        if (latestMessage != null) {
            this.latestMessage = latestMessage;
        } else {
            this.latestMessage = "";
        }


    }

    public String getLatestMessageAt() {
        return latestMessageAt;
    }

    public void setLatestMessageAt(String latestMessageAt) {
        if (latestMessageAt != null) {
            this.latestMessageAt = latestMessageAt;
        } else {
            this.latestMessageAt = "";
        }

    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        if (messages != null) {
            this.messages = messages;
        } else {
            this.messages = new ArrayList<>();
        }

    }

    public String getLatestMessageBy() {
        return latestMessageBy;
    }

    public void setLatestMessageBy(String latestMessageBy) {
        if (latestMessageBy != null) {
            this.latestMessageBy = latestMessageBy;
        } else {
            this.latestMessageBy = "";
        }

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
