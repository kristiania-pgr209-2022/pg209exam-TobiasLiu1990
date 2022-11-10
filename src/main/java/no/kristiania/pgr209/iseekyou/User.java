package no.kristiania.pgr209.iseekyou;

public class User {

    private int id;
    private String fullName;
    private String email;
    private UserColor color;

    public User() {
    }

    public User(int id, String fullName, String email, UserColor color) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.color = color;
    }


    public UserColor getColor() {
        return color;
    }

    public void setColor(UserColor color) {
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
