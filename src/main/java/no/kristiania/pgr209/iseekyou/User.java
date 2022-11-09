package no.kristiania.pgr209.iseekyou;

public class User {

    private int id;
    private String fullName;
    private String eMail;

    public User() {
    }

    public User(int id, String fullName, String eMail) {
        this.id = id;
        this.fullName = fullName;
        this.eMail = eMail;
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
