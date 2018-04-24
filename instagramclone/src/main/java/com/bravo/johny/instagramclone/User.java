package com.bravo.johny.instagramclone;

import android.graphics.Bitmap;

public class User {

    private String name;
    private String city;
    private Bitmap userPhoto;

    public User(String name, String city, Bitmap userPhoto) {
        this.name = name;
        this.city = city;
        this.userPhoto = userPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }
}
