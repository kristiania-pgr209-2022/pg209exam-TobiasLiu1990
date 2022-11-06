package no.kristiania.pgr209.ISeekYou;

import java.time.LocalDateTime;

public class Message {

    private int id;
    private String messageText;
    private LocalDateTime messageDate;

    public Message() {
    }

    public Message(String messageText) {
        this.messageText = messageText;
        this.messageDate = LocalDateTime.now();
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

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }
}
