package no.kristiania.pgr209.ISeekYou;

public class Conversation {

    private int id;
    private String conversationName;

    public Conversation() {

    }
    public Conversation(String conversationName) {
        this.conversationName = conversationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }
}
