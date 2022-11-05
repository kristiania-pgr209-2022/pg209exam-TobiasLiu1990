package no.kristiania.pgr209.ISeekYou;

import java.time.LocalDateTime;

public class Message {

    private int id;
    private String messageText;
    private LocalDateTime messageDate;

    public Message() {
    }

    public Message(int id, String messageText, LocalDateTime messageDate) {
        this.id = id;
        this.messageText = messageText;
        this.messageDate = messageDate;
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
