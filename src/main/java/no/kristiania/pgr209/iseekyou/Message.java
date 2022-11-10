package no.kristiania.pgr209.iseekyou;

import java.sql.Date;
import java.time.LocalDate;

public class Message {

    private int id;
    private int sender;
    private String senderName;
    private String messageText;
    private Date messageDate;

    public Message() {
    }

    public Message(int sender, String senderName, String messageText) {
        this.sender = sender;
        this.senderName = senderName;
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

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
