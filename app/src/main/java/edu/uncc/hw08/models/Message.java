package edu.uncc.hw08.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable, Comparable<Message> {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Message o) {
        String msgDate = this.getMessageAt();
        String OthermsgDate = o.getMessageAt();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

        try {
            Date msgDateType = formatter.parse(msgDate);
            Date OthermsgDateTpe = formatter.parse(OthermsgDate);
            return msgDateType.compareTo(OthermsgDateTpe);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return  0;
    }
}
