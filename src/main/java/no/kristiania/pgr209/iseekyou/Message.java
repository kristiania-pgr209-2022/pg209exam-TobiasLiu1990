package no.kristiania.pgr209.iseekyou;

import java.sql.Date;
import java.time.LocalDate;

public class Message {

    private int id;
    private String sender;
    private String messageText;
    private Date messageDate;

    public Message() {
    }

    public Message(String sender, String messageText) {
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
