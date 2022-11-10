package no.kristiania.pgr209.iseekyou;

import java.awt.*;

public class User {

    private int id;
    private String fullName;
    private String email;
    private Color color;

    public User() {
    }

    public User(int id, String fullName, String email, Color color) {
        this.color = color;
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
