package edu.uncc.hw08.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable {
    public String id;
    public String senderId;
    public String receiverId;
    public Message latestChat;
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

    public Message getLatestChat() {
        return latestChat;
    }

    public void setLatestChat(Message latestChat) {
        this.latestChat = latestChat;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", latestChat=" + latestChat +
                ", messages=" + messages +
                '}';
    }
}
