package com.example.fadebook.ui.MainActivity.Home;

public class Users {
    private String name;
    private String email;
    private String uid;
    private String state;

    public Users() {
    }

    public Users(String name, String email, String uid, String state) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
