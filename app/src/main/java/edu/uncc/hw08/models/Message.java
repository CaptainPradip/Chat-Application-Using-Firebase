package edu.uncc.hw08.models;

import java.io.Serializable;

public class Message implements Serializable {
    public String messageBy;
    public String message;
    public String messageAt;
    public String messageId;
    public String senderId;

    public String getMessageBy() {
        return messageBy;
    }

    public void setMessageBy(String messageBy) {
        this.messageBy = messageBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageAt() {
        return messageAt;
    }

    public void setMessageAt(String messageAt) {
        this.messageAt = messageAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageBy='" + messageBy + '\'' +
                ", message='" + message + '\'' +
                ", messageAt='" + messageAt + '\'' +
                ", messageId='" + messageId + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}
