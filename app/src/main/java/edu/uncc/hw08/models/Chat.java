package edu.uncc.hw08.models;

import java.io.Serializable;

public class Chat implements Serializable {
    public String chatBy;
    public String chat;
    public String chatAt;
    public String chatId;
    public String chatById;

    public String getChatBy() {
        return chatBy;
    }

    public void setChatBy(String chatBy) {
        this.chatBy = chatBy;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getChatAt() {
        return chatAt;
    }

    public void setChatAt(String chatAt) {
        this.chatAt = chatAt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatById() {
        return chatById;
    }

    public void setChatById(String chatById) {
        this.chatById = chatById;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatBy='" + chatBy + '\'' +
                ", chat='" + chat + '\'' +
                ", chatAt='" + chatAt + '\'' +
                ", chatId='" + chatId + '\'' +
                ", chatById='" + chatById + '\'' +
                '}';
    }
}
