package no.kristiania.pgr209.iseekyou;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String color;

    public User() {
    }

    public User(String fullName, String email, String color) {
        this.fullName = fullName;
        this.email = email;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
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
