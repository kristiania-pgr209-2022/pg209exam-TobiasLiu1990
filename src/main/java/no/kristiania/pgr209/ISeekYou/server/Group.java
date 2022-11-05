package no.kristiania.pgr209.ISeekYou.server;

import no.kristiania.pgr209.ISeekYou.Message;
import no.kristiania.pgr209.ISeekYou.User;

import java.util.List;

public class Group {

    private int id;
    private List<User> userGroup;
    private List<Message> messages;

    public Group() {
    }

    public Group(int id, List<User> userGroup, List<Message> messages) {
        this.id = id;
        this.userGroup = userGroup;
        this.messages = messages;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(List<User> userGroup) {
        this.userGroup = userGroup;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
