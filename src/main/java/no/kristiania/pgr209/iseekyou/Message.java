package no.kristiania.pgr209.iseekyou;

import java.sql.Date;
import java.time.LocalDate;

public class Message {

    private int id;
    private int senderId;
    private String senderName;
    private String messageText;
    private Date messageDate;

    public Message() {
    }

    public Message(int sender, String messageText) {
        this.senderId = sender;
        this.messageText = messageText;
        this.messageDate = Date.valueOf(LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
