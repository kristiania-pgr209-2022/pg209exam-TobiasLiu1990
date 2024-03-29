package no.kristiania.pgr209.iseekyou;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String color;
    private int age;

    public User() {
    }

    public User(int id, String fullName, String email, int age, String color) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.color = color;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\n" +
                "Name: " + this.fullName + "\n" +
                "Mail: " + this.email + "\n" +
                "Age: " + this.age + "\n" +
                "Favorite color: " + this.color;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
