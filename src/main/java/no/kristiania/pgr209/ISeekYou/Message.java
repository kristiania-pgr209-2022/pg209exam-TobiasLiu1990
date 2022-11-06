package no.kristiania.pgr209.ISeekYou;

import java.sql.Date;
import java.time.LocalDate;

public class Message {

    private int id;
    private String messageText;
    private Date messageDate;

    public Message() {
    }

    public Message(String messageText) {
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
}
