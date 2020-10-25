package com.example.fadebook.pojo.modules;

public class Users {
    private String name;
    private String email;
    private String uid;
    private boolean state;
    private String imageUrl;
    private double lat;
    private double lon;

    public Users() {

    }

    public Users(String name, String email, String uid, boolean state, String imgUrl) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.state = state;
        this.imageUrl = imgUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public boolean getState() {
        return state;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
