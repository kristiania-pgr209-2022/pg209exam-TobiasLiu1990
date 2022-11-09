package no.kristiania.pgr209.iseekyou;

public class Conversation {

    private int id;
    private String conversationTitle;

    public Conversation() {

    }
    public Conversation(String conversationName) {
        this.conversationTitle = conversationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }
}
