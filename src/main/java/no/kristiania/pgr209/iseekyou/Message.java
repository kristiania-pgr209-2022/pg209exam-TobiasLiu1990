package no.kristiania.pgr209.iseekyou;

import java.sql.Date;
import java.time.LocalDate;

public class Message {

    private int id;
    private int senderId;
    private int conversationId;
    private String senderName;
    private String content;
    private Date messageDate;

    public Message() {
    }

    public Message(int sender, String content) {
        this.senderId = sender;
        this.content = content;
        this.messageDate = Date.valueOf(LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }
}